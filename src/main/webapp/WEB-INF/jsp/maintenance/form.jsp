<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Create Maintenance</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h1 {
            color: #2c3e50;
        }
        .error {
            color: #dc3545; font-weight:
                bold; margin-bottom: 15px;
        }
        input[type="datetime-local"], textarea {
            padding: 8px;
            margin: 5px 0 15px 0;
            width: 300px;
            border: 1px solid #ced4da;
            border-radius: 4px;
        }
        input[type="submit"] {
            background: #dc3545;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .cancel {
            margin-left: 20px;
            padding: 10px 20px;
            background: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .vehicle-info {
            background: #e9ecef;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
        }
    </style>

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
    <a href="${pageContext.request.contextPath}/vehicles?action=list" class="cancel">Cancel</a>
</form>
</body>
</html>