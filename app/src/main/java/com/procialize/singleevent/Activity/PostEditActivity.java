package com.procialize.singleevent.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.CustomTools.PicassoTrustAll;
import com.procialize.singleevent.CustomTools.ScaledImageView;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.MyApplication;
import com.squareup.picasso.Picasso;

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
import java.util.Date;
import java.util.HashMap;

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

import static org.apache.http.HttpVersion.HTTP_1_1;

public class PostEditActivity extends AppCompatActivity implements OnClickListener {

    private ImageView back_edt_post, wall_sender_thumbnail_post;

    private ScaledImageView post_thumbnail;
    private LinearLayout back_edt_post_layout, post_btn_layout;
    private TextView post_txt_post, txtcount1;
    private EditText post_status_post;
    private ProgressDialog pDialog;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ApiConstant constant;
    File sourceFile;
    String profilepic;
    private String postUrl = "";
    ImageView imgPlay;
    private String senderImageURL = "";
    ProgressBar progressbar;
    private String actionFlag;

    private static int RESULT_LOAD_IMAGE = 1;

    private String postMsg = "";

    private String picturePath = "";
    EditText postEt;
    Button postbtn;
    MyApplication appDelegate;
    ImageView Uploadiv;
    // Access Token Variable
    private String accessToken, mCurrentPhotoPath;
    ImageView profileIV;
    // Session Manager Class
    SessionManager session;

    String eventId;
    String type_of_user;
    // Connection Detector class
    private ConnectionDetector cd;
    //	private UserProfile userData;
//	MixpanelAPI mixpanel;
    private String ImageFlag = "0";
    private String notify_id, NotifyType, wallStatus, ImageStatus;
    static final int REQUEST_TAKE_PHOTO = 1;
    String MY_PREFS_NAME = "ProcializeInfo";
    private VideoView displayRecordedVideo;

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                appDelegate.setPostImagePath("");
            }
        });
        // mixpanel.track("Agenda Detail Page");


        // mixpanel.track("Wall Post Page");

        actionFlag = getIntent().getExtras().getString("for");
        notify_id = getIntent().getExtras().getString("feedid");
//        NotifyType = getIntent().getExtras().getString("NotificationType");
        wallStatus = getIntent().getExtras().getString("status");
        ImageStatus = getIntent().getExtras().getString("Image");

        session = new SessionManager(getApplicationContext());
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "1");
        HashMap<String, String> user = session.getUserDetails();




        // InitializeGUI
        initializeGUI();
        profilepic = user.get(SessionManager.KEY_PIC);

        if (profilepic != null) {
            Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            }).into(profileIV).onLoadStarted(getDrawable(R.drawable.profilepic_placeholder));
        } else {
            profileIV.setImageResource(R.drawable.profilepic_placeholder);

        }

        //post_status_post.setHint("What's on your mind?");
        if (actionFlag.equalsIgnoreCase("Status")) {
            //post_status_post.setText(wallStatus);
            postEt.setText(StringEscapeUtils.unescapeJava(wallStatus));

            //postEt.setText(getEmojiFromString(wallStatus));
            Uploadiv.setVisibility(View.GONE);

        } else if (actionFlag.equalsIgnoreCase("Image")) {
           // postEt.setText(getEmojiFromString(wallStatus));
            postEt.setText(StringEscapeUtils.unescapeJava(wallStatus));
			/*if (ImageStatus.toString().length() > 5){
				post_status_post.setText(getEmojiFromString(ImageStatus));
		}else {
			post_status_post.setText((ImageStatus));
		}*/
            Uploadiv.setVisibility(View.VISIBLE);
            //Glide.with(this).load(constant.WEBSERVICE_URL + constant.STATUS_IMAGE_URL+ wallStatus).into(post_thumbnail);
            PicassoTrustAll.getInstance(this).load(ApiConstant.newsfeedwall + ImageStatus)
                    .placeholder(R.drawable.gallery_placeholder)
                    .into(Uploadiv);
            if (ImageFlag.equalsIgnoreCase("0")) {
                picturePath = ApiConstant.newsfeedwall + ImageStatus;
                appDelegate.setPostImagePath(picturePath);

            }


        }


        //post_thumbnail.setVisibility(View.GONE);

        Uploadiv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (actionFlag.equalsIgnoreCase("Image")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // Android M Permission check
						/*if (PostEditActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && PostEditActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
							final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
							final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
							ActivityCompat.requestPermissions(PostEditActivity.this, permissionswrite, 0);
							ActivityCompat.requestPermissions(PostEditActivity.this, permissions, 0);
						} else {

							showAlert(R.array.selectType);

						}*/
                        if (PostEditActivity.this.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(PostEditActivity.this, permissions, 0);
                        } else if (PostEditActivity.this.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(PostEditActivity.this, permissions, 0);
                        } else {

                            showAlert(R.array.selectType);

                        }

                    } else {
                        showAlert(R.array.selectType);

                    }

					/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						// Android M Permission check
						if (PostEditActivity.this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
							final String[] permissions = new String[] { "android.permission.READ_EXTERNAL_STORAGE" };
							ActivityCompat.requestPermissions(PostEditActivity.this,
									permissions, 0);
						} else {

							Intent i = new Intent(
									Intent.ACTION_PICK,
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
							startActivityForResult(i, RESULT_LOAD_IMAGE);
						}

					}
					else
					{
						Intent i = new Intent(
								Intent.ACTION_PICK,
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);
					}
*/
                }


            }
        });


    }

    public void showAlert(int res) {


        new MaterialDialog.Builder(PostEditActivity.this)
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
                .show();
    }

    private void selectType(int pos) {
        if (pos == 0) {
            ImageFlag = "1";

            openGallery(pos);

        } else if (pos == 1) {
            ImageFlag = "1";

            cameraTask(pos);

        }

    }

    private void openGallery(int pos) {

        startStorage();


    }


    public void startStorage() {

        Log.i("android", "startStorage");


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);


    }

    public void cameraTask(int imgPos) {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(PostEditActivity.this,
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
                        "com.procialize.singleevent.android.fileprovider",
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
        picturePath = compressImage(mCurrentPhotoPath);

        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(Uploadiv);


        Toast.makeText(PostEditActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }


    public static String getEmojiFromString(String emojiString) {

        if (!emojiString.contains("\\u")) {

            return emojiString;
        }
        String EmojiEncodedString = "";

        int position = emojiString.indexOf("\\u");

        while (position != -1) {

            if (position != 0) {
                EmojiEncodedString += emojiString.substring(0, position);
            }

            String token = emojiString.substring(position + 2, position + 6);
            emojiString = emojiString.substring(position + 6);
            EmojiEncodedString += (char) Integer.parseInt(token, 16);
            position = emojiString.indexOf("\\u");
        }
        EmojiEncodedString += emojiString;

        return EmojiEncodedString;
    }

    private void initializeGUI() {

        appDelegate = (MyApplication) getApplicationContext();

        // Connection Detector Reference
        cd = new ConnectionDetector(getApplicationContext());

        // Session Manager
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        String profilepic = user.get(SessionManager.KEY_PIC);
        // Typeface Initialize
//        Typeface typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/FuturaStd-Medium.ttf");

        accessToken = user.get(SessionManager.KEY_TOKEN);

        // Typeface Initialize
//        Typeface typeFace = Typeface.createFromAsset(getAssets(),
//                "fonts/FuturaStd-Medium.ttf");

        //TextView txtheader = (TextView)findViewById(R.id.txtheader);
        //txtheader.setTypeface(typeFace);


        postEt = findViewById(R.id.posttextEt);
        postbtn = findViewById(R.id.postbtn);
        Uploadiv = findViewById(R.id.Uploadiv);
        profileIV = findViewById(R.id.profileIV);
        displayRecordedVideo = findViewById(R.id.Upvideov);
        imgPlay = findViewById(R.id.imgPlay);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        postbtn.setOnClickListener(this);
		procializeDB = new DBHelper(PostEditActivity.this);
		db = procializeDB.getReadableDatabase();
		//userData = procializeDB.getUserProfile();
//
	//	type_of_user = userData.getAttendee_type();

        // Initialize Constant Class Reference
        constant = new ApiConstant();

        // User Image URL
//        senderImageURL = ApiConstant.newsfeedwall + ;

        // Post Status & Image URL
        postUrl = ApiConstant.baseUrl + "EditNewsFeed";

//        Picasso.with(this).load(senderImageURL)
//                .placeholder(R.drawable.profilepic_placeholder)
//                .into(Uploadiv);

        //sms_count = (TextView) findViewById(R.id.textView2);


//        final TextWatcher txwatcher = new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//                int tick = start + after;
//                if (tick < 500) {
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
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//                System.out.print("Hello");
//            }
//        };
//
//        post_status_post.addTextChangedListener(txwatcher);

    }

    @Override
    public void onClick(View v) {

        if (v == back_edt_post || v == back_edt_post_layout) {
            appDelegate.setPostImagePath("");

            finish();

            /*
             * Apply our splash exit (fade in) and main entry (fade out)
             * animation transitions.
             */
//			overridePendingTransition(R.anim.pull_in_left,
//					R.anim.pull_out_right);

        } else if (v == postbtn) {

            // mixpanel.track("Wall Post Button");

//			try {
//				JSONObject props = new JSONObject();
//				props.put("Name", MainActivity.sname);
//				props.put("Designation", MainActivity.sdesig);
//				props.put("Email", MainActivity.semail);
//				props.put("City", MainActivity.scity);
//
//				mixpanel.track("WallPost Button Clicked", props);
//
//			} catch (JSONException e) {
//				Log.e("MYAPP", "Unable to add properties to JSONObject", e);
//			}

            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(postEt.getWindowToken(),
                            0);

            //postMsg = post_status_post.getText().toString();

            postMsg = StringEscapeUtils.escapeJava(postEt.getText().toString());

            // post_status_post.setText("");
            // post_status_post
            // .setHint("What's on your mind (Not more than 400 characters)");
            picturePath = appDelegate.getPostImagePath();

            // Check for Internet Connection
            if (cd.isConnectingToInternet()) {
                if (actionFlag.equalsIgnoreCase("Image") || actionFlag.equalsIgnoreCase("Video")) {

                    postbtn.setEnabled(false);
                    System.out
                            .println("Post Image URL  inside SubmitPostTask :"
                                    + appDelegate.getPostImagePath());

                    appDelegate.setPostImagePath("");
                    // post_status_post
                    // .setText("What's on your mind (Not more than 400 characters)");
                    new SubmitPostTask().execute(postUrl);
                } else {
                    System.out
                            .println("Post Image URL  inside SubmitStatusOnlyTask :" + appDelegate.getPostImagePath());
                    postbtn.setEnabled(false);
                    if (postMsg.length() > 0) {
                        new SubmitStatusOnlyTask().execute(postUrl);
                    } else {
                        Toast.makeText(PostEditActivity.this,
                                "Please enter post", Toast.LENGTH_SHORT)
                                .show();
                    }

                }
            } else {
                Toast.makeText(getBaseContext(), "No Internet Connection",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

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

            Toast.makeText(PostEditActivity.this, "Image selected",
                    Toast.LENGTH_SHORT).show();

            cursor.close();
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();

            // setPic();
        }


			/*post_status_post.setHint("Say something about this photo");

			post_thumbnail.setVisibility(View.VISIBLE);

			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);

			String compressedImagePath = compressImage(picturePath);
			appDelegate.setPostImagePath(compressedImagePath);

			Glide.with(this).load(compressedImagePath).into(post_thumbnail);

			// Picasso.with(PostEditActivity.this).load(compressedImagePath)
			// .into(post_thumbnail);

			Toast.makeText(PostEditActivity.this, "Image selected",
					Toast.LENGTH_SHORT).show();

			cursor.close();*/

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

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
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

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

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

    /*public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;

        String error = "";
        String message = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PostEditActivity.this,R.style.Base_Theme_AppCompat_Dialog_Alert);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            try {
                entity.addPart("api_access_token", new StringBody(accessToken));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            try {
                entity.addPart("noty_id", new StringBody(notify_id));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            try {
                entity.addPart("user_type", new StringBody(type_of_user));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }
            try {
                entity.addPart("status", new StringBody(postMsg));
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            if(ImageFlag.equalsIgnoreCase("1")){

            if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {

                File file = new File(picturePath);
                FileBody encFile = new FileBody(file, "image/jpeg/jpg");
                Log.w("TAG", "image file-> " + encFile);
                entity.addPart("wall_image", encFile);
            }
        }
            httppost.setEntity(entity);

            try {
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                is = httpEntity.getContent();
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json1 = sb.toString();
                Log.e("JSON", json1);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(json1);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // try parse the string to a JSON object
            try {
                json = new JSONObject(json1);
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
            if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }

            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();

            } else {
                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }*/
    public class SubmitPostTask extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;


        String message = "";

        String error = "";
        String msg = "";

        String res = "";

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
                if(ImageFlag.equalsIgnoreCase("1")) {

                    if (picturePath != null && !(picturePath.equalsIgnoreCase(""))) {
                        builder.addFormDataPart("media_file", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile));
                    }
                }

                builder.addFormDataPart("api_access_token", accessToken);
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("news_feed_id", notify_id);

                builder.addFormDataPart("type", actionFlag);
                builder.addFormDataPart("status", postMsg);

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
           postbtn.setEnabled(true);
            if (error.equalsIgnoreCase("success")) {

                // notifyDataSetChanged();

                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostEditActivity.this, HomeActivity.class);
                startActivity(MainIntent);

            } else {
                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
                        .show();
            }


        }

    }


    //	public class SubmitStatusOnlyTask extends
//			AsyncTask<String, String, JSONObject> {
//
//		String json1 = "";
//		InputStream is = null;
//		JSONObject json = null;
//		JSONObject status;
//
//		String error = "";
//		String message = "";
//
//		@Override
//		protected void onPreExecute() {
//
//			super.onPreExecute();
//			// Showing progress dialog
//			pDialog = new ProgressDialog(PostEditActivity.this,R.style.Base_Theme_AppCompat_Dialog_Alert);
//			pDialog.setMessage("Please wait...");
//			pDialog.setCancelable(false);
//			pDialog.show();
//
//		}
//
//		@Override
//		protected JSONObject doInBackground(String... params) {
//
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpPost httppost = new HttpPost(params[0]);
//			MultipartEntity entity = new MultipartEntity(
//					HttpMultipartMode.BROWSER_COMPATIBLE);
//			try {
//				entity.addPart("api_access_token", new StringBody(accessToken));
//			} catch (UnsupportedEncodingException e) {
//
//				e.printStackTrace();
//			}
//			try {
//				entity.addPart("noty_id", new StringBody(notify_id));
//			} catch (UnsupportedEncodingException e) {
//
//				e.printStackTrace();
//			}
//			try {
//
//				entity.addPart("user_type", new StringBody(type_of_user));
//			} catch (UnsupportedEncodingException e) {
//
//				e.printStackTrace();
//			}
//			try {
//				entity.addPart("status", new StringBody(postMsg));
//			} catch (UnsupportedEncodingException e) {
//
//				e.printStackTrace();
//			}
//			/*try {
//				entity.addPart("wall_image", new StringBody(""));
//			} catch (UnsupportedEncodingException e) {
//
//				e.printStackTrace();
//			}
//*/
//
//			httppost.setEntity(entity);
//			try {
//				HttpResponse response = httpclient.execute(httppost);
//				HttpEntity httpEntity = response.getEntity();
//				is = httpEntity.getContent();
//			} catch (ClientProtocolException e) {
//
//				e.printStackTrace();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//
//			try {
//				BufferedReader reader = new BufferedReader(
//						new InputStreamReader(is, "iso-8859-1"), 8);
//				StringBuilder sb = new StringBuilder();
//				String line = null;
//				while ((line = reader.readLine()) != null) {
//					sb.append(line + "\n");
//				}
//				is.close();
//				json1 = sb.toString();
//				Log.e("JSON", json1);
//			} catch (Exception e) {
//				Log.e("Buffer Error", "Error converting result " + e.toString());
//			}
//
//			// try parse the string to a JSON object
//			try {
//				json = new JSONObject(json1);
//			} catch (JSONException e) {
//				Log.e("JSON Parser", "Error parsing data " + e.toString());
//			}
//
//			// try parse the string to a JSON object
//			try {
//				json = new JSONObject(json1);
//				error = json.getString("status");
//				message = json.getString("msg");
//			} catch (JSONException e) {
//				Log.e("JSON Parser", "Error parsing data " + e.toString());
//
//			}
//
//			return json;
//
//		}
//
//		@Override
//		protected void onPostExecute(JSONObject result) {
//
//			super.onPostExecute(result);
//			// Dismiss the progress dialog
//
//			if (pDialog != null) {
//				pDialog.dismiss();
//				pDialog = null;
//			}
//
//			// String s = "";
//			// String message = "";
//			// Toast.makeText(activity, "Status Posted Successfully",
//			// Toast.LENGTH_SHORT).show();
//
//			// try {
//			// s = result.get("error").toString();
//			// message = result.get("msg").toString();
//			// } catch (JSONException e) {
//			// e.printStackTrace();
//			// }
//
//			if (error.equalsIgnoreCase("success")) {
//
//				// notifyDataSetChanged();
//
//				Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
//						.show();
//
//				Intent MainIntent = new Intent(PostEditActivity.this, MainActivity.class);
//				startActivity(MainIntent);
//
//			} else {
//				Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
//						.show();
//			}
//
//			// try {
//			// s = result.get("error").toString();
//			// message = result.get("msg").toString();
//			// if (s.equalsIgnoreCase("success")) {
//			//
//			// Toast.makeText(activity, message, Toast.LENGTH_SHORT)
//			// .show();
//			//
//			// } else {
//			// Toast.makeText(activity, message, Toast.LENGTH_SHORT)
//			// .show();
//			// }
//			// } catch (JSONException e1) {
//			// e1.printStackTrace();
//			// }
//
//			// if (s.equalsIgnoreCase("success"))
//
//			// new FetchWallNotification().execute();
//			// try {
//			// status = result.getJSONObject("error");
//			// s = status.getString("error");
//			// if (s.equalsIgnoreCase("success")) {
//			//
//			// Toast.makeText(activity, " Status Posted Successfully",
//			// Toast.LENGTH_SHORT).show();
//			// } else {
//			//
//			//
//			//
//			// Toast.makeText(activity, "Update failed",
//			// Toast.LENGTH_SHORT).show();
//			// }
//			//
//			// } catch (JSONException e) {
//			// e.printStackTrace();
//			// }
//
//		}
//	}
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

                builder.addFormDataPart("api_access_token", accessToken);
                builder.addFormDataPart("event_id", eventId);
                builder.addFormDataPart("news_feed_id", notify_id);
                //builder.addFormDataPart("user_type", "A");
                builder.addFormDataPart("status", postMsg);
                builder.addFormDataPart("type", actionFlag);

                builder.addFormDataPart("media_file", "");


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
            postbtn.setEnabled(true);
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

                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
                        .show();

                Intent MainIntent = new Intent(PostEditActivity.this, HomeActivity.class);
                startActivity(MainIntent);
            } else {
                Toast.makeText(PostEditActivity.this, message, Toast.LENGTH_SHORT)
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
        }
		/*if (requestCode == 0) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			} else {
				Toast.makeText(this, "No permission to read external storage.",
						Toast.LENGTH_SHORT).show();

			}
		} else {
		}*/
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

    public void showProgress() {
        if (progressbar.getVisibility() == View.GONE) {
            progressbar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressbar.getVisibility() == View.VISIBLE) {
            progressbar.setVisibility(View.GONE);
        }
    }


}

