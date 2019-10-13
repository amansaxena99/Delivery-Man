package com.refl3xn.prototype;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static FirebaseUser currentUser;
    public static FirebaseAuth mAuth;
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    public static String mUsername="0";
    public static users usr;

    ConstraintLayout layout;
    TextView textView, textView1, textView2;
    Button loginButton, signupButton;

    public void loginClicked(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void signupClicked(View view){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    public void helpClicked(View view){
        Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
    }

    public void animate(){
        layout = findViewById(R.id.layout);
        layout.setAlpha(0);
        layout.animate().alpha(1).setDuration(400);

        textView = findViewById(R.id.textView);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        textView.setAlpha(0);
        textView1.setAlpha(0);
        textView2.setAlpha(0);

        textView.setTranslationY(20);
        textView1.setTranslationY(20);
        textView2.setTranslationY(20);

        textView.animate().translationYBy(-20).alpha(1).setDuration(1000);
        textView1.animate().translationYBy(-20).alpha(1).setDuration(1000);
        textView2.animate().translationYBy(-20).alpha(1).setDuration(1000);

        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        loginButton.setAlpha(0);
        signupButton.setAlpha(0);

        loginButton.setTranslationY(20);
        signupButton.setTranslationY(20);

        loginButton.animate().translationYBy(-20).alpha(1).setDuration(1000);
        signupButton.animate().translationYBy(-20).alpha(1).setDuration(1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animate();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
//            finish();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
