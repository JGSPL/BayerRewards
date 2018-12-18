package com.procialize.singleevent.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Adapter.AgendaAdapter;
import com.procialize.singleevent.Adapter.CustomMenuAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.CustomTools.CustomViewPager;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.EmptyViewActivity;
import com.procialize.singleevent.Fragments.AgendaFolderFragment;
import com.procialize.singleevent.Fragments.AgendaFragment;
import com.procialize.singleevent.Fragments.AttendeeFragment;

import com.procialize.singleevent.Fragments.GeneralInfo;
import com.procialize.singleevent.Fragments.SpeakerFragment;
import com.procialize.singleevent.Fragments.WallFragment_POST;
import com.procialize.singleevent.GetterSetter.AgendaList;
import com.procialize.singleevent.GetterSetter.EventMenuSettingList;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.FetchAgenda;
import com.procialize.singleevent.InnerDrawerActivity.AgendaActivity;
import com.procialize.singleevent.InnerDrawerActivity.AgendaVacationActivity;
import com.procialize.singleevent.InnerDrawerActivity.AttendeeActivity;
import com.procialize.singleevent.InnerDrawerActivity.DocumentsActivity;
import com.procialize.singleevent.InnerDrawerActivity.EngagementActivity;
import com.procialize.singleevent.InnerDrawerActivity.EventInfoActivity;

import com.procialize.singleevent.InnerDrawerActivity.FeedBackActivity;
import com.procialize.singleevent.InnerDrawerActivity.FolderQuizActivity;
import com.procialize.singleevent.InnerDrawerActivity.GalleryActivity;
import com.procialize.singleevent.InnerDrawerActivity.GeneralInfoActivity;
import com.procialize.singleevent.InnerDrawerActivity.LivePollActivity;
import com.procialize.singleevent.InnerDrawerActivity.MyTravelActivity;
import com.procialize.singleevent.InnerDrawerActivity.NotificationActivity;
import com.procialize.singleevent.InnerDrawerActivity.QAAttendeeActivity;
import com.procialize.singleevent.InnerDrawerActivity.QADirectActivity;
import com.procialize.singleevent.InnerDrawerActivity.QASpeakerActivity;
import com.procialize.singleevent.InnerDrawerActivity.QRGeneratorActivity;
import com.procialize.singleevent.InnerDrawerActivity.QRScanActivity;
import com.procialize.singleevent.InnerDrawerActivity.QuizActivity;
import com.procialize.singleevent.InnerDrawerActivity.SpeakerActivity;
import com.procialize.singleevent.InnerDrawerActivity.VideoActivity;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Res;
import com.procialize.singleevent.Utility.Util;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


//preet

public class HomeActivity extends AppCompatActivity implements CustomMenuAdapter.CustomMenuAdapterListner {

    RecyclerView menurecycler;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    SessionManager session;
    List<EventSettingList> eventSettingLists;
    List<EventMenuSettingList> eventMenuSettingLists;
    HashMap<String, String> eventlist;
    ImageView headerlogoIv;
    private APIService mAPIService;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    String side_menu = "0", side_menu_my_travel = "0", side_menu_notification = "0", side_menu_display_qr = "0", side_menu_qr_scanner = "0",
            side_menu_quiz = "0", side_menu_live_poll = "0", side_menu_survey = "0",
            side_menu_feedback = "0", side_menu_gallery_video = "0", gallery_video_native = "0", gallery_video_youtube = "0",
            side_menu_image_gallery = "0", selfie_contest = "0", video_contest = "0",
            side_menu_event_info = "0", side_menu_document = "0", side_menu_engagement = "0",
            engagement_selfie_contest = "0", engagement_video_contest = "0",
            news_feed_video = "0", QA_speaker = "0", QA_direct = "0", QA_session = "0", side_menu_attendee = "0", side_menu_speaker = "0", side_menu_agenda = "0",
            side_menu_general_info = "0", edit_profile_company = "0", edit_profile_designation = "0", agenda_conference = "0", agenda_vacation = "0",
            side_menu_contact = "0", side_menu_email = "0", side_menu_leaderboard = "0";
    String news_feed = "0", attendee = "0", speaker = "0", agenda = "0", edit_profile = "0", general_ifo = "0";
    private List<AgendaList> agendaList;
    private List<AgendaList> agendaDBList;
    private List<AgendaList> tempagendaList = new ArrayList<AgendaList>();
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_EVENT = "EventId";
    String eventid, token;
    String email, password;
    CustomMenuAdapter customMenuAdapter;
    TextView logout, home, contactus, eventname, switchbt, eula, privacy_policy;
    String eventnamestr;
    public static final int RequestPermissionCode = 8;
    public static String logoImg = "", colorActive = "";
    public static int activetab;
    private DBHelper procializeDB;

    private int[] tabIcons = {
            R.drawable.ic_newsfeed,
            R.drawable.ic_agenda,
            R.drawable.ic_attendee,
            R.drawable.ic_speaker,
            R.drawable.general_info
    };

    private Res res;
    private DBHelper dbHelper;

    @Override
    public Resources getResources() {
        if (res == null) {
            res = new Res(super.getResources());
        }
        return res;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }

        initializeView();
    }

    private void initializeView() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user1 = sessionManager.getUserDetails();

        // token
        token = user1.get(SessionManager.KEY_TOKEN);
        activetab = getResources().getColor(R.color.activetab);
        activetab = Color.parseColor(colorActive);
        //        SharedPreferences.Editor editor = getSharedPreferences(MY_EVENT, MODE_PRIVATE).edit();
//        editor.putString("eventid",eventid);
//        editor.apply();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE).edit();
        editor.putString("loginfirst", "1");
        editor.apply();

//        Intent intent = getIntent();
//        eventid = intent.getStringExtra("eventId");
//        eventnamestr = intent.getStringExtra("eventnamestr");


//        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        editor.putString("eventid", eventid);
//        editor.putString("eventnamestr", eventnamestr);
//        editor.putString("loginfirst", "1");
//        editor.apply();

        mAPIService = ApiUtils.getAPIService();
        session = new SessionManager(getApplicationContext());
        eventSettingLists = new ArrayList<>();
        eventMenuSettingLists = new ArrayList<>();
        cd = new ConnectionDetector(this);
        dbHelper = new DBHelper(this);

        procializeDB = new DBHelper(this);
        db = procializeDB.getWritableDatabase();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        email = user.get(SessionManager.KEY_EMAIL);
        password = user.get(SessionManager.KEY_PASSWORD);


        if (session != null) {
            eventSettingLists = session.loadEventList();
            eventMenuSettingLists = session.loadMenuEventList();
            Setting(eventSettingLists);
        }

    }

    private void afterSettingView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.setTabTextColors(Color.parseColor("#4D4D4D"), Color.parseColor(colorActive));


        try {

            int i = tabLayout.getTabCount();

            if (i == 4) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//               tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//               tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//               tabLayout.getTabAt(3).setIcon(tabIcons[3]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(3).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        if (tab.getText().equals("Agenda")) {
                            if (cd.isConnectingToInternet()) {

                                fetchAgenda(token, eventid);
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 3) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//               tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//               tabLayout.getTabAt(2).setIcon(tabIcons[2]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        if (tab.getText().equals("Agenda")) {
                            if (cd.isConnectingToInternet()) {

                                fetchAgenda(token, eventid);
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 2) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//               tabLayout.getTabAt(1).setIcon(tabIcons[1]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
                tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#4D4D4D"), PorterDuff.Mode.SRC_IN);


                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        if (tab.getText().equals("Agenda")) {
                            if (cd.isConnectingToInternet()) {

                                fetchAgenda(token, eventid);
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                        String string1 = "#4D4D4D";
                        int color1 = Color.parseColor(string1);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
                        tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else if (i == 1) {
//               tabLayout.getTabAt(0).setIcon(tabIcons[0]);


                tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        JZVideoPlayerStandard.releaseAllVideos();
                        String string = colorActive;
                        int color = Color.parseColor(string);

// int tabIconColor = ContextCompat.getColor(HomeActivity.this, color); //tabselected color
                        tab.getIcon().setColorFilter(color, PorterDuff.Mode.SRC_IN);

                        if (tab.getText().equals("Agenda")) {
                            if (cd.isConnectingToInternet()) {

                                fetchAgenda(token, eventid);
                            }
                        }
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

//                       String string1 = "#7898a9";
//                       int color1 = Color.parseColor(string1);
//
//// int tabIconColor = ContextCompat.getColor(HomeActivity.this,color1);//tabunselected color
//                       tab.getIcon().setColorFilter(color1, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menurecycler = navigationView.findViewById(R.id.menurecycler);
        logout = navigationView.findViewById(R.id.logout);
        home = navigationView.findViewById(R.id.home);
        contactus = navigationView.findViewById(R.id.contactus);
        switchbt = navigationView.findViewById(R.id.switchbt);
        privacy_policy = navigationView.findViewById(R.id.privacy_policy);
        eula = navigationView.findViewById(R.id.eula);

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                JZVideoPlayerStandard.releaseAllVideos();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();
                SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                pref.clear();

//                SharedPreferences settings = getSharedPreferences(MY_PREFS_LOGIN, 0);
//                if (settings.contains("loginfirst")) {
//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.remove("loginfirst");
//                    editor.apply();
//                }
                Intent main = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(main);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(main);
                finish();
            }
        });


        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(main);

            }
        });

        eula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent main = new Intent(getApplicationContext(), EULAActivity.class);
                startActivity(main);


            }
        });

        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), PrivacyPolicy.class);
                startActivity(main);

            }
        });

        switchbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                if (prefs.contains("eventnamestr")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("eventnamestr");
                    editor.commit();
                }
                if (prefs.contains("eventid")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("eventid");
                    editor.commit();
                }

                // SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                // pref.clear();


                session.logoutUser();


                Intent main = new Intent(getApplicationContext(), EventChooserActivity.class);
                main.putExtra("email", email);
                main.putExtra("password", password);
                startActivity(main);
                finish();
            }
        });

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        //  menurecycler.setLayoutAnimation(animation);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        menurecycler.setLayoutManager(mLayoutManager);


        List<EventMenuSettingList> anotherList = new ArrayList<EventMenuSettingList>();
        anotherList.addAll(eventMenuSettingLists);


        if (eventMenuSettingLists != null) {
            customMenuAdapter = new CustomMenuAdapter(this, eventMenuSettingLists, this, side_menu_agenda);
            menurecycler.setAdapter(customMenuAdapter);
            customMenuAdapter.notifyDataSetChanged();
        }


        profiledetails();


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
//                profiledetails();
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        if (side_menu.equalsIgnoreCase("0")) {
            navigationView.setVisibility(View.GONE);
        } else {
            navigationView.setVisibility(View.VISIBLE);
        }

    }


    public void profiledetails() {

        View header = navigationView.getHeaderView(0);
        RelativeLayout outer = (RelativeLayout) findViewById(R.id.my);
        RelativeLayout headerRel = (RelativeLayout) outer.findViewById(R.id.relbelo);

        TextView nameTv = (TextView) outer.findViewById(R.id.nameTv);
        TextView lastNameTv = (TextView) outer.findViewById(R.id.lastNameTv);
        TextView designationTv = (TextView) outer.findViewById(R.id.designationTv);
        TextView compantyTv = (TextView) outer.findViewById(R.id.compantyTv);
        final ImageView profileIV = (ImageView) outer.findViewById(R.id.profileIV);
        final ProgressBar progressView = (ProgressBar) outer.findViewById(R.id.progressView);

        eventname = outer.findViewById(R.id.eventname);
        eventname.setTextColor(Color.parseColor(colorActive));
        headerRel.setBackgroundColor(Color.parseColor(colorActive));
        outer.setBackgroundColor(Color.parseColor(colorActive));

        eventname.setText(eventnamestr);


        ImageView editIv = outer.findViewById(R.id.editIv);


//        if (edit_profile.equalsIgnoreCase("1")) {
//            editIv.setVisibility(View.VISIBLE);
//
//        } else {
//
//            editIv.setVisibility(View.GONE);
//
//        }


        editIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                SharedPreferences mypref = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
//                String event_id = mypref.getString("eventid", "");
//                String eventnamestr = mypref.getString("eventnamestr", "");

                Intent Profile = new Intent(getApplicationContext(), ProfileActivity.class);
//                Profile.putExtra("eventId", event_id);
//                Profile.putExtra("eventnamestr", eventnamestr);
                startActivity(Profile);
                finish();
            }
        });


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);
        String lname = user.get(SessionManager.KEY_LNAME);

        // designation
        String designation = user.get(SessionManager.KEY_DESIGNATION);

        // company
        String company = user.get(SessionManager.KEY_COMPANY);

        //profilepic
        String profilepic = user.get(SessionManager.KEY_PIC);

        if (edit_profile_company.equalsIgnoreCase("0")) {
//            compantyTv.setVisibility(View.GONE);
            compantyTv.setText("");
        } else {
            compantyTv.setVisibility(View.VISIBLE);
            compantyTv.setText(company);
        }

        if (edit_profile_designation.equalsIgnoreCase("0")) {
//            designationTv.setVisibility(View.GONE);
            designationTv.setText("");
        } else {
            designationTv.setVisibility(View.VISIBLE);
            designationTv.setText(designation);
        }


        nameTv.setText(name + " " + lname);
//        lastNameTv.setText(lname);


        if (profilepic != null) {
            Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV).onLoadStarted(getDrawable(R.drawable.profilepic_placeholder));
        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);
            progressView.setVisibility(View.GONE);
        }

    }

    private void setupTabIcons() {


        if (tabLayout.getTabAt(0) != null) {
            if (tabLayout.getTabAt(0).getText().equals("News Feed")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(0).getText().equals("Agenda")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(0).getText().equals("Attendees")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(0).getText().equals("Speakers")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(0).getText().equals("General Info")) {
                tabLayout.getTabAt(0).setIcon(tabIcons[4]);
            }
        }


        if (tabLayout.getTabAt(1) != null) {
            if (tabLayout.getTabAt(1).getText().equals("News Feed")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(1).getText().equals("Agenda")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(1).getText().equals("Attendees")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(1).getText().equals("Speakers")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(1).getText().equals("General Info")) {
                tabLayout.getTabAt(1).setIcon(tabIcons[4]);
            }
        }


        if (tabLayout.getTabAt(2) != null) {
            if (tabLayout.getTabAt(2).getText().equals("News Feed")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(2).getText().equals("Agenda")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(2).getText().equals("Attendees")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(2).getText().equals("Speakers")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(2).getText().equals("General Info")) {
                tabLayout.getTabAt(2).setIcon(tabIcons[4]);
            }
        }


        if (tabLayout.getTabAt(3) != null) {
            if (tabLayout.getTabAt(3).getText().equals("News Feed")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[0]);
            } else if (tabLayout.getTabAt(3).getText().equals("Agenda")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[1]);
            } else if (tabLayout.getTabAt(3).getText().equals("Attendees")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[2]);
            } else if (tabLayout.getTabAt(3).getText().equals("Speakers")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[3]);
            } else if (tabLayout.getTabAt(3).getText().equals("General Info")) {
                tabLayout.getTabAt(3).setIcon(tabIcons[4]);
            }
        }

//
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            //noinspection ConstantConditions
//            TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            ImageView imageView= (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab,null);
//            if (i==0)
//            {
//                tv.setText("News Feed");
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
//            }else if (i==1)
//            {
//                tv.setText("Agenda");
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_newsfeed) );
//            }else if (i==2)
//            {
//                tv.setText("Attendees");
//
//            }else if (i==3)
//            {
//                tv.setText("Speakers");
//            }
//            tabLayout.getTabAt(i).setCustomView(tv);
//
//        }
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (news_feed.equalsIgnoreCase("1")) {
            adapter.addFragment(new WallFragment_POST(), "News Feed");
        }
        if (agenda.equalsIgnoreCase("1")) {
            if (agenda_conference.equalsIgnoreCase("1")) {
                adapter.addFragment(new AgendaFragment(), "Agenda");
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                adapter.addFragment(new AgendaFolderFragment(), "Agenda");
            }

        }
        if (attendee.equalsIgnoreCase("1")) {
            adapter.addFragment(new AttendeeFragment(), "Attendees");
        }
        if (speaker.equalsIgnoreCase("1")) {
            adapter.addFragment(new SpeakerFragment(), "Speakers");
        }
        if (general_ifo.equalsIgnoreCase("1")) {
            adapter.addFragment(new GeneralInfo(), "General Info");
        }
        if (news_feed.equalsIgnoreCase("1") && agenda.equalsIgnoreCase("0") &&
                attendee.equalsIgnoreCase("0") && speaker.equalsIgnoreCase("0") &&
                general_ifo.equalsIgnoreCase("0")) {
            adapter.addFragment(new WallFragment_POST(), "News Feed");
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            AppBarLayout appTab = findViewById(R.id.appTab);
//            appTab.setElevation(0);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    10.0f
            );

            viewPager.setLayoutParams(param);
            appTab.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);


        }
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//        profiledetails();
        super.onResume();
    }

    private void Setting(List<EventSettingList> eventSettingLists) {

        if (eventSettingLists.size() != 0) {
            for (int i = 0; i < eventSettingLists.size(); i++) {
                if (eventSettingLists.get(i).getFieldName().equals("side_menu")) {
                    side_menu = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_my_travel")) {
                    side_menu_my_travel = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_notification")) {
                    side_menu_notification = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_display_qr")) {
                    side_menu_display_qr = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_qr_scanner")) {
                    side_menu_qr_scanner = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_quiz")) {
                    side_menu_quiz = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_live_poll")) {
                    side_menu_live_poll = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_survey")) {
                    side_menu_survey = eventSettingLists.get(i).getFieldValue();

                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_feedback")) {
                    side_menu_feedback = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_gallery_video")) {
                    side_menu_gallery_video = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("gallery_video_native")) {
                    gallery_video_native = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("gallery_video_youtube")) {
                    gallery_video_youtube = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_image_gallery")) {
                    side_menu_image_gallery = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("selfie_contest")) {
                    selfie_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("video_contest")) {
                    video_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_document")) {
                    side_menu_document = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("news_feed_video")) {
                    news_feed_video = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("news_feed")) {
                    news_feed = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_attendee")) {
                    attendee = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_speaker")) {
                    speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_agenda")) {
                    agenda = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile")) {
                    edit_profile = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("main_tab_gen_info")) {
                    general_ifo = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_session")) {
                    QA_session = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_speaker")) {
                    QA_speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("side_menu_engagement")) {
                    side_menu_engagement = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("engagement_selfie_contest")) {
                    engagement_selfie_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("engagement_video_contest")) {
                    engagement_video_contest = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_event_info")) {
                    side_menu_event_info = eventSettingLists.get(i).getFieldValue();
                }/* else if (eventSettingLists.get(i).getFieldName().equals("side_menu_agenda")) {
                    side_menu_agenda = eventSettingLists.get(i).getFieldValue();
                } */ else if (eventSettingLists.get(i).getFieldName().equals("side_menu_attendee")) {
                    side_menu_attendee = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_speaker")) {
                    side_menu_speaker = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile_designation")) {
                    edit_profile_designation = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("edit_profile_company")) {
                    edit_profile_company = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_direct_question")) {
                    QA_direct = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("agenda_conference")) {
                    agenda_conference = eventSettingLists.get(i).getFieldValue();
                    if (agenda_conference.equalsIgnoreCase("1")) {
                        side_menu_agenda = "1";
                    }
                    ////

                } else if (eventSettingLists.get(i).getFieldName().equals("agenda_vacation")) {
                    agenda_vacation = eventSettingLists.get(i).getFieldValue();
                    if (agenda_vacation.equalsIgnoreCase("1")) {
                        side_menu_agenda = "1";
                    }


                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_contact_us")) {
                    side_menu_contact = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_email_template")) {
                    side_menu_email = eventSettingLists.get(i).getFieldValue();
                } else if (eventSettingLists.get(i).getFieldName().equals("side_menu_leaderboard")) {
                    side_menu_leaderboard = eventSettingLists.get(i).getFieldValue();
                }

            }
        }

        afterSettingView();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to exit?");
        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        finish();
                    }
                });
        builder.show();
    }


    @Override
    public void onContactSelected(EventMenuSettingList menuSettingList) {

        if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_my_travel")) {
            Intent selfie = new Intent(this, MyTravelActivity.class);
            startActivity(selfie);


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_notification")) {
            Intent notify = new Intent(this, NotificationActivity.class);
            startActivity(notify);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_display_qr")) {
            Intent qrgenerate = new Intent(this, QRGeneratorActivity.class);
            startActivity(qrgenerate);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_qr_scanner")) {
            Intent scanqr = new Intent(this, QRScanActivity.class);
            startActivity(scanqr);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_quiz")) {
            Intent quiz = new Intent(this, FolderQuizActivity.class);
            startActivity(quiz);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_live_poll")) {
            Intent livepol = new Intent(this, LivePollActivity.class);
            startActivity(livepol);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_survey")) {
            Intent feedback = new Intent(this, FeedBackActivity.class);
            startActivity(feedback);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_feedback")) {
//            Intent feedback = new Intent(this, FeedBackActivity.class);
////            startActivity(feedback);
            Toast.makeText(HomeActivity.this, "Comming Soon...", Toast.LENGTH_SHORT).show();

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gallery_video")) {
            Intent video = new Intent(this, VideoActivity.class);
            startActivity(video);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_image_gallery")) {
            Intent gallery = new Intent(this, GalleryActivity.class);
            startActivity(gallery);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_document")) {

            Intent doc = new Intent(this, DocumentsActivity.class);
            startActivity(doc);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_event_info")) {
            Intent event = new Intent(this, EventInfoActivity.class);
            startActivity(event);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_q&a")) {
            if (QA_session.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QAAttendeeActivity.class);
                startActivity(event);
            } else if (QA_speaker.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QASpeakerActivity.class);
                startActivity(event);
            } else if (QA_direct.equalsIgnoreCase("1")) {
                Intent event = new Intent(this, QADirectActivity.class);
                startActivity(event);
            } else {

                Intent intent = new Intent(HomeActivity.this, EmptyViewActivity.class);
                startActivity(intent);
//                setContentView(R.layout.activity_empty_view);
//                ImageView imageView = findViewById(R.id.back);
//                TextView text_empty = findViewById(R.id.text_empty);
//
//                final ImageView headerlogoIv1 = findViewById(R.id.headerlogoIv);
//                Glide.with(this).load("http://www.procialize.info/uploads/app_logo/" + logoImg).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        headerlogoIv1.setImageResource(R.drawable.splashlogo);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                }).into(headerlogoIv1);
//                text_empty.setText("No Data Found");
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
//                        startActivity(intent);
//                    }
//                });
            }


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_engagement")) {
            Intent engagement = new Intent(this, EngagementActivity.class);
            startActivity(engagement);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_attendee")) {
            Intent attendee = new Intent(this, AttendeeActivity.class);
            startActivity(attendee);

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_speaker")) {
            Intent speaker = new Intent(this, SpeakerActivity.class);
            startActivity(speaker);

        } /*else if (menuSettingList.getFieldName().equalsIgnoreCase("agenda_conference") ||menuSettingList.getFieldName().equalsIgnoreCase("agenda_vacation")) {

            if (agenda_conference.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaActivity.class);
                startActivity(agenda);
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaVacationActivity.class);
                startActivity(agenda);
            }


        }*/ else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_agenda")) {

            if (agenda_conference.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaActivity.class);
                startActivity(agenda);
            } else if (agenda_vacation.equalsIgnoreCase("1")) {
                Intent agenda = new Intent(this, AgendaVacationActivity.class);
                startActivity(agenda);
            }


        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_gen_info")) {
            Intent generalinfo = new Intent(this, GeneralInfoActivity.class);
            startActivity(generalinfo);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_contact_us")) {
            Intent generalinfo = new Intent(this, WebViewActivity.class);
            startActivity(generalinfo);
        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_email_template")) {
            Toast.makeText(HomeActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();

        } else if (menuSettingList.getFieldName().equalsIgnoreCase("side_menu_leaderboard")) {
            Toast.makeText(HomeActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show();

        }


    }


    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_CONTACTS,
                        READ_CONTACTS,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        VIBRATE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readcontactpermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writecontactpermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readstoragepermjission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragepermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean vibratepermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && readcontactpermission && writecontactpermission && readstoragepermjission && writestoragepermission && vibratepermission) {

//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(HomeActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }


    public void fetchAgenda(String token, String eventid) {

        mAPIService.AgendaFetchPost(token, eventid).enqueue(new Callback<FetchAgenda>() {
            @Override
            public void onResponse(Call<FetchAgenda> call, Response<FetchAgenda> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    showResponse(response);
                } else {

                    // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAgenda> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");

                // Toast.makeText(getContext(),"Unable to process",Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void showResponse(Response<FetchAgenda> response) {
        try {
            String date = "";
            for (int i = 0; i < response.body().getAgendaList().size(); i++) {
                if (response.body().getAgendaList().get(i).getSessionDate().equalsIgnoreCase(date)) {
                    date = response.body().getAgendaList().get(i).getSessionDate();
                    tempagendaList.add(response.body().getAgendaList().get(i));
                } else {
                    date = response.body().getAgendaList().get(i).getSessionDate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        agendaList = response.body().getAgendaList();
        procializeDB.clearAgendaTable();
        procializeDB.insertAgendaInfo(agendaList, db);


    }

}
