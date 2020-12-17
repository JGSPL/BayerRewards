package com.bayer.bayerreward.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.CommentActivity;
import com.bayer.bayerreward.Activity.ImageViewActivity;
import com.bayer.bayerreward.Activity.LikeDetailActivity;
import com.bayer.bayerreward.Activity.LoginActivity;
import com.bayer.bayerreward.Activity.PostEditActivity;
import com.bayer.bayerreward.Activity.PostEditActivityOld;
import com.bayer.bayerreward.Adapter.LikeAdapter;
import com.bayer.bayerreward.Adapter.NewsfeedAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.Analytic;
import com.bayer.bayerreward.GetterSetter.AttendeeList;
import com.bayer.bayerreward.GetterSetter.DeletePost;
import com.bayer.bayerreward.GetterSetter.EventSettingList;
import com.bayer.bayerreward.GetterSetter.FetchAttendee;
import com.bayer.bayerreward.GetterSetter.FetchFeed;
import com.bayer.bayerreward.GetterSetter.LikeListing;
import com.bayer.bayerreward.GetterSetter.LikePost;
import com.bayer.bayerreward.GetterSetter.NewsFeedList;
import com.bayer.bayerreward.GetterSetter.ProfileSave;
import com.bayer.bayerreward.GetterSetter.ReportPost;
import com.bayer.bayerreward.GetterSetter.ReportPostHide;
import com.bayer.bayerreward.GetterSetter.ReportUser;
import com.bayer.bayerreward.GetterSetter.ReportUserHide;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bayer.bayerreward.Utility.Util.setTextViewDrawableColor;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class WallFragment_POST extends Fragment implements NewsfeedAdapter.FeedAdapterListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Bundle mBundleRecyclerViewState;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    public Parcelable state;
    ProgressBar progressbar;
    ListView feedrecycler;
    SwipeRefreshLayout newsfeedrefresh;
    LinearLayout mainLLpost, mindTv;
    NewsfeedAdapter feedAdapter;
    LikeAdapter likeAdapter;
    String news_feed_post = "0", news_feed_images = "0", news_feed_video = "0";
    String value = "text";
    String token, LIST_INSTANCE_STATE;
    HashMap<String, String> user;
    BottomSheetDialog dialog;
    Dialog myDialog;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String eventid;
    List<AttendeeList> attendeeLists;
    RecyclerView likelist;
    ImageView profileIV;
    LayoutAnimationController animation;
    SessionManager sessionManager;
    SharedPreferences prefs;
    String colorActive;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private APIService mAPIService;
    private FeedFragment.OnFragmentInteractionListener mListener;
    private List<EventSettingList> eventSettingLists;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<NewsFeedList> newsfeedList;
    private List<NewsFeedList> newsfeedsDBList;
    private DBHelper dbHelper;
    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private LinearLayoutManager mLayoutManager;
    private List<AttendeeList> attendeeList;
    RelativeLayout relative;
    int feed_pos;
    static int index;
    TextView pullrefresh;
    static int mCurrentY, mCurrentX;
    static int top;
    private static final String LIST_STATE = "listState";
    private Parcelable mListState = null;
    String device = Build.MODEL;

    public WallFragment_POST() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    static public void shareImage(final String data, String url, final Context context) {
        Picasso.with(context).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_SUBJECT, data);
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));

                context.startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    static public Uri getLocalBitmapUri(Bitmap bmp, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, "com.procialize.eventsapp.android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        mAPIService = ApiUtils.getAPIService();

        sessionManager = new SessionManager(getContext());

        user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);

        final String profilepic = user.get(SessionManager.KEY_PIC);

        MpinScanner.count=0;
        prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        View view = inflater.inflate(R.layout.wall_post, container, false);

        feedrecycler = view.findViewById(R.id.feedrecycler);
        progressbar = view.findViewById(R.id.progressbar);
        relative = view.findViewById(R.id.relative);
        pullrefresh = view.findViewById(R.id.pullrefresh);
//        feedrecycler.setHasFixedSize(true);
//        feedrecycler.setNestedScrollingEnabled(false);

        pullrefresh.setTextColor(Color.parseColor(colorActive));
        int resId = R.anim.layout_animation_slide_right;
        animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
        // feedrecycler.setLayoutAnimation(animation);
//        try {
//
//            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
//            Resources res = getResources();
//            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
//            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
//            relative.setBackgroundDrawable(bd);
//
//            Log.e("PATH", String.valueOf(mypath));
//        } catch (Exception e) {
//            e.printStackTrace();
//            relative.setBackgroundColor(Color.parseColor("#f1f1f1"));
//        }

        newsfeedrefresh = view.findViewById(R.id.newsfeedrefresh);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        feedrecycler.setLayoutManager(mLayoutManager);
        cd = new ConnectionDetector(getActivity());
        dbHelper = new DBHelper(getActivity());
//        feedrecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                JZVideoPlayerStandard.releaseAllVideos();
//            }
//        });

        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();

        SharedPreferences prefs1 = getActivity().getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE);
        String login = prefs1.getString("loginfirst", "");


        if (login.equalsIgnoreCase("1")) {
            progressbar.setVisibility(View.GONE);
            db = procializeDB.getReadableDatabase();

            newsfeedsDBList = dbHelper.getNewsFeedDetails();

            if (newsfeedsDBList.size() == 0) {
                NewsFeedList newsFeedList = new NewsFeedList();
                newsFeedList.setType("text");
                newsfeedsDBList.add(newsFeedList);
                feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, true);
                feedrecycler.setSelection(mCurrentX);
            }

        } else {

            if (cd.isConnectingToInternet()) {
                fetchFeed(token, eventid);
            } else {
                db = procializeDB.getReadableDatabase();

                newsfeedsDBList = dbHelper.getNewsFeedDetails();

                if (newsfeedsDBList.size() == 0) {
                    NewsFeedList newsFeedList = new NewsFeedList();
                    newsFeedList.setType("text");

                    newsfeedsDBList.add(newsFeedList);
//                Parcelable state = feedrecycler.onSaveInstanceState();
                    feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, true);
                    feedrecycler.setSelection(mCurrentX);


// ...

// restore index and position

                } else {
                    Parcelable state = feedrecycler.onSaveInstanceState();
                    feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, false);
                    feedAdapter.notifyDataSetChanged();
                    feedrecycler.setAdapter(feedAdapter);
                    feedrecycler.setSelection(mCurrentX);


                }
            }
        }

        newsfeedrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //fetchFeed(token,eventid);
                if (cd.isConnectingToInternet()) {
                    fetchFeed(token, eventid);
                } else {
                    db = procializeDB.getReadableDatabase();

                    newsfeedsDBList = dbHelper.getNewsFeedDetails();

                    if (newsfeedsDBList.size() == 0) {
                        NewsFeedList newsFeedList = new NewsFeedList();
                        newsFeedList.setType("text");

                        newsfeedsDBList.add(newsFeedList);
                        feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, true);
                        feedrecycler.setSelection(mCurrentX);
                        index = feedrecycler.getFirstVisiblePosition();

                    } else {
                        feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, false);
                        feedAdapter.notifyDataSetChanged();
                        feedrecycler.setAdapter(feedAdapter);
                        feedrecycler.setSelection(mCurrentX);

                    }

                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(true);
                    }

                }
            }
        });

        mAPIService.AttendeeFetchPost(token, eventid).enqueue(new Callback<FetchAttendee>() {
            @Override
            public void onResponse(Call<FetchAttendee> call, Response<FetchAttendee> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();
                    } else {
                        showResponseAttendee(response);
                    }
                } else {

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchAttendee> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });

        // specify an adapter (see also next example)
//        FeedAdapter feedAdapter = new FeedAdapter(getActivity(),HomeActivity.NewsFeedarryList);
//        feedrecycler.setAdapter(feedAdapter);


        feedrecycler.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
//                 Safety net

//                MyJZVideoPlayerStandard videoView = view.findViewById(R.id.videoplayer);
//
//                if (videoView.currentScreen!=SCREEN_WINDOW_FULLSCREEN) {
//                    try {
//
//                        if (videoView != null) {
//                            if (videoView.isCurrentPlay()) {
//                                videoView.onStatePause();
//                            }
//                            videoView.release();
//
//                        }
//                        // videoView.release();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });

        feedrecycler.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // JZVideoPlayer.releaseAllVideos();
                JZVideoPlayer.goOnPlayOnPause();

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentX = firstVisibleItem;
//                    mCurrentY = view.getScrollY();

            }
        });


        CommonFunction.crashlytics("WallPostFrag","");
        firbaseAnalytics(getActivity(),"WallPostFrag","");
        return view;

    }


    public void showResponseAttendee(Response<FetchAttendee> response) {

        // specify an adapter (see also next example)
        attendeeList = response.body().getAttendeeList();
        procializeDB.clearAttendeesTable();
        procializeDB.insertAttendeesInfo(attendeeList, db);
        //attendeesDBList = dbHelper.getAttendeeDetails();


    }


    private void weightapply(RelativeLayout txtfeedRv, RelativeLayout imagefeedRv, RelativeLayout videofeedRv, View viewone, View viewteo) {

        if (txtfeedRv.getVisibility() == View.GONE && imagefeedRv.getVisibility() == View.VISIBLE && videofeedRv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            imagefeedRv.setLayoutParams(param);
            videofeedRv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewteo.setVisibility(View.VISIBLE);
            value = "image";

        } else if (imagefeedRv.getVisibility() == View.GONE && txtfeedRv.getVisibility() == View.VISIBLE && videofeedRv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            txtfeedRv.setLayoutParams(param);
            videofeedRv.setLayoutParams(param);

            viewone.setVisibility(View.VISIBLE);
            viewteo.setVisibility(View.GONE);

            value = "text";

        } else if (videofeedRv.getVisibility() == View.GONE && imagefeedRv.getVisibility() == View.VISIBLE && txtfeedRv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            txtfeedRv.setLayoutParams(param);
            imagefeedRv.setLayoutParams(param);

            viewone.setVisibility(View.VISIBLE);
            viewteo.setVisibility(View.GONE);
            value = "text";

        } else if (videofeedRv.getVisibility() == View.VISIBLE && imagefeedRv.getVisibility() == View.VISIBLE && txtfeedRv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );
            videofeedRv.setLayoutParams(param);
            txtfeedRv.setLayoutParams(param);
            imagefeedRv.setLayoutParams(param);


            viewone.setVisibility(View.VISIBLE);
            viewteo.setVisibility(View.VISIBLE);
            value = "text";

        } else if (videofeedRv.getVisibility() == View.VISIBLE && imagefeedRv.getVisibility() == View.GONE && txtfeedRv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            videofeedRv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewteo.setVisibility(View.GONE);
            value = "video";
        } else if (videofeedRv.getVisibility() == View.GONE && imagefeedRv.getVisibility() == View.VISIBLE && txtfeedRv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            imagefeedRv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewteo.setVisibility(View.GONE);
            value = "image";

        } else if (videofeedRv.getVisibility() == View.GONE && imagefeedRv.getVisibility() == View.GONE && txtfeedRv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );

            txtfeedRv.setLayoutParams(param);

            viewone.setVisibility(View.GONE);
            viewteo.setVisibility(View.GONE);
            value = "text ";
        } else if (videofeedRv.getVisibility() == View.GONE && imagefeedRv.getVisibility() == View.GONE && txtfeedRv.getVisibility() == View.GONE) {
            mindTv.setVisibility(View.GONE);
            mainLLpost.setVisibility(View.GONE);
        }

    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("news_feed_video")) {
                news_feed_video = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("news_feed_post")) {
                news_feed_post = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("news_feed_images")) {
                news_feed_images = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onContactSelected(NewsFeedList feed, ImageView ivProfile) {

        if (feed.getType().equals("Image")) {
            Intent imageview = new Intent(getContext(), ImageViewActivity.class);
            imageview.putExtra("url", ApiConstant.newsfeedwall + feed.getMediaFile());
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), ivProfile, "profile");
            startActivity(imageview, options.toBundle());
        }
//        else if (feed.getType().equals("Video")) {
//            Intent videoview = new Intent(getContext(), VideoViewActivity.class);
//            videoview.putExtra("url", ApiConstant.newsfeedwall + feed.getMediaFile());
//            startActivity(videoview);
//        }

    }

    @Override
    public void likeTvViewOnClick(View v, NewsFeedList feed, int position, TextView likeimage, TextView liketext) {

        int count = Integer.parseInt(feed.getTotalLikes());

        Drawable[] drawables = likeimage.getCompoundDrawables();
        Bitmap bitmap = ((BitmapDrawable) drawables[2]).getBitmap();

        Bitmap bitmap2 = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_like)).getBitmap();


//        if(!drawables[2].equals(R.drawable.ic_like)){
        if (bitmap != bitmap2) {

            feed.setLikeFlag("0");
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
//            likeimage.setBackgroundResource(R.drawable.ic_like);
            if (cd.isConnectingToInternet()) {
                PostLike(eventid, feed.getNewsFeedId(), token);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            try {

                if (count > 0) {
                    count = count - 1;
                    feed.setTotalLikes(String.valueOf(count));
                    liketext.setText(count + " Likes");

                    feed.setTotalLikes(String.valueOf(count));

                } else {
                    liketext.setText("0" + " Likes");
                    feed.setTotalLikes("0");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            feed.setLikeFlag("1");
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);

            setTextViewDrawableColor(likeimage, colorActive);

//            likeimage.setBackgroundResource(R.drawable.ic_afterlike);
            if (cd.isConnectingToInternet()) {
                PostLike(eventid, feed.getNewsFeedId(), token);
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            try {

                count = count + 1;
                feed.setTotalLikes(String.valueOf(count));

                liketext.setText(count + " Likes");
                feed.setTotalLikes(String.valueOf(count));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        newsfeedList.set(position, feed);


    }

    @Override
    public void commentTvViewOnClick(View v, NewsFeedList feed, int position) {


        float width = Float.parseFloat(feed.getWidth());
        float height = Float.parseFloat(feed.getHeight());

        float p1 = height / width;
        feed_pos = position;
        feedrecycler.setSelection(feed_pos);
        Intent comment = new Intent(getContext(), CommentActivity.class);

        comment.putExtra("fname", feed.getFirstName());
        comment.putExtra("lname", feed.getLastName());
        comment.putExtra("company", feed.getCompanyName());
        comment.putExtra("designation", feed.getDesignation());

        comment.putExtra("heading", feed.getPostStatus());
        comment.putExtra("date", feed.getPostDate());
        comment.putExtra("Likes", feed.getTotalLikes());
        comment.putExtra("Likeflag", feed.getLikeFlag());
        comment.putExtra("Comments", feed.getTotalComments());
        comment.putExtra("profilepic", ApiConstant.profilepic + feed.getProfilePic());
        comment.putExtra("type", feed.getType());
        comment.putExtra("feedid", feed.getNewsFeedId());
        comment.putExtra("AspectRatio", p1);
        comment.putExtra("noti_type", "Wall_Post");

        if (feed.getType().equalsIgnoreCase("Image")) {
            comment.putExtra("url", ApiConstant.newsfeedwall + feed.getMediaFile());
        } else if (feed.getType().equalsIgnoreCase("Video")) {
            comment.putExtra("videourl", ApiConstant.newsfeedwall + feed.getMediaFile());
            comment.putExtra("thumbImg", ApiConstant.newsfeedwall + feed.getThumbImage());
        }
        comment.putExtra("flag", "noti");
        startActivity(comment);

    }

    @Override
    public void shareTvFollowOnClick(View v, NewsFeedList feed) {

        if (feed.getType().equals("Image")) {

            shareImage(feed.getPostDate() + "\n" + feed.getPostStatus(), ApiConstant.newsfeedwall + feed.getMediaFile(), getContext());

        } else if (feed.getType().equals("Video")) {

            shareTextUrl(feed.getPostDate() + "\n" + feed.getPostStatus(), ApiConstant.newsfeedwall + feed.getMediaFile());

        } else {
            shareTextUrl(feed.getPostDate() + "\n" + feed.getPostStatus(), StringEscapeUtils.unescapeJava(feed.getPostStatus()));
        }
    }

    @Override
    public void moreTvFollowOnClick(View v, final NewsFeedList feed, final int position) {

        dialog = new BottomSheetDialog(getActivity());

        dialog.setContentView(R.layout.botomfeeddialouge);


        TextView reportTv = dialog.findViewById(R.id.reportTv);
        TextView hideTv = dialog.findViewById(R.id.hideTv);
        TextView deleteTv = dialog.findViewById(R.id.deleteTv);
        TextView reportuserTv = dialog.findViewById(R.id.reportuserTv);
        TextView blockuserTv = dialog.findViewById(R.id.blockuserTv);
        TextView cancelTv = dialog.findViewById(R.id.cancelTv);
        TextView editIV = dialog.findViewById(R.id.editIV);


        if (user.get(SessionManager.ATTENDEE_STATUS).equalsIgnoreCase("1")) {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.VISIBLE);

                } else {
                    editIV.setVisibility(View.VISIBLE);

                }
            } else {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
            }

        } else {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feed.getAttendeeId())) {
                deleteTv.setVisibility(View.VISIBLE);
                //editIV.setVisibility(View.VISIBLE);
                hideTv.setVisibility(View.GONE);
                reportTv.setVisibility(View.GONE);
                reportuserTv.setVisibility(View.GONE);
                blockuserTv.setVisibility(View.GONE);

                if (feed.getType().equalsIgnoreCase("Video")) {
                    editIV.setVisibility(View.VISIBLE);

                } else {
                    editIV.setVisibility(View.VISIBLE);

                }

            } else {
                deleteTv.setVisibility(View.GONE);
                editIV.setVisibility(View.GONE);
                hideTv.setVisibility(View.VISIBLE);
                reportTv.setVisibility(View.VISIBLE);
                reportuserTv.setVisibility(View.VISIBLE);
                blockuserTv.setVisibility(View.VISIBLE);
            }

        }
        deleteTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDelete(eventid, feed.getNewsFeedId(), token, position);
            }
        });

        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent edit = new Intent(getActivity(), PostEditActivityOld.class);
//                edit.putExtra("for", feed.getType());
//                edit.putExtra("feedid", feed.getNewsFeedId());
//                edit.putExtra("status", feed.getPostStatus());
                if (feed.getType().equalsIgnoreCase("Image")) {
                    Intent editimage = new Intent(getActivity(), PostEditActivity.class);
                    editimage.putExtra("for", feed.getType());
                    editimage.putExtra("feedid", feed.getNewsFeedId());
                    editimage.putExtra("status", feed.getPostStatus());
                    editimage.putExtra("Image", feed.getMediaFile());
                    startActivity(editimage);
                    dialog.dismiss();
                } else if (feed.getType().equalsIgnoreCase("Video")) {
                    Intent edit = new Intent(getActivity(), PostEditActivity.class);
                    edit.putExtra("for", feed.getType());
                    edit.putExtra("feedid", feed.getNewsFeedId());
                    edit.putExtra("status", feed.getPostStatus());
                    edit.putExtra("Video", feed.getMediaFile());
                    edit.putExtra("Image", feed.getThumbImage());
                    startActivity(edit);
                    dialog.dismiss();
                } else if (feed.getType().equalsIgnoreCase("Status")) {
                    Intent edit = new Intent(getActivity(), PostEditActivity.class);
                    edit.putExtra("for", feed.getType());
                    edit.putExtra("feedid", feed.getNewsFeedId());
                    edit.putExtra("status", feed.getPostStatus());
                    startActivity(edit);
                    dialog.dismiss();
                }

            }
        });

        hideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportPostHide(eventid, feed.getNewsFeedId(), token, position);
            }
        });

        reportTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportPost", feed.getNewsFeedId());
            }
        });


        reportuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge("reportUser", feed.getAttendeeId());
            }
        });

        blockuserTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReportUserHide(eventid, feed.getAttendeeId(), token);

            }
        });


        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void moreLikeListOnClick(View v, NewsFeedList feed, int position) {
        PostLikeList(eventid, feed.getType(), feed, token);
//        dialog = new BottomSheetDialog(getActivity());
//
//        dialog.setContentView(R.layout.botomlikelistdialouge);
//
//        likelist = dialog.findViewById(R.id.likelist);
//
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        likelist.setLayoutManager(mLayoutManager);
    }


//    private Bitmap getBitmapFromView(View view) {
//        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        Drawable bgDrawable =view.getBackground();
//        if (bgDrawable!=null) {
//            //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        }   else{
//            //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE);
//        }
//        view.draw(canvas);
//        return returnedBitmap;
//    }

    @Override
    public void FeedEditOnClick(View v, NewsFeedList feed, int position) {

        Intent edit = new Intent(getActivity(), PostEditActivityOld.class);
        edit.putExtra("for", feed.getType());
        edit.putExtra("feedid", feed.getNewsFeedId());
        edit.putExtra("status", feed.getPostStatus());
        if (feed.getType().equalsIgnoreCase("Image")) {
            edit.putExtra("Image", feed.getMediaFile());
        } else if (feed.getType().equalsIgnoreCase("Video")) {
            edit.putExtra("Video", feed.getMediaFile());
        }
        startActivity(edit);
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

    private void showratedialouge(final String from, final String id) {

        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

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
                        ReportPost(eventid, id, token, msg);
                    } else if (from.equalsIgnoreCase("reportUser")) {
                        ReportUser(eventid, id, token, msg);
                    }
                } else {
                    Toast.makeText(getActivity(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void fetchFeed(String token, String eventid) {

        showProgress();

        mAPIService.FeedFetchPost(token, eventid).enqueue(new Callback<FetchFeed>() {
            @Override
            public void onResponse(Call<FetchFeed> call, Response<FetchFeed> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(false);
                    }

                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();

                    } else {


                        showResponse(response);
                    }
                } else {

                    dismissProgress();
                    if (newsfeedrefresh.isRefreshing()) {
                        newsfeedrefresh.setRefreshing(false);
                    }
                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                //  Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (newsfeedrefresh.isRefreshing()) {
                    newsfeedrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<FetchFeed> response) {

//        SessionManager sessionManager = new SessionManager(getContext());
//
//        String name = response.body().getNewsFeedList().get(0).getFirstName()+" "+response.body().getNewsFeedList().get(0).getLastName();
//        String designation=response.body().getNewsFeedList().get(0).getDesignation();
//        String company = response.body().getNewsFeedList().get(0).getCompanyName();
//        String pic = response.body().getNewsFeedList().get(0).getProfilePic();
//
//        sessionManager.createProfileSession(name,company,designation,pic);
        try {
            Log.d("Newseed", response.toString());
            newsfeedList = response.body().getNewsFeedList();
            procializeDB.clearNewsFeedTable();
            procializeDB.insertNEwsFeedInfo(newsfeedList, db);
//            feedrecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
            newsfeedsDBList = dbHelper.getNewsFeedDetails();
            fetchProfileDetail(token, eventid);

            if (newsfeedsDBList.size() == 0) {
                NewsFeedList newsFeedList = new NewsFeedList();
                newsFeedList.setType("text");

                newsfeedsDBList.add(newsFeedList);
                feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, true);
                feedrecycler.setAdapter(feedAdapter);
                feedrecycler.setSelection(mCurrentX);

            } else {
                feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, false);
                feedAdapter.notifyDataSetChanged();
                feedrecycler.setAdapter(feedAdapter);
                feedrecycler.setSelection(mCurrentX);
            }


//            feedrecycler.smoothScrollToPosition(feed_pos);
//            feedrecycler.scheduleLayoutAnimation();
            SubmitAnalytics(token, eventid, "", "", "newsfeed");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void PostLike(String eventid, String feedid, String token) {

//        showProgress();
        mAPIService.postLike(eventid, feedid, token).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.toString());
//                    dismissProgress();
                    showPostlikeresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                dismissProgress();
                // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPostlikeresponse(Response<LikePost> response) {

        if (response.body().getStatus().equals("Success")) {
            Log.e("post", "success");

            if (device.equalsIgnoreCase("vivo V3")) {
                fetchFeed(token, eventid);
            }
//            fetchFeed(token, eventid);
        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void PostDelete(String eventid, String feedid, String token, final int position) {
//        showProgress();
        mAPIService.DeletePost(token, eventid, feedid).enqueue(new Callback<DeletePost>() {
            @Override
            public void onResponse(Call<DeletePost> call, Response<DeletePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    DeletePostresponse(response, position);
                } else {
//                    dismissProgress();
                    //Toast.makeText(getContext(),response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeletePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<DeletePost> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            feedAdapter.feedLists.remove(position);
//            feedAdapter.notifyItemRemoved(position);
            feedAdapter.notifyDataSetChanged();
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void ReportPostHide(String eventid, String feedid, String token, final int position) {
//        showProgress();
        mAPIService.ReportPostHide(token, eventid, feedid).enqueue(new Callback<ReportPostHide>() {
            @Override
            public void onResponse(Call<ReportPostHide> call, Response<ReportPostHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostHideresponse(response, position);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportPostHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostHideresponse(Response<ReportPostHide> response, int position) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

            feedAdapter.feedLists.remove(position);
//            feedAdapter.notifyItemRemoved(position);
            feedAdapter.notifyDataSetChanged();
            dialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void ReportPost(String eventid, String feedid, String token, String text) {
//        showProgress();
        mAPIService.ReportPost(token, eventid, feedid, text).enqueue(new Callback<ReportPost>() {
            @Override
            public void onResponse(Call<ReportPost> call, Response<ReportPost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportPostresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportPost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportPostresponse(Response<ReportPost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    //Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportUser> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportUserresponse(Response<ReportUser> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

            myDialog.dismiss();

        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();
        }
    }

    public void ReportUserHide(String eventid, String target_attendee_id, String token) {
//        showProgress();
        mAPIService.ReportUserHide(token, eventid, target_attendee_id).enqueue(new Callback<ReportUserHide>() {
            @Override
            public void onResponse(Call<ReportUserHide> call, Response<ReportUserHide> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    ReportUserHideresponse(response);
                } else {
//                    dismissProgress();

                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReportUserHide> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(getContext(), "Unable to Delete please try again later", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void ReportUserHideresponse(Response<ReportUserHide> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            fetchFeed(token, eventid);


        } else {
            Log.e("post", "fail");
            dialog.dismiss();
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void PostLikeList(String eventid, String usertype, NewsFeedList feed, String token) {
        float width = Float.parseFloat(feed.getWidth());
        float height = Float.parseFloat(feed.getHeight());

        float p1 = height / width;
        Intent intent = new Intent(getActivity(), LikeDetailActivity.class);
        intent.putExtra("fname", feed.getFirstName());
        intent.putExtra("lname", feed.getLastName());
        intent.putExtra("company", feed.getCompanyName());
        intent.putExtra("designation", feed.getDesignation());

        intent.putExtra("heading", feed.getPostStatus());
        intent.putExtra("date", feed.getPostDate());
        intent.putExtra("Likes", feed.getTotalLikes());
        intent.putExtra("Likeflag", feed.getLikeFlag());
        intent.putExtra("Comments", feed.getTotalComments());
        intent.putExtra("profilepic", ApiConstant.profilepic + feed.getProfilePic());
        intent.putExtra("type", feed.getType());
        intent.putExtra("feedid", feed.getNewsFeedId());
        intent.putExtra("AspectRatio", p1);
        intent.putExtra("noti_type", "Wall_Post");

        if (feed.getType().equalsIgnoreCase("Image")) {
            intent.putExtra("url", ApiConstant.newsfeedwall + feed.getMediaFile());
        } else if (feed.getType().equalsIgnoreCase("Video")) {
            intent.putExtra("videourl", ApiConstant.newsfeedwall + feed.getMediaFile());
            intent.putExtra("thumbImg", ApiConstant.newsfeedwall + feed.getThumbImage());
        }
        intent.putExtra("flag", "noti");
        startActivity(intent);

//        Intent intent = new Intent(getActivity(), LikeDetailActivity.class);
//        intent.putExtra("noificationid", noificationid);
//        startActivity(intent);
//        showProgress();
//        mAPIService.postLikeUserList(token, noificationid, eventid).enqueue(new Callback<LikeListing>() {
//            @Override
//            public void onResponse(Call<LikeListing> call, Response<LikeListing> response) {
////                Log.d("","LikeListResp"+response.body());
//                if (response.body().getStatus().equalsIgnoreCase("success")) {
//                    Log.i("hit", "post submitted to API." + response.body().toString());
////                    dismissProgress();
//                    showPostLikeListresponse(response);
//                } else {
////                    dismissProgress();
//                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LikeListing> call, Throwable t) {
//                Log.e("hit", "Unable to submit post to API.");
////                dismissProgress();
//                Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
//
//            }
//        });
    }

    private void showPostLikeListresponse(Response<LikeListing> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {

            Log.e("post", "success");
            attendeeLists = new ArrayList<>();

            attendeeLists = response.body().getAttendeeList();

//            likeAdapter = new LikeAdapter(getActivity(), attendeeLists, this);
//            likeAdapter.notifyDataSetChanged();
//            likelist.setAdapter(likeAdapter);
//            likelist.scheduleLayoutAnimation();

            Intent intent = new Intent(getActivity(), LikeDetailActivity.class);
            startActivity(intent);

            if (attendeeLists.size() != 0) {
                dialog.show();
            } else {

            }

        } else {
            Log.e("post", "fail");
            Log.e("list", response.body().getAttendeeList().size() + "");
//            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        procializeDB = new DBHelper(getActivity());
        db = procializeDB.getWritableDatabase();

        //fetchFeed(token,eventid);
        if (cd.isConnectingToInternet()) {
            fetchFeed(token, eventid);
        } else {
            db = procializeDB.getReadableDatabase();

            newsfeedsDBList = dbHelper.getNewsFeedDetails();

            if (newsfeedsDBList.size() == 0) {
                NewsFeedList newsFeedList = new NewsFeedList();
                newsFeedList.setType("text");

                newsfeedsDBList.add(newsFeedList);
                feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, true);
//                feedrecycler.setSelectionFromTop(index, top);
                feedrecycler.setSelection(mCurrentX);
//                feedrecycler.scrollTo(mCurrentX, mCurrentY);

            } else {
                feedAdapter = new NewsfeedAdapter(getActivity(), newsfeedsDBList, WallFragment_POST.this, false);
                feedAdapter.notifyDataSetChanged();
                feedrecycler.setAdapter(feedAdapter);
                feedrecycler.setSelection(mCurrentX);
//                feedrecycler.scrollTo(mCurrentX, mCurrentY);
//                feedrecycler.setSelectionFromTop(index, top);
            }


        }
    }

//    @Override
//    public void onContactSelected(AttendeeList attendeeList) {
//
//        Intent attendee = new Intent(getActivity(), AttendeeDetailActivity.class);
//        attendee.putExtra("id", attendeeList.getAttendeeId());
//        attendee.putExtra("name", attendeeList.getFirstName() + "" + attendeeList.getLastName());
//        attendee.putExtra("city", attendeeList.getCity());
//        attendee.putExtra("country", attendeeList.getCountry());
//        attendee.putExtra("company", attendeeList.getCompanyName());
//        attendee.putExtra("designation", attendeeList.getDesignation());
//        attendee.putExtra("description", attendeeList.getDescription());
//        attendee.putExtra("totalrating", "");
//        attendee.putExtra("profile", attendeeList.getProfilePic());
//        startActivity(attendee);
//
//
//    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    // Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                //Toast.makeText(getActivity(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void fetchProfileDetail(String token, String eventid) {

        RequestBody token1 = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody eventid1 = RequestBody.create(MediaType.parse("text/plain"), eventid);
//        showProgress();
        mAPIService.fetchProfileDetail(token1, eventid1).enqueue(new Callback<ProfileSave>() {
            @Override
            public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        sessionManager.logoutUser();
                        Intent main = new Intent(getContext(), LoginActivity.class);
                        startActivity(main);
                        getActivity().finish();
                    } else {

                        showPfResponse(response);
                    }
//                    dismissProgress();
//                    showResponse(response);
                } else {


//                    dismissProgress();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileSave> call, Throwable t) {
                //  Toast.makeText(getActivity(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();

            }
        });
    }

    public void showPfResponse(Response<ProfileSave> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getUserData().equals(null))) {

                try {
                    String name = response.body().getUserData().getFirstName();
                    String company = response.body().getUserData().getCompanyName();
                    String designation = response.body().getUserData().getDesignation();
                    String pic = response.body().getUserData().getProfilePic();
                    String lastname = response.body().getUserData().getLastName();
                    String city = response.body().getUserData().getCity();
                    String mobno = response.body().getUserData().getMobile();
                    String email = response.body().getUserData().getEmail();
                    String country = response.body().getUserData().getCountry();
                    String description = response.body().getUserData().getDescription();
                    String attendee_status = response.body().getUserData().getAttendee_status();
                    String urid = response.body().getUserData().getUri_id();
                    String addr1 = response.body().getUserData().getAddress1();
                    String addr2 = response.body().getUserData().getAddress2();

                    Context context = getContext();
                    if (context != null) {
                        SessionManager sessionManager = new SessionManager(context);
                        if (sessionManager != null) {
                            sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno, attendee_status, urid, addr1, addr2);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {

            }
        } else {
            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
        progressbar.setIndeterminate(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrapDrawable = DrawableCompat.wrap(progressbar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, Color.parseColor(colorActive));
            progressbar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressbar.getIndeterminateDrawable().setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_IN);
        }

    }

    public void dismissProgress() {

        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_INSTANCE_STATE, feedrecycler.onSaveInstanceState());
    }
}
