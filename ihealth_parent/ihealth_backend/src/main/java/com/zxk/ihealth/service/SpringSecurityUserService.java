package com.zxk.ihealth.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.domain.Permission;
import com.zxk.ihealth.domain.Role;
import com.zxk.ihealth.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询数据库
        User user = userService.findUserByUsername(username);
        //判断user
        if (user == null){
            //说明不存在该用户
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        //至此说明存在该用户
        //创建集合存储角色及权限
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            //获取绝俗对应的权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        //返回结果
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
    }
}
