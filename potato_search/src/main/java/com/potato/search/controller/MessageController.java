package com.potato.search.controller;

import com.potato.search.pojo.Message;
import com.potato.search.service.MessageService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/search")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * 添加
     */
    @PostMapping
    public Result add(@RequestBody Message message){
        messageService.add(message);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 搜索资讯
     */
    @PostMapping("/message/{page}/{size}")
    public Result findSearch(@RequestBody Map<String,String> keywords, @PathVariable int page, @PathVariable int size){

        Page<Message> list = messageService.findSearch(keywords.get("keywords"),page,size);

        if(CollectionUtils.isEmpty(list.getContent())){
            return new Result(false,StatusCode.ERROR,"查询失败，库中无搜索内容");
        }
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(list.getTotalElements(),list.getContent()));
    }
}
