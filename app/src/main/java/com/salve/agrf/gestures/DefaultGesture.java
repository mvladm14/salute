package com.salve.agrf.gestures;

import android.util.Log;

import com.salve.preferences.SalvePreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 7/21/2015.
 */
public class DefaultGesture {

    private static final String TAG = "DefaultGesture";

    public static List<Gesture> getDefaultValues() {

        Log.e(TAG, "Accessing default gesture values");

        List<Gesture> values = new ArrayList<>();

        List<float[]> sensorDataList = new ArrayList<>();
        float[][] sensorValues = new float[32][3];
        int counter = 0;

        sensorValues[counter++] = new float[]{0.9268936f, 0.14556576f, 0.67683417f};
        sensorValues[counter++] = new float[]{0.8537872f, 0.12524481f, 0.6571498f};
        sensorValues[counter++] = new float[]{0.78171957f, 0.14285864f, 0.64327484f};
        sensorValues[counter++] = new float[]{0.70993525f, 0.1708183f, 0.63098437f};
        sensorValues[counter++] = new float[]{0.6403894f, 0.21467096f, 0.64071465f};
        sensorValues[counter++] = new float[]{0.57252234f, 0.27044335f, 0.6669605f};
        sensorValues[counter++] = new float[]{0.5069637f, 0.33884206f, 0.6879949f};
        sensorValues[counter++] = new float[]{0.4455602f, 0.42996794f, 0.69964886f};
        sensorValues[counter++] = new float[]{0.3908021f, 0.506516f, 0.7126948f};
        sensorValues[counter++] = new float[]{0.37591636f, 0.49559656f, 0.734093f};
        sensorValues[counter++] = new float[]{0.36103067f, 0.48467708f, 0.7554912f};
        sensorValues[counter++] = new float[]{0.3877487f, 0.3710897f, 0.7246915f};
        sensorValues[counter++] = new float[]{0.41766697f, 0.24960473f, 0.68987656f};
        sensorValues[counter++] = new float[]{0.44817984f, 0.15725464f, 0.7141359f};
        sensorValues[counter++] = new float[]{0.47893056f, 0.07655851f, 0.7620247f};
        sensorValues[counter++] = new float[]{0.4952608f, 0.024140332f, 0.76958996f};
        sensorValues[counter++] = new float[]{0.49717048f, 0.0f, 0.7368316f};
        sensorValues[counter++] = new float[]{0.49026626f, 0.018712042f, 0.7110684f};
        sensorValues[counter++] = new float[]{0.46132722f, 0.14455505f, 0.7027932f};
        sensorValues[counter++] = new float[]{0.4321085f, 0.2708141f, 0.69590294f};
        sensorValues[counter++] = new float[]{0.39925227f, 0.40248385f, 0.70701826f};
        sensorValues[counter++] = new float[]{0.36639595f, 0.5341539f, 0.71813357f};
        sensorValues[counter++] = new float[]{0.37849054f, 0.54196745f, 0.72232366f};
        sensorValues[counter++] = new float[]{0.39807704f, 0.5291383f, 0.72535956f};
        sensorValues[counter++] = new float[]{0.44023332f, 0.4457666f, 0.72858435f};
        sensorValues[counter++] = new float[]{0.49492854f, 0.3232043f, 0.73191404f};
        sensorValues[counter++] = new float[]{0.5680489f, 0.20209022f, 0.7187491f};
        sensorValues[counter++] = new float[]{0.66573644f, 0.08290656f, 0.6835914f};
        sensorValues[counter++] = new float[]{0.7286193f, 0.048284054f, 0.6504694f};
        sensorValues[counter++] = new float[]{0.663886f, 0.32371876f, 0.6248111f};
        sensorValues[counter++] = new float[]{0.86045706f, 0.071340136f, 0.67017126f};

        for (int i = 0; i < counter ; i++) {
            sensorDataList.add(sensorValues[i]);
        }

        Gesture gesture = new Gesture(sensorDataList,SalvePreferences.DEFAULT_GESTURE);

        values.add(gesture);

        return values;
    }
}
