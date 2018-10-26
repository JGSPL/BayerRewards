package com.procialize.vivo_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.procialize.vivo_app.GetterSetter.DocumentList;
import com.procialize.vivo_app.GetterSetter.Forecast;
import com.procialize.vivo_app.GetterSetter.Weather;
import com.procialize.vivo_app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private List<Forecast> forecastList;
    private Context context;
    private WeatherAdapterListner listener;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv,dateTv,maxTv,minTv,typev;
        public ImageView imageIv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            dateTv = view.findViewById(R.id.dateTv);
            maxTv = view.findViewById(R.id.maxTv);
            minTv = view.findViewById(R.id.minTv);
            typev = view.findViewById(R.id.typev);

            imageIv = view.findViewById(R.id.imageIv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(forecastList.get(getAdapterPosition()));
                }
            });
        }
    }


    public WeatherAdapter(Context context, List<Forecast> forecastList, WeatherAdapterListner listener) {
        this.forecastList = forecastList;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weatherrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Forecast forecast = forecastList.get(position);

        char tmp = 0x00B0;


        int min_temp = (Integer.parseInt(forecast.getLow()) - 32) * 5 / 9;
        int max_temp = (Integer.parseInt(forecast.getHigh()) - 32) * 5 / 9;


        holder.maxTv.setText(String.valueOf(max_temp) + tmp+"" );
        holder.minTv.setText(String.valueOf(min_temp) + tmp+"" );

        holder.typev.setText(forecast.getText());

//        if (forecast.getText().equalsIgnoreCase("Thunderstorms"))
//        {
//            holder.imageIv.setImageResource(R.drawable.thunderwhite);
//        }else if (forecast.getText().equalsIgnoreCase("Mostly Cloudy"))
//        {
//            holder.imageIv.setImageResource(R.drawable.cleanwhite);
//        }else if (forecast.getText().equalsIgnoreCase("Cloudy"))
//        {
//            holder.imageIv.setImageResource(R.drawable.cloudywhite);
//        }else
//        {
//            holder.imageIv.setImageResource(R.drawable.sunnywhite);
//        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM");
        SimpleDateFormat mymonthFormat = new SimpleDateFormat("EEE");

        Date date = new Date();
        try {
            date = dateFormat.parse(forecast.getDate());
            System.out.println("Date is: "+date);
        } catch (ParseException e) {e.printStackTrace();}

        String datestr = myFormat.format(date);
        String month = mymonthFormat.format(date);

        holder.dateTv.setText(datestr);
        holder.nameTv.setText(month);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public interface WeatherAdapterListner {
        void onContactSelected(Forecast forecast);
    }
}