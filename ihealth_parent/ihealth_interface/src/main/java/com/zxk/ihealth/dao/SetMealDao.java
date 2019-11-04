package com.zxk.ihealth.dao;

import com.github.pagehelper.Page;
import com.zxk.ihealth.domain.Setmeal;
import com.zxk.ihealth.entity.QueryPageBean;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface SetMealDao {

    @Select({"<script>",
            "select * from t_setmeal " ,
            "<where>" ,
            "<if test='queryString!=null and queryString.length > 0 '>" ,
            "and code like '%${queryString}%' or name like '%${queryString}%' or helpCode like '%${queryString}%'" ,
            "</if>" ,
            "</where>",
            "</script>"})
    Page<Setmeal> findByCondition(QueryPageBean pageBean);

    @Insert("insert into t_setmeal values(null,#{name},#{code},#{helpCode},#{sex},#{age},#{price},#{remark},#{attention},#{img})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void addMeal(Setmeal setmeal);

    @Insert("insert into t_setmeal_checkgroup values(#{setmealId},#{checkgroupId})")
    void setMealGroupRal(Map<String, Integer> map);
}
