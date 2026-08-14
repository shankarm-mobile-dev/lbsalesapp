package com.suguna.lbsalesperson.diffpriceallocation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.suguna.lbsalesperson.Adapter.ListViewAdapter_ordersummarybranch2;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.LoginActivity;
import com.suguna.lbsalesperson.OrderBranchHierarchy2;
import com.suguna.lbsalesperson.POJO.ResultPOJO;
import com.suguna.lbsalesperson.POJO.SugOrderSummary_POJO;
import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.CustomDate_Edittext_1;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;
import com.suguna.lbsalesperson.Utils.NetworkUtil;
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener;
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel;
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel;
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity;
import com.truelife.api.AppServices;
import com.truelife.http.Response;
import com.truelife.http.ResponseListener;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class BottomSheetDialog extends BottomSheetDialogFragment  {
    EditText productlist,fromwt,towt;
    TextView todate,fromdate;
    PopupWindow popup_question_list;
    ImageView img_close;
    LinearLayout saveButton;
    ArrayList<DiffnameModel> mDiffnameList = new ArrayList<DiffnameModel>();
    ArrayList<DiffdeclareModel> mDiffdeclareList = new ArrayList<DiffdeclareModel>();
    CustomDate_Edittext_1 custdate;
    public static final String Name = "name";
    public static final String Password = "pass";
    public static final String SERVERDATE = "serverdate";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    Sug_lblogin lbloginDAO;
    DatabaseUpDown databaseUpDown;
    NetworkUtil netutil;
    String str_product,str_fromwt,str_towt,str_fromdate,str_todate;
    List<ResultPOJO>  ResultPOJO_POJOlist;
    TextView txtsaveButton;
    private ConsumptionValidator feedConsWatc;
    String date="";
    Calendar c;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,
                container, false);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        lbloginDAO = new Sug_lblogin(getActivity());
        databaseUpDown = new DatabaseUpDown(getActivity());
        netutil=new NetworkUtil(getActivity());
        productlist=(EditText) v.findViewById(R.id.productlist);
        todate=(TextView) v.findViewById(R.id.todate);
        fromdate=(TextView) v.findViewById(R.id.fromdate);
        fromwt=(EditText) v.findViewById(R.id.fromwt);
        towt=(EditText) v.findViewById(R.id.towt);
        img_close=(ImageView)v.findViewById(R.id.img_close);
        saveButton=(LinearLayout) v.findViewById(R.id.saveButton);
        txtsaveButton=(TextView) v.findViewById(R.id.txtsaveButton);
        mDiffnameList= (ArrayList<DiffnameModel>) DiffPriceallocation.mDiffnameList;
        mDiffdeclareList= (ArrayList<DiffdeclareModel>) DiffPriceallocation.mDiffdeclareList;
        feedConsWatc = new ConsumptionValidator(fromwt, 1, 3);
        fromwt.addTextChangedListener(feedConsWatc);
        feedConsWatc = new ConsumptionValidator(towt, 1, 3);
        towt.addTextChangedListener(feedConsWatc);
       // todate.setEnabled(false);
       // fromdate.setEnabled(false);
       // todate.setKeyListener(null);
       // fromdate.setKeyListener(null);

            //d = sdf.parse(String.valueOf(date));
            String date_server = sharedpreferences.getString(SERVERDATE, "");
            c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            Date d = null;
            try {
                //d = sdf.parse(String.valueOf(date));

                c.setTime(sdf.parse(String.valueOf(date_server)));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //String CreationDate = df.format(d);
            date = df.format(c.getTime());


        if(!DiffPriceallocation.cur_position.equals("0"))
        {
            //Log.e("ss",DiffPriceallocation.cur_position-1);
            DiffdeclareModel model=mDiffdeclareList.get(Integer.parseInt(DiffPriceallocation.cur_position)-1);
            productlist.setText(model.getPROD_NAME());
            productlist.setTag(model.getPRODUCT_CATEGORY());
            fromdate.setText(model.getFROM_DATE());
            fromwt.setText(model.getLOW_WT());
            towt.setText(model.getHIGH_WT());

            productlist.setFocusable(false);
            fromdate.setFocusable(false);
            fromwt.setFocusable(false);
            towt.setFocusable(false);

            productlist.setClickable(false);
            fromdate.setClickable(false);
            fromwt.setClickable(false);
            towt.setClickable(false);



            txtsaveButton.setText("Update");
        }
        productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtsaveButton.getText().toString().equals("Update")) {
                    birdsextype_drop_down();
                }
            }
        });
        fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String date=sdf.format(c.getTime());*/
                if(!txtsaveButton.getText().toString().equals("Update"))
                {

                int days=0;
                SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
                String strDate=sdf1.format(c.getTime());
                days=isValiddayDiff(date,get_max_date(strDate));
                Log.e("day",String.valueOf(days));
                custdate = new CustomDate_Edittext_1(getActivity(), fromdate, "Date ", "From Date", days, get_max_date(DiffPriceallocation.month));
            }}
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                String date=sdf.format(c.getTime());*/

                if(DiffPriceallocation.cur_position.equals("0")) {
                    int days=0;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
                    String strDate=sdf1.format(c.getTime());
                    days=isValiddayDiff(date,get_max_date(strDate));
                    Log.e("day",String.valueOf(days));
                    custdate = new CustomDate_Edittext_1(getActivity(), todate, "Date ", "To Date", days, get_max_date(DiffPriceallocation.month));
                }
                else
                {
                    int days=0;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
                    String strDate=sdf1.format(c.getTime());
                    days=isValiddayDiff(date,get_max_date(strDate));
                    Log.e("day",String.valueOf(days));
                    custdate = new CustomDate_Edittext_1(getActivity(), todate, "Date ", "To Date", days, get_max_date(strDate));
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // birdsextype_drop_down();
                dismiss();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // birdsextype_drop_down();
                str_product=productlist.getText().toString().trim();
                str_fromwt=fromwt.getText().toString().trim();
                str_towt=towt.getText().toString().trim();
                str_fromdate=fromdate.getText().toString().trim();
                str_todate=todate.getText().toString().trim();
               if(str_product.equals("Select"))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Select Product Category ", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(str_fromwt.equals(""))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Enter From Bird Weight ", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(Float.parseFloat(str_fromwt)<0 || Float.parseFloat(str_fromwt)>3)
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Enter From Bird Weight less then 3KG", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(str_towt.equals(""))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Enter To Bird Weight ", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(Float.parseFloat(str_towt)<0 || Float.parseFloat(str_towt)>3)
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Enter To Bird Weight less then 3KG", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(Float.parseFloat(str_towt)<=Float.parseFloat(str_fromwt))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Entered From Bird Weight is greater then To Bird", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(validatewgt(Float.parseFloat(str_fromwt),Float.parseFloat(str_towt))){
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Minimum 99 grams gap is required", Toast.LENGTH_SHORT,
                           ICaster.notify); ///change 299
               }
               else if(str_fromdate.equals("Select date"))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Select From date ", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(str_todate.equals("Select date"))
               {
                   ICaster.Toast_msg_bottom(getActivity(),
                           "Select To date ", Toast.LENGTH_SHORT,
                           ICaster.notify);
               }
               else if(!isValidTimeDiff(str_fromdate,str_todate))
                {
                    ICaster.Toast_msg_bottom(getActivity(),
                            "From date is greater than To date ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                }
               else
               {
                   if(DiffPriceallocation.cur_position.equals("0")) {
                       new savediffpricedeclare().execute();
                   }
                   else
                   {
                       DiffdeclareModel model=mDiffdeclareList.get(Integer.parseInt(DiffPriceallocation.cur_position)-1);
                       if(!isValidTimeDiff(str_todate,model.getTO_DATE()))
                       {
                           ICaster.Toast_msg_bottom(getActivity(),
                                   "Selected date is greater than present effective To date ", Toast.LENGTH_SHORT,
                                   ICaster.notify);
                       }
                       else
                       {
                           new updatediffpricedeclare().execute();
                       }
                   }
               }
            }
        });

        return v;
    }
    private boolean isValidTimeDiff(String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        // Date d1 = new Date();
        // Date d2 = new Date();
        String d1 = "";
        String d2 = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date d;
        Calendar thatDay = Calendar.getInstance();
        try {
            d = sdf.parse(startTime);

            startTime = output.format(d);
            d = sdf.parse(endTime);
            endTime = output.format(d);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("Start End Times :: ", "StartTime :" + startTime
                + " \nEndTime :" + endTime);
        try {
            // d1 = format.parse(startTime);
            // d2 = format.parse(endTime);
            //
            // long diff = d2.getTime() - d1.getTime();
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date date1 = format2.parse(startTime);
            Date date2 = format2.parse(endTime);
            long difference = date2.getTime() - date1.getTime();
            //d1 = convertTo24Hour(startTime);
            //d2 = convertTo24Hour(endTime);
            //double diff = Double.parseDouble(d2) - Double.parseDouble(d1);
            Log.e("start", String.valueOf(date1.getTime()));
            Log.e("end", String.valueOf(date2.getTime()));
            Log.e("diff", String.valueOf(difference));

            if (difference >= 0) {
                //Toast.makeText(getApplicationContext(),String.valueOf(date1.getTime())+" / "+ String.valueOf(date2.getTime())+" "+ String.valueOf(difference), Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    //Get last date in month
    public String get_max_date(String date)
    {
        String formatdate="";
        try
        {
            SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy",Locale.ENGLISH);
          //  Calendar cal = Calendar.getInstance();
            c.setTime(monthDate.parse(date));
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
            formatdate = format.format(c.getTime());
        } catch (ParseException e)  {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formatdate;
    }
    public boolean validatewgt(float fromwt,float towt)
    {
        boolean result=false;
        fromwt=fromwt*1000;
        towt=towt*1000;
        if((towt-fromwt)<99)  ///change 299
        {
            result=true;
        }
        return result;
    }
    private void birdsextype_drop_down() {
        LayoutInflater layout = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vie = layout.inflate(R.layout.bodycondition_list, null);
        popup_question_list = new PopupWindow(vie, productlist.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popup_question_list.setOutsideTouchable(true);
        popup_question_list.setBackgroundDrawable(getActivity()
                .getResources().getDrawable(R.drawable.popup));
        popup_question_list.setFocusable(true);
        popup_question_list.showAsDropDown(productlist);
        ListView list = (ListView) vie.findViewById(R.id.list);
        ArrayList<DropDown_list> arrayList_division = new ArrayList<>();

        for (int i=0;i<mDiffnameList.size();i++){
            DropDown_list a = new DropDown_list();
            DiffnameModel model=new DiffnameModel();
            model=mDiffnameList.get(i);
            a.setDropdown_name(model.getDESCRIPTION());
            a.setDropdown_id(model.getTAG());
            arrayList_division.add(a);
        }


        Dropdown_list_adapter adapter = new Dropdown_list_adapter(
                getActivity(), arrayList_division);
        list.setAdapter(adapter);

    }



    private class Dropdown_list_adapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        ArrayList<DropDown_list> arrayList;

        public Dropdown_list_adapter(Context context,
                                     ArrayList<DropDown_list> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(
                    R.layout.body_condition_drop_down, null);
            final TextView name_div = (TextView) rowView
                    .findViewById(R.id.textView0);
            DropDown_list div = arrayList.get(position);
            name_div.setText(div.getDropdown_name());
            name_div.setTag(div.getDropdown_id());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productlist.setText(name_div.getText().toString());
                    productlist.setTag(name_div.getTag().toString());
                    popup_question_list.dismiss();

                }
            });

            return rowView;
        }
    }
    public class savediffpricedeclare extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean download = false;
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            ResultPOJO_POJOlist = new ArrayList<ResultPOJO>();
            ResultPOJO_POJOlist=databaseUpDown.savediffpricedeclare_server(USERID,
                    TKEY,custnumber,productlist.getTag().toString(),str_fromwt,str_towt,str_fromdate,str_todate);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(ResultPOJO_POJOlist.size()>0)
            {
                ResultPOJO pojo=ResultPOJO_POJOlist.get(0);
                if(pojo.getResponse().equals("true"))
                {
                    ICaster.Toast_msg_bottom(getActivity(),
                            pojo.getMessage(), Toast.LENGTH_SHORT,
                            ICaster.notify);
                   // DiffPriceallocation diff=new DiffPriceallocation();
                   // diff.getDiffpricenameList_resume();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    dismiss();
                }
                else
                {
                    ICaster.Toast_msg_bottom(getActivity(),
                            pojo.getMessage(), Toast.LENGTH_SHORT,
                            ICaster.notify);
                }
            }
            else
            {
                ICaster.Toast_msg_bottom(getActivity(),
                        "Error is saving process", Toast.LENGTH_SHORT,
                        ICaster.notify);
            }

        }
    }
    public class updatediffpricedeclare extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(getActivity())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean download = false;
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            ResultPOJO_POJOlist = new ArrayList<ResultPOJO>();
            ResultPOJO_POJOlist=databaseUpDown.updatediffpricedeclare_server(USERID,
                    TKEY,custnumber,productlist.getTag().toString(),str_fromwt,str_towt,str_fromdate,str_todate);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(ResultPOJO_POJOlist.size()>0)
            {
                ResultPOJO pojo=ResultPOJO_POJOlist.get(0);
                if(pojo.getResponse().equals("true"))
                {
                    ICaster.Toast_msg_bottom(getActivity(),
                            pojo.getMessage(), Toast.LENGTH_SHORT,
                            ICaster.notify);
                    // DiffPriceallocation diff=new DiffPriceallocation();
                    // diff.getDiffpricenameList_resume();
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                    dismiss();
                }
                else
                {
                    ICaster.Toast_msg_bottom(getActivity(),
                            pojo.getMessage(), Toast.LENGTH_SHORT,
                            ICaster.notify);
                }
            }
            else
            {
                ICaster.Toast_msg_bottom(getActivity(),
                        "Error is saving process", Toast.LENGTH_SHORT,
                        ICaster.notify);
            }

        }
    }

    private int isValiddayDiff(String startTime, String endTime) {
        int day=0;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        // Date d1 = new Date();
        // Date d2 = new Date();
        String d1 = "";
        String d2 = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date d;
        Calendar thatDay = Calendar.getInstance();
        try {
            d = sdf.parse(startTime);

            startTime = output.format(d);
            d = sdf.parse(endTime);
            endTime = output.format(d);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.v("Start End Times :: ", "StartTime :" + startTime
                + " \nEndTime :" + endTime);
        try {
            // d1 = format.parse(startTime);
            // d2 = format.parse(endTime);
            //
            // long diff = d2.getTime() - d1.getTime();
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            Date date1 = format2.parse(startTime);
            Date date2 = format2.parse(endTime);
            long different = date2.getTime() - date1.getTime();
            //d1 = convertTo24Hour(startTime);
            //d2 = convertTo24Hour(endTime);
            //double diff = Double.parseDouble(d2) - Double.parseDouble(d1);
            Log.e("start", String.valueOf(date1.getTime()));
            Log.e("end", String.valueOf(date2.getTime()));
            Log.e("diff", String.valueOf(different));
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            day=(int)elapsedDays;
            //different = different % daysInMilli;

            //long elapsedHours = different / hoursInMilli;
           // different = different % hoursInMilli;

           // long elapsedMinutes = different / minutesInMilli;
           // different = different % minutesInMilli;

           // long elapsedSeconds = different / secondsInMilli;
            //if (difference >= 0) {
                //Toast.makeText(getApplicationContext(),String.valueOf(date1.getTime())+" / "+ String.valueOf(date2.getTime())+" "+ String.valueOf(difference), Toast.LENGTH_LONG).show();
              //  return true;
          //  }
        } catch (Exception e) {

        }
        return day;
    }
}
