package com.procialize.vivo_app.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.procialize.vivo_app.GetterSetter.QuizFolder;
import com.procialize.vivo_app.R;

import java.util.List;

public class QuizFolderAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<QuizFolder> quizList;


    public QuizFolderAdapter(Activity activity, List<QuizFolder> quizList) {
        this.activity = activity;
        this.quizList = quizList;
    }

    @Override
    public int getCount() {
        return quizList.size();
    }

    @Override
    public Object getItem(int location) {
        return quizList.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public QuizFolder getQuestionIdFromList(int position) {
        return quizList.get(position);
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.quiz_row, null);

            holder = new ViewHolder();

            // holder.video_preview_img = (ImageView) convertView
            // .findViewById(R.id.video_preview_img);

            holder.quiz_title_txt = (TextView) convertView
                    .findViewById(R.id.video_title_txt);

            Typeface typeFace = Typeface.createFromAsset(activity.getAssets(),
                    "Roboto-Light.ttf");
            holder.quiz_title_txt.setTypeface(typeFace);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.quiz_title_txt.setText(quizList.get(position).getFolder_name());
        return convertView;
    }

    static class ViewHolder {
        TextView quiz_title_txt;

    }

}
