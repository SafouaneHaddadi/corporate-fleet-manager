<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Reservations</title>
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

<div>
    <c:if test="${loggedUser.role == 'MANAGER'}">
        <a href="${pageContext.request.contextPath}/reservations?action=list"> All reservations </a>
    </c:if>
</div>

<br/>
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
        <table border="1">
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
                            ${r.status}
                        <c:if test="${r.status == 'REFUSED' && not empty r.refusalReason}">
                            <br><strong>Refused for:</strong> ${r.refusalReason}
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p>No reservations found.</p>
    </c:otherwise>
</c:choose>

<br/>
<a href="${pageContext.request.contextPath}/vehicles?action=available">
    Return to the list of vehicles
</a>

</body>
</html>