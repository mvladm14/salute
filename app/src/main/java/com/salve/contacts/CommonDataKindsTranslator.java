package com.salve.contacts;

import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by rroxa_000 on 7/18/2015.
 */
public final class CommonDataKindsTranslator {
    private static final int ERROR_CODE = -1;

    public static int getCommonDataKinds(ContactFieldType type) {
        if (type instanceof ContactEmailType) {
            return getCommonDataKindsForEmail((ContactEmailType) type);
        } else if (type instanceof ContactPhoneType) {
            return getCommonDataKindsForPhone((ContactPhoneType) type);
        }
        return ERROR_CODE;
    }

    private static int getCommonDataKindsForEmail(ContactEmailType type) {

        switch (type) {
            case HOME:
                return ContactsContract.CommonDataKinds.Email.TYPE_HOME;
            case WORK:
                return ContactsContract.CommonDataKinds.Email.TYPE_WORK;
            case CUSTOM:
                return ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM;
            case OTHER:
                return ContactsContract.CommonDataKinds.Email.TYPE_OTHER;
            default:
                return ERROR_CODE;
        }
    }

    private static int getCommonDataKindsForPhone(ContactPhoneType type) {
        switch (type) {
            case HOME:
                return ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
            case WORK:
                return ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
            case MOBILE:
                return ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
            case CUSTOM:
                return ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM;
            case OTHER:
                return ContactsContract.CommonDataKinds.Phone.TYPE_OTHER;
            default:
                return ERROR_CODE;
        }
    }


}
