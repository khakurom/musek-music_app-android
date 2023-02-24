package com.example.musek.model_data;

public class ArtistTrending {
    private int idPlaylist;
    private String imgArtist;
    private String nameArtist;
    private String title;

    public ArtistTrending() {
    }
    public ArtistTrending(String imgArtist, String nameArtist) {

        this.imgArtist = imgArtist;
        this.nameArtist = nameArtist;
    }

    public ArtistTrending(int idPlaylist,String imgArtist, String title) {
        this.idPlaylist = idPlaylist;
        this.imgArtist = imgArtist;
        this.title = title;
    }

    public int getIdPlaylist() {
        return idPlaylist;
    }

    public String getImgArtist() {
        return imgArtist;
    }

    public void setImgArtist(String imgArtist) {
        this.imgArtist = imgArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
