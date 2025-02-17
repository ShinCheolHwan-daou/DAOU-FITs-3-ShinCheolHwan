package org.example.booksearch.service;

import org.example.booksearch.vo.BookVO;

import java.util.List;

public interface BookService {
    List<BookVO> getBooksByKeywordAndPrice(String keyword, Integer price);
    BookVO getBookByIsbn(String isbn);
}
