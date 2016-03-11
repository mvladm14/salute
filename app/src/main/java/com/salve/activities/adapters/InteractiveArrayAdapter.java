package com.salve.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.salve.R;
import com.salve.activities.models.PreferencesModel;

import java.util.List;

public class InteractiveArrayAdapter extends ArrayAdapter<PreferencesModel> {

    private final List<PreferencesModel> list;
    private final Activity activity;

    public InteractiveArrayAdapter(Activity activity, List<PreferencesModel> list) {
        super(activity, R.layout.preferences_rowbuttonlayout, list);
        this.activity = activity;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
        protected ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = LayoutInflater.from(activity);
            view = inflator.inflate(R.layout.preferences_rowbuttonlayout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.preferences_row_textView);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.preferences_row_image);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.preferences_row_checkBox);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            PreferencesModel element = (PreferencesModel) viewHolder.checkbox
                                    .getTag();
                            element.setSelected(buttonView.isChecked());

                            Context ctx = activity.getApplicationContext();
                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putBoolean(viewHolder.text.getText().toString(), buttonView.isChecked());
                            editor.apply();
                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.imageView.setImageResource(list.get(position).getResId());

        Context ctx = activity.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        holder.checkbox.setChecked(prefs.getBoolean(holder.text.getText().toString(), true));

        return view;
    }
}
