package cf.eaugenethomas.bus_track;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnshowloaction;
    Button stoploc;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mpermission = Manifest.permission.ACCESS_FINE_LOCATION;
    Context mContext;
    GPSTracker gps;
    TextView location;
    EditText editText;
    TelephonyManager telephonyManager;
    String busno;
    String id;
    FirebaseDatabase db;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        id=telephonyManager.getDeviceId();
        editText = (EditText)findViewById(R.id.busno);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            Log.d("testw","1");
            //Toast.makeText(mContext,"You need have granted permission",Toast.LENGTH_SHORT).show();
            gps = new GPSTracker(mContext, MainActivity.this);
            if (gps.cangetgps()) {
                btnshowloaction=(Button)findViewById(R.id.button);
                btnshowloaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("testw","7");
                        busno = editText.getText().toString();
                        //use to start the services
                        location=(TextView)findViewById(R.id.locationtext);
                        gps.getLocation();
                            double latitude=gps.getLatitude();
                            double longitude=gps.getLongitude();
                            location.setText(latitude+" "+longitude);
                        //Toast.makeText(getApplicationContext(),id.toString()+"+"+ busno.toUpperCase(), Toast.LENGTH_LONG).show();
                       if(!(busno.isEmpty()))
                       {
                           db=FirebaseDatabase.getInstance();
                           Log.d("testw","success");
                           ref=db.getReference(id.toString());
                           //ref.setValue(busno.toUpperCase()+","+latitude+","+longitude);
                           ref.child("bus_no").setValue( busno.toUpperCase());
                           ref.child("latitude").setValue(latitude);
                           ref.child("longitude").setValue(longitude);
                       }
                        startService((new Intent(MainActivity.this,GPSTracker.class)).putExtra("imei",id.toString()));
                    }
                });

                stoploc=(Button)findViewById(R.id.button1);
                stoploc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        stopService(new Intent(MainActivity.this,GPSTracker.class));
                        Toast.makeText(getApplicationContext(), "stopped", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                gps.showSettingsAlert();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                Log.d("testw","2");
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    gps = new GPSTracker(mContext, MainActivity.this);

                    // Check if GPS enabled
                    if (gps.cangetgps()) {

                        btnshowloaction=(Button)findViewById(R.id.button);
                        btnshowloaction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d("testw","8");
                                location=(TextView)findViewById(R.id.locationtext);
                                gps.getLocation();
                                double latitude=gps.getLatitude();
                                double longitude=gps.getLongitude();
                                location.setText(latitude+" "+longitude);
                            }
                        });

                       /* double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        // \n is for new line
                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();*/
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(mContext, "You need to grant permission", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}

