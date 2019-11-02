package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface EmpDao {
    @Select("select * from emp")
    List<Employee> findAll();

    @Insert("insert into emp values (null,#{name},#{birthday},#{salary})")
    public void save(Employee employee);

    @Delete("delete from emp where id=#{id}")
    void deleteById(Integer id);

    @Select("select * from emp where id=#{id}")
    Employee findById(Integer id);

    @Update("update emp set id=#{id},name=#{name},birthday=#{birthday},salary=#{salary} where id=#{id}")
    void update(Employee employee);
}
