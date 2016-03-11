package com.salve.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.salve.R;
import com.salve.activities.adapters.InteractiveArrayAdapter;
import com.salve.activities.adapters.SocialExpandableListAdapter;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.operations.PreferencesOpsImpl;

public class PreferencesFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        initializeFields();

        return rootView;
    }

    private void initializeFields() {
        initializeUIFields();
    }

    private void initializeUIFields() {

        ExpandableListView expandablelistView = (ExpandableListView) rootView.findViewById(R.id.preferences_expandableListView);
        SocialExpandableListAdapter socialExpandableAdapter = new SocialExpandableListAdapter(getActivity(), PreferencesOpsImpl.getGroups());
        expandablelistView.setAdapter(socialExpandableAdapter);

        ListView listView = (ListView) rootView.findViewById(R.id.preferences_listView);
        ArrayAdapter<PreferencesModel> adapter = new InteractiveArrayAdapter(getActivity(), PreferencesOpsImpl.getPreferencesModels());
        listView.setAdapter(adapter);
    }
}
