package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.User;

public interface UserService {
    User findUserByUsername(String username);
}
