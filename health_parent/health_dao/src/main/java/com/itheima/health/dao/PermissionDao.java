package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {

    //根据roleid查找premission
    Set<Permission> findPermissionsByRoleId(Integer roleId);

    //查找所有权限
    List<Permission> findAll();

}
