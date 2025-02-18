package com.example.myboardproject.dao;

import com.example.myboardproject.vo.BoardVO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardDAO {
    SqlSession sqlSession;

    public BoardDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }


    public List<BoardVO> selectAllBoards() {
        return sqlSession.selectList("com.example.mappers.boards.selectAllBoards");
    }

    public BoardVO selectBoardById(Integer boardId) {
        return sqlSession.selectOne("com.example.mappers.boards.selectBoardById", boardId);
    }

    public Boolean deleteBoardById(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.deleteBoardById", boardId) == 1;
    }

    public Boolean insertBoard(String title, String content, String writerId) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("content", content);
        params.put("writerId", writerId);
        return sqlSession.insert("com.example.mappers.boards.insertBoard", params) == 1;
    }
}
