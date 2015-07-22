package com.salve.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rroxa_000 on 7/11/2015.
 */
public class ImportContact {
    String TAG = "IMPORT CONTACT";
    //used to determine which fields need to be added
    private boolean needsHomeEmailField;
    private boolean needsWorkEmailField;
    private boolean needsCustomEmailField;
    private boolean needsOtherEmailField;
    private boolean needsHomePhoneField;
    private boolean needsWorkPhoneField;
    private boolean needsCustomPhoneField;
    private boolean needsOtherPhoneField;
    private boolean needsMobilePhoneField;

    public void importNewContact(Context context, ContactInformation contact) {
        ContentResolver cr = context.getContentResolver();
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        if (contact.getName() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                    .build());
        }
        if (!contact.getPhoneNumbers().isEmpty()) {
            Iterator it = contact.getPhoneNumbers().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (pair.getKey() instanceof ContactPhoneType) {
                    ContactPhoneType type = (ContactPhoneType) pair.getKey();
                    List<String> phones = (List<String>) pair.getValue();
                    for (String phoneNo : phones) {
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNo)
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                                        CommonDataKindsTranslator.getCommonDataKinds(type))
                                .build());
                    }
                } else {
                    Log.e(TAG, "Error when casting from pair.getKey() to ContactPhoneType");
                }
            }
        }
        if (!contact.getEmails().isEmpty()) {
            Iterator it = contact.getEmails().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (pair.getKey() instanceof ContactEmailType) {
                    ContactEmailType type = (ContactEmailType) pair.getKey();
                    List<String> emails = (List<String>) pair.getValue();
                    for (String email : emails) {
                        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)

                                .withValue(ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Email.DATA, email)
                                .withValue(ContactsContract.CommonDataKinds.Email.TYPE,
                                        CommonDataKindsTranslator.getCommonDataKinds(type))
                                .build());
                    }
                } else {
                    Log.e(TAG, "Error when casting from pair.getKey() to ContactEmailType");
                }
            }
        }
        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Created a new contact:\n" + contact.toString());
    }

    public void updateContact(Context context, ContactInformation contact) {
        ContentResolver cr = context.getContentResolver();

        if (!contactExists(context, contact)) {
            Log.e(TAG, "Inserting new contact...");
            importNewContact(context, contact);
        } else {
            Log.e(TAG, "Updating existing contact");
            //Check what new information is received
            setTheNewFields(context, contact);
            boolean needsNewFields = needsHomeEmailField || needsWorkEmailField
                    || needsCustomEmailField || needsOtherEmailField
                    || needsHomePhoneField || needsWorkPhoneField
                    || needsCustomPhoneField || needsOtherPhoneField
                    || needsMobilePhoneField;

            if (!needsNewFields) {
                Log.e(TAG, "Updating without adding new fields...");
                updateContactInfo(cr, contact);
            } else {
                //needs new fields to be inserted
                Log.e(TAG, "Updating with addition of new fields...");
                insertNewFields(context, contact);
            }
        }
    }

    private void insertNewFields(Context context, ContactInformation contact) {
        String mRawContactId = getRawCursorId(context, contact.getName());
        if (mRawContactId == null) {
            Log.e(TAG, "e null");
        } else {
            Log.e(TAG, mRawContactId);
        }

        //insert email fields
        if (needsWorkEmailField) {
            Log.e(TAG, "needsWorkEmailField");
            insertEmailField(mRawContactId, context, contact, ContactEmailType.WORK);
        }
        if (needsOtherEmailField) {
            Log.e(TAG, "needsOtherEmailField");
            insertEmailField(mRawContactId, context, contact, ContactEmailType.OTHER);
        }
        if (needsHomeEmailField) {
            Log.e(TAG, "needsHomeEmailField");
            insertEmailField(mRawContactId, context, contact, ContactEmailType.HOME);
        }
        if (needsCustomEmailField) {
            Log.e(TAG, "needsCustomEmailField");
            insertEmailField(mRawContactId, context, contact, ContactEmailType.CUSTOM);
        }

        //insert phone fields
        if (needsMobilePhoneField) {
            Log.e(TAG, "needsMobilePhoneField");
            insertPhoneField(mRawContactId, context, contact, ContactPhoneType.MOBILE);
        }
        if (needsHomePhoneField) {
            Log.e(TAG, "needsHomePhoneField");
            insertPhoneField(mRawContactId, context, contact, ContactPhoneType.HOME);
        }
        if (needsWorkPhoneField) {
            Log.e(TAG, "needsWorkPhoneField");
            insertPhoneField(mRawContactId, context, contact, ContactPhoneType.WORK);
        }
        if (needsOtherPhoneField) {
            Log.e(TAG, "needsOtherPhoneField");
            insertPhoneField(mRawContactId, context, contact, ContactPhoneType.OTHER);
        }
        if (needsCustomPhoneField) {
            Log.e(TAG, "needsCustomPhoneField");
            insertPhoneField(mRawContactId, context, contact, ContactPhoneType.CUSTOM);
        }
        Log.e(TAG,"Done with adding fields");
    }

    private String getRawCursorId(Context context, String name) {
        Log.e(TAG, "getRawCursorId");
        String contactId = getContactID(context, name);
        Log.e(TAG, "CONTACT ID " + contactId);
        Cursor mRawContactCursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + " = ?",
                new String[]{contactId},
                null);
        Log.e(TAG, "Got RawContact Cursor");
        while (mRawContactCursor.moveToNext()) {
            String rawId = getCursorString(mRawContactCursor, ContactsContract.RawContacts._ID);
            Log.e(TAG, "RAW ID: " + rawId);
            return rawId;
        }
        return null;
    }

    private void insertEmailField(String mRawContactId, Context context, ContactInformation contact,
                                  ContactEmailType type) {
        List<String> emails = contact.getEmails().get(type);
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        for (String email : emails) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, mRawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
                    .withValue(Email.DATA, email)
                    .withValue(Email.TYPE, CommonDataKindsTranslator.getCommonDataKinds(type))
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Inserted a new email with type:\n" + CommonDataKindsTranslator.getCommonDataKinds(type));
    }

    private void insertPhoneField(String mRawContactId, Context context, ContactInformation contact,
                                  ContactPhoneType type) {
        List<String> phones = contact.getPhoneNumbers().get(type);
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        for (String phoneNo : phones) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValue(ContactsContract.Data.RAW_CONTACT_ID, mRawContactId)
                    .withValue(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.Data.DATA1, phoneNo)
                    .withValue(Phone.TYPE, CommonDataKindsTranslator.getCommonDataKinds(type))
                    .build());
        }
        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Inserted a new phone with type:\n" + CommonDataKindsTranslator.getCommonDataKinds(type));
    }


    private void updateContactInfo(ContentResolver cr, ContactInformation contact) {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();
        if (!contact.getPhoneNumbers().isEmpty()) {
            Log.e(TAG, "Has some phone numbers to update");
            Iterator it = contact.getPhoneNumbers().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ContactPhoneType type = (ContactPhoneType) pair.getKey();
                List<String> phones = (List<String>) pair.getValue();
                for (String phoneNo : phones) {
                    String where = getWhere(String.valueOf(Phone.TYPE));
                    String[] params = getParams(contact.getName(),
                            String.valueOf(CommonDataKindsTranslator.getCommonDataKinds(type)),
                            FieldName.PHONE);

                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(where, params)
                            .withValue(Phone.DATA, phoneNo)
                            .withValue(Phone.TYPE, CommonDataKindsTranslator.getCommonDataKinds(type))
                            .build());
                }
            }
        }
        if (!contact.getEmails().isEmpty()) {
            Log.e(TAG, "Has some emails to update");
            Iterator it = contact.getEmails().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ContactEmailType type = (ContactEmailType) pair.getKey();
                List<String> emails = (List<String>) pair.getValue();
                for (String email : emails) {
                    String where = getWhere(String.valueOf(Email.TYPE));
                    String[] params = getParams(contact.getName(),
                            String.valueOf(CommonDataKindsTranslator.getCommonDataKinds(type)),
                            FieldName.Email);
                    ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                            .withSelection(where, params)
                            .withValue(Email.DATA, email)
                            .withValue(Email.TYPE, CommonDataKindsTranslator.getCommonDataKinds(type))
                            .build());
                }
            }
        }
        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private String getWhere(String type) {
        return ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                type + " = ?";
    }

    private String[] getParams(String name, String type, FieldName fieldName) {
        if (FieldName.Email.equals(fieldName)) {
            return new String[]{name,
                    Email.CONTENT_ITEM_TYPE,
                    type};
        } else if (FieldName.PHONE.equals(fieldName)) {
            return new String[]{name,
                    Phone.CONTENT_ITEM_TYPE,
                    type};
        }
        return new String[0];
    }


    private void setTheNewFields(Context context, ContactInformation receivedContact) {
        //check for emails
        setFieldsForEmails(context, receivedContact);
        //check for phone numbers
        setFieldsForPhoneNumbers(context, receivedContact);
    }

    private void setFieldsForPhoneNumbers(Context context, ContactInformation receivedContact) {
        if (!receivedContact.getPhoneNumbers().isEmpty()) {
            Iterator it = receivedContact.getPhoneNumbers().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ContactPhoneType type = (ContactPhoneType) pair.getKey();
                if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Phone.TYPE_HOME) {
                    if (!containsPhone(context, receivedContact, String.valueOf(Phone.TYPE_HOME))) {
                        needsHomePhoneField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Phone.TYPE_WORK) {
                    if (!containsPhone(context, receivedContact, String.valueOf(Phone.TYPE_WORK))) {
                        needsWorkPhoneField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Phone.TYPE_OTHER) {
                    if (!containsPhone(context, receivedContact, String.valueOf(Phone.TYPE_OTHER))) {
                        needsOtherPhoneField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Phone.TYPE_CUSTOM) {
                    if (!containsPhone(context, receivedContact, String.valueOf(Phone.TYPE_CUSTOM))) {
                        needsCustomPhoneField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Phone.TYPE_MOBILE) {
                    if (!containsPhone(context, receivedContact, String.valueOf(Phone.TYPE_MOBILE))) {
                        needsMobilePhoneField = true;
                    }
                }
            }
        }
    }

    private void setFieldsForEmails(Context context, ContactInformation receivedContact) {
        if (!receivedContact.getEmails().isEmpty()) {
            Iterator it = receivedContact.getEmails().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ContactEmailType type = (ContactEmailType) pair.getKey();
                if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Email.TYPE_HOME) {
                    if (!containsEmail(context, receivedContact, String.valueOf(Email.TYPE_HOME))) {
                        needsHomeEmailField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Email.TYPE_WORK) {
                    if (!containsEmail(context, receivedContact, String.valueOf(Email.TYPE_WORK))) {
                        needsWorkEmailField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Email.TYPE_OTHER) {
                    if (!containsEmail(context, receivedContact, String.valueOf(Email.TYPE_OTHER))) {
                        needsOtherEmailField = true;
                    }
                } else if (CommonDataKindsTranslator.getCommonDataKinds(type)
                        == Email.TYPE_CUSTOM) {
                    if (!containsEmail(context, receivedContact, String.valueOf(Email.TYPE_CUSTOM))) {
                        needsCustomEmailField = true;
                    }
                }
            }
        }
    }


    private boolean contactExists(Context context, ContactInformation contact) {
        String name = contact.getName();
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


    private boolean containsEmail(Context context, ContactInformation contact, String type) {
        String name = contact.getName();

        String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.Email.TYPE) + " = ?";

        String[] params = new String[]{name,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                type};
        Cursor cur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);
        boolean containsEmail = cur.moveToNext();
        cur.close();
        return containsEmail;
    }

    private boolean containsPhone(Context context, ContactInformation contact, String type) {
        String name = contact.getName();

        String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                ContactsContract.Data.MIMETYPE + " = ? AND " +
                String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ?";

        String[] params = new String[]{name,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                type};
        Cursor cur = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, params, null);
        boolean containsPhone = cur.moveToNext();
        cur.close();
        return containsPhone;

    }

    private String getContactID(Context context, String name) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String existName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (existName.contains(name)) {
                    String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    cur.close();
                    return contactId;
                }
            }
        }
        cur.close();
        return null;
    }

    private static String getCursorString(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index != -1) return cursor.getString(index);
        return null;
    }

}
