package com.suguna.lbsalesperson.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suguna.lbsalesperson.Database.MyDatabase;
import com.suguna.lbsalesperson.POJO.SugLogin_POJO;
import com.suguna.lbsalesperson.POJO.SugOutStanding_POJO;

import java.util.ArrayList;
import java.util.List;

public class SugOutStanding_DAO  {
    public static final String KEY_NAME = "NAME";
    public static final String KEY_PRICE = "PRICE";
    public static final String KEY_FOLLOWING = "FOLLOWING";
    public static final String KEY_CODE = "CODE";
    public static final String KEY_DRILLDOWN = "DRILLDOWN";


    private static final String DATABASE_TABLE = "Outstanding";
    MyDatabase mydb;
    SQLiteDatabase db;

    // class importer
    public SugOutStanding_DAO(Context ctxt) {
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
    public long bulkinsertItem(List<SugOutStanding_POJO> lslogindetails) {
        ContentValues initialValues = null;
        long rtn = 0;

        try {
            open();
            for (int i = 0; i < lslogindetails.size(); i++) {
                SugOutStanding_POJO lslogin_pojo = lslogindetails.get(i);
                initialValues = new ContentValues();
                initialValues.put(KEY_NAME, lslogin_pojo.getNAME());
                initialValues.put(KEY_CODE, lslogin_pojo.getCODE());
                initialValues.put(KEY_DRILLDOWN, lslogin_pojo.getDRILLDOWN());
                initialValues.put(KEY_FOLLOWING, lslogin_pojo.getFOLLOWING());
                initialValues.put(KEY_PRICE, lslogin_pojo.getPRICE());
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
    public List<SugOutStanding_POJO> getvalues() {
        // TODO Auto-generated method stub
        List<SugOutStanding_POJO> gradingitem = new ArrayList<SugOutStanding_POJO>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE +"";
        Cursor cursor = null;
        open();
        db = mydb.getReadableDatabase();

        try {
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            SugOutStanding_POJO  CVvalues_POJO;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CVvalues_POJO = new SugOutStanding_POJO();
                    CVvalues_POJO.setNAME(cursor.getString(0));
                    CVvalues_POJO.setPRICE(cursor.getString(1));
                    CVvalues_POJO.setFOLLOWING(cursor.getString(2));
                    CVvalues_POJO.setCODE(cursor.getString(3));
                    CVvalues_POJO.setDRILLDOWN(cursor.getString(4));



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

    public List<SugOutStanding_POJO> getvaluessearch(String search) {
        // TODO Auto-generated method stub
        List<SugOutStanding_POJO> gradingitem = new ArrayList<SugOutStanding_POJO>();


        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE + " where  NAME LIKE'%"+search+"%'";
        Cursor cursor = null;
        open();
        db = mydb.getReadableDatabase();

        try {
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            SugOutStanding_POJO  CVvalues_POJO;
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CVvalues_POJO = new SugOutStanding_POJO();
                    CVvalues_POJO.setNAME(cursor.getString(0));
                    CVvalues_POJO.setPRICE(cursor.getString(1));
                    CVvalues_POJO.setFOLLOWING(cursor.getString(2));
                    CVvalues_POJO.setCODE(cursor.getString(3));
                    CVvalues_POJO.setDRILLDOWN(cursor.getString(4));



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
