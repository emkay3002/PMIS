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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CommunityServiceController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button backButton;

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

    private ObservableList<CommunityServiceModel> communityServiceData;

    @FXML
    public void initialize() {
        // Check if buttons are null
        if (backButton == null || viewTaskAssignmentButton == null) {
            System.err.println("FXML components not properly injected");
            return;
        }

        // Set up the columns in the table
        columnInmateID.setCellValueFactory(new PropertyValueFactory<>("inmateID"));
        columnTaskID.setCellValueFactory(new PropertyValueFactory<>("taskID"));
        columnTaskName.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        columnTaskStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));

        // Load data from the database
        loadData();

        // Set up the back button event handler
        backButton.setOnAction(this::handleBackButtonAction);

        // Set up the view task assignment button event handler
        viewTaskAssignmentButton.setOnAction(this::handleViewTaskAssignmentAction);
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

        // Dummy data for now
        communityServiceData.add(new CommunityServiceModel(1, 101, "Cleaning", "In Progress"));
        communityServiceData.add(new CommunityServiceModel(2, 102, "Cooking", "Complete"));
        communityServiceData.add(new CommunityServiceModel(3, 103, "Gardening", "Slacking"));

        // Uncomment and modify the following block to load actual data from the database
        /*
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM CommunityService")) {

            while (resultSet.next()) {
                int inmateID = resultSet.getInt("InmateID");
                int taskID = resultSet.getInt("TaskID");
                String taskName = resultSet.getString("TaskName");
                String taskStatus = resultSet.getString("TaskStatus");

                communityServiceData.add(new CommunityService(inmateID, taskID, taskName, taskStatus));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }
        */

        // Set the data to the table
        communityServiceTable.setItems(communityServiceData);
    }
}
