package com.zxk.ihealth.controller;

import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.constant.RedisMessageConstant;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.util.SendSMSUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order.do")
    public Result send4Order(@RequestParam("telephone") String telephone){
        //生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //调用工具发送短信
        try {
            SendSMSUtil.sendMsm(telephone,SendSMSUtil.signName,SendSMSUtil.templateCode_Order,code);
            //发送成功就将验证码存入Redis中
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER,60*5,code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("/send4Login.do")
    public Result send4Login(@RequestParam("telephone") String telephone){
        //生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //调用工具发送短信
        try {
            SendSMSUtil.sendMsm(telephone,SendSMSUtil.signName,SendSMSUtil.templateCode_Login,code);
            //发送成功就将验证码存入Redis中
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN,60*5,code);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
