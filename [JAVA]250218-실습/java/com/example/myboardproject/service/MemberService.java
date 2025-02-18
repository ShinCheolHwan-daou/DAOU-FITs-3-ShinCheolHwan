package com.example.myboardproject.service;

import com.example.myboardproject.dao.MemberDAO;
import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.MemberVO;
import org.apache.ibatis.session.SqlSession;

public class MemberService {
    public MemberVO login(String id, String password) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        MemberDAO memberDAO = new MemberDAO(sqlSession);
        return memberDAO.selectMemberByIdAndPassword(id, password);
    }
}
