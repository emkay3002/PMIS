package com.example.backup;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SurveyTaking {

    @FXML
    private VBox surveyListVBox;

    private DatabaseConnection database = new DatabaseConnection();

    @FXML
    private void initialize() {
        loadSurveys();
    }

    private void loadSurveys() {
        surveyListVBox.getChildren().clear();
        try (Connection conn = database.getConnection()) {
            String query = "SELECT * FROM Survey";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Survey survey = new Survey();
                survey.setSurveyId(rs.getInt("SurveyID"));
                survey.setTitle(rs.getString("Title"));
                survey.setDescription(rs.getString("Description"));
                survey.setCreatedDate(rs.getDate("CreatedDate").toLocalDate());
                survey.setDueDate(rs.getDate("DueDate").toLocalDate());

                // Load questions for the survey
                List<String> questions = new ArrayList<>();
                String questionQuery = "SELECT QuestionText FROM Question WHERE SurveyID = ?";
                PreparedStatement questionStmt = conn.prepareStatement(questionQuery);
                questionStmt.setInt(1, survey.getSurveyId());
                ResultSet questionRs = questionStmt.executeQuery();
                while (questionRs.next()) {
                    questions.add(questionRs.getString("QuestionText"));
                }
                survey.setQuestions(questions);

                Button surveyButton = new Button(survey.getTitle());
                surveyButton.setOnAction(e -> displaySurvey(survey));
                surveyListVBox.getChildren().add(surveyButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySurvey(Survey survey) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/prisonmis/survey_questions.fxml"));
            Parent root = loader.load();
            SurveyQuestions controller = loader.getController();
            controller.setSurvey(survey);

            Stage stage = new Stage();
            stage.setTitle("Survey Questions");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

