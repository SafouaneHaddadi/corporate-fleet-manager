<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Vehicle Details</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f8f9fa;
        }

        h1 {
            color: #2c3e50;
        }

        .details {
            background: white;
            padding: 25px;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            width: 400px;
        }

        .details p {
            margin: 10px 0;
        }

        .label {
            font-weight: bold;
            color: #495057;
        }

        .available {
            color: #28a745;
            font-weight: bold;
        }

        .maintenance {
            color: #dc3545;
            font-weight: bold;
        }

        .back {
            margin-top: 20px;
            display: inline-block;
            padding: 8px 14px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .back:hover {
            background: #0056b3;
        }
    </style>
</head>

<body>

<h1>Vehicle Details</h1>

<c:if test="${not empty vehicle}">
    <div class="details">
        <p><span class="label">Brand:</span> ${vehicle.brand}</p>
        <p><span class="label">Model:</span> ${vehicle.model}</p>
        <p><span class="label">License Plate:</span> ${vehicle.licensePlate}</p>
        <p><span class="label">Year:</span> ${vehicle.year}</p>
        <p><span class="label">Mileage:</span> ${vehicle.mileage} km</p>
        <p>
            <span class="label">Status:</span>
            <c:choose>
                <c:when test="${vehicle.status == 'AVAILABLE'}">
                    <span class="available">Available</span>
                </c:when>
                <c:when test="${vehicle.status == 'MAINTENANCE'}">
                    <span class="maintenance">Maintenance</span>
                </c:when>
                <c:otherwise>
                    ${vehicle.status}
                </c:otherwise>
            </c:choose>
        </p>
    </div>
</c:if>

<a class="back" href="${pageContext.request.contextPath}/vehicles?action=available">
    ← Back to list
</a>

</body>
</html>
