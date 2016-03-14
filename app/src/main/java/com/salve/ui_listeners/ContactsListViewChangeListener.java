package com.salve.ui_listeners;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.salve.R;
import com.salve.activities.operations.HomeFragmentOpsImpl;

public class ContactsListViewChangeListener implements View.OnLayoutChangeListener {

    private static final String TAG = "ContactsViewListener";

    private ListView mListView;
    private View.OnClickListener mClickListener;

    public ContactsListViewChangeListener(ListView view, View.OnClickListener clickListener) {
        mListView = view;
        mClickListener = clickListener;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        mListView.removeOnLayoutChangeListener(this);
        Log.e(TAG, "updated");

        for (int i = 0; i < mListView.getChildCount(); i++) {
            SwipeLayout swipeLayout = (SwipeLayout) mListView.getChildAt(i);
            swipeLayout.findViewById(R.id.trash).setOnClickListener(mClickListener);
            swipeLayout.findViewById(R.id.add_contact).setOnClickListener(mClickListener);
        }

        ((HomeFragmentOpsImpl)mClickListener).setContactsTextViewVisible(mListView.getChildCount() == 0);
    }
}