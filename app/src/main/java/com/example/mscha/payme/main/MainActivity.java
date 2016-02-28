package com.example.mscha.payme.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mscha.payme.R;
import com.example.mscha.payme.login.LoginActivity;
import com.example.mscha.payme.main.history.HistoryFragment;
import com.example.mscha.payme.main.history.HistoryItem;
import com.example.mscha.payme.newpm.NewPmActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, HistoryFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final int NEW_PM_REQUEST = 0;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;
    private MainPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.presenter = new MainPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        //TODO toolbar zu hoch korrigieren

        progressDialog = new ProgressDialog(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewPm();
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.onPostCreate();
    }

    public void updateHistoryItems(List<HistoryItem> items, int fragmentId) {
        HistoryFragment historyFragment = null;
        if(fragmentId == HistoryFragment.PM_FRAGMENT_ID) {
            historyFragment = (HistoryFragment) sectionsPagerAdapter.getRegisteredFragment(HistoryFragment.PM_FRAGMENT_ID);
        }
        else if(fragmentId == HistoryFragment.PT_FRAGMENT_ID) {
            historyFragment = (HistoryFragment) sectionsPagerAdapter.getRegisteredFragment(HistoryFragment.PT_FRAGMENT_ID);
        }

        if (historyFragment != null) {
            historyFragment.updateListView(items);
        } else {
            Log.e(TAG, "historyFragment is null");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_PM_REQUEST) {
            if (resultCode == NewPmActivity.RESULT_OK) {
                this.presenter.onNewPmSent();
                Log.d(TAG, "New PM was sent");
            } else if (resultCode == NewPmActivity.RESULT_CANCELED) {
                Log.d(TAG, "New PM canceled");
            }
        }
    }

    public void startNewPm() {
        startActivityForResult(new Intent(this, NewPmActivity.class), NEW_PM_REQUEST);
    }

    public void navigateToLogin() {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            this.presenter.onLogout();
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(HistoryItem historyItem) {
        //TODO hier detail view starten
        Log.d(TAG, historyItem.toString());
    }

    @Override
    public void onRefresh() {
        presenter.onRefresh(viewPager.getCurrentItem());
    }

    public void showLoginProgressDialog(boolean show) {
        if (show) {
            progressDialog.setMessage(getString(R.string.logging_in));
            this.progressDialog.show();
        } else
            this.progressDialog.hide();
    }

    public void showLogoutProgressDialog(boolean show) {
        if (show) {
            progressDialog.setMessage(getString(R.string.logout_progress_dialog_message));
            this.progressDialog.show();
        } else
            this.progressDialog.hide();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SparseArray<Fragment> registeredFragments = new SparseArray<>();
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == HistoryFragment.PM_FRAGMENT_ID)
                return HistoryFragment.newInstance(HistoryFragment.PM_FRAGMENT_ID);
            if (position == HistoryFragment.PT_FRAGMENT_ID)
                return HistoryFragment.newInstance(HistoryFragment.PT_FRAGMENT_ID);
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.payme_fragment_tab_title);
                case 1:
                    return getString(R.string.paythem_fragment_tab_title);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            Log.d(TAG, "Fragment has been added at position: " + position);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
    }
}
