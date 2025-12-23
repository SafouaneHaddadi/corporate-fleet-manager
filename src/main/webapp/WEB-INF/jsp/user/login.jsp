<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Login</title>
</head>
<body>

<h2>Login</h2>

<c:if test="${not empty errorMessage}">
    <p style="color:red">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/users" method="post">

    <input type="hidden" name="action" value="login">

    Username:<br>
    <input type="text" name="username"
           value="${user.username}">
    <br><br>

    Password:<br>
    <input type="password" name="password">
    <br><br>

    <button type="submit">Login</button>
</form>

<p>
    Don't already have an account ?
    <a href="${pageContext.request.contextPath}/users?action=register">
        Register
    </a>
</p>

</body>
</html>
