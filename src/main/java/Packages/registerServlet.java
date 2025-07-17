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

        //Read & basic‐validate inputs
        String snStr  = request.getParameter("student_number");
        String name   = request.getParameter("name");
        String surname= request.getParameter("surname");
        String email  = request.getParameter("email");
        String phone  = request.getParameter("phone");
        String pwd    = request.getParameter("password");
        String hashed = hashPassword(pwd);
        String error = null;
        int studentNumber = 0;

        if (snStr==null || name==null || surname==null ||
                email==null || phone==null || pwd==null ||
                snStr.isBlank()|| name.isBlank()   || surname.isBlank() ||
                email.isBlank()|| phone.isBlank()  || pwd.isBlank()) {
            error = "All fields are required.";
        } else {
            //parse student number
            try {
                studentNumber = Integer.parseInt(snStr);
            } catch (NumberFormatException e) {
                error = "Student number must be a valid number.";
            }
            //email format
            if (error==null && !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
                error = "Invalid email format.";
            }
            //phone digits
            if (error==null && !phone.matches("\\d{10}")) {
                error = "Phone number must be exactly 10 digits.";
            }
            //password length
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

        //Hash the password
        

        //Check duplicates & insert
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

            //bind insert params
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

    //BCrypt password hashing
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }
}