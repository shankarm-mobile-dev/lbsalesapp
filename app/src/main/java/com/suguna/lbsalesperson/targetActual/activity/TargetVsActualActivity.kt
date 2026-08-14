package com.suguna.lbsalesperson.targetActual.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.listener.ClickListener
import com.suguna.lbsalesperson.targetActual.model.TargetVsActualModel
import com.suguna.lbsalesperson.targetActual.adapter.TargetVsActualAdapter
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject

class TargetVsActualActivity : BaseActivity(), ClickListener, ResponseListener {

    var mMenuRecycler: RecyclerView? = null

    var mMenuAdapter: TargetVsActualAdapter? = null

    var mData = arrayListOf<TargetVsActualModel>()

    var weekDetailsTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_target_vs_actual)
        init()
        if (Utility.isInternetAvailable(myContext))
            getList()
        else     AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_internet), "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }, false)

    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun clickListener() {

    }

    private fun getList() {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                when (LocalStorageSP.getLoginUser(myContext).USERTYPE) {
                    "0" -> AppServices.API.targetvsActual_executive
                    "1" -> AppServices.API.targetvsActual_head
                    else -> AppServices.API.targetvsActual_executive
                }
                ,
                TargetVsActualModel::class.java, jsonParam

        )
    }


    override fun init() {



        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Target Vs Actual"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mMenuRecycler = findViewById(R.id.recycleview)
        weekDetailsTv = findViewById(R.id.week_details_tv)

        clickListener()
        //setRecycleViewAdapter()
    }

    private fun setRecycleViewAdapter() {


        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = TargetVsActualAdapter(myContext, mData, this)
        mMenuRecycler!!.adapter = mMenuAdapter
    }

    override fun click(value: String) {

    }

    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {
            if (r.requestType!! == AppServices.API.targetvsActual_executive.hashCode() || r.requestType!! == AppServices.API.targetvsActual_head.hashCode()) {
                val data = (r as TargetVsActualModel)

                weekDetailsTv!!.text = "Week No." + data.WeekDetails!!.weekNo.toString() + " - Qty In Tons"
                val listData = data.data
                if (!listData.isNullOrEmpty() && listData.size > 0) {

                    mData = listData
                    var targetQty: Double = 0.0
                    var achievedQty: Double = 0.0
                    var achievedPercent: Double = 0.0
                    mData.forEach {
                        //achievedPercent = achievedPercent + it.achievedPercent!!
                        targetQty = targetQty + it.targetQty!!
                        achievedQty = achievedQty + it.salesKGS!!
                    }

                    try {
                        achievedPercent = (achievedQty / targetQty) * 100
                    } catch (e:Exception) {

                    }

                    val aLastData = TargetVsActualModel()

                    aLastData.day = "Total"
                    aLastData.salesKGS = achievedQty
                    aLastData.targetQty = targetQty
                    aLastData.achievedPercent = achievedPercent
                    mData.add(aLastData)
                    setRecycleViewAdapter()
                } else {
                    AppDialogs.customOkAction(myContext, "Alert", "No Data Available. Please check after sometimes !!!", "Ok", object : AppDialogs.ConfirmListener {
                        override fun yes() {
                            finish()
                        }
                    }, false)
                }

            }
        }
    }
}