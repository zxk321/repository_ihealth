package com.zxk.ihealth.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zxk.ihealth.dao.MemberDao;
import com.zxk.ihealth.domain.Member;
import com.zxk.ihealth.entity.Result;
import com.zxk.ihealth.service.MemberService;
import com.zxk.ihealth.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result login(Map map) {
        //1.判断用户是否是会员
        //1.1根据电话号码调用memberDao层查询是否存在用户
        Member member = memberDao.findByTelephone((String)map.get("telephone"));
        //1.2判断member
        if (member == null){
            //1.2.1说明不存在此会员，添加该会员
            member.setPhoneNumber((String) map.get("telephone"));//会员电话
            member.setRegTime(new Date());//会员注册日期
            memberDao.add(member);
        }
        return null;
    }

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        if (member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public Long findCountByMonth(String month) {
        return memberDao.findCountByMonth(month);
    }
}
