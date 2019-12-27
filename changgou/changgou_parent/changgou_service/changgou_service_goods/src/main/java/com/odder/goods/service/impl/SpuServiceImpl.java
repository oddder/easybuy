package com.odder.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.odder.goods.dao.CategoryMapper;
import com.odder.goods.pojo.*;
import com.odder.goods.service.SpuService;
import com.odder.goods.dao.BrandMapper;
import com.odder.goods.dao.SkuMapper;
import com.odder.goods.dao.SpuMapper;
import entity.IdWorker;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/****
 * @Author:sz.itheima
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl implements SpuService {


    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SpuMapper spuMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private SkuMapper skuMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private IdWorker idWorker;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 批量上架
     * @param ids
     * @return int
     * @date 0:45 2019/12/22
     * @author Odder
     **/
    public int putMany(Long[] ids){
        Spu spu = new Spu();
        spu.setIsMarketable("1");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //加入id
        criteria.andIn("id", Arrays.asList(ids));
        //上架商品必须未删除 以通过审核
        criteria.andEqualTo("isMarketable", "0"); //下架商品才能上架
        criteria.andEqualTo("status", "1"); //审核通过的
        criteria.andEqualTo("isDelete", "0"); //非删除的
        return spuMapper.updateByExampleSelective(spu,example);
    }

    /**
     * 批量下架
     * @param ids
     * @return int
     * @date 10:11 2019/12/22
     * @author Odder
     **/
    @Override
    public int pullMany(Long[] ids) {
        Spu spu = new Spu();
        spu.setIsMarketable("1");

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",Arrays.asList(ids));
        //为上架状态
        criteria.andEqualTo("isMarketable","1");
        //未删除状态
        criteria.andEqualTo("isDelete","0");
        return spuMapper.updateByExampleSelective(spu,example);
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @Override
    public void logicDelete(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("1".equals(spu.getIsMarketable())) {
            throw new RuntimeException("上架商品,不能删除!");
        }
        //修改
        spu.setIsDelete("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 恢复数据
     * @return
     */
    @Override
    public void restore(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("0".equals(spu.getIsDelete())) {
            throw new RuntimeException("商品未删除!!!");
        }
        spu.setIsDelete("0");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 审核商品
     * @param id
     * @return void
     * @date 0:43 2019/12/22
     * @author Odder
     **/
    public void audit(Long id){
        //查询商品
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断商品是否已经删除
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw new RuntimeException("此商品已被删除，无法审核！");
        }
        //实现上架和审核
        spu.setStatus("1"); //审核通过
        spu.setIsMarketable("1"); //上架
        spuMapper.updateByPrimaryKeySelective(spu);

    }

    /***
     * 查找商品
     * @param id
     * @return com.odder.goods.pojo.Goods
     * @date 23:04 2019/12/21
     * @author Odder
     **/
    @Override
    public Goods findGoodsById(Long id) {
        Goods goods = new Goods();
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu==null) {
            throw new RuntimeException();
        }
        goods.setSpu(spu);
        Long spuId = spu.getId();
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        goods.setSkuList(skuList);
        return goods;
    }

    /**
     * 下架商品
     * @param id
     * @return void
     * @date 0:27 2019/12/22
     * @author Odder
     **/
    @Override
    public void pull(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        //判断是否删除
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("此商品已被删除，无法下架！");
        }
        //修改上架状态
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 上架商品
     * @param id
     * @return void
     * @date 0:38 2019/12/22
     * @author Odder
     **/
    @Override
    public void put(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("1".equals(spu.getIsDelete())) {
            throw new RuntimeException("此商品已被删除，无法上架！");
        }
        if (!"1".equals(spu.getIsEnableSpec())) {
            throw new RuntimeException("此商品未通过审核，无法上架！");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKeySelective(spu);
    }


    /**
     * 添加商品
     * @param goods
     * @return void
     * @date 21:54 2019/12/21
     * @author Odder
     **/
    @Override
    public void saveGoods(Goods goods) {
        Spu spu = goods.getSpu();
        if (spu.getId()==null) {
            spu.setId(idWorker.nextId());
        }
        spuMapper.insertSelective(spu);

        Date date = new Date();
        Integer category3Id = spu.getCategory3Id();
        Category category = categoryMapper.selectByPrimaryKey(category3Id);
        String categoryName = category.getName();
        List<Sku> skuList = goods.getSkuList();
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        for (Sku sku : skuList) {
            String name = spu.getName();
            Map<String,String> nameMap = JSON.parseObject(sku.getSpec(), Map.class);
            Set<Map.Entry<String, String>> entries = nameMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                name += " " + entry.getValue();
            }
            sku.setName(name);
            sku.setId(idWorker.nextId());
            sku.setSpuId(spu.getId());
            sku.setUpdateTime(date);
            sku.setCreateTime(date);
            sku.setCategoryId(category3Id);
            sku.setCategoryName(categoryName);
            sku.setBrandName(brand.getName());
            skuMapper.insertSelective(sku);
        }


    }

    /**
     * Spu条件+分页查询
     * @param spu 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Spu> findPage(Spu spu, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(spu);
        //执行搜索
        return new PageInfo<Spu>(spuMapper.selectByExample(example));
    }

    /**
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Spu> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Spu>(spuMapper.selectAll());
    }

    /**
     * Spu条件查询
     * @param spu
     * @return
     */
    @Override
    public List<Spu> findList(Spu spu){
        //构建查询条件
        Example example = createExample(spu);
        //根据构建的条件查询数据
        return spuMapper.selectByExample(example);
    }


    /**
     * Spu构建查询对象
     * @param spu
     * @return
     */
    public Example createExample(Spu spu){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(spu!=null){
            // 主键
            if(!StringUtils.isEmpty(spu.getId())){
                    criteria.andEqualTo("id",spu.getId());
            }
            // 货号
            if(!StringUtils.isEmpty(spu.getSn())){
                    criteria.andEqualTo("sn",spu.getSn());
            }
            // SPU名
            if(!StringUtils.isEmpty(spu.getName())){
                    criteria.andLike("name","%"+spu.getName()+"%");
            }
            // 副标题
            if(!StringUtils.isEmpty(spu.getCaption())){
                    criteria.andEqualTo("caption",spu.getCaption());
            }
            // 品牌ID
            if(!StringUtils.isEmpty(spu.getBrandId())){
                    criteria.andEqualTo("brandId",spu.getBrandId());
            }
            // 一级分类
            if(!StringUtils.isEmpty(spu.getCategory1Id())){
                    criteria.andEqualTo("category1Id",spu.getCategory1Id());
            }
            // 二级分类
            if(!StringUtils.isEmpty(spu.getCategory2Id())){
                    criteria.andEqualTo("category2Id",spu.getCategory2Id());
            }
            // 三级分类
            if(!StringUtils.isEmpty(spu.getCategory3Id())){
                    criteria.andEqualTo("category3Id",spu.getCategory3Id());
            }
            // 模板ID
            if(!StringUtils.isEmpty(spu.getTemplateId())){
                    criteria.andEqualTo("templateId",spu.getTemplateId());
            }
            // 运费模板id
            if(!StringUtils.isEmpty(spu.getFreightId())){
                    criteria.andEqualTo("freightId",spu.getFreightId());
            }
            // 图片
            if(!StringUtils.isEmpty(spu.getImage())){
                    criteria.andEqualTo("image",spu.getImage());
            }
            // 图片列表
            if(!StringUtils.isEmpty(spu.getImages())){
                    criteria.andEqualTo("images",spu.getImages());
            }
            // 售后服务
            if(!StringUtils.isEmpty(spu.getSaleService())){
                    criteria.andEqualTo("saleService",spu.getSaleService());
            }
            // 介绍
            if(!StringUtils.isEmpty(spu.getIntroduction())){
                    criteria.andEqualTo("introduction",spu.getIntroduction());
            }
            // 规格列表
            if(!StringUtils.isEmpty(spu.getSpecItems())){
                    criteria.andEqualTo("specItems",spu.getSpecItems());
            }
            // 参数列表
            if(!StringUtils.isEmpty(spu.getParaItems())){
                    criteria.andEqualTo("paraItems",spu.getParaItems());
            }
            // 销量
            if(!StringUtils.isEmpty(spu.getSaleNum())){
                    criteria.andEqualTo("saleNum",spu.getSaleNum());
            }
            // 评论数
            if(!StringUtils.isEmpty(spu.getCommentNum())){
                    criteria.andEqualTo("commentNum",spu.getCommentNum());
            }
            // 是否上架,0已下架，1已上架
            if(!StringUtils.isEmpty(spu.getIsMarketable())){
                    criteria.andEqualTo("isMarketable",spu.getIsMarketable());
            }
            // 是否启用规格
            if(!StringUtils.isEmpty(spu.getIsEnableSpec())){
                    criteria.andEqualTo("isEnableSpec",spu.getIsEnableSpec());
            }
            // 是否删除,0:未删除，1：已删除
            if(!StringUtils.isEmpty(spu.getIsDelete())){
                    criteria.andEqualTo("isDelete",spu.getIsDelete());
            }
            // 审核状态，0：未审核，1：已审核，2：审核不通过
            if(!StringUtils.isEmpty(spu.getStatus())){
                    criteria.andEqualTo("status",spu.getStatus());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(Long id){
        //判断必须先逻辑删除
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if ("0".equals(spu.getIsDelete())) {
            throw new RuntimeException("该商品不能删除!!!");
        }
        spuMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Spu
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 增加Spu
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
    @Override
    public Spu findById(Long id){
        return  spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Spu全部数据
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

}
