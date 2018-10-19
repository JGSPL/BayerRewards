package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procialize.singleevent.Activity.PdfViewerActivity;
import com.procialize.singleevent.GetterSetter.DocumentList;
import com.procialize.singleevent.GetterSetter.TravelList;
import com.procialize.singleevent.R;

import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class MyTravelAdapter extends RecyclerView.Adapter<MyTravelAdapter.MyViewHolder> {

    private List<TravelList> travelLists;
    private Context context;
    private MyTravelAdapterListner listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(travelLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public MyTravelAdapter(Context context, List<TravelList> travelList,MyTravelAdapterListner listener) {
        this.travelLists = travelList;
        this.listener=listener;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TravelList travel = travelLists.get(position);
        holder.nameTv.setText(travel.getTitle());
    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(TravelList travel);
    }
}