package com.example.backup;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CommunityServiceModel {

    private final SimpleIntegerProperty inmateID;
    private final SimpleIntegerProperty taskID;
    private final SimpleStringProperty taskName;
    private final SimpleStringProperty taskStatus;

    public CommunityServiceModel(int inmateID, int taskID, String taskName, String taskStatus) {
        this.inmateID = new SimpleIntegerProperty(inmateID);
        this.taskID = new SimpleIntegerProperty(taskID);
        this.taskName = new SimpleStringProperty(taskName);
        this.taskStatus = new SimpleStringProperty(taskStatus);
    }

    public int getInmateID() {
        return inmateID.get();
    }

    public SimpleIntegerProperty inmateIDProperty() {
        return inmateID;
    }

    public void setInmateID(int inmateID) {
        this.inmateID.set(inmateID);
    }

    public int getTaskID() {
        return taskID.get();
    }

    public SimpleIntegerProperty taskIDProperty() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID.set(taskID);
    }

    public String getTaskName() {
        return taskName.get();
    }

    public SimpleStringProperty taskNameProperty() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    public String getTaskStatus() {
        return taskStatus.get();
    }

    public SimpleStringProperty taskStatusProperty() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus.set(taskStatus);
    }
}

