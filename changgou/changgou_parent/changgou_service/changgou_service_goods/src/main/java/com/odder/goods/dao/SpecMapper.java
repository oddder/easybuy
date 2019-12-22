package com.odder.goods.dao;
import com.odder.goods.pojo.Spec;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:sz.itheima
 * @Description:Spec的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SpecMapper extends Mapper<Spec> {

    @Select("select s.* from tb_spec s , tb_category c where c.id=#{cid} and c.template_id = s.template_id ")
    List<Spec> selectByCategoryId(int cid);
}
