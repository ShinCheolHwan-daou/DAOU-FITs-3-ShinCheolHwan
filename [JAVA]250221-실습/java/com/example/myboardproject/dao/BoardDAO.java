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

    public List<BoardVO> selectBoardsByKeyword(String keyword) {
        return sqlSession.selectList("com.example.mappers.boards.selectBoardsByKeyword", "%" + keyword + "%");
    }

    public List<BoardVO> selectBoardsByTitleKeyword(String keyword) {
        return sqlSession.selectList("com.example.mappers.boards.selectBoardsByTitleKeyword", "%" + keyword + "%");
    }

    public List<BoardVO> selectBoardsByContentKeyword(String keyword) {
        return sqlSession.selectList("com.example.mappers.boards.selectBoardsByContentKeyword", "%" + keyword + "%");
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

    public Boolean increaseBoardViewCount(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.increaseBoardViewCount", boardId) == 1;
    }

    public Boolean increaseBoardCommentCount(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.increaseBoardCommentCount", boardId) == 1;
    }

    public Boolean decreaseBoardCommentCount(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.decreaseBoardCommentCount", boardId) == 1;
    }

    public Boolean increaseBoardLikeCount(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.increaseBoardLikeCount", boardId) == 1;
    }

    public Boolean decreaseBoardLikeCount(Integer boardId) {
        return sqlSession.update("com.example.mappers.boards.decreaseBoardLikeCount", boardId) == 1;
    }

    public Boolean updateBoard(Integer boardId, String title, String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("boardId", boardId);
        params.put("title", title);
        params.put("content", content);
        return sqlSession.update("com.example.mappers.boards.updateBoard", params) == 1;
    }
}
