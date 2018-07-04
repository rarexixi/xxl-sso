package org.xi.sso.server.mapper;

import org.xi.sso.server.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

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
    UserEntity getByUsername(@Param("username") String username);

    /**
     * 根据主键获取
     *
     * @param id
     * @return
     */
    UserEntity getById(@Param("id") Integer id);
}
