package cf.eaugenethomas.bus_track;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.os.*;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by Eaugene on 12/01/2018.
 */

public class GPSTracker extends Service {

    private Context mcontext;
    boolean isgpsenabled = false;
    boolean isnetworkenabled = false;
    boolean cangetgps = false;

    private Context mcontext1;
    Activity activity1;
    public int oo = 0;
    private boolean isRunning;
    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_FOR_UPDATES = 1;
    private static final long MIN_TIME_FOR_UPDATES = 1000;
    protected LocationManager locationManager;
    Activity activity;

    public static final long INTERVAL = 10000;//variable to execute services every 10 second
    private Handler mHandler = new Handler(); // run on another Thread to avoid crash
    private Timer mTimer = null; // timer handling

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("unsupported Operation");
    }

    @Override
    public void onCreate() {
        // cancel if service is  already existed
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, INTERVAL);// schedule task
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "In Destroy", Toast.LENGTH_SHORT).show();//display toast when method called
        mTimer.cancel();//cancel the timer
    }


    //inner class of TimeDisplayTimerTask
    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast at every 10 second
                    abc();
                    Toast.makeText(getApplicationContext(), "Notify222222", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public GPSTracker() {
    }

    public GPSTracker(Context context, Activity activity) {
        Log.d("testw", "15");
        this.mcontext = context;
        this.activity = activity;

        getLocation();
    }

    public void abc() {
        Log.d("testw", "yes");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("testw","qwe");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("testw","qwe3");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("testw","qwe1");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d("testw","qwe4");
        try {
            locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            Log.d("testw","fgh");
            isgpsenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isnetworkenabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("testw", "zxc");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_FOR_UPDATES, mLocationListener);
            Log.d("testw", "qwe5");
            if (locationManager != null) {
                Log.d("testw", "hey1");
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    Log.d("testw","uyiu");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(), latitude + "+" + longitude, Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch(Exception e)
        {
            Log.d("testw",e.toString());
        }
    }

    public Location getLocation()
    {
        Log.d("testw","try");
        try
        {
            locationManager=(LocationManager)mcontext.getSystemService(LOCATION_SERVICE);
            isgpsenabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isnetworkenabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isnetworkenabled && !isgpsenabled)
            {
Log.d("testw","16");
            }
            else {
                this.cangetgps = true;
                Log.d("testw","27");
                if (isnetworkenabled) {
                    Log.d("testw","5");
                    if((ActivityCompat.checkSelfPermission( (Activity)mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission((Activity)mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                    {
                        return null;
                    }
                    Log.d("testw","no");
                    int requestPermissionsCode = 50;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_FOR_UPDATES, MIN_DISTANCE_FOR_UPDATES, mLocationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //Toast.makeText(getApplicationContext(), latitude+"+"+longitude, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

                if(isgpsenabled)
                {
                    Log.d("testw","6");
                    if(true) {
                        Log.d("testw","9");
                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 50);
                            Log.d("testw","13");
                    }else
                        {
                            Log.d("testw","10");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_FOR_UPDATES,MIN_DISTANCE_FOR_UPDATES,mLocationListener);
                        if(locationManager!=null)
                        {Log.d("testw","11");
                            location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location!=null)
                            {Log.d("testw","12");
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                Log.d("testw",latitude+"+"+longitude);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            Log.i("testw",e.toString());
        }
        return location;
    }

    public void stopusingGPS()
    {
        if(locationManager!=null)
        {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            locationManager.removeUpdates((LocationListener) GPSTracker.this);
        }
    }
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public double getLatitude() {

        if(location!=null)
        {
            Log.d("testw","3");
            latitude=location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location!=null)
        {
            Log.d("testw","4");
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean cangetgps() {
        return this.cangetgps;
    }
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(mcontext);

        alertdialog.setTitle("GPS is not enables");
        alertdialog.setMessage("GPS is not available . Do you want to enable it ?");

        alertdialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivity(intent);
            }
        });

        alertdialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertdialog.show();
    }




}