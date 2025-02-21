package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;
import com.example.myboardproject.service.CommentService;
import com.example.myboardproject.vo.CommentVO;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "boardComment", value = "/boards/detail/comments")
public class BoardCommentController extends HttpServlet {

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
        int boardId = (Integer) requestData.get("boardId");
        String content = (String) requestData.get("content");
        CommentVO newComment = new CommentVO(content, boardId, member);
        CommentService commentService = new CommentService();
        boolean result = commentService.addComment(newComment);

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", result ? "success" : "fail");
        if (result) {
            newComment = commentService.getCommentById(newComment.getId());
            newComment.getWriter().setPassword(null);
            responseMap.put("comment", newComment);
        }
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
