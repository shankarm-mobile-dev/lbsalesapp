package com.suguna.lbsalesperson.bookOrdersView

import android.R.attr
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.LoginActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.CustomDate_Edittext_1
import com.suguna.lbsalesperson.Utils.DateUtil
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.bookOrdersView.adapter.bookorderRecyclerAdapter
import com.suguna.lbsalesperson.bookOrdersView.model.BookOrderOtpModel
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.RegionModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class bookOrderViewActivity : BaseActivity(), BranchAllocationListener, DatePickerDialog.OnDateSetListener,
    ResponseListener {
    var mMenuRecycler: RecyclerView? = null
    var mHeaderRecycler: LinearLayout? = null
    var lyt_filter: LinearLayout? = null
    var mMenuAdapter: bookorderRecyclerAdapter? = null
    var mSelectedDateValue: String? = null
    var mRegionList: ArrayList<RegionModel> = arrayListOf()
    var mReasonList: ArrayList<RegionModel.ReasonModel> = arrayListOf()
    var mRegionNameList: ArrayList<String> = arrayListOf()
    var mReasonNameList: ArrayList<String> = arrayListOf()
    var mBranchNameList: ArrayList<String> = arrayListOf()
    var mCustNameList: ArrayList<String> = arrayListOf()
    var mliftotpdetails:ArrayList<BookOrderOtpModel> = arrayListOf()
    var mliftotptempdetails:ArrayList<BookOrderOtpModel> = arrayListOf()

    public var mRegionId: String = ""
    //Views
    lateinit var mDatePickerTV: TextView;
    lateinit var mRegionTV: TextView;
    lateinit var branch_tv: TextView;
    lateinit var customer_tv: TextView;
    lateinit var mFindTV: TextView;
    var custdate: CustomDate_Edittext_1? = null
    val MyPREFERENCES = "MyPrefs"
    val Name = "name"
    val Password = "pass"
    val DeviceId = "deviceid"
    val DAYS = "days"
    var sharedpreferences: SharedPreferences? = null
    var mStatus: String = ""
    var mBranch: String = ""
    var mDownStatus: String = ""
    lateinit var recycleview_bottom:LinearLayout
    lateinit var tot_declare_qty:TextView
    lateinit var tot_booked_qty:TextView
    lateinit var tot_booked_per:TextView
    var mOrderRefresh: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_order_view)
        sharedpreferences = getSharedPreferences(
            LoginActivity.MyPREFERENCES,
            MODE_PRIVATE)
        init()
    }
    override fun clickListener() {
        mFindTV.setOnClickListener {
            if (Utility.isInternetAvailable(myContext)) {
                if (validateGetOrdersDetails()) {
                    mOrderRefresh="0";
                    getOrderList()
                }
            } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)

        }
        mDatePickerTV.setOnClickListener {
            // DateUtil.showFromDateDialog(myContext, mSelectedDateValue!!, this, false)
            val c = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            //sharedpreferences!!.getString(DAYS, "")?.toInt()!!
            custdate = CustomDate_Edittext_1(this, mDatePickerTV, "Date ", "Order Date", sharedpreferences!!.getString(DAYS, "")?.toInt()!!, sdf.format(
                Date()
            ))
        }

        mRegionTV.setOnClickListener {

            if(mRegionList.size>0)
            {

                AppDialogs.showSingleChoice(
                    myContext,
                    "Select Region",
                    mRegionNameList,
                    object : SingleChoiceAdapter.Callback {
                        override fun info(position: Int, text: String) {
                            AppDialogs.hideSingleChoice()
                            mRegionTV.text = text
                            mRegionId = Utility.getIdFromName(mRegionList, text)
                            Log.e("Region Id", mRegionId)
                        }
                    },
                    true,
                    true
                )
            }
            else
            {
                AppDialogs.showToastDialog(myContext, "Region is not available")
            }
        }

        branch_tv.setOnClickListener {

            if(mliftotpdetails.size>0)
            {
                getBranchname()
                AppDialogs.showSingleChoice(
                    myContext,
                    "Select Branch",
                    mBranchNameList,
                    object : SingleChoiceAdapter.Callback {
                        override fun info(position: Int, text: String) {
                            AppDialogs.hideSingleChoice()
                            branch_tv.text = text
                            customer_tv.text ="All"
                            getliftotp()
                            // mRegionId = Utility.getIdFromName(mRegionList, text)
                            Log.e("Region Id", mRegionId)
                        }
                    },
                    true,
                    true
                )
            }
            else
            {
                AppDialogs.showToastDialog(myContext, "Branch is not available")
            }
        }

        customer_tv.setOnClickListener {

            if(mRegionList.size>0)
            {
                getCustomername()
                AppDialogs.showSingleChoice(
                    myContext,
                    "Select Customer",
                    mCustNameList,
                    object : SingleChoiceAdapter.Callback {
                        override fun info(position: Int, text: String) {
                            AppDialogs.hideSingleChoice()
                            customer_tv.text = text
                            getliftotp()
                            // mRegionId = Utility.getIdFromName(mRegionList, text)
                            Log.e("Region Id", mRegionId)
                        }
                    },
                    true,
                    true
                )
            }
            else
            {
                AppDialogs.showToastDialog(myContext, "Customer is not available")
            }
        }
    }

    private fun getCustomername() {
        mCustNameList.clear()
        mCustNameList.add("All")
        mliftotpdetails.forEach {

            if(branch_tv.text.equals("All")) {
                if (!mCustNameList.contains(it.locationName!!))
                    mCustNameList.add(it.locationName!!)
            }
            else
            {
                if (!(mCustNameList.contains(it.locationName!!)) && (branch_tv.text.equals(it.locationName)))
                    mCustNameList.add(it.locationName!!)
            }


        }
    }

    private fun getBranchname() {
        mBranchNameList.clear()
        mBranchNameList.add("All")
        mliftotpdetails.forEach {
            if(!mBranchNameList.contains(it.locationName!!))
                mBranchNameList.add(it.branchCode!!)


        }
    }
    private fun getliftotp() {
        mliftotptempdetails.clear()
        mliftotpdetails.forEach {

            if(branch_tv.text.equals("All")) {
                if (customer_tv.text.equals("All")){
                    if(branch_tv.text.equals(it.locationName!!)) {
                        mliftotptempdetails.add(it)
                    }else {
                        mliftotptempdetails.add(it)
                    }
                }
                else if(customer_tv.text.equals(it.locationName!!))
                {
                    mliftotptempdetails.add(it)
                }
            }
            else
            {
                if(customer_tv.text.equals("All")) {
                    if(branch_tv.text.equals(it.locationName!!)) {
                        mliftotptempdetails.add(it)
                    }
                    else if(branch_tv.text.equals("All")) {
                        mliftotptempdetails.add(it)
                    }
                }
                else if (customer_tv.text.equals(it.locationName!!)) {
                    if(branch_tv.text.equals(it.locationName!!)) {
                        mliftotptempdetails.add(it)
                    }
                    else if(branch_tv.text.equals("All")) {
                        mliftotptempdetails.add(it)
                    }
                }
            }
        }
        setRecycleViewAdapter(mliftotptempdetails)
    }

    @SuppressLint("GestureBackNavigation")
    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun init() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.label_book_order_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mMenuRecycler = findViewById(R.id.recycleview)
        mHeaderRecycler = findViewById(R.id.recycleview_header)
        mDatePickerTV = findViewById(R.id.date_picker_tv)
        mRegionTV = findViewById(R.id.region_tv)
        mFindTV = findViewById(R.id.find_tv)
        lyt_filter= findViewById(R.id.lyt_filter)
        branch_tv = findViewById(R.id.branch_tv)
        customer_tv  = findViewById(R.id.customer_tv)
        recycleview_bottom = findViewById(R.id.recycleview_bottom)
        tot_declare_qty= findViewById(R.id.tot_declare_qty)
        tot_booked_qty= findViewById(R.id.tot_booked_qty)
        tot_booked_per= findViewById(R.id.tot_booked_per)
        getCurrentDate()
        clickListener()

        if (Utility.isInternetAvailable(myContext))
            getRegionList()
        else AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_internet), "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }, false)
    }


    fun validateGetOrdersDetails(): Boolean {

        if (mSelectedDateValue.isNullOrEmpty()) {
            AppDialogs.showToastDialog(myContext, "Please Choose Order Date")
            return false
        } else if (mRegionId.isEmpty()) {
            AppDialogs.showToastDialog(myContext, "Please Select Region")
            return false
        } else {
            return true
        }

    }
    private fun getCurrentDate() {
        mSelectedDateValue = DateUtil.currentDate
        mSelectedDateValue = DateUtil.convertDateFormat(mSelectedDateValue!!, "dd MMM yyyy", "dd-MMM-yyyy")
        mDatePickerTV.text = mSelectedDateValue
    }
    private fun getpreviousDate() {
        mSelectedDateValue = DateUtil.previousDays
        mSelectedDateValue = DateUtil.convertDateFormat(mSelectedDateValue!!, "dd MMM yyyy", "dd-MMM-yyyy")
        mDatePickerTV.text = mSelectedDateValue
    }

    override fun onClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onSubmit() {
        TODO("Not yet implemented")
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        TODO("Not yet implemented")
    }

    @SuppressLint("DefaultLocale")
    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.get_bookorder_regions.hashCode()) {
                val data = (r as RegionModel)
                mRegionList = data.data!!

                mRegionNameList.clear()
                mReasonNameList.clear()
                mRegionList.forEach {

                    mRegionNameList.add(it.regionName!!)

                    mReasonList=it.data_reason!!

                    val editor = sharedpreferences!!.edit()
                    editor.putString(DAYS, it.ENTRYDAYS!!)
                    editor.commit()

                    if(mRegionList.size==1)
                    {
                        mRegionTV.text=it.regionName!!
                        mRegionId =it.regionCode!!
                        if( it.ENTRYDAYS!! != "0") {
                            getpreviousDate()

                        }
                        // getpreviousDate()
                        mOrderRefresh="0";
                        getOrderList()

                    }

                }

            } else if (r.requestType.hashCode() == AppServices.API.get_bookorder_order.hashCode()) {
                val data = (r as BookOrderOtpModel)
                Log.e("come", "come")
                Log.e("data", data.data!!.size.toString())
                mliftotpdetails=data.data!!;
                if (data.data!!.size > 0) {
                    mHeaderRecycler!!.visibility = View.VISIBLE
                    lyt_filter!!.visibility = View.GONE
                    customer_tv.text ="All"
                    branch_tv.text ="All"
                    recycleview_bottom!!.visibility = View.VISIBLE

                    var tot_declared=0
                    var tot_booked=0
                   // var tot_bookedper=0.0f
                    for (item in data.data!!) {
                        if(item.availableKg!!.length>0)
                        {
                            tot_declared += item.availableKg.toInt()
                        }
                        if(item.bookedKg!!.length>0)
                        {
                            tot_booked += item.bookedKg.toInt()
                        }

                    }

                    /*if(tot_booked>0)
                    {
                        tot_bookedper = ((tot_booked/tot_declared)).toFloat()*100

                    }*/
                    if(tot_declared.toFloat()>0) {
                        val tot_bookedper = (tot_booked.toFloat() / tot_declared.toFloat()) * 100
                        val rounded = BigDecimal(tot_bookedper.toDouble())
                            .setScale(2, RoundingMode.HALF_UP)
                        tot_booked_per.text= rounded.toString()
                    }
                    tot_declare_qty.text= tot_declared.toString()
                    tot_booked_qty.text=tot_booked.toString()
                   // val formatted_per = String.format("%.2f", tot_bookedper)


                } else {
                    mHeaderRecycler!!.visibility = View.GONE
                    recycleview_bottom!!.visibility = View.GONE
                    lyt_filter!!.visibility = View.GONE
                    AppDialogs.showToastshort(myContext, "No Data Found!!!")
                }
                if(mOrderRefresh=="0") {
                    setRecycleViewAdapter(data.data!!)
                }
                else
                {
                    setRecycleRefreshViewAdapter(data.data!!)
                }
            }
            else if (r.requestType.hashCode() == AppServices.API.update_bookorder.hashCode()) {
                if(mStatus.equals("Y"))
                {
                    AppDialogs.showToastshort(myContext, "Order booking has been stopped ")
                    mOrderRefresh="1";
                    getOrderList()
                }
                else
                {
                AppDialogs.showToastshort(myContext, "Order booking has been restarted ")
                    mOrderRefresh="1";
                    getOrderList()
}

            }
            else if (r.requestType.hashCode() == AppServices.API.update_pricelistorder.hashCode()) {
                if(mStatus.equals("Y"))
                {
                    AppDialogs.showToastshort(myContext, "Price list Showing in SMART APP has been stopped ")
                    mOrderRefresh="1";
                    getOrderList()
                }
                else
                {
                    AppDialogs.showToastshort(myContext, "Price list Showing in SMART APP has been restarted ")
                    mOrderRefresh="1";
                    getOrderList()
                }

            }
            else if (r.requestType.hashCode() == AppServices.API.update_downtrentorder.hashCode()) {
                if(r.responseCode ==200) {
                    if (mStatus.equals("Y")) {
                        AppDialogs.showToastshort(myContext, "Down trend is enabled. ")
                        mOrderRefresh="1";
                        getOrderList()
                    } else {
                        AppDialogs.showToastshort(myContext, "Down trend is disabled. ")
                        mOrderRefresh="1";
                        getOrderList()
                    }
                }
                else
                {
                    AppDialogs.showToastshort(myContext, r.responseMessage)
                }

            }


        }
    }
    private fun setRecycleViewAdapter(data: ArrayList<BookOrderOtpModel>) {
        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = bookorderRecyclerAdapter(myContext, data)
        mMenuRecycler!!.adapter = mMenuAdapter



    }
    private fun setRecycleRefreshViewAdapter(data: ArrayList<BookOrderOtpModel>) {

        mMenuAdapter?.refreshList(data)




    }

    private fun getRegionList() {
        AppDialogs.showProgressDialog(myContext)
        //getRegion_asyn().execute()
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_bookorder_regions,
            RegionModel::class.java, jsonParam

        )
    }
    private fun getOrderList() {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        jsonParam.put("REGIONCODE", mRegionId)
        jsonParam.put("ORDERDATE", mDatePickerTV.text.toString())
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_bookorder_order,
            BookOrderOtpModel::class.java, jsonParam

        )
    }

    public fun update_bookorderList(branhcode: String?, status: String, locationName: String?) {

        //getRegion_asyn().execute()
        mStatus=status
        mReasonNameList.clear()
        var reason=""
        var reasonheading="";
        if (locationName != null) {
            mBranch=locationName
        }

            mReasonList.forEach {
                if(mStatus.equals("Y")) {
                    if (it.isStopped!!.equals("1")) {
                        mReasonNameList.add(it.Reason!!)
                    }
                    reasonheading=" Select Stop Reason"
                }
                else
                {
                    if (it.isStopped!!.equals("0")) {
                        mReasonNameList.add(it.Reason!!)
                    }
                    reasonheading=" Select Start Reason"
                }
            }

        AppDialogs.showSingleChoice(
            myContext,
            reasonheading,
            mReasonNameList,
            object : SingleChoiceAdapter.Callback {
                override fun info(position: Int, text: String) {
                    AppDialogs.hideSingleChoice()
                    AppDialogs.showProgressDialog(myContext)
                    reason= text
                    val jsonParam = JSONObject()
                    jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
                    jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
                    jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)

                    jsonParam.put("BRANCHCODE", branhcode)
                    jsonParam.put("ORDERDATE", mDatePickerTV.text.toString())
                    jsonParam.put("STATUS", status)
                    jsonParam.put("REASON", reason)
                    AppServices.execute(
                        myContext, this@bookOrderViewActivity,
                        Request.Method.POST,
                        AppServices.API.update_bookorder,
                        RegionModel::class.java, jsonParam

                    )
                    Log.e("Reason", reason)
                }
            },
            true,
            true
        )


    }

    public fun update_pricestopList(branhcode: String?, status: String, locationName: String?) {

        //getRegion_asyn().execute()
        mStatus=status
        mReasonNameList.clear()
        var reason=""
        var reasonheading="";
        if (locationName != null) {
            mBranch=locationName
        }

        mReasonList.forEach {
            if(mStatus.equals("Y")) {
                if (it.isStopped!!.equals("1.1")) {
                    mReasonNameList.add(it.Reason!!)
                }
                reasonheading=" Select Disable Reason"
            }
            else
            {
                if (it.isStopped!!.equals("0.0")) {
                    mReasonNameList.add(it.Reason!!)
                }
                reasonheading=" Select Disable Reason"
            }
        }

        AppDialogs.showSingleChoice(
            myContext,
            reasonheading,
            mReasonNameList,
            object : SingleChoiceAdapter.Callback {
                override fun info(position: Int, text: String) {
                    AppDialogs.hideSingleChoice()
                    AppDialogs.showProgressDialog(myContext)
                    reason= text
                    val jsonParam = JSONObject()
                    jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
                    jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
                    jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)

                    jsonParam.put("BRANCHCODE", branhcode)
                    jsonParam.put("ORDERDATE", mDatePickerTV.text.toString())
                    jsonParam.put("STATUS", status)
                    jsonParam.put("REASON", reason)
                    AppServices.execute(
                        myContext, this@bookOrderViewActivity,
                        Request.Method.POST,
                        AppServices.API.update_pricelistorder,
                        RegionModel::class.java, jsonParam

                    )
                    Log.e("Reason", reason)
                }
            },
            true,
            true
        )


    }

    public fun update_downtrendList(branhcode: String?, status: String, locationName: String?) {

        //getRegion_asyn().execute()
        mStatus=status
        mReasonNameList.clear()
        var reason=""
        var reasonheading="";
        if (locationName != null) {
            mBranch=locationName
        }

        mReasonList.forEach {
            if(mStatus.equals("Y")) {
                if (it.isStopped!!.equals("1.1")) {
                    mReasonNameList.add(it.Reason!!)
                }
                reasonheading=" Select Disable Reason"
            }
            else
            {
                if (it.isStopped!!.equals("0.0")) {
                    mReasonNameList.add(it.Reason!!)
                }
                reasonheading=" Select Disable Reason"
            }
        }

        AppDialogs.showSingleChoice(
            myContext,
            reasonheading,
            mReasonNameList,
            object : SingleChoiceAdapter.Callback {
                override fun info(position: Int, text: String) {
                    AppDialogs.hideSingleChoice()
                    AppDialogs.showProgressDialog(myContext)
                    reason= text
                    val jsonParam = JSONObject()
                    jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
                    jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
                    jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)

                    jsonParam.put("BRANCHCODE", branhcode)
                    jsonParam.put("ORDERDATE", mDatePickerTV.text.toString())
                    jsonParam.put("STATUS", status)
                    jsonParam.put("REASON", reason)
                    AppServices.execute(
                        myContext, this@bookOrderViewActivity,
                        Request.Method.POST,
                        AppServices.API.update_downtrentorder,
                        Response::class.java, jsonParam

                    )
                    Log.e("Reason", reason)
                }
            },
            true,
            true
        )


    }

}