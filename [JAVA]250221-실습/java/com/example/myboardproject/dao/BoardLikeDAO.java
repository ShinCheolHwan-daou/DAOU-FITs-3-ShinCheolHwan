package com.example.myboardproject.dao;

import com.example.myboardproject.vo.BoardLikeVO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class BoardLikeDAO {
    private SqlSession sqlSession;

    public BoardLikeDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public BoardLikeVO getBoardLikeByPk(int boardId, String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("memberId", memberId);
        return sqlSession.selectOne("com.example.mappers.boardlikes.getBoardLikeByPk", map);
    }

    public boolean upsertBoardLike(int boardId, String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("memberId", memberId);
        return sqlSession.insert("com.example.mappers.boardlikes.upsertBoardLike", map) == 1;
    }

    public boolean deleteBoardLike(int boardId, String memberId) {
        Map<String, Object> map = new HashMap<>();
        map.put("boardId", boardId);
        map.put("memberId", memberId);
        return sqlSession.update("com.example.mappers.boardlikes.deleteBoardLike", map) == 1;
    }
}
