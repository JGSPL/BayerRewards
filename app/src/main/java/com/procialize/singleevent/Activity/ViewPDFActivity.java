package com.procialize.singleevent.Activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Utility.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewPDFActivity extends AppCompatActivity {

    String url,name,Page ;
    String header = "";
    WebView webview;
    private ProgressBar progressBar;
    ImageView backIv;
    private APIService mAPIService;
    TextView Savetv;
    ImageView headerlogoIv;
    ProgressDialog pDialog;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

      //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorwhite), PorterDuff.Mode.SRC_ATOP);
        webview = (WebView) findViewById(R.id.webView);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);
//        headerlogoIv = (TextView) findViewById(R.id.headerlogoIv);
//        Savetv = (TextView)findViewById(R.id.Savetv);

        cd = new ConnectionDetector(this);


        url = getIntent().getStringExtra("url");
//        header = getIntent().getStringExtra("header");
//        name = getIntent().getStringExtra("name");
//        Page = getIntent().getStringExtra("Page");

//        headerlogoIv.setText(header);

//        if(Page.equalsIgnoreCase("speaker")){
//            //Savetv.setVisibility(View.INVISIBLE);
//        }


//        Log.e("name",name);
//
//
//        mAPIService = ApiUtils.getAPIService();
//
//        SessionManager sessionManager = new SessionManager(this);
//
//        HashMap<String, String> user = sessionManager.getUserDetails();
//
//        // token
//        final String token = user.get(SessionManager.KEY_TOKEN);
//
//        webview = findViewById(R.id.webView);
//        progressBar = findViewById(R.id.progressBar);
//        backIv = findViewById(R.id.backIv);
//
//        backIv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        Util.logomethod(this, headerlogoIv);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webview.setScrollbarFadingEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        
//        Savetv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (cd.isConnectingToInternet()) {
//                    //sharePDF(url,ViewPDFActivity.this);
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//                        if (ViewPDFActivity.this.checkSelfPermission(
//                                "android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
//                            final String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
//                            ActivityCompat.requestPermissions(ViewPDFActivity.this, permissions, 0);
//                        } else if (ViewPDFActivity.this.checkSelfPermission(
//                                "android.permission.READ_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
//                            final String[] permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
//                            ActivityCompat.requestPermissions(ViewPDFActivity.this, permissions, 0);
//                        } else {
//
//                            new myAsyncTask().execute();
//
//                        }
//
//
//
//                    } else {
//                        new myAsyncTask().execute();
//
//                    }
//
//
//                } else {
//                    Toast.makeText(getBaseContext(), "No Internet Connection",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        

        pDialog = new ProgressDialog(ViewPDFActivity.this);

        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });

        webview.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);

    }




    static public void sharePDF(String url, final Context context) {

        /*Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(sharingIntent, "Share using"));*/

       Uri pictureUri = Uri.parse(url);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
        shareIntent.setType("application/pdf");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share pdf..."));
    }

    class myAsyncTask extends AsyncTask<Void, Void, Void> {
        TextView tv;
        public ProgressDialog dialog;

        myAsyncTask() {
            dialog = new ProgressDialog(ViewPDFActivity.this);
            dialog.setMessage("Downloading file. Please wait...");
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(true);
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dismiss the dialog after the file was downloaded
            dialog.setProgress(0);
            dialog.dismiss();
           /* Toast.makeText(ViewPDFActivity.this, "Download completed- check folder Kotak", Toast.LENGTH_SHORT)
                    .show();*/
            // getActivity().dismissDialog(progress_bar_type);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                // set the download URL, a url that points to a file on the
                // internet
                // this is the file to be downloaded
                URL url1 = new URL(url);

                // create the new connection
                HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                // set up some things on the connection
                // urlConnection.setRequestMethod("GET");
                //urlConnection.setDoOutput(true);

                // and connect!
                urlConnection.connect();

                // set the path where we want to save the file
                // in this case, going to save it on the root directory of the
                // sd card.
                File SDCardRoot = Environment.getExternalStorageDirectory();
                // create a new file, specifying the path, and the filename
                // which we want to save the file as.
                // File file = new File(SDCardRoot, imageURLName);

                String PATH = Environment.getExternalStorageDirectory() + "/kotak/";
                File folder = new File(PATH);
                if (!folder.exists()) {
                    folder.mkdir();// If there is no folder it will be created.
                }

                File file = new File(PATH, name);

                // this will be used to write the downloaded data into the file
                // we created
                FileOutputStream fileOutput = new FileOutputStream(file);

                // this will be used in reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                // this is the total size of the file
                int totalSize = urlConnection.getContentLength();
                // variable to store total downloaded bytes
                int downloadedSize = 0;

                // create a buffer...
                byte[] buffer = new byte[1024];
                int bufferLength = 0; // used to store a temporary size of the
                // buffer

                // now, read through the input buffer and write the contents to
                // the file
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    // add the data in the buffer to the file in the file output
                    // stream (the file on the sd card
                    fileOutput.write(buffer, 0, bufferLength);
                    // add up the size so we know how much is downloaded
                    downloadedSize += bufferLength;

                }
                // close the output stream when done
                fileOutput.close();

                // catch some possible errors...
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/kotak/" + name);  // -> filename = maven.pdf
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_SEND);
            //pdfIntent.setDataAndType(path, "application/pdf");
            //pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            pdfIntent.setAction(Intent.ACTION_SEND);
            pdfIntent.putExtra(Intent.EXTRA_STREAM, path);
            pdfIntent.setType("application/pdf");
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try{
             //   startActivity(pdfIntent);
                startActivity(Intent.createChooser(pdfIntent, "Share pdf..."));

            }catch(ActivityNotFoundException e){
                Toast.makeText(ViewPDFActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
            // see
            // http://androidsnippets.com/download-an-http-file-to-sdcard-with-progress-notification
            return null;
        }

        /*
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

    }

}
