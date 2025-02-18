package com.example.myboardproject.controller;

import com.example.myboardproject.service.BoardService;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "boardCreate", value = "/boards/create")
public class BoardCreateController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendRedirect("/");
            return;
        }

        RequestDispatcher view = req.getRequestDispatcher("boardCreate.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendRedirect("/");
            return;
        }

        BoardService boardService = new BoardService();
        boardService.createBoard(
                req.getParameter("title"),
                req.getParameter("content"),
                member.getId()
        );

        resp.sendRedirect("/boards");
    }
}
