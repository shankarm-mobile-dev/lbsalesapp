package com.suguna.lbsalesperson.priceallocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.suguna.lbsalesperson.R;

import com.suguna.lbsalesperson.Sendreceiver.WebserviceURLs;
import com.suguna.lbsalesperson.Utils.LBConstant;
import com.suguna.lbsalesperson.Utils.LocalStorageSP;
import com.suguna.lbsalesperson.base.BaseActivity;
import com.suguna.lbsalesperson.priceallocation.model.difflblist;
import com.suguna.lbsalesperson.priceallocation.model.difflbprice;
import com.suguna.lbsalesperson.priceallocation.model.lbitemlist;
import com.suguna.lbsalesperson.priceallocation.model.lblconcessionreasonlist;
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet;
import com.suguna.lbsalesperson.priceallocation.model.lbpricedetlist;
import com.suguna.lbsalesperson.priceallocation.model.lbpricemaxlist;
import com.suguna.lbsalesperson.priceallocation.model.lbsavelist;
import com.suguna.lbsalesperson.priceallocation.model.response;


import org.apache.commons.lang3.StringEscapeUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
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

public class DataAccessManager {

    public static String LOG_TAG = "com.suguna.lbprice";
    public static String baseUrl = "";
    public static WebserviceURLs wsurl;
   //public static final String SERVER_URL = "http://10.5.1.228/lborderappWs/index.php/api/"; // DEV
   //public static final String SERVER_URL = "http://10.11.1.124:8090/api/"; // shankar system new java webservice
   //  public static final String SERVER_URL = "https://salesapps.sgitsys.com:8443/lborder2/api/"; // PROD
    public static final String SERVER_URL = "http://10.5.1.227:8080/lborder2/api/"; // local server
    //public static final String SERVER_URL = "https://bankuat.sgitsys.com:8443/lborder/api/";
    public static final String LOGIN_URL = SERVER_URL + "pricedeclaration/login";
    public static final String LB_PRICE_URL = SERVER_URL + "pricedeclaration/getlbdet";
    public static final String MASTER_ITEMS_URL = SERVER_URL + "pricedeclaration/lbitem";
    public static final String LB_CON_PRICE_URL = SERVER_URL + "pricedeclaration/getlbconcessiondet";
    public static final String LB_PRICE_SURL = SERVER_URL + "pricedeclaration/savelbprice";
    public static final String LB_PRICE_SURL_NEW = SERVER_URL + "pricedeclaration/savelbprice_new";
    public static final int INSTALLED_APK_VERSION = 9;
    public static String responsemessage = "";
    public static final String VERSION_CHK_URL = SERVER_URL + "pricedeclaration/getlappversion";

    public static final String MASTER_CONCESSION_REASON_URL = SERVER_URL + "pricedeclaration/lbconcessionreason";
    public static final String MASTER_DIFFPRICELIST = SERVER_URL + "pricedeclaration/get_pricelist_declare";
    public static final String LB_PRICE_URL_NEW = SERVER_URL + "pricedeclaration/getlbdetnew";
    public static final String MASTER_MAXPRICE_URL = SERVER_URL + "pricedeclaration/lbpricemax";
    // + "
    // public static final String VERSION_CHK_URL = "";

    /*public static int login(String user, String pass, String imei) {

        Log.d(LOG_TAG, imei);
        response mresponse;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";

        try {

            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", user);
            jsonobj.put("password", pass);
            jsonobj.put("imei", imei);
            json = jsonobj.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(LOGIN_URL);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");

        try {
            HttpResponse response = client.execute(request);
            // responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                Userinfo.setUsername(user);
                Userinfo.setPassword(pass);
                Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    Log.d("com.suguna.lbprice", "hai" + returnStr);

                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<response>() {
                    }.getType());

                    JSONObject jObj = new JSONObject(returnStr);
                    String userID = jObj.getString("UserId");
                    String ledgerID = jObj.getString("LedgerId");
                    String statusCode = jObj.getString("StatusCode");
                    if (mresponse != null) {

                        responseCode = Integer.parseInt(statusCode);
                        // Userinfo.setToken(mresponse.getToken());
                        Userinfo.setUserid(userID);
                        Userinfo.setLedgerid(ledgerID);
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return responseCode;
    }*/

    public static lbpricedetlist getlbdet(String lb_Date, String PriceType,Context aContext) {
        lbpricedetlist mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            jsonobj.put("lb_date", lb_Date);
            json = jsonobj.toString();
            Log.e("json",jsonobj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = null;
        if (PriceType == "BASE")
            request = new HttpPost(LB_PRICE_URL);
        else
            request = new HttpPost(LB_CON_PRICE_URL);

        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);
                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");
                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());
                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<lbpricedetlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                        //Userinfo.setToken(mresponse.getToken());
                    }
                    Log.d(LOG_TAG, returnStr);
                    Log.e("json",returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return mresponse;
    }

    public static lbpricedetlist savelbprice(String trx_date, String itemcode, String uom,
                                             ArrayList<lbpricedet> itemlist, String price_type, String concessionreason,Context aContext) {

        lbpricedetlist mresponse = null;
        StringBuilder builder = new StringBuilder();
        int responseCode = 0;
        HttpClient client = new DefaultHttpClient();
        String json = "";
        lbsavelist locallbsavelist = new lbsavelist();

        locallbsavelist.setPricetype(price_type);
        locallbsavelist.setUsername(LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
        locallbsavelist.setImei(LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
        locallbsavelist.setUserid( LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
        locallbsavelist.setTrx_date(trx_date);
        locallbsavelist.setItemcode(itemcode);
        locallbsavelist.setUom(uom);
        locallbsavelist.setResult(itemlist);
        locallbsavelist.setLedgerid("1001");

        if (concessionreason != "")
            locallbsavelist.setReason(concessionreason);
        else
            locallbsavelist.setReason("");

        Gson gson = new Gson();
        json = gson.toJson(locallbsavelist);
         Log.e("json",json);
        HttpPost request = new HttpPost(LB_PRICE_SURL);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    mresponse = gson.fromJson(returnStr, new TypeToken<lbpricedetlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                       // Userinfo.setToken(mresponse.getToken());
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }

        Log.d(LOG_TAG, json);

        return mresponse;
    }

    public static lbitemlist getlbitem(String pricetype,Context aContext) {
        lbitemlist mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            jsonobj.put("pricetype", pricetype);
            json = jsonobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(MASTER_ITEMS_URL);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<lbitemlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return mresponse;
    }


    // 04-APR-2016 FOR Concession
    public static lblconcessionreasonlist getlbconcessionreason(Context aContext) {
        lblconcessionreasonlist mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            json = jsonobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(MASTER_CONCESSION_REASON_URL);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<lblconcessionreasonlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        Log.d(LOG_TAG, mresponse.getMessage());
        return mresponse;
    }
    public static difflbprice lbdeclare_prod_list(String pricetype, Context aContext) {
        difflbprice mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            jsonobj.put("pricetype", pricetype);
            json = jsonobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(MASTER_DIFFPRICELIST);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<difflbprice>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                    }
                    Log.e(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return mresponse;
    }

    @SuppressLint("SuspiciousIndentation")
    public static lbpricedetlist savelbprice_new(String trx_date, String itemcode, String uom,
                                                 ArrayList<lbpricedet> itemlist, String price_type, String concessionreason, Context aContext) {

        lbpricedetlist mresponse = null;
        StringBuilder builder = new StringBuilder();
        int responseCode = 0;
        HttpClient client = new DefaultHttpClient();
        String json = "";
        lbsavelist locallbsavelist = new lbsavelist();

        locallbsavelist.setPricetype(price_type);
        locallbsavelist.setUsername(LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
        locallbsavelist.setImei(LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
        locallbsavelist.setUserid( LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
        locallbsavelist.setTrx_date(trx_date);
        locallbsavelist.setItemcode(itemcode);
        locallbsavelist.setUom(uom);
        locallbsavelist.setResult(itemlist);
        locallbsavelist.setLedgerid("2025");

        if (concessionreason != "")
            locallbsavelist.setReason(concessionreason);
        else
            locallbsavelist.setReason("");

        Gson gson = new Gson();
        json = gson.toJson(locallbsavelist);
            Log.e("json",json.toString());
        HttpPost request = new HttpPost(LB_PRICE_SURL_NEW);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    mresponse = gson.fromJson(returnStr, new TypeToken<lbpricedetlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                        // Userinfo.setToken(mresponse.getToken());
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }

        Log.d(LOG_TAG, json);

        return mresponse;
    }

    public static lbpricedetlist getlbdet_new(String lb_Date, String PriceType,Context aContext) {
        lbpricedetlist mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            jsonobj.put("lb_date", lb_Date);
            json = jsonobj.toString();
            Log.e("json",jsonobj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = null;
        if (PriceType == "BASE")
            request = new HttpPost(LB_PRICE_URL_NEW);
        else
            request = new HttpPost(LB_CON_PRICE_URL);

        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());

                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<lbpricedetlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                        //Userinfo.setToken(mresponse.getToken());
                    }
                    Log.d(LOG_TAG, returnStr);
                    Log.e("json",returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return mresponse;
    }

    public static lbpricemaxlist getlbpricemax(String pricetype, Context aContext) {
        lbpricemaxlist mresponse = null;
        int responseCode = 0;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        String json = "";
        try {
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("username", LocalStorageSP.INSTANCE.getLoginUser(aContext).getUSERNAME());
            jsonobj.put("imei", LocalStorageSP.INSTANCE.get(aContext, aContext.getString(R.string.device_id), ""));
            jsonobj.put("pricetype", pricetype);
            json = jsonobj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpPost request = new HttpPost(MASTER_MAXPRICE_URL);
        try {
            StringEntity se = new StringEntity(json);
            request.setEntity(se);
        } catch (UnsupportedEncodingException ex1) {
            ex1.printStackTrace();
        }

        request.setHeader("accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + LocalStorageSP.INSTANCE.getLoginUser(aContext).getTKEY());
        try {
            HttpResponse response = client.execute(request);
            responseCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == 401) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // Userinfo.setUsername(user);
                // Userinfo.setPassword(pass);
                // Userinfo.setImei(imei);

                InputStream inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                for (String line = null; (line = bufferedReader.readLine()) != null; ) {
                    builder.append(line).append("\n");

                    String returnStr = StringEscapeUtils.unescapeJava(builder.toString());
                    Log.d(LOG_TAG, returnStr);
                    Gson gson = new Gson();
                    mresponse = gson.fromJson(returnStr, new TypeToken<lbpricemaxlist>() {
                    }.getType());
                    if (mresponse != null) {
                        responseCode = Integer.parseInt(mresponse.getStatusCode());
                    }
                    Log.d(LOG_TAG, returnStr);
                }
            }
        } catch (Exception ex) {
            Log.d(LOG_TAG, ex.getMessage());
        }
        return mresponse;
    }
}
