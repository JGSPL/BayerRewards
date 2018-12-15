package com.procialize.singleevent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Activity.ImageViewActivity;
import com.procialize.singleevent.CustomTools.PicassoTrustAll;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.GetterSetter.FirstLevelFilter;
import com.procialize.singleevent.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gauravnaik309 on 01/03/18.
 */

public class SwipepagerAdapter extends PagerAdapter {

    private List<FirstLevelFilter> images;
    private LayoutInflater inflater;
    private Context context;
    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive, img;
    private ConnectionDetector cd;

    public SwipepagerAdapter(Context context, List<FirstLevelFilter> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        cd = new ConnectionDetector(context);
        final FirstLevelFilter firstLevelFilter = images.get(position);

        img = firstLevelFilter.getFileName().substring(58, 60);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        TextView name = (TextView) myImageLayout.findViewById(R.id.name);
        LinearLayout linMsg = (LinearLayout) myImageLayout.findViewById(R.id.linMsg);
        LinearLayout linsave = (LinearLayout) myImageLayout.findViewById(R.id.linsave);
        TextView savebtn = (TextView) myImageLayout.findViewById(R.id.savebtn);
        TextView sharebtn = (TextView) myImageLayout.findViewById(R.id.sharebtn);
        linsave.setBackgroundColor(Color.parseColor(colorActive));
        linMsg.setBackgroundColor(Color.parseColor(colorActive));
        savebtn.setBackgroundColor(Color.parseColor(colorActive));
        sharebtn.setBackgroundColor(Color.parseColor(colorActive));
        name.setTextColor(Color.parseColor(colorActive));
        final ProgressBar progressBar = myImageLayout.findViewById(R.id.progressbar);

        Glide.with(context).load(firstLevelFilter.getFileName())
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(myImage).onLoadStarted(context.getDrawable(R.drawable.gallery_placeholder));

        name.setText(firstLevelFilter.getTitle());
        linMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (context.checkSelfPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions((Activity) context, permissions, 0);
                        } else if (context.checkSelfPermission(
                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                            ActivityCompat.requestPermissions((Activity) context, permissions, 0);
                        } else {

                            // new myAsyncTask().execute();
                            PicassoTrustAll.getInstance(context)
                                    .load(firstLevelFilter.getFileName())
                                    .into(new com.squareup.picasso.Target() {
                                              @Override
                                              public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                  try {
                                                      String root = Environment.getExternalStorageDirectory().toString();
                                                      File myDir = new File(root + "/Procialize");

                                                      if (!myDir.exists()) {
                                                          myDir.mkdirs();
                                                      }
                                                      Toast.makeText(context,
                                                              "Download completed- check folder Procialize/Image",
                                                              Toast.LENGTH_SHORT).show();
                                                      String name = img + ".jpg";
                                                      myDir = new File(myDir, name);
                                                      FileOutputStream out = new FileOutputStream(myDir);
                                                      bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                      out.flush();
                                                      out.close();
                                                  } catch (Exception e) {
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
                        PicassoTrustAll.getInstance(context)
                                .load(firstLevelFilter.getFileName())
                                .into(new com.squareup.picasso.Target() {
                                          @Override
                                          public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                              try {
                                                  String root = Environment.getExternalStorageDirectory().toString();
                                                  File myDir = new File(root + "/Procialize");

                                                  if (!myDir.exists()) {
                                                      myDir.mkdirs();
                                                  }
                                                  Toast.makeText(context,
                                                          "Download completed- check folder Procialize/Image",
                                                          Toast.LENGTH_SHORT).show();
                                                  String name = img + ".jpg";
                                                  myDir = new File(myDir, name);

                                                  FileOutputStream out = new FileOutputStream(myDir);
                                                  bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                                  out.flush();
                                                  out.close();
                                              } catch (Exception e) {
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
                    Toast.makeText(context, "No Internet Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(firstLevelFilter.getFileName(), context);
            }
        });
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
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
            bmpUri = FileProvider.getUriForFile(context, "com.procialize.singleevent.android.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}