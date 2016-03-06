package com.example.mscha.payme.main.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mscha.payme.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PtHistoryAdapter extends RecyclerView.Adapter<PtHistoryAdapter.ViewHolder> {

    private static final String TAG = "PtHistoryAdapter";
    private final HistoryFragment.OnListFragmentInteractionListener listener;
    private List<HistoryItem> historyItems;
    private NumberFormat numberFormat;
    private DateFormat dateFormat;

    public PtHistoryAdapter(List<HistoryItem> historyItems, HistoryFragment.OnListFragmentInteractionListener listener) {
        this.historyItems = historyItems;
        this.listener = listener;
        numberFormat = NumberFormat.getCurrencyInstance();
        dateFormat = SimpleDateFormat.getDateTimeInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_listview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.historyItem = historyItems.get(historyItems.size() - position - 1);
        holder.titleTV.setText(holder.historyItem.creator + " - " + holder.historyItem.title);
        holder.descriptionTV.setText(holder.historyItem.description);
        holder.priceTV.setText(numberFormat.format(holder.historyItem.price));
        holder.dateTime.setText(dateFormat.format(holder.historyItem.dateTime));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onListFragmentInteraction(holder.historyItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTV;
        public final TextView descriptionTV;
        public final TextView dateTime;
        public final TextView priceTV;
        public HistoryItem historyItem;

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
            return super.toString() + " '" + this.historyItem.toString() + "'";
        }
    }
}
