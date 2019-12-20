package com.odder.goods.service;

import com.github.pagehelper.PageInfo;
import com.odder.goods.pojo.Template;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 14:18
 * @Version 1.0
 */
public interface TemplateService {

    /**
     * 查询所有
     * @param null
     * @return list
     * @date 14:19 2019/12/20
     * @author Odder
     **/
    List<Template> findAll();

    /**
     * 添加
     * @param template
     */
    void add(Template template);

    /**
     * 条件查询
     * @param template
     * @return java.util.List<com.odder.goods.pojo.Template>
     * @date 14:21 2019/12/20
     * @author Odder
     **/
    List<Template> findByRule(Template template);

    /**
     *分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Template> findPage(int page , int size );

    /**
     * 根据条件查询并分页
     * @param template
     * @param page
     * @param size
     * @return
     */
    PageInfo<Template> findPageAndRule(Template template,int page,int size);

    /**
     * 根据id查找
     * @param id
     * @return
     */
    Template findById(int id);

    /***
     * 根据id修改
     * @param template
     * @return void
     * @date 14:26 2019/12/20
     * @author Odder
     **/
    void updateById(Template template);

    /**
     * 根据id删除
     * @param id
     * @return void
     * @date 14:27 2019/12/20
     * @author Odder
     **/
    void deleteById(int id);
}
