package com.agh.pum.listazakupow;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Locale;

/**
 * Klasa TrackService z usluga korzystajaca ze wspolrzednych GPS
 */

public class TrackService extends Service {

    private static final String TAG = TrackService.class.getSimpleName();
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // The minimum time between updates in milliseconds
    private static final long MIN_MINUTES = 1000*60;
    // The minimum distance to change Updates in meters
    private static final float MIN_METRES = 100f;
    // Declaring a Location Manager
    private LocationManager locationManager;
    private String preferredProvider;
    private String strStatus;

    private final LocationListener gpsLocationListener = new LocationListener(){

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    strStatus = getString(R.string.status_gps_available);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    strStatus = getString(R.string.status_gps_out);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    strStatus = getString(R.string.status_gps_temp_unavailable);
                    break;
                default:
                    strStatus = getString(R.string.status_gps_unknown);
            }
            Log.w(TAG, strStatus);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, getString(R.string.status_gps_prov_enabled));
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.w(TAG, getString(R.string.status_gps_prov_disabled));
        }

        @Override
        public void onLocationChanged(Location location) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(networkLocationListener);
            }
            strStatus = getString(R.string.status_gps_new_loc)
                    + String.format(Locale.GERMANY, "%9.6f", location.getLatitude()) + ", "
                    + String.format(Locale.GERMANY, "%9.6f", location.getLongitude());
            Log.i(TAG, strStatus);
        }
    };

    private final LocationListener networkLocationListener = new LocationListener(){

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras){
                switch (status) {
                    case LocationProvider.AVAILABLE:
                        strStatus = getString(R.string.status_net_loc_available);
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        strStatus = getString(R.string.status_net_loc_out);
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        strStatus = getString(R.string.status_net_loc_temp_unavailable);
                        break;
                    default:
                        strStatus = getString(R.string.status_net_unknown);
                }
                Log.w(TAG, strStatus);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i(TAG, getString(R.string.status_net_prov_enabled));
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.w(TAG, getString(R.string.status_net_prov_disabled));
            }

            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    strStatus = getString(R.string.status_net_new_loc) + " "
                            + String.format(Locale.GERMANY, "%9.6f", location.getLatitude()) + ", "
                            + String.format(Locale.GERMANY, "%9.6f", location.getLongitude());
                    Log.i(TAG, strStatus);
                }
            }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initGPS(); //Ustawienie obiektu LocationManager

        if (preferredProvider != null
            && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(preferredProvider, MIN_MINUTES, MIN_METRES, gpsLocationListener);
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(networkLocationListener);
        }
        return super.onUnbind(intent);
    }

    private void initGPS() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (isNetworkEnabled){
                Criteria criteria = new Criteria();
                preferredProvider = locationManager.getBestProvider(criteria, true);
            }
        }
        else {
            preferredProvider = LocationManager.GPS_PROVIDER;
        }

        if (preferredProvider != null)
        {
            Log.i(TAG, "preferredProvider: " + preferredProvider);

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Location loc = locationManager.getLastKnownLocation(preferredProvider);

                if (loc != null) {
                    strStatus = getString(R.string.status_net_last_loc) + " "
                            + String.format(Locale.GERMANY, "%9.6f", loc.getLatitude()) + ", "
                            + String.format(Locale.GERMANY, "%9.6f", loc.getLongitude());
                    Log.i(TAG, strStatus);
                }
            }
        }
    }
}
