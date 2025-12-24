<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Corporate Fleet Manager - Welcome</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f4f4f4;
            color: #333;
            text-align: center;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 30px;
        }
        .container {
            max-width: 700px;
            margin: 0 auto;
            background: white;
            padding: 40px;
            border-radius: 8px;
        }
        p {
            font-size: 18px;
            line-height: 1.6;
            margin: 20px 0;
        }
        .btn {
            display: inline-block;
            padding: 12px 25px;
            margin: 15px;
            font-size: 16px;
            text-decoration: none;
            border-radius: 5px;
            background: #007bff;
            color: white;
        }
        .btn:hover {
            background: #0056b3;
        }
        .btn-secondary {
            background: #28a745;
        }
        .btn-secondary:hover {
            background: #218838;
        }
        .footer {
            margin-top: 50px;
            font-size: 14px;
            color: #666;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to Corporate Fleet Manager</h1>

    <p>
        We are a modern company that provides a shared vehicle fleet for all employees.
    </p>

    <p>
        You can easily view available vehicles and reserve one for your business trips.
    </p>

    <p>
        <a href="${pageContext.request.contextPath}/vehicles?action=available" class="btn btn-secondary">
            View Available Vehicles
        </a>
    </p>

    <p>
        To reserve a vehicle or access full features, please create an account.
    </p>

    <p>
        <a href="${pageContext.request.contextPath}/users?action=register" class="btn">
            Register Now
        </a>
    </p>

    <p>
        Already have an account? <a href="${pageContext.request.contextPath}/users?action=login">Log in here</a>
    </p>

    <div class="footer">
        © 2025 Corporate Fleet Manager
    </div>
</div>
</body>
</html>