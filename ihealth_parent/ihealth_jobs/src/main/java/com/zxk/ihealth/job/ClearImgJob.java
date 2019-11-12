package com.zxk.ihealth.job;

import com.zxk.ihealth.constant.RedisConstant;
import com.zxk.ihealth.util.AliYunUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

@Component
public class ClearImgJob {
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        System.out.println("定时清理图片任务开始执行了!! "+new Date());
        Jedis jedis = jedisPool.getResource();
        //根据Redis中存储的两个集合求差集
        Set<String> set = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set != null) {
            for (String fileName : set) {
                //根据差集删除云空间的图片
                AliYunUtil.deleteByFileName(fileName);
                //删除redis集合中的图片
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            }
        }
        System.out.println("定时清理图片任务执行结束了!! "+new Date());
    }
}
