package com.refl3xn.prototype;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import static com.refl3xn.prototype.ActiveListingActivity.it;

public class ItemMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    LatLng pic, del, usrll;

    users delu;
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

        // Add a marker in Sydney and move the camera
        pic = new LatLng(it.getpLat(), it.getpLng());
        del = new LatLng(it.getdLat(), it.getdLng());
        usrll = new LatLng(usr.getLatitue(), usr.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(usrll, 13));
        mMap.addMarker(new MarkerOptions().position(pic).title(it.getItem() + ": Rs." + it.getItemCost()).snippet(it.getPickupAddress() + " payment: Rs." + it.getDeliveryCost()));
        mMap.addMarker(new MarkerOptions().position(del).title("to deliver here").snippet(it.getDeliveryAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot temp : dataSnapshot.child("users").getChildren()) {
                        Log.i("data:",temp.getValue().toString());
                        Log.i("data:", temp.getKey());
                        if (temp.getKey().equals(it.duid)) {
                            delu = temp.getValue(users.class);
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
    }

    private void updateLocations() {
        mMap.clear();
        usrll = new LatLng(delu.getLatitue(), delu.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pic).title(it.getItem() + ": Rs." + it.getItemCost()).snippet(it.getPickupAddress() + " payment: Rs." + it.getDeliveryCost()));
        mMap.addMarker(new MarkerOptions().position(del).title("to deliver here").snippet(it.getDeliveryAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(usrll).title("you are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }
}
