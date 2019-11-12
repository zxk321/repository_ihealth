package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.constant.RedisMessageConstant;
import com.zxk.ihealth.domain.Order;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.OrderService;
import com.zxk.ihealth.util.SendSMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping("/submitOrder.do")
    public Result submitOrder(@RequestBody Map map){
        //1.判断验证码是否一致
        //1.1从Redis中找出验证码
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        Result result = null;
        //比较用书输入的验证码和后台存储的验证码是否一致
        if (codeInRedis != null && map.get("validateCode") != null && codeInRedis.equals(map.get("validateCode"))){
            //验证码通过验证，设置预约的类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //调用service层执行预约相关业务
            try {
                result = orderService.submitOrder(map);
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
            //判断是否预约成功
            if (result.isFlag()){
                //预约成功,发送短信提醒用户
                try {
                    String orderDate = (String) map.get("orderDate");
                    SendSMSUtil.sendMsm(telephone, SendSMSUtil.signName, SendSMSUtil.templateCode_OrderSuccess, orderDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //从Redis中删除验证码
                jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            }
            return result;
        }else {
            //验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById.do")
    public Result findById(@RequestParam("id") Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
