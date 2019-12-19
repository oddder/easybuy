package com.odder.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.odder.goods.dao.BrandMapper;
import com.odder.goods.pojo.Brand;
import com.odder.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/18 21:34
 * @Version 1.0
 */
@Service
public class BrandServiceImpl implements BrandService {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public Brand findById(int id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void delete(int id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Brand> findList(Brand brand) {
        //构建查询条件
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand!=null){
            //品牌名字查询
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            //品牌首字母查询
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter",brand.getLetter());
            }
        }
        //按构造的条件查询
        return brandMapper.selectByExample(example);

    }

    @Override
    public PageInfo<Brand> findPage(int page, int size) {
        //分页条件
        Page<Object> objects = PageHelper.startPage(page, size);
        //查询数据
        List<Brand> brands = brandMapper.selectAll();
        //封装并返回
        return new PageInfo<Brand>(brands);
    }

    @Override
    public PageInfo<Brand> findPageByBrand(int page, int size, Brand brand) {
        PageHelper.startPage(page, size);
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if (brand!=null){
            //品牌名字查询
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            //品牌首字母查询
            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter",brand.getLetter());
            }
        }
        List<Brand> brands = brandMapper.selectByExample(example);
        return new PageInfo<>(brands);
    }
}
