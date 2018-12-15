package com.procialize.singleevent.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.procialize.singleevent.DbHelper.ConnectionDetector;
import com.procialize.singleevent.DbHelper.DBHelper;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.EventSettingList;

import com.procialize.singleevent.GetterSetter.SendMessagePost;
import com.procialize.singleevent.GetterSetter.UserData;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;
import com.procialize.singleevent.Utility.Util;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendeeDetailActivity extends AppCompatActivity {


    String attendeeid, city, country, company, designation, description, totalrating, name, profile, mobile;
    TextView tvname, tvcompany, tvdesignation, tvcity, tvmob, attendeetitle;
    TextView sendbtn;
    Dialog myDialog;
    APIService mAPIService;
    SessionManager sessionManager;
    String apikey;
    ImageView profileIV;
    ProgressBar progressBar, progressBarmain;
    String attendee_company, attendee_location, attendee_mobile, attendee_design;
    List<EventSettingList> eventSettingLists;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid, colorActive,eventnamestr;
    UserData userData;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ConnectionDetector cd;
    private List<AttendeeList> attendeeList;
    private List<AttendeeList> attendeesDBList;
    private DBHelper dbHelper;
    String getattendee;
    EditText posttextEt;
    View viewtwo, viewthree, viewone, viewtfour;
    ProgressDialog progressDialog;
    LinearLayout linearsaveandsend;
    ImageView headerlogoIv;
    TextView saveContact;
    private RelativeLayout layoutTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_detail);
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
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");

        eventnamestr = prefs.getString("eventnamestr", "");


        dbHelper = new DBHelper(AttendeeDetailActivity.this);
        db = dbHelper.getWritableDatabase();

        db = dbHelper.getReadableDatabase();

        userData = dbHelper.getUserDetails();


        // token


        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(AttendeeDetailActivity.this);

        HashMap<String, String> user = sessionManager.getUserDetails();

        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        getattendee = user.get(SessionManager.KEY_ID);



        eventSettingLists = sessionManager.loadEventList();

        if (eventSettingLists.size() != 0) {
            applysetting(eventSettingLists);
        }

        try {
            attendeeid = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            city = getIntent().getExtras().getString("city");
            country = getIntent().getExtras().getString("country");
            company = getIntent().getExtras().getString("company");
            designation = getIntent().getExtras().getString("designation");
            description = getIntent().getExtras().getString("description");
            totalrating = getIntent().getExtras().getString("totalrating");
            profile = getIntent().getExtras().getString("profile");
            mobile = getIntent().getExtras().getString("mobile");
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvname = findViewById(R.id.tvname);
        attendeetitle = findViewById(R.id.attendeetitle);
        tvcompany = findViewById(R.id.tvcompany);
        tvdesignation = findViewById(R.id.tvdesignation);
        tvcity = findViewById(R.id.tvcity);
        profileIV = findViewById(R.id.profileIV);
        progressBar = findViewById(R.id.progressBar);
        progressBarmain = findViewById(R.id.progressBarmain);
        posttextEt = findViewById(R.id.posttextEt);
        viewtwo = findViewById(R.id.viewtwo);
        viewthree = findViewById(R.id.viewthree);
        viewone = findViewById(R.id.viewone);
        viewtfour = findViewById(R.id.viewtfour);
        linearsaveandsend = findViewById(R.id.linearsaveandsend);
        saveContact = findViewById(R.id.saveContact);
        tvmob = findViewById(R.id.tvmob);
        layoutTop = findViewById(R.id.layoutTop);
        LinearLayout linMsg = findViewById(R.id.linMsg);
        LinearLayout linsave = findViewById(R.id.linsave);


        attendeetitle.setTextColor(Color.parseColor(colorActive));
        tvname.setTextColor(Color.parseColor(colorActive));
        layoutTop.setBackgroundColor(Color.parseColor(colorActive));
        saveContact.setBackgroundColor(Color.parseColor(colorActive));
        linMsg.setBackgroundColor(Color.parseColor(colorActive));
        linsave.setBackgroundColor(Color.parseColor(colorActive));


        sendbtn = findViewById(R.id.sendMsg);
        sendbtn.setBackgroundColor(Color.parseColor(colorActive));
        sendbtn.setVisibility(View.GONE);
        if (attendeeid.equalsIgnoreCase(getattendee)) {
            sendbtn.setVisibility(View.GONE);
            linearsaveandsend.setVisibility(View.GONE);
        } else {
            sendbtn.setVisibility(View.VISIBLE);
            linearsaveandsend.setVisibility(View.VISIBLE);
        }

        if (name.equalsIgnoreCase("N A")) {
            tvname.setVisibility(View.GONE);
        } else if (name != null) {
            tvname.setText(name);
        } else {
            tvname.setVisibility(View.GONE);
        }

        try {
            if (company.equalsIgnoreCase("N A")) {
                tvcompany.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            } else if (company != null && attendee_company.equalsIgnoreCase("1")) {
                if (company.equalsIgnoreCase("")) {
                    tvcompany.setVisibility(View.GONE);
                    viewthree.setVisibility(View.GONE);
                } else if (company.equalsIgnoreCase(" ")) {
                    tvcompany.setVisibility(View.GONE);
                    viewthree.setVisibility(View.GONE);
                } else {
                    tvcompany.setText(company);
                }

            } else {
                tvcompany.setVisibility(View.GONE);
                viewthree.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvcompany.setVisibility(View.GONE);
            viewthree.setVisibility(View.GONE);
        }


        try {
            if (attendee_mobile.equalsIgnoreCase("N A")) {
                tvmob.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);
            } else if (mobile != null && attendee_mobile.equalsIgnoreCase("1")) {
                if (mobile.equalsIgnoreCase("")) {
                    tvmob.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);
                } else if (mobile.equalsIgnoreCase(" ")) {
                    tvmob.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);
                } else {
                    tvmob.setText(mobile);
                }


            } else {
                tvmob.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvmob.setVisibility(View.GONE);
            viewtfour.setVisibility(View.GONE);
        }


        try {
            if (designation.equalsIgnoreCase("N A")) {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);

            } else if (designation != null && attendee_design.equalsIgnoreCase("1")) {
                if (designation.equalsIgnoreCase("")) {
                    tvdesignation.setVisibility(View.GONE);
                    viewtwo.setVisibility(View.GONE);
                    viewone.setVisibility(View.GONE);
                } else if (designation.equalsIgnoreCase(" ")) {
                    tvdesignation.setVisibility(View.GONE);
                    viewtwo.setVisibility(View.GONE);
                    viewone.setVisibility(View.GONE);
                } else {
                    tvdesignation.setText(designation);
                }
            } else {
                tvdesignation.setVisibility(View.GONE);
                viewtwo.setVisibility(View.GONE);
                viewone.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            tvdesignation.setVisibility(View.GONE);
            viewone.setVisibility(View.GONE);
            e.printStackTrace();
        }

        try {
            if (city.equalsIgnoreCase("N A")) {
                tvcity.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);

            } else if (city != null && attendee_location.equalsIgnoreCase("1")) {
                if (city.equalsIgnoreCase("")) {
                    tvcity.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);

                } else if (city.equalsIgnoreCase(" ")) {
                    tvcity.setVisibility(View.GONE);
                    viewtfour.setVisibility(View.GONE);

                } else {
                    tvcity.setText(city);
                }
            } else {
                tvcity.setVisibility(View.GONE);
                viewtfour.setVisibility(View.GONE);

            }
        } catch (Exception e) {
            tvcity.setVisibility(View.GONE);
            viewtfour.setVisibility(View.GONE);

            e.printStackTrace();
        }


        if (profile != null) {
            Glide.with(this).load(ApiConstant.profilepic + profile).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    profileIV.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(profileIV);
        } else {
            progressBar.setVisibility(View.GONE);
        }


        linMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posttextEt.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(posttextEt.getText().toString());
                    PostMesssage(eventid, msg, apikey, attendeeid);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });

        linsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    addToContactList(AttendeeDetailActivity.this, name, mobile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void applysetting(List<EventSettingList> eventSettingLists) {

        for (int i = 0; i < eventSettingLists.size(); i++) {

            if (eventSettingLists.get(i).getFieldName().equals("attendee_company")) {
                attendee_company = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("attendee_location")) {
                attendee_location = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equals("attendee_mobile")) {
                attendee_mobile = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_design")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            } else if (eventSettingLists.get(i).getFieldName().equalsIgnoreCase("attendee_designation")) {
                attendee_design = eventSettingLists.get(i).getFieldValue();
            }
        }
    }


    private void showratedialouge() {

        myDialog = new Dialog(AttendeeDetailActivity.this);
        myDialog.setContentView(R.layout.dialouge_msg_layout);
        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id

        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.canclebtn);
        Button ratebtn = myDialog.findViewById(R.id.ratebtn);

        final EditText etmsg = myDialog.findViewById(R.id.etmsg);

        final TextView counttv = myDialog.findViewById(R.id.counttv);
        final TextView nametv = myDialog.findViewById(R.id.nametv);

        nametv.setText("To " + name);

        etmsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                count = 250 - s.length();
                counttv.setText(count + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etmsg.getText().toString().length() > 0) {

                    String msg = StringEscapeUtils.escapeJava(etmsg.getText().toString());
                    PostMesssage(eventid, msg, apikey, attendeeid);
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void PostMesssage(String eventid, String msg, String token, String attendeeid) {
        showProgress();
//        showProgress();
        mAPIService.SendMessagePost(token, eventid, msg, attendeeid, "").enqueue(new Callback<SendMessagePost>() {
            @Override
            public void onResponse(Call<SendMessagePost> call, Response<SendMessagePost> response) {

                if (response.isSuccessful()) {
                    dismissProgress();
                    Log.i("hit", "post submitted to API." + response.body().toString());
//                    dismissProgress();
                    posttextEt.setText("");
                    DeletePostresponse(response);
                } else {
                    dismissProgress();
//                    dismissProgress();

                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendMessagePost> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Low network or no network");
//                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    private void DeletePostresponse(Response<SendMessagePost> response) {

        if (response.body().getStatus().equalsIgnoreCase("Success")) {

            Log.e("post", "success");

//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();


        } else {
            Log.e("post", "fail");
//            myDialog.dismiss();
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        //  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onResume();
    }

    public void addToContactList(Context context, String strDisplayName, String strNumber) throws Exception {

        // Get android phone contact content provider uri.
        //Uri addContactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        // Below uri can avoid java.lang.UnsupportedOperationException: URI: content://com.android.contacts/data/phones error.
        Uri addContactsUri = ContactsContract.Data.CONTENT_URI;

        // Add an empty contact and get the generated id.
        long rowContactId = getRawContactId();

        // Add contact name data.
        insertContactDisplayName(addContactsUri, rowContactId, strDisplayName);

        insertContactNotes(addContactsUri, rowContactId, strDisplayName);

        insertContactPhoneNumber(addContactsUri, rowContactId, strNumber, strDisplayName);

        Toast.makeText(getApplicationContext(),"New contact has been added, go back to previous page to see it in contacts list." , Toast.LENGTH_LONG).show();

//        finish();


    }

    public void showProgress() {
        if (progressBarmain.getVisibility() == View.GONE) {
            progressBarmain.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBarmain.getVisibility() == View.VISIBLE) {
            progressBarmain.setVisibility(View.GONE);
        }
    }


    // Insert newly created contact display name.
    private void insertContactDisplayName(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName);

        //Notes
        contentValues.put(ContactsContract.CommonDataKinds.Note.NOTE, "Met At "+eventnamestr);


        getContentResolver().insert(addContactsUri, contentValues);
    }

    private void insertContactNotes(Uri addContactsUri, long rawContactId, String displayName) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);

        //Notes
        contentValues.put(ContactsContract.CommonDataKinds.Note.NOTE, "Met At "+eventnamestr);

        getContentResolver().insert(addContactsUri, contentValues);
    }


        private void insertContactPhoneNumber(Uri addContactsUri, long rawContactId, String phoneNumber, String strDisplayName)
    {
        // Create a ContentValues object.
        ContentValues contentValues = new ContentValues();

        // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);

        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);

        // Put phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);

        // Calculate phone type by user selection.
        int phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

//        if("home".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
//        }else if("mobile".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;
//        }else if("work".equalsIgnoreCase(phoneTypeStr))
//        {
//            phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK;
//        }
        // Put phone type value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType);

        // Insert new contact data into phone contact list.
        getContentResolver().insert(addContactsUri, contentValues);


    }

    private long getRawContactId()
    {
        // Inser an empty contact.
        ContentValues contentValues = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
        // Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);
        return ret;
    }



}
