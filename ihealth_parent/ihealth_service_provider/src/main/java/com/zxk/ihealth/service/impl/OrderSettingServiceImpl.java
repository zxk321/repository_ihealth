package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.dao.OrderSettingDao;
import com.zxk.ihealth.domain.OrderSetting;
import com.zxk.ihealth.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addOrderSetting(List<OrderSetting> list) {
        //遍历集合得到每一个OrderSetting对象
        for (OrderSetting orderSetting : list) {
            //首先判断当天是否已存在预约设置
            Long count = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            //判断count值的大小
            if (count>0){
                //说明已设置过预约信息,更新预约信息
                orderSettingDao.updateOrderSetting(orderSetting);
            }else {
                //说明不存在当前预约信息,添加预约信息
                orderSettingDao.addOrderSetting(orderSetting);
            }
        }
    }

    @Override
    public List<Map> findOrderSettingByMonth(String date) {
        List<OrderSetting> orderSettingList = orderSettingDao.findOrderSettingByMonth(date);
        //创建集合存储数据
        List<Map> list = new ArrayList<>();
        //遍历预约信息集合
        for (OrderSetting orderSetting : orderSettingList) {
            Map map = new HashMap();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            list.add(map);
        }
        return list;
    }

    @Override
    public void editOrderSetting(OrderSetting orderSetting) {
        //首先判断当天是否已存在预约设置
        Long count = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //判断count值的大小
        if (count>0){
            //说明已设置过预约信息,更新预约信息
            orderSettingDao.updateOrderSetting(orderSetting);
        }else {
            //说明不存在当前预约信息,添加预约信息
            orderSettingDao.addOrderSetting(orderSetting);
        }
    }
}
