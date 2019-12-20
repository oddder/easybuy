package com.odder.goods.controller;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Template;
import com.odder.goods.service.TemplateService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 15:49
 * @Version 1.0
 */
@RestController
@RequestMapping("template")
@CrossOrigin
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping
    public Result findAll(){
        List<Template> all = templateService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",all);
    }

    @PostMapping
    public Result<PageInfo<Template>> add(@RequestBody Template template){
        templateService.add(template);
        return new Result<>(true, StatusCode.OK,"添加成功");
    }

    @PostMapping("search")
    public Result<PageInfo<Template>> findByRole(@RequestBody Template template){
        List<Template> byRule = templateService.findByRule(template);
        return new Result<>(true, StatusCode.OK,"查询成功",byRule);
    }

    @GetMapping("search/{page}/{size}")
    public Result findPage(@PathVariable int page , @PathVariable int size){
        PageInfo<Template> page1 = templateService.findPage(page, size);
        return new Result(true, StatusCode.OK,"查询成功",page1);
    }

    @PostMapping("search/{page}/{size}")
    public Result findPageAndRule(@PathVariable int page , @PathVariable int size ,@RequestBody Template template){
        PageInfo<Template> page1 = templateService.findPageAndRule(template,page, size);
        return new Result(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping("search/{id}")
    public Result findById(@PathVariable int id){
        Template byId = templateService.findById( id);
        return new Result(true, StatusCode.OK,"查询成功",byId);
    }

    @PutMapping("{id}")
    public Result findById(@PathVariable int id,@RequestBody Template template){
        template.setId(id);
        templateService.updateById(template);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @DeleteMapping("{id}")
    public Result deleteById(@PathVariable int id){
        templateService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}
