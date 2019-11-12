package com.zxk.ihealth.controller;

import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.entity.Result;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/getUsername.do")
    public Result getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }else {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    @RequestMapping("/getError")
    public Map getError(HttpSession session){
        //创建map集合，用来存储响应的数据
        Map map = new HashMap();
        //通过session获取SpringSecurity的异常信息
        Exception exception = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        if (exception!=null){
            //将异常信息存入map集合
            map.put("errorMsg",exception.getMessage());
        }
        //返回结果集合
        return map;
    }
}
