package com.salve.agrf.gestures;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandPendingResult;
import com.microsoft.band.ConnectionState;
import com.salve.activities.MainScreen;
import com.salve.agrf.gestures.classifier.Distribution;
import com.salve.agrf.gestures.classifier.GestureClassifier;
import com.salve.agrf.gestures.classifier.featureExtraction.NormedGridExtractor;
import com.salve.agrf.gestures.recorder.GestureRecorder;
import com.salve.agrf.gestures.recorder.GestureRecorderListener;
import com.salve.band.sensors.registration.SensorRegistrationManager;
import com.salve.band.tasks.AsyncResponse;
import com.salve.band.tasks.BandConnectionTask;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GestureRecognitionService extends Service implements GestureRecorderListener, AsyncResponse {

    private final String TAG = "GestureRecognitionSvc";

    GestureRecorder recorder;
    GestureClassifier classifier;
    String activeTrainingSet;
    String activeLearnLabel;
    boolean isLearning, isClassifying;

    Set<IGestureRecognitionListener> listeners = new HashSet<>();

    private BandClient bandClient;
    private SensorRegistrationManager sensorRegistrationManager;
    ConnectionState state;

    @Override
    public void onCreate() {

        BandInfo[] pairedBands = BandClientManager.getInstance().getPairedBands();
        bandClient = BandClientManager.getInstance().create(this, pairedBands[0]);
        sensorRegistrationManager = new SensorRegistrationManager(bandClient);

        connect();

        recorder = new GestureRecorder(sensorRegistrationManager, this);
        classifier = new GestureClassifier(new NormedGridExtractor(), this);
        super.onCreate();
    }

    public void connect() {
        BandPendingResult<ConnectionState> pendingResult = bandClient.connect();
        BandConnectionTask connectionTask = new BandConnectionTask(this);
        connectionTask.execute(pendingResult);
    }

    @Override
    public void onFinishedConnection(ConnectionState connectionState) {
        state = connectionState;
        Log.e(TAG, state.toString());
        recorder.registerListener(this);

        Intent intent = new Intent(this, MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onFinishedVersion(String version) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return gestureRecognitionServiceStub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG, "onUnbind");
        recorder.unregisterListener(this);
        return super.onUnbind(intent);
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
            Distribution distribution = classifier.classifySignal(activeTrainingSet, new Gesture(values, null));
            recorder.pause(false);
            if (distribution != null && distribution.size() > 0) {
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
            Log.e(TAG, "Starting classification");
            activeTrainingSet = trainingSetName;
            isClassifying = true;
            recorder.start();
            classifier.loadTrainingSet(trainingSetName);
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
        public void setThreshold(float threshold) throws RemoteException {
            recorder.setThreshold(threshold);
        }
    };
}