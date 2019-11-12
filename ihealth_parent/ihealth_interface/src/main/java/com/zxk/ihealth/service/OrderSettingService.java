package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addOrderSetting(List<OrderSetting> list);

    List<Map> findOrderSettingByMonth(String date);

    void editOrderSetting(OrderSetting orderSetting);
}
