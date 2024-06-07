package com.example.backup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

public class MFRSController {

    @FXML
    private Button backButton;
    @FXML
    private TableView<FacilityMaintenance> requestStatusTableView;
    @FXML
    private TableColumn<FacilityMaintenance, Integer> requestIdColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> natureColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> locationColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> urgencyColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> assignedStaffColumn;
    @FXML
    private TableColumn<FacilityMaintenance, String> progressColumn;
    @FXML
    private Button refreshButton;
    @FXML
    private MenuItem sortByRequestId;
    @FXML
    private MenuItem sortByUrgency;
    @FXML
    private MenuItem sortByWaitingRecords;
    @FXML
    private MenuItem sortByInProgressRecords;

    private ObservableList<FacilityMaintenance> maintenanceRequests;

    @FXML
    public void initialize() {
        // Initialize table columns
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        natureColumn.setCellValueFactory(new PropertyValueFactory<>("requestNature"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        urgencyColumn.setCellValueFactory(new PropertyValueFactory<>("urgency"));
        assignedStaffColumn.setCellValueFactory(new PropertyValueFactory<>("assignedStaff"));
        progressColumn.setCellValueFactory(new PropertyValueFactory<>("progress"));

        // Initialize data
        maintenanceRequests = FXCollections.observableArrayList();
        loadRequestsFromDatabase();

        // Set items to table
        requestStatusTableView.setItems(maintenanceRequests);
    }

    private void loadRequestsFromDatabase() {
        // Simulate database loading
        maintenanceRequests.clear();
        maintenanceRequests.addAll(
                new FacilityMaintenance(1, "Plumbing Issue", "Building A", "Leaking pipe", "High", "John Doe", "In Progress"),
                new FacilityMaintenance(2, "Electrical Issue", "Building B", "Power outage", "Medium", "Jane Smith", "Waiting")
        );
    }


    @FXML
    private void handleBackButtonAction() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
        SceneLoader.loadScene("/com/example/backup/Facility.fxml", new Stage());
    }

    @FXML
    private void handleRefreshButtonAction() {
        // Reload data from database
        loadRequestsFromDatabase();
    }

    @FXML
    private void handleSortByRequestIdAction() {
        requestStatusTableView.getSortOrder().clear();
        requestStatusTableView.getSortOrder().add(requestIdColumn);
        requestIdColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    @FXML
    private void handleSortByUrgencyAction() {
        requestStatusTableView.getSortOrder().clear();
        requestStatusTableView.getSortOrder().add(urgencyColumn);
        urgencyColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    @FXML
    private void handleSortByWaitingRecordsAction() {
        // Assuming 'Progress' column indicates the status
        ObservableList<FacilityMaintenance> filteredList = FXCollections.observableArrayList();
        for (FacilityMaintenance request : maintenanceRequests) {
            if ("Waiting".equals(request.getProgress())) {
                filteredList.add(request);
            }
        }
        requestStatusTableView.setItems(filteredList);
    }

    @FXML
    private void handleSortByInProgressRecordsAction() {
        // Assuming 'Progress' column indicates the status
        ObservableList<FacilityMaintenance> filteredList = FXCollections.observableArrayList();
        for (FacilityMaintenance request : maintenanceRequests) {
            if ("In Progress".equals(request.getProgress())) {
                filteredList.add(request);
            }
        }
        requestStatusTableView.setItems(filteredList);
    }
}
