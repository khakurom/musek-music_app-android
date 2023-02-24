package com.example.musek.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musek.activity.main.MainActivity;
import com.example.musek.databinding.PhotoFeatureBinding;
import com.example.musek.model_data.Featured;
import com.example.musek.model_data.PlaylistObject;

import java.util.List;

public class AdapterFeature extends  RecyclerView.Adapter<AdapterFeature.ViewHolder>{
    private List<Featured> featuredList;
    private ViewPager2 viewPager2;
    private MainActivity mActivity;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            featuredList.addAll(featuredList);
            notifyDataSetChanged();
        }
    };

    public AdapterFeature(List<Featured> featuredList, ViewPager2 viewPager2, MainActivity activity) {
        this.featuredList = featuredList;
        this.viewPager2 = viewPager2;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoFeatureBinding binding = PhotoFeatureBinding.inflate(LayoutInflater.from(parent.getContext())
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Featured featured = featuredList.get(position);
        if (featured == null){
            return;
        }
        holder.binding.photoFeature.setImageResource(featured.getImgFeatured());



        if(position == featuredList.size()-2){
            viewPager2.post(runnable);
        }
        int idPlaylist = featured.getIdPlaylist();
        String nameType = "playlist";
        PlaylistObject object = new PlaylistObject(idPlaylist,String.valueOf(featured.getImgFeatured())
                ,nameType,featured.getTitle());
        holder.binding.photoFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = 2;
                mActivity.goToFragmentPlaylist(object,flag);

            }
        });
    }



    @Override
    public int getItemCount() {
        if (featuredList != null){
            return featuredList.size();
        }
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final PhotoFeatureBinding binding;

        public ViewHolder(@NonNull PhotoFeatureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

    }
}
