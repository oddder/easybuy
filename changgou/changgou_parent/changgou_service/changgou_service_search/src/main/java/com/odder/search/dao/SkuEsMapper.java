package com.odder.search.dao;

import com.odder.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/25 16:11
 * @Version 1.0
 */
@Repository
public interface SkuEsMapper extends ElasticsearchRepository<SkuInfo,Long> {

}

