package com.procialize.eventsapp.InnerDrawerActivity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.procialize.eventsapp.Activity.AttendeeDetailActivity;
import com.procialize.eventsapp.Activity.CommentActivity;
import com.procialize.eventsapp.Activity.LikeDetailActivity;
import com.procialize.eventsapp.Adapter.NotificationAdapter;
import com.procialize.eventsapp.ApiConstant.APIService;
import com.procialize.eventsapp.ApiConstant.ApiConstant;
import com.procialize.eventsapp.ApiConstant.ApiUtils;
import com.procialize.eventsapp.DbHelper.DBHelper;
import com.procialize.eventsapp.GetterSetter.AttendeeList;
import com.procialize.eventsapp.GetterSetter.EventSettingList;
import com.procialize.eventsapp.GetterSetter.NewsFeedList;
import com.procialize.eventsapp.GetterSetter.NotificationList;
import com.procialize.eventsapp.GetterSetter.NotificationListFetch;
import com.procialize.eventsapp.GetterSetter.NotificationSend;
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterListner {


    SwipeRefreshLayout notificationRvrefresh;
    RecyclerView notificationRv;
    //    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    ImageView headerlogoIv;
    List<EventSettingList> eventSettingLists;
    String news_feed_share,
            news_feed_comment,
            news_feed_like;
    private APIService mAPIService;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<NewsFeedList> newsfeedsDBList;
    private List<AttendeeList> attendeeDBList;
    Dialog myDialog;
    int currentApiVersion = android.os.Build.VERSION.SDK_INT;
    String formatdate;
    final long[] time = new long[1];
    String token, attendee_status;
    ImageView add_icon;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");


        procializeDB = new DBHelper(NotificationActivity.this);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(NotificationActivity.this);
        //   overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);


        TextView notyHeader = findViewById(R.id.notyHeader);
        notyHeader.setTextColor(Color.parseColor(colorActive));
        notificationRv = findViewById(R.id.notificationRv);
        linear = findViewById(R.id.linear);
        add_icon = findViewById(R.id.add_icon);
        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(add_icon.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        add_icon.setImageDrawable(drawable);
//        progressBar = findViewById(R.id.progressBar);
        notificationRvrefresh = findViewById(R.id.notificationRvrefresh);

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);
        attendee_status = user.get(SessionManager.ATTENDEE_STATUS);

        eventSettingLists = new ArrayList<>();
        eventSettingLists = SessionManager.loadEventList();
        applysetting(eventSettingLists);


        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        notificationRv.setLayoutManager(mLayoutManager);


        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        // notificationRv.setLayoutAnimation(animation);


        fetchNotification(token, eventid);

        notificationRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNotification(token, eventid);
            }
        });

        if (attendee_status.equalsIgnoreCase("1")) {
            add_icon.setVisibility(View.VISIBLE);
        } else {
            add_icon.setVisibility(View.GONE);
        }

        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });

    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_like")) {
                news_feed_like = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_comment")) {
                news_feed_comment = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_share")) {
                news_feed_share = eventSettingLists.get(i).getFieldValue();
            }

        }
    }


    public void fetchNotification(String token, String eventid) {
//        showProgress();
        mAPIService.NotificationListFetch(token, eventid).enqueue(new Callback<NotificationListFetch>() {
            @Override
            public void onResponse(Call<NotificationListFetch> call, Response<NotificationListFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    showResponse(response);
                } else {

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationListFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
                if (notificationRvrefresh.isRefreshing()) {
                    notificationRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<NotificationListFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if ((!response.body().getNotificationList().isEmpty())) {
                NotificationAdapter notificationAdapter = new NotificationAdapter(this, response.body().getNotificationList(), this);
                notificationAdapter.notifyDataSetChanged();
                notificationRv.setAdapter(notificationAdapter);
                notificationRv.scheduleLayoutAnimation();
            } else {
                setContentView(R.layout.activity_empty_view);
                ImageView imageView = findViewById(R.id.back);
                TextView text_empty = findViewById(R.id.text_empty);
                ImageView headerlogoIv = findViewById(R.id.headerlogoIv);
                Util.logomethod(this, headerlogoIv);
                text_empty.setText("Notification not available");
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

//    public void showProgress() {
//        if (progressBar.getVisibility() == View.GONE) {
//            progressBar.setVisibility(View.VISIBLE);
//        }
//    }

//    public void dismissProgress() {
//        if (progressBar.getVisibility() == View.VISIBLE) {
//            progressBar.setVisibility(View.GONE);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    /*
        @Override
        public void onContactSelected(NotificationList notification) {
            if (notification.getNotificationType() != null) {
                db = procializeDB.getReadableDatabase();

                newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notification.getNotificationPostId());
                if (notification.getNotificationType().equalsIgnoreCase("Cmnt") || notification.getNotificationType().equalsIgnoreCase("Like")) {
                    Intent comment = new Intent(this, CommentActivity.class);
                    comment.putExtra("feedid", notification.getNotificationPostId());
                    comment.putExtra("type", notification.getNotificationType());
                    comment.putExtra("company", notification.getCompanyName());
                    comment.putExtra("fname", notification.getAttendeeFirstName());
                    comment.putExtra("lname", notification.getAttendeeLastName());
                    comment.putExtra("profilepic", notification.getProfilePic());
                    comment.putExtra("noti_type", "Notification");
                    try {
                        comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                        comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startActivity(comment);
                }
            }

        }
    */
    @Override
    public void onContactSelected(NotificationList notification) {
        if (notification.getNotificationType() != null) {
            db = procializeDB.getReadableDatabase();

            newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notification.getNotificationPostId());
            if (notification.getNotificationType().equalsIgnoreCase("Cmnt") && news_feed_comment != null) {

                if (!news_feed_comment.equalsIgnoreCase("0")) {
                    Intent comment = new Intent(this, CommentActivity.class);
                    comment.putExtra("feedid", notification.getNotificationPostId());
                    comment.putExtra("type", notification.getNotificationType());

                    comment.putExtra("noti_type", "Notification");
                    try {
                        float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                        float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                        float p1 = height / width;
                        comment.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                        comment.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                        comment.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                        comment.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                        comment.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                        comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                        comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                        comment.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                        comment.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                        comment.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                        comment.putExtra("AspectRatio", p1);
                        if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Image")) {
                            comment.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                        } else if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Video")) {
                            comment.putExtra("videourl", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            comment.putExtra("thumbImg", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getThumbImage());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startActivity(comment);

                }

            } else if (notification.getNotificationType().equalsIgnoreCase("Msg")) {

                attendeeDBList = dbHelper.getAttendeeDetailsId(notification.getAttendeeId());
                if (attendeeDBList.size() > 0) {

                    Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notification.getAttendeeId());
                    attendeetail.putExtra("name", notification.getAttendeeFirstName() + " " + notification.getAttendeeLastName());
                    attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
                    attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
                    attendeetail.putExtra("company", notification.getCompanyName());
                    attendeetail.putExtra("designation", notification.getDesignation());
                    attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
                    attendeetail.putExtra("profile", notification.getProfilePic());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                    startActivity(attendeetail);
                } else {
                    Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
                    attendeetail.putExtra("id", notification.getAttendeeId());
                    attendeetail.putExtra("name", notification.getAttendeeFirstName() + " " + notification.getAttendeeLastName());
                    attendeetail.putExtra("city", "");
                    attendeetail.putExtra("country", "");
                    attendeetail.putExtra("company", notification.getCompanyName());
                    attendeetail.putExtra("designation", notification.getDesignation());
                    attendeetail.putExtra("description", "");
                    attendeetail.putExtra("profile", notification.getProfilePic());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
                    startActivity(attendeetail);
                }


            } else if (notification.getNotificationType().equalsIgnoreCase("Like") && news_feed_like != null) {
                if (!news_feed_like.equalsIgnoreCase("0")) {
                    Intent likedetail = new Intent(this, LikeDetailActivity.class);
                    likedetail.putExtra("feedid", notification.getNotificationPostId());
                    likedetail.putExtra("type", notification.getNotificationType());

                    likedetail.putExtra("noti_type", "Notification");
                    try {
                        float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                        float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                        float p1 = height / width;
                        likedetail.putExtra("heading", newsfeedsDBList.get(0).getPostStatus());
                        likedetail.putExtra("company", newsfeedsDBList.get(0).getCompanyName());
                        likedetail.putExtra("fname", newsfeedsDBList.get(0).getFirstName());
                        likedetail.putExtra("lname", newsfeedsDBList.get(0).getLastName());
                        likedetail.putExtra("profilepic", newsfeedsDBList.get(0).getProfilePic());
                        likedetail.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                        likedetail.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                        likedetail.putExtra("designation", newsfeedsDBList.get(0).getDesignation());
                        likedetail.putExtra("Likeflag", newsfeedsDBList.get(0).getLikeFlag());
                        likedetail.putExtra("date", newsfeedsDBList.get(0).getPostDate());

                        likedetail.putExtra("AspectRatio", p1);
                        if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Image")) {
                            likedetail.putExtra("url", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                        } else if (newsfeedsDBList.get(0).getType().equalsIgnoreCase("Video")) {
                            likedetail.putExtra("videourl", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getMediaFile());
                            likedetail.putExtra("thumbImg", ApiConstant.newsfeedwall + newsfeedsDBList.get(0).getThumbImage());
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    startActivity(likedetail);
                }
            }
        }

/*
        if (notification.getNotificationType() != null) {
            db = procializeDB.getReadableDatabase();

            newsfeedsDBList = dbHelper.getNewsFeedLikeandComment(notification.getNotificationPostId());
            if (notification.getNotificationType().equalsIgnoreCase("Cmnt") || notification.getNotificationType().equalsIgnoreCase("Like")) {
                Intent comment = new Intent(this, CommentActivity.class);
                comment.putExtra("feedid", notification.getNotificationPostId());
                comment.putExtra("type", notification.getNotificationType());
                comment.putExtra("company", notification.getCompanyName());
                comment.putExtra("name", notification.getAttendeeFirstName());
                comment.putExtra("profilepic", notification.getProfilePic());
                comment.putExtra("noti_type", "Notification");
                try {
                    comment.putExtra("Likes", newsfeedsDBList.get(0).getTotalLikes());
                    comment.putExtra("Comments", newsfeedsDBList.get(0).getTotalComments());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                startActivity(comment);
            }
        }
*/

    }


    @Override
    public void onReplyClick(NotificationList notificationList) {

        /*Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
        attendeetail.putExtra("id", notificationList.getAttendeeId());
        attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
        attendeetail.putExtra("city", "");
        attendeetail.putExtra("country", "");
        attendeetail.putExtra("company", "");
        attendeetail.putExtra("designation", notificationList.getDesignation());
        attendeetail.putExtra("description", "");
        attendeetail.putExtra("profile", notificationList.getProfilePic());

//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
        startActivity(attendeetail);*/
        attendeeDBList = dbHelper.getAttendeeDetailsId(notificationList.getAttendeeId());
        if (attendeeDBList.size() > 0) {

            Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
            attendeetail.putExtra("id", notificationList.getAttendeeId());
            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            attendeetail.putExtra("city", attendeeDBList.get(0).getCity());
            attendeetail.putExtra("country", attendeeDBList.get(0).getCountry());
            attendeetail.putExtra("company", notificationList.getCompanyName());
            attendeetail.putExtra("designation", notificationList.getDesignation());
            attendeetail.putExtra("description", attendeeDBList.get(0).getDescription());
            attendeetail.putExtra("profile", notificationList.getProfilePic());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
            startActivity(attendeetail);
        } else {
            Intent attendeetail = new Intent(this, AttendeeDetailActivity.class);
            attendeetail.putExtra("id", notificationList.getAttendeeId());
            attendeetail.putExtra("name", notificationList.getAttendeeFirstName() + " " + notificationList.getAttendeeLastName());
            attendeetail.putExtra("city", "");
            attendeetail.putExtra("country", "");
            attendeetail.putExtra("company", notificationList.getCompanyName());
            attendeetail.putExtra("designation", notificationList.getDesignation());
            attendeetail.putExtra("description", "");
            attendeetail.putExtra("profile", notificationList.getProfilePic());


//                speakeretail.putExtra("totalrate",attendee.getTotalRating());
            startActivity(attendeetail);
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    private void showratedialouge() {

        myDialog = new Dialog(NotificationActivity.this);
        myDialog.setContentView(R.layout.add_notification);
        myDialog.setCancelable(false);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);
        ImageView id_date = myDialog.findViewById(R.id.id_date);
        Button canclebtn = myDialog.findViewById(R.id.canclebtn);
        Button send_notification = myDialog.findViewById(R.id.send_notification);
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);
        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String dateString = sdf.format(date);
        nametv.setText(dateString);

        nametv.setTextColor(Color.parseColor(colorActive));
        diatitle.setBackgroundColor(Color.parseColor(colorActive));
        send_notification.setBackgroundColor(Color.parseColor(colorActive));
        canclebtn.setBackgroundColor(Color.parseColor(colorActive));

        id_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final View dialogView = View.inflate(NotificationActivity.this, R.layout.activity_date_picker, null);
                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(NotificationActivity.this).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        int selectyear = datePicker.getYear();
                        int selectmonth = datePicker.getMonth();
                        int selectday = datePicker.getDayOfMonth();
                        int selecttime = 0;
                        int selecthour = 0;
                        if (currentApiVersion > android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                            selecttime = timePicker.getMinute();
                            selecthour = timePicker.getHour();
                        } else {
                            selecttime = timePicker.getCurrentMinute();
                            selecthour = timePicker.getCurrentHour();

                        }
                        int seconds = calendar.get(Calendar.SECOND);


                        Date mDate = new GregorianCalendar(selectyear, selectmonth, selectday, selecthour, selecttime).getTime();
                        formatdate = (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss", mDate);
                        String date = (String) android.text.format.DateFormat.format("dd MMMM HH:mm", mDate);
                        nametv.setText(date);
                        time[0] = calendar.getTimeInMillis();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });


        send_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nametv.getText().toString().isEmpty()) {
                    Toast.makeText(NotificationActivity.this, "Select Date", Toast.LENGTH_SHORT).show();
                } else if (etmsg.getText().toString().isEmpty()) {
                    Toast.makeText(NotificationActivity.this, "Enter your message", Toast.LENGTH_SHORT).show();
                } else {
                    sendNotification(token, eventid, etmsg.getText().toString(), formatdate);
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        myDialog.show();

    }

    public void sendNotification(String token, String eventid, String message, String display_time) {
//        showProgress();
        mAPIService.SendNotification(token, eventid, message, display_time).enqueue(new Callback<NotificationSend>() {
            @Override
            public void onResponse(Call<NotificationSend> call, Response<NotificationSend> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    showResponseSendNotification(response);
                } else {

                    if (notificationRvrefresh.isRefreshing()) {
                        notificationRvrefresh.setRefreshing(false);
                    }
//                    dismissProgress();
                    Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationSend> call, Throwable t) {
                Toast.makeText(NotificationActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
                if (notificationRvrefresh.isRefreshing()) {
                    notificationRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponseSendNotification(Response<NotificationSend> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            myDialog.dismiss();
            Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NotificationActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

}
