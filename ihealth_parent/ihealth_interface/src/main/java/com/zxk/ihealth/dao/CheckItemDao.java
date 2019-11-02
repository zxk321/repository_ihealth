package com.zxk.ihealth.dao;

import com.github.pagehelper.Page;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.QueryPageBean;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CheckItemDao {
    //添加检查项
    @Insert("insert into t_checkitem values(null,#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    public void addItem(CheckItem checkItem);

    //分页查找检查项
    @Select("<script>"+
            " select * from t_checkitem "+
            "<if test='queryString!=null and queryString.length > 0'>"+
            " where (code like '%${queryString}%' or name like '%${queryString}%')"+
            "</if>"+
            "</script>")
    Page<CheckItem> findByCondition(QueryPageBean pageBean);

    //根据id查询检查项被多少检查组关联
    @Select("select count(*) from t_checkgroup_checkitem where checkitem_id=#{id}")
    Long findCountById(Integer id);

    //根据id删除检查项
    @Delete("delete from t_checkitem where id=#{id}")
    void deleteById(Integer id);

    //根据id查询检查项
    @Select("select * from t_checkitem where id=#{id}")
    CheckItem findById(Integer id);

    //修改检查项
    @Update({"<script>",
            "update t_checkitem",
            "<set>",
            "<if test='name != null'>",
            "name = #{name},",
            "</if>",
            "<if test='sex != null'>",
            "sex = #{sex}," ,
            "</if>" ,
            "<if test='code != null'>" ,
            "code = #{code}," ,
            "</if>" ,
            "<if test='age != null'>" ,
            "age = #{age}," ,
            "</if>" ,
            "<if test='price != null'>" ,
            "price = #{price}," ,
            "</if>" ,
            "<if test='type != null'>" ,
            "type = #{type}," ,
            "</if>" ,
            "<if test='attention != null'>" ,
            "attention = #{attention}," ,
            "</if>" ,
            "<if test='remark != null'>" ,
            "remark = #{remark}," ,
            "</if>" ,
            "</set>" ,
            "where id = #{id}",
            "</script>"})
    void updateItem(CheckItem checkItem);

    //查找所有的检查项
    @Select("select * from t_checkitem")
    List<CheckItem> findAll();
}
