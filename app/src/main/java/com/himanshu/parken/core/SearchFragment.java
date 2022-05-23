package com.himanshu.parken.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.himanshu.parken.R;

import java.io.IOException;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_search_fragment_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                LatLng latLngDefault = new LatLng(30.316207100760515, 78.07107054376682);
                MarkerOptions markerOptionsDefault = new MarkerOptions();
                markerOptionsDefault.position(latLngDefault);
                markerOptionsDefault.title("Default Location");
                googleMap.addMarker(markerOptionsDefault);
                CameraUpdate cameraUpdateDefault = CameraUpdateFactory.newLatLngZoom(latLngDefault, 15);
                googleMap.animateCamera(cameraUpdateDefault);

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setCompassEnabled(true);

                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Location Permission Needed")
                            .setMessage("This app needs the Location permission, please accept to use location functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(requireActivity(),
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_LOCATION);
                                }
                            })
                            .create()
                            .show();
                    Toast.makeText(getActivity(), "ERROR: Fine Location", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                    Toast.makeText(getActivity(), "ERROR: Coarse Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                googleMap.setMyLocationEnabled(true);


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Here");
                        googleMap.clear();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        googleMap.addMarker(markerOptions);

                        Geocoder geocoder = new Geocoder(getActivity());

                        try {
                            ArrayList<Address> addressArrayList = (ArrayList<Address>) geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
                            Toast.makeText(getActivity(), "Add: " + addressArrayList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                        } catch (IOException ioException) {
                            Toast.makeText(getActivity(), ioException.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        return view;
    }
}