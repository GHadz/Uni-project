package com.example.main.ui;
//class for the ride format that we're gonna list with all the needed details
public class drive {
    private String driveId,driverName,date,destination,source;
    public drive(String driveId, String driverName, String date, String source, String destination) {
        this.driveId = driveId;
        this.driverName = driverName;
        this.date = date;
        this.destination = destination;
        this.source = source;
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
}
