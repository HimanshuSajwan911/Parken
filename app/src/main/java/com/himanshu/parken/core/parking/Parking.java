package com.himanshu.parken.core.parking;

import java.util.ArrayList;

public class Parking {

    private ArrayList<ParkingLot> parkings;

    public Parking() {
    }

    public Parking(ArrayList<ParkingLot> parkings) {
        this.parkings = parkings;
    }

    public ArrayList<ParkingLot> getParkings() {
        return parkings;
    }

    public void setParkings(ArrayList<ParkingLot> parkings) {
        this.parkings = parkings;
    }
}
