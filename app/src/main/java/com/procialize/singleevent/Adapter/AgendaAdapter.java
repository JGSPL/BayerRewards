package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.MyViewHolder> implements AgendaDateWiseAdapter.AgendaAdapterListner {

    private List<AgendaList> agendaLists;
    private List<AgendaList> tempagendaList = new ArrayList<AgendaList>();
    private Context context;
    String date = "";
    String newdate = "";
    private AgendaAdapterListner listener;
    AgendaDateWiseAdapter agendaDateWiseAdapter;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;


    @Override
    public void onContactSelecteddate(AgendaList agenda) {
        Intent agendadetail = new Intent(context, AgendaDetailActivity.class);

        agendadetail.putExtra("id", agenda.getSessionId());
        agendadetail.putExtra("date", agenda.getSessionDate());
        agendadetail.putExtra("name", agenda.getSessionName());
        agendadetail.putExtra("description", agenda.getSessionDescription());
        agendadetail.putExtra("starttime", agenda.getSessionStartTime());
        agendadetail.putExtra("endtime", agenda.getSessionEndTime());

        context.startActivity(agendadetail);
    }



    public AgendaAdapter(Context context, List<AgendaList> agendaLists, AgendaAdapterListner listener) {
        this.agendaLists = agendaLists;
        this.context = context;
        this.listener = listener;
//        this.tempagendaList = tempagendaList;

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive","");

    }

    @Override
    public int getItemCount() {
        return agendaLists.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AgendaList agenda = agendaLists.get(position);

//        holder.nameTv.setText(agenda.getSessionName());
//        holder.descriptionTv.setText(agenda.getSessionDescription());

        holder.tvheading.setBackgroundColor(Color.parseColor(colorActive));

        if (!(tempagendaList.isEmpty())) {
            tempagendaList.clear();
        }
        if (agenda.getSessionDate().equals(date)) {
            holder.tvheading.setVisibility(View.GONE);
            date = agenda.getSessionDate();
            holder.recycler_agenda.setVisibility(View.GONE);
//            tempagendaList.add(agendaLists.get(position));
        } else {
            holder.tvheading.setVisibility(View.VISIBLE);
            holder.recycler_agenda.setVisibility(View.VISIBLE);
            date = agenda.getSessionDate();

            try {
                SimpleDateFormat originalFormat = new SimpleDateFormat("dd-mm-yyyy", Locale.UK);
                SimpleDateFormat targetFormat = new SimpleDateFormat(" dd\nMMM");
                Date date = originalFormat.parse(agenda.getSessionDate());
                String sessiondate = targetFormat.format(date);
                holder.tvheading.setText(sessiondate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        for (int i = 0; i < agendaLists.size(); i++) {
            if (agenda.getSessionDate().equals(newdate)) {
                newdate = agendaLists.get(i).getSessionDate();
                if (agenda.getSessionDate().equals(newdate)) {
//                newdate = agendaLists.get(i).getSessionDate();
                    tempagendaList.add(agendaLists.get(i));
                }

            } else {
                newdate = agendaLists.get(i).getSessionDate();
                if (agenda.getSessionDate().equals(newdate)) {
//                newdate = agendaLists.get(i).getSessionDate();
                    tempagendaList.add(agendaLists.get(i));
                }
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        holder.recycler_agenda.setLayoutManager(mLayoutManager);

        agendaDateWiseAdapter = new AgendaDateWiseAdapter(context, tempagendaList,this);
        agendaDateWiseAdapter.notifyDataSetChanged();
        holder.recycler_agenda.setAdapter(agendaDateWiseAdapter);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agendaistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvheading;
        /*nameTv, dateTv, descriptionTv, */
        public LinearLayout mainLL;
        public RecyclerView recycler_agenda;

        public MyViewHolder(View view) {
            super(view);
//            nameTv = (TextView) view.findViewById(R.id.nameTv);
//            dateTv = (TextView) view.findViewById(R.id.dateTv);
//            descriptionTv = (TextView) view.findViewById(R.id.descriptionTv);
            tvheading = view.findViewById(R.id.tvheading);
            recycler_agenda = view.findViewById(R.id.recycler_agenda);

            mainLL = view.findViewById(R.id.mainLL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(agendaLists.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface AgendaAdapterListner {
        void onContactSelected(AgendaList agendaList);
    }


}