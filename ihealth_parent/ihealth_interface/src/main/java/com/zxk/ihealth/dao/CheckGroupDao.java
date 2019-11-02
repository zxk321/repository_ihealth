package com.zxk.ihealth.dao;

import com.github.pagehelper.Page;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.QueryPageBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface CheckGroupDao {
    //添加检查项
    @Insert("insert into t_checkgroup values(null,#{code},#{name},#{helpCode},#{sex},#{remark},#{attention})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addGroup(CheckGroup checkGroup);

    //关联检查组与检查项
    @Insert("insert into t_checkgroup_checkitem values(#{checkgroup_id},#{checkitem_id})")
    void setGroupCheckItemRal(Map<String, Integer> map);

    //分页查找检查项
    @Select({"<script>",
            " select * from t_checkgroup ",
            "<if test='queryString!=null and queryString.length > 0'>",
            " where (code like '%${queryString}%' or name like '%${queryString}%' or helpCode like '%${queryString}%')",
            "</if>",
            "</script>"})
    Page<CheckItem> findByCondition(QueryPageBean pageBean);
}
