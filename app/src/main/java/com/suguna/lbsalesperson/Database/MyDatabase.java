package com.suguna.lbsalesperson.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.suguna.lbsalesperson.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MyDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "lbsalesperson_db";
	private static final int DATABASE_VERSION = 1; // Version released on
	// 28-04-2016
	private SQLiteDatabase db;
	private final Context myContext;

	public MyDatabase(Context ctxt) {
		super(ctxt, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = ctxt;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			executeCreateSQLScript(database);
			checkDB();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void checkDB() {
		// TODO Auto-generated method stub
		int version = db.getVersion();
		if (version == 1) {
			db.beginTransaction();
			try {
				onUpgrade(db, version, 1);
				db.setVersion(1);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
		try {

			executeUpdateSQLScript(db);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void executeCreateSQLScript(SQLiteDatabase database) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		InputStream inputStream = myContext.getResources().openRawResource(
				R.raw.sugunabfm_db);
		// myContext.getResources().openRawResource(R.raw.sugunabfm_db);
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}

			outputStream.close();
			inputStream.close();

			String[] createScript = outputStream.toString().split(";");

			for (int i = 0; i < createScript.length; i++) {
				String sqlStatement = createScript[i].trim();

				if (sqlStatement.length() > 0) {
					database.execSQL(sqlStatement + ";");
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	private void executeUpdateSQLScript(SQLiteDatabase database) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len;
		InputStream inputStream = // myContext.getResources().openRawResource(R.raw.sugunabfm_db);
		myContext.getResources().openRawResource(R.raw.sugunabfm_db_upgrade);

		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}

			outputStream.close();
			inputStream.close();

			String[] createScript = outputStream.toString().split(";");

			for (int i = 0; i < createScript.length; i++) {
				String sqlStatement = createScript[i].trim();

				if (sqlStatement.length() > 0) {
					database.execSQL(sqlStatement + ";");
				}
			}
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
