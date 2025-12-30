<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Reservations</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
        }
        a {
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            background-color: white;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #343a40;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .pending { color: #e67e22; font-weight: bold; }
        .approved { color: #27ae60; font-weight: bold; }
        .refused { color: #c0392b; font-weight: bold; }
        .refusal {
            color: #c0392b;
            font-style: italic;
            display: block;
            margin-top: 5px;
        }
        .back {
            display: block;
            text-align: center;
            margin-top: 30px;
            font-size: 1.1em;
        }
        form {
            text-align: center;
            margin: 20px 0;
        }
    </style>
</head>
<body>

<c:choose>
    <c:when test="${my}">
        <h1>My reservations</h1>
    </c:when>
    <c:otherwise>
        <h1>All reservations</h1>
    </c:otherwise>
</c:choose>

<div style="text-align: center; margin-bottom: 20px;">
</div>

<c:if test="${loggedUser.role == 'MANAGER'}">
    <form action="${pageContext.request.contextPath}/reservations" method="get">
        <input type="hidden" name="action" value="searchStatus"/>
        <label>Status :</label>
        <select name="status">
            <option value="">-- all --</option>
            <option value="PENDING">PENDING</option>
            <option value="APPROVED">APPROVED</option>
            <option value="REFUSED">REFUSED</option>
        </select>
        <input type="submit" value="Search"/>
    </form>
</c:if>

<c:choose>
    <c:when test="${not empty reservations}">
        <table>
            <tr>
                <th>Vehicle</th>
                <th>Start</th>
                <th>End</th>
                <th>Reason</th>
                <th>Status</th>
            </tr>
            <c:forEach var="r" items="${reservations}">
                <tr>
                    <td>
                            ${r.vehicle.brand} ${r.vehicle.model} (${r.vehicle.licensePlate})
                    </td>
                    <td>${r.startDate}</td>
                    <td>${r.endDate}</td>
                    <td>${r.reason}</td>
                    <td>
                        <c:choose>
                            <c:when test="${r.status == 'PENDING'}">
                                <span class="pending">${r.status}</span>
                            </c:when>
                            <c:when test="${r.status == 'APPROVED'}">
                                <span class="approved">${r.status}</span>
                            </c:when>
                            <c:when test="${r.status == 'REFUSED'}">
                                <span class="refused">${r.status}</span>
                                <c:if test="${not empty r.refusalReason}">
                                    <span class="refusal">Refused for: ${r.refusalReason}</span>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                ${r.status}
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p style="text-align: center; color: #666;">No reservations found.</p>
    </c:otherwise>
</c:choose>

<div class="back">
    <a href="${pageContext.request.contextPath}/vehicles?action=available">
        Return to the list of vehicles
    </a>
</div>

</body>
</html>