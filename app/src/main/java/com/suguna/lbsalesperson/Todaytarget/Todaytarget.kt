package com.suguna.lbsalesperson.Todaytarget

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.LoginActivity

import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Todaytarget.adapter.TodayTargetRecyclerAdapter
import com.suguna.lbsalesperson.Todaytarget.model.ResultPOJO
import com.suguna.lbsalesperson.Todaytarget.model.TodayTargetdeclareModel
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener

import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale

class Todaytarget : BaseActivity(), BranchAllocationListener, DatePickerDialog.OnDateSetListener,
    ResponseListener {
    var sharedpreferences: SharedPreferences? = null
    val MyPREFERENCES = "MyPrefs"
    val Name = "name"
    val Password = "pass"
    val DeviceId = "deviceid"
    var saveButton: LinearLayout? = null
    var mMenuRecycler: RecyclerView? = null
    var mMenuAdapter: TodayTargetRecyclerAdapter? = null
    var edtdate: EditText? = null
    var edtregion: EditText?=null
    var price_min:Float = 1f
    var price_max:Float = 999f
    var tvtarget:TextView?=null
    var tvtargetavg:TextView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_target)
        sharedpreferences = getSharedPreferences(
            LoginActivity.MyPREFERENCES,
            MODE_PRIVATE)
        init()
        clickListener()
        getTodayTargetdeclareList()
       // getDiffpricedeclareList()

    }
    override fun clickListener() {
        saveButton?.setOnClickListener(View.OnClickListener {
            val ietmslist1 = ArrayList<TodayTargetdeclareModel>()
            ietmslist1.clear()
            var flag = false
            for (i in mDiffdeclareList!!.indices) {
                val det = mDiffdeclareList!![i]
                val dataRate = det.DECLARE_TARGET
                if (dataRate!!.length > 0 && det.ISUPDATE.equals("N")) {
                    val rateValue = det.DECLARE_TARGET!!.toDouble()
                    if (rateValue > 0) {
                        ietmslist1.add(det)
                    }
                }
            }
            if(ietmslist1.size>0)
            {
                var rate = ""
                var rateValue = 0.0
                for (a in ietmslist1.indices) {
                    rate = ietmslist1[a]
                        .DECLARE_TARGET.toString()
                    if(rate.length>0){
                        rateValue = ietmslist1[a]
                            .DECLARE_TARGET!!.toDouble()}
                    if ((rateValue >= price_min) && (rateValue <= price_max)) {

                        flag = true
                    }
                    else
                    {
                        flag = false
                        AlertDialog.Builder(myContext)
                            .setTitle("Warning")
                            .setMessage(
                                Html.fromHtml( "Please Enter the Target in between the range <b>"+price_min+" to "+price_max+"</b>"))
                            .setPositiveButton(
                                android.R.string.yes
                            ) { dialog, which ->
                                // continue with delete
                            }
                            .setIcon(
                                android.R.drawable.ic_dialog_alert)
                            .show()
                        break
                    }
                    }
            }
            else
            {
                Toast.makeText(myContext, "Please Enter the Target",
                    Toast.LENGTH_LONG).show()
            }
            if(flag)
            {
                saveTodayTargetdeclareList(ietmslist1)

            }
          /*  for (i in mDiffdeclareList!!.indices) {
                val det = mDiffdeclareList!![i]
                val dataRate = det.DECLARE_TARGET
                if (dataRate.length > 0) {
                    val rateValue = det.DECLARE_TARGET.toDouble()
                    if (rateValue > 0) {
                        ietmslist1.add(det)
                    }
            }*/
        })
    }

    override fun init() {
        saveButton=findViewById(R.id.saveButton);
        mMenuRecycler = findViewById(R.id.recycleview)
        edtdate = findViewById(R.id.edtdate)
        edtregion= findViewById(R.id.edtregion)
        tvtarget= findViewById(R.id.tvtarget)
        tvtargetavg= findViewById(R.id.tvtargetavg)
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        val strDate = format.format(calendar.getTime())
        edtdate!!.setText(strDate)
    }
    private fun getTodayTargetdeclareList() {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
      //  jsonParam.put("FROMDATE", get_min_date(edtdate!!.text.toString().trim()))
      //  jsonParam.put("TODATE", get_max_date(edtdate!!.text.toString().trim()))
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_today_target_cluster,
            TodayTargetdeclareModel::class.java, jsonParam

        )

    }

    private fun setRecycleViewAdapter(data: ArrayList<TodayTargetdeclareModel>) {
        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = TodayTargetRecyclerAdapter(myContext, data!!,this)
        mMenuRecycler!!.adapter = mMenuAdapter
    }

    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {
            if (r.requestType.hashCode() == AppServices.API.save_today_target_cluster.hashCode()) {
                val data = (r as ResultPOJO)
                mDiffnameList = data.data!!
                if(mDiffnameList.size>0)
                {
                    getTodayTargetdeclareList()
                }
            }

            if (r.requestType.hashCode() == AppServices.API.get_today_target_cluster.hashCode()) {
                val data = (r as TodayTargetdeclareModel)
                mDiffdeclareList = data.data!!
                setRecycleViewAdapter(mDiffdeclareList)
                if(mDiffdeclareList.size>0)
                {


                    var count=0
                    var countavg=0
                    for (item in mDiffdeclareList) {
                        //println(item)

                        if(item.DECLARE_TARGET!!.length>0)
                        {
                            count+=item.DECLARE_TARGET!!.toInt()
                        }
                        if(count>0)
                        {
                            tvtarget!!.text=count.toString()
                        }
                        if(item.AVG_TARGET!!.length>0)
                        {
                            countavg+=item.AVG_TARGET!!.toInt()
                        }
                        if(countavg>0)
                        {
                            tvtargetavg!!.text=countavg.toString()
                        }
                    }

                }

            }
        }
    }

    private fun s(item: TodayTargetdeclareModel) =
        item.DECLARE_TARGET!!

    override fun onClick(view: View?, position: Int) {

    }

    override fun onLongClick(view: View?, position: Int) {

    }

    override fun onSubmit() {

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

    }
    companion object {
        lateinit var mDiffnameList: ArrayList<ResultPOJO>
        lateinit var month:String
        lateinit var cur_position:String
        lateinit var mDiffdeclareList: ArrayList<TodayTargetdeclareModel>
    }

    private fun saveTodayTargetdeclareList(ietmslist1: ArrayList<TodayTargetdeclareModel>) {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        val jsonArray = JSONArray()
        for (a in ietmslist1.indices) {
            val jsonParam1 = JSONObject()
            jsonParam1.put("CLUSTER_NAME", ietmslist1[a]
                .CLUSTER_NAME.toString())
            jsonParam1.put("CLUSTER_DESC", ietmslist1[a]
                .CLUSTER_DESC.toString())
            jsonParam1.put("REGION_CODE", ietmslist1[a]
                .REGION_CODE.toString())
            jsonParam1.put("DECLARE_TARGET", ietmslist1[a]
                .DECLARE_TARGET.toString())

            jsonArray.put(jsonParam1)

        }
        jsonParam.put("TARGET_LINES", jsonArray)
        //  jsonParam.put("FROMDATE", get_min_date(edtdate!!.text.toString().trim()))
        //  jsonParam.put("TODATE", get_max_date(edtdate!!.text.toString().trim()))
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.save_today_target_cluster,
            ResultPOJO::class.java, jsonParam

        )

    }
}