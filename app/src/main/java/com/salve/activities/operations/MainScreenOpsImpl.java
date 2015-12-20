package com.salve.activities.operations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.MainScreen;
import com.salve.activities.Preferences;
import com.salve.activities.adapters.ContactsListViewAdapter;
import com.salve.activities.asyncReplies.IGestureConnectionServiceAsyncReply;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.GestureRecognitionService;
import com.salve.contacts.ContactInformation;
import com.salve.contacts.ContactsFileManager;
import com.salve.contacts.ImportContact;
import com.salve.preferences.SalvePreferences;

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

        /*
        for (int i = 0; i < 5; i++) {
            ContactInformation cf = new ContactInformation();
            cf.setName("Lodewijck " + i);
            contacts.add(cf);
        }
        */

        mContactsListAdapter.setContacts(contacts);
        mContactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServiceDisconnected() {

    }

    public void initializeUIFields() {
        TextView mainScreenStatusMessage = (TextView) mainScreen.findViewById(R.id.mainScreenStatusMessage);
        ListView mContactsListView = (ListView) mainScreen.findViewById(R.id.contacts_list_view);

        if (connectionState == ConnectionState.CONNECTED) {
            mainScreenStatusMessage.setText(mainScreen.getString(R.string.wearableIsConnected));
        } else {
            mainScreenStatusMessage.setText(mainScreen.getString(R.string.wearableIsNotConnected));
            mainScreenStatusMessage.setTextColor(Color.RED);
        }

        mContactsListAdapter = new ContactsListViewAdapter(mainScreen, contacts);
        mContactsListView.setAdapter(mContactsListAdapter);
        mContactsListAdapter.setMode(Attributes.Mode.Single);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mainScreen);
        if (prefs.getBoolean(SalvePreferences.SHOW_DIALOG, true)) {
            launchDialog();
        } else {
            Log.e(TAG, "Not showing info dialog anymore");
        }
    }

    private void launchDialog() {
        Log.e(TAG, "Showing info dialog");
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mainScreen);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.mainScreenSwipeMessage)
                .setTitle("Info");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.mainScreen);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SalvePreferences.SHOW_DIALOG, false);
        editor.commit();
    }

    public void removeContact(View view) {
        int position = getContactPosition(view);
        contacts.remove(position);
        mContactsListAdapter.notifyDataSetChanged();
        new ContactsFileManager(mainScreen).updateContactsFile(contacts);
    }

    public void addContact(View view) {
        int position = getContactPosition(view);
        ContactInformation receivedContact = contacts.remove(position);
        mContactsListAdapter.notifyDataSetChanged();
        new ContactsFileManager(mainScreen).updateContactsFile(contacts);

        //import to contact list
        ImportContact op = new ImportContact();
        op.updateContact(mainScreen, receivedContact);
    }

    private int getContactPosition(View view) {
        ListView listView = (ListView) mainScreen.findViewById(R.id.contacts_list_view);
        return listView.getPositionForView(view);
    }

}
