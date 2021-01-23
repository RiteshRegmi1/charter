package com.blogspot.riteshcodejava.charter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.riteshcodejava.charter.Models.UserModels;
import com.blogspot.riteshcodejava.charter.Models.messageModel;
import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.databinding.ReveiveLayoutBinding;
import com.blogspot.riteshcodejava.charter.databinding.SendLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<messageModel> list;
    private int SEND_MESSAGE = 1;
    private int RECEIVE_MESSAGE = 2;

    public messageAdapter(Context context, ArrayList<messageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SEND_MESSAGE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_layout,parent,false);
            return new sendViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reveive_layout,parent,false);
            return new receiveViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        messageModel messageModel = list.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel.getSenderId())){
            return SEND_MESSAGE;
        }else{
            return RECEIVE_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
     if (holder.getClass() == sendViewHolder.class){
         ((sendViewHolder) holder).binding.sendMessage.setText(list.get(position).getMessage());
     }else{
         ((receiveViewHolder)holder).binding.ReceiveMessage.setText(list.get(position).getMessage());
     }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class sendViewHolder extends RecyclerView.ViewHolder{
        SendLayoutBinding binding;
        public sendViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SendLayoutBinding.bind(itemView);
        }
    }
    class  receiveViewHolder extends RecyclerView.ViewHolder{
        ReveiveLayoutBinding binding;
        public receiveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ReveiveLayoutBinding.bind(itemView);
        }

    }
}
