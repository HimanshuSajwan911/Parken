package com.himanshu.parken.home;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.himanshu.parken.R;
import com.himanshu.parken.core.booking.Booking;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    public static SlotBookingFirebaseRecyclerAdapter currentSlotBookingFirebaseRecyclerAdapter, previousSlotBookingFirebaseRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
        }

        RecyclerView recyclerViewCurrentBooking = view.findViewById(R.id.recyclerView_fragment_home_current_booking);
        recyclerViewCurrentBooking.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        Query queryCurrentBooking = FirebaseDatabase.getInstance().getReference("User").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("booking").orderByChild("status").equalTo(true);

        FirebaseRecyclerOptions<Booking> optionsCurrentBooking = new FirebaseRecyclerOptions.Builder<Booking>()
                        .setQuery(queryCurrentBooking, Booking.class)
                        .build();

        currentSlotBookingFirebaseRecyclerAdapter = new SlotBookingFirebaseRecyclerAdapter(optionsCurrentBooking);
        recyclerViewCurrentBooking.setAdapter(currentSlotBookingFirebaseRecyclerAdapter);


        RecyclerView recyclerViewPreviousBooking = view.findViewById(R.id.recyclerView_fragment_home_previous_booking);
        recyclerViewPreviousBooking.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        Query queryPreviousBooking = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()).child("booking").orderByChild("status").equalTo(false);

        FirebaseRecyclerOptions<Booking> optionsPreviousBooking = new FirebaseRecyclerOptions.Builder<Booking>()
                .setQuery(queryPreviousBooking, Booking.class)
                .build();

        previousSlotBookingFirebaseRecyclerAdapter = new SlotBookingFirebaseRecyclerAdapter(optionsPreviousBooking);
        recyclerViewPreviousBooking.setAdapter(previousSlotBookingFirebaseRecyclerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        currentSlotBookingFirebaseRecyclerAdapter.startListening();
        previousSlotBookingFirebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        currentSlotBookingFirebaseRecyclerAdapter.stopListening();
        previousSlotBookingFirebaseRecyclerAdapter.startListening();
    }

}