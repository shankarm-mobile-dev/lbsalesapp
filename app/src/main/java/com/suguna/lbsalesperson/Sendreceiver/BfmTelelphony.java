/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suguna.lbsalesperson.Sendreceiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

public class BfmTelelphony {

    private Context context;
    private TelephonyManager telephonymanager;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String DeviceID = "deviceid";
    SharedPreferences sharedpreferences;
    String deviceId="";
    public BfmTelelphony(Context _context) {
        this.context = _context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(DeviceID))
        {
            deviceId=sharedpreferences.getString(DeviceID, "");
        }
    }

    /**
     * return Imei(International Mobile Equipment Identity), may be return null.
     *
     * @return
     */

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public String getIMEINumber() {
        String IMEI="";

       /* IMEI = Settings.Secure.getString(this.context.getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
     telephonymanager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);

       if (null == telephonymanager)
        {
            return "error";
        }
        if (null != telephonymanager)
        {
            //return "noerror";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null == telephonymanager.getImei() || 0 == telephonymanager.getImei().length()) {
                if(deviceId.equals(""))
                {
                    //return Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    return telephonymanager.getImei();
                }
                else
                {
                    return deviceId;
                }
            }
            else {
                return telephonymanager.getDeviceId();
            }

        } else {
            return telephonymanager.getDeviceId();
        }

    //return IMEI;

    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public String getAndroidID() {
        String IMEI="";

       telephonymanager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);

        if (null == telephonymanager)
        {
            return "error";
        }
        if (null != telephonymanager)
        {
            //return "noerror";
        }

        return Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }
    public String getAndroidos() {
        String IMEI="";

        IMEI=android.os.Build.VERSION.RELEASE;
        return IMEI;
      //  return Settings.Secure.getString(this.context.getContentResolver(), Settings.Secure.ANDROID_ID);

    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public Boolean CheckIMEINumber() {
        String IMEI="";
       //* IMEI = Settings.Secure.getString(this.context.getContentResolver(),
          //      Settings.Secure.ANDROID_ID);//*
        telephonymanager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);

        if (null == telephonymanager)
        {
            return false;
        }
        if (null != telephonymanager)
        {
            //return "noerror";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (null == telephonymanager.getImei() || 0 == telephonymanager.getImei().length()) {
                return false;
            }
            else {
                return true;
            }

        } else {
            return true;
        }

    }


    @SuppressLint("MissingPermission")
    public String getSIMNumber() {
        String SIM;
        telephonymanager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
      // SIM = telephonymanager.getDeviceId();
         SIM=telephonymanager.getLine1Number();
      // SIM="352602083214020";
        if (android.os.Build.VERSION.RELEASE.startsWith("4.4")) {
            return SIM;
        } else {
            return getNonKitKatIMEINumber();
        }
    }

    @SuppressWarnings("unused")
	public String getNonKitKatIMEINumber() {
      /*  TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(context);
        String imeiSIM1 = telephonyInfo.getImeiSIM1();
        String imeiSIM2 = telephonyInfo.getImeiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        boolean isDualSIM = telephonyInfo.isDualSIM();
      // imeiSIM1= "911661502549428";
     //  imeiSIM2= "911661502549428";
        if (imeiSIM1.length() >= 1) {
            return imeiSIM1;
        } else {
            return imeiSIM2;
        }
*/
    return null;
    }

}

