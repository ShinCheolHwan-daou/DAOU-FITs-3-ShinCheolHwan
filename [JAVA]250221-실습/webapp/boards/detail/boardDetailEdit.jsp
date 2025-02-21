<%@ page import="com.example.myboardproject.vo.BoardVO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <script src="boardDetailEdit.js"></script>
    <title>게시글 수정</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 400px;
        }

        h1 {
            text-align: center;
            margin-bottom: 20px;
        }

        input[type="text"], textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 4px;
            border: 1px solid #ddd;
        }

        textarea[name="content"] {
            height: 480px;
            resize: none;
        }

        input[type="button"] {
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 4px;
            background-color: #007bff;
            color: white;
            cursor: pointer;
        }

        input[type="button"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<%
    BoardVO board = (BoardVO) request.getAttribute("board");
%>
<body>
<div class="container">
    <h1>게시글 수정</h1>
    <input type="text" name="title" placeholder="제목을 입력하세요" value="<%=board.getTitle()%>" required/>
    <textarea name="content" placeholder="내용을 입력하세요" rows="5" required><%=board.getContent()%></textarea>
    <input type="button" value="수정" onclick="editBoard(<%=board.getId()%>)"/>
</div>
</body>
</html>
