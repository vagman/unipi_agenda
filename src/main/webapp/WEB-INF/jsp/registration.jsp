<%--
  Created by IntelliJ IDEA.
  User: Ilias Brinias
  Date: 23/4/2021
  Time: 1:21 μ.μ.
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Registration</h1>
<%--@elvariable id="user" type="com.exc.unipi_agenda.model.User"--%>
<form:form modelAttribute="user">
    <table>
        <tr>
            <td>
                Name:
                .
            </td>
        </tr>
        <tr>
            <td>
                <form:input path="firstName"/>
            </td>
        </tr>
        <tr>
            <td>
                <form:input path="lastName"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Add Registration">
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
