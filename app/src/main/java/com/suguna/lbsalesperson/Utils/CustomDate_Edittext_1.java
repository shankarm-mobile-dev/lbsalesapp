package com.suguna.lbsalesperson.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomDate_Edittext_1 {

    static final int DATE_DIALOG_ID = 1;

    Calendar c = Calendar.getInstance();
    Context ctxt;
    EditText edittext_customdate;
    TextView textView_customdate;
    int hour;
    int minute;
    int mYear;
    int mMonth;
    int mDay;
    int resDate;


    public CustomDate_Edittext_1(Context ctxt, TextView edtdate, String title,
                                 String message, int date, String serverdate) {

        this.textView_customdate = edtdate;
        this.ctxt = ctxt;
        //this.fragment=fragment;
        //textView_customdate.setTextSize(12f);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        java.util.Date d = null;
        try {
            //d = sdf.parse(String.valueOf(date));

            c.setTime(sdf.parse(String.valueOf(serverdate)));


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.resDate =date;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        Activity activity = (Activity) ctxt;

        // (resCondition.equals("restrict")) {
        customdate(activity, title, message,serverdate);
        //else {
        //	customdate_3days(activity, title, message);
        //}

    }

    /* Date edittext starts here */
    public void customdate(final Activity activity, final String title,
                           final String message,final String serverdate) {
        try {
            textView_customdate.setOnClickListener(new View.OnClickListener() {
                // @Override
                public void onClick(View v) {
                    DatePickerDialog dp = new DatePickerDialog(activity,
                            new DatePickerDialog.OnDateSetListener() {
                                @SuppressLint("SimpleDateFormat")
                                @SuppressWarnings("deprecation")
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int monthOfYear,
                                                      int dayOfMonth) {
                                    Date dt = new Date((year - 1900),
                                            monthOfYear, dayOfMonth);
                                    SimpleDateFormat sdf = new SimpleDateFormat(
                                            "dd-MMM-yyyy", Locale.ENGLISH);

                                    textView_customdate.setText(sdf.format(dt));

                                }
                            }, mYear, mMonth, mDay);
                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                    java.util.Date d = null;
                    try {
                        //d = sdf.parse(String.valueOf(date));

                        calendar.setTime(sdf.parse(String.valueOf(serverdate)));
                        calendar1.setTime(sdf.parse(String.valueOf(serverdate)));

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//					dp.getDatePicker().setMaxDate(
//							new java.util.Date().getTime() + 30 * 24 * 3600
//									* 1000 ) ;
					/*dp.getDatePicker().setMinDate(
							new java.util.Date().getTime() );*/
                    calendar.add(Calendar.DATE, -resDate);
                    dp.getDatePicker().setMinDate(calendar.getTimeInMillis());  //set min date
                    // add date to 30 days later
                    dp.getDatePicker().setMaxDate(calendar1.getTimeInMillis()); //set max date
                    dp.setTitle(title);
                    dp.setMessage(message);
                    dp.show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Get first date in month
    public long get_min_date(String date)
    {
        String formatdate="";
        Calendar cal = null;
        long startDate=0;
        try {
            SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
            cal = Calendar.getInstance();
            cal.setTime(monthDate.parse(date));
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            formatdate = format.format(cal.getTime());
            java.util.Date date1 = format.parse(formatdate);
            startDate = date1.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return startDate;

    }
    //Get last date in month
    public long get_max_date(String date)
    {
        String formatdate="";
        Calendar cal = null;
        long startDate=0;
        try
        {
            SimpleDateFormat monthDate = new SimpleDateFormat("MMM-yyyy");
            cal = Calendar.getInstance();
            cal.setTime(monthDate.parse(date));
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            formatdate = format.format(cal.getTime());
            java.util.Date date1 = format.parse(formatdate);
            startDate = date1.getTime();
        } catch (ParseException e)  {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return startDate;
    }


}
