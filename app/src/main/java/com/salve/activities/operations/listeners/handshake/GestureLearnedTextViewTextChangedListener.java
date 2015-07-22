package com.salve.activities.operations.listeners.handshake;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

/**
 * Created by Vlad on 7/22/2015.
 */
public class GestureLearnedTextViewTextChangedListener implements TextWatcher {

    private RadioButton myOwnGesture;
    private ImageView trashImageView;

    public GestureLearnedTextViewTextChangedListener(RadioButton myOwnGesture, ImageView trashImageView) {
        this.myOwnGesture = myOwnGesture;
        this.trashImageView = trashImageView;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            trashImageView.setVisibility(View.INVISIBLE);
            myOwnGesture.setEnabled(false);
        } else {
            trashImageView.setVisibility(View.VISIBLE);
            myOwnGesture.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
