package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;
import com.example.myboardproject.service.CommentService;
import com.example.myboardproject.vo.MemberVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "boardCommentDetail", value = "/boards/detail/comments/detail")
public class BoardCommentDetailController extends HttpServlet {
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validation
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        // service
        int commentId = Integer.parseInt(req.getParameter("commentId"));
        CommentService commentService = new CommentService();
        boolean result = commentService.deleteComment(commentId, member.getId());

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", result ? "success" : "fail");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
