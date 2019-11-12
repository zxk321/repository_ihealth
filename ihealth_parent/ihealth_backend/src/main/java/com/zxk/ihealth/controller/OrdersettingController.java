package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.domain.OrderSetting;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.OrderSettingService;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrdersettingController {
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 上传预约表格信息
     * @param excelFile
     * @return
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/upload.do")
    public Result uploadOrdersetting(@RequestParam("excelFile") MultipartFile excelFile){
        //创建工作簿
        Workbook workbook = null;
        try {
            String extension = FilenameUtils.getExtension(excelFile.getOriginalFilename());
            if ("xlsx".equals(extension)){
                //2007以上版本
                workbook = new XSSFWorkbook(excelFile.getInputStream());
            }else if ("xls".equals(extension)){
                //2003以下版本
                workbook = new HSSFWorkbook(excelFile.getInputStream());
            }else {
                return new Result(false,"上传的文件必须是Excel格式文件，请重新上传！");
            }
            //获取工作表
            Sheet sheet = workbook.getSheetAt(0);
            //创建集合存储数据
            List<OrderSetting> list = new ArrayList<>();
            //遍历工作表
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Date date = sheet.getRow(i).getCell(0).getDateCellValue();
                int number = (int) sheet.getRow(i).getCell(1).getNumericCellValue();
                //创建OrderSetting对象
                OrderSetting orderSetting = new OrderSetting(date,number);
                //将对象存入集合
                list.add(orderSetting);
            }
            //调用service进行添加
            orderSettingService.addOrderSetting(list);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 页面展示预约信息
     * @param date
     * @return
     */
    @RequestMapping("/findOrderSettingByMonth.do")
    public Result findOrderSettingByMonth(String date){
        try {
            List<Map> list = orderSettingService.findOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 设置预约信息
     * @param orderSetting
     * @return
     */
    @PreAuthorize("hasAuthority('ORDERSETTING')")
    @RequestMapping("/editOrderSetting.do")
    public Result editOrderSetting(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editOrderSetting(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
