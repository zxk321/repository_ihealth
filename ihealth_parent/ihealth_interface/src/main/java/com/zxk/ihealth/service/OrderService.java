package com.zxk.ihealth.service;

import com.zxk.ihealth.entity.Result;

import java.util.Map;

public interface OrderService {
    Result submitOrder(Map map) throws Exception;

    Map findById(Integer id);

    Map<String, Object> getSetmealReport();

}
