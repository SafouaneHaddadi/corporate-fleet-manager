<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Register</title>
    <style>
        body {
            font-family: Arial; margin: 40px;
        }
        .form input {
            padding: 8px; margin: 5px 0; width: 250px;
        }
        .error {
            color: red; margin: 10px 0;
        }
        button {
            padding: 10px 20px; margin-top: 10px;
        }
    </style>
</head>
<body>

<h2>Registration</h2>

<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<form class="form" action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="action" value="register">

    <div>Username:</div>
    <input type="text" name="username" value="${user.username}">

    <div>Email:</div>
    <input type="email" name="email" value="${user.email}">

    <div>Password:</div>
    <input type="password" name="password">

    <br>
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