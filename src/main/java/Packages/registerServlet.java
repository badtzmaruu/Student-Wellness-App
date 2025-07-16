/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Packages;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;




/**
 *
 * @author rjjou
 */
@WebServlet(name = "registerServlet", urlPatterns = {"/registerServlet"})
public class registerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

 
        String studentNumberStr = request.getParameter("student_number");
         int studentNumber;
            try {
                studentNumber = Integer.parseInt(studentNumberStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Student number must be a valid number.");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
                return;
            }
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Basic Validation
        if (studentNumberStr == null || name == null || surname == null ||
            email == null || phone == null || password == null ||
            studentNumberStr.isEmpty() || name.isEmpty() || surname.isEmpty() ||
            email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Email format check
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            request.setAttribute("error", "Invalid email format.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Phone number check
        if (!phone.matches("\\d{10}")) {
            request.setAttribute("error", "Phone number must be exactly 10 digits.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        // Password length check
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
            return;
        }

        try (Connection conn = connectDb.getConnection()) {
            // Check for duplicate email or student number
            String checkQuery = "SELECT * FROM users WHERE email = ? OR student_number = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setInt(2, studentNumber);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                request.setAttribute("error", "Email or Student Number already exists.");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
                return;
            }

            // Insert new user with hashed password
            String insertQuery = "INSERT INTO users (student_number, name, surname, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, studentNumber);
            insertStmt.setString(2, name);
            insertStmt.setString(3, surname);
            insertStmt.setString(4, email);
            insertStmt.setString(5, phone);
            insertStmt.setString(6, hashPassword(password));

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                request.setAttribute("message", "Registration successful. You can now log in.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Registration failed. Try again.");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("registration.jsp").forward(request, response);
        }
    }

    // Do hash here
    private String hashPassword(String password) {
        return password; 
    }
}