package org.example.booksearch.dao;

import org.apache.ibatis.session.SqlSession;
import org.example.booksearch.vo.BookVO;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDAO {
    private SqlSession sqlSession;

    public BookDAO() {

    }

    public BookDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<BookVO> getBooksByKeywordAndPrice(String keyword, Integer price) {
        List<BookVO> books = Collections.emptyList();
        Map<String, Object> map = new HashMap<>();
        map.put("keyword", "%" + keyword + "%");
        map.put("price", price);
        try {
            books = sqlSession.selectList("example.MyBook.selectBooksByKeywordAndPrice", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public BookVO getBookByIsbn(String isbn) {
        BookVO book = null;
        try {
            book = sqlSession.selectOne("example.MyBook.selectBookByIsbn", isbn);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }
}
