package com.salve.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import java.util.ArrayList;

/**
 * Created by rroxa_000 on 7/11/2015.
 */
public class ImportContact {
    String TAG = "IMPORT CONTACT";

    public void importUsingIntent(Context context, AccountUtils.UserProfile contact) {

        ContentResolver cr = context.getContentResolver();

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (contact.possibleNames() != null && !contact.possibleNames().isEmpty()) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.possibleNames().get(0))
                    .build());
        }

        if (contact.possiblePhoneNumbers() != null && !contact.possiblePhoneNumbers().isEmpty()) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.possiblePhoneNumbers().get(0))
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        if (contact.possibleEmails() != null && !contact.possibleEmails().isEmpty()) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.possibleEmails().get(0))
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "Created a new contact:\n" + contact.toString());
    }

    public void updateContact(Context context, AccountUtils.UserProfile contact) {

        ContentResolver cr = context.getContentResolver();

        String name = contact.possibleNames().get(0);
        String email = contact.possibleEmails().get(0);
        String phone = contact.possiblePhoneNumbers().get(0);

        String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE) + " = ?";

        String[] params = new String[]{name,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE_WORK)};

        String wherePhone = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ?";

        String[] paramsPhone = new String[]{name,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)};
        Cursor phoneCur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);

        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        if (!contactExists(context, contact)) {

            importUsingIntent(context, contact);
        } else {
            if(containsEmail(context)){
                Log.e(TAG,"HAS EMAIL FIELD");
            }
            else{
                Log.e(TAG, "DOES not have email field");
            }
            Log.e(TAG, "UPDATE");
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(where, params)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(wherePhone, paramsPhone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.DATA, phone)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private boolean contactExists(Context context, AccountUtils.UserProfile contact) {

        String name = contact.possibleNames().get(0);

        ContentResolver cr = context.getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String existName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (existName.contains(name)) {
                    Log.e(TAG, "The contact name: " + name + " already exists");
                    cur.close();
                    return true;
                }
            }
        }
        cur.close();
        return false;
    }

    private String mRawContactId;
    private String mDataId;

    private boolean containsEmail(Context context) {
        boolean returnValue = false;
        Cursor mContactCursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        try {
            if (mContactCursor.moveToFirst()) {
                String mContactId = getCursorString(mContactCursor,
                        ContactsContract.Contacts._ID);
                Cursor mRawContactCursor = context.getContentResolver().query(
                        ContactsContract.RawContacts.CONTENT_URI,
                        null,
                        ContactsContract.Data.CONTACT_ID + " = ?",
                        new String[]{mContactId},
                        null);

                Log.v("RawContact", "Got RawContact Cursor");

                try {
                    ArrayList<String> mRawContactIds = new ArrayList<String>();
                    while (mRawContactCursor.moveToNext()) {
                        String rawId = getCursorString(mRawContactCursor, ContactsContract.RawContacts._ID);
                        Log.e("RawContact", "ID: " + rawId);
                        mRawContactIds.add(rawId);
                    }
                    for (String rawId : mRawContactIds) {
                        // Make sure the "last checked" RawContactId is set locally for use in insert & update.
                        mRawContactId = rawId;
                        Cursor mDataCursor = context.getContentResolver().query(
                                Data.CONTENT_URI,
                                null,
                                Data.RAW_CONTACT_ID + " = ? AND "
                                        + Data.MIMETYPE + " = ? AND "
                                        + Email.TYPE + " = ?",
                                new String[] { mRawContactId,
                                               Email.CONTENT_ITEM_TYPE,
                                        String.valueOf(Email.TYPE_HOME)},
                                null);


                        if (mDataCursor.getCount() > 0) {
                            mDataCursor.moveToFirst();
                            mDataId = getCursorString(mDataCursor, ContactsContract.Data._ID);
                            Log.e("Data", "Found data item with MIMETYPE and EMAIL.TYPE");
                            mDataCursor.close();
                            returnValue = true;
                            break;
                        } else {
                            Log.e("Data", "Data doesn't contain MIMETYPE and EMAIL.TYPE");
                            mDataCursor.close();
                        }
                        returnValue = false;
                    }
                } finally {
                    mRawContactCursor.close();
                }
            }
        } catch (Exception e) {
            Log.w("UpdateContact", e.getMessage());
            for (StackTraceElement ste : e.getStackTrace()) {
                Log.w("UpdateContact", "\t" + ste.toString());
            }
            throw new RuntimeException();
        } finally {
            mContactCursor.close();
        }
        return returnValue;
    }


    private static String getCursorString(Cursor cursor, String columnName) {
        Log.e("COLUMN Name",columnName);
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) return cursor.getString(index);
        return null;
    }

}
