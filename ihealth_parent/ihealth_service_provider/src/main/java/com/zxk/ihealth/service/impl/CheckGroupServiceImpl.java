package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxk.ihealth.dao.CheckGroupDao;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void addGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用dao层进行添加
        checkGroupDao.addGroup(checkGroup);
        //添加检查组与检查项的关联关系
        if(checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroup.getId());
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setGroupCheckItemRal(map);
            }
        }
    }

    @Override
    public PageResult<CheckItem> findPage(QueryPageBean pageBean) {
        //设置分页参数
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //调用到层查询结果集
        Page<CheckItem> page = checkGroupDao.findByCondition(pageBean);
        //返回分页查询结果对象
        return new PageResult(page.getTotal(),page.getResult());
    }
}
