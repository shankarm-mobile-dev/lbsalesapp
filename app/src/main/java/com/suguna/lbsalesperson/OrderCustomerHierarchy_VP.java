package com.suguna.lbsalesperson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.suguna.lbsalesperson.Adapter.ListViewAdapter_ordercustomer;
import com.suguna.lbsalesperson.Adapter.ListViewAdapter_ordercustomerVP;
import com.suguna.lbsalesperson.DAO.SugOrderSales_DAO;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SUGCustDashboard_POJO;
import com.suguna.lbsalesperson.POJO.SUGRegionDashboard_POJO;
import com.suguna.lbsalesperson.POJO.SugOrderSummary_POJO;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class OrderCustomerHierarchy_VP extends AppCompatActivity {
    NetworkUtil netutil;
    DatabaseUpDown databaseUpDown;public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;
    ListView list_dailyprice;

    TextView titletool, emptyText,txt_day;
    List<SugOrderSummary_POJO> Ministatement_POJOlist;
    ListViewAdapter_ordercustomerVP adapter;
    EditText search;
    ImageView close;
    ImageButton imgbtnfilter;
    SugOrderSales_DAO OrderSales_DAO;
    String day,region,date;
    List<SUGCustDashboard_POJO> RegionDashboardPOJOlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordersaleshierarchy_vp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        databaseUpDown = new DatabaseUpDown(this);
        netutil=new NetworkUtil(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        lbloginDAO = new Sug_lblogin(this);
        OrderSales_DAO= new SugOrderSales_DAO(this);
        String custnumber="";
        custnumber=sharedpreferences.getString(Name, "");
        String USERID="",TKEY="";
        USERID=lbloginDAO.getCUSTNAME(custnumber);
        fields_declaration();
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() != null)
        {

            date=(getIntent().getStringExtra("date"));
            day=(getIntent().getStringExtra("day"));
            region=(getIntent().getStringExtra("region"));
            txt_day.setText(day);
            getSupportActionBar().setTitle(region+ "- Customers Booked Orders");
        }
        else
        {
            getSupportActionBar().setTitle("Mr. Abdul Hakeem Ashiq J");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        OnField_Click();
        OnField_Listender();
        if (netutil.isOnline()) {
            new checkdashboarddetails_asyn().execute();
        } else {
            ICaster.Toast_msg_internet(OrderCustomerHierarchy_VP.this,
                    "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                    ICaster.notify);

            // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

        }
    }
    private void OnField_Click() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ministatement_POJOlist = new ArrayList<SugOrderSummary_POJO>();
                Ministatement_POJOlist=OrderSales_DAO.getvalues();
                // adapter.refreshEvents(POJOlist);
                adapter = new ListViewAdapter_ordercustomerVP(OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                        OrderCustomerHierarchy_VP.this);
                list_dailyprice.setAdapter(adapter);
                search.setText("");
            }
        });
    }
    private void OnField_Listender() {
        search.addTextChangedListener(new TextWatcher() {

            @SuppressLint("DefaultLocale") @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                String value = "";
                value = cs.toString();
                //Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                List<SugOutStanding_POJO> POJOlist = new ArrayList<SugOutStanding_POJO>();
                //   POJOlist=Ministatement_POJOlist;
                if (value.equals("") || value.length() == 0)
                {
                    Ministatement_POJOlist = new ArrayList<SugOrderSummary_POJO>();
                    Ministatement_POJOlist=OrderSales_DAO.getvalues();
                    // adapter.refreshEvents(POJOlist);
                    adapter = new ListViewAdapter_ordercustomerVP(OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                            OrderCustomerHierarchy_VP.this);
                    list_dailyprice.setAdapter(adapter);

                    Log.e("Zero","Zero");
                    //Toast.makeText(getApplicationContext(), String.valueOf(Sugonhanddetails_POJOlist.size()), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Ministatement_POJOlist = new ArrayList<SugOrderSummary_POJO>();
                    Ministatement_POJOlist=OrderSales_DAO.getvaluessearch(value);
                    adapter = new ListViewAdapter_ordercustomerVP(OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                            OrderCustomerHierarchy_VP.this);
                    list_dailyprice.setAdapter(adapter);;
                    Log.e("Non-Zero","Non-Zero");
                    //Toast.makeText(getApplicationContext(), String.valueOf(Sugonhanddetails_POJOlist_search.size()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), String.valueOf(Sugonhanddetails_POJOlist.size()), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
        return;

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void fields_declaration() {
        // TODO Auto-generated method stub
        list_dailyprice= (ListView) findViewById(R.id.list_dailyprice);
        emptyText = (TextView) findViewById(android.R.id.empty);
        txt_day= (TextView) findViewById(R.id.txt_day);
        list_dailyprice.setEmptyView(emptyText);
        search= (EditText) findViewById(R.id.search);
        close= (ImageView) findViewById(R.id.close);
        imgbtnfilter= (ImageButton) findViewById(R.id.imgbtnfilter);
    }
    public class getordersummarydetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progress = new ProgressDialog(HistoricPrice.this);
            SpannableString spannableString = new SpannableString(
                    "Get Historic Price");
            Typeface typeFace = Typeface.createFromAsset(getAssets(),
                    "fonts/CALIBRIB.TTF");
            spannableString.setSpan(new RelativeSizeSpan(1.0f), 0,
                    spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new CustomTypefaceSpan("", typeFace), 0,
                    spannableString.length(), 0);
            progress.setMessage(spannableString);
            // progress.setMessage("Validate Login");
            progress.setCancelable(false);
            // Show progressdialog
            progress.setIndeterminate(true);
            progress.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.circular_progress_bar));
            progress.show();*/
            dialog = new ACProgressFlower.Builder(OrderCustomerHierarchy_VP.this)
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
            Ministatement_POJOlist = new ArrayList<SugOrderSummary_POJO>();
            Ministatement_POJOlist=databaseUpDown.get_ordersummarybrnach_details_server(USERID,
                    TKEY,custnumber,"-");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(Ministatement_POJOlist.size()>0)
            {
                OrderSales_DAO.deleteAll();
                OrderSales_DAO.bulkinsertItem(Ministatement_POJOlist);
                adapter = new ListViewAdapter_ordercustomerVP(
                        OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                        OrderCustomerHierarchy_VP.this);
                list_dailyprice.setAdapter(adapter);

            }
            else
            {
                OrderSales_DAO.deleteAll();
                OrderSales_DAO.bulkinsertItem(Ministatement_POJOlist);
                adapter = new ListViewAdapter_ordercustomerVP(OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                        OrderCustomerHierarchy_VP.this);
                list_dailyprice.setAdapter(adapter);
            }

        }
    }
    public class checkdashboarddetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(OrderCustomerHierarchy_VP.this)
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
            Ministatement_POJOlist = new
                    ArrayList<SugOrderSummary_POJO>();
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            Ministatement_POJOlist = databaseUpDown.get_regiondashbordCust_dtls(custnumber,USERID,TKEY,date,region);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(Ministatement_POJOlist.size()>0)
            {
                OrderSales_DAO.deleteAll();
            OrderSales_DAO.bulkinsertItem(Ministatement_POJOlist);
            adapter = new ListViewAdapter_ordercustomerVP(
                    OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                    OrderCustomerHierarchy_VP.this);
            list_dailyprice.setAdapter(adapter);

        }
            else
        {
            OrderSales_DAO.deleteAll();
            OrderSales_DAO.bulkinsertItem(Ministatement_POJOlist);
            adapter = new ListViewAdapter_ordercustomerVP(OrderCustomerHierarchy_VP.this, Ministatement_POJOlist,
                    OrderCustomerHierarchy_VP.this);
            list_dailyprice.setAdapter(adapter);
        }
        }
    }

}
