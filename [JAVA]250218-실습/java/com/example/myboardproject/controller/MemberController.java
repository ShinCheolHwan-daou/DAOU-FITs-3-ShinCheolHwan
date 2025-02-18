package com.example.myboardproject.controller;

import com.example.myboardproject.service.MemberService;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "members", value = "/members")
public class MemberController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        MemberService memberService = new MemberService();
        MemberVO member = memberService.login(id, password);
        if (member != null) {
            HttpSession session = req.getSession();
            session.setAttribute("member", member);
            resp.sendRedirect("/boards");
        } else {
            resp.sendRedirect("/");
        }
    }
}
