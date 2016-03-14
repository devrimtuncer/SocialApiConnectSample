package com.devrimtuncer.socialconnection.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devrimtuncer.socialconnection.R;
import com.devrimtuncer.socialconnection.activities.loggedin.ItemDetailActivity;
import com.devrimtuncer.socialconnection.activities.loggedin.ItemListActivity;
import com.devrimtuncer.socialconnection.pojo.Item;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends BaseFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    /**
     * The dummy content this fragment is presenting.
     */
    private Item mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(getResources().getString(R.string.id_text, String.valueOf(mItem.getId())));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        TextView itemDetailTimeTextView = (TextView) rootView.findViewById(R.id.item_detail_time);
        TextView itemDetailInfoTextView = (TextView) rootView.findViewById(R.id.item_detail_info);

        if (mItem != null) {
            itemDetailTimeTextView.setText(mItem.getCreatedAt());
            itemDetailInfoTextView.setText(mItem.getText());
        } else {
            itemDetailTimeTextView.setText(null);
            itemDetailInfoTextView.setText(null);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }
}
