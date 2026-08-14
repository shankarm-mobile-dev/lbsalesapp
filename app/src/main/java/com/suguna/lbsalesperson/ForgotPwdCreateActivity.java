package com.suguna.lbsalesperson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kinda.alert.KAlertDialog;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SugRegister_POJO;
import com.suguna.lbsalesperson.Sendreceiver.BfmTelelphony;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.NetworkUtil;
import com.suguna.lbsalesperson.Utils.Passpolicy;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class ForgotPwdCreateActivity extends AppCompatActivity {
    Button submit;
    EditText edit_username,edit_password,edit_otp;
    String str_username,str_password,str_otp;
    private ProgressDialog progress;
    DatabaseUpDown databaseUpDown;
    NetworkUtil netutil;
    String custcode="",mobileno="";
    List<SugRegister_POJO> RegisterPOJOlist;
    private String deviceid = null;
    private String androdid = null;
    private BfmTelelphony bfmTelelphony;
    boolean checkimeideviec=false;
    String osvalue="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot2);
        databaseUpDown = new DatabaseUpDown(this);
        netutil=new NetworkUtil(this);
        bfmTelelphony = new BfmTelelphony(this);
        try {
            deviceid = bfmTelelphony.getIMEINumber();
            checkimeideviec=bfmTelelphony.CheckIMEINumber();
            androdid = bfmTelelphony.getAndroidID();
            osvalue=bfmTelelphony.getAndroidos();
            if(!checkimeideviec) {
                deviceid ="0";
            }

        }
        catch (SecurityException e)
        {
            deviceid ="0";
            androdid = bfmTelelphony.getAndroidID();
            e.printStackTrace();
            Log.e("erro",String.valueOf(e.getMessage()));

        }
        On_Fielddeclartion();
        On_FieldClick();
        try {
           // new OTP_Receiver().setEditText(edit_otp);
        }
        catch (Exception e)
        {

        }
    }
    private void On_Fielddeclartion() {
        submit=(Button) findViewById(R.id.btn_submit);
        edit_username=(EditText) findViewById(R.id.edit_username);
        edit_password=(EditText) findViewById(R.id.edit_password);
        edit_otp=(EditText) findViewById(R.id.edit_otp);

        if (getIntent().getExtras() != null) {
            edit_username.setText(getIntent().getStringExtra("custname"));
            custcode=getIntent().getStringExtra("custcode");
            mobileno=getIntent().getStringExtra("mobile");
        }

    }


    private void On_FieldClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username=edit_username.getText().toString();
                str_password=edit_password.getText().toString();
                str_otp=edit_otp.getText().toString();

                if(str_password.equals(""))
                {

                    ICaster.Toast_msg_bottom(ForgotPwdCreateActivity.this,
                            "Enter password ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                    // Toast.makeText(getApplicationContext(),"Enter password",Toast.LENGTH_SHORT).show();
                }
                else if(!Passpolicy.isValid(custcode,str_password))
                {
                    ICaster.Toast_msg_bottom(ForgotPwdCreateActivity.this,
                            Passpolicy.getStatusMessage(), Toast.LENGTH_SHORT,
                            ICaster.notify);
                }
                else if(str_otp.equals(""))
                {
                    ICaster.Toast_msg_bottom(ForgotPwdCreateActivity.this,
                            "Enter Received OTP ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                    //Toast.makeText(getApplicationContext(),"Enter Received OTP",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    // Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    // startActivity(i);
                    if (netutil.isOnline()) { new checklogindetails_asyn().execute();
                    } else {
                        ICaster.Toast_msg_internet(ForgotPwdCreateActivity.this,
                                "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                                ICaster.notify);

                        // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
    public class checklogindetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progress = new ProgressDialog(ForgorPasswordCreate.this);
            SpannableString spannableString = new SpannableString(
                    "Update Password");
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
            dialog = new ACProgressFlower.Builder(ForgotPwdCreateActivity.this)
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
            RegisterPOJOlist = new
                    ArrayList<SugRegister_POJO>();
            RegisterPOJOlist = databaseUpDown.updatelogin(custcode,str_password,str_otp ,deviceid,androdid,osvalue                 );
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(RegisterPOJOlist.size()>0)
            {
                SugRegister_POJO lsloginPOJO;
                String status="",statuscode="",message="",custname="",mobile="";
                for (int i = 0; i < RegisterPOJOlist.size(); i++) {
                    lsloginPOJO = RegisterPOJOlist.get(i);
                    status=lsloginPOJO.getSTATUS();
                    statuscode=lsloginPOJO.getSTATUSCODE();
                    message=lsloginPOJO.getMESSAGE();;
                    custname=lsloginPOJO.getCUSTNAME();
                    mobile=lsloginPOJO.getMOBILENO();
                }
                if(statuscode.equals("200")) {

                    new KAlertDialog(ForgotPwdCreateActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText(status)
                            .setContentText(message)
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    Intent i = new Intent(ForgotPwdCreateActivity.this, LoginActivity.class);

                                    startActivity(i);
                                    finish();

                                    kAlertDialog.dismiss();
                                }
                            })
                            .show();


                }
                else
                {
                    new KAlertDialog(ForgotPwdCreateActivity.this, KAlertDialog.ERROR_TYPE)
                            .setTitleText(status)
                            .setContentText(message)
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    //finish();

                                    kAlertDialog.dismiss();
                                }
                            })
                            .show();
                }
            }
            else
            {
                new KAlertDialog(ForgotPwdCreateActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Some error occured while register!")
                        .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(KAlertDialog kAlertDialog) {
                                //finish();

                                kAlertDialog.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

}

