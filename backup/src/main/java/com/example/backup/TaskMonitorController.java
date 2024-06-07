package com.example.backup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskMonitorController {

    @FXML
    private TableView<CommunityServiceModel> taskMonitorTable;

    @FXML
    private TableColumn<CommunityServiceModel, Integer> columnInmateID;

    @FXML
    private TableColumn<CommunityServiceModel, Integer> columnTaskID;

    @FXML
    private TableColumn<CommunityServiceModel, String> columnTaskName;

    @FXML
    private TableColumn<CommunityServiceModel, String> columnTaskStatus;

    @FXML
    private ComboBox<Integer> inmateIDComboBox;

    private ObservableList<CommunityServiceModel> taskMonitorData;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    @FXML
    public void initialize() {
        // Set up the columns in the table
        columnInmateID.setCellValueFactory(new PropertyValueFactory<>("inmateID"));
        columnTaskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        columnTaskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        columnTaskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));

        backButton.setOnAction(this::handleBackButtonAction);

        // Load data from the database
        loadData();

        // Set submit button action
        submitButton.setOnAction(this::handleSubmitButtonAction);
    }

    private void loadData() {
        taskMonitorData = FXCollections.observableArrayList();

        // Dummy data for now
        taskMonitorData.add(new CommunityServiceModel(1, 101, "Cleaning", "In Progress"));
        taskMonitorData.add(new CommunityServiceModel(4, 104, "Tutoring", "In Progress"));
        taskMonitorData.add(new CommunityServiceModel(5, 105, "Painting", "Waiting"));
        taskMonitorData.add(new CommunityServiceModel(7, 107, "Mentoring", "Slacking"));
        taskMonitorData.add(new CommunityServiceModel(8, 108, "Fundraising", "Waiting"));
        taskMonitorData.add(new CommunityServiceModel(10, 110, "Volunteer Coordination", "Slacking"));

        // Filter the data to show only inmates with "Slacking" and "Waiting" task statuses
        ObservableList<CommunityServiceModel> filteredData = taskMonitorData.filtered(
                model -> model.getTaskStatus().equals("Slacking") || model.getTaskStatus().equals("Waiting")
        );

        // Set the filtered data to the table
        taskMonitorTable.setItems(filteredData);

        // Filter data for ComboBox to show only "Waiting" task statuses
        ObservableList<Integer> waitingInmateIDs = FXCollections.observableArrayList();
        for (CommunityServiceModel model : taskMonitorData) {
            if (model.getTaskStatus().equals("Waiting")) {
                waitingInmateIDs.add(model.getInmateID());
            }
        }
        inmateIDComboBox.setItems(waitingInmateIDs);
    }

    @FXML
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

    @FXML
    private void sortTableByTaskStatusInProgress(ActionEvent event) {
        // Filter the data to show only "In Progress" task statuses
        ObservableList<CommunityServiceModel> filteredData = taskMonitorData.filtered(
                model -> model.getTaskStatus().equals("In Progress")
        );

        // Set the filtered data to the table
        taskMonitorTable.setItems(filteredData);
    }

    @FXML
    private void sortTableByTaskStatusWaiting(ActionEvent event) {
        // Filter the data to show only "Waiting" task statuses
        ObservableList<CommunityServiceModel> filteredData = taskMonitorData.filtered(
                model -> model.getTaskStatus().equals("Waiting")
        );

        // Set the filtered data to the table
        taskMonitorTable.setItems(filteredData);
    }

    @FXML
    private void handleRefreshButtonAction(ActionEvent event) {
        loadData();
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent event) {
        Integer selectedInmateID = inmateIDComboBox.getValue();
        if (selectedInmateID != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning Issued");
            alert.setHeaderText(null);
            alert.setContentText("Warning issued to inmate ID: " + selectedInmateID);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Inmate Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select an inmate ID to issue a warning.");
            alert.showAndWait();
        }
    }
}
