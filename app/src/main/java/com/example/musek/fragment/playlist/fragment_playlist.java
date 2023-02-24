package com.example.musek.fragment.playlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.musek.R;
import com.example.musek.adapter.AdapterPlayList;
import com.example.musek.databinding.FragmentPlaylistBinding;
import com.example.musek.model_data.DataSong;
import com.example.musek.model_data.PlaylistObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_playlist extends Fragment implements FunctionPlaylist {
    private FragmentPlaylistBinding binding;
    private View mView;
    private AdapterPlayList adapterPlayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlaylistBinding.inflate(inflater, container,
                false);
        mView = binding.getRoot();
        PlaylistPresenter presenter = new PlaylistPresenter(this);
        getDetailPlaylist(presenter);
        return mView;
    }


    private void getDetailPlaylist(PlaylistPresenter presenter) {
        PlaylistObject object = new PlaylistObject();
        int flag = 0;
        Bundle getData = this.getArguments();
        if (getData != null) {
            object = (PlaylistObject) getData.get("key_object");
            flag = getData.getInt("key_flag");
        }
        presenter.setAdapter(object,flag);
    }

    @Override
    public void setAdapter(PlaylistObject object, int flag) {
        String namePlayList = selectPlaylist(object);

        adapterPlayList = new AdapterPlayList(getDataPlaylist(namePlayList, object.getNameType()),
                getActivity(), namePlayList ,flag);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        binding.rcvPlaylist.setLayoutManager(linearLayoutManager);
        binding.rcvPlaylist.setNestedScrollingEnabled(false);
        binding.rcvPlaylist.setFocusable(false);
        binding.rcvPlaylist.setAdapter(adapterPlayList);
    }

    // get data trending song from firebase
    private List<DataSong> getDataPlaylist(String namePlayList, String nameType) {

        List<DataSong> dataPlayList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child(nameType).child(namePlayList);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong playlist = snapshot.getValue(DataSong.class);
                if (playlist != null) {
                    dataPlayList.add(playlist);
                    adapterPlayList.notifyDataSetChanged();
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
        return dataPlayList;
    }



    private String selectPlaylist(PlaylistObject object) {
        String namePlaylist = "";
        switch (object.getIdPlaylist()) {
            case 1:
            case 4:
                namePlaylist = "HieuThuHai";
                binding.imgPlaylist.setImageResource(R.drawable.feature_1);
                binding.tvNamePlaylist.setText(object.getTitle());
                break;
            case 2:
                namePlaylist = "KetHopGayNghien";
                binding.imgPlaylist.setImageResource(R.drawable.feature_2);
                binding.tvNamePlaylist.setText(object.getTitle());
                break;
            case 3:
                namePlaylist = "NhacMoiMoiNgay";
                binding.imgPlaylist.setImageResource(R.drawable.feature_3);
                binding.tvNamePlaylist.setText(object.getTitle());
                break;
            case 5:
                namePlaylist = "DucPhuc";
                Glide.with(getActivity()).load(object.getImgPlaylist()).centerCrop().into(
                        binding.imgPlaylist);
                binding.tvNamePlaylist.setText(object.getTitle());
                break;
            case 6:
                namePlaylist = "PhanManhQuynh";
                Glide.with(getActivity()).load(object.getImgPlaylist()).centerCrop().into(
                        binding.imgPlaylist);
                binding.tvNamePlaylist.setText("The best song of Phan Manh Quynh");
                break;
        }

        return namePlaylist;

    }





}