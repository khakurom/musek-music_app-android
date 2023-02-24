package com.example.musek.fragment.playlist;

import com.example.musek.model_data.PlaylistObject;

public class PlaylistPresenter {
    private FunctionPlaylist functionPlaylist;

    public PlaylistPresenter(FunctionPlaylist functionPlaylist) {
        this.functionPlaylist = functionPlaylist;
    }
    public void setAdapter(PlaylistObject object, int flag){
        if (object == null || flag == 0){
            return;
        }
        functionPlaylist.setAdapter(object, flag);
    }
}
