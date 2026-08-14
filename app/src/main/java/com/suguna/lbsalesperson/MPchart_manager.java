package com.suguna.lbsalesperson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
/*

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
*/
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SUGRegionDashboard_POJO;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MPchart_manager extends AppCompatActivity /*implements OnChartValueSelectedListener*/ {
   // BarChart barChart;

    // variable for our bar data set.
    //BarDataSet barDataSet1, barDataSet2,barDataSet3,barDataSet4;

    // array list for storing entries.
    ArrayList barEntries;

    // creating a string array for displaying days.
    String[] days = new String[]{"TN","T2","KA","K2","AP","AH","KL"};
    int click=0;
    DatabaseUpDown databaseUpDown;
    List<SUGRegionDashboard_POJO> RegionDashboardPOJOlist;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;
    TextView txt_yesterday,txt_today;
    String date,day,Value,xIndex,region;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart_manager);
        databaseUpDown = new DatabaseUpDown(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        lbloginDAO = new Sug_lblogin(this);
        setToolBar();
        On_FieldClick();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        date=sdf.format(new java.util.Date());
        day="Today";
        if (getIntent().getExtras() != null) {
            day=(getIntent().getStringExtra("day"));
            if(day.equals("Yesterday"))
            {
                txt_today.setTextColor(getResources().getColor(
                        R.color.black));
                txt_yesterday.setTextColor(getResources().getColor(
                        R.color.white));
                txt_today.setBackground((getResources().getDrawable(
                        R.drawable.list_background_oval)));
                txt_yesterday.setBackground((getResources().getDrawable(
                        R.drawable.click_green_selector)));
                getSupportActionBar().setTitle("Yest. - Order Status");
                Calendar c = Calendar.getInstance();

                date=sdf.format(new java.util.Date());
                date=sdf.format(new java.util.Date());
                Date d = null;
                try {
                    //d = sdf.parse(String.valueOf(date));

                    c.setTime(sdf.parse((date)));
                    c.add(Calendar.DATE, -1);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //String CreationDate = df.format(d);

                date=sdf.format(c.getTime());
            }
        }
        //new checkdashboarddetails_asyn().execute();
        // initializing variable for bar chart.


        // creating a new bar data set.

    }
    private void On_FieldClick() {
        txt_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_today.setTextColor(getResources().getColor(
                        R.color.white));
                txt_yesterday.setTextColor(getResources().getColor(
                        R.color.black));
                txt_today.setBackground((getResources().getDrawable(
                        R.drawable.click_green_selector)));
                txt_yesterday.setBackground((getResources().getDrawable(
                        R.drawable.list_background_oval)));
                getSupportActionBar().setTitle("Today - Order Status");

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                date=sdf.format(new java.util.Date());
                //new checkdashboarddetails_asyn().execute();
                Intent i = new Intent(MPchart_manager.this, MPchart_manager.class);
                i.putExtra("day", "Today");
                startActivity(i);
                finish();
            }
        });
        txt_yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_today.setTextColor(getResources().getColor(
                        R.color.black));
                txt_yesterday.setTextColor(getResources().getColor(
                        R.color.white));
                txt_today.setBackground((getResources().getDrawable(
                        R.drawable.list_background_oval)));
                txt_yesterday.setBackground((getResources().getDrawable(
                        R.drawable.click_green_selector)));
                getSupportActionBar().setTitle("Yest. - Order Status");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
                date=sdf.format(new java.util.Date());
                Date d = null;
                try {
                    //d = sdf.parse(String.valueOf(date));

                    c.setTime(sdf.parse((date)));
                    c.add(Calendar.DATE, -1);

                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //String CreationDate = df.format(d);

                date=sdf.format(c.getTime());
                Intent i = new Intent(MPchart_manager.this, MPchart_manager.class);
                i.putExtra("day", "Yesterday");
                startActivity(i);
                finish();
                //new checkdashboarddetails_asyn().execute();
            }
        });
    }
    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
        return;

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void setToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Today - Order Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_today = (TextView) findViewById(R.id.txt_today);
        txt_yesterday = (TextView) findViewById(R.id.txt_yesterday);
    }
    public void popup_save() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                MPchart_manager.this);
        LayoutInflater inflater = MPchart_manager.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_confirm, null);
        dialogBuilder.setView(dialogView);
        /*
         * final Dialog dialog = new Dialog(MainActivity.this); // Include
         * dialog.xml file dialog.setContentView(R.layout.layout_popup);
         */
        // Set dialog title
        Typeface customfont = Typeface.createFromAsset(getAssets(),
                "fonts/MORE.TTF");
        Typeface customfont1 = Typeface.createFromAsset(getAssets(),
                "fonts/MORE.TTF");
        // set values for custom dialog components - text, image and
        // button
        // TextView txtheading = (TextView) dialogView.findViewById(R.id.txttitle);
        // txtheading.setText("Confirm");
        // TextView txtsub = (TextView) dialogView.findViewById(R.id.txtsubject);
        // txtsub.setText("Are you sure to Leave from LB Sales Application?");
        // txtsub.setText(Html.fromHtml("Are you sure to save candling egg details and the candling date & time is : <span style=\"color:#228B22\">"+str_receipt_date+" "+str_receipt_time+"</span >"));
        TextView txtok = (TextView) dialogView.findViewById(R.id.txtok);
        TextView txtcancel = (TextView) dialogView.findViewById(R.id.txtcancel);
        TextView txt_so = (TextView) dialogView.findViewById(R.id.txt_so);
        TextView txt_customer = (TextView) dialogView.findViewById(R.id.txt_customer);
        LinearLayout lyt_close=(LinearLayout) dialogView.findViewById(R.id.lyt_close);
        txtcancel.setText("NO");
        txtok.setText("YES");
        // txtheading.setTypeface(customfont);
        // txtsub.setTypeface(customfont1);
        txtok.setTypeface(customfont);
        txtcancel.setTypeface(customfont);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.setCancelable(false);
        // dialog.show();
        // if decline button is clicked, close the custom dialog
        txt_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                // dialog.dismiss();
                b.dismiss();
                click=0;
                Intent i = new Intent(MPchart_manager.this, MPchart2.class);
                i.putExtra("date", date);
                i.putExtra("day", day);
                i.putExtra("region", region);
                startActivity(i);

            }
        });
        txt_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                // newtimer1.cancel();
                // dialog.dismiss();
                b.dismiss();
                click=0;
                Intent i = new Intent(MPchart_manager.this, OrderCustomerHierarchy_VP.class);
                i.putExtra("name", "Mr. Abdul Hakeem Ashiq J");
                i.putExtra("day", day);
                i.putExtra("region", region);
                i.putExtra("date", date);
                startActivity(i);


            }
        });
        lyt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                // newtimer1.cancel();
                // dialog.dismiss();
                b.dismiss();
                click=0;


            }
        });
    }
    // array list for first set
  /*  private ArrayList<BarEntry> getTagert(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {

        // creating a new array list
        SUGRegionDashboard_POJO lsloginPOJO;
        barEntries = new ArrayList<>();
        for (int i = 0; i <  regionDashboardPOJOlist.size(); i++) {
            lsloginPOJO=regionDashboardPOJOlist.get(i);
            barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getTARGET_QTY())));
        }

       // barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getTARGET_QTY())));
        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.

        return barEntries;
    }
*/
    // array list for second set.
   /* private ArrayList<BarEntry> getBooked(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {

        // creating a new array list
        SUGRegionDashboard_POJO lsloginPOJO;
        barEntries = new ArrayList<>();
        for (int i = 0; i <  regionDashboardPOJOlist.size(); i++) {
            lsloginPOJO=regionDashboardPOJOlist.get(i);
            barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getBOOKED_QTY())));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> getScheduled(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {

        // creating a new array list
        SUGRegionDashboard_POJO lsloginPOJO;
        barEntries = new ArrayList<>();
        for (int i = 0; i <  regionDashboardPOJOlist.size(); i++) {
            lsloginPOJO=regionDashboardPOJOlist.get(i);
            barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getSCHEDULED_QTY())));
        }
        return barEntries;
    }

    private ArrayList<BarEntry> getLifted(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {

        // creating a new array list
        SUGRegionDashboard_POJO lsloginPOJO;
        barEntries = new ArrayList<>();
        for (int i = 0; i <  regionDashboardPOJOlist.size(); i++) {

            lsloginPOJO=regionDashboardPOJOlist.get(i);
            barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getLIFTED_QTY())));
        }
        return barEntries;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex()+"-"+ h.getAxis());
        Log.e("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex()+"-"+ h.getAxis());

        if(click==0) {
            click=1;
            Value=String.valueOf(e.getY());
            xIndex=String.valueOf(e.getX());
            Log.e("xIndex",xIndex);
            DecimalFormat df = new DecimalFormat("##");
            df.setRoundingMode(RoundingMode.UP);
            SUGRegionDashboard_POJO lsloginPOJO;
            Log.e("xIndex",String.valueOf(Integer.parseInt(df.format(Float.parseFloat(xIndex)))));
            lsloginPOJO=RegionDashboardPOJOlist.get(0);
            region=lsloginPOJO.getREGION_CODE();
            Log.e("region",lsloginPOJO.getREGION_CODE());
            popup_save();
        }
    }

    @Override
    public void onNothingSelected() {

    }

    public class GradeValueChart extends ValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public GradeValueChart() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


            String val = String.valueOf(value);


            return val + " - 10%"; // e.g. append any text
        }
    }


    public class format extends ValueFormatter {

        private DecimalFormat mFormat;

        public format() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {


            String val = String.valueOf(value);
            Log.e("val",val);

            return val ; // e.g. append any text
        }
    }
    public class checkdashboarddetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(MPchart_manager.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build();
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            boolean download = false;
            RegionDashboardPOJOlist = new
                    ArrayList<SUGRegionDashboard_POJO>();
            String custnumber="";
            custnumber=sharedpreferences.getString(Name, "");
            String USERID="",TKEY="";
            USERID=lbloginDAO.getUSERID(custnumber);
            TKEY=lbloginDAO.getTKEY(custnumber);
            RegionDashboardPOJOlist = databaseUpDown.get_regiondashbordmanager_dtls(custnumber,USERID,TKEY,date);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(RegionDashboardPOJOlist.size()>0)
            {
                setbarchar(RegionDashboardPOJOlist);
            }
            else
            {

            }
        }
    }

    private void setbarchar(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {
        float size= (float) (regionDashboardPOJOlist.size());

        // String[] days = new String[]{"TN","T2","KA","K2","AP","AH","KL"};
        String[] region = new String[regionDashboardPOJOlist.size()];
        SUGRegionDashboard_POJO lsloginPOJO;
        for (int i1 = 0; i1 < regionDashboardPOJOlist.size(); i1++) {
            lsloginPOJO=regionDashboardPOJOlist.get(i1);
            region[i1] = lsloginPOJO.getREGION_CODE();
        }
        barChart = findViewById(R.id.idBarChart);
        barDataSet4 = new BarDataSet(getTagert(regionDashboardPOJOlist), "");
        barDataSet4.setColor(getApplicationContext().getResources().getColor(R.color.app_target));
        barDataSet3 = new BarDataSet(getBooked(regionDashboardPOJOlist), "");
        barDataSet3.setColor(getApplicationContext().getResources().getColor(R.color.app_booked));
        barDataSet2 = new BarDataSet(getScheduled(regionDashboardPOJOlist), "");
        barDataSet2.setColor(getApplicationContext().getResources().getColor(R.color.app_schedlued));
        barDataSet1 = new BarDataSet(getLifted(regionDashboardPOJOlist), "");
        barDataSet1.setColor(getApplicationContext().getResources().getColor(R.color.app_lifted));
        barDataSet4.setValueTextColors(Collections.singletonList((getApplicationContext().getResources().getColor(R.color.app_red))));
        barDataSet3.setValueTextColors(Collections.singletonList((getApplicationContext().getResources().getColor(R.color.app_red))));
        barDataSet2.setValueTextColors(Collections.singletonList((getApplicationContext().getResources().getColor(R.color.app_red))));
        barDataSet1.setValueTextColors(Collections.singletonList((getApplicationContext().getResources().getColor(R.color.app_red))));
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/mo_me.ttf");
        barDataSet1.setValueTextSize(9f);
        barDataSet1.setValueTypeface(tf);
        barDataSet1.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        barDataSet2.setValueTextSize(9f);
        barDataSet2.setValueTypeface(tf);
        barDataSet2.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        barDataSet3.setValueTextSize(9f);
        barDataSet3.setValueTypeface(tf);
        barDataSet3.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        barDataSet4.setValueTextSize(9f);
        barDataSet4.setValueTypeface(tf);
        barDataSet4.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        // below line is to add bar data set to our bar data.
        BarData data = new BarData(barDataSet1, barDataSet2 ,barDataSet3, barDataSet4);

        // after adding data to our bar data we
        // are setting that data to our bar chart.
        barChart.setData(data);
        barChart.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        barChart.setVisibleXRangeMaximum(1f);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setOnChartValueSelectedListener(this);
        // below line is to remove description
        // label of our bar chart.
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        // below line is to get x axis
        // of our bar chart.
        XAxis xAxis = barChart.getXAxis();

        // below line is to set value formatter to our x-axis and
        // we are adding our days to our x axis.
        xAxis.setValueFormatter(new IndexAxisValueFormatter(region));
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(region));

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(1);
        // below line is to set position
        // to our x-axis to bottom.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //xAxis.setXOffset(-350);
        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1f);

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(true);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/mo_me.ttf");
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        xAxis.setTextSize(11f);
        xAxis.setTypeface(tf);
        xAxis.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
        xAxis.setEnabled(true);
        YAxis yl = barChart.getAxisLeft();
        yl.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        yl.setTextSize(11f);
        yl.setTypeface(tf);
        yl.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
        yl.setEnabled(true);

        // below line is to make our
        // bar chart as draggable.
        // barChart.setDragEnabled(false);
        int groupCount = xAxis.getLabelCount();
        // below line is to make visible
        // range for our bar chart.
        // barChart.setVisibleXRangeMaximum(3.5f);
        // barChart.setVisibleXRangeMaximum(size);
        //  barChart.moveViewToX(6);

        //  barChart.moveViewToX(1);
        // below line is to add bar
        // space to our chart.
        float barSpace = 0.02f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.35f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.25f);



        // below line is to set minimum
        // axis to our chart.
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMinValue(0f);
        barChart.getAxisRight().setAxisMinValue(0f);
       // barChart.getXAxis().setAxisMinimum(2f);
        // below line is to
        // animate our chart.
        barChart.animate();
        // Log.e("groupCount",String.valueOf(groupCount));
        // below line is to group bars
        // and add spacing to it.
        if(size<6)
        {
          // barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
            //   barChart.getXAxis().setAxisMaximum(size-6 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        }
        else
        {
            //  barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        }
        //  barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        barChart.groupBars(0, groupSpace, barSpace);
       // barChart.getXAxis().setCenterAxisLabels(true);
        //barChart.setDrawValueAboveBar(false);

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate();
        barChart.getAxisLeft().setEnabled(true); //show y-axis at left
        barChart.getAxisRight().setEnabled(true); //hide y-axis at right
        barChart.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        // barChart.setTouchEnabled(false);
        // barChart.setPinchZoom(false);

        barChart.getXAxis().setDrawAxisLine(false);
        barChart.setHorizontalScrollBarEnabled(true);


        barChart.refreshDrawableState();
        barChart.notifyDataSetChanged();

    }

    public static class MyXAxisValueFormatter extends IndexAxisValueFormatter {

        private String[] mValues;
        private String first;


        public MyXAxisValueFormatter(String[] values) {
            Log.e("val1","val1");
            this.mValues = values;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            Log.e("value",String.valueOf(value));
            return "5";
        }

        *//** this is only needed if numbers are returned, else return 0 *//*

    }*/
}
