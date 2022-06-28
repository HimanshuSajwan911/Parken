package com.himanshu.parken.core.parking;

public class Lot {

    private Boolean status;
    private String userId;
    private String startDateTime;
    private String endDateTime;

    public Lot() {
    }

    public Lot(Boolean status, String userId, String startDateTime, String endDateTime) {
        this.status = status;
        this.userId = userId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
