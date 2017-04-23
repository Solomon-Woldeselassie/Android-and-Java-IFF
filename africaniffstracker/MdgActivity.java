package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
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


public class MdgActivity extends Activity {

    public static String ARG_COUNTRY = "1";
    public MdgData[] mdg_country_data;
    private String country;

    // JSON Node names
    private static final String TAG_MDG_GOAL = "mdgs";
    private static final String TAG_IDENTIFIER= "mdg_data";
    private static final String TAG_MDG= "mdg";
    private static final String TAG_COUNTRY = "country";

    JSONArray mdg_data = null;
    JsonLoader jl = new JsonLoader();
    AssetManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdg);

        am = getAssets();
        mdg_country_data = null;
        country = null;

        jl = new JsonLoader();
        jl.ARG_FILENAME = "mdgs.json";

        String jsonStr = jl.loadJSONFromAsset(am);

//        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray mdgs_data = jsonObj.getJSONArray(TAG_MDG_GOAL);
                int numY = mdgs_data.length();
                String[] mdgs = new String[numY];
                String[] mdgs_full = new String[numY];
                for(int y = 0; y < numY; y++){
                    JSONObject c = mdgs_data.getJSONObject(y);
                    mdgs[y] = c.names().getString(0);//abbreviated MDG4 goal
                    mdgs_full[y] = c.getString(mdgs[y]);//full text of the MDG4 goal
//                    toast(mdgs_full[y]);
                }
                jl.ARG_FILENAME = "mdg_data.json";
                jsonStr = jl.loadJSONFromAsset(am);
                jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                mdg_data = jsonObj.getJSONArray(TAG_IDENTIFIER);
                int num = mdg_data.length();
                // looping through All MDG Data to find a country's data
                for (int i = 0; i < num; i++) {
                    JSONObject c = mdg_data.getJSONObject(i);
                    String current = c.getString(TAG_COUNTRY);

                    if(ARG_COUNTRY.compareToIgnoreCase(current) == 0){
                        country = current;
                        JSONObject icd = c.getJSONObject(TAG_MDG);
                        mdg_country_data = new MdgData[numY];
                        for (int ifd = 0; ifd < numY; ifd++){
                            MdgData mdgD;
                            String amount = icd.getString(mdgs[ifd]);
                            mdgD = new MdgData(mdgs_full[ifd], amount);//full text MDG4 goal, achieved amount
                            mdg_country_data[ifd] = mdgD;
                        }
                        /*int flagId = getResources().getIdentifier(country.toLowerCase(Locale.getDefault()).concat(getString(R.string.flagExt)),
                                "drawable", this.getPackageName());
                        if(flagId > 0)
                            (findViewById(R.id.iff_impact_data_country_flag_image_view)).setBackground((Drawable) getResources().getDrawable(flagId));
*/
                        ((TextView) findViewById(R.id.iff_impact_data_title)).setText(country.replaceAll("_", " "));
                        MdgDataAdapter adapter;
                        adapter = new MdgDataAdapter(this, R.layout.mdg_list_item, mdg_country_data);
                        ListView listView1 = (ListView)findViewById(R.id.listview_iff_impact_country_data);
                        listView1.setAdapter(adapter);
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

        if(country == null){
            ((TextView) findViewById(R.id.iff_impact_data_title)).setText(ARG_COUNTRY.replaceAll("_", " "));
            ((TextView) findViewById(R.id.iff_impact_header_text_view)).setText(R.string.mdg_data_not_available);
            /*MdgDataAdapter adapter;
            adapter = new MdgDataAdapter(this, R.layout.mdg_list_item, mdg_country_data);
            ListView listView1 = (ListView)findViewById(R.id.listview_iff_impact_country_data);
            listView1.setAdapter(adapter);*/
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mdg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(MdgActivity.this, GetInvolvedActivity.class);
        startActivity(intent);
    }

    public void toast (String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }
}
