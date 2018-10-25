package com.procialize.vivo_app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.procialize.vivo_app.CustomTools.PicassoTrustAll;
import com.procialize.vivo_app.DbHelper.ConnectionDetector;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Utility.Util;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageViewActivity extends AppCompatActivity {

    PhotoView imageView;
    LinearLayout savebtn, sharebtn;
    String url;
    public ProgressDialog progressDialog;
    private ProgressBar progressBar;
    ImageView headerlogoIv;
    private ConnectionDetector cd;

    String imgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        cd = new ConnectionDetector(getApplicationContext());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            imgname = url.substring(58,60);

        }


        imageView = findViewById(R.id.webView1);
        savebtn = findViewById(R.id.savebtn);
        sharebtn = findViewById(R.id.sharebtn);
        progressBar = findViewById(R.id.progressBar);

        Glide.with(this).load(url)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(imageView).onLoadStarted(this.getDrawable(R.drawable.gallery_placeholder));


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ImageViewActivity.this.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(ImageViewActivity.this, permissions, 0);
                        } else if (ImageViewActivity.this.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions(ImageViewActivity.this, permissions, 0);
                        } else {

                            // new myAsyncTask().execute();
                            PicassoTrustAll.getInstance(ImageViewActivity.this)
                                    .load(url)
                                    .into(new com.squareup.picasso.Target() {
                                              @Override
                                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                  try {
                                                      String root = Environment.getExternalStorageDirectory().toString();
                                                      File myDir = new File(root + "/vivo");

                                                      if (!myDir.exists()) {
                                                          myDir.mkdirs();
                                                      }
                                                      Toast.makeText(ImageViewActivity.this,
                                                              "Download completed- check folder vivo/Image",
                                                              Toast.LENGTH_SHORT).show();
                                                      String name = imgname + ".jpg";
                                                      myDir = new File(myDir, name);
                                                      FileOutputStream out = new FileOutputStream(myDir);
                                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                      out.flush();
                                                      out.close();
                                                  } catch(Exception e){
                                                      // some action
                                                  }
                                              }

                                              @Override
                                              public void onBitmapFailed(Drawable errorDrawable) {
                                              }

                                              @Override
                                              public void onPrepareLoad(Drawable placeHolderDrawable) {
                                              }
                                          }
                                    );

                        }



                    } else {
                        //new myAsyncTask().execute();
                        PicassoTrustAll.getInstance(ImageViewActivity.this)
                                .load(url)
                                .into(new com.squareup.picasso.Target() {
                                          @Override
                                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                              try {
                                                  String root = Environment.getExternalStorageDirectory().toString();
                                                  File myDir = new File(root + "/vivo");

                                                  if (!myDir.exists()) {
                                                      myDir.mkdirs();
                                                  }
                                                  Toast.makeText(ImageViewActivity.this,
                                                          "Download completed- check folder vivo/Image",
                                                          Toast.LENGTH_SHORT).show();
                                                  String name = imgname + ".jpg";
                                                  myDir = new File(myDir, name);

                                                  FileOutputStream out = new FileOutputStream(myDir);
                                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                  out.flush();
                                                  out.close();
                                              } catch(Exception e){
                                                  // some action
                                              }
                                          }

                                          @Override
                                          public void onBitmapFailed(Drawable errorDrawable) {
                                          }

                                          @Override
                                          public void onPrepareLoad(Drawable placeHolderDrawable) {
                                          }
                                      }
                                );

                    }


                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }

/*
                ImageViewActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        new ImageDownloadAndSave().execute();
                    }
                });
*/

            }
        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(url, ImageViewActivity.this);
            }
        });
    }


    private class ImageDownloadAndSave extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... arg0) {
            ShowProgress();
            downloadImagesToSdCard(url, "download.jpg");
            return null;
        }

        private void downloadImagesToSdCard(String downloadUrl, String imageName) {
            try {
                URL url = new URL(downloadUrl);
                /* making a directory in sdcard */
                String sdCard = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(sdCard, "vivo");

                /*  if specified not exist create new */
                if (!myDir.exists()) {
                    myDir.mkdir();
                    Log.v("", "inside mkdir");
                }

                /* checks the file and if it already exist delete */
                String fname = imageName;
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();

                /* Open a connection */
                URLConnection ucon = url.openConnection();
                InputStream inputStream = null;
                HttpURLConnection httpConn = (HttpURLConnection) ucon;
                httpConn.setRequestMethod("GET");
                httpConn.connect();

                if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = httpConn.getInputStream();
                }

                FileOutputStream fos = new FileOutputStream(file);
                int totalSize = httpConn.getContentLength();
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    Log.i("Progress:", "downloadedSize:" + downloadedSize + "totalSize:" + totalSize);
                }

                fos.close();
                CancelProgress();
                Log.d("test", "Image Saved in sdcard..");

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "File Save Successfully", Toast.LENGTH_SHORT).show();
                                CancelProgress();
                            }
                        }
                );

            } catch (IOException io) {
                CancelProgress();
                Toast.makeText(getApplicationContext(), "File Not Save", Toast.LENGTH_SHORT).show();
                io.printStackTrace();
            } catch (Exception e) {
                CancelProgress();
                Toast.makeText(getApplicationContext(), "File Not Save", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public void ShowProgress() {
        if (!progressDialog.isShowing()) {

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.show();
                        }
                    }
            );

        }
    }

    public void CancelProgress() {
        if (progressDialog.isShowing()) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }
            );
        }
    }


    static public void shareImage(String url, final Context context) {
        Picasso.with(context).load(url).into(new com.squareup.picasso.Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
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
            bmpUri = FileProvider.getUriForFile(context, "com.procialize.vivo_app.android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
