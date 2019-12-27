package com.odder.content.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.Descriptors;
import com.odder.content.feign.ContentFeign;
import com.odder.content.pojo.Content;
import com.xpand.starter.canal.annotation.*;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/23 21:44
 * @Version 1.0
 */
@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 广告监听
     *
     * @param eventType
     * @param rowData
     * @return void
     * @date 17:26 2019/12/25
     * @author Odder
     **/
    @ListenPoint(destination = "example", schema = "changgou_content", table = "tb_content", eventType =
            {CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE, CanalEntry.EventType.DELETE})
    public void onEventContent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("---------广告监听--------");
        //先获取categoryId
        String categoryId = "";
        //新增操作，取修改后的值
        if (eventType == CanalEntry.EventType.INSERT) {
            //tb_content表中categoryId在第二列，所以下标为1
            categoryId = rowData.getAfterColumns(1).getValue();
            //修改操作，取修改后的值
        } else if (eventType == CanalEntry.EventType.UPDATE) {
            //tb_content表中categoryId在第二列，所以下标为1
            categoryId = rowData.getAfterColumns(1).getValue();
            //如果修改过程中，把categoryId修改了，要把修改前的广告分类缓存也更新了
            String categoryIdBefore = rowData.getBeforeColumns(1).getValue();
            if (!categoryIdBefore.equals(categoryId)) {
                Result<List<Content>> result = contentFeign.findByCategory(new Long(categoryIdBefore));
                if (result.getData() != null) {
                    //更新缓存
                    stringRedisTemplate.boundValueOps("content_" + categoryIdBefore).set(JSON.toJSONString(result.getData()));
                }
            }
        } else {  //删除操作，取修改前的值
            //tb_content表中categoryId在第二列，所以下标为1
            categoryId = rowData.getBeforeColumns(1).getValue();
        }
        //根据categoryId查询所有广告列表
        Result<List<Content>> result = contentFeign.findByCategory(new Long(categoryId));
        if (result.getData() != null) {
            //更新缓存
            stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(result.getData()));
        }
    }


    /**
     * 新增监听
     *
     * @InsertListenPoint：新增监听 CanalEntry.EventType:变更操作类型
     * CanalEntry.RowData：此次变更的数据
     */
    /*@InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        //rowData.getBeforeColumnsList():数据变更前的内容
        //rowData.getAfterColumnsList()：数据变更后的内容
        System.out.println("---------新增监听--------");
        Descriptors.EnumDescriptor descriptorForType = eventType.getDescriptorForType();
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }
*/
    /**
     * 修改监听
     *
     * @UpdateListenPoint：更新监听 CanalEntry.EventType:变更操作类型
     * CanalEntry.RowData：此次变更的数据
     */
    /*@UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("---------更新监听--------");
        int i = 0;
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            CanalEntry.Column beforeColumns = rowData.getBeforeColumns(i);
            if (!beforeColumns.getValue().equals(column.getValue())) {
                System.out.println("修改了字段:" + column.getName() + ":");
                System.out.println(beforeColumns.getValue() + "===>" + column.getValue());
            }
            i++;
        }
    }
*/
    /**
     * 删除监听
     *
     * @param eventType
     * @param rowData
     */
   /* @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        System.out.println("---------删除监听--------");
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println(column.getName() + "---" + column.getValue());
        }
    }
*/
    /**
     * 自定义监听
     *
     * @ListenPoint：自定义监听 destination：必须使用canal.properties配置文件中canal.destinations属性的名字
     * schema：监听的数据库
     * table：监听的表
     * eventType：监听的操作类型
     */
   /* @ListenPoint(destination = "example", schema = "changgou_content", table = "tb_content", eventType = CanalEntry.EventType.DELETE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.out.println("---------自定义监听--------");
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }*/
}
