package com.odder.goods.dao;
import com.odder.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:sz.itheima
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {
    /***
     * 连表查询
     * @param cid
     * @return java.util.List<com.odder.goods.pojo.Brand>
     * @date 18:16 2019/12/21
     * @author Odder
     **/
    @Select("SELECT tb.* FROM tb_category_brand tcb,tb_brand tb WHERE" +
            " tcb.category_id=#{cid} AND  tb.id=tcb.brand_id ")
    List<Brand> selectByCategory(int cid);
}
