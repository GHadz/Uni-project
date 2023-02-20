package com.example.main.ui;
//class fort the requests in request recycler
public class Requests {

    private String requestID;
    private String driverName;
    private String date;
    private String destination;
    private String source;

    public Requests(String requestID, String driverName, String date, String destination, String source) {
        this.requestID = requestID;
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

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}