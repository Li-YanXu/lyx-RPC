package com.lyx.provider;

import com.lyx.common.model.User;
import com.lyx.common.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println("用户名："+user.getUserName());
        return user;
    }
}
