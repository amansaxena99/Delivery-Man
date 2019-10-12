package com.refl3xn.prototype;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import static com.refl3xn.prototype.MainActivity.currentUser;
import static com.refl3xn.prototype.MainActivity.mDatabaseReference;

public class ProfileActivity extends AppCompatActivity {

    Location currLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    MenuInflater menuInflater;
    Menu menu1;

    users usr;

    public void activeListingView(View view){
        Intent intent = new Intent(getApplicationContext(), ActiveListingActivity.class);
        startActivity(intent);
    }

    public void deliveryListingView (View view){
        Intent intent = new Intent(getApplicationContext(), DeliveryListingActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu1 = menu;
        menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void startListening(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startListening();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final int[] flag1 = {0};


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location:", location.toString());
                if (flag1[0] != 0){
                    updateLocation(location);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null){
//                updateLocation(lastKnownLocation);
            }
        }

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("data::", dataSnapshot.toString());
                for (DataSnapshot temp : dataSnapshot.getChildren()){
                    Log.i("data::", temp.child(currentUser.getUid()).getValue(users.class).getName());
                    usr = temp.child(currentUser.getUid()).getValue(users.class);
                    Toast.makeText(ProfileActivity.this, "Welcome " + usr.getName(), Toast.LENGTH_SHORT).show();
                    flag1[0] = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

    }

    public void updateLocation(Location location){
        currLocation = location;
        usr.setLatitue(currLocation.getLatitude());
        usr.setLongitude(currLocation.getLongitude());
        mDatabaseReference.child("users").child(currentUser.getUid()).setValue(usr);
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();


        /*latitudeTextView.setText("Latitude : " + location.getLatitude());
        longitudeTextView.setText("Longitude : " + location.getLongitude());
        altitudeTextView.setText("Altitude : " + location.getAltitude());
        accuracyTextView.setText("Accuracy : " + location.getAccuracy());*/
    }
}
