package com.example.musek.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musek.R;
import com.example.musek.databinding.ActivityMainBinding;
import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.fragment.account.fragment_account;
import com.example.musek.fragment.discovery.fragment_discovery;
import com.example.musek.fragment.favorite_song.fragment_favorite_song;
import com.example.musek.fragment.playlist.fragment_playlist;
import com.example.musek.fragment.search.fragment_search;
import com.example.musek.fragment.trending.fragment_trending;

import com.example.musek.model_data.DataSong;
import com.example.musek.model_data.PlaylistObject;
import com.example.musek.my_service.MyService;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FunctionMainActivity {
    private static final int REQUEST_CODE = 1;
    private ActivityMainBinding binding;
    private int  numberSong, index, flag, action ;
    private boolean isPlaying;
    private DataSong object;
    private DataSong objectDetailSong;
    private MainPresenter mainPresenter;
    private String namePlaylist, nameType,phoneNumber,phoneNumberSaved = "";
    private Context context = MainActivity.this;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            objectDetailSong = (DataSong) bundle.get("key_detail_song");
            isPlaying = bundle.getBoolean("status_playing");
            action = (int) bundle.get("action");
            handleAction(action);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setOnClickBottomNavigation();
        mainPresenter = new MainPresenter(this,MainActivity.this);
        setOnClick();
        MyService.setMainActivity(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter("send_data"));
    }
    protected void onDestroy() {
        super.onDestroy();
        deletePhoneNumber ();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            phoneNumber = (String) data.getSerializableExtra("key_phone_number");
            if (phoneNumber != null && !phoneNumber.equals(phoneNumberSaved)) {
                savePhoneNumber();
                phoneNumberSaved = phoneNumber;
                return;
            }

            getPhoneNumber ();
            objectDetailSong = (DataSong) data.getSerializableExtra("key_detail_song");
            isPlaying = (boolean) data.getSerializableExtra("status_playing");
            flag = (int) data.getSerializableExtra("key_flag");
            namePlaylist = (String) data.getSerializableExtra("key_name_playlist");
            phoneNumber = (String) data.getSerializableExtra("key_phone_number");
            getNumberSong(flag);
            openLayoutBottom();
            binding.layoutBottomPlayMusic.setVisibility(View.VISIBLE);
            binding.line2.setVisibility(View.VISIBLE);
        }
    }
    private void savePhoneNumber (){
        fragment_account.setPhoneNumber(phoneNumber);
        PlaySongActivity.setPhoneNumber(phoneNumber);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phone_number",phoneNumber );
        editor.apply();
    }
    private void getPhoneNumber (){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String getPhoneNumber = prefs.getString("phone_number", null);
        if (getPhoneNumber != null) {
            phoneNumber = getPhoneNumber;
        }
    }
    public void deletePhoneNumber (){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("phone_number");
        editor.apply();
    }


    private void setOnClickBottomNavigation() {

        getSupportFragmentManager().beginTransaction().replace(R.id.constraint_layout,
                new fragment_discovery(MainActivity.this,phoneNumber)).commit();
        binding.idBottomNaviBar.setSelectedItemId(R.id.id_discovery);
        binding.idBottomNaviBar.setSelected(false);
        binding.idBottomNaviBar.setActivated(false);
        binding.idBottomNaviBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.constraint_layout,
                                new fragment_account(MainActivity.this,phoneNumber)).commit();
                        break;

                    case R.id.id_discovery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.constraint_layout,
                                new fragment_discovery(MainActivity.this,phoneNumber)).commit();

                        break;
                    case R.id.id_trending:
                        getSupportFragmentManager().beginTransaction().replace(R.id.constraint_layout,
                                new fragment_trending(phoneNumber)).commit();

                        break;
                    case R.id.id_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.constraint_layout,
                                new fragment_search(phoneNumber)).commit();
                        break;
                }
                return true;
            }

        });
    }


    private void handleAction(int action) {
        switch (action) {
            case MyService.ACTION_PAUSE:
                binding.imgPlayOrPause.setImageResource(R.drawable.icon_play);
                break;
            case MyService.ACTION_RESUME:
            case MyService.ACTION_START:
                binding.imgPlayOrPause.setImageResource(R.drawable.icon_pause);
                break;
            case MyService.ACTION_PRE:
                mainPresenter.playPreSong();
                break;
            case MyService.ACTION_NEXT:
                mainPresenter.playNextSong();
                break;


        }
    }

    private void openLayoutBottom() {
        mainPresenter.setLayoutBottom(objectDetailSong.getNameSong(), objectDetailSong.getNameSingle(),
                objectDetailSong.getImageSingle());
        mainPresenter.setButtonPlayOrPause(isPlaying);
    }


    @Override
    public void setButtonIsPause() {
        binding.imgPlayOrPause.setImageResource(R.drawable.icon_pause);
        startAnimationImage();
    }

    @Override
    public void setButtonIsPlay() {
        binding.imgPlayOrPause.setImageResource(R.drawable.icon_play);
        stopAnimationImage();
    }

    @Override
    public void setLayoutBottom() {
        Glide.with(this).load(objectDetailSong.getImageSingle()).centerCrop().into(binding.imgSingle);
        binding.tvNameSong.setText(objectDetailSong.getNameSong());
        binding.tvNameSingle.setText(objectDetailSong.getNameSingle());
    }


    // set on click
    public void setOnClick() {
        binding.layoutBottomPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlaySongActivity();

            }
        });
        binding.imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    SendActivityToService(MyService.ACTION_PAUSE, objectDetailSong);
                    stopAnimationImage();
                } else {
                    SendActivityToService(MyService.ACTION_RESUME, objectDetailSong);
                    binding.imgPlayOrPause.setImageResource(R.drawable.icon_pause);
                    startAnimationImage();
                }
            }
        });
        binding.imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.playNextSong();
            }
        });
        binding.imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mainPresenter.checkPhoneNumber(phoneNumber);
            }
        });


    }



    @Override
    public void playNextSong() {
        if (objectDetailSong == null) {
            Toast.makeText(context, "objectDetailSong == null", Toast.LENGTH_SHORT).show();
            return;
        }
        index = objectDetailSong.getSongId() + 2;
        if (index > numberSong) {
            index = 1;
        }
        compareCondition(index);

    }

    @Override
    public void playPreSong() {
        if (object == null) {
            return;
        }
        index = object.getSongId();
        compareCondition(index);

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
                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                }else {
                    databaseReference.child(objectDetailSong.getNameSong()).setValue(objectDetailSong);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    private void compareCondition(int index) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        switch (flag){
            case 1:
                if (nameType == null ){
                    return;
                }
                DatabaseReference databaseReference1 = database.getReference("all_song").
                        child(nameType).child(String.valueOf(index));
                loadDataFromFirebase (databaseReference1);
                break;
            case 2:
            case 3:
                if (nameType == null || namePlaylist == null){
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference databaseReference2 = database.getReference("all_song").
                        child(nameType).child(namePlaylist).child(String.valueOf(index));
                loadDataFromFirebase (databaseReference2);
                break;
        }

    }

    private void loadDataFromFirebase(DatabaseReference databaseReference) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSong dataSong = snapshot.getValue(DataSong.class);
                if (dataSong != null) {
                    object = dataSong;
                    startServiceAgain(object);
                    if (MainActivity.this.isFinishing()) {
                        return;
                    }
                    binding.tvNameSong.setText(object.getNameSong());
                    binding.tvNameSingle.setText(object.getNameSingle());
                    Glide.with(MainActivity.this).load(object.getImageSingle()).centerCrop().
                            into(binding.imgSingle);
                    startAnimationImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void startServiceAgain(DataSong dataSong) {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataSong);
        bundle.putSerializable("key_flag", flag);
        bundle.putSerializable("key_name_playlist", namePlaylist);
        intent.putExtras(bundle);
        startService(intent);
    }


    // send action to service
    private void SendActivityToService(int action, DataSong dataTrendingSong) {
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataTrendingSong);
        intent.putExtras(bundle);
        intent.putExtra("key_action_1", action);
        startService(intent);
    }

    // go to play song
    public void goToPlaySongActivity() {

        Intent intent = new Intent(this, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", objectDetailSong);
        intent.putExtra("status_playing", isPlaying);
        intent.putExtra("key_flag", flag);
        intent.putExtra("key_name_playlist", namePlaylist);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
        binding.layoutBottomPlayMusic.setVisibility(View.GONE);

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

    // get number song
    private int getNumberSong(int flag) {
        switch (flag) {
            case 1:
                numberSong = 10;
                nameType = "trending_song";
                break;
            case 2:
                numberSong = 5;
                nameType = "playlist";
                break;
            case 3:
                numberSong = 5;
                nameType = "artist_playlist";
                break;
        }
        return numberSong;
    }

    // go to fragment playlist
    public void goToFragmentPlaylist(PlaylistObject object, int flag) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_object", object);
        bundle.putInt("key_flag", flag);
        Fragment fragment = new fragment_playlist();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    // go to fragment favorite song
    public void goToFragmentFavoriteSong (String phoneNumber){
        Bundle bundle = new Bundle();
        bundle.putString("key_phone_number", phoneNumber);
        Fragment fragment = new fragment_favorite_song();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }




}