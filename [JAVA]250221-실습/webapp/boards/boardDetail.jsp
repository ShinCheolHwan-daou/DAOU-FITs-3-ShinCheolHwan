<%@ page import="com.example.myboardproject.vo.BoardVO" %>
<%@ page import="com.example.myboardproject.vo.MemberVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.example.myboardproject.vo.CommentVO" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script src="boardDetail.js"></script>
    <title>게시글 상세</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            padding: 20px;
            display: flex;
            justify-content: center;
        }

        .container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 600px;
        }

        .title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .meta {
            color: gray;
            font-size: 14px;
            margin-bottom: 20px;
        }

        .meta button {
            background: none;
            border: none;
            cursor: pointer;
        }

        .content {
            margin-bottom: 20px;
        }

        .actions {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }

        .actions input, .comment button {
            padding: 8px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            color: white;
        }

        .actions > input[name="edit-btn"] {
            background-color: #007bff;
        }

        .actions > input[name="delete-btn"], .comment button[name="delete-btn"] {
            background-color: #FF4D4D;
        }

        .comments {
            margin-top: 20px;
        }

        .comment-section {
            margin-top: 30px;
            max-height: 300px;
            -ms-overflow-style: none; /* for Internet Explorer, Edge */
            scrollbar-width: none; /* for Firefox */
            overflow-y: scroll;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 10px;
            background: #fafafa;
        }

        .comment:nth-child(1) {
            border: none;
        }

        .comment {
            border-top: 1px solid #ddd;
            padding: 10px 0;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .comment-meta {
            color: gray;
            font-size: 12px;
        }

        textarea {
            width: 100%;
            padding: 10px;
            border-radius: 4px;
            border: 1px solid #ddd;
            resize: none;
        }

        .comment-input {
            display: flex;
            gap: 10px;
            margin-top: 10px;
        }
    </style>
</head>
<%
    MemberVO member = (MemberVO) request.getSession().getAttribute("member");
    BoardVO board = (BoardVO) request.getAttribute("board");
    List<CommentVO> comments = (List<CommentVO>) request.getAttribute("comments");
    boolean boardLike = (boolean) request.getAttribute("boardLike");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<body>
<div class="container">
    <div class="title"><%= board.getTitle() %>
    </div>
    <div class="meta">작성자: <%= board.getWriter().getName() %> | 작성일: <%= sdf.format(board.getCreatedAt()) %>
    </div>
    <div class="content"><%= board.getContent() %>
    </div>
    <div class="meta">조회수: <%= board.getViewCount() %> | 좋아요: <span id="likeCount"><%= board.getLikeCount() %></span>
        <%if (!board.getWriter().getId().equals(member.getId())) {%>
        <button name="like-btn" style="display:<%= boardLike ? "none" : "inline-block" %>"
                onclick="likeBoard(<%= board.getId() %>)">🤍
        </button>
        <button name="cancel-like-btn" style="display:<%= boardLike ? "inline-block" : "none" %>"
                onclick="cancelLikeBoard(<%= board.getId() %>)">❤️
        </button>
        <%}%>
    </div>
    <div class="actions">
        <% if (member.getId().equals(board.getWriter().getId())) { %>
        <input name="edit-btn" type="button" value="수정" onclick="editBoard(<%= board.getId() %>)"/>
        <input name="delete-btn" type="button" value="삭제" onclick="deleteBoard(<%= board.getId() %>)"/>
        <% } %>
    </div>
    <div class="comments">
        <strong>댓글 (<span id="commentCount"><%= board.getCommentCount() %></span>)</strong>
        <div class="comment-input">
            <textarea name="new-comment" placeholder="내용을 입력하세요" rows="2" required></textarea>
            <input type="button" value="작성" onclick="writeComment(<%= board.getId() %>)"/>
        </div>
        <div class="comment-section">
            <% for (CommentVO comment : comments) { %>
            <div class="comment" id="comment-<%=comment.getId()%>">
                <div>
                    <div><%= comment.getContent() %>
                    </div>
                    <div class="comment-meta"><%= sdf.format(comment.getCreatedAt()) %>
                        | <%= comment.getWriter().getName() %>
                    </div>
                </div>
                <% if (comment.getWriter().getId().equals(member.getId())) { %>
                <button name="delete-btn" onclick="deleteComment(<%= comment.getId() %>)">삭제</button>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
