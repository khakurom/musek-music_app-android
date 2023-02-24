package com.example.musek.activity.playsong;

import android.app.Activity;
import android.content.Intent;

import com.example.musek.activity.login.SignInActivity;

public class PlaySongPresenter {
    private FunctionPlaySong functionPlaySong;
    private Activity activity;
    private static final int REQUEST_CODE = 1;


    public PlaySongPresenter(Activity activity,FunctionPlaySong functionPlaySong) {
        this.activity = activity;
        this.functionPlaySong = functionPlaySong;
    }

    public void playSong(){
        functionPlaySong.playOrPauseSong();
    }
    public void checkStatusPlaying(Boolean isPlaying){
        if (isPlaying){
            functionPlaySong.setIconPause();
        }else {
            functionPlaySong.setIconPlay();
        }
    }
    public void setLayoutPlaySong (String song, String single, String imageSingle){
        if (song == null && single == null && imageSingle == null ){
            return;
        }
        functionPlaySong.setLayout();
    }
    public void setAdapter(){
        functionPlaySong.setAdapter();
    }
    public void checkPhoneNumber (String phoneNumber){
        if (phoneNumber == null){
            Intent intent = new Intent(activity, SignInActivity.class);
            activity.startActivityForResult(intent,REQUEST_CODE);
        }else {
            functionPlaySong.addSongToFavorite(phoneNumber);
        }
    }








}
