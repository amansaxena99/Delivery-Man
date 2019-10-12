package com.refl3xn.prototype;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import static com.refl3xn.prototype.AddActivity.item;
import static com.refl3xn.prototype.MainActivity.usr;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    LocationManager locationManager;
    LocationListener locationListener;

    LatLng location;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


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

        LatLng loc = new LatLng(usr.getLatitue(), usr.getLongitude());
        mMap.addMarker(new MarkerOptions().position(loc).title("here"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 13));

        type = getIntent().getStringExtra("type");
        mMap.setOnMapLongClickListener(this);


    }

    public void okFunction(View view){
        if (Integer.parseInt(type) == 0) {
            item.setdLat(location.latitude);
            item.setdLng(location.longitude);
            finish();
        } else {
            item.setpLat(location.latitude);
            item.setpLng(location.longitude);
            finish();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        String address = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAdress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (listAdress != null && listAdress.size() > 0){
                Log.i("place info", listAdress.get(0).toString());
                if (listAdress.get(0).getAddressLine(0) != null){
                    address += listAdress.get(0).getAddressLine(0);
                    Log.i("addrss123:", listAdress.get(0).getAddressLine(0));
                } else {
                    address += "Could not find Addrss :/";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        location = latLng;
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
    }
}
