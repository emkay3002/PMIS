package com.example.backup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskMonitorController {

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Set the action for the back button
        backButton.setOnAction(this::handleBackButtonAction);
    }

    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the community service FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CommunityService.fxml"));
            Parent communityServicePage = loader.load();

            // Create a new scene with the loaded FXML
            Scene communityServiceScene = new Scene(communityServicePage);

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene to the stage
            stage.setScene(communityServiceScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Optional: show an alert dialog or log the error
        }
    }
}
