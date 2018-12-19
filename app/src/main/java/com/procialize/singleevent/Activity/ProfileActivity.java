package com.procialize.singleevent.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.Analytic;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.GetterSetter.ProfileSave;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;
import com.procialize.singleevent.Utility.Utility;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProfileActivity extends AppCompatActivity {

    Button savebtn;
    private APIService mAPIService;
    SessionManager sessionManager;
    EditText Etfirstname, Etlastname, Etdesignation, Etcompany, Etdescription, Etcity, Etcountry, Etmobile, Etemail;
    ProgressBar progressView;
    ImageView profileIV;
    File file;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String edit_profile, edit_profile_pic, edit_profile_naame, edit_profile_designation, edit_profile_company,
            edit_profile_location, edit_profile_mobile, edit_profile_email;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    public static final int RequestPermissionCode = 8;
    String eventnamestr;
    String profilepic;
    String api_token, colorActive;
    TextView txt_upload;
    ImageView headerlogoIv;
    RelativeLayout linear_upload;
    String MY_PREFS_LOGIN = "ProcializeLogin";
    public static String logoImg = "";
    TextInputLayout input_layout_firstname, input_layout_lastname, input_layout_designation, input_layout_company,
            input_layout_mobile, input_layout_desc, input_layout_city, input_layout_emailid, input_layout_country;

    RelativeLayout relative;
    ProgressDialog progressDialog;

    String mCurrentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "");
        eventnamestr = prefs.getString("eventnamestr", "");
        logoImg = prefs.getString("logoImg", "");
        colorActive = prefs.getString("colorActive", "");

        SharedPreferences.Editor editorlogin = getSharedPreferences(MY_PREFS_LOGIN, MODE_PRIVATE).edit();
        editorlogin.putString("loginfirst", "1").commit();

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        api_token = user.get(SessionManager.KEY_TOKEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        Intent intent=getIntent();
//        eventid=intent.getStringExtra("eventId");
//        eventnamestr=intent.getStringExtra("eventnamestr");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod1(this, headerlogoIv);

        mAPIService = ApiUtils.getAPIService();

        fetchProfileDetail(api_token, eventid);

        SubmitAnalytics(api_token, eventid, "", "", "EditProfile");

        if (CheckingPermissionIsEnabledOrNot()) {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }
    }

    private void initializeView() {


//        sessionManager = new SessionManager(this);


        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetails();

        // name
        String name = user.get(SessionManager.KEY_NAME);

        String lname = user.get(SessionManager.KEY_LNAME);

        // designation
        String designation = user.get(SessionManager.KEY_DESIGNATION);

        // company
        String company = user.get(SessionManager.KEY_COMPANY);

        //description
        String description = user.get(SessionManager.KEY_DESCRIPTION);

        //mobile
        String mobile = user.get(SessionManager.KEY_MOBILE);

        //city
        String city = user.get(SessionManager.KEY_CITY);

        //country
        String country = user.get(SessionManager.KEY_COUNTRY);

        //profilepic
        profilepic = user.get(SessionManager.KEY_PIC);

        //email
        String email = user.get(SessionManager.KEY_EMAIL);


        //Log.d("Profile Pic", profilepic);
        eventSettingLists = SessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }


        input_layout_firstname = findViewById(R.id.input_layout_firstname);
        input_layout_lastname = findViewById(R.id.input_layout_lastname);
        input_layout_designation = findViewById(R.id.input_layout_designation);
        input_layout_company = findViewById(R.id.input_layout_company);
        input_layout_mobile = findViewById(R.id.input_layout_mobile);
        input_layout_desc = findViewById(R.id.input_layout_desc);
        input_layout_city = findViewById(R.id.input_layout_city);
        input_layout_emailid = findViewById(R.id.input_layout_emailid);
        input_layout_country = findViewById(R.id.input_layout_country);
        relative = findViewById(R.id.relative);


        Etfirstname = findViewById(R.id.Etfirstname);
        Etlastname = findViewById(R.id.Etlastname);
        Etdesignation = findViewById(R.id.Etdesignation);
        Etcompany = findViewById(R.id.Etcompany);
        Etdescription = findViewById(R.id.Etdescription);
        Etcity = findViewById(R.id.Etcity);
        Etcountry = findViewById(R.id.Etcountry);
        Etmobile = findViewById(R.id.Etmobile);
        Etemail = findViewById(R.id.Etemail);

        savebtn = findViewById(R.id.savebtn);
        txt_upload = findViewById(R.id.txt_upload);
        linear_upload = findViewById(R.id.linear_upload);

        TextView header = findViewById(R.id.title);
        header.setTextColor(Color.parseColor(colorActive));

        RelativeLayout layoutTop = findViewById(R.id.layoutTop);
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));

        savebtn.setBackgroundColor(Color.parseColor(colorActive));
        txt_upload.setTextColor(Color.parseColor(colorActive));


        profileIV = findViewById(R.id.profileIV);
        progressView = findViewById(R.id.progressView);


        if (designation != null && edit_profile_designation.equalsIgnoreCase("1")) {
            Etdesignation.setText(designation);
            Etdesignation.setVisibility(View.VISIBLE);
            input_layout_designation.setVisibility(View.VISIBLE);
        } else {
            if (designation != null) {
                Etdesignation.setText(designation);
                Etdesignation.setVisibility(View.VISIBLE);
                input_layout_designation.setVisibility(View.VISIBLE);

            }
            Etdesignation.setVisibility(View.GONE);
            input_layout_designation.setVisibility(View.GONE);
        }

        if (description != null) {
            Etdescription.setText(description);
            Etdescription.setVisibility(View.GONE);
            input_layout_desc.setVisibility(View.GONE);
        } else {
            if (description != null) {
                Etdescription.setText(description);
                Etdescription.setVisibility(View.GONE);
                input_layout_desc.setVisibility(View.GONE);
            }
            Etdescription.setVisibility(View.GONE);
            input_layout_desc.setVisibility(View.GONE);
        }

        if (city != null && edit_profile_location.equalsIgnoreCase("1")) {
            Etcity.setText(city);
            Etcity.setVisibility(View.VISIBLE);
            input_layout_city.setVisibility(View.VISIBLE);
        } else {
            if (city != null) {
                Etcity.setText(city);
                Etcity.setVisibility(View.VISIBLE);
                input_layout_city.setVisibility(View.VISIBLE);
            }
            Etcity.setVisibility(View.GONE);
            input_layout_city.setVisibility(View.GONE);
        }

        if (country != null && edit_profile_location.equalsIgnoreCase("1")) {
            Etcountry.setText(country);
            Etcountry.setVisibility(View.VISIBLE);
            input_layout_country.setVisibility(View.VISIBLE);
        } else {
            if (country != null) {
                Etcountry.setText(country);
                Etcountry.setVisibility(View.VISIBLE);
                input_layout_country.setVisibility(View.VISIBLE);
            }
            Etcountry.setVisibility(View.GONE);
            input_layout_country.setVisibility(View.GONE);

        }

        if (mobile != null && edit_profile_mobile.equalsIgnoreCase("1")) {
            Etmobile.setText(mobile);
            Etmobile.setVisibility(View.VISIBLE);
            input_layout_mobile.setVisibility(View.VISIBLE);
        } else {
            if (mobile != null) {
                Etmobile.setText(mobile);
                Etmobile.setVisibility(View.VISIBLE);
                input_layout_mobile.setVisibility(View.VISIBLE);
            }
            Etmobile.setVisibility(View.GONE);
            input_layout_mobile.setVisibility(View.GONE);

        }

        if (company != null && edit_profile_company.equalsIgnoreCase("1")) {
            Etcompany.setText(company);
            Etcompany.setVisibility(View.VISIBLE);
            input_layout_company.setVisibility(View.VISIBLE);

        } else {

            if (company != null) {
                Etcompany.setText(company);
                Etcompany.setVisibility(View.VISIBLE);
                input_layout_company.setVisibility(View.VISIBLE);

            }

            Etcompany.setVisibility(View.GONE);
            input_layout_company.setVisibility(View.GONE);
        }

        try {
            if (name != null && edit_profile_naame.equalsIgnoreCase("1")) {

                Etfirstname.setText(name);
                Etlastname.setText(lname);
                Etfirstname.setVisibility(View.VISIBLE);
                Etlastname.setVisibility(View.VISIBLE);
                input_layout_firstname.setVisibility(View.VISIBLE);
                input_layout_lastname.setVisibility(View.VISIBLE);

            } else {
                if (name != null) {
                    Etfirstname.setText(name);
                    Etlastname.setText(lname);
                    Etfirstname.setVisibility(View.VISIBLE);
                    Etlastname.setVisibility(View.VISIBLE);
                    input_layout_firstname.setVisibility(View.VISIBLE);
                    input_layout_lastname.setVisibility(View.VISIBLE);
                }
                Etfirstname.setVisibility(View.GONE);
                Etlastname.setVisibility(View.GONE);
                input_layout_firstname.setVisibility(View.GONE);
                input_layout_lastname.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (email != null && edit_profile_email.equalsIgnoreCase("1")) {
            Etemail.setText(email);
            Etemail.setVisibility(View.VISIBLE);
            input_layout_emailid.setVisibility(View.VISIBLE);
        } else {
            if (email != null) {
                Etemail.setText(email);
                Etemail.setVisibility(View.VISIBLE);
                input_layout_emailid.setVisibility(View.VISIBLE);

            }
            Etemail.setVisibility(View.GONE);
            input_layout_emailid.setVisibility(View.GONE);
        }


        if (edit_profile_pic.equalsIgnoreCase("1")) {
            profileIV.setVisibility(View.VISIBLE);
            txt_upload.setVisibility(View.VISIBLE);
            relative.setVisibility(View.VISIBLE);
            if (profilepic != null) {
                Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            } else {
                profileIV.setImageResource(R.drawable.profilepic_placeholder);

            }
        } else {
            if (profilepic != null) {
                Glide.with(this).load(ApiConstant.profilepic + profilepic).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        profileIV.setImageResource(R.drawable.profilepic_placeholder);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(profileIV);
            }
            relative.setVisibility(View.GONE);
            profileIV.setVisibility(View.GONE);
            txt_upload.setVisibility(View.GONE);
            progressView.setVisibility(View.GONE);
        }

        linear_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        txt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Etfirstname = findViewById(R.id.Etfirstname);
                Etlastname = findViewById(R.id.Etlastname);
                Etdesignation = findViewById(R.id.Etdesignation);
                Etcompany = findViewById(R.id.Etcompany);
                Etdescription = findViewById(R.id.Etdescription);
                Etcity = findViewById(R.id.Etcity);
                Etcountry = findViewById(R.id.Etcountry);
                Etmobile = findViewById(R.id.Etmobile);
                Etemail = findViewById(R.id.Etemail);
                if (Etemail.getVisibility() == View.VISIBLE && Etemail.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                } else if (Etfirstname.getVisibility() == View.VISIBLE && Etfirstname.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (Etlastname.getVisibility() == View.VISIBLE && Etlastname.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (Etdesignation.getVisibility() == View.VISIBLE && Etdesignation.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Designation", Toast.LENGTH_SHORT).show();
                } else if (Etcompany.getVisibility() == View.VISIBLE && Etcompany.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Company Name", Toast.LENGTH_SHORT).show();
                } else if (Etmobile.getVisibility() == View.VISIBLE && Etmobile.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
                } else if (Etcity.getVisibility() == View.VISIBLE && Etcity.getText().toString().isEmpty()) {
                    Toast.makeText(ProfileActivity.this, "Enter City Name", Toast.LENGTH_SHORT).show();
                } else {
                    saveProfile();
                }

            }
        });

    }

    public void fetchProfileDetail(String token, String eventid) {

        RequestBody token1 = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody eventid1 = RequestBody.create(MediaType.parse("text/plain"), eventid);
//        showProgress();
        mAPIService.fetchProfileDetail(token1, eventid1).enqueue(new Callback<ProfileSave>() {
            @Override
            public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    if(response.body().getMsg().equalsIgnoreCase("Invalid Token!")){
                        sessionManager.logoutUser();
                        Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(main);
                        finish();

                    }else {

                        showResponse(response);
                    }
//                    dismissProgress();
//                    showResponse(response);
                } else {


//                    dismissProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileSave> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();

            }
        });
    }

    public void showResponse(Response<ProfileSave> response) {

        // specify an adapter (see also next example)
        if (response.body().getStatus().equalsIgnoreCase("success")) {
            if (!(response.body().getUserData().equals(null))) {

                try {
                    String name = response.body().getUserData().getFirstName();
                    String company = response.body().getUserData().getCompanyName();
                    String designation = response.body().getUserData().getDesignation();
                    String pic = response.body().getUserData().getProfilePic();
                    String lastname = response.body().getUserData().getLastName();
                    String city = response.body().getUserData().getCity();
                    String mobno = response.body().getUserData().getMobile();
                    String email = response.body().getUserData().getEmail();
                    String country = response.body().getUserData().getCountry();
                    String description = response.body().getUserData().getDescription();

                    SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                    pref.clear();

                    sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno);

                    initializeView();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {

            }
            if(response.body().getMsg().equalsIgnoreCase("Invalid Token!")){
                sessionManager.logoutUser();
                Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(main);
                finish();

            }
        } else {
            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

        }
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {
        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile")) {
                edit_profile = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_pic")) {
                edit_profile_pic = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_naame")) {
                edit_profile_naame = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_name")) {
                edit_profile_naame = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_designation")) {
                edit_profile_designation = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_company")) {
                edit_profile_company = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_location")) {
                edit_profile_location = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_mobile")) {
                edit_profile_mobile = eventSettingLists.get(i).getFieldValue();
            }

            if (eventSettingLists.get(i).getFieldName().equals("edit_profile_email")) {
                edit_profile_email = eventSettingLists.get(i).getFieldValue();
            }

        }
    }


    public void saveProfile() {

        Showprogress(this, "Loading.......");

        HashMap<String, String> user = sessionManager.getUserDetails();

        final String api_token = user.get(SessionManager.KEY_TOKEN);
        String firstname = Etfirstname.getText().toString();
        String lastname = Etlastname.getText().toString();
        String description = Etdescription.getText().toString();
        String designation = Etdesignation.getText().toString();
        String city = Etcity.getText().toString();
        String country = Etcountry.getText().toString();
        String mobile = Etmobile.getText().toString();
        String companyname = Etcompany.getText().toString();

        MultipartBody.Part body = null;
        if (file != null) {

            if (!file.equals("")) {
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
                SharedPreferences.Editor editor = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                editor.putString("profile", String.valueOf(file)).commit();
//          editor.putString("loginfirst","1");
                editor.apply();
            }
        } else {
            SharedPreferences pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE);
            String prof_pic = pref.getString("profile", "");
            // String prof_pic = user.get(SessionManager.KEY_PIC);

            if (prof_pic != null) {
                if (!prof_pic.equals("")) {
                    file = new File(prof_pic);
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                    body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
                }
            } else {
//                file = new File(profilepic);
//                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profilepic);
//                body = MultipartBody.Part.createFormData("profile_pic", profilepic, reqFile);
            }
//            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profilepic);
//            body = MultipartBody.Part.createFormData("profile_pic", profilepic, reqFile);
        }

        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), api_token);
        RequestBody fstname = RequestBody.create(MediaType.parse("text/plain"), firstname);
        RequestBody lstname = RequestBody.create(MediaType.parse("text/plain"), lastname);
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody cit = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody countr = RequestBody.create(MediaType.parse("text/plain"), country);
        RequestBody mob = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody cmp = RequestBody.create(MediaType.parse("text/plain"), companyname);
        RequestBody des = RequestBody.create(MediaType.parse("text/plain"), designation);
        RequestBody typ = RequestBody.create(MediaType.parse("text/plain"), "A");
        RequestBody evid = RequestBody.create(MediaType.parse("text/plain"), eventid);


        if (body == null) {

            mAPIService.ProfileSave(token, fstname, lstname, desc, cit, countr,
                    mob, typ, des, evid, cmp).enqueue(new Callback<ProfileSave>() {
                @Override
                public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {
                    try {

                        Dismissprogress();
                        if (response.body().getStatus().equals("success")) {
                            Log.i("hit", "post submitted to API." + response.body().toString());

                            String name = response.body().getUserData().getFirstName();
                            String company = response.body().getUserData().getCompanyName();
                            String designation = response.body().getUserData().getDesignation();
                            String pic = response.body().getUserData().getProfilePic();
                            String lastname = response.body().getUserData().getLastName();
                            String city = response.body().getUserData().getCity();
                            String mobno = response.body().getUserData().getMobile();
                            String email = response.body().getUserData().getEmail();
                            String country = response.body().getUserData().getCountry();
                            String description = response.body().getUserData().getDescription();

                            SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                            pref.clear();

                            sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno);
//                      initializeView();

                            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                            home.putExtra("eventId", eventid);
                            home.putExtra("eventnamestr", eventnamestr);
                            startActivity(home);
                            finish();

                            SubmitAnalytics(api_token, eventid, "", "", "EditProfileSubmit");
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
//                        home.putExtra("eventId", eventid);
//                        home.putExtra("eventnamestr", eventnamestr);
//                        startActivity(home);
//                        finish();
                        }
                    } catch (Exception e) {
                        Dismissprogress();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProfileSave> call, Throwable t) {
                    Dismissprogress();
                    Log.e("hit", "Low network or no network");
                    // Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAPIService.ProfileSave(token, fstname, lstname, desc, cit, countr,
                    mob, typ, des, evid, cmp, body).enqueue(new Callback<ProfileSave>() {
                @Override
                public void onResponse(Call<ProfileSave> call, Response<ProfileSave> response) {
                    try {
                        Dismissprogress();
                        if(response.body().getMsg().equalsIgnoreCase("Invalid Token!")){
                            sessionManager.logoutUser();
                            Intent main = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(main);
                            finish();

                        }
                        if (response.body().getStatus().equals("success")) {
                            Log.i("hit", "post submitted to API." + response.body().toString());

                            String name = response.body().getUserData().getFirstName();
                            String company = response.body().getUserData().getCompanyName();
                            String designation = response.body().getUserData().getDesignation();
                            String pic = response.body().getUserData().getProfilePic();
                            String lastname = response.body().getUserData().getLastName();
                            String city = response.body().getUserData().getCity();
                            String mobno = response.body().getUserData().getMobile();
                            String email = response.body().getUserData().getEmail();
                            String country = response.body().getUserData().getCountry();
                            String description = response.body().getUserData().getDescription();

                            SharedPreferences.Editor pref = getSharedPreferences("PROFILE_PICTURE", MODE_PRIVATE).edit();
                            pref.clear();

                            sessionManager.createProfileSession(name, company, designation, pic, lastname, city, description, country, email, mobno);
//                      initializeView();

                            Intent home = new Intent(getApplicationContext(), HomeActivity.class);
                            home.putExtra("eventId", eventid);
                            home.putExtra("eventnamestr", eventnamestr);
                            startActivity(home);
                            finish();

                            SubmitAnalytics(api_token, eventid, "", "", "EditProfileSubmit");
                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
//                        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
//                        home.putExtra("eventId", eventid);
//                        home.putExtra("eventnamestr", eventnamestr);
//                        startActivity(home);
//                        finish();
                        }
                    } catch (Exception e) {
                        Dismissprogress();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProfileSave> call, Throwable t) {
                    Dismissprogress();
                    Log.e("hit", "Low network or no network");
                    //Toast.makeText(getApplicationContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, REQUEST_CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.procialize.singleevent.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            } else if (requestCode == UCrop.REQUEST_CROP) {
                final Uri resultUri = UCrop.getOutput(data);
                onSelectFromCropResult(resultUri);
            } else if (resultCode == UCrop.RESULT_ERROR) {
                final Throwable cropError = UCrop.getError(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            Uri tempUri;
            if (data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                tempUri = getImageUri(getApplicationContext(), thumbnail);

                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2)
                        .withMaxResultSize(200, 200)
                        .start(this);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(tempUri));
            } else {
                file = new File(mCurrentPhotoPath);

                tempUri = Uri.fromFile(file);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2)
                        .withMaxResultSize(200, 200)
                        .start(this);
            }

//        profileIV.setImageURI(tempUri);

            Glide.with(this).load(tempUri).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressView.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), bm);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                file = new File(getRealPathFromURI(tempUri));

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                UCrop.of(tempUri, Uri.parse(destination.getAbsolutePath()))
                        .withAspectRatio(2, 2)
                        .withMaxResultSize(200, 200)
                        .start(this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Glide.with(this).load(bm).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                profileIV.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }
        }).into(profileIV);
//        profileIV.setImageBitmap(bm);
    }

    private void onSelectFromCropResult(Uri tempUri) {

        if (tempUri != null) {

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            file = new File(tempUri.getPath());

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Glide.with(this).load(tempUri).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                profileIV.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressView.setVisibility(View.GONE);
                return false;
            }
        }).into(profileIV);
//        profileIV.setImageBitmap(bm);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(home);
        finish();
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
        ActivityCompat.requestPermissions(ProfileActivity.this, new String[]
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
                        Toast.makeText(ProfileActivity.this, "We need your permission so you can enjoy full features of app", Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }

    public void SubmitAnalytics(String token, String eventid, String target_attendee_id, String target_attendee_type, String analytic_type) {

        mAPIService.Analytic(token, eventid, target_attendee_id, target_attendee_type, analytic_type).enqueue(new Callback<Analytic>() {
            @Override
            public void onResponse(Call<Analytic> call, Response<Analytic> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "Analytics Sumbitted" + response.body().toString());


                } else {

                    // Toast.makeText(ProfileActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Analytic> call, Throwable t) {
                // Toast.makeText(ProfileActivity.this, "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void Showprogress(Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }


    private void Dismissprogress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
