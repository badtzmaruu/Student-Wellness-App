/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Packages;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "registerServlet", urlPatterns = {"/registerServlet"})
public class registerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // 1) Read & basic‐validate inputs
        String snStr  = request.getParameter("student_number");
        String name   = request.getParameter("name");
        String surname= request.getParameter("surname");
        String email  = request.getParameter("email");
        String phone  = request.getParameter("phone");
        String pwd    = request.getParameter("password");

        String error = null;
        int studentNumber = 0;

        if (snStr==null || name==null || surname==null ||
                email==null || phone==null || pwd==null ||
                snStr.isBlank()|| name.isBlank()   || surname.isBlank() ||
                email.isBlank()|| phone.isBlank()  || pwd.isBlank()) {
            error = "All fields are required.";
        } else {
            // parse student number
            try {
                studentNumber = Integer.parseInt(snStr);
            } catch (NumberFormatException e) {
                error = "Student number must be a valid number.";
            }
            // email format
            if (error==null && !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                error = "Invalid email format.";
            }
            // phone digits
            if (error==null && !phone.matches("\\d{10}")) {
                error = "Phone number must be exactly 10 digits.";
            }
            // password length
            if (error==null && pwd.length()<6) {
                error = "Password must be at least 6 characters.";
            }
        }

        if (error!=null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("registration.jsp")
                    .forward(request, response);
            return;
        }

        // 2) Hash the password
        String hashed = hashPassword(pwd);

        // 3) Check duplicates & insert
        String checkSql  = "SELECT 1 FROM users WHERE email = ? OR student_number = ?";
        String insertSql = "INSERT INTO users "
                + "(student_number,name,surname,email,phone,password_hash) "
                + "VALUES (?,?,?,?,?,?)";

        try (Connection conn = connectDb.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            checkStmt.setString(1, email);
            checkStmt.setInt(2, studentNumber);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("error",
                            "Email or Student Number already exists.");
                    request.getRequestDispatcher("registration.jsp")
                            .forward(request, response);
                    return;
                }
            }

            // bind insert params
            insertStmt.setInt   (1, studentNumber);
            insertStmt.setString(2, name);
            insertStmt.setString(3, surname);
            insertStmt.setString(4, email);
            insertStmt.setString(5, phone);
            insertStmt.setString(6, hashed);

            int inserted = insertStmt.executeUpdate();
            if (inserted==1) {
                request.setAttribute("message",
                        "Registration successful. You can now log in.");
                request.getRequestDispatcher("login.jsp")
                        .forward(request, response);
            } else {
                request.setAttribute("error","Registration failed. Try again.");
                request.getRequestDispatcher("registration.jsp")
                        .forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error","An error occurred: "+e.getMessage());
            request.getRequestDispatcher("registration.jsp")
                    .forward(request, response);
        }
    }

    // ----------------------------------------------------------------
    // BCrypt password hashing
    private String hashPassword(String password) {
        // 12 rounds is a good default work factor
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}





/*
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String studentNumber = request.getParameter("student_number");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

//validation
        if (studentNumber.isEmpty() || name.isEmpty() || surname.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

//email format check
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            request.setAttribute("error", "Invalid email format.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

//phone number check
        if (!phone.matches("\\d{10}")) {
            request.setAttribute("error", "Phone number must be exactly 10 digits.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

//password length check
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try (Connection conn = ConnectionDB.getConnection()) {
            //Check for duplicate email or student number
            String checkQuery = "SELECT * FROM users WHERE email = ? OR student_number = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, studentNumber);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                request.setAttribute("error", "Email or Student Number already exists.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            //Insert new user with hashed password
            String insertQuery = "INSERT INTO users (student_number, name, surname, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, studentNumber);
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
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }} catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occured: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
*/