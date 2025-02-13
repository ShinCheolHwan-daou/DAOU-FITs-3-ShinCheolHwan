package example.dao;

import example.vo.BookVO;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

// 이전에는 transaction 처리를 위해 Connection 전달
// 이번에는 SqlSessionFactory 전달
public class BookDAO {
    private SqlSession sqlSession;

    public BookDAO() {
    }

    public BookDAO(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<BookVO> selectByTitleKeyword(String keyword) {
        List<BookVO> bookVOList = null;

        try {
            bookVOList = sqlSession.selectList("example.MyBook.selectByTitleKeyword", "%" + keyword + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookVOList;
    }

    public int insert(BookVO bookVO) {
        int result = 0;

        try {
            result = sqlSession.insert("example.MyBook.insert", bookVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int update(BookVO bookVO) {
        int result = 0;

        try {
            result = sqlSession.update("example.MyBook.update", bookVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int delete(String isbn) {
        int result = 0;

        try {
            result = sqlSession.delete("example.MyBook.delete", isbn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
