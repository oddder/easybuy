package com.odder.goods.service;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Brand;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/18 21:32
 * @Version 1.0
 */
public interface BrandService {
    /**
     * 查找所有品牌
     *
     * @return java.util.List<com.odder.goods.pojo.Brand>
     * @Param []
     * @Date 21:33 2019/12/18
     * @Author Odder
     **/
    List<Brand> findAll();

    /**
     * 根据id查找品牌
     *
     * @param id brand id
     * @return com.odder.goods.pojo.Brand
     * @date 22:20 2019/12/18
     * @author Odder
     **/
    Brand findById(int id);

    /**
     * add a brand
     *
     * @param brand brand
     * @return void
     * @date 23:20 2019/12/18
     * @author Odder
     **/
    void add(Brand brand);

    /**
     * 修改brand
     *
     * @param brand
     * @return void
     * @date 23:30 2019/12/18
     * @author Odder
     **/
    void update(Brand brand);

    /**
     * delete brand
     *
     * @param id
     * @return void
     * @date 23:36 2019/12/18
     * @author Odder
     **/
    void delete(int id);


    /**
     * 根据查询条件查询
     *
     * @param brand
     * @return void
     * @date 14:59 2019/12/19
     * @author Odder
     **/
    List<Brand> findList(Brand brand);

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @return java.util.List<com.odder.goods.pojo.Brand>
     * @date 16:39 2019/12/19
     * @author Odder
     **/
    PageInfo<Brand> findPage(int page , int size);

    /**
     * 查询条件并分页
     * @param page
     * @param size
     * @param brand
     * @return com.github.pagehelper.PageInfo<com.odder.goods.pojo.Brand>
     * @date 17:15 2019/12/19
     * @author Odder
     **/
    PageInfo<Brand> findPageByBrand(int page, int size, Brand brand);
}
