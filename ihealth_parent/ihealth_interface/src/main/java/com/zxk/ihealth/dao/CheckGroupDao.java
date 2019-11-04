package com.zxk.ihealth.dao;

import com.github.pagehelper.Page;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.QueryPageBean;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    //添加检查项
    @Insert("insert into t_checkgroup values(null,#{code},#{name},#{helpCode},#{sex},#{remark},#{attention})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void addGroup(CheckGroup checkGroup);

    //关联检查组与检查项方式1
    @Insert("insert into t_checkgroup_checkitem values(#{checkgroup_id},#{checkitem_id})")
    void setGroupCheckItemRal(Map<String, Integer> map);

    /*//关联检查组与检查项方式2
    @Insert({"<script>",
            "insert into t_checkgroup_checkitem " ,
            "<foreach item='id' separator=','>" ,
            "(#{checkGroupId},#{id})" ,
            " </foreach>", 
            "</script>"})
    void setGroupCheckItemRal2(Integer checkGroupId,Integer[] checkitemIds);*/

    //分页查找检查项
    @Select({"<script>",
            " select * from t_checkgroup ",
            "<if test='queryString!=null and queryString.length > 0'>",
            " where (code like '%${queryString}%' or name like '%${queryString}%' or helpCode like '%${queryString}%')",
            "</if>",
            "</script>"})
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "code",column = "code"),
            @Result(property = "name",column = "name"),
            @Result(property = "helpCode",column = "helpCode"),
            @Result(property = "sex",column = "sex"),
            @Result(property = "remark",column = "remark"),
            @Result(property = "attention",column = "attention"),
            @Result(property = "checkItems",column = "id",
                    javaType = List.class,
                    many = @Many(select="com.zxk.ihealth.dao.CheckItemDao.findByCheckGroupId"))
    })
    Page<CheckGroup> findByCondition(QueryPageBean pageBean);

    //查询检查组关联的检查项
    @Select("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{id}")
    List<Integer> findRelationById(Integer id);

    //根据id查询检查组
    @Select("select * from t_checkgroup where id=#{id}")
    CheckGroup findGroupById(Integer id);

    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{id}")
    void deleteGroupCheckItemRal(Integer id);

    @Update({"<script>",
            "update t_checkgroup " ,
            "<set>" ,
            "<if test='name != null'>" ,
            "name = #{name}," ,
            "</if>" ,
            "<if test='sex != null'>" ,
            "sex = #{sex}," ,
            "</if>" ,
            "<if test='code != null'>" ,
            "code = #{code}," ,
            "</if>" ,
            "<if test='helpCode != null'>" ,
            "helpCode = #{helpCode}," ,
            "</if>" ,
            "<if test='attention != null'>" ,
            "attention = #{attention}," ,
            "</if>" ,
            "<if test='remark != null'>" ,
            "remark = #{remark}," ,
            "</if>" ,
            "</set> " ,
            "where id = #{id}",
            "</script>"})
    void updateGroup(CheckGroup checkGroup);

    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();

}
