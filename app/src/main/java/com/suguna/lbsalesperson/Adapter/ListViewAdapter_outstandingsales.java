package com.suguna.lbsalesperson.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suguna.lbsalesperson.OutstandingSalesHierarchy;
import com.suguna.lbsalesperson.OutstandingSalesHierarchy2;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;
import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.LBConstant;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ListViewAdapter_outstandingsales extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<SugOutStanding_POJO> data;
    OutstandingSalesHierarchy fragment;
    //HashMap<String, String> resultp = new HashMap<String, String>();
    SugOutStanding_POJO resultp = new SugOutStanding_POJO();

    int count = 0;

    public ListViewAdapter_outstandingsales(Context context,
                                            List<SugOutStanding_POJO> dailypricePOJOList, OutstandingSalesHierarchy dailyprice) {
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
        ImageView img_following;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_outstanding,
                parent, false);
        // Get the position
        resultp = data.get(position);
        name = (TextView) itemView.findViewById(R.id.txt_name);
        price = (TextView) itemView.findViewById(R.id.txt_price);
        following = (TextView) itemView.findViewById(R.id.txt_following);
        img_following = (ImageView) itemView.findViewById(R.id.img_following);
        credit = (TextView) itemView.findViewById(R.id.txt_credit);
        overdue = (TextView) itemView.findViewById(R.id.txt_overdue);
        txt_overduetommorrrow = (TextView) itemView.findViewById(R.id.txt_overduetommorrrow);
        txt_overduedayaftertomo= (TextView) itemView.findViewById(R.id.txt_overduedayaftertomo);

        if (LocalStorageSP.INSTANCE.getLoginUser(context).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_HEAD)) {
            credit.setVisibility(View.VISIBLE);
            credit.setText(resultp.getCREDIT_LIMIT());

        } else if (LocalStorageSP.INSTANCE.getLoginUser(context).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_EXECUTIVE)) {
            credit.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3.5f
            );
           // name.setLayoutParams(param);

        }
        if (!resultp.getDRILLDOWN().equalsIgnoreCase("Yes")) {
            if (Double.parseDouble(resultp.getOVER_DUE()) > 0) {
                name.setTextColor(context.getResources().getColor(R.color.app_red));
                price.setTextColor(context.getResources().getColor(R.color.app_red));
                following.setTextColor(context.getResources().getColor(R.color.app_red));
                credit.setTextColor(context.getResources().getColor(R.color.app_red));
                overdue.setTextColor(context.getResources().getColor(R.color.app_red));
                txt_overduetommorrrow.setTextColor(context.getResources().getColor(R.color.app_red));
                txt_overduedayaftertomo.setTextColor(context.getResources().getColor(R.color.app_red));
            } else {
                name.setTextColor(context.getResources().getColor(R.color.green_active));
                price.setTextColor(context.getResources().getColor(R.color.green_active));
                following.setTextColor(context.getResources().getColor(R.color.green_active));
                credit.setTextColor(context.getResources().getColor(R.color.green_active));
                overdue.setTextColor(context.getResources().getColor(R.color.green_active));
                txt_overduetommorrrow.setTextColor(context.getResources().getColor(R.color.green_active));
                txt_overduedayaftertomo.setTextColor(context.getResources().getColor(R.color.green_active));
            }
        }

        DecimalFormat df = new DecimalFormat("####.##");
     //   df.setRoundingMode(RoundingMode.UNNECESSARY);

        price.setText(String.valueOf(df.format(Float.parseFloat(resultp.getPRICE()))));
        overdue.setText(resultp.getOVER_DUE());
        name.setText(resultp.getNAME());
        following.setText(resultp.getFOLLOWING());
        txt_overduetommorrrow.setText(resultp.getOVERDUE_TOMORROW());
        txt_overduedayaftertomo.setText(resultp.getOVERDUE_AFTERTOMORROW());
        int count = 0;
        count = position + 1;
        if (count % 2 == 0) {
            itemView.setBackgroundColor(Color.parseColor("#ededed"));
        }
        if (resultp.getDRILLDOWN().equalsIgnoreCase("Yes")) {
            img_following.setVisibility(View.VISIBLE);
            following.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = data.get(position);
                if (resultp.getDRILLDOWN().equalsIgnoreCase("Yes")) {
                    Intent i = new Intent(context, OutstandingSalesHierarchy2.class);
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
