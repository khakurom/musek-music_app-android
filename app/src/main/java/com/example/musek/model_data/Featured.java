package com.example.musek.model_data;

import java.io.Serializable;

public class Featured implements Serializable {
    private int idPlaylist;
    private int imgFeatured;
    private String title;

    public Featured() {
    }

    public Featured(int idPlaylist, int imgFeatured, String title) {
        this.idPlaylist = idPlaylist;
        this.imgFeatured = imgFeatured;
        this.title = title;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public int getImgFeatured() {
        return imgFeatured;
    }

    public void setImgFeatured(int imgFeatured) {
        this.imgFeatured = imgFeatured;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
