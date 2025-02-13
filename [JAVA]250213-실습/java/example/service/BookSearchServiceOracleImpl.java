package example.service;

import example.dao.BookDAO;
import example.mybatis.MyBatisSessionFactory;
import example.vo.BookVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class BookSearchServiceOracleImpl implements BookSearchService {
    @Override
    public List<BookVO> searchBookByTitleKeyword(String keyword) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        List<BookVO> books = bookDAO.selectByTitleKeyword(keyword);
        sqlSession.close();
        return books;
    }

    @Override
    public boolean createBook(BookVO book) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        boolean result = bookDAO.insert(book) == 1;
        if (result) {
            sqlSession.commit();
        } else {
            sqlSession.rollback();
        }
        sqlSession.close();
        return result;
    }

    @Override
    public boolean updateBook(BookVO book) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        boolean result = bookDAO.update(book) == 1;
        if (result) {
            sqlSession.commit();
        } else {
            sqlSession.rollback();
        }
        sqlSession.close();
        return result;
    }

    @Override
    public boolean deleteBook(String isbn) {
        SqlSessionFactory sqlSessionFactory = MyBatisSessionFactory.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDAO bookDAO = new BookDAO(sqlSession);
        boolean result = bookDAO.delete(isbn) == 1;
        if (result) {
            sqlSession.commit();
        } else {
            sqlSession.rollback();
        }
        sqlSession.close();
        return result;
    }
}
