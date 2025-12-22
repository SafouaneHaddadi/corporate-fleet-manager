<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Vehicle Details</title>
</head>
<body>
<h1>Vehicle details</h1>
<c:if test="${not empty vehicle}"> <!-- vehicle vient de la servlet-->
    <p><strong> Brand:</strong> ${vehicle.brand}</p>
    <p><strong>Model:</strong> ${vehicle.model}</p>
    <p><strong>License Plate:</strong> ${vehicle.licensePlate}</p>
    <p><strong>Year:</strong> ${vehicle.year}</p>
    <p><strong>Mileage:</strong> ${vehicle.mileage} km</p>
    <p><strong>Status:</strong>
        <c:choose>
        <c:when test="${vehicle.status == 'AVAILABLE'}">Available</c:when>
        <c:when test="${vehicle.status == 'MAINTENANCE'}">Maintenance</c:when>
            <c:otherwise>${vehicle.status}</c:otherwise>
    </c:choose>
</c:if>

<a href="${pageContext.request.contextPath}/vehicles?action=list">Back to list</a>

</body>
</html>
