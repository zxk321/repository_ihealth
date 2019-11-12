package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    @Select({"<script>",
            "select * from t_order" ,
            "<where>" ,
            "    <if test='id != null'>" ,
            "        and id = #{id}" ,
            "    </if>" ,
            "    <if test='memberId != null'>" ,
            "        and member_id = #{memberId}" ,
            "    </if>" ,
            "    <if test='orderDate != null'>" ,
            "        and orderDate = #{orderDate}" ,
            "    </if>" ,
            "    <if test='orderType != null'>" ,
            "        and orderType = #{orderType}" ,
            "    </if>" ,
            "    <if test='orderStatus != null'>" ,
            "        and orderStatus = #{orderStatus}" ,
            "    </if>" ,
            "    <if test='setmealId != null'>" ,
            "        and setmeal_id = #{setmealId}" ,
            "    </if>" ,
            "</where>",
            "</script>"})
    Order findByCondition(Order order);

    @Insert("insert into t_order(member_id,orderDate,orderType,orderStatus,setmeal_id) values (#{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void add(Order order);

    @Select("SELECT " +
            "mb.`name` AS member, " +
            "od.orderDate, " +
            "od.orderType, " +
            "st.`name` AS setmeal " +
            "FROM " +
            "t_order AS od " +
            "INNER JOIN t_member AS mb ON od.member_id = mb.id " +
            "INNER JOIN t_setmeal AS st ON od.setmeal_id = st.id " +
            "WHERE " +
            "od.id = #{id} ")
    Map findById(Integer id);

    //套餐预约数查询
    @Select("SELECT Count(t_order.id) AS `value`,(select name from t_setmeal where t_order.setmeal_id=t_setmeal.id) `name` FROM t_order GROUP BY t_order.setmeal_id")
    List<Map<String, Object>> getSetmealReport();

    //查询当日预约数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE DATE_FORMAT(orderDate,'%Y-%m-%d') = DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long todayNewCount(Date date);

    //查询本周预约数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE DATE_FORMAT(orderDate,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long weekNewCount(Date date);

    //查询本月预约数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE DATE_FORMAT(orderDate,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long monthNewCount(Date date);

    //查询当日到诊数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE orderStatus = '已到诊' AND DATE_FORMAT(orderDate,'%Y-%m-%d') = DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long todayVisitedCount(Date date);

    //查询本周到诊数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE orderStatus = '已到诊' AND DATE_FORMAT(orderDate,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long weekVisitedCount(Date date);

    //查询本月到诊数
    @Select("SELECT Count(t_order.id) FROM t_order WHERE orderStatus = '已到诊' AND DATE_FORMAT(orderDate,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long monthVisitedCount(Date date);

    //热门预约套餐查询
    @Select("SELECT\n" +
            "\t(SELECT NAME FROM t_setmeal WHERE t_order.setmeal_id = t_setmeal.id) name,\n" +
            "\tCount(id) setmeal_count,\n" +
            "\t(COUNT(id)/(SELECT COUNT(id) FROM t_order)) proportion\n" +
            "FROM\n" +
            "\tt_order \n" +
            "GROUP BY\n" +
            "\tt_order.setmeal_id \n" +
            "ORDER BY\n" +
            "\tsetmeal_count DESC")
    List<Map<String, Object>> findHotSetmeal();

}
