package com.example.musek.my_service;

import static com.example.musek.my_service.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musek.R;
import com.example.musek.activity.main.MainActivity;
import com.example.musek.activity.playsong.PlaySongActivity;
import com.example.musek.model_data.DataSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {
    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_PRE = 3;
    public static final int ACTION_NEXT = 4;
    public static final int ACTION_START = 5;

    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private DataSong mDataSong;
    private DataSong mSong;
    private static PlaySongActivity playSongActivity;
    private static MainActivity mainActivity;
    private TextView tvCurrentTime, tvTotalTime, tvNameSingle, tvNameSong;
    private SeekBar seekBar;
    private Handler handler;
    private int flag, numberSong;
    private Runnable runnable;
    private ImageView imgNext, imgPrevious, imgSingle, imgBack;
    private String nameCurrentSong, nameOtherSong, nameType , namePlaylist,phoneNumber ;



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle getData = intent.getExtras();
        declareID();

        if (getData != null) {
            mDataSong = (DataSong) getData.get("key");
            flag = getData.getInt("key_flag");
            namePlaylist = getData.getString("key_name_playlist");
            setNameTypeAndNumberSong ();
            setOnClick();
            if (mDataSong != null) {
                mSong = mDataSong;
                startMusic(mDataSong, getApplicationContext());
                sendNotificationMediaStyle(mDataSong);
            }

        }else {
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        }

        int actionMusic = intent.getIntExtra("key_action_1", 0);
        handleMusic(actionMusic);
        return START_NOT_STICKY;

    }


    private void declareID() {
        if (playSongActivity == null){
            return;
        }
        tvCurrentTime = playSongActivity.findViewById(R.id.tv_currentTime);
        tvTotalTime = playSongActivity.findViewById(R.id.tv_totalTime);
        seekBar = playSongActivity.findViewById(R.id.seekBar);
        imgPrevious = playSongActivity.findViewById(R.id.img_previous);
        imgNext = playSongActivity.findViewById(R.id.img_next);
        imgSingle = playSongActivity.findViewById(R.id.img_single);
        tvNameSingle = playSongActivity.findViewById(R.id.tv_nameSingle);
        tvNameSong = playSongActivity.findViewById(R.id.tv_nameSong);
        imgBack = playSongActivity.findViewById(R.id.img_back);

    }
    // set on click in play song activity
    private void setOnClick() {
        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });
    }

    private void playPreviousSong() {
        if (mDataSong== null){
            return ;
        }
        int index = mDataSong.getSongId();

        compareCondition(index);

    }

    private void playNextSong() {
        if (mDataSong== null){
            return ;
        }
        int index = mDataSong.getSongId() + 2;
        if (index > numberSong){
            index = 1;
        }
        compareCondition(index);
    }
    private void compareCondition(int index){
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
                    mDataSong = dataSong;
                    startServiceAgain(mDataSong);
                    if (playSongActivity.isFinishing() ){
                        return;
                    }
                    tvNameSong.setText(dataSong.getNameSong());
                    tvNameSingle.setText(dataSong.getNameSingle());
                    Glide.with(playSongActivity).load(dataSong.getImageSingle()).centerCrop().
                            into(imgSingle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void startServiceAgain (DataSong dataSong){
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataSong);
        bundle.putSerializable("key_flag", flag);
        bundle.putSerializable("key_name_playlist", namePlaylist);
        intent.putExtras(bundle);
        startService(intent);
    }


    public static void setPlaySongActivity(PlaySongActivity activity) {
        playSongActivity = activity;
    }
    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }

    // start music
    public void startMusic(DataSong dataTrendingSong, Context context) {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(dataTrendingSong.getUrlMp3()));
            nameCurrentSong = dataTrendingSong.getNameSong();
        }
        if (mediaPlayer != null) {
            nameOtherSong = dataTrendingSong.getNameSong();

            if (!nameCurrentSong.equals(nameOtherSong)) {
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(context, Uri.parse(dataTrendingSong.getUrlMp3()));
                nameCurrentSong = nameOtherSong;
            }
        }
        mediaPlayer.start();
        sendNotificationMediaStyle(mDataSong);
        isPlaying = true;
        seekBar.setMax(mediaPlayer.getDuration());
        tvTotalTime.setText(formatTime((long) mediaPlayer.getDuration()));
        String totalTime = tvTotalTime.getText().toString().trim();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                tvCurrentTime.setText(formatTime((long) mediaPlayer.getCurrentPosition()));
                String currentTime = tvCurrentTime.getText().toString().trim();
                if (currentTime.equals(totalTime)){
                    playNextSong();
                    if(!mainActivity.isFinishing()){
                        mainActivity.playNextSong();
                    }

                }
                handler.postDelayed(this, 1000);
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 1000);
        sendServiceToActivity(ACTION_START,getApplicationContext());

    }


    // handle music
    private void handleMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                PauseMusic();
                handler.removeCallbacks(runnable);
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_PRE:
                playPreviousSong();
                sendServiceToActivity(ACTION_PRE,getApplicationContext());
                break;
            case ACTION_NEXT:
                playNextSong();
                sendServiceToActivity(ACTION_NEXT,getApplicationContext());
                break;

        }
    }


    // pause music
    private void PauseMusic() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            sendNotificationMediaStyle(mSong);
            sendServiceToActivity(ACTION_PAUSE,getApplicationContext());
        }

    }

    // resume music
    private void resumeMusic() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
            sendNotificationMediaStyle(mSong);
            sendServiceToActivity(ACTION_RESUME,getApplicationContext());
        }
    }
    private void sendNotificationMediaStyle(DataSong dataTrendingSong) {
        Intent intent = new Intent(this, PlaySongActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataTrendingSong);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT,bundle);



        @SuppressLint("RemoteViewLayout") RemoteViews remoteViews = new RemoteViews(getPackageName()
                , R.layout.custom_notification);
        if (dataTrendingSong.getImageSingle() == null){
            Toast.makeText(getApplicationContext(), "dataTrendingSong.getImageSingle() == null"
                    , Toast.LENGTH_SHORT).show();
        }
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(dataTrendingSong.getImageSingle())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        remoteViews.setImageViewBitmap(R.id.img_single, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) { }
                });


        remoteViews.setTextViewText(R.id.tv_nameSong, dataTrendingSong.getNameSong());
        remoteViews.setTextViewText(R.id.tv_nameSingle, dataTrendingSong.getNameSingle());

        remoteViews.setImageViewResource(R.id.img_previous, R.drawable.icon_pre);
        remoteViews.setImageViewResource(R.id.img_playOrPause, R.drawable.icon_pause);
        remoteViews.setImageViewResource(R.id.img_next, R.drawable.icon_next);
        if (isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.img_playOrPause, getPendingIntent(this, ACTION_PAUSE
            ,dataTrendingSong));
            remoteViews.setImageViewResource(R.id.img_playOrPause, R.drawable.icon_pause);
            remoteViews.setOnClickPendingIntent(R.id.img_previous,getPendingIntent(this,ACTION_PRE,
                    dataTrendingSong));
            remoteViews.setOnClickPendingIntent(R.id.img_next,getPendingIntent(this,ACTION_NEXT,
                    dataTrendingSong));
        } else {
            remoteViews.setOnClickPendingIntent(R.id.img_playOrPause, getPendingIntent(this, ACTION_RESUME
            ,dataTrendingSong));
            remoteViews.setImageViewResource(R.id.img_playOrPause, R.drawable.icon_play);
            remoteViews.setOnClickPendingIntent(R.id.img_previous,getPendingIntent(this,ACTION_PRE,
                    dataTrendingSong));
            remoteViews.setOnClickPendingIntent(R.id.img_next,getPendingIntent(this,ACTION_NEXT,
                    dataTrendingSong));
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSmallIcon(R.drawable.icon_music)
                .setSound(null)
                .build();

        startForeground(1, notification);

    }



    private PendingIntent getPendingIntent(Context context, int action, DataSong dataTrendingSong) {
        Intent intent = new Intent(this, MyReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataTrendingSong);
        intent.putExtras(bundle);
        intent.putExtra("key_action", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    public void sendServiceToActivity(int action, Context context) {
        Intent intent = new Intent("send_data");
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_detail_song", mDataSong);
        bundle.putSerializable("action", action);
        bundle.putSerializable("status_playing", isPlaying);
        bundle.putSerializable("key_name_playlist", namePlaylist);
        bundle.putSerializable("key_flag", flag);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    // set name type and number song
    private void setNameTypeAndNumberSong (){
        switch (flag){
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
        }
    }




    // covert time
    private String formatTime(Long mls) {
        String timeString = "";
        String secondString;
        int hours = (int) (mls / (1000 * 60 * 60));
        int minutes = (int) (mls % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((mls % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timeString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        timeString = timeString + minutes + ":" + secondString;
        return timeString;
    }

    // stop service when the app is killed by swiping
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }


}
