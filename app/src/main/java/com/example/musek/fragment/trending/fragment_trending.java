package com.example.musek.fragment.trending;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musek.adapter.AdapterTrendingSong;
import com.example.musek.databinding.FragmentTrendingBinding;
import com.example.musek.model_data.DataSong;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_trending extends Fragment {
    private FragmentTrendingBinding binding;
    private View mView;
    private List<DataSong> mDataTrendingSongList;
    private  AdapterTrendingSong adapterTrendingSong;
    private String phoneNumber;

    public fragment_trending(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTrendingBinding.inflate(inflater, container,
                false);
        declarePresenter ();
        mView  = binding.getRoot();
        return mView;
    }

    public void declarePresenter (){
        TrendingPresenter trendingPresenter = new TrendingPresenter(binding,
                getActivity());
        mDataTrendingSongList = new ArrayList<>();
        getDataTrendingSong();
        adapterTrendingSong = new AdapterTrendingSong(mDataTrendingSongList, getActivity(),phoneNumber);
        trendingPresenter.SetTrendingSong(adapterTrendingSong);
    }
    // get data trending song from firebase
    private void getDataTrendingSong() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("trending_song");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataTrendingSong = snapshot.getValue(DataSong.class);
                if (dataTrendingSong != null){
                    mDataTrendingSongList.add(dataTrendingSong);
                    adapterTrendingSong.notifyDataSetChanged();
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
    }





}