package com.blogspot.riteshcodejava.charter.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.riteshcodejava.charter.MessageDetailedActivity.MessageDetailedActivity;
import com.blogspot.riteshcodejava.charter.Models.UserModels;
import com.blogspot.riteshcodejava.charter.R;
import com.blogspot.riteshcodejava.charter.databinding.RowChartsBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserAdapters extends RecyclerView.Adapter<UserAdapters.ViewHolder> {
    Context context;
    ArrayList<UserModels> list;

    public UserAdapters(Context context, ArrayList<UserModels> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_charts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getProfilePicture()).placeholder(R.drawable.avatar).into(holder.binding.userImage);
        holder.binding.UserNameTextView.setText(list.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageDetailedActivity.class);
                intent.putExtra("uid",list.get(position).getUid());
                intent.putExtra("name",list.get(position).getName());
                intent.putExtra("photo",list.get(position).getProfilePicture());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RowChartsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowChartsBinding.bind(itemView);
        }
    }
}
