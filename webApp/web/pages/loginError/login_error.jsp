<%--
  Created by IntelliJ IDEA.
  User: ooria
  Date: 10/4/2020
  Time: 11:40 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <%@page import="sdm.utils.*" %>
    <%@ page import="sdm.constants.Constants" %>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Sign up page - error</title>
</head>
<body>
<div class="container">
    <% String usernameFromSession = SessionUtils.getUsername(request);%>
    <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>
    <h1>Welcome to the Super Duper Market</h1>
    <br/>
    <form method="GET" action="login">
        <fieldset>
            <legend>Sign up:</legend>
            <label for="name">Username:</label><br>
            <input type="text" id="name" name="<%=Constants.USERNAME%>" value="<%=usernameFromParameter%>" required autofocus class=""/><br>

            <label for="userType">User type:</label><br>
            <select id="userType" name="userType">
                <option value="customer">Customer</option>
                <option value="vendor">Vendor</option>
            </select><br><br>

            <input type="submit" value="Login"/><br>

            <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>
            <% if (errorMessage != null) {%>
            <span class="bg-danger" style="color:red;"><%=errorMessage%></span>
            <% } %>
        </fieldset>
    </form>

<%--    <% } else {%>--%>
<%--    <h1>Welcome back, <%=usernameFromSession%></h1>--%>
<%--    <a href="../stores/regions.html">Click here to enter the Super Duper Market room</a>--%>
<%--    <br/>--%>
<%--    <a href="login?logout=true" id="logout">logout</a>--%>
<%--    <% }%>--%>
</div>

</body>
</html>
