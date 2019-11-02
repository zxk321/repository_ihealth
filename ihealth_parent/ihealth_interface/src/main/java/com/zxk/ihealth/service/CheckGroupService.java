package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;

public interface CheckGroupService {

    void addGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult<CheckItem> findPage(QueryPageBean pageBean);
}
