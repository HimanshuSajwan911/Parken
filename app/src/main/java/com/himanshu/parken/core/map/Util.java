package com.himanshu.parken.core.map;

import com.google.android.gms.maps.model.LatLng;

public class Util {

    public static String encodeCoordinates(LatLng latLng) {

        double lat = latLng.latitude;
        double lng = latLng.longitude;

        return encodeCoordinates(lat, lng);
    }

    public static String encodeCoordinates(double latitude, double longitude) {

        String latLngString = latitude + ":" + longitude;
        latLngString = latLngString.replace(".", "_");
        latLngString = latLngString.replace(" ", "");

        return latLngString;
    }

    public static LatLng decodeCoordinates(String coordinate) {
        String[] latLng = coordinate.split(":");

        String lat = latLng[0].replace("_", ".");
        String lng = latLng[1].replace("_", ".");

        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);

        return new LatLng(latitude, longitude);
    }

}
