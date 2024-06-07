package com.example.backup;

import com.example.backup.CommunityServiceModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.util.Comparator;
import java.util.function.UnaryOperator;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.MenuItem;

public class CommunityServiceController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button backButton;

    @FXML
    private MenuButton sortMenuButton;

    @FXML
    private Button viewTaskAssignmentButton;


    @FXML
    private TableView<CommunityServiceModel> communityServiceTable;

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

    @FXML
    private MenuItem sortByTaskStatusWaitingMenuItem;

    @FXML
    private MenuItem sortByTaskStatusInProgressMenuItem;

    @FXML
    private MenuItem sortByTaskStatusCompletedMenuItem;

    @FXML
    private MenuItem sortByInmateIDMenuItem;

    private ObservableList<CommunityServiceModel> communityServiceData;

    @FXML
    public void initialize() {
        // Check if buttons are null

        if (backButton == null ) {
            System.err.println("FXML components not properly injected");
            return;
        }

        // Set up the columns in the table
        columnInmateID.setCellValueFactory(new PropertyValueFactory<>("inmateID"));
        columnTaskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        columnTaskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        columnTaskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));

        // Add event handlers for sorting options
        sortByTaskStatusWaitingMenuItem.setOnAction(event -> sortTableByTaskStatus("Waiting"));
        sortByTaskStatusInProgressMenuItem.setOnAction(event -> sortTableByTaskStatus("In Progress"));
        sortByTaskStatusCompletedMenuItem.setOnAction(event -> sortTableByTaskStatus("Completed"));
        sortByInmateIDMenuItem.setOnAction(event -> sortTableByInmateID());

        // Load data from the database
        loadData();

        // Set up the back button event handler
        backButton.setOnAction(this::handleBackButtonAction);

        //view task assign
        viewTaskAssignmentButton.setOnAction(this::handleViewTaskAssignmentAction);

        // Populate the inmate ID ComboBox
        populateInmateIDComboBox();

        // Populate the task name ComboBox
        populateTaskNameComboBox();

        // Restricting input to numbers between 1 and 15
        /*UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.isContentChange()) {
                String newText = change.getControlNewText();
                if (newText.matches("\\d*")) { // Check if newText contains only digits
                    try {
                        int value = Integer.parseInt(newText);
                        if (value >= 1 && value <= 15) {
                            return change;
                        }
                    } catch (NumberFormatException e) {
                        // Do nothing if parsing fails
                    }
                }
                return null; // Reject the change if it doesn't match the criteria
            }
            return change;
        };*/

        // Applying the filter to the hours TextField
        //hoursTextField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, filter));

    }

    private void sortTableByTaskStatus(String status) {
        communityServiceData.sort(Comparator.comparing(CommunityServiceModel::getTaskStatus));
        communityServiceTable.setItems(communityServiceData);
    }

    private void sortTableByInmateID() {
        communityServiceData.sort(Comparator.comparing(CommunityServiceModel::getInmateID));
        communityServiceTable.setItems(communityServiceData);
    }
    @FXML
    private void handleAssignButtonAction(ActionEvent event) {
        // Create an alert for task assignment
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Assigned");
        alert.setHeaderText(null);
        alert.setContentText("Task assigned successfully.");
        alert.showAndWait();

        // Add the assigned task data to the table view
        String taskName = taskNameComboBox.getValue();
        int taskId = getTaskId(taskName);
        int inmateId = inmateIDComboBox.getValue();
        //String hours = hoursTextField.getText();

        // Assuming the status is "In Progress" for newly assigned tasks
        CommunityServiceModel assignedTask = new CommunityServiceModel(inmateId, taskId, taskName, "In Progress");
        communityServiceData.add(assignedTask);
    }

    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Load the home page FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent homePage = loader.load();

            // Create a new scene with the home page
            Scene homeScene = new Scene(homePage);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(homeScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an alert dialog
        }
    }

    private void handleViewTaskAssignmentAction(ActionEvent event) {
        try {
            // Load the task monitoring FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskMonitor.fxml"));
            Parent taskMonitoringPage = loader.load();

            // Create a new scene with the task monitoring page
            Scene taskMonitoringScene = new Scene(taskMonitoringPage);

            // Get the stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(taskMonitoringScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an alert dialog
        }
    }

    private void loadData() {
        communityServiceData = FXCollections.observableArrayList();

        //Dummy data for now
        communityServiceData.add(new CommunityServiceModel(1, 101, "Cleaning", "In Progress"));
        communityServiceData.add(new CommunityServiceModel(2, 102, "Cooking", "Complete"));
        communityServiceData.add(new CommunityServiceModel(3, 103, "Gardening", "Slacking"));

        // Set the data to the table
        communityServiceTable.setItems(communityServiceData);
    }

    private void populateInmateIDComboBox() {
        // Dummy data for now
        List<Integer> inmateIDs = new ArrayList<>();
        inmateIDs.add(1);
        inmateIDs.add(2);
        inmateIDs.add(3);

        // Set the data to the ComboBox
        inmateIDComboBox.setItems(FXCollections.observableArrayList(inmateIDs));
    }

    @FXML
    private ComboBox<String> taskNameComboBox;

    private void populateTaskNameComboBox() {
        // Dummy list of task names
        List<String> taskNames = Arrays.asList("Cleaning", "Cooking", "Gardening", "Painting", "Teaching");

        // Set the data to the ComboBox
        taskNameComboBox.setItems(FXCollections.observableArrayList(taskNames));
    }

    // Method to get the task ID based on the task name (dummy implementation)
    private int getTaskId(String taskName) {
        // Dummy implementation to return task ID based on task name
        return switch (taskName) {
            case "Cleaning" -> 101;
            case "Cooking" -> 102;
            case "Gardening" -> 103;
            case "Painting" -> 104;
            case "Teaching" -> 105;
            default -> 0; // Return 0 if task name is not found
        };
    }
}