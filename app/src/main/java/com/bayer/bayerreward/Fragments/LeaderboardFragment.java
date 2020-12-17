package com.bayer.bayerreward.Fragments;


import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bayer.bayerreward.R;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class LeaderboardFragment extends Fragment {

    private ViewPager viewPager;
    TabLayout tabs;
    ProgressBar progressBar;
    Adapter adapter;
    public static TextView txt_cnt, txt_time, txt_rank;
    String MY_TOTAL_POINTS = "totalPoints";
    String MY_PREFS_RANK = "rank";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        MpinScanner.count = 0;
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        txt_cnt = (TextView) view.findViewById(R.id.txt_cnt);
        txt_rank = (TextView) view.findViewById(R.id.txt_rank);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        View root = tabs.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.colorwhite));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(10);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SharedPreferences prefs2 = getActivity().getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                String rank = prefs2.getString("rank", "");


                SharedPreferences prefs3 = getActivity().getSharedPreferences(MY_PREFS_RANK, MODE_PRIVATE);
                String rankregion = prefs3.getString("rankregion", "");

                if (tab.getText().equals("Territory")) {
                    txt_rank.setText(rank);
                } else if (tab.getText().equals("Region")) {
                    txt_rank.setText(rankregion);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        CommonFunction.crashlytics("LeaderboardFrag","");
        firbaseAnalytics(getActivity(),"LeaderboardFrag","");
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {


        adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new TerritoryFragment(), "Territory");
        adapter.addFragment(new RegionFragment(), "Region");
//        adapter.addFragment(new BuisnessUnitFragment(), "Buisness Unit");
        viewPager.setAdapter(adapter);


    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
