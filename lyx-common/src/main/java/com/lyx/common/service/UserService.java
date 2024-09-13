package com.lyx.common.service;

import com.lyx.common.model.User;

public interface UserService {
    User getUser(User user);
    default int getAge(){
        return 8;
    }
}
