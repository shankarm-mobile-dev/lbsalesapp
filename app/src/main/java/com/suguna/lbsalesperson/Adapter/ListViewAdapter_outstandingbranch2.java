package com.suguna.lbsalesperson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suguna.lbsalesperson.OutstandingBranchHierarchy;
import com.suguna.lbsalesperson.OutstandingBranchHierarchy2;
import com.suguna.lbsalesperson.OutstandingSalesHierarchy2;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;
import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.LBConstant;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;

import java.util.List;

public class ListViewAdapter_outstandingbranch2 extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<SugOutStanding_POJO> data;
    OutstandingBranchHierarchy2 fragment;
    //HashMap<String, String> resultp = new HashMap<String, String>();
    SugOutStanding_POJO resultp = new SugOutStanding_POJO();

    int count = 0;

    public ListViewAdapter_outstandingbranch2(Context context,
                                              List<SugOutStanding_POJO> dailypricePOJOList, OutstandingBranchHierarchy2 dailyprice) {
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

        TextView name, price, following, credit, overdue,txt_overduetommorrrow,txt_overduedayaftertomo;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_outstanding,
                parent, false);
        // Get the position
        resultp = data.get(position);
        credit = (TextView) itemView.findViewById(R.id.txt_credit);
        overdue = (TextView) itemView.findViewById(R.id.txt_overdue);
        name = (TextView) itemView.findViewById(R.id.txt_name);
        price = (TextView) itemView.findViewById(R.id.txt_price);
        following = (TextView) itemView.findViewById(R.id.txt_following);
        txt_overduetommorrrow = (TextView) itemView.findViewById(R.id.txt_overduetommorrrow);
        txt_overduedayaftertomo= (TextView) itemView.findViewById(R.id.txt_overduedayaftertomo);

        if (LocalStorageSP.INSTANCE.getLoginUser(context).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_HEAD)) {
            credit.setVisibility(View.VISIBLE);
            overdue.setVisibility(View.VISIBLE);
            txt_overduetommorrrow.setVisibility(View.VISIBLE);
            txt_overduedayaftertomo.setVisibility(View.VISIBLE);
            credit.setText(resultp.getCREDIT_LIMIT());
            overdue.setText(resultp.getOVER_DUE());
            txt_overduetommorrrow.setText(resultp.getOVERDUE_TOMORROW());
                    txt_overduedayaftertomo.setText(resultp.getOVERDUE_AFTERTOMORROW());
        } else if (LocalStorageSP.INSTANCE.getLoginUser(context).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_EXECUTIVE)) {
            credit.setVisibility(View.GONE);
            overdue.setVisibility(View.GONE);
            txt_overduetommorrrow.setVisibility(View.GONE);
            txt_overduedayaftertomo.setVisibility(View.GONE);
        }
        overdue.setText(resultp.getOVER_DUE());
        price.setText(resultp.getPRICE());
        name.setText(resultp.getNAME());
        following.setText(resultp.getFOLLOWING());
        txt_overduetommorrrow.setText(resultp.getOVERDUE_TOMORROW());
        txt_overduedayaftertomo.setText(resultp.getOVERDUE_AFTERTOMORROW());
        int count = 0;
        count = position + 1;
        if (count % 2 == 0) {
            itemView.setBackgroundColor(Color.parseColor("#ededed"));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                if (resultp.getDRILLDOWN().equalsIgnoreCase("Yes")) {
                    Intent i = new Intent(context, OutstandingBranchHierarchy2.class);
                    i.putExtra("code", resultp.getCODE());
                    i.putExtra("name", resultp.getNAME());

                    context.startActivity(i);
                }
            }
        });

        return itemView;
    }

    public void refreshEvents(List<SugOutStanding_POJO> arraylist) {
        // TODO Auto-generated method stub
        this.data.clear();
        this.data.addAll(arraylist);
        notifyDataSetChanged();
    }
}


