package com.suguna.lbsalesperson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity;
import com.suguna.lbsalesperson.feedback.activity.FeedbackView;
import com.suguna.lbsalesperson.POJO.SugFeedback_POJO;
import com.suguna.lbsalesperson.R;

import java.util.List;

public class ListViewAdapter_feedback extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<SugFeedback_POJO> data;
    FeedBackActivity fragment;
    //HashMap<String, String> resultp = new HashMap<String, String>();
    SugFeedback_POJO resultp = new SugFeedback_POJO();


    public ListViewAdapter_feedback(Context context,
                                    List<SugFeedback_POJO> dailypricePOJOList, FeedBackActivity dailyprice) {
        this.context = context;
        data = dailypricePOJOList;
        this.fragment = dailyprice;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables

        TextView date, des, status;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_feedback, parent, false);
        // Get the position
        resultp = data.get(position);
        date = (TextView) itemView.findViewById(R.id.txt_date);
        des = (TextView) itemView.findViewById(R.id.txt_des);
        status = (TextView) itemView.findViewById(R.id.txt_status);

        date.setText(resultp.getFEEDBACK_DATE());
        des.setText(resultp.getCUSTOMER_NAME());

        status.setText(resultp.getRESPONSE_STATUS());
        int count = 0;
        count = position + 1;
        if (count % 2 == 0) {
            itemView.setBackgroundColor(Color.parseColor("#ededed"));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                //  if (!resultp.getRESPONSE_STATUS().equalsIgnoreCase("Completed")) {
                Intent i = new Intent(context, FeedbackView.class);
                i.putExtra("id", resultp.getFEEDBACK_ID());

                context.startActivity(i);
                //   }


            }
        });
        return itemView;
    }

    public void refreshEvents(List<SugFeedback_POJO> arraylist) {
        // TODO Auto-generated method stub
        this.data.clear();
        this.data.addAll(arraylist);
        notifyDataSetChanged();
    }
}
