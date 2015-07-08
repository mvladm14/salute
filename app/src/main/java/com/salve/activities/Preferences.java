package com.salve.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.salve.R;
import com.salve.activities.adapters.InteractiveArrayAdapter;
import com.salve.activities.adapters.SocialExpandableListAdapter;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.models.SocialGroup;

import java.util.ArrayList;
import java.util.List;

public class Preferences extends AppCompatActivity {

    private SparseArray<SocialGroup> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        createData();

        ExpandableListView expandablelistView = (ExpandableListView) findViewById(R.id.preferences_expandableListView);
        SocialExpandableListAdapter socialExpandableAdapter = new SocialExpandableListAdapter(this, groups);
        expandablelistView.setAdapter(socialExpandableAdapter);

        ListView listView = (ListView) findViewById(R.id.preferences_listView);
        ArrayAdapter<PreferencesModel> adapter = new InteractiveArrayAdapter(this, getModel());
        listView.setAdapter(adapter);
    }

    private void createData() {
        groups = new SparseArray<>();
        for (int j = 0; j < 1; j++) {

            List<PreferencesModel> socialNetworks = new ArrayList<>();
            socialNetworks.add(new PreferencesModel("Facebook", R.drawable.facebook));
            socialNetworks.add(new PreferencesModel("Twitter", R.drawable.twitter));
            socialNetworks.add(new PreferencesModel("Linkedin", R.drawable.linkedin));

            SocialGroup group = new SocialGroup(new PreferencesModel("Social", R.drawable.social), socialNetworks);
            groups.append(j, group);
        }
    }

    private List<PreferencesModel> getModel() {
        List<PreferencesModel> list = new ArrayList<>();
        list.add(get("Name & Surname", R.drawable.namesurname));
        list.add(get("Mobile Phone No.", R.drawable.mobilephoneno));
        list.add(get("Email", R.drawable.email));
        list.add(get("Address", R.drawable.address));

        selectMainItems(list);

        return list;
    }

    private void selectMainItems(List<PreferencesModel> list) {
        for (int i = 0; i < 4; i++) {
            list.get(i).setSelected(true);
        }
    }

    private PreferencesModel get(String s, int resId) {
        return new PreferencesModel(s, resId);
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
