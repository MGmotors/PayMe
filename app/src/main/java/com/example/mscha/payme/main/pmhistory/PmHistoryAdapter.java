package com.example.mscha.payme.main.pmhistory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mscha.payme.R;
import com.example.mscha.payme.main.pmhistory.PmHistoryFragment.OnListFragmentInteractionListener;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PmHistoryAdapter extends RecyclerView.Adapter<PmHistoryAdapter.ViewHolder> {

    private static final String TAG = "PmHistoryAdapter";
    private final OnListFragmentInteractionListener listener;
    private List<PmHistoryItem> pmHistoryItems;
    private NumberFormat numberFormat;
    private DateFormat dateFormat;

    public PmHistoryAdapter(List<PmHistoryItem> pmHistoryItems, OnListFragmentInteractionListener listener) {
        this.pmHistoryItems = pmHistoryItems;
        this.listener = listener;
        numberFormat = NumberFormat.getCurrencyInstance();
        dateFormat = SimpleDateFormat.getDateTimeInstance();
    }

    public void updateAllItems(List<PmHistoryItem> pmHistoryItems) {
        this.pmHistoryItems.clear();
        this.pmHistoryItems.addAll(pmHistoryItems);
        this.notifyDataSetChanged();
    }

    public void addItem(PmHistoryItem item) {
        this.pmHistoryItems.add(item);
        this.notifyItemInserted(pmHistoryItems.size() - 1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pmHistoryItem = pmHistoryItems.get(pmHistoryItems.size() - position - 1);
        holder.titleTV.setText(holder.pmHistoryItem.title);
        holder.descriptionTV.setText(holder.pmHistoryItem.description);
        holder.priceTV.setText(numberFormat.format(holder.pmHistoryItem.price));
        holder.dateTime.setText(dateFormat.format(holder.pmHistoryItem.dateTime));

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
        return pmHistoryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTV;
        public final TextView descriptionTV;
        public final TextView dateTime;
        public final TextView priceTV;
        public PmHistoryItem pmHistoryItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.titleTV = (TextView) view.findViewById(R.id.pmHistoryTitle);
            this.descriptionTV = (TextView) view.findViewById(R.id.pmHistoryDescription);
            this.dateTime = (TextView) view.findViewById(R.id.pmHistoryDateTime);
            this.priceTV = (TextView) view.findViewById(R.id.pmHistoryPrice);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + this.pmHistoryItem.toString() + "'";
        }
    }
}
