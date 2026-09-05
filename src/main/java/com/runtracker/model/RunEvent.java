package com.runtracker.model;

public class RunEvent {

    private String userId;
    private String runId;
    private double latitude;
    private double longitude;
    private double pace;
    private double distanceKm;
    private long timestamp;

    // Empty constructor — needed by Kafka to deserialize JSON back to object
    public RunEvent() {}

    // All args constructor — used when creating a new RunEvent in producer
    public RunEvent(String userId, String runId, double latitude,
                    double longitude, double pace,
                    double distanceKm, long timestamp) {
        this.userId = userId;
        this.runId = runId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pace = pace;
        this.distanceKm = distanceKm;
        this.timestamp = timestamp;
    }

    // Getters — allow other classes to READ private fields
    public String getUserId() { return userId; }
    public String getRunId() { return runId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getPace() { return pace; }
    public double getDistanceKm() { return distanceKm; }
    public long getTimestamp() { return timestamp; }

    // Setters — allow other classes to WRITE private fields
    public void setUserId(String userId) { this.userId = userId; }
    public void setRunId(String runId) { this.runId = runId; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setPace(double pace) { this.pace = pace; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    // toString — used for logging, prints RunEvent details in console
    @Override
    public String toString() {
        return "RunEvent{" +
                "userId='" + userId + '\'' +
                ", runId='" + runId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", pace=" + pace +
                ", distanceKm=" + distanceKm +
                ", timestamp=" + timestamp +
                '}';
    }
}
