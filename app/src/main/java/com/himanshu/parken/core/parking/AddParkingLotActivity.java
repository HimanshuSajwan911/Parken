package com.himanshu.parken.core.parking;

import static com.himanshu.parken.core.map.Util.encodeCoordinates;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.FirebaseDatabase;
import com.himanshu.parken.R;

import java.util.HashMap;
import java.util.Objects;

public class AddParkingLotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking_lot);

        Button bt = findViewById(R.id.button_addprk);
        bt.setOnClickListener(v -> addParkingLot());


    }

    private void addParkingLot() {

        EditText etLotName = findViewById(R.id.textInputEditText_add_parking_lot_name);
        EditText etLongitude = findViewById(R.id.textInputEditText_add_parking_lot_latitude);
        EditText etLatitude = findViewById(R.id.textInputEditText_add_parking_lot_longitude);
        EditText etSlotCount = findViewById(R.id.textInputEditText_add_parking_lot_slot_count);

        String lotName = etLotName.getText().toString();
        double latitude = Double.parseDouble(etLongitude.getText().toString());
        double longitude = Double.parseDouble(etLatitude.getText().toString());
        int slotCount = Integer.parseInt(etSlotCount.getText().toString());

        Slot slot = new Slot();
        slot.setStatus(ParkingLot.PARKING_AVAILABLE);
        slot.setBookingDateTime("");
        slot.setExitDateTime("");
        slot.setUserId("");

        HashMap<String, Slot> lotMap = new HashMap<>();

        for (int i = 1; i <= slotCount; ++i) {
            lotMap.put("P" + i, slot);
        }

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLotName(lotName);
        parkingLot.setLatitude(latitude);
        parkingLot.setLongitude(longitude);
        parkingLot.setSlotCount(slotCount);
        parkingLot.setSlots(lotMap);

        FirebaseDatabase.getInstance().getReference("Parking")
                .child(encodeCoordinates(new LatLng(latitude, longitude)))
                .setValue(parkingLot).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddParkingLotActivity.this, "Parking Lot added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddParkingLotActivity.this, "ERROR: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
