package com.example.myboardproject.controller;

import com.example.myboardproject.myjson.MyJsonParser;
import com.example.myboardproject.service.BoardLikeService;
import com.example.myboardproject.service.BoardService;
import com.example.myboardproject.service.CommentService;
import com.example.myboardproject.vo.BoardVO;
import com.example.myboardproject.vo.CommentVO;
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

@WebServlet(name = "boardDetail", value = "/boards/detail")
public class BoardDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        board.setViewCount(board.getViewCount() + 1);
        boardService.increaseViewCount(boardId);
        req.setAttribute("board", board);

        CommentService commentService = new CommentService();
        List<CommentVO> comments = commentService.getCommentsByBoardId(boardId);
        req.setAttribute("comments", comments);

        BoardLikeService boardLikeService = new BoardLikeService();
        boolean boardLike = boardLikeService.checkBoardLike(boardId, member.getId());
        req.setAttribute("boardLike", boardLike);


        RequestDispatcher view = req.getRequestDispatcher("boardDetail.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validation
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        // service
        Map<String, Object> requestData = MyJsonParser.jsonToMap(req.getReader());
        int boardId = Integer.parseInt(req.getParameter("boardId"));
        String title = (String) requestData.get("title");
        String content = (String) requestData.get("content");

        BoardService boardService = new BoardService();
        boolean result = boardService.updateBoard(boardId, title, content, member.getId());

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", result ? "success" : "fail");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // validation
        MemberVO member = (MemberVO) req.getSession().getAttribute("member");
        if (member == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required");
            return;
        }

        // service
        int boardId = Integer.parseInt(req.getParameter("boardId"));
        BoardService boardService = new BoardService();
        boolean result = boardService.deleteBoard(boardId, member.getId());

        // response
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", result ? "success" : "fail");
        resp.getWriter().write(MyJsonParser.mapToJson(responseMap));
    }
}
