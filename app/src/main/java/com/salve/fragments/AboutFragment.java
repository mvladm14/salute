package com.salve.fragments;

import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.salve.R;

public class AboutFragment extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_about, container, false);

        initializeFields();

        return rootView;
    }

    private void initializeFields() {
        initializeUIFields();
    }

    private void initializeUIFields() {
        PackageInfo pInfo;
        String version = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TextView versionTV = (TextView) rootView.findViewById(R.id.txtVersionNoLabel);
        versionTV.setText(version);

        TextView ownerTV = (TextView) rootView.findViewById(R.id.txtOwnerValueLabel);
        ownerTV.setText(R.string.applicationOwner);
    }
}
