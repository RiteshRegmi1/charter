package com.blogspot.riteshcodejava.charter.OtpVerifiedActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.SetupProfileActivity.SetupProfileActivity;
import com.blogspot.riteshcodejava.charter.databinding.ActivityOptBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.util.concurrent.TimeUnit;

public class OptActivity extends AppCompatActivity {
    ActivityOptBinding binding;
    FirebaseAuth mAuth;
    String verification;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.otpView.requestFocus();
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.otpTextViewShowPhoneNumber.setText("OTP "+phoneNumber);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Otp");
        progressDialog.show();
        progressDialog.setCancelable(false);
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OptActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                verification  = s;
                                super.onCodeSent(s, forceResendingToken);
                                progressDialog.dismiss();

                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verification,otp);
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(OptActivity.this, SetupProfileActivity.class));
                            finishAffinity();
                        }else
                            Toast.makeText(OptActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        
    }
}