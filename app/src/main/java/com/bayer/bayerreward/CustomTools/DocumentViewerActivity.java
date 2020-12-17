package com.bayer.bayerreward.CustomTools;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.bayer.bayerreward.R;

public class DocumentViewerActivity extends AppCompatActivity {

    String url;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_viewer);

        url = getIntent().getStringExtra("url");


        pdfView = findViewById(R.id.pdfView);
        pdfView.fromUri(Uri.parse(url));

    }
}
