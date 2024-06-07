package com.example.backup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class FacilityController {

    @FXML
    private Button backButton;
    @FXML
    private TextField requestNatureField;
    @FXML
    private TextField locationField;
    @FXML
    private TextArea commentsField;
    @FXML
    private ComboBox<String> urgencyComboBox;
    @FXML
    private TableView<FacilityMaintenance> requestTableView;
    @FXML
    private TableColumn<FacilityMaintenance, Integer> requestIdColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> requestNatureColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> locationColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> commentsColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> urgencyColumn;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button monitorStatusButton;
    @FXML
    private Label statusLabel;

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/pmis";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "123456";

    @FXML
    public void initialize() {
        urgencyComboBox.getItems().addAll("High Priority", "Medium Priority", "Low Priority");
        initializeTableColumns();
        loadRequestsFromDatabase();
    }

    private void initializeTableColumns() {
        requestIdColumn.setCellValueFactory(cellData -> cellData.getValue().requestIdProperty().asObject());
        requestNatureColumn.setCellValueFactory(cellData -> cellData.getValue().requestNatureProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        commentsColumn.setCellValueFactory(cellData -> cellData.getValue().commentsProperty());
        urgencyColumn.setCellValueFactory(cellData -> cellData.getValue().urgencyProperty());
    }

    private void loadRequestsFromDatabase() {
        ObservableList<FacilityMaintenance> requestList = FXCollections.observableArrayList();
        String sql = "SELECT request_id, request_nature, location, comments, urgency FROM FacilityMaintenanceRequests";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                String requestNature = rs.getString("request_nature");
                String location = rs.getString("location");
                String comments = rs.getString("comments");
                String urgency = rs.getString("urgency");

                // Set assigned staff and progress as empty strings for now
                FacilityMaintenance request = new FacilityMaintenance(requestId, requestNature, location, comments, urgency, "", "");
                requestList.add(request);
            }

            requestTableView.setItems(requestList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBackButtonAction() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
        SceneLoader.loadScene("hello-view.fxml", new Stage());
    }

    @FXML
    private void handleSubmitButtonAction() {
        String requestNature = requestNatureField.getText();
        String location = locationField.getText();
        String comments = commentsField.getText();
        String urgency = urgencyComboBox.getValue();

        if (requestNature.isEmpty() || location.isEmpty() || urgency == null) {
            statusLabel.setText("Please fill all required fields.");
            return;
        }

        if (saveToDatabase(requestNature, location, comments, urgency)) {
            statusLabel.setText("Request submitted successfully!");
            clearForm();
            loadRequestsFromDatabase(); // Refresh the table after submission
        } else {
            statusLabel.setText("Error submitting request. Please try again.");
        }
    }

    private boolean saveToDatabase(String requestNature, String location, String comments, String urgency) {
        String sql = "INSERT INTO FacilityMaintenanceRequests (request_nature, location, comments, urgency) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, requestNature);
            pstmt.setString(2, location);
            pstmt.setString(3, comments);
            pstmt.setString(4, urgency);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 1) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int requestId = generatedKeys.getInt(1);
                        // Optionally, you can return the generated request ID
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        clearForm();
    }

    private void clearForm() {
        requestNatureField.clear();
        locationField.clear();
        commentsField.clear();
        urgencyComboBox.getSelectionModel().clearSelection();
        statusLabel.setText("");
    }

    @FXML
    private void handleMonitorStatusAction() {
        try {
            // Load the FXML file for MFRS
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/backup/MFRS.fxml"));

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Monitor Facility Maintenance Requests Status");
            stage.setScene(new Scene(root));
            stage.show();

            // Optionally close the current window
            Stage currentStage = (Stage) monitorStatusButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
