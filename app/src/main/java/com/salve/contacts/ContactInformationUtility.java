package com.salve.contacts;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by rroxa_000 on 7/18/2015.
 */
public class ContactInformationUtility implements Serializable {

    private static final String TAG = "ContactInfoUtility";

    public static ContactInformation getUserProfile(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                ? getUserProfileOnIcsDevice(context) :
                getUserProfileOnGingerbreadDevice(context);
    }

    private static ContactInformation getUserProfileOnGingerbreadDevice(Context context) {
        // Other that using Patterns (API level 8) this works on devices down to API level 5
        final Matcher valid_email_address = Patterns.EMAIL_ADDRESS.matcher("");
        final Account[] accounts = AccountManager.get(context).getAccounts();
        final ContactInformation user_profile = new ContactInformation();

        for (Account account : accounts) {
            if (valid_email_address.reset(account.name).matches())
                user_profile.setName(account.name);
        }
        // Gets the phone number of the device is the device has one
        if (context.getPackageManager().hasSystemFeature(Context.TELEPHONY_SERVICE)) {
            final TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNo = telephony.getLine1Number();
            List<String> phoneNumbers = new ArrayList<>();
            phoneNumbers.add(phoneNo);
            user_profile.getPhoneNumbers().put(ContactPhoneType.HOME, phoneNumbers);
        }

        return user_profile;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static ContactInformation getUserProfileOnIcsDevice(Context context) {
        Log.e(TAG, "getUserProfileOnIcsDevice");
        final ContentResolver content = context.getContentResolver();

        final Cursor cursor = content.query(
                // Retrieves data rows for the device user's 'profile' contact
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Selects only email addresses or phone no or name or pic
                ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=? OR "
                        + ContactsContract.Contacts.Data.MIMETYPE + "=?",
                new String[]{
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                },

                // Show primary rows first. Note that there won't be a primary email address if the
                // user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC"
        );

        final ContactInformation user_profile = populateContact(cursor);

        cursor.close();

        return user_profile;
    }

    private static ContactInformation populateContact(Cursor cursor) {
        final ContactInformation user_profile = new ContactInformation();
        String mime_type;
        List<String> homeEmails = new ArrayList<>();
        List<String> workEmails = new ArrayList<>();
        List<String> customEmails = new ArrayList<>();
        List<String> otherEmails = new ArrayList<>();
        List<String> homeNo = new ArrayList<>();
        List<String> workNo = new ArrayList<>();
        List<String> mobileNo = new ArrayList<>();
        List<String> otherNo = new ArrayList<>();
        List<String> customNo = new ArrayList<>();

        while (cursor.moveToNext()) {
            mime_type = cursor.getString(ProfileQuery.MIME_TYPE);
            if (mime_type.equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {
                addEmails(cursor, user_profile, homeEmails, workEmails, customEmails, otherEmails);
            } else if (mime_type.equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                addName(cursor, user_profile);
            } else if (mime_type.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                addPhoneNumbers(cursor, user_profile, homeNo, workNo, mobileNo, otherNo, customNo);
            }
        }
        return user_profile;
    }

    private static void addPhoneNumbers(Cursor cursor, ContactInformation user_profile, List<String> homeNo, List<String> workNo, List<String> mobileNo, List<String> otherNo, List<String> customNo) {
        if (cursor.getInt(ProfileQuery.PHONE_TYPE) == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
            homeNo.add(cursor.getString(ProfileQuery.PHONE_NUMBER));
            user_profile.getPhoneNumbers().put(ContactPhoneType.HOME, homeNo);
        } else if (cursor.getInt(ProfileQuery.PHONE_TYPE) == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
            workNo.add(cursor.getString(ProfileQuery.PHONE_NUMBER));
            user_profile.getPhoneNumbers().put(ContactPhoneType.WORK, workNo);
        } else if (cursor.getInt(ProfileQuery.PHONE_TYPE) == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
            mobileNo.add(cursor.getString(ProfileQuery.PHONE_NUMBER));
            user_profile.getPhoneNumbers().put(ContactPhoneType.MOBILE, mobileNo);
        } else if (cursor.getInt(ProfileQuery.PHONE_TYPE) == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
            customNo.add(cursor.getString(ProfileQuery.PHONE_NUMBER));
            user_profile.getPhoneNumbers().put(ContactPhoneType.CUSTOM, customNo);
        } else if (cursor.getInt(ProfileQuery.PHONE_TYPE) == ContactsContract.CommonDataKinds.Phone.TYPE_OTHER) {
            otherNo.add(cursor.getString(ProfileQuery.PHONE_NUMBER));
            user_profile.getPhoneNumbers().put(ContactPhoneType.OTHER, otherNo);
        }
    }

    private static void addName(Cursor cursor, ContactInformation user_profile) {
        user_profile.setName(cursor.getString(ProfileQuery.GIVEN_NAME)
                + " "
                + cursor.getString(ProfileQuery.FAMILY_NAME));
    }

    private static void addEmails(Cursor cursor, ContactInformation user_profile, List<String> homeEmails, List<String> workEmails, List<String> customEmails, List<String> otherEmails) {
        if (cursor.getInt(ProfileQuery.EMAIL_TYPE) == ContactsContract.CommonDataKinds.Email.TYPE_HOME) {
            homeEmails.add(cursor.getString(ProfileQuery.EMAIL));
            user_profile.getEmails().put(ContactEmailType.HOME, homeEmails);
        } else if (cursor.getInt(ProfileQuery.EMAIL_TYPE) == ContactsContract.CommonDataKinds.Email.TYPE_WORK) {
            workEmails.add(cursor.getString(ProfileQuery.EMAIL));
            user_profile.getEmails().put(ContactEmailType.WORK, workEmails);
        } else if (cursor.getInt(ProfileQuery.EMAIL_TYPE) == ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM) {
            customEmails.add(cursor.getString(ProfileQuery.EMAIL));
            user_profile.getEmails().put(ContactEmailType.CUSTOM, customEmails);
        } else if (cursor.getInt(ProfileQuery.EMAIL_TYPE) == ContactsContract.CommonDataKinds.Email.TYPE_OTHER) {
            otherEmails.add(cursor.getString(ProfileQuery.EMAIL));
            user_profile.getEmails().put(ContactEmailType.OTHER, otherEmails);
        }
    }

    private interface ProfileQuery extends Serializable {
        /**
         * The set of columns to extract from the profile query results
         */
        long serialVersionUID = 1L;
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI,
                ContactsContract.Contacts.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE
        };


        // Column index for the email address in the profile query results
        int EMAIL = 0;
        //Column index for the primary email address indicator in the profile query results
        int IS_PRIMARY_EMAIL = 1;
        //Column index for the family name in the profile query results
        int FAMILY_NAME = 2;
        // Column index for the given name in the profile query results
        int GIVEN_NAME = 3;
        // Column index for the phone number in the profile query results
        int PHONE_NUMBER = 4;
        //Column index for the primary phone number in the profile query results
        int IS_PRIMARY_PHONE_NUMBER = 5;
        // Column index for the photo in the profile query results
        int PHOTO = 6;
        // Column index for the MIME type in the profile query results
        int MIME_TYPE = 7;
        // Column index for the EMAIL type in the profile query results
        int EMAIL_TYPE = 8;
        // Column index for the PHONE type in the profile query results
        int PHONE_TYPE = 9;
    }


}
