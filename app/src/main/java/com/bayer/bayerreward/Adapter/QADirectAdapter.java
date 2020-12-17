package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.CustomTools.ScaledImageView;
import com.bayer.bayerreward.GetterSetter.DirectQuestion;
import com.bayer.bayerreward.GetterSetter.EventSettingList;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Utility;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZVideoPlayerStandard;

import static android.content.Context.MODE_PRIVATE;

public class QADirectAdapter extends RecyclerView.Adapter<QADirectAdapter.MyViewHolder> {

    String token, message;
    String QA_like_question, QA_reply_question;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<DirectQuestion> directQuestionLists;
    private Context context;
    private QADirectAdapterListner listener;
    private String speakername;
    private APIService mAPIService;
    public MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    private Handler myHandler = new Handler();

    public QADirectAdapter(Context context, List<DirectQuestion> directQuestionLists, QADirectAdapterListner listener) {
        this.directQuestionLists = directQuestionLists;

        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qarow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DirectQuestion question = directQuestionLists.get(position);
        holder.nameTv.setTextColor(Color.parseColor(colorActive));

        holder.nameTv.setText(question.getFirst_name());
        holder.QaTv.setText(StringEscapeUtils.unescapeJava(question.getQuestion()));

//        holder.AnsTv.setVisibility(View.GONE);

        if (holder.likeLL.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.likeLL.getLayoutParams();
            p.setMargins(5, 15, 5, 5);
            holder.likeLL.requestLayout();
        }


//
//array("jpeg", "jpg", "png", "JPEG", "JPG", "PNG", "MP4", "mp4", "mp3", "MP3", "MOV", "mov");
        if (question.getMedia().contains(".png")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);

            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);

        } else if (question.getMedia().contains(".jpeg")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);
        } else if (question.getMedia().contains(".jpg")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);
        } else if (question.getMedia().contains(".JPEG")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);
        } else if (question.getMedia().contains(".JPG")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);
        } else if (question.getMedia().contains(".PNG")) {
//            mediaPlayer.release();
            holder.feedimageIv.setVisibility(View.VISIBLE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            PicassoTrustAll.getInstance(context)
                    .load(ApiConstant.qna + question.getMedia())
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.feedimageIv);
        } else if (question.getMedia().contains(".MP4")) {
//            mediaPlayer.release();
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.VISIBLE);
            holder.jzVideoplayer.setUp(ApiConstant.qna + question.getMedia()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

            Glide.with(holder.jzVideoplayer.getContext()).load(ApiConstant.qna + question.getMedia()).into(holder.jzVideoplayer.thumbImageView);
//            holder.videoplayer.getSettings().setJavaScriptEnabled(true);
//            holder.videoplayer.getSettings().setPluginState(WebSettings.PluginState.ON);
//            holder.videoplayer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            holder.videoplayer.getSettings().setSupportMultipleWindows(true);
//            holder.videoplayer.getSettings().setSupportZoom(true);
//            holder.videoplayer.getSettings().setBuiltInZoomControls(true);
//            holder.videoplayer.getSettings().setAllowFileAccess(true);
//
//            holder.videoplayer.loadUrl(ApiConstant.qna + question.getMedia());
//            holder.videoplayer.setVisibility(View.VISIBLE);

//            Glide.with(holder.videoplayer.getContext()).load(ApiConstant.newsfeedwall + feed.getThumbImage()).into(holder.videoplayer.thumbImageView);
        } else if (question.getMedia().contains(".mp4")) {
//            mediaPlayer.release();
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.VISIBLE);
            holder.jzVideoplayer.setUp(ApiConstant.qna + question.getMedia()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

            Glide.with(holder.jzVideoplayer.getContext()).load(ApiConstant.qna + question.getMedia()).into(holder.jzVideoplayer.thumbImageView);
//            holder.videoplayer.getSettings().setJavaScriptEnabled(true);
//            holder.videoplayer.getSettings().setPluginState(WebSettings.PluginState.ON);
//            holder.videoplayer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            holder.videoplayer.getSettings().setSupportMultipleWindows(true);
//            holder.videoplayer.getSettings().setSupportZoom(true);
//            holder.videoplayer.getSettings().setBuiltInZoomControls(true);
//            holder.videoplayer.getSettings().setAllowFileAccess(true);
//
//            holder.videoplayer.loadUrl(ApiConstant.qna + question.getMedia());
//            holder.videoplayer.setVisibility(View.VISIBLE);

        } else if (question.getMedia().contains(".mp3")) {
//            mediaPlayer.release();
            holder.linear_audio.setVisibility(View.VISIBLE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            try {
//                mediaPlayer.stop();
//                mediaPlayer.reset();
//                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + question.getMedia()));
//                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            mediaPlayer.start();
            holder.seekBar_play.setClickable(false);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.VISIBLE);
            holder.feedimageIv.setVisibility(View.GONE);

            holder.img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Pausing sound", Toast.LENGTH_SHORT).show();
                    oneTimeOnly = 0;
                    mediaPlayer.release();
//                    MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
//                    mediaPlayer.release();
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.release();
//                        mediaPlayer.stop();
//                    }
                    holder.img_play.setVisibility(View.VISIBLE);
                    holder.img_pause.setVisibility(View.GONE);
//                img_play.setEnabled(false);
//                img_pause.setEnabled(true);
                }
            });


            holder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "Playing sound", Toast.LENGTH_SHORT).show();
                    oneTimeOnly = 0;
//                    mediaPlayer.reset();
//                mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + question.getMedia()));
                    try {
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
//                        mediaPlayer.setDataSource(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
//                        if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();

                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            holder.seekBar_play.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        holder.txt_timeplayed.setText(String.format("%d.%d ",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );


                        holder.img_play.setVisibility(View.GONE);
                        holder.img_pause.setVisibility(View.VISIBLE);
                        holder.seekBar_play.setProgress((int) startTime);


//                    myHandler.postDelayed(UpdateSongTime, 100);

                        if (mediaPlayer.getDuration() == mediaPlayer.getCurrentPosition()) {
                            holder.img_play.setVisibility(View.VISIBLE);
                            holder.img_pause.setVisibility(View.GONE);
                        }


//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (question.getMedia().contains(".MP3")) {
//            mediaPlayer.release();
            holder.linear_audio.setVisibility(View.VISIBLE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + question.getMedia()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.seekBar_play.setClickable(false);

            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.VISIBLE);
            holder.feedimageIv.setVisibility(View.GONE);

            holder.img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Pausing sound", Toast.LENGTH_SHORT).show();
                    oneTimeOnly = 0;
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
                    mediaPlayer.pause();
                    holder.img_play.setVisibility(View.VISIBLE);
                    holder.img_pause.setVisibility(View.GONE);
//                img_play.setEnabled(false);
//                img_pause.setEnabled(true);
                }
            });


            holder.img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                Toast.makeText(context, "Playing sound", Toast.LENGTH_SHORT).show();
                    oneTimeOnly = 0;
//                    mediaPlayer.reset();
//                mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + question.getMedia()));
                    try {
                        MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
//                        mediaPlayer.setDataSource(context, Uri.parse(ApiConstant.qna + directQuestionLists.get(position).getMedia()));
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();

                            finalTime = mediaPlayer.getDuration();
                            startTime = mediaPlayer.getCurrentPosition();

                            if (oneTimeOnly == 0) {
                                holder.seekBar_play.setMax((int) finalTime);
                                oneTimeOnly = 1;
                            }

                            holder.txt_timeplayed.setText(String.format("%d.%d ",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    finalTime)))
                            );


                            holder.img_play.setVisibility(View.GONE);
                            holder.img_pause.setVisibility(View.VISIBLE);
                            holder.seekBar_play.setProgress((int) startTime);


//                    myHandler.postDelayed(UpdateSongTime, 100);

                            if (mediaPlayer.getDuration() == mediaPlayer.getCurrentPosition()) {
                                holder.img_play.setVisibility(View.VISIBLE);
                                holder.img_pause.setVisibility(View.GONE);
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } else if (question.getMedia().contains(".MOV")) {
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.VISIBLE);
            holder.jzVideoplayer.setUp(ApiConstant.qna + question.getMedia()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

            Glide.with(holder.jzVideoplayer.getContext()).load(ApiConstant.qna + question.getMedia()).into(holder.jzVideoplayer.thumbImageView);
//            holder.videoplayer.getSettings().setJavaScriptEnabled(true);
//            holder.videoplayer.getSettings().setPluginState(WebSettings.PluginState.ON);
//            holder.videoplayer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            holder.videoplayer.getSettings().setSupportMultipleWindows(true);
//            holder.videoplayer.getSettings().setSupportZoom(true);
//            holder.videoplayer.getSettings().setBuiltInZoomControls(true);
//            holder.videoplayer.getSettings().setAllowFileAccess(true);
//            holder.videoplayer.loadUrl(ApiConstant.qna + question.getMedia());
//            holder.videoplayer.setVisibility(View.VISIBLE);

        } else if (question.getMedia().contains(".mov")) {
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.VISIBLE);
            holder.jzVideoplayer.setUp(ApiConstant.qna + question.getMedia()
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");

            Glide.with(holder.jzVideoplayer.getContext()).load(ApiConstant.qna + question.getMedia()).into(holder.jzVideoplayer.thumbImageView);
//            holder.videoplayer.getSettings().setJavaScriptEnabled(true);
//            holder.videoplayer.getSettings().setPluginState(WebSettings.PluginState.ON);
//            holder.videoplayer.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            holder.videoplayer.getSettings().setSupportMultipleWindows(true);
//            holder.videoplayer.getSettings().setSupportZoom(true);
//            holder.videoplayer.getSettings().setBuiltInZoomControls(true);
//            holder.videoplayer.getSettings().setAllowFileAccess(true);
//
//            holder.videoplayer.loadUrl(ApiConstant.qna + question.getMedia());
//            holder.videoplayer.setVisibility(View.VISIBLE);

        } else {
            holder.videoplayer.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.linear_audio.setVisibility(View.GONE);
            holder.feedimageIv.setVisibility(View.GONE);
            holder.videoplayer.setVisibility(View.GONE);
            holder.jzVideoplayer.setVisibility(View.GONE);
        }


        holder.countTv.setText(question.getTotal_likes() + " Likes");

        mAPIService = ApiUtils.getAPIService();

        SessionManager sessionManager = new SessionManager(context);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // token
        token = user.get(SessionManager.KEY_TOKEN);


        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        if (question.getAnswer() != null && QA_reply_question.equalsIgnoreCase("1")) {
            if (!question.getAnswer().equalsIgnoreCase("null")) {
                holder.AnsTv.setVisibility(View.VISIBLE);
                holder.AnsTv.setText("Ans :- " + StringEscapeUtils.unescapeJava(question.getAnswer()));
            } else {
                holder.AnsTv.setVisibility(View.GONE);
            }
        } else {
            holder.AnsTv.setVisibility(View.GONE);
        }

        SimpleDateFormat formatter = null;

        String formate1 = ApiConstant.dateformat;
        String formate2 = ApiConstant.dateformat1;

        if (Utility.isValidFormat(formate1, question.getCreated(), Locale.UK)) {
            formatter = new SimpleDateFormat(ApiConstant.dateformat);
        } else if (Utility.isValidFormat(formate2, question.getCreated(), Locale.UK)) {
            formatter = new SimpleDateFormat(ApiConstant.dateformat1);
        }
        try {
            Date date1 = formatter.parse(question.getCreated());

            DateFormat originalFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.UK);

            String date = originalFormat.format(date1);

            holder.dateTv.setText(date);

        } catch (
                ParseException e) {
            e.printStackTrace();
        }


        if (QA_like_question.equalsIgnoreCase("1")) {
            holder.likeLL.setVisibility(View.VISIBLE);
        } else {
            holder.likeLL.setVisibility(View.VISIBLE);
        }


        if (question.getLike_flag().

                equals("0")) {
            holder.likeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
        } else {
            holder.likeIv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_afterlike));
            int colorInt = Color.parseColor(colorActive);
            holder.likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

        }


    }

    @Override
    public int getItemCount() {
        return directQuestionLists.size();
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("Q&A_like_question")) {
                QA_like_question = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("Q&A_reply_question")) {
                QA_reply_question = eventSettingLists.get(i).getFieldValue();
            }
        }
    }

    public interface QADirectAdapterListner {
        //        void onContactSelected(SpeakerQuestionList question);
//
        void onLikeListener(View v, DirectQuestion question, int position, TextView count, ImageView likeIv);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, QaTv, AnsTv, countTv;
        public ImageView likeIv, playicon, img_play, img_pause, img_cancel;
        SeekBar seekBar_play;
        WebView videoplayer;
        LinearLayout linear_audio;
        ScaledImageView feedimageIv;
        TextView txt_timeplayed;
        LinearLayout likeLL;
        JZVideoPlayerStandard jzVideoplayer;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dateTv = view.findViewById(R.id.dateTv);
            QaTv = view.findViewById(R.id.QaTv);
            AnsTv = view.findViewById(R.id.AnsTv);
            countTv = view.findViewById(R.id.countTv);
            likeLL = view.findViewById(R.id.likeLL);
            playicon = view.findViewById(R.id.playicon);
            img_play = view.findViewById(R.id.img_play);
            img_pause = view.findViewById(R.id.img_pause);
            img_cancel = view.findViewById(R.id.img_cancel);
            seekBar_play = view.findViewById(R.id.seekBar_play);
            videoplayer = view.findViewById(R.id.videoplayer);
            linear_audio = view.findViewById(R.id.linear_audio);
            feedimageIv = view.findViewById(R.id.feedimageIv);
            txt_timeplayed = view.findViewById(R.id.txt_timeplayed);
            likeLL = view.findViewById(R.id.likeLL);
            jzVideoplayer = view.findViewById(R.id.jzVideoplayer);

            likeIv = view.findViewById(R.id.likeIv);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelected(directQuestionLists.get(getAdapterPosition()));
//                }
//            });
//
            likeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLikeListener(v, directQuestionLists.get(getAdapterPosition()), getAdapterPosition(), countTv, likeIv);
                }
            });
        }
    }

//    private Runnable UpdateSongTime = new Runnable() {
//        public void run() {
//            startTime = mediaPlayer.getCurrentPosition();
//            holder.txt_timeplayed.setText(String.format("%d.%d ",
//                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
//                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
//                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
//                                    toMinutes((long) startTime)))
//            );
//            holder.seekBar_play.setProgress((int) startTime);
//
//            myHandler.postDelayed(this, 100);
//
//
//        }
//    };


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
