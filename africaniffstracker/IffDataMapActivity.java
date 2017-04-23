package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class IffDataMapActivity extends Activity implements View.OnTouchListener {

    private static int tolerance = 25;
    // JSON Node names
    private static final String TAG_IDENTIFIER = "countries";
    private static final String TAG_MIN_COLOR = "min";
    private static final String TAG_MAX_COLOR = "max";
    private static final String TAG_COUNTRY = "country";

    JsonLoader jl = new JsonLoader();
    AssetManager am;
    /**
     * Create the view for the activity.
     *
     */

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iff_data_map);

        ImageView iv = (ImageView) findViewById (R.id.african_countries);
//        ImageView iv = (ImageView) findViewById (R.id.image_areas);
        if (iv != null) {
            iv.setOnTouchListener (this);
        }

        toast("Tap on a country to see detailed data.");
    }

    /**
     * Respond to the user touching the screen.
     * Change images to make things appear and disappear from the screen.
     *
     */

    public boolean onTouch (View v, MotionEvent ev)
    {
        boolean handledHere = false;

        final int action = ev.getAction();

        final int evX = (int) ev.getX();
        final int evY = (int) ev.getY();
        int nextImage = -1;			// resource id of the next image to display

        // If we cannot find the imageView, return.
        ImageView imageView = (ImageView) v.findViewById (R.id.african_countries);
//        ImageView imageView = (ImageView) v.findViewById (R.id.image_areas);
        if (imageView == null) return false;

        // When the action is Down, see if we should show the "pressed" image for the default image.
        // We do this when the default image is showing. That condition is detectable by looking at the
        // tag of the view. If it is null or contains the resource number of the default image, display the pressed image.
        Integer tagNum = (Integer) imageView.getTag ();
        int currentResource = (tagNum == null) ? R.drawable.africa_mask : tagNum.intValue ();
//        int currentResource = (tagNum == null) ? R.drawable.africa_mask : tagNum.intValue ();

        // Now that we know the current resource being displayed we can handle the DOWN and UP events.

        switch (action) {
            case MotionEvent.ACTION_DOWN :
                if (currentResource == R.drawable.africa_mask) {
                    handledHere = true;
                } else handledHere = true;
                break;

            case MotionEvent.ACTION_UP :
                // On the UP, we do the click action.
//                int touchColor = getHotspotColor (R.id.image_areas, evX, evY);
                int touchColor = getHotspotColor (R.id.image_areas, evX, evY);

//                toast(String.valueOf(touchColor));

                String country = getCountryByColor((Math.abs(touchColor)));
                if(!country.equalsIgnoreCase("0")) {
                    Intent intent = new Intent(IffDataMapActivity.this, CountryIffData.class);
                    CountryIffData.ARG_COUNTRY = String.valueOf(country);
                    startActivity(intent);
                }
                handledHere = true;
                break;

            default:
                handledHere = false;
        } // end switch

        if (handledHere) {

            if (nextImage > 0) {
                imageView.setImageResource (nextImage);
                imageView.setTag (nextImage);
            }
        }
        return handledHere;
    }

    /**
     * Resume the activity.
     */

    @Override protected void onResume() {
        super.onResume();

//        View v  = findViewById (R.id.wglxy_bar);
//        if (v != null) {
//            Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//            //anim1.setAnimationListener (new StartActivityAfterAnimation (i));
//            v.startAnimation (anim1);
//        }
    }

    /**
     * Handle a click on the Wglxy views at the bottom.
     *
     */

    public void onClickWglxy (View v) {
        Intent viewIntent = new Intent ("android.intent.action.VIEW",
                Uri.parse("http://double-star.appspot.com/blahti/ds-download.html"));
        startActivity(viewIntent);

    }


/**
 */
// More methods

    /**
     * Get the color from the hotspot image at point x-y.
     *
     */

    public int getHotspotColor (int hotspotId, int x, int y) {
        ImageView img = (ImageView) findViewById (hotspotId);
        if (img == null) {
            Log.d("ImageAreasActivity", "Hot spot image not found");
            return 0;
        } else {
            img.setDrawingCacheEnabled(true);
            Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache());
            if (hotspots == null) {
                Log.d ("ImageAreasActivity", "Hot spot bitmap was not created");
                return 0;
            } else {
                img.setDrawingCacheEnabled(false);
                return hotspots.getPixel(x, y);
            }
        }
    }



    public String getCountryByColor(int color){
        try {
            am = getAssets();
            jl = new JsonLoader();
            jl.ARG_FILENAME = "countries.json";
            String jsonStr = jl.loadJSONFromAsset(am);
//            Log.d("Response: ", "> " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray countries_data = jsonObj.getJSONArray(TAG_IDENTIFIER);
                    int numY = countries_data.length();
                    for(int y = 0; y < numY; y++){
                        JSONObject c = countries_data.getJSONObject(y);
                        if(color >= Integer.parseInt(c.getString(TAG_MIN_COLOR)) && color <= Integer.parseInt(c.getString(TAG_MAX_COLOR)))
                            return c.getString(TAG_COUNTRY);
                        else
                            continue;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

        } catch (Exception e){

        }
        return "0";
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(IffDataMapActivity.this, GetInvolvedActivity.class);
        startActivity(intent);
    }

    public void CountryList(View v){
        Intent intent;
        intent = new Intent(IffDataMapActivity.this, CountryListActivity.class);
        startActivity(intent);
//        toast("Here");
    }

    /**
     * Show a string on the screen via Toast.
     *
     * @param msg String
     * @return void
     */
    public void toast (String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show ();
    }


} // end class
