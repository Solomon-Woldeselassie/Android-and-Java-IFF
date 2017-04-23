package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class PublicationsActivity extends Activity {

    private ProgressDialog pDialog;

    // URL to get dataSrcs JSON
//    private static String url = "http://api.androidhive.info/dataSrcs/";

    // JSON Node names
    private static final String TAG_IDENTIFIER = "documents";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL = "url";
    private static final String TAG_COVER = "cover";
    private static final String TAG_DOP = "dop";

    JSONArray dataSrcs = null;
    JsonLoader jl = new JsonLoader();
    AssetManager am;
    public PublicationsData[] publication_list_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);

        am = getAssets();

        jl = new JsonLoader();
        JsonLoader.ARG_FILENAME = "documents.json";

        String jsonStr = jl.loadJSONFromAsset(am);

        if (jsonStr != null) try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            // Getting JSON Array node
            dataSrcs = jsonObj.getJSONArray(TAG_IDENTIFIER);
            int length = dataSrcs.length();
            publication_list_data = new PublicationsData[length];

            // looping through All dataSrcs
            for (int i = 0; i < length; i++) {
                JSONObject c = dataSrcs.getJSONObject(i);
                PublicationsData pLD = null;
                int imageId = getResources().getIdentifier(c.getString(TAG_COVER).toLowerCase(Locale.getDefault()),
                        "drawable", this.getPackageName());
                pLD = new PublicationsData(c.getString(TAG_TITLE), c.getString(TAG_DOP), c.getString(TAG_URL), imageId);
                publication_list_data[i] = pLD;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        else Log.e("ServiceHandler", "Couldn't get any data from the url");

        PublicationListDataAdapter adapter;
        adapter = new PublicationListDataAdapter(this, R.layout.publications_list_item, publication_list_data);
        ListView listView1 = (ListView)findViewById(R.id.publications_list_view);
        listView1.setAdapter(adapter);

        // Listview on item click listener
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String src = ((TextView) view.findViewById(R.id.publication_url)).getText().toString();
//                toast(src);
                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(), PublicationDetailActivity.class);
                PublicationDetailActivity.ARG_SRC = src;
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_mdg, menu);
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

    public void toast (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(PublicationsActivity.this, GetInvolvedActivity.class);
        startActivity(intent);
    }
}
