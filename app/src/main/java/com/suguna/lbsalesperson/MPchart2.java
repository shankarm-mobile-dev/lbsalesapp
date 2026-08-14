package com.suguna.lbsalesperson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
/*

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
*/
import com.suguna.lbsalesperson.DAO.Sug_lblogin;
import com.suguna.lbsalesperson.Database.DatabaseUpDown;
import com.suguna.lbsalesperson.POJO.SUGRegionDashboard_POJO;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class MPchart2  extends AppCompatActivity  /*implements OnChartValueSelectedListener*/ {
    //BarChart barChart;

    // variable for our bar data set.
    //BarDataSet barDataSet1, barDataSet2,barDataSet3,barDataSet4;

    // array list for storing entries.
    ArrayList barEntries;
    DatabaseUpDown databaseUpDown;
    List<SUGRegionDashboard_POJO> RegionDashboardPOJOlist;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "name";
    public static final String Password = "pass";
    SharedPreferences sharedpreferences;
    Sug_lblogin lbloginDAO;
    String date,day,region,name,Value,xIndex,code;
    int click=0;
    // creating a string array for displaying days.
    String[] days = new String[]{"Mr. Dinesh Kumar","Mr. Saravanakumar G","Mr. Saravanan S","Mr. Sunilkumar C","Mr. Ravikumar S","Mr. Dharmaraj P","Mr. Jagadish Naskar"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpchart2);

        databaseUpDown = new DatabaseUpDown(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        lbloginDAO = new Sug_lblogin(this);

        if (getIntent().getExtras() != null) {
            date=(getIntent().getStringExtra("date"));
            day=(getIntent().getStringExtra("day"));
            region=(getIntent().getStringExtra("region"));

        }
        setToolBar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        //date=sdf.format(new java.util.Date());
      //  new checkdashboarddetails_asyn().execute();
        // initializing variable for bar chart.
        //barChart = findViewById(R.id.idBarChart);

      /*  // creating a new bar data set.
        barDataSet4 = new BarDataSet(getTagert(), "");
        barDataSet4.setColor(getApplicationContext().getResources().getColor(R.color.red_active));
        barDataSet3 = new BarDataSet(getBooked(), "");
        barDataSet3.setColor(getApplicationContext().getResources().getColor(R.color.orange_active));
        barDataSet2 = new BarDataSet(getScheduled(), "");
        barDataSet2.setColor(getApplicationContext().getResources().getColor(R.color.purple_active));
        barDataSet1 = new BarDataSet(getLifted(), "");
        barDataSet1.setColor(getApplicationContext().getResources().getColor(R.color.green_active));

        // below line is to add bar data set to our bar data.
        BarData data = new BarData(barDataSet1, barDataSet2 ,barDataSet3, barDataSet4 );

        // after adding data to our bar data we
        // are setting that data to our bar chart.
        barChart.setData(data);
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
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true);

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1);

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(false);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/MORE.TTF");
        xAxis.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        xAxis.setTypeface(tf);
        YAxis yl = barChart.getAxisLeft();
        yl.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        yl.setTypeface(tf);
        // below line is to make our
        // bar chart as draggable.
        barChart.setDragEnabled(false);

        // below line is to make visible
        // range for our bar chart.
        barChart.setVisibleXRangeMaximum(6.5f);

        // below line is to add bar
        // space to our chart.
        float barSpace = 0.08f;

        // below line is use to add group
        // spacing to our bar chart.
        float groupSpace = 0.2f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.12f);

        // below line is to set minimum
        // axis to our chart.
        barChart.getXAxis().setAxisMinimum(0);

        // below line is to
        // animate our chart.
        barChart.animate();

        // below line is to group bars
        // and add spacing to it.
        barChart.groupBars(0, groupSpace, barSpace);

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate();
        barChart.getAxisLeft().setEnabled(true); //show y-axis at left
        barChart.getAxisRight().setEnabled(false); //hide y-axis at right
        barChart.setTouchEnabled(false);
        barChart.setPinchZoom(false);
        barChart.getXAxis().setDrawAxisLine(false);*/
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
        getSupportActionBar().setTitle("Order Status "+region+"- By SO");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void popup_save() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                MPchart2.this);
        LayoutInflater inflater = MPchart2.this.getLayoutInflater();
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
        txt_so.setVisibility(View.GONE);
        // txtheading.setTypeface(customfont);
        // txtsub.setTypeface(customfont1);
        txtok.setTypeface(customfont);
        txtcancel.setTypeface(customfont);
        final AlertDialog b = dialogBuilder.create();
        b.show();
        b.setCancelable(false);
        // dialog.show();
        // if decline button is clicked, close the custom dialog
       /* txt_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                // dialog.dismiss();
                b.dismiss();
                click=0;
                Intent i = new Intent(MPchart.this, MPchart2.class);
                i.putExtra("date", date);
                startActivity(i);

            }
        });*/
        txt_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                // newtimer1.cancel();
                // dialog.dismiss();
                b.dismiss();
                click=0;
                Intent i = new Intent(MPchart2.this, OrderCustomerHierarchy_SalesPerson.class);
                i.putExtra("day", day);
                i.putExtra("region", region);
                i.putExtra("name", name);
                i.putExtra("code", code);
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
   /* // array list for first set
    private ArrayList<BarEntry> getTagert() {

        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1f, 300));
        barEntries.add(new BarEntry(2f, 250));
        barEntries.add(new BarEntry(3f, 270));
        barEntries.add(new BarEntry(4f, 260));
        barEntries.add(new BarEntry(5f, 230));
        barEntries.add(new BarEntry(6f, 230));
        barEntries.add(new BarEntry(7f, 200));
        return barEntries;
    }

    // array list for second set.
    private ArrayList<BarEntry> getBooked() {

        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1f, 100));
        barEntries.add(new BarEntry(2f, 70));
        barEntries.add(new BarEntry(3f, 60));
        barEntries.add(new BarEntry(4f, 55));
        barEntries.add(new BarEntry(5f, 65));
        barEntries.add(new BarEntry(6f, 57));
        barEntries.add(new BarEntry(7f, 55));
        return barEntries;
    }

    private ArrayList<BarEntry> getScheduled() {

        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1f, 25));
        barEntries.add(new BarEntry(2f, 20));
        barEntries.add(new BarEntry(3f, 15));
        barEntries.add(new BarEntry(4f, 12));
        barEntries.add(new BarEntry(5f, 18));
        barEntries.add(new BarEntry(6f, 16));
        barEntries.add(new BarEntry(7f, 13));
        return barEntries;
    }

    private ArrayList<BarEntry> getLifted() {

        // creating a new array list
        barEntries = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntries.add(new BarEntry(1f, 18));
        barEntries.add(new BarEntry(2f, 14));
        barEntries.add(new BarEntry(3f, 10));
        barEntries.add(new BarEntry(4f, 8));
        barEntries.add(new BarEntry(5f, 12));
        barEntries.add(new BarEntry(6f, 11));
        barEntries.add(new BarEntry(7f, 10));
        return barEntries;
    }*/
   // array list for first set
   /*private ArrayList<BarEntry> getTagert(List<SUGRegionDashboard_POJO> regionDashboardPOJOlist) {

       // creating a new array list
       SUGRegionDashboard_POJO lsloginPOJO;
       barEntries = new ArrayList<>();
       for (int i = 0; i <  regionDashboardPOJOlist.size(); i++) {
           lsloginPOJO=regionDashboardPOJOlist.get(i);
           barEntries.add(new BarEntry((float)(i+1), Float.parseFloat(lsloginPOJO.getTARGET_QTY())));
       }
       // adding new entry to our array list with bar
       // entry and passing x and y axis value to it.

       return barEntries;
   }*/

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
        if(click==0) {
            click=1;
            Value=String.valueOf(e.getY());
            xIndex=String.valueOf(e.getX());
            Log.e("xIndex",xIndex);
            DecimalFormat df = new DecimalFormat("##");
            df.setRoundingMode(RoundingMode.UP);
            SUGRegionDashboard_POJO lsloginPOJO;
            Log.e("xIndex",String.valueOf(Integer.parseInt(df.format(Float.parseFloat(xIndex)))));
            lsloginPOJO=RegionDashboardPOJOlist.get(Integer.parseInt(df.format(Float.parseFloat(xIndex)))-1);
            name=lsloginPOJO.getDESCRIPTION();
            code=lsloginPOJO.getPARENT_CODE();
            Log.e("name",lsloginPOJO.getDESCRIPTION());
            popup_save();
        }
    }

    @Override
    public void onNothingSelected() {

    }
    public class checkdashboarddetails_asyn extends
            AsyncTask<String, String, Boolean> {
        ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ACProgressFlower.Builder(MPchart2.this)
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
            RegionDashboardPOJOlist = databaseUpDown.get_regiondashbordS0_dtls(custnumber,USERID,TKEY,date,region);
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
          Log.e("size",String.valueOf(size));
        // String[] days = new String[]{"TN","T2","KA","K2","AP","AH","KL"};
        String[] region = new String[regionDashboardPOJOlist.size()];
        SUGRegionDashboard_POJO lsloginPOJO;
        for (int i1 = 0; i1 < regionDashboardPOJOlist.size(); i1++) {
            lsloginPOJO=regionDashboardPOJOlist.get(i1);
            region[i1] = lsloginPOJO.getDESCRIPTION();
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
        barChart.setVisibleXRangeMaximum(7f);
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

        // below line is to set center axis
        // labels to our bar chart.
        xAxis.setCenterAxisLabels(true);

        // below line is to set position
        // to our x-axis to bottom.
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // below line is to set granularity
        // to our x axis labels.
        xAxis.setGranularity(1);

        // below line is to enable
        // granularity to our x axis.
        xAxis.setGranularityEnabled(false);

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
        float groupSpace = 0.32f;

        // we are setting width of
        // bar in below line.
        data.setBarWidth(0.15f);



        // below line is to set minimum
        // axis to our chart.
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMinValue(0f);
        barChart.getAxisRight().setAxisMinValue(0f);

        // below line is to
        // animate our chart.
        barChart.animate();
       // Log.e("groupCount",String.valueOf(groupCount));
        // below line is to group bars
        // and add spacing to it.
        if(size<6)
        {
            barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
            //   barChart.getXAxis().setAxisMaximum(size-6 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        }
        else
        {
            //  barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        }
      //  barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * (groupCount));
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.getXAxis().setCenterAxisLabels(true);
        //barChart.setDrawValueAboveBar(false);

        // below line is to invalidate
        // our bar chart.
        barChart.invalidate();
        barChart.getAxisLeft().setEnabled(true); //show y-axis at left
        barChart.getAxisRight().setEnabled(true); //hide y-axis at right
        barChart.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
       // barChart.setTouchEnabled(false);
        barChart.setPinchZoom(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.refreshDrawableState();
        barChart.notifyDataSetChanged();

    }

    public class CustomXAxisRenderer extends XAxisRenderer {
        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }

        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
            String line[] = formattedLabel.split("\n");
            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
            Utils.drawXAxisValue(c, line[1], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
        }
    }*/
}