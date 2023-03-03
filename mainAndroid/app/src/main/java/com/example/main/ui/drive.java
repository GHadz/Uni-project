package com.example.main.ui;
//class for the ride format that we're gonna list with all the needed details
public class Drive {
    private String driveId,driverName,date,destination,source,status;
    private int passengerNbr,capacity;

    //used in the passenger fragment so the passenger can see the ride he requested to join
    public Drive(String driveId, String driverName, String date, String source, String destination) {
        this.driveId = driveId;
        this.driverName = driverName;
        this.date = date;
        this.destination = destination;
        this.source = source;
    }
    //used in the driver fragment to see the driver details for the request
    public Drive(String driveId, String date, int pass, String stat,int capacity) {
        this.driveId = driveId;
        this.date =date;
        this.passengerNbr = pass;
        this.status = stat;
        this.capacity = capacity;

    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDriveId() {
        return driveId;
    }

    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPassengerNbr() {
        return passengerNbr;
    }

    public void setPassengerNbr(int passengerNbr) {
        this.passengerNbr = passengerNbr;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
