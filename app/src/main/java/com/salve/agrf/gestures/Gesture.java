package com.salve.agrf.gestures;

/**
 * Created by Vlad on 6/19/2015.
 */

import java.io.Serializable;
import java.util.List;

public class Gesture implements Serializable {

    private static final long serialVersionUID = 7148492971634218981L;
    private String label;
    private List<float[]> values;

    public Gesture(List<float[]> values, String label) {
        setValues(values);
        setLabel(label);
    }

    public void setValues(List<float[]> values) {
        this.values = values;
    }

    public String getLabel() {
        return label;
    }

    public List<float[]> getValues() {
        return values;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public float getValue(int index, int dim) {
        return values.get(index)[dim];
    }

    public void setValue(int index, int dim, float f) {
        values.get(index)[dim] = f;
    }

    public int length() {
        return values.size();
    }

    @Override
    public String toString() {
        String valuesString = "";
        for (int i = 0; i < values.size(); i++) {
            String list = "sensorValues[counter++] = new float[]{";
            for (int j = 0; j < values.get(i).length - 1; j++) {
                list += values.get(i)[j] + "f, ";
            }
            list += values.get(i)[values.get(i).length - 1]  + "f};\n";
            valuesString += list;
        }
        return this.label + ": " + " Values = " + valuesString;
    }
}
