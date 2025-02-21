package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "logout", value = "/logout")
public class logoutController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "success");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
