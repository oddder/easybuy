package com.odder.goods.controller;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Album;
import com.odder.goods.service.AlbumService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 0:39
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public Result<List<Album>> findAll(){
        List<Album> all = albumService.findAll();
        return new Result<>(true, StatusCode.OK,"查询成功",all);
    }

    @PostMapping
    public Result add(@RequestBody Album album){
        albumService.add(album);
        return new Result<>(true, StatusCode.OK,"add成功");
    }

    @PostMapping("search")
    public Result<List<Album>> findByRule(@RequestBody Album album){
        List<Album> all = albumService.findByRule(album);
        return new Result<>(true, StatusCode.OK,"查询成功",all);
    }

    @GetMapping("search/{page}/{size}")
    public Result<PageInfo<Album>> findPage(@PathVariable int page ,@PathVariable int size){
        PageInfo<Album> page1 = albumService.findPage(page, size);
        return new Result<>(true, StatusCode.OK,"查询成功",page1);
    }

    @PostMapping("search/{page}/{size}")
    public Result<PageInfo<Album>> findPageAndRule(@RequestBody Album album ,@PathVariable int page ,@PathVariable int size){
        PageInfo<Album> page1 = albumService.findPageAndRule(page,size,album);
        return new Result<>(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable int id ){
        return new Result<>(true, StatusCode.OK,"查询成功",albumService.findById(id));
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable int id ,@RequestBody Album album){
        album.setId((long)id);
        albumService.updateById(album);
        return new Result<>(true, StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id ){
        albumService.deleteById(id);
        return new Result<>(true, StatusCode.OK,"删除成功");
    }

}


