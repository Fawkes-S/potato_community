package com.potato.advice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.potato.advice.client.UserClient;
import com.potato.advice.pojo.Advice;
import com.potato.advice.service.AdviceService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.potato.advice.pojo.Advice;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/advice")
public class AdviceController {

    @Autowired
    private AdviceService adviceService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserClient userClient;

    /**
     * 查询所有
     */
    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",adviceService.findAll());
    }

    /**
     * 根据ID查询
     * @param adviceid ID
     * @return
     */
    @GetMapping("/{adviceid}")
    public Result findById(@PathVariable String adviceid){
        return new Result(true,StatusCode.OK,"查询成功",adviceService.findById(adviceid));
    }

    /**
     * 分页+多条件查询
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Advice> pageList = adviceService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"分页查询成功",  new PageResult<Advice>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 动态条件查询
     * @param searchMap
     * @param page 页码
     * @param size 页大小
     * @return
     */
    @PostMapping("/search/back/{page}/{size}")
    public Result backSearch( @RequestBody Map searchMap, @PathVariable int page, @PathVariable int size){
        Page<Advice> pageList = adviceService.backSearch(searchMap, page, size);
        return new Result(true,StatusCode.OK,"后台页面查询成功", new PageResult<Advice>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * △Feign
     * 添加
     * 必须有user用户登录才能吐糟建议
     * @param map
     */
    @PostMapping
    public Result add(@RequestBody Map<String,String> map){
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        String userid = jwtUtil.parseJWT(token).getId();
        //Feign调用potato_user的findById()
        Result result = userClient.findById(userid);
        JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(result));
        String nickname = jo.getJSONObject("data").getString("nickname");
        Advice advice =new Advice();
        advice.setParentid(map.get("parentid"));
        advice.setContent(map.get("content"));
        advice.setNickname(nickname);
        advice.setUserId(userid);
        if(token==null||"".equals(token)){
            return new Result(false,StatusCode.ACCESS_ERROR,"未登录用户，请先登录！");
        }
        adviceService.add(advice);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改
     * @param advice
     */
    @PutMapping("/{adviceid}")
    public Result update(@RequestBody Advice advice,@PathVariable String adviceid){
        //设置id
        advice.set_id(adviceid);
        adviceService.update(advice);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param adviceid
     */
    @DeleteMapping("/{adviceid}")
    public Result deleteById(@PathVariable String adviceid){
        adviceService.deleteById(adviceid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID(parentid)查询吐槽数据
     */
    @GetMapping("/comment/{parentId}/{page}/{size}")
    public Result findByParentId(@PathVariable String parentId,@PathVariable int page,@PathVariable int size){
        Page<Advice> commentlist = adviceService.findByParentId(parentId,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(commentlist.getTotalElements(),commentlist.getContent()));
    }


    /**
     * 建议投诉有用（点赞）,并且不能重复点赞
     * @param adviceid
     */
    @PutMapping("/thumbup/{adviceid}")
    public Result thumbUp(@PathVariable String adviceid){
        String header = request.getHeader("Authorization");
        String token = header.substring(7);
        if(token==null||"".equals(token)){
            return new Result(false,StatusCode.ACCESS_ERROR,"未登录用户，请先登录！");
        }
        String userid = jwtUtil.parseJWT(token).getId();
        System.out.println("userid:"+userid);
        //从redis查询用户是否已经点赞过
        String flag = (String) redisTemplate.opsForValue().get("advice_"+userid+"_"+adviceid);
        if(flag != null){
            return new Result(false,StatusCode.REPEAT_ERROR,"该用户已经点过赞");
        }

        adviceService.thumbUp(adviceid);

        //数据存到redis
        redisTemplate.opsForValue().set("advice_"+userid+"_"+adviceid, "1",120, TimeUnit.SECONDS);
        return new Result(true,StatusCode.OK,"点赞成功");
    }



}
