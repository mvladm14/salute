package com.salve.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.operations.HandShakeOpsImpl;
import com.salve.preferences.SalvePreferences;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HandShake extends AppCompatActivity {

    private static final String TAG = "HandShake";

    private HandShakeOpsImpl handShakeOps;

    private RadioButton myOwnGesture;
    private RadioButton defaultGesture;
    private TextView gestureLearnedTextView;
    private ImageView trashImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_shake);

        initializeFields();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeNonUIFields() {
        handShakeOps = new HandShakeOpsImpl(this);
    }

    private void initializeUIFields() {

        trashImageView = (ImageView) findViewById(R.id.trashImageView);
        myOwnGesture = (RadioButton) findViewById(R.id.handshake_myOwnGesture);
        defaultGesture = (RadioButton) findViewById(R.id.handshake_defaultGesture);
        gestureLearnedTextView = (TextView) findViewById(R.id.handshake_gestureLearnedMessage);

        trashImageView.setVisibility(View.INVISIBLE);

        gestureLearnedTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    trashImageView.setVisibility(View.INVISIBLE);
                    myOwnGesture.setEnabled(false);
                } else {
                    trashImageView.setVisibility(View.VISIBLE);
                    myOwnGesture.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        defaultGesture.setChecked(prefs.getBoolean(SalvePreferences.DEFAULT_GESTURE, true));
        myOwnGesture.setChecked(prefs.getBoolean(SalvePreferences.MY_OWN_GESTURE, false));
        gestureLearnedTextView.setText(prefs.getString(SalvePreferences.OWN_GESTURE_DEFINE_DATE, ""));

        myOwnGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    defaultGesture.setChecked(false);
                    handShakeOps.restartClassification(SalvePreferences.MY_OWN_GESTURE);
                }
                Context ctx = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SalvePreferences.MY_OWN_GESTURE, b);
                editor.apply();

            }
        });

        defaultGesture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    myOwnGesture.setChecked(false);
                    handShakeOps.restartClassification(SalvePreferences.DEFAULT_GESTURE);
                }

                Context ctx = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(SalvePreferences.DEFAULT_GESTURE, b);
                editor.apply();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        handShakeOps.unbindService();
        super.onDestroy();
    }

    public void defineHandshake(View view) {
        handShakeOps.defineHandShake();
    }

    public void onGestureLearned() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm");
        String currentDateandTime = sdf.format(new Date());

        gestureLearnedTextView.setText(String.format("%s", "Defined on " + currentDateandTime));
        myOwnGesture.setEnabled(true);

        Context ctx = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SalvePreferences.OWN_GESTURE_DEFINE_DATE, String.format("%s", "Defined on " + currentDateandTime));
        editor.apply();
    }

    public void deleteGestureData(View view) {
        gestureLearnedTextView.setText("");
        myOwnGesture.setChecked(false);
        myOwnGesture.setEnabled(false);
        defaultGesture.setChecked(true);
        handShakeOps.deleteGestureData();
    }
}
