package Controller;

import model.Appointment;
import model.AppointmentDAO;
import view.AppointmentPanel;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for handling appointment-related operations between the view and model
 */
public class AppointmentController {
    private AppointmentPanel view;
    private AppointmentDAO appointmentDAO;

    public AppointmentController(AppointmentPanel view, AppointmentDAO appointmentDAO) {
        this.view = view;
        this.appointmentDAO = appointmentDAO;
        initController();
    }

    private void initController() {
        view.setCreateAppointmentListener(e -> createAppointment());
        view.setUpdateAppointmentListener(e -> updateAppointment());
        view.setCancelAppointmentListener(e -> cancelAppointment());
        view.setSearchAppointmentsListener(e -> searchAppointments());
        loadAllAppointments();
    }

    private void createAppointment() {
        try {
            Appointment newAppointment = view.getNewAppointmentData();
            boolean success = appointmentDAO.addAppointment(newAppointment);
            if (success) {
                view.showSuccessMessage("Appointment created successfully!");
                loadAllAppointments();
            } else {
                view.showErrorMessage("Failed to create appointment.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error creating appointment: " + e.getMessage());
        }
    }

    private void updateAppointment() {
        try {
            Appointment updatedAppointment = view.getUpdatedAppointmentData();
            boolean success = appointmentDAO.updateAppointment(updatedAppointment);
            if (success) {
                view.showSuccessMessage("Appointment updated successfully!");
                loadAllAppointments();
            } else {
                view.showErrorMessage("Failed to update appointment.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error updating appointment: " + e.getMessage());
        }
    }

    private void cancelAppointment() {
        try {
            int appointmentId = view.getSelectedAppointmentId();
            boolean success = appointmentDAO.cancelAppointment(appointmentId);
            if (success) {
                view.showSuccessMessage("Appointment cancelled successfully!");
                loadAllAppointments();
            } else {
                view.showErrorMessage("Failed to cancel appointment.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error cancelling appointment: " + e.getMessage());
        }
    }

    private void searchAppointments() {
        try {
            int searchType = view.getSearchType();
            String searchTerm = view.getSearchTerm();
            LocalDateTime searchDate = view.getSearchDate();
            
            List<Appointment> appointments;
            
            switch (searchType) {
                case 0: // Search by student ID
                    int studentId = Integer.parseInt(searchTerm);
                    appointments = appointmentDAO.getAppointmentsByStudent(studentId);
                    break;
                case 1: // Search by counselor ID
                    int counselorId = Integer.parseInt(searchTerm);
                    appointments = appointmentDAO.getAppointmentsByCounselor(counselorId);
                    break;
                case 2: // Search by date
                    appointments = appointmentDAO.getAppointmentsByDate(searchDate.toLocalDate());
                    break;
                default:
                    appointments = appointmentDAO.getAllAppointments();
            }
            
            view.displayAppointments(appointments);
        } catch (Exception e) {
            view.showErrorMessage("Error searching appointments: " + e.getMessage());
        }
    }

    private void loadAllAppointments() {
        try {
            List<Appointment> appointments = appointmentDAO.getAllAppointments();
            view.displayAppointments(appointments);
        } catch (Exception e) {
            view.showErrorMessage("Error loading appointments: " + e.getMessage());
        }
    }
}
