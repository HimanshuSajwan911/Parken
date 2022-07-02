package com.himanshu.parken.core.parking;

public class Slot {

    private Boolean status;
    private String userId;
    private String bookingDateTime;
    private String exitDateTime;
    private String reserverName;
    private String vehicleNumber;

    public Slot() {
    }

    public Slot(Boolean status, String userId, String bookingDateTime, String exitDateTime, String reserverName, String vehicleNumber) {
        this.status = status;
        this.userId = userId;
        this.bookingDateTime = bookingDateTime;
        this.exitDateTime = exitDateTime;
        this.reserverName = reserverName;
        this.vehicleNumber = vehicleNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public String getExitDateTime() {
        return exitDateTime;
    }

    public void setExitDateTime(String exitDateTime) {
        this.exitDateTime = exitDateTime;
    }

    public String getReserverName() {
        return reserverName;
    }

    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }
}
