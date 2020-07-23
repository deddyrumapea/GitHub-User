package com.romnan.consumerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.romnan.consumerapp.R;
import com.romnan.consumerapp.model.User;

import java.util.ArrayList;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder> {
    private ArrayList<User> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setData(ArrayList<User> userList) {
        mData.clear();
        mData.addAll(userList);
        notifyDataSetChanged();
    }

    public ArrayList<User> getUsersList() {
        return mData;
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public UserRecyclerViewAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        holder.bind(mData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback
                        .onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(User data);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText, usernameText, locationText;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImage = itemView.findViewById(R.id.user_profile_image);
            nameText = itemView.findViewById(R.id.user_name);
            usernameText = itemView.findViewById(R.id.user_username);
            locationText = itemView.findViewById(R.id.user_location);
        }

        void bind(User user) {
            Glide
                    .with(avatarImage)
                    .load(user.getAvatar())
                    .apply(new RequestOptions().override(80, 80))
                    .circleCrop()
                    .into(avatarImage);
            usernameText.setText(user.getUsername());
            usernameText.setSelected(true);
            nameText.setText(user.getName());
            nameText.setSelected(true);
            locationText.setText(user.getLocation());
        }
    }
}