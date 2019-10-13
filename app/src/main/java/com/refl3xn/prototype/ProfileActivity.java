package com.refl3xn.prototype;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.refl3xn.prototype.MainActivity.currentUser;
import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class ProfileActivity extends AppCompatActivity {

    Location currLocation;
    LocationManager locationManager;
    LocationListener locationListener;
    MenuInflater menuInflater;
    Menu menu1;

    Boolean dflag;

    public static Item it;
    ProgressBar progressBar;

    String name, phone;

    TextView textView, dtextView;

    public void editProfile(View view){
        Toast.makeText(this, "edit clicked", Toast.LENGTH_SHORT).show();
    }
    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void activeListingView(View view){
        Intent intent = new Intent(getApplicationContext(), ActiveListingActivity.class);
        startActivity(intent);
    }

    public void deliveryListingView (View view){
        if (dflag){
            Toast.makeText(this, "making delivery", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), DeliveryLookActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), DeliveryMapsActivity.class);
            startActivity(intent);
        }
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

    public void onlogin(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dflag = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        textView = findViewById(R.id.textView);
        dtextView = findViewById(R.id.textView4);
        phone = getIntent().getStringExtra("Phone");

        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
//        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        datar();


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location:", location.toString());
                updateLocation(location);
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
            /*Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null){
                updateLocation(lastKnownLocation);
            }*/
        }

    }

    private void datar() {
        it = new Item();
        name = getIntent().getStringExtra("Name");
        phone = getIntent().getStringExtra("Phone");
//        Toast.makeText(this, name + phone, Toast.LENGTH_SHORT).show();
//        usr = new users();

        if (name == null) {
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot temp : dataSnapshot.child("users").getChildren()) {
                            Log.i("data:",temp.getValue().toString());
                            Log.i("data:", temp.getKey());
                            if (temp.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                                usr = temp.getValue(users.class);
                                Toast.makeText(ProfileActivity.this, "Welcome " + usr.getName(), Toast.LENGTH_SHORT).show();
                                textView.setText(usr.name+"\n"+usr.phone);
                            }
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception execp) {
                        Toast.makeText(ProfileActivity.this, "please Register before login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Boolean ddflag = false;
                        for (DataSnapshot temp : dataSnapshot.child("listing").getChildren()) {
                            Log.i("data:",temp.getValue().toString());
                            Log.i("data:", temp.getKey());
                            it = temp.getValue(Item.class);
                            if (it.getDuid().equals(FirebaseAuth.getInstance().getUid()) && (it.getStatus() == 1 || it.getStatus() == 2 || it.getStatus() == 3)) {
                                dflag = true;
                                ddflag = true;
                                dtextView.setText("Delivering: " + it.getItem() + "\nAddress: " + it.getDeliveryAddress() + "\nform: " + it.getPickupAddress());
                                break;
                            }
                        }
                        if (ddflag == false){
                            dflag = false;
                            dtextView.setText("Not delivering any thing\ncurrently");
                        }
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    } catch (Exception execp) {
                        Toast.makeText(ProfileActivity.this, "please Register before login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // ...
                }
            });
        } else {
            usr = new users(name, phone, 0, 0);
            mDatabaseReference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(usr);
            Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void updateLocation(Location location){
        currLocation = location;
        usr.setLatitue(currLocation.getLatitude());
        usr.setLongitude(currLocation.getLongitude());
        mDatabaseReference.child("users").child(currentUser.getUid()).setValue(usr);
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();

    }
}
