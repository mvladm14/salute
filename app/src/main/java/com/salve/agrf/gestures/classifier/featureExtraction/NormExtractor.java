package com.salve.agrf.gestures.classifier.featureExtraction;

import com.salve.agrf.gestures.Gesture;

import java.util.ArrayList;

/**
 * Created by Vlad on 6/19/2015.
 */
public class NormExtractor implements IFeatureExtractor {

    public Gesture sampleSignal(Gesture signal) {
        ArrayList<float[]> sampledValues = new ArrayList<float[]>(signal.length());
        Gesture sampledSignal = new Gesture(sampledValues, signal.getLabel());

        float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
        for (int i = 0; i < signal.length(); i++) {
            for (int j = 0; j < 3; j++) {
                if (signal.getValue(i, j) > max) {
                    max = signal.getValue(i, j);
                }
                if (signal.getValue(i, j) < min) {
                    min = signal.getValue(i, j);
                }
            }
        }
        for (int i = 0; i < signal.length(); ++i) {
            sampledValues.add(new float[3]);
            for (int j = 0; j < 3; ++j) {
                sampledSignal.setValue(i, j, (signal.getValue(i, j) - min) / (max - min));
            }
        }
        return sampledSignal;
    }
}

