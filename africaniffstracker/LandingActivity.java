package com.miasmesh.meqdim.africaniffstracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class LandingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_landing);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    public void IffData(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, IffDataMapActivity.class);
        startActivity(intent);
    }

    public void articles(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, ArticlesActivity.class);
        startActivity(intent);
    }

    public void publications(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, PublicationsActivity.class);
        startActivity(intent);
    }

    public void GetInvolved(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, GetInvolvedActivity.class);
        startActivity(intent);
    }

    public void dataCharts(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, DataChartsActivity.class);
        startActivity(intent);
    }

    public void about(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void dataSources(View v){
        Intent intent;
        intent = new Intent(LandingActivity.this, DataSources.class);
        startActivity(intent);
    }

    public void stopTheBleeding(View v){
//        Intent intent;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://stopthebleedingafrica.org/"));
        startActivity(browserIntent);
    }
}
