
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Student Wellness App</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
        
        <h1>Welcome Student</h1>
        <form name="logIn" action="logIn.jsp" method="post">
            <input type="text" name="txtUsername" value="" size="50" />
            <input type="password" name="txtPassword" value="" size="50" />
            <button type="submit">Log In</button>
        </form>
        <form name="Register" action="Register.jsp" method="post">
            <button>Register</button>
        </form>
        
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <p class="error-message"><%= errorMessage %></p>
        <%
            }
        %>

    </body>
</html>
