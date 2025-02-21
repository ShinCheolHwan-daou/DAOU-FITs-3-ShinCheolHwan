package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;
import com.example.myboardproject.service.MemberService;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "login", value = "/login")
public class loginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // service
        Map<String, Object> requestData = MyJsonParser.jsonToMap(req.getReader());
        String id = (String) requestData.get("id");
        String password = (String) requestData.get("password");

        MemberService memberService = new MemberService();
        MemberVO member = memberService.login(id, password);
        if (member != null) {
            HttpSession session = req.getSession();
            session.setAttribute("member", member);
        }

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", member != null ? "success" : "fail");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
