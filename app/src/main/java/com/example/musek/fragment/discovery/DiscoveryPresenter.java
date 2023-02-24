package com.example.musek.fragment.discovery;

public class DiscoveryPresenter {
    private DiscoveryInterface function;

    public DiscoveryPresenter(DiscoveryInterface function) {
        this.function = function;
    }
    public void setAdapterFeature(){
        function.setAdapter();
    }
    public void setAdapterArtistTrending(){
        function.setAdapter();
    }

}
