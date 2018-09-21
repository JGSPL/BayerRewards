package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.singleevent.Fragments.AgendaFolderListFragment;
import com.procialize.singleevent.Fragments.AgendaListFragment;
import com.procialize.singleevent.GetterSetter.Agenda;
import com.procialize.singleevent.GetterSetter.AgendaFolder;
import com.procialize.singleevent.GetterSetter.AgendaMediaList;
import com.procialize.singleevent.GetterSetter.AgendaVacationList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgendaVacationActivity extends AppCompatActivity implements SwipeAgendaImageAdapter.SwipeAgendaImageAdapterListner {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_vacation);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(AgendaVacationActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        procializeDB = new DBHelper(AgendaVacationActivity.this);
        db = procializeDB.getReadableDatabase();
//
//		// Connection Detector Reference
        cd = new ConnectionDetector(getApplicationContext());


        constant = new ApiConstant();
        handler = new Handler();
        session = new SessionManager(getApplicationContext());
//		api_access_token = session.getAccessToken();
//		eventId = session.getEventId();
        imgTemp = "0";

        // Create FragmentTabHost
        //agendaTabHost = new FragmentTabHost(getActivity());
        agendaTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabWidget = (TabWidget) findViewById(android.R.id.tabs);
        agendaTabHost.setup(AgendaVacationActivity.this, getSupportFragmentManager(), android.R.id.tabcontent);
        txtname = (TextView) findViewById(R.id.txtname);
        webView = (WebView) findViewById(R.id.webView);

        // // Locate fragment1.xml to create FragmentTabHost
        //agendaTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabcontent);


        if (cd.isConnectingToInternet()) {

            // Initialize Update Agenda URL
            updateAgendaUrl = constant.INDEPENDENT_AGENDA;

            fetchAgenda(token, eventid);
        }

    }

    @Override
    public void onContactSelected(AgendaFolder filtergallerylists) {

    }

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
                    Toast.makeText(AgendaVacationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Agenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//				progressBar.setVisibility(View.GONE);
//				if (agendafeedrefresh.isRefreshing()) {
//					agendafeedrefresh.setRefreshing(false);
//				}
                Toast.makeText(AgendaVacationActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

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

        dbHelper = new DBHelper(AgendaVacationActivity.this);
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


                swipepagerAdapter = new SwipepagerAgendaImage(AgendaVacationActivity.this, agendaFolderImageList);
                mViewPager.setAdapter(swipepagerAdapter);
                swipepagerAdapter.notifyDataSetChanged();
                //mViewPager.setOffscreenPageLimit(0);
                sliderDotspanel.removeAllViews();

                dotscount = agendaFolderImageList.size();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(AgendaVacationActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.active_dot));

                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.non_active_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.active_dot));

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                imgTemp = "1";

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

                swipepagerAdapter = new SwipepagerAgendaImage(AgendaVacationActivity.this, agendaFolderImageList);
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

                    dots[i] = new ImageView(AgendaVacationActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.non_active_dot));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.active_dot));

                mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                        for (int i = 0; i < dotscount; i++) {
                            dots[i].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.non_active_dot));
                        }

                        dots[position].setImageDrawable(ContextCompat.getDrawable(AgendaVacationActivity.this, R.drawable.active_dot));

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

    private static View createTabView(final Context context, final String text, final String text1) {
        View view = LayoutInflater.from(context).inflate(R.layout.agenda_tabs_bg, null);

        TextView tv = (TextView) view.findViewById(R.id.tabsText);
        TextView tv1 = (TextView) view.findViewById(R.id.tabsecondText);
        tab_text = (LinearLayout) view.findViewById(R.id.tab_text);
        tv.setText(text);
        tv1.setText(text1);


        return view;
    }
}
