package com.suguna.lbsalesperson.branchallocation.activity


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
import com.suguna.lbsalesperson.Database.DatabaseUpDown
import com.suguna.lbsalesperson.LoginActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.*
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.branchallocation.adapter.BranchAllocationRecyclerAdapter
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.BranchAllocationModel
import com.suguna.lbsalesperson.branchallocation.model.RegionModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BranchAllocationActivity : BaseActivity(), BranchAllocationListener, DatePickerDialog.OnDateSetListener, ResponseListener {

    var db: DatabaseUpDown? = null
    var mMenuRecycler: RecyclerView? = null
    var mHeaderRecycler: LinearLayout? = null

    var mMenuAdapter: BranchAllocationRecyclerAdapter? = null

    var mSelectedDateValue: String? = null
    var mRegionList: ArrayList<RegionModel> = arrayListOf()

    var mRegionNameList: ArrayList<String> = arrayListOf()
    var mRegionId: String = ""
    //Views

    lateinit var mDatePickerTV: TextView;
    lateinit var mRegionTV: TextView;
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
        setContentView(R.layout.activity_branch_allocation)
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES,
                MODE_PRIVATE)
        init()
    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
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
            //DateUtil.showFromDateDialog(myContext, mSelectedDateValue!!, this, false)
            val c = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            //sharedpreferences!!.getString(DAYS, "")?.toInt()!!
            custdate = CustomDate_Edittext_1(this, mDatePickerTV, "Date ", "Order Date", sharedpreferences!!.getString(DAYS, "")?.toInt()!!, sdf.format(Date()))
        }

        mRegionTV.setOnClickListener {


            AppDialogs.showSingleChoice(myContext, "Select Region", mRegionNameList, object : SingleChoiceAdapter.Callback {
                override fun info(position: Int, text: String) {
                    AppDialogs.hideSingleChoice()
                    mRegionTV.text = text
                    mRegionId = Utility.getIdFromName(mRegionList, text)
                    Log.e("Region Id", mRegionId)

                }
            }, true, true)
        }
    }

    override fun init() {

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Branch Allocation"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        mMenuRecycler = findViewById(R.id.recycleview)
        mHeaderRecycler = findViewById(R.id.recycleview_header)
        mDatePickerTV = findViewById(R.id.date_picker_tv)
        mRegionTV = findViewById(R.id.region_tv)
        mFindTV = findViewById(R.id.find_tv)

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

    private fun getCurrentDate() {
        mSelectedDateValue = DateUtil.currentDate
        mSelectedDateValue = DateUtil.convertDateFormat(mSelectedDateValue!!, "dd MMM yyyy", "dd-MMM-yyyy")
        mDatePickerTV.text = mSelectedDateValue
    }

    private fun setRecycleViewAdapter(data: BranchAllocationModel) {

        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)

        mMenuAdapter = BranchAllocationRecyclerAdapter(myContext, data.orders!!, data.branches!!, this)
        mMenuRecycler!!.adapter = mMenuAdapter
    }

    override fun onClick(view: View?, position: Int) {

    }

    override fun onLongClick(view: View?, position: Int) {

    }

    override fun onSubmit() {
        getOrderList()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mSelectedDateValue = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
        mSelectedDateValue = DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy")
        mDatePickerTV.text = mSelectedDateValue
    }

    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.get_empbranch_allocation.hashCode()) {
                val data = (r as RegionModel)
                mRegionList = data.data!!
                mRegionNameList.clear()
                mRegionList.forEach {

                    mRegionNameList.add(it.regionName!!)
                }

            } else if (r.requestType.hashCode() == AppServices.API.get_get_empbranch_orderdetails.hashCode()) {
                val data = (r as BranchAllocationModel)

                if (data.orders!!.size > 0) {
                    mHeaderRecycler!!.visibility = View.VISIBLE

                } else {
                    mHeaderRecycler!!.visibility = View.GONE
                    AppDialogs.showToastshort(myContext, "No Data Found!!!")
                }
                setRecycleViewAdapter(data)
            }
        }
    }

    private fun getRegionList() {
        AppDialogs.showProgressDialog(myContext)

        //getRegion_asyn().execute()
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.get_empbranch_allocation,
                RegionModel::class.java, jsonParam

        )
    }

    private fun getOrderList() {
        AppDialogs.showProgressDialog(myContext)

        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("REGIONCODE", mRegionId)
        jsonParam.put("ORDERDATE", mDatePickerTV.text.toString())
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.get_get_empbranch_orderdetails,
                BranchAllocationModel::class.java, jsonParam

        )
    }

    /* inner class getRegion_asyn : AsyncTask<String?, String?, Boolean>() {

         override fun onPreExecute() {
             super.onPreExecute()

         }


         override fun onPostExecute(result: Boolean) {
             super.onPostExecute(result)
             AppDialogs.hideProgressDialog()
         }

         override fun doInBackground(vararg params: String?): Boolean {
             db = DatabaseUpDown(myContext)
             db!!.getRegionDetails()
             return true
         }
     }*/
}