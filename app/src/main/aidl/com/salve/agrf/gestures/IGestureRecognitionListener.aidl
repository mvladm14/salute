// IGestureRecognitionListener.aidl
package com.salve.agrf.gestures;

// Declare any non-default types here with import statements
import com.salve.agrf.gestures.classifier.Distribution;

interface IGestureRecognitionListener {
	void onGestureRecognized(in Distribution distribution);

	 void onGestureLearned(String gestureName);

	 void onTrainingSetDeleted(String trainingSet);
}
