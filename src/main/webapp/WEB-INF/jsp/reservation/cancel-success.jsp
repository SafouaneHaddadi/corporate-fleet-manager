<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Reservation Cancelled</title>

</head>
<body>
<h1>Reservation Cancelled</h1>

<p>
    Reservation #${cancelled.id} has been cancelled for reason: ${cancelled.refusalReason}.
</p>

<div>
    Original reservation:<br>
    <strong>${cancelled.vehicle.brand} ${cancelled.vehicle.model}</strong> (${cancelled.vehicle.licensePlate})<br>
    From ${cancelled.startDate} to ${cancelled.endDate}<br>
    Employee: ${cancelled.employee.username}
</div>

<c:if test="${not empty alternatives}">
    <div>
        <h2>Assign a replacement vehicle</h2>
        <p>Please select one of the available vehicles for the same period:</p>

        <c:forEach var="v" items="${alternatives}">
            <div>
                <strong>${v.brand} ${v.model}</strong> (${v.licensePlate})<br>
                <small>Year: ${v.year} - Mileage: ${v.mileage} km</small>

                <a href="${pageContext.request.contextPath}/reservations?action=assignReplacement&originalId=${cancelled.id}&newVehicleId=${v.id}"
                   onclick="return confirm('Assign this vehicle to ${cancelled.employee.username}?');">
                    Assign this vehicle
                </a>
            </div>
        </c:forEach>
    </div>
</c:if>

<c:if test="${empty alternatives}">
    <p>No alternative vehicle available for this period.</p>
</c:if>

<div>
    <a href="${pageContext.request.contextPath}/reservations?action=list">Back to reservations</a>
</div>
</body>
</html>