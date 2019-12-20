package com.odder.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.odder.goods.dao.TemplateMapper;
import com.odder.goods.pojo.Template;
import com.odder.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/20 14:28
 * @Version 1.0
 */


@Service
public class TemplateServiceImpl implements TemplateService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private TemplateMapper templateMapper;

    @Override
    public List<Template> findAll() {
        return templateMapper.selectAll();
    }

    @Override
    public void add(Template template) {
        templateMapper.insert(template);
    }

    @Override
    public List<Template> findByRule(Template template) {
        Example example = getExample(template);
        templateMapper.selectByExample(example);
        return null;
    }

    private Example getExample(Template template) {
        Example example = new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if (template!=null) {
            if (template.getName()!=null&& !"".equals(template.getName())) {
                criteria.andLike("name","%"+template.getName()+"%");
            }
            if (template.getId()!=null&& !"".equals(template.getId())) {
                criteria.andEqualTo("id",template.getId());
            }
            if (template.getParaNum()!=null&& !"".equals(template.getParaNum())) {
                criteria.andLike("paraNum","%"+template.getParaNum()+"%");
            }
            if (template.getSpecNum()!=null&& !"".equals(template.getSpecNum())) {
                criteria.andLike("specNum","%"+template.getSpecNum()+"%");
            }
        }
        return example;
    }

    @Override
    public PageInfo<Template> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(templateMapper.selectAll());
    }

    @Override
    public PageInfo<Template> findPageAndRule(Template template, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = getExample(template);
        return new PageInfo<>(templateMapper.selectByExample(example));
    }

    @Override
    public Template findById(int id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateById(Template template) {
        templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void deleteById(int id) {
        templateMapper.deleteByPrimaryKey(id);
    }
}
