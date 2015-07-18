package com.salve.activities;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.operations.LoadingScreenOpsImpl;
import com.salve.agrf.gestures.GestureConnectionService;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.contacts.AccountUtils;


public class TestingActivity extends AppCompatActivity {

    private static final String TAG = "TestingActivity";

    private TextView trainingTV;
    private GestureConnectionService gestureConnectionService;
    private IBinder gestureListenerStub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing);

        trainingTV = (TextView) findViewById(R.id.button99);

        gestureConnectionService = LoadingScreenOpsImpl.gestureConnectionService;
        gestureListenerStub = LoadingScreenOpsImpl.gestureListenerStub;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void connect(View view) {
        final String activeTrainingSet = "handshake";
        try {
            gestureConnectionService
                    .getRecognitionService()
                    .startClassificationMode(activeTrainingSet);
            gestureConnectionService
                    .getRecognitionService()
                    .registerListener(IGestureRecognitionListener.Stub.asInterface(gestureListenerStub));
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }

    }

    public void getContacts(View view) {
        AccountUtils.UserProfile userProfile = AccountUtils.getUserProfile(this);
    }

    public void training(View v) {

        Log.e(TAG, "Train button pushed");
        IGestureRecognitionService recognitionService = gestureConnectionService.getRecognitionService();
        if (recognitionService != null) {
            try {
                if (!recognitionService.isLearning()) {
                    trainingTV.setText("Stop Training");
                    recognitionService.startLearnMode("handshake", "HANDSHAKE");
                } else {
                    trainingTV.setText("Start Training");
                    recognitionService.stopLearnMode();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "recognitionService is null");
        }
    }

}
