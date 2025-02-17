package org.example.booksearch.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.booksearch.dao.BookDAO;
import org.example.booksearch.mybatis.MyBatisSessionFactory;
import org.example.booksearch.vo.BookVO;

import java.util.List;

public class BookServiceOracleImpl implements BookService {
    @Override
    public BookVO getBookByIsbn(String isbn) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        return bookDAO.getBookByIsbn(isbn);
    }

    @Override
    public List<BookVO> getBooksByKeywordAndPrice(String keyword, Integer price) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        List<BookVO> books = bookDAO.getBooksByKeywordAndPrice(keyword, price);
        sqlSession.close();
        return books;
    }
}
