package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserDao {

    User findUserByUsername(String username);

    List<Menu> findUserMenu(String username);

}
