package com.refl3xn.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText mobileEditText;

    public void backClicked(View view){
        finish();
    }

    public void loginClicked(View view){
        Toast.makeText(this, "Login Clicked", Toast.LENGTH_SHORT).show();
        if (mobileEditText.getText().toString().length() == 10) {
            Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
            intent.putExtra("phonenumber", mobileEditText.getText().toString());
            intent.putExtra("type", 2);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "enter valid mobile no.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void signupClicked(View view){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mobileEditText = findViewById(R.id.mobileEditText);

    }


}