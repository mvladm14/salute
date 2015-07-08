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
public class PreferencesOpsImpl implements IPreferencesOps {

    private List<PreferencesModel> preferencesModels;

    public SparseArray<SocialGroup> createSocialGroups() {
        SparseArray<SocialGroup> groups = new SparseArray<>();
        for (int j = 0; j < 1; j++) {

            List<PreferencesModel> socialNetworks = new ArrayList<>();
            socialNetworks.add(new PreferencesModel("Facebook", R.drawable.facebook));
            socialNetworks.add(new PreferencesModel("Twitter", R.drawable.twitter));
            socialNetworks.add(new PreferencesModel("Linkedin", R.drawable.linkedin));

            SocialGroup group = new SocialGroup(new PreferencesModel("Social", R.drawable.social), socialNetworks);
            groups.append(j, group);
        }
        return groups;
    }

    @Override
    public List<PreferencesModel> createPreferencesModels() {
        preferencesModels = new ArrayList<>();
        preferencesModels.add(create("Name & Surname", R.drawable.namesurname));
        preferencesModels.add(create("Mobile Phone No.", R.drawable.mobilephoneno));
        preferencesModels.add(create("Email", R.drawable.email));
        preferencesModels.add(create("Address", R.drawable.address));

        selectMainItems(preferencesModels);

        return preferencesModels;
    }

    private void selectMainItems(List<PreferencesModel> list) {
        for (int i = 0; i < 4; i++) {
            list.get(i).setSelected(true);
        }
    }

    private PreferencesModel create(String s, int resId) {
        return new PreferencesModel(s, resId);
    }
}
