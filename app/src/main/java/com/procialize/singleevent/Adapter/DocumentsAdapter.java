package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.procialize.singleevent.GetterSetter.DocumentList;
import com.procialize.singleevent.R;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Naushad on 10/31/2017.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {

    private List<DocumentList> docLists;
    private Context context;
    private DocumentsAdapterListner listener;
    String MY_PREFS_NAME = "ProcializeInfo";
    String MY_PREFS_LOGIN = "ProcializeLogin";
    String colorActive;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        ImageView ic_rightarrow;



        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);
            ic_rightarrow = (ImageView) view.findViewById(R.id.ic_rightarrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(docLists.get(getAdapterPosition()));
                }
            });
        }
    }


    public DocumentsAdapter(Context context, List<DocumentList> docList,DocumentsAdapterListner listener) {
        this.docLists = docList;
        this.listener=listener;
        this.context=context;
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        colorActive = prefs.getString("colorActive","");

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedbackrow, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DocumentList survey = docLists.get(position);

        int colorInt = Color.parseColor(colorActive);

        ColorStateList csl = ColorStateList.valueOf(colorInt);
        Drawable drawable = DrawableCompat.wrap(holder.ic_rightarrow.getDrawable());
        DrawableCompat.setTintList(drawable, csl);
        holder.ic_rightarrow.setImageDrawable(drawable);

        holder.nameTv.setText(survey.getTitle());
    }

    @Override
    public int getItemCount() {
        return docLists.size();
    }

    public interface DocumentsAdapterListner {
        void onContactSelected(DocumentList document);
    }
}