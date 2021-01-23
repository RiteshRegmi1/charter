package com.blogspot.riteshcodejava.charter.SetupProfileActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blogspot.riteshcodejava.charter.MainActivity;
import com.blogspot.riteshcodejava.charter.Models.UserModels;
import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding bindingProfile;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingProfile = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(bindingProfile.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        getSupportActionBar().hide();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Creating Account");
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);

        bindingProfile.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);
            }
        });


        bindingProfile.setUpProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name =  bindingProfile.NameEditText.getText().toString();
               if (name.isEmpty()) {
                   bindingProfile.NameEditText.setError("Please Enter a Name");
                   return;
               }
               dialog.show();
               if(selectedImage!=null){
                   StorageReference storageReference = storage.getReference().child("profile").child(mAuth.getUid());
                   storageReference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                           if (task.isSuccessful()){
                               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       String name = bindingProfile.NameEditText.getText().toString();
                                       String imageUrl = uri.toString();
                                       String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();
                                       String uid = mAuth.getUid();

                                       UserModels userModels = new UserModels(uid,name,phoneNumber,imageUrl);
                                       database.getReference().
                                               child("Users").
                                               child(uid).
                                               setValue(userModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void aVoid) {
                                               dialog.dismiss();
                                          Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                                          startActivity(intent);
                                          finishAffinity();
                                           }
                                       });
                                   }
                               });
                           }
                       }
                   });
               }else{

                       String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();
                       String uid = mAuth.getUid();

                       UserModels userModels = new UserModels(uid,name,phoneNumber,"No Image");
                       database.getReference().
                               child("Users").
                               child(uid).
                               setValue(userModels).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               dialog.dismiss();
                               Intent intent = new Intent(SetupProfileActivity.this,MainActivity.class);
                               startActivity(intent);
                               finishAffinity();
                           }
                       });
                   }
               }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!= null){
            if(data.getData() != null){
                bindingProfile.profileImage.setImageURI(data.getData());
                selectedImage = data.getData();
            }
        }

    }
}