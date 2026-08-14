package com.suguna.lbsalesperson.feedback.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.suguna.lbsalesperson.Adapter.ListViewAdapter_feedback;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SugFeedback_POJO;
import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class FeedBackActivity extends AppCompatActivity {
    private ProgressDialog progress;
    DatabaseUpDown databaseUpDown;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;

    List<SugFeedback_POJO> Feedback_POJOlist;
    TextView txt_transdate;

    LinearLayout lyt_add;
    AlertDialog alertDialog = null;


    ListView list_ministatment;
    ListViewAdapter_feedback adapter;
    TextView emptyText;
    NetworkUtil netutil;
    int resume=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FeedBack");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lbloginDAO = new Sug_lblogin(this);
        databaseUpDown = new DatabaseUpDown(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        netutil=new NetworkUtil(this);
        On_Fielddeclartion();
        if (netutil.isOnline()) {
            new getFeedbackdetails_asyn().execute();
        } else {
            ICaster.Toast_msg_internet(FeedBackActivity.this,
                    "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                    ICaster.notify);

            // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

        }

    }
    private void On_Fielddeclartion() {
        list_ministatment = (ListView) findViewById(R.id.list_ministatment);
        emptyText = (TextView) findViewById(android.R.id.empty);
        list_ministatment.setEmptyView(emptyText);
    }
    @Override
    public void onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed();

    }
    @Override
    public void onResume() {
        if(resume==1) {
            if (netutil.isOnline()) {
                new getFeedbackdetails2_asyn().execute();
            } else {

            }
        }
        super.onResume();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public class getFeedbackdetails_asyn extends
            AsyncTask<String, String, Boolean>
    {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* progress = new ProgressDialog(FeedBack.this);
            SpannableString spannableString = new SpannableString(
                    "Get Customer Feedback");
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
            dialog = new ACProgressFlower.Builder(FeedBackActivity.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            boolean download = false;
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            Feedback_POJOlist = new ArrayList<SugFeedback_POJO>();
            Feedback_POJOlist=databaseUpDown.get_Feedback_details_server(USERID,
                    TKEY,custnumber);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);
            dialog.dismiss();
            if(Feedback_POJOlist.size()>0)
            {
                resume=1;
                adapter = new ListViewAdapter_feedback(
                        FeedBackActivity.this, Feedback_POJOlist,
                        FeedBackActivity.this);
                list_ministatment.setAdapter(adapter);
            }
            else
            {
                adapter = new ListViewAdapter_feedback(
                        FeedBackActivity.this, Feedback_POJOlist,
                        FeedBackActivity.this);
                list_ministatment.setAdapter(adapter);
            }
        }
    }
    public class getFeedbackdetails2_asyn extends
            AsyncTask<String, String, Boolean>
    {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* progress = new ProgressDialog(FeedBack.this);
            SpannableString spannableString = new SpannableString(
                    "Get Customer Feedback");
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

        }
        @Override
        protected Boolean doInBackground(String... params)
        {
            // TODO Auto-generated method stub
            boolean download = false;
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            Feedback_POJOlist = new ArrayList<SugFeedback_POJO>();
            Feedback_POJOlist=databaseUpDown.get_Feedback_details_server(USERID,
                    TKEY,custnumber);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result)
        {
            super.onPostExecute(result);

            if(Feedback_POJOlist.size()>0)
            {

                adapter = new ListViewAdapter_feedback(
                        FeedBackActivity.this, Feedback_POJOlist,
                        FeedBackActivity.this);
                list_ministatment.setAdapter(adapter);
            }
            else
            {
                adapter = new ListViewAdapter_feedback(
                        FeedBackActivity.this, Feedback_POJOlist,
                        FeedBackActivity.this);
                list_ministatment.setAdapter(adapter);
            }
        }
    }
}
