package com.salve.activities.operations;

import android.util.SparseArray;

import com.salve.activities.models.PreferencesModel;
import com.salve.activities.models.SocialGroup;

import java.util.List;

/**
 * Created by Vlad on 7/8/2015.
 */
public interface IPreferencesOps{

    public SparseArray<SocialGroup> createSocialGroups();

    public List<PreferencesModel> createPreferencesModels();
}
