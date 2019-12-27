package com.odder.search.controller;

import com.odder.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/25 16:35
 * @Version 1.0
 */
@RequestMapping(value = "search")
@CrossOrigin
@RestController
public class SkuController {

    @Autowired
    private SkuService skuService;

    /***
     * 把sku信息传入es中
     * @param
     * @return entity.Result
     * @date 16:40 2019/12/25
     * @author Odder
     **/
    @RequestMapping("import")
    public Result importSku(){
        skuService.importSku();
        return new Result(true, StatusCode.OK,"导入数据到索引库中成功！");
    }

    /**
     * 搜索商品
     * @return
     */
    @PostMapping
    public Map search(@RequestBody(required = false) Map searchMap){
        return  skuService.search(searchMap);
    }

}
