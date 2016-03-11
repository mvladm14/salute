package com.salve.activities.operations;

import android.util.SparseArray;

import com.salve.R;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.models.SocialGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad on 7/8/2015.
 */
public class PreferencesOpsImpl {

    private static List<PreferencesModel> preferencesModels;
    private static SparseArray<SocialGroup> groups;
    private static List<PreferencesModel> socialNetworkPreferencesModels;

    public static List<PreferencesModel> getPreferencesModels() {
        if (preferencesModels == null) {
            preferencesModels = createPreferencesModels();
        }
        return preferencesModels;
    }

    public static SparseArray<SocialGroup> getGroups() {
        if (groups == null) {
            groups = createSocialGroups();
        }
        return groups;
    }

    private static SparseArray<SocialGroup> createSocialGroups() {
        SparseArray<SocialGroup> socialGroups = new SparseArray<>();
        for (int j = 0; j < 1; j++) {

            socialNetworkPreferencesModels = new ArrayList<>();
            socialNetworkPreferencesModels.add(new PreferencesModel("Facebook", R.drawable.facebook));
            socialNetworkPreferencesModels.add(new PreferencesModel("Twitter", R.drawable.twitter));
            socialNetworkPreferencesModels.add(new PreferencesModel("Linkedin", R.drawable.linkedin));

            SocialGroup group = new SocialGroup(new PreferencesModel("Social", R.drawable.social), socialNetworkPreferencesModels);
            socialGroups.append(j, group);
        }
        return socialGroups;
    }

    private static List<PreferencesModel> createPreferencesModels() {
        List<PreferencesModel> preferences = new ArrayList<>();
        preferences.add(create("Name & Surname", R.drawable.namesurname));
        preferences.add(create("Mobile Phone No.", R.drawable.mobilephoneno));
        preferences.add(create("Email", R.drawable.email));
        preferences.add(create("Address", R.drawable.address));

        return preferences;
    }

    private static PreferencesModel create(String s, int resId) {
        return new PreferencesModel(s, resId);
    }
}
