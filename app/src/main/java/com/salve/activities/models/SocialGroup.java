package com.salve.activities.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 7/7/2015.
 */
public class SocialGroup {

    public PreferencesModel mySelf;
    public final List<PreferencesModel> children;

    public SocialGroup(PreferencesModel mySelf, List<PreferencesModel> preferencesModels) {
        this.children = preferencesModels;
        this.mySelf = mySelf;
    }
}
