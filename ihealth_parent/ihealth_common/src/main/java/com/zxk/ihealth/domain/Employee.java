package com.zxk.ihealth.domain;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {
    private Integer id;
    private String name;
    private Date birthday;
    private Double salary;

    public Employee() {
    }

    public Employee(Integer id, String name, Date birthday, Double salary) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", salary=" + salary +
                '}';
    }
}
