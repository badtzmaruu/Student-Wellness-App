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
import jakarta.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

//

@WebServlet (name = "logInServlet", urlPatterns = {"/logInServlet"})
public class logInServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("txtUsername");
        String password = request.getParameter("txtPassword");
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String error = null;
        
        if (username == null || password == null || username.isEmpty() || password.isEmpty()){
            error = "Please enter username and password";
        }
        
        if (error!=null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("login.jsp")
                    .forward(request, response);
            return;
        }
        
        try{
            conn = connectDb.getConnection();
            
            String sql = "SELECT student_number, password_hash FROM users WHERE email = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()){
                String storedHashedPassword = rs.getString("password_hash");
                int studentNumber = rs.getInt("student_number");
                
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                   
                    HttpSession session = request.getSession(); 
                    session.setAttribute("loggedInUserEmail", username);
                    session.setAttribute("loggedInStudentNumber", studentNumber);
                    
                    response.sendRedirect("dashServlet"); 
                    return;
                }else {
                    error = "Invalid email or password.";
                }
            } else {
                error = "Invalid email or password.";
            }
        } catch(Exception e){
            e.printStackTrace();
            error = "An error occurred during login. Please try again.";
        }finally {
            try { if (rs != null) rs.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (pstmt != null) pstmt.close(); } catch (Exception e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
        // If we reach here, it means login failed (either validation error or incorrect credentials)
        request.setAttribute("error", error);
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
   
}

