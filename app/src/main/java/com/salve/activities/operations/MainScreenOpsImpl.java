package com.salve.activities.operations;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.MainScreen;
import com.salve.activities.adapters.ContactsListViewAdapter;
import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.contacts.ContactInformation;

import java.util.ArrayList;
import java.util.List;

public class MainScreenOpsImpl implements IGestureConnectionServiceAsyncReply {

    private static final String TAG = "MainScreenOpsImpl";

    private GestureConnectionService gestureConnectionService;

    private MainScreen mainScreen;

    private ConnectionState connectionState;

    private List<ContactInformation> contacts;

    private ContactsListViewAdapter mContactsListAdapter;

    public MainScreenOpsImpl(MainScreen mainScreen, ConnectionState connectionState) {
        this.mainScreen = mainScreen;
        this.connectionState = connectionState;
        this.contacts = new ArrayList<>();
        gestureConnectionService = new GestureConnectionService(this);

        Intent bindIntent = new Intent(this.mainScreen, GestureRecognitionService.class);
        this.mainScreen.bindService(bindIntent, gestureConnectionService, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected() {
        Log.e(TAG, "Connected to the background service");
        try {
            contacts = gestureConnectionService
                    .getRecognitionService()
                    .getContacts();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mContactsListAdapter.setContacts(contacts);
        mContactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServiceDisconnected() {

    }

    public void initializeUIFields() {
        TextView mainScreenStatusMessage = (TextView) mainScreen.findViewById(R.id.mainScreenStatusMessage);
        TextView mainScreenInformationMessage = (TextView) mainScreen.findViewById(R.id.mainScreenInformationMessage);
        ListView mContactsListView = (ListView) mainScreen.findViewById(R.id.contacts_list_view);

        if (connectionState == ConnectionState.CONNECTED) {
            mainScreenStatusMessage.setText(mainScreen.getString(R.string.wearableIsConnected));
            mainScreenInformationMessage.setText(mainScreen.getString(R.string.mainScreenInfoMessageOnBandConnected));
        } else {
            mainScreenStatusMessage.setText(mainScreen.getString(R.string.wearableIsNotConnected));
            mainScreenStatusMessage.setTextColor(Color.RED);
            mainScreenInformationMessage.setText(mainScreen.getString(R.string.mainScreenInfoMessageOnBandNotConnected));
        }

        mContactsListAdapter = new ContactsListViewAdapter(mainScreen, contacts);
        mContactsListView.setAdapter(mContactsListAdapter);
        mContactsListAdapter.setMode(Attributes.Mode.Single);
    }
}
