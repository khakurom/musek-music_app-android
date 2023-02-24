package com.example.musek.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.databinding.ItemArtistBinding;
import com.example.musek.model_data.ArtistTrending;
import com.example.musek.model_data.PlaylistObject;

import java.util.List;

public class AdapterArtistTrending extends RecyclerView.Adapter<AdapterArtistTrending.ViewHolder>{
    private List<ArtistTrending> artistTrendingList;
    private MainActivity activity;
    private PlaylistObject object ;

    public AdapterArtistTrending(List<ArtistTrending> artistTrendingList, MainActivity activity) {
        this.artistTrendingList = artistTrendingList;
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

        ArtistTrending artistTrending = artistTrendingList.get(position);
        if (artistTrending == null) {
            return;
        }

        Glide.with(activity).load(artistTrending.getImgArtist()).centerCrop().into(
                holder.binding.imgSingle);
        holder.binding.tvNameSingle.setText(artistTrending.getNameArtist());
        holder.binding.layoutItemArtist.setOnClickListener(new View.OnClickListener() {
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
        if (artistTrendingList != null) {
            return artistTrendingList.size();
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
