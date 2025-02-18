<%@ page import="com.example.myboardproject.vo.MemberVO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.myboardproject.vo.BoardVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시판</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            text-align: center;
        }

        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
        }

        th {
            background-color: #f4f4f4;
        }

        tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        h1 {
            color: #333;
        }
    </style>
</head>
<%
    MemberVO member = (MemberVO) request.getSession().getAttribute("member");
    List<BoardVO> boards = (List<BoardVO>) request.getAttribute("boards");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<body>
<h1><%=member.getName()%>님, 환영합니다</h1>
<button onclick="location.href='/boards/create'">글쓰기</button>
<table>
    <thead>
    <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
    </tr>
    </thead>
    <tbody>
    <% for (BoardVO board : boards) { %>
    <tr onclick="location.href='/boards/detail?boardId=<%=board.getId()%>'">
        <td><%= board.getId() %>
        </td>
        <td><%= board.getTitle() %>
        </td>
        <td><%= board.getWriter().getName() %>
        </td>
        <td><%= sdf.format(board.getCreatedAt()) %>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>
</body>
</html>
