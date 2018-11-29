package com.procialize.singleevent.Activity;


import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import com.procialize.singleevent.Utility.ServiceHandler;
import com.procialize.singleevent.Utility.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class CurrencyConverter extends AppCompatActivity {

    Spinner firstans_list_spinner, secondans_list_spinner;
    private APIService mAPIService;
    String MY_PREFS_NAME = "ProcializeInfo";
    String eventid,colorActive;
    List<DropDownList> list = null;
    Button btnConverter;
    String fromCurrency, toCurrency, amount, currencyDropDown = "";
    EditText edtAmount, txtValue;
    ProgressBar progressBar;
    ImageView headerlogoIv;
    TextView txtHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter_new);
        SessionManager sessionManager = new SessionManager(CurrencyConverter.this);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive","");


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
        currencyDropDown = "https://www.procialize.info/API/event_api_call/GenInfoCurrencyDropdown";

        secondans_list_spinner = (Spinner) findViewById(R.id.secondans_list_spinner);
        firstans_list_spinner = (Spinner) findViewById(R.id.firstans_list_spinner);
        progressBar = findViewById(R.id.progressBar);

        /*econdans_list_spinner.setSelection(0, true);
        View v = secondans_list_spinner.getSelectedView();
        ((TextView)v).setTextColor(getResources().getColor(R.color.white));

        firstans_list_spinner.setSelection(0, true);
        View v1 = firstans_list_spinner.getSelectedView();
        ((TextView)v1).setTextColor(getResources().getColor(R.color.activetab));*/

        btnConverter = (Button) findViewById(R.id.btnConverter);
        edtAmount = (EditText) findViewById(R.id.edtAmount);
        txtValue = (EditText) findViewById(R.id.txtValue);
        txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setTextColor(Color.parseColor(colorActive));
        btnConverter.setTextColor(Color.parseColor(colorActive));

        txtValue.setEnabled(false);
        getInfoTab();

        btnConverter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstans_list_spinner.getSelectedItem()!=null && secondans_list_spinner.getSelectedItem()!=null) {
                    fromCurrency = firstans_list_spinner.getSelectedItem().toString();
                    toCurrency = secondans_list_spinner.getSelectedItem().toString();
                    amount = edtAmount.getText().toString();
                    if (amount.length() == 0) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverter.this);
                        builder.setTitle("");
                        builder.setMessage("Please Enter Value");

                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.show();
                    } else {
                        submitCurrency(fromCurrency, toCurrency, amount);

                    }
                }else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CurrencyConverter.this);
                    builder.setTitle("");
                    builder.setMessage("Please Select Dropdown Value");

                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.show();
                }

            }
        });

    }


    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
//        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/NotoSans-Light.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
//            view.setTypeface(font);
            view.setTextColor(Color.parseColor("#0E73BA"));

            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
//            view.setTypeface(font);
            view.setTextColor(Color.parseColor("#0E73BA"));
            view.setPadding(15, 15, 15, 15);
            return view;
        }
    }

    private static class MySpinnerAdapter1 extends ArrayAdapter<String> {
        // Initialise custom font, for example:
//        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
//                "fonts/NotoSans-Light.ttf");

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter1(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
//            view.setTypeface(font);
            view.setTextColor(Color.parseColor("#ffffff"));

            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
//            view.setTypeface(font);
            view.setTextColor(Color.parseColor("#ffffff"));
            view.setPadding(15, 15, 15, 15);
            return view;
        }
    }

    public void getInfoTab() {

        new GetCurrencyDropDown().execute(currencyDropDown);


/*
        mAPIService.FetchCurrencyDropDown(eventid).enqueue(new Callback<CurrencyDropDown>() {
            @Override
            public void onResponse(Call<CurrencyDropDown> call, Response<CurrencyDropDown> response) {

                if (response.body().getStatus().equals("success")) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    Log.i("hit", "post submitted to API." + response.body().getDropDownList().toString());

                    progressDialog.dismiss();
                    try {
                        if (response.body().getDropDownList() == null || response.body().getDropDownList().isEmpty()) {
                            ArrayList<String> myList = new ArrayList<>();
                            myList.add("");


                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, myList);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            firstans_list_spinner.setAdapter(adapter);

                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, myList);
                            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            secondans_list_spinner.setAdapter(adapter2);
                        } else {
                                list=response.body().getDropDownList();

                            ArrayAdapter<DropDownList> adapter = new ArrayAdapter<DropDownList>(CurrencyConverter.this,
                                    android.R.layout.simple_spinner_item, list);
                            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
*/
    }

    public class GetCurrencyDropDown extends AsyncTask<String, String, JSONObject> {

        String json1 = "";
        InputStream is = null;
        JSONObject json = null;
        JSONObject status;
        JSONArray jsonArray;
        String statusDesc, msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            showProgress();

        }

        @Override
        protected JSONObject doInBackground(String... params) {


            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            nameValuePair.add(new BasicNameValuePair("event_id", eventid));//quesRate1


            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(currencyDropDown, ServiceHandler.POST,
                    nameValuePair);

//            Log.d("Response: ", "> " + jsonStr);

            try {
                json = new JSONObject(jsonStr);


            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());

            }


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            dismissProgress();
            List<String> categories = new ArrayList<String>();

            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                statusDesc = jsonObject.getString("status");
                msg = jsonObject.getString("msg");

                if (statusDesc.equalsIgnoreCase("success")) {
                    jsonArray = jsonObject.getJSONArray("currency_dropdown");
                    int count = 0;
                    String currency_code;

                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        currency_code = JO.getString("currency_code");
                        categories.add(currency_code);
                        count++;
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CurrencyConverter.this, android.R.layout.simple_spinner_item, categories);

                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    MySpinnerAdapter adapter = new MySpinnerAdapter(CurrencyConverter.this,
                            android.R.layout.simple_spinner_item, categories
                    );

                    MySpinnerAdapter1 adapter1 = new MySpinnerAdapter1(CurrencyConverter.this,
                            R.layout.spinner_dropdown_item, categories
                    );

                    // attaching data adapter to spinner
                    firstans_list_spinner.setAdapter(adapter1);
                    secondans_list_spinner.setAdapter(adapter);
                } else {

                    Toast.makeText(CurrencyConverter.this, msg,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    public void submitCurrency(String fromCurrency, String toCurrency, String amount) {
        showProgress();

        mAPIService.SubmitCurrencyConverter(eventid, fromCurrency, toCurrency, amount).enqueue(new Callback<CurrencyConverterResponse>() {
            @Override
            public void onResponse(Call<CurrencyConverterResponse> call, Response<CurrencyConverterResponse> response) {

                if (response.body().getStatus().equals("success")) {
                    dismissProgress();
//                    Log.i("hit", "post submitted to API." + response.body().toString());
                    String converted_amt = response.body().getConverted_amount();

                    txtValue.setText(converted_amt);

                } else {
                    dismissProgress();
                    Toast.makeText(CurrencyConverter.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CurrencyConverterResponse> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Unable to submit post to API.");
                Toast.makeText(CurrencyConverter.this, "Low network or no network", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }


}
