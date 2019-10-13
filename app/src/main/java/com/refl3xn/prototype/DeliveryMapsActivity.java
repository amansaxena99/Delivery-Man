package com.refl3xn.prototype;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class DeliveryMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Item temp;


    public void acceptFunction(View view){
        temp.setStatus(1);
        temp.setDuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mDatabaseReference.child("listing").child(temp.getUid()).setValue(temp);
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        finishAffinity();
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    Button acceptButton;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    Item it;
    ArrayList<Item> itemsList;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        temp = new Item();
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                acceptButton.setVisibility(View.INVISIBLE);
                updateMap();
            }
        });




        mMap = googleMap;

        acceptButton = findViewById(R.id.button);
        acceptButton.setVisibility(View.INVISIBLE);


        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
//        progressBar.setVisibility(View.VISIBLE);

        itemsList = new ArrayList<>();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    itemsList.clear();
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    for (DataSnapshot temp : dataSnapshot.child("listing").getChildren()) {
                        Log.i("data:",temp.getValue().toString());
                        Log.i("data:", temp.getKey());
                        if (!temp.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            it = temp.getValue(Item.class);
                            if (it.getStatus() == 0) {
                                Log.i("data:", "added");
                                itemsList.add(it);
                            }
                        }
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    updateMap();
                } catch (Exception execp) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
        LatLng sydney = new LatLng(usr.getLatitue(), usr.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("you"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    }




    private void updateMap() {
        mMap.clear();
        for (int i=0;i<itemsList.size();i++){
            LatLng sydney = new LatLng(itemsList.get(i).getpLat(), itemsList.get(i).getpLng());
            mMap.addMarker(new MarkerOptions().position(sydney).title(itemsList.get(i).getItem() + ": Rs." + itemsList.get(i).getItemCost()).snippet(itemsList.get(i).getPickupAddress() + " payment: Rs." + itemsList.get(i).getDeliveryCost()));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.getTitle().equals("to deliver here")){
                        return false;
                    } else {
                        acceptButton.setVisibility(View.VISIBLE);
                        Log.i("data:", marker.getSnippet());
                        mMap.clear();
                        Marker m = mMap.addMarker(new MarkerOptions().position(marker.getPosition()).title(marker.getTitle()).snippet(marker.getSnippet()));
                        for (int i = 0; i < itemsList.size(); i++) {
                            Log.i("data:", "found this");
                            if (itemsList.get(i).getpLat() == marker.getPosition().latitude && itemsList.get(i).getpLng() == marker.getPosition().longitude) {
                                Log.i("data:", "found");
                                temp = itemsList.get(i);
                                Marker m1 = mMap.addMarker(new MarkerOptions().position(new LatLng(itemsList.get(i).dLat, itemsList.get(i).getdLng())).title("to deliver here").snippet(itemsList.get(i).getDeliveryAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                m1.showInfoWindow();
                            }
                        }
                        m.showInfoWindow();
                        return true;
                    }
                }
            });

        }

    }
}
