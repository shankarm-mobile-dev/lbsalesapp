package com.suguna.lbsalesperson;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kinda.alert.KAlertDialog;
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.ApplicationInfo;
import com.suguna.lbsalesperson.POJO.SugLogin_POJO;
import com.suguna.lbsalesperson.POJO.User;
import com.suguna.lbsalesperson.Sendreceiver.BfmTelelphony;
import com.suguna.lbsalesperson.Utils.ICaster;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;
import com.suguna.lbsalesperson.Utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class LoginActivity extends AppCompatActivity {
    Button submit;
    EditText edit_username, edit_password;
    String str_username, str_password;

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    public static final String DeviceId = "deviceid";
    public static final String DAYS = "days";
    public static final String EMPName = "empname";
    public static final String SERVERDATE = "serverdate";
    SharedPreferences sharedpreferences;

    Context myContext;
    private ProgressDialog progress;
    DatabaseUpDown databaseUpDown;
    List<SugLogin_POJO> lsloginPOJOlist;

    Sug_lblogin lbloginDAO;
    LinearLayout lyt_register;
    TextView txt_forgot, enquiryTv;
    NetworkUtil netutil;

    private BfmTelelphony bfmTelelphony;
    private String deviceid = null;
    private String androdid = null;
    boolean checkimeideviec = false;
    String osvalue = "0";
    TextView txt_deviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        myContext = this;
        databaseUpDown = new DatabaseUpDown(this);
        lbloginDAO = new Sug_lblogin(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        netutil = new NetworkUtil(this);
        netutil = new NetworkUtil(this);
        bfmTelelphony = new BfmTelelphony(this);
        try {
            deviceid = bfmTelelphony.getIMEINumber();
            checkimeideviec = bfmTelelphony.CheckIMEINumber();
            androdid = bfmTelelphony.getAndroidID();
            osvalue = bfmTelelphony.getAndroidos();
            if (!checkimeideviec) {
                LocalStorageSP.INSTANCE.put(myContext, getString(R.string.device_id), androdid);
                deviceid = "0";
            } else {
                LocalStorageSP.INSTANCE.put(myContext, getString(R.string.device_id), deviceid);
            }

        } catch (SecurityException e) {
            deviceid ="0";
            androdid = bfmTelelphony.getAndroidID();
            e.printStackTrace();
            Log.e("erro", String.valueOf(e.getMessage()));

        }
        On_Fielddeclartion();
        On_FieldClick();
        if (sharedpreferences.contains(EMPName)) {
             edit_username.setText(sharedpreferences.getString(EMPName, ""));
             edit_password.setText(sharedpreferences.getString(Password, ""));
        }

        if (sharedpreferences.contains(DeviceId)) {
            //  Toast.makeText(getApplicationContext(),sharedpreferences.getString(DeviceId, ""),Toast.LENGTH_SHORT).show();
        }
        txt_deviceid.setText("Device ID : "+androdid);
        String msg = "Dear Customer your forgot password OTP is 456932 . Do not share with anyone. ";
        // here we are spliting the sms using " : " symbol
        String otp = msg.split("\\s+")[7];
        Log.e("otp",otp);
    }

    private void On_FieldClick() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username = edit_username.getText().toString();
                str_password = edit_password.getText().toString();
                if (str_username.equals("")) {
                    ICaster.Toast_msg_bottom(LoginActivity.this,
                            "Enter Customer Code ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                    // Toast.makeText(getApplicationContext(),"Enter Customer Code",Toast.LENGTH_SHORT).show();
                } else if (str_password.equals("")) {
                    ICaster.Toast_msg_bottom(LoginActivity.this,
                            "Enter Password ", Toast.LENGTH_SHORT,
                            ICaster.notify);
                    //Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                } else {
                    // Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    // startActivity(i);
                    if (netutil.isOnline()) {
                        new checklogindetails_asyn().execute();
                    } else {
                        ICaster.Toast_msg_internet(LoginActivity.this,
                                "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                                ICaster.notify);
                        // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, ForgoyPwdGenerateActivity.class);
                startActivity(i);
            }
        });
        lyt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterGenerateActivity.class);
                startActivity(i);
            }
        });

        enquiryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://customer.sgitsys.com/lb_customer_register/Broiler Live Bird"));
                startActivity(browserIntent);
            }
        });
    }

    private void On_Fielddeclartion() {
        submit = (Button) findViewById(R.id.btn_submit);
        edit_username = (EditText) findViewById(R.id.edit_username);
        edit_password = (EditText) findViewById(R.id.edit_password);
        txt_forgot = (TextView) findViewById(R.id.textView3);
        enquiryTv = (TextView) findViewById(R.id.enquiry_tv);
        lyt_register = (LinearLayout) findViewById(R.id.lyt_register);
        txt_deviceid = (TextView) findViewById(R.id.txt_deviceid);
    }

    public class checklogindetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        String appversion = "0";
        int devappver = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progress = new ProgressDialog(LoginActivity.this);
            SpannableString spannableString = new SpannableString(
                    "Validate Login");
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
            dialog = new ACProgressFlower.Builder(LoginActivity.this)
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
            lsloginPOJOlist = new ArrayList<SugLogin_POJO>();
            lsloginPOJOlist = databaseUpDown.get_login_details_server(str_username,
                    str_password, deviceid, androdid);
            appversion = databaseUpDown.get_appversion_server("");

            if (lsloginPOJOlist.size() > 0) {
                if (sharedpreferences.contains(DeviceId)) {
                    String deiveid = "";
                   // databaseUpDown.updatefirebaseid(str_username, sharedpreferences.getString(DeviceId, ""));
                    //  Toast.makeText(getApplicationContext(),sharedpreferences.getString(DeviceId, ""),Toast.LENGTH_SHORT).show();
                }

            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            devappver = ApplicationInfo
                    .getAPPLICATION_VERSIONCODE();
            int server = 0;
            try {
                server = Integer.parseInt(appversion);
            } catch (Exception e) {
                server = 0;
            }

            if (server > devappver) {
                prefDial_check(1);
            } else if (lsloginPOJOlist.size() > 0) {
                SugLogin_POJO lslogin_pojo = lsloginPOJOlist.get(0);
                String pwd_exp="",usertype="",code="",status="",message="";
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Name, lslogin_pojo.getUSERNAME());
                editor.putString(EMPName, str_username);
                editor.putString(Password, str_password);
                editor.putString(DAYS, lslogin_pojo.getDays());
                editor.putString(SERVERDATE, lslogin_pojo.getServertime());
                editor.commit();
                lbloginDAO.deleteAll();
                lbloginDAO.bulkinsertItem(lsloginPOJOlist);
                pwd_exp=lslogin_pojo.getPWD_EXP();
                code=lslogin_pojo.getStatus_code();
                message=  lslogin_pojo.getMessage();
                status= lslogin_pojo.getStatus();


                    if(status.equals("Successfully")) {
                        if(pwd_exp.equals("1"))
                        {
                            Toast.makeText(getApplicationContext(),"Your password is expired, set the new password and then proceed the login",Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, ResetPasswordCreate.class);
                            i.putExtra("username", edit_username.getText().toString());
                            i.putExtra("usertype", usertype);
                            i.putExtra("password", edit_password.getText().toString());
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                    {
                       /* ICaster.Toast_msg_bottom(LoginActivity.this,
                                message, Toast.LENGTH_SHORT,
                                ICaster.notify);*/
                        new KAlertDialog(LoginActivity.this, KAlertDialog.WARNING_TYPE)
                                .setTitleText(status)
                                .setContentText(message)
                                .setConfirmClickListener(new KAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {


                                        kAlertDialog.dismiss();
                                    }
                                })
                                .show();
                    }

                //  Toast.makeText(getApplicationContext(),"Successfully Login",Toast.LENGTH_SHORT).show();
            } else {
                ICaster.Toast_msg_bottom(LoginActivity.this,
                        "Invalid Login Credinals ", Toast.LENGTH_SHORT,
                        ICaster.notify);
                // Toast.makeText(getApplicationContext(),"Invalid Login Credinals",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void prefDial_check(int i) {
        // TODO Auto-generated method stub
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle("Update Application");
        quitDialog.setMessage("Please update new version from playstore");
        quitDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (netutil.isOnline()) {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    String url_more_app = "https://play.google.com/store/apps/details?id=com.suguna.lbsalesperson";
                                    Intent viewIntent =
                                            new Intent("android.intent.action.VIEW",
                                                    Uri.parse(url_more_app));
                                    startActivity(viewIntent);
                                }
                            });
                        } else {

                        }
                    }
                });

        quitDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        if (1 == i)
            quitDialog.show();
    }
}
