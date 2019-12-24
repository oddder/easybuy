package com.odder.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.Descriptors;
import com.xpand.starter.canal.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author Odder
 * @Date 2019/12/23 21:44
 * @Version 1.0
 */
@CanalEventListener
public class CanalDataEventListener {
    /**
     * 新增监听
     * @InsertListenPoint：新增监听
     * CanalEntry.EventType:变更操作类型
     * CanalEntry.RowData：此次变更的数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData){
        //rowData.getBeforeColumnsList():数据变更前的内容
        //rowData.getAfterColumnsList()：数据变更后的内容
        System.out.println("---------新增监听--------");
        Descriptors.EnumDescriptor descriptorForType = eventType.getDescriptorForType();
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }
    /**
     * 修改监听
     * @UpdateListenPoint：更新监听
     * CanalEntry.EventType:变更操作类型
     * CanalEntry.RowData：此次变更的数据
     */
    @UpdateListenPoint
    public void onEventUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
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

    /**
     * 删除监听
     * @param eventType
     * @param rowData
     */
    @DeleteListenPoint
    public void onEventDelete(CanalEntry.EventType eventType , CanalEntry.RowData rowData){
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        System.out.println("---------删除监听--------");
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println(column.getName() + "---" + column.getValue());
        }
    }

    /**
     * 自定义监听
     * @ListenPoint：自定义监听
     * destination：必须使用canal.properties配置文件中canal.destinations属性的名字
     * schema：监听的数据库
     * table：监听的表
     * eventType：监听的操作类型
     */
    @ListenPoint(destination = "example",schema = "changgou_content",table = "tb_content",eventType = CanalEntry.EventType.DELETE)
    public void onEventCustomUpdate(CanalEntry.EventType eventType,CanalEntry.RowData rowData){
        System.out.println("---------自定义监听--------");
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println(column.getName() + ":" + column.getValue());
        }
    }

}
