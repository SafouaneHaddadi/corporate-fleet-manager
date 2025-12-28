<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Create reservation</title>
</head>
<body>

<h1>Create a reservation</h1>

<c:if test="${not empty errorMessage}">
    <div style="color: red; font-weight: bold;">
            ${errorMessage}
    </div>
</c:if>

<!-- info sur le véhicule réservé -->
<h3>Vehicle</h3>
<p>
    <strong>${vehicle.brand} ${vehicle.model}</strong><br/>
    Plate : <strong>${vehicle.licensePlate}</strong>
</p>

<form method="post" action="${pageContext.request.contextPath}/reservations">

    <input type="hidden" name="action" value="create"/>

    <input type="hidden" name="vehicleId" value="${vehicle.id}"/>

    <label>Start date :</label><br/>
    <input type="datetime-local" name="startDate" required/><br/><br/>

    <label>End date :</label><br/>
    <input type="datetime-local" name="endDate" required/><br/><br/>

    <label>Reason :</label><br/>
    <textarea name="reason"required></textarea><br/><br/>

    <input type="submit" value="Confirm reservation"/>
</form>

<br/>

<a href="${pageContext.request.contextPath}/vehicles?action=available">
    Back to vehicles
</a>

</body>
</html>
