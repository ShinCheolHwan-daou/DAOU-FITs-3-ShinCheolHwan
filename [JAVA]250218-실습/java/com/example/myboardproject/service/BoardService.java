package com.example.myboardproject.service;

import com.example.myboardproject.dao.BoardDAO;
import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.BoardVO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class BoardService {
    public List<BoardVO> getAllBoard() {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        BoardDAO boardDAO = new BoardDAO(sqlSession);
        return boardDAO.selectAllBoards();
    }

    public BoardVO getBoardById(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        BoardDAO boardDAO = new BoardDAO(sqlSession);
        return boardDAO.selectBoardById(boardId);
    }

    public Boolean deleteBoardById(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        BoardDAO boardDAO = new BoardDAO(sqlSession);
        if (!boardDAO.deleteBoardById(boardId)) {
            sqlSession.rollback();
            return false;
        }
        sqlSession.commit();
        return true;
    }

    public Boolean createBoard(String title, String content, String writerId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        BoardDAO boardDAO = new BoardDAO(sqlSession);
        if (!boardDAO.insertBoard(title, content, writerId)) {
            sqlSession.rollback();
            return false;
        }

        sqlSession.commit();
        return true;
    }

}
