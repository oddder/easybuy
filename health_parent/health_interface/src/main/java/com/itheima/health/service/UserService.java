package com.itheima.health.service;

import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;

import java.util.ArrayList;

public interface UserService {

    User findUserByUsername(String username);

    ArrayList<Menu> findUserMenu(String username);
}
