package com.suguna.lbsalesperson.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.suguna.lbsalesperson.Database.MyDatabase;
import com.suguna.lbsalesperson.POJO.SugLogin_POJO;

import java.util.List;

public class Sug_lblogin {
    public static final String KEY_USERID = "USERID";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_Pass_word = "Pass_word";
    public static final String KEY_TKEY = "TKEY";
    public static final String KEY_USERDES = "USERDES";


    private static final String DATABASE_TABLE = "lblogin";
    MyDatabase mydb;
    SQLiteDatabase db;

    // class importer
    public Sug_lblogin(Context ctxt) {
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
    public long bulkinsertItem(List<SugLogin_POJO> lslogindetails) {
        ContentValues initialValues = null;
        long rtn = 0;

        try {
            open();
            for (int i = 0; i < lslogindetails.size(); i++) {
                SugLogin_POJO lslogin_pojo = lslogindetails.get(i);
                initialValues = new ContentValues();
                initialValues.put(KEY_USERID, lslogin_pojo.getUSERID());
                initialValues.put(KEY_USERNAME, lslogin_pojo.getUSERNAME());
                initialValues.put(KEY_Pass_word, lslogin_pojo.getPass_word());
                initialValues.put(KEY_TKEY, lslogin_pojo.getTKEY());
                initialValues.put(KEY_USERDES, lslogin_pojo.getUSERDES());
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

    public String getUSERDES(String lscode) {
        String orgID = "";
        String selectQuery = "SELECT USERDES FROM lblogin where USERNAME='"
                + lscode + "'";
        Cursor cursor = null;

        try {
            db = mydb.getReadableDatabase();

            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    orgID = cursor.getString(0);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Log.e("Get USERDES ", e.getMessage());
        } finally {
            close();
        }
        return orgID;
    }

    public String getUSERID(String lscode) {
        String orgID = "";
        String selectQuery = "SELECT USERID FROM lblogin where USERNAME='"
                + lscode + "'";
        Cursor cursor = null;

        try {
            db = mydb.getReadableDatabase();

            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    orgID = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Get USERDES ", e.getMessage());
        } finally {
            close();
        }
        return orgID;
    }

    public String getTKEY(String lscode) {
        String orgID = "";
        String selectQuery = "SELECT TKEY FROM lblogin where USERNAME='"
                + lscode + "'";
        Cursor cursor = null;
        try {
            db = mydb.getReadableDatabase();
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    orgID = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Get USERDES ", e.getMessage());
        } finally {
            close();
        }
        return orgID;
    }
    public String getCUSTNAME(String lscode) {
        String orgID = "";
        String selectQuery = "SELECT USERDES FROM lblogin where USERNAME='"
                + lscode + "'";
        Cursor cursor = null;
        try {
            db = mydb.getReadableDatabase();
            if (cursor == null) {
                cursor = db.rawQuery(selectQuery, null);
            }
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    orgID = cursor.getString(0);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Get USERDES ", e.getMessage());
        } finally {
            close();
        }
        return orgID;
    }
}
