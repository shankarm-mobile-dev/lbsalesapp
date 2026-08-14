package com.suguna.lbsalesperson.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.suguna.lbsalesperson.POJO.ResultPOJO;
import com.suguna.lbsalesperson.POJO.SUGCustDashboard_POJO;
import com.suguna.lbsalesperson.POJO.SUGRegionDashboard_POJO;
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity;
import com.suguna.lbsalesperson.POJO.SugFeedBackDetail_POJO;
import com.suguna.lbsalesperson.POJO.SugFeedback_POJO;
import com.suguna.lbsalesperson.POJO.SugLogin_POJO;
import com.suguna.lbsalesperson.POJO.SugOrderSummary_POJO;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;
import com.suguna.lbsalesperson.POJO.SugPrice_POJO;
import com.suguna.lbsalesperson.POJO.SugRegister_POJO;
import com.suguna.lbsalesperson.POJO.User;
import com.suguna.lbsalesperson.Sendreceiver.WebserviceURLs;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;
import com.suguna.lbsalesperson.feedback.model.FeedbackResponseModel;
import com.truelife.api.AppServices;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUpDown {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String APPUPDATE_NAMESPACE = "http://ws.rdws.suguna.groups.appupdate/";
    private static String URL, LBURL, LBREPORTURL;
    @SuppressLint("SdCardPath")
    private static final String INTERNALDBURL = "/data/data/com.sugunaapps.retaildelivery/databases/bfpa_db";
    @SuppressWarnings("unused")
    private Context ctxt;
    // private DatabaseUpDownDAO databaseupdown_dao;
    private WebserviceURLs wsurl;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'HH:mm:ss");
    public static int xmlLong;

    public DatabaseUpDown(Context ctxt) {
        this.ctxt = ctxt;

        wsurl = new WebserviceURLs();
        URL = wsurl.getUpDownWs();
        LBURL = wsurl.getHatcheryWs();
        LBREPORTURL = wsurl.getHatcheryReportWs();
        // URL = wsurl.getUpDownWs();

    }


    public String get_reponse_withoutHeader(String URL,String JSON)
    {
        String line = "";
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(URL);
        Log.e("URL:", URL);
        try {

            StringEntity se = new StringEntity(JSON, "UTF-8");
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());

        } catch (ClientProtocolException e) {

            // TODO Auto-generated catch block
        } catch (IOException e) {

            // TODO Auto-generated catch block
        }
        return line;
    }

    public String get_reponse_withHeader(String URL,String JSON,String TKEY)
    {
        String line = "";
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(URL);
        Log.e("URL:", URL);
        try {
            StringEntity se = new StringEntity(JSON, "UTF-8");
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            httppost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + TKEY);
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());
        } catch (ClientProtocolException e) {

            // TODO Auto-generated catch block
        } catch (IOException e) {

            // TODO Auto-generated catch block
        }
        return line;
    }

    public List<SugLogin_POJO> get_login_details_server(String username, String password, String deviceid, String androidid
    ) {
        // TODO Auto-generated method stub
        final String METHOD_NAME = "auth/login";
        //final String METHOD_NAME = "loginemp";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugLogin_POJO> lsloginPOJOlist = new
                ArrayList<SugLogin_POJO>();
        try {
           jsonObject.accumulate("username", username);
            jsonObject.accumulate("password", password);
           jsonObject.accumulate("deviceid", deviceid);
            jsonObject.accumulate("androidid", androidid);
            jsonObject.accumulate("os", "0");
            jsonObject.accumulate("usertype", "EMPLOYEE");
       } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);
        try { // Locate the array name in`
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("Successfully")) {
                SugLogin_POJO lsloginPOJO;
                lsloginPOJO = new SugLogin_POJO();
                lsloginPOJO.setStatus(object .getString("STATUS"));
                lsloginPOJO.setUSERNAME(object.getString("USERNAME"));
                lsloginPOJO.setUSERDES(object.getString("USERDES"));
                lsloginPOJO.setUSERID(object.getString("USERID"));
                lsloginPOJO.setTKEY(object.getString("TKEY"));
                lsloginPOJO.setmUserType(object.getString("USERTYPE"));
                lsloginPOJO.setLedgerId(object.getString("LEDGER_ID"));
                lsloginPOJO.setDays(object.getString("DAYS"));
                if(object.has("PROD_NAME"))
                {
                    lsloginPOJO.setPROD_NAME(object.getString("PROD_NAME"));
                }
                else
                {
                    lsloginPOJO.setPROD_NAME("0");
                }
                lsloginPOJO.setPass_word(password);
                if (object.has("PWD_EXP")) {
                    lsloginPOJO.setPWD_EXP(object.getString("PWD_EXP"));
                   // lsloginPOJO.setPWD_EXP("0");
                }
                else
                {
                    lsloginPOJO.setPWD_EXP("0");
                }
                lsloginPOJO.setServertime(object.getString("SERVERTIME"));
                if(object.has("FARM_WISE_AREA"))
                {
                    lsloginPOJO.setFarm_wise_price_enable(object.getString("FARM_WISE_AREA"));
                }
                else
                {
                    lsloginPOJO.setFarm_wise_price_enable("0");
                }
                lsloginPOJOlist.add(lsloginPOJO);
                User user = new User();

                user.setUSERDES(lsloginPOJO.getUSERDES());
                user.setUSERID(lsloginPOJO.getUSERID());
                user.setUSERNAME(lsloginPOJO.getUSERNAME());
                user.setUSERTYPE(lsloginPOJO.getmUserType());
                user.setTKEY(lsloginPOJO.getTKEY());
                user.setPASSWORD(password);
                user.setLedgerId(lsloginPOJO.getLedgerId());
                user.setPROD_NAME(lsloginPOJO.getPROD_NAME());
                user.setFARM_WISE_AREA(lsloginPOJO.getFarm_wise_price_enable());
                LocalStorageSP.INSTANCE.storeLoginUser(ctxt, user);
            }
            else if(Status.equals("Failure"))
            {
                SugLogin_POJO lsloginPOJO;
                lsloginPOJO = new SugLogin_POJO();

                lsloginPOJO.setStatus(object .getString("STATUS"));
                lsloginPOJO.setMessage(object .getString("MESSAGE"));
                lsloginPOJO.setPass_word(password);
                if(object.has("STATUSCODE")) {
                    lsloginPOJO.setStatus_code(object.getString("STATUSCODE"));
                }
                else
                {
                    lsloginPOJO.setStatus_code("200");
                }
                lsloginPOJOlist.add(lsloginPOJO);
            }
        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;

    }

    public String get_appversion_server(String appname) {
        // TODO Auto-generated method stub
        final String METHOD_NAME = "auth/getempapp_version";
        String line = "";
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.accumulate("appname", appname);


            // jsonObject.accumulate("password", str_password);
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);

        return line;

    }

    public void updatefirebaseid(String str_username, String androidid) {
        final String METHOD_NAME = "updatefirebaseid_server";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", str_username);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("ANDRODID", androidid);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(LBURL + METHOD_NAME);
        Log.e("URL:", LBURL + METHOD_NAME);
        /*
         * httppost.addHeader( "client_key" , "a" ); httppost.addHeader(
         * "client_secret" , "b" );
         */
        try {


            //StringEntity se = new StringEntity(json, "UTF-8");
            // httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            StringEntity se = new StringEntity(json, "UTF-8");
            httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());

        } catch (ClientProtocolException e) {
            flag = false;
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
            // TODO Auto-generated catch block
        }


    }

    public List<SugRegister_POJO> get_registeredcus(String str_username) {
        final String METHOD_NAME = "auth/otp";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", str_username);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("MODE", "REGISTER");

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugRegister_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugRegister_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setSTATUS(jsonobject.getString("STATUS"));
                    lsloginPOJO.setSTATUSCODE(jsonobject.getString("STATUSCODE"));

                    lsloginPOJO.setMESSAGE(jsonobject.getString("MESSAGE"));
                    if (jsonobject.has("CUSTNAME")) {
                        lsloginPOJO.setCUSTNAME(jsonobject.getString("CUSTNAME"));
                    } else {
                        lsloginPOJO.setCUSTNAME("");
                    }

                    if (jsonobject.has("MOBILENO")) {
                        lsloginPOJO.setMOBILENO(jsonobject.getString("MOBILENO"));
                    } else {
                        lsloginPOJO.setMOBILENO("");
                    }


                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugRegister_POJO> createlogin(String custcode, String str_password, String str_otp, String deviceid, String androidid, String os) {
        final String METHOD_NAME = "auth/register";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", custcode);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("PASSWORD", str_password);
            jsonObject.accumulate("OTP", str_otp);
            jsonObject.accumulate("DEVICEID", deviceid);
            jsonObject.accumulate("ANDRODID", androidid);
            jsonObject.accumulate("OS", os);
            jsonObject.accumulate("MODE", "REGISTER");

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugRegister_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugRegister_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setSTATUS(jsonobject.getString("STATUS"));
                    lsloginPOJO.setSTATUSCODE(jsonobject.getString("STATUSCODE"));

                    lsloginPOJO.setMESSAGE(jsonobject.getString("MESSAGE"));
                    if (jsonobject.has("CUSTNAME")) {
                        lsloginPOJO.setCUSTNAME(jsonobject.getString("CUSTNAME"));
                    } else {
                        lsloginPOJO.setCUSTNAME("");
                    }

                    if (jsonobject.has("MOBILENO")) {
                        lsloginPOJO.setMOBILENO(jsonobject.getString("MOBILENO"));
                    } else {
                        lsloginPOJO.setMOBILENO("");
                    }


                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugRegister_POJO> get_forgotcus(String str_username) {
        final String METHOD_NAME = "auth/otp";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", str_username);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("MODE", "FORGOT");

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugRegister_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugRegister_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setSTATUS(jsonobject.getString("STATUS"));
                    lsloginPOJO.setSTATUSCODE(jsonobject.getString("STATUSCODE"));

                    lsloginPOJO.setMESSAGE(jsonobject.getString("MESSAGE"));
                    if (jsonobject.has("CUSTNAME")) {
                        lsloginPOJO.setCUSTNAME(jsonobject.getString("CUSTNAME"));
                    } else {
                        lsloginPOJO.setCUSTNAME("");
                    }

                    if (jsonobject.has("MOBILENO")) {
                        lsloginPOJO.setMOBILENO(jsonobject.getString("MOBILENO"));
                    } else {
                        lsloginPOJO.setMOBILENO("");
                    }


                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugRegister_POJO> updatelogin(String custcode, String str_password, String str_otp, String deviceid, String androidid, String os) {
        final String METHOD_NAME = "auth/password/update";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", custcode);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("PASSWORD", str_password);
            jsonObject.accumulate("OTP", str_otp);
            jsonObject.accumulate("DEVICEID", deviceid);
            jsonObject.accumulate("ANDRODID", androidid);
            jsonObject.accumulate("OS", os);
            jsonObject.accumulate("MODE", "FORGOT");


        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugRegister_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugRegister_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setSTATUS(jsonobject.getString("STATUS"));
                    lsloginPOJO.setSTATUSCODE(jsonobject.getString("STATUSCODE"));

                    lsloginPOJO.setMESSAGE(jsonobject.getString("MESSAGE"));
                    if (jsonobject.has("CUSTNAME")) {
                        lsloginPOJO.setCUSTNAME(jsonobject.getString("CUSTNAME"));
                    } else {
                        lsloginPOJO.setCUSTNAME("");
                    }

                    if (jsonobject.has("MOBILENO")) {
                        lsloginPOJO.setMOBILENO(jsonobject.getString("MOBILENO"));
                    } else {
                        lsloginPOJO.setMOBILENO("");
                    }


                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugPrice_POJO> get_currentpriceall_details_server(String userid, String tkey, String custnumber) {
        final String METHOD_NAME = "pricelist/get_emp_day_branchrate";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONObject jsonObject3 = new JSONObject();
        JSONObject jsonObject4 = new JSONObject();
        JSONArray jsonarray = new JSONArray();

        List<SugPrice_POJO> lsloginPOJOlist = new
                ArrayList<SugPrice_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("CUSNUM", custnumber);
            jsonObject.accumulate("EMPNUM", custnumber);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL+METHOD_NAME,json,tkey);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugPrice_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugPrice_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setPRICE_DATE(jsonobject.getString("PRICE_DATE"));
                    lsloginPOJO.setBRANCH_NAME(jsonobject.getString("BRANCH_NAME"));
                    lsloginPOJO.setUNIT_PRICE(jsonobject.getString("UNIT_PRICE"));
                    // lsloginPOJO.setRATE(jsonobject .getString("RATE"));
                    // lsloginPOJO.setFLAG(jsonobject .getString("FLAG"));


                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugFeedback_POJO> get_Feedback_details_server(String userid, String tkey, String custnumber) {
        //  final String METHOD_NAME = "get_Feebacklist";
        final String METHOD_NAME = "feedback/get_Feebackresponselist";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugFeedback_POJO> lsloginPOJOlist = new
                ArrayList<SugFeedback_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPNUM", custnumber);


        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugFeedback_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugFeedback_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setFEEDBACK_ID(jsonobject.getString("FEEDBACK_ID"));
                    lsloginPOJO.setFEEDBACK_DATE(jsonobject.getString("FEEDBACK_DATE"));
                    lsloginPOJO.setFEEDBACK_TYPE(jsonobject.getString("FEEDBACK_TYPE"));
                    lsloginPOJO.setFEEDBACK_DESC(jsonobject.getString("FEEDBACK_DESC"));
                    lsloginPOJO.setRESPONSE_STATUS(jsonobject.getString("RESPONSE_STATUS"));
                    lsloginPOJO.setCUSTOMER_NAME(jsonobject.getString("CUSTOMER_NAME"));

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugFeedBackDetail_POJO> get_Feedbackfull_details_server(String userid, String tkey, String custnumber, String id) {
        final String METHOD_NAME = "feedback/get_Feeback_det";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugFeedBackDetail_POJO> lsloginPOJOlist = new
                ArrayList<SugFeedBackDetail_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPNUM", custnumber);
            jsonObject.accumulate("FEEDBACK_ID", id);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugFeedBackDetail_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                JSONArray historyJarray = object.getJSONArray("FEEDBACK_HISTORY");

                ArrayList<FeedbackResponseModel> mResponseArray = new ArrayList<>();
                for (int i = 0; i < historyJarray.length(); i++) {
                    JSONObject jsonobject = historyJarray.getJSONObject(i);
                    FeedbackResponseModel fb = new FeedbackResponseModel();
                    fb.setName(jsonobject.getString("NAME"));
                    fb.setResponseText(jsonobject.getString("COMMENTS"));
                    fb.setUserId(jsonobject.getString("TO_USER"));
                    fb.setJob(jsonobject.getString("JOB"));

                    //  if (!fb.getUserId().equalsIgnoreCase(LocalStorageSP.INSTANCE.getLoginUser(ctxt).getUSERNAME())) {
                    mResponseArray.add(fb);
                    // }

                }
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugFeedBackDetail_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setFEEDBACK_ID(jsonobject.getString("FEEDBACK_ID"));
                    lsloginPOJO.setFEEDBACK_DATE(jsonobject.getString("FEEDBACK_DATE"));
                    lsloginPOJO.setFEEDBACK_TYPE(jsonobject.getString("FEEDBACK_TYPE"));
                    lsloginPOJO.setFEEDBACK_DESC(jsonobject.getString("FEEDBACK_DESC"));
                    lsloginPOJO.setREFERENCE_TYPE(jsonobject.getString("REFERENCE_TYPE"));

                    lsloginPOJO.setFROM_USER(jsonobject.getString("FROM_USER"));
                    lsloginPOJO.setREFERENCE_NO(jsonobject.getString("REFERENCE_NO"));
                    lsloginPOJO.setTO_USER(jsonobject.getString("TO_USER"));
                    lsloginPOJO.setREFERENCE_STATUS(jsonobject.getString("RESPONSE_STATUS"));
                    lsloginPOJO.setFINAL_COMMENTS(jsonobject.getString("FINAL_COMMENTS"));

                    lsloginPOJO.setCUSTOMER_NAME(jsonobject.getString("CUSTOMER_NAME"));
                    lsloginPOJO.setLIFTING_DATE(jsonobject.getString("LIFTING_DATE"));
                    lsloginPOJO.setLOCATION_NAME(jsonobject.getString("LOCATION_NAME"));


                    lsloginPOJO.setmResponseArray(mResponseArray);
                    if (jsonobject.has("FINAL_DATE")) {
                        lsloginPOJO.setFINAL_DATE(jsonobject.getString("FINAL_DATE"));
                    }
                    lsloginPOJO.setALLOW_UPDATE(jsonobject.getString("ALLOW_UPDATE"));
                    if (jsonobject.has("RATING")) {
                        lsloginPOJO.setRATING(jsonobject.getString("RATING"));
                    } else {
                        lsloginPOJO.setRATING(("0"));
                    }

                    lsloginPOJOlist.add(lsloginPOJO);
                }

            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugOutStanding_POJO> get_outstandingsales_details_server(String userid, String tkey, String custnumber) {
        final String METHOD_NAME = "outstanding/get_outstanding_sales";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugOutStanding_POJO> lsloginPOJOlist = new
                ArrayList<SugOutStanding_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPCODE", custnumber);
            jsonObject.accumulate("EMPNUM", custnumber);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOutStanding_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugOutStanding_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setCODE(jsonobject.getString("CODE"));
                    lsloginPOJO.setNAME(jsonobject.getString("NAME"));
                    lsloginPOJO.setPRICE(jsonobject.getString("AMOUNT"));
                    lsloginPOJO.setFOLLOWING(jsonobject.getString("FOLLOWUP"));
                    lsloginPOJO.setDRILLDOWN(jsonobject.getString("DRILLDOWN"));
                    lsloginPOJO.setCREDIT_LIMIT(jsonobject.getString("CR_LIMIT"));
                    lsloginPOJO.setOVER_DUE(jsonobject.getString("OVERDUE"));
                    if(jsonobject.has("OVERDUE_TOMORROW")){
                        lsloginPOJO.setOVERDUE_TOMORROW(jsonobject.getString("OVERDUE_TOMORROW"));}
                    else
                    {lsloginPOJO.setOVERDUE_TOMORROW(jsonobject.getString("OVERDUE"));}
                    if(jsonobject.has("OVERDUE_AFTERTOMORROW")){
                        lsloginPOJO.setOVERDUE_AFTERTOMORROW(jsonobject.getString("OVERDUE_AFTERTOMORROW"));}
                    else
                    {lsloginPOJO.setOVERDUE_AFTERTOMORROW(jsonobject.getString("OVERDUE"));}

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugOutStanding_POJO> get_outstandingbranch_details_server(String userid, String tkey, String custnumber, String BRANCHCODE) {
        final String METHOD_NAME = "outstanding/get_outstanding_branch";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugOutStanding_POJO> lsloginPOJOlist = new
                ArrayList<SugOutStanding_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPCODE", custnumber);
            jsonObject.accumulate("EMPNUM", custnumber);
            jsonObject.accumulate("BRANCHCODE", BRANCHCODE);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOutStanding_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugOutStanding_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setCODE(jsonobject.getString("CODE"));
                    lsloginPOJO.setNAME(jsonobject.getString("NAME"));
                    lsloginPOJO.setPRICE(jsonobject.getString("AMOUNT"));
                    lsloginPOJO.setFOLLOWING(jsonobject.getString("FOLLOWUP"));
                    lsloginPOJO.setDRILLDOWN(jsonobject.getString("DRILLDOWN"));
                    lsloginPOJO.setCREDIT_LIMIT(jsonobject.getString("CR_LIMIT"));
                    lsloginPOJO.setOVER_DUE(jsonobject.getString("OVERDUE"));
                    if(jsonobject.has("OVERDUE_TOMORROW")){
                    lsloginPOJO.setOVERDUE_TOMORROW(jsonobject.getString("OVERDUE_TOMORROW"));}
                    else
                    {lsloginPOJO.setOVERDUE_TOMORROW(jsonobject.getString("OVERDUE"));}
                    if(jsonobject.has("OVERDUE_AFTERTOMORROW")){
                    lsloginPOJO.setOVERDUE_AFTERTOMORROW(jsonobject.getString("OVERDUE_AFTERTOMORROW"));}
                    else
                    {lsloginPOJO.setOVERDUE_AFTERTOMORROW(jsonobject.getString("OVERDUE"));}

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<SugOrderSummary_POJO> get_ordersummary_details_server(String userid, String tkey, String custnumber) {
        final String METHOD_NAME = "ordersummary/get_ordersummary_sales";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugOrderSummary_POJO> lsloginPOJOlist = new
                ArrayList<SugOrderSummary_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPCODE", custnumber);
            jsonObject.accumulate("EMPNUM", custnumber);
            // jsonObject.accumulate("BRANCHCODE", BRANCHCODE);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL+METHOD_NAME,json,tkey);
        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOrderSummary_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugOrderSummary_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    if (jsonobject.has("CODE")) {
                        lsloginPOJO.setCODE(jsonobject.getString("CODE"));
                    } else {
                        lsloginPOJO.setCODE("");
                    }
                    lsloginPOJO.setNAME(jsonobject.getString("NAME"));
                    lsloginPOJO.setTODAY(jsonobject.getString("TODAY_ORDER_QTY"));
                    lsloginPOJO.setLASTWK(jsonobject.getString("LASTWEEK_QTY"));
                    lsloginPOJO.setLAST5WK(jsonobject.getString("LAST5WEEK_QTY"));
                    lsloginPOJO.setTODAYAVG(jsonobject.getString("TODAY_AVG_WT"));
                    lsloginPOJO.setLASTWKAVG(jsonobject.getString("LASTWEEK_AVG_WT"));
                    lsloginPOJO.setLAST5WKAVG(jsonobject.getString("LAST5WEEK_AVG_WT"));
                    lsloginPOJO.setDRILLDOWN(jsonobject.getString("DRILLDOWN"));
                    if (jsonobject.has("BRANCHNAME")) {
                        lsloginPOJO.setBRANCHNAME(jsonobject.getString("BRANCHNAME"));
                    } else {
                        lsloginPOJO.setBRANCHNAME("");
                    }

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public String save_feedback_details_server(String userid, String tkey, String custnumber, String id, String feedback) {
        //    final String METHOD_NAME = "update_feeback_status";
        final String METHOD_NAME = "update_feedback_status_emp";
        String line = "false";
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");


        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPNUM", custnumber);
            jsonObject.accumulate("FEEDBACK_ID", id);
            jsonObject.accumulate("FEEDBACK_COMMENTS", feedback);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(LBURL + METHOD_NAME);
        Log.e("URL:", LBURL + METHOD_NAME);
        /*
         * httppost.addHeader( "client_key" , "a" ); httppost.addHeader(
         * "client_secret" , "b" );
         */
        try {
            // Add your data

            // nameValuePairs.add(new BasicNameValuePair("client_secret",
            // "b"));
            StringEntity se = new StringEntity(json, "UTF-8");
            httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());

        } catch (ClientProtocolException e) {
            flag = false;
            // TODO Auto-generated catch block
        } catch (IOException e) {
            flag = false;
            // TODO Auto-generated catch block
        }


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                line = "true";
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return line;
    }

    public List<SugOrderSummary_POJO> get_ordersummarybrnach_details_server(String userid, String tkey, String custnumber, String BRANCHCODE) {
        final String METHOD_NAME = "ordersummary/get_ordersummary_branch";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugOrderSummary_POJO> lsloginPOJOlist = new
                ArrayList<SugOrderSummary_POJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPCODE", custnumber);
            jsonObject.accumulate("BRANCHCODE", BRANCHCODE);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);




        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOrderSummary_POJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new SugOrderSummary_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    if (jsonobject.has("CODE")) {
                        lsloginPOJO.setCODE(jsonobject.getString("CODE"));
                    } else {
                        lsloginPOJO.setCODE("");
                    }
                    lsloginPOJO.setNAME(jsonobject.getString("NAME"));
                    lsloginPOJO.setTODAY(jsonobject.getString("TODAY_ORDER_QTY"));
                    lsloginPOJO.setLASTWK(jsonobject.getString("LASTWEEK_QTY"));
                    lsloginPOJO.setLAST5WK(jsonobject.getString("LAST5WEEK_QTY"));
                    lsloginPOJO.setTODAYAVG(jsonobject.getString("TODAY_AVG_WT"));
                    lsloginPOJO.setLASTWKAVG(jsonobject.getString("LASTWEEK_AVG_WT"));
                    lsloginPOJO.setLAST5WKAVG(jsonobject.getString("LAST5WEEK_AVG_WT"));
                    lsloginPOJO.setDRILLDOWN(jsonobject.getString("DRILLDOWN"));
                    if (jsonobject.has("BRANCH")) {
                        lsloginPOJO.setBRANCHNAME(jsonobject.getString("BRANCH"));
                    } else {
                        lsloginPOJO.setBRANCHNAME("");
                    }

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }


    public void getRegionDetails() {
        final String METHOD_NAME = AppServices.API.INSTANCE.getGet_empbranch_allocation();
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<SugOrderSummary_POJO> lsloginPOJOlist = new
                ArrayList<SugOrderSummary_POJO>();
        try {
            jsonObject.accumulate("USERID", LocalStorageSP.INSTANCE.getLoginUser(ctxt).getUSERID());
            jsonObject.accumulate("TKEY", LocalStorageSP.INSTANCE.getLoginUser(ctxt).getTKEY());
            jsonObject.accumulate("EMPCODE", LocalStorageSP.INSTANCE.get(ctxt, FeedBackActivity.Name, ""));


        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(AppServices.API.INSTANCE.getBaseUrl() + METHOD_NAME);
        Log.e("URL:", AppServices.API.INSTANCE.getBaseUrl() + METHOD_NAME);
        /*
         * httppost.addHeader( "client_key" , "a" ); httppost.addHeader(
         * "client_secret" , "b" );
         */
        try {
            // Add your data
            // nameValuePairs.add(new BasicNameValuePair("client_secret",
            // "b"));
            StringEntity se = new StringEntity(json, "UTF-8");
            httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());

        } catch (ClientProtocolException e) {
            flag = false;
            // TODO Auto-generated catch block
        } catch (IOException e) {
            flag = false;
            // TODO Auto-generated catch block
        }


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

           /* if (Status.equals("OK")) {
                SugOrderSummary_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SugOrderSummary_POJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    if(jsonobject.has("CODE")) {
                        lsloginPOJO.setCODE(jsonobject.getString("CODE"));
                    }
                    else
                    {
                        lsloginPOJO.setCODE("");
                    }
                    lsloginPOJO.setNAME(jsonobject .getString("NAME"));
                    lsloginPOJO.setTODAY(jsonobject .getString("TODAY_ORDER_QTY"));
                    lsloginPOJO.setLASTWK(jsonobject .getString("LASTWEEK_QTY"));
                    lsloginPOJO.setLAST5WK(jsonobject .getString("LAST5WEEK_QTY"));
                    lsloginPOJO.setTODAYAVG(jsonobject .getString("TODAY_AVG_WT"));
                    lsloginPOJO.setLASTWKAVG(jsonobject .getString("LASTWEEK_AVG_WT"));
                    lsloginPOJO.setLAST5WKAVG(jsonobject .getString("LAST5WEEK_AVG_WT"));
                    lsloginPOJO.setDRILLDOWN(jsonobject .getString("DRILLDOWN"));
                    if(jsonobject.has("BRANCH")) {
                        lsloginPOJO.setBRANCHNAME(jsonobject.getString("BRANCH"));
                    }
                    else
                    {
                        lsloginPOJO.setBRANCHNAME("");
                    }

                    lsloginPOJOlist.add(lsloginPOJO); }
            }*/

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }

    }


    public List<ResultPOJO> savediffpricedeclare_server(String userid, String tkey, String custnumber, String pricename,String fromwt,String towt,String fromdate,String todate) {
        final String METHOD_NAME = "pricedeclaration/savediffpricedeclare";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<ResultPOJO> lsloginPOJOlist = new
                ArrayList<ResultPOJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPNUM", custnumber);
            jsonObject.accumulate("PROD_NAME", pricename);
            jsonObject.accumulate("FROM_WT", fromwt);
            jsonObject.accumulate("TO_WT", towt);
            jsonObject.accumulate("FROM_DATE", fromdate);
            jsonObject.accumulate("TO_DATE", todate);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
         line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                ResultPOJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new ResultPOJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setMessage(jsonobject.getString("Message"));
                    lsloginPOJO.setResponse(jsonobject.getString("Response"));
                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }

    public List<ResultPOJO> updatediffpricedeclare_server(String userid, String tkey, String custnumber, String pricename,String fromwt,String towt,String fromdate,String todate) {
        final String METHOD_NAME = "pricedeclaration/updatediffpricedeclare";
        String line = "";
        JSONObject jsonObject = new JSONObject();


        List<ResultPOJO> lsloginPOJOlist = new
                ArrayList<ResultPOJO>();
        try {
            jsonObject.accumulate("USERID", userid);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("EMPNUM", custnumber);
            jsonObject.accumulate("PROD_NAME", pricename);
            jsonObject.accumulate("FROM_WT", fromwt);
            jsonObject.accumulate("TO_WT", towt);
            jsonObject.accumulate("FROM_DATE", fromdate);
            jsonObject.accumulate("TO_DATE", todate);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                ResultPOJO lsloginPOJO;
                JSONArray Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i < Jarray.length(); i++) {
                    lsloginPOJO = new ResultPOJO();
                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setMessage(jsonobject.getString("Message"));
                    lsloginPOJO.setResponse(jsonobject.getString("Response"));

                    lsloginPOJOlist.add(lsloginPOJO);
                }
            }

        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            flag =
                    false;
            e.printStackTrace();
        }
        return lsloginPOJOlist;
    }
    public List<SugRegister_POJO> resetlogin(String str_username, String str_newpassword) {
        final String METHOD_NAME = "auth/password/reset";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugRegister_POJO> lsloginPOJOlist = new
                ArrayList<SugRegister_POJO>();
        try {
            jsonObject.accumulate("USERNAME", str_username);
            jsonObject.accumulate("APPLICATION", "LB-ORDER");
            jsonObject.accumulate("USERTYPE", "EMPLOYEE");
            jsonObject.accumulate("PASSWORD", str_newpassword);
            jsonObject.accumulate("MODE", "RESET");


        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withoutHeader(URL + METHOD_NAME,json);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugRegister_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SugRegister_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setSTATUS(jsonobject.getString("STATUS"));
                    lsloginPOJO.setSTATUSCODE(jsonobject .getString("STATUSCODE"));

                    lsloginPOJO.setMESSAGE(jsonobject.getString("MESSAGE"));
                    if (jsonobject.has("CUSTNAME")) {
                        lsloginPOJO.setCUSTNAME(jsonobject .getString("CUSTNAME"));}
                    else{lsloginPOJO.setCUSTNAME("");}

                    if (jsonobject.has("MOBILENO")) {
                        lsloginPOJO.setMOBILENO(jsonobject .getString("MOBILENO"));
                    }         else{lsloginPOJO.setMOBILENO("");}



                    lsloginPOJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return lsloginPOJOlist;
    }

    public List<SUGRegionDashboard_POJO> get_regiondashbord_dtls(String str_username, String str_userid,String tkey,String date) {
        final String METHOD_NAME = "get_regiondashbord_dtls";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SUGRegionDashboard_POJO> POJOlist = new
                ArrayList<SUGRegionDashboard_POJO>();
        try {
            jsonObject.accumulate("USERCODE", str_username);
            jsonObject.accumulate("DATE", date);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("USERID", str_userid);




        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;

        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(LBREPORTURL + METHOD_NAME);
        Log.e("URL:", LBREPORTURL + METHOD_NAME);
        /*
         * httppost.addHeader( "client_key" , "a" ); httppost.addHeader(
         * "client_secret" , "b" );
         */
        try {



            //StringEntity se = new StringEntity(json, "UTF-8");
            // httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            StringEntity se = new StringEntity(json, "UTF-8");
            httppost.setEntity(se);
            // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            String responseBody = EntityUtils.toString(response.getEntity());
            line = responseBody.toString();
            // Log.d("Http Response:", responseBody.toString());
            Log.e("Http Response:", responseBody.toString());

        } catch (ClientProtocolException e) {
            flag = false;
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
            // TODO Auto-generated catch block
        }


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SUGRegionDashboard_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SUGRegionDashboard_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setDESCRIPTION(jsonobject.getString("DESCRIPTION"));
                    lsloginPOJO.setTAG(jsonobject .getString("TAG"));

                    lsloginPOJO.setREGION_CODE(jsonobject.getString("REGION_CODE"));
                    lsloginPOJO.setBOOKED_QTY(jsonobject.getString("BOOKED_QTY"));
                    lsloginPOJO.setSCHEDULED_QTY(jsonobject.getString("SCHEDULED_QTY"));
                    lsloginPOJO.setLIFTED_QTY(jsonobject.getString("LIFTED_QTY"));
                    lsloginPOJO.setTARGET_QTY(jsonobject.getString("TARGET_QTY"));


                    POJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return POJOlist;
    }
    public List<SUGRegionDashboard_POJO> get_regiondashbordS0_dtls(String str_username, String str_userid,String tkey,String date,String region) {
        final String METHOD_NAME = "ordersummary/get_regiondashbordSO_dtls";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SUGRegionDashboard_POJO> POJOlist = new
                ArrayList<SUGRegionDashboard_POJO>();
        try {
            jsonObject.accumulate("USERCODE", str_username);
            jsonObject.accumulate("DATE", date);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("USERID", str_userid);
            jsonObject.accumulate("REGION", region);



        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SUGRegionDashboard_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SUGRegionDashboard_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setDESCRIPTION(jsonobject.getString("SO_NAME"));
                    if(jsonobject.has("JOB_TITLE")) {
                        lsloginPOJO.setTAG(jsonobject.getString("JOB_TITLE"));
                    }
                    lsloginPOJO.setREGION_CODE(jsonobject.getString("REGION_CODE"));
                    lsloginPOJO.setBOOKED_QTY(jsonobject.getString("BOOKED_QTY"));
                    lsloginPOJO.setSCHEDULED_QTY(jsonobject.getString("SCHEDULED_QTY"));
                    lsloginPOJO.setLIFTED_QTY(jsonobject.getString("LIFTED_QTY"));
                    lsloginPOJO.setTARGET_QTY(jsonobject.getString("TARGET_QTY"));
                     if(jsonobject.has("PARENT_CODE"))
                     {
                         lsloginPOJO.setPARENT_CODE(jsonobject.getString("PARENT_CODE"));
                     }

                    POJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return POJOlist;
    }

    public List<SugOrderSummary_POJO> get_regiondashbordCust_dtls(String str_username, String str_userid,String tkey,String date,String region) {
        final String METHOD_NAME = "ordersummary/get_regiondashbordcust_dtls";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugOrderSummary_POJO> POJOlist = new
                ArrayList<SugOrderSummary_POJO>();
        try {
            jsonObject.accumulate("USERCODE", str_username);
            jsonObject.accumulate("DATE", date);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("USERID", str_userid);
            jsonObject.accumulate("REGION", region);



        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);



        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOrderSummary_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SugOrderSummary_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setNAME(jsonobject.getString("CUSTOMER_NAME"));
                    lsloginPOJO.setCODE(jsonobject.getString("CUSTOMER_CODE"));
                    // lsloginPOJO.setTAG(jsonobject .getString("TAG"));

                  // lsloginPOJO.setREGION_CODE(jsonobject.getString("REGION_CODE"));
                    lsloginPOJO.setTODAY(jsonobject.getString("BOOKED_QTY"));
                    lsloginPOJO.setLASTWK(jsonobject.getString("LAST_WEEK_ORDER_KGS"));
                    lsloginPOJO.setLAST5WKAVG(jsonobject.getString("LAST_FIVE_WEEKS_SALES_KGS"));
                   // lsloginPOJO.setTARGET_QTY(jsonobject.getString("TARGET_QTY"));
                    lsloginPOJO.setBRANCHNAME(jsonobject.getString("MONTH_CLUB"));


                    POJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return POJOlist;
    }

    public List<SugOrderSummary_POJO> get_regiondashbordcustsaleper_dtls(String str_username, String str_userid,String tkey,String date,String region) {
        final String METHOD_NAME = "ordersummary/get_regiondashbordcustsaleper_dtls";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SugOrderSummary_POJO> POJOlist = new
                ArrayList<SugOrderSummary_POJO>();
        try {
            jsonObject.accumulate("USERCODE", str_username);
            jsonObject.accumulate("DATE", date);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("USERID", str_userid);
            jsonObject.accumulate("REGION", region);



        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);


        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SugOrderSummary_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SugOrderSummary_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);
                    lsloginPOJO.setNAME(jsonobject.getString("CUSTOMER_NAME"));
                    lsloginPOJO.setCODE(jsonobject.getString("CUSTOMER_CODE"));
                    // lsloginPOJO.setTAG(jsonobject .getString("TAG"));

                    // lsloginPOJO.setREGION_CODE(jsonobject.getString("REGION_CODE"));
                    lsloginPOJO.setTODAY(jsonobject.getString("BOOKED_QTY"));
                    lsloginPOJO.setLASTWK(jsonobject.getString("LAST_WEEK_ORDER_KGS"));
                    lsloginPOJO.setLAST5WKAVG(jsonobject.getString("LAST_FIVE_WEEKS_SALES_KGS"));
                     lsloginPOJO.setBRANCHNAME(jsonobject.getString("MONTH_CLUB"));


                    POJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return POJOlist;
    }

    public List<SUGRegionDashboard_POJO> get_regiondashbordmanager_dtls(String str_username, String str_userid,String tkey,String date) {
            final String METHOD_NAME = "ordersummary/get_regiondashbordmanager_dtls";
        String line = "";
        JSONObject jsonObject = new JSONObject();
        List<SUGRegionDashboard_POJO> POJOlist = new
                ArrayList<SUGRegionDashboard_POJO>();
        try {
            jsonObject.accumulate("USERCODE", str_username);
            jsonObject.accumulate("DATE", date);
            jsonObject.accumulate("TKEY", tkey);
            jsonObject.accumulate("USERID", str_userid);




        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // 4. convert JSONObject to JSON to String
        String json = jsonObject.toString();
        Log.e("json:", json);
        boolean flag = false;
        line=get_reponse_withHeader(URL + METHOD_NAME,json,tkey);




        try { // Locate the array name in
            JSONObject object = new JSONObject(line);
            String Status = object.getString("STATUS");

            if (Status.equals("OK")) {
                SUGRegionDashboard_POJO lsloginPOJO;
                JSONArray  Jarray = object.getJSONArray("RESULT");
                for (int i = 0; i <  Jarray.length(); i++)
                {
                    lsloginPOJO = new SUGRegionDashboard_POJO();

                    JSONObject jsonobject = Jarray.getJSONObject(i);

                    lsloginPOJO.setDESCRIPTION(jsonobject.getString("DESCRIPTION"));
                    lsloginPOJO.setTAG(jsonobject .getString("TAG"));

                    lsloginPOJO.setREGION_CODE(jsonobject.getString("REGION_CODE"));
                    lsloginPOJO.setBOOKED_QTY(jsonobject.getString("BOOKED_QTY"));
                    lsloginPOJO.setSCHEDULED_QTY(jsonobject.getString("SCHEDULED_QTY"));
                    lsloginPOJO.setLIFTED_QTY(jsonobject.getString("LIFTED_QTY"));
                    lsloginPOJO.setTARGET_QTY(jsonobject.getString("TARGET_QTY"));


                    POJOlist.add(lsloginPOJO); } }

        } catch (JSONException e) { Log.e("Error", e.getMessage()); flag =
                false; e.printStackTrace(); }
        return POJOlist;
    }
}
