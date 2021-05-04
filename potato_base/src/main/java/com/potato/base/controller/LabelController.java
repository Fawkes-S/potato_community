package com.potato.base.controller;

import com.potato.base.pojo.Label;
import com.potato.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin            // 跨域;解决前端跨域请求问题
@Transactional
@RequestMapping("/label")
public class LabelController {
    @Autowired
    private LabelService labelServices;

    @Autowired
    private HttpServletRequest request;

    @GetMapping
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", labelServices.findAll());
    }

    @GetMapping("/{labelId}")
    public Result findById(@PathVariable("labelId") String id) {
        return new Result(true, StatusCode.OK, "查询成功", labelServices.findById(id));
    }

    @PostMapping
    public Result save(@RequestBody Label label) {
        labelServices.save(label);
        return new Result(true, StatusCode.OK, "添加成功");
    }

    @PutMapping("/{labelId}")
    public Result update(@PathVariable("labelId") String labelId, @RequestBody Label label) {
        label.setId(labelId);
        labelServices.update(label);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @DeleteMapping("/{labelId}")
    public Result deleteById(@PathVariable("labelId") String id) {
        labelServices.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping("/search")
    public Result findSearch(@RequestBody Label label) {                        //@RequestBody可转map,object
        List<Label> list = labelServices.findSearch(label);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }

    @PostMapping("/search/{page}/{size}")
    public Result pageQuery(@RequestBody Label label, @PathVariable("page") int currentPage, @PathVariable("size") int pageSize) {
        Page<Label> pageData = labelServices.pageQuery(label, currentPage, pageSize);
        return new Result(true, StatusCode.OK, "查询成功",
                new PageResult<>(pageData.getTotalElements(), pageData.getContent())); //getTotalElements()总记录数
    }
}