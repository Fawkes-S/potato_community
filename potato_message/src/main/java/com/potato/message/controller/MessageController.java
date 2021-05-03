package com.potato.message.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.potato.message.pojo.Message;
import com.potato.message.service.MessageService;

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
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        return new Result(true,StatusCode.OK,"查询成功",messageService.findAll());
    }

    /**
     * 根据ID查询
     * @param messageid ID
     * @return
     */
    @GetMapping("/{messageid}")
    public Result findById(@PathVariable String messageid){
        return new Result(true,StatusCode.OK,"查询成功",messageService.findById(messageid));
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
        Page<Message> pageList = messageService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<Message>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @PostMapping("/search")
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",messageService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param message
     */
    @PostMapping
    public Result add(@RequestBody Message message  ){
        messageService.add(message);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改
     * @param message
     */
    @PutMapping("/{messageid}")
    public Result update(@RequestBody Message message, @PathVariable String messageid ){
        message.setId(messageid);
        messageService.update(message);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param messageid
     */
    @DeleteMapping("/{messageid}")
    public Result delete(@PathVariable String messageid ){
        messageService.deleteById(messageid);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 审核
     */
    @PutMapping("/examine/{messageid}")
    public Result examine(@PathVariable String messageid){
        messageService.examine(messageid);
        return new Result(true,StatusCode.OK,"审核成功");
    }

    /**
     * 点赞
     */
    @PutMapping("/thumbUp/{messageid}")
    public Result thumbUp(@PathVariable String messageid){
        messageService.thumbUp(messageid);
        return new Result(true,StatusCode.OK,"点赞成功");
    }

}