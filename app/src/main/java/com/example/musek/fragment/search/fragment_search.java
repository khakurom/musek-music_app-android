package com.example.musek.fragment.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.musek.R;
import com.example.musek.adapter.AdapterAllSong;
import com.example.musek.databinding.FragmentPlaylistBinding;
import com.example.musek.databinding.FragmentSearchBinding;
import com.example.musek.model_data.DataSong;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_search extends Fragment {
    private String phoneNumber;
    private FragmentSearchBinding binding;
    private View mView;
    private AdapterAllSong adapterAllSong;
    private List<DataSong> dataSongList;

    public fragment_search(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container,
                false);
        mView = binding.getRoot();
        setOnClick();
        setRcvSearch();
        return mView;
    }

    private void setOnClick() {
        binding.searchSong.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapterAllSong.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterAllSong.getFilter().filter(newText);
                return false;
            }
        });
    }
    // set rcv search film
    private void setRcvSearch() {
        getDataAllSong ();
        adapterAllSong = new AdapterAllSong(dataSongList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        binding.rcvAllSong.setLayoutManager(linearLayoutManager);
        binding.rcvAllSong.setFocusable(false);
        binding.rcvAllSong.setNestedScrollingEnabled(false);
        binding.rcvAllSong.setAdapter(adapterAllSong);


    }
    private void getDataAllSong (){
        dataSongList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("trending_song");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    dataSongList.add(dataSong);
                }
                adapterAllSong.notifyDataSetChanged();
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
        getSongDucPhuc ();
    }
    private void getSongDucPhuc (){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("artist_playlist").child("DucPhuc");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    dataSongList.add(dataSong);
                }
                adapterAllSong.notifyDataSetChanged();
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
        getSongHieuThuHai();
    }
    private void getSongHieuThuHai (){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("artist_playlist").child("HieuThuHai");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    dataSongList.add(dataSong);
                }
                adapterAllSong.notifyDataSetChanged();
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
        getSongPhanManhQuynh();
    }

    private void getSongPhanManhQuynh() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("artist_playlist").child("PhanManhQuynh");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    dataSongList.add(dataSong);
                }
                adapterAllSong.notifyDataSetChanged();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_song, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
}