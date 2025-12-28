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

        .search-form {
            margin-top: 10px;
        }

        .search-form input[type=text] {
            padding: 6px;
            width: 200px;
            border: 1px solid #ccc;
        }

        .search-form input[type=submit] {
            padding: 6px 12px;
            background: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .delete-btn {
            background: #dc3545;
            color: white;
            padding: 4px 8px;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            margin-left: 8px;
        }

        .edit-link {
            padding: 4px 10px;
            background: #ffc107;
            color: #212529;
            text-decoration: none;
            border-radius: 4px;
            font-size: 14px;
        }

        .manager-note {
            background: #fff3cd;
            padding: 10px;
            margin: 10px 0;
            border-left: 3px solid #ffc107;
        }
        .manager-note p {
            margin: 0;
            font-style: italic;
            color: #856404;
        }


    </style>
</head>
<body>
<h1>Vehicle Management</h1>

<c:if test="${not empty loggedUser}">
    <p>
        Welcome back, <strong>${loggedUser.username}!</strong>
    </p>

    <c:if test="${loggedUser.role == 'MANAGER'}">
        <div class="manager-note">
            <p>
                With great power comes great responsibility...<br>
                Remember: A good manager leads by example.
            </p>
        </div>
    </c:if>
</c:if>

<div class="menu">
    <a href="${pageContext.request.contextPath}/vehicles?action=available">
        Available Vehicles
    </a>
    <c:if test="${loggedUser.role == 'MANAGER'}">
        <a href="${pageContext.request.contextPath}/vehicles?action=list">
            All Vehicles
        </a>
        <a href="${pageContext.request.contextPath}/vehicles?action=create">
            Add Vehicle
        </a>
        <a href="${pageContext.request.contextPath}/reservations?action=list">
            Manage Reservations
        </a>
    </c:if>
    <c:if test="${loggedUser.role == 'EMPLOYEE'}">
        <a href="${pageContext.request.contextPath}/reservations?action=my">
            My Reservations
        </a>
    </c:if>
</div>

<form  class="search-form" action="${pageContext.request.contextPath}/vehicles" method = "get">
    <input type="hidden" name="action" value="search" />
    <input type="text" name="search" placeholder="Search by brand"
           value="${brand != null ? brand: ''}"/> <!-- si on a déjà cherché qlq chose, ça pré-remplit -->
    <input type="submit" value="Search"/>
</form>

<c:if test="${not empty loggedUser}">
    <div style="position: absolute; top: 10px; right: 20px;">
        <a href="${pageContext.request.contextPath}/users?action=logout">
            Logout
        </a>
    </div>
</c:if>

<c:if test="${empty loggedUser}">
    <div>
        <p>You are viewing the list of available vehicles.</p>
        <p>
            <a href="${pageContext.request.contextPath}/users?action=login">
                Log in to view details and reserve a vehicle
            </a>
        </p>
    </div>
</c:if>

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
                    <td>
                        <c:if test="${not empty loggedUser}">
                            <a href="${pageContext.request.contextPath}/vehicles?action=view&id=${v.id}">
                                View details
                            </a>
                        </c:if>
                        <c:if test="${loggedUser.role=='MANAGER'}">
                            <form action="${pageContext.request.contextPath}/vehicles" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="id" value="${v.id}"/>
                                <input type="submit"
                                       value="Delete"
                                       class="delete-btn"
                                       onclick="return confirm('Are you sure you want to delete this vehicle ?');"/>
                            </form>
                            <a class="edit-link" href = "${pageContext.request.contextPath}/vehicles?action=edit&id=${v.id}">
                                Edit
                            </a>
                        </c:if>
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