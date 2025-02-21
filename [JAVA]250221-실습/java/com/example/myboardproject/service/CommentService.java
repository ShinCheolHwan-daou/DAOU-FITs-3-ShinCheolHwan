package com.example.myboardproject.service;

import com.example.myboardproject.dao.BoardDAO;
import com.example.myboardproject.dao.CommentDAO;
import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.CommentVO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CommentService {
    public List<CommentVO> getCommentsByBoardId(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            CommentDAO commentDAO = new CommentDAO(sqlSession);
            return commentDAO.selectCommentsByBoardId(boardId);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public CommentVO getCommentById(int commentId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            CommentDAO commentDAO = new CommentDAO(sqlSession);
            return commentDAO.selectCommentById(commentId);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public boolean addComment(CommentVO comment) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            CommentDAO commentDAO = new CommentDAO(sqlSession);
            if (!commentDAO.insertComment(comment)) {
                sqlSession.rollback();
                return false;
            }
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.increaseBoardCommentCount(comment.getBoardId())) {
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

    public boolean deleteComment(Integer commentId, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            CommentDAO commentDAO = new CommentDAO(sqlSession);
            CommentVO comment = commentDAO.selectCommentById(commentId);
            if (comment == null || !comment.getWriter().getId().equals(memberId)) {
                return false;
            }
            if (!commentDAO.deleteComment(commentId)) {
                sqlSession.rollback();
                return false;
            }
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.decreaseBoardCommentCount(comment.getBoardId())) {
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
