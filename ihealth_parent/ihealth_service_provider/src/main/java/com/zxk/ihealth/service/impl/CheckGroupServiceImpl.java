package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxk.ihealth.dao.CheckGroupDao;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void addGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用dao层进行添加
        checkGroupDao.addGroup(checkGroup);
        //添加检查组与检查项的关联关系
        this.setGroupCheckItemRal(checkGroup.getId(),checkitemIds);
    }

    /**
     * 添加检查组与检查项的关联关系
     * @param checkGroupId
     * @param checkitemIds
     */
    public void setGroupCheckItemRal(Integer checkGroupId, Integer[] checkitemIds) {
        //添加检查组与检查项的关联关系
        if(checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkitemId);
                checkGroupDao.setGroupCheckItemRal(map);
            }
        }
    }

    /**
     * 分页查询检查组
     * @param pageBean
     * @return
     */
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean pageBean) {
        //设置分页参数
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //调用到层查询结果集
        Page<CheckGroup> page = checkGroupDao.findByCondition(pageBean);
        //返回分页查询结果对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据CheckGroup的id查询关联的检查项ids
     * @param checkGroupId
     * @return
     */
    @Override
    public List<Integer> findRelationById(Integer checkGroupId) {
        return checkGroupDao.findRelationById(checkGroupId);
    }

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findGroupById(Integer id) {
        return checkGroupDao.findGroupById(id);
    }

    /**
     * 修改检查组信息
     * @param checkGroup
     * @param checkitemIds
     */
    @Override
    public void updateGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        //首先将中间表中检查组与检查项的关联关系删除
        checkGroupDao.deleteGroupCheckItemRal(checkGroup.getId());
        //其次将新的关联关系添加至中间表
        //checkGroupDao.setGroupCheckItemRal2(checkGroup.getId(),checkitemIds);
        this.setGroupCheckItemRal(checkGroup.getId(),checkitemIds);
        //最后将检查组信息更新
        checkGroupDao.updateGroup(checkGroup);
    }
}
