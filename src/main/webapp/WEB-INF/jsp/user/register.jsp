<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Inscription</title>
</head>
<body>

<h2>Registration</h2>

<c:if test="${not empty errorMessage}">
    <p style="color:red">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/users" method="post">

    <input type="hidden" name="action" value="register">

    <label>Username :</label><br>
    <input type="text" name="username"
           value="${user.username}">
    <br><br>

    <label>Email :</label><br>
    <input type="email" name="email"
           value="${user.email}">
    <br><br>

    <label>Password :</label><br>
    <input type="password" name="password">
    <br><br>

    <button type="submit">Register</button>
</form>

<p>
    Already have an account ?
    <a href="${pageContext.request.contextPath}/users?action=login">
        Login
    </a>
</p>

</body>
</html>
