package com.example.musek.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.databinding.ItemSongBinding;
import com.example.musek.model_data.DataSong;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllSong extends RecyclerView.Adapter<AdapterAllSong.ViewHolder>{
    private List<DataSong> dataSongList;
    private List <DataSong> dataSongList1;
    private Activity activity;
    private static final int REQUEST_CODE = 1;

    public AdapterAllSong(List<DataSong> dataSongList, Activity activity) {
        this.dataSongList = dataSongList;
        this.dataSongList1 = dataSongList;
        this.activity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSongBinding itemSongBinding = ItemSongBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(itemSongBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataSong dataSong = dataSongList.get(position);
        Picasso.get().load(dataSong.getImageSingle()).into(holder.binding.imgSingle);
        holder.binding.tvNameSong.setText(dataSong.getNameSong());
        holder.binding.tvNameSingle.setText(dataSong.getNameSingle());
        DataSong detailSong = new DataSong(dataSong.getImageSingle(),dataSong.getNameSingle(),
                dataSong.getNameSong(),dataSong.getSongId(),dataSong.getUrlMp3());
        holder.binding.layoutItemSong.setOnClickListener(new View.OnClickListener() {
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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    dataSongList = dataSongList1;
                } else {
                    List<DataSong> list = new ArrayList<>();
                    for (DataSong dataSong : dataSongList1) {
                        if (dataSong.getNameSong().toLowerCase().contains(strSearch.toLowerCase())) {
                            list.add(dataSong);
                        }
                    }
                    dataSongList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataSongList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataSongList = (List<DataSong>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        if(dataSongList != null){
            return dataSongList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemSongBinding binding;

        public ViewHolder(@NonNull ItemSongBinding itemSongBinding) {
            super(itemSongBinding.getRoot());
            this.binding = itemSongBinding;

        }
    }
}
