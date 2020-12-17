package com.bayer.bayerreward.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.LocationDistrict;
import com.bayer.bayerreward.GetterSetter.LocationState;
import com.bayer.bayerreward.GetterSetter.LocationTaluka;
import com.bayer.bayerreward.GetterSetter.LocationVillage;
import com.bayer.bayerreward.GetterSetter.SignUpRequest;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class SignupBayerActivity extends AppCompatActivity {

    ImageView headerlogoIv;
    Button btn_submit;
    EditText edit_shop_name, edit_mobile_name, edit_last_name, edit_first_name, edit_pincode;
    Spinner state_spinner, district_spinner, taluka_spinner, village_spinner;
    CheckBox chkbox;
    APIService mAPIService;
    ProgressBar progressBar;
    String MY_PREFS_NAME = "ProcializeInfo";
    SessionManager sessionManager;
    List<String> states = new ArrayList<>();
    List<String> district = new ArrayList<>();
    List<String> taluka = new ArrayList<>();
    List<String> village = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_bayer);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        btn_submit = findViewById(R.id.btn_submit);
        edit_shop_name = findViewById(R.id.edit_shop_name);
        edit_mobile_name = findViewById(R.id.edit_mobile_name);
        edit_last_name = findViewById(R.id.edit_last_name);
        edit_first_name = findViewById(R.id.edit_first_name);
        edit_pincode = findViewById(R.id.edit_pincode);
        state_spinner = findViewById(R.id.state_spinner);
        district_spinner = findViewById(R.id.district_spinner);
        taluka_spinner = findViewById(R.id.taluka_spinner);
        village_spinner = findViewById(R.id.village_spinner);
        chkbox = findViewById(R.id.chkbox);
        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.activetab), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        mAPIService = ApiUtils.getAPIService();

        LocationState("194", "");

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selectedstate = state_spinner.getSelectedItem().toString();
                LocationDistrict("194", "", Selectedstate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selecteddistrict = district_spinner.getSelectedItem().toString();
                LocationTaluka("194", "", Selecteddistrict);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        taluka_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Selectedtaluka = taluka_spinner.getSelectedItem().toString();
                LocationVillage("194", "", Selectedtaluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = edit_first_name.getText().toString();
                String lname = edit_last_name.getText().toString();
                String mobile = edit_mobile_name.getText().toString();
                String shopname = edit_shop_name.getText().toString();
                String pincode = edit_pincode.getText().toString();
                String state = state_spinner.getSelectedItem().toString();
                String district = district_spinner.getSelectedItem().toString();
                String taluka = taluka_spinner.getSelectedItem().toString();
                String village = village_spinner.getSelectedItem().toString();

                if (fname.isEmpty()) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (lname.isEmpty()) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (mobile.isEmpty()) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (pincode.isEmpty()) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (state.equalsIgnoreCase("Select State")) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (district.equalsIgnoreCase("Select District")) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (taluka.equalsIgnoreCase("Select Taluka")) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (village.equalsIgnoreCase("Select Village")) {
                    Toast.makeText(SignupBayerActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!chkbox.isChecked()) {
                    Toast.makeText(SignupBayerActivity.this, "Agree to the terms and conditions.", Toast.LENGTH_SHORT).show();
                } else {
                    SignUpRequest("194", fname, lname, mobile, shopname, pincode, state, district, taluka, village);
                }


            }
        });

        CommonFunction.crashlytics("SignUpBayer","");
        firbaseAnalytics(this,"SignUpBayer","");
    }

    public void LocationState(String eventid, String token) {
        showProgress();
        mAPIService.LocationState(token, eventid).enqueue(new Callback<LocationState>() {
            @Override
            public void onResponse(Call<LocationState> call, Response<LocationState> response) {

                if (response.isSuccessful()) {
                    disableProgress();
                    states.clear();
                    states.add(0, "Select State");
                    for (int i = 0; i < response.body().getLocation_state_master().size(); i++) {
                        states.add(response.body().getLocation_state_master().get(i).getState());
                    }
                    ArrayAdapter dataAdapter2 = new ArrayAdapter(SignupBayerActivity.this, R.layout.spinner_item, states);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    state_spinner.setAdapter(dataAdapter2);
                } else {
                    disableProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LocationState> call, Throwable t) {
                disableProgress();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void LocationDistrict(String eventid, String token, String state) {
        showProgress();
        mAPIService.LocationDistrict(token, eventid, state).enqueue(new Callback<LocationDistrict>() {
            @Override
            public void onResponse(Call<LocationDistrict> call, Response<LocationDistrict> response) {

                if (response.isSuccessful()) {
                    disableProgress();
                    district.clear();
                    district.add(0, "Select District");
                    for (int i = 0; i < response.body().getLocation_district_master().size(); i++) {
                        district.add(response.body().getLocation_district_master().get(i).getDistrict());
                    }
                    ArrayAdapter dataAdapter2 = new ArrayAdapter(SignupBayerActivity.this, R.layout.spinner_item, district);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    district_spinner.setAdapter(dataAdapter2);
                } else {
                    disableProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LocationDistrict> call, Throwable t) {
                disableProgress();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void LocationTaluka(String eventid, String token, String district) {
        showProgress();
        mAPIService.LocationTaluka(token, eventid, district).enqueue(new Callback<LocationTaluka>() {
            @Override
            public void onResponse(Call<LocationTaluka> call, Response<LocationTaluka> response) {

                if (response.isSuccessful()) {
                    disableProgress();
                    taluka.clear();
                    taluka.add(0, "Select Taluka");
                    for (int i = 0; i < response.body().getLocation_taluka_master().size(); i++) {
                        taluka.add(response.body().getLocation_taluka_master().get(i).getTaluka());
                    }
                    ArrayAdapter dataAdapter2 = new ArrayAdapter(SignupBayerActivity.this, R.layout.spinner_item, taluka);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    taluka_spinner.setAdapter(dataAdapter2);
                } else {
                    disableProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LocationTaluka> call, Throwable t) {
                disableProgress();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void LocationVillage(String eventid, String token, String district) {
        showProgress();
        mAPIService.LocationVillage(token, eventid, district).enqueue(new Callback<LocationVillage>() {
            @Override
            public void onResponse(Call<LocationVillage> call, Response<LocationVillage> response) {

                if (response.isSuccessful()) {
                    disableProgress();
                    village.clear();
                    village.add(0, "Select Village");
                    for (int i = 0; i < response.body().getLocation_village_master().size(); i++) {
                        village.add(response.body().getLocation_village_master().get(i).getVillage());
                    }
                    ArrayAdapter dataAdapter2 = new ArrayAdapter(SignupBayerActivity.this, R.layout.spinner_item, village);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    village_spinner.setAdapter(dataAdapter2);
                } else {
                    disableProgress();
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<LocationVillage> call, Throwable t) {
                disableProgress();
                Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void SignUpRequest(String eventid, String fname, String lname, String mobile, String shopname, String pincode, String state, String district, String taluka, String village) {

        mAPIService.SignUpRequest(eventid, fname, lname, mobile, shopname, pincode, state, district, taluka, village).enqueue(new Callback<SignUpRequest>() {
            @Override
            public void onResponse(Call<SignUpRequest> call, Response<SignUpRequest> response) {

                if (response.isSuccessful()) {
                    Intent intent = new Intent(SignupBayerActivity.this, ThankyouSignup.class);
                    startActivity(intent);
                    finish();


                } else {
                    Toast.makeText(getApplicationContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignUpRequest> call, Throwable t) {


            }
        });
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void disableProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
