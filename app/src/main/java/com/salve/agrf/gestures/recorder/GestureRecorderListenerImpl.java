package com.salve.agrf.gestures.recorder;

import android.app.Service;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.salve.agrf.gestures.Gesture;
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.agrf.gestures.IGestureRecognitionService;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.agrf.gestures.classifier.GestureClassifier;
import com.salve.agrf.gestures.classifier.featureExtraction.NormedGridExtractor;
import com.salve.bluetooth.BluetoothUtilityOps;
import com.salve.contacts.ContactInformation;
import com.salve.contacts.ContactsFileManager;
import com.salve.exceptions.bluetooth.BluetoothNotEnabledException;
import com.salve.preferences.SalvePreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestureRecorderListenerImpl implements GestureRecorderListener {

    private static final String TAG = "GestureRecListenerImpl";

    private boolean isLearning;
    private boolean isClassifying;

    private GestureRecorder recorder;
    private GestureClassifier classifier;

    private String activeTrainingSet;
    private String activeLearnLabel;

    private Set<IGestureRecognitionListener> listeners;

    private BluetoothUtilityOps bluetoothOps;

    private Service mService;

    public GestureRecorderListenerImpl(Service service, GestureRecorder recorder) {
        this.mService = service;
        this.listeners = new HashSet<>();
        this.recorder = recorder;
        classifier = new GestureClassifier(new NormedGridExtractor(), service);
        try {
            this.bluetoothOps = BluetoothUtilityOps.getInstance(service);
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder getBinder() {
        return gestureRecognitionServiceStub;
    }

    @Override
    public void onGestureRecorded(List<float[]> values) {
        Log.e(TAG, "onGestureRecorded");
        if (isLearning) {
            classifier.trainData(activeTrainingSet, new Gesture(values, activeLearnLabel));
            classifier.commitData();
            Log.e(TAG, "number of listeners = " + listeners.size());
            for (IGestureRecognitionListener listener : listeners) {
                try {
                    listener.onGestureLearned(activeLearnLabel);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "Trained");
        } else if (isClassifying) {
            recorder.pause(true);
            Log.e(TAG, "Classifying with " + activeTrainingSet);
            Distribution distribution = classifier.classifySignal(activeTrainingSet, new Gesture(values, null));
            recorder.pause(false);
            if (distribution != null && distribution.size() > 0) {
                Log.e(TAG, String.format("%s: %f", distribution.getBestMatch(), distribution.getBestDistance()));
                Log.e(TAG, "GESTURE was recognized");

                if (distribution.getBestDistance() < SalvePreferences.GESTURE_IDENTIFICATION_THRESHOLD) {

                    //TODO enable this to send contact (via bluetooth) upon handshake
                    bluetoothOps.sendContactViaBluetooth();

                    for (IGestureRecognitionListener listener : listeners) {
                        try {
                            listener.onGestureRecognized(distribution);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    IBinder gestureRecognitionServiceStub = new IGestureRecognitionService.Stub() {

        private final String TAG = "gestureRecogSvcStub";

        @Override
        public void deleteTrainingSet(String trainingSetName) throws RemoteException {
            if (classifier.deleteTrainingSet(trainingSetName)) {
                for (IGestureRecognitionListener listener : listeners) {
                    try {
                        listener.onTrainingSetDeleted(trainingSetName);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void onPushToGesture(boolean pushed) throws RemoteException {
            recorder.onPushToGesture(pushed);
        }

        @Override
        public void registerListener(IGestureRecognitionListener listener) throws RemoteException {
            Log.e(TAG, "registerListener()");
            if (listener != null) {
                listeners.add(listener);
            }
        }

        @Override
        public void startClassificationMode(String trainingSetName) throws RemoteException {
            startClassification(trainingSetName);
        }

        @Override
        public void startLearnMode(String trainingSetName, String gestureName) throws RemoteException {
            Log.e(TAG, "Starting to learn");
            activeTrainingSet = trainingSetName;
            activeLearnLabel = gestureName;
            isLearning = true;

            //recorder.setRecordMode(GestureRecorder.RecordMode.PUSH_TO_GESTURE);
        }

        @Override
        public void stopLearnMode() throws RemoteException {
            Log.e(TAG, "Stopped learning");
            isLearning = false;

            //recorder.setRecordMode(GestureRecorder.RecordMode.MOTION_DETECTION);
        }

        @Override
        public void unregisterListener(IGestureRecognitionListener listener) throws RemoteException {
            Log.e(TAG, "unregisterListener");
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                stopClassificationMode();
            }
        }

        @Override
        public List<String> getGestureList(String trainingSet) throws RemoteException {
            return classifier.getLabels(trainingSet);
        }

        @Override
        public void stopClassificationMode() throws RemoteException {
            isClassifying = false;
            recorder.stop();
        }

        @Override
        public void deleteGesture(String trainingSetName, String gestureName) throws RemoteException {
            classifier.deleteLabel(trainingSetName, gestureName);
            classifier.commitData();
        }

        @Override
        public boolean isLearning() throws RemoteException {
            return isLearning;
        }

        @Override
        public List<ContactInformation> getContacts() throws RemoteException {
            ContactsFileManager contactsFileManager = new ContactsFileManager(mService);
            List<ContactInformation> contacts = contactsFileManager.readImportedContacts();
            return contacts;
        }

        @Override
        public void setThreshold(float threshold) throws RemoteException {
            recorder.setThreshold(threshold);
        }
    };

    public void startClassification(String trainingSetName) {
        Log.e(TAG, "Starting classification for " + trainingSetName);
        activeTrainingSet = trainingSetName;
        isClassifying = true;
        if (recorder != null && classifier != null) {
            recorder.start();
            classifier.loadTrainingSet(trainingSetName);
        }
    }
}
