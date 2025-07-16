<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
    </head>
    <body>
        <%
            // Retrieve data from session
            String studentName = (String) session.getAttribute("studentName");
            String studentSurname = (String) session.getAttribute("studentSurname");
            
            // Basic check if session attributes exist (user is logged in)
            if (studentName != null && studentSurname != null) {
        %>
                <h1>Welcome, <%= studentName %> <%= studentSurname %>!</h1>
                <p>This is your personalized dashboard.</p>
                
                <%-- Logout Button --%>
                <form action="dashServlet" method="post">
                    <input type="submit" value="Logout">
                </form>
        <%
            } else {
                // If session attributes are not found, redirect to login page
                response.sendRedirect("login.jsp");
            }
        %>
    </body>
</html>
