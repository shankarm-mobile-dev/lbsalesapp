package com.suguna.lbsalesperson.customerclub.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.customerclub.adapter.CustomerClubDetailsAdapter
import com.suguna.lbsalesperson.customerclub.model.CustomerClubDetailModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.listener.ClickListener
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject

class CustomerClubDetailActivity : BaseActivity(), ClickListener, ResponseListener {

    var mMenuRecycler: RecyclerView? = null
    var mSearchED: EditText? = null
    var mSearchCloseImg: ImageView? = null
    var mNoDataFoundTV: TextView? = null

    var mMenuAdapter: CustomerClubDetailsAdapter? = null

    var mData = arrayListOf<CustomerClubDetailModel.ClubDetails>()

    var mSelectedDateTv: TextView? = null

    var mSelectedDate: String? = null
    var mCurrentYearClubTv: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_club_detail)
        init()


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun filter(aQuery: String): ArrayList<CustomerClubDetailModel.ClubDetails>? {
        val mFilterData: ArrayList<CustomerClubDetailModel.ClubDetails> = arrayListOf()
        if (mData.size > 0) {
            for (i in mData.indices) {
                if (aQuery?.lowercase()
                        ?.let { mData.get(i).customerName!!.lowercase().contains(it) } == true || aQuery?.lowercase()
                        ?.let { mData.get(i).customerCode!!.lowercase().contains(it) } == true
                ) {
                    mFilterData.add(mData.get(i))
                }
            }
        }
        return mFilterData
    }

    override fun clickListener() {
        mSearchCloseImg?.setOnClickListener {
            mSearchED?.setText("")
            setRecycleViewAdapter(mData)
        }

        mSearchED?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (!s.isNullOrBlank() && s.toString().length > 0) {
                    val mFilterData = filter(s.toString())
                    setRecycleViewAdapter(mFilterData!!)
                } else {
                    setRecycleViewAdapter(mData)
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

    private fun getList() {
        AppDialogs.showProgressDialog(myContext)


        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("YEAR", mSelectedDate)
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                when (LocalStorageSP.getLoginUser(myContext).USERTYPE) {
                    "0" -> AppServices.API.get_empexe_cusclub_drill
                    "1" -> AppServices.API.get_customer_club_details
                    else -> AppServices.API.get_customer_club_details
                },
                CustomerClubDetailModel::class.java, jsonParam

        )
    }


    override fun init() {


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Customer Club"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mMenuRecycler = findViewById(R.id.recycleview)
        mSearchED = findViewById(R.id.search)
        mSearchCloseImg = findViewById(R.id.close)
        mNoDataFoundTV = findViewById(R.id.tv_no_data)
        mSelectedDateTv = findViewById(R.id.selected_year_tv)
        mCurrentYearClubTv = findViewById(R.id.current_year_tv)


        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(getString(R.string.intent_customer_club_selected_date))) {
                mSelectedDate = bundle.getString(getString(R.string.intent_customer_club_selected_date))
            }
            if (bundle.containsKey(getString(R.string.intent_customer_club_selected_display_date))) {
                mSelectedDateTv!!.text = bundle.getString(getString(R.string.intent_customer_club_selected_display_date)) + " Club"
            }
        }

        clickListener()

        callwebService()
    }


    private fun callwebService() {

        if (Utility.isInternetAvailable(myContext))
            getList()
        else AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_internet), "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }, false)
    }

    private fun setRecycleViewAdapter(aData: ArrayList<CustomerClubDetailModel.ClubDetails>) {
        mMenuRecycler!!.setHasFixedSize(false)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = CustomerClubDetailsAdapter(myContext, aData, this)
        mMenuRecycler!!.adapter = mMenuAdapter
        if (aData.size > 0) {
            mNoDataFoundTV?.visibility = View.GONE

        } else{
            mNoDataFoundTV?.visibility = View.VISIBLE
        }
    }

    override fun click(value: String) {

    }

    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {
            if (r.requestType!! == AppServices.API.get_customer_club_details.hashCode() || r.requestType!! == AppServices.API.get_empexe_cusclub_drill.hashCode()) {
                val data = (r as CustomerClubDetailModel)

                // weekDetailsTv!!.text = "Week No." + data.WeekDetails!!.weekNo.toString() + " - Qty In Tons"
                val listData = data.data
                if (!listData.isNullOrEmpty() && listData.size > 0) {
                    mCurrentYearClubTv!!.text = listData.get(0).year
                    mData = listData

                    setRecycleViewAdapter(mData)
                } else {
                    AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_data_found), getString(R.string.label_ok), object : AppDialogs.ConfirmListener {
                        override fun yes() {
                            finish()
                        }
                    }, false)
                }

            }
        }
    }
}