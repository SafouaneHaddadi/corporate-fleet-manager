<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Reservation Cancelled</title>

    <style>
        body { font-family: Arial; margin: 40px; }
        h1 { color: #2c3e50; }
        h2 { color: #17a2b8; margin-top: 30px; }
        .reason { color: #dc3545; font-weight: bold; }
        .vehicle-box {
            border: 1px solid #ddd;
            padding: 12px;
            margin: 10px 0;
        }
        .assign-btn {
            background: #28a745;
            color: white;
            padding: 6px 12px;
            text-decoration: none;
            border-radius: 3px;
            display: inline-block;
            margin-top: 8px;
        }
        .back-link { margin-top: 30px; display: block; }
    </style>

</head>
<body>
<h1>Reservation Cancelled</h1>

<p>Reservation #${cancelled.id} has been cancelled.</p>
<p class="reason">Reason: ${cancelled.refusalReason}</p>



<div>
    <strong>Original reservation:</strong><br>
    ${cancelled.vehicle.brand} ${cancelled.vehicle.model} (${cancelled.vehicle.licensePlate})<br>
    From
    <fmt:parseDate value="${cancelled.startDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedStartDate" />
    <fmt:formatDate value="${parsedStartDate}" pattern="dd/MM/yyyy HH:mm" />
    to
    <fmt:parseDate value="${cancelled.endDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedEndDate" />
    <fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy HH:mm" />
    <br>
    Employee: ${cancelled.employee.username}
</div>

<c:if test="${not empty alternatives}">
    <h2>Assign Replacement Vehicle</h2>
    <p>Available vehicles for same period:</p>

    <c:forEach var="v" items="${alternatives}">
        <div class="vehicle-box">
            <strong>${v.brand} ${v.model}</strong> (${v.licensePlate})<br>
            Year: ${v.year} - ${v.mileage} km
            <a href="${pageContext.request.contextPath}/reservations?action=assignReplacement&originalId=${cancelled.id}&newVehicleId=${v.id}"
               class="assign-btn"
               onclick="return confirm('Assign to ${cancelled.employee.username}?');">
                Assign
            </a>
        </div>
    </c:forEach>
</c:if>

<c:if test="${empty alternatives}">
    <p>No alternative vehicle available for this period.</p>
</c:if>

<div>
    <a href="${pageContext.request.contextPath}/reservations?action=list" class="back-link">
        Back to reservations
    </a>
</div>
</body>
</html>