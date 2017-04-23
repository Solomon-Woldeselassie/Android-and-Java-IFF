package com.miasmesh.meqdim.africaniffstracker;

/**
 * Created by meqdim on 29/7/2015.
 */
public class PanelListData {

    public String name;
    public String title;
    public String role;
    public Integer pic;
    public String profile;

    public PanelListData (){super();}

    public PanelListData (String name, String title, String role, Integer pic, String profile){
        super();
        this.name = name;
        this.title = title;
        this.role = role;
        this.pic = pic;
        this.profile = profile;
    }
}
