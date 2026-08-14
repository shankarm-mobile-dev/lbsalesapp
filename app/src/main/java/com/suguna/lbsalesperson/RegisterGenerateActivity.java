package com.suguna.lbsalesperson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kinda.alert.KAlertDialog;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SugRegister_POJO;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class RegisterGenerateActivity extends AppCompatActivity {
    Button submit;
    EditText edit_username,edit_password;
    String str_username,str_password;
    private ProgressDialog progress;
    DatabaseUpDown databaseUpDown;
    NetworkUtil netutil;
    List<SugRegister_POJO> RegisterPOJOlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        databaseUpDown = new DatabaseUpDown(this);
        netutil=new NetworkUtil(this);
        On_Fielddeclartion();
        On_FieldClick();
    }
    private void On_Fielddeclartion() {
        submit=(Button) findViewById(R.id.btn_submit);
        edit_username=(EditText) findViewById(R.id.edit_username);

    }


    private void On_FieldClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username=edit_username.getText().toString();

                if(str_username.equals(""))
                {
                    ICaster.Toast_msg_bottom(RegisterGenerateActivity.this,
                            "Enter Customer Code ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                    // Toast.makeText(getApplicationContext(),"Enter Customer Code",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    // Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    // startActivity(i);
                    if (netutil.isOnline()) {
                        new checklogindetails_asyn().execute();
                    } else {
                        ICaster.Toast_msg_internet(RegisterGenerateActivity.this,
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
        String status="",statuscode="",message="",custname="",mobile="";
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          /*  progress = new ProgressDialog(RegisterGenerate.this);
            SpannableString spannableString = new SpannableString(
                    "Validate Customer Code");
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
            dialog = new ACProgressFlower.Builder(RegisterGenerateActivity.this)
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
            RegisterPOJOlist = databaseUpDown.get_registeredcus(str_username                    );
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if(RegisterPOJOlist.size()>0)
            {
                SugRegister_POJO lsloginPOJO;

                for (int i = 0; i < RegisterPOJOlist.size(); i++) {
                    lsloginPOJO = RegisterPOJOlist.get(i);
                    status=lsloginPOJO.getSTATUS();
                    statuscode=lsloginPOJO.getSTATUSCODE();
                    message=lsloginPOJO.getMESSAGE();;
                    custname=lsloginPOJO.getCUSTNAME();
                    mobile=lsloginPOJO.getMOBILENO();
                }
                if(statuscode.equals("200")) {
                    new KAlertDialog(RegisterGenerateActivity.this, KAlertDialog.SUCCESS_TYPE)
                            .setTitleText(status)
                            .setContentText(message)
                            .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(KAlertDialog kAlertDialog) {
                                    Intent i = new Intent(RegisterGenerateActivity.this, RegisterCreateActivty.class);
                                    i.putExtra("custname", custname);
                                    i.putExtra("mobile", mobile);
                                    i.putExtra("custcode", str_username);
                                    startActivity(i);
                                    finish();

                                    kAlertDialog.dismiss();
                                }
                            })
                            .show();

                }
                else
                {
                    new KAlertDialog(RegisterGenerateActivity.this, KAlertDialog.ERROR_TYPE)
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
                new KAlertDialog(RegisterGenerateActivity.this, KAlertDialog.ERROR_TYPE)
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

