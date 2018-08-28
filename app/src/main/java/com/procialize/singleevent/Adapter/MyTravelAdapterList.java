package com.procialize.singleevent.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.procialize.singleevent.GetterSetter.TravelList;
import com.procialize.singleevent.R;

import java.util.List;

public class MyTravelAdapterList extends BaseAdapter {
    private List<TravelList> travelLists;
    private Context context;
    private MyTravelAdapterListner listener;
    private LayoutInflater inflater;

    public MyTravelAdapterList(Context context, List<TravelList> travelList, MyTravelAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getCount() {
        return travelLists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.feedbackrow, null);

            holder = new ViewHolder();

            Display dispDefault = ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            int totalwidth = dispDefault.getWidth();


            holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);


            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(travelLists.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView nameTv;
    }

    public interface MyTravelAdapterListner {
        void onContactSelected(TravelList travel);
    }
}
