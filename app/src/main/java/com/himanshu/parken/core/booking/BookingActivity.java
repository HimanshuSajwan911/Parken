package com.himanshu.parken.core.booking;

import static com.himanshu.parken.core.map.Util.encodeCoordinates;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.parken.R;
import com.himanshu.parken.core.map.SearchFragment;
import com.himanshu.parken.core.parking.Slot;
import com.himanshu.parken.core.parking.ParkingLot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class BookingActivity extends AppCompatActivity implements OnSlotClickListener {

    private static final String TAG = "BookingActivity";

    public static String parkingLotId, lotId;

    private HashMap<String, Slot> slots;

    public static  DatabaseReference databaseReferenceParking, databaseReferenceUser;

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private ParkingLot mParkingLot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Booking");

        slots = new HashMap<>();
        recyclerView = findViewById(R.id.recyclerView_booking);

        TextView tvBooking = findViewById(R.id.textView_booking);

        mParkingLot = SearchFragment.selectedParkingLot;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(BookingActivity.this, 3, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        LatLng selectedLatLng = SearchFragment.getSelectedLatLng();
        tvBooking.setText(mParkingLot.getLotName());

        String latLngEncodedString = encodeCoordinates(selectedLatLng);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Parking");

        Query query = reference.orderByKey().equalTo(latLngEncodedString);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                slots.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ParkingLot parkingLot = dataSnapshot.getValue(ParkingLot.class);
                    assert parkingLot != null;
                    slots = parkingLot.getSlots();
                    mParkingLot = parkingLot;
                    bookingAdapter = new BookingAdapter(BookingActivity.this, slots, BookingActivity.this);
                    recyclerView.setAdapter(bookingAdapter);

                }
                bookingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Toast.makeText(BookingActivity.this, "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d(TAG, "user not logged in.");
                    query.removeEventListener(this);
                }
            }

        });

        /*ParkingDatabase.fetchParkingLot(selectedLatLng, new OnGetDataListener() {
            @Override
            public void onSuccess(ParkingLot parkingLot) {
                if (parkingLot != null) {
                    mParkingLot = parkingLot;
                    bookingAdapter = new BookingAdapter(BookingActivity.this, parkingLot, BookingActivity.this);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(bookingAdapter);
                    bookingAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BookingActivity.this, "Parking Lot error, please try later.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure() {
            }
        });*/

    }

    @Override
    public void onSlotClick(int position, View view) {

        parkingLotId = encodeCoordinates(mParkingLot.getLatitude(), mParkingLot.getLongitude());
        lotId = "P" + (position + 1);
        Slot slot = mParkingLot.getSlots().get(lotId);

        String userId = FirebaseAuth.getInstance().getUid();

        databaseReferenceParking = FirebaseDatabase.getInstance().getReference("Parking").child(parkingLotId).child("slots").child(lotId);
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("User").child(userId).child("booking").child(parkingLotId + ":" + lotId);


        Date date = new Date();
        SimpleDateFormat lotStatusDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = lotStatusDateFormat.format(date);

        if (slot.getStatus() == ParkingLot.PARKING_AVAILABLE) {

            BookingDialog bookingDialog = new BookingDialog(mParkingLot, position);

            bookingDialog.show(getSupportFragmentManager(), "Booking Dialog");

            /*Calendar myCalendar= Calendar.getInstance();

            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    mYear = year;
                    mMonth = month;
                    mDay = day;

                    TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, hourOfDay, minute) -> {
                        mHour = hourOfDay;
                        mMinute = minute;
                        bookSlot();
                    };

                    TimePickerDialog timePickerDialog = new TimePickerDialog(BookingActivity.this, onTimeSetListener, myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), true);
                    timePickerDialog.setTitle("Select Exit Time:");
                    timePickerDialog.show();
                }
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingActivity.this, onDateSetListener, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("Select Exit Date");
            datePickerDialog.show();*/



        } else if (slot.getUserId().equals(FirebaseAuth.getInstance().getUid())) {

            HashMap updatedLotBooking = new HashMap<>();
            updatedLotBooking.put("exitDateTime", currentDateTime);
            updatedLotBooking.put("status", ParkingLot.PARKING_AVAILABLE);

            databaseReferenceParking.updateChildren(updatedLotBooking).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Toast.makeText(BookingActivity.this, "Your lot is canceled.", Toast.LENGTH_SHORT).show();
                }
            });


            HashMap updateUserBooking = new HashMap();
            updateUserBooking.put("exitDateTime", currentDateTime);
            updateUserBooking.put("status", ParkingLot.PARKING_AVAILABLE);

            databaseReferenceUser.updateChildren(updateUserBooking);

        } else {
            Toast.makeText(BookingActivity.this, "Parking is Taken: ", Toast.LENGTH_SHORT).show();
        }
    }

}