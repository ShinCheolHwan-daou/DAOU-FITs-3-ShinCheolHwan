<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<form action="/members" method="post">
    ID <input type="text" name="id"/>
    PW <input type="password" name="password"/>
    <input type="submit"/>
</form>
</body>
</html>