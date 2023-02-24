package com.example.musek.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musek.R;
import com.example.musek.activity.login.SignInActivity;
import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.databinding.ItemSongBinding;
import com.example.musek.databinding.ItemSongFavoriteBinding;
import com.example.musek.model_data.DataSong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFavoriteSong extends RecyclerView.Adapter<AdapterFavoriteSong.ViewHolder> {
    private List<DataSong> dataSongList;
    private Activity activity;
    private static final int REQUEST_CODE = 1;
    private String phoneNumber;


    public AdapterFavoriteSong(List<DataSong> dataSongList, Activity activity, String phoneNumber) {
        this.dataSongList = dataSongList;
        this.activity = activity;
        this.phoneNumber = phoneNumber;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongFavoriteBinding itemSongFavoriteBinding = ItemSongFavoriteBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false);
        return new ViewHolder(itemSongFavoriteBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSong dataSong = dataSongList.get(position);
        DataSong detailSong = new DataSong(dataSong.getImageSingle(), dataSong.getNameSingle(),
                dataSong.getNameSong(),dataSong.getSongId(),dataSong.getUrlMp3());
        holder.binding.tvNameSong.setText(dataSong.getNameSong());
        holder.binding.tvNameSingle.setText(dataSong.getNameSingle());
        Picasso.get().load(dataSong.getImageSingle()).into(holder.binding.imgSingle);
        holder.binding.layoutItemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlaySong (detailSong);
            }
        });
        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNotify(Gravity.CENTER, dataSong);
            }
        });

    }

    private void goToPlaySong(DataSong detailSong) {
        Intent intent = new Intent(activity, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", detailSong);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public int getItemCount() {
        return dataSongList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongFavoriteBinding binding;

        public ViewHolder(@NonNull ItemSongFavoriteBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.binding = itemSongBinding;

        }
    }

    public void openDialogNotify(int gravity, DataSong dataSong) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notify);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        Button btCancel = dialog.findViewById(R.id.bt_cancel);
        Button btDelete = dialog.findViewById(R.id.bt_delete);
        TextView textView = dialog.findViewById(R.id.tv_text);
        ImageView imgClose = dialog.findViewById(R.id.img_close);
        textView.setText("Delete " + dataSong.getNameSong() + " ?");
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFavoriteSong(dataSong);
                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void DeleteFavoriteSong(DataSong dataSong) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_account").
                child(phoneNumber).child("favorite_song");
        databaseReference.child(dataSong.getNameSong()).removeValue();
    }
}
