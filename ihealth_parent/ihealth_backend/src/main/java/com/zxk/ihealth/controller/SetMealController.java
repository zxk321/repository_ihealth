package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.constant.RedisConstant;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.Setmeal;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.SetMealService;
import com.zxk.ihealth.util.AliYunUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 分页查询
     * @param pageBean
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_QUERY')")
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean pageBean){
        return setMealService.findPage(pageBean);
    }

    /**
     * 图片上传
     * @param imgFile
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_EDIT')")
    @RequestMapping("/upload.do")
    public Result uploadImg(@RequestParam("imgFile") MultipartFile imgFile){
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //获取文件的扩展名
            String extension = FilenameUtils.getExtension(originalFilename);
            //生成新的文件名，带扩展
            String fileName = UUID.randomUUID().toString() + "." + extension;
            //调用阿里云上传图片
            AliYunUtil.uploadByBytes(imgFile.getBytes(),fileName);
            //上传成功后将图片存入Redis的set集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //返回结果
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 添加检查套餐
     * @param setmeal
     * @param checkgroupIds
     * @return
     */
    @PreAuthorize("hasAuthority('SETMEAL_ADD')")
    @RequestMapping("/addMeal.do")
    public Result addGroup(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        try {
            setMealService.addMeal(setmeal,checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

}
