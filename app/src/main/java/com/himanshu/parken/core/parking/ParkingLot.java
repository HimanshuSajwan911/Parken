package com.himanshu.parken.core.parking;

import java.util.HashMap;

public class ParkingLot {

    public static final Boolean PARKING_TAKEN = true;
    public static final Boolean PARKING_AVAILABLE = false;

    private int LotCount;
    private HashMap<String, Boolean> Lots;
    private double Latitude, Longitude;

    public ParkingLot(){

    }

    public ParkingLot(int lotCount, HashMap<String, Boolean> lots, double latitude, double longitude, String lt) {
        LotCount = lotCount;
        Lots = lots;
        Latitude = latitude;
        Longitude = longitude;
    }

    public int getLotCount() {
        return LotCount;
    }

    public void setLotCount(int lotCount) {
        LotCount = lotCount;
    }

    public HashMap<String, Boolean> getLots() {
        return Lots;
    }

    public void setLots(HashMap<String, Boolean> lots) {
        Lots = lots;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
