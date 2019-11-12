package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.Role;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleDao {

    @Select("select * from t_role r,t_user_role ur where r.id=ur.role_id and ur.user_id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "keyword",column = "keyword"),
            @Result(property = "description",column = "description"),
            @Result(property = "permissions",column = "id",javaType = Set.class,
                    many = @Many(select = "com.zxk.ihealth.dao.PermissionDao.findByRoleId")
            )
    })
    Set<Role> findByUserId(Integer id);
}
