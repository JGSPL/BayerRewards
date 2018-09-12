package com.procialize.singleevent.Activity;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.GetterSetter.CurrencyConverterResponse;
import com.procialize.singleevent.GetterSetter.CurrencyDropDown;
import com.procialize.singleevent.GetterSetter.DropDownList;
import com.procialize.singleevent.GetterSetter.GeneralInfoList;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyConverter extends AppCompatActivity {

    Spinner firstans_list_spinner, secondans_list_spinner;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid;
    List<DropDownList> list;
    Button btnConverter;
    String fromCurrency, toCurrency, amount;
    EditText edtAmount, txtValue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);
        SessionManager sessionManager = new SessionManager(CurrencyConverter.this);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");


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

        secondans_list_spinner = (Spinner) findViewById(R.id.secondans_list_spinner);
        firstans_list_spinner = (Spinner) findViewById(R.id.firstans_list_spinner);
        btnConverter = (Button) findViewById(R.id.btnConverter);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        txtValue = (EditText) findViewById(R.id.txtValue);
        getInfoTab();

        btnConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromCurrency = firstans_list_spinner.getSelectedItem().toString();
                toCurrency = secondans_list_spinner.getSelectedItem().toString();
                amount = edtAmount.getText().toString();

                submitCurrency(fromCurrency, toCurrency, amount);
            }
        });

    }


    public void getInfoTab() {

        progressDialog = new ProgressDialog(CurrencyConverter.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAPIService.FetchCurrencyDropDown(eventid).enqueue(new Callback<CurrencyDropDown>() {
            @Override
            public void onResponse(Call<CurrencyDropDown> call, Response<CurrencyDropDown> response) {

                if (response.body().getStatus().equals("success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    progressDialog.dismiss();
                    try {
                        if (response.body().getDropDownList() == null || response.body().getDropDownList().isEmpty()) {
                            ArrayList<String> myList = new ArrayList<>();
                            myList.add("");


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, myList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            firstans_list_spinner.setAdapter(adapter);

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, myList);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            secondans_list_spinner.setAdapter(adapter2);
                        } else {
                            list = response.body().getDropDownList();
                            ArrayAdapter<DropDownList> adapter = new ArrayAdapter<DropDownList>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            firstans_list_spinner.setAdapter(adapter);

                            ArrayAdapter<DropDownList> adapter2 = new ArrayAdapter<DropDownList>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, list);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            secondans_list_spinner.setAdapter(adapter2);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CurrencyConverter.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrencyDropDown> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CurrencyConverter.this, "Unable to process", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submitCurrency(String fromCurrency, String toCurrency, String amount) {
        progressDialog = new ProgressDialog(CurrencyConverter.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        mAPIService.SubmitCurrencyConverter(eventid, fromCurrency, toCurrency, amount).enqueue(new Callback<CurrencyConverterResponse>() {
            @Override
            public void onResponse(Call<CurrencyConverterResponse> call, Response<CurrencyConverterResponse> response) {

                if (response.body().getStatus().equals("success")) {
                    progressDialog.dismiss();
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    String converted_amt = response.body().getConverted_amount();

                    txtValue.setText(converted_amt);

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(CurrencyConverter.this, "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrencyConverterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CurrencyConverter.this, "Unable to process", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
