package com.example.mscha.payme.main.pmhistory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mscha.payme.R;
import com.example.mscha.payme.main.pmhistory.PmHistoryFragment.OnListFragmentInteractionListener;

import java.text.NumberFormat;
import java.util.List;

public class PmHistoryItemRecyclerViewAdapter extends RecyclerView.Adapter<PmHistoryItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "PmHistoryItemRecyclerViewAdapter";
    private final OnListFragmentInteractionListener listener;
    private List<PmHistoryItem> pmHistoryItems;
    private NumberFormat numberFormat;

    public PmHistoryItemRecyclerViewAdapter(List<PmHistoryItem> pmHistoryItems, OnListFragmentInteractionListener listener) {
        this.pmHistoryItems = pmHistoryItems;
        this.listener = listener;
        numberFormat = NumberFormat.getCurrencyInstance();
    }

    public void updateItems(List<PmHistoryItem> pmHistoryItems) {
        this.pmHistoryItems.clear();
        this.pmHistoryItems.addAll(pmHistoryItems);
        this.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pmHistoryItem = pmHistoryItems.get(position);
        holder.titleTV.setText(pmHistoryItems.get(position).title);
        holder.descriptionTV.setText(pmHistoryItems.get(position).description);
        holder.priceTV.setText(numberFormat.format(pmHistoryItems.get(position).price));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onListFragmentInteraction(holder.pmHistoryItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pmHistoryItems == null)
            return 0;
        return pmHistoryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTV;
        public final TextView descriptionTV;
        public final TextView priceTV;
        public PmHistoryItem pmHistoryItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.titleTV = (TextView) view.findViewById(R.id.pmHistoryTitle);
            this.descriptionTV = (TextView) view.findViewById(R.id.pmHistoryDescription);
            this.priceTV = (TextView) view.findViewById(R.id.pmHistoryPrice);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + this.pmHistoryItem.toString() + "'";
        }
    }
}
