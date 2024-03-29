// IGestureRecognitionService.aidl
package com.salve.agrf.gestures;

// Declare any non-default types here with import statements
import com.salve.agrf.gestures.IGestureRecognitionListener;
import com.salve.contacts.ContactInformation;

interface IGestureRecognitionService {

    void startClassificationMode(String trainingSetName);

    void stopClassificationMode();

	void registerListener(IGestureRecognitionListener listener);

	void unregisterListener(IGestureRecognitionListener listener);

	void startLearnMode(String trainingSetName, String gestureName);

	void stopLearnMode();

	void setThreshold(float threshold);

	void onPushToGesture(boolean pushed);

	void deleteTrainingSet(String trainingSetName);

	void deleteGesture(String trainingSetName, String gestureName);

	List<String> getGestureList(String trainingSet);

	boolean isLearning();

	List<ContactInformation> getContacts();
}
