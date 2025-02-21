package com.example.myboardproject.service;

import com.example.myboardproject.dao.BoardDAO;
import com.example.myboardproject.mybatis.MyBatisSessionFactory;
import com.example.myboardproject.vo.BoardVO;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class BoardService {
    public List<BoardVO> searchBoards(String searchType, String searchText) {
        List<BoardVO> boards = new ArrayList<BoardVO>();

        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        BoardDAO boardDAO = new BoardDAO(sqlSession);
        if (searchType == null || searchType.isEmpty() || searchText == null || searchText.isEmpty()) {
            boards = boardDAO.selectAllBoards();
        } else if (searchType.equals("1")) {
            boards = boardDAO.selectBoardsByKeyword(searchText);
        } else if (searchType.equals("2")) {
            boards = boardDAO.selectBoardsByTitleKeyword(searchText);
        } else if (searchType.equals("3")) {
            boards = boardDAO.selectBoardsByContentKeyword(searchText);
        }
        return boards;
    }

    public List<BoardVO> getAllBoard() {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            return boardDAO.selectAllBoards();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public BoardVO getBoardById(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            return boardDAO.selectBoardById(boardId);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    public Boolean deleteBoardById(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.deleteBoardById(boardId)) {
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

    public Boolean deleteBoard(int boardId, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            BoardVO board = boardDAO.selectBoardById(boardId);

            if (!board.getWriter().getId().equals(memberId)) {
                return false;
            }

            if (!boardDAO.deleteBoardById(boardId)) {
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


    public Boolean createBoard(String title, String content, String writerId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.insertBoard(title, content, writerId)) {
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

    public Boolean increaseViewCount(Integer boardId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            if (!boardDAO.increaseBoardViewCount(boardId)) {
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

    public Boolean updateBoard(Integer boardId, String title, String content, String memberId) {
        SqlSession sqlSession = MyBatisSessionFactory.getSqlSessionFactory().openSession();
        try {
            BoardDAO boardDAO = new BoardDAO(sqlSession);
            BoardVO board = boardDAO.selectBoardById(boardId);
            if (board == null || !board.getWriter().getId().equals(memberId)) {
                return false;
            }
            if (!boardDAO.updateBoard(boardId, title, content)) {
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
