package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Adapter.CommentAdapter;
import com.procialize.singleevent.Adapter.GifEmojiAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.ApiConstant.TenorApiService;
import com.procialize.singleevent.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.singleevent.CustomTools.PixabayImageView;
import com.procialize.singleevent.GetterSetter.CommentDataList;
import com.procialize.singleevent.GetterSetter.CommentList;
import com.procialize.singleevent.GetterSetter.DeleteNewsFeedComment;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.FetchFeed;
import com.procialize.singleevent.GetterSetter.GifId;
import com.procialize.singleevent.GetterSetter.LikePost;
import com.procialize.singleevent.GetterSetter.PostComment;
import com.procialize.singleevent.GetterSetter.ReportComment;
import com.procialize.singleevent.GetterSetter.ReportCommentHide;
import com.procialize.singleevent.GetterSetter.ReportUser;
import com.procialize.singleevent.GetterSetter.Result;
import com.procialize.singleevent.GetterSetter.response;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JZVideoPlayerStandard;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity implements CommentAdapter.CommentAdapterListner, GifEmojiAdapter.GifEmojiAdapterListner {

    public TextView nameTv, designationTv, companyTv, dateTv, headingTv, likeTv, commentTv, sharetext;
    public ImageView profileIv;
    public ProgressBar progressView, feedprogress;
    public PixabayImageView feedimageIv;
    String name, company, designation, heading, date, Likes, Likeflag, Comments, profileurl, noti_profileurl, feedurl, type, feedid, apikey, thumbImg, videourl, noti_type;
    ProgressDialog progress;
    private APIService mAPIService;
    private TenorApiService mAPItenorService;
    RecyclerView commentrecycler;
    ProgressBar progressBar, emojibar;
    SessionManager sessionManager;

    EditText commentEt, searchEt;
    Button commentbtn;
    float p1 = 0;
    BottomSheetDialog dialog;
    CommentAdapter commentAdapter;
    Dialog myDialog;

    ImageView gif, backIv;
    ImageView playicon;
    View view;
    RecyclerView gifrecycler;
    LinearLayout action_container, container2;
    MyJZVideoPlayerStandard videoplayer;
    LinearLayout linearshare,
            linearcomment,
            linearlike;

    private static final String API_KEY = "TVG20YJW1MXR";
    private String id;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    HashMap<String, String> user;
    String user_id;
    List<EventSettingList> eventSettingLists;
    String news_feed_share,
            news_feed_comment,
            news_feed_like;
    ImageView headerlogoIv,image2;
    String colorActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CommentActivity.this, HomeActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");



        Intent intent = getIntent();
        mAPIService = ApiUtils.getAPIService();
        mAPItenorService = ApiUtils.getTenorAPIService();
        sessionManager = new SessionManager(getApplicationContext());
        user = sessionManager.getUserDetails();

        user_id = user.get(SessionManager.KEY_ID);
        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        eventSettingLists = sessionManager.loadEventList();
        applysetting(eventSettingLists);

        try {
            if (intent != null) {
                name = intent.getStringExtra("name");
                company = intent.getStringExtra("company");
                designation = intent.getStringExtra("designation");
                heading = intent.getStringExtra("heading");
                date = intent.getStringExtra("date");
                if (intent.getStringExtra("Likes") == null) {
                    Likes = "1";
                } else {
                    Likes = intent.getStringExtra("Likes");
                }

                if (intent.getStringExtra("Comments") == null) {
                    Comments = "1";
                } else {
                    Comments = intent.getStringExtra("Comments");
                }
                Likeflag = intent.getStringExtra("Likeflag");
                noti_type = intent.getStringExtra("noti_type");

                if (noti_type.equalsIgnoreCase("Notification")) {
                    noti_profileurl = intent.getStringExtra("profilepic");
                } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
                    profileurl = intent.getStringExtra("profilepic");
                }

                type = intent.getStringExtra("type");

                feedid = intent.getStringExtra("feedid");

                p1 = intent.getFloatExtra("AspectRatio", (float) 0.000);

                if (type.equalsIgnoreCase("Image")) {
                    feedurl = intent.getStringExtra("url");

                    Log.e("feedurl", feedurl);

                } else if (type.equalsIgnoreCase("Video")) {
                    thumbImg = intent.getStringExtra("thumbImg");
                    videourl = intent.getStringExtra("videourl");
                }
                Log.e("p", p1 + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeview();
    }

    private void initializeview() {

        nameTv = findViewById(R.id.nameTv);
        nameTv.setTextColor(Color.parseColor(colorActive));
        companyTv = findViewById(R.id.companyTv);
        designationTv = findViewById(R.id.designationTv);
        dateTv = findViewById(R.id.dateTv);
        headingTv = findViewById(R.id.headingTv);
        likeTv = findViewById(R.id.likeTv);
        commentTv = findViewById(R.id.commentTv);
        sharetext = findViewById(R.id.sharetext);
        image2 = findViewById(R.id.image2);
        commentEt = findViewById(R.id.commentEt);
        searchEt = findViewById(R.id.searchEt);
        commentbtn = findViewById(R.id.commentBt);
        playicon = findViewById(R.id.playicon);
        linearshare = findViewById(R.id.linearshare);
        linearcomment = findViewById(R.id.linearcomment);
        linearlike = findViewById(R.id.linearlike);
        commentbtn.setBackgroundColor(Color.parseColor(colorActive));

        feedimageIv = findViewById(R.id.feedimageIv);
//        feedimageIv.setAspectRatio(p1);

        profileIv = findViewById(R.id.profileIV);

        progressBar = findViewById(R.id.progressBar);
        emojibar = findViewById(R.id.emojibar);

        progressView = findViewById(R.id.progressView);
        feedprogress = findViewById(R.id.feedprogress);
        videoplayer = (MyJZVideoPlayerStandard) findViewById(R.id.videoplayer);

        nameTv.setText(name);
        companyTv.setText(company);
        designationTv.setText(designation);
        headingTv.setText(StringEscapeUtils.unescapeJava(heading));
        likeTv.setText(Likes + " Likes ");
        commentTv.setText(Comments + " Comments ");

        commentrecycler = findViewById(R.id.commentrecycler);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(CommentActivity.this);
        commentrecycler.setLayoutManager(mLayoutManager);


        gif = findViewById(R.id.gif);
        backIv = findViewById(R.id.backIv);

        gifrecycler = findViewById(R.id.gifrecycler);
        action_container = findViewById(R.id.action_container);
        container2 = findViewById(R.id.container2);


        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.HORIZONTAL, false);
        gifrecycler.setLayoutManager(horizontalLayoutManagaer);


        view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (news_feed_like.equalsIgnoreCase("0")) {
            likeTv.setVisibility(View.GONE);
            linearlike.setVisibility(View.GONE);

        } else {
            likeTv.setVisibility(View.VISIBLE);
            linearlike.setVisibility(View.VISIBLE);
        }

        if (news_feed_comment.equalsIgnoreCase("0")) {
            commentTv.setVisibility(View.GONE);
            linearcomment.setVisibility(View.GONE);
        } else {
            commentTv.setVisibility(View.VISIBLE);
            linearcomment.setVisibility(View.VISIBLE);
        }

        if (news_feed_share.equalsIgnoreCase("0")) {
            sharetext.setVisibility(View.GONE);
            linearshare.setVisibility(View.GONE);
        } else {
            sharetext.setVisibility(View.VISIBLE);
            linearshare.setVisibility(View.VISIBLE);
        }
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (container2.getVisibility() == View.GONE) {

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        commentEt.setTextColor(Color.parseColor("#0000"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    action_container.setVisibility(View.GONE);
                    container2.setVisibility(View.VISIBLE);
                    GetId(API_KEY);

                } else {
                    action_container.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.GONE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (container2.getVisibility() == View.VISIBLE) {
                    action_container.setVisibility(View.VISIBLE);
                    container2.setVisibility(View.GONE);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    Search(s.toString(), API_KEY, id);
                }

            }
        });


        fetchCommentDetails(apikey, eventid, feedid);
        getComment(eventid, feedid);
//        initiate();

        commentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = StringEscapeUtils.escapeJava(commentEt.getText().toString());
                View view = CommentActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (comment.equals("") || comment.equals(" ")) {
                    Toast.makeText(getApplicationContext(), "Please enter something", Toast.LENGTH_SHORT).show();
                } else {

                    PostComment(eventid, feedid, comment, apikey);
                }
            }
        });


        likeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Likeflag.equals("0")) {
                    likeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);

                    Likeflag = "1";
                    PostLike(eventid, feedid, apikey);
                    Likecount("Like");
                } else {
                    likeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);

                    Likeflag = "0";
                    PostLike(eventid, feedid, apikey);
                    Likecount("Dislike");
                }
            }
        });

        sharetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Image")) {

                    shareTextUrl(date + "\n" + heading, feedurl);

                } else if (type.equals("Video")) {

                    shareTextUrl(date + "\n" + heading, videourl);

                } else {
                    shareTextUrl(date, heading);
                }
            }
        });

    }


    public void Likecount(String flag) {
        if (flag.equals("Dislike")) {
            try {

                int count = Integer.parseInt(Likes);

                if (count > 0) {
                    count = count - 1;
                    likeTv.setText(count + " Likes");
                    Likes = String.valueOf(count);

                } else {
                    likeTv.setText("0" + " Likes");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {

                int count = Integer.parseInt(Likes);


                count = count + 1;
                likeTv.setText(count + " Likes");
                Likes = String.valueOf(count);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void Commentcount() {

        try {

            int count = Integer.parseInt(Comments);


            count = count + 1;
            commentTv.setText(count + " Comments");
            Comments = String.valueOf(count);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void CommentcountDec() {

        try {

            int count = Integer.parseInt(Comments);


            count = count - 1;
            commentTv.setText(count + " Comments");
            Comments = String.valueOf(count);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getComment(String eventid, String feedid) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.getComment(eventid, feedid).enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);

                    showResponse(response);
                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void showResponse(Response<CommentList> response) {

        if (response.body().toString().length() > 0) {

            // specify an adapter (see also next example)
            commentAdapter = new CommentAdapter(CommentActivity.this, response.body().getCommentDataList(), this);
            commentrecycler.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();


        } else {
        }
    }


    public void PostComment(String eventid, String feedid, String comments, String accesskey) {
        showProgress();
        mAPIService.postComment(eventid, feedid, comments, accesskey).enqueue(new Callback<PostComment>() {
            @Override
            public void onResponse(Call<PostComment> call, Response<PostComment> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    commentEt.setText(" ");
                    showPostCommentResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostComment> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    private void showPostCommentResponse(Response<PostComment> response) {

        if (response.body().getStatus().equals("success")) {

            // specify an adapter (see also next example)
            getComment(eventid, feedid);

            Commentcount();

        } else {

        }
    }


    public void PostLike(String eventid, String feedid, String token) {
        showProgress();
        mAPIService.postLike(eventid, feedid, token).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showPostlikeresponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    private void showPostlikeresponse(Response<LikePost> response) {

        if (response.body().getStatus().equals("Success")) {

            Log.e("post", "success");
        } else {
            Log.e("post", "fail");
        }
    }


    public void showProgress() {
        progress = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progress.setMessage("Loading....");
        progress.setTitle("Progress");
        progress.show();
    }

    public void dismissProgress() {
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @Override
    public void onContactSelected(CommentDataList comment) {

    }

    @Override
    public void onMoreSelected(final CommentDataList comment, final int position) {


        dialog = new BottomSheetDialog(this);

        dialog.setContentView(R.layout.botomcommentdialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
//        TextView blockuserTv = dialog.findViewById(R.id.blockuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);

        if (user_id.equalsIgnoreCase(comment.getAttendeeId())) {
            deleteTv.setVisibility(View.VISIBLE);
            reportuserTv.setVisibility(View.GONE);
            hideTv.setVisibility(View.GONE);
            reportTv.setVisibility(View.GONE);
        } else {
            deleteTv.setVisibility(View.GONE);
            reportuserTv.setVisibility(View.VISIBLE);
            hideTv.setVisibility(View.VISIBLE);
            reportTv.setVisibility(View.VISIBLE);
        }

        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteNewsFeedComment(eventid, comment.getNewsFeedId(), comment.getCommentId(), apikey, position);
            }
        });

        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportCommentHide(eventid, comment.getCommentId(), apikey, position);
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportPost", comment.getCommentId());
            }
        });


        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportUser", comment.getAttendeeId());
            }
        });
//
//        blockuserTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                ReportUserHide("1", feed.getAttendeeId(), token);
//
//            }
//        });


        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showratedialouge(final String from, final String id) {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);
        ratebtn.setText("Report User");
        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);

        title.setText("Report User");

        nametv.setText("To " + "Admin");


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


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    dialog.dismiss();
                    if (from.equalsIgnoreCase("reportPost")) {
                        ReportComment(eventid, id, apikey, msg);
                    } else if (from.equalsIgnoreCase("reportUser")) {
                        ReportUser(eventid, id, apikey, msg);
                    }
                } else {
                    Toast.makeText(CommentActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void fetchCommentDetails(String token, String eventid, String postid) {
        showProgress();
        mAPIService.NewsFeedDetailFetch(token, eventid, postid).enqueue(new Callback<FetchFeed>() {
            @Override
            public void onResponse(Call<FetchFeed> call, Response<FetchFeed> response) {

                if (response.isSuccessful()) {


                    Log.i("hit", "post submitted to API." + response.body().toString());

                    dismissProgress();
                    showCommentDetailResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchFeed> call, Throwable t) {

                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showCommentDetailResponse(Response<FetchFeed> response) {

        // specify an adapter (see also next example)
        if (response.body().getNewsFeedList().size() != 0) {
            name = response.body().getNewsFeedList().get(0).getFirstName() + " " + response.body().getNewsFeedList().get(0).getFirstName();
            company = response.body().getNewsFeedList().get(0).getCompanyName();
            designation = response.body().getNewsFeedList().get(0).getDesignation();
            heading = response.body().getNewsFeedList().get(0).getPostStatus();
            date = response.body().getNewsFeedList().get(0).getPostDate();
            Likes = response.body().getNewsFeedList().get(0).getTotalLikes();
            Likeflag = response.body().getNewsFeedList().get(0).getLikeFlag();
            Comments = response.body().getNewsFeedList().get(0).getTotalComments();
            profileurl = response.body().getNewsFeedList().get(0).getProfilePic();
            type = response.body().getNewsFeedList().get(0).getType();
          //  feedurl = response.body().getNewsFeedList().get(0).getMediaFile();
            feedid = response.body().getNewsFeedList().get(0).getNewsFeedId();


            float width = Float.parseFloat(response.body().getNewsFeedList().get(0).getWidth());
            float height = Float.parseFloat(response.body().getNewsFeedList().get(0).getHeight());

            p1 = (float) (height / width);


            Log.e("p", p1 + "");

            feedimageIv.setAspectRatio(p1);


            if (type.equalsIgnoreCase("Image")) {
                feedurl = ApiConstant.newsfeedwall + response.body().getNewsFeedList().get(0).getMediaFile();
            } else if (type.equalsIgnoreCase("Video")) {
                thumbImg = ApiConstant.newsfeedwall + response.body().getNewsFeedList().get(0).getThumbImage();
            } else {

            }

            initiate();
        }

    }


    public void DeleteNewsFeedComment(String eventid, String feedid, String commentid, String token, final int position) {
//        showProgress();
        mAPIService.DeleteNewsFeedComment(token, feedid, commentid, eventid).enqueue(new Callback<DeleteNewsFeedComment>() {
            @Override
            public void onResponse(Call<DeleteNewsFeedComment> call, Response<DeleteNewsFeedComment> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    Deletecommentresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(CommentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteNewsFeedComment> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CommentActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void Deletecommentresponse(Response<DeleteNewsFeedComment> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            commentAdapter.commentLists.remove(position);
            commentAdapter.notifyItemRemoved(position);
            dialog.dismiss();
            CommentcountDec();
        } else {
            Log.e("post", "fail");
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    public void ReportCommentHide(String eventid, String commentid, String token, final int position) {
//        showProgress();
        mAPIService.ReportCommentHide(token, eventid, commentid).enqueue(new Callback<ReportCommentHide>() {
            @Override
            public void onResponse(Call<ReportCommentHide> call, Response<ReportCommentHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportCommentHideresponse(response, position);
                } else {
//                    dismissProgress();

                    Toast.makeText(CommentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportCommentHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CommentActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportCommentHideresponse(Response<ReportCommentHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            commentAdapter.commentLists.remove(position);
            commentAdapter.notifyItemRemoved(position);
            dialog.dismiss();
            CommentcountDec();

        } else {
            Log.e("post", "fail");
            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }


    public void ReportComment(String eventid, String commentid, String token, String text) {
//        showProgress();
        mAPIService.ReportComment(token, eventid, commentid, text).enqueue(new Callback<ReportComment>() {
            @Override
            public void onResponse(Call<ReportComment> call, Response<ReportComment> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportCommentresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(CommentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportComment> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CommentActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportCommentresponse(Response<ReportComment> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }


    public void ReportUser(String eventid, String target_attendee_id, String token, String text) {
//        showProgress();
        mAPIService.ReportUser(token, eventid, target_attendee_id, text).enqueue(new Callback<ReportUser>() {
            @Override
            public void onResponse(Call<ReportUser> call, Response<ReportUser> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportUserresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(CommentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportUser> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CommentActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportUserresponse(Response<ReportUser> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(CommentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }


    public void GetId(String key) {
        emojibar.setVisibility(View.VISIBLE);
        mAPItenorService.GifIdPost(key).enqueue(new Callback<GifId>() {
            @Override
            public void onResponse(Call<GifId> call, Response<GifId> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    showgifIdResponse(response);
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    emojibar.setVisibility(View.GONE);
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<GifId> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                emojibar.setVisibility(View.GONE);
            }
        });
    }

    public void showgifIdResponse(Response<GifId> response) {

        if (response != null) {
            id = response.body().getAnon_id();
            Log.e("id", id);
            Result(API_KEY, id);
        } else {
            emojibar.setVisibility(View.GONE);
        }
    }


    public void Result(String key, String id) {
        mAPItenorService.Result(key, id).enqueue(new Callback<response>() {
            @Override
            public void onResponse(Call<response> call, Response<response> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    showResult(response);
                } else {
                    emojibar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<response> call, Throwable t) {
                emojibar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showResult(Response<response> response) {

        if (response != null) {
            emojibar.setVisibility(View.GONE);

            Log.e("size", response.body().getResults().size() + "");
            GifEmojiAdapter gifEmojiAdapter = new GifEmojiAdapter(CommentActivity.this, response.body().getResults(), this);
            gifrecycler.setAdapter(gifEmojiAdapter);
            gifEmojiAdapter.notifyDataSetChanged();
        } else {
            emojibar.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

        }
    }


    public void Search(String query, String key, String id) {
        mAPItenorService.search(query, key, id).enqueue(new Callback<response>() {
            @Override
            public void onResponse(Call<response> call, Response<response> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    showsearchResult(response);
                } else {
                    emojibar.setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<response> call, Throwable t) {
                emojibar.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showsearchResult(Response<response> response) {

        if (response != null) {
            Log.e("size", response.body().getResults().size() + "");
            GifEmojiAdapter gifEmojiAdapter = new GifEmojiAdapter(CommentActivity.this, response.body().getResults(), this);
            gifrecycler.setAdapter(gifEmojiAdapter);
            gifEmojiAdapter.notifyDataSetChanged();
        } else {
//            Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onGifSelected(Result result) {


        PostComment(eventid, feedid, result.getMedia().get(0).getGif().getUrl(), apikey);
        action_container.setVisibility(View.VISIBLE);
        container2.setVisibility(View.GONE);
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initiate() {
//        nameTv.setText(name);
//        companyTv.setText(company);
//        designationTv.setText(designation);
//        headingTv.setText(StringEscapeUtils.unescapeJava(heading));
//        likeTv.setText(Likes + " Likes ");
//        commentTv.setText(Comments + " Comments ");

        if (date != null) {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
//            try {
//                Date date1 = formatter.parse(date);
//
//                DateFormat originalFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.ENGLISH);
//
//                String date = originalFormat.format(date1);
//
//                dateTv.setText(date);
//
//
//                Log.e("date", date);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date1 = formatter.parse(date);

                DateFormat originalFormat = new SimpleDateFormat("dd MMM , yyyy KK:mm", Locale.ENGLISH);

                String date = originalFormat.format(date1);

                dateTv.setText(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


        if (Likeflag != null) {
            if (Likeflag.equals("1")) {
                likeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);
            } else {
                likeTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
            }
        }

        if (noti_type.equalsIgnoreCase("Notification")) {

            if (noti_profileurl != null) {

                Glide.with(this).load(ApiConstant.profilepic + noti_profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIv.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIv);
            } else {
                progressView.setVisibility(View.GONE);
            }

        } else if (noti_type.equalsIgnoreCase("Wall_Post")) {
            if (profileurl != null) {

                Glide.with(this).load(ApiConstant.profilepic + profileurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIv.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIv);
            } else {
                progressView.setVisibility(View.GONE);
            }
        }

        if (type.equals("Image")) {
            //photo

            if (feedurl != null) {
                feedimageIv.setVisibility(View.VISIBLE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                Glide.with(this).load(feedurl).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(feedimageIv).onLoadStarted(getDrawable(R.drawable.gallery_placeholder));
            } else {
                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Video")) {
            //video

           /* if (videourl != null) {
                feedimageIv.setVisibility(View.GONE);
                videoplayer.setVisibility(View.VISIBLE);
                playicon.setVisibility(View.GONE);
                videoplayer.setUp(videourl
                        , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

                Glide.with(CommentActivity.this).load(videourl).into(videoplayer.thumbImageView);


                feedprogress.setVisibility(View.GONE);
            }

        } else if (type.equals("Status")) {
            feedimageIv.setVisibility(View.GONE);
            feedprogress.setVisibility(View.GONE);
        }*/
            if (thumbImg != null) {
                feedimageIv.setVisibility(View.VISIBLE);
                videoplayer.setVisibility(View.GONE);
                playicon.setVisibility(View.GONE);
                image2.setVisibility(View.VISIBLE);
                Glide.with(this).load(thumbImg).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        feedprogress.setVisibility(View.GONE);
                        return false;
                    }
                }).into(feedimageIv).onLoadStarted(getDrawable(R.drawable.gallery_placeholder));
            } else {
                feedprogress.setVisibility(View.GONE);
            }
        }

    }
//
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(CommentActivity.this, HomeActivity.class);
//        startActivity(intent);
        finish();
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

    private void shareTextUrl(String data, String url) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, data);
        share.putExtra(Intent.EXTRA_TEXT, url);

        startActivity(Intent.createChooser(share, "Share link!"));
    }

}
