package com.bayer.bayerreward.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
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
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.CustomTools.ProgressRequestBodyVideo;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.GetterSetter.PostTextFeed;
import com.bayer.bayerreward.InnerDrawerActivity.EngagementActivity;
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
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;
import static org.apache.http.HttpVersion.HTTP_1_1;

public class ReportActivity extends AppCompatActivity implements ProgressRequestBodyVideo.UploadCallbacks {

    RelativeLayout relative;
    Spinner spinner;
    EditText etmsg;
    Button btn_submit;
    BottomSheetDialog dialog;
    MediaRecorder mRecorder;
    String angle = "0";
    File file;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    private APIService mAPIService;
    String[] report = new String[]{"Field Demonstration", "Channel Partner Field Visit", "Display Contest",
            "Mega Farmer Meet", "Pest Infestation", "others"};
    private Handler myHandler = new Handler();
    public int oneTimeOnly = 0;
    public double startTime = 0;
    public double finalTime = 0;
    final String[] options = {"Select Image", "Select Audio", "Select Video", "Cancel"};
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_TAKE_GALLERY_VIDEO = 3;
    private static final int SELECT_AUDIO = 4;
    String mCurrentPhotoPath;
    ImageView image_upload, Uploadiv, imgPlay;
    MyApplication appDelegate;
    public static String flag = "";
    File mediaFile;
    String userChoosenTask;
    private Uri uri;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    String videoUrl;
    public static File imgeFile;
    VideoView Upvideov;
    RelativeLayout relative_video;
    private String picturePath = "";
    File sourceFile;
    String postUrl;
    SessionManager sessionManager;
    String apikey = "";
    private Button mAudioRecordButton;
    RelativeLayout relative_audio;
    Button browse_audio;
    String selectedPath = "";
    Uri selectedImageUri;
    LinearLayout linear_audio;
    public MediaPlayer mediaPlayer;
    SeekBar seekBar_play;
    ImageView img_pause, img_play, img_cancel;
    TextView txt_timeplayed, txt_time;
    String fileName;
    Boolean isRecording;
    Handler handler;
    int recordTime;
    ProgressBar progressBar;
    ConnectionDetector cd;

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#0092df"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JZVideoPlayerStandard.releaseAllVideos();
                onBackPressed();
            }
        });


        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sessionManager = new SessionManager(ReportActivity.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        apikey = user.get(SessionManager.KEY_TOKEN);
        mAPIService = ApiUtils.getAPIService();
        postUrl = ApiConstant.baseUrl + "ReportActivity";
        handler = new Handler();

        appDelegate = (MyApplication) getApplicationContext();
        headerlogoIv = findViewById(R.id.headerlogoIv);
        spinner = findViewById(R.id.spinner);
        image_upload = findViewById(R.id.image_upload);
        Upvideov = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        Uploadiv = findViewById(R.id.Uploadiv);
        relative_video = findViewById(R.id.relative_video);
        btn_submit = findViewById(R.id.btn_submit);
        etmsg = findViewById(R.id.etmsg);
        relative_audio = findViewById(R.id.relative_audio);
        mAudioRecordButton = findViewById(R.id.audio_record_button);
        browse_audio = findViewById(R.id.browse_audio);
        linear_audio = findViewById(R.id.linear_audio);
        seekBar_play = findViewById(R.id.seekBar_play);
        img_play = findViewById(R.id.img_play);
        img_pause = findViewById(R.id.img_pause);
        txt_timeplayed = findViewById(R.id.txt_timeplayed);
        img_cancel = findViewById(R.id.img_cancel);
        txt_time = findViewById(R.id.txt_time);
        progressBar = findViewById(R.id.progressBar);
        relative = findViewById(R.id.relative);
        ArrayAdapter dataAdapter = new ArrayAdapter(ReportActivity.this, R.layout.spinner_item, report);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        cd = new ConnectionDetector(ReportActivity.this);
        browse_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setTitle("Select Option");
                builder.setCancelable(false);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Which", String.valueOf(options[which]));
                        if (options[which].equalsIgnoreCase("Select Image")) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ReportActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                    final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                                    ActivityCompat.requestPermissions(ReportActivity.this, permissions, 0);
                                } else if (ReportActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                                    ActivityCompat.requestPermissions(ReportActivity.this, permissions, 0);
                                } else {
                                    showAlert(R.array.selectType);
                                }
                            }

                        } else if (options[which].equalsIgnoreCase("Select Audio")) {

                            if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
                                ActivityCompat.requestPermissions(ReportActivity.this, permissions, 10);
                            } else {
                                openGalleryAudio();
                            }
                        } else if (options[which].equalsIgnoreCase("Select Video")) {
                            selectVideo();
                        }/* else if (options[which].equalsIgnoreCase("Cancel")) {
                            dialog.dismiss();
                            flag = "";
                        }*/
                    }
                });
                builder.show();

            }
        });


        mAudioRecordButton.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        flag = "Audio";
                        if (linear_audio.getVisibility() == View.VISIBLE) {
                            startTime = 0;
                            finalTime = 0;
                            oneTimeOnly = 0;
                            seekBar_play.setProgress(0);
                            mediaPlayer.stop();
                            img_play.setVisibility(View.VISIBLE);
                            img_pause.setVisibility(View.GONE);
                            linear_audio.setVisibility(View.GONE);
                        }

                        if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.RECORD_AUDIO"};
//                                ActivityCompat.requestPermissions(ReportActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO, READ_EXTERNAL_STORAGE}, 10);
                            ActivityCompat.requestPermissions(ReportActivity.this, permissions, 10);
                        } else {
                            try {
                                handler = new Handler();

                                fileName = getAudioFilename();
                                isRecording = false;
                                txt_time.setVisibility(View.VISIBLE);
                                if (!isRecording) {
                                    //Create MediaRecorder and initialize audio source, output format, and audio encoder
                                    mRecorder = new MediaRecorder();
                                    mRecorder.reset();
                                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                    mRecorder.setOutputFile(fileName);
                                    // Starting record time
                                    recordTime = 0;
                                    // Show TextView that displays record time

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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
//                        startRecording();

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


        Upvideov.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                imgPlay.setImageResource(R.drawable.ic_media_play);
                imgPlay.setVisibility(View.VISIBLE);
            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Upvideov.setVisibility(View.VISIBLE);
                Uploadiv.setVisibility(View.GONE);

                if (!Upvideov.isPlaying()) {

                    try {
                        // Start the MediaController

                        imgPlay.setImageResource(R.drawable.ic_media_pause);

                        //  mediacontrolle.setAnchorView(videoview);
                        // Get the URL from String VideoURL

                        Upvideov.start();
                        imgPlay.setVisibility(View.GONE);


                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    Upvideov.pause();

                    imgPlay.setImageResource(R.drawable.ic_media_play);
                    imgPlay.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JZVideoPlayerStandard.releaseAllVideos();

                InputMethodManager imm = (InputMethodManager) ReportActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(relative.getWindowToken(), 0);
                if (flag == "") {
                    if (etmsg.getText().toString().isEmpty()) {
                        Toast.makeText(ReportActivity.this, "Enter description.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (cd.isConnectingToInternet()) {
                            new SubmitOnlyTask().execute();
                        } else {
                            Toast.makeText(ReportActivity.this, "No Internet Connect", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (flag.equalsIgnoreCase("Image")) {
                    picturePath = appDelegate.getPostImagePath();
                    if (appDelegate.getPostImagePath() != null
                            && appDelegate.getPostImagePath().length() > 0) {
                        System.out.println("Post Image URL  inside SubmitPostTask :" + appDelegate.getPostImagePath());

                        appDelegate.setPostImagePath("");
                        if (cd.isConnectingToInternet()) {
                            new SubmitPostTask().execute();
                        } else {
                            Toast.makeText(ReportActivity.this, "No Internet Connect", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ReportActivity.this, "Select Image", Toast.LENGTH_SHORT).show();
                    }
                } else if (flag.equalsIgnoreCase("Video")) {
                    Upvideov.pause();
                    RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                    RequestBody eventId = RequestBody.create(MediaType.parse("text/plain"), eventid);

                    RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(etmsg.getText().toString()));
                    RequestBody report_dropdown = RequestBody.create(MediaType.parse("text/plain"), spinner.getSelectedItem().toString());
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
                        ProgressRequestBodyVideo reqFile = new ProgressRequestBodyVideo(file, ReportActivity.this);
                        body = MultipartBody.Part.createFormData("media_file", file.getName(), reqFile);

                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file1);
                        body1 = MultipartBody.Part.createFormData("media_file_thumb", file1.getName(), requestFile);
                        if (cd.isConnectingToInternet()) {
                            postFeedVideo(token, eventId, status, report_dropdown, body, body1);
                        } else {
                            Toast.makeText(ReportActivity.this, "No Internet Connect", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ReportActivity.this, "Please Select any Video", Toast.LENGTH_SHORT).show();
                    }
                } else if (flag.equalsIgnoreCase("Audio")) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        seekBar_play.setProgress(0);
                    }

                    if (cd.isConnectingToInternet()) {
                        new SubmitPostTaskAudio().execute();
                    } else {
                        Toast.makeText(ReportActivity.this, "No Internet Connect", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        CommonFunction.crashlytics("ReportActivity", "");
        firbaseAnalytics(this, "ReportActivity", "");
    }

    public void openGalleryAudio() {

//        Intent intent = new Intent();
//        intent.setType("audio/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Audio"), SELECT_AUDIO);

        flag = "Audio";
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("audio/*");
        startActivityForResult(intent, SELECT_AUDIO);
    }

    public void postFeedVideo(RequestBody api_access_token, RequestBody eventid, RequestBody status, RequestBody report_dropdown, MultipartBody.Part fbody, MultipartBody.Part thumb) {
        showProgress();
        btn_submit.setClickable(false);
        mAPIService.ReportActivity(api_access_token, eventid, status, report_dropdown, fbody, thumb).enqueue(new Callback<PostTextFeed>() {
            @Override
            public void onResponse(Call<PostTextFeed> call, retrofit2.Response<PostTextFeed> response) {
                Log.i("hit", "post submitted to API." + response.body().toString());
                btn_submit.setClickable(true);
                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    flag = "";
                    Toast.makeText(ReportActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    dismissProgress();
                    Toast.makeText(ReportActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostTextFeed> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                dismissProgress();
                btn_submit.setClickable(true);
                Toast.makeText(ReportActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library"};

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ReportActivity.this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ReportActivity.this);

//                recorderTask(1);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    flag = "Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(ReportActivity.this.getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } /*else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    flag = "";
                }*/
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void videogalleryIntent() {
        flag = "Video";
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }


    public void showAlert(int res) {

        new MaterialDialog.Builder(ReportActivity.this)
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

        } /*else {
            flag = "";
            new MaterialDialog.Builder(ReportActivity.this).cancelable(true);
        }*/

    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (ReportActivity.this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(ReportActivity.this,
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
        if (takePictureIntent.resolveActivity(ReportActivity.this.getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ReportActivity.this,
                        "com.bayer.bayerreward.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void openGallery(int pos) {
        startStorage();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        flag = "Image";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ReportActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createaudioFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ReportActivity.this.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".mp3",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        fileName = image.getAbsolutePath();
        return image;
    }

    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY);

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
        Cursor cursor = ReportActivity.this.getContentResolver().query(contentUri, null, null,
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

        linear_audio.setVisibility(View.GONE);
        relative_video.setVisibility(View.GONE);
        image_upload.setVisibility(View.VISIBLE);
        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(image_upload);


        Toast.makeText(ReportActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }

    Runnable UpdateSongTime = new Runnable() {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK) {
            flag = "Image";
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = ReportActivity.this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            String compressedImagePath = compressImage(picturePath);
            appDelegate.setPostImagePath(compressedImagePath);

            // PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);
            Glide.with(this).load(compressedImagePath).into(image_upload);
            linear_audio.setVisibility(View.GONE);
            relative_video.setVisibility(View.GONE);
            image_upload.setVisibility(View.VISIBLE);
            // PicassoTrustAll.getInstance(ReportActivity.this).load(compressedImagePath)
            // .into(post_thumbnail);

            Toast.makeText(ReportActivity.this, "Image selected",
                    Toast.LENGTH_SHORT).show();

            cursor.close();

        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO && data.getData() != null) {
            image_upload.setVisibility(View.GONE);
            linear_audio.setVisibility(View.GONE);
            relative_video.setVisibility(View.VISIBLE);
            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(ReportActivity.this, data.getData());
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
                        Toast.makeText(ReportActivity.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();
                        relative_video.setVisibility(View.GONE);
                        flag = "";
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


                        Toast.makeText(ReportActivity.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }
            }

        } else if (resultCode == RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE && data.getData() != null) {
            uri = data.getData();

            ArrayList<String> supportedMedia = new ArrayList<String>();

            supportedMedia.add(".mp4");
            supportedMedia.add(".mov");
            supportedMedia.add(".3gp");


            videoUrl = ScalingUtilities.getPath(ReportActivity.this, data.getData());
            file = new File(videoUrl);

            String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));
            linear_audio.setVisibility(View.GONE);
            image_upload.setVisibility(View.GONE);
            relative_video.setVisibility(View.VISIBLE);
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
                        Toast.makeText(ReportActivity.this, "Select an video not more than 15 seconds",
                                Toast.LENGTH_SHORT).show();
                        relative_video.setVisibility(View.GONE);
                        flag = "";
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


                        Toast.makeText(ReportActivity.this, "Video selected",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {


                    Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                }


            } else {


                Toast.makeText(ReportActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

            }

        } else if (resultCode == RESULT_OK && requestCode == SELECT_AUDIO) {


            final Uri path = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = ReportActivity.this.getContentResolver().query(path,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

//            selectedPath = data.getData().getPath().replace(":", "/").replace("document", "storage");

//            String compressedImagePath = compressImage(picturePath);
//            selectedPath = path.getPath();
            selectedPath = ScalingUtilities.getPath(ReportActivity.this, data.getData());
//            selectedPath = picturePath;

            image_upload.setVisibility(View.GONE);
            relative_video.setVisibility(View.GONE);
            linear_audio.setVisibility(View.VISIBLE);
            mediaPlayer = MediaPlayer.create(ReportActivity.this, path);
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                seekBar_play.setProgress(0);
            }
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
                    img_play.setVisibility(View.VISIBLE);
                    img_pause.setVisibility(View.GONE);
                    fileName = "";
                    selectedPath = "";
                    txt_time.setText("");
                    txt_time.setVisibility(View.INVISIBLE);
                    linear_audio.setVisibility(View.GONE);
                    mediaPlayer.reset();
                    txt_timeplayed.setText("");
                    myHandler.removeCallbacksAndMessages(null);
                    handler.removeCallbacksAndMessages(null);
                }
            });

            img_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ReportActivity.this, "Pausing sound", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                    img_play.setVisibility(View.VISIBLE);
                    img_pause.setVisibility(View.GONE);
                }
            });


            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ReportActivity.this, "Playing sound", Toast.LENGTH_SHORT).show();
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
                }
            });
        } else {
            flag = "";
        }


    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ReportActivity.this.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(ReportActivity.this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalleryAudio();
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
                Toast.makeText(ReportActivity.this, "No permission to capture storage.",
                        Toast.LENGTH_SHORT).show();

            } else {
                //captureImage();

                dispatchTakePictureIntent();
            }


        }
    }

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
            showProgress();
            btn_submit.setClickable(false);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


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

                builder.addFormDataPart("description", StringEscapeUtils.escapeJava(etmsg.getText().toString()));
                builder.addFormDataPart("report_dropdown", spinner.getSelectedItem().toString());

                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                Response response = null;


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
            dismissProgress();
            btn_submit.setClickable(true);
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();
                flag = "";
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
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
            showProgress();
            btn_submit.setClickable(false);

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

                builder.addFormDataPart("description", StringEscapeUtils.escapeJava(etmsg.getText().toString()));
                builder.addFormDataPart("report_dropdown", spinner.getSelectedItem().toString());

                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                Response response = null;


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
            dismissProgress();
            btn_submit.setClickable(true);
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();
                flag = "";
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
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
        String msg = "";

        String res = null;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            showProgress();
            btn_submit.setClickable(false);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {


                if (selectedPath != null && !(selectedPath.equalsIgnoreCase(""))) {
                    sourceFile = new File(selectedPath);
                }


                //Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("audio/*");

//                 = sourceFile.getName();
//                String filename= selectedPath.substring(selectedPath.lastIndexOf("/") + 1);
                String filename = sourceFile.getName();

                OkHttpClient client = null;
                try {

                    URL url = new URL(postUrl);
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

                builder.addFormDataPart("description", StringEscapeUtils.escapeJava(etmsg.getText().toString()));
                builder.addFormDataPart("report_dropdown", spinner.getSelectedItem().toString());

                RequestBody requestBody = builder.build();


                Request request = new Request.Builder()
                        .url(postUrl)
                        .post(requestBody)
                        .build();

                HttpEntity httpEntity = null;
                Response response = null;


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
            dismissProgress();
            btn_submit.setClickable(true);
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();
                startTime = 0;
                finalTime = 0;
                seekBar_play.setProgress(0);
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
                fileName = "";
                oneTimeOnly = 0;
                flag = "";
                Intent intent = new Intent(ReportActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }

    private static HttpResponse transformResponse(Response response) {

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

    public void stopRecording() {
        try {
            if (isRecording) {
                // Stop recording and release resource
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                // Change isRecording flag to false
                isRecording = false;
                // Hide TextView that shows record time
//            txt_time.setVisibility(TextView.INVISIBLE);
                playIt(); // Play the audio
            }
        } catch (Exception e) {
            Toast.makeText(ReportActivity.this, "Hold for recording audio", Toast.LENGTH_SHORT).show();
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
        image_upload.setVisibility(View.GONE);
        relative_video.setVisibility(View.GONE);

        selectedPath = fileName;

        mediaPlayer = MediaPlayer.create(ReportActivity.this, Uri.parse(fileName));
//            mediaPlayer.start();
        seekBar_play.setClickable(false);

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                finalTime = 0;
                seekBar_play.setProgress(0);
                oneTimeOnly = 0;
                fileName = "";
                selectedPath = "";
                img_play.setVisibility(View.VISIBLE);
                img_pause.setVisibility(View.GONE);
                txt_time.setVisibility(View.INVISIBLE);
                txt_time.setText("");
                linear_audio.setVisibility(View.GONE);
                mediaPlayer.stop();
                mediaPlayer.reset();
                handler.removeCallbacksAndMessages(null);
                myHandler.removeCallbacksAndMessages(null);
                txt_timeplayed.setText("");


            }
        });

        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportActivity.this, "Pausing sound", Toast.LENGTH_SHORT).show();
                mediaPlayer.pause();
                img_play.setVisibility(View.VISIBLE);
                img_pause.setVisibility(View.GONE);
            }
        });


        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReportActivity.this, "Playing sound", Toast.LENGTH_SHORT).show();
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

//                img_play.setEnabled(true);
//                img_pause.setEnabled(false);
            }
        });
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

    @Override
    protected void onPause() {
        super.onPause();
        Upvideov.pause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Upvideov.pause();
        flag = "";
        JZVideoPlayerStandard.releaseAllVideos();
        Intent intent = new Intent(ReportActivity.this, EngagementActivity.class);
        startActivity(intent);
        finish();
    }
}
