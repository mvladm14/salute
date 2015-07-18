package com.salve.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.salve.R;

/**
 * Created by Vlad on 7/16/2015.
 */
public class ContactsListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;

    public ContactsListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.contact_swipe_layout_row, null);
        SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, swipeLayout.findViewById(R.id.bottom_wrapper_left));
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.bottom_wrapper_right));

        return v;
    }

    @Override
    public void fillValues(int position, View view) {
//        TextView t = (TextView)view.findViewById(R.id.position);
//        t.setText((position + 1) + ".");
    }

    @Override
    public int getCount() {
        return 30;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
