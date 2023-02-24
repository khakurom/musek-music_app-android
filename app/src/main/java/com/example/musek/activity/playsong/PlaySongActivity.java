package com.example.musek.activity.playsong;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musek.R;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.adapter.AdapterRecommend;
import com.example.musek.databinding.ActivityPlaySongBinding;

import com.example.musek.fragment.account.fragment_account;
import com.example.musek.model_data.DataSong;
import com.example.musek.my_service.MyService;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity implements FunctionPlaySong {
    private ActivityPlaySongBinding binding;
    private boolean isPlaying;
    private int flag;
    private String namePlaylist;
    private int action;
    private PlaySongPresenter playSongPresenter;
    private Handler handler = new Handler();
    private DataSong objectDetailSong;
    private List<DataSong> mDataSongList;
    private AdapterRecommend adapterRecommend;
    private static String phoneNumber;
    private static final int REQUEST_CODE = 1;

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            objectDetailSong = (DataSong) bundle.get("key_detail_song");
            isPlaying = (boolean) bundle.get("status_playing");
            action = (int) bundle.get("action");
            playSongPresenter.checkStatusPlaying(isPlaying);

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        // view binding
        binding = ActivityPlaySongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // method
        getDetailSong();
        declarePresenter();
        clickStartService();
        setOnClick();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            phoneNumber = (String) data.getSerializableExtra("key_phone_number");


        }
    }


    // get detail song from main activity
    public void getDetailSong() {
        Bundle getData = getIntent().getExtras();
        if (getData != null) {
            objectDetailSong = (DataSong) getData.get("key_detail_song");
            flag = getData.getInt("key_flag");
            namePlaylist = getData.getString("key_name_playlist");
        }
    }


    // control presenter
    private void declarePresenter() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter
                ("send_data"));
        playSongPresenter = new PlaySongPresenter(this,this);
        playSongPresenter.setLayoutPlaySong(objectDetailSong.getNameSong(),
                objectDetailSong.getNameSingle(), objectDetailSong.getImageSingle());
        playSongPresenter.playSong();
        playSongPresenter.setAdapter();

    }


    // set on click play song
    @Override
    public void playOrPauseSong() {

        binding.imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    SendActivityToService(MyService.ACTION_PAUSE);
                } else {
                    SendActivityToService(MyService.ACTION_RESUME);
                }
            }
        });
    }

    @Override
    public void setIconPlay() {
        binding.imgPlayOrPause.setImageResource(R.drawable.icon_play);
        stopAnimationImage();
    }

    @Override
    public void setIconPause() {
        binding.imgPlayOrPause.setImageResource(R.drawable.icon_pause);
        startAnimationImage();
    }


    @Override
    public void setLayout() {
        binding.tvNameSong.setText(objectDetailSong.getNameSong());
        binding.tvNameSingle.setText(objectDetailSong.getNameSingle());
        Glide.with(this).load(objectDetailSong.getImageSingle()).centerCrop().into(binding.imgSingle);
    }

    @Override
    public void setAdapter() {
//        compareCondition();
//
//        adapterRecommend = new AdapterRecommend(mDataSongList,
//                PlaySongActivity.this,flag);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
//                RecyclerView.VERTICAL, false);
//        binding.rcvPlaySong.setLayoutManager(linearLayoutManager);
//        binding.rcvPlaySong.setFocusable(false);
//        binding.rcvPlaySong.setNestedScrollingEnabled(false);
//        binding.rcvPlaySong.setAdapter(adapterRecommend);
    }

    @Override
    public void addSongToFavorite(String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("user_account").
                child(phoneNumber).child("favorite_song");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(objectDetailSong.getNameSong())){
                    Toast.makeText(PlaySongActivity.this, "Added", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child(objectDetailSong.getNameSong()).setValue(objectDetailSong);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // start foreground service
    public void clickStartService() {
        MyService.setPlaySongActivity(PlaySongActivity.this);
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", objectDetailSong);
        intent.putExtras(bundle);
        intent.putExtra("key_flag", flag);
        intent.putExtra("key_name_playlist", namePlaylist);
        startService(intent);
    }

    // send action to service
    private void SendActivityToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", objectDetailSong);
        intent.putExtras(bundle);
        intent.putExtra("key_action_1", action);
        startService(intent);
    }

    // set on click
    private void setOnClick() {
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra("key_detail_song", objectDetailSong);
                data.putExtra("key_flag", flag);
                data.putExtra("key_name_playlist", namePlaylist);
                data.putExtra("status_playing", isPlaying);
                data.putExtra("key_phone_number", phoneNumber);
                setResult(RESULT_OK, data);
                finish();
                onBackPressed();
            }
        });
        binding.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSongPresenter.checkPhoneNumber(phoneNumber);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    // stop circle image (single)
    private void stopAnimationImage() {
        binding.imgSingle.animate().cancel();
    }

    // start circle image (single)
    private void startAnimationImage() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                binding.imgSingle.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        binding.imgSingle.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();

    }

    private void compareCondition() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        switch (flag) {

            case 1:
                if (setNameType() == null) {
                    return;
                }
                DatabaseReference databaseReference1 = database.getReference("all_song").
                        child(setNameType());
                loadDataFromFirebase(databaseReference1);
                break;
            case 2:
            case 3:
                if (setNameType() == null || namePlaylist == null) {
                    return;
                }

                DatabaseReference databaseReference2 = database.getReference("all_song").
                        child(setNameType()).child(namePlaylist);
                loadDataFromFirebase(databaseReference2);
                break;
        }

    }

    private void loadDataFromFirebase(DatabaseReference databaseReference) {
        mDataSongList = new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataSong dataTrendingSong = snapshot.getValue(DataSong.class);
                if (dataTrendingSong != null) {
                    mDataSongList.add(dataTrendingSong);
                    adapterRecommend.notifyDataSetChanged();

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

    private String setNameType() {
        String nameType = "";
        switch (flag) {
            case 1:
                nameType = "trending_song";
                break;
            case 2:
                nameType = "playlist";
                break;
            case 3:
                nameType = "artist_playlist";
                break;
        }
        return nameType;
    }
    public static void setPhoneNumber(String phoneNumber1) {
        phoneNumber = phoneNumber1;
    }


}