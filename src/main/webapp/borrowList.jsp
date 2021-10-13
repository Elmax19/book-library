<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Borrow List</title>
</head>
<body>
<c:if test="${not empty borrows}">
    <div class="table-responsive">
        <table class="table table-striped table-sm" border="2">
            <thead>
            <tr>
                <th>Reader Email</th>
                <th>Reader Name</th>
                <th>Borrow Date</th>
                <th>Due Date</th>
                <th>Return Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="borrow" items="${requestScope.borrows}">
                <tr>
                    <td>${borrow.reader.email}</td>
                    <td>${borrow.reader.firstName} ${borrow.reader.lastName}</td>
                    <td>${borrow.borrowDate}</td>
                    <td>${borrow.borrowDate.plusMonths(borrow.duration)}</td>
                    <td>${borrow.returnDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</c:if>
</body>
</html>