package com.suguna.lbsalesperson.Utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickerWidget {
	
	private String mDateDisplayFormat = "dd-MMM-yyyy";
	private Context myContext = null;
	private String TAG;
	private Calendar mCalendar = null;
	private int mPickerType = 0;
	private CharSequence mCurrentSetDate = null;
	private int mYear=0;
	private int mMonthOfYear=0;
	private int mDateOfMonth=0;
	private DatePickerDialog mPickDate = null;
	public EditText mcurrentedittext = null;

	
	public DatePickerWidget(Context paramyContext, int paramInt, Date paramDate)
	{
		this.TAG = DatePickerWidget.class.getSimpleName();
		try
		{
			this.myContext = paramyContext;
			Calendar localCalendar = Calendar.getInstance();
			this.mCalendar = localCalendar;
		    this.mPickerType = paramInt;
		    CharSequence localCharSequence1;
		    if (paramDate == null)
		    {
		    	Date localDate = this.mCalendar.getTime();
		    	localCharSequence1 = DateFormat.format(this.mDateDisplayFormat, localDate);
		    }
		    else
		    {
		    	localCharSequence1 = DateFormat.format(this.mDateDisplayFormat, paramDate);
		    }
		    
		    mCurrentSetDate=localCharSequence1;
		    
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	public void onSetDatePicker(Date paramDate)
	  {
		try
	    {
			if (this.mCalendar == null)
		    {
		        Calendar localCalendar = Calendar.getInstance();
		        this.mCalendar = localCalendar;
		    }
			
			if (paramDate == null)
		    {
		        int j = this.mCalendar.get(1);
		        this.mYear = j;
		        int k = this.mCalendar.get(2);
		        this.mMonthOfYear = k;
		        int m = this.mCalendar.get(5);
		        this.mDateOfMonth = m;
		    }
			if(mPickerType==2) {
				onCreateDatePickerDialog();
			}else{
				onCreateDatePickerDialog1();
			}
			this.mPickDate.show();
			
	    }
		catch(Exception ex)
		{
			
		}
	  }
	private void onCreateDatePickerDialog() {
		Context localContext = this.myContext;
		int i = this.mYear;
		int j = this.mMonthOfYear;
		int k = this.mDateOfMonth;

		DatePickerDialog localDatePickerDialog1 = new DatePickerDialog(localContext, null, i, j, k);
		this.mPickDate = localDatePickerDialog1;
		this.mPickDate.setCancelable(true);
		this.mPickDate.setCanceledOnTouchOutside(true);

		// Allow only today and one day back
		long today = System.currentTimeMillis();
		long oneDayBack = today - (24 * 60 * 60 * 1000); // subtract 1 day in milliseconds

		this.mPickDate.getDatePicker().setMaxDate(today);
		this.mPickDate.getDatePicker().setMinDate(oneDayBack);

		DatePickerDialog localDatePickerDialog3 = this.mPickDate;

		localDatePickerDialog3.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_NEGATIVE) {
					mPickDate.dismiss();
				}
			}
		});

		localDatePickerDialog3.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (which == DialogInterface.BUTTON_POSITIVE) {
					int i4 = mPickDate.getDatePicker().getYear();
					int i5 = mPickDate.getDatePicker().getMonth();
					int i6 = mPickDate.getDatePicker().getDayOfMonth();
					mPickDate.updateDate(i4, i5, i6);
					mCalendar.set(i4, i5, i6);
					Date localDate3 = mCalendar.getTime();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
					mCurrentSetDate = sdf.format(localDate3);
					SendDateChangeEvent();
					mPickDate.dismiss();
				}
			}
		});
	}

	private void onCreateDatePickerDialog1()
	  {
		 Context localContext = this.myContext;
		 int i = this.mYear;
		 int j = this.mMonthOfYear;
		 int k = this.mDateOfMonth;
		 
		 DatePickerDialog localDatePickerDialog1 = new DatePickerDialog(localContext, null, i, j, k);
		 this.mPickDate = localDatePickerDialog1;
		 this.mPickDate.setCancelable(true);
		 this.mPickDate.setCanceledOnTouchOutside(true);
		  this.mPickDate.getDatePicker().setMaxDate(System.currentTimeMillis());
		 
		 DatePickerDialog localDatePickerDialog3 = this.mPickDate;
		 
		 localDatePickerDialog3.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			       if (which == DialogInterface.BUTTON_NEGATIVE) {
			    	   mPickDate.dismiss();
			       }
			    }
			  });

		 localDatePickerDialog3.setButton(DialogInterface.BUTTON_POSITIVE, "Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
			       if (which == DialogInterface.BUTTON_POSITIVE) {
			    	  int i4=mPickDate.getDatePicker().getYear();
			    	  int i5=mPickDate.getDatePicker().getMonth();
			    	  int i6=mPickDate.getDatePicker().getDayOfMonth();
			    	  mPickDate.updateDate(i4, i5, i6);
			    	  mCalendar.set(i4, i5, i6);
			    	  Date localDate3 = mCalendar.getTime();
			    	  CharSequence localCharSequence1 = DateFormat.format(mDateDisplayFormat, localDate3);
					   SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
					   mCurrentSetDate=sdf.format(localDate3);
					  // mCurrentSetDate = localCharSequence1;
			          SendDateChangeEvent();
			    	  mPickDate.dismiss();
			       }
			    }
			  });
	  }
	
	public CharSequence GetCurrentDateField()
	{
	    return this.mCurrentSetDate;
	}
	
	public void SendDateChangeEvent()
	{
		mcurrentedittext.setText(GetCurrentDateField());
	}
	
}
