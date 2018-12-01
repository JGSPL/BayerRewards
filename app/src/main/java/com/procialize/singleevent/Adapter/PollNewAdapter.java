package com.procialize.singleevent.Adapter;

import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.procialize.singleevent.GetterSetter.LivePollList;
import com.procialize.singleevent.GetterSetter.LivePollOptionList;
import com.procialize.singleevent.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

public class PollNewAdapter extends BaseAdapter {
    private List<LivePollList> pollLists;
    private List<LivePollOptionList> optionLists;
    private Context context;
    private PollAdapterListner listener;
    private LayoutInflater inflater;

    public PollNewAdapter(Context context, List<LivePollList> pollLists, List<LivePollOptionList> optionLists, PollAdapterListner listener) {
        this.pollLists = pollLists;
        this.optionLists = optionLists;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pollLists.size();
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
        final LivePollList pollList = pollLists.get(position);
        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.poll_row, null);

            holder = new ViewHolder();

            Display dispDefault = ((WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay();
            int totalwidth = dispDefault.getWidth();
            holder.nameTv = convertView.findViewById(R.id.nameTv);
            holder.imageIv = convertView.findViewById(R.id.imageIv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(StringEscapeUtils.unescapeJava(pollList.getQuestion()));
        holder.nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactSelected(pollLists.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        public TextView nameTv;
        public ImageView imageIv;
    }

    public interface PollAdapterListner {
        void onContactSelected(LivePollList pollList);
    }
}