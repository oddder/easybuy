package com.odder.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.odder.goods.feign.SkuFeign;
import com.odder.goods.pojo.Sku;
import com.odder.search.dao.SkuEsMapper;
import com.odder.search.pojo.SkuInfo;
import com.odder.search.service.SkuService;
import entity.Result;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.uhighlight.FieldHighlighter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/25 16:15
 * @Version 1.0
 */

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SkuEsMapper skuEsMapper;
    @Autowired
    private ElasticsearchTemplate esTemplate;

    /***
     * 把数据写入es
     * @param
     * @return void
     * @date 20:26 2019/12/25
     * @author Odder
     **/
    @Override
    public void importSku() {
        //调用changgou-service-goods微服务
        Result<List<Sku>> byStatus = skuFeign.findByStatus("1");
        //将数据转成search.Sku
        List<SkuInfo> skuInfos = JSON.parseArray(JSON.toJSONString(byStatus.getData()), SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map<String, Object> specMap = JSON.parseObject(skuInfo.getSpec());
            skuInfo.setSpecMap(specMap);
        }
        skuEsMapper.saveAll(skuInfos);
    }

    /***
     * 接收一个map请求参数,对应其条件查询商品返回
     * @param searchMap
     * @return java.util.Map
     * @date 19:11 2019/12/25
     * @author Odder
     **/
    @Override
    public Map search(Map<String, String> searchMap) {
        HashMap<String, Object> map = new HashMap();
        //1、构建基本查询条件
        NativeSearchQueryBuilder builder = builderBasicQuery(searchMap);
        //2、根据查询条件-搜索商品列表
        searchList(map, builder);
        //3、一次分组查询分类、品牌与规格
        searchGroup(map, builder);
        //4、返回结果集
        return map;
    }

    /**
     * 一次分组查询分类、品牌与规格
     *
     * @param map
     * @param builder
     * @return void
     * @date 23:13 2019/12/25
     * @author Odder
     **/
    private void searchGroup(HashMap<String, Object> map, NativeSearchQueryBuilder builder) {
        //1.添加分组查询参数-builder.addAggregation(termsAggregationBuilder)
        //TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("group_spec").field("spec.keyword").size(10000);
        builder.addAggregation(AggregationBuilders.terms("group_category").field("categoryName"));
        builder.addAggregation(AggregationBuilders.terms("group_brand").field("brandName"));
        builder.addAggregation(AggregationBuilders.terms("group_spec").field("spec.keyword").size(10000));

        //2.执行搜索
        AggregatedPage<SkuInfo> page = esTemplate.queryForPage(builder.build(), SkuInfo.class);

        //3.获取所有分组查询结果集
        Aggregations aggregations = page.getAggregations();

        //4.1、提取分类结果
        getCategoryList(map, aggregations);

        //4.2、提取品牌结果
        getBrandList(map, aggregations);

        //4.3、提取分类结果
        getSpecMap(map, aggregations);
    }

    /***
     * 提取规格参数结果
     * @param map
     * @param aggregations
     * @return void
     * @date 19:44 2019/12/26
     * @author Odder
     **/
    private void getSpecMap(HashMap<String, Object> map, Aggregations aggregations) {
        StringTerms group_spec = aggregations.get("group_spec");
        ArrayList<String> specList = new ArrayList<>();
        for (StringTerms.Bucket bucket : group_spec.getBuckets()) {
            specList.add(bucket.getKeyAsString());
        }
        //用map存储spec数据
        //创建一个map用来存数据
        HashMap<String, Set<String>> stringSetHashMap = new HashMap<>();
        //遍历结果集
        for (String spec : specList) {
            //转换成map集合
            Map<String, String> jsonMap = JSON.parseObject(spec, Map.class);
            //遍历,组合结果子集
            for (String s : jsonMap.keySet()) {
                //判断是否有这个键值对
                Set<String> strings = stringSetHashMap.get(s);
                if (strings == null) {
                    //没有,存入一个set
                    strings = new HashSet<>();
                }
                //set中存入元素
                strings.add(jsonMap.get(s));
                //总和成一条结果
                stringSetHashMap.put(s, strings);
            }
        }
        map.put("specMap", stringSetHashMap);
    }

    /***
     *  提取品牌结果
     * @param map
     * @param aggregations
     * @return void
     * @date 19:40 2019/12/26
     * @author Odder
     **/
    private void getBrandList(HashMap<String, Object> map, Aggregations aggregations) {
        StringTerms brand = aggregations.get("group_brand");
        ArrayList<String> brandList = new ArrayList<>();
        for (StringTerms.Bucket bucket : brand.getBuckets()) {
            brandList.add(bucket.getKeyAsString());
        }
        map.put("brandList", brandList);
    }

    /***
     *  提取分类结果
     *
     * @param map
     * @param aggregations
     * @return void
     * @date 19:31 2019/12/26
     * @author Odder
     **/
    private void getCategoryList(HashMap<String, Object> map, Aggregations aggregations) {
        StringTerms group_category = aggregations.get("group_category");
        List<StringTerms.Bucket> buckets = group_category.getBuckets();
        ArrayList<String> categoryList = new ArrayList<>();
        for (StringTerms.Bucket bucket : buckets) {
            categoryList.add(bucket.getKeyAsString());
        }
        map.put("categoryList", categoryList);
    }


    /***
     *1、构建基本查询条件
     * @param searchMap
     * @return
     * @date 20:21 2019/12/25
     * @author Odder
     **/
    private NativeSearchQueryBuilder builderBasicQuery(Map<String, String> searchMap) {
        //1、创建查询条件构建器-builder = new NativeSearchQueryBuilder()
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //2、组装查询条件
        if (searchMap != null) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            //2.1关键字搜索-builder.withQuery(QueryBuilders.matchQuery(域名，内容))
            String keywords = searchMap.get("keywords") == null ? "" : searchMap.get("keywords");
            //如果用户传入了关键字
            if (StringUtils.isNotEmpty(keywords)) {
                boolQueryBuilder.must(QueryBuilders.matchQuery("name", keywords));
            }
            //2.2 分类查询
            String category = searchMap.get("category") == null ? "" : searchMap.get("category");
            //查询category域
            if (StringUtils.isNotEmpty(category)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName", category));
            }
            //2.3 品牌查询
            String brand = searchMap.get("brand") == null ? "" : searchMap.get("brand");
            if (StringUtils.isNotEmpty(brand)) {
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName", brand));
            }
            //2.4 规格查询
            //读取用户传入的所有参数的key
            for (String keySet : searchMap.keySet()) {
                if (keySet.startsWith("spec_")) {
                    String specField = "specMap." + keySet.substring(5) + ".keyword";
                    boolQueryBuilder.must(QueryBuilders.termQuery(specField, searchMap.get(keySet)));
                }
            }
            //2.5 价格区间查询
            String price = searchMap.get("price") == null ? "" : searchMap.get("price");
            if (StringUtils.isNotEmpty(price)) {
                //200-500
                String[] split = price.split("-");
                if (StringUtils.isNotEmpty(split[0])) {
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gte(split[0]));
                }
                if (split.length > 1) {
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(split[1]));
                }
            }
            //追加多条件匹配搜索
            queryBuilder.withQuery(boolQueryBuilder);
            //分页
            int pageNumb = 0;
            if (StringUtils.isNotEmpty(searchMap.get("pageNumb"))) {
                pageNumb = new Integer(searchMap.get("pageNumb")) - 1;
            }
            int pageSize = 5;
            PageRequest pageInfo = PageRequest.of(pageNumb, pageSize);
            queryBuilder.withPageable(pageInfo);
            //排序
            //排序方式
            if (StringUtils.isNotEmpty(searchMap.get("sortRule"))) {
                String sortRule = searchMap.get("sortRule");
                //排序域名
                if (StringUtils.isNotEmpty(searchMap.get("sortField"))) {
                    String sortField = searchMap.get("sortField");
                    queryBuilder.withSort(SortBuilders.fieldSort(sortField).order(SortOrder.valueOf(sortRule.toUpperCase())));
                }
            }
        }
        return queryBuilder;
    }

    /***
     *2、根据查询条件-搜索商品列表
     * @param map
     * @param queryBuilder
     * @return void
     * @date 20:20 2019/12/25
     * @author Odder
     **/
    private void searchList(HashMap<String, Object> map, NativeSearchQueryBuilder queryBuilder) {
        //h1.配置高亮查询信息-hField = new HighlightBuilder.Field()
        //h1.1:设置高亮域名-在构造函数中设置
        HighlightBuilder.Field hLightField = new HighlightBuilder.Field("name");
        //h1.2：设置高亮前缀-hField.preTags
        hLightField.preTags("<em style='color:red;'>");
        //h1.3：设置高亮后缀-hField.postTags
        hLightField.postTags("</em>");
        //h1.4：设置碎片大小-hField.fragmentSize
        hLightField.fragmentSize(100);
        //h1.5：追加高亮查询信息-builder.withHighlightFields()
        queryBuilder.withHighlightFields(hLightField);

        //3、获取NativeSearchQuery搜索条件对象-builder.build()
        NativeSearchQuery build = queryBuilder.build();

        //h2.高亮数据读取-AggregatedPage<SkuInfo> page = esTemplate.queryForPage(query, SkuInfo.class, new SearchResultMapper(){})
        AggregatedPage<SkuInfo> skuInfos = esTemplate.queryForPage(build, SkuInfo.class, new SearchResultMapper() {

            //h2.1实现mapResults(查询到的结果,数据列表的类型,分页选项)方法
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                //h2.2 先定义一组查询结果列表-List<T> list = new ArrayList<T>()
                ArrayList<T> skuInfos = new ArrayList<T>();
                //h2.3 遍历查询到的所有高亮数据-response.getHits().for
                for (SearchHit hit : response.getHits()) {
                    //h2.3.1 先获取当次结果的原始数据(无高亮)-hit.getSourceAsString()
                    String sourceAsString = hit.getSourceAsString();
                    //h2.3.2 把json串转换为SkuInfo对象-skuInfo = JSON.parseObject()
                    SkuInfo skuInfo = JSON.parseObject(sourceAsString, SkuInfo.class);
                    //h2.3.3 获取name域的高亮数据-nameHighlight = hit.getHighlightFields().get("name")
                    HighlightField nameHighLight = hit.getHighlightFields().get("name");

                    //h2.3.4 如果高亮数据不为空-读取高亮数据
                    if (nameHighLight != null) {
                        //h2.3.4.1 定义一个StringBuffer用于存储高亮碎片-buffer = new StringBuffer()
                        StringBuffer stringBuffer = new StringBuffer();
                        //h2.3.4.2 循环组装高亮碎片数据- nameHighlight.getFragments().for(追加数据)
                        for (Text fragment : nameHighLight.getFragments()) {
                            stringBuffer.append(fragment);
                        }
                        //h2.3.4.3 将非高亮数据替换成高亮数据-skuInfo.setName()
                        skuInfo.setName(stringBuffer.toString());
                    }

                    //h2.3.5 将替换了高亮数据的对象封装到List中-list.add((T) esItem)
                    skuInfos.add((T) skuInfo);
                }
                //h2.4 参考new AggregatedPageImpl<T>(list,pageable,response.getHits().getTotalHits())
                return new AggregatedPageImpl<T>(skuInfos, pageable, response.getHits().getTotalHits());
            }
        });
        //5、包装结果并返回
        map.put("rows", skuInfos.getContent());
        map.put("total", skuInfos.getTotalElements());
        map.put("totalPages", skuInfos.getTotalPages());
    }
}

