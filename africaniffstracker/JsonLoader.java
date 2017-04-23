package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by meqdim on 17/6/2015.
 */
public class JsonLoader{
    public static String ARG_FILENAME = "countries.json";

    public JsonLoader() {super();}

    public String loadJSONFromAsset(AssetManager am) {
        String json = null;
        try {
            InputStream is = am.open(ARG_FILENAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}

