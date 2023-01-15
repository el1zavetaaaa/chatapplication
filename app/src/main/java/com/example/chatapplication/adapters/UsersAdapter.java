package com.example.chatapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.entity.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

    private final ArrayList<User> users;
    private final Context context;
    private final OnUserClickListener onUserClickListener;

    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    public interface OnUserClickListener {
        void onUserClicked(int position);
    }


    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.user_holder, parent, false);

        return new UserHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder
                .txtUserName
                .setText(users
                        .get(position)
                        .getUserName());
        Glide
                .with(context)
                .load(users
                        .get(position)
                        .getProfilePicture())
                .error(R.drawable.account_img)
                .placeholder(R.drawable.account_img)
                .into(holder.imageProfile);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView txtUserName;
        ImageView imageProfile;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> onUserClickListener.onUserClicked(getAdapterPosition()));
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imageProfile = itemView.findViewById(R.id.img_pro);
        }
    }
}
