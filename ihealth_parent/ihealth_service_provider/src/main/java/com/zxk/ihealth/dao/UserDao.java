package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface UserDao {

    @Select("select * from t_user where username=#{username}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "birthday", column = "birthday"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "username", column = "username"),
            @Result(property = "password", column = "password"),
            @Result(property = "remark", column = "remark"),
            @Result(property = "station", column = "station"),
            @Result(property = "telephone", column = "telephone"),
            @Result(property = "roles",column = "id",javaType = Set.class,
                    many = @Many(select = "com.zxk.ihealth.dao.RoleDao.findByUserId")
            )
    })
    User findByUsername(String username);
}
