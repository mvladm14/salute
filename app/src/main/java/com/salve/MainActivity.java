package com.salve;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.salve.band.BandUtils;
import com.salve.band.BandVersionType;
import com.salve.contacts.AccountUtils;


public class MainActivity extends ActionBarActivity {

    private BandUtils bandUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void connect(View view) {

        bandUtils = new BandUtils(this);
        bandUtils.connect();

    }

    public void getVersion(View view) {
        bandUtils.retrieveVersion(BandVersionType.FIRMWARE);
    }

    public void getContacts(View view) {

        AccountUtils.UserProfile userProfile = AccountUtils.getUserProfile(this);

    }
}
