package controller;

import model.Feedback;
import model.FeedbackDAO;
import view.FeedbackPanel;
import java.util.List;

/**
 * Controller for handling feedback-related operations between the view and model
 */
public class FeedbackController {
    private FeedbackPanel view;
    private FeedbackDAO feedbackDAO;

    public FeedbackController(FeedbackPanel view, FeedbackDAO feedbackDAO) {
        this.view = view;
        this.feedbackDAO = feedbackDAO;
        initController();
    }

    private void initController() {
        view.setSubmitFeedbackListener(e -> submitFeedback());
        view.setUpdateFeedbackListener(e -> updateFeedback());
        view.setDeleteFeedbackListener(e -> deleteFeedback());
        view.setSearchFeedbackListener(e -> searchFeedback());
        loadAllFeedback();
    }

    private void submitFeedback() {
        try {
            Feedback newFeedback = view.getNewFeedbackData();
            boolean success = feedbackDAO.addFeedback(newFeedback);
            if (success) {
                view.showSuccessMessage("Feedback submitted successfully!");
                loadAllFeedback();
            } else {
                view.showErrorMessage("Failed to submit feedback.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error submitting feedback: " + e.getMessage());
        }
    }

    private void updateFeedback() {
        try {
            Feedback updatedFeedback = view.getUpdatedFeedbackData();
            boolean success = feedbackDAO.updateFeedback(updatedFeedback);
            if (success) {
                view.showSuccessMessage("Feedback updated successfully!");
                loadAllFeedback();
            } else {
                view.showErrorMessage("Failed to update feedback.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error updating feedback: " + e.getMessage());
        }
    }

    private void deleteFeedback() {
        try {
            int feedbackId = view.getSelectedFeedbackId();
            boolean success = feedbackDAO.deleteFeedback(feedbackId);
            if (success) {
                view.showSuccessMessage("Feedback deleted successfully!");
                loadAllFeedback();
            } else {
                view.showErrorMessage("Failed to delete feedback.");
            }
        } catch (Exception e) {
            view.showErrorMessage("Error deleting feedback: " + e.getMessage());
        }
    }

    private void searchFeedback() {
        try {
            int searchType = view.getSearchType();
            String searchTerm = view.getSearchTerm();
            
            List<Feedback> feedbacks;
            
            switch (searchType) {
                case 0: // Search by appointment ID
                    int appointmentId = Integer.parseInt(searchTerm);
                    feedbacks = feedbackDAO.getFeedbackByAppointment(appointmentId);
                    break;
                case 1: // Search by student ID
                    int studentId = Integer.parseInt(searchTerm);
                    feedbacks = feedbackDAO.getFeedbackByStudent(studentId);
                    break;
                case 2: // Search by counselor ID
                    int counselorId = Integer.parseInt(searchTerm);
                    feedbacks = feedbackDAO.getFeedbackByCounselor(counselorId);
                    break;
                case 3: // Search by rating
                    int rating = Integer.parseInt(searchTerm);
                    feedbacks = feedbackDAO.getFeedbackByRating(rating);
                    break;
                default:
                    feedbacks = feedbackDAO.getAllFeedback();
            }
            
            view.displayFeedback(feedbacks);
        } catch (Exception e) {
            view.showErrorMessage("Error searching feedback: " + e.getMessage());
        }
    }

    private void loadAllFeedback() {
        try {
            List<Feedback> feedbacks = feedbackDAO.getAllFeedback();
            view.displayFeedback(feedbacks);
        } catch (Exception e) {
            view.showErrorMessage("Error loading feedback: " + e.getMessage());
        }
    }

    // Additional method to get feedback for a specific appointment
    public Feedback getFeedbackForAppointment(int appointmentId) {
        try {
            return feedbackDAO.getFeedbackByAppointment(appointmentId).stream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            view.showErrorMessage("Error retrieving feedback: " + e.getMessage());
            return null;
        }
    }
}
