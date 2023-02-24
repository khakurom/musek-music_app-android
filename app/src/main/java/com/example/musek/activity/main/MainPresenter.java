package com.example.musek.activity.main;

import android.content.Intent;
import android.widget.Toast;

import com.example.musek.activity.login.SignInActivity;

public class MainPresenter {
    private FunctionMainActivity functionMainActivity;
    private MainActivity activity;
    private static final int REQUEST_CODE = 1;

    public MainPresenter(FunctionMainActivity functionMainActivity,MainActivity activity) {
        this.functionMainActivity = functionMainActivity;
        this.activity = activity;
    }
    public void setButtonPlayOrPause (Boolean isPlaying){
        if (isPlaying){
            functionMainActivity.setButtonIsPause();
        }else {
            functionMainActivity.setButtonIsPlay();
        }
    }
    public void setLayoutBottom(String song, String single, String imageSingle){
        if (song == null && single == null && imageSingle == null ){
            return;
        }
        functionMainActivity.setLayoutBottom();

    }
    public void playNextSong (){
        functionMainActivity.playNextSong();
    }
    public void playPreSong(){
        functionMainActivity.playPreSong();
    }

    public void checkPhoneNumber (String phoneNumber){
        if (phoneNumber == null){
            Intent intent = new Intent(activity, SignInActivity.class);
            activity.startActivityForResult(intent,REQUEST_CODE);
        }else {
            functionMainActivity.addSongToFavorite(phoneNumber);
        }
    }
}
