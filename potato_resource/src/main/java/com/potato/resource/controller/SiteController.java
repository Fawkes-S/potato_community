package com.potato.resource.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.potato.resource.pojo.Site;
import com.potato.resource.service.SiteService;

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
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteService siteService;


    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",siteService.findAll());
    }

    /**
     * 根据ID查询
     * @param siteId ID
     * @return
     */
    @GetMapping("/{siteId}")
    public Result findById(@PathVariable String siteId){
        return new Result(true,StatusCode.OK,"查询成功",siteService.findById(siteId));
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",siteService.findSearch(searchMap));
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
        Page<Site> pageList = siteService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Site>(pageList.getTotalElements(), pageList.getContent()) );
    }


    /**
     * 增加
     * @param site
     */
    @PostMapping
    public Result add(@RequestBody Site site  ){
        siteService.add(site);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param site
     */
    @PutMapping("/{siteId}")
    public Result update(@RequestBody Site site, @PathVariable String siteId ){
        site.setId(siteId);
        siteService.update(site);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param siteId
     */
    @DeleteMapping("/{siteId}")
    public Result delete(@PathVariable String siteId ){
        siteService.deleteById(siteId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 推荐场地
     */
    @GetMapping("/search/recommend")
    public Result recommend(){
        List<Site> list = siteService.recommend();
        return new Result(true,StatusCode.OK,"推荐场地查询成功",list);
    }

    /**
     * 最新场地
     */
    @GetMapping("/search/newList")
    public Result newList(){
        List<Site> list = siteService.newList();
        return new Result(true,StatusCode.OK,"最新场地查询成功",list);
    }

}