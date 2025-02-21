package com.example.myboardproject.dao;

import com.example.myboardproject.vo.CommentVO;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentDAO {
    SqlSession sqlSession;

    public CommentDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<CommentVO> selectCommentsByBoardId(Integer boardId) {
        return sqlSession.selectList("com.example.mappers.comments.selectCommentsByBoardId", boardId);
    }

    public CommentVO selectCommentById(int commentId) {
        return sqlSession.selectOne("com.example.mappers.comments.selectCommentById", commentId);
    }

    public boolean insertComment(CommentVO comment) {
        return sqlSession.insert("com.example.mappers.comments.insertComment", comment) == 1;
    }

    public boolean deleteComment(int commentId) {
        return sqlSession.update("com.example.mappers.comments.deleteComment", commentId) == 1;
    }
}
