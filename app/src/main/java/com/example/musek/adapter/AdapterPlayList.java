package com.example.musek.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musek.activity.playsong.PlaySongActivity;

import com.example.musek.databinding.ItemSongBinding;
import com.example.musek.model_data.DataSong;
import com.squareup.picasso.Picasso;


import java.util.List;

public class AdapterPlayList extends RecyclerView.Adapter<AdapterPlayList.ViewHolderPlaylist>{
    private final List<DataSong> dataPlaylists;
    private final Activity activity;
    private final String namePlaylist;
    private final int flag;
    private static final int REQUEST_CODE = 1;


    public AdapterPlayList(List<DataSong> DataPlaylist, Activity activity,String namePlaylist,
                           int flag) {
        this.dataPlaylists = DataPlaylist;
        this.activity = activity;
        this.namePlaylist = namePlaylist;
        this.flag = flag;

    }

    @NonNull
    @Override
    public ViewHolderPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding binding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolderPlaylist(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPlaylist holder, int position) {
        DataSong playlist = dataPlaylists.get(position);

        DataSong detailSong = new DataSong(playlist.getImageSingle(), playlist.getNameSingle(),
                playlist.getNameSong() , playlist.getSongId(), playlist.getUrlMp3());
        Picasso.get().load(playlist.getImageSingle()).into(holder.binding.imgSingle);
        holder.binding.tvNameSong.setText(playlist.getNameSong());
        holder.binding.tvNameSingle.setText(playlist.getNameSingle());
        holder.binding.layoutItemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailSong.getImageSingle() == null){
                    Toast.makeText(activity, "null", Toast.LENGTH_SHORT).show();
                }
                goToPlaySong(detailSong);
            }
        });
    }

    private void goToPlaySong(DataSong detailSong) {
        Intent intent = new Intent(activity, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", detailSong);
        intent.putExtra("key_flag", flag);
        intent.putExtra("key_name_playlist", namePlaylist);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_CODE);


    }

    @Override
    public int getItemCount() {
        if (dataPlaylists != null){
            return dataPlaylists.size();
        }
        return 0;
    }

    public static class ViewHolderPlaylist extends RecyclerView.ViewHolder {
        private final ItemSongBinding binding;

        public ViewHolderPlaylist (@NonNull ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.binding = itemSongBinding;

        }
    }
}
