package com.salve.contacts;

import java.io.Serializable;

/**
 * Created by rroxa_000 on 7/18/2015.
 */
public enum ContactPhoneType implements Serializable, ContactFieldType {
    HOME,
    WORK,
    CUSTOM,
    MOBILE,
    OTHER
}
