package com.miasmesh.meqdim.africaniffstracker;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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


public class ArticlesActivity extends ListActivity {
    private ProgressDialog pDialog;

    // URL to get dataSrcs JSON
//    private static String url = "http://api.androidhive.info/dataSrcs/";

    // JSON Node names
    private static final String TAG_IDENTIFIER = "articles";
    private static final String TAG_TITLE = "title";
    private static final String TAG_URL = "url";
    private static final String TAG_META = "meta";
    private static final String TAG_DATE = "date";

    // dataSrcs JSONArray
    JSONArray dataSrcs = null;
    JsonLoader jl = new JsonLoader();
//    AssetManager am;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> dataSrcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);

        toast("Tap on an article to read the full story.");

        dataSrcList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String src = ((TextView) view.findViewById(R.id.article_url)).getText().toString();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(src));
                startActivity(browserIntent);
//                toast(src);
//                String cost = ((TextView) view.findViewById(R.id.email))
//                        .getText().toString();
//                String description = ((TextView) view.findViewById(R.id.mobile))
//                        .getText().toString();

                // Starting single dataSrc activity
//                Intent in = new Intent(getApplicationContext(),
//                        CountryIffData.class);
//                CountryIffData.ARG_COUNTRY = country;
////                in.putExtra(_COUNTRY, country);
//                startActivity(in);

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
            pDialog = new ProgressDialog(ArticlesActivity.this);
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
            jl.ARG_FILENAME = "articles.json";

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

                        String at = c.getString(TAG_TITLE);
                        String au = c.getString(TAG_URL);
                        String ab = c.getString(TAG_META);
                        String ad = c.getString(TAG_DATE);

                        // tmp hashmap for single dataSrc
                        HashMap<String, String> dataSrc = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        dataSrc.put(TAG_TITLE, at);
                        dataSrc.put(TAG_URL, au);
                        dataSrc.put(TAG_META, ab);
                        dataSrc.put(TAG_DATE, ad);

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
                    ArticlesActivity.this, dataSrcList,
                    R.layout.articles_list_item, new String[]{TAG_TITLE, TAG_URL, TAG_DATE, TAG_META}, new int[]{R.id.article_title, R.id.article_url, R.id.article_date, R.id.article_brief});
            setListAdapter(adapter);
        }

    }

    public void toast (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(ArticlesActivity.this, GetInvolvedActivity.class);
        startActivity(intent);
    }
}
