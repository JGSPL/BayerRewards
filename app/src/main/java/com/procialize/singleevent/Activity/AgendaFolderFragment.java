package com.procialize.singleevent.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabWidget;

import com.procialize.singleevent.CustomTools.AutoScrollViewPager;
import com.procialize.singleevent.R;


public class AgendaFolderFragment extends Fragment {
    private FragmentTabHost agendaTabHost;
    private TabWidget tabWidget;
    public static AutoScrollViewPager mViewPager;
    LinearLayout sliderDotspanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agenda_folder, container, false);

        agendaTabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabWidget = (TabWidget) view.findViewById(android.R.id.tabs);
        agendaTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        return view;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager = (AutoScrollViewPager) getActivity().findViewById(R.id.pagerimage);
        sliderDotspanel = (LinearLayout) getActivity().findViewById(R.id.SliderDots);

		/*mViewPager.startAutoScroll();
		mViewPager.setInterval(5000);
		mViewPager.setStopScrollWhenTouch(true);
		mViewPager.setCycle(true);*/

    }

}
