package com.runtracker.entity; // an entity is a class that maps directly to a database table

import jakarta.persistence.*; // JPA library * imports all - @entity,table,id,generatedvalue,column
//the above are the annotations which tells JPA to map this class to MySQL table

@Entity//tells JPA
//"This class represents a database table. Every object of this class = one row in that table."
@Table(name = "run_records")
//Tells JPA what to name the table in MySQL.
//Without this, JPA uses the class name as table name — RunRecord becomes table run_record

//CREATE TABLE IF NOT EXISTS run_records (...)
//You never write this SQL yourself — JPA does it automatically because of ddl-auto: update in application.yml.
public class RunRecord {

    @Id//TO MARK PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Tells MySQL to auto-generate this value. Every time you insert a new row, MySQL automatically assigns the next number:

    private Long id;

    private String userId;
    private String runId;
    private double latitude;
    private double longitude;
    private double pace;
    private double distanceKm;
    private long timestamp;

    // Empty constructor — required by JPA
    public RunRecord() {}

    // All args constructor
    public RunRecord(String userId, String runId, double latitude,
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

    // Getters
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getRunId() { return runId; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public double getPace() { return pace; }
    public double getDistanceKm() { return distanceKm; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRunId(String runId) { this.runId = runId; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setPace(double pace) { this.pace = pace; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "RunRecord{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", runId='" + runId + '\'' +
                ", pace=" + pace +
                ", distanceKm=" + distanceKm +
                '}';
    }
}
