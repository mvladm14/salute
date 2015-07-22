package com.salve.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.salve.R;
import com.salve.activities.adapters.InteractiveArrayAdapter;
import com.salve.activities.adapters.SocialExpandableListAdapter;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.navigation.NavigationManager;
import com.salve.activities.operations.PreferencesOpsImpl;

public class Preferences extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        initializeFields();
    }

    private void initializeFields() {
        initializeUIFields();
    }

    private void initializeUIFields() {

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
}
