<%@ page import="com.example.myboardproject.vo.MemberVO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.myboardproject.vo.BoardVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script src="boards.js"></script>
    <title>게시판</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            padding: 20px;
        }

        .container {
            min-width: 800px;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            text-align: center;
        }

        .top-actions {
            display: flex;
            justify-content: space-between;
            margin-bottom: 20px;
        }

        .top-actions button {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            color: white;
            cursor: pointer;
        }

        .top-actions button[name="write-btn"] {
            background-color: #007bff;
        }

        .top-actions button[name="logout-btn"] {
            background-color: #FF4D4D;
        }

        .search-bar {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin-bottom: 20px;
        }

        .search-bar input, select {
            padding: 8px;
            border-radius: 4px;
            border: 1px solid #ddd;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: center;
        }

        th, td {
            padding: 10px;
            border: 1px solid #ddd;
        }

        th {
            background-color: #f1f1f1;
        }

        tr:hover {
            background-color: #f9f9f9;
            cursor: pointer;
        }

        .table-container {
            -ms-overflow-style: none; /* for Internet Explorer, Edge */
            scrollbar-width: none; /* for Firefox */
            overflow-y: scroll;
            max-height: 500px;
            border: 1px solid #ddd;
            border-radius: 4px;
            margin-top: 10px;
        }
    </style>
</head>
<%
    MemberVO member = (MemberVO) request.getSession().getAttribute("member");
    List<BoardVO> boards = (List<BoardVO>) request.getAttribute("boards");
    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
%>
<body>
<div class="container">
    <h1><%=member.getName()%>님, 환영합니다</h1>
    <div class="top-actions">
        <button name="write-btn" onclick="moveToWriteBoard()">글쓰기</button>
        <button name="logout-btn" onclick="logout()">로그아웃</button>
    </div>
    <div class="search-bar">
        <select id="searchType">
            <option value="1">모두</option>
            <option value="2">제목</option>
            <option value="3">내용</option>
        </select>
        <input id="searchText" type="text" placeholder="검색어 입력"/>
        <button onclick="search()">검색</button>
    </div>
    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th>글 번호</th>
                <th>글 제목</th>
                <th>글 작성자</th>
                <th>글 작성일</th>
                <th>댓글 수</th>
                <th>좋아요 수</th>
                <th>글 조회수</th>
            </tr>
            </thead>
            <tbody>
            <% for (BoardVO board : boards) { %>
            <tr onclick="moveToBoardDetail(<%=board.getId()%>)">
                <td><%=board.getId()%>
                </td>
                <td><%=board.getTitle()%>
                </td>
                <td><%=board.getWriter().getName()%>
                </td>
                <td><%=sdf.format(board.getCreatedAt())%>
                </td>
                <td><%=board.getCommentCount()%>
                </td>
                <td><%=board.getLikeCount()%>
                </td>
                <td><%=board.getViewCount()%>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
