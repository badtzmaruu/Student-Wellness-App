package com.mycompany.milestone2.dao;

import com.mycompany.milestone2.model.Appointment;
import com.mycompany.milestone2.model.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    public void addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO Appointments (student_number, counselor_id, date, time, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, appointment.getStudentNumber());
            stmt.setInt(2, appointment.getCounselorId());
            stmt.setDate(3, Date.valueOf(appointment.getDate()));
            stmt.setTime(4, Time.valueOf(appointment.getTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    appointment.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Appointment> getUpcomingAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments WHERE date >= CURRENT_DATE ORDER BY date, time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getString("student_number"),
                    rs.getInt("counselor_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("time").toLocalTime(),
                    rs.getString("status")
                );
                appointment.setId(rs.getInt("id"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }
    
    // Add update, delete, and other read methods as needed
}
