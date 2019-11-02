package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.Employee;

import java.util.List;

public interface EmpService {

    /**
     * 查询所有员工
     * @return
     */
    public List<Employee> findAll();

    /**
     * 添加员工
     * @param employee
     */
    public void save(Employee employee);

    /**
     * 通过id删除用户
     * @param id
     */
    public void deleteById(Integer id);

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    public Employee findById(Integer id);

    /**
     * 修改用户
     * @param employee
     */
    public void update(Employee employee);
}
