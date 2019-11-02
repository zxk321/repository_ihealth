package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.dao.EmpDao;
import com.zxk.ihealth.domain.Employee;
import com.zxk.ihealth.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = EmpService.class)
@Transactional
//@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpDao empDao;

    /**
     * 查询所有员工
     * @return
     */
    @Override
    public List<Employee> findAll(){
        List<Employee> employeeList = empDao.findAll();
        return employeeList;
    }

    /**
     * 添加员工
     * @param employee
     */
    @Override
    public void save(Employee employee){
        empDao.save(employee);
    }

    /**
     * 通过id删除用户
     * @param id
     */
    @Override
    public void deleteById(Integer id){
        empDao.deleteById(id);
    }

    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    @Override
    public Employee findById(Integer id){
        Employee employee = empDao.findById(id);
        return employee;
    }

    /**
     * 修改用户
     * @param employee
     */
    @Override
    public void update(Employee employee){
        empDao.update(employee);
    }

}
