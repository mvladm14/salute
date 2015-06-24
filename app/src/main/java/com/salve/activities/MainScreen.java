package com.salve.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.salve.R;
import com.salve.activityOperations.IMainScreenOps;
import com.salve.activityOperations.MainScreenOpsImpl;


public class MainScreen extends Activity {

    private IMainScreenOps mainScreenOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeFields();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeUIFields() {
        View view = findViewById(R.id.circle);
        GradientDrawable bgDrawable = (GradientDrawable) view.getBackground();
        bgDrawable.setColor(Color.BLACK);
    }

    private void initializeNonUIFields() {
        mainScreenOps = new MainScreenOpsImpl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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

    public void goToTestingScreen(View view) {
        mainScreenOps.goToActivity(this, TestingActivity.class);
    }
}
