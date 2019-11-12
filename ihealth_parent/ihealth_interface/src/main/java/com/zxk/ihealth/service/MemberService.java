package com.zxk.ihealth.service;

import com.zxk.ihealth.domain.Member;
import com.zxk.ihealth.entity.Result;

import java.util.Map;

public interface MemberService {
    Result login(Map map);

    Member findByTelephone(String telephone);

    void add(Member member);

    Long findCountByMonth(String month);
}
