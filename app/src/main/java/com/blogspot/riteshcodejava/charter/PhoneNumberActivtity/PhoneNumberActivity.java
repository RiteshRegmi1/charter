package com.blogspot.riteshcodejava.charter.PhoneNumberActivtity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.riteshcodejava.charter.MainActivity;
import com.blogspot.riteshcodejava.charter.OtpVerifiedActivity.OptActivity;
import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.phoneNumberEditText.requestFocus();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
       Intent intent = new Intent(this,MainActivity.class);
       startActivity(intent);
       finish();
        }else {


            binding.continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PhoneNumberActivity.this, OptActivity.class);
                    intent.putExtra("phoneNumber", binding.phoneNumberEditText.getText().toString());
                    startActivity(intent);
                }
            });
        }

    }
}