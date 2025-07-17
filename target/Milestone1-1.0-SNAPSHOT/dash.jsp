
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="./styles.css"/> 

        <title>Dashboard</title>

    </head>
    <body>
        <div class="container">
        <%
            String userName = (String) session.getAttribute("loggedInUserName");
            String userSurname = (String) session.getAttribute("loggedInUserSurname");
            String userEmail = (String) session.getAttribute("loggedInUserEmail");

            if (userName != null && !userName.isEmpty() && userSurname != null && !userSurname.isEmpty()) {
        %>
            <h1>Welcome <%= userName %> <%= userSurname %>!</h1>
        <%
            } else if (userEmail != null && !userEmail.isEmpty()) {
                
        %>
            <h1>Welcome <%= userEmail %>!</h1>
        <%
            } else {
                response.sendRedirect("login.jsp?message=Please log in to access the dashboard.");
                return; 
            }
        %>
           

        <p>This is your personalized dashboard content.</p>
        <p>You can add more dashboard features here.</p>

        <% if (session.getAttribute("loggedInStudentNumber") != null) { %>
            <p>Your Student Number: <%= session.getAttribute("loggedInStudentNumber") %></p>
        <% } %>

        <p><a href="logoutServlet">Log Out</a></p>
           </div>
    </body>
</html>
