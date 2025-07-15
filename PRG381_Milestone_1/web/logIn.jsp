<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="javax.servlet.http.HttpSession" %>
<%
    String username = request.getParameter("txtUsername");
    String password = request.getParameter("txtPassword");
    String errorMessage = null; 

    // --- Input Validation ---
    if (username == null || username.trim().isEmpty()) {
        errorMessage = "Username cannot be empty.";
    } else if (password == null || password.trim().isEmpty()) {
        errorMessage = "Password cannot be empty.";
    }

    // If there's an input validation error, set the message and forward back to the login form
    if (errorMessage != null) {
        request.setAttribute("errorMessage", errorMessage); 
        request.getRequestDispatcher("index.jsp").forward(request, response);
        return; // STOP execution of this JSP
    }

    // --- Authentication Logic (only if input is valid and not null/empty) ---
    if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("admin")) {
        // Authentication successful - no error message needed, errorMessage remains null
    } else {
        errorMessage = "Invalid username or password."; // Generic error for security
    }

    // --- Handle Success or Failure ---
    if (errorMessage == null) { // Login was successful
        session.setAttribute("loggedInUser", username); // Store user in session
        response.sendRedirect("Dashboard.jsp"); // Redirect to dashboard
        return; // STOP execution of this JSP
    } else { // Login failed (authentication error)
        request.setAttribute("errorMessage", errorMessage); // Set the error for index.jsp
        request.getRequestDispatcher("index.jsp").forward(request, response);
        return; // STOP execution of this JSP
    }
%>