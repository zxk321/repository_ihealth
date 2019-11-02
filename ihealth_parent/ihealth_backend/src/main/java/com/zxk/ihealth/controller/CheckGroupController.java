package com.zxk.ihealth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zxk.ihealth.constant.MessageConstant;
import com.zxk.ihealth.domain.CheckGroup;
import com.zxk.ihealth.domain.CheckItem;
import com.zxk.ihealth.entity.PageResult;
import com.zxk.ihealth.entity.QueryPageBean;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.CheckGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/checkGroup")
@ResponseBody
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping("/addGroup.do")
    public Result addItem(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.addGroup(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean pageBean){
        //System.out.println(pageBean);
        PageResult<CheckItem> pageResult = checkGroupService.findPage(pageBean);
        //System.out.println(pageResult.getRows().get(0).getCode());
        return pageResult;
    }
}
