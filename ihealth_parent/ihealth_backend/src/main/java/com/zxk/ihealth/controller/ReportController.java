package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.MemberService;
import com.zxk.ihealth.service.OrderService;
import com.zxk.ihealth.service.ReportService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
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

    /**
     * 导出运营数据
     * @return
     */
    @RequestMapping("/exportBusinessReport.do")
    public void exportBusinessReport(HttpSession session, HttpServletResponse response) throws IOException {
        try{
            //查询相关数据
            Map<String,Object> map = reportService.getOperationData();

            //获取报表路径
            String path = session.getServletContext().getRealPath("/")+"template/report_template.xlsx";

            //创建工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(path));
            if (workbook!=null){
                //获取工作表
                XSSFSheet sheet = workbook.getSheetAt(0);
                if (sheet!=null){
                    //获取单元格
                    sheet.getRow(2).getCell(5).setCellValue(map.get("reportDate").toString());
                    sheet.getRow(4).getCell(5).setCellValue(map.get("todayNewMember").toString());
                    sheet.getRow(4).getCell(7).setCellValue(map.get("totalMember").toString());
                    sheet.getRow(5).getCell(5).setCellValue(map.get("thisWeekNewMember").toString());
                    sheet.getRow(5).getCell(7).setCellValue(map.get("thisMonthNewMember").toString());

                    sheet.getRow(7).getCell(5).setCellValue(map.get("todayOrderNumber").toString());
                    sheet.getRow(8).getCell(5).setCellValue(map.get("thisWeekOrderNumber").toString());
                    sheet.getRow(9).getCell(5).setCellValue(map.get("thisMonthOrderNumber").toString());

                    sheet.getRow(7).getCell(7).setCellValue(map.get("todayVisitsNumber").toString());
                    sheet.getRow(8).getCell(7).setCellValue(map.get("thisWeekVisitsNumber").toString());
                    sheet.getRow(9).getCell(7).setCellValue(map.get("thisMonthVisitsNumber").toString());

                    List hotSetmeal = (List) map.get("hotSetmeal");
                    //遍历map集合中hotSetmeal集合
                    for (int i = 0; i < hotSetmeal.size(); i++) {

                        Map setmealMap = (Map) hotSetmeal.get(i);
                        //获取行
                        Row row = sheet.getRow(12+i);
                        row.getCell(4).setCellValue(setmealMap.get("name").toString());
                        row.getCell(5).setCellValue(setmealMap.get("setmeal_count").toString());
                        row.getCell(6).setCellValue(setmealMap.get("proportion").toString());
                    }

                    //设置响应头
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    String filename = new String("运营数据统计表.xlsx".getBytes("utf-8"),"iso-8859-1");
                    response.setHeader("content-Disposition","attachment;filename="+filename);
                    ServletOutputStream outputStream = response.getOutputStream();
                    //输出
                    workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    workbook.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
