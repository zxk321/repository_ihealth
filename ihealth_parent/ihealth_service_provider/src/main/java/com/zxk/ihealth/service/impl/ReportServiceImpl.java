package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.dao.MemberDao;
import com.zxk.ihealth.dao.OrderDao;
import com.zxk.ihealth.service.MemberService;
import com.zxk.ihealth.service.ReportService;
import com.zxk.ihealth.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public Map<String, Object> getOperationData() {
        //查询新增会员数
        Long member_todayCount = memberDao.todayNewCount(new Date());
        //查询总会员数
        Long member_totalCount = memberDao.findTotalCount();
        //查询本周新增会员数
        Long member_weekCount = memberDao.weekNewCount(DateUtils.getFirstDayOfWeek(new Date()));
        //查询本月新增会员数
        Long member_monthCount = memberDao.monthNewCount(DateUtils.getFirstDay4ThisMonth());

        //查询今日预约数
        Long order_todayCount = orderDao.todayNewCount(new Date());
        //查询本周预约数
        Long order_weekCount = orderDao.weekNewCount(DateUtils.getFirstDayOfWeek(new Date()));
        //查询本月预约数
        Long order_monthCount = orderDao.monthNewCount(DateUtils.getFirstDay4ThisMonth());

        //查询今日到诊数
        Long order_todayVisitedCount = orderDao.todayVisitedCount(new Date());
        //查询本周到诊数
        Long order_weekVisitedCount = orderDao.weekVisitedCount(DateUtils.getFirstDayOfWeek(new Date()));
        //查询本月到诊数
        Long order_monthVisitedCount = orderDao.monthVisitedCount(DateUtils.getFirstDay4ThisMonth());

        //查询热门预约套餐
        List<Map<String,Object>> hotSetmealList = orderDao.findHotSetmeal();

        //创建集合存储数据
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("reportDate",new Date());
        map.put("todayNewMember",member_todayCount);
        map.put("totalMember",member_totalCount);
        map.put("thisWeekNewMember",member_weekCount);
        map.put("thisMonthNewMember",member_monthCount);
        map.put("todayOrderNumber",order_todayCount);
        map.put("thisWeekOrderNumber",order_weekCount);
        map.put("thisMonthOrderNumber",order_monthCount);
        map.put("todayVisitsNumber",order_todayVisitedCount);
        map.put("thisWeekVisitsNumber",order_weekVisitedCount);
        map.put("thisMonthVisitsNumber",order_monthVisitedCount);
        map.put("hotSetmeal",hotSetmealList);

        //返回数据
        return map;
    }
}
