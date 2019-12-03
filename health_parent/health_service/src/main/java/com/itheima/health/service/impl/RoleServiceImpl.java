package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.entity.PageResult;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;

    //分页
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Role> roles = roleDao.findPage(queryString);
        return new PageResult(roles.getTotal(), roles.getResult());
    }

    //查找所有权限
    public List<Permission> findPermission() {
        return permissionDao.findAll();
    }

    //查找所有菜单
    public List<Menu> findMenu() {
        return menuDao.findAll();
    }

    //添加角色
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        if (role.getKeyword() == null || "".equals(role.getKeyword())) {
            throw new RuntimeException("关键字不能为空");
        }
        if (role.getName() == null || "".equals(role.getName())) {
            throw new RuntimeException("您创建的角色需要一个名字");
        }
        //对role表进行添加
        roleDao.add(role);
        //添加中间表数据
        if (permissionIds.length < 1 || permissionIds == null) {
            throw new RuntimeException("请给您创建的角色添加权限");
        }
        for (Integer permissionId : permissionIds) {
            roleDao.roleAddPermission(role.getId(), permissionId);
        }


        if (menuIds.length < 1 || menuIds == null) {
            throw new RuntimeException("请给您创建的角色添加可以访问的菜单");
        }
        for (Integer menuId : menuIds) {
            roleDao.roleAddmenu(role.getId(), menuId);
        }

    }

    //删除角色
    public void delete(int id) {
        //删除之前需清除中间表中的关联数据
        roleDao.removePremission(id);
        roleDao.removeMenu(id);
        roleDao.delete(id);
    }

    ////根据id查找
    public HashMap<String, Object> findById(int id) {
//    返回一个map集合
        Role byId = roleDao.findById(id);
        ArrayList<Integer> permissionIds = new ArrayList<>();
        Set<Permission> permissions = byId.getPermissions();
        for (Permission permission : permissions) {
            Integer permissionId = permission.getId();
            permissionIds.add(permissionId);
        }
        LinkedHashSet<Menu> menus = byId.getMenus();
        ArrayList<Integer> menuIds = new ArrayList<>();
        for (Menu menu : menus) {
            Integer menuId = menu.getId();
            menuIds.add(menuId);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("role", byId);
        result.put("permissionIds", permissionIds);
        result.put("menuIds", menuIds);
        return result;
    }

    //编辑role
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        if (role.getKeyword() == null || "".equals(role.getKeyword())) {
            throw new RuntimeException("关键字不能为空");
        }
        if (role.getName() == null || "".equals(role.getName())) {
            throw new RuntimeException("您编辑的角色需要一个名字");
        }
        //对role表进行编辑
        roleDao.edit(role);
        //清除中间表
        roleDao.removePremission(role.getId());
        roleDao.removeMenu(role.getId());

        //重新创建中间表联系
        if (permissionIds.length < 1 || permissionIds == null) {
            throw new RuntimeException("请给您编辑的角色添加权限");
        }
        if (menuIds.length < 1 || menuIds == null) {
            throw new RuntimeException("请给您编辑的角色添加可以访问的菜单");
        }
        //直接添加就好了
        for (Integer permissionId : permissionIds) {
            roleDao.roleAddPermission(role.getId(), permissionId);
        }

        for (Integer menuId : menuIds) {
            roleDao.roleAddmenu(role.getId(), menuId);
        }
    }
}
