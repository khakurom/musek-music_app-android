package com.example.musek.fragment.favorite_song;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musek.R;
import com.example.musek.adapter.AdapterFavoriteSong;
import com.example.musek.databinding.FragmentFavoriteSongBinding;
import com.example.musek.databinding.FragmentPlaylistBinding;
import com.example.musek.fragment.playlist.PlaylistPresenter;
import com.example.musek.model_data.DataSong;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_favorite_song extends Fragment {
    private FragmentFavoriteSongBinding binding;
    private View mView;
    private String phoneNumber;
    private AdapterFavoriteSong adapterFavoriteSong;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteSongBinding.inflate(inflater, container,
                false);
        mView = binding.getRoot();
        getPhoneNumber ();
        setAdapter();
        return mView;
    }
    private void getPhoneNumber() {
        Bundle getData = getArguments();
        if (getData != null){
            phoneNumber = getData.getString("key_phone_number");

        }
    }
    private void setAdapter() {
        List<DataSong> dataSongList = new ArrayList<>();
        getFavoriteSong(dataSongList);
        adapterFavoriteSong = new AdapterFavoriteSong(dataSongList,
                getActivity(),phoneNumber);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                RecyclerView.VERTICAL, false);
        binding.rcvFavoriteSong.setLayoutManager(linearLayoutManager);
        binding.rcvFavoriteSong.setNestedScrollingEnabled(false);
        binding.rcvFavoriteSong.setFocusable(false);
        binding.rcvFavoriteSong.setAdapter(adapterFavoriteSong);
    }
    private void getFavoriteSong (List <DataSong> dataSongList){
        if (phoneNumber == null){
            Toast.makeText(getActivity(), "You must to login account", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_account").
                child(phoneNumber).child("favorite_song");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null){
                    dataSongList.add(dataSong);

                }
                adapterFavoriteSong.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                for (DataSong songDelete : dataSongList) {
                    if (dataSong.getNameSong().equals( songDelete.getNameSong())) {
                        dataSongList.remove(songDelete);
                        break;
                    }
                }
                adapterFavoriteSong.notifyDataSetChanged();
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