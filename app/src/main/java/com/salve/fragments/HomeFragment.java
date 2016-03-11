package com.salve.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salve.R;
import com.salve.activities.operations.HomeFragmentOpsImpl;
import com.salve.band.tasks.BandConnectionAsyncResponseImpl;

import com.microsoft.band.ConnectionState;
import com.salve.contacts.ContactInformation;

import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private HomeFragmentOpsImpl screenOps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = this.getArguments();
        ConnectionState connectionState = null;
        if (bundle != null) {
            int connectionValue = bundle.getInt(BandConnectionAsyncResponseImpl.BAND_CONNECTION_STATUS);
            for (int i = 0; i < ConnectionState.values().length; i++) {
                if (connectionValue == ConnectionState.values()[i].ordinal()) {
                    connectionState = ConnectionState.values()[i];
                    Log.e(TAG, "Band ConnectionState is: " + connectionState + "");
                    break;
                }
            }
        }

        screenOps = new HomeFragmentOpsImpl(rootView, connectionState);

        initializeFields();

        return rootView;
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeUIFields() {
        screenOps.initializeUIFields();
    }

    private void initializeNonUIFields() {

    }

    public void updateContacts(List<ContactInformation> contacts) {
        screenOps.updateContacts(contacts);
    }
}
