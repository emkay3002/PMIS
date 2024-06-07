package com.example.backup;

import javafx.beans.property.*;

public class FacilityMaintenance {
    private final IntegerProperty requestId;
    private final StringProperty requestNature;
    private final StringProperty location;
    private final StringProperty comments;
    private final StringProperty urgency;
    private final StringProperty assignedStaff;
    private final StringProperty progress;

    public FacilityMaintenance(int requestId, String requestNature, String location, String comments, String urgency, String assignedStaff, String progress) {
        this.requestId = new SimpleIntegerProperty(requestId);
        this.requestNature = new SimpleStringProperty(requestNature);
        this.location = new SimpleStringProperty(location);
        this.comments = new SimpleStringProperty(comments);
        this.urgency = new SimpleStringProperty(urgency);
        this.assignedStaff = new SimpleStringProperty(assignedStaff);
        this.progress = new SimpleStringProperty(progress);
    }

    public int getRequestId() {
        return requestId.get();
    }

    public IntegerProperty requestIdProperty() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId.set(requestId);
    }

    public String getRequestNature() {
        return requestNature.get();
    }

    public StringProperty requestNatureProperty() {
        return requestNature;
    }

    public void setRequestNature(String requestNature) {
        this.requestNature.set(requestNature);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getComments() {
        return comments.get();
    }

    public StringProperty commentsProperty() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments.set(comments);
    }

    public String getUrgency() {
        return urgency.get();
    }

    public StringProperty urgencyProperty() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency.set(urgency);
    }

    public String getAssignedStaff() {
        return assignedStaff.get();
    }

    public StringProperty assignedStaffProperty() {
        return assignedStaff;
    }

    public void setAssignedStaff(String assignedStaff) {
        this.assignedStaff.set(assignedStaff);
    }

    public String getProgress() {
        return progress.get();
    }

    public StringProperty progressProperty() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress.set(progress);
    }
}
