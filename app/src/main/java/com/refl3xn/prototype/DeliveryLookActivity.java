package com.refl3xn.prototype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class DeliveryLookActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView textView;
    Button button;

    public void completeFunction(View view){
        if (ProfileActivity.it.getStatus() == 1){
            button.setText("delivered");
            ProfileActivity.it.setStatus(2);
            mDatabaseReference.child("listing").child(ProfileActivity.it.getUid()).setValue(ProfileActivity.it);
            Toast.makeText(this, "order picked", Toast.LENGTH_SHORT).show();
            return;
        } else if (ProfileActivity.it.getStatus() == 2){
            ProfileActivity.it.setStatus(3);
            mDatabaseReference.child("listing").child(ProfileActivity.it.getUid()).setValue(ProfileActivity.it);
            Toast.makeText(this, "Delivered", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            finishAffinity();
            startActivity(intent);
            finish();
            return;
        } else if (ProfileActivity.it.getStatus() == 3){
            Toast.makeText(this, "order not confirmed delivered from customer side", Toast.LENGTH_SHORT).show();
            return;
        }

        /*mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot temp: dataSnapshot.child("listing").getChildren()) {
                    Log.i("data::::", "this" + ProfileActivity.it.uid);
                    if (temp.getKey().equals(ProfileActivity.it.getUid())) {
                        temp.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_look);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    LatLng pic, del, usrll;
    users tempuser;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        pic = new LatLng(ProfileActivity.it.getpLat(), ProfileActivity.it.getpLng());
        del = new LatLng(ProfileActivity.it.getdLat(), ProfileActivity.it.getdLng());
        usrll = new LatLng(usr.getLatitue(), usr.getLongitude());
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(pic).title(ProfileActivity.it.getItem() + ": Rs." + ProfileActivity.it.getItemCost()).snippet(ProfileActivity.it.getPickupAddress() + " payment: Rs." + ProfileActivity.it.getDeliveryCost()));
        mMap.addMarker(new MarkerOptions().position(del).title("to deliver here").snippet(ProfileActivity.it.getDeliveryAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(usrll).title("you are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        if (ProfileActivity.it.getStatus() == 1){
            button.setText("picked");
        } else if (ProfileActivity.it.getStatus() == 2){
            button.setText("delivered");
        } else if (ProfileActivity.it.getStatus() == 3){
            button.setText("Completed");
        }


        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot temp : dataSnapshot.child("users").getChildren()) {
                        Log.i("data:",temp.getValue().toString());
                        Log.i("data:", temp.getKey());
                        if (temp.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            usr = temp.getValue(users.class);
                            Log.i("data::::", "changed");
                            updateLocations();
                        }
                    }
                } catch (Exception execp) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot temp : dataSnapshot.child("users").getChildren()) {
                        Log.i("data:",temp.getValue().toString());
                        Log.i("data:", temp.getKey());
                        if (temp.getKey().equals(ProfileActivity.it.getUid())) {
                            tempuser = temp.getValue(users.class);
                            Log.i("data::::", "changed");
                            textView.setText(tempuser.getName() + "\n" + tempuser.getPhone());
                        }
                    }
                } catch (Exception execp) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrll, 13));
    }

    private void updateLocations() {
        mMap.clear();
        usrll = new LatLng(usr.getLatitue(), usr.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pic).title(ProfileActivity.it.getItem() + ": Rs." + ProfileActivity.it.getItemCost()).snippet("payment: Rs." + ProfileActivity.it.getDeliveryCost()));
        mMap.addMarker(new MarkerOptions().position(del).title("to deliver here").snippet(ProfileActivity.it.getDeliveryAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(usrll).title("you are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrll, 13));
    }
}
