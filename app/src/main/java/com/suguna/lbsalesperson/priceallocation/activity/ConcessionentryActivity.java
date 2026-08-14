package com.suguna.lbsalesperson.priceallocation.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suguna.lbsalesperson.R;
import com.suguna.lbsalesperson.Utils.AppDialogs;
import com.suguna.lbsalesperson.Utils.DatePickerWidget;
import com.suguna.lbsalesperson.priceallocation.DataAccessManager;
import com.suguna.lbsalesperson.priceallocation.adapter.ConcessionPriceAdapter;
import com.suguna.lbsalesperson.priceallocation.model.lbitemlist;
import com.suguna.lbsalesperson.priceallocation.model.lblconcessionreasonlist;
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet;
import com.suguna.lbsalesperson.priceallocation.model.lbpricedetlist;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConcessionentryActivity extends AppCompatActivity {
    Context mContext;
    ProgressDialog mProg;
    lbpricedetlist mresponse;
    int responsecode = 0;
    String msg = "";
    private DataFetchTask mAuthTask = null;
    private DataSaveTask mSaveTask = null;
    private DataGetItemTask mGetItemTask = null;
    private DataGetReasonTask mGetReasonTask = null;
    List<HashMap<String, String>> Selecteditemlist = new ArrayList<HashMap<String, String>>();
    ArrayList<lbpricedet> ietmslist;
    ConcessionPriceAdapter itemsadapter;
    ListView ltv_itemslist;
    EditText edtdate;
    ImageButton saveButton;
    lbitemlist mlbitem;
    EditText edtitem;
    EditText edtreason;
    Boolean mEditable;
    TextView tvreason;
    lblconcessionreasonlist mlblconcessionreasonlist;
    final String mpricetype = "CONCESSION";
    public String LOG_TAG = "com.suguna.lbprice";
    private DatePickerWidget mDatePicker = null;
  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_concession);
      ietmslist = new ArrayList<lbpricedet>();
       mContext = getApplicationContext();
      ltv_itemslist = (ListView) findViewById(R.id.list);
      tvreason = (TextView) findViewById(R.id.tvreason);
      edtdate = (EditText) findViewById(R.id.edtdate);
      edtitem = (EditText) findViewById(R.id.edtitem);
      edtreason = (EditText) findViewById(R.id.edtreason);

      saveButton = (ImageButton) findViewById(R.id.saveButton);

      edtdate.setFocusable(false);
      edtdate.setFocusableInTouchMode(false);
      edtdate.setClickable(false);

      edtitem.setFocusable(false);
      edtitem.setFocusableInTouchMode(false);
      edtitem.setClickable(false);

      edtreason.setFocusable(false);
      edtreason.setFocusableInTouchMode(false);
      edtreason.setClickable(false);

      edtreason.setVisibility(View.GONE);
      tvreason.setVisibility(View.GONE);
      mEditable=true;
      if (mEditable == false) {
          saveButton.setVisibility(View.INVISIBLE);

      } else {
          saveButton.setVisibility(View.VISIBLE);
      }

      itemsadapter = new ConcessionPriceAdapter(mContext, R.layout.lbprice_list_item,
              ietmslist, mEditable);
      ltv_itemslist.setAdapter(itemsadapter);

      showProgress(true);
      mGetItemTask = new DataGetItemTask();
      mGetItemTask.execute((Void) null);

      mGetReasonTask = new DataGetReasonTask();
      mGetReasonTask.execute((Void) null);

      setbtnclick();
   }
    private void showProgress(final boolean show) {

        if (show == true) {
            mProg = new ProgressDialog(ConcessionentryActivity.this);
            mProg.setMessage("Loading...");
            mProg.setCancelable(false);
            mProg.show();
        } else {
            mProg.hide();
        }

    }

    public class DataFetchTask extends AsyncTask<Void, Void, Boolean> {

        String mlb_date;

        DataFetchTask(String lb_date) {
            mlb_date = lb_date;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mresponse = DataAccessManager.getlbdet(mlb_date, "CONCESSION",mContext);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (mresponse != null) {
                responsecode = Integer.parseInt(mresponse.getStatusCode());
            } else {
                responsecode = -1;
            }

            if (success) {
                if (responsecode >= 200) {
                    ietmslist.clear();
                    ietmslist.addAll(mresponse.getResult());
                    itemsadapter.notifyDataSetChanged();
                } else {

                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    private void setbtnclick() {

        if (mEditable == false) {
            edtdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mDatePicker == null) {

                        DatePickerWidget localDatePickerWidget1 = new DatePickerWidget(
                                getApplicationContext(), 1, null);
                        mDatePicker = localDatePickerWidget1;
                        mDatePicker.mcurrentedittext = edtdate;
                    }
                    mDatePicker.onSetDatePicker(null);
                }
            });

        }
        edtdate.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String text = s.toString();

                if (text != "" && !text.isEmpty()) {
                    if (mGetItemTask != null) {
                       showProgress(false);
                    }
                    // Toast.makeText(mContext, text,
                    // Toast.LENGTH_SHORT).show();
                     showProgress(true);
                    mAuthTask = new DataFetchTask(text);
                    mAuthTask.execute((Void) null);
                }
            }

        });
        if (mEditable == true) {
            saveButton.setOnClickListener(new View.OnClickListener() {

                private boolean flag;

                @Override
                public void onClick(View v) {
                    ArrayList<lbpricedet> ietmslist1 = new ArrayList<lbpricedet>();
                    ietmslist1.clear();
                    for (int i = 0; i < ietmslist.size(); i++) {
                        lbpricedet det = ietmslist.get(i);
                        String dataRate = det.getRATE();
                        if (dataRate.length() > 0) {

                            double rateValue = 0;
                            if (NumberUtils.isNumber(det.getCONCESSIONRATE())) {
                                rateValue = Double.parseDouble(det.getCONCESSIONRATE());
                            }

                            //=Double.parseDouble(det.getCONCESSIONRATE());
                            if (rateValue > 0 && det.getCONCESSIONSTATUS().equals("N")) {
                                ietmslist1.add(det);
                            }
                        }
                    }
                    if (ietmslist1.size() > 0) {
                        double rateValue = 0.0;
                        double rateValue1 = 0.0;
                        for (int a = 0; a < ietmslist1.size(); a++) {
                            rateValue = Double.parseDouble(ietmslist1.get(a)
                                    .getRATE());
                            rateValue1 = Double.parseDouble(ietmslist1.get(a)
                                    .getCONCESSIONRATE());

                            if (rateValue1 > rateValue) {
                                flag = false;
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                                "Please Enter the LMT below Rs." + rateValue + "")
                                        .setPositiveButton(
                                                android.R.string.yes,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        // continue with delete
                                                    }
                                                })

                                        .setIcon(
                                                android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            } else if (rateValue1 > 20) {//rateValue
                                flag = false;
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Warning")
//										.setMessage(
//												"Final Price should not be Rs.0" )
                                        .setMessage(
                                                "Please Enter the LMT below Rs.20")
                                        .setPositiveButton(
                                                android.R.string.yes,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        // continue with delete
                                                    }
                                                })

                                        .setIcon(
                                                android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            } else if (rateValue1 < 0.50) {
                                flag = false;
                                new AlertDialog.Builder(mContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                                "Please Enter the Price above Rs.0.50")
                                        .setPositiveButton(
                                                android.R.string.yes,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog,
                                                            int which) {
                                                        // continue with delete
                                                    }
                                                })

                                        .setIcon(
                                                android.R.drawable.ic_dialog_alert)
                                        .show();
                                break;
                            } else {
                                flag = true;
                            }

                        }
                        if (flag) {

                            String lb_date = edtdate.getText().toString();
                            String itemcode = mlbitem.getResult().get(0)
                                    .getITEM_CODE();
                            String uom = mlbitem.getResult().get(0).getUOM();
                            String reason = edtreason.getText().toString();

                            // DataAccessManager.savelbprice("12-DEC-2015",
                            // "BRCULL", "KG", ietmslist1);
                            showProgress(true);
                            mSaveTask = new DataSaveTask(lb_date, itemcode,
                                    uom, ietmslist1, reason);
                            mSaveTask.execute((Void) null);
                        }
                        // else {
                        // // Toast.makeText(mContext,
                        // // "Please Enter the Price below 200Rs.",
                        // // Toast.LENGTH_LONG).show();
                        //
                        // }

                    } else {
                        Toast.makeText(mContext, "Please Enter the Price",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public class DataSaveTask extends AsyncTask<Void, Void, Boolean> {

        String mtrx_date;
        String mitemcode;
        String mreason;
        String muom;
        ArrayList<lbpricedet> mitem;

        DataSaveTask(String trx_date, String itemcode, String uom,
                     ArrayList<lbpricedet> item, String reason) {
            mtrx_date = trx_date;
            muom = uom;
            mitemcode = itemcode;
            mitem = item;
            mreason = reason;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mresponse = DataAccessManager.savelbprice(mtrx_date, mitemcode,
                    muom, mitem, mpricetype, mreason,mContext);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (mresponse != null) {
                responsecode = Integer.parseInt(mresponse.getStatusCode());
                msg = String.valueOf(mresponse.getMessage());
            } else {
                responsecode = -1;
            }

            if (success) {
                if (responsecode == 200) {
                    // ietmslist.clear();
                    // ietmslist.addAll(mresponse.getResult());
                    itemsadapter.notifyDataSetChanged();
                    Toast.makeText(mContext, "Price Details has been updated",
                            Toast.LENGTH_LONG).show();
                    String current_date = edtdate.getText().toString();
                    edtdate.setText(current_date);
                } else if (responsecode == -4) {
                    Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Price Details has not updated",
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    public class DataGetItemTask extends AsyncTask<Void, Void, Boolean> {

        DataGetItemTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mlbitem = DataAccessManager.getlbitem("",mContext);
            if (mlbitem != null) {
                responsecode = Integer.parseInt(mlbitem.getStatusCode());
            } else {
                responsecode = -1;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mGetItemTask = null;
            showProgress(false);

            if (success) {
                if (responsecode >= 200) {
                    edtdate.setText("");
                    edtitem.setText("");
                    edtdate.setText(mlbitem.getResult().get(0)
                            .getCURRENT_DATE());
                    edtitem.setText(mlbitem.getResult().get(0).getITEM_NAME());
                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

    public class DataGetReasonTask extends AsyncTask<Void, Void, Boolean> {

        DataGetReasonTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mlblconcessionreasonlist = DataAccessManager.getlbconcessionreason(mContext);

            if (mlblconcessionreasonlist != null) {
                responsecode = Integer.parseInt(mlblconcessionreasonlist.getStatusCode());
            } else {
                responsecode = -1;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mGetItemTask = null;
            showProgress(false);

            if (success) {
                if (responsecode >= 200) {
                    //edtdate.setText("");
                    edtreason.setText("");
                    edtreason.setText(mlblconcessionreasonlist.getResult().get(0).getREASON());
                } else {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
           showProgress(false);
        }

    }


}