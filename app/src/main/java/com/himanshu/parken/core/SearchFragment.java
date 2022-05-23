package com.himanshu.parken.core;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.himanshu.parken.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap googleMapMain;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentContainerView_fragment_search_map);

        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                googleMapMain = googleMap;

                checkGps();

                LatLng latLngDefault = new LatLng(30.316207100760515, 78.07107054376682);
                MarkerOptions markerOptionsDefault = new MarkerOptions();
                markerOptionsDefault.position(latLngDefault);
                markerOptionsDefault.title("Default Location");
                googleMapMain.addMarker(markerOptionsDefault);
                CameraUpdate cameraUpdateDefault = CameraUpdateFactory.newLatLngZoom(latLngDefault, 15);
                googleMapMain.animateCamera(cameraUpdateDefault);

                googleMapMain.getUiSettings().setZoomControlsEnabled(true);
                googleMapMain.getUiSettings().setCompassEnabled(true);

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

                googleMapMain.getUiSettings().setMyLocationButtonEnabled(true);
                googleMapMain.setMyLocationEnabled(true);


                googleMapMain.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title("Here");
                        googleMapMain.clear();
                        googleMapMain.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                        googleMapMain.addMarker(markerOptions);

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


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_search, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menuItem_fragment_map_none) {
            googleMapMain.setMapType(GoogleMap.MAP_TYPE_NONE);
        } else if (id == R.id.menuItem_fragment_map_normal) {
            googleMapMain.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.menuItem_fragment_map_satellite) {
            googleMapMain.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.menuItem_fragment_map_hybrid) {
            googleMapMain.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.menuItem_fragment_map_terrain) {
            googleMapMain.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkGps() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        LocationSettingsRequest.Builder locationSettingBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsResponseTask = LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(locationSettingBuilder.build());

        locationSettingsResponseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse locationSettingsResponse = task.getResult(ApiException.class);
                    Toast.makeText(getActivity(), "GPS is enabled.", Toast.LENGTH_SHORT).show();
                    getCurrentUpdate();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(requireActivity(), 101);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE){
                        Toast.makeText(getActivity(), "GPS not available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101){
            if(resultCode == RESULT_OK){
                Toast.makeText(getActivity(), "now GPS is enabled.", Toast.LENGTH_SHORT).show();
            }
        }

    }
    @SuppressLint("MissingPermission")
    private void getCurrentUpdate(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                //Toast.makeText(getActivity(), "Location " + latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
            }
        }, Looper.getMainLooper());
    }

}