package example.service;

import example.vo.BookVO;

import java.util.List;

public interface BookSearchService {
    List<BookVO> searchBookByTitleKeyword(String keyword);

    boolean createBook(BookVO book);

    boolean updateBook(BookVO book);

    boolean deleteBook(String isbn);
}
