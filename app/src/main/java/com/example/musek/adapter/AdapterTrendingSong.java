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

public class AdapterTrendingSong extends RecyclerView.Adapter<AdapterTrendingSong.ViewHolder> {
    private List<DataSong> mDataTrendingSongList;
    private Activity mActivity;
    private int flag = 1;
    private static final int REQUEST_CODE = 1;
    private String phoneNumber;



    public AdapterTrendingSong(List<DataSong> mDataTrendingSongList, Activity mActivity,String phoneNumber) {
        this.mDataTrendingSongList = mDataTrendingSongList;
        this.mActivity = mActivity;
        this.phoneNumber = phoneNumber;

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
        DataSong dataSong = mDataTrendingSongList.get(position);
        if (dataSong == null) {
            return;
        }
        holder.binding.tvNumber.setText(String.valueOf(dataSong.getSongId()+1));
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
        Intent intent = new Intent(mActivity, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", detailSong);
        intent.putExtra("key_flag", flag);
        intent.putExtras(bundle);
        mActivity.startActivityForResult(intent, REQUEST_CODE);

    }


    @Override
    public int getItemCount() {
        if (mDataTrendingSongList != null) {
            return mDataTrendingSongList.size();
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
