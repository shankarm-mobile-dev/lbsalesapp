/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suguna.lbsalesperson.Utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.suguna.lbsalesperson.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class ICaster {
	// edittext to String

	public static final int error = 0;
	public static final int notify = 1;

	public static String getEt_String(EditText ip) {
		String op = "";

		try {
			op = ip.getText().toString().trim();
		} catch (Exception e) {
			return "";
		}

		return op;
	}

	// object to integer
	public static Integer getEt_integer(EditText ip) {
		int op = 0;

		try {
			op = Integer.parseInt(ip.getText().toString());
		} catch (Exception e) {
			return 0;
		}

		return op;
	}

	// object to long
	public static long getEt_long(EditText ip) {
		long op = 0;

		try {
			op = Long.parseLong(ip.getText().toString());
		} catch (Exception e) {
			return 0;
		}

		return op;
	}

	// object to double
	public static Float getEt_float(EditText ip) {
		float op = 0.00f;

		try {
			op = Float.parseFloat(ip.getText().toString());
		} catch (Exception e) {
			return 0.00f;
		}

		return op;
	}

	// object to double
	public static Double getEt_double(EditText ip) {
		double op = 0.00;

		try {
			op = Double.parseDouble(ip.getText().toString());
		} catch (Exception e) {
			return 0.00;
		}

		return op;
	}

	/**
	 * return default value when ip is null
	 */
	public static Double getEt_Num_Default(EditText ip) {
		double op = 0.00;

		try {
			op = Double.parseDouble(ip.getText().toString());
		} catch (Exception e) {
			return 0.00;
		}

		return op;
	}

	/**
	 * return default value when ip is null
	 */
	public static String getEt_Str_Default(EditText ip) {
		String op = "";

		try {
			op = ip.getText().toString().trim();

			if (op.trim().isEmpty() || (op.length() == 0)) {
				return "NA";
			} else {
				return op;
			}
		} catch (Exception e) {
			return "NA";
		}
	}

	// String to integer
	public static int getString_integer(String ip) {
		int op = 0;

		try {
			op = Integer.parseInt(ip.toString());
		} catch (Exception e) {
			return 0;
		}

		return op;
	}

	public static String doFormat(String inputdate, String fromFormat,
                                  String toFromat) {
		String formattedDate;

		try {
			DateFormat originalFormat = new SimpleDateFormat(fromFormat,
					Locale.ENGLISH);
			DateFormat targetFormat = new SimpleDateFormat(toFromat);
			Date date = originalFormat.parse(inputdate);
			formattedDate = targetFormat.format(date);
		} catch (ParseException ex) {
			return "";
		}

		return formattedDate;
	}

	public static Date doDateConv(String inputdate, String pattern) {
		Date date;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		try {
			date = formatter.parse(inputdate);
		} catch (ParseException ex) {
			return null;
		}

		return date;
	}

	public static String dateToString(Date p_date, String pattern) {
		String strdate;
		SimpleDateFormat formatter;

		formatter = new SimpleDateFormat(pattern);
		strdate = formatter.format(p_date);
		return strdate;
	}

	public static boolean checkBackDate(Date date, int backdays) {
		Date startDate;

		try {
			startDate = (Date) (date);

			long daysDiff = getDaysDiff(startDate, new Date());

			if ((daysDiff <= backdays) && (daysDiff >= 0)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

		return false;
	}

	public static boolean checkBackDate(String date, String dateformat,
                                        int backdays) {
		SimpleDateFormat df = new SimpleDateFormat(dateformat);
		Date startDate;

		try {
			startDate = (Date) df.parse(date);

			long daysDiff = getDaysDiff(startDate, new Date());

			if ((daysDiff <= backdays) && (daysDiff >= 0)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();

			return false;
		}

		return false;
	}

	//
	// public static long getDaysDiff(Date fromdate, Date todate) {
	// Calendar fromCalendar = Calendar.getInstance();
	// Calendar toCalendar = Calendar.getInstance();
	// fromCalendar.setTime(fromdate);
	// toCalendar.setTime(todate);
	//
	// long fromMilliSec = fromCalendar.getTimeInMillis();
	// long toMilliSec = toCalendar.getTimeInMillis();
	// long diff = toMilliSec - fromMilliSec;
	// long diffSeconds = diff / 1000;
	// long diffMinutes = diff / (60 * 1000);
	// long diffHours = diff / (60 * 60 * 1000);
	// long diffDays = diff / (24 * 60 * 60 * 1000);
	//
	// if (fromCalendar.get(Calendar.DAY_OF_YEAR) >
	// toCalendar.get(Calendar.DAY_OF_YEAR)) {
	// /*if the entry date id greater than current date then it will return -1
	// so invalidate date entry */
	// return -1;
	// }
	//
	// return diffDays;
	// }

	public static long getDaysDiff(Date fromdate, Date todate) {
		// in milliseconds
		long diff = todate.getTime() - fromdate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}

	public static void play_Error(Context ctxt) {
		ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
		toneG.startTone(ToneGenerator.TONE_PROP_BEEP, 200);
	}

	// commonly called methods
	// to disp toast msg
	@SuppressWarnings("unused")
	public static void Toast_msg(Context ctxt, String msg, int period,
                                 int MessageType) {

		LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
		View layout = inflater.inflate(R.layout.layot_toast,
				(ViewGroup) ((Activity) ctxt)
						.findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);

		Toast toast = new Toast(ctxt);
		toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
				0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}
	@SuppressWarnings("unused")
	public static void Toast_msg_bottom(Context ctxt, String msg, int period,
                                        int MessageType) {

		LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
		View layout = inflater.inflate(R.layout.layot_toast,
				(ViewGroup) ((Activity) ctxt)
						.findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);

		Toast toast = new Toast(ctxt);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER,
				0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}
	public static void Toast_msg_internet(Context ctxt, String msg, int period,
                                          int MessageType) {

		LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
		View layout = inflater.inflate(R.layout.layout_toast_internert,
				(ViewGroup) ((Activity) ctxt)
						.findViewById(R.id.toast_layout_root));

		ImageView image = (ImageView) layout.findViewById(R.id.image);
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);

		Toast toast = new Toast(ctxt);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER,
				0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}
}
