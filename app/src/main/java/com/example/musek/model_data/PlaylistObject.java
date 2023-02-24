package com.example.musek.model_data;

import java.io.Serializable;

public class PlaylistObject implements Serializable {
    private int idPlaylist;
    private String imgPlaylist;
    private String nameType;
    private String title;

    public PlaylistObject() {
    }

    public PlaylistObject(int idPlaylist, String imgPlaylist, String nameType, String title) {
        this.idPlaylist = idPlaylist;
        this.imgPlaylist = imgPlaylist;
        this.nameType = nameType;
        this.title = title;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getImgPlaylist() {
        return imgPlaylist;
    }

    public void setImgPlaylist(String imgPlaylist) {
        this.imgPlaylist = imgPlaylist;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
