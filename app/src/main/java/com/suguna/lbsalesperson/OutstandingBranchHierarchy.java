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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.suguna.lbsalesperson.Adapter.ListViewAdapter_outstandingbranch;
import com.suguna.lbsalesperson.Adapter.ListViewAdapter_outstandingsales;
import com.suguna.lbsalesperson.DAO.SugOutStanding_DAO;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.LBConstant;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class OutstandingBranchHierarchy  extends AppCompatActivity {
    NetworkUtil netutil;
    DatabaseUpDown databaseUpDown;
    List<SugOutStanding_POJO> Ministatement_POJOlist;
    List<SugOutStanding_POJO> Ministatement_POJOlist1;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;
    ListView list_dailyprice;

    TextView titletool, emptyText,tv_name;
    ListViewAdapter_outstandingbranch adapter;
    EditText search;
    ImageView close;
    ImageButton imgbtnfilter;
    TextView creditLimitLabel, overDueLabel,  outstandingTV;
   // SugOutStanding_DAO OutStanding_DAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outstandingsales);
        Toolbar toolbar = findViewById(R.id.toolbar);



        databaseUpDown = new DatabaseUpDown(this);
        netutil=new NetworkUtil(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        lbloginDAO = new Sug_lblogin(this);
       // OutStanding_DAO = new SugOutStanding_DAO(this);
        String custnumber="";
        custnumber=sharedpreferences.getString(Name, "");
        String USERID="",TKEY="";
        USERID=lbloginDAO.getCUSTNAME(custnumber);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(USERID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fields_declaration();
        OnField_Click();
        OnField_Listender();
        if (netutil.isOnline()) {
            new getoutstandingdetails_asyn().execute();
        } else {
            ICaster.Toast_msg_internet(OutstandingBranchHierarchy.this,
                    "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                    ICaster.notify);

            // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

        }
    }
    private void OnField_Click() {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ministatement_POJOlist=Ministatement_POJOlist1;

                adapter = new ListViewAdapter_outstandingbranch(
                        OutstandingBranchHierarchy.this, Ministatement_POJOlist,
                        OutstandingBranchHierarchy.this);
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

                List<SugOutStanding_POJO> POJOlist = new ArrayList<SugOutStanding_POJO>();

                if (value.equals("") || value.length() == 0)
                {
                    Ministatement_POJOlist=filter("");
                    // adapter.refreshEvents(POJOlist);
                    adapter = new ListViewAdapter_outstandingbranch(
                            OutstandingBranchHierarchy.this, Ministatement_POJOlist,
                            OutstandingBranchHierarchy.this);
                    list_dailyprice.setAdapter(adapter);

                }
                else
                {
                    Ministatement_POJOlist=filter(value);
                    adapter = new ListViewAdapter_outstandingbranch(
                            OutstandingBranchHierarchy.this, Ministatement_POJOlist,
                            OutstandingBranchHierarchy.this);
                    list_dailyprice.setAdapter(adapter);

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
    protected void onResume() {
        super.onResume();


    }
    @Override
    public void onBackPressed() {
        finish();
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
        tv_name= (TextView) findViewById(R.id.tv_name);
        list_dailyprice.setEmptyView(emptyText);
        search= (EditText) findViewById(R.id.search);
        close= (ImageView) findViewById(R.id.close);
        imgbtnfilter= (ImageButton) findViewById(R.id.imgbtnfilter);


        overDueLabel = (TextView) findViewById(R.id.overdue_limit_label);
        creditLimitLabel = (TextView) findViewById(R.id.credit_limit_label);
        outstandingTV = (TextView) findViewById(R.id.tv_outstanding);



        if (LocalStorageSP.INSTANCE.getLoginUser(this).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_HEAD)) {

            creditLimitLabel.setVisibility(View.VISIBLE);

        } else if (LocalStorageSP.INSTANCE.getLoginUser(this).getUSERTYPE().equalsIgnoreCase(LBConstant.SALES_EXECUTIVE)) {
            creditLimitLabel.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    3.5f
            );
            //tv_name.setLayoutParams(param);

        }

    }
    public class getoutstandingdetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(OutstandingBranchHierarchy.this)
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
            Ministatement_POJOlist = new ArrayList<>();
            Ministatement_POJOlist=databaseUpDown.get_outstandingbranch_details_server(USERID,
                    TKEY,custnumber,"");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Ministatement_POJOlist1=Ministatement_POJOlist;
            if(Ministatement_POJOlist.size()>0)
            {
                adapter = new ListViewAdapter_outstandingbranch(
                        OutstandingBranchHierarchy.this, Ministatement_POJOlist,
                        OutstandingBranchHierarchy.this);
                list_dailyprice.setAdapter(adapter);

                if(Ministatement_POJOlist.size()>0)
                {
                    if(Ministatement_POJOlist.get(0).getDRILLDOWN().equals("Yes"))
                    {
                        tv_name.setText("Sales Person");
                    }
                    else
                    {
                        tv_name.setText("Customer");
                    }
                }
            }
            else
            {
                adapter = new ListViewAdapter_outstandingbranch(OutstandingBranchHierarchy.this, Ministatement_POJOlist,
                        OutstandingBranchHierarchy.this);
                list_dailyprice.setAdapter(adapter);
            }
        }
    }

    public List<SugOutStanding_POJO>  filter(String aQuery) {
        List<SugOutStanding_POJO> Ministatement_POJOlist_filter = new ArrayList<>();
        if (Ministatement_POJOlist1.size() > 0) {
            for (int i = 0; i < Ministatement_POJOlist1.size(); i++) {
                if (Ministatement_POJOlist1.get(i).getNAME().toLowerCase().contains(aQuery.toLowerCase())) {
                    Ministatement_POJOlist_filter.add(Ministatement_POJOlist1.get(i));
                }
            }
        }
        return Ministatement_POJOlist_filter ;
    }
}

