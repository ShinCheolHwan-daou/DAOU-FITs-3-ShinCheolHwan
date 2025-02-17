package org.example.booksearch.controller;

import org.example.booksearch.service.BookService;
import org.example.booksearch.service.BookServiceOracleImpl;
import org.example.booksearch.vo.BookVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "books", value = "/books")
public class BookController extends HttpServlet {

    public BookController() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String isbn = req.getParameter("isbn");
        if (isbn == null) {
            resp.sendError(400, "no keyword");
            return;
        }
        BookService bookService = new BookServiceOracleImpl();
        BookVO book = bookService.getBookByIsbn(isbn);
        if (book == null) {
            resp.sendError(400, "no book found");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Book Search</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<div>Bisbn: " + book.getBisbn() + "</div>");
        out.println("<div>Btitle: " + book.getBtitle() + "</div>");
        out.println("<div>Bdate: " + book.getBdate() + "</div>");
        out.println("<div>Bpage: " + book.getBpage() + "</div>");
        out.println("<div>Bprice: " + book.getBprice() + "</div>");
        out.println("<div>Bauthor: " + book.getBauthor() + "</div>");
        out.println("<div>Btranslator: " + book.getBtranslator() + "</div>");
        out.println("<div>Bpublisher: " + book.getBpublisher() + "</div>");
        out.println("<div>Bimgurl: " + book.getBimgurl() + "</div>");
        out.println("</body>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String keyword = req.getParameter("keyword");
        if (keyword == null) {
            resp.sendError(400, "no keyword");
            return;
        }
        Integer price = null;
        try {
            price = Integer.parseInt(req.getParameter("price"));
        } catch (Exception e) {
        }
        if (price == null) {
            resp.sendError(400, "no price");
            return;
        }

        BookService bookService = new BookServiceOracleImpl();
        List<BookVO> books = bookService.getBooksByKeywordAndPrice(keyword, price);


        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Book Search</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>검색 결과입니다</h1>");
        out.println("<h2>검색 키워드 : " + keyword + "</h2>");
        out.println("<h2>검색가격 : " + price + "</h2>");
        out.println("<ul>");
        for (BookVO book : books) {
            out.println("<li><a href=\"/book-search/books?isbn=" + book.getBisbn() + "\">"
                    + book.getBtitle() + ", " + book.getBprice() +
                    "</a></li>");
        }
        out.println("</ul>");
        out.println("</body>");
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
