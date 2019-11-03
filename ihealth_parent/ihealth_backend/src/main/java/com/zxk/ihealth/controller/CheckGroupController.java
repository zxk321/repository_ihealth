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

import java.util.List;

@Controller
@RequestMapping("/checkGroup")
@ResponseBody
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/addGroup.do")
    public Result addGroup(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.addGroup(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    /**
     * 分页查询检查组
     * @param pageBean
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean pageBean){
        //System.out.println(pageBean);
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(pageBean);
        //System.out.println(pageResult.getRows().get(0).getCode());
        return pageResult;
    }

    /**
     * 根据id查询检查组
     * @param id
     * @return
     */
    @RequestMapping("/findById.do")
    public Result findItemById(Integer id){
        try {
            CheckGroup checkGroup = checkGroupService.findGroupById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据CheckGroup的id查询关联的检查项ids
     * @param id
     * @return
     */
    @RequestMapping("/findRelationById.do")
    public Result findRelationById(Integer id){
        try {
            List<Integer> checkItemIds = checkGroupService.findRelationById(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 修改检查组信息
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/updateGroup.do")
    public Result updateGroup(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.updateGroup(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
}
