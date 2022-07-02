package com.himanshu.parken.home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.himanshu.parken.R;
import com.himanshu.parken.core.booking.Booking;

public class SlotBookingFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<Booking, SlotBookingFirebaseRecyclerAdapter.ViewHolder> {

    public SlotBookingFirebaseRecyclerAdapter(@NonNull FirebaseRecyclerOptions<Booking> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Booking model) {

        holder.tvReserverName.setText("Reserver Name: " + model.getReserverName());
        holder.tvVehicleNumber.setText("Vehicle Number: " + model.getVehicleNumber());
        holder.tvStartDateTime.setText("Booking Date Time: " + model.getBookingDateTime());
        holder.tvExitDateTime.setText("Exit Date Time: " + model.getExitDateTime());
        holder.tvParkingLotName.setText("Parking Lot Name: " + model.getLotName());
        holder.tvSlotId.setText("Slot ID: " + model.getSlotId());

        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked " + holder.getAbsoluteAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);

        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvReserverName, tvParkingLotName, tvVehicleNumber, tvStartDateTime, tvExitDateTime, tvSlotId, tvTimeRemaining;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvReserverName = itemView.findViewById(R.id.textView_booking_item_reserver_name);
            tvVehicleNumber = itemView.findViewById(R.id.textView_booking_item_vehicle_number);
            tvStartDateTime = itemView.findViewById(R.id.textView_booking_item_booking_date_time);
            tvExitDateTime = itemView.findViewById(R.id.textView_booking_item_exit_date_time);
            tvParkingLotName = itemView.findViewById(R.id.textView_booking_item_parking_lot_name);
            tvSlotId = itemView.findViewById(R.id.textView_booking_item_slot_id);
            tvTimeRemaining = itemView.findViewById(R.id.textView_booking_item_time_remaining);
            cardView = itemView.findViewById(R.id.cardView_root);
        }
    }

}
