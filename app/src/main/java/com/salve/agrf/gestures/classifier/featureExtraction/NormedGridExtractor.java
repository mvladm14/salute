package com.salve.agrf.gestures.classifier.featureExtraction;

import com.salve.agrf.gestures.Gesture;

/**
 * Created by Vlad on 6/19/2015.
 */
public class NormedGridExtractor implements IFeatureExtractorConstCount {

    public Gesture sampleSignal(Gesture signal) {
        Gesture s = new GridExtractor().sampleSignal(signal);
        return new NormExtractor().sampleSignal(s);
    }
}

