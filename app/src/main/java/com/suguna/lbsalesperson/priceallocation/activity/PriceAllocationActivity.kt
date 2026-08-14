package com.suguna.lbsalesperson.priceallocation.activity

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.*
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.priceallocation.DataAccessManager
import com.suguna.lbsalesperson.priceallocation.adapter.PriceRecyclerAdapter
import com.suguna.lbsalesperson.priceallocation.model.lbitemlist
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet
import com.suguna.lbsalesperson.priceallocation.model.lbpricedetlist
import com.suguna.lbsalesperson.priceallocation.model.lbpricemaxlist
import java.math.RoundingMode
import java.text.DecimalFormat

class PriceAllocationActivity : BaseActivity() {


    //var mProg: ProgressDialog? = null
    var mresponse: lbpricedetlist? = null
    var responsecode = 0
    var msg = ""
    private var mAuthTask: DataFetchTask? = null
    private var mSaveTask: DataSaveTask? = null
    private var mGetItemTask: DataGetItemTask? = null
    private var mGetpricemaxTask: DataGetPricevalue? = null
    var Selecteditemlist: List<HashMap<String, String>> = ArrayList()
    var ietmslist: ArrayList<lbpricedet>? = null
   // var itemsadapter: PriceAdapter? = null
    var ltv_itemslist: ListView? = null
    var edtdate: EditText? = null
    var saveButton: LinearLayout? = null
    var mlbitem: lbitemlist? = null
    var mlbpricemaxitem: lbpricemaxlist? = null
    var edtitem: EditText? = null
    var mEditable: Boolean? = null
    val mpricetype = "BASE"
    private var mDatePicker: DatePickerWidget? = null
    var tvservicedate1: TextView? = null
    var tvcobbwt: TextView? = null
    var tvservicedate2: TextView? = null
    var lyt_dyn: LinearLayout?=null
    var mMenuRecycler: RecyclerView? = null
    var mMenuAdapter: PriceRecyclerAdapter? = null
    var price_min:Float = 0.0f
    var price_max:Float = 200.0f
    var declare_value:Float = 0.1f
    var wt_min:Float = 0.0f
    var wt_max:Float = 3.0f
    var checkbox_fillfinalprice:CheckBox?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_allocation)
        init()
    }
    override fun onResume() {
        Log.e("resume","resume")
        super.onResume()
    }
    override fun clickListener() {
    }
    override fun init() {
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey(getString(R.string.is_editable))) {
                mEditable = bundle.getBoolean(getString(R.string.is_editable))
            }
        }
        ietmslist = ArrayList()

        ltv_itemslist = findViewById<View>(R.id.list) as ListView
        ltv_itemslist!!.transcriptMode = ListView.TRANSCRIPT_MODE_NORMAL
        //ltv_itemslist.setStackFromBottom(true);
        edtdate = findViewById<View>(R.id.edtdate) as EditText
        edtitem = findViewById<View>(R.id.edtitem) as EditText
        saveButton = findViewById<View>(R.id.saveButton) as LinearLayout
        tvservicedate1 = findViewById<View>(R.id.tvservicedate1) as TextView
        tvservicedate2 = findViewById<View>(R.id.tvservicedate2) as TextView
        tvcobbwt= findViewById<View>(R.id.tvcobbwt) as TextView
        mMenuRecycler = findViewById(R.id.recycleview)
        checkbox_fillfinalprice = findViewById<View>(R.id.checkbox_fillfinalprice) as CheckBox
        edtdate!!.isFocusable = false
        edtdate!!.isFocusableInTouchMode = false
        edtdate!!.isClickable = false
        edtitem!!.isFocusable = false
        edtitem!!.isFocusableInTouchMode = false
        edtitem!!.isClickable = false
        if (mEditable == false) {
            saveButton!!.visibility = View.INVISIBLE
            checkbox_fillfinalprice!!.visibility = View.GONE
        } else {
            saveButton!!.visibility = View.VISIBLE
            checkbox_fillfinalprice!!.visibility = View.GONE
        }
       // itemsadapter = PriceAdapter(myContext, R.layout.lbprice_list_item,
        //        ietmslist!!, mEditable!!)
       // ltv_itemslist!!.adapter = itemsadapter
        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)

        mMenuAdapter = PriceRecyclerAdapter(myContext, R.layout.lbprice_list_item,
            ietmslist!!, mEditable!!)
        mMenuRecycler!!.adapter = mMenuAdapter
        // TODO Auto-generated method stub

        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
            tvservicedate1!!.visibility = View.VISIBLE
            tvservicedate2!!.visibility = View.VISIBLE
            tvcobbwt!!.visibility = View.VISIBLE // to change
        }
        else
        {
            tvservicedate1!!.visibility = View.VISIBLE
            tvservicedate2!!.visibility = View.GONE
            tvcobbwt!!.visibility = View.GONE
            tvservicedate1!!.setText("Market Price")
            checkbox_fillfinalprice!!.visibility = View.GONE
        }
      //  tvservicedate1!!.visibility = View.GONE
       // tvservicedate2!!.visibility = View.GONE
        if (Utility.isInternetAvailable(myContext)) {
            AppDialogs.showProgressDialog(myContext)
            mGetItemTask = DataGetItemTask()
            mGetItemTask!!.execute(null as Void?)
            mGetpricemaxTask = DataGetPricevalue()
            mGetpricemaxTask!!.execute(null as Void?)
        } else AppDialogs.customOkAction(myContext, getString(R.string.title_alert), getString(R.string.no_internet), "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {
                finish()
            }
        }, false)
        setbtnclick()

        clickListener()

    }



    private fun setbtnclick() {
        if (mEditable == false) {
            edtdate!!.setOnClickListener {
                if (mDatePicker == null) {
                    val localActivity1 = myContext
                    val localDatePickerWidget1 = DatePickerWidget(
                            localActivity1, 1, null)
                    mDatePicker = localDatePickerWidget1
                    mDatePicker!!.mcurrentedittext = edtdate
                }
                mDatePicker!!.onSetDatePicker(null)
            }
        }
        else if (mEditable == true) {
            edtdate!!.setOnClickListener {
                if (mDatePicker == null) {
                    val localActivity1 = myContext
                    val localDatePickerWidget1 = DatePickerWidget(
                        localActivity1, 2, null)
                    mDatePicker = localDatePickerWidget1
                    mDatePicker!!.mcurrentedittext = edtdate
                }
                mDatePicker!!.onSetDatePicker(null)
            }
        }
        edtdate!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                val text = s.toString()
                if (text !== "" && !text.isEmpty()) {
                    if (mGetItemTask != null) {
                         AppDialogs.hideProgressDialog()
                       
                    }
                    // Toast.makeText(myContext, text,
                    // Toast.LENGTH_SHORT).show();
                   AppDialogs.showProgressDialog(myContext)
                    //showProgress(true)
                    mAuthTask = DataFetchTask(text)
                    mAuthTask!!.execute(null as Void?)
                }
            }
        })

        checkbox_fillfinalprice!!.setOnClickListener(View.OnClickListener {

            AppDialogs.showProgressDialog(myContext)
            if(checkbox_fillfinalprice!!.isChecked)
            {
              for (i in ietmslist!!.indices) {
                  val det = ietmslist!![i]
                  if(det.proV_STATUS=="Y" && det.status == "N")
                  {
                      val localRate = det.proV_PRICE
                      val localRate1=det.proV_PRICE.toDouble()
                      if (localRate.length > 0) {
                          if (localRate1 > 0 && det.proV_STATUS == "Y" && det.status == "N") {
                              det.rate=localRate
                          }
                          else
                          {
                              //  det.proV_PRICE="0"
                          }
                      }
                      else
                      {
                          // det.proV_PRICE="0"
                      }
                      if (localRate1 > 0 && det.proV_STATUS == "Y" && det.status == "N") {
                          Log.e("sss",det.brancH_CODE)
                          Log.e("sss",det.proV_PRICE)
                          ietmslist!!.set(i,det)
                      }
                  }
              }
              mMenuAdapter!!.notifyDataSetChanged()
          }
          else
          {
              for (i in ietmslist!!.indices) {
                  val det = ietmslist!![i]
                  if(det.proV_STATUS=="Y" && det.status == "N")
                  {
                      val localRate = det.proV_PRICE
                      val localRate1=det.proV_PRICE.toDouble()
                      if (localRate.length > 0) {
                          if (localRate1 > 0 && det.proV_STATUS == "Y" && det.status == "N") {
                              det.rate="0"
                          }
                          else
                          {
                             // det.proV_PRICE="0"
                          }
                      }
                      else
                      {
                          //det.proV_PRICE="0"
                      }
                      if (localRate1 > 0 && det.proV_STATUS == "Y" && det.status == "N") {
                          Log.e("sss",det.brancH_CODE)
                          Log.e("sss",det.proV_PRICE)
                          ietmslist!!.set(i,det)
                      }
                  }
              }
              mMenuAdapter!!.notifyDataSetChanged()
          }
            AppDialogs.hideProgressDialog()
        })

        if (mEditable == true) {
            saveButton!!.setOnClickListener(object : View.OnClickListener {



                private var flag = false
                override fun onClick(v: View) {




                    val ietmslist1 = ArrayList<lbpricedet>()
                    ietmslist1.clear()
                    for (i in ietmslist!!.indices) {
                        val det = ietmslist!![i]
                        val dataRate = det.rate

                        if (dataRate.length > 0) {
                            val rateValue = det.rate.toDouble()
                            var cobbRate = ""
                            var cobbWt = ""
                            if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
                                cobbRate = det.cobB_PRICE
                                cobbWt=det.cobB_WT
                            }
                            else
                            {
                                cobbRate = det.locaL_PRICE
                            }
                            val localRate = det.proV_PRICE
                            if (cobbRate.length > 0) {
                                var cobbRate1 =0.0;
                                    if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
                                    cobbRate1 = det.cobB_PRICE.toDouble()
                                }
                                else
                                {
                                    cobbRate1 = det.locaL_PRICE.toDouble()
                                }

                                if (cobbRate1 > 0 && det.status == "N") {

                                }
                                else
                                {
                                    det.cobB_PRICE="0"
                                }
                            }
                            else
                            {
                                det.cobB_PRICE="0"
                            }
                            if (cobbWt.length > 0) {
                                var cobbRate1 =0.0;
                                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
                                    cobbRate1 = det.cobB_WT.toDouble()
                                }


                                if (cobbRate1 > 0 && det.status == "N") {

                                }
                                else
                                {
                                    det.cobB_WT="0"
                                }
                            }
                            else
                            {
                                det.cobB_WT="0"
                            }
                            if (localRate.length > 0) {
                                val localRate1 = det.proV_PRICE.toDouble()
                                if (localRate1 > 0 && det.status == "N") {

                                }
                                else
                                {
                                    det.proV_PRICE="0"
                                }
                            }
                            else
                            {
                                det.proV_PRICE="0"
                            }
                            if (rateValue > 0 && det.status == "N") {
                                ietmslist1.add(det)
                            }
                            else
                            {
                                if(det.proV_STATUS=="N" && det.status == "N")
                                {

                                    val localRate = det.proV_PRICE
                                    var localRate1=0.0;
                                    if (localRate.length > 0) {
                                        localRate1=det.proV_PRICE.toDouble()
                                        if (localRate1 > 0 && det.proV_STATUS == "N") {

                                        }
                                        else
                                        {
                                            det.proV_PRICE="0"
                                        }
                                    }
                                    else
                                    {
                                        det.proV_PRICE="0"
                                    }
                                    if (localRate1 > 0 && det.proV_STATUS == "N") {
                                        Log.e("sss",det.brancH_CODE)
                                        Log.e("sss",det.proV_PRICE)
                                        ietmslist1.add(det)
                                    }
                                }
                            }
                        }
                        else
                        {
                            if(det.proV_STATUS=="N" && det.status == "N")
                            {

                                val localRate = det.proV_PRICE
                                var localRate1=0.0;
                                if (localRate.length > 0) {
                                    localRate1=det.proV_PRICE.toDouble()
                                    if (localRate1 > 0 && det.proV_STATUS == "N") {

                                    }
                                    else
                                    {
                                        det.proV_PRICE="0"
                                    }
                                }
                                else
                                {
                                    det.proV_PRICE="0"
                                }
                                if (localRate1 > 0 && det.proV_STATUS == "N") {
                                    Log.e("sss",det.brancH_CODE)
                                    Log.e("sss",det.proV_PRICE)
                                    ietmslist1.add(det)
                                }
                            }
                        }
                    }
                    if (ietmslist1.size > 0) {
                        var rate = ""
                        var rateValue = 0.0
                        var cobbrateValue = 0.0
                       var localrateValue = 0.0
                        var cobbrateWt=0.0
                        for (a in ietmslist1.indices) {
                             rateValue = 0.0
                             cobbrateValue = 0.0
                             localrateValue = 0.0
                            cobbrateWt = 0.0
                            rate =ietmslist1[a]
                                .rate
                            if(rate.length>0){
                            rateValue = ietmslist1[a]
                                    .rate.toDouble()}
                            if ((rateValue >= price_min) && (rateValue <= price_max)) {

                                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
                                    cobbrateValue = ietmslist1[a]
                                        .cobB_PRICE.toDouble()
                                    localrateValue = ietmslist1[a]
                                        .proV_PRICE.toDouble()
                                    cobbrateWt= ietmslist1[a]
                                        .cobB_WT.toDouble()
                                    if((localrateValue > 1) && (ietmslist1[a].proV_STATUS.equals("N"))) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Kindly declare either provisional price or final price for the branch <b>"+ietmslist1[a].brancH_NAME+"</b>"))
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
                                     else if((cobbrateValue == 0.0) ) /*|| (localrateValue<= 0.0)*/
                                     {
                                         flag = false
                                         AlertDialog.Builder(myContext)
                                             .setTitle("Warning")
                                             .setMessage(
                                                 Html.fromHtml("Cobb price is mandatory while confirm the final price for the branch <b>"+ietmslist1[a].brancH_NAME+"</b>"))
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
                                    else if((cobbrateValue < price_min) || (cobbrateValue > price_max)) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Cobb Price/Cobb Associate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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
                                    else if ((cobbrateValue < price_min) || (cobbrateValue > price_max))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Cobb Price/Cobb Associate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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
                                    else if((cobbrateWt == 0.0) ) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Cobb weight is mandatory while confirm the final price for the branch <b>"+ietmslist1[a].brancH_NAME+"</b>"))
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

                                    else if((cobbrateWt < wt_min) || (cobbrateWt > wt_max) && !ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION")) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Cobb weight in between the range <b>"+wt_min+" to "+wt_max+"</b>"))
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
                                    else if ((cobbrateWt < wt_min) || (cobbrateWt > wt_max) && !ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION"))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Cobb weight in between the range <b>"+wt_min+" to "+wt_max+"</b>"))
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

                                    else if(validate_satellite_price(ietmslist1[a]))
                                    {

                                        flag = false
                                        break
                                    }

                                    if(rateValue.toDouble() % 100.0 == 0.0)
                                    {

                                    }
                                    else
                                    {
                                        var rateValue_checkvalue=0.0f;
                                        var cobbrateValue_checkvalue=0.0f;
                                        var df = DecimalFormat("###")
                                        df.roundingMode = RoundingMode.FLOOR
                                        rateValue_checkvalue = rateValue.toFloat()-df.format(rateValue).toFloat()
                                        cobbrateValue_checkvalue = cobbrateValue.toFloat()-df.format(cobbrateValue).toFloat()
                                        df = DecimalFormat("###.##")
                                        df.roundingMode = RoundingMode.HALF_UP
                                        rateValue_checkvalue=df.format(rateValue_checkvalue).toFloat()
                                        cobbrateValue_checkvalue=df.format(cobbrateValue_checkvalue).toFloat()
                                        Log.e("rateValue_checkvalue", rateValue_checkvalue.toString())
                                        Log.e("predecimal_checkvalue", (rateValue_checkvalue.toDouble() % declare_value).toString())
                                        Log.e("cobbrateValue_checkval", cobbrateValue_checkvalue.toString())
                                        Log.e("cobbrateValue_checkval", (cobbrateValue_checkvalue.toDouble() % declare_value).toString())
                                        if(rateValue_checkvalue.toDouble() % declare_value != 0.0)
                                        {
                                            flag = false
                                            AlertDialog.Builder(myContext)
                                                .setTitle("Warning")
                                                .setMessage(
                                                    Html.fromHtml( "Final Price declaration will be allowed multiplication of <b>"+declare_value+" paise."))
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
                                        else if(cobbrateValue_checkvalue.toDouble() % declare_value != 0.0)
                                        {
                                            flag = false
                                            AlertDialog.Builder(myContext)
                                                .setTitle("Warning")
                                                .setMessage(
                                                    Html.fromHtml( "Cobb Price declaration will be allowed multiplication of <b>"+declare_value+" paise."))
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
                                else if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2022", ignoreCase = true)) {
                                    cobbrateValue = ietmslist1[a]
                                        .locaL_PRICE.toDouble()
                                    if ( (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                                            "2022",
                                            ignoreCase = true
                                        )) && ((rateValue > ietmslist1[a].bD_GOVT_RATE.toDouble()))) //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Punched Final Price is more than Govt. Cap Price, Kindly re-check and punch the final price is equal or < than Govt. Cap Price<b>"+ietmslist1[a].bD_GOVT_RATE+"</b>"))
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
                                    else if((cobbrateValue == 0.0) ) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Market price is mandatory while confirm the final price for the branch <b>"+ietmslist1[a].brancH_NAME+"</b>"))
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
                                    else if((cobbrateValue < price_min) || (cobbrateValue > price_max)) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Market Price/Cobb Associate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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
                                    else if ((cobbrateValue < price_min) || (cobbrateValue > price_max))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Please Enter the Market Price/Cobb Associate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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

                                flag = true

                            } else {
                                if(ietmslist1[a].proV_STATUS.equals("N"))
                                {
                                    localrateValue = ietmslist1[a]
                                        .proV_PRICE.toDouble()
                                if(ietmslist1[a].entrY_PROV_STATUS.equals("0"))  {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Provisional price list not available for <b>"+ietmslist1[a].brancH_NAME+"</b>"))
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
                                else if(ietmslist1[a].previouS_PRICE.equals("0"))  {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            "Previous day declared price or Reg. Avg. Selling price is not available")
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
                                else
                                {
                                    var prevrateValue  = ietmslist1[a]
                                        .previouS_PRICE.toDouble()
                                    var localratepermin = prevrateValue - (10 * prevrateValue) / 100.0
                                    var localratepermax = prevrateValue + (10 * prevrateValue) / 100.0
                                    Log.e("prevrateValue", prevrateValue.toString())
                                    Log.e("localratepermin", localratepermin.toString())
                                    Log.e("localratepermax", localratepermax.toString())
                                    val df = DecimalFormat("###")
                                    df.roundingMode = RoundingMode.HALF_UP
                                    localratepermin = df.format(localratepermin).toDouble()
                                    localratepermax = df.format(localratepermax).toDouble()
                                    Log.e("localratepermin1", localratepermin.toString())
                                    Log.e("localratepermax1", localratepermax.toString())


                                    if((localrateValue < localratepermin) || (localrateValue > localratepermax)) /*|| (localrateValue<= 0.0)*/
                                {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Provisional price should be with in the range of plus or minus 10% from the previous day branch <b>"+ietmslist1[a].brancH_NAME+"</b> declared rate or Reg. Avg. Selling price"))
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
                                else if ((localrateValue < localratepermin) || (localrateValue > localratepermax))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Provisional price should be with in the range of plus or minus 10% from the previous day branch <b>"+ietmslist1[a].brancH_NAME+"</b> declared rate or Reg. Avg. Selling price"))
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
                                    if((localrateValue < price_min) || (localrateValue > price_max)) /*|| (localrateValue<= 0.0)*/
                                {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Please Enter the Provisional rate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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
                                else if ((localrateValue < price_min) || (localrateValue > price_max))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                {
                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Please Enter the Provisional rate in between the range <b>"+price_min+" to "+price_max+"</b>"))
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

                                else
                                {
                                    var localrateValue_checkvalue=0.0f;

                                    var df = DecimalFormat("###")
                                    df.roundingMode = RoundingMode.FLOOR
                                    localrateValue_checkvalue = rateValue.toFloat()-df.format(localrateValue).toFloat()

                                    df = DecimalFormat("###.##")
                                    df.roundingMode = RoundingMode.HALF_UP
                                    localrateValue_checkvalue=df.format(localrateValue_checkvalue).toFloat()

                                    Log.e("localrateValue_check", localrateValue_checkvalue.toString())
                                    Log.e("localrateValue_check", (localrateValue_checkvalue.toDouble() % declare_value).toString())

                                    if(localrateValue_checkvalue.toDouble() % declare_value != 0.0)
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml( "Provisional Price declaration will be allowed multiplication of <b>"+declare_value+" paise."))
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
                                    flag = true
                                }
                                }
                                }
                                else {

                                    flag = false
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        /*.setMessage(
                                                "Please Enter the Price below Rs 500.")*/
                                        .setMessage(
                                            Html.fromHtml("Please Enter the Price in between the range <b>" + price_min + " to " + price_max+"</b>")
                                        )
                                        .setPositiveButton(
                                            android.R.string.yes
                                        ) { dialog, which ->
                                            // continue with delete
                                        }
                                        .setIcon(
                                            android.R.drawable.ic_dialog_alert
                                        )
                                        .show()
                                    break
                                }
                            }
                        }
                        if (flag) {
                            var message_final="";
                            if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                                    "2025",
                                    ignoreCase = true
                                )
                            ) {
                                message_final="Are you sure confirming the price. <br><br> (<b>This provisional price will be used to credit check customer orders</b>)";
                            }
                            else
                            {
                                message_final="Are you sure confirming the price. <br>";
                            }
                        AlertDialog.Builder(myContext)
                            .setTitle("Price Declaration")

                            .setMessage(
                                Html.fromHtml(message_final))
                            .setPositiveButton(
                                "Yes"
                            ) { dialog, which ->

                            val lb_date = edtdate!!.text.toString()
                            val itemcode = mlbitem!!.result[0]
                                    .iteM_CODE
                            val uom = mlbitem!!.result[0].uom

                            // DataAccessManager.savelbprice("12-DEC-2015",
                            // "BRCULL", "KG", ietmslist1);
                            val localnetworkutil = NetworkUtil(myContext)
                            if (localnetworkutil.isOnline) {
                            AppDialogs.showProgressDialog(myContext)
                                mSaveTask = DataSaveTask(lb_date, itemcode,
                                        uom, ietmslist1)
                                mSaveTask!!.execute(null as Void?)
                            } else {
                                Toast.makeText(myContext, "No Internet Connection.",
                                        Toast.LENGTH_LONG).show()
                            }
                            }
                            .setNegativeButton("No"){ dialog, which ->

                            }
                            .setIcon(
                                android.R.drawable.ic_dialog_alert)
                            .show()
                        }
                        // else {
                        // // Toast.makeText(myContext,
                        // // "Please Enter the Price below 200Rs.",
                        // // Toast.LENGTH_LONG).show();
                        //
                        // }
                    } else {
                        Toast.makeText(myContext, "Please Enter the Price",
                                Toast.LENGTH_LONG).show()
                    }


                }
            })


        }
    }

    private fun validate_satellite_price(ietmslist1: lbpricedet): Boolean {

                for (i in ietmslist!!.indices) {
                    var branchcode =""
                    var zonename=""
                    var branchcode1 =""
                    var zonename1=""
                    val det = ietmslist!![i]

                    zonename=ietmslist1.zonE_NAME
                    branchcode=ietmslist1.brancH_CODE
                    zonename1= det.zonE_NAME
                    branchcode1=det.brancH_CODE

                    if(zonename == "SATELLITE")
                    {
                        branchcode=branchcode.take(3)

                        if(zonename1 != "SATELLITE")
                        {
                            if(branchcode == branchcode1)
                            {
                                if(det.rate == "" || det.rate.toDouble() <= 0)
                                {
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            "Please Enter the price for home branch "+det.brancH_NAME)
                                        .setPositiveButton(
                                            android.R.string.yes
                                        ) { dialog, which ->
                                            // continue with delete
                                        }
                                        .setIcon(
                                            android.R.drawable.ic_dialog_alert)
                                        .show()
                                    return true;
                                }
                            }
                        }
                        /*else
                        {
                            branchcode1=branchcode1.take(3)
                            if(branchcode == branchcode1)
                            {
                                if(det.rate == "" || det.rate.toDouble() <= 0)
                                {
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            "Please Enter the price for satellite branch "+det.brancH_NAME)
                                        .setPositiveButton(
                                            android.R.string.yes
                                        ) { dialog, which ->
                                            // continue with delete
                                        }
                                        .setIcon(
                                            android.R.drawable.ic_dialog_alert)
                                        .show()
                                    return true;
                                }
                            }
                        }*/
                    }
                    else
                    {

                        if(zonename1 == "SATELLITE")
                        {
                            branchcode1=branchcode1.take(3)


                            if(branchcode == branchcode1)
                            {

                                if(det.rate == "" || det.rate.toDouble() <= 0)
                                {
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            "Please Enter the price for satellite branch "+det.brancH_NAME)
                                        .setPositiveButton(
                                            android.R.string.yes
                                        ) { dialog, which ->
                                            // continue with delete
                                        }
                                        .setIcon(
                                            android.R.drawable.ic_dialog_alert)
                                        .show()
                                    return true;
                                }
                            }

                        }
                        /*else
                        {

                            if(branchcode == branchcode1)
                            {
                                if(det.rate == "" || det.rate.toDouble() <= 0)
                                {
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            "Please Enter the price for branch "+det.brancH_NAME)
                                        .setPositiveButton(
                                            android.R.string.yes
                                        ) { dialog, which ->
                                            // continue with delete
                                        }
                                        .setIcon(
                                            android.R.drawable.ic_dialog_alert)
                                        .show()

                                    return true;
                                }
                            }
                        }*/
                    }

        }
      return false;
    }

    inner class DataFetchTask internal constructor(var mlb_date: String) : AsyncTask<Void?, Void?, Boolean>() {
        protected override fun doInBackground(vararg params: Void?): Boolean {
            mresponse = DataAccessManager.getlbdet(mlb_date, "BASE", myContext)
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null
             AppDialogs.hideProgressDialog()
            responsecode = if (mresponse != null) {
                mresponse!!.statusCode.toInt()
            } else {
                -1
            }
            if (success) {
                if (responsecode >= 200) {
                    ietmslist!!.clear()
                    ietmslist!!.addAll(mresponse!!.result)
                    mMenuAdapter!!.notifyDataSetChanged()
                } else {
                }
            }
        }

        override fun onCancelled() {
            mAuthTask = null
             AppDialogs.hideProgressDialog()
        }

    }

/*    private fun showProgress(show: Boolean) {
        if (show == true) {
            mProg = ProgressDialog(myContext)
            mProg!!.setMessage("Loading...")
            mProg!!.setCancelable(false)
            mProg!!.show()
        } else {
            mProg!!.hide()
        }
    }*/

    inner class DataSaveTask internal constructor(var mtrx_date: String, var mitemcode: String, var muom: String,
                                                  var mitem: ArrayList<lbpricedet>) : AsyncTask<Void?, Void?, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mresponse = DataAccessManager.savelbprice(mtrx_date, mitemcode,
                    muom, mitem, mpricetype, "", myContext)
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null
             AppDialogs.hideProgressDialog()
            if (mresponse != null) {
                responsecode = mresponse!!.statusCode.toInt()
                msg = mresponse!!.message.toString()
            } else {
                responsecode = -1
            }
            if (success) {
                if (responsecode == 200) {
                    // ietmslist.clear();
                    // ietmslist.addAll(mresponse.getResult());
                    mMenuAdapter!!.notifyDataSetChanged()
                    Toast.makeText(myContext, "Declared Price updated successfully.",
                            Toast.LENGTH_LONG).show()
                    val current_date = edtdate!!.text.toString()
                    edtdate!!.setText(current_date)
                } else if (responsecode == -4) {
                    Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(myContext, "Price Details has not updated.",
                            Toast.LENGTH_LONG).show()
                }
            }
        }

        override fun onCancelled() {
            mAuthTask = null
             AppDialogs.hideProgressDialog()
        }


    }

    inner class DataGetItemTask internal constructor() : AsyncTask<Void?, Void?, Boolean>() {
        protected override fun doInBackground(vararg params: Void?): Boolean {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mlbitem = DataAccessManager.getlbitem("PRICE", myContext)
            responsecode = if (mlbitem != null) {
                mlbitem!!.statusCode.toInt()
            } else {
                -1
            }
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mGetItemTask = null
             AppDialogs.hideProgressDialog()
            if (success) {
                if (responsecode >= 200) {
                    edtdate!!.setText("")
                    edtitem!!.setText("")
                    edtdate!!.setText(mlbitem!!.result[0]
                            .currenT_DATE)
                    edtitem!!.setText(mlbitem!!.result[0].iteM_NAME)
                    if(!mlbitem!!.result[0]
                            .target.equals("T"))
                    {
                        /*Toast.makeText(myContext, "Declare All Target Clusters", Toast.LENGTH_SHORT)
                            .show()
                        finish()*/

                            if (mEditable == true) {
                                showSimplePopup(myContext)
                                //  mEditable = bundle.getBoolean(getString(R.string.is_editable))
                            }


                    }
                } else {
                    Toast.makeText(myContext, "Error", Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
        override fun onCancelled() {
            mAuthTask = null
             AppDialogs.hideProgressDialog()
        }
    }
    fun showSimplePopup(myContext: BaseActivity) {
        val builder = AlertDialog.Builder(myContext)
        builder.setTitle("Declare Target")
        builder.setMessage("Please Declare the ‘Today Target’ and proceed for price mapping.")
        builder.setCancelable(false)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    inner class DataGetPricevalue internal constructor() : AsyncTask<Void?, Void?, Boolean>() {
        protected override fun doInBackground(vararg params: Void?): Boolean {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mlbpricemaxitem = DataAccessManager.getlbpricemax("PRICE", myContext)
            responsecode = if (mlbpricemaxitem != null) {
                mlbpricemaxitem!!.statusCode.toInt()
            } else {
                -1
            }
            Log.e("e",mlbpricemaxitem!!.statusCode.toString());
            return true
        }


        override fun onPostExecute(success: Boolean) {
                        /*AlertDialog.Builder(myContext)
                            .setTitle("Warning")
                            *//*.setMessage(
                                    "Please Enter the Price below Rs 500.")*//*
                            .setMessage(
                                "Price list option is not available")
                            .setPositiveButton(
                                android.R.string.yes
                            ) { dialog, which ->
                                // continue with delete
                                finish()
                            }
                            .setIcon(
                                android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show()
                        price_min=1.0f
                        price_max=200.00f
                    }*/

            if (success) {
                if (responsecode >= 200) {
                    /*edtdate!!.setText("")
                    edtitem!!.setText("")
                    edtdate!!.setText(mlbitem!!.result[0]
                        .currenT_DATE)
                    edtitem!!.setText(mlbitem!!.result[0].iteM_NAME)*/
                    if(mlbpricemaxitem!!.result.size>0) {
                        try{
                            price_min=mlbpricemaxitem!!.result[0]
                                .minimum.toFloat()
                            price_max=mlbpricemaxitem!!.result[0]
                                .maximum.toFloat()
                            declare_value = mlbpricemaxitem!!.result[0].decimaL_VALUE
                                .toFloat()
                            Log.e(
                                "region", mlbpricemaxitem!!.result[0]
                                    .regioN_CODE
                            )
                            Log.e(
                                "minimum", mlbpricemaxitem!!.result[0]
                                    .minimum
                            )
                            Log.e(
                                "maximum", mlbpricemaxitem!!.result[0]
                                    .maximum
                            )
                            Log.e(
                                "declare_value", mlbpricemaxitem!!.result[0]
                                    .decimaL_VALUE
                            )
                        }
                        catch (e:Exception)
                        {
                            AlertDialog.Builder(myContext)
                                .setTitle("Warning")
                                /*.setMessage(
                                        "Please Enter the Price below Rs 500.")*/
                                .setMessage(
                                    "Price list option is not available")
                                .setPositiveButton(
                                    android.R.string.yes
                                ) { dialog, which ->
                                    // continue with delete
                                    finish()
                                }
                                .setIcon(
                                    android.R.drawable.ic_dialog_alert)
                                .setCancelable(false)
                                .show()
                            price_min=1.0f
                            price_max=200.00f
                            declare_value = 0.1f

                        }

                    }
                    else
                    {
                        AlertDialog.Builder(myContext)
                            .setTitle("Warning")
                            /*.setMessage(
                                    "Please Enter the Price below Rs 500.")*/
                            .setMessage(
                                "Price list option is not available")
                            .setPositiveButton(
                                android.R.string.yes
                            ) { dialog, which ->
                                // continue with delete
                                finish()
                            }
                            .setIcon(
                                android.R.drawable.ic_dialog_alert)
                            .setCancelable(false)
                            .show()
                        price_min=1.0f
                        price_max=200.00f
                        declare_value = 0.1f
                    }
                } else {
                    Toast.makeText(myContext, "Error", Toast.LENGTH_SHORT)
                        .show()
                }
            }
                /*} else {
                    Toast.makeText(myContext, "Error", Toast.LENGTH_SHORT)
                        .show()
                }*/
            //}
        //}
        }
        override fun onCancelled() {
            mAuthTask = null

        }
    }
}