package com.salve.activities.operations.listeners.handshake;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.salve.activities.HandShake;
import com.salve.preferences.SalvePreferences;

/**
 * Created by Vlad on 7/22/2015.
 */
public class GestureRadioButtonCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {

    private RadioButton otherGesture;
    private String salvePreferencesHandshake;
    private HandShake activity;

    public GestureRadioButtonCheckedChangedListener(HandShake activity, RadioButton otherGesture, String salvePreferencesHandshake) {
        this.activity = activity;
        this.otherGesture = otherGesture;
        this.salvePreferencesHandshake = salvePreferencesHandshake;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            otherGesture.setChecked(false);
            activity.restartClassification(salvePreferencesHandshake);
        }
        Context ctx = activity.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(salvePreferencesHandshake, b);
        editor.apply();
    }
}
