package com.odder.content.feign;

import com.odder.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/24 11:10
 * @Version 1.0
 */
@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {
    /**
     * 根据分类id查询所有广告列表
     * @param id
     * @return
     */
    @GetMapping(value = "/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable Long id);

}
