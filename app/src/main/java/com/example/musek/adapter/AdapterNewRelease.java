package com.example.musek.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.databinding.ItemArtistBinding;
import com.example.musek.model_data.DataSong;

import java.util.List;

public class AdapterNewRelease  extends RecyclerView.Adapter<AdapterNewRelease.ViewHolder>{
    private List<DataSong> dataSongList;
    private Activity activity;
    private static final int REQUEST_CODE = 1;

    public AdapterNewRelease(List<DataSong> dataSongList, Activity activity) {
        this.dataSongList = dataSongList;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemArtistBinding binding = ItemArtistBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSong dataSong = dataSongList.get(position);
        if (dataSong == null){
            return;
        }
        Glide.with(activity).load(dataSong.getImageSingle()).centerCrop().into(
                holder.binding.imgSingle);
        holder.binding.tvNameSingle.setText(dataSong.getNameSingle());
        DataSong detailSong = new DataSong(dataSong.getImageSingle(),dataSong.getNameSingle(),
                dataSong.getNameSong(),dataSong.getSongId(),dataSong.getUrlMp3());
        holder.binding.layoutItemArtist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlayMusicActivity(detailSong);
            }
        });
    }
    public void goToPlayMusicActivity(DataSong detailSong) {
        Intent intent = new Intent(activity, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", detailSong);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    public int getItemCount() {
        if (dataSongList != null) {
            return dataSongList.size();
        }
        return 0;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemArtistBinding binding;

        public ViewHolder(@NonNull ItemArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
