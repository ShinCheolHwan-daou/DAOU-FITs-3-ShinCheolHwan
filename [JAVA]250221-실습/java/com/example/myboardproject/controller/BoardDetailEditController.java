package com.example.myboardproject.controller;

import com.example.myboardproject.service.BoardService;
import com.example.myboardproject.vo.BoardVO;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "boardDetailEdit", value = "/boards/detail/edit")
public class BoardDetailEditController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendRedirect("/");
            return;
        }
        int boardId = Integer.parseInt(req.getParameter("boardId"));
        BoardService boardService = new BoardService();
        BoardVO board = boardService.getBoardById(boardId);
        if (!board.getWriter().getId().equals(member.getId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Cannot edit");
            return;
        }
        req.setAttribute("board", board);
        RequestDispatcher view = req.getRequestDispatcher("boardDetailEdit.jsp");
        view.forward(req, resp);
    }
}
