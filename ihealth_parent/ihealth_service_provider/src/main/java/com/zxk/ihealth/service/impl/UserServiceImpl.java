package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.dao.UserDao;
import com.zxk.ihealth.domain.User;
import com.zxk.ihealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        //调用持久层查询
        return userDao.findByUsername(username);
    }
}
