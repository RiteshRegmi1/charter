package com.blogspot.riteshcodejava.charter.MessageDetailedActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.blogspot.riteshcodejava.charter.Adapters.messageAdapter;
import com.blogspot.riteshcodejava.charter.Models.UserModels;
import com.blogspot.riteshcodejava.charter.Models.messageModel;
import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.databinding.ActivityMessageDetailedBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MessageDetailedActivity extends AppCompatActivity {

    ActivityMessageDetailedBinding binding;
    ArrayList<messageModel> list;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String senderRoom;
    String receiverRoom;
    messageAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String name = getIntent().getStringExtra("name");
        String pho = getIntent().getStringExtra("photo");

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        adapter = new messageAdapter(this,list);
        binding.detailedChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.detailedChatRecyclerView.setAdapter(adapter);
        String receiverId = getIntent().getStringExtra("uid");
        String senderId = FirebaseAuth.getInstance().getUid();


        database.getReference().child("chat").child(senderId+receiverId).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    messageModel model = snapshot1.getValue(messageModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //getting data for receiver
        database.getReference().child("chat").child(receiverId+senderId).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    messageModel model = snapshot1.getValue(messageModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.sendMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senderRoom = senderId+receiverId;
                receiverRoom = receiverId+senderId;
                String message = binding.messageEditText.getText().toString();
                binding.messageEditText.setText("");
                Date date = new Date();
                messageModel messageModel = new messageModel(message,senderId,date.getTime());
                database.getReference().child("chat").child(senderId+receiverId).child("message").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("chat").child(receiverId+senderId).child("message").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}