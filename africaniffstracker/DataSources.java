package com.miasmesh.meqdim.africaniffstracker;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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


public class DataSources extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get dataSrcs JSON
//    private static String url = "http://api.androidhive.info/dataSrcs/";

    // JSON Node names
    private static final String TAG_IDENTIFIER = "data_sources";
    private static final String TAG_DATA_SOURCE = "source";
    private static final String TAG_URL = "url";

    // dataSrcs JSONArray
    JSONArray dataSrcs = null;
    JsonLoader jl = new JsonLoader();
//    AssetManager am;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataSrcList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_sources);

        dataSrcList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String src = ((TextView) view.findViewById(R.id.source_url)).getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(src));
                startActivity(browserIntent);
            }
        });

        // Calling async task to get json
        new GetDataSources().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDataSources extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(DataSources.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
//            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            jl = new JsonLoader();
            jl.ARG_FILENAME = "data_sources.json";

            String jsonStr = jl.loadJSONFromAsset(getAssets());

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    dataSrcs = jsonObj.getJSONArray(TAG_IDENTIFIER);

                    // looping through All dataSrcs
                    for (int i = 0; i < dataSrcs.length(); i++) {
                        JSONObject c = dataSrcs.getJSONObject(i);

                        String ds = c.getString(TAG_DATA_SOURCE);
                        String du = c.getString(TAG_URL);

//                        toast(ds.concat(" -- ").concat(du));
//                        toast(ds.concat(du));

                        // tmp hashmap for single dataSrc
                        HashMap<String, String> dataSrc = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        dataSrc.put(TAG_DATA_SOURCE, ds);
                        dataSrc.put(TAG_URL, du);

                        // adding dataSrc to dataSrc list
                        dataSrcList.add(dataSrc);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */

            ListAdapter adapter = new SimpleAdapter(
                    DataSources.this, dataSrcList,
                    R.layout.data_sources_list_item, new String[]{TAG_DATA_SOURCE, TAG_URL}, new int[]{R.id.data_source, R.id.source_url});
            setListAdapter(adapter);
        }

    }

    public void toast (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(DataSources.this, GetInvolvedActivity.class);
        startActivity(intent);
    }
}
