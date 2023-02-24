package com.example.musek.fragment.trending;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musek.adapter.AdapterTrendingSong;
import com.example.musek.databinding.FragmentTrendingBinding;

public class TrendingPresenter {

    private FragmentTrendingBinding fragmentTrendingBinding;
    private Activity mActivity;


    public TrendingPresenter(FragmentTrendingBinding fragmentTrendingBinding, Activity activity) {
        this.fragmentTrendingBinding = fragmentTrendingBinding;
        this.mActivity = activity;
    }
    // set recycle view trending song
    public void SetTrendingSong (AdapterTrendingSong adapterTrendingSong){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity,
                RecyclerView.VERTICAL,false);
        fragmentTrendingBinding.rcvTrendingSong.setLayoutManager(linearLayoutManager);
        fragmentTrendingBinding.rcvTrendingSong.setNestedScrollingEnabled(false);
        fragmentTrendingBinding.rcvTrendingSong.setFocusable(false);
        fragmentTrendingBinding.rcvTrendingSong.setAdapter(adapterTrendingSong);

    }

}
