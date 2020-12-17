package com.bayer.bayerreward.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bayer.bayerreward.Activity.AttendeeDetailActivity;
import com.bayer.bayerreward.Activity.PostActivity;
import com.bayer.bayerreward.Activity.PostActivityVideo;
import com.bayer.bayerreward.Activity.PostViewActivity;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.CustomTools.ScaledImageView;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.AttendeeList;
import com.bayer.bayerreward.GetterSetter.EventSettingList;
import com.bayer.bayerreward.GetterSetter.NewsFeedList;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Utility;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.jzvd.JZVideoPlayerStandard;

import static android.content.Context.MODE_PRIVATE;
import static com.bayer.bayerreward.Utility.Util.setTextViewDrawableColor;

public class NewsfeedAdapter extends BaseAdapter {

    String profilepic = "";
    String attendee_status = "";
    public List<NewsFeedList> feedLists;
    APIService mAPIService;
    SessionManager sessionManager;
    float p1;
    String news_feed_like, news_feed_comment, news_feed_share;
    List<EventSettingList> eventSettingLists;
    HashMap<String, String> user;
    String news_feed_post = "1", news_feed_images = "1", news_feed_video = "1", designatio = "1", company = "1";
    String topMgmtFlag;
    Boolean value;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private Context context;
    private FeedAdapterListner listener;
    private LayoutInflater inflater;
    ConnectionDetector cd;
    private List<AttendeeList> attendeeDBList;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    String substring;
    String device = Build.MODEL;

    public NewsfeedAdapter(Context con, List<NewsFeedList> feedLists, FeedAdapterListner listener, Boolean value) {

        this.feedLists = feedLists;
        this.listener = listener;
        this.context = con;
        this.value = value;
        if (con != null) {
            SessionManager sessionManager = new SessionManager(con);
            user = sessionManager.getUserDetails();
            profilepic = user.get(SessionManager.KEY_PIC);
            attendee_status = user.get(SessionManager.ATTENDEE_STATUS);
            topMgmtFlag = sessionManager.getSkipFlag();
            SharedPreferences prefs = con.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            colorActive = prefs.getString("colorActive", "");
            cd = new ConnectionDetector(context);
        }

        procializeDB = new DBHelper(context);
        db = procializeDB.getWritableDatabase();
        dbHelper = new DBHelper(context);
    }


    @Override
    public int getCount() {
        return feedLists.size();
    }

    @Override
    public Object getItem(int position) {
        return feedLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        NewsFeedList feed;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.newsfeedlistingrow,
                    null);

            holder = new ViewHolder();

            holder.nameTv = convertView.findViewById(R.id.nameTv);
            holder.companyTv = convertView.findViewById(R.id.companyTv);
            holder.designationTv = convertView.findViewById(R.id.designationTv);
            holder.dateTv = convertView.findViewById(R.id.dateTv);
            holder.headingTv = convertView.findViewById(R.id.headingTv);
            holder.testdata = convertView.findViewById(R.id.testdata);

            holder.likeTv = convertView.findViewById(R.id.likeTv);
            holder.commentTv = convertView.findViewById(R.id.commentTv);
            holder.shareTv = convertView.findViewById(R.id.shareTv);
            holder.img_like = convertView.findViewById(R.id.img_like);

            holder.liketext = convertView.findViewById(R.id.liketext);
            holder.commenttext = convertView.findViewById(R.id.commenttext);

            holder.feedimageIv = convertView.findViewById(R.id.feedimageIv);
            holder.VideoView = convertView.findViewById(R.id.videoplayer);

            holder.profileIv = convertView.findViewById(R.id.profileIV);


            holder.progressView = convertView.findViewById(R.id.progressView);
            holder.feedprogress = convertView.findViewById(R.id.feedprogress);

            holder.playicon = convertView.findViewById(R.id.playicon);
            holder.moreIV = convertView.findViewById(R.id.moreIV);
            holder.editIV = convertView.findViewById(R.id.editIV);

            holder.viewone = convertView.findViewById(R.id.viewone);
            holder.viewtwo = convertView.findViewById(R.id.viewtwo);

            holder.txtfeedRv = convertView.findViewById(R.id.txtfeedRv);
            holder.mindTv = convertView.findViewById(R.id.mindTv);
            holder.imagefeedRv = convertView.findViewById(R.id.imagefeedRv);
            holder.videofeedRv = convertView.findViewById(R.id.videofeedRv);
            holder.post_layout = convertView.findViewById(R.id.post_layout);
            holder.feedll = convertView.findViewById(R.id.feedll);

            holder.view = convertView.findViewById(R.id.view);
            holder.viewteo = convertView.findViewById(R.id.viewteo);


            holder.mainLLpost = convertView.findViewById(R.id.mainLLpost);


            holder.profilestatus = convertView.findViewById(R.id.profilestatus);

            if (profilepic != null) {
//                Glide.with(context).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
//
//                        holder.profilestatus.setImageResource(R.drawable.profilepic_placeholder);
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, com.bumptech.glide.request.target.Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                        return false;
//                    }
//                }).into(holder.profilestatus);
                PicassoTrustAll.getInstance(context)
                        .load(ApiConstant.profilepic + profilepic)
                        .placeholder(R.drawable.profilepic_placeholder)
                        .into(holder.profilestatus);

            }


            weightapply(holder.txtfeedRv, holder.imagefeedRv, holder.videofeedRv, holder.viewone, holder.viewteo);

            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position == 0) {

            if (value == true) {
                if (topMgmtFlag.equalsIgnoreCase("1")) {
                    holder.post_layout.setVisibility(RelativeLayout.GONE);

                } else {
                    holder.post_layout.setVisibility(RelativeLayout.GONE);

                }
                holder.feedll.setVisibility(RelativeLayout.GONE);

                holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.mindTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.txtfeedRv.getVisibility() == View.VISIBLE) {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "text");
                            context.startActivity(postview);
                        } else if (holder.imagefeedRv.getVisibility() == View.VISIBLE) {
                            Intent postview = new Intent(context, PostActivity.class);
                            postview.putExtra("for", "image");
                            context.startActivity(postview);
                        } else if (holder.videofeedRv.getVisibility() == View.VISIBLE) {
                            if (device.equalsIgnoreCase("vivo V3")) {
                                Intent postview = new Intent(context, PostActivityVideo.class);
                                postview.putExtra("for", "video");
                                context.startActivity(postview);
                            } else {
                                Intent postview = new Intent(context, PostViewActivity.class);
                                postview.putExtra("for", "video");
                                context.startActivity(postview);
                            }
                        }
//                getActivity().finish();
                    }
                });

                holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }
//                getActivity().finish();
                    }
                });
            } else {
                if (topMgmtFlag.equalsIgnoreCase("1")) {
                    holder.post_layout.setVisibility(RelativeLayout.GONE);

                } else {
                    holder.post_layout.setVisibility(RelativeLayout.GONE);

                }
                holder.feedll.setVisibility(RelativeLayout.VISIBLE);
                holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.mindTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (holder.txtfeedRv.getVisibility() == View.VISIBLE) {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "text");
                            context.startActivity(postview);
                        } else if (holder.imagefeedRv.getVisibility() == View.VISIBLE) {
                            Intent postview = new Intent(context, PostActivity.class);
                            postview.putExtra("for", "image");
                            context.startActivity(postview);
                        } else if (holder.videofeedRv.getVisibility() == View.VISIBLE) {
                            if (device.equalsIgnoreCase("vivo V3")) {
                                Intent postview = new Intent(context, PostActivityVideo.class);
                                postview.putExtra("for", "video");
                                context.startActivity(postview);
                            } else {
                                Intent postview = new Intent(context, PostViewActivity.class);
                                postview.putExtra("for", "video");
                                context.startActivity(postview);
                            }
                        }
//                getActivity().finish();
                    }
                });

                holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent postview = new Intent(context, PostActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
//                getActivity().finish();
                    }
                });

                holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (device.equalsIgnoreCase("vivo V3")) {
                            Intent postview = new Intent(context, PostActivityVideo.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        } else {
                            Intent postview = new Intent(context, PostViewActivity.class);
                            postview.putExtra("for", "video");
                            context.startActivity(postview);
                        }
                    }
                });
            }
        } else {
            holder.post_layout.setVisibility(RelativeLayout.GONE);
        }

        holder.nameTv.setTextColor(Color.parseColor(colorActive));
        feed = feedLists.get(position);
        if (feed.getLastName() == null) {
            holder.nameTv.setText(feed.getFirstName());
        } else {
            holder.nameTv.setText(feed.getFirstName() + " " + feed.getLastName());
        }

        if (designatio.equalsIgnoreCase("0")) {
            holder.designationTv.setVisibility(View.GONE);
        } else {
            holder.designationTv.setText(feed.getDesignation());
            holder.designationTv.setVisibility(View.GONE);
        }

        if (company.equalsIgnoreCase("0")) {
            holder.companyTv.setVisibility(View.GONE);
        } else {
            holder.companyTv.setText(feed.getCompanyName());
            holder.companyTv.setVisibility(View.GONE);
        }

        holder.testdata.setText(StringEscapeUtils.unescapeJava(feed.getPostStatus()));

        final SpannableStringBuilder stringBuilder = new SpannableStringBuilder(holder.testdata.getText());
        if (feed.getPostStatus() != null) {

            holder.headingTv.setVisibility(View.VISIBLE);
//                    holder.wallNotificationText.setText(getEmojiFromString(notificationImageStatus));
            int flag = 0;
            for (int i = 0; i < stringBuilder.length(); i++) {
                String sample = stringBuilder.toString();
                if ((stringBuilder.charAt(i) == '<')) {
                    try {
                        String text = "<";
                        String text1 = ">";

                        if (flag == 0) {
                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            context.startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.replace(start, end + 1, substring);
                                    holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    holder.headingTv.setMovementMethod(LinkMovementMethod.getInstance());
                                    holder.headingTv.setText(stringBuilder);
                                    flag = 1;
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        } else {

                            int start = sample.indexOf(text, i);
                            int end = sample.indexOf(text1, i);

                            Log.v("Indexes of", "Start : " + start + "," + end);
                            try {
                                substring = sample.substring(start, end + 1);
                                Log.v("String names: ", substring);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (substring.contains("<")) {
                                if (sample.contains(substring)) {
                                    substring = substring.replace("<", "");
                                    substring = substring.replace(">", "");
                                    int index = substring.indexOf("^");
//                                    substring = substring.replace("^", "");
                                    final String attendeeid = substring.substring(0, index);
                                    substring = substring.substring(index + 1, substring.length());


                                    stringBuilder.setSpan(stringBuilder, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    stringBuilder.setSpan(new ForegroundColorSpan(Color.RED), start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.setSpan(new ClickableSpan() {
                                        @Override
                                        public void onClick(View widget) {
                                            attendeeDBList = dbHelper.getAttendeeDetailsId(attendeeid);
                                            Intent intent = new Intent(context, AttendeeDetailActivity.class);
                                            intent.putExtra("id", attendeeDBList.get(0).getAttendeeId());
                                            intent.putExtra("name", attendeeDBList.get(0).getFirstName() + " " + attendeeDBList.get(0).getLastName());
                                            intent.putExtra("city", attendeeDBList.get(0).getCity());
                                            intent.putExtra("country", attendeeDBList.get(0).getCountry());
                                            intent.putExtra("company", attendeeDBList.get(0).getCompanyName());
                                            intent.putExtra("designation", attendeeDBList.get(0).getDesignation());
                                            intent.putExtra("description", attendeeDBList.get(0).getDescription());
                                            intent.putExtra("profile", attendeeDBList.get(0).getProfilePic());
                                            context.startActivity(intent);
                                        }
                                    }, start, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                    stringBuilder.replace(start, end + 1, substring);
                                    holder.testdata.setText(stringBuilder, TextView.BufferType.SPANNABLE);
                                    holder.headingTv.setMovementMethod(LinkMovementMethod.getInstance());

                                    holder.headingTv.setText(stringBuilder);


//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        holder.attendee_comments.setText(attendees.getComment().indexOf(substring, start));
//                        attendees.setComment(substring);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            holder.headingTv.setText(stringBuilder);
        } else {
            holder.headingTv.setVisibility(View.GONE);
        }

        holder.liketext.setText(feed.getTotalLikes() + " Likes ");
        holder.commenttext.setText(feed.getTotalComments() + " Comments ");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        eventSettingLists = SessionManager.loadEventList();
        holder.liketext.setFocusable(true);

        if (attendee_status.equalsIgnoreCase("1")) {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feedLists.get(position).getAttendeeId())) {
                holder.editIV.setVisibility(View.GONE);
                holder.moreIV.setVisibility(View.VISIBLE);
            } else {
                holder.editIV.setVisibility(View.GONE);
                holder.moreIV.setVisibility(View.VISIBLE);
            }
        } else {
            if (user.get(SessionManager.KEY_ID).equalsIgnoreCase(feedLists.get(position).getAttendeeId())) {
                holder.editIV.setVisibility(View.GONE);
                holder.moreIV.setVisibility(View.VISIBLE);
//            reportTv.setVisibility(View.GONE);
//            reportuserTv.setVisibility(View.GONE);
//            blockuserTv.setVisibility(View.GONE);
            } else {
                holder.editIV.setVisibility(View.GONE);
                holder.moreIV.setVisibility(View.GONE);
//            deleteTv.setVisibility(View.GONE);
//            hideTv.setVisibility(View.VISIBLE);
//            reportTv.setVisibility(View.VISIBLE);
//            reportuserTv.setVisibility(View.VISIBLE);
//            blockuserTv.setVisibility(View.VISIBLE);
            }
        }
//        weightapply(holder.likeTv, holder.commentTv, holder.shareTv, holder.viewone, holder.viewtwo);

//        try {
//            float width = Float.parseFloat(feed.getWidth());
//            float height = Float.parseFloat(feed.getHeight());
//
//            p1 = (float) (height / width);
//            holder.feedimageIv.setAspectRatio(p1);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (feed.getLikeFlag() != null) {
            if (feed.getLikeFlag().equals("1")) {

                // holder.img_like.setBackgroundResource(R.drawable.ic_afterlike);

                holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);
                int colorInt = Color.parseColor(colorActive);
                setTextViewDrawableColor(holder.img_like, colorActive);

            /*ColorStateList csl = ColorStateList.valueOf(colorInt);
            Drawable drawable = DrawableCompat.wrap(holder.img_like.getDrawableState());
            DrawableCompat.setTintList(drawable, csl);
            holder.img_like.setImageDrawable(drawable);
*/

            } else {
                holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
//            holder.img_like.setBackgroundResource(R.drawable.ic_like);
            }
        }

        if (feed.getPostDate() != null) {
            SimpleDateFormat formatter = null;

            String formate1 = ApiConstant.dateformat;
            String formate2 = ApiConstant.dateformat1;

            if (Utility.isValidFormat(formate1, feed.getPostDate(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat);
            } else if (Utility.isValidFormat(formate2, feed.getPostDate(), Locale.UK)) {
                formatter = new SimpleDateFormat(ApiConstant.dateformat1);
            }

            try {
                Date date1 = formatter.parse(feed.getPostDate());

                //DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);
                DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

                String date = originalFormat.format(date1);

                holder.dateTv.setText(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (feed.getProfilePic() != null) {

//            Glide.with(context).load(ApiConstant.profilepic + feed.getProfilePic())
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.progressView.setVisibility(View.GONE);
//                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
//                    return true;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.progressView.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(holder.profileIv).onLoadStarted(context.getDrawable(R.drawable.profilepic_placeholder));

            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.profilepic + feed.getProfilePic())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.profileIv);

        } else {
            holder.progressView.setVisibility(View.GONE);
            holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);

        }


        if (feed.getType().equals("Image")) {
            //photo

            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.playicon.setVisibility(View.GONE);
            holder.VideoView.setVisibility(View.GONE);

//            Glide.with(context).load((ApiConstant.newsfeedwall + feed.getMediaFile()))
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(holder.feedimageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.newsfeedwall + feed.getMediaFile())
                    .placeholder(R.drawable.gallery_placeholder)
                    .into(holder.feedimageIv);


        } else if (feed.getType().equals("Video")) {
            //video

            holder.feedimageIv.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            if (holder.feedprogress.getVisibility() == View.VISIBLE) {
                holder.feedprogress.setVisibility(View.GONE);
            }
            holder.VideoView.setVisibility(View.VISIBLE);

//
//            if (holder.VideoView.isCurrentPlay())
//            {
//
//                try{
//                    holder.VideoView.backPress();
//                    holder.VideoView.onStatePrepared();
////                    holder.VideoView.releaseAllVideos();
//                    holder.VideoView.playOnThisJzvd();
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }

            holder.VideoView.setUp(ApiConstant.newsfeedwall + feed.getMediaFile()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

            Glide.with(holder.VideoView.getContext()).load(ApiConstant.newsfeedwall + feed.getThumbImage()).into(holder.VideoView.thumbImageView);


//            JZVideoPlayer.setJzUserAction(new MyUserActionStandard());

//
//            holder.VideoView.thumbImageView.setImageURI(uri);
//            holder.VideoView.setUp(ApiConstant.newsfeedwall+feed.getMediaFile(),JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,"");

//            Glide.with(context).load((ApiConstant.newsfeedwall + feed.getThumbImage()))
//                    .apply(RequestOptions.skipMemoryCacheOf(true))
//                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.feedprogress.setVisibility(View.GONE);
//                    return false;
//                }
//            }).into(holder.feedimageIv).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        } else {
            holder.feedimageIv.setVisibility(View.GONE);
            holder.playicon.setVisibility(View.GONE);
            holder.feedprogress.setVisibility(View.GONE);
            holder.VideoView.setVisibility(View.GONE);

        }


//
//        holder.likeTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        if (news_feed_post.equalsIgnoreCase("0")) {
            holder.txtfeedRv.setVisibility(View.GONE);

            holder.view.setVisibility(View.GONE);
        } else {
            holder.txtfeedRv.setVisibility(View.VISIBLE);

            holder.view.setVisibility(View.VISIBLE);
        }

        if (news_feed_images.equalsIgnoreCase("0")) {
            holder.imagefeedRv.setVisibility(View.GONE);
            holder.viewteo.setVisibility(View.GONE);
        } else {
            holder.imagefeedRv.setVisibility(View.VISIBLE);
            holder.viewteo.setVisibility(View.VISIBLE);
        }

        if (news_feed_video.equalsIgnoreCase("0")) {
            holder.videofeedRv.setVisibility(View.GONE);
            holder.viewteo.setVisibility(View.GONE);
        } else {
            holder.videofeedRv.setVisibility(View.VISIBLE);
            holder.viewteo.setVisibility(View.VISIBLE);
        }

        if (news_feed_images.equalsIgnoreCase("0") && news_feed_post.equalsIgnoreCase("0") && news_feed_video.equalsIgnoreCase("0")) {
            holder.mainLLpost.setVisibility(View.GONE);
            holder.mindTv.setVisibility(View.GONE);
        } else {
            holder.mainLLpost.setVisibility(View.VISIBLE);
            holder.mindTv.setVisibility(View.VISIBLE);
        }

        holder.feedimageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onContactSelected(feedLists.get(position), holder.feedimageIv);
            }
        });


        holder.likeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    listener.likeTvViewOnClick(v, feedLists.get(position), position, holder.img_like, holder.liketext);
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commentTvViewOnClick(v, feedLists.get(position), position);

            }
        });


        holder.shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.shareTvFollowOnClick(v, feedLists.get(position));
            }
        });

        holder.moreIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.moreTvFollowOnClick(v, feedLists.get(position), position);

//                    feedLists.remove(getAdapterPosition());
//                    notifyItemRemoved(getAdapterPosition());
            }
        });


        holder.liketext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.moreLikeListOnClick(v, feedLists.get(position), position);


            }
        });


        holder.editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.FeedEditOnClick(v, feedLists.get(position), position);

            }
        });

        holder.profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiConstant.profilepic + feedLists.get(position).getProfilePic();
                imagealert(url);
            }
        });

        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedLists.get(position).getAttendee_type().equalsIgnoreCase("A")) {
                    attendeeDBList = dbHelper.getAttendeeDetailsId(feedLists.get(position).getAttendeeId());
                    Intent intent = new Intent(context, AttendeeDetailActivity.class);
                    intent.putExtra("id", feedLists.get(position).getAttendeeId());
                    intent.putExtra("name", feedLists.get(position).getFirstName() + " " + feedLists.get(position).getLastName());
                    intent.putExtra("city", attendeeDBList.get(0).getCity());
                    intent.putExtra("country", attendeeDBList.get(0).getCountry());
                    intent.putExtra("company", feedLists.get(position).getCompanyName());
                    intent.putExtra("designation", feedLists.get(position).getDesignation());
                    intent.putExtra("description", attendeeDBList.get(0).getDescription());
                    intent.putExtra("profile", feedLists.get(position).getProfilePic());
                    context.startActivity(intent);
                }
            }
        });

        if (news_feed_like.equalsIgnoreCase("0")) {
            holder.likeTv.setVisibility(View.GONE);
//            holder.viewone.setVisibility(View.GONE);
        } else {
            holder.likeTv.setVisibility(View.VISIBLE);
//            holder.viewone.setVisibility(View.VISIBLE);
        }

        if (news_feed_comment.equalsIgnoreCase("0")) {
            holder.commentTv.setVisibility(View.GONE);
//            holder.viewtwo.setVisibility(View.GONE);

        } else {
            holder.commentTv.setVisibility(View.VISIBLE);
//            holder.viewtwo.setVisibility(View.VISIBLE);
        }

        if (news_feed_share.equalsIgnoreCase("0")) {
            holder.shareTv.setVisibility(View.GONE);
//            holder.viewtwo.setVisibility(View.GONE);
        } else {
            holder.shareTv.setVisibility(View.GONE);
//            holder.viewtwo.setVisibility(View.VISIBLE);
        }

//        if (news_feed_comment.equalsIgnoreCase("0") && news_feed_share.equalsIgnoreCase("0")) {
//            holder.viewtwo.setVisibility(View.GONE);
////            holder.viewone.setVisibility(View.GONE);
//        } else {
//            holder.viewtwo.setVisibility(View.VISIBLE);
////            holder.viewone.setVisibility(View.VISIBLE);
//        }


        return convertView;
    }

    private void weightapply(RelativeLayout likeTv, RelativeLayout commentTv, RelativeLayout shareTv, View viewone, View viewtwo) {

        if (likeTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.VISIBLE);

        } else if (commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );
            likeTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.5f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);

//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.GONE);


        } else if (likeTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.VISIBLE && shareTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            likeTv.setLayoutParams(param);
            commentTv.setLayoutParams(param);
            shareTv.setLayoutParams(param);


//            viewone.setVisibility(View.VISIBLE);
//            viewtwo.setVisibility(View.VISIBLE);

        } else if (shareTv.getVisibility() == View.VISIBLE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            shareTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.VISIBLE && likeTv.getVisibility() == View.GONE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );
            commentTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);

        } else if (shareTv.getVisibility() == View.GONE && commentTv.getVisibility() == View.GONE && likeTv.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3.0f
            );

            likeTv.setLayoutParams(param);

//            viewone.setVisibility(View.GONE);
//            viewtwo.setVisibility(View.GONE);
        }

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
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_video")) {
                news_feed_video = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_post")) {
                news_feed_post = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("news_feed_images")) {
                news_feed_images = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_designation")) {
                designatio = eventSettingLists.get(i).getFieldValue();
            }
            if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("edit_profile_company")) {
                company = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    public interface FeedAdapterListner {
        void onContactSelected(NewsFeedList feed, ImageView imageView);

        void likeTvViewOnClick(View v, NewsFeedList feed, int position, TextView likeimage, TextView liketext);

        void commentTvViewOnClick(View v, NewsFeedList feed, int position);

        void shareTvFollowOnClick(View v, NewsFeedList feed);

        void moreTvFollowOnClick(View v, NewsFeedList feed, int position);

        void moreLikeListOnClick(View v, NewsFeedList feed, int position);

        void FeedEditOnClick(View v, NewsFeedList feed, int position);
    }

    static class ViewHolder {
        public TextView nameTv, designationTv, companyTv, dateTv, headingTv, liketext, commenttext, sharetext, img_like, testdata;
        public ImageView img_vol, img_playback;
        public ProgressBar progressView, feedprogress;
        public ScaledImageView feedimageIv, profileIv, profilestatus;
        public ImageView playicon, moreIV, editIV;
        public View viewone, viewtwo, viewteo, view;
        RelativeLayout txtfeedRv, imagefeedRv, videofeedRv;
        private LinearLayout likeTv, commentTv, shareTv, mindTv, mainLLpost, post_layout, feedll;
        private JZVideoPlayerStandard VideoView;

    }

    public void imagealert(String URL) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                                | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setContentView(R.layout.imagepopulayout);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
//        String imgae = dbManager.GetimageUrl(datamodel.get(position).getProdcutid());
//        String imageUrl = ApiConstant.profilepic + feed.getProfilePic();
        Picasso.with(context).load(URL).into(image);
        dialog.show();
    }


}
