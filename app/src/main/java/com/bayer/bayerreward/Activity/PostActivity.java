package com.bayer.bayerreward.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.percolate.mentions.Mentionable;
import com.percolate.mentions.Mentions;
import com.percolate.mentions.QueryListener;
import com.percolate.mentions.SuggestionsListener;
import com.bayer.bayerreward.Adapter.UsersAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.CustomTools.CircleDisplay;
import com.bayer.bayerreward.CustomTools.ImagePath_MarshMallow;
import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.CustomTools.ProgressRequestBodyImage;
import com.bayer.bayerreward.CustomTools.ProgressRequestBodyVideo;
import com.bayer.bayerreward.CustomTools.RecyclerItemClickListener;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.AttendeeList;
import com.bayer.bayerreward.GetterSetter.Comment;
import com.bayer.bayerreward.GetterSetter.Mention;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.MyApplication;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.bayer.bayerreward.Utility.ScalingUtilities.getPath;
import static org.apache.http.HttpVersion.HTTP_1_1;

//import android.support.text.emoji.widget.EmojiAppCompatEditText;

public class PostActivity extends AppCompatActivity implements OnClickListener, ProgressRequestBodyImage.UploadCallbacks, ProgressRequestBodyVideo.UploadCallbacks, QueryListener, SuggestionsListener {
    private static final int REQUEST_VIDEO_CAPTURE = 300;
    private static final String SERVER_PATH = "";
    EditText postEt;
    TextView postbtn;
    APIService mAPIService;
    CircleDisplay progressbar;
    SessionManager sessionManager;
    private UsersAdapter usersAdapter;
    String apikey = "";
    RequestBody fbody = null;
    String to;
    ImageView Uploadiv;
    String typepost;
    String userChoosenTask;
    File file;
    Uri capturedImageUri;
    String eventId;
    ImageView profileIV;
    String mCurrentPhotoPath;
    ImageView imgPlay;
    SessionManager session;
    File sourceFile;
    MyApplication appDelegate;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive;
    ImageView headerlogoIv;
    TextView txtcount;
    Uri imageUri;
    private int REQUEST_TAKE_GALLERY = 1, REQUEST_TAKE_PHOTO = 2, REQUEST_TAKE_GALLERY_VIDEO = 3;
    private Uri uri;
    private String pathToStoredVideo;
    private String selectedImagePath;
    private VideoView displayRecordedVideo;
    private String postMsg = "";
    private String picturePath = "";
    private String actionFlag;
    private ConnectionDetector cd;
    private String senderImageURL = "";
    private String postUrl = "";
    private ProgressDialog pDialog;
    private List<AttendeeList> userList;
    private Mentions mentions;
    TextView textData;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    List<AttendeeList> customers = null;
    private SQLiteDatabase db;
    RelativeLayout linear;

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
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;

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

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        actionFlag = getIntent().getExtras().getString("for");

        // InitializeGUI
        initializeGUI();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        linear = findViewById(R.id.linear);
        Util.logomethod(this, headerlogoIv);

        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                appDelegate.setPostImagePath("");
            }
        });

        try {
//            ContextWrapper cw = new ContextWrapper(HomeActivity.this);
            //path to /data/data/yourapp/app_data/dirName
//            File directory = cw.getDir("/storage/emulated/0/Procialize/", Context.MODE_PRIVATE);
            File mypath = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Procialize/" + "background.jpg");
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(mypath));
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            linear.setBackgroundDrawable(bd);

            Log.e("PATH", String.valueOf(mypath));
        } catch (Exception e) {
            e.printStackTrace();
            linear.setBackgroundColor(Color.parseColor("#f1f1f1"));
        }
        if (actionFlag.equalsIgnoreCase("image")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android M Permission check
                /*if (PostActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && PostActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                    final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissionswrite, 0);
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else {

                    showAlert(R.array.selectType);

                }*/
                if (this.checkSelfPermission(
                        "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else if (this.checkSelfPermission(
                        "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                    ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                } else {

                    showAlert(R.array.selectType);

                }

            } else {
                showAlert(R.array.selectType);

            }
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // Android M Permission check
                    if (this.checkSelfPermission(
                            "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                        final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                        ActivityCompat.requestPermissions(PostActivity.this, permissions, 0);
                    } else {

                    showAlert(R.array.selectType);

                }

            } else {
                showAlert(R.array.selectType);

            }*/


        } else if (actionFlag.equalsIgnoreCase("video")) {
            selectVideo();
        }

        /*
         * else { post_status_post
         * .setHint("What's on your mind? (Not more than 500 characters)");
         * post_thumbnail.setVisibility(View.GONE); }
         */

        // Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_LONG)
        // .show();

    }

//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
//
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
//        return image;
//    }

    public void showAlert(int res) {

        new MaterialDialog.Builder(PostActivity.this)
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

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(PostActivity.this,
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

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 100);
            }

        }
    }

    private void startCamera() {


        File direct = new File(Environment.getExternalStorageDirectory() + "/MyFolder/Images");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/MyFolder/Images/");
            wallpaperDirectory.mkdirs();
        }

        file = new File(new File("/sdcard/MyFolder/Images/"), System.currentTimeMillis() + ".jpg");
        imageUri = Uri.fromFile(file);


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                imageUri);


        startActivityForResult(takePictureIntent, 1);


    }

    private void openGallery(int pos) {

        startStorage();


    }

    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_TAKE_GALLERY);


    }

    private void initializeGUI() {

        appDelegate = (MyApplication) getApplicationContext();
        // Connection Detector Reference
        cd = new ConnectionDetector(getApplicationContext());

        // Session Manager
        session = new SessionManager(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");


        HashMap<String, String> user = session.getUserDetails();

        String profilepic = user.get(SessionManager.KEY_PIC);
        // Typeface Initialize
//        Typeface typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/FuturaStd-Medium.ttf");

        apikey = user.get(SessionManager.KEY_TOKEN);

        // TextView header1 = (TextView)findViewById(R.id.header1);
        // header1.setTypeface(typeFace);


        postEt = findViewById(R.id.posttextEt);
        txtcount = findViewById(R.id.txtcount);
        postbtn = findViewById(R.id.postbtn);
        Uploadiv = findViewById(R.id.Uploadiv);
        profileIV = findViewById(R.id.profileIV);
        displayRecordedVideo = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = findViewById(R.id.progressbar);
        textData = (TextView) findViewById(R.id.textData);
        // postbtn.setBackgroundColor(Color.parseColor(colorActive));

        postbtn.setOnClickListener(this);


        final TextWatcher txwatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start,
                                          int count, int after) {
                int tick = start + after;
                if (tick < 128) {
                    int remaining = 500 - tick;
                    // txtcount1.setText(String.valueOf(remaining));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                txtcount.setText(String.valueOf(500 - s.length()) + "/");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                System.out.print("Hello");
            }
        };

        postEt.addTextChangedListener(txwatcher);


        // Initialize Constant Class Reference


        // User Image URL
        senderImageURL = ApiConstant.profilepic + profilepic;

        // Post Status & Image URL
        postUrl = ApiConstant.baseUrl + "PostNewsFeed";

        PicassoTrustAll.getInstance(this).load(senderImageURL)
                .placeholder(R.drawable.profilepic_placeholder)
                .into(profileIV);
        procializeDB = new DBHelper(PostActivity.this);
        db = procializeDB.getReadableDatabase();
        dbHelper = new DBHelper(PostActivity.this);
        customers = new ArrayList<AttendeeList>();
        userList = procializeDB.getAttendeeDetails();
        procializeDB.getReadableDatabase();
        // attendeesList = (ListView)
        // getActivity().findViewById(R.id.speakers_list);
        customers = dbHelper.getAttendeeDetails();
        mentions = new Mentions.Builder(this, postEt)
                .suggestionsListener(this)
                .queryListener(this)
                .build();

        setupMentionsList();

        // sms_count = (TextView) findViewById(R.id.textView2);

//        final TextWatcher txwatcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start,
//                                          int count, int after) {
//                int tick = start + after;
//                if (tick < 128) {
//                    int remaining = 500 - tick;
//                    // txtcount1.setText(String.valueOf(remaining));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//                txtcount1.setText(String.valueOf(500 - s.length()));
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // TODO Auto-generated method stub
//                System.out.print("Hello");
//            }
//        };
//
//        post_status_post.addTextChangedListener(txwatcher);

    }

    @Override
    public void onClick(View v) {

        if (v == postbtn) {


            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(postEt.getWindowToken(),
                            0);

            postMsg = postEt.getText().toString();
//            postMsg = StringEscapeUtils.escapeJava(postEt.getText().toString().trim());
            // post_status_post.setText("");
            // post_status_post
            // .setHint("What's on your mind (Not more than 500 characters)");

            final Comment comment = new Comment();
            comment.setComment(postMsg);
            comment.setMentions(mentions.getInsertedMentions());
            textData.setText(postMsg);

            postMsg = highlightMentions(textData, comment.getMentions());

            picturePath = appDelegate.getPostImagePath();
            // Check for Internet Connection
           /* if (picturePath.equalsIgnoreCase("")) {
                Toast.makeText(PostActivity.this,
                        "Select Image", Toast.LENGTH_SHORT)
                        .show();
            } else {*/
            if (cd.isConnectingToInternet()) {

                if (actionFlag.equalsIgnoreCase("image")) {
                    if (appDelegate.getPostImagePath() != null
                            && appDelegate.getPostImagePath().length() > 0) {
                        System.out
                                .println("Post Image URL  inside SubmitPostTask :"
                                        + appDelegate.getPostImagePath());

                        appDelegate.setPostImagePath("");
                        // post_status_post
                        // .setText("What's on your mind (Not more than 500 characters)");
                        new SubmitPostTask().execute();
                    } else {
                        Toast.makeText(PostActivity.this,
                                "Select Image", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    System.out
                            .println("Post Image URL  inside SubmitStatusOnlyTask :"
                                    + appDelegate.getPostImagePath());

                    if (postMsg.length() > 0) {
                        String emoji = escapeJavaString(postMsg);
                        // StringEscapeUtils.escapeJava(toServer);
                        // Character strChar = postMsg.charAt(0);
                        new SubmitStatusOnlyTask().execute(postUrl);

                        // String charStr = postMsg.getText().toString();

                    } else {
                        Toast.makeText(PostActivity.this,
                                "Please enter status", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            }

            // }
        }

    }

    public String escapeJavaString(String st) {
        int ss1 = 0, ss2;
        Boolean high = true;
        StringBuilder builder = new StringBuilder();
        try {
            for (int i = 0; i < st.length(); i++) {
                char c = st.charAt(i);
                if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && !Character.isWhitespace(c)) {
                    String unicode = String.valueOf(c);
                    int code = (int) c;
                    if (!(code >= 0 && code <= 255)) {
                        unicode = Integer.toHexString(c);
                        if (high) {
                            high = false;
                            ss1 = Integer.parseInt(unicode, 16);
                        } else {
                            high = true;
                            ss2 = Integer.parseInt(unicode, 16);
                            char chars = Character.toChars(ss1)[0];
                            char chars2 = Character.toChars(ss2)[0];
                            int codepoint = Character.toCodePoint(chars, chars2);
                            unicode = "Ax" + codepoint;
                            builder.append(unicode);
                        }
                    }
                } else {
                    builder.append(c);
                }
            }
            //  Log.i(TAG, builder.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (data == null) {
//            Intent intent1=new Intent(PostActivity.this,HomeActivity.class);
//            startActivity(intent1);
//            finish();
//        } else {

        if (data != null) {
            if (data.getData() != null) {
                if (requestCode == REQUEST_TAKE_GALLERY && resultCode == RESULT_OK && data != null) {
//            if (data == null) {
//                Intent intent1 = new Intent(PostActivity.this, HomeActivity.class);
//                startActivity(intent1);
//                finish();
//            } else {
                    postEt.setHint("Say something about this photo");
                    Uploadiv.setVisibility(View.VISIBLE);

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
                    Glide.with(this).load(compressedImagePath).into(Uploadiv);


                    // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
                    // .into(post_thumbnail);

                    Toast.makeText(PostActivity.this, "Image selected",
                            Toast.LENGTH_SHORT).show();

                    cursor.close();
//            }
                } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                    setpic2();

                    // setPic();
                } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                    uri = data.getData();
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) displayRecordedVideo.getLayoutParams();
                    params.width = (int) (300 * metrics.density);
                    params.height = (int) (250 * metrics.density);

                    displayRecordedVideo.setLayoutParams(params);
                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                    displayRecordedVideo.setLayoutParams(params);
                    displayRecordedVideo.start();

                    if (Build.VERSION.SDK_INT > 22)
                        pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                    else
                        //else we will get path directly
                        pathToStoredVideo = uri.getPath();
                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                    //Store the video to your server
                    file = new File(pathToStoredVideo);

                    Uploadiv.setVisibility(View.GONE);
                    displayRecordedVideo.setVisibility(View.VISIBLE);

                    uri = data.getData();


                    displayRecordedVideo.setVideoURI(uri);

                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    android.widget.LinearLayout.LayoutParams params1 = (android.widget.LinearLayout.LayoutParams) displayRecordedVideo.getLayoutParams();
                    params.width = (int) (300 * metrics.density);
                    params.height = (int) (250 * metrics.density);

                    displayRecordedVideo.setLayoutParams(params1);
                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                    displayRecordedVideo.setLayoutParams(params1);
                    displayRecordedVideo.start();
                    try {
                        if (uri != null) {

                            MediaPlayer mp = MediaPlayer.create(this, uri);
                            int duration = mp.getDuration();
                            mp.release();

                            if ((duration / 1000) > 15) {
                                // Show Your Messages
                                Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                if (Build.VERSION.SDK_INT > 22) {
                                    pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                } else {
                                    //else we will get path directly
                                    pathToStoredVideo = uri.getPath();

                                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                }
                                file = new File(pathToStoredVideo);

                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                    Uri selectedMediaUri = data.getData();
                    if (selectedMediaUri != null) {
                        if (selectedMediaUri.toString().contains("video")) {

                            if (data != null && data.getStringExtra("video") != null)

                                Uploadiv.setVisibility(View.GONE);
                            displayRecordedVideo.setVisibility(View.VISIBLE);
                            if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                                Uri selectedImageUri = data.getData();

                                // OI FILE Manager
                                selectedImagePath = selectedImageUri.getPath();

                                // MEDIA GALLERY
                                selectedImagePath = getPath(PostActivity.this, selectedImageUri);
                                uri = selectedImageUri;
                                if (selectedImagePath != null) {

                                    displayRecordedVideo.setVideoURI(selectedImageUri);
                                    DisplayMetrics metrics = new DisplayMetrics();
                                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                    android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) displayRecordedVideo.getLayoutParams();
                                    params.width = (int) (300 * metrics.density);
                                    params.height = (int) (250 * metrics.density);

                                    displayRecordedVideo.setLayoutParams(params);
                                    displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                    displayRecordedVideo.setLayoutParams(params);
                                    displayRecordedVideo.start();
                                    try {
                                        if (uri != null) {

                                            MediaPlayer mp = MediaPlayer.create(this, uri);
                                            int duration = mp.getDuration();
                                            mp.release();

                                            if ((duration / 1000) > 15) {
                                                // Show Your Messages
                                                Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                                if (Build.VERSION.SDK_INT > 22) {
                                                    pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                                                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                                } else {
                                                    //else we will get path directly
                                                    pathToStoredVideo = uri.getPath();

                                                    Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                                }
                                                file = new File(pathToStoredVideo);

                                            }
                                        } else {

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_VIDEO_CAPTURE) {
                                uri = data.getData();

                                displayRecordedVideo.setVideoURI(uri);
                                DisplayMetrics metrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) displayRecordedVideo.getLayoutParams();
                                params.width = (int) (300 * metrics.density);
                                params.height = (int) (250 * metrics.density);

                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.start();

                                try {
                                    if (uri != null) {

                                        MediaPlayer mp = MediaPlayer.create(this, uri);
                                        int duration = mp.getDuration();
                                        mp.release();

                                        if ((duration / 1000) > 15) {
                                            // Show Your Messages
                                            Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);
                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            } else {
                                                //else we will get path directly
                                                pathToStoredVideo = uri.getPath();

                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            }
                                            file = new File(pathToStoredVideo);

                                        }
                                    } else {

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (resultCode == Activity.RESULT_OK) {
                                Uploadiv.setVisibility(View.GONE);
                                displayRecordedVideo.setVisibility(View.VISIBLE);

                                uri = data.getData();


                                displayRecordedVideo.setVideoURI(uri);
                                DisplayMetrics metrics = new DisplayMetrics();
                                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) displayRecordedVideo.getLayoutParams();
                                params.width = (int) (300 * metrics.density);
                                params.height = (int) (250 * metrics.density);

                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.setVideoURI(uri);
//                    displayRecordedVideo.setMediaController(new MediaController(this));
//                    ViewGroup.LayoutParams params = displayRecordedVideo.getLayoutParams();
//
//                    params.height = 600;
                                displayRecordedVideo.setLayoutParams(params);
                                displayRecordedVideo.start();


                                try {
                                    if (uri != null) {

                                        MediaPlayer mp = MediaPlayer.create(this, uri);
                                        int duration = mp.getDuration();
                                        mp.release();

                                        if ((duration / 1000) > 15) {
                                            // Show Your Messages
                                            Toast.makeText(PostActivity.this, "Please select video length less than 15 sec", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //Store the video to your server


//                                    pathToStoredVideo = getRealPathFromURIPathVideo(data.getData(),PostViewActivity.this);


                                            if (Build.VERSION.SDK_INT > 22) {
                                                pathToStoredVideo = ImagePath_MarshMallow.getPath(PostActivity.this, uri);
                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            } else {
                                                //else we will get path directly
                                                pathToStoredVideo = uri.getPath();

                                                Log.d("video", "Recorded Video Path " + pathToStoredVideo);
                                            }
                                            file = new File(pathToStoredVideo);

                                        }
                                    } else {

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }

            } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
                setpic2();
            } else {
                finish();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();
        } else {
            finish();
        }
//        }

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
                        "com.procialize.eventsapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void setpic2() {

        Uploadiv.setVisibility(View.VISIBLE);
        //selfieSubmit.setVisibility(View.VISIBLE);
        //edtImagename.setVisibility(View.VISIBLE);


        String compressedImagePath = compressImage(mCurrentPhotoPath);
        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(Uploadiv);


        Toast.makeText(PostActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }

    private void setPic() {
        // Get the dimensions of the View
        //int targetW = post_thumbnail.getWidth();
        //int targetH = post_thumbnail.getHeight();

        int targetW = 400;
        int targetH = 600;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        // mImageView.setImageBitmap(bitmap);

        //    String compressedImagePath = compressImage(mCurrentPhotoPath);


        Glide.with(this).load(mCurrentPhotoPath).into(Uploadiv);


        Toast.makeText(PostActivity.this, "Image selected" + mCurrentPhotoPath,
                Toast.LENGTH_SHORT).show();

    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                 && null != data) {

             post_status_post.setHint("Say something about this photo");
             post_status_post.setEnabled(true);
             post_thumbnail.setVisibility(View.VISIBLE);


             Uri selectedImage = data.getData();
             String[] filePathColumn = {MediaStore.Images.Media.DATA};

             Cursor cursor = getContentResolver().query(selectedImage,
                     filePathColumn, null, null, null);
             cursor.moveToFirst();

             int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             String picturePath = cursor.getString(columnIndex);

             String compressedImagePath = compressImage(picturePath);
             appDelegate.setPostImagePath(compressedImagePath);

             PicassoTrustAll.getInstance(this).load(compressedImagePath).into(post_thumbnail);

             // PicassoTrustAll.getInstance(PostActivity.this).load(compressedImagePath)
             // .into(post_thumbnail);

             Toast.makeText(PostActivity.this, "Image selected",
                     Toast.LENGTH_SHORT).show();

             cursor.close();
         }
     }
 */
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                showAlert(R.array.selectType);


            } else {
                Toast.makeText(this, "No permission to read external storage.",
                        Toast.LENGTH_SHORT).show();

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
/*if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();

            } else {
                Toast.makeText(this, "No permission to capture photo.",
                        Toast.LENGTH_SHORT).show();

            }*/
        }
    }


    @Override
    public void onDestroy() {

        dismissProgress();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appDelegate.setPostImagePath("");

    }

    private void selectVideo() {
        final CharSequence[] items = {"Take Video", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Video!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(PostActivity.this);

                if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        videogalleryIntent();

                } else if (items[item].equals("Take Video")) {
                    userChoosenTask = "Take Video";
                    if (result) {
                        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_FULL_SCREEN, true);
                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//                        videoCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); //0 means low & 1 means high
                        if (videoCaptureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(videoCaptureIntent, REQUEST_VIDEO_CAPTURE);
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                    Intent intent = new Intent(PostActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void videogalleryIntent() {
//        Intent intent = new Intent();
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/*"});
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_TAKE_GALLERY_VIDEO);
    }

    public void showProgress() {
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progrssdialogback);
        postbtn.setEnabled(false);
        postEt.setEnabled(false);
        progressbar.setVisibility(View.VISIBLE);
        progressbar.setProgress(0);
        progressbar.setMaxValue(100);
        progressbar.setProgressColor(Color.parseColor(colorActive));
        progressbar.setText(String.valueOf(0));
        progressbar.setTextColor(Color.parseColor(colorActive));
        progressbar.setSuffix("%");
        progressbar.setPrefix("");

    }

    public void dismissProgress() {
        postbtn.setEnabled(true);
        postEt.setEnabled(true);
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onProgressUpdate(int percentage) {
        Log.e("per", String.valueOf(percentage));
//        progressbar.showValue(99f, 100f, true);
        progressbar.setProgress(percentage);
        progressbar.setText(String.valueOf(percentage));
    }

    @Override
    public void onError() {
        dismissProgress();
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void onQueryReceived(String s) {
        final List<AttendeeList> users = searchUsers(s);
        if (users != null && !users.isEmpty()) {
            usersAdapter.clear();
            usersAdapter.setCurrentQuery(s);
            usersAdapter.addAll(users);
            showMentionsList(true);
        } else {
            showMentionsList(false);
        }
    }

    @Override
    public void displaySuggestions(boolean b) {
        if (b) {
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list_layout);
        }
    }

    public List<AttendeeList> searchUsers(String query) {
        final List<AttendeeList> searchResults = new ArrayList<>();
        if (StringUtils.isNotBlank(query)) {
            query = query.toLowerCase(Locale.US);
            if (userList != null && !userList.isEmpty()) {
                for (AttendeeList user : userList) {
                    final String firstName = user.getFirstName().toLowerCase();
                    final String lastName = user.getLastName().toLowerCase();
                    if (firstName.startsWith(query) || lastName.startsWith(query)) {
                        searchResults.add(user);
                    }
                }
            }

        }
        return searchResults;
    }


    private void showMentionsList(boolean display) {
        com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list_layout);
        if (display) {
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_empty_view);
        } else {
            com.percolate.caffeine.ViewUtils.hideView(this, R.id.mentions_list);
            com.percolate.caffeine.ViewUtils.showView(this, R.id.mentions_empty_view);
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
                builder.addFormDataPart("event_id", eventId);

                builder.addFormDataPart("type", actionFlag);
                builder.addFormDataPart("status", StringEscapeUtils.escapeJava(postMsg));

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

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
                finish();

            } else {
                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }

    public class SubmitStatusOnlyTask extends
            AsyncTask<String, String, JSONObject> {

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
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("user_type", actionFlag);
                builder.addFormDataPart("status", StringEscapeUtils.unescapeJava(postMsg));


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

            }


            return json;

        }

        @Override
        protected void onPostExecute(JSONObject result) {

            super.onPostExecute(result);
            // Dismiss the progress dialog

            dismissProgress();

            // String s = "";
            // String message = "";
            // Toast.makeText(activity, "Status Posted Successfully",
            // Toast.LENGTH_SHORT).show();

            // try {
            // s = result.get("error").toString();
            // message = result.get("msg").toString();
            // } catch (JSONException e) {
            // e.printStackTrace();
            // }

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostActivity.this, HomeActivity.class);
                startActivity(MainIntent);
            } else {
                Toast.makeText(PostActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }

            // try {
            // s = result.get("error").toString();
            // message = result.get("msg").toString();
            // if (s.equalsIgnoreCase("success")) {
            //
            // Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            // .show();
            //
            // } else {
            // Toast.makeText(activity, message, Toast.LENGTH_SHORT)
            // .show();
            // }
            // } catch (JSONException e1) {
            // e1.printStackTrace();
            // }

            // if (s.equalsIgnoreCase("success"))

            // new FetchWallNotification().execute();
            // try {
            // status = result.getJSONObject("error");
            // s = status.getString("error");
            // if (s.equalsIgnoreCase("success")) {
            //
            // Toast.makeText(activity, " Status Posted Successfully",
            // Toast.LENGTH_SHORT).show();
            // } else {
            //
            //
            //
            // Toast.makeText(activity, "Update failed",
            // Toast.LENGTH_SHORT).show();
            // }
            //
            // } catch (JSONException e) {
            // e.printStackTrace();
            // }

        }
    }

    private void setupMentionsList() {
        final RecyclerView mentionsList = findViewById(R.id.mentions_list);
        mentionsList.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(this);
        mentionsList.setAdapter(usersAdapter);
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(postEt.getWindowToken(), 0);
        // set on item click listener
        mentionsList.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final AttendeeList user = usersAdapter.getItem(position);

                /*
                 * We are creating a mentions object which implements the
                 * <code>Mentionable</code> interface this allows the library to set the offset
                 * and length of the mention.
                 */
                if (user != null) {
                    final Mention mention = new Mention();
                    mention.setMentionName(user.getFirstName() + " " + user.getLastName() + " ");
                    mention.setMentionid(user.getAttendeeId());
                    mentions.insertMention(mention);


                }
            }
        }));
    }

    private String highlightMentions(TextView commentTextView, final List<Mentionable> mentions) {
        String sample = null;
        int flag = 1;
        int flag2 = 1;

        if (commentTextView != null && mentions != null && !mentions.isEmpty()) {
            final SpannableStringBuilder spannable = new SpannableStringBuilder(commentTextView.getText());
            int start;
            int end;

            for (Mentionable mention : mentions) {
                if (mention != null) {
                    if (flag == 1) {
                        start = mention.getMentionOffset();
                        end = start + mention.getMentionLength();

                        if (commentTextView.length() >= end) {


//                        spannable.append(sample, start+1, end+1);

                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start, end) + ">";

//                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
                            spannable.setSpan(sample, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannable.replace(start, end, sample);
                            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                            flag = 2;
                        }
                    } else if (mentions.indexOf(mention) < 3) {

                        /*<23553-Sneha Deshmukh><23371-Anis Ansari><23362-Atish k>Bharat Rawal */
                        start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
                        end = start + mention.getMentionLength() + 1;
                        if (commentTextView.length() >= end) {
                            sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + 1, end) + ">";

//                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
                            spannable.setSpan(sample, start + mentions.indexOf(mention), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spannable.replace(start + mentions.indexOf(mention), end
                                    , sample);
                            commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                        }
                    } else if (mentions.indexOf(mention) >= 3) {
                        if (flag2 == 1) {
                            start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
                            end = start + mention.getMentionLength() + 2;
                            if (commentTextView.length() >= end) {
                                /*<23553-Sneha Deshmukh><23553-Sneha Deshmukh><23553-Sneha Deshmukh>Sneha Deshmukh Sneha Deshmukh */
                                sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + mentions.indexOf(mention) - 1, end + 1) + ">";

//                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
                                spannable.setSpan(sample, start + mentions.indexOf(mention) - 1, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.replace(start + mentions.indexOf(mention) - 1, end + 1
                                        , sample);
                                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                                flag2 = 2;
                            }
                        } else {
                            start = mention.getMentionOffset() + 6 * mentions.indexOf(mention);
                            end = start + mention.getMentionLength() + 3;
                            if (commentTextView.length() >= end) {
                                /*<23553-Sneha Deshmukh><23553-Sneha Deshmukh><23553-Sneha Deshmukh>Sneha Deshmukh Sneha Deshmukh */
                                sample = "<" + mention.getMentionid() + "^" + commentTextView.getText().toString().substring(start + mentions.indexOf(mention) - 1, end + 1) + ">";

//                        commentTextView = commentTextView.substring(start, end) + ">" + commentTextView.substring(end, commentTextView.length());
                                spannable.setSpan(sample, start + mentions.indexOf(mention), end + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                spannable.replace(start + mentions.indexOf(mention), end + 2
                                        , sample);
                                commentTextView.setText(spannable, TextView.BufferType.SPANNABLE);
                            }
                        }
                        Log.v("Final String", commentTextView.toString());


                    }
                }
            }
//            commentTextView.setText(spannable);
        }

        return commentTextView.getText().toString();
    }

}
