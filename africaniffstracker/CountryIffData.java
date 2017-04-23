package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class CountryIffData extends Activity {

    public static String ARG_COUNTRY = "1";

    public IffData[] iff_country_data;
    private String country = null;

    // JSON Node names
    private static final String TAG_YEAR_DATA = "years";
    private static final String TAG_IFF_DATA = "iff_data";
    private static final String TAG_IFF= "iff";
    private static final String TAG_YEAR = "year";
    private static final String TAG_AMOUNT = "amount";
    private static final String TAG_COUNTRY = "country";

    JSONArray iff_data = null;
    JsonLoader jl = new JsonLoader();
    AssetManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_iff_data);
        am = getAssets();
        iff_country_data = null;


//        jl.ARG_FILENAME = "countries.json";
        jl = new JsonLoader();
        jl.ARG_FILENAME = "years.json";

        String jsonStr = jl.loadJSONFromAsset(am);

//        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
//                toast(ARG_COUNTRY);
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray years_data = jsonObj.getJSONArray(TAG_YEAR_DATA);
                int numY = years_data.length();
                String[] years = new String[numY];
                for(int y = 0; y < numY; y++){
                    JSONObject c = years_data.getJSONObject(y);
                    years[y] = c.names().getString(0);
                }
                jl.ARG_FILENAME = "iff_data.json";
                jsonStr = jl.loadJSONFromAsset(am);
                jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                iff_data = jsonObj.getJSONArray(TAG_IFF_DATA);
                int num = iff_data.length();

                // looping through All IFF Data to find a country's data
                for (int i = 0; i < num; i++) {
                    JSONObject c = iff_data.getJSONObject(i);
                    String current = c.getString(TAG_COUNTRY);
                    if(ARG_COUNTRY.compareToIgnoreCase(current) == 0){
                        country = current;
                        JSONObject icd = c.getJSONObject(TAG_IFF);

                        iff_country_data = new IffData[numY];
                        for (int ifd = 0; ifd < numY; ifd++){
                            IffData iffD;
                            String amount = icd.getString(years[ifd]);
                            iffD = new IffData(years[ifd], amount);
                            iff_country_data[ifd] = iffD;
                        }
//                        if(country != null){
//                            int flagId = getResources().getIdentifier(country.toLowerCase(Locale.getDefault()).concat(getString(R.string.flagExt)),
/*                          int flagId = getResources().getIdentifier(country.toLowerCase(Locale.getDefault()),
                                "drawable", this.getPackageName());
                            if(flagId > 0)
                                (findViewById(R.id.iff_data_country_flag_image_view)).setBackground((Drawable) getResources().getDrawable(flagId));
*/
                            ((TextView) findViewById(R.id.iff_data_title)).setText(ARG_COUNTRY.replaceAll("_", " "));
                            IffDataAdapter adapter;
                            adapter = new IffDataAdapter(this, R.layout.country_iff_data_list_item, iff_country_data);
                            ListView listView1 = (ListView)findViewById(R.id.listview_country_data);
                            listView1.setAdapter(adapter);

//                        }
                    }
                    else
                        continue;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

       /* if(country != null){
            ((TextView) findViewById(R.id.iff_data_title)).setText(country.replaceAll("_", " "));
            IffDataAdapter adapter;
            adapter = new IffDataAdapter(this, R.layout.country_iff_data_list_item, iff_country_data);
            ListView listView1 = (ListView)findViewById(R.id.listview_country_data);
            listView1.setAdapter(adapter);
        }*/
    }



    public void IffImpact(View v){
        Intent intent = new Intent(CountryIffData.this, MdgActivity.class);
        MdgActivity.ARG_COUNTRY = ARG_COUNTRY;
        startActivity(intent);
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(CountryIffData.this, GetInvolvedActivity.class);
        startActivity(intent);
    }

    public void toast (String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }
}
