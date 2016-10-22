package com.alanpasi.ondeestavoce;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView mLatUserOne;
    private TextView mLonUserOne;
    private TextView mSpeedUserOne;

    private TextView mLatUserTwo;
    private TextView mLonUserTwo;
    private TextView mSpeedUserTwo;

    private Button mTBtnUserOne;
    private Button mTBtnUserTwo;
    private Button mBtnShowMap;

    private boolean userOneIsLoggedIn = false;
    private boolean userTwoIsLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRefUserOne = database.getReference("user_one");
        final DatabaseReference myRefUserTwo = database.getReference("user_two");

        mLatUserOne = (TextView) findViewById(R.id.lat_user_one);
        mLonUserOne = (TextView) findViewById(R.id.lon_user_one);
        mSpeedUserOne = (TextView) findViewById(R.id.speed_user_one);

        mLatUserTwo = (TextView) findViewById(R.id.lat_user_two);
        mLonUserTwo = (TextView) findViewById(R.id.lon_user_two);
        mSpeedUserTwo = (TextView) findViewById(R.id.speed_user_two);

        mTBtnUserOne = (Button) findViewById(R.id.tbtn_user_one);
        mTBtnUserTwo = (Button) findViewById(R.id.tbtn_user_two);
        mBtnShowMap = (Button) findViewById(R.id.btn_show_map);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mTBtnUserOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userOneIsLoggedIn) {
                    myRefUserOne.child("is_on").setValue(true);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    userOneIsLoggedIn = true;
                }
                else{
                    userOneIsLoggedIn = false;
                    myRefUserOne.child("is_on").setValue(false);
                    locationManager.removeUpdates(locationListener);
                    myRefUserOne.child("latitude").setValue(0);
                    myRefUserOne.child("longitude").setValue(0);
                    myRefUserOne.child("speed").setValue(0);
                }

            }
        });


        mTBtnUserTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userTwoIsLoggedIn) {
                    myRefUserTwo.child("is_on").setValue(true);
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    userTwoIsLoggedIn = true;
                }
                else{
                    userTwoIsLoggedIn = false;
                    myRefUserTwo.child("is_on").setValue(false);
                    locationManager.removeUpdates(locationListener);
                    myRefUserTwo.child("latitude").setValue(0);
                    myRefUserTwo.child("longitude").setValue(0);
                    myRefUserTwo.child("speed").setValue(0);
                }

            }
        });

        mBtnShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowMap.class);
                startActivity(intent);
            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (userOneIsLoggedIn) {
                    mLatUserOne.setText(String.valueOf(location.getLatitude()));
                    mLonUserOne.setText(String.valueOf(location.getLongitude()));
                    mSpeedUserOne.setText(String.valueOf(location.getSpeed()));

                    myRefUserOne.child("latitude").setValue(location.getLatitude());
                    myRefUserOne.child("longitude").setValue(location.getLongitude());
                    myRefUserOne.child("speed").setValue(location.getSpeed());
                } else {
                    if (userTwoIsLoggedIn) {
                        mLatUserTwo.setText(String.valueOf(location.getLatitude()));
                        mLonUserTwo.setText(String.valueOf(location.getLongitude()));
                        mSpeedUserTwo.setText(String.valueOf(location.getSpeed()));

                        myRefUserTwo.child("latitude").setValue(location.getLatitude());
                        myRefUserTwo.child("longitude").setValue(location.getLongitude());
                        myRefUserTwo.child("speed").setValue(location.getSpeed());
                    }
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
    }
}
