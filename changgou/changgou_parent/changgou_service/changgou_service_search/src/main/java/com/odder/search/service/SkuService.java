package com.odder.search.service;

import java.util.Map;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/25 16:14
 * @Version 1.0
 */
public interface SkuService{

    /***
     * 导入SKU数据
     */
    void importSku();

    /***
     * 搜索商品
     * @param searchMap 搜索条件
     * @return 结果集
     */
    Map search(Map<String, String> searchMap);


}
