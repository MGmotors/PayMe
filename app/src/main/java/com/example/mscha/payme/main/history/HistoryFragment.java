package com.example.mscha.payme.main.history;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mscha.payme.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    public static final int PM_FRAGMENT_ID = 0;
    public static final int PT_FRAGMENT_ID = 1;
    public static final String TAG = "HistoryFragment";
    private static final String ARG_FRAGMENT_ID = "fragmentId";
    private int fragmentId;
    private OnListFragmentInteractionListener interactionListener;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<HistoryItem> historyItems;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance(int fragmentId) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_ID, fragmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.fragmentId = getArguments().getInt(ARG_FRAGMENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pm_history_item_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.FragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        this.historyItems = new ArrayList<>();

        if (this.fragmentId == PM_FRAGMENT_ID)
            adapter = new PmHistoryAdapter(this.historyItems, interactionListener);
        else if (this.fragmentId == PT_FRAGMENT_ID)
            adapter = new PtHistoryAdapter(this.historyItems, interactionListener);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateListView(List<HistoryItem> historyItems) {
        this.historyItems.clear();
        this.historyItems.addAll(historyItems);
        this.adapter.notifyDataSetChanged();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    public void addItem(HistoryItem item) {
        this.historyItems.add(item);
        this.adapter.notifyItemInserted(historyItems.size() - 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener && context instanceof SwipeRefreshLayout.OnRefreshListener) {
            interactionListener = (OnListFragmentInteractionListener) context;
            refreshListener = (SwipeRefreshLayout.OnRefreshListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener and SwipeRefreshLayout.OnRefreshListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
        refreshListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(HistoryItem historyItem);
    }
}
