package com.suguna.lbsalesperson.customerclub.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.customerclub.adapter.CustomerClubAdapter
import com.suguna.lbsalesperson.customerclub.model.ClubModel
import com.suguna.lbsalesperson.customerclub.model.YearModel
import com.suguna.lbsalesperson.listener.ClickListener
import com.truelife.api.AppServices
import com.truelife.http.PlainResponseListener
import org.json.JSONArray
import org.json.JSONObject

class CustomerClubActivity : BaseActivity(), ClickListener, PlainResponseListener {

    var mMenuRecycler: RecyclerView? = null
    var mRecycleviewHeader: LinearLayout? = null

    var mMenuAdapter: CustomerClubAdapter? = null

    var mClubDataAL = arrayListOf<ClubModel>()
    var mYearDataAl = arrayListOf<YearModel>()
    var mYearList = arrayListOf<String>()
    var mSelectedYear = ""
    var mSelectedYearId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_club)
        init()
    }

    override fun clickListener() {

    }

    fun setToolBar() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Customer Club"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun init() {

        mMenuRecycler = findViewById(R.id.recycleview)
        mRecycleviewHeader = findViewById(R.id.recycleview_header)
        setToolBar()
        //  setRecycleViewAdapter()
        clickListener()

        if (Utility.isInternetAvailable(myContext))
            getList()
        else AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_internet), "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }, false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed()

    }

    private fun setRecycleViewAdapter() {

        if (mClubDataAL.size > 0) {
            mRecycleviewHeader!!.visibility = View.VISIBLE
            mMenuRecycler!!.setHasFixedSize(true)
            mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
            mMenuRecycler!!.setNestedScrollingEnabled(true)
            mMenuAdapter = CustomerClubAdapter(myContext, mClubDataAL, this)
            mMenuRecycler!!.adapter = mMenuAdapter
        } else {
            mRecycleviewHeader!!.visibility = View.GONE
            AppDialogs.showToastshort(myContext, "No Data Found!!!. Please try again later")
            finish()
        }
    }

    override fun click(value: String) {


        AppDialogs.showSingleChoice(myContext, "Select Year", mYearList, object : SingleChoiceAdapter.Callback {
            override fun info(position: Int, text: String) {
                AppDialogs.hideSingleChoice()
                mSelectedYear = text

                mYearDataAl.forEach {

                    if (text.equals(it.year)) {
                        mSelectedYearId = it.periodYear!!
                    }
                }
            //    Log.e("selected Year ", mSelectedYearId)
                startActivity(Intent(myContext, CustomerClubDetailActivity::class.java).putExtra(getString(R.string.intent_customer_club_selected_date), mSelectedYearId).putExtra(getString(R.string.intent_customer_club_selected_display_date), mSelectedYear))


            }
        }, true, false)
    }

    private fun getList() {
        AppDialogs.showProgressDialog(myContext)


        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        AppServices.plainExecute(
                myContext, this,
                Request.Method.POST,
                when (LocalStorageSP.getLoginUser(myContext).USERTYPE) {
                    "0" -> AppServices.API.get_customer_club_executive
                    "1" -> AppServices.API.get_customer_club_manager
                    else -> AppServices.API.get_customer_club_executive
                }, jsonParam
        )
    }

    override fun onResponse(r: String?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        Log.e("Plain Response", r.toString())

        val json = JSONObject(r)

        if(json.getString("STATUS").equals("ok",true)) {

            val resultObject = json.getJSONObject("RESULT")

            //Get Year Data

            val yearJsonArray = json.getJSONArray("YEAR") as JSONArray
            mYearDataAl.clear()
            mYearList.clear()
            for (x in 0 until yearJsonArray.length()) {

                val jsonObj = yearJsonArray.getJSONObject(x)
                val yearmodel = YearModel()

                yearmodel.periodYear = jsonObj.getString("PERIOD_YEAR")
                yearmodel.year = jsonObj.getString("YEAR")
                mYearDataAl.add(yearmodel)
                mYearList.add(yearmodel.year!!)
            }


            val clubJsonArray = json.getJSONArray("CLUBNAME") as JSONArray

            mClubDataAL.clear()
            for (x in 0 until clubJsonArray.length()) {

                val jsonObj = clubJsonArray.getJSONObject(x)

                val mClubData = ClubModel()
                val clubName = jsonObj.getString("CLUB")
                mClubData.club = clubName
                mClubData.seqId = jsonObj.getInt("SEQ_ID")
                mClubData.count = clubName
                mClubData.count = "0"
                if (resultObject.has(clubName)) {
                    val clubCount = resultObject.getString(clubName)
                    mClubData.count = clubCount
                }

                mClubDataAL.add(mClubData)

            }

            setRecycleViewAdapter()

        }else{
            AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.msg_something_went_wrong), "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {
                    finish()
                }
            }, false)
        }

    }


}


