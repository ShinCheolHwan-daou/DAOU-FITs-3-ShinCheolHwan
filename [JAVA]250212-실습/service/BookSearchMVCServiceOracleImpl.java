package booksearchmvc.service;

import booksearchmvc.dao.BookDAO;
import booksearchmvc.vo.BookVO;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// service에서 Database transaction 지정
public class BookSearchMVCServiceOracleImpl implements BookSearchMVCService {


    public BookSearchMVCServiceOracleImpl() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<BookVO> searchBookByKeyword(String text) throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "C##JAVA_DEV";
        String pw = "1234";
        Connection conn = DriverManager.getConnection(url, id, pw);
        conn.setAutoCommit(false);

        BookDAO bookDAO = new BookDAO(conn);
        ObservableList<BookVO> result = bookDAO.select(text);
        if (result != null) {
            conn.commit();
        } else {
            conn.rollback();
        }
        conn.close();
        return result;
    }

    @Override
    public boolean deleteBook(String isbn) throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "C##JAVA_DEV";
        String pw = "1234";
        Connection conn = DriverManager.getConnection(url, id, pw);
        conn.setAutoCommit(false);

        BookDAO bookDAO = new BookDAO(conn);
        boolean result = bookDAO.delete(isbn);
        if (result) {
            conn.commit();
        } else {
            conn.rollback();
        }
        conn.close();
        return result;
    }

    @Override
    public boolean updateBook(BookVO book) throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String id = "C##JAVA_DEV";
        String pw = "1234";
        Connection conn = DriverManager.getConnection(url, id, pw);
        conn.setAutoCommit(false);

        BookDAO bookDAO = new BookDAO(conn);
        boolean result = bookDAO.update(book);
        if (result) {
            conn.commit();
        } else {
            conn.rollback();
        }
        conn.close();
        return result;
    }
}
