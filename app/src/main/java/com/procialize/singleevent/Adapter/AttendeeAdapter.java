package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.procialize.singleevent.Activity.AttendeeDetailActivity;
import com.procialize.singleevent.ApiConstant.ApiConstant;
import com.procialize.singleevent.Fragments.AttendeeFragment;
import com.procialize.singleevent.GetterSetter.AttendeeList;
import com.procialize.singleevent.GetterSetter.EventSettingList;
import com.procialize.singleevent.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Naushad on 10/31/2017.
 */

public class AttendeeAdapter extends RecyclerView.Adapter<AttendeeAdapter.MyViewHolder> implements Filterable {

    private List<AttendeeList> attendeeLists;
    private Context context;
    private List<AttendeeList> attendeeListFiltered;
    private AttendeeAdapterListner listener;
    List<EventSettingList> eventSettingLists;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv, locationTv, designationTv;
        public ImageView profileIv;
        public LinearLayout mainLL;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            nameTv = view.findViewById(R.id.nameTv);
            locationTv = view.findViewById(R.id.locationTv);
            designationTv = view.findViewById(R.id.designationTv);

            profileIv = view.findViewById(R.id.profileIV);

            mainLL = view.findViewById(R.id.mainLL);

            progressBar = view.findViewById(R.id.progressBar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(attendeeListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public AttendeeAdapter(Context context, List<AttendeeList> attendeeLists, AttendeeAdapterListner listener) {
        this.attendeeLists = attendeeLists;
        this.attendeeListFiltered = attendeeLists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendeelistingrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AttendeeList attendee = attendeeListFiltered.get(position);
        if (attendee.getFirstName().equalsIgnoreCase("N A")) {
            holder.nameTv.setText("");
        } else {
            holder.nameTv.setText(attendee.getFirstName() + " " + attendee.getLastName());
        }
        if(attendee.getCity().equalsIgnoreCase("N A")){
            holder.locationTv.setText("");
        }else {
            holder.locationTv.setText(attendee.getCity());
        }

        if(attendee.getDesignation().equalsIgnoreCase("N A")){
            holder.designationTv.setText("");
        }else {
            holder.designationTv.setText(attendee.getDesignation());
        }



        if (attendee.getProfilePic() != null) {


            Glide.with(context).load(ApiConstant.profilepic + attendee.getProfilePic())
                    .apply(RequestOptions.skipMemoryCacheOf(true))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.profileIv.setImageResource(R.drawable.profilepic_placeholder);
                    return true;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.profileIv);

        } else {
            holder.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return attendeeListFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    attendeeListFiltered = attendeeLists;
                } else {
                    List<AttendeeList> filteredList = new ArrayList<>();
                    for (AttendeeList row : attendeeLists) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        String name = row.getFirstName().toLowerCase() + " " + row.getLastName().toLowerCase();

                        if (name.contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    attendeeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = attendeeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                attendeeListFiltered = (ArrayList<AttendeeList>) filterResults.values;

                if (attendeeListFiltered.size() == 0) {
//                    Toast.makeText(context, "No Attendee Found", Toast.LENGTH_SHORT).show();
                }
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public interface AttendeeAdapterListner {
        void onContactSelected(AttendeeList attendee);
    }
}