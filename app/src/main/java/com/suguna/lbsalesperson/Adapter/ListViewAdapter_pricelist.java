package com.suguna.lbsalesperson.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suguna.lbsalesperson.POJO.SugPrice_POJO;
import com.suguna.lbsalesperson.PriceListActivity;
import com.suguna.lbsalesperson.R;

import java.util.List;

public class ListViewAdapter_pricelist extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<SugPrice_POJO> data;
    PriceListActivity fragment;
    //HashMap<String, String> resultp = new HashMap<String, String>();
    SugPrice_POJO resultp = new SugPrice_POJO();

    int count=0;
    public ListViewAdapter_pricelist(Context context,
                                    List<SugPrice_POJO> dailypricePOJOList, PriceListActivity dailyprice) {
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

        TextView name,price;


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.single_dailyprice,
                parent, false);
        // Get the position
        resultp = data.get(position);
        name=(TextView)itemView.findViewById(R.id.txt_name);
        price=(TextView)itemView.findViewById(R.id.txt_price);
        price.setText(resultp.getUNIT_PRICE());
        name.setText(resultp.getBRANCH_NAME());
        int count=0;
        count=position+1;
        if(count%2 == 0) {
            itemView.setBackgroundColor(Color.parseColor("#ededed"));
        }

        return itemView;
    }

    public void refreshEvents(List<SugPrice_POJO> arraylist) {
        // TODO Auto-generated method stub
        this.data.clear();
        this.data.addAll(arraylist);
        notifyDataSetChanged();
    }
}
