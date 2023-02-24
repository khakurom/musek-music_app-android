package com.example.musek.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.databinding.ItemSongBinding;
import com.example.musek.model_data.DataSong;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterRecommend extends RecyclerView.Adapter<AdapterRecommend.ViewHolder> {
    private List<DataSong> dataPlaylists;
    private Activity activity;
    private int flag;
    private static final int REQUEST_CODE = 1;


    public AdapterRecommend(List<DataSong> dataPlaylists, Activity activity, int flag) {
        this.dataPlaylists = dataPlaylists;
        this.activity = activity;
        this.flag = flag;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding binding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSong dataSong = dataPlaylists.get(position);
        Picasso.get().load(dataSong.getImageSingle()).into(holder.binding.imgSingle);
        holder.binding.tvNameSong.setText(dataSong.getNameSong());
        holder.binding.tvNameSingle.setText(dataSong.getNameSingle());
        DataSong detailSong = new DataSong(dataSong.getImageSingle(), dataSong.getNameSingle(),
                dataSong.getNameSong(), dataSong.getSongId(), dataSong.getUrlMp3());
        holder.binding.layoutItemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlaySongActivity(detailSong);
            }
        });

    }

    private void goToPlaySongActivity(DataSong detailSong) {
        Intent intent = new Intent(activity, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", detailSong);
        intent.putExtra("key_flag", flag);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);
        activity.finish();
    }

    @Override
    public int getItemCount() {
        if (dataPlaylists != null){
            return dataPlaylists.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding binding;

        public ViewHolder(@NonNull ItemSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }
}
