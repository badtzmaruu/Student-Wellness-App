package com.mycompany.milestone2.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int id;
    private String studentNumber;
    private int counselorId;
    private LocalDate date;
    private LocalTime time;
    private String status; // "Scheduled", "Completed", "Cancelled"

    public Appointment(String studentNumber, int counselorId, LocalDate date, LocalTime time, String status) {
        this.studentNumber = studentNumber;
        this.counselorId = counselorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStudentNumber() { return studentNumber; }
    public int getCounselorId() { return counselorId; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
    public String getStatus() { return status; }
    
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public void setCounselorId(int counselorId) { this.counselorId = counselorId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setTime(LocalTime time) { this.time = time; }
    public void setStatus(String status) { this.status = status; }
}
