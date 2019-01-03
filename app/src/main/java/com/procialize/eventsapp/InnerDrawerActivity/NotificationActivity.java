package com.procialize.eventsapp.InnerDrawerActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.procialize.eventsapp.R;
import com.procialize.eventsapp.Session.SessionManager;
import com.procialize.eventsapp.Utility.Util;

import java.util.ArrayList;
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
//        progressBar = findViewById(R.id.progressBar);
        notificationRvrefresh = findViewById(R.id.notificationRvrefresh);

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        final String token = user.get(SessionManager.KEY_TOKEN);

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
}
