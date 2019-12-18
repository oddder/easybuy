package com.odder.goods.controller;

import com.odder.goods.pojo.Brand;
import com.odder.goods.service.BrandService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/18 21:39
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/brand")
@CrossOrigin
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> allBrand = brandService.findAll();
        return new Result<List<Brand>>(true, StatusCode.OK, "查询品牌信息成功", allBrand);
    }

    @GetMapping("{id}")
    public Result<Brand> findById(@PathVariable int id) {
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK, "查询品牌信息成功", brand);
    }

    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"保存品牌成功");
    }

    @PutMapping("{id}")
    public Result update(@RequestBody Brand brand ,@PathVariable int id){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改品牌成功");
    }

    @DeleteMapping("{id}")
    public Result delete(@PathVariable int id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
