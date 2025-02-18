package com.example.myboardproject.controller;

import com.example.myboardproject.service.BoardService;
import com.example.myboardproject.vo.BoardVO;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "boardDelete", value = "/boards/delete")
public class BoardDeleteController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        MemberVO member = (MemberVO) session.getAttribute("member");
        if (member == null) {
            resp.sendRedirect("/");
            return;
        }

        BoardService boardService = new BoardService();
        Integer boardId = Integer.parseInt(req.getParameter("boardId"));
        BoardVO board = boardService.getBoardById(boardId);
        if (board == null) {
            resp.sendRedirect("/boards");
            return;
        }

        if (!member.getId().equals(board.getWriter().getId())) {
            resp.sendRedirect("/boards");
            return;
        }

        boolean result = boardService.deleteBoardById(boardId);
        resp.sendRedirect("/boards");
    }
}
