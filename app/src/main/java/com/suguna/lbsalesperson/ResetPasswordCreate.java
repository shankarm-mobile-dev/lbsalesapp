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

public class ResetPasswordCreate extends AppCompatActivity {
    Button submit;
    EditText edit_username,edit_oldpassword,edit_newpassword,edit_confirmpassword;
    String str_username,str_password,str_otp,str_oldpassword="",str_newpassword="",str_confirmpassword="",str_userpassword;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password_create);
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
    }
    private void On_Fielddeclartion() {
        submit=(Button) findViewById(R.id.btn_submit);
        edit_username=(EditText) findViewById(R.id.edit_username);
       /* edit_password=(EditText) findViewById(R.id.edit_password);
        edit_otp=(EditText) findViewById(R.id.edit_otp);*/
        edit_oldpassword=(EditText) findViewById(R.id.edit_oldpassword);
        edit_newpassword=(EditText) findViewById(R.id.edit_newpassword);
        edit_confirmpassword=(EditText) findViewById(R.id.edit_confirmpassword);

        if (getIntent().getExtras() != null) {
            edit_username.setText(getIntent().getStringExtra("username"));
            str_username=getIntent().getStringExtra("username");
            str_userpassword=getIntent().getStringExtra("password");
            //mobileno=getIntent().getStringExtra("mobile");
        }

    }


    private void On_FieldClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  str_username=edit_username.getText().toString();
                str_oldpassword=edit_oldpassword.getText().toString();
                str_newpassword=edit_newpassword.getText().toString();
                str_confirmpassword=edit_confirmpassword.getText().toString();

                if(str_oldpassword.equals(""))
                {

                    Toast.makeText(ResetPasswordCreate.this,
                            "Enter Old password ", Toast.LENGTH_SHORT).show();

                }
                else if(!str_userpassword.equals(str_oldpassword))
                {

                    Toast.makeText(ResetPasswordCreate.this,
                            "Entered old password is wrong", Toast.LENGTH_SHORT).show();

                }
                else if(str_newpassword.equals(""))
                {

                    Toast.makeText(ResetPasswordCreate.this,
                            "Enter New password ", Toast.LENGTH_SHORT).show();

                }
                else if(!Passpolicy.isValid(str_username,str_newpassword))
                {
                    Toast.makeText(ResetPasswordCreate.this,
                            Passpolicy.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
                else if(str_confirmpassword.equals(""))
                {

                    Toast.makeText(ResetPasswordCreate.this,
                            "Enter Confirm password ", Toast.LENGTH_SHORT).show();

                }
                else if(!str_confirmpassword.equals(str_newpassword))
                {

                    Toast.makeText(ResetPasswordCreate.this,
                            "Entered New and Confirm password is not matched", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    // Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    // startActivity(i);
                    if (netutil.isOnline()) { new checklogindetails_asyn().execute();
                    } else {
                        ICaster.Toast_msg_internet(ResetPasswordCreate.this,
                                "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                                ICaster.notify);

                        // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    public static boolean isAlphaNumeric(String s) {
        String pattern = "^[a-zA-Z0-9]*$";
        return s.matches(pattern);
    }

    public static boolean passwordStrength(String password) {
        System.out.println("Password Length : " + password.length());
        return password.length() >= 6;
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
            dialog = new ACProgressFlower.Builder(ResetPasswordCreate.this)
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
            RegisterPOJOlist = databaseUpDown.resetlogin(str_username,str_newpassword);
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

                    new KAlertDialog(ResetPasswordCreate.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText(status)
                            .setContentText(message)
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    Intent i = new Intent(ResetPasswordCreate.this, LoginActivity.class);

                                    startActivity(i);
                                    finish();

                                    kAlertDialog.dismiss();
                                }
                            })
                            .show();


                }
                else
                {
                    new KAlertDialog(ResetPasswordCreate.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(ResetPasswordCreate.this, KAlertDialog.ERROR_TYPE)
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