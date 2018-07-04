package org.xi.sso.server.service;

import org.xi.sso.server.entity.UserEntity;

public interface UserService {

    /**
     * 添加
     *
     * @param entity
     * @return
     */
    int insert(UserEntity entity);

    /**
     * 查询
     *
     * @param username
     * @return
     */
    UserEntity getByUsername(String username);

    /**
     * 根据主键获取
     *
     * @param id
     * @return
     */
    UserEntity getById(Integer id);
}
