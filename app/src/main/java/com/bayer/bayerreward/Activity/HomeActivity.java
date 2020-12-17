package com.bayer.bayerreward.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.bayer.bayerreward.Adapter.CustomMenuAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.BuildConfig;
import com.bayer.bayerreward.CustomTools.CustomViewPager;
import com.bayer.bayerreward.CustomTools.MyJZVideoPlayerStandard;
import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.EmptyViewActivity;
import com.bayer.bayerreward.Fragments.LeaderboardFragment;
import com.bayer.bayerreward.Fragments.MpinScanner;
import com.bayer.bayerreward.Fragments.MyPreferenceFragment;
import com.bayer.bayerreward.Fragments.WallFragment_POST;
import com.bayer.bayerreward.GetterSetter.AgendaList;
import com.bayer.bayerreward.GetterSetter.EventMenuSettingList;
import com.bayer.bayerreward.GetterSetter.EventSettingList;
import com.bayer.bayerreward.GetterSetter.FetchAgenda;
import com.bayer.bayerreward.InnerDrawerActivity.AgendaActivity;
import com.bayer.bayerreward.InnerDrawerActivity.AgendaVacationActivity;
import com.bayer.bayerreward.InnerDrawerActivity.AttendeeActivity;
import com.bayer.bayerreward.InnerDrawerActivity.DocumentsActivity;
import com.bayer.bayerreward.InnerDrawerActivity.EngagementActivity;
import com.bayer.bayerreward.InnerDrawerActivity.EventInfoActivity;
import com.bayer.bayerreward.InnerDrawerActivity.FeedBackActivity;
import com.bayer.bayerreward.InnerDrawerActivity.FolderQuizActivity;
import com.bayer.bayerreward.InnerDrawerActivity.GalleryActivity;
import com.bayer.bayerreward.InnerDrawerActivity.GeneralInfoActivity;
import com.bayer.bayerreward.InnerDrawerActivity.LivePollActivity;
import com.bayer.bayerreward.InnerDrawerActivity.MyTravelActivity;
import com.bayer.bayerreward.InnerDrawerActivity.NotificationActivity;
import com.bayer.bayerreward.InnerDrawerActivity.QAAttendeeActivity;
import com.bayer.bayerreward.InnerDrawerActivity.QADirectActivity;
import com.bayer.bayerreward.InnerDrawerActivity.QASpeakerActivity;
import com.bayer.bayerreward.InnerDrawerActivity.QRGeneratorActivity;
import com.bayer.bayerreward.InnerDrawerActivity.QRScanActivity;
import com.bayer.bayerreward.InnerDrawerActivity.SpeakerActivity;
import com.bayer.bayerreward.InnerDrawerActivity.TermsandCondition;
import com.bayer.bayerreward.InnerDrawerActivity.VideoActivity;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Res;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;
import static org.apache.http.HttpVersion.HTTP_1_1;

public class HomeActivity extends AppCompatActivity implements CustomMenuAdapter.CustomMenuAdapterListner {

    public static final int RequestPermissionCode = 8;
    public static String logoImg = "", colorActive = "", eventback = "";
    public static int activetab;
    RecyclerView menurecycler;
    SessionManager session;
    List<EventSettingList> eventSettingLists;
    List<EventMenuSettingList> eventMenuSettingLists;
    HashMap<String, String> eventlist;
    ImageView headerlogoIv;
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
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String MY_EVENT = "EventId";
    String eventid, token;
    String email, password;
    CustomMenuAdapter customMenuAdapter;
    TextView txt_vrsion, logout, home, contactus, eventname, switchbt, chatbt, eula, privacy_policy, eventInfo, termsandcondition, qna, engangement, notification, demo_plot_form, nearbydemo, scheme_offer, mpointcalc;
    String eventnamestr;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private APIService mAPIService;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AgendaList> agendaList;
    private List<AgendaList> agendaDBList;
    private List<AgendaList> tempagendaList = new ArrayList<AgendaList>();
    private DBHelper procializeDB;
    String MY_PREFS_LEADER = "ProcializeLeader";
    String MY_PREFS_RANK = "rank";
    String MY_TOTAL_POINTS = "totalPoints";
    LinearLayout linear;
    String imgname;
    private Res res;
    private DBHelper dbHelper;
    public static int flag = 0;
    public String postUrl;
    String app_version;


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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (CheckingPermissionIsEnabledOrNot()) {
            // Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }

        drawerLayout = findViewById(R.id.drawer);

        initializeView();
        postUrl = ApiConstant.baseUrl + "forceUpdate/android";
        app_version = BuildConfig.VERSION_NAME;

        CommonFunction.crashlytics("Home", "");
        firbaseAnalytics(this, "Home", "");
        if (cd.isConnectingToInternet()) {
            new SubmitPostTask().execute();
        } else {
            Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void initializeView() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");
        eventback = prefs.getString("eventback", "");
        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user1 = sessionManager.getUserDetails();
        linear = findViewById(R.id.linear);
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

        imgname = "background";//url.substring(58, 60);

        PicassoTrustAll.getInstance(HomeActivity.this)
                .load(ApiConstant.eventpic + eventback)
                .into(new com.squareup.picasso.Target() {
                          @Override
                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                              try {
                                  String root = Environment.getExternalStorageDirectory().toString();
                                  File myDir = new File(root + "/Procialize");

                                  if (!myDir.exists()) {
                                      myDir.mkdirs();
                                  }

                                  String name = imgname + ".jpg";
                                  myDir = new File(myDir, name);
                                  FileOutputStream out = new FileOutputStream(myDir);
                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                  out.flush();
                                  out.close();
                              } catch (Exception e) {
                                  // some action
                              }
                          }

                          @Override
                          public void onBitmapFailed(Drawable errorDrawable) {
                          }

                          @Override
                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                          }
                      }
                );


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
            eventSettingLists = SessionManager.loadEventList();
            eventMenuSettingLists = SessionManager.loadMenuEventList();
            Setting(eventSettingLists);
        }

    }

    private void afterSettingView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);

        Util.logomethod(this, headerlogoIv);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);
        viewPager.setPagingEnabled(false);
        setupViewPager(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1) {
                    new MyPreferenceFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if (flag == 0) {
            viewPager.setCurrentItem(0);
        } else if (flag == 1) {
            viewPager.setCurrentItem(2);
            flag = 0;
        } else {
            viewPager.setCurrentItem(0);
        }
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();


        navigationView = findViewById(R.id.navigation_view);
        menurecycler = navigationView.findViewById(R.id.menurecycler);
        logout = navigationView.findViewById(R.id.logout);
        home = navigationView.findViewById(R.id.home);
        contactus = navigationView.findViewById(R.id.contactus);
        switchbt = navigationView.findViewById(R.id.switchbt);
        chatbt = navigationView.findViewById(R.id.chatbt);
        privacy_policy = navigationView.findViewById(R.id.privacy_policy);
        eventInfo = navigationView.findViewById(R.id.eventInfo);
        notification = navigationView.findViewById(R.id.notification);
        demo_plot_form = navigationView.findViewById(R.id.demo_plot_form);
        nearbydemo = navigationView.findViewById(R.id.nearbydemo);
        scheme_offer = navigationView.findViewById(R.id.scheme_offer);
        mpointcalc = navigationView.findViewById(R.id.mpointcalc);
        engangement = navigationView.findViewById(R.id.engangement);
        qna = navigationView.findViewById(R.id.qna);
        termsandcondition = navigationView.findViewById(R.id.termsandcondition);
        txt_vrsion = navigationView.findViewById(R.id.txt_vrsion);

        eula = navigationView.findViewById(R.id.eula);


        txt_vrsion.setText("Version : " + BuildConfig.VERSION_NAME);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session.logoutUser();
                SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                pref.clear();

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_LEADER, MODE_PRIVATE);
                if (prefs.contains("firsttime")) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove("firsttime");
                    editor.commit();
                }

                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_RANK, MODE_PRIVATE);
                if (prefs1.contains("rankregion")) {
                    SharedPreferences.Editor editor = prefs1.edit();
                    editor.remove("rankregion");
                    editor.commit();
                }

                SharedPreferences prefs2 = getSharedPreferences(MY_TOTAL_POINTS, MODE_PRIVATE);
                if (prefs2.contains("rank")) {
                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.remove("rank");
                    editor.commit();
                }
                if (prefs2.contains("date")) {
                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.remove("date");
                    editor.commit();
                }
                if (prefs2.contains("totalpoint")) {
                    SharedPreferences.Editor editor = prefs2.edit();
                    editor.remove("totalpoint");
                    editor.commit();
                }

                try {
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
                    mypath.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), AboutBayerActivity.class);
                startActivity(main);

            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(main);

            }
        });

        demo_plot_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), AccountHistoryActivity.class);
                startActivity(main);

            }
        });
        nearbydemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), CatlogActivity.class);
                startActivity(main);

            }
        });
        scheme_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), SchemeOfferActivity.class);
                startActivity(main);

            }
        });
        mpointcalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), MpointCalc.class);
                startActivity(main);

            }
        });
        engangement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), EngagementActivity.class);
                startActivity(main);

            }
        });

        qna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), QADirectActivity.class);
                startActivity(main);

            }
        });

        termsandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), TermsandCondition.class);
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

//        chatbt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intercom.client().displayMessenger();
//            }
//        });

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

                try {
                    File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
                    mypath.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.menuicon);

        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
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
        RelativeLayout outer = findViewById(R.id.my);
        RelativeLayout headerRel = outer.findViewById(R.id.relbelo);

        TextView nameTv = outer.findViewById(R.id.nameTv);
        TextView lastNameTv = outer.findViewById(R.id.lastNameTv);
        TextView designationTv = outer.findViewById(R.id.designationTv);
        TextView compantyTv = outer.findViewById(R.id.compantyTv);
        final ImageView profileIV = outer.findViewById(R.id.profileIV);
        final ProgressBar progressView = outer.findViewById(R.id.progressView);

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
//                Profile.putExtra("back",eventlist.)
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
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return false;
                }
            }).into(profileIV);
        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);
            progressView.setVisibility(View.GONE);
        }

    }

    private void setupTabIcons() {


        tabLayout.getTabAt(0).setIcon(R.drawable.selector_newsfeed);


        tabLayout.getTabAt(1).setIcon(R.drawable.selected_myperformance);


        tabLayout.getTabAt(2).setIcon(R.drawable.selected_mpinscanner);


        tabLayout.getTabAt(3).setIcon(R.drawable.selected_leaderboard);
//        if (tabLayout.getTabAt(0) != null) {
//            if (tabLayout.getTabAt(0).getText().equals("News Feed")) {
//                tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//            } else if (tabLayout.getTabAt(0).getText().equals("Agenda")) {
//                tabLayout.getTabAt(0).setIcon(tabIcons[1]);
//            } else if (tabLayout.getTabAt(0).getText().equals("Attendees")) {
//                tabLayout.getTabAt(0).setIcon(tabIcons[2]);
//            } else if (tabLayout.getTabAt(0).getText().equals("Speakers")) {
//                tabLayout.getTabAt(0).setIcon(tabIcons[3]);
//            } else if (tabLayout.getTabAt(0).getText().equals("General Info")) {
//                tabLayout.getTabAt(0).setIcon(tabIcons[4]);
//            }
//        }
//
//
//        if (tabLayout.getTabAt(1) != null) {
//            if (tabLayout.getTabAt(1).getText().equals("News Feed")) {
//                tabLayout.getTabAt(1).setIcon(tabIcons[0]);
//            } else if (tabLayout.getTabAt(1).getText().equals("Agenda")) {
//                tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//            } else if (tabLayout.getTabAt(1).getText().equals("Attendees")) {
//                tabLayout.getTabAt(1).setIcon(tabIcons[2]);
//            } else if (tabLayout.getTabAt(1).getText().equals("Speakers")) {
//                tabLayout.getTabAt(1).setIcon(tabIcons[3]);
//            } else if (tabLayout.getTabAt(1).getText().equals("General Info")) {
//                tabLayout.getTabAt(1).setIcon(tabIcons[4]);
//            }
//        }
//
//
//        if (tabLayout.getTabAt(2) != null) {
//            if (tabLayout.getTabAt(2).getText().equals("News Feed")) {
//                tabLayout.getTabAt(2).setIcon(tabIcons[0]);
//            } else if (tabLayout.getTabAt(2).getText().equals("Agenda")) {
//                tabLayout.getTabAt(2).setIcon(tabIcons[1]);
//            } else if (tabLayout.getTabAt(2).getText().equals("Attendees")) {
//                tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//            } else if (tabLayout.getTabAt(2).getText().equals("Speakers")) {
//                tabLayout.getTabAt(2).setIcon(tabIcons[3]);
//            } else if (tabLayout.getTabAt(2).getText().equals("General Info")) {
//                tabLayout.getTabAt(2).setIcon(tabIcons[4]);
//            }
//        }
//
//
//        if (tabLayout.getTabAt(3) != null) {
//            if (tabLayout.getTabAt(3).getText().equals("News Feed")) {
//                tabLayout.getTabAt(3).setIcon(tabIcons[0]);
//            } else if (tabLayout.getTabAt(3).getText().equals("Agenda")) {
//                tabLayout.getTabAt(3).setIcon(tabIcons[1]);
//            } else if (tabLayout.getTabAt(3).getText().equals("Attendees")) {
//                tabLayout.getTabAt(3).setIcon(tabIcons[2]);
//            } else if (tabLayout.getTabAt(3).getText().equals("Speakers")) {
//                tabLayout.getTabAt(3).setIcon(tabIcons[3]);
//            } else if (tabLayout.getTabAt(3).getText().equals("General Info")) {
//                tabLayout.getTabAt(3).setIcon(tabIcons[4]);
//            }
//        }
//        if (tabLayout.getTabAt(4) != null) {
//            if (tabLayout.getTabAt(4).getText().equals("News Feed")) {
//                tabLayout.getTabAt(4).setIcon(tabIcons[0]);
//            } else if (tabLayout.getTabAt(4).getText().equals("Agenda")) {
//                tabLayout.getTabAt(4).setIcon(tabIcons[1]);
//            } else if (tabLayout.getTabAt(4).getText().equals("Attendees")) {
//                tabLayout.getTabAt(4).setIcon(tabIcons[2]);
//            } else if (tabLayout.getTabAt(4).getText().equals("Speakers")) {
//                tabLayout.getTabAt(4).setIcon(tabIcons[3]);
//            } else if (tabLayout.getTabAt(4).getText().equals("General Info")) {
//                tabLayout.getTabAt(4).setIcon(tabIcons[4]);
//            }
//        }

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
        adapter.addFragment(new WallFragment_POST(), "NewsFeed");
        adapter.addFragment(new MyPreferenceFragment(), "MyPerformance");
        adapter.addFragment(new MpinScanner(), "mPin Scanner");
        adapter.addFragment(new LeaderboardFragment(), "Leaderboard");


//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        if (news_feed.equalsIgnoreCase("1")) {
//            adapter.addFragment(new WallFragment_POST(), "News Feed");
//        }
//        if (agenda.equalsIgnoreCase("1")) {
//            if (agenda_conference.equalsIgnoreCase("1")) {
//                adapter.addFragment(new AgendaFragment(), "Agenda");
//            } else if (agenda_vacation.equalsIgnoreCase("1")) {
//                adapter.addFragment(new AgendaFolderFragment(), "Agenda");
//            }
//
//        }
//        if (attendee.equalsIgnoreCase("1")) {
//            adapter.addFragment(new AttendeeFragment(), "Attendees");
//        }
//        if (speaker.equalsIgnoreCase("1")) {
//            adapter.addFragment(new SpeakerFragment(), "Speakers");
//        }
//        if (general_ifo.equalsIgnoreCase("1")) {
//            adapter.addFragment(new GeneralInfo(), "General Info");
//        }
//        if (news_feed.equalsIgnoreCase("1") && agenda.equalsIgnoreCase("0") &&
//                attendee.equalsIgnoreCase("0") && speaker.equalsIgnoreCase("0") &&
//                general_ifo.equalsIgnoreCase("0")) {
//            adapter.addFragment(new WallFragment_POST(), "News Feed");
//            tabLayout = findViewById(R.id.tabs);
//            AppBarLayout appTab = findViewById(R.id.appTab);
////            appTab.setElevation(0);
//            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    10.0f
//            );
//
//            viewPager.setLayoutParams(param);
//            appTab.setVisibility(View.GONE);
//            tabLayout.setVisibility(View.GONE);
//
//
//        }
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

        boolean check = MyJZVideoPlayerStandard.backPress();

        if (check == true) {
            MyJZVideoPlayerStandard.goOnPlayOnPause();
            MyJZVideoPlayerStandard.quitFullscreenOrTinyWindow();
        } else {

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

                            ActivityCompat.finishAffinity(HomeActivity.this);

                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
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

    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String msg = "";

        String res = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());
            OkHttpClient client = null;
            try {

                client = getUnsafeOkHttpClient().newBuilder().build();

            } catch (Exception e) {
                e.printStackTrace();
            }

            MultipartBody.Builder builder = new MultipartBody.Builder();


            Request request = new Request.Builder()
                    .url(postUrl)
                    .get()
                    .build();

            HttpEntity httpEntity = null;
            okhttp3.Response response = null;

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("app_version");
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            } catch (NullPointerException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }

            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog

            if (error.equalsIgnoreCase("success")) {

                if (message.equalsIgnoreCase(app_version)) {

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setTitle("Exit");
                    builder.setCancelable(false);
                    builder.setMessage("New update is available,Please update application.");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                    finishAffinity();
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.bayer.bayerreward"));
                                    startActivity(intent);
                                }
                            });
                    builder.show();
                }


            } else {
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private static HttpResponse transformResponse(okhttp3.Response response) {

        BasicHttpResponse httpResponse = null;
        try {
            int code = 0;
            if (response != null)
                code = response.code();


            try {
                String message = response.message();
                httpResponse = new BasicHttpResponse(HTTP_1_1, code, message);

                ResponseBody body = response.body();
                InputStreamEntity entity = new InputStreamEntity(body.byteStream(), body.contentLength());
                httpResponse.setEntity(entity);

                Headers headers = response.headers();
                for (int i = 0, size = headers.size(); i < size; i++) {
                    String name = headers.name(i);
                    String value = headers.value(i);
                    httpResponse.addHeader(name, value);
                    if ("Content-Type".equalsIgnoreCase(name)) {
                        entity.setContentType(value);
                    } else if ("Content-Encoding".equalsIgnoreCase(name)) {
                        entity.setContentEncoding(value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return httpResponse;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        // Create a trust manager that does not validate certificate chains
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.sslSocketFactory(sslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        return builder.build();
    }

}
