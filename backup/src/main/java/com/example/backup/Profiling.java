package com.example.backup;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class Profiling {

    @FXML
    private TextField inmateNameField;

    @FXML
    private TextField inmateIdField;

    private DatabaseConnection database = new DatabaseConnection();

    @FXML
    private void handleSubmit() {
        String inmateId = inmateIdField.getText();

        if (inmateId.isEmpty()) {
            showAlert("Error", "Inmate ID cannot be empty.");
            return;
        }

        try {
            ResultSet rs = database.getInmateById(Integer.parseInt(inmateId));

            if (rs.next()) {
                String inmateName = rs.getString("Name");
                String dateOfBirth = rs.getString("DateOfBirth");
                String gender = rs.getString("Gender");
                String incarcerationDate = rs.getString("IncarcerationDate");
                String releaseDate = rs.getString("ReleaseDate");
                String behaviorRecords = rs.getString("BehaviorRecords");
                String rehabilitationStatus = rs.getString("RehabilitationStatus");

                inmateNameField.setText(inmateName);

                String inmateDetails = String.format(
                        "Inmate Name: %s%nDate of Birth: %s%nGender: %s%nIncarceration Date: %s%nRelease Date: %s%nBehavior Records: %s%nRehabilitation Status: %s",
                        inmateName, dateOfBirth, gender, incarcerationDate, releaseDate, behaviorRecords, rehabilitationStatus
                );

                showAlert("Inmate Details", inmateDetails);
            } else {
                showAlert("Error", "No inmate found with ID: " + inmateId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Error", "Inmate ID must be a valid number.");
        }
    }

    @FXML
    private void handleGenerateReport() {
        StringBuilder report = new StringBuilder();
        List<Integer> ages = new ArrayList<>();
        int maleCount = 0;
        int femaleCount = 0;
        int totalCount = 0;

        try {
            ResultSet rs = database.getAllInmates();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (rs.next()) {
                String inmateName = rs.getString("Name");
                String dateOfBirth = rs.getString("DateOfBirth");
                String gender = rs.getString("Gender");
                String incarcerationDate = rs.getString("IncarcerationDate");
                String releaseDate = rs.getString("ReleaseDate");
                String behaviorRecords = rs.getString("BehaviorRecords");
                String rehabilitationStatus = rs.getString("RehabilitationStatus");

                LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
                int age = Period.between(dob, LocalDate.now()).getYears();
                ages.add(age);

                if ("Male".equalsIgnoreCase(gender)) {
                    maleCount++;
                } else if ("Female".equalsIgnoreCase(gender)) {
                    femaleCount++;
                }

                report.append(String.format(
                        "Inmate Name: %s%nDate of Birth: %s%nGender: %s%nIncarceration Date: %s%nRelease Date: %s%nBehavior Records: %s%nRehabilitation Status: %s%n%n",
                        inmateName, dateOfBirth, gender, incarcerationDate, releaseDate, behaviorRecords, rehabilitationStatus
                ));

                totalCount++;
            }

            // Calculate average age
            double averageAge = ages.stream().mapToInt(Integer::intValue).average().orElse(0.0);

            // Append statistics to the report
            report.append(String.format("Total Inmates: %d%n", totalCount));
            report.append(String.format("Male Inmates: %d%n", maleCount));
            report.append(String.format("Female Inmates: %d%n", femaleCount));
            report.append(String.format("Average Age: %.2f%n", averageAge));

            saveReportToFile(report.toString());
            showAlert("Report Generated", "The report has been generated successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Database error: " + e.getMessage());
        }
    }

    private void saveReportToFile(String reportContent) {
        try (FileWriter writer = new FileWriter("InmateReport.txt")) {
            writer.write(reportContent);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the report: " + e.getMessage());
        }
    }

    private void showAlert(String error, String s) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }
}
