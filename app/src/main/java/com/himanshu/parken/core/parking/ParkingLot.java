package com.himanshu.parken.core.parking;

import java.util.HashMap;

public class ParkingLot {

    public static final Boolean PARKING_TAKEN = true;
    public static final Boolean PARKING_AVAILABLE = false;

    private int lotCount;
    private HashMap<String, Lot> lots;
    private double latitude, longitude;

    public ParkingLot(){

    }

    public ParkingLot(double latitude, double longitude, int lotCount, HashMap<String, Lot> lots) {
        this.lotCount = lotCount;
        this.lots = lots;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLotCount() {
        return lotCount;
    }

    public void setLotCount(int lotCount) {
        this.lotCount = lotCount;
    }

    public HashMap<String, Lot> getLots() {
        return lots;
    }

    public void setLots(HashMap<String, Lot> lots) {
        this.lots = lots;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
