package com.example.myboardproject.dao;

import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.MemberVO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MemberDAO {
    private SqlSession sqlSession;

    public MemberDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public MemberVO selectMemberByIdAndPassword(String id, String password) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("password", password);
        return sqlSession.selectOne("com.example.mappers.members.selectMemberByIdAndPassword", map);
    }
}
