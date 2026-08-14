package com.suguna.lbsalesperson.priceallocation.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.DecimalDigitsInputFilter;
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet;

import org.apache.commons.lang3.math.NumberUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

@SuppressLint("InflateParams")
public class ConcessionPriceAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<lbpricedet> Itemlist;
	Boolean mEditable = false;

	public ConcessionPriceAdapter(Context context, int resource,
                                  ArrayList<lbpricedet> item, Boolean Editable) {
		mEditable = Editable;
		mContext = context;
		this.Itemlist = item;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Itemlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Itemlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext.getApplicationContext());
			convertView = inflater.inflate(R.layout.lbconcessionprice_list_item, null);
			holder.tvbranchname = (TextView) convertView
					.findViewById(R.id.tvbranchname);
			holder.tvrate = (TextView) convertView.findViewById(R.id.tvrate);
			holder.edtrate = (EditText) convertView.findViewById(R.id.edtrate);
			holder.tvfinalprice= (TextView) convertView.findViewById(R.id.tvfinalprice);
			
			
			holder.lydetail = (LinearLayout) convertView.findViewById(R.id.lydetail);
			
			holder.broilerhdr = (TextView) convertView
					.findViewById(R.id.txtBroilerHdr);
			holder.retailhdr = (TextView) convertView
					.findViewById(R.id.txtRetailHdr);
			
			convertView.setTag(holder);
			
			holder.edtrate.setTag("0");

		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		
		
		holder.broilerhdr.setVisibility(View.GONE);
		holder.retailhdr.setVisibility(View.GONE);
		holder.lydetail.setVisibility(View.GONE);

		holder.ref = position;
		holder.edtrate
				.setFilters(new InputFilter[] { new DecimalDigitsInputFilter(2) });

		
		if (Itemlist.get(position).getID()==1) {
			holder.broilerhdr.setVisibility(View.VISIBLE);
			holder.retailhdr.setVisibility(View.GONE);
			holder.lydetail.setVisibility(View.GONE);
		}
		else if (Itemlist.get(position).getID()==3) {
			holder.broilerhdr.setVisibility(View.GONE);
			holder.retailhdr.setVisibility(View.VISIBLE);
			holder.lydetail.setVisibility(View.GONE);
		}
		else
		{
			holder.lydetail.setVisibility(View.VISIBLE);
		}
		
		
		if (Itemlist.get(position).getID()==4) {
			holder.tvbranchname.setText(Itemlist.get(position)
					.getLOCATION_CODE());	
		}
		else
		{
			holder.tvbranchname.setText(Itemlist.get(position).getBRANCH_NAME());
		}
		
		//holder.tvbranchname.setText(Itemlist.get(position).getBRANCH_NAME());
		holder.edtrate.setText(Itemlist.get(position).getCONCESSIONRATE());
		holder.tvrate.setText(Itemlist.get(position).getRATE());

		if (mEditable == false) {
//			holder.edtrate.setVisibility(View.GONE);
//			holder.tvrate.setVisibility(View.VISIBLE);
		} else {
			if (Itemlist.get(position).getCONCESSIONSTATUS().equals("Y")) {
//				holder.edtrate.setVisibility(View.GONE);
//				holder.tvrate.setVisibility(View.VISIBLE);
				holder.edtrate.setEnabled(false);
			} else {
//				holder.edtrate.setVisibility(View.VISIBLE);
//				holder.tvrate.setVisibility(View.GONE);
				holder.edtrate.setEnabled(true);
			}
		}

		String dataRate = Itemlist.get(position).getCONCESSIONRATE();

		if (dataRate.length() > 0) {

			double rateValue=0;
			
			if(NumberUtils.isNumber(Itemlist.get(position).getCONCESSIONRATE()))
				rateValue=Double.parseDouble(Itemlist.get(position).getCONCESSIONRATE());
			
			double rateValue1 = Double.parseDouble(Itemlist.get(position)
					.getRATE());
			
			double finalrate=rateValue1-rateValue;
			
			if (rateValue > 0.0) {
				NumberFormat formatter = NumberFormat
						.getCurrencyInstance(new Locale("en", "IN"));
				formatter.setMaximumFractionDigits(2);
				String moneyString = formatter.format(rateValue);
				//holder.edtrate.setText(moneyString);
			} else {
				holder.edtrate.setText("0");
			}
			
			holder.tvfinalprice.setText(String.valueOf(finalrate));

		} else {
			holder.edtrate.setText("");
		}

		holder.edtrate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				holder.edtrate.setFocusable(true);
				holder.edtrate.setFocusableInTouchMode(true);
				holder.edtrate.setClickable(true);
				holder.edtrate.requestFocus();

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Itemlist.get(holder.ref).setCONCESSIONRATE(arg0.toString());
				
				double rateValue=0;
				
				if(NumberUtils.isNumber(arg0.toString()))
				{
					rateValue = Double.parseDouble(arg0.toString());
				}
				
				double rateValue1 = Double.parseDouble(Itemlist.get(holder.ref)
						.getRATE());
				
				if(rateValue>rateValue1)
				{
					
					
					//holder.tvfinalprice.setText(String.valueOf(finalrate));
					
//					if(holder.edtrate.getTag()!=arg0.toString())
//					{
//						holder.edtrate.setTag(rateValue);
//						
//						new AlertDialog.Builder(mContext)
//						.setTitle("Warning")
//						.setMessage(
//								"Please Enter the Price below Rs" + rateValue +"." )
//						.setPositiveButton(
//								android.R.string.yes,
//								new DialogInterface.OnClickListener() {
//									public void onClick(
//											DialogInterface dialog,
//											int which) {
//										// continue with delete
//									}
//								})
//	
//						.setIcon(
//								android.R.drawable.ic_dialog_alert)
//						.show();
//					}
				}
				else
				{
//				
					double finalrate=rateValue1-rateValue;
//				
					holder.tvfinalprice.setText(String.valueOf(finalrate));
					
				}
				
			}
		});

		holder.edtrate.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// Toast.makeText(
				// mContext,
				// "Booting - "
				// + Itemlist.get(holder.ref).getBRANCH_NAME(),
				// 1950).show();

				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(holder.edtrate,
						InputMethodManager.SHOW_IMPLICIT);
				return false;
			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView tvrate, tvbranchname,tvfinalprice;
		TextView broilerhdr, retailhdr;
		EditText edtrate;
		LinearLayout lydetail;
		int ref;
	}
	
}
