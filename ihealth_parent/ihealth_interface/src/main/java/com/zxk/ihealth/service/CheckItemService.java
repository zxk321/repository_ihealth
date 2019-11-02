package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;

import java.util.List;

public interface CheckItemService {
    public void addItem(CheckItem checkItem);

    PageResult<CheckItem> findPage(QueryPageBean pageBean);

    void deleteById(Integer id);

    CheckItem findItemById(Integer id);

    void updateItemById(CheckItem checkItem);

    List<CheckItem> findAllCheckItem();
}
