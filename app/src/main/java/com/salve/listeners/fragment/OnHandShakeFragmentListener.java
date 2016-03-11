package com.salve.listeners.fragment;

// Container Activity must implement this interface
public interface OnHandShakeFragmentListener {
    public void restartClassification(String activeTrainingSet);
    public void deleteGestureData();
}
