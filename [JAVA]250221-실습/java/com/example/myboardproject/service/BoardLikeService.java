package com.example.myboardproject.service;

import com.example.myboardproject.dao.BoardDAO;
import com.example.myboardproject.dao.BoardLikeDAO;
import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.BoardLikeVO;
import com.example.myboardproject.vo.BoardVO;
import org.apache.ibatis.session.SqlSession;

public class BoardLikeService {
    public boolean checkBoardLike(int boardId, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardLikeDAO boardLikeDAO = new BoardLikeDAO(sqlSession);
            BoardLikeVO boardLike = boardLikeDAO.getBoardLikeByPk(boardId, memberId);
            return boardLike != null;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public boolean likeBoard(int boardId, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            BoardVO board = boardDAO.selectBoardById(boardId);
            if (board == null) {
                return false;
            }

            if (board.getWriter().getId().equals(memberId)) {
                return false;
            }

            BoardLikeDAO boardLikeDAO = new BoardLikeDAO(sqlSession);
            BoardLikeVO boardLike = boardLikeDAO.getBoardLikeByPk(boardId, memberId);
            if (boardLike != null) {
                return false;
            }

            if (!boardLikeDAO.upsertBoardLike(boardId, memberId)) {
                sqlSession.rollback();
                return false;
            }

            if (!boardDAO.increaseBoardLikeCount(boardId)) {
                sqlSession.rollback();
                return false;
            }
            sqlSession.commit();
            return true;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public boolean deleteLikeBoard(int boardId, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardLikeDAO boardLikeDAO = new BoardLikeDAO(sqlSession);
            if (!boardLikeDAO.deleteBoardLike(boardId, memberId)) {
                sqlSession.rollback();
                return false;
            }

            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.decreaseBoardLikeCount(boardId)) {
                sqlSession.rollback();
                return false;
            }
            sqlSession.commit();
            return true;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
}
