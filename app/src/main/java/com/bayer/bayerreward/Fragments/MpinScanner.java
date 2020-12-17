package com.bayer.bayerreward.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.HomeActivity;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.GetterSetter.RedeemRequest;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import java.util.HashMap;

import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;


public class MpinScanner extends Fragment implements QRCodeReaderView.OnQRCodeReadListener {
    QRCodeReaderView qrCodeReaderView;
    Dialog myDialog;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey, eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    ProgressBar progressBar;
    LinearLayout linear, linearmain;
    Button btn_start, btn_stop, btn_scananother;
    String mpins = "";
    public static int count = 0;
    TextView txt_cnt, txt_mpin;
    ConnectionDetector cd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mpin_scanner, container, false);
        qrCodeReaderView = (QRCodeReaderView) view.findViewById(R.id.qrdecoderview);
        linearmain = (LinearLayout) view.findViewById(R.id.linearmain);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        btn_start = (Button) view.findViewById(R.id.btn_start);
        btn_stop = (Button) view.findViewById(R.id.btn_stop);
        btn_scananother = (Button) view.findViewById(R.id.btn_scananother);
        txt_mpin = (TextView) view.findViewById(R.id.txt_mpin);
        txt_cnt = (TextView) view.findViewById(R.id.txt_cnt);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        cd = new ConnectionDetector(getActivity());

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mpins = mpins + "$#" + pin;
//                Log.d("QR's", mpins);
//                Log.d("Count", String.valueOf(count));
//                qrCodeReaderView.stopCamera();
                if (cd.isConnectingToInternet()) {

                    MpinSubmit(apikey, eventid, mpins);
                } else {
                    CustomToast("Internet not available");
//                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        btn_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                qrCodeReaderView.setVisibility(View.VISIBLE);
//                linear.setVisibility(View.GONE);
//                btn_start.setVisibility(View.GONE);
//                btn_stop.setVisibility(View.VISIBLE);
//                btn_scananother.setVisibility(View.GONE);
//            }
//        });
//
//        btn_stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                qrCodeReaderView.setVisibility(View.GONE);
//                linear.setVisibility(View.VISIBLE);
//                btn_start.setVisibility(View.VISIBLE);
//                btn_stop.setVisibility(View.GONE);
//                btn_scananother.setVisibility(View.GONE);
//            }
//        });
//
        btn_scananother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                qrCodeReaderView.setVisibility(View.VISIBLE);
//                linear.setVisibility(View.GONE);
//                btn_start.setVisibility(View.GONE);
//                btn_stop.setVisibility(View.VISIBLE);
//                btn_scananother.setVisibility(View.GONE);
//                qrCodeReaderView.startCamera();
//                linearmain.setVisibility(View.GONE);

                ScanQR();
//
//                // Use this function to enable/disable decoding
//                qrCodeReaderView.setQRDecodingEnabled(true);
//
//                // Use this function to change the autofocus interval (default is 5 secs)
//                qrCodeReaderView.setAutofocusInterval(2000L);
//
//                // Use this function to enable/disable Torch
//                qrCodeReaderView.setTorchEnabled(true);
//
//                // Use this function to set back camera preview
//                qrCodeReaderView.setBackCamera();
            }
        });

        ScanQR();
        mAPIService = ApiUtils.getAPIService();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, getActivity().MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(getActivity());

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        CommonFunction.crashlytics("MpinScanner","");
        firbaseAnalytics(getActivity(),"MpinScanner","");
        return view;
    }

    public void ScanQR() {
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }


    @Override
    public void onQRCodeRead(String text, PointF[] points) {
//        qrCodeReaderView.stopCamera();

//        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        String parts[] = text.split("\\r?\\n");
        String pin = parts[0];
        if (count == 0) {
            mpins = pin;
//            showratedialouge(mpins);
            count = count + 1;
            txt_cnt.setText(String.valueOf(count));
            txt_mpin.setText(mpins);
//            btn_scananother.setVisibility(View.VISIBLE);

//            qrCodeReaderView.startCamera();
//            qrCodeReaderView.setOnQRCodeReadListener(this);

            // Use this function to enable/disable decoding
//            qrCodeReaderView.setQRDecodingEnabled(true);
//
//            // Use this function to change the autofocus interval (default is 5 secs)
//            qrCodeReaderView.setAutofocusInterval(2000L);
//
//            // Use this function to enable/disable Torch
//            qrCodeReaderView.setTorchEnabled(true);
//
//            // Use this function to set back camera preview
//            qrCodeReaderView.setBackCamera();
        } else if (count >= 1) {
            if (mpins.contains(pin)) {

//                Toast.makeText(getActivity(), "QR already scanned.", Toast.LENGTH_SHORT).show();
                Log.d("Mpin", mpins);
                Log.d("Pin", pin);

//            qrCodeReaderView.setVisibility(View.GONE);
//            linear.setVisibility(View.VISIBLE);
//            btn_start.setVisibility(View.GONE);
//            btn_stop.setVisibility(View.GONE);
//            btn_scananother.setVisibility(View.VISIBLE);
            } else {
                mpins = mpins + "$#" + pin;
                Log.d("QR's", mpins);
                Log.d("Count", String.valueOf(count));
//            showratedialouge(mpins);
                count = count + 1;
                txt_cnt.setText(String.valueOf(count));
                txt_mpin.setText(pin);
            }
        }

    }

    private void showratedialouge(final String scannedmpin) {
        qrCodeReaderView.stopCamera();
        myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.dialogmpin);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.btn_cancel);
        Button btn_submit = myDialog.findViewById(R.id.btn_submit);
        ImageView img_cancel = myDialog.findViewById(R.id.img_cancel);
        TextView txt_number = myDialog.findViewById(R.id.txt_number);
        progressBar = myDialog.findViewById(R.id.progressBar);

        txt_number.setText(String.valueOf(count + 1));
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MpinSubmit(apikey, eventid, scannedmpin);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                myDialog.cancel();
                qrCodeReaderView.setVisibility(View.GONE);
                linear.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.GONE);
                btn_stop.setVisibility(View.GONE);
                btn_scananother.setVisibility(View.VISIBLE);
//                qrCodeReaderView.startCamera();
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                myDialog.cancel();
                mpins = "";
                count = 0;
                qrCodeReaderView.setVisibility(View.GONE);
                linear.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                btn_stop.setVisibility(View.GONE);
                btn_scananother.setVisibility(View.GONE);

            }
        });
    }


    public void MpinSubmit(String token, String eventid, String scanned_qr_list) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.Mpinsubmit(token, eventid, scanned_qr_list).enqueue(new Callback<RedeemRequest>() {
            @Override
            public void onResponse(Call<RedeemRequest> call, Response<RedeemRequest> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());

//                    myDialog.dismiss();
//                    myDialog.cancel();
//                    qrCodeReaderView.setVisibility(View.GONE);
//                    linear.setVisibility(View.VISIBLE);
//                    btn_start.setVisibility(View.VISIBLE);
//                    btn_stop.setVisibility(View.GONE);
//                    btn_scananother.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
//                    CustomToast(response.body().getMsg());
                    CustomToast("Scanned mPins successfully sent to Server");
                    count = 0;
                    mpins = "";
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    HomeActivity.flag = 1;
                } else {

//                    myDialog.dismiss();
//                    myDialog.cancel();
//                    mpins = "";
//                    qrCodeReaderView.setVisibility(View.GONE);
//                    linear.setVisibility(View.VISIBLE);
//                    btn_start.setVisibility(View.VISIBLE);
//                    btn_stop.setVisibility(View.GONE);
//                    btn_scananother.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    count = 0;
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    HomeActivity.flag = 1;
                }
            }

            @Override
            public void onFailure(Call<RedeemRequest> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
                progressBar.setVisibility(View.GONE);
                count = 0;
//                myDialog.dismiss();
//                myDialog.cancel();
//                qrCodeReaderView.setVisibility(View.GONE);
//                linear.setVisibility(View.VISIBLE);
//                btn_start.setVisibility(View.VISIBLE);
//                btn_stop.setVisibility(View.GONE);
//                btn_scananother.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Unable to submit QR", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                HomeActivity.flag = 1;

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        qrCodeReaderView.stopCamera();
        mpins = "";
    }

    public void CustomToast(String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) getView().findViewById(R.id.toast_layout_root));
        // get the reference of TextView and ImageVIew from inflated layout
        TextView toastTextView = (TextView) layout.findViewById(R.id.toastTextView);
        // set the text in the TextView
        toastTextView.setText(msg);
        // set the Image in the ImageView
        // create a new Toast using context
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG); // set the duration for the Toast
        toast.setView(layout); // set the inflated layout
        toast.show();
    }

}
