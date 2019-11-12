package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.constant.RedisMessageConstant;
import com.zxk.ihealth.domain.Member;
import com.zxk.ihealth.domain.Order;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.MemberService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    @RequestMapping("/login.do")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //1.判断验证码是否一致
        //1.1从Redis中找出验证码
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //比较用书输入的验证码和后台存储的验证码是否一致
        if (codeInRedis != null && map.get("validateCode") != null && codeInRedis.equals(map.get("validateCode"))) {
            //验证码通过验证
            //1.判断用户是否是会员
            //1.1根据电话号码调用memberService层查询是否存在用户
            Member member = memberService.findByTelephone((String)map.get("telephone"));
            //1.2判断member
            if (member == null){
                //1.2.1说明不存在此会员，添加该会员
                member = new Member();
                member.setPhoneNumber((String) map.get("telephone"));//会员电话
                member.setRegTime(new Date());//会员注册日期
                memberService.add(member);
            }
            //登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//路径
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);
            //保存会员信息到Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);

            //从Redis中删除验证码
            jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码不正确
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
