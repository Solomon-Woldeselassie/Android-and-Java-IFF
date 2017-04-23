package com.miasmesh.meqdim.africaniffstracker;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
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


public class CountryListActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_IDENTIFIER = "countries";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_ID = "id";
    private static final String TAG_COLOR = "color";
    private static final String TAG_COLOR_MIN = "min";
    private static final String TAG_COLOR_MAX = "max";
    private static final String TAG_TRUE_NAME = "true_name";
    private static final String TAG_FLAG = "flag";

    // contacts JSONArray
    JSONArray contacts = null;
    JsonLoader jl = new JsonLoader();
//    AssetManager am;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);

        contactList = new ArrayList<HashMap<String, String>>();

        ListView lv = getListView();

        // Listview on item click listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String country = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
//                String cost = ((TextView) view.findViewById(R.id.email))
//                        .getText().toString();
//                String description = ((TextView) view.findViewById(R.id.mobile))
//                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        CountryIffData.class);
                CountryIffData.ARG_COUNTRY = country;
//                in.putExtra(_COUNTRY, country);
                startActivity(in);

            }
        });

        // Calling async task to get json
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CountryListActivity.this);
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
            jl.ARG_FILENAME = "countries.json";

            String jsonStr = jl.loadJSONFromAsset(getAssets());

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray(TAG_IDENTIFIER);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String country = c.getString(TAG_COUNTRY);
                        String minColor = c.getString(TAG_COLOR_MIN);
                        String maxColor = c.getString(TAG_COLOR_MAX);

                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put(TAG_ID, id);
                        contact.put(TAG_COUNTRY, country);
                        contact.put(TAG_TRUE_NAME, country.replaceAll("_", " "));
                        contact.put(TAG_COLOR_MIN, minColor);
                        contact.put(TAG_COLOR_MAX, maxColor);
                        contact.put(TAG_FLAG, maxColor);

                        // adding contact to contact list
                        contactList.add(contact);
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
                    CountryListActivity.this, contactList,
                    R.layout.country_list_item, new String[]{TAG_TRUE_NAME, TAG_COUNTRY}, new int[]{R.id.true_name, R.id.name});
//                    R.layout.country_list_item, new String[]{TAG_TRUE_NAME, TAG_COUNTRY, TAG_FLAG}, new int[]{R.id.true_name, R.id.name, R.id.country_list_flag});
            setListAdapter(adapter);
        }

    }

    public void toast (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }
}