package com.salve.activities.operations.listeners.handshake;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.salve.fragments.HandShakeFragment;

public class GestureRadioButtonCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {

    private RadioButton otherGesture;
    private String salvePreferencesHandshake;
    private HandShakeFragment mHandShakeFragment;

    public GestureRadioButtonCheckedChangedListener(HandShakeFragment fragment, RadioButton otherGesture, String salvePreferencesHandshake) {
        this.mHandShakeFragment = fragment;
        this.otherGesture = otherGesture;
        this.salvePreferencesHandshake = salvePreferencesHandshake;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            otherGesture.setChecked(false);
            mHandShakeFragment.restartClassification(salvePreferencesHandshake);
        }
        Context ctx = mHandShakeFragment.getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(salvePreferencesHandshake, b);
        editor.apply();
    }
}
