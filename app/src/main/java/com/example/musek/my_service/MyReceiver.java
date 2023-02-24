package com.example.musek.my_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.example.musek.model_data.DataSong;


public class MyReceiver extends BroadcastReceiver {
    private DataSong dataTrendingSong;
    @Override
    public void onReceive(Context context, Intent intent) {
        int action = intent.getIntExtra("key_action",0);
        Bundle getData = intent.getExtras();
        if (getData != null){
            dataTrendingSong = (DataSong) getData.get("key");
        }

        Intent intent1 = new Intent(context, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", dataTrendingSong);
        intent1.putExtras(bundle);
        intent1.putExtra("key_action_1", action);
        context.startService(intent1);
    }
}
