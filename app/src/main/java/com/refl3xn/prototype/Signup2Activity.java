package com.refl3xn.prototype;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static com.refl3xn.prototype.MainActivity.currentUser;
import static com.refl3xn.prototype.MainActivity.mAuth;
import static com.refl3xn.prototype.MainActivity.mDatabaseReference;
import static com.refl3xn.prototype.MainActivity.usr;

public class Signup2Activity extends AppCompatActivity {

    EditText otpEditText;
    String verificationid, name, number;
    ProgressBar progressBar;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        mAuth = FirebaseAuth.getInstance();
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("phonenumber");
        String phonenumber = "+91" + number;
        Log.i("datanow", name + number);
        type = getIntent().getIntExtra("type", 0);
        Log.i("datanow", String.valueOf(type));

        sendVerificationCode(phonenumber);

        progressBar = findViewById(R.id.progressbar);
        otpEditText = findViewById(R.id.otpEditText);
    }

    public void backClicked(View view){
        finish();
    }

    public void continueClicked(View view) {
        String code = otpEditText.getText().toString().trim();

        if ((code.isEmpty() || code.length() < 6)) {

            otpEditText.setError("Enter code...");
            otpEditText.requestFocus();
            return;
        }
        verifyCode(code);
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (type == 1){
                                Log.i("datanow", name + number);
                                usr = new users(name, number, 0, 0);
                                mDatabaseReference.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(usr);
                            } else {
                            }
                            Intent intent = new Intent(Signup2Activity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Signup2Activity.this,  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void sendVerificationCode(String number){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationid = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Signup2Activity.this, e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };
}
