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
}
