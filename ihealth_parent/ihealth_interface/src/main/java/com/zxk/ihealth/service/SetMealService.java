package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.Setmeal;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;

public interface SetMealService {
    PageResult findPage(QueryPageBean pageBean);

    void addMeal(Setmeal setmeal, Integer[] checkgroupIds);
}
