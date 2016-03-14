package com.salve.activities.operations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.microsoft.band.ConnectionState;
import com.salve.R;
import com.salve.activities.adapters.ContactsListViewAdapter;
import com.salve.contacts.ContactInformation;
import com.salve.contacts.ContactsFileManager;
import com.salve.contacts.ImportContact;
import com.salve.preferences.SalvePreferences;
import com.salve.ui_listeners.ContactsListViewChangeListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentOpsImpl implements View.OnClickListener {

    private static final String TAG = "HomeFragmentOpsImpl";

    private View rootView;

    private ConnectionState connectionState;

    private List<ContactInformation> mContacts;

    private ContactsListViewAdapter mContactsListAdapter;

    public HomeFragmentOpsImpl(View mainScreen, ConnectionState connectionState) {
        this.rootView = mainScreen;
        this.connectionState = connectionState;
        this.mContacts = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trash:
                removeContact(v);
                break;

            case R.id.add_contact:
                addContact(v);
                break;
            default:
                break;
        }
    }

    public void updateContacts(List<ContactInformation> contacts) {
        this.mContacts = contacts;
        mContactsListAdapter.setContacts(contacts);
        mContactsListAdapter.notifyDataSetChanged();
    }

    public void initializeUIFields() {

        TextView mainScreenStatusMessage = (TextView) rootView.findViewById(R.id.mainScreenStatusMessage);
        final ListView mContactsListView = (ListView) rootView.findViewById(R.id.contacts_list_view);

        if (connectionState == ConnectionState.CONNECTED) {
            mainScreenStatusMessage.setText(R.string.wearableIsConnected);
        } else {
            mainScreenStatusMessage.setText(R.string.wearableIsNotConnected);
            mainScreenStatusMessage.setTextColor(Color.RED);
        }

        mContactsListAdapter = new ContactsListViewAdapter(rootView.getContext(), mContacts);
        mContactsListAdapter.setMode(Attributes.Mode.Single);
        mContactsListView.setAdapter(mContactsListAdapter);
        mContactsListView.addOnLayoutChangeListener(new ContactsListViewChangeListener(mContactsListView, this));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        if (prefs.getBoolean(SalvePreferences.SHOW_DIALOG, true)) {
            launchDialog();
        } else {
            Log.e(TAG, "Not showing info dialog anymore");
        }
    }

    private void launchDialog() {
        Log.e(TAG, "Showing info dialog");
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

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

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(SalvePreferences.SHOW_DIALOG, false);
        editor.apply();
    }

    public void removeContact(View view) {
        Log.e(TAG, "removing new contact");
        int position = getContactPosition(view);
        mContacts.remove(position);
        mContactsListAdapter.notifyDataSetChanged();
        new ContactsFileManager(rootView.getContext()).updateContactsFile(mContacts);
    }

    public void addContact(View view) {
        Log.e(TAG, "Adding new contact");
        int position = getContactPosition(view);
        ContactInformation receivedContact = mContacts.remove(position);
        mContactsListAdapter.notifyDataSetChanged();
        new ContactsFileManager(rootView.getContext()).updateContactsFile(mContacts);

        //import to contact list
        ImportContact op = new ImportContact();
        op.updateContact(rootView.getContext(), receivedContact);
    }

    private int getContactPosition(View view) {
        ListView listView = (ListView) rootView.findViewById(R.id.contacts_list_view);
        return listView.getPositionForView(view);
    }

    public void setContactsTextViewVisible(boolean contactsTextViewVisible) {
        TextView contactsTV = (TextView) rootView.findViewById(R.id.contactsWillAppearHere);
        contactsTV.setVisibility(contactsTextViewVisible ? View.VISIBLE : View.INVISIBLE);
    }
}
