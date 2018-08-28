package com.procialize.singleevent.Activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.procialize.singleevent.R;

public class PdfViewerActivity extends AppCompatActivity {

    String url = "";
    WebView webview;
    private ProgressBar progressBar;
    ImageView backIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        url = getIntent().getStringExtra("url");


        webview = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        backIv = findViewById(R.id.backIv);

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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


        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (!PdfViewerActivity.this.isFinishing()){
                    if (progressBar.getVisibility() == View.GONE) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if (progress == 100) {
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        webview.loadUrl(url);
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }
}
