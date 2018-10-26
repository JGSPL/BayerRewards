package com.procialize.vivo_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.procialize.vivo_app.Activity.PostActivity;
import com.procialize.vivo_app.Activity.PostViewActivity;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiConstant;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.CustomTools.MyJZVideoPlayerStandard;
import com.procialize.vivo_app.CustomTools.PicassoTrustAll;
import com.procialize.vivo_app.CustomTools.ScaledImageView;
import com.procialize.vivo_app.GetterSetter.EventSettingList;
import com.procialize.vivo_app.GetterSetter.LikePost;
import com.procialize.vivo_app.GetterSetter.NewsFeedList;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;

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

import static android.content.Context.MODE_PRIVATE;

public class NewsfeedAdapter extends BaseAdapter {

    public List<NewsFeedList> feedLists;
    private Context context;
    APIService mAPIService;
    SessionManager sessionManager;
    float p1;
    private FeedAdapterListner listener;
    String news_feed_like, news_feed_comment, news_feed_share;
    List<EventSettingList> eventSettingLists;
    HashMap<String, String> user;
    private LayoutInflater inflater;
    final String profilepic;
    String news_feed_post = "1", news_feed_images = "1", news_feed_video = "1";
    String topMgmtFlag;
    String userType, noty_id;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    String token;


    public NewsfeedAdapter(Context con, List<NewsFeedList> feedLists, FeedAdapterListner listener) {

        this.feedLists = feedLists;
        this.listener = listener;
        this.context = con;
        SessionManager sessionManager = new SessionManager(con);
        user = sessionManager.getUserDetails();
        profilepic = user.get(SessionManager.KEY_PIC);
        token = user.get(SessionManager.KEY_TOKEN);
        topMgmtFlag = sessionManager.getSkipFlag();
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

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

            if (topMgmtFlag.equalsIgnoreCase("1")) {
                holder.post_layout.setVisibility(RelativeLayout.VISIBLE);

            } else {
                holder.post_layout.setVisibility(RelativeLayout.GONE);

            }

           /* holder.txtfeedRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postview = new Intent(context, PostViewActivity.class);
                    postview.putExtra("for", "text");
                    context.startActivity(postview);
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
                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
                    } else if (holder.videofeedRv.getVisibility() == View.VISIBLE) {
                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "video");
                        context.startActivity(postview);
                    }
//                getActivity().finish();
                }
            });*/
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

                    if(holder.txtfeedRv.getVisibility()==View.VISIBLE) {
                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "text");
                        context.startActivity(postview);
                    }else if(holder.imagefeedRv.getVisibility()==View.VISIBLE){
                        Intent postview = new Intent(context, PostActivity.class);
                        postview.putExtra("for", "image");
                        context.startActivity(postview);
                    }else if(holder.videofeedRv.getVisibility()==View.VISIBLE){
                        Intent postview = new Intent(context, PostViewActivity.class);
                        postview.putExtra("for", "video");
                        context.startActivity(postview);
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

                    Intent postview = new Intent(context, PostViewActivity.class);

                    postview.putExtra("for", "video");
                    context.startActivity(postview);
//                getActivity().finish();
                }
            });

           /* holder.imagefeedRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postview = new Intent(context, PostViewActivity.class);
                    postview.putExtra("for", "image");
                    context.startActivity(postview);
//                getActivity().finish();
                }
            });

            holder.videofeedRv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent postview = new Intent(context, PostViewActivity.class);

                    postview.putExtra("for", "video");
                    context.startActivity(postview);
//                getActivity().finish();
                }
            });*/
        } else {
            holder.post_layout.setVisibility(RelativeLayout.GONE);
        }
        feed = feedLists.get(position);
        if (feed.getLastName() == null) {
            holder.nameTv.setText(feed.getFirstName());
        } else {
            holder.nameTv.setText(feed.getFirstName() + " " + feed.getLastName());
        }

        holder.companyTv.setText(feed.getCompanyName());
        holder.designationTv.setText(feed.getDesignation());
        holder.headingTv.setText(StringEscapeUtils.unescapeJava(feed.getPostStatus()));
        holder.liketext.setText(feed.getTotalLikes() + " Likes ");
        holder.commenttext.setText(feed.getTotalComments() + " Comments ");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);

        eventSettingLists = sessionManager.loadEventList();
        holder.liketext.setFocusable(true);

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

        if (feed.getLikeFlag().equals("1")) {


            holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);

//            feed.notify();
//            holder.img_like.setBackgroundResource(R.drawable.ic_afterlike);

        } else {
            holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
//            holder.img_like.setBackgroundResource(R.drawable.ic_like);
//            feed.notify();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = formatter.parse(feed.getPostDate());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM , HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);

        } catch (ParseException e) {
            e.printStackTrace();
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
                    , JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");

            Glide.with(holder.VideoView.getContext()).load(ApiConstant.newsfeedwall + feed.getMediaFile()).into(holder.VideoView.thumbImageView);


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


        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.likeTvViewOnClick(v, feedLists.get(position), position, holder.img_like, holder.liketext);

//                if (topMgmtFlag.equalsIgnoreCase("0")) {// // TODO
                // holder.like_btn.setEnabled(false);
                // holder.like_btn.setClickable(false);
                // // holder.like_btn.setAlpha(0.5f);
                // // holder.like_count_text.setText("");

//                Drawable[] drawables = holder.img_like.getCompoundDrawables();
//                Bitmap bitmap = ((BitmapDrawable) drawables[2]).getBitmap();
//
//                Bitmap bitmap2 = ((BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_like)).getBitmap();
//                int count = Integer.parseInt(feedLists.get(position).getTotalLikes());
//
////        if(!drawables[2].equals(R.drawable.ic_like)){
//                if (bitmap == bitmap2) {
//
////                    if (feedLists.get(position).getLikeFlag()
////                            .equalsIgnoreCase("0")) {
//                    // holder.like_btn.setEnabled(true);
//                    // holder.like_btn.setClickable(true);
//                    // holder.like_btn.setAlpha(1f);
//
////                        holder.img_like.setBackgroundResource(R.drawable.ic_like);
//                    holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_afterlike, 0);
//
//                    int likeCount = Integer.parseInt(feedLists
//                            .get(position).getTotalLikes());
//                    count = count + 1;
//
//                    holder.liketext.setText(count + " Likes");
//
//                } else {
//                    // holder.like_btn.setEnabled(false);
//                    // holder.like_btn.setClickable(false);
//                    // holder.like_btn.setAlpha(0.5f);
//
////                        holder.img_like.setBackgroundResource(R.drawable.ic_afterlike);
//
//                    holder.img_like.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_like, 0);
////                    int count = Integer.parseInt(feedLists
////                            .get(position).getTotalLikes());
//
//                    if (count > 0) {
//                        count = count - 1;
//                    } else {
//
//                        count = 0;
//                    }
//
//                    holder.liketext.setText(count + " Likes");
//
//                }
//
//                int likeCount = Integer.parseInt(feedLists.get(
//                        position).getTotalLikes());
//                likeCount = likeCount + 1;
//                // holder.like_count_text.setText(likeCount + " Likes");
//                userType = feedLists.get(position)
//                        .getType();
//                noty_id = feedLists.get(position)
//                        .getNewsFeedId();
//                PostLike(eventid, noty_id, token);
//
////                    new updateLike().execute();
////                } else {
////
////                    Toast.makeText(context, "Only for Registered Users",
////                            Toast.LENGTH_SHORT).show();
////                }
//
            }

        });

        holder.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commentTvViewOnClick(v, feedLists.get(position));

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
            holder.shareTv.setVisibility(View.VISIBLE);
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

    static class ViewHolder {
        public TextView nameTv, designationTv, companyTv, dateTv, headingTv, liketext, commenttext, sharetext, img_like;
        private LinearLayout likeTv, commentTv, shareTv, mindTv, mainLLpost, post_layout;
        public ImageView img_vol, img_playback;
        public ProgressBar progressView, feedprogress;
        public ScaledImageView feedimageIv, profileIv, profilestatus;
        public ImageView playicon, moreIV, editIV;
        public View viewone, viewtwo, viewteo, view;
        private MyJZVideoPlayerStandard VideoView;
        RelativeLayout txtfeedRv, imagefeedRv, videofeedRv;

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

    public interface FeedAdapterListner {
        void onContactSelected(NewsFeedList feed, ImageView imageView);

        void likeTvViewOnClick(View v, NewsFeedList feed, int position, TextView likeimage, TextView liketext);

        void commentTvViewOnClick(View v, NewsFeedList feed);

        void shareTvFollowOnClick(View v, NewsFeedList feed);

        void moreTvFollowOnClick(View v, NewsFeedList feed, int position);

        void moreLikeListOnClick(View v, NewsFeedList feed, int position);

        void FeedEditOnClick(View v, NewsFeedList feed, int position);
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
                    Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                dismissProgress();
                Toast.makeText(context, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPostlikeresponse(Response<LikePost> response) {

        if (response.body().getStatus().equals("Success")) {
            Log.e("post", "success");
//            feedAdapter.notifyDataSetChanged();
//            fetchFeed(token, eventid);
        } else {
            Log.e("post", "fail");
            Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

}
