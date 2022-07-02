package com.himanshu.parken.core.booking;


import static com.himanshu.parken.core.map.Util.encodeCoordinates;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.himanshu.parken.R;
import com.himanshu.parken.core.parking.ParkingLot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class BookingDialog extends DialogFragment {

    private EditText etReserverName, etVehicleNumber, etExitDate, etExitTime;

    private int mYear, mMonth, mDay, mHour, mMinute, mPosition;
    private Calendar mCalendar = Calendar.getInstance();

    private ParkingLot mParkingLot;

    public BookingDialog(){

    }

    public BookingDialog(ParkingLot parkingLot, int position){
        mParkingLot = parkingLot;
        mPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_booking, container, false);

        etReserverName = view.findViewById(R.id.textInputEditText_dialog_booking_reserver_name);
        etVehicleNumber = view.findViewById(R.id.textInputEditText_dialog_booking_vehicle_number);
        etExitDate = view.findViewById(R.id.textInputEditText_dialog_booking_exit_date);
        etExitTime = view.findViewById(R.id.textInputEditText_dialog_booking_exit_time);

        Button btCancel = view.findViewById(R.id.button_dialog_booking_cancel);
        Button btSubmit = view.findViewById(R.id.button_dialog_booking_submit);

        etExitDate.setFocusable(false);
        etExitTime.setFocusable(false);
        etExitDate.setKeyListener(null);
        etExitTime.setKeyListener(null);

        etExitDate.setOnClickListener(v -> {
            DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> {
                mYear = year;
                mMonth = month;
                mDay = day;

                String yearString = (year < 10) ? ("0" + year) : String.valueOf(year);
                String monthString = (month < 10) ? ("0" + month) : String.valueOf(month);
                String dayString = (day < 10) ? ("0" + day) : String.valueOf(day);

                String exitDate = dayString + "-" + monthString + "-" + yearString;
                etExitDate.setText(exitDate);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), onDateSetListener, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setTitle("Select Exit Date");
            datePickerDialog.show();
        });

        etExitTime.setOnClickListener(v -> {

            TimePickerDialog.OnTimeSetListener onTimeSetListener = (timePicker, hourOfDay, minute) -> {
                mHour = hourOfDay;
                mMinute = minute;

                String hourString = (hourOfDay < 10) ? ("0" + hourOfDay) : String.valueOf(hourOfDay);
                String minuteString = (minute < 10) ? ("0" + minute) : String.valueOf(minute);

                String exitTime = hourString + ":" + minuteString + ":" + "00";
                etExitTime.setText(exitTime);
            };

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), true);
            timePickerDialog.setTitle("Select Exit Time:");
            timePickerDialog.show();
        });

        btCancel.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookSlot();
            }
        });


        return view;
    }


    private void bookSlot(){

        Date date = new Date();
        SimpleDateFormat lotStatusDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentDateTime = lotStatusDateFormat.format(date);

        String userId = FirebaseAuth.getInstance().getUid();

        String exitDate = etExitDate.getText().toString();
        String exitTime = etExitTime.getText().toString();
        String exitDateTime = exitDate + " " + exitTime;

        String reserverName = etReserverName.getText().toString();
        String vehicleNumber = etVehicleNumber.getText().toString();

        if (TextUtils.isEmpty(reserverName)) {
            etReserverName.setError("Reserver Name required!");
            etReserverName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(vehicleNumber)) {
            etVehicleNumber.setError("Vehicle Number required!");
            etVehicleNumber.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(exitDate)) {
            etExitDate.setError("Exit Date required!");
            etExitDate.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(exitTime)) {
            etExitTime.setError("Exit Time required!");
            etExitTime.requestFocus();
            return;
        }

        HashMap updatedLotBooking = new HashMap<>();
        updatedLotBooking.put("bookingDateTime", currentDateTime);
        updatedLotBooking.put("exitDateTime", exitDateTime);
        updatedLotBooking.put("status", ParkingLot.PARKING_TAKEN);
        updatedLotBooking.put("userId", userId);
        updatedLotBooking.put("reserverName", reserverName);
        updatedLotBooking.put("vehicleNumber", vehicleNumber);

        BookingActivity.databaseReferenceParking.updateChildren(updatedLotBooking).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Your lot is booked.", Toast.LENGTH_SHORT).show();
                getDialog().dismiss();
            } else {
                Toast.makeText(getContext(), "some error occurred\ntry again later!", Toast.LENGTH_SHORT).show();

            }
        });

        String parkingLotId = encodeCoordinates(mParkingLot.getLatitude(), mParkingLot.getLongitude());
        String slotId = "P" + (mPosition + 1);
        String parkingSlotId = parkingLotId + ":" + slotId;

        HashMap updateUserBooking = new HashMap();
        updateUserBooking.put("bookingDateTime", currentDateTime);
        updateUserBooking.put("status", ParkingLot.PARKING_TAKEN);
        updateUserBooking.put("exitDateTime", exitDateTime);
        updateUserBooking.put("reserverName", reserverName);
        updateUserBooking.put("slotId", slotId);
        updateUserBooking.put("lotName", mParkingLot.getLotName());
        updateUserBooking.put("vehicleNumber", vehicleNumber);

        BookingActivity.databaseReferenceUser.updateChildren(updateUserBooking);
    }


}
