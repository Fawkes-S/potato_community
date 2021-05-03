package com.potato.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.potato.user.pojo.User;
import com.potato.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;



/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;



    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
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
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param user
     */
    @PostMapping
    public Result add(@RequestBody User user  ){
        userService.add(user);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param user
     */
    @PutMapping("/{id}")
    public Result update(@RequestBody User user, @PathVariable String id ){
        user.setId(id);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }



    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        Map<String,String> map = new HashMap<>();
        map.put("mobile",user.getMobile());
        List<User> list = userService.findSearch(map);
        if(null == list || list.size() ==0 ){
            userService.add(user);
            return new Result(true,StatusCode.OK,"注册成功！");
        }
        return new Result(true,StatusCode.OK,"此手机号已经注册过，请重新注册！");

    }

    /**
     * 登录
     * @param map
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map){
        User user = userService.login(map);
        if(user == null){
            return new Result(false,StatusCode.ERROR,"用户名或者密码错误");
        }
        String token = jwtUtil.createJWT(user.getId(),user.getMobile(),"user");
        return new Result(true,StatusCode.OK,"登录成功",token);
    }

    /**
     * 删除 必须有admin角色才能删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id){
        userService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
