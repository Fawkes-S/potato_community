package com.potato.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.potato.user.pojo.Admin;
import com.potato.user.service.AdminService;

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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",adminService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",adminService.findById(id));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",adminService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param admin
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Admin admin){
        adminService.add(admin);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param admin
     */
    @PutMapping("/{id}")
    public Result update(@RequestBody Admin admin, @PathVariable String id ){
        admin.setId(id);
        adminService.update(admin);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id ){
        adminService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result loginOut(){
        return new Result(true,StatusCode.OK,"登出成功");
    }

    /**
     * 获取信息
     * @return
     */
    @GetMapping("/info")
    public Result getInfo(String token) {
        HashMap<Object, Object> map = new HashMap<>();
        map.put("roles", "admin");
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("name", "Super Admin");
        return new Result(true,StatusCode.OK,"成功得到信息",map);
    }
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String,String> map){

        Admin admin = adminService.login(map);

        if(admin!=null){
            //登录成功
            //生成Token,并给前端签发Token
            String token = jwtUtil.createJWT(admin.getId(), admin.getUsername(), "admin");
            //把token返回给前端
            Map<String,String> result = new HashMap<>();
            result.put("name",admin.getUsername());
            result.put("role","admin");
            result.put("token",token);

            return new Result(true,StatusCode.OK,"admin登录成功",result);
        }else {
            return new Result(false,StatusCode.ERROR,"用户名或者密码错误!");
        }
    }
}