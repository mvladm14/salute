package com.salve.contacts;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactInformation implements Serializable, Parcelable {

    private static final long serialVersionUID = 1L;

    private static final String EMAIL_ = "EMAIL_";
    private static final String PHONE_ = "PHONE_";

    private String name;
    private String address;
    private Map<ContactFieldType, ArrayList<String>> emails;
    private Map<ContactFieldType, ArrayList<String>> phoneNumbers;

    public ContactInformation() {
        emails = new HashMap<>();
        phoneNumbers = new HashMap<>();
    }

    private ContactInformation(Parcel parcel) {

        emails = new HashMap<>();
        phoneNumbers = new HashMap<>();

        Bundle bundle = parcel.readBundle();
        for (String key : bundle.keySet()) {
            if (key.contains(EMAIL_)) {
                ContactEmailType type = ContactEmailType.valueOf(key.replace(EMAIL_, ""));
                emails.put(type, bundle.getStringArrayList(EMAIL_ + type.toString()));
            }
            if (key.contains(PHONE_)) {
                ContactPhoneType type = ContactPhoneType.valueOf(key.replace(PHONE_, ""));
                phoneNumbers.put(type, bundle.getStringArrayList(PHONE_ + type.toString()));
            }
        }
        name = parcel.readString();
        address = parcel.readString();

    }

    public static ContactInformation getMyContact(Context context) {
        return ContactInformationUtility.getUserProfile(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<ContactFieldType, ArrayList<String>> getEmails() {
        return emails;
    }

    public Map<ContactFieldType, ArrayList<String>> getPhoneNumbers() {
        return phoneNumbers;
    }

    @Override
    public String toString() {
        return "ContactInformation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", emails=" + emails +
                ", phoneNumbers=" + phoneNumbers +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        for (ContactFieldType key : emails.keySet()) {
            bundle.putStringArrayList(EMAIL_ + key.toString(), emails.get(key));
        }
        for (ContactFieldType key : phoneNumbers.keySet()) {
            bundle.putStringArrayList(PHONE_ + key.toString(), phoneNumbers.get(key));
        }

        parcel.writeBundle(bundle);
        parcel.writeString(name);
        parcel.writeString(address);
    }

    public static final Parcelable.Creator<ContactInformation> CREATOR = new Creator<ContactInformation>() {

        @Override
        public ContactInformation createFromParcel(Parcel parcel) {
            return new ContactInformation(parcel);
        }

        @Override
        public ContactInformation[] newArray(int i) {
            return new ContactInformation[0];
        }
    };
}
