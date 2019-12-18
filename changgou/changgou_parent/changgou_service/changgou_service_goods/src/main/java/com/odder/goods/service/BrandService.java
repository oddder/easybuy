package com.odder.goods.service;

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
     * @Param []
     * @return java.util.List<com.odder.goods.pojo.Brand>
     * @Date 21:33 2019/12/18
     * @Author Odder
     **/
    List<Brand> findAll();

   /**
    * 根据id查找品牌
    * @param id brand id
    * @return com.odder.goods.pojo.Brand
    * @date 22:20 2019/12/18
    * @author Odder
    **/
    Brand findById(int id);

    /**
     * add a brand
     * @param brand brand
     * @return void
     * @date 23:20 2019/12/18
     * @author Odder
     **/
    void add(Brand brand);

    /**
     *  修改brand
     * @param brand
     * @return void
     * @date 23:30 2019/12/18
     * @author Odder
     **/
    void update(Brand brand);

    /**
     * delete brand
     * @param id
     * @return void
     * @date 23:36 2019/12/18
     * @author Odder
     **/
    void delete(int id);




}
