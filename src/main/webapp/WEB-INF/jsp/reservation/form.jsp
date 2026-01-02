<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create a reservation</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            line-height: 1.6;
        }
        h1 {
            color: #2c3e50;
            margin-bottom: 20px;
        }
        .error {
            color: #dc3545;
            padding: 10px;
            background: #f8d7da;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            margin-bottom: 15px;
        }
        input[type="text"],
        input[type="number"],
        input[type="datetime-local"],
        textarea {
            padding: 8px;
            margin: 5px 0 15px 0;
            width: 300px;
            border: 1px solid #ced4da;
            border-radius: 4px;
        }
        input[type="submit"] {
            background: #28a745;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }
        input[type="submit"]:hover {
            background: #218838;
        }
        .cancel {
            display: inline-block;
            margin-left: 20px;
            padding: 10px 20px;
            background: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .cancel:hover {
            background: #5a6268;
        }
        .vehicle-info {
            background: #e9ecef;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h1>Create a reservation</h1>

<c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
</c:if>

<c:if test="${not empty vehicle}">
    <div class="vehicle-info">
        Selected vehicle:<br/>
            ${vehicle.brand} ${vehicle.model} - Plate: ${vehicle.licensePlate}
    </div>
</c:if>

<form method="post" action="${pageContext.request.contextPath}/reservations">
    <input type="hidden" name="action" value="create"/>
    <input type="hidden" name="vehicleId" value="${vehicle.id}"/>

    <label>Reason:</label><br/>
    <textarea name="reason" rows="4" cols="50" required>${reservation.reason}</textarea><br/><br/>

    <label>Start date and time:</label><br/>
    <input type="datetime-local" name="startDate" value="${reservation.startDateStr}" required/><br/><br/>

    <label>End date and time:</label><br/>
    <input type="datetime-local" name="endDate" value="${reservation.endDateStr}" required/><br/><br/>

    <input type="submit" value="Reserve"/>
    <a href="${pageContext.request.contextPath}/vehicles?action=available" class="cancel">Cancel</a>
</form>

</body>
</html>