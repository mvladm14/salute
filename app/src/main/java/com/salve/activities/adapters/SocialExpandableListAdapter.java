package com.salve.activities.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.models.PreferencesModel;
import com.salve.activities.models.SocialGroup;

public class SocialExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<SocialGroup> groups;
    public LayoutInflater inflater;
    public Context mContext;

    public SocialExpandableListAdapter(Context context, SparseArray<SocialGroup> groups) {
        mContext = context;
        this.groups = groups;
        inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final PreferencesModel children = (PreferencesModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.preferences_rowbuttonlayout, null);
        }

        final TextView text = (TextView) convertView.findViewById(R.id.preferences_row_textView);
        text.setText(children.getName());

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.preferences_row_checkBox);
        checkBox.setEnabled(false);

        //TODO enable this in a future release
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                checkBox.setChecked(b);
//                Context ctx = mContext.getApplicationContext();
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putBoolean(text.getText().toString(), b);
//                editor.apply();
//            }
//        });
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkBox.setChecked(!checkBox.isChecked());
//            }
//        });

        Context ctx = mContext.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        checkBox.setChecked(prefs.getBoolean(text.getText().toString(), false));

        ImageView imageView = (ImageView) convertView.findViewById(R.id.preferences_row_image);
        imageView.setImageResource(children.getResId());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.preferences_expandable_row, null);
        }
        SocialGroup group = (SocialGroup) getGroup(groupPosition);

        ((CheckedTextView) convertView).setText(group.mySelf.getName());
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
