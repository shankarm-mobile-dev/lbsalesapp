package com.suguna.lbsalesperson;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.suguna.lbsalesperson.Adapter.ListViewAdapter_pricelist;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SugPrice_POJO;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class PriceListActivity extends AppCompatActivity {
    NetworkUtil netutil;
    DatabaseUpDown databaseUpDown;
    List<SugPrice_POJO> Ministatement_POJOlist;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;
    ListViewAdapter_pricelist adapter;
    ListView list_dailyprice;

    TextView titletool, emptyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricelist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Current Price");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lbloginDAO = new Sug_lblogin(this);
        databaseUpDown = new DatabaseUpDown(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        fields_declaration();
        netutil=new NetworkUtil(this);
        if (netutil.isOnline()) {
            new getcurrentpricedetails_asyn().execute();
        } else {
            ICaster.Toast_msg_internet(PriceListActivity.this,
                    "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                    ICaster.notify);

            // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();

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
        list_dailyprice.setEmptyView(emptyText);
    }
    public class getcurrentpricedetails_asyn extends
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
            dialog = new ACProgressFlower.Builder(PriceListActivity.this)
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
            Ministatement_POJOlist = new ArrayList<SugPrice_POJO>();
            Ministatement_POJOlist=databaseUpDown.get_currentpriceall_details_server(USERID,
                    TKEY,custnumber);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(Ministatement_POJOlist.size()>0)
            {
                adapter = new ListViewAdapter_pricelist(
                        PriceListActivity.this, Ministatement_POJOlist,
                        PriceListActivity.this);
                list_dailyprice.setAdapter(adapter);

            }
            else
            {
                adapter = new ListViewAdapter_pricelist(PriceListActivity.this, Ministatement_POJOlist,
                        PriceListActivity.this);
                list_dailyprice.setAdapter(adapter);
            }

        }
    }
}