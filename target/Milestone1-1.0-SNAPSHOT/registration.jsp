<%-- 
    Document   : registration
    Created on : 15 Jul 2025, 23:34:54
    Author     : rjjou
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
    String message = "", error = "";

    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String studentNumber = request.getParameter("student_number");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        if (studentNumber.isEmpty() || name.isEmpty() || surname.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            error = "All fields are required.";
        } else if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            error = "Invalid email format.";
        } else if (!phone.matches("\\d{10}")) {
            error = "Phone number must be exactly 10 digits.";
        } else if (password.length() < 6) {
            error = "Password must be at least 6 characters.";
        } else {
            try {
                Class.forName("org.postgresql.Driver");
               java.sql.Connection conn = java.sql.DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", "postgres", "Giepie20040402");
                java.sql.PreparedStatement check = conn.prepareStatement("SELECT * FROM users WHERE email = ? OR student_number = ?");
                check.setString(1, email);
                check.setInt(2, Integer.parseInt(studentNumber));
                java.sql.ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    error = "Email or Student Number already exists.";
                } else {
                    // Insert new user
                    java.sql.PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO users (student_number, name, surname, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)"
                    );
                    insert.setInt(1, Integer.parseInt(studentNumber));
                    insert.setString(2, name);
                    insert.setString(3, surname);
                    insert.setString(4, email);
                    insert.setString(5, phone);
                    insert.setString(6, password);

                    int result = insert.executeUpdate();
                    if (result > 0) {
                        message = "Registration successful. You may now log in.";
                    } else {
                        error = "Failed to register.";
                    }
                }
                conn.close();
            } catch (Exception e) {
                error = "Error: " + e.getMessage();
            }
        }
    }
%>

<form method="post">
    Student Number: <input type="text" name="student_number"><br/>
    Name: <input type="text" name="name"><br/>
    Surname: <input type="text" name="surname"><br/>
    Email: <input type="text" name="email"><br/>
    Phone: <input type="text" name="phone"><br/>
    Password: <input type="password" name="password"><br/>
    <input type="submit" value="Register">
</form>

<p class="error"><%= error %></p>
<p class="success"><%= message %></p>


    <%-- Show error or success message --%>
    <p class="error">
        <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
    </p>
    <p class="success">
        <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
    </p>
        </div>
        </div>
    </body>
</html>