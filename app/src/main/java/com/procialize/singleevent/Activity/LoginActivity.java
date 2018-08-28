package com.procialize.singleevent.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.procialize.singleevent.Adapter.EventAdapter;
import com.procialize.singleevent.ApiConstant.APIService;
import com.procialize.singleevent.ApiConstant.ApiUtils;
import com.procialize.singleevent.CustomTools.Connectivity;
import com.procialize.singleevent.GetterSetter.EventListing;
import com.procialize.singleevent.GetterSetter.Login;
import com.procialize.singleevent.R;
import com.procialize.singleevent.Session.SessionManager;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LoginActivity extends AppCompatActivity {


    Boolean emailbool=false,passwordbool=false;
    private APIService mAPIService;
    ProgressBar progressBar2;
    Button loginbtn;
    String emailid,password;
    public static final int RequestPermissionCode = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);



        mAPIService = ApiUtils.getAPIService();

        underLineTextview();
        initializeView();
    }


    private void underLineTextview() {

        TextView textView = findViewById(R.id.textView);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setText("Designed & Developed by Procialize");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://www.procialize.net/home.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void initializeView() {


        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        final TextInputEditText Etemail,Etpassword;
        final TextInputLayout inputLayoutemail,inputLayoutpassword;


        Etemail = findViewById(R.id.input_email);
        Etpassword=findViewById(R.id.input_password);
        progressBar2=findViewById(R.id.progressBar2);

        inputLayoutemail = findViewById(R.id.input_layout_email);
        inputLayoutemail.setErrorEnabled(true);
        inputLayoutpassword = findViewById(R.id.input_layout_password);
        inputLayoutpassword.setErrorEnabled(true);

        loginbtn=findViewById(R.id.loginbtn);

        Etemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.toString().matches(emailPattern) && s.length() > 0)
                {
                    inputLayoutemail.setError(null);
                    emailbool = true;
                }
                else
                {
                    inputLayoutemail.setError("Please Enter Valid Email Id");
                    emailbool = false;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // other stuffs
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // other stuffs
            }
        });


        Etpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() > 0)
                {
                    inputLayoutpassword.setError(null);
                    passwordbool=true;
                }
                else
                {
                    inputLayoutpassword.setError("Please Enter Password");
                    passwordbool=false;
                }
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

                if (emailbool==false)
                {
                    inputLayoutemail.setError("Please Enter Email Id");
                }else if (passwordbool==false)
                {
                    inputLayoutemail.setError(null);
                    inputLayoutpassword.setError("Please Enter Password");
                }else
                {
                    inputLayoutpassword.setError(null);
                    inputLayoutemail.setError(null);

                    if (Connectivity.isConnected(LoginActivity.this)) {
                        emailid = Etemail.getText().toString();
                        password = Etpassword.getText().toString();
                        showProgress();
                        sendEventList(Etemail.getText().toString(), Etpassword.getText().toString());
                    }else
                    {
                        Toast.makeText(LoginActivity.this,"You are not connected to Internet for processing",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        if(CheckingPermissionIsEnabledOrNot())
        {
//            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();
        }

        // If, If permission is not enabled then else condition will execute.
        else {

            //Calling method to enable permission.
            RequestMultiplePermission();

        }



    }

    public void sendEventList(String email, String password) {
        mAPIService.EventListPost(email, password).enqueue(new Callback<EventListing>() {
            @Override
            public void onResponse(Call<EventListing> call, Response<EventListing> response) {

                if(response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.body().toString());
                    dismissProgress();
                    showResponse(response);
                }else
                {
                    dismissProgress();
                    Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                    Log.i("hit", "post submitted to API Wrong.");
                }
            }

            @Override
            public void onFailure(Call<EventListing> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Unable to process",Toast.LENGTH_SHORT).show();
                dismissProgress();
            }
        });
    }

    public void showResponse(Response<EventListing> response) {

        if (response.body().getStatus().equals("success")) {

            Intent event = new Intent(getApplicationContext(), EventChooserActivity.class);
            event.putExtra("email",emailid);
            event.putExtra("password",password);
            startActivity(event);
            finish();

        }else
        {
            Toast.makeText(this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    public void showProgress()
    {
        progressBar2.setVisibility(View.VISIBLE);
        loginbtn.setEnabled(false);
    }

    public void dismissProgress()
    {
        if (progressBar2.getVisibility()==View.VISIBLE)
        {
            progressBar2.setVisibility(View.GONE);
        }
        loginbtn.setEnabled(true);
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
                SixthPermissionResult == PackageManager.PERMISSION_GRANTED ;
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
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"We need your permission so you can enjoy full features of app",Toast.LENGTH_LONG).show();
                        RequestMultiplePermission();

                    }
                }

                break;
        }
    }
}
