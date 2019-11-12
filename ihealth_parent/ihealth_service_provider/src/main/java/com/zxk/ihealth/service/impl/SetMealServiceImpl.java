package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxk.ihealth.constant.RedisConstant;
import com.zxk.ihealth.dao.SetMealDao;
import com.zxk.ihealth.domain.Setmeal;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询所有的检查套餐
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {
        return setMealDao.getSetmeal();
    }

    /**
     * 根基id查询检查套餐
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    /**
     * 分页展示检查套餐
     * @param pageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        //设置分页查询参数
        PageHelper.startPage(pageBean.getCurrentPage(),pageBean.getPageSize());
        //调用dao查询
        Page<Setmeal> page = setMealDao.findByCondition(pageBean);
        //返回分页结果
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void addMeal(Setmeal setmeal, Integer[] checkgroupIds) {
        //调用dao层添加检查套餐
        setMealDao.addMeal(setmeal);
        //设置检查套餐与检查组的关联关系
        this.setMealGroupRal(setmeal.getId(),checkgroupIds);
        //检查套餐添加成功后，将图片的名称存入Redis的set集合中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
    }

    /**
     * 设置检查套餐与检查组的关联关系
     * @param setmealId
     * @param checkgroupIds
     */
    public void setMealGroupRal(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds !=null && checkgroupIds.length>0){
            for (Integer checkgroupId : checkgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setMealDao.setMealGroupRal(map);
            }
        }
    }

}
