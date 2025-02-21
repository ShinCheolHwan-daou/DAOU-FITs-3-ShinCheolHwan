package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;
import com.example.myboardproject.service.BoardService;
import com.example.myboardproject.vo.BoardVO;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "boards", value = "/boards")
public class BoardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        MemberVO member = (MemberVO) session.getAttribute("member");

        if (member == null) {
            resp.sendRedirect("/");
            return;
        }

        String searchType = req.getParameter("searchType");
        String searchText = req.getParameter("searchText");

        BoardService boardService = new BoardService();
        List<BoardVO> boards = boardService.searchBoards(searchType, searchText);
        req.setAttribute("boards", boards);

        RequestDispatcher view = req.getRequestDispatcher("boards.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validation
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        // service
        Map<String, Object> requestData = MyJsonParser.jsonToMap(req.getReader());
        String title = (String) requestData.get("title");
        String content = (String) requestData.get("content");

        BoardService boardService = new BoardService();
        boolean result = boardService.createBoard(
                title,
                content,
                member.getId()
        );

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", result ? "success" : "fail");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
