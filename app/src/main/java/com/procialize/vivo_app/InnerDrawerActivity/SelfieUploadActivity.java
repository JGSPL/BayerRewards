package com.procialize.vivo_app.InnerDrawerActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
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

import com.bumptech.glide.Glide;
import com.procialize.vivo_app.ApiConstant.APIService;
import com.procialize.vivo_app.ApiConstant.ApiUtils;
import com.procialize.vivo_app.GetterSetter.PostSelfie;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Session.SessionManager;
import com.procialize.vivo_app.Utility.Util;
import com.procialize.vivo_app.Utility.Utility;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SelfieUploadActivity extends AppCompatActivity {

    ImageView imgpreview;
    String userChoosenTask;
    Button btnSubmit;
    Uri capturedImageUri;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, REQUEST_TAKE_PHOTO = 2;
    File file = null;
    LinearLayout llData;
    SessionManager sessionManager;
    String apikey;
    TextInputEditText editTitle;
    APIService mAPIService;
    ProgressDialog progress;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventId;
    String mCurrentPhotoPath;
    ImageView headerlogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_upload);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventId = prefs.getString("eventid", "");


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
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);

        imgpreview = findViewById(R.id.imgpreview);
        btnSubmit = findViewById(R.id.btnSubmit);
        editTitle = findViewById(R.id.editTitle);

        sessionManager = new SessionManager(this);

        mAPIService = ApiUtils.getAPIService();

        llData = findViewById(R.id.llData);
        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);


        selectImage();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String data = editTitle.getText().toString();
//                if (data.equals("")) {
//
//                } else {
                showProgress();


                RequestBody token = RequestBody.create(MediaType.parse("text/plain"), apikey);
                RequestBody eventid = RequestBody.create(MediaType.parse("text/plain"), eventId);
                RequestBody status = RequestBody.create(MediaType.parse("text/plain"), StringEscapeUtils.escapeJava(data));
                MultipartBody.Part body = null;

                if (file != null) {

                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    body = MultipartBody.Part.createFormData("selfie_image", file.getName(), reqFile);
                }


                postImage(token, eventid, status, body);
//                }
            }
        });

    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SelfieUploadActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result) {

//                        Calendar cal = Calendar.getInstance();
//                        File file = new File(Environment.getExternalStorageDirectory(), (cal.getTimeInMillis() + ".jpg"));
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        } else {
//                            file.delete();
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                        capturedImageUri = Uri.fromFile(file);
//                        imgpreview.setImageURI(capturedImageUri);
//
//                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);
//                        startActivityForResult(cameraIntent, REQUEST_CAMERA);
//                    }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // Android M Permission check
                            if (SelfieUploadActivity.this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED && SelfieUploadActivity.this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                                final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                                final String[] permissionswrite = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
                                ActivityCompat.requestPermissions(SelfieUploadActivity.this, permissionswrite, 0);
                                ActivityCompat.requestPermissions(SelfieUploadActivity.this, permissions, 0);
                            } else {

                                cameraTask();

                            }

                        } else {
                            cameraTask();

                        }
                    }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result) {
                        imagegalleryIntent();
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

    private void imagegalleryIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    public void cameraTask() {


        Log.i("android", "startStorage");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            // Android M Permission check
            if (this.checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                final String[] permissions = new String[]{android.Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(SelfieUploadActivity.this,
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
            file = null;
            try {
                file = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.procialize.vivo_app.android.fileprovider",
                        file);
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

    private String getRealPathFromURIImage(String contentURI) {
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

        imgpreview.setVisibility(View.VISIBLE);
        //selfieSubmit.setVisibility(View.VISIBLE);
        //edtImagename.setVisibility(View.VISIBLE);


        String compressedImagePath = compressImage(mCurrentPhotoPath);
//        appDelegate.setPostImagePath(compressedImagePath);

        Glide.with(this).load(compressedImagePath).into(imgpreview);


        Toast.makeText(SelfieUploadActivity.this, "Image selected",
                Toast.LENGTH_SHORT).show();

    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURIImage(imageUri);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        llData.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setpic2();

        } else if (resultCode == this.RESULT_OK && requestCode == SELECT_FILE) {

            onSelectFromGalleryResult(data);

        } else if (resultCode == this.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO) {

            llData.setVisibility(View.VISIBLE);
            if (capturedImageUri != null) {
                file = new File(capturedImageUri.getPath());
                imgpreview.setImageURI(capturedImageUri);
            }
        } else {
            finish();
        }

    }

    private void onSelectFromGalleryResult(Intent data) {

//        imgpreview.setVisibility(View.VISIBLE);
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                imgpreview.setImageBitmap(bm);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(tempUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgpreview.setImageBitmap(bm);
    }

    private void onCaptureImageResult() {
        file = new File(getRealPathFromURI(capturedImageUri));


        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), capturedImageUri);
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, 120, 120);

            imgpreview.setImageBitmap(bitmap);
            // imageView.setImageBitmap(thumbBitmap);
        } catch (IOException ex) {
            //......
        }


    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void postImage(RequestBody api_access_token, RequestBody eventid, RequestBody status, MultipartBody.Part fbody) {
        mAPIService.PostSelfie(api_access_token, eventid, status, fbody).enqueue(new Callback<PostSelfie>() {
            @Override
            public void onResponse(Call<PostSelfie> call, Response<PostSelfie> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostSelfie> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                Log.e("hit", t.getMessage());
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<PostSelfie> response) {

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

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
