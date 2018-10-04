package com.procialize.singleevent.InnerDrawerActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;


import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;

public class QRGeneratorActivity extends AppCompatActivity {

    TextView nameTv, companyTv, cityTv, emailTv, mobileTv,
            scan_id_txtTv, designationTv;
    SessionManager sessionManager;
    String name, designation, company, email, mobile, city, lname;
    ImageView img_qr_code_image;
    String QRcode;
    public final static int WIDTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgeneration);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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

        sessionManager = new SessionManager(this);

        // get user data from session
        HashMap<String, String> userData = sessionManager.getUserDetails();

        name = userData.get(SessionManager.KEY_NAME);
        lname = userData.get(SessionManager.KEY_LNAME);
        designation = userData.get(SessionManager.KEY_DESIGNATION);
        company = userData.get(SessionManager.KEY_COMPANY);
        email = userData.get(SessionManager.KEY_EMAIL);
        mobile = userData.get(SessionManager.KEY_MOBILE);
        city = userData.get(SessionManager.KEY_CITY);

        scan_id_txtTv = findViewById(R.id.scan_id_txt);


        nameTv = findViewById(R.id.name);
        nameTv.setText("Name :  " + name + " " + lname);

//      designationTv = findViewById(R.id.designation);
//      designationTv.setText("Designation: " + designation);

        companyTv = findViewById(R.id.company);
        companyTv.setText("Company : " + company);

        cityTv = findViewById(R.id.city);
        cityTv.setText("City : " + city);

        emailTv = findViewById(R.id.email);
        emailTv.setText("Email : " + email);

        mobileTv = findViewById(R.id.mobile);
        mobileTv.setText("Mobile : " + mobile);

        designationTv = findViewById(R.id.designation);
        designationTv.setText("Designation : " + designation);


        img_qr_code_image = findViewById(R.id.img_qr_code_image);


        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {

                QRcode = name + " " + "\n"
                        + lname + "\n"
                        + designation + "\n"
                        + email + "\n"
                        + mobile + "\n"
                        + company + "\n"
                        + city;
//
//                QRcode = name + "\n" +
//                        designation + "\n"
//                        + email + "\n"
//                        + mobile + "\n"
//                        + company + "\n"
//                        + city;

                try {
                    synchronized (this) {
                        wait(100);
                        // runOnUiThread method used to do UI task in main
                        // thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    img_qr_code_image.setImageBitmap(bitmap);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        t.start();


    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    @SuppressWarnings("deprecation")
    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE,
                    WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK
                        : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
        return bitmap;
    } // / end of this method


    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
