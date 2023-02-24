package com.example.musek.fragment.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.musek.R;
import com.example.musek.activity.login.SignInActivity;
import com.example.musek.activity.login.SignUpActivity;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.adapter.AdapterRecommendPlaylist;
import com.example.musek.databinding.FragmentAccountBinding;
import com.example.musek.model_data.ArtistTrending;
import com.example.musek.model_data.DataSong;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class fragment_account extends Fragment implements AccountInterface{
    private View mView;
    private FragmentAccountBinding binding;
    private static String phoneNumber;
    private List <DataSong> favoriteSongList;
    private AccountPresenter presenter;
    private MainActivity activity;
    private static final int REQUEST_CODE = 1;
    private AdapterRecommendPlaylist adapter1;


    public fragment_account(MainActivity activity, String phoneNumber) {
        this.activity = activity;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        mView  = binding.getRoot();
        presenter = new AccountPresenter(this);
        presenter.setAdapter();
        setOnClick ();
        return mView;
    }


    private void setOnClick() {
        binding.imgAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.checkLogin(phoneNumber, Gravity.CENTER);
            }
        });
        binding.favoriteSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToFragmentFavoriteSong(phoneNumber);
            }
        });
    }


    @Override
    public void setAdapter() {
        // set adapter recommend playlist
        adapter1  = new AdapterRecommendPlaylist(getArtistList(),activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false);
        binding.rcvRecommendPlaylist.setLayoutManager(linearLayoutManager);
        binding.rcvRecommendPlaylist.setNestedScrollingEnabled(false);
        binding.rcvRecommendPlaylist.setFocusable(false);
        binding.rcvRecommendPlaylist.setAdapter(adapter1);



    }
    // open dialog login

    @Override
    public void openDialogLogin(int gravity) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        Button btSignUp =  dialog.findViewById(R.id.bt_signUp);
        Button btSignIn = dialog.findViewById(R.id.bt_signIn);
        ImageView imgClose = dialog.findViewById(R.id.img_close);
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                activity.startActivityForResult(intent,REQUEST_CODE);
                dialog.dismiss();
            }
        });
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                activity.startActivityForResult(intent,REQUEST_CODE);
                dialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    // open dialog logout

    @Override
    public void openDialogLogout(int gravity) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        Button btLogout =  dialog.findViewById(R.id.bt_logout);
        ImageView imgClose = dialog.findViewById(R.id.img_close);

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_CODE);
                dialog.dismiss();
            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public static void setPhoneNumber(String phoneNumber1) {
        phoneNumber = phoneNumber1;
    }





    // get data recommend playlist
    private List<ArtistTrending> getArtistList (){
        List <ArtistTrending> list = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("all_song").
                child("trending_artist");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ArtistTrending playlist = snapshot.getValue(ArtistTrending.class);
                if (playlist != null) {
                    list.add(playlist);
                    adapter1.notifyDataSetChanged();
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

        return list;
    }


}