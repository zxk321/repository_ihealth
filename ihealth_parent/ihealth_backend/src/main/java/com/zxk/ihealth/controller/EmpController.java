package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.domain.Employee;
import com.zxk.ihealth.service.EmpService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/emp")
@ResponseBody
public class EmpController {
    @Reference
    private EmpService empService;

    @RequestMapping("/save")
    public Map<String,Object> save(@RequestBody Employee employee){
        empService.save(employee);
        Map<String,Object> map = new HashMap<>();
        map.put("message","添加成功");
        return map;
    }

    @RequestMapping("/findAll")
    public List<Employee> findAll(){
        List<Employee> employeeList = empService.findAll();
        return employeeList;
    }

    @RequestMapping("/findById")
    public Employee findById(Integer id){
        Employee employee = empService.findById(id);
        return employee;
    }

    @RequestMapping("/deleteById")
    public void deleteById(Integer id){
       empService.deleteById(id);
    }

    @RequestMapping("/update")
    public void update(@RequestBody Employee employee){
        empService.update(employee);
    }

}
