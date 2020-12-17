package com.bayer.bayerreward.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bayer.bayerreward.ApiConstant.ApiConstant;
import com.bayer.bayerreward.CustomTools.PicassoTrustAll;
import com.bayer.bayerreward.GetterSetter.schemes_and_offers_list;
import com.bayer.bayerreward.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SchemeOfferAdapter extends RecyclerView.Adapter<SchemeOfferAdapter.MyViewHolder> {

    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;
    private List<schemes_and_offers_list> travelLists;
    private Context context;
    private SchemeOfferAdapter.SchemeOfferAdapterListner listener;

    public SchemeOfferAdapter(Context context, List<schemes_and_offers_list> travelList, SchemeOfferAdapter.SchemeOfferAdapterListner listener) {
        this.travelLists = travelList;
        this.listener = listener;
        this.context = context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive", "");

    }

    @Override
    public SchemeOfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scheme_offer_activity, parent, false);

        return new SchemeOfferAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SchemeOfferAdapter.MyViewHolder holder, int position) {
        final schemes_and_offers_list travel = travelLists.get(position);


        holder.txt_title.setText(travel.getTitle());
//        holder.txt_desc.setText(Html.fromHtml(travel.getDescription()));
        PicassoTrustAll.getInstance(context)
                .load(ApiConstant.imgURL + "uploads/schemes_and_offers/" + travel.getTile_images())
                .fit()
                .placeholder(R.drawable.folder_back)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return travelLists.size();
    }

    public interface SchemeOfferAdapterListner {
        void onContactSelected(schemes_and_offers_list travel);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_title, txt_desc;
        ImageView img, img_next;
        RelativeLayout relative;


        public MyViewHolder(View view) {
            super(view);
            txt_title = view.findViewById(R.id.txt_title);
            txt_desc = view.findViewById(R.id.txt_desc);
            img_next = view.findViewById(R.id.img_next);
            img = view.findViewById(R.id.img);
            relative = view.findViewById(R.id.relative);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(travelLists.get(getAdapterPosition()));
                }
            });
        }
    }
}
