package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface PermissionDao {

    @Select("select * from t_permission p,t_role_permission rp where p.id=rp.permission_id and rp.role_id = #{id}")
    Set<Permission> findByRoleId(Integer id);
}
