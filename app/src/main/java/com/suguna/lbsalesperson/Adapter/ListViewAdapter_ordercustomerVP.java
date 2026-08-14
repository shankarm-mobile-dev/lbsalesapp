package com.suguna.lbsalesperson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suguna.lbsalesperson.OrderCustomerHierarchy;
import com.suguna.lbsalesperson.OrderCustomerHierarchy_VP;
import com.suguna.lbsalesperson.OutstandingSalesHierarchy2;
import com.suguna.lbsalesperson.POJO.SugOrderSummary_POJO;
import com.suguna.lbsalesperson.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter_ordercustomerVP extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<SugOrderSummary_POJO> data;
    OrderCustomerHierarchy_VP fragment;
    //HashMap<String, String> resultp = new HashMap<String, String>();
    SugOrderSummary_POJO resultp = new SugOrderSummary_POJO();

    int count=0;
    public ListViewAdapter_ordercustomerVP(Context context,
                                         List<SugOrderSummary_POJO> dailypricePOJOList, OrderCustomerHierarchy_VP dailyprice) {
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

        TextView name,today,lstwk,lst5wk,name1,todayavg,lstwkavg,lst5wkavg,txt_club;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_summary_vp,
                parent, false);
        // Get the position
        resultp = data.get(position);
        name=(TextView)itemView.findViewById(R.id.txt_name);
        name1=(TextView)itemView.findViewById(R.id.txt_name1);
        today=(TextView)itemView.findViewById(R.id.txt_today);
        lstwk=(TextView)itemView.findViewById(R.id.txt_lstwk);
        lst5wk=(TextView)itemView.findViewById(R.id.txt_lst5wk);
        todayavg=(TextView)itemView.findViewById(R.id.txt_todayavg);
        lstwkavg=(TextView)itemView.findViewById(R.id.txt_lstwkavg);
        lst5wkavg=(TextView)itemView.findViewById(R.id.txt_lst5wkavg);
        txt_club=(TextView)itemView.findViewById(R.id.txt_club);
        today.setText(resultp.getTODAY());
        name.setText(resultp.getNAME());
        lstwk.setText(resultp.getLASTWK());
        txt_club.setText(resultp.getBRANCHNAME());

        if(resultp.getBRANCHNAME().equals("Diamond"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_diamond)));
        }
        else if(resultp.getBRANCHNAME().equals("Gold"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_gold)));
        }
        else if(resultp.getBRANCHNAME().equals("Silver"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_silver)));
        }
        else if(resultp.getBRANCHNAME().equals("Silver Plus"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_silver)));
        }
        else if(resultp.getBRANCHNAME().equals("Bronze"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_bronze)));
        }
        else if(resultp.getBRANCHNAME().equals("Non Club Member"))
        {
            txt_club.setBackground(context.getResources().getDrawable((R.drawable.list_background_nonclub)));
        }

        try {
            DecimalFormat df = new DecimalFormat("####");
            df.setRoundingMode(RoundingMode.UP);
            today.setText(String.valueOf(df.format(Float.parseFloat(resultp.getTODAY()))));
            lstwk.setText(String.valueOf(df.format(Float.parseFloat(resultp.getLASTWK()))));
            lst5wk.setText(String.valueOf(df.format(Float.parseFloat(resultp.getLAST5WKAVG()))));
        }
        catch (Exception e)
        {

        }
        //lst5wk.setText(resultp.getLAST5WK());


       /* if(resultp.getBRANCHNAME().length()>0 && !resultp.getBRANCHNAME().equals("-"))
        {
            name1.setText(resultp.getBRANCHNAME());
           // todayavg.setText(resultp.getTODAYAVG());
           // lstwkavg.setText(resultp.getLASTWKAVG());
          //  lst5wkavg.setText(resultp.getLAST5WKAVG());
            name1.setVisibility(View.VISIBLE);
        }
        else
        {

        }*/
        int count=0;
        count=position+1;
        if(count%2 == 0) {
            itemView.setBackgroundColor(Color.parseColor("#ededed"));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                /*if(resultp.getDRILLDOWN().equalsIgnoreCase("Yes")) {
                    Intent i = new Intent(context, OutstandingSalesHierarchy2.class);
                    i.putExtra("code", resultp.getCODE());
                    i.putExtra("name", resultp.getNAME());

                    context.startActivity(i);
                }*/
            }
        });

        return itemView;
    }

    public void refreshEvents(List<SugOrderSummary_POJO> arraylist) {
        // TODO Auto-generated method stub
        this.data.clear();
        this.data.addAll(arraylist);
        notifyDataSetChanged();
    }
}
