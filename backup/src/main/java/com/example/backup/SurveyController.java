package com.example.backup;

import com.example.backup.DatabaseConnection;
import com.example.backup.Survey;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SurveyController {
    @FXML
    private TextField surveyTitleField;

    @FXML
    private TextArea surveyDescriptionArea;

    @FXML
    private TextArea questionsArea;

    @FXML
    private Button submitSurveyButton;

    @FXML
    private ListView<String> surveyListView;

    @FXML
    private VBox surveyQuestionsVBox;

    private DatabaseConnection database = new DatabaseConnection();

    @FXML
    private void initialize() {
        loadSurveys();
    }

    @FXML
    private void handleSubmitSurvey() {
        try {
            String title = surveyTitleField.getText();
            String description = surveyDescriptionArea.getText();
            LocalDate createdDate = LocalDate.now();
            LocalDate dueDate = createdDate.plusMonths(1);

            Survey survey = new Survey();
            survey.setTitle(title);
            survey.setDescription(description);
            survey.setCreatedDate(createdDate);
            survey.setDueDate(dueDate);

            List<String> questions = new ArrayList<>();
            String[] questionTexts = questionsArea.getText().split("\n");
            for (String questionText : questionTexts) {
                questions.add(questionText);
            }
            survey.setQuestions(questions);

            int surveyId = saveSurvey(survey);
            for (String questionText : questions) {
                saveQuestion(surveyId, questionText);
            }

            showAlert("Survey Created", "The survey has been created successfully.");
            loadSurveys();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while creating the survey.");
        }
    }

    private int saveSurvey(Survey survey) throws SQLException {
        String query = "INSERT INTO Survey (Title, Description, CreatedDate, DueDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, survey.getTitle());
            pstmt.setString(2, survey.getDescription());
            pstmt.setDate(3, Date.valueOf(survey.getCreatedDate()));
            pstmt.setDate(4, Date.valueOf(survey.getDueDate()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating survey failed, no ID obtained.");
                }
            }
        }
    }

    private void saveQuestion(int surveyId, String questionText) throws SQLException {
        String query = "INSERT INTO Question (SurveyID, QuestionText) VALUES (?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, surveyId);
            pstmt.setString(2, questionText);
            pstmt.executeUpdate();
        }
    }

    private void loadSurveys() {
        surveyListView.getItems().clear();
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT SurveyID, Title FROM Survey");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int surveyId = rs.getInt("SurveyID");
                String title = rs.getString("Title");
                surveyListView.getItems().add(surveyId + ": " + title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading surveys.");
        }
    }

    @FXML
    private void handleSurveySelection() {
        String selectedSurvey = surveyListView.getSelectionModel().getSelectedItem();
        if (selectedSurvey != null) {
            int surveyId = Integer.parseInt(selectedSurvey.split(":")[0]);
            displaySurveyQuestions(surveyId);
        }
    }

    private void displaySurveyQuestions(int surveyId) {
        surveyQuestionsVBox.getChildren().clear();
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT QuestionText FROM Question WHERE SurveyID = ?")) {

            stmt.setInt(1, surveyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String questionText = rs.getString("QuestionText");
                    Label questionLabel = new Label(questionText);
                    TextField answerField = new TextField();
                    surveyQuestionsVBox.getChildren().addAll(questionLabel, answerField);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading survey questions.");
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

