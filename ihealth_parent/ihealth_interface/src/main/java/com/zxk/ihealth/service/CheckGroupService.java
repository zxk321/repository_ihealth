package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;

import java.util.List;

public interface CheckGroupService {

    void addGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult<CheckGroup> findPage(QueryPageBean pageBean);

    List<Integer> findRelationById(Integer id);

    CheckGroup findGroupById(Integer id);

    void updateGroup(CheckGroup checkGroup, Integer[] checkitemIds);

    List<CheckGroup> findAll();

}
