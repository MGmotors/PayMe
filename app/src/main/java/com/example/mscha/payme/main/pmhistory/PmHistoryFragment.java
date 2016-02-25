package com.example.mscha.payme.main.pmhistory;

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

public class PmHistoryFragment extends Fragment {

    private static final String TAG = "PtHistoryFragment";
    private OnListFragmentInteractionListener interactionListener;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;
    private PmHistoryAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PmHistoryFragment() {
    }

    public static PmHistoryFragment newInstance() {
        return new PmHistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pm_history_item_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pmFragmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new PmHistoryAdapter(new ArrayList<PmHistoryItem>(), interactionListener);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateListView(List<PmHistoryItem> pmHistoryItems) {
        this.adapter.updateAllItems(pmHistoryItems);
        this.swipeRefreshLayout.setRefreshing(false);
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
        void onListFragmentInteraction(PmHistoryItem pmHistoryItem);
    }
}
