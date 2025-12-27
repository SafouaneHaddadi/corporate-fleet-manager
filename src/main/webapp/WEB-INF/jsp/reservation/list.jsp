<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Reservations</title>
</head>
<body>
<h1>Reservations</h1>

<div>
    <a href="${pageContext.request.contextPath}/reservations?action=list"> All reservations </a>
</div>

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

<c:choose>
    <c:when test="${not empty reservations}">
        <table border="1">
            <tr>
                <th>Vehicle</th>
                <th>Employee</th>
                <th>Start</th>
                <th>End</th>
                <th>Status</th>
                <th>Refusal Reason</th>
            </tr>
            <c:forEach var="r" items="${reservations}">
                <tr>
                    <td>
                            ${r.vehicle.brand} ${r.vehicle.model} (${r.vehicle.licensePlate})
                    </td>
                    <td>${r.employee.username}</td>
                    <td>${r.startDate}</td>
                    <td>${r.endDate}</td>
                    <td>${r.status}</td>
                    <td>
                        <c:if test="${not empty r.refusalReason}">
                            ${r.refusalReason}
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

<a href="${pageContext.request.contextPath}/vehicles?action=list">
    Return
</a>

</body>
</html>