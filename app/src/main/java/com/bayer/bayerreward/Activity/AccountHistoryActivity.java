
package com.bayer.bayerreward.Activity;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bayer.bayerreward.Adapter.AccountHistoryAdapter;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.DbHelper.ConnectionDetector;
import com.bayer.bayerreward.DbHelper.DBHelper;
import com.bayer.bayerreward.GetterSetter.AccountHistory;
import com.bayer.bayerreward.GetterSetter.account_history_data;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bayer.bayerreward.Utility.Util;
import com.bayer.bayerreward.utils.CommonFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bayer.bayerreward.utils.CommonFunction.firbaseAnalytics;

public class AccountHistoryActivity extends AppCompatActivity {

    ImageView headerlogoIv;
    SessionManager sessionManager;
    RelativeLayout relative;
    ProgressBar progressBar;
    APIService mAPIService;
    String apikey, eventid;
    String MY_PREFS_NAME = "ProcializeInfo";
    ConnectionDetector cd;
    RecyclerView recycler_acc_history;
    private DBHelper dbHelper;
    private DBHelper procializeDB;
    private SQLiteDatabase db;
    private ArrayList<account_history_data> attendeeDBList = new ArrayList<>();
    private List<account_history_data> historylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_history);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(Color.parseColor("#0092df"), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cd = new ConnectionDetector(AccountHistoryActivity.this);
        headerlogoIv = findViewById(R.id.headerlogoIv);
        recycler_acc_history = findViewById(R.id.recycler_acc_history);
        sessionManager = new SessionManager(AccountHistoryActivity.this);
        progressBar = findViewById(R.id.progressBar);
        Util.logomethod(this, headerlogoIv);
        mAPIService = ApiUtils.getAPIService();
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        HashMap<String, String> user = sessionManager.getUserDetails();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_acc_history.setLayoutManager(mLayoutManager);
        dbHelper = new DBHelper(AccountHistoryActivity.this);

        procializeDB = new DBHelper(AccountHistoryActivity.this);
        db = procializeDB.getWritableDatabase();

        attendeeDBList = new ArrayList<>();
        historylist = new ArrayList<>();
        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);
        if (cd.isConnectingToInternet()) {
            AccountHistory(eventid, apikey);
        }

        CommonFunction.crashlytics("AccountHistory","");
        firbaseAnalytics(this,"AccountHistory","");

    }

    public void AccountHistory(String eventid, String token) {
        showProgress();
//        showProgress();
        mAPIService.AccountHistory(token, eventid).enqueue(new Callback<AccountHistory>() {
            @Override
            public void onResponse(Call<AccountHistory> call, Response<AccountHistory> response) {

                if (response.isSuccessful()) {
                    dismissProgress();
                    dbHelper.clearAccHistoryTable();
                    dbHelper.insertAccHistoryTable(response.body().getAccount_history_data(), db);

                    db = procializeDB.getReadableDatabase();

                    attendeeDBList.clear();
                    attendeeDBList = dbHelper.getAccHistoryDetails();

                    historylist.clear();
                    historylist = removeDuplicates(attendeeDBList);


                    AccountHistoryAdapter adapter = new AccountHistoryAdapter(AccountHistoryActivity.this, historylist, response.body().getTotal_available_point());
                    adapter.notifyDataSetChanged();
                    recycler_acc_history.setAdapter(adapter);


                } else {
                    dismissProgress();


                }
            }

            @Override
            public void onFailure(Call<AccountHistory> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Low network or no network");
//                Toast.makeText(getApplicationContext(), "Low network or no network", Toast.LENGTH_SHORT).show();

//                dismissProgress();
            }
        });
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public ArrayList<account_history_data> removeDuplicates(ArrayList<account_history_data> list) {
        Set<account_history_data> set = new TreeSet(new Comparator<account_history_data>() {

            @Override
            public int compare(account_history_data o1, account_history_data o2) {
                String sessiondate = null;
                String sessiondate1 = null;
//                try {
//                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
//                    SimpleDateFormat targetFormat = new SimpleDateFormat(" dd/MM/yyyy");
//                    Date date = originalFormat.parse(o1.getTransaction_date());
//                    sessiondate = targetFormat.format(date);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                try {
//                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);
//                    SimpleDateFormat targetFormat = new SimpleDateFormat(" dd/MM/yyyy");
//                    Date date = originalFormat.parse(o2.getTransaction_date());
//                    sessiondate1 = targetFormat.format(date);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                if (o1.getTransaction_date().equalsIgnoreCase(o2.getTransaction_date())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final ArrayList newList = new ArrayList(set);
        return newList;
    }
}
