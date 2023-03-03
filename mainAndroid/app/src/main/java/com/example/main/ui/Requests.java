package com.example.main.ui;
//class fort the requests in request recycler
public class Requests {

    private String requestID,details;
    private String driverName,date,destination,source;
    private String passengerName,phone;
    private int passRating;

    public Requests(String requestID, String driverName, String date, String destination, String source) {
        this.requestID = requestID;
        this.driverName = driverName;
        this.date = date;
        this.destination = destination;
        this.source = source;
    }
    public Requests(String requestID, String passengerName,int rating,String details,String phone) {
        this.requestID = requestID;
        this.passengerName = passengerName;
        this.passRating = rating;
        this.details = details;
        this.phone = phone;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getPassRating() {
        return passRating;
    }

    public void setPassRating(int passRating) {
        this.passRating = passRating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}