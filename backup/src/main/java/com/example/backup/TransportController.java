package com.example.backup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransportController {

    @FXML
    private TableView<TransportRequest> transportTableView;
    @FXML
    private TableColumn<TransportRequest, Integer> requestIdColumn;
    @FXML
    private TableColumn<TransportRequest, Integer> inmateIdColumn;
    @FXML
    private TableColumn<TransportRequest, String> inmateNameColumn;
    @FXML
    private TableColumn<TransportRequest, String> destinationColumn;
    @FXML
    private TableColumn<TransportRequest, LocalDate> dateColumn;
    @FXML
    private TableColumn<TransportRequest, LocalTime> timeColumn;
    @FXML
    private TableColumn<TransportRequest, String> statusColumn;
    @FXML
    private TextField inmateIdField;
    @FXML
    private TextField inmateNameField;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker dateField;
    @FXML
    private TextField timeField;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        requestIdColumn.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        inmateIdColumn.setCellValueFactory(new PropertyValueFactory<>("inmateId"));
        inmateNameColumn.setCellValueFactory(new PropertyValueFactory<>("inmateName"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTransportRequests();
    }

    @FXML
    private void handleSaveRequest() {
        int inmateId = Integer.parseInt(inmateIdField.getText());
        String inmateName = inmateNameField.getText();
        String destination = destinationField.getText();
        LocalDate date = dateField.getValue();
        LocalTime time = LocalTime.parse(timeField.getText());
        String status = "pending"; // Assuming the status is "pending" when a new request is created

        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertSQL = "INSERT INTO TransportRequests (InmateID, InmateName, Destination, Date, Time, Status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, inmateId);
            preparedStatement.setString(2, inmateName);
            preparedStatement.setString(3, destination);
            preparedStatement.setDate(4, java.sql.Date.valueOf(date));
            preparedStatement.setTime(5, java.sql.Time.valueOf(time));
            preparedStatement.setString(6, status);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int requestId = generatedKeys.getInt(1);
                        TransportRequest request = new TransportRequest(requestId, inmateId, inmateName, destination, date, time, status);

                        // Add the new request to the TableView
                        transportTableView.getItems().add(request);

                        // Show success message
                        showAlert("Record added successfully");
                    } else {
                        throw new SQLException("Creating transport request failed, no ID obtained.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Clear the input fields after saving
        inmateIdField.clear();
        inmateNameField.clear();
        destinationField.clear();
        dateField.setValue(null);
        timeField.clear();
    }

    @FXML
    private void handleBackButton() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close(); // Close the current stage
        SceneLoader.loadScene("hello-view.fxml", new Stage());
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadTransportRequests() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM TransportRequests");
             ResultSet resultSet = statement.executeQuery()) {

            ObservableList<TransportRequest> transportRequests = FXCollections.observableArrayList();

            while (resultSet.next()) {
                int requestId = resultSet.getInt("RequestID");
                int inmateId = resultSet.getInt("InmateID");
                String inmateName = resultSet.getString("InmateName");
                String destination = resultSet.getString("Destination");
                LocalDate date = resultSet.getDate("Date").toLocalDate();
                LocalTime time = resultSet.getTime("Time").toLocalTime();
                String status = resultSet.getString("Status");

                TransportRequest request = new TransportRequest(requestId, inmateId, inmateName, destination, date, time, status);
                transportRequests.add(request);
            }

            transportTableView.setItems(transportRequests);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
