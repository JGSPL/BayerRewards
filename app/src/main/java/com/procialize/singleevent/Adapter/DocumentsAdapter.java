package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.procialize.singleevent.GetterSetter.DocumentList;
import com.procialize.singleevent.R;
import java.util.List;

/**
 * Created by Naushad on 10/31/2017.
 */

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.MyViewHolder> {

    private List<DocumentList> docLists;
    private Context context;
    private DocumentsAdapterListner listener;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;

        public MyViewHolder(View view) {
            super(view);
            nameTv = (TextView) view.findViewById(R.id.nameTv);

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