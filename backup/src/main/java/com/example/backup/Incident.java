package com.example.backup;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;

public class Incident {

    @FXML
    private TextField incidentIdField;

    @FXML
    private TextField officerIdField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField timeField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField natureField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button attachFileButton;

    @FXML
    private TextArea witnessesArea;

    private File attachedFile;

    private DatabaseConnection database = new DatabaseConnection();

    @FXML
    private void handleAttachFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Attach File");
        Stage stage = (Stage) attachFileButton.getScene().getWindow();
        attachedFile = fileChooser.showOpenDialog(stage);
        if (attachedFile != null) {
            showAlert("File Attached", "File attached: " + attachedFile.getName());
        }
    }

    @FXML
    private void handleSubmitIncident() {
        try {
            int incidentId = Integer.parseInt(incidentIdField.getText());
            int officerId = Integer.parseInt(officerIdField.getText());
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeField.getText());
            String location = locationField.getText();
            String nature = natureField.getText();
            String description = descriptionArea.getText();
            String witnesses = witnessesArea.getText();

            if (description.isEmpty() || location.isEmpty() || nature.isEmpty() || date == null || time == null) {
                showAlert("Error", "All fields must be filled out.");
                return;
            }

            logIncident(incidentId, officerId, date, time, location, nature, description);
            if (attachedFile != null) {
                Attachment attachment = new Attachment();
                attachment.setIncidentId(incidentId);
                attachment.setFilePath(attachedFile.getAbsolutePath());
                attachment.setFileType(Attachment.getFileExtension(attachedFile));
                attachment.setUploadedBy(officerId);
                attachment.setDateUploaded(LocalDate.now());
                attachment.saveAttachment(database);
            }
            Witness.saveWitnesses(incidentId, witnesses, database);

            showAlert("Incident Reported", "The incident has been reported successfully.");

        } catch (NumberFormatException e) {
            showAlert("Error", "Incident ID and Officer ID must be numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while reporting the incident.");
        }
    }

    private void logIncident(int incidentId, int officerId, LocalDate date, LocalTime time, String location, String nature, String description) throws SQLException {
        String query = "INSERT INTO Incident (IncidentID, OfficerID, Date, Time, Location, Nature, Description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, incidentId);
            pstmt.setInt(2, officerId);
            pstmt.setDate(3, java.sql.Date.valueOf(date));
            pstmt.setTime(4, Time.valueOf(time));
            pstmt.setString(5, location);
            pstmt.setString(6, nature);
            pstmt.setString(7, description);
            pstmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

