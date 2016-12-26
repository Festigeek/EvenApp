package ch.hes_so.eventapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mysteriosis on 17.12.16.
 */

public class LocationService extends Service
{
    public static final String MESSAGE = "Update";
    private static final int ONE_MINUTE = 1000 * 60 * 1;

    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.v("START_SERVICE", "DONE");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
        }
        catch (SecurityException se) {
            Toast.makeText( getApplicationContext(), "Permission Error", Toast.LENGTH_SHORT ).show();
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) return true;

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTE;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTE;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) return true;
        if (isSignificantlyOlder) return false;

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate) return true;
        if (isNewer && !isLessAccurate) return true;
        if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) return true;

        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null)
            return provider2 == null;

        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        try {
            locationManager.removeUpdates(listener);
        }
        catch (SecurityException se) {
            Toast.makeText( getApplicationContext(), "Permission Error", Toast.LENGTH_SHORT ).show();
        }
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(final Location loc)
        {
            if(isBetterLocation(loc, previousBestLocation)) {
                previousBestLocation = loc;

                Intent intent = new Intent();
                intent.setAction(MESSAGE);
                intent.putExtra("Latitude", loc.getLatitude());
                intent.putExtra("Longitude", loc.getLongitude());
                intent.putExtra("Provider", loc.getProvider());
                sendBroadcast(intent);
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }
}