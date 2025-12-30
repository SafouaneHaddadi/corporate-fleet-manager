<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create Maintenance</title>

</head>
<body>
<h1>Create Maintenance</h1>

<c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
</c:if>

<c:if test="${not empty vehicle}">
    <div>
        Vehicle: <strong>${vehicle.brand} ${vehicle.model}</strong> - Plate: <strong>${vehicle.licensePlate}</strong>
    </div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/maintenances">
    <input type="hidden" name="action" value="create"/>
    <input type="hidden" name="vehicleId" value="${vehicle.id}"/>

    <label>Start date and time:</label><br/>
    <input type="datetime-local" name="startDate" required/><br/><br/>

    <label>End date and time:</label><br/>
    <input type="datetime-local" name="endDate" required/><br/><br/>

    <label>Description:</label><br/>
    <textarea name="description" rows="4" cols="50" required></textarea><br/><br/>

    <input type="submit" value="Create Maintenance"/>
    <br>
    <a href="${pageContext.request.contextPath}/vehicles?action=list">
        Cancel
    </a>
</form>
</body>
</html>