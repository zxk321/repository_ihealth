package com.zxk.ihealth.dao;

import com.github.pagehelper.Page;
import com.zxk.ihealth.domain.Setmeal;
import com.zxk.ihealth.entity.QueryPageBean;
import org.apache.ibatis.annotations.*;

import java.util.List;
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

    @Select("select * from t_setmeal")
    List<Setmeal> getSetmeal();

    @Select("select * from t_setmeal where id = #{id}")
    @Results({
            @Result(id = true,property = "id",column = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "code",column = "code"),
            @Result(property = "helpCode",column = "helpCode"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "age",column = "age"),
            @Result(property = "price",column = "price"),
            @Result(property = "remark",column = "remark"),
            @Result(property = "attention",column = "attention"),
            @Result(property = "img",column = "img"),
            @Result(property = "checkGroups",column = "id",
                    javaType = List.class,
                    many = @Many(select = "com.zxk.ihealth.dao.CheckGroupDao.findBySetmealId")
            )
    })
    Setmeal findById(Integer id);
}
