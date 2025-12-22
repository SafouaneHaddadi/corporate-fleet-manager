<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Vehicles</title>
</head>
<body>
<h1>Vehicle list</h1>

<a href="${pageContext.request.contextPath}/vehicles?action=available">Show available vehicles</a>
<br>
<a href="${pageContext.request.contextPath}/vehicles?action=list">Show all vehicles</a>

<br><br>

<c:choose>
    <c:when test="${not empty vehicles}">
        <table border="1">
            <tr>
                <th>Brand</th>
                <th>Model</th>
                <th>License plate</th>
                <th>Year</th>
                <th>Mileage</th>
                <th>Status</th>
            </tr>
            <c:forEach var="v" items="${vehicles}">
                <tr>
                    <td>${v.brand}</td>
                    <td>${v.model}</td>
                    <td>${v.licensePlate}</td>
                    <td>${v.year}</td>
                    <td>${v.mileage}</td>
                    <td>
                        <c:choose>
                            <c:when test="${v.status == 'AVAILABLE'}">
                                <span style="color: green;">Available</span>
                            </c:when>
                            <c:when test="${v.status == 'MAINTENANCE'}">
                                <span style="color: red;">In maintenance</span>
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p><strong>No vehicles found</strong></p>
    </c:otherwise>
</c:choose>

</body>
</html>