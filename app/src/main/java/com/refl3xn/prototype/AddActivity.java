package com.refl3xn.prototype;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.refl3xn.prototype.MainActivity.currentUser;
import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class AddActivity extends AppCompatActivity {

    EditText itemEditText, itemcostEditText, deliveryCostEditText, pickupAddressEditText, deliveryAddressEditText;
    public static Item item;

    public void deliveryLocation(View view){
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.putExtra("type", "0");
        startActivity(intent);
    }

    public void pickupLocation(View view){
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        intent.putExtra("type", "1");
        startActivity(intent);
    }

    public void addFunction(View view){
        if (itemEditText == null || deliveryCostEditText == null || deliveryAddressEditText == null || itemcostEditText == null || pickupAddressEditText == null){
            Toast.makeText(this, "enter all details", Toast.LENGTH_SHORT).show();
        } else {
            item.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
            item.setItem(itemEditText.getText().toString());
            item.setDeliveryAddress(deliveryAddressEditText.getText().toString());
            item.setDeliveryCost(deliveryCostEditText.getText().toString());
            item.setItemCost(itemcostEditText.getText().toString());
            item.setPickupAddress(pickupAddressEditText.getText().toString());
            item.setDuid("na");
            mDatabaseReference.child("listing").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(item);
            Intent intent = new Intent(getApplicationContext(), ActiveListingActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        item = new Item();

        itemEditText = findViewById(R.id.editText);
        deliveryCostEditText = findViewById(R.id.editText2);
        deliveryAddressEditText = findViewById(R.id.editText3);
        itemcostEditText = findViewById(R.id.editText4);
        pickupAddressEditText = findViewById(R.id.editText5);

    }
}
