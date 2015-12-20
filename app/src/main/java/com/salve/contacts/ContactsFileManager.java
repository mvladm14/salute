package com.salve.contacts;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactsFileManager {

    private static final String TAG = "ContactsFileManager";

    private static final String CONTACTS_FILE = "CONTACTS_FILE";

    private Context mContext;

    public ContactsFileManager(Context context) {
        this.mContext = context;
    }

    public void updateContactsFile(List<ContactInformation> contacts) {
        Log.e(TAG,"Update contacts file");
        try {
            FileOutputStream fos = new FileOutputStream(new File(mContext.getExternalFilesDir(null), CONTACTS_FILE + ".gst").toString());
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(contacts);

            o.close();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString() + "from writeContactToFile");
            e.printStackTrace();
        }
    }

    public void writeContactToFile(ContactInformation contactInformation) {

        Log.e(TAG, "writeContactToFile().");

        List<ContactInformation> contacts = readImportedContacts();
        contacts.add(contactInformation);

        try {
            FileOutputStream fos = new FileOutputStream(new File(mContext.getExternalFilesDir(null), CONTACTS_FILE + ".gst").toString());
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(contacts);

            o.close();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, e.toString() + "from writeContactToFile");
            e.printStackTrace();
        }
    }

    public List<ContactInformation> readImportedContacts() {
        Log.e(TAG, "readImportedContacts().");
        List<ContactInformation> contacts;
        try {
            FileInputStream input = new FileInputStream(new File(mContext.getExternalFilesDir(null), CONTACTS_FILE + ".gst"));
            ObjectInputStream o = new ObjectInputStream(input);
            contacts = (List<ContactInformation>) o.readObject();
            try {
                o.close();
                input.close();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString() + "from readImportedContacts");
            e.printStackTrace();
            contacts = new ArrayList<>();
        }

        String contactsInfo = "";
        for (ContactInformation information : contacts) {
            contactsInfo += information.toString() + "\n";
        }
        Log.e(TAG, contactsInfo);

        return contacts;
    }
}
