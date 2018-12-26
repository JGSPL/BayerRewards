package com.procialize.singleevent.InnerDrawerActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.procialize.singleevent.Utility.Util;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;

public class QRGeneratorActivity extends AppCompatActivity {

    TextView nameTv, companyTv, cityTv, emailTv, mobileTv,
            scan_id_txtTv, designationTv, header, scantxt;
    SessionManager sessionManager;
    String name, designation, company, email, mobile, city, lname;
    ImageView img_qr_code_image;
    String QRcode;
    public final static int WIDTH = 500;
    ImageView headerlogoIv;
    String MY_PREFS_NAME = "ProcializeInfo";
    String colorActive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgeneration);
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

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this, headerlogoIv);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");


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

        final String fullName = name + " " + lname;


//        scan_id_txtTv = findViewById(R.id.scan_id_txt);
        scantxt = findViewById(R.id.scantxt);

        header = findViewById(R.id.header);
        header.setTextColor(Color.parseColor(colorActive));
        nameTv = findViewById(R.id.name);
        nameTv.setText(name + " " + lname);
        nameTv.setTextColor(Color.parseColor(colorActive));

        TextView txtNamehead = findViewById(R.id.txtNamehead);
//        txtNamehead.setTextColor(Color.parseColor(colorActive));
        scantxt.setTextColor(Color.parseColor(colorActive));

//      designationTv = findViewById(R.id.designation);
//      designationTv.setText("Designation: " + designation);

        companyTv = findViewById(R.id.company);
        companyTv.setText(company);

        cityTv = findViewById(R.id.city);
        cityTv.setText(city);

        emailTv = findViewById(R.id.email);
        emailTv.setText(email);

        mobileTv = findViewById(R.id.mobile);
        mobileTv.setText(mobile);

        designationTv = findViewById(R.id.designation);
        designationTv.setText(designation);


        img_qr_code_image = findViewById(R.id.img_qr_code_image);


        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {

              /*  QRcode = name + " " + "\n"
                        + lname + "\n"
                        + designation + "\n"
                        + email + "\n"
                        + mobile + "\n"
                        + company + "\n"
                        + city;*/

                QRcode = name + "\n" + lname + "\n"
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
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
