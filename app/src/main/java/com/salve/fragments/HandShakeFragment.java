package com.salve.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salve.R;
import com.salve.activities.MainScreen;
import com.salve.activities.operations.HandShakeOpsImpl;
import com.salve.listeners.fragment.OnHandShakeFragmentListener;

public class HandShakeFragment extends Fragment {

    private HandShakeOpsImpl handShakeOps;
    private OnHandShakeFragmentListener fragmentListener;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_handshake, container, false);

        initializeFields();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentListener = ((MainScreen)activity).getScreenOps();
    }

    private void initializeFields() {
        initializeNonUIFields();
        initializeUIFields();
    }

    private void initializeNonUIFields() {
        handShakeOps = new HandShakeOpsImpl(this, rootView);
        handShakeOps.setFragmentCallback(fragmentListener);
    }

    private void initializeUIFields() {
        handShakeOps.initializeUIFields();
    }

    public void onGestureLearned() {
        handShakeOps.onGestureLearned();
    }

    public void restartClassification(String activeTrainingSet) {
        handShakeOps.restartClassification(activeTrainingSet);
    }
}
