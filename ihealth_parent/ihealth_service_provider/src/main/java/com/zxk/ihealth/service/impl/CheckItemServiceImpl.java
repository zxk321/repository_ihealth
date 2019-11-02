package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zxk.ihealth.dao.CheckItemDao;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void addItem(CheckItem checkItem) {
        //调用dao层添加
        checkItemDao.addItem(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean pageBean) {
        //设置分页参数
        PageHelper.startPage(pageBean.getCurrentPage(), pageBean.getPageSize());
        //调用到层查询结果集
        Page<CheckItem> page = checkItemDao.findByCondition(pageBean);
        //返回分页查询结果对象
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void deleteById(Integer id) {
        //根据id查询当前id项是否关联到检查组
        Long count = checkItemDao.findCountById(id);
        //判断count的值
        if (count > 0){
            //说明已经被关联
            throw new RuntimeException("当前检查项已关联至检查组，无法删除！");
        }
        //否则说明检查项没有被关联
        checkItemDao.deleteById(id);
    }

    @Override
    public CheckItem findItemById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void updateItemById(CheckItem checkItem) {
        checkItemDao.updateItem(checkItem);
    }

    @Override
    public List<CheckItem> findAllCheckItem() {
        return checkItemDao.findAll();
    }
}
