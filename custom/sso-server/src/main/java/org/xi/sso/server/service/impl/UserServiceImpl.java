package org.xi.sso.server.service.impl;

import org.xi.sso.server.entity.UserEntity;
import org.xi.sso.server.mapper.UserMapper;
import org.xi.sso.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public int insert(UserEntity entity) {

        int result = userMapper.insert(entity);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getByUsername(String username) {

        UserEntity entity = userMapper.getByUsername(username);
        return entity;
    }

    @Transactional(readOnly = true)
    @Override
    public UserEntity getById(Integer id) {

        UserEntity entity = userMapper.getById(id);
        return entity;
    }
}
