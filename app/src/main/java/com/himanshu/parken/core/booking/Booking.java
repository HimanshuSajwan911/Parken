package com.himanshu.parken.core.booking;

public class Booking {

    private String reserverName;
    private String lotName;
    private String bookingDateTime;
    private String parkingSlotId;
    private String exitDateTime;
    private String vehicleNumber;
    private Boolean status;

    public Booking() {
    }

    public Booking(String reserverName, String lotName, String bookingDateTime, String parkingSlotId, String exitDateTime, String vehicleNumber, Boolean status) {
        this.reserverName = reserverName;
        this.lotName = lotName;
        this.bookingDateTime = bookingDateTime;
        this.parkingSlotId = parkingSlotId;
        this.exitDateTime = exitDateTime;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
    }

    public Booking(String reserverName, String lotName, String bookingDateTime, String exitDateTime, String vehicleNumber, boolean status) {
        this.reserverName = reserverName;
        this.lotName = lotName;
        this.bookingDateTime = bookingDateTime;
        this.exitDateTime = exitDateTime;
        this.vehicleNumber = vehicleNumber;
        this.status = status;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public String getParkingSlotId() {
        return parkingSlotId;
    }

    public void setParkingSlotId(String parkingSlotId) {
        this.parkingSlotId = parkingSlotId;
    }

    public String getReserverName() {
        return reserverName;
    }

    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
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

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
