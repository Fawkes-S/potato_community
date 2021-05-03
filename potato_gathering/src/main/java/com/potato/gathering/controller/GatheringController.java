package com.potato.gathering.controller;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",gatheringService.findSearch(searchMap));
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

}