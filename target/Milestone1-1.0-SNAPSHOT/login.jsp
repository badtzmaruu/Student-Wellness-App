<%--
    Document   : login
    Created on : 15 Jul 2025, 23:35:00
    Author     : rjjou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
       <link rel="stylesheet" href="./styles.css"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Log In Page</title>
        <style>
            /* Add some basic styling for error/success messages */
            .error { color: red; font-weight: bold; }
            .message { color: green; font-weight: bold; }
        </style>
    </head>
    <body>
        <div class="container">
        <h1>Welcome Back!</h1>

        <%
            String errorMessage = null;
            String successMessage = null;
            String oldUsername = ""; 
            
            if ("POST".equalsIgnoreCase(request.getMethod())) {
                errorMessage = (String) request.getAttribute("error");
                successMessage = (String) request.getAttribute("message"); 
                oldUsername = (String) request.getAttribute("username"); 

               
                if (oldUsername == null) {
                    oldUsername = ""; 
                }
            }
            
        %>
 

        <%-- Display Error Message (if any) --%>
        <% if (errorMessage != null && !errorMessage.isEmpty()) { %>
            <p class="error"><%= errorMessage %></p>
        <% } %>

        <%-- Display Success Message (if any) --%>
        <% if (successMessage != null && !successMessage.isEmpty()) { %>
            <p class="message"><%= successMessage %></p>
        <% } %>

        <form name="login" action="loginServlet" method="POST">
            <div class="form-group">
            Email: <input type="text" name="txtUsername" value="<%= oldUsername %>" size="50" /><br/>
            </div>
            <div class="form-group">
            Password: <input type="password" name="txtPassword" value="" size="50" /><br/>
            <input type="submit" value="Log In" name="btnLogIn" />
            </div>
        </form>

        <p>Don't have an account? <a href="registration.jsp">Register Here</a></p>

    </body>
</div>
</html>
