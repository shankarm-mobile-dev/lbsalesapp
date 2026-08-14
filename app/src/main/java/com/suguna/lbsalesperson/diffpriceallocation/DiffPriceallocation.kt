package com.suguna.lbsalesperson.diffpriceallocation

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.LoginActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.diffpriceallocation.Adapter.DiffPriceRecyclerAdapter
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DiffPriceallocation : BaseActivity(), BranchAllocationListener, DatePickerDialog.OnDateSetListener,ResponseListener{
    var sharedpreferences: SharedPreferences? = null
    val MyPREFERENCES = "MyPrefs"
    val Name = "name"
    val Password = "pass"
    val DeviceId = "deviceid"
    var saveButton: LinearLayout? = null
    var mMenuRecycler: RecyclerView? = null
    var mMenuAdapter: DiffPriceRecyclerAdapter? = null
    var edtdate:EditText ? = null
    var edtregion:EditText?=null
    private var mYear = 0
    private var mMonth = 0
    private val mDay = 0
    val amonthList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diff_priceallocation)
        sharedpreferences = getSharedPreferences(
            LoginActivity.MyPREFERENCES,
            MODE_PRIVATE)
        init()
        clickListener()
      getDiffpricenameList()
      getDiffpricedeclareList()

    }

    override fun onResume() {
      //  getDiffpricenameList_resume()
        super.onResume()
    }
    override fun init() {
        saveButton=findViewById(R.id.saveButton);
        mMenuRecycler = findViewById(R.id.recycleview)
        edtdate = findViewById(R.id.edtdate)
        edtregion= findViewById(R.id.edtregion)
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("MMM-yyyy", Locale.ENGLISH)
        val strDate = format.format(calendar.getTime())
        edtdate!!.setText(strDate)

    }

    override fun clickListener() {


        saveButton?.setOnClickListener(View.OnClickListener {
            month=edtdate!!.text.toString().trim()
            cur_position="0"
            val bottomSheet = BottomSheetDialog()
            bottomSheet.show(
                supportFragmentManager,
                "ModalBottomSheet"
            )
        })
        edtdate?.setOnClickListener(View.OnClickListener {

            getcal()
        })

    }
    @Throws(ParseException::class)
    private fun getcal() {
        amonthList.clear()
        val allDates: MutableList<String> = ArrayList()
        val calendar = Calendar.getInstance()
        //web.getSettings().setBuiltInZoomControls(true);
        val c = Calendar.getInstance()
        mYear = c[Calendar.YEAR]
        mMonth = c[Calendar.MONTH]
        calendar[mYear, mMonth] = mDay
        calendar.add(Calendar.MONTH, 1)
        val format = SimpleDateFormat("MMM-yyyy", Locale.ENGLISH)
        val strDate = format.format(calendar.time)
        val monthDate = SimpleDateFormat("MMM-yyyy", Locale.ENGLISH)
        val cal = Calendar.getInstance()
        cal.time = monthDate.parse(strDate)
        for (i in 1..3) {
            val month_name1 = monthDate.format(cal.time)
            allDates.add(month_name1.uppercase())
            cal.add(Calendar.MONTH, -1)
            amonthList.add(month_name1.uppercase());
        }

        AppDialogs.showSingleChoice(myContext, "Choose month", amonthList, object : SingleChoiceAdapter.Callback {
            override fun info(pos: Int, text: String) {
                AppDialogs.hideSingleChoice()
                edtdate!!.setText(text)
                getDiffpricedeclareList()
            }
        }, true, true)
        Log.e("month", allDates.toString())

    }

    //Get first date in month
    fun get_min_date(date: String?): String? {
        var formatdate = ""
        try {
            val monthDate = SimpleDateFormat("MMM-yyyy", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            cal.time = monthDate.parse(date)
            cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
            val format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            formatdate = format.format(cal.time)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return formatdate
    }

    //Get last date in month
    fun get_max_date(date: String?): String? {
        var formatdate = ""
        try {
            val monthDate = SimpleDateFormat("MMM-yyyy", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            cal.time = monthDate.parse(date)
            cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val format = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            formatdate = format.format(cal.time)
        } catch (e: ParseException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return formatdate
    }
    fun call(position: Int) {
        month=edtdate!!.text.toString().trim()
        cur_position=position.toString()
        val bottomSheet = BottomSheetDialog()
        bottomSheet.show(
            supportFragmentManager,
            "ModalBottomSheet"
        )
    }
    private fun getDiffpricenameList() {
       val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_diffprice_lookup,
            DiffnameModel::class.java, jsonParam

        )

    }
    private fun getDiffpricedeclareList() {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        jsonParam.put("FROMDATE", get_min_date(edtdate!!.text.toString().trim()))
        jsonParam.put("TODATE", get_max_date(edtdate!!.text.toString().trim()))
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_declare_diffprice,
            DiffdeclareModel::class.java, jsonParam

        )

    }
    public fun getDiffpricenameList_resume() {

        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, Name, "")!!)
        //jsonParam.put("ORDERDATE", DateUtil.convertDateFormat(mSelectedDateValue!!, "dd-MM-yyyy", "dd-MMM-yyyy"))
        Log.e("jsonParam", jsonParam.toString());
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_declare_diffprice,
            DiffdeclareModel::class.java, jsonParam
        )

    }
    private fun setRecycleViewAdapter(data: ArrayList<DiffdeclareModel>) {
        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)
        mMenuAdapter = DiffPriceRecyclerAdapter(myContext, data!!,this)
        mMenuRecycler!!.adapter = mMenuAdapter
    }
    override fun onResponse(r: Response?) {
        runOnUiThread {
            AppDialogs.hideProgressDialog()
        }
        if (r != null) {
            if (r.requestType.hashCode() == AppServices.API.get_diffprice_lookup.hashCode()) {
                val data = (r as DiffnameModel)
                mDiffnameList= data.data!!
                mDiffnameList.forEach {
                   it.REGION?.let {
                       it1 -> Log.e("REGION", it1)
                       edtregion?.setText(it1)
                   };
                }
            }

            if (r.requestType.hashCode() == AppServices.API.get_declare_diffprice.hashCode()) {
                val data = (r as DiffdeclareModel)
                mDiffdeclareList= data.data!!
                setRecycleViewAdapter(mDiffdeclareList)

            }
        }
    }
    override fun onClick(view: View?, position: Int) {

    }

    override fun onLongClick(view: View?, position: Int) {

    }

    override fun onSubmit() {

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

    }

    companion object {
        lateinit var mDiffnameList: ArrayList<DiffnameModel>
        lateinit var month:String
        lateinit var cur_position:String
        lateinit var mDiffdeclareList: ArrayList<DiffdeclareModel>
    }

}


