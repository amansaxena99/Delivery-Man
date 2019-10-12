package com.refl3xn.prototype;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.refl3xn.prototype.MainActivity.currentUser;
import static com.refl3xn.prototype.MainActivity.mDatabaseReference;

public class ProfileActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, currentUser.getUid(), Toast.LENGTH_SHORT).show();

    }
}
