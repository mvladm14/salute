package com.salve.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.salve.R;
import com.salve.contacts.ContactInformation;

import java.util.List;

public class ContactsListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<ContactInformation> contacts;

    public ContactsListViewAdapter(Context mContext, List<ContactInformation> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
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
        SwipeLayout swipeLayout = (SwipeLayout) view.findViewById(getSwipeLayoutResourceId(position));
        TextView textView = (TextView) swipeLayout.getSurfaceView().findViewById(R.id.contact_details);
        textView.setText(contacts.get(position).getName());

        ImageView imageView = (ImageView) swipeLayout.getSurfaceView().findViewById(R.id.contact_feeling);
        imageView.setImageResource(contacts.get(position).getHeartRate() > 0.7 ?
                R.drawable.interested : R.drawable.bored);
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setContacts(List<ContactInformation> contacts) {
        this.contacts = contacts;
    }
}
