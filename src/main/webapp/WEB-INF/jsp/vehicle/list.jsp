<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Vehicle Management</title>
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

        .menu {
            margin: 20px 0;
            padding: 15px;
            background: #f8f9fa;
            border: 1px solid #dee2e6;
        }

        .menu a {
            margin-right: 15px;
            padding: 8px 16px;
            background: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }

        .menu a:hover {
            background: #0056b3;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th {
            background: #343a40;
            color: white;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #dee2e6;
        }

        tr:nth-child(even) {
            background: #f8f9fa;
        }

        .available {
            color: #28a745;
            font-weight: bold;
        }

        .maintenance {
            color: #dc3545;
            font-weight: bold;
        }

        .empty-message {
            text-align: center;
            padding: 40px;
            color: #6c757d;
            font-size: 18px;
        }

        .count {
            margin-top: 15px;
            padding: 10px;
            background: #e9ecef;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h1>Vehicle Management</h1>

<div class="menu">
    <a href="${pageContext.request.contextPath}/vehicles?action=available">
        Available Vehicles
    </a>
    <a href="${pageContext.request.contextPath}/vehicles?action=list">
        All Vehicles
    </a>
</div>

<c:choose>
    <c:when test="${not empty vehicles}">
        <table>
            <tr>
                <th>Brand</th>
                <th>Model</th>
                <th>License Plate</th>
                <th>Year</th>
                <th>Mileage</th>
                <th>Status</th>
            </tr>
            <c:forEach var="v" items="${vehicles}">
                <tr>
                    <td><strong>${v.brand}</strong></td>
                    <td>${v.model}</td>
                    <td><strong>${v.licensePlate}</strong></td>
                    <td>${v.year}</td>
                    <td>${v.mileage} km</td>
                    <td>
                        <c:choose>
                            <c:when test="${v.status == 'AVAILABLE'}">
                                <span class="available">Available</span>
                            </c:when>
                            <c:when test="${v.status == 'MAINTENANCE'}">
                                <span class="maintenance">Maintenance</span>
                            </c:when>
                            <c:otherwise>
                                ${v.status}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>

        <div class="count">
            Total vehicles: ${vehicles.size()}
        </div>
    </c:when>
    <c:otherwise>
        <div class="empty-message">
            No vehicles found in the database.
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>