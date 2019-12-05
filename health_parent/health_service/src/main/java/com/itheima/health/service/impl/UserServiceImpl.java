package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.UserDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CheckItemServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2019/11/19 15:48
 * @Version V1.0
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    @Override
    public ArrayList<Menu> findUserMenu(String username) {
        List<Menu> userMenu = userDao.findUserMenu(username);
        ArrayList<Menu> menus = new ArrayList<>();
        //查询出没有父级菜单的菜单
        for (Menu menu : userMenu) {
            if (menu.getParentMenuId()==null) {
                menus.add(menu);
            }
        }
        //遍历menus中的所有菜单
        for (Menu menu : menus) {
            ArrayList<Menu> menus1 = new ArrayList<>();
            Integer id = menu.getId();
            //再次遍历用户所有菜单
            for (Menu userMenu1 : userMenu) {
                Integer parentMenuId = userMenu1.getParentMenuId();
                if (parentMenuId!=null) {
                    //判断是否该id为父菜单的id
                    if (id.equals(parentMenuId)) {
                        //添加到改菜单中的子菜单
                        menus1.add(userMenu1);
                    }
                }
            }
            menu.setChildren(menus1);
        }
        return menus;
    }
}
