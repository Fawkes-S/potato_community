package com.potato.gathering.controller;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.potato.gathering.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.potato.gathering.pojo.Gathering;
import com.potato.gathering.service.GatheringService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/gathering")
public class GatheringController {

    @Autowired
    private GatheringService gatheringService;

    @Autowired
    private UserClient userClient;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",gatheringService.findAll());
    }

    /**
     * 根据ID查询
     * @param gatheringid ID
     * @return
     */
    @GetMapping("/{gatheringid}")
    public Result findById(@PathVariable String gatheringid){
        return new Result(true,StatusCode.OK,"查询成功",gatheringService.findById(gatheringid));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @PostMapping("/search/{page}/{size}")
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Gathering> pageList = gatheringService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Gathering>(pageList.getTotalElements(), pageList.getContent()) );
    }

//    /**
//     * 分页+多条件查询(个人中心)
//     * @param searchMap 查询条件封装
//     * @param page 页码
//     * @param size 页大小
//     * @return 分页结果
//     */
//    @PostMapping("/searchM)
//    public Result findSearchManger(@RequestBody Map searchMap){
//        Page<Gathering> pageList = gatheringService.findSearchManger(searchMap, page, size);
//        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Gathering>(pageList.getTotalElements(), pageList.getContent()) );
//    }

    /**
     * 根据条件查询(个人中心)
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public Result findSearch( @RequestBody Map searchMap){
        Result result = userClient.findById(searchMap.get("id").toString());
        JSONObject jo = JSONObject.parseObject(JSONObject.toJSONString(result));
        String gatheringid = jo.getJSONObject("data").getString("gatheringid");
        searchMap.put("id",gatheringid);
        return new Result(true,StatusCode.OK,"查询成功",gatheringService.findSearchM(searchMap));
    }

    /**
     * 增加
     * @param gathering
     */
    @PostMapping
    public Result add(@RequestBody Gathering gathering  ){
        gatheringService.add(gathering);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param gathering
     */
    @PutMapping("/{gatheringid}")
    public Result update(@RequestBody Gathering gathering, @PathVariable String gatheringid ){
        gathering.setId(gatheringid);
        gatheringService.update(gathering);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param gatheringid
     */
    @DeleteMapping("/{gatheringid}")
    public Result delete(@PathVariable String gatheringid ){
        gatheringService.deleteById(gatheringid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * Feign调用User模块
     * @return
     */
    @GetMapping("/user")
    public Result findAllUsers(){
        Result result = userClient.findAll();
        return result;
    }

    /**
     * △
     * Feign User报名活动 添加活动id
     * @return
     */
    @PostMapping("/addGathering")
    public Result addGathering(@RequestBody Map<String,String> map){
        Result result = userClient.addGathering(map);
        return result;
    }
}