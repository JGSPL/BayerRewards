package com.procialize.singleevent.InnerDrawerActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.procialize.singleevent.Activity.HomeActivity;
import com.procialize.singleevent.Activity.PostViewActivity;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.PostSelfie;
import com.procialize.singleevent.GetterSetter.PostVideoSelfie;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.ScalingUtilities;
import com.procialize.singleevent.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoContestUploadActivity extends AppCompatActivity {

    private Uri uri;
    private String pathToStoredVideo;
    private static final String SERVER_PATH = "";
    Uri capturedImageUri;
    private VideoView displayRecordedVideo;
    ImageView imgPlay;
    Button btnSubmit;
    File file = null;
    LinearLayout llData;
    SessionManager sessionManager;
    String apikey;
    TextInputEditText editTitle;
    APIService mAPIService;
    ProgressDialog progress;
    String userChoosenTask;
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventId;
    String angle = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");

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


        displayRecordedVideo = findViewById(R.id.videopreview);
        btnSubmit = findViewById(R.id.btnSubmit);
        editTitle = findViewById(R.id.editTitle);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);

        sessionManager = new SessionManager(this);

        mAPIService = ApiUtils.getAPIService();

        llData = findViewById(R.id.llData);
        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTitle.getText().toString();
                if (data.equals("")) {
                    Toast.makeText(VideoContestUploadActivity.this, "Enter Caption", Toast.LENGTH_SHORT).show();

                } else {
                    showProgress();


                    RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                    RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
                    RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                    MultipartBody.Part body = null;

                    if (file != null) {

                        RequestBody reqFile = RequestBody.create(MediaType.parse("video/*"), file);
                        body = MultipartBody.Part.createFormData("video_file", file.getName(), reqFile);
                    }


                    PostVideoContest(token, eventid, status, body);
                }
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                post_thumbnail.setVisibility(View.GONE);

                displayRecordedVideo.setVisibility(View.VISIBLE);

                if (!displayRecordedVideo.isPlaying()) {

                    try {
                        // Start the MediaController
                        imgPlay.setVisibility(View.GONE);
                        imgPlay.setImageResource(R.drawable.ic_media_pause);

                        //  mediacontrolle.setAnchorView(videoview);
                        // Get the URL from String VideoURL

                        displayRecordedVideo.start();


                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    displayRecordedVideo.pause();
                    imgPlay.setVisibility(View.VISIBLE);
                    imgPlay.setImageResource(R.drawable.ic_media_play);
                }

            }
        });

        displayRecordedVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                imgPlay.setVisibility(View.VISIBLE);
                imgPlay.setImageResource(R.drawable.ic_media_play);

            }
        });
        selectVideo();
    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(VideoContestUploadActivity.this);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    if (result) {
                        userChoosenTask = "Take Video";

                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void videogalleryIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public static File videoFile;
    String videoUrl;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            llData.setVisibility(View.VISIBLE);
//            Uri selectedMediaUri = (Uri) data.getExtras().get("data");
            Uri selectedMediaUri = data.getData();
//            if (selectedMediaUri.toString().contains("video")) {


            displayRecordedVideo.setVisibility(View.VISIBLE);

            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                uri = data.getData();
                MediaPlayer mp = MediaPlayer.create(this, uri);
                int duration = mp.getDuration();
                mp.release();

                if ((duration / 1000) > 15) {
                    // Show Your Messages
                    Toast.makeText(VideoContestUploadActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(VideoContestUploadActivity.this, HomeActivity.class);
//                        startActivity(intent);
                    finish();
                } else {
                    displayRecordedVideo.setVideoURI(uri);
                    displayRecordedVideo.start();
                    imgPlay.setVisibility(View.GONE);

                    pathToStoredVideo = getRealPathFromURIPathVideo(uri, VideoContestUploadActivity.this);
                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                    //Store the video to your server
                    file = new File(pathToStoredVideo);
                }
            } else if (resultCode == Activity.RESULT_OK && requestCode == SELECT_FILE) {
                uri = data.getData();
                displayRecordedVideo.setVideoURI(uri);
                displayRecordedVideo.start();
                imgPlay.setVisibility(View.GONE);
                ArrayList<String> supportedMedia = new ArrayList<String>();

                supportedMedia.add(".mp4");
                supportedMedia.add(".mov");
                supportedMedia.add(".3gp");


                videoUrl = ScalingUtilities.getPath(VideoContestUploadActivity.this, data.getData());
//                    pathToStoredVideo = getRealPathFromURIPathVideo(uri, VideoContestUploadActivity.this);
                videoFile = new File(videoUrl);

                String fileExtnesion = videoUrl.substring(videoUrl.lastIndexOf("."));

                MediaMetadataRetriever m = new MediaMetadataRetriever();

                m.setDataSource(videoFile.getAbsolutePath());
                Bitmap thumbnail = m.getFrameAtTime();

                if (Build.VERSION.SDK_INT >= 17) {
                    angle = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

                    //  Log.e("Rotation", s);
                }
                if (supportedMedia.contains(fileExtnesion)) {


                    long file_size = Integer.parseInt(String.valueOf(videoFile.length()));
//                long fileMb = AudioPost.bytesToMeg(file_size);


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
                            Toast.makeText(VideoContestUploadActivity.this, "Select an video not more than 15 seconds",
                                    Toast.LENGTH_SHORT).show();
                            finish();

                        } else {


                            llData.setVisibility(View.VISIBLE);
                            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                            mediaMetadataRetriever.setDataSource(videoUrl);

                            Uri video = Uri.parse(videoUrl);


                            // videoview.setMediaController(mediacontrolle);
                            displayRecordedVideo.setVideoURI(video);


                            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(1000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                            file = createDirectoryAndSaveFile(bitmap);


//                                post_thumbnail.setImageBitmap(bitmap);
//                                imgPlay.setVisibility(View.VISIBLE);
//                                post_thumbnail.setVisibility(View.VISIBLE);

                            Toast.makeText(VideoContestUploadActivity.this, "Video selected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {


                        Log.i("android", "exception is " + e.getLocalizedMessage() + " " + e.getStackTrace());

                    }


                } else {


                    Toast.makeText(VideoContestUploadActivity.this, "Only .mp4,.mov,.3gp File formats allowed ", Toast.LENGTH_SHORT).show();

                }
            }

//            }

        } else {
            Intent intent = new Intent(VideoContestUploadActivity.this, VideoContestActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private String getRealPathFromURIPathVideo(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    public void PostVideoContest(RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody) {
        mAPIService.PostVideoContest(api_access_token, eventid, status, fbody).enqueue(new Callback<PostVideoSelfie>() {
            @Override
            public void onResponse(Call<PostVideoSelfie> call, Response<PostVideoSelfie> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostVideoSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<PostVideoSelfie> response) {

        if (response.body().getStatus().equals("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
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

    public static File createDirectoryAndSaveFile(Bitmap imageToSave) throws IOException {

        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        direct.mkdir();

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(direct, System.currentTimeMillis() + ".jpg");

//        if (file.exists()) {
//            file.delete();
//        }

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
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VideoContestUploadActivity.this, VideoContestActivity.class);
        startActivity(intent);
        finish();
    }
}
