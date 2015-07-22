package com.salve.contacts;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rroxa_000 on 7/18/2015.
 */
public class ContactInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String address;
    private Map<ContactFieldType,List<String>> emails;
    private Map<ContactFieldType,List<String>> phoneNumbers;

    public ContactInformation() {
        emails = new HashMap<>();
        phoneNumbers = new HashMap<>();
    }

    public static ContactInformation getMyContact(Context context){
        return ContactInformationUtility.getUserProfile(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<ContactFieldType, List<String>> getEmails() {
        return emails;
    }

    public Map<ContactFieldType, List<String>> getPhoneNumbers() {
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
}
