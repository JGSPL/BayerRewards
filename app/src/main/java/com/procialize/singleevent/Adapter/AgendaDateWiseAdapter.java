package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procialize.singleevent.Activity.AgendaDetailActivity;
import com.procialize.singleevent.GetterSetter.AgendaList;
import com.procialize.singleevent.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgendaDateWiseAdapter extends RecyclerView.Adapter<AgendaDateWiseAdapter.MyViewHolder> {

    private List<AgendaList> agendaLists;
    private Context context;
    String date = "";
    private AgendaAdapterListner listener;
//    private AgendaAdapter.AgendaAdapterListner listener;

    public AgendaDateWiseAdapter(Context context, List<AgendaList> agendaLists, AgendaAdapterListner listener) {
        this.agendaLists = agendaLists;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agenda_date_wise, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final AgendaList agenda = agendaLists.get(position);

        holder.nameTv.setText(agenda.getSessionName());
        holder.descriptionTv.setText(agenda.getSessionDescription());


        try {

            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy kk:mm");

            Date startdate = originalFormat.parse(agenda.getSessionStartTime());
            Date enddate = originalFormat.parse(agenda.getSessionEndTime());

            String startdatestr = targetFormat.format(startdate);
            String enddatestr = targetFormat.format(enddate);


            holder.dateTv.setText(startdatestr + " - " + enddatestr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder. mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agendadetail = new Intent(context, AgendaDetailActivity.class);

                agendadetail.putExtra("id", agenda.getSessionId());
                agendadetail.putExtra("date", agenda.getSessionDate());
                agendadetail.putExtra("name", agenda.getSessionName());
                agendadetail.putExtra("description", agenda.getSessionDescription());
                agendadetail.putExtra("starttime", agenda.getSessionStartTime());
                agendadetail.putExtra("endtime", agenda.getSessionEndTime());
                context.startActivity(agendadetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return agendaLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, dateTv, descriptionTv, tvheading;
        public LinearLayout mainLL;


        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            dateTv = (TextView) view.findViewById(R.id.dateTv);
            descriptionTv = (TextView) view.findViewById(R.id.descriptionTv);
            tvheading = (TextView) view.findViewById(R.id.tvheading);


            mainLL = (LinearLayout) view.findViewById(R.id.mainLL);



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onContactSelecteddate(agendaLists.get(getAdapterPosition()));
//                }
//            });

        }
    }

    public interface AgendaAdapterListner {
        void onContactSelecteddate(AgendaList agendaList);
    }

}
