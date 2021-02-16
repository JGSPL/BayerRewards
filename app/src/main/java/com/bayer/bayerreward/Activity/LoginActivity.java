package com.bayer.bayerreward.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.BuildConfig;
import com.bayer.bayerreward.CustomTools.Connectivity;
import com.bayer.bayerreward.GetterSetter.EventListing;
import com.bayer.bayerreward.GetterSetter.Forgot;
import com.bayer.bayerreward.GetterSetter.Login;
import com.bayer.bayerreward.GetterSetter.ResendOTP;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.gcm.GCMHelper;
import com.bayer.bayerreward.utils.CommonFunction;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class LoginActivity extends AppCompatActivity {


    public static final int RequestPermissionCode = 8;
    Boolean emailbool = false, passwordbool = false;
    ProgressBar progressBar2;
    Button loginbtn, createaccbtn;
    String emailid, password;
    ImageView headerlogoIv;
    Dialog myDialog;
    private APIService mAPIService;
    private String passcode;
    String passwordkey, email, passwordemail, access_token;
    private Dialog passcodeDialog;
    String mobile;
    String background, colorActive, logoImg, eventnamestr, eventid;
    String gcmRegID, app_version, os_version, device, platform;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    static final String TAG = "GCMDemo";
    SessionManager session;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

        mAPIService = ApiUtils.getAPIService();

        platform = "android";
        device = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
        app_version = "Version" + BuildConfig.VERSION_NAME;
        session = new SessionManager(getApplicationContext());

        underLineTextview();
        initializeView();

        CommonFunction.crashlytics("Login","");
        firbaseAnalytics(this,"Login","");
    }


    private void underLineTextview() {

        TextView textView = findViewById(R.id.textView);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText("Designed & Developed by Procialize");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.procialize.net";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();
            }
        });
    }

    private void initializeView() {


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        final TextInputEditText Etemail, Etpassword;
        final TextInputLayout inputLayoutemail;
        final TextView text_forgotPswd;

//        headerlogoIv = findViewById(R.id.headerlogoIv);
//        Util.logomethod(this,headerlogoIv);
        Etemail = findViewById(R.id.input_email);
//        Etpassword = findViewById(R.id.input_password);
        progressBar2 = findViewById(R.id.progressBar2);
//        text_forgotPswd = findViewById(R.id.text_forgotPswd);
//        text_forgotPswd.getPaint().setUnderlineText(true);
//        inputLayoutemail = findViewById(R.id.input_layout_email);
//        inputLayoutemail.setErrorEnabled(true);
//        inputLayoutpassword = findViewById(R.id.input_layout_password);
//        inputLayoutpassword.setErrorEnabled(true);

//        text_forgotPswd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forgetPasswordDialog();
//            }
//        });

        loginbtn = findViewById(R.id.loginbtn);


        Etemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                //                    inputLayoutemail.setError(null);
//                    inputLayoutemail.setError("Please Enter Valid Email Id");
                emailbool = s.toString().matches(emailPattern) && s.length() > 0;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Etemail.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter Mobile No.", Toast.LENGTH_SHORT).show();
                } else {
//                    inputLayoutpassword.setError(null);
//                    inputLayoutemail.setError(null);

                    if (Connectivity.isConnected(LoginActivity.this)) {
                        mobile = Etemail.getText().toString();
                        showProgress();
                        sendLogin(mobile);
//                        sendEventList(Etemail.getText().toString(), Etpassword.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, "You are not connected to Internet for processing", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }

        if (checkPlayServices()) {

            // gcmRegID = getRegistrationId(this);

            gcmRegID = session.getGcmID();

            if (gcmRegID.isEmpty()) {
                // storeRegistrationId(this, gcmRegID);
                new getGCMRegId().execute();
            }

        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }


    }

    public void sendEventList(String access_token) {
        mAPIService.GetEventList(access_token).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    if (response.body().getMsg().equalsIgnoreCase("Invalid Token!")) {
                        Intent main = new Intent(LoginActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    } else {
                        showResponse(response);
                    }
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {

            for (int i = 0; i < response.body().getUserEventList().size(); i++) {
                if (response.body().getUserEventList().get(i).getEventId().equalsIgnoreCase("194")) {
                    eventid = response.body().getUserEventList().get(i).getEventId();
                    eventnamestr = response.body().getUserEventList().get(i).getName();
                    logoImg = response.body().getUserEventList().get(i).getHeader_logo();
                    colorActive = response.body().getUserEventList().get(i).getPrimary_color_code();
                    background = response.body().getUserEventList().get(i).getBackground_image();

                    LoginPost(access_token, eventid, gcmRegID, platform, device, os_version, app_version);
                }
            }
        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void LoginPost(String access_token, final String eventid, String registration_id, String platform, String device, String os_version, String app_version) {
        mAPIService.LoginPost(access_token, eventid, registration_id, platform, device, os_version, app_version).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseLoginPost(response, eventid);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseLoginPost(Response<Login> response, String eventid) {

        if (response.body().getStatus().equals("success")) {


            SessionManager sessionManager = new SessionManager(this);

            String firstname = response.body().getUserData().getFirstName();
            String lastname = response.body().getUserData().getLastName();
            String email = response.body().getUserData().getEmail();
            String mobile = response.body().getUserData().getMobile();
            String designation = response.body().getUserData().getDesignation();
            String company = response.body().getUserData().getCompanyName();
            String token = response.body().getUserData().getApiAccessToken();
            String desc = response.body().getUserData().getDescription();
            String city = response.body().getUserData().getCity();
            String country = response.body().getUserData().getCountry();
            String pic = response.body().getUserData().getProfilePic();
            String id = response.body().getUserData().getAttendeeId();
            String attendee_status = response.body().getUserData().getAttendee_status();
            String urid = response.body().getUserData().getUri_id();
            String addr1 = response.body().getUserData().getAddress1();
            String addr2 = response.body().getUserData().getAddress2();

            sessionManager.createLoginSession(firstname, lastname, email, mobile, company, designation, token, desc, city, country, pic, id, emailid, password, "1", attendee_status,urid,addr1,addr2);
            SessionManager.saveSharedPreferencesEventList(response.body().getEventSettingList());
            SessionManager.saveSharedPreferencesMenuEventList(response.body().getEventMenuSettingList());


            SharedPreferences prefs = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE);
            String login = prefs.getString("loginfirst", "");


            if (login.equalsIgnoreCase("1")) {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
                editor.putString("logoImg", logoImg).commit();
                editor.putString("colorActive", colorActive).commit();
                editor.putString("eventback", background).commit();


//              Company company1 = new Company.Builder().withName(company).build();

//                UserAttributes userAttributes = new UserAttributes.Builder()
//                        .withName(firstname + " " + lastname)
//                        .withEmail(email)
//                        .withPhone(mobile)
////                      .withCompany(company1)
//                        .withUserId(id)
//                        .build();
//
//                Registration registration = Registration.create().withUserAttributes(userAttributes);
//                Intercom.client().registerIdentifiedUser(registration);

//                editor.putString("loginfirst","1");
                editor.apply();
                Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                home.putExtra("back", eventid);
//                home.putExtra("eventnamestr", eventnamestr);
                startActivity(home);
                finish();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("eventid", eventid).commit();
                editor.putString("eventnamestr", eventnamestr).commit();
                editor.putString("logoImg", logoImg).commit();
                editor.putString("colorActive", colorActive).commit();

//              editor.putString("loginfirst","1");
                editor.apply();

                Intent home = new Intent(getApplicationContext(), ProfileActivity.class);
                home.putExtra("back", eventid);
//              home.putExtra("eventnamestr", eventnamestr);
                startActivity(home);
                finish();
            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgress() {
        progressBar2.setVisibility(View.VISIBLE);
        loginbtn.setEnabled(false);
    }

    public void dismissProgress() {
        if (progressBar2.getVisibility() == View.VISIBLE) {
            progressBar2.setVisibility(View.GONE);
        }
        loginbtn.setEnabled(true);
    }

    @Override
    protected void onResume() {
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SixthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_CONTACTS,
                        READ_CONTACTS,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        VIBRATE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readcontactpermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writecontactpermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readstoragepermjission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragepermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean vibratepermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && readcontactpermission && writecontactpermission && readstoragepermjission && writestoragepermission && vibratepermission) {

//                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }

    public void forgetPasswordDialog() {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.dialog_signin);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();

        Button submit = myDialog.findViewById(R.id.submit);
        final EditText edit_emailid = myDialog.findViewById(R.id.edit_emailid);
        ImageView imgCancel = myDialog.findViewById(R.id.imgCancel);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_emailid.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                } else {

                    String email = edit_emailid.getText().toString().trim();
                    forgotPassword(email);
                }
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }

    public void forgotPassword(String email) {
        mAPIService.ForgotPassword(email).enqueue(new Callback<Forgot>() {
            @Override
            public void onResponse(Call<Forgot> call, Response<Forgot> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseForgotP(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<Forgot> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseForgotP(Response<Forgot> response) {

        if (response.body().getStatus().equals("success")) {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            myDialog.dismiss();

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void sendLogin(String mobile) {
        mAPIService.LoginMobile(mobile).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponseLogin(response);
                } else {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponseLogin(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {
            passwordkey = response.body().getUserData().getPassword_key();
            email = response.body().getUserData().getEmail();
            passwordemail = response.body().getUserData().getPassword_key();
            access_token = response.body().getUserData().getApiAccessToken();
            passcodeDialog(LoginActivity.this);

            Log.e("Password Key ==",passwordkey);

//            SessionManager sessionManager = new SessionManager(this);
//
//            String firstname = response.body().getUserData().getFirstName();
//            String lastname = response.body().getUserData().getLastName();
//            String email = response.body().getUserData().getEmail();
//            String mobile = response.body().getUserData().getMobile();
//            String designation = response.body().getUserData().getDesignation();
//            String company = response.body().getUserData().getCompanyName();
//            String token = response.body().getUserData().getApiAccessToken();
//            apitoken = response.body().getUserData().getApiAccessToken();
//            String desc = response.body().getUserData().getDescription();
//            String city = response.body().getUserData().getCity();
//            String country = response.body().getUserData().getCountry();
//            String pic = response.body().getUserData().getProfilePic();
//            String id = response.body().getUserData().getAttendeeId();
//            String attendee_status = response.body().getUserData().getAttendee_status();
//
//
//            showProgress();
////            sendEventList(email);
//
////            sessionManager.createLoginSession(firstname, lastname, email, mobile, company, designation, token, desc, city, country, pic, id, email, "", "1", attendee_status);
////            SessionManager.saveSharedPreferencesEventList(response.body().getEventSettingList());
////            SessionManager.saveSharedPreferencesMenuEventList(response.body().getEventMenuSettingList());
//
////            if (response.body().getUserData().getReset_password_flag().equals("0")) {
//////                resetpassworddialouge(this);
////
////            } else {
//
//                SharedPreferences prefs = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE);
//                String login = prefs.getString("loginfirst", "");
//
//                if (colorActive == null || colorActive.isEmpty()) {
//                    colorActive = "#008dd2";
//                }
//
//                if (login.equalsIgnoreCase("1")) {
//                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putString("eventid", eventid).commit();
//                    editor.putString("eventnamestr", eventnamestr).commit();
//                    editor.putString("logoImg", logoImg).commit();
//                    editor.putString("colorActive", colorActive).commit();
//                    editor.apply();
//
//
//                    Intent home = new Intent(getApplicationContext(), EventChooserActivity.class);
//                    startActivity(home);
//                    finish();
//                } else {
//                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//                    editor.putString("eventid", eventid).commit();
//                    editor.putString("eventnamestr", eventnamestr).commit();
//                    editor.putString("logoImg", logoImg).commit();
//                    editor.putString("colorActive", colorActive).commit();
//                    editor.apply();
//
//                    Intent home = new Intent(getApplicationContext(), EventChooserActivity.class);
//                    startActivity(home);
//                    finish();
//                }
//            }

        } else {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }


    public void passcodeDialog(final Context activity) {


        passcodeDialog = new Dialog(activity);
        passcodeDialog.setContentView(R.layout.passcode_dialog);

        // Font Initialization
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "DINPro-Light_13935.ttf");

        // Set Font
        SpannableString title = new SpannableString("Enter Passcode");
        title.setSpan(typeFace, 0, 12, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        passcodeDialog.setTitle(title);
        passcodeDialog.setCancelable(false);

        final EditText submitPasscode = (EditText) passcodeDialog
                .findViewById(R.id.message_edt_send__dialog);
        submitPasscode.setTypeface(typeFace);

        Button submit = (Button) passcodeDialog
                .findViewById(R.id.btn_send_dialog);
        Button btn_resend = (Button) passcodeDialog
                .findViewById(R.id.btn_resend);
        progressBar = (ProgressBar) passcodeDialog
                .findViewById(R.id.progressBar);

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendOTP(mobile);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // if (cd.isConnectingToInternet()) {
                //
                // } else {
                // Toast.makeText(getBaseContext(), "No Internet Connection",
                // Toast.LENGTH_SHORT).show();
                // }

                if (submitPasscode.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter passcode",
                            Toast.LENGTH_SHORT).show();
                } else {
                    passcode = submitPasscode.getText().toString();

                    if (passwordkey.equalsIgnoreCase(passcode)) {

                        if (passcodeDialog.isShowing())
                            passcodeDialog.dismiss();

                        sendEventList(access_token);

//                        Intent intent = new Intent(LoginActivity.this, EventChooserActivity.class);
//                        intent.putExtra("email", email);
//                        intent.putExtra("password", passwordemail);
//                        intent.putExtra("access_token", access_token);
//                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Passcode",
                                Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });

        ImageView close = (ImageView) passcodeDialog
                .findViewById(R.id.cross);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                passcodeDialog.dismiss();

            }
        });

        passcodeDialog.show();

    }

    public void ResendOTP(String mobile) {
        progressBar.setVisibility(View.VISIBLE);
        mAPIService.ResendOtp(mobile).enqueue(new Callback<ResendOTP>() {
            @Override
            public void onResponse(Call<ResendOTP> call, Response<ResendOTP> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                    showResponseResendOTP(response);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),  response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<ResendOTP> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    public void showResponseResendOTP(Response<ResendOTP> response) {
//
//        if (response.body().getStatus().equals("success")) {
//
//            passwordkey = response.body().getUserData().getPassword_key();
//        } else {
//            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
//        }
//    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private class getGCMRegId extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            GCMHelper gcmRegistrationHelper = new GCMHelper(
                    getApplicationContext());
            try {
                //gcmRegID = gcmRegistrationHelper.GCMRegister(REG_ID);
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.d("MYTAG", "This is your Firebase token" + token);

                gcmRegID = token;

                session.storeGcmID(gcmRegID);

                // storeRegistrationId(getApplicationContext(), gcmRegID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // session = new SessionManagement(getApplicationContext());
            // if (session.isLoggedIn()) {
            // Update GCM ID to Server
            // new updateGCMRegId().execute();
            // }

        }
    }

}