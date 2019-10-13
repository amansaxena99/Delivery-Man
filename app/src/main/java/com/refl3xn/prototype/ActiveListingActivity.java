package com.refl3xn.prototype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class ActiveListingActivity extends AppCompatActivity {

    public void addFunction(View view){
        if (textView.getText().equals("No listings")){
            Intent intent = new Intent(getApplicationContext(), AddActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "listings limited to one", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewStat(View view){
        if (!textView.getText().equals("No listings") && it.getDuid().equals("na")){
            Toast.makeText(this, "no one has accepted delivery yet", Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    public void cancelOrder(View view){
        if (it.getStatus() == 0){
            Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot temp: dataSnapshot.child("listing").getChildren()) {
                        if (temp.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            temp.getRef().removeValue();
                            textView.setText("No listings");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "cancellation period is over now ", Toast.LENGTH_SHORT).show();
        }
    }

    TextView textView;
    ProgressBar progressBar;
    Item it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_listing);
        textView = findViewById(R.id.textView);
        textView.setText("No listings");
        it = new Item();
        progressBar = findViewById(R.id.progressBar);
        progressBar.bringToFront();
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot temp : dataSnapshot.child("listing").getChildren()) {
                        Log.i("data:",temp.getValue().toString());
                        Log.i("data:", temp.getKey());
                        if (temp.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            it = temp.getValue(Item.class);
                            textView.setText(it.getItem() + "\nfrom: " + it.getPickupAddress() + "\nfor: Rs" + it.getDeliveryCost() + "\nStatus: " + it.getStatus());
                        }
                    }
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    progressBar.setVisibility(View.INVISIBLE);
                } catch (Exception execp) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}
