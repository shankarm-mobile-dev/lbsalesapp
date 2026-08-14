package com.suguna.lbsalesperson.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suguna.lbsalesperson.Database.MyDatabase;
import com.suguna.lbsalesperson.POJO.SugOrderSummary_POJO;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;

import java.util.ArrayList;
import java.util.List;

public class SugOrderSales_DAO  {
    public static final String KEY_NAME = "NAME";
    public static final String KEY_CODE = "CODE";
    public static final String KEY_TODAY = "TODAY";
    public static final String KEY_LASTWK = "LASTWK";
    public static final String KEY_LAST5WK = "LAST5WK";
    public static final String KEY_DRILLDOWN = "DRILLDOWN";
    public static final String KEY_TODAYAVG = "TODAYAVG";
    public static final String KEY_LASTWKAVG = "LASTWKAVG";
    public static final String KEY_LAST5WKAVG= "LAST5WKAVG";
    public static final String KEY_BRANCHNAME = "BRANCHNAME";

    private static final String DATABASE_TABLE = "OrderSales";
    MyDatabase mydb;
    SQLiteDatabase db;

    // class importer
    public SugOrderSales_DAO(Context ctxt) {
        if (mydb == null) {
            mydb = new MyDatabase(ctxt);
        }
    }

    public void open() {
        db = mydb.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }
    /**
     * Bulk Insert Ls Login Details on Download Button
     *
     * @return
     */
    public long bulkinsertItem(List<SugOrderSummary_POJO> lslogindetails) {
        ContentValues initialValues = null;
        long rtn = 0;

        try {
            open();
            for (int i = 0; i < lslogindetails.size(); i++) {
                SugOrderSummary_POJO lslogin_pojo = lslogindetails.get(i);
                initialValues = new ContentValues();
                initialValues.put(KEY_NAME, lslogin_pojo.getNAME());
                initialValues.put(KEY_CODE, lslogin_pojo.getCODE());
                initialValues.put(KEY_DRILLDOWN, lslogin_pojo.getDRILLDOWN());
                initialValues.put(KEY_TODAY, lslogin_pojo.getTODAY());
                initialValues.put(KEY_LASTWK, lslogin_pojo.getLASTWK());
                initialValues.put(KEY_LAST5WK, lslogin_pojo.getLAST5WK());

                initialValues.put(KEY_TODAYAVG, lslogin_pojo.getTODAYAVG());
                initialValues.put(KEY_LASTWKAVG, lslogin_pojo.getLASTWKAVG());
                initialValues.put(KEY_LAST5WKAVG, lslogin_pojo.getLAST5WKAVG());
                initialValues.put(KEY_BRANCHNAME, lslogin_pojo.getBRANCHNAME());

                rtn = db.insert(DATABASE_TABLE, null, initialValues);
            }
            // db.execSQL("insert into Lslogin values('000','Default-orgname','Default-branchname','123456','Default-Login','123456','N',date('now'),'Yes','N','0000');");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return rtn;
    }

    /**
     * It will Delete all row in the Temporary Mortality table
     *
     * @return
     */
    public boolean deleteAll() {
        boolean bol_rtn = false;

        try {
            open();
            bol_rtn = db.delete(DATABASE_TABLE, null, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }

        return bol_rtn;
    }
    public List<SugOrderSummary_POJO> getvalues() {
        // TODO Auto-generated method stub
        List<SugOrderSummary_POJO> gradingitem = new ArrayList<SugOrderSummary_POJO>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE +"";
        Cursor cursor = null;
        open();
        db = mydb.getReadableDatabase();

        try {
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            SugOrderSummary_POJO  CVvalues_POJO;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CVvalues_POJO = new SugOrderSummary_POJO();
                    CVvalues_POJO.setNAME(cursor.getString(0));
                    CVvalues_POJO.setCODE(cursor.getString(1));
                    CVvalues_POJO.setTODAY(cursor.getString(2));
                    CVvalues_POJO.setLASTWK(cursor.getString(3));
                    CVvalues_POJO.setLAST5WK(cursor.getString(4));
                    CVvalues_POJO.setDRILLDOWN(cursor.getString(5));
                    CVvalues_POJO.setTODAYAVG(cursor.getString(6));
                    CVvalues_POJO.setLASTWKAVG(cursor.getString(7));
                    CVvalues_POJO.setLAST5WKAVG(cursor.getString(8));
                    CVvalues_POJO.setBRANCHNAME(cursor.getString(9));



                    gradingitem.add(CVvalues_POJO);


                } while (cursor.moveToNext());
            }

            // closing connection
        } catch (Exception e) {
            Log.e("Get AllLineCodes", e.getMessage());
        }

        close();

        // returning lables
        return gradingitem;
    }

    public List<SugOrderSummary_POJO> getvaluessearch(String search) {
        // TODO Auto-generated method stub
        List<SugOrderSummary_POJO> gradingitem = new ArrayList<SugOrderSummary_POJO>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE + " where  NAME LIKE'%"+search+"%'";
        Cursor cursor = null;
        open();
        db = mydb.getReadableDatabase();

        try {
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            SugOrderSummary_POJO  CVvalues_POJO;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CVvalues_POJO = new SugOrderSummary_POJO();
                    CVvalues_POJO.setNAME(cursor.getString(0));
                    CVvalues_POJO.setCODE(cursor.getString(1));
                    CVvalues_POJO.setTODAY(cursor.getString(2));
                    CVvalues_POJO.setLASTWK(cursor.getString(3));
                    CVvalues_POJO.setLAST5WK(cursor.getString(4));
                    CVvalues_POJO.setDRILLDOWN(cursor.getString(5));
                    CVvalues_POJO.setTODAYAVG(cursor.getString(6));
                    CVvalues_POJO.setLASTWKAVG(cursor.getString(7));
                    CVvalues_POJO.setLAST5WKAVG(cursor.getString(8));
                    CVvalues_POJO.setBRANCHNAME(cursor.getString(9));



                    gradingitem.add(CVvalues_POJO);


                } while (cursor.moveToNext());
            }

            // closing connection
        } catch (Exception e) {
            Log.e("Get AllLineCodes", e.getMessage());
        }

        close();

        // returning lables
        return gradingitem;
    }


}
