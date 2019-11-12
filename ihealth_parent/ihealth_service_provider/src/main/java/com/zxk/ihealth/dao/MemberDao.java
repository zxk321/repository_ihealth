package com.zxk.ihealth.dao;

import com.zxk.ihealth.domain.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

public interface MemberDao {
    //根据手机号查询member信息
    @Select("select * from t_member where phoneNumber = #{telephone}")
    Member findByTelephone(String telephone);

    @Insert("insert into t_member(fileNumber,name,sex,idCard,phoneNumber,regTime,password,email,birthday,remark) " +
            "values (#{fileNumber},#{name},#{sex},#{idCard},#{phoneNumber},#{regTime},#{password},#{email},#{birthday},#{remark})")
    //将会员的主键id设置回原对象
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void add(Member member);

    //根据月份查询该月份之前的所有会员数量
    @Select("SELECT Count(t_member.id) FROM t_member WHERE DATE_FORMAT(t_member.regTime,'%Y-%m') <= #{month}")
    Long findCountByMonth(String month);

    //查询当日新增会员数量
    @Select("SELECT Count(t_member.id) FROM t_member WHERE DATE_FORMAT(regTime,'%Y-%m-%d') = DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long todayNewCount(Date date);

    //查询会员总数
    @Select("select count(id) from t_member")
    Long findTotalCount();

    //查询本周新增会员总数
    @Select("SELECT Count(t_member.id) FROM t_member WHERE DATE_FORMAT(regTime,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long weekNewCount(Date date);

    //查询本月新增会员总数
    @Select("SELECT Count(t_member.id) FROM t_member WHERE DATE_FORMAT(regTime,'%Y-%m-%d') >= DATE_FORMAT(#{date},'%Y-%m-%d')")
    Long monthNewCount(Date date);
}
