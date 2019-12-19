package com.odder.goods.service;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Album;
import entity.PageResult;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 0:03
 * @Version 1.0
 */
public interface AlbumService {
    /**
     * find all
     *
     * @param
     * @return java.util.List<com.odder.goods.pojo.Album>
     * @date 0:05 2019/12/20
     * @author Odder
     **/
    List<Album> findAll();

    /**
     * ad d
     *
     * @param album
     * @return void
     * @date 0:06 2019/12/20
     * @author Odder
     **/
    void add(Album album);

    /**
     * find by rule
     *
     * @param album
     * @return java.util.List<com.odder.goods.pojo.Album>
     * @date 0:07 2019/12/20
     * @author Odder
     **/
    List<Album> findByRule(Album album);

    /**
     * find page
     *
     * @param page
     * @param size
     * @return entity.PageResult<com.odder.goods.pojo.Album>
     * @date 0:09 2019/12/20
     * @author Odder
     **/
    PageInfo<Album> findPage(int page, int size);

    /**
     * find
     * @param page
     * @param size
     * @param album
     * @return
     */
    PageInfo<Album> findPageAndRule(int page, int size , Album album);

    /**
     * find by id
     * @param id
     * @return entity.PageResult<com.odder.goods.pojo.Album>
     * @date 0:12 2019/12/20
     * @author Odder
     **/
    Album findById(int id);

    /***
     * update
     * @param album
     * @return void
     * @date 0:13 2019/12/20
     * @author Odder
     **/
    void updateById(Album album);

    /***
     * delete
     * @param id
     * @return void
     * @date 0:13 2019/12/20
     * @author Odder
     **/
    void deleteById(int id);
}
