<%@ page import="com.example.myboardproject.vo.BoardVO" %>
<%@ page import="com.example.myboardproject.vo.MemberVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 상세</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            text-align: center;
        }

        .container {
            width: 60%;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
            background-color: #f9f9f9;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        .info {
            font-size: 14px;
            color: #666;
            margin-bottom: 10px;
        }

        .content {
            text-align: left;
            background-color: #fff;
            padding: 15px;
            border-radius: 5px;
            border: 1px solid #ddd;
        }

        .btn-delete {
            margin-top: 20px;
        }

        .btn-delete input {
            background-color: red;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 5px;
            cursor: pointer;
        }

        .btn-delete input:hover {
            background-color: darkred;
        }
    </style>
</head>
<%
    MemberVO member = (MemberVO) request.getSession().getAttribute("member");
    BoardVO board = (BoardVO) request.getAttribute("board");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<body>
<div class="container">
    <h1><%= board.getTitle() %>
    </h1>
    <div class="info">작성자: <%= board.getWriter().getName() %> | 작성일: <%= sdf.format(board.getCreatedAt()) %>
    </div>
    <div class="content">
        <p><%= board.getContent() %>
        </p>
    </div>
    <% if (member.getId().equals(board.getWriter().getId())) { %>
    <div class="btn-delete">
        <form action="/boards/delete?boardId=<%= board.getId() %>" method="post">
            <input type="submit" value="삭제">
        </form>
    </div>
    <% } %>
</div>
</body>
</html>
