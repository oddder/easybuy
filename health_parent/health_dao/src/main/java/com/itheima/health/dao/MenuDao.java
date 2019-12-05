package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuDao {

    //查找所有菜单
    List<Menu> findAll();

    //根据roleid查找menu
    List<Menu> findMenuByRoleId();

    //根据id查找menu
    Menu findById();

}
