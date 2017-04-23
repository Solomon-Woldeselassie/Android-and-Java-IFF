package com.miasmesh.meqdim.africaniffstracker;

/**
 * Created by meqdim on 29/7/2015.
 */
public class PublicationsData {
    public String title;
    public String dop;
    public String url;
    public Integer cover;

    public PublicationsData (){super();}

    public PublicationsData (String title, String dop, String url, Integer cover){
        super();
        this.title = title;
        this.dop = dop;
        this.url = url;
        this.cover = cover;
    }
}
