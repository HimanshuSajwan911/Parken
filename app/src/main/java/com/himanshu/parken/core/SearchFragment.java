package com.himanshu.parken.core;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.himanshu.parken.R;
import com.himanshu.parken.core.booking.BookingActivity;
import com.himanshu.parken.core.parking.ParkingLot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private boolean isPermissionGranted;
    private static Location lastLocation;
    private static LatLng latLngBooking;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mGoogleMap;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public String bestProvider;
    public LocationManager locationManager;
    private SupportMapFragment supportMapFragment;

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

        supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentContainerView_fragment_search_map);

        checkLocationPermission();

        if(isPermissionGranted) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            loadMap();
        }

        return view;
    }

    @SuppressLint("MissingPermission")
    private void mapReady() {
        checkGps();

        lastLocation = new Location("");
        lastLocation.setLatitude(0);
        lastLocation.setLongitude(0);

        getLastLocation();

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);

        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //loadParking();

        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // Triggered when user click any marker on the map

                Toast.makeText(getActivity(), "going to Booking", Toast.LENGTH_SHORT).show();

                latLngBooking = marker.getPosition();

                Intent intent = new Intent(getActivity(), BookingActivity.class);
                startActivity(intent);

                return false;
            }
        });

        moveToCurrentLocation();

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                //mGoogleMap.clear();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                //mGoogleMap.addMarker(markerOptions);

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


    private void loadMap(){
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                mGoogleMap = googleMap;
                //Places.initialize(getContext(),"@string/API_KEY");

                mapReady();
            }
        });
    }

    private void moveToCurrentLocation(){
        LatLng latLngDefault = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        MarkerOptions markerOptionsDefault = new MarkerOptions();
        markerOptionsDefault.position(latLngDefault);
        markerOptionsDefault.title("Your Location");
        mGoogleMap.addMarker(markerOptionsDefault);
        CameraUpdate cameraUpdateDefault = CameraUpdateFactory.newLatLngZoom(latLngDefault, 15);
        mGoogleMap.animateCamera(cameraUpdateDefault);
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
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        } else if (id == R.id.menuItem_fragment_map_normal) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else if (id == R.id.menuItem_fragment_map_satellite) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else if (id == R.id.menuItem_fragment_map_hybrid) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        } else if (id == R.id.menuItem_fragment_map_terrain) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkGps() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(8000);
        locationRequest.setFastestInterval(5000);

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
                    //Toast.makeText(getActivity(), "GPS is enabled.", Toast.LENGTH_SHORT).show();
                    //getCurrentUpdate();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(requireActivity(), 101);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(getActivity(), "GPS not available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }


    @SuppressLint("MissingPermission")
    private void getCurrentUpdate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Toast.makeText(getActivity(), "Location " + latitude + " : " + longitude, Toast.LENGTH_SHORT).show();
            }
        }, Looper.getMainLooper());
    }

    private boolean isFineLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isCoarseLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkLocationPermission() {
        Dexter.withContext(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPermissionGranted = true;
                Toast.makeText(getActivity(), "Permission Granted.", Toast.LENGTH_SHORT).show();
                loadMap();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intentSetting = new Intent();
                intentSetting.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), "");
                intentSetting.setData(uri);
                startActivity(intentSetting);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

        @SuppressLint("MissingPermission")
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            Toast.makeText(getActivity(), "GPS is on", Toast.LENGTH_SHORT).show();
            lastLocation = location;
            //Toast.makeText(getActivity(), "Location: " + lastLocation.getLatitude() + " : " + lastLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        } else {
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) this);
        }

    }

    public static LatLng getSelectedLatLng(){
        return latLngBooking;
    }

}