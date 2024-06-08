package com.example.backup;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Attachment {
    private int incidentId;
    private String filePath;
    private String fileType;
    private int uploadedBy;
    private LocalDate dateUploaded;

    // Getters and setters
    public int getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(int incidentId) {
        this.incidentId = incidentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDate getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(LocalDate dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public void saveAttachment(DatabaseConnection database) throws SQLException, IOException {
        String query = "INSERT INTO IncidentAttachment (IncidentID, FilePath, FileType, UploadedBy, DateUploaded) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = database.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, getIncidentId());
            pstmt.setString(2, getFilePath());
            pstmt.setString(3, getFileType());
            pstmt.setInt(4, getUploadedBy());
            pstmt.setDate(5, java.sql.Date.valueOf(getDateUploaded()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int attachmentId = generatedKeys.getInt(1);
                    System.out.println("Attachment saved with ID: " + attachmentId);
                } else {
                    throw new SQLException("Creating attachment failed, no ID obtained.");
                }
            }
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}

