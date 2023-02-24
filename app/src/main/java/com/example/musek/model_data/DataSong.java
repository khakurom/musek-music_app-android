package com.example.musek.model_data;

import java.io.Serializable;

public class DataSong implements Serializable {
    private String imageSingle;
    private String nameSingle;
    private String nameSong;
    private int songId;
    private String urlMp3;

    public DataSong() {
    }

    public DataSong(String imageSingle, String nameSingle, String nameSong,int songId, String urlMp3) {

        this.imageSingle = imageSingle;
        this.nameSingle = nameSingle;
        this.nameSong = nameSong;
        this.songId = songId;
        this.urlMp3 = urlMp3;

    }

    public DataSong(String imageSingle, String nameSingle, String nameSong) {
        this.imageSingle = imageSingle;
        this.nameSingle = nameSingle;
        this.nameSong = nameSong;
    }

    public String getImageSingle() {
        return imageSingle;
    }

    public void setImageSingle(String imageSingle) {
        this.imageSingle = imageSingle;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameSingle() {
        return nameSingle;
    }

    public void setNameSingle(String nameSingle) {
        this.nameSingle = nameSingle;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getUrlMp3() {
        return urlMp3;
    }

    public void setUrlMp3(String urlMp3) {
        this.urlMp3 = urlMp3;
    }
}
