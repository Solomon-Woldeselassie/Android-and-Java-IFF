package com.miasmesh.meqdim.africaniffstracker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class PublicationDetailActivity extends Activity {
    public static String ARG_SRC = "";

    private static final String TAG_IDENTIFIER = "documents";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL = "url";
    private static final String TAG_COVER = "cover";
    private static final String TAG_DOP = "dop";
    private static final String TAG_ABSTRACT = "abstract";

    JSONArray dataSrcs = null;
    JsonLoader jl = new JsonLoader();
    AssetManager am;
    public PublicationsData[] publication_list_data;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_detail);

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
                String current = c.getString(TAG_URL);
                if(ARG_SRC.compareToIgnoreCase(current) == 0){
                    ((TextView) findViewById(R.id.documentTitle)).setText(c.getString(TAG_TITLE));
                    ((TextView) findViewById(R.id.documentDop)).setText(c.getString(TAG_DOP));
                    ((TextView) findViewById(R.id.documentAbstract)).setText(c.getString(TAG_ABSTRACT));
                    ((TextView) findViewById(R.id.documentSrc)).setText(current);
                     int flagId = getResources().getIdentifier(c.getString(TAG_COVER).toLowerCase(Locale.getDefault()), "drawable", this.getPackageName());
                     if(flagId > 0)
                        (findViewById(R.id.documentCoverImg)).setBackground((Drawable) getResources().getDrawable(flagId));
                }
                else
                    continue;
            }

            //read pdf online
            Button documentRead = (Button) findViewById(R.id.documentRead);
            documentRead.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    String src = ((TextView) findViewById(R.id.documentSrc)).getText().toString();
                    Uri path = Uri.parse(src);
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(path, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try{
                        startActivity(pdfIntent);
                    } catch(ActivityNotFoundException e){
                        toast("No Application available to view pdf");
                    }
                }
            });
            //download pdf
            Button documentDown = (Button) findViewById(R.id.documentDownload);
            documentDown.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    String src = ((TextView) findViewById(R.id.documentSrc)).getText().toString();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(src));
                    startActivity(browserIntent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        else Log.e("ServiceHandler", "Couldn't get any data from the url");
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

    public void download(){}
    public void readOnline(){

    }
}
