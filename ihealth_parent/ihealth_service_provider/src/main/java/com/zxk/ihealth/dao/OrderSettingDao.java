package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {
    @Select("select count(*) from t_ordersetting where orderDate=#{orderDate}")
    Long findByOrderDate(Date orderDate);

    @Update("update t_ordersetting set number=#{number} where orderDate=#{orderDate}")
    void updateOrderSetting(OrderSetting orderSetting);

    @Insert("insert into t_ordersetting(orderDate,number) values(#{orderDate},#{number})")
    void addOrderSetting(OrderSetting orderSetting);

    @Select("SELECT orderDate,number,reservations FROM t_ordersetting WHERE DATE_FORMAT(orderDate,'%Y-%m')=#{date}")
    List<OrderSetting> findOrderSettingByMonth(String date);

    @Select("select * from t_ordersetting where orderDate=#{date}")
    OrderSetting getByOrderDate(Date date);

    @Update("update t_ordersetting set reservations=#{reservations} where orderDate=#{orderDate}")
    void editReservations(OrderSetting orderSetting);
}
