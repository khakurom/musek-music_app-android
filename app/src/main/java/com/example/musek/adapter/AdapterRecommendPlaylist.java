package com.example.musek.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.databinding.ItemSongBinding;
import com.example.musek.model_data.ArtistTrending;
import com.example.musek.model_data.PlaylistObject;

import java.util.List;

public class AdapterRecommendPlaylist extends RecyclerView.Adapter<AdapterRecommendPlaylist.ViewHolder>{
    private List<ArtistTrending> list;
    private MainActivity activity;
    private PlaylistObject object ;

    public AdapterRecommendPlaylist(List<ArtistTrending> list, MainActivity activity) {
        this.list = list;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterRecommendPlaylist.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecommendPlaylist.ViewHolder holder, int position) {
        ArtistTrending artistTrending = list.get(position);
        Glide.with(activity).load(artistTrending.getImgArtist()).centerCrop().into(
                holder.binding.imgSingle);
        holder.binding.tvNameSong.setText(artistTrending.getTitle());
        holder.binding.tvNameSingle.setText(artistTrending.getNameArtist());
        holder.binding.layoutItemSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 3;
                String nameType = "artist_playlist";
                int idPlaylist = artistTrending.getIdPlaylist();
                object = new PlaylistObject(idPlaylist,artistTrending.getImgArtist(),nameType,
                        artistTrending.getTitle());
                activity.goToFragmentPlaylist(object,flag);
            }
        });
    }

    @Override
    public int getItemCount() {
       return list.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding binding;

        public ViewHolder(@NonNull ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.binding = itemSongBinding;

        }
    }
}
