package com.procialize.singleevent.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.singleevent.Adapter.SwipeAgendaImageAdapter;
import com.procialize.singleevent.Adapter.SwipepagerAgendaImage;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.CustomTools.AutoScrollViewPager;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.Agenda;
import com.procialize.singleevent.GetterSetter.AgendaFolder;

import com.procialize.singleevent.GetterSetter.AgendaMediaList;
import com.procialize.singleevent.GetterSetter.AgendaVacationList;
import com.procialize.singleevent.GetterSetter.FetchAgenda;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AgendaFolderFragment extends Fragment implements SwipeAgendaImageAdapter.SwipeAgendaImageAdapterListner {
    // Declare Variable
    private FragmentTabHost agendaTabHost;
    String url = "";

    String email = "";

    ArrayList<String> eventDates = new ArrayList<String>();
    ArrayList<String> eventtempDates = new ArrayList<String>();

    Date d, d1;
    String updateAgendaUrl = "";
    ProgressDialog pDialog;

    // Database Helper
    private DBHelper procializeDB;
    private SQLiteDatabase db;

    private List<AgendaVacationList> agendaList = new ArrayList<AgendaVacationList>();
    private List<AgendaVacationList> agendaDBList;
    private List<AgendaMediaList> agendaFolderDBList;
    SessionManager session;
    ApiConstant constant;
    String api_access_token;
    ConnectionDetector cd;

    private DBHelper dbHelper;

    private APIService mAPIService;
    private List<AgendaMediaList> agendaFolderList = new ArrayList<AgendaMediaList>();
    private ArrayList<AgendaMediaList> agendaFolderImageList = new ArrayList<AgendaMediaList>();

    private String imgTemp = "0";
    public static AutoScrollViewPager mViewPager;
    private TabWidget tabWidget;
    SwipepagerAgendaImage swipepagerAdapter;
    int page;
    Handler handler;
    Runnable runnable;
    String dayFromDate;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    String eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    String token;
    static LinearLayout tab_text;
    TextView txtname;
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.agenda_folder_fragment,
                container, false);
        // Toast.makeText(getActivity(), "Inside Agenda Fragment",
        // Toast.LENGTH_LONG).show();
        System.out.println("Inside Agenda Fragment");

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(getContext());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getReadableDatabase();
//
//		// Connection Detector Reference
        cd = new ConnectionDetector(getActivity().getApplicationContext());


        constant = new ApiConstant();
        handler = new Handler();
        session = new SessionManager(getActivity().getApplicationContext());
//		api_access_token = session.getAccessToken();
//		eventId = session.getEventId();
        imgTemp = "0";

        // Create FragmentTabHost
        //agendaTabHost = new FragmentTabHost(getActivity());
        agendaTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        tabWidget = (TabWidget) rootView.findViewById(android.R.id.tabs);
        agendaTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        txtname = (TextView) rootView.findViewById(R.id.txtname);
        webView = (WebView) rootView.findViewById(R.id.webView);

        // // Locate fragment1.xml to create FragmentTabHost
        //agendaTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontent);


        if (cd.isConnectingToInternet()) {

            // Initialize Update Agenda URL
            updateAgendaUrl = constant.INDEPENDENT_AGENDA;

            fetchAgenda(token, eventid);
        }


//		agendaTabHost.getTabWidget().setDividerDrawable(R.drawable.vertical_divider);

        return rootView;

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

    private void initview(final Response<Agenda> response) throws ParseException {


        String tempDate = "";
        if (eventtempDates.size() > 0) {
            eventtempDates.clear();
        }

        for (int i = 0; i < agendaDBList.size(); i++) {

            String eventdate = "";
            eventdate = agendaDBList.get(i).getFolder_name();


            if (!tempDate.equalsIgnoreCase(eventdate)) {
                tempDate = eventdate;

                eventDates.add(tempDate);
            }
        }

        txtname.setText(agendaDBList.get(0).getSession_name());
        webView.loadData(agendaDBList.get(0).getSession_description(), "text/html", null);

		/*int cnt= 0;
		for(int i=0;i<eventtempDates.size();i++){
			for(int j=i+1;j<eventtempDates.size();j++){
				if(eventtempDates.get(i).equals(eventtempDates.get(j))){
					cnt+=1;
				}
			}
			if(cnt<1){
				eventDates.add(eventtempDates.get(i));
			}
			cnt=0;
		}*/

        for (int j = 0; j < eventDates.size(); j++) {

            Bundle sessionDate = new Bundle();
            sessionDate.putString("sessionDate", eventDates.get(j));

            //String tag = "Tab" + j;
            String tag = eventDates.get(j);


            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd");

            DateFormat outputFormat = new SimpleDateFormat("dd");
            try {
                d = inputFormat.parse(eventDates.get(j));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(d);


            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = dateformat.parse(eventDates.get(j));
                //DateFormat dayFormate=new SimpleDateFormat("EEE");
                DateFormat dayFormate = new SimpleDateFormat("MMM");

                dayFromDate = dayFormate.format(date);
                Log.d("asd", "----------:: " + dayFromDate);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            // Create Session Tabs
            agendaTabHost.addTab(
                    agendaTabHost.newTabSpec(tag)
                            .setIndicator(createTabView(agendaTabHost.getContext(), outputDateStr, dayFromDate)),
                    AgendaFolderListFragment.class, sessionDate);


            if (imgTemp.equalsIgnoreCase("0")) {
                if (agendaFolderImageList.size() > 0) {
                    agendaFolderImageList.clear();
                }
//                for (int i = 0; i < agendaDBList.size(); i++) {


                String imagedate = agendaDBList.get(0).getFolder_name();
                String session = agendaDBList.get(0).getSessionId();
                if (imagedate.equalsIgnoreCase(eventDates.get(0))) {
                    for (int k = 0; k < agendaFolderDBList.size(); k++) {
                        if (session.equalsIgnoreCase(agendaFolderDBList.get(k).getSession_vacation_id()))
                            agendaFolderImageList.add(agendaFolderDBList.get(k));
                    }
                }

//                }


                swipepagerAdapter = new SwipepagerAgendaImage(getActivity(), agendaFolderImageList);
                mViewPager.setAdapter(swipepagerAdapter);
                swipepagerAdapter.notifyDataSetChanged();
                //mViewPager.setOffscreenPageLimit(0);
                sliderDotspanel.removeAllViews();

                dotscount = agendaFolderImageList.size();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                try {


                    mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    imgTemp = "1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }


        // Temp
        if (eventDates.size() == 0) {

            Bundle sessionDate = new Bundle();
            sessionDate.putString("sessionDate", "");

            // Create Session Tabs
            agendaTabHost.addTab(
                    agendaTabHost.newTabSpec("Tab 0")
                            .setIndicator(createTabView(agendaTabHost.getContext(), "No Agenda Available", "")),
                    AgendaListFragment.class, sessionDate);

        }


        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            tabWidget.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorselectedtab));
        }

        // set the active tab
        tabWidget.getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorunselectedtab));

//        for (int i = 0; i < agendaTabHost.getTabWidget().getChildCount(); i++) {
//            // mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FF0000")); // unselected
//            tab_text = (LinearLayout) agendaTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_text); //Unselected Tabs
//            tab_text.setBackgroundColor(Color.parseColor("#f15a2b"));
//        }

        agendaTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                JZVideoPlayer.releaseAllVideos();
                for (int i = 0; i < tabWidget.getChildCount(); i++) {
                    tabWidget.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorselectedtab));
                }

                // set the active tab
                tabWidget.getChildAt(agendaTabHost.getCurrentTab()).setBackgroundColor(getResources().getColor(R.color.colorunselectedtab));
//                tab_text = (LinearLayout) agendaTabHost.getTabWidget().getChildAt(i).findViewById(R.id.tab_text); //Unselected Tabs
//                tab_text.setBackgroundColor(Color.parseColor("#80000000"));

                if (agendaFolderImageList.size() > 0) {
                    agendaFolderImageList.clear();
                }
                sliderDotspanel.removeAllViews();

//                for (int i=0;i<agendaDBList.size();i++){

//                }

                for (int i = 0; i < agendaDBList.size(); i++) {


                    String imagedate = agendaDBList.get(i).getFolder_name();
                    String sessionId = agendaDBList.get(i).getSessionId();
                    if (imagedate.equalsIgnoreCase(tabId)) {
                        txtname.setText(agendaDBList.get(i).getSession_name());
                        webView.loadData(agendaDBList.get(i).getSession_description(), "text/html", null);
                        for (int j = 0; j < agendaFolderDBList.size(); j++) {
                            if (sessionId.equalsIgnoreCase(agendaFolderDBList.get(j).getSession_vacation_id()))
                                agendaFolderImageList.add(agendaFolderDBList.get(j));
                        }
                    }

                }

                swipepagerAdapter = new SwipepagerAgendaImage(getActivity(), agendaFolderImageList);
                mViewPager.setAdapter(swipepagerAdapter);
                //mViewPager.setOffscreenPageLimit(0);


                swipepagerAdapter.notifyDataSetChanged();

                try {

                    if (SwipepagerAgendaImage.videoplayer != null) {
                        if (SwipepagerAgendaImage.videoplayer.isCurrentPlay()) {
                            SwipepagerAgendaImage.videoplayer.release();
                        }

                    }
                    SwipepagerAgendaImage.videoplayer.release();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                dotscount = agendaFolderImageList.size();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));



                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.non_active_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.active_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        try {

                            if (SwipepagerAgendaImage.videoplayer != null) {
                                if (SwipepagerAgendaImage.videoplayer.isCurrentPlay()) {
                                    SwipepagerAgendaImage.videoplayer.release();
                                }

                            }
                            SwipepagerAgendaImage.videoplayer.release();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                //Toast.makeText(getActivity(), tabId, Toast.LENGTH_SHORT).show();

            }
        });
    }

	/*@Override
	public void onResume() {
		super.onResume();
		if (cd.isConnectingToInternet()) {

			// Initialize Update Agenda URL
			updateAgendaUrl = constant.WEBSERVICE_URL + constant.WEBSERVICE_FOLDER
					+ constant.INDEPENDENT_AGENDA;

			new GetUpdateAgenda().execute();
		}
	}*/

    // private void setupTab(final View view, final String tag) {
    // View tabview = createTabView(agendaTabHost.getContext(), tag);
    // TabSpec setContent = agendaTabHost.newTabSpec(tag).setIndicator(tabview)
    // .setContent(new TabContentFactory() {
    // public View createTabContent(String tag) {
    // return view;
    // }
    // });
    // agendaTabHost.addTab(setContent);
    // }

    // Detach FragmentTabHost
    @Override
    public void onDetach() {
        super.onDetach();

        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static View createTabView(final Context context, final String text, final String text1) {
        View view = LayoutInflater.from(context).inflate(R.layout.agenda_tabs_bg, null);

        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        TextView tv1 = (TextView) view.findViewById(R.id.tabsecondText);
        tab_text = (LinearLayout) view.findViewById(R.id.tab_text);
        tv.setText(text);
        tv1.setText(text1);


        return view;
    }

    // Remove FragmentTabHost
    @Override
    public void onDestroyView() {

        agendaTabHost = null;
        eventDates.clear();
        eventtempDates.clear();
        super.onDestroyView();

    }

    @Override
    public void onContactSelected(AgendaFolder filtergallerylists) {

    }

//	private class GetUpdateAgenda extends AsyncTask<Void, Void, Void> {
//
//		JSONObject jsonObj = null;
//		String status = "";
//		String statusMsg = "";
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//
//			// Showing Progress Dialog9820325285
//			pDialog = new ProgressDialog(getActivity(), R.style.LoaderTheme);
//			pDialog.setCancelable(false);
//			pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//			pDialog.show();
//		}
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//
//			// Setting Thread Priority
//			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
//
//			// Creating service handler class instance
//			ServiceHandler sh = new ServiceHandler();
//			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
//
//			nameValuePair.add(new BasicNameValuePair("api_access_token", api_access_token));
//
//			nameValuePair.add(new BasicNameValuePair("event_id", eventId));
//
//			// nameValuePair.add(new BasicNameValuePair("password", password));
//
//			// Making a request to url and getting response
//			String jsonStr = sh.makeServiceCall(updateAgendaUrl, ServiceHandler.POST, nameValuePair);
//
//			Log.d("Response: ", "> " + jsonStr);
//
//			if (jsonStr != null) {
//				try {
//
//					jsonObj = new JSONObject(jsonStr);
//					status = jsonObj.getString("status");
//					statusMsg = jsonObj.getString("msg");
//
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//
//			if (status.equalsIgnoreCase("success")) {
//
//				// Agenda Parser
//				agendaParser = new AgendaParser();
//				agendaList = agendaParser.Agenda_Parser(jsonStr);
//
//				//AgendaFolder Parser
//				agendaFolderParser = new AgendaFolderParser();
//				agendaFolderList = agendaFolderParser.Agenda_Folder_Parser(jsonStr);
//
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//
//			// // Dismiss the progress dialog
//			 if (pDialog.isShowing())
//			 pDialog.dismiss();
//
//			// Database Operations
//
//
//			if (status.equalsIgnoreCase("success")) {
//
//				procializeDB.clearAgendaTable();
//				procializeDB.clearAgendaFolderTable();
//
//				procializeDB.insertAgendaData(agendaList, db);
//				procializeDB.insertAgendaFolderData(agendaFolderList,db);
//
//				dbHelper = new DBHelper(getActivity());
//				agendaDBList = new ArrayList<Agenda>();
//				agendaFolderDBList = new ArrayList<AgendaFolder>();
//				agendaDBList = dbHelper.getAgendaList();
//				agendaFolderDBList = dbHelper.getAgendaFolderList();
//
//
//
//			} else if (status.equalsIgnoreCase("error")) {
//
//				if (pDialog != null) {
//					pDialog.dismiss();
//					pDialog = null;
//				}
//
//
//
//			}
//
//		}
//	}

    public void fetchAgenda(String token, String eventid) {
//		progressBar.setVisibility(View.VISIBLE);
        mAPIService.AgendaFetchVacation(token, eventid).enqueue(new Callback<Agenda>() {
            @Override
            public void onResponse(Call<Agenda> call, Response<Agenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//					progressBar.setVisibility(View.GONE);

//					if (agendafeedrefresh.isRefreshing()) {
//						agendafeedrefresh.setRefreshing(false);
//					}
                    showResponse(response);
                } else {
//					progressBar.setVisibility(View.GONE);
//					if (agendafeedrefresh.isRefreshing()) {
//						agendafeedrefresh.setRefreshing(false);
//					}
                    Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//				progressBar.setVisibility(View.GONE);
//				if (agendafeedrefresh.isRefreshing()) {
//					agendafeedrefresh.setRefreshing(false);
//				}
                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showResponse(Response<Agenda> response) {

        procializeDB.clearAgendaVacationTable();
        procializeDB.clearAgendaFolerTable();

        agendaList = response.body().getAgenda_vacation_list();
        agendaFolderList = response.body().getAgenda_vacation_media_list();
        procializeDB.insertAgendaVacationInfo(agendaList, db);
        procializeDB.insertAgendaMediaInfo(agendaFolderList, db);

        dbHelper = new DBHelper(getActivity());
        agendaDBList = new ArrayList<AgendaVacationList>();
        agendaFolderDBList = new ArrayList<AgendaMediaList>();
        agendaDBList = dbHelper.getAgendaFolderList();
        agendaFolderDBList = dbHelper.getAgendaMediaList();

        try {
            initview(response);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}
