package com.himanshu.parken.database;

import com.himanshu.parken.core.parking.ParkingLot;

public interface OnGetDataListener {
    void onSuccess(ParkingLot parkingLot);
    void onStart();
    void onFailure();
}
