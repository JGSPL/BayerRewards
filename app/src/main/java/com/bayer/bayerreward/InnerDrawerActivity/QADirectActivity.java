package com.bayer.bayerreward.InnerDrawerActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bayer.bayerreward.Adapter.QADirectAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.CustomTools.ProgressRequestBodyVideo;
import com.bayer.bayerreward.GetterSetter.DirectQuestion;
import com.bayer.bayerreward.GetterSetter.PostTextFeed;
import com.bayer.bayerreward.GetterSetter.QADirectFetch;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.MyApplication;
import com.bayer.bayerreward.Utility.ScalingUtilities;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.Utility.Utility;
import com.bayer.bayerreward.utils.CommonFunction;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.jzvd.JZVideoPlayerStandard;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.Utility.Utility.setgradientDrawable;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;
import static org.apache.http.HttpVersion.HTTP_1_1;

public class QADirectActivity extends AppCompatActivity implements QADirectAdapter.QADirectAdapterListner, ProgressRequestBodyVideo.UploadCallbacks {

    public Button postbtn;
    public QADirectAdapter qaAttendeeAdapter;
    Dialog myDialog;
    String token;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    SwipeRefreshLayout qaRvrefresh;
    ProgressBar progressBar, progressBardia;
    RecyclerView qaRv;
    ImageView headerlogoIv;
    RelativeLayout linUpper;
    TextView txtEmpty, nmtxt, pullrefresh;
    private APIService mAPIService;
    LinearLayout linear;
    RelativeLayout relative_audio, relative_video;
    VideoView Upvideov;
    TextView txt_time, txt_timeplayed;
    Button audio_record_button, browse_audio, browse_media;
    ImageView image_upload, imgPlay, Uploadiv, img_play, img_pause, img_cancel;
    SeekBar seekBar_play;
    final String[] options = {"Select Image", "Select Video"};
    String flag;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    String userChoosenTask;
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_TAKE_GALLERY_VIDEO = 3;
    String mCurrentPhotoPath;
    MyApplication appDelegate;
    private Uri uri;
    String videoUrl;
    File file;
    String selectedPath = "";
    public static File imgeFile;
    String angle = "0";
    private static final int SELECT_AUDIO = 4;
    private MediaPlayer mediaPlayer;
    LinearLayout linear_audio;
    private Handler myHandler = new Handler();
    public static int oneTimeOnly = 0;
    private double startTime = 0;
    private double finalTime = 0;
    Handler handler;
    String fileName, picturePath;
    Boolean isRecording;
    int recordTime, playTime;
    MediaRecorder mRecorder;
    File sourceFile;
    String postUrl;
    String msg;
    Button cancelbtn, ratebtn;
    String apikey = "";
    SessionManager sessionManager;
    EditText etmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qadirect);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        apikey = user.get(SessionManager.KEY_TOKEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressBar = findViewById(R.id.progressBar);
        qaRvrefresh = findViewById(R.id.qaRvrefresh);
        postbtn = findViewById(R.id.postbtn);
        qaRv = findViewById(R.id.qaRv);
        linUpper = findViewById(R.id.linUpper);
        txtEmpty = findViewById(R.id.txtEmpty);
        nmtxt = findViewById(R.id.nmtxt);
        linear = findViewById(R.id.linear);
        pullrefresh = findViewById(R.id.pullrefresh);

        appDelegate = (MyApplication) getApplicationContext();

        GradientDrawable shape = setgradientDrawable(5, colorActive);
        postbtn.setBackground(shape);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#0092df"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                JZVideoPlayerStandard.releaseAllVideos();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        nmtxt.setTextColor(Color.parseColor(colorActive));
        pullrefresh.setTextColor(Color.parseColor(colorActive));
        txtEmpty.setTextColor(Color.parseColor(colorActive));

        mAPIService = ApiUtils.getAPIService();

        postUrl = ApiConstant.baseUrl + "QADirectPost";

        SessionManager sessionManager = new SessionManager(QADirectActivity.this);

        token = user.get(SessionManager.KEY_TOKEN);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        qaRv.setLayoutManager(mLayoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        // qaRv.setLayoutAnimation(animation);

        QAFetch(token, eventid);


        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showratedialouge();
            }
        });

        qaRvrefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                QAFetch(token, eventid);
            }
        });
        CommonFunction.crashlytics("QADiredct", "");
        firbaseAnalytics(this, "QADiredct", "");

        qaRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                JZVideoPlayerStandard.releaseAllVideos();
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(QADirectActivity.this);

//                recorderTask(1);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                }/* else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent(QADirectActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }*/
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void videogalleryIntent() {


        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void showAlert(int res) {

        new MaterialDialog.Builder(QADirectActivity.this)
                .title("Select Image")
                .items(res)
                .titleColor(getResources().getColor(android.R.color.black))
                .contentColor(getResources().getColor(android.R.color.black))
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                        selectType(which);

                        return true; // allow selection


                    }
                })
                .positiveText("CHOOSE")
                .cancelable(false)
                .show();
    }

    private void selectType(int pos) {
        if (pos == 0) {

            openGallery(pos);

        } else if (pos == 1) {

            cameraTask(pos);

        } else {
            finish();
        }

    }

    private void openGallery(int pos) {

        startStorage();


    }

    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY);

    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(QADirectActivity.this,
                        permissions, 1);
            } else {

                //startCamera();
                //  captureImage();
                dispatchTakePictureIntent();

            }

        } else {
            //startCamera();
            // captureImage();
            dispatchTakePictureIntent();

        }


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bayer.bayerreward.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public String getAudioFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Music");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".mp3");
        return uriSting;

    }

    public void QAFetch(String token, String eventid) {
        showProgress();
        mAPIService.QADirectFetch(token, eventid).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    showResponse(response);
                } else {
                    if (qaRvrefresh.isRefreshing()) {
                        qaRvrefresh.setRefreshing(false);
                    }
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

                dismissProgress();
                if (qaRvrefresh.isRefreshing()) {
                    qaRvrefresh.setRefreshing(false);
                }
            }
        });
    }

    public void showResponse(Response<QADirectFetch> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {

            if (myDialog != null) {
                if (myDialog.isShowing()) {
                    myDialog.dismiss();
                }
            }
            if (!(response.body().getQa_question().isEmpty())) {
                //  txtEmpty.setVisibility(View.GONE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                qaAttendeeAdapter = new QADirectAdapter(QADirectActivity.this, response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
//                qaRv.scheduleLayoutAnimation();
                txtEmpty.setVisibility(View.GONE);
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.noqna));

                qaAttendeeAdapter = new QADirectAdapter(QADirectActivity.this, response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();


            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }


    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showratedialouge() {

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();
        LinearLayout diatitle = myDialog.findViewById(R.id.diatitle);

        diatitle.setBackgroundColor(Color.parseColor(colorActive));


        cancelbtn = myDialog.findViewById(R.id.canclebtn);
        ratebtn = myDialog.findViewById(R.id.ratebtn);

        cancelbtn.setBackgroundColor(Color.parseColor(colorActive));
        ratebtn.setBackgroundColor(Color.parseColor(colorActive));
        handler = new Handler();
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);

        etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);
        final TextView title = myDialog.findViewById(R.id.title);
        relative_audio = myDialog.findViewById(R.id.relative_audio);
        relative_video = myDialog.findViewById(R.id.relative_video);
        Uploadiv = myDialog.findViewById(R.id.Uploadiv);
        imgPlay = myDialog.findViewById(R.id.imgPlay);
        Upvideov = myDialog.findViewById(R.id.Upvideov);
        txt_time = myDialog.findViewById(R.id.txt_time);
        audio_record_button = myDialog.findViewById(R.id.audio_record_button);
        browse_audio = myDialog.findViewById(R.id.browse_audio);
        browse_media = myDialog.findViewById(R.id.browse_media);
        image_upload = myDialog.findViewById(R.id.image_upload);
        imgPlay = myDialog.findViewById(R.id.imgPlay);
        Upvideov = myDialog.findViewById(R.id.Upvideov);
        img_play = myDialog.findViewById(R.id.img_play);
        img_pause = myDialog.findViewById(R.id.img_pause);
        img_cancel = myDialog.findViewById(R.id.img_cancel);
        seekBar_play = myDialog.findViewById(R.id.seekBar_play);
        txt_timeplayed = myDialog.findViewById(R.id.txt_timeplayed);
        progressBardia = myDialog.findViewById(R.id.progressBardia);

        linear_audio = myDialog.findViewById(R.id.linear_audio);
        nametv.setVisibility(View.GONE);
        title.setText("Post Question");

        nametv.setTextColor(Color.parseColor(colorActive));


        browse_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(QADirectActivity.this);
                builder.setTitle("Select Option");
                builder.setCancelable(false);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ("Select Image".equals(options[which])) {
                            flag = "Image";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                    final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                                    ActivityCompat.requestPermissions(QADirectActivity.this, permissions, 0);
                                } else if (checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                                    ActivityCompat.requestPermissions(QADirectActivity.this, permissions, 0);
                                } else {

                                    showAlert(R.array.selectType);

                                }
                            }

                        } else if ("Select Video".equals(options[which])) {
                            flag = "Video";

                            selectVideo();
                        } /*else if ("Cancel".equals(options[which])) {
                            dialog.dismiss();
                        }*/

                    }
                });
                builder.show();
            }

        });

        audio_record_button.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (ActivityCompat.checkSelfPermission(QADirectActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
                            ActivityCompat.requestPermissions(QADirectActivity.this, permissions, 10);
                        } else {
                            flag = "Audio";
                            handler = new Handler();
                            fileName = getAudioFilename();
                            isRecording = false;
                            txt_time.setVisibility(View.VISIBLE);

                            startRecording();
                        }

                        return true;
                    }

                    case MotionEvent.ACTION_UP: {
                        stopRecording();
                        return true;
                    }

                    default:
                        return false;
                }
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

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                appDelegate.setPostImagePath("");
            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                appDelegate.setPostImagePath("");
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());


//                if (etmsg.getText().toString().length() > 0) {
//
//
//                    PostQuetion(token, eventid, msg);
//                } else {
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(QADirectActivity.this);
//                    builder.setTitle("");
//                    builder.setMessage("Please post a question");
//
//                    builder.setPositiveButton("OK",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                    //  Toast.makeText(getApplicationContext(), "Please post a question", Toast.LENGTH_SHORT).show();
//                }

                if (msg.isEmpty()) {
                    Toast.makeText(QADirectActivity.this, "Enter Description", Toast.LENGTH_SHORT).show();
                } else {
                    if (flag == null) {
                        ratebtn.setClickable(false);
                        ratebtn.setEnabled(false);
                        new SubmitOnlyTask().execute();
                    } else if (flag.equalsIgnoreCase("")) {
                        ratebtn.setClickable(false);
                        ratebtn.setEnabled(false);
                        new SubmitOnlyTask().execute();
                    } else if (flag.equalsIgnoreCase("Image")) {
                        picturePath = appDelegate.getPostImagePath();
                        if (appDelegate.getPostImagePath() != null
                                && appDelegate.getPostImagePath().length() > 0) {
                            System.out.println("Post Image URL  inside SubmitPostTask :" + appDelegate.getPostImagePath());
                            ratebtn.setClickable(false);
                            ratebtn.setEnabled(false);
                            appDelegate.setPostImagePath("");
                            new SubmitPostTask().execute();
                        } else {
                            Toast.makeText(QADirectActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                        }
                    } else if (flag.equalsIgnoreCase("Video")) {

                        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                        RequestBody eventId = RequestBody.create(MediaType.parse("text/plain"), eventid);

                        RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(etmsg.getText().toString()));
                        MultipartBody.Part body = null;
                        MultipartBody.Part body1 = null;

                        if (file != null) {

                            MediaMetadataRetriever m = new MediaMetadataRetriever();
                            m.setDataSource(file.getAbsolutePath());
                            Bitmap bit = m.getFrameAtTime();
                            String filename = String.valueOf(System.currentTimeMillis()) + ".png";
                            File sd = Environment.getExternalStorageDirectory();
                            File dest = new File(sd, filename);

                            try {
                                FileOutputStream out = new FileOutputStream(dest);
                                bit.compress(Bitmap.CompressFormat.PNG, 90, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            File file1 = new File(dest.getAbsolutePath());
                            ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, QADirectActivity.this);
                            body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);

                            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                            body1 = MultipartBody.Part.createFormData("media_file_thumb", file1.getName(), requestFile);
                            ratebtn.setClickable(false);
                            ratebtn.setEnabled(false);
                            postFeedVideo(token, eventId, status, body, body1);
                        } else {
                            Toast.makeText(QADirectActivity.this, "Please Select any Video", Toast.LENGTH_SHORT).show();
                        }
                    } else if (flag.equalsIgnoreCase("Audio")) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            seekBar_play.setProgress(0);
                        }
                        ratebtn.setClickable(false);
                        ratebtn.setEnabled(false);
                        new SubmitPostTaskAudio().execute();
                    }
                }
            }
        });
    }

    public void openGalleryAudio() {
        Intent intent = new Intent();
        intent.setType("audio/.mp3");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Audio "), SELECT_AUDIO);
    }

    private void PostQuetion(final String token, final String eventid, String msg) {
        mAPIService.QADirectPost(token, eventid, msg).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    if (myDialog != null) {
                        if (myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                    }
                    QAFetch(token, eventid);

                } else {

                    Toast.makeText(QADirectActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(QADirectActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onLikeListener(View v, DirectQuestion question, int position, TextView countTv, ImageView likeIv) {
        if (question.getLike_flag().equals("1")) {


            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like));
            try {

                int count = Integer.parseInt(question.getTotal_likes());

                if (count > 0) {
                    count = count - 1;
                    countTv.setText(count + " Likes");

                } else {
                    countTv.setText("0" + " Likes");
                }

//            QAFetch(token,eventid);
                QALike(token, eventid, question.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            likeIv.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_afterlike));
            likeIv.setColorFilter(Color.parseColor(colorActive), PorterDuff.Mode.SRC_ATOP);

            QALike(token, eventid, question.getId());


            try {

                int count = Integer.parseInt(question.getTotal_likes());


                count = count + 1;
                countTv.setText(count + " Likes");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void QALike(String token, String eventid, String questionid) {
        mAPIService.QADirectLike(token, eventid, questionid).enqueue(new Callback<QADirectFetch>() {
            @Override
            public void onResponse(Call<QADirectFetch> call, Response<QADirectFetch> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().getMsg());


                    showLikeResponse(response);
                } else {

                    Toast.makeText(QADirectActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<QADirectFetch> call, Throwable t) {
                Toast.makeText(QADirectActivity.this, "Low network or no network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showLikeResponse(Response<QADirectFetch> response) {

        if (response.body().getStatus().equalsIgnoreCase("success")) {
//            Toast.makeText(QASpeakerActivity.this,response.message(),Toast.LENGTH_SHORT).show();
//            ArrayList<DirectQuestion> speakerQuestionLists = new ArrayList<>();

            if (!(response.body().getQa_question().isEmpty())) {
                txtEmpty.setVisibility(View.GONE);
                //linUpper.setBackground(getResources().getDrawable(R.drawable.close_icon));

                qaAttendeeAdapter = new QADirectAdapter(QADirectActivity.this, response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();
            } else {
                txtEmpty.setVisibility(View.VISIBLE);
                // linUpper.setBackground(getResources().getDrawable(R.drawable.qnadi));

                qaAttendeeAdapter = new QADirectAdapter(QADirectActivity.this, response.body().getQa_question(), this);
                qaAttendeeAdapter.notifyDataSetChanged();
                qaRv.setAdapter(qaAttendeeAdapter);
                qaRv.scheduleLayoutAnimation();


            }

           /* qaAttendeeAdapter = new QADirectAdapter(QADirectActivity.this, response.body().getQa_question(), this);
            qaAttendeeAdapter.notifyDataSetChanged();
            qaRv.setAdapter(qaAttendeeAdapter);
            qaRv.scheduleLayoutAnimation();*/
        } else {
            Toast.makeText(QADirectActivity.this, response.message(), Toast.LENGTH_SHORT).show();
        }
    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        // by setting this field as true, the actual bitmap pixels are not
        // loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        // max Height and width values of the compressed image is taken as
        // 816x612

        float maxHeight = 916.0f;
        float maxWidth = 712.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        // width and height values are set maintaining the aspect ratio of the
        // image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        // setting inSampleSize value allows to load a scaled down version of
        // the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth,
                actualHeight);

        // inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

        // this options allow android to claim the bitmap memory if it runs low
        // on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            // load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        // check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();

        try {
            out = new FileOutputStream(filename);

            // write the compressed bitmap at the destination specified by
            // filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null,
                null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private void setpic2() {
        image_upload.setVisibility(View.VISIBLE);
        relative_audio.setVisibility(View.GONE);
        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(image_upload);


        Toast.makeText(QADirectActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK) {
            image_upload.setVisibility(View.VISIBLE);
            relative_audio.setVisibility(View.GONE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            String compressedImagePath = compressImage(picturePath);
            appDelegate.setPostImagePath(compressedImagePath);

            // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
            Glide.with(this).load(compressedImagePath).into(image_upload);


            // PicassoTrustAll.getInstance(QADirectActivity.this).load(compressedImagePath)
            // .into(post_thumbnail);

            Toast.makeText(QADirectActivity.this, "Image selected",
                    Toast.LENGTH_SHORT).show();

            cursor.close();

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO && data.getData() != null) {

//            onSelectFromGalleryResult(data);
            relative_video.setVisibility(View.VISIBLE);
            relative_audio.setVisibility(View.GONE);
            image_upload.setVisibility(View.GONE);
            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(QADirectActivity.this, data.getData());
            file = new File(videoUrl);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));


            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(file.length()));
                long fileMb = bytesToMeg(file_size);


                //if (fileMb >= 16)
                // Toast.makeText(VideoPost.this, "Upload a video not more than 15 MB in size",
                //        Toast.LENGTH_SHORT).show();

                //  else {
                try {
                    MediaPlayer mplayer = new MediaPlayer();
                    mplayer.reset();
                    mplayer.setDataSource(videoUrl);
                    mplayer.prepare();

                    long totalFileDuration = mplayer.getDuration();
                    Log.i("android", "data is " + totalFileDuration);


                    int sec = (int) ((totalFileDuration / (1000)));

                    Log.i("android", "data is " + sec);


                    if (sec > 15) {
                        Toast.makeText(QADirectActivity.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();
                        relative_video.setVisibility(View.GONE);
                        relative_audio.setVisibility(View.VISIBLE);
                        image_upload.setVisibility(View.GONE);
                        flag=null;
                    } else {


                        //llPost.setVisibility(View.VISIBLE);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoUrl);

                        Uri video = Uri.parse(videoUrl);


                        // videoview.setMediaController(mediacontrolle);
                        Upvideov.setVideoURI(video);


                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        imgeFile = createDirectoryAndSaveFile(bitmap);

                        Bitmap thumbnail = mediaMetadataRetriever.getFrameAtTime();

                        Uploadiv.setImageBitmap(thumbnail);
                        imgPlay.setVisibility(View.VISIBLE);
                        Uploadiv.setVisibility(View.VISIBLE);


                        MediaMetadataRetriever m = new MediaMetadataRetriever();

                        m.setDataSource(videoUrl);
                        //  Bitmap thumbnail = m.getFrameAtTime();
//
                        if (Build.VERSION.SDK_INT >= 17) {
                            angle = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                            //  Log.e("Rotation", s);
                        }


                        Toast.makeText(QADirectActivity.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE && data.getData() != null) {
            uri = data.getData();
            relative_video.setVisibility(View.VISIBLE);
            relative_audio.setVisibility(View.GONE);
            image_upload.setVisibility(View.GONE);
            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(QADirectActivity.this, data.getData());
            file = new File(videoUrl);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));


            if (supportedMedia.contains(fileExtnesion)) {


                long file_size = Integer.parseInt(String.valueOf(file.length()));
                long fileMb = bytesToMeg(file_size);


                //if (fileMb >= 16)
                // Toast.makeText(VideoPost.this, "Upload a video not more than 15 MB in size",
                //        Toast.LENGTH_SHORT).show();

                //  else {
                try {
                    MediaPlayer mplayer = new MediaPlayer();
                    mplayer.reset();
                    mplayer.setDataSource(videoUrl);
                    mplayer.prepare();

                    long totalFileDuration = mplayer.getDuration();
                    Log.i("android", "data is " + totalFileDuration);


                    int sec = (int) ((totalFileDuration / (1000)));

                    Log.i("android", "data is " + sec);


                    if (sec > 15) {
                        Toast.makeText(QADirectActivity.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();
                        relative_video.setVisibility(View.GONE);
                        relative_audio.setVisibility(View.VISIBLE);
                        image_upload.setVisibility(View.GONE);
                        flag=null;
                    } else {


                        //llPost.setVisibility(View.VISIBLE);
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(videoUrl);

                        Uri video = Uri.parse(videoUrl);


                        // videoview.setMediaController(mediacontrolle);
                        Upvideov.setVideoURI(video);


                        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                        imgeFile = createDirectoryAndSaveFile(bitmap);

                        Bitmap thumbnail = mediaMetadataRetriever.getFrameAtTime();

                        Uploadiv.setImageBitmap(thumbnail);
                        imgPlay.setVisibility(View.VISIBLE);
                        Uploadiv.setVisibility(View.VISIBLE);


                        MediaMetadataRetriever m = new MediaMetadataRetriever();

                        m.setDataSource(videoUrl);
                        //  Bitmap thumbnail = m.getFrameAtTime();
//
                        if (Build.VERSION.SDK_INT >= 17) {
                            angle = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                            //  Log.e("Rotation", s);
                        }


                        Toast.makeText(QADirectActivity.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }


            } else {


                Toast.makeText(QADirectActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

            }

        } else if (resultCode == RESULT_OK && requestCode == SELECT_AUDIO) {

            relative_audio.setVisibility(View.GONE);
            image_upload.setVisibility(View.GONE);
            final Uri path = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(path,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

//            String compressedImagePath = compressImage(picturePath);
            selectedPath = picturePath;

            linear_audio.setVisibility(View.VISIBLE);
            relative_audio.setVisibility(View.GONE);
            mediaPlayer = MediaPlayer.create(QADirectActivity.this, path);
//            mediaPlayer.start();
            seekBar_play.setClickable(false);

            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTime = 0;
                    finalTime = 0;
                    oneTimeOnly = 0;
                    seekBar_play.setProgress(0);
                    mediaPlayer.stop();
                    selectedPath = "";
                    linear_audio.setVisibility(View.GONE);

                }
            });

            img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    img_play.setVisibility(View.VISIBLE);
                    img_pause.setVisibility(View.GONE);

                }
            });


            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
//                mediaPlayer = MediaPlayer.create(TrainingAudioActivity.this, uri);
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();

                        finalTime = mediaPlayer.getDuration();
                        startTime = mediaPlayer.getCurrentPosition();

                        if (oneTimeOnly == 0) {
                            seekBar_play.setMax((int) finalTime);
                            oneTimeOnly = 1;
                        }

                        txt_timeplayed.setText(String.format("%d.%d ",
                                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                finalTime)))
                        );


                        img_play.setVisibility(View.GONE);
                        img_pause.setVisibility(View.VISIBLE);
                        seekBar_play.setProgress((int) startTime);
                        myHandler.postDelayed(UpdateSongTime, 100);

                        if (mediaPlayer.getDuration() == mediaPlayer.getCurrentPosition()) {
                            img_play.setVisibility(View.VISIBLE);
                            img_pause.setVisibility(View.GONE);
                        }


                    }
                }
            });
        } else {
            flag = "";
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();

            txt_timeplayed.setText(String.format("%d.%d ",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar_play.setProgress((int) startTime);

            myHandler.postDelayed(this, 100);


        }
    };

    private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMeg(long bytes) {
        return bytes / MEGABYTE;
    }

    public static File createDirectoryAndSaveFile(Bitmap imageToSave) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        if (file.exists()) {
            file.delete();
        }


        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

    }

    public void startRecording() {
        if (!isRecording) {
            //Create MediaRecorder and initialize audio source, output format, and audio encoder
            mRecorder = new MediaRecorder();
            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(8000);
            mRecorder.setAudioEncodingBitRate(16);
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setOutputFile(fileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // Starting record time
            recordTime = 0;
            // Show TextView that displays record time
            txt_time.setVisibility(TextView.VISIBLE);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare failed");
            }
            // Start record job
            mRecorder.start();
            // Change isRecroding flag to true
            isRecording = true;
            // Post the record progress
            handler.post(UpdateRecordTime);
        }
    }

    public void stopRecording() {
//
        try {
            if (isRecording) {
                // Stop recording and release resource
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                // Change isRecording flag to false
                isRecording = false;
                relative_audio.setVisibility(View.GONE);
                // Hide TextView that shows record time
//            txt_time.setVisibility(TextView.INVISIBLE);
                playIt(); // Play the audio
            }
        } catch (Exception e) {
            Toast.makeText(QADirectActivity.this, "Hold for recording audio", Toast.LENGTH_SHORT).show();
            mRecorder = null;
            // Change isRecording flag to false
            isRecording = false;
        }
    }

    Runnable UpdateRecordTime = new Runnable() {
        public void run() {
            if (isRecording) {
                txt_time.setText(String.valueOf(recordTime));
                recordTime += 1;


                handler.postDelayed(this, 1000);
            }
        }
    };

    public void playIt() {
        linear_audio.setVisibility(View.VISIBLE);
//        String[] filePathColumn = {MediaStore.Images.Media.DATA};


        selectedPath = fileName;
        mediaPlayer = MediaPlayer.create(QADirectActivity.this, Uri.parse(fileName));
//            mediaPlayer.start();
        seekBar_play.setClickable(false);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                finalTime = 0;
                seekBar_play.setProgress(0);
                oneTimeOnly = 0;
                mediaPlayer.stop();
                selectedPath = "";
                linear_audio.setVisibility(View.GONE);
                relative_audio.setVisibility(View.VISIBLE);
                txt_time.setText("");
                img_play.setVisibility(View.VISIBLE);
                img_pause.setVisibility(View.GONE);
                txt_timeplayed.setText("");

            }
        });

        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                img_play.setVisibility(View.VISIBLE);
                img_pause.setVisibility(View.GONE);

            }
        });


        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
//                mediaPlayer = MediaPlayer.create(TrainingAudioActivity.this, uri);
                mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekBar_play.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }

                txt_timeplayed.setText(String.format("%d.%d ",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime))));

//                tx1.setText(String.format("%d min, %d sec",
//                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
//                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
//                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
//                                        startTime)))
//                );

                img_play.setVisibility(View.GONE);
                img_pause.setVisibility(View.VISIBLE);
                seekBar_play.setProgress((int) startTime);
                myHandler.postDelayed(UpdateSongTime, 100);

                if (mediaPlayer.getDuration() == mediaPlayer.getCurrentPosition()) {
                    img_play.setVisibility(View.VISIBLE);
                    img_pause.setVisibility(View.GONE);
                }

//                img_play.setEnabled(true);
//                img_pause.setEnabled(false);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                flag = "Audio";
//                handler = new Handler();
//                fileName = getAudioFilename();
//                isRecording = false;
//                txt_time.setVisibility(View.VISIBLE);

//                startRecording();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {

            int permGranted = PackageManager.PERMISSION_GRANTED;

            Boolean permissionRequired = false;

            for (int perm : grantResults) {

                if (perm != permGranted) {
                    permissionRequired = true;
                }
            }

            //if permission is still required
            if (permissionRequired) {
                Toast.makeText(this, "No permission to capture storage.",
                        Toast.LENGTH_SHORT).show();

            } else {
                //captureImage();

                dispatchTakePictureIntent();
            }


        }
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    public class SubmitOnlyTask extends AsyncTask<String, String, JSONObject> {

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
            // Showing progress dialog
            progressBardia.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);


                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventid);

                builder.addFormDataPart("question", StringEscapeUtils.escapeJava(etmsg.getText().toString()));


                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                okhttp3.Response response = null;


                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
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
            progressBardia.setVisibility(View.GONE);
            ratebtn.setClickable(true);
            ratebtn.setEnabled(true);
            myDialog.dismiss();
            flag=null;
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
                        .show();


            } else {
                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


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
            // Showing progress dialog
            progressBardia.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    sourceFile = new File(picturePath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                String filename = picturePath.substring(picturePath.lastIndexOf("/") + 1);


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);
                if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("media_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                }

                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventid);

                builder.addFormDataPart("question", StringEscapeUtils.escapeJava(etmsg.getText().toString()));


                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                okhttp3.Response response = null;


                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
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
            progressBardia.setVisibility(View.GONE);
            ratebtn.setClickable(true);
            ratebtn.setEnabled(true);
            myDialog.dismiss();
            flag=null;
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
                        .show();


            } else {
                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }

    public class SubmitPostTaskAudio extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String res = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            progressBardia.setVisibility(View.VISIBLE);

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                if (selectedPath != null && !(selectedPath.equalsIgnoreCase(""))) {
                    sourceFile = new File(selectedPath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("audio/*");

                String filename = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);


                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
                    // SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(url);


                    client = getUnsafeOkHttpClient().newBuilder().build();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                MultipartBody.Builder builder = new MultipartBody.Builder();

                builder.setType(MultipartBody.FORM);
                if (selectedPath != null && !(selectedPath.equalsIgnoreCase(""))) {
                    builder.addFormDataPart("media_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile + ""));
                }

                builder.addFormDataPart("api_access_token", apikey);
                builder.addFormDataPart("event_id", eventid);

                builder.addFormDataPart("question", msg);


                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                okhttp3.Response response = null;


                try {
                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //res = response.body().string();
                httpEntity = transformResponse(response).getEntity();
                res = EntityUtils.toString(httpEntity);

            } catch (IOException e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(res);
                error = json.getString("status");
                message = json.getString("msg");
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
            progressBardia.setVisibility(View.GONE);
            myDialog.dismiss();
            ratebtn.setClickable(true);
            ratebtn.setEnabled(true);
            flag=null;
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();
                startTime = 0;
                finalTime = 0;
                seekBar_play.setProgress(0);
                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
                        .show();


            } else {
                Toast.makeText(QADirectActivity.this, message, Toast.LENGTH_SHORT)
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

    public void postFeedVideo(RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody, MultipartBody.Part thumb) {
        progressBardia.setVisibility(View.VISIBLE);
        mAPIService.QADirectPost(api_access_token, eventid, status, fbody, thumb).enqueue(new Callback<PostTextFeed>() {
            @Override
            public void onResponse(Call<PostTextFeed> call, retrofit2.Response<PostTextFeed> response) {
                ratebtn.setClickable(true);
                ratebtn.setEnabled(true);
                myDialog.dismiss();
                flag=null;
                Log.i("hit", "post submitted to API." + response.body().toString());
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBardia.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                } else {
                    progressBardia.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostTextFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                progressBardia.setVisibility(View.GONE);
                myDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void playAudio(String path) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JZVideoPlayerStandard.releaseAllVideos();
    }
}
