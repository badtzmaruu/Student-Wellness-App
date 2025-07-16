/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Packages;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author rjjou
 */
@WebServlet(name = "loginServlet", urlPatterns = {"/loginServlet"})
public class loginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //get form data 
        String studentNumberStr = request.getParameter("student_number");
         int studentNumber;
            try {
                studentNumber = Integer.parseInt(studentNumberStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Student number must be a valid number.");
                request.getRequestDispatcher("registration.jsp").forward(request, response);
                return;
            }
        String password = request.getParameter("password");
                
        //validating input
        if (studentNumberStr == null || studentNumberStr.isEmpty() || password == null || password.isEmpty() ){
            request.setAttribute("error", "Please enter both student number and password.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
        
        try (Connection conn = connectDb.getConnection()){
            //query the database for the student
            String sql = "SELECT name, password FROM users WHERE student_number = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, studentNumber);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()){
                String storedPassword = rs.getString("password");
                String studentName = rs.getString("name");
                
                //call hash here
                //String hashedInputPassword = hashPassword(password);
                
                if (storedPassword.equals(password)){
                   HttpSession session = request.getSession();
                   session.setAttribute("studentNumber", studentNumber);
                   session.setAttribute("studentName", studentName);
                   response.sendRedirect("dash.jsp");
                }else{
                    request.setAttribute("error", "Incorrect Password.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            }else {
                
                request.setAttribute("error", "Student Number not Found");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "Login failed due to error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
        
    }
    
      //put hash here like registration

    
  // Handles POST requests from login.jsp
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);
}

// Handles GET requests 
@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    processRequest(request, response);
}

// Description of the servlet
@Override
public String getServletInfo() {
    return "Handles student login authentication";
}

 

}

