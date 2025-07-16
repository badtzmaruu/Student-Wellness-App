package Packages;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rjjou
 */
public class connectDb {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "Jonathan";
    private static final String PASSWORD = "J0n@than";

//    public static Connection getConnection() throws SQLException {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
    
    public static Connection getConnection() throws SQLException {
    try {
        Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new SQLException("PostgreSQL JDBC Driver not found.");
    }
    return DriverManager.getConnection(URL, USER, PASSWORD);
}
}
