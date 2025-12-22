<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Add a vehicle</title>
</head>
<body>
<h1>Add a new vehicle</h1>

<c:if test="${not empty errorMessage}">
    <p style="color:red;"> ${errorMessage}</p>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/vehicles">

    <input type="hidden" name="action" value="create"/>

    Brand: <input type="text" name="brand" value="${vehicle.brand}"/><br/>
    Model: <input type="text" name="model" value="${vehicle.model}"/><br/>
    License plate: <input type="text" name="licensePlate" value="${vehicle.licensePlate}"/><br/>
    Year: <input type="number" name="year" value="${vehicle.year}"/><br/>
    Mileage: <input type="number" name="mileage" value="${vehicle.mileage}"/><br/>

    <br/>

    <input type="submit" value="Save"/>
</form>

<a href="${pageContext.request.contextPath}/vehicles?action=list">Cancel</a>

</body>
</html>
