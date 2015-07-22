package com.salve.activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.navigation.NavigationManager;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.initializeFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_preferences:
                NavigationManager.goToActivity(this, Preferences.class);
                break;
            case R.id.menu_testing:
                NavigationManager.goToActivity(this, TestingActivity.class);
                break;
            case R.id.menu_about:
                NavigationManager.goToActivity(this, About.class);
                break;
            case R.id.menu_handshake:
                NavigationManager.goToActivity(this, HandShake.class);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeFields() {
        initializeUIFields();
    }

    private void initializeUIFields() {
        PackageInfo pInfo;
        String version = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView versionTV = (TextView) findViewById(R.id.txtVersionNoLabel);
        versionTV.setText(version);

        TextView ownerTV = (TextView) findViewById(R.id.txtOwnerValueLabel);
        ownerTV.setText(R.string.applicationOwner);
    }
}
