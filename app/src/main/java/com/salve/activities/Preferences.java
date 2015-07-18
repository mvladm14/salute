package com.salve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.salve.R;
import com.salve.activities.adapters.InteractiveArrayAdapter;
import com.salve.activities.adapters.SocialExpandableListAdapter;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.operations.PreferencesOpsImpl;

public class Preferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);



        ExpandableListView expandablelistView = (ExpandableListView) findViewById(R.id.preferences_expandableListView);
        SocialExpandableListAdapter socialExpandableAdapter = new SocialExpandableListAdapter(this, PreferencesOpsImpl.getGroups());
        expandablelistView.setAdapter(socialExpandableAdapter);

        ListView listView = (ListView) findViewById(R.id.preferences_listView);
        ArrayAdapter<PreferencesModel> adapter = new InteractiveArrayAdapter(this, PreferencesOpsImpl.getPreferencesModels());
        listView.setAdapter(adapter);
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
        if (id == R.id.menu_preferences) {
            Intent intent = new Intent(this, Preferences.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
