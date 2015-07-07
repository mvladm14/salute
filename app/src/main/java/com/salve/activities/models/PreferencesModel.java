package com.salve.activities.models;

/**
 * Created by Vlad on 7/7/2015.
 */
public class PreferencesModel {
    private String name;
    private boolean selected;
    private int resId;

    public PreferencesModel(String name, int resId) {
        this.name = name;
        this.resId = resId;
        selected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
