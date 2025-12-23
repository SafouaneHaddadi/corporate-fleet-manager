<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add/Edit a vehicle</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            line-height: 1.6;
        }

        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .error {
            color: #dc3545;
            padding: 10px;
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin-bottom: 15px;
        }

        input[type="text"],
        input[type="number"] {
            padding: 8px;
            margin: 5px 0 15px 0;
            width: 250px;
            border: 1px solid #ced4da;
            border-radius: 4px;
        }

        input[type="submit"] {
            background: #28a745;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }

        input[type="submit"]:hover {
            background: #218838;
        }

        .cancel {
            display: inline-block;
            margin-top: 15px;
            padding: 8px 16px;
            background: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .cancel:hover {
            background: #5a6268;
        }
    </style>
</head>
<body>

<h1>
    <c:choose>
        <c:when test="${vehicle.id != null}">
            Edit vehicle
        </c:when>
        <c:otherwise>
            Add a new vehicle
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/vehicles">

    <input type="hidden" name="action" value="${vehicle.id !=null ? 'update' : 'create'}"/>

    <c:if test="${vehicle.id != null}">
        <input type="hidden" name="id" value="${vehicle.id}"
    </c:if>

    Brand: <br/>
    <input type="text" name="brand" value="${vehicle.brand}"/><br/>

    Model: <br/>
    <input type="text" name="model" value="${vehicle.model}"/><br/>

    License plate: <br/>
    <input type="text" name="licensePlate" value="${vehicle.licensePlate}"/><br/>

    Year: <br/>
    <input type="number" name="year" value="${vehicle.year}"/><br/>

    Mileage: <br/>
    <input type="number" name="mileage" value="${vehicle.mileage}"/><br/>

    <br/>

    <input type="submit" value="Save"/>
</form>

<a href="${pageContext.request.contextPath}/vehicles?action=list" class="cancel">
    Cancel
</a>
</body>
</html>