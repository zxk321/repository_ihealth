package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.dao.MemberDao;
import com.zxk.ihealth.dao.OrderDao;
import com.zxk.ihealth.dao.OrderSettingDao;
import com.zxk.ihealth.domain.Member;
import com.zxk.ihealth.domain.Order;
import com.zxk.ihealth.domain.OrderSetting;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.OrderService;
import com.zxk.ihealth.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 提交预约信息
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) throws Exception {
        //1.判断用户是否是会员
        //1.1根据电话号码调用memberDao层查询是否存在用户
        Member member = memberDao.findByTelephone((String)map.get("telephone"));
        //1.2判断member
        if (member == null){
            //1.2.1说明不存在此会员，添加该会员
            member = new Member();
            member.setName((String) map.get("name"));//会员姓名
            member.setIdCard((String) map.get("idCard"));//会员身份证
            member.setSex((String) map.get("sex"));//会员性别
            member.setPhoneNumber((String) map.get("telephone"));//会员电话
            member.setRegTime(new Date());//会员注册日期
            memberDao.add(member);
        }

        //2.根据提交的日期查询该日期是否已设置过预约信息
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.getByOrderDate(date);
        //2.1判断orderSetting
        if (orderSetting == null){
            //2.1.1说明当前预约日期还不能进行预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.2判断用户是否重复预约，根据会员id，预约日期，套餐setmealId
        Integer setmealId = Integer.parseInt((String)map.get("setmealId"));
        Order order = new Order(member.getId(),date,null,null,setmealId);
        Order order_query = orderDao.findByCondition(order);
        if (order_query!=null){
            //2.2.1说明已经预约,直接返回信息
            return new Result(false,MessageConstant.HAS_ORDERED);
        }
        //2.3判断当前日期是否预约满
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
            //2.3.1说明当前预约已满，返回不能预约消息
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //到此，说明可以预约，保存预约信息
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);

        //预约完成后，将预约信息中的已预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservations(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById(id);
    }

    @Override
    public Map<String, Object> getSetmealReport() {
        //从数据库中查询已预约的套餐名称及预约数量
        List<Map<String,Object>> setmealMaps = orderDao.getSetmealReport();
        //创建集合存储预约名称
        List<String> setmealNameList = new ArrayList<String>();
        //遍历setmealMaps集合
        for (Map<String, Object> map : setmealMaps) {
            String name = (String) map.get("name");
            setmealNameList.add(name);
        }
        //创建集合存储响应结果集合
        Map<String,Object> map = new HashMap<>();
        map.put("setmealName",setmealNameList);
        map.put("setmealData",setmealMaps);
        return map;
    }
}
