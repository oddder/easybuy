package com.odder.goods.service;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Goods;
import com.odder.goods.pojo.Spu;

import java.util.List;

/****
 * @Author:sz.itheima
 * @Description:Spu业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpuService {

    /**
     * 上架商品
     * @param id
     * @return void
     * @date 0:13 2019/12/22
     * @author Odder
     **/
    void audit(Long id);

    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(Long id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(Long id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();

    /**
     * 添加商品
     * @param goods
     * @return void
     * @date 21:53 2019/12/21
     * @author Odder
     **/
    void saveGoods(Goods goods);

    /**
     * 查找商品
     * @param id
     * @return void
     * @date 23:01 2019/12/21
     * @author Odder
     **/
    Goods findGoodsById(Long id);


    /**
     * 下架商品
     * @param id
     * @return void
     * @date 0:26 2019/12/22
     * @author Odder
     **/
    void pull(Long id);

    /**
     * 上架商品
     * @param id
     * @return void
     * @date 0:37 2019/12/22
     * @author Odder
     **/
    void put(Long id);

    /**
     * 批量上架
     * @param ids
     * @return int
     * @date 0:45 2019/12/22
     * @author Odder
     **/
    int putMany(Long[] ids);

    /**
     * 批量pull
     * @param ids
     * @return int
     * @date 0:45 2019/12/22
     * @author Odder
     **/
    int pullMany(Long[] ids);

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    void logicDelete(Long id);

    /**
     * 恢复数据
     * @return
     */
    void restore(Long id);
}
