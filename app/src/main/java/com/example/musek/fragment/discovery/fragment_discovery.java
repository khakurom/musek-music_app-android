package com.example.musek.fragment.discovery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musek.R;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.adapter.AdapterArtistTrending;
import com.example.musek.adapter.AdapterFeature;

import com.example.musek.adapter.AdapterNewRelease;
import com.example.musek.databinding.FragmentDiscoveryBinding;
import com.example.musek.model_data.ArtistTrending;

import com.example.musek.model_data.DataSong;
import com.example.musek.model_data.Featured;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_discovery extends Fragment implements DiscoveryInterface {
    private FragmentDiscoveryBinding binding;
    private View mView;
    private List<Featured> featuredList;
    private AdapterArtistTrending adapterArtistTrending;
    private AdapterNewRelease adapterNewRelease;
    private MainActivity activity;
    private String phoneNumber;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.vpFeature.getCurrentItem() == featuredList.size() - 1) {
                binding.vpFeature.setCurrentItem(0);


            } else {
                binding.vpFeature.setCurrentItem(binding.vpFeature.getCurrentItem() + 1);
            }
        }
    };

    public fragment_discovery(MainActivity activity,String phoneNumber) {
        this.activity = activity;
        this.phoneNumber = phoneNumber;
    }

    private Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscoveryBinding.inflate(inflater, container,
                false);
        mView  = binding.getRoot();
        DiscoveryPresenter presenter = new DiscoveryPresenter(this);
        setFeature(presenter);
        setArtistTrending(presenter);

        return mView;
    }



    private void setFeature( DiscoveryPresenter presenter) {
        presenter.setAdapterFeature();
    }
    private void setArtistTrending(DiscoveryPresenter presenter) {
        presenter.setAdapterArtistTrending();
    }

    @Override
    public void setAdapter() {
        // set adapter feature
        settingViewPager2();
        binding.vpFeature.setAdapter(new AdapterFeature(setDataFeature(), binding.vpFeature, activity));
        binding.vpFeature.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(mRunnable);
                handler.postDelayed(mRunnable, 3000);
            }
        });
        binding.circleIndicator.setViewPager(binding.vpFeature);
        // set adapter artist

        adapterArtistTrending = new AdapterArtistTrending(getDataTrendingArtist(), activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        binding.rcvTrendingArtist.setLayoutManager(linearLayoutManager);
        binding.rcvTrendingArtist.setFocusable(false);
        binding.rcvTrendingArtist.setNestedScrollingEnabled(false);
        binding.rcvTrendingArtist.setAdapter(adapterArtistTrending);
        // set adapter new release
        adapterNewRelease =  new AdapterNewRelease(getDataNewRelease(),activity);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false);
        binding.rcvNewRelease.setLayoutManager(linearLayoutManager1);
        binding.rcvNewRelease.setFocusable(false);
        binding.rcvNewRelease.setNestedScrollingEnabled(false);
        binding.rcvNewRelease.setAdapter(adapterNewRelease);


    }
    // setting view pager 2
    private void settingViewPager2() {
        binding.vpFeature.setOffscreenPageLimit(3);
        binding.vpFeature.setClipToPadding(false);
        binding.vpFeature.setClipChildren(false);
        binding.vpFeature.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        binding.vpFeature.setPageTransformer(compositePageTransformer);

    }
    // set photo feature
    private List<Featured> setDataFeature(){
        featuredList = new ArrayList<>();
        featuredList.add(new Featured(1, R.drawable.feature_1,"The best song of HieuThuHai"));
        featuredList.add(new Featured(2, R.drawable.feature_2,"Addictive combination"));
        featuredList.add(new Featured(3, R.drawable.feature_3,"New song every day"));
        return featuredList;
    }

    // get data trending artist from firebase
    private List<ArtistTrending> getDataTrendingArtist() {
        List<ArtistTrending> artistTrendingList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("trending_artist");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArtistTrending artistTrending = snapshot.getValue(ArtistTrending.class);
                if (artistTrending != null){
                    artistTrendingList.add(artistTrending);
                    adapterArtistTrending.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return artistTrendingList;
    }
    ///
    private List<DataSong> getDataNewRelease() {
        List<DataSong> newReleaseList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("new_release");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    newReleaseList.add(dataSong);
                    adapterNewRelease.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return newReleaseList;
    }








}