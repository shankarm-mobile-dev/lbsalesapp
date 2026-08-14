package com.suguna.lbsalesperson.soOrders

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
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.RegionModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.soOrders.adapter.soRecyclerAdapter
import com.suguna.lbsalesperson.soOrders.model.SoOrderModel
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class soViewActivity: BaseActivity(), BranchAllocationListener, DatePickerDialog.OnDateSetListener,
    ResponseListener {
    var mMenuRecycler: RecyclerView? = null
    var mHeaderRecycler: LinearLayout? = null
    var lyt_filter: LinearLayout? = null
    var mMenuAdapter: soRecyclerAdapter? = null
    var mSelectedDateValue: String? = null
    var mRegionList: ArrayList<RegionModel> = arrayListOf()
    var mRegionNameList: ArrayList<String> = arrayListOf()
    var mBranchNameList: ArrayList<String> = arrayListOf()
    var mCustNameList: ArrayList<String> = arrayListOf()
    var mliftotpdetails:ArrayList<SoOrderModel> = arrayListOf()
    var mliftotptempdetails:ArrayList<SoOrderModel> = arrayListOf()

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_so_order_view)
        sharedpreferences = getSharedPreferences(
            LoginActivity.MyPREFERENCES,
            MODE_PRIVATE)
        init()
    }
    override fun clickListener() {
        mFindTV.setOnClickListener {
            if (Utility.isInternetAvailable(myContext)) {
                if (validateGetOrdersDetails()) {
                    getOrderList()
                }
            } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)

        }
        mDatePickerTV.setOnClickListener {
            val c = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
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
                if (!mCustNameList.contains(it.customerName!!))
                    mCustNameList.add(it.customerName!!)
            }
            else
            {
                if (!(mCustNameList.contains(it.customerName!!)) && (branch_tv.text.equals(it.branchName)))
                    mCustNameList.add(it.customerName!!)
            }


        }
    }

    private fun getBranchname() {
        mBranchNameList.clear()
        mBranchNameList.add("All")
        mliftotpdetails.forEach {
            if(!mBranchNameList.contains(it.branchName!!))
                mBranchNameList.add(it.branchName!!)


        }
    }
    private fun getliftotp() {
        mliftotptempdetails.clear()
        mliftotpdetails.forEach {

            if(branch_tv.text.equals("All")) {
                if (customer_tv.text.equals("All")){
                    if(branch_tv.text.equals(it.branchName!!)) {
                        mliftotptempdetails.add(it)
                    }else {
                        mliftotptempdetails.add(it)
                    }
                }
                else if(customer_tv.text.equals(it.customerName!!))
                {
                    mliftotptempdetails.add(it)
                }
            }
            else
            {
                if(customer_tv.text.equals("All")) {
                    if(branch_tv.text.equals(it.branchName!!)) {
                        mliftotptempdetails.add(it)
                    }
                    else if(branch_tv.text.equals("All")) {
                        mliftotptempdetails.add(it)
                    }
                }
                else if (customer_tv.text.equals(it.customerName!!)) {
                    if(branch_tv.text.equals(it.branchName!!)) {
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
        supportActionBar!!.title = getString(R.string.label_so_order)
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

    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.get_so_regions.hashCode()) {
                val data = (r as RegionModel)
                mRegionList = data.data!!
                mRegionNameList.clear()
                mRegionList.forEach {

                    mRegionNameList.add(it.regionName!!)
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
                        getOrderList()

                    }

                }

            } else if (r.requestType.hashCode() == AppServices.API.get_so_order.hashCode()) {
                val data = (r as SoOrderModel)
                Log.e("come", "come")
                Log.e("data", data.data!!.size.toString())
                mliftotpdetails=data.data!!;
                if (data.data!!.size > 0) {
                    mHeaderRecycler!!.visibility = View.VISIBLE
                    lyt_filter!!.visibility = View.VISIBLE
                    customer_tv.text ="All"
                    branch_tv.text ="All"

                } else {
                    mHeaderRecycler!!.visibility = View.GONE
                    lyt_filter!!.visibility = View.GONE
                    AppDialogs.showToastshort(myContext, "No Data Found!!!")
                }
                setRecycleViewAdapter(data.data!!)
            }
            else if (r.requestType.hashCode() == AppServices.API.update_so_order.hashCode()) {

                    getOrderList()


            }


        }
    }
    private fun setRecycleViewAdapter(data: ArrayList<SoOrderModel>) {
        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = soRecyclerAdapter(myContext, data)
        mMenuRecycler!!.adapter = mMenuAdapter
    }
    private fun getRegionList() {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_so_regions,
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
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_so_order,
            SoOrderModel::class.java, jsonParam

        )
    }

    public fun update_soList(refNo: String?, qty: String, reason: String, status: String) {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("HEADER_ID", refNo)
        jsonParam.put("QUANTITY", qty)
        jsonParam.put("REASON", reason)
        jsonParam.put("STATUS", status)
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.update_so_order,
            RegionModel::class.java, jsonParam

        )
    }
}
