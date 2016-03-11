package com.salve.activities.operations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.operations.listeners.handshake.AlertDialogOnShowListener;
import com.salve.fragments.HandShakeFragment;
import com.salve.activities.operations.listeners.handshake.GestureLearnedTextViewTextChangedListener;
import com.salve.activities.operations.listeners.handshake.GestureRadioButtonCheckedChangedListener;
import com.salve.listeners.fragment.OnHandShakeFragmentListener;
import com.salve.preferences.SalvePreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HandShakeOpsImpl implements View.OnClickListener {

    private static final String TAG = "HandShakeOpsImpl";

    private Fragment mFragment;

    private View mRootView;
    private RadioButton myOwnGesture;
    private RadioButton defaultGesture;
    private TextView gestureLearnedTextView;
    private OnHandShakeFragmentListener fragmentCallback;

    public HandShakeOpsImpl(Fragment activity, View rootView) {
        this.mFragment = activity;
        this.mRootView = rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.trashImageView:
                deleteGestureData();
                break;
            case R.id.handshake_buttonDefineOwnGesture:
                defineHandShake();
            default:
                break;
        }
    }

    public void defineHandShake() {

        final AlertDialog alertDialog = createAlertDialog(mFragment.getActivity());
        alertDialog.setOnShowListener(new AlertDialogOnShowListener(alertDialog, mFragment.getActivity()));
        alertDialog.show();
    }

    private AlertDialog createAlertDialog(Activity activity) {
        String alertDialogMessage =
                String.format("%s\n\n%s\n%s\n%s",
                        activity.getString(R.string.handshake_alertDialog_hint),
                        activity.getString(R.string.handshake_alertDialog_instruction1),
                        activity.getString(R.string.handshake_alertDialog_instruction2),
                        activity.getString(R.string.handshake_alertDialog_instruction3));

        return new AlertDialog.Builder(activity)
                .setTitle(R.string.handshake_alertDialog_title)
                .setMessage(alertDialogMessage)
                .setPositiveButton(R.string.handshake_alertDialog_start, null)
                .setNegativeButton(R.string.handshake_alertDialog_cancel, null)
                .setIcon(R.drawable.handshake)
                .create();
    }

    public void deleteGestureData() {
        gestureLearnedTextView.setText("");
        myOwnGesture.setChecked(false);
        myOwnGesture.setEnabled(false);
        defaultGesture.setChecked(true);
        fragmentCallback.deleteGestureData();
    }

    public void initializeUIFields() {

        ImageView trashImageView = (ImageView) mRootView.findViewById(R.id.trashImageView);
        myOwnGesture = (RadioButton) mRootView.findViewById(R.id.handshake_myOwnGesture);
        defaultGesture = (RadioButton) mRootView.findViewById(R.id.handshake_defaultGesture);
        gestureLearnedTextView = (TextView) mRootView.findViewById(R.id.handshake_gestureLearnedMessage);

        trashImageView.setVisibility(View.INVISIBLE);

        gestureLearnedTextView.addTextChangedListener(new GestureLearnedTextViewTextChangedListener(myOwnGesture, trashImageView));

        Context ctx = mFragment.getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        defaultGesture.setChecked(prefs.getBoolean(SalvePreferences.DEFAULT_GESTURE, true));
        myOwnGesture.setChecked(prefs.getBoolean(SalvePreferences.MY_OWN_GESTURE, false));
        gestureLearnedTextView.setText(prefs.getString(SalvePreferences.OWN_GESTURE_DEFINE_DATE, ""));
        myOwnGesture.setOnCheckedChangeListener(
                new GestureRadioButtonCheckedChangedListener((HandShakeFragment) mFragment, defaultGesture, SalvePreferences.MY_OWN_GESTURE));

        defaultGesture.setOnCheckedChangeListener(
                new GestureRadioButtonCheckedChangedListener((HandShakeFragment) mFragment, myOwnGesture, SalvePreferences.DEFAULT_GESTURE));

        Button buttonDefineOwnGesture = (Button) mRootView.findViewById(R.id.handshake_buttonDefineOwnGesture);
        buttonDefineOwnGesture.setOnClickListener(this);

        ImageView deleteOwnData = (ImageView) mRootView.findViewById(R.id.trashImageView);
        deleteOwnData.setOnClickListener(this);
    }

    public void onGestureLearned() {

        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
        String currentDateandTime = sdf.format(new Date());

        gestureLearnedTextView.setText(String.format("%s", "Defined on " + currentDateandTime));
        myOwnGesture.setEnabled(true);

        Context ctx = mFragment.getActivity().getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SalvePreferences.OWN_GESTURE_DEFINE_DATE, String.format("%s", "Defined on " + currentDateandTime));
        editor.apply();
    }

    public void restartClassification(String activeTrainingSet) {
        fragmentCallback.restartClassification(activeTrainingSet);
    }

    public void setFragmentCallback(OnHandShakeFragmentListener fragmentCallback) {
        this.fragmentCallback = fragmentCallback;
    }
}
