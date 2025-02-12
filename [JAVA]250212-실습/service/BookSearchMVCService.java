package booksearchmvc.service;

import booksearchmvc.vo.BookVO;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface BookSearchMVCService {
    ObservableList<BookVO> searchBookByKeyword(String search) throws SQLException;

    boolean deleteBook(String isbn) throws SQLException;

    boolean updateBook(BookVO book) throws SQLException;
}
