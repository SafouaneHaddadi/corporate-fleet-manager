<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Reservations</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
            background-color: #f9f9f9;
            color: #333;
        }
        h1 {
            color: #2c3e50;
            text-align: center;
        }
        a {
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            background-color: white;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #343a40;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .pending {
            color: #e67e22;
            font-weight: bold;
        }
        .approved {
            color: #27ae60;
            font-weight: bold;
        }
        .refused {
            color: #c0392b;
            font-weight: bold;
        }

        .back {
            display: block;
            text-align: center;
            margin-top: 30px;
            font-size: 1.1em;
        }
        form {
            text-align: center;
            margin: 20px 0;
        }
        .approval-info {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
            padding: 5px;
            background: #f8f9fa;
            border-radius: 4px;
            border-left: 3px solid #28a745;
        }
        .refusal-info {
            font-size: 12px;
            color: #666;
            margin-top: 5px;
            padding: 5px;
            background: #f8f9fa;
            border-radius: 4px;
            border-left: 3px solid #dc3545;
        }
        .reason-box {
            background: #fff3cd;
            border: 1px solid #ffeaa7;
            padding: 8px;
            margin-top: 5px;
            border-radius: 4px;
            font-size: 11px;
            color: #856404;
        }
    </style>
</head>
<body>

<c:if test="${not empty successMessage}">
    <div style="background:#d4edda; color:#155724;">
        ${successMessage}
    </div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div style="background:#f8d7da; color:#721c24;">
         ${errorMessage}
    </div>
</c:if>

<c:choose>
    <c:when test="${my}">
        <h1>My reservations</h1>
    </c:when>
    <c:otherwise>
        <h1>All reservations</h1>
    </c:otherwise>
</c:choose>

<div style="text-align: center; margin-bottom: 20px;">
</div>

<c:if test="${loggedUser.role == 'MANAGER'}">
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
</c:if>

<c:choose>
    <c:when test="${not empty reservations}">
        <table>
            <tr>
                <th>Vehicle</th>
                <th>Start</th>
                <th>End</th>
                <th>Reason</th>
                <th>Status</th>
            </tr>
            <c:forEach var="r" items="${reservations}">
                <tr>
                    <td>
                            ${r.vehicle.brand} ${r.vehicle.model} (${r.vehicle.licensePlate})
                    </td>
                    <td>
                        <fmt:parseDate value="${r.startDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedStartDate" />
                        <fmt:formatDate value="${parsedStartDate}" pattern="dd/MM/yyyy HH:mm" />
                    </td>
                    <td>
                        <fmt:parseDate value="${r.endDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedEndDate" />
                        <fmt:formatDate value="${parsedEndDate}" pattern="dd/MM/yyyy HH:mm" />
                    </td>
                    <td>${r.reason}</td>
                    <td>
                        <c:choose>
                            <c:when test="${r.status == 'PENDING'}">
                                <span class="pending">${r.status}</span>
                                <c:if test="${r.vehicle.status == 'MAINTENANCE'}">
                                    <div>
                                        ⚠️ This vehicle is currently in maintenance.
                                    </div>
                                </c:if>
                            </c:when>
                            <c:when test="${r.status == 'APPROVED'}">
                                <span class="approved">${r.status}</span>
                                <c:if test="${not empty r.approvedBy}">
                                    <div class="approval-info">
                                        <strong>Approved by:</strong> ${r.approvedBy.username}
                                        <c:if test="${not empty r.approvedAt}">
                                            <br/><strong>When:</strong>
                                            <fmt:parseDate value="${r.approvedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedApprovedAt" />
                                            <fmt:formatDate value="${parsedApprovedAt}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:when test="${r.status == 'REFUSED'}">
                                <span class="refused">${r.status}</span>
                                <c:if test="${not empty r.approvedBy}">
                                    <div class="refusal-info">
                                        <strong>✗ Refused by:</strong> ${r.approvedBy.username}
                                        <c:if test="${not empty r.approvedAt}">
                                            <br/><strong>When:</strong>
                                            <fmt:parseDate value="${r.approvedAt}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedApprovedAt" />
                                            <fmt:formatDate value="${parsedApprovedAt}" pattern="dd/MM/yyyy HH:mm" />
                                        </c:if>
                                        <c:if test="${not empty r.refusalReason}">
                                            <div class="reason-box">
                                                <strong>Reason:</strong> "${r.refusalReason}"
                                            </div>
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                ${r.status}
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${loggedUser.role == 'MANAGER' && r.status == 'PENDING' && r.vehicle.status != 'MAINTENANCE'}">
                            <form action="${pageContext.request.contextPath}/reservations" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="approve"/>
                                <input type="hidden" name="id" value="${r.id}"/>
                                <input type="submit" value="Approve"/>
                            </form>

                            <form action="${pageContext.request.contextPath}/reservations" method="get" style="display:inline;">
                                <input type="hidden" name="action" value="declineForm"/>
                                <input type="hidden" name="id" value="${r.id}"/>
                                <input type="submit" value="Decline"/>
                            </form>
                        </c:if>
                        <c:if test="${loggedUser.role == 'MANAGER' && r.status == 'APPROVED'}">
                            <a href="${pageContext.request.contextPath}/reservations?action=cancel&id=${r.id}" onclick="return confirm('Are you sure you want to cancel this reservation ?');">Cancel</a>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p style="text-align: center; color: #666;">No reservations found.</p>
    </c:otherwise>
</c:choose>

<div class="back">
    <a href="${pageContext.request.contextPath}/vehicles?action=available">
        Return to the list of vehicles
    </a>
</div>

</body>
</html>