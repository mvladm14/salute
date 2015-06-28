package com.salve.agrf.gestures.classifier;

import android.content.Context;
import android.util.Log;

import com.salve.agrf.gestures.Gesture;
import com.salve.agrf.gestures.classifier.featureExtraction.IFeatureExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class GestureClassifier {

    private final String TAG = "GestureClassifier";

    protected List<Gesture> trainingSet = Collections.emptyList();
    protected IFeatureExtractor featureExtractor;
    protected String activeTrainingSet = "";
    private final Context context;

    public GestureClassifier(IFeatureExtractor fE, Context context) {
        trainingSet = new ArrayList<>();
        featureExtractor = fE;
        this.context = context;
    }

    public boolean commitData() {
        if (activeTrainingSet != null && activeTrainingSet != "") {
            try {
                FileOutputStream fos = new FileOutputStream(new File(context.getExternalFilesDir(null), activeTrainingSet + ".gst").toString());
                ObjectOutputStream o = new ObjectOutputStream(fos);
                o.writeObject(trainingSet);
                o.close();
                fos.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean trainData(String trainingSetName, Gesture signal) {
        loadTrainingSet(trainingSetName);
        trainingSet.add(featureExtractor.sampleSignal(signal));
        return true;
    }

    @SuppressWarnings("unchecked")
    public void loadTrainingSet(String trainingSetName) {
        if (!trainingSetName.equals(activeTrainingSet)) {
            activeTrainingSet = trainingSetName;
            FileInputStream input;
            ObjectInputStream o;
            try {
                File f = context.getExternalFilesDir(null);
                Log.e(TAG,f == null ? "NULL external file" : f.getAbsolutePath());
                input = new FileInputStream(new File(context.getExternalFilesDir(null), activeTrainingSet + ".gst"));
                o = new ObjectInputStream(input);
                trainingSet = (ArrayList<Gesture>) o.readObject();
                try {
                    o.close();
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "FUCKED UP");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e(TAG, "FUCKED UP 2");
                trainingSet = new ArrayList<>();
            }
        }
    }

    public boolean checkForLabel(String trainingSetName, String label) {
        loadTrainingSet(trainingSetName);
        for (Gesture s : trainingSet) {
            if (s.getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkForTrainingSet(String trainingSetName) {
        File file = new File(trainingSetName + ".gst");
        return file.exists();
    }

    public boolean deleteTrainingSet(String trainingSetName) {
        Log.e(TAG, String.format("Try to delete training set %s\n", trainingSetName));
        if (activeTrainingSet != null && activeTrainingSet.equals(trainingSetName)) {
            trainingSet = new ArrayList<>();
        }
        // File file = new File("\\sdcard\\" + activeTrainingSet + ".gst");
        // if (file.exists()) {
        // file.delete();
        // return true;
        // }
        // return false;
        return context.deleteFile(activeTrainingSet + ".gst");

    }

    public boolean deleteLabel(String trainingSetName, String label) {
        loadTrainingSet(trainingSetName);
        boolean labelExisted = false;
        ListIterator<Gesture> it = trainingSet.listIterator();
        while (it.hasNext()) {
            Gesture s = it.next();
            if (s.getLabel().equals(label)) {
                it.remove();
                labelExisted = true;
            }
        }
        return labelExisted;
    }

    public List<String> getLabels(String trainingSetName) {
        loadTrainingSet(trainingSetName);
        List<String> labels = new ArrayList<>();

        for (Gesture s : trainingSet) {
            if (!labels.contains(s.getLabel())) {
                labels.add(s.getLabel());
            }
        }
        return labels;
    }

    public IFeatureExtractor getFeatureExtractor() {
        return featureExtractor;
    }

    public void setFeatureExtractor(IFeatureExtractor featureExtractor) {
        this.featureExtractor = featureExtractor;
    }

    public Distribution classifySignal(String trainingSetName, Gesture signal) {
        if (trainingSetName == null) {
            Log.e(TAG, "No Training Set Name specified");
            trainingSetName = "default";
        }
        if (!trainingSetName.equals(activeTrainingSet)) {
            loadTrainingSet(trainingSetName);
        }

        Distribution distribution = new Distribution();
        Gesture sampledSignal = featureExtractor.sampleSignal(signal);

        for (Gesture s : trainingSet) {
            double dist = DTWAlgorithm.calcDistance(s, sampledSignal);
            distribution.addEntry(s.getLabel(), dist);
        }
        if (trainingSet.isEmpty()) {
            Log.e(TAG, String.format("No training data for trainingSet %s available.\n", trainingSetName));
        }

        return distribution;
    }

}
