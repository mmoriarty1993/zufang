package com.imooc.service.user;

import com.imooc.entity.User;

public interface UserService {
    User findUserByName(String userName);
}
