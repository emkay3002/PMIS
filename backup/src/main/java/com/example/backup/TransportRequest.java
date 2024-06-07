package com.example.backup;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransportRequest {
    private int requestId;
    private int inmateId;
    private String inmateName;
    private String destination;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public TransportRequest(int requestId, int inmateId, String inmateName, String destination, LocalDate date, LocalTime time, String status) {
        this.requestId = requestId;
        this.inmateId = inmateId;
        this.inmateName = inmateName;
        this.destination = destination;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getInmateId() {
        return inmateId;
    }

    public void setInmateId(int inmateId) {
        this.inmateId = inmateId;
    }

    public String getInmateName() {
        return inmateName;
    }

    public void setInmateName(String inmateName) {
        this.inmateName = inmateName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
