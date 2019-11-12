package com.zxk.ihealth.util;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

import java.util.HashMap;
import java.util.Map;

public class SendSMSUtil {
    private static final String accessKeyId = "LTAI4Fw423VWiFaiZVHFj7Ar";
    private static final String accessKeySecret = "DYGk0ZUNXNSDPXpJaVQoYVy17ow6nK";
    private static final String regionId = "cn-beijing";
    public static final String signName = "爱健康";
    public static final String templateCode_Order = "SMS_176913282";
    public static final String templateCode_Login = "SMS_176928211";
    public static final String templateCode_ChangePWD = "SMS_176938290";
    public static final String templateCode_OrderSuccess = "SMS_177250870";

    public static String sendMsm(String phoneNumber,String signName,String templateCode,String templateParam) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", phoneNumber);//要发送的手机号
        request.putQueryParameter("SignName", signName);//签名
        request.putQueryParameter("TemplateCode", templateCode);//模版CODE
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code",templateParam);
        String jsonTemplateParam = JSON.toJSONString(map);
        request.putQueryParameter("TemplateParam", jsonTemplateParam);//模板中code的值
        try {
            CommonResponse response = client.getCommonResponse(request);
            //System.out.println(response.getData());
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return "";
    }

}
