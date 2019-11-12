package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.MemberService;
import com.zxk.ihealth.service.OrderService;
import com.zxk.ihealth.service.ReportService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;

    /**
     * 会员数量统计
     * @return
     */
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport(){
        //获取当前日期
        Calendar calendar = Calendar.getInstance();
        //获取当前日期12个月前的日期
        calendar.add(Calendar.MONTH,-12);
        //创建集合用来存储月份
        List<String> monthList = new ArrayList<String>();
        //循环的到一年以前至现在的每个月
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            String date = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
            monthList.add(date);
        }
        //创建集合存储每月累计的会员数量
        List<Long> memeberCountList = new ArrayList<Long>();
        //遍历月份集合，从数据库中查询累计会员数量
        for (String month : monthList) {
            Long count = memberService.findCountByMonth(month);
            memeberCountList.add(count);
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("monthData",monthList);
        map.put("memberData",memeberCountList);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @Reference
    private OrderService orderService;

    /**
     * 套餐预约占比统计
     * @return
     */
    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport(){
        try {
            Map<String,Object> map = orderService.getSetmealReport();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS);
        }
    }

    @Reference
    private ReportService reportService;
    /**
     * 运营数据统计
     * @return
     */
    @RequestMapping("/getOperationData.do")
    public Result getOperationData(){
        try {
            Map<String,Object> map = reportService.getOperationData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
        }
    }
}
