<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>All Maintenances</title>

</head>
<body>
<h1>All Maintenances</h1>

<c:choose>
    <c:when test="${not empty maintenances}">
        <table>
            <tr>
                <th>Vehicle</th>
                <th>Start</th>
                <th>End</th>
                <th>Description</th>
            </tr>
            <c:forEach var="m" items="${maintenances}">
                <tr>
                    <td>${m.vehicle.brand} ${m.vehicle.model} (${m.vehicle.licensePlate})</td>
                    <td>${m.startDate}</td>
                    <td>${m.endDate}</td>
                    <td>${m.description}</td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p style="text-align: center;">No maintenance planned.</p>
    </c:otherwise>
</c:choose>

<div>
    <a href="${pageContext.request.contextPath}/vehicles?action=list">
         Back to vehicles
    </a>
</div>
</body>
</html>