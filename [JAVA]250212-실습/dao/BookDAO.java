package booksearchmvc.dao;

import booksearchmvc.vo.BookVO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BookDAO {

    private Connection conn;

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    public ObservableList<BookVO> select(String text) throws SQLException {
        ObservableList<BookVO> books = FXCollections.observableArrayList();

        // 3. sql 작성
        String sql = "SELECT BISBN, BTITLE, BPRICE, BAUTHOR FROM BOOK WHERE BTITLE LIKE ?";

        // 4. sql 실행
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, "%" + text + "%");

        // 5. 결과 처리
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            BookVO book = new BookVO(
                    rs.getString("BISBN"),
                    rs.getString("BTITLE"),
                    rs.getInt("BPRICE"),
                    rs.getString("BAUTHOR")
            );
            books.add(book);
        }

        pstmt.close();
        return books;
    }

    public boolean delete(String isbn) throws SQLException {
        String sql = "DELETE FROM BOOK WHERE BISBN = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, isbn);
        int result = pstmt.executeUpdate();
        pstmt.close();
        return result == 1;
    }

    public boolean update(BookVO book) throws SQLException {
        String sql = "UPDATE BOOK SET BTITLE = ?, BPRICE = ?, BAUTHOR = ? WHERE BISBN = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, book.getBtitle());
        pstmt.setInt(2, book.getBprice());
        pstmt.setString(3, book.getBauthor());
        pstmt.setString(4, book.getBisbn());
        int result = pstmt.executeUpdate();
        pstmt.close();
        return result == 1;
    }
}
