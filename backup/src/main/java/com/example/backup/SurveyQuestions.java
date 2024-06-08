package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SurveyQuestions {

    @FXML
    private Label surveyTitleLabel;

    @FXML
    private VBox surveyQuestionsVBox;

    private Survey selectedSurvey;
    private DatabaseConnection database = new DatabaseConnection();

    public void setSurvey(Survey survey) {
        this.selectedSurvey = survey;
        displaySurveyQuestions();
    }

    private void displaySurveyQuestions() {
        surveyQuestionsVBox.getChildren().clear();
        surveyTitleLabel.setText(selectedSurvey.getTitle());

        for (int i = 0; i < selectedSurvey.getQuestions().size(); i++) {
            String question = selectedSurvey.getQuestions().get(i);
            Label questionLabel = new Label(question);
            TextField answerField = new TextField();
            answerField.setId("question" + i);
            surveyQuestionsVBox.getChildren().addAll(questionLabel, answerField);
        }
    }

    @FXML
    private void handleSubmitSurvey() {
        try {
            int inmateId = 1; // Replace with actual inmate ID
            List<Feedback> feedbacks = new ArrayList<>();

            for (int i = 0; i < selectedSurvey.getQuestions().size(); i++) {
                TextField answerField = (TextField) surveyQuestionsVBox.lookup("#question" + i);
                String answer = answerField.getText();
                Feedback feedback = new Feedback();
                feedback.setSurveyId(selectedSurvey.getSurveyId());
                feedback.setInmateId(inmateId);
                feedback.setQuestionIndex(i);
                feedback.setAnswer(answer);
                feedbacks.add(feedback);
            }

            saveFeedbacks(feedbacks);
            showAlert("Survey Submitted", "Thank you for your feedback!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while submitting the survey.");
        }
    }

    private void saveFeedbacks(List<Feedback> feedbacks) throws SQLException {
        String query = "INSERT INTO Feedback (SurveyID, InmateID, QuestionIndex, Answer) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (Feedback feedback : feedbacks) {
                pstmt.setInt(1, feedback.getSurveyId());
                pstmt.setInt(2, feedback.getInmateId());
                pstmt.setInt(3, feedback.getQuestionIndex());
                pstmt.setString(4, feedback.getAnswer());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

