package com.refl3xn.prototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignupActivity extends AppCompatActivity {

    EditText nameEditText, mobileEditText;
    public void backClicked(View view){
        finish();
    }

    public void loginClicked(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void Login(){
        Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
        startActivity(intent);
        finish();
    }

    public void signupClicked(View view){
        if (mobileEditText.getText().toString().length() == 10) {
            Intent intent = new Intent(getApplicationContext(), Signup2Activity.class);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("phonenumber", mobileEditText.getText().toString());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "enter valid mobile no.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);


    }
}