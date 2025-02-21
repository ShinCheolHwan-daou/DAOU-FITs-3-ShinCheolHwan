<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>
    <!-- user define javascript -->
    <script src="index.js"></script>
    <title>로그인</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f4f4f4;
            font-family: Arial, sans-serif;
        }

        .login-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            text-align: center;
            width: 300px;
        }

        .login-container h2 {
            margin-bottom: 20px;
        }

        .login-container input {
            width: 80%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .login-container input[type="button"] {
            background-color: #007bff;
            color: white;
            border: none;
            cursor: pointer;
        }

        .login-container input[type="button"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>로그인</h2>
    <input id="id" type="text" name="id" placeholder="ID" required/>
    <input id="password" type="password" name="password" placeholder="PW" required/>
    <input type="button" value="로그인" onclick="login()"/>
</div>
</body>
</html>
