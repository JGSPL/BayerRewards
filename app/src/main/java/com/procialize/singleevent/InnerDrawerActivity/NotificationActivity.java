package com.procialize.singleevent.InnerDrawerActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Activity.AttendeeDetailActivity;
import com.procialize.singleevent.Activity.CommentActivity;
import com.procialize.singleevent.Activity.HomeActivity;
import com.procialize.singleevent.Adapter.NotificationAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.NewsFeedList;
import com.procialize.singleevent.GetterSetter.NotificationList;
import com.procialize.singleevent.GetterSetter.NotificationListFetch;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements NotificationAdapter.NotificationAdapterListner {


    private APIService mAPIService;
    SwipeRefreshLayout notificationRvrefresh;
    RecyclerView notificationRv;
    //    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, logoImg, colorActive;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private List<NewsFeedList> newsfeedsDBList;
    private List<AttendeeList> attendeeDBList;

    ImageView headerlogoIv;

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


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);


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
                Util.logomethod(this,headerlogoIv);
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
            if (notification.getNotificationType().equalsIgnoreCase("Cmnt") || notification.getNotificationType().equalsIgnoreCase("Like")) {
                Intent comment = new Intent(this, CommentActivity.class);
                comment.putExtra("feedid", notification.getNotificationPostId());
                comment.putExtra("type", notification.getNotificationType());

                comment.putExtra("noti_type", "Notification");
                try {
                    float width = Float.parseFloat(newsfeedsDBList.get(0).getWidth());
                    float height = Float.parseFloat(newsfeedsDBList.get(0).getHeight());

                    float p1 = (float) (height / width);
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
            }else if(notification.getNotificationType().equalsIgnoreCase("Msg"))
            {

                attendeeDBList = dbHelper.getAttendeeDetailsId(notification.getAttendeeId());
                if(attendeeDBList.size()>0) {

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
                }else{
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
        if(attendeeDBList.size()>0) {

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
        }else{
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
