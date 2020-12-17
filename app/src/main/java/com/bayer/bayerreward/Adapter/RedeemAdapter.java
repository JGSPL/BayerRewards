package com.bayer.bayerreward.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bayer.bayerreward.Activity.CatlogActivity;
import com.bayer.bayerreward.ApiConstant.APIService;
import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.ApiConstant.ApiUtils;
import com.bayer.bayerreward.GetterSetter.RedeemRequest;
import com.bayer.bayerreward.GetterSetter.catalog_list;
import com.bayer.bayerreward.R;
import com.bayer.bayerreward.Session.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RedeemAdapter extends RecyclerView.Adapter<RedeemAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<catalog_list> travelLists;
    private Context context;
    int points1;
    Dialog myDialog;
    APIService mAPIService;
    ProgressBar progressbar;
    String eventid, apikey;
    SessionManager sessionManager;


    public RedeemAdapter(Context context, List<catalog_list> travelList) {
        this.travelLists = travelList;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, context.MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");
        eventid = prefs.getString("eventid", "1");

        mAPIService = ApiUtils.getAPIService();
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        // apikey
        apikey = user.get(SessionManager.KEY_TOKEN);

    }

    @Override
    public RedeemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catlog_item, parent, false);

        return new RedeemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RedeemAdapter.MyViewHolder holder, int position) {
        final catalog_list travel = travelLists.get(position);


        holder.txt_rewardname.setText(travel.getProduct_name());

        holder.txt_points.setText("Value =" + travel.getProduct_value() + " Points");

        Glide.with(context).load(ApiConstant.Catlog + travel.getProduct_image())
                .apply(RequestOptions.skipMemoryCacheOf(false))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.image_reward.setImageResource(R.drawable.profilepic_placeholder);
                return true;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(holder.image_reward);

        if (Integer.parseInt(travel.getProduct_value()) <= Integer.parseInt(CatlogActivity.txt_totalpoints.getText().toString())) {
            holder.btn_reedem.setVisibility(View.VISIBLE);
            holder.txt_needed.setVisibility(View.GONE);

        } else {
            holder.btn_reedem.setVisibility(View.GONE);
            holder.txt_needed.setVisibility(View.VISIBLE);

            int Total = (Integer.parseInt(travel.getProduct_value()) - Integer.parseInt(CatlogActivity.txt_totalpoints.getText().toString()));
            holder.txt_needed.setText(String.valueOf(Total) + " Points more needed.");
        }

        holder.btn_reedem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showratedialouge(travel.getProduct_name(), travel.getProduct_value(), travel.getProduct_code());
            }
        });


    }


    @Override
    public int getItemCount() {
        return travelLists.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_rewardname,
                txt_points,
                txt_needed;
        Button btn_reedem;
        ImageView image_reward;

        public MyViewHolder(View view) {
            super(view);
            txt_rewardname = view.findViewById(R.id.txt_rewardname);
            txt_points = view.findViewById(R.id.txt_points);
            txt_needed = view.findViewById(R.id.txt_needed);
            btn_reedem = view.findViewById(R.id.btn_reedem);
            image_reward = view.findViewById(R.id.image_reward);

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void showratedialouge(final String name, final String value, final String productcode) {

        myDialog = new Dialog(context);
        myDialog.setContentView(R.layout.confirmation_dialog);
//        myDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id
        myDialog.setCancelable(false);
        myDialog.show();


        Button cancelbtn = myDialog.findViewById(R.id.btn_cancel);
        final Button btn_submit = myDialog.findViewById(R.id.btn_submit);
        final EditText edit_quantity = myDialog.findViewById(R.id.edit_quantity);
        TextView txtValue = myDialog.findViewById(R.id.txtValue);
        TextView txt_name = myDialog.findViewById(R.id.name);
        progressbar = myDialog.findViewById(R.id.progressBar);

        txt_name.setText(name);
        txtValue.setText("Value = " + value + " Points");

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edit_quantity.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Please enter any value for proceed", Toast.LENGTH_SHORT).show();

                } else {
                    Long quantity = Long.parseLong(edit_quantity.getText().toString());
                    int totalpoints = Integer.parseInt(CatlogActivity.txt_totalpoints.getText().toString());
                    Long mpoints = Long.parseLong(value) * quantity;

                    if (mpoints > Long.parseLong(CatlogActivity.txt_totalpoints.getText().toString())) {
                        Toast.makeText(context, "You dont have sufficient balance points", Toast.LENGTH_SHORT).show();
                    } else {
                        btn_submit.setEnabled(false);
                        btn_submit.setClickable(false);
                        RedeemRequest(eventid, apikey, productcode, name, String.valueOf(quantity), String.valueOf(mpoints));
                    }
                }


            }
        });
    }

    public void RedeemRequest(String eventid, String token, String product_code, String product_name, String no_of_points, String mpoints) {
        showProgress();
//        showProgress();
        mAPIService.RedeemRequest(token, eventid, product_code, product_name, no_of_points, mpoints).enqueue(new Callback<RedeemRequest>() {
            @Override
            public void onResponse(Call<RedeemRequest> call, Response<RedeemRequest> response) {

                if (response.isSuccessful()) {
//                    btn_submit.setEnabled(false);
//                    btn_submit.setClickable(false);
                    dismissProgress();
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    myDialog.dismiss();
                    if ((Integer.parseInt(response.body().getTotal_available_point())) < 0) {
                        CatlogActivity.txt_totalpoints.setText("0");
                    } else {
                        CatlogActivity.txt_totalpoints.setText(response.body().getTotal_available_point());
                    }
                    Intent intent = new Intent(context, CatlogActivity.class);
                    context.startActivity(intent);
                } else {

                    dismissProgress();
                    myDialog.dismiss();


                }
            }

            @Override
            public void onFailure(Call<RedeemRequest> call, Throwable t) {
                dismissProgress();
                Log.e("hit", "Low network or no network");
                myDialog.dismiss();
//                btn_submit.setEnabled(false);
//                btn_submit.setClickable(false);

            }
        });
    }

    public void showProgress() {
        progressbar.setVisibility(View.VISIBLE);
    }

    public void dismissProgress() {
        progressbar.setVisibility(View.GONE);
    }


}

