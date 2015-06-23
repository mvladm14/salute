package com.salve.agrf.gestures.classifier.featureExtraction;

import com.salve.agrf.gestures.Gesture;

/**
 * Created by Vlad on 6/19/2015.
 */
public class SimpleExtractor implements IFeatureExtractor {

    public Gesture sampleSignal(Gesture signal) {
        return signal;
    }
}

