package sdm.feedback;

import DTO.FeedbackDTO;

import java.time.LocalDate;

public class Feedback {
    private final String customerName;
    private final LocalDate localDate;
    private final int rating;
    private final String feedback;

    public Feedback(String customerName, LocalDate localDate, int rating, String feedback) {
        this.customerName = customerName;
        this.localDate = localDate;
        this.rating = rating;
        this.feedback = feedback;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public int getRating() {
        return rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public FeedbackDTO feedbackToFeedbackDTO(){
        return new FeedbackDTO(customerName, localDate, rating, feedback);
    }
}
