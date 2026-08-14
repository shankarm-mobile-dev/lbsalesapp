package com.suguna.lbsalesperson.priceallocation.activity

import android.app.AlertDialog
import android.content.Context
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
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.priceallocation.DataAccessManager
import com.suguna.lbsalesperson.priceallocation.PriceDeclarationListener
import com.suguna.lbsalesperson.priceallocation.SpanningLinearLayoutManager
import com.suguna.lbsalesperson.priceallocation.adapter.PriceRecyclerAdapter_new
import com.suguna.lbsalesperson.priceallocation.adapter.RecyclerViewAdapter_Pricelistname
import com.suguna.lbsalesperson.priceallocation.model.*
import com.truelife.http.ResponseListener
import java.math.RoundingMode
import java.text.DecimalFormat

class DiffPriceAllocationActivity : BaseActivity(), PriceDeclarationListener {


    //var mProg: ProgressDialog? = null
    var mresponse: lbpricedetlist? = null
    var responsecode = 0
    var msg = ""
    private var mAuthTask: DataFetchTask? = null
    private var mSaveTask: DataSaveTask? = null
    private var mGetItemTask: DataGetItemTask? = null
    var Selecteditemlist: List<HashMap<String, String>> = ArrayList()
    var ietmslist: ArrayList<lbpricedet>? = null
    // var itemsadapter: PriceAdapter? = null
    var ltv_itemslist: ListView? = null
    var edtdate: EditText? = null
    var saveButton: LinearLayout? = null
    var mlbitem: lbitemlist? = null
    var mdiffprice: difflbprice? = difflbprice()
    var edtitem: EditText? = null
    var mEditable: Boolean? = null
    var mShowpopup: Boolean? = true
    val mpricetype = "BASE"
    private var mDatePicker: DatePickerWidget? = null
    var tvservicedate1: TextView? = null
    var tvservicedate2: TextView? = null
    var lyt_dyn: LinearLayout?=null
    var mMenuRecycler: RecyclerView? = null
    var mMenuAdapter: PriceRecyclerAdapter_new? = null
    var difflblist: ArrayList<difflblist>? = null
    var diffsize=0
    var price_min:Float = 0.0f
    var price_max:Float = 200.0f
    var declare_value:Float = 0.1f
    var wt_min:Float = 0.0f
    var wt_max:Float = 3.0f
    private var mGetpricemaxTask: DataGetPricevalue? = null
    var mlbpricemaxitem: lbpricemaxlist? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diff_price_allocation)
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
        mMenuRecycler = findViewById(R.id.recycleview)
        edtdate!!.isFocusable = false
        edtdate!!.isFocusableInTouchMode = false
        edtdate!!.isClickable = false
        edtitem!!.isFocusable = false
        edtitem!!.isFocusableInTouchMode = false
        edtitem!!.isClickable = false
        if (mEditable == false) {
            saveButton!!.visibility = View.INVISIBLE
        } else {
            saveButton!!.visibility = View.VISIBLE
        }
        // itemsadapter = PriceAdapter(myContext, R.layout.lbprice_list_item,
        //        ietmslist!!, mEditable!!)
        // ltv_itemslist!!.adapter = itemsadapter


        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(myContext))
        mMenuRecycler!!.setNestedScrollingEnabled(true)


        // TODO Auto-generated method stub

        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
            tvservicedate1!!.visibility = View.VISIBLE
            tvservicedate2!!.visibility = View.VISIBLE
        }
        else
        {
            tvservicedate1!!.visibility = View.GONE
            tvservicedate2!!.visibility = View.GONE
        }

        if (Utility.isInternetAvailable(myContext)) {
            //AppDialogs.showProgressDialog(myContext)
            mGetItemTask = DataGetItemTask()
            mGetItemTask!!.execute(null as Void?)
            DataPricecount().execute(null as Void?)
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

                    //showProgress(true)
                    AppDialogs.showProgressDialog(myContext)
                    mAuthTask = DataFetchTask(text)
                    mAuthTask!!.execute(null as Void?)
                }
            }
        })
        if (mEditable == true) {
            saveButton!!.setOnClickListener(object : View.OnClickListener {
                private var flag = false
                private var flag_check = false
                override fun onClick(v: View) {
                    val ietmslist1 = ArrayList<lbpricedet>()
                    ietmslist1.clear()
                    first@ for (a in ietmslist!!.indices) {
                        val det = ietmslist!![a].diffprice
                        var check_count=0
                        var check_provcount=0
                        var count=det.size
                        if(ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION"))
                        {
                            count=count-1
                            Log.e("ss202","ss202")
                        }
                        Log.e("ss200","ss200")
                        second@ for (i in det.indices) {
                            val dataRate = det[i].rate
                            if (dataRate.length > 0) {
                                val rateValue = det[i].rate.toDouble()
                                if (rateValue > 0 && det[i].status.equals("N")) {
                                  //  ietmslist1.addAll(det)
                                    check_count=check_count+1
                                    Log.e("ss207","ss207")
                                }
                                else
                                {
                                    val dataRate = det[i].proV_PRICE
                                    if (dataRate.length > 0) {
                                    val rateValue = det[i].proV_PRICE.toDouble()
                                    if (rateValue > 0 && det[i].status.equals("N") && det[i].proV_STATUS.equals("N")) {
                                        //  ietmslist1.addAll(det)
                                        check_provcount=check_provcount+1
                                        Log.e("ss218","ss218")
                                    }
                                    }
                                }

                            }
                            else
                            {
                                val dataRate = det[i].proV_PRICE
                                if (dataRate.length > 0) {
                                    val rateValue = det[i].proV_PRICE.toDouble()
                                    if (rateValue > 0 && det[i].status.equals("N") && det[i].proV_STATUS.equals("N")) {
                                        //  ietmslist1.addAll(det)
                                        check_provcount=check_provcount+1
                                        Log.e("ss231","ss231")
                                    }
                                }
                            }
                            if((ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION") && (det[i].producT_CATEGORY.equals("COBB Wt.") || det[i].producT_CATEGORY.equals("COBB"))))
                            {
                                check_count=check_count+1
                                Log.e("ss238","ss238")
                            }
                        }
                        if(check_count==count)
                        {
                            ietmslist1.add(ietmslist!![a])
                            Log.e("ss239","ss239")
                        }
                        else if(check_count>=1)
                        {
                            /*Toast.makeText(myContext, "Please Enter All the Final Prices for the branch " +ietmslist!![a].brancH_NAME,
                                Toast.LENGTH_LONG).show()*/
                            var product_name="";
                            val det1 = ietmslist!![a].diffprice
                            third@ for (j in det1.indices) {
                                val dataRate = det1[j].rate
                                if (dataRate.length == 0) {
                                    Log.e("ss250","ss250")
                                        product_name = det1[j].producT_CATEGORY
                                        break@third


                                }
                                else{
                                    val rateValue = det[j].rate.toDouble()
                                    if (rateValue <= 0) {
                                        Log.e("ss259","ss259")
                                        product_name = det1[j].producT_CATEGORY
                                        break@third
                                    }
                                }
                            }
                            if(!product_name.equals("COBB Wt.") )
                            {
                                Log.e("ss267","ss267")
                                if(!(ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION") && (product_name.equals("COBB Wt.") || product_name.equals("COBB"))))
                            {
                                Log.e("ss270","ss270")
                            AlertDialog.Builder(myContext)
                                .setTitle("Warning")
                                .setMessage(
                                    Html.fromHtml("Please enter <b>"+product_name+"</b> birds price for the branch <b>" +ietmslist!![a].brancH_NAME +"</b> and proceed."))
                                .setPositiveButton(
                                    android.R.string.yes
                                ) { dialog, which ->
                                    // continue with delete
                                }
                                .setIcon(
                                    android.R.drawable.ic_dialog_alert)
                                .show()
                                ietmslist1.clear()
                                flag_check=true
                                break@first
                                }

                            }

                            else
                            {
                                Log.e("ss292","ss292")
                                if(!(ietmslist!![a].opM_DIVISION.equals("RETAIL DIVISION") && (product_name.equals("COBB Wt.")  || product_name.equals("COBB")))) {
                                    Log.e("ss294","ss294")
                                    AlertDialog.Builder(myContext)
                                        .setTitle("Warning")
                                        .setMessage(
                                            Html.fromHtml("Please enter <b>" + product_name + "</b> for the branch <b>" + ietmslist!![a].brancH_NAME + "</b> and proceed.")
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
                                    ietmslist1.clear()
                                    flag_check=true
                                    break@first
                                }

                            }

                        }
                        if(check_provcount==(count-2))
                        {
                            Log.e("ss319","ss319")
                            ietmslist1.add(ietmslist!![a])
                        }
                        else if(check_provcount>=1)
                        {
                           /* Toast.makeText(myContext, "Please Enter All the Provisional Prices for the branch "+ietmslist!![a].brancH_NAME,
                                Toast.LENGTH_LONG).show()*/
                            Log.e("ss326","ss326")
                            var product_name="";
                            val det1 = ietmslist!![a].diffprice
                            third@ for (j in det1.indices) {
                                val dataRate = det1[j].proV_PRICE
                                if (dataRate.length == 0) {
                                    product_name=det1[j].producT_CATEGORY
                                    break@third
                                }else{
                                    val rateValue = det[j].proV_PRICE.toDouble()
                                    if (rateValue <= 0) {
                                        product_name = det1[j].producT_CATEGORY
                                        break@third
                                    }
                                }
                            }
                            Log.e("ss342","ss342")
                            AlertDialog.Builder(myContext)
                                .setTitle("Warning")
                                .setMessage(
                                    Html.fromHtml("Please enter <b>"+product_name+"</b> provisional price for the branch <b>" +ietmslist!![a].brancH_NAME +"</b> and proceed."))
                                .setPositiveButton(
                                    android.R.string.yes
                                ) { dialog, which ->
                                    // continue with delete
                                }
                                .setIcon(
                                    android.R.drawable.ic_dialog_alert)
                                .show()
                            ietmslist1.clear()
                            flag_check=true
                            break@first
                        }
                        /*for (i in det.indices) {
                            val dataRate = det[i].rate

                            if (dataRate.length > 0) {
                                val rateValue = det[i].rate.toDouble()
                                if (rateValue > 0) {
                                    //  ietmslist1.addAll(det)

                                }
                            }
                        }*/
                    }
                    if (ietmslist1.size > 0) {
                        var rateValue = 0.0
                        var cobbrateValue = 0.0
                        var localrateValue = 0.0
                        var localrate =""
                        var rate =""
                        Log.e("ss377","ss377")
                        first@ for (a in ietmslist1.indices) {
                             rateValue = 0.0
                             cobbrateValue = 0.0
                             localrateValue = 0.0
                             localrate =""
                             rate =""
                            val det = ietmslist!![a].diffprice
                            second@  for (i in det.indices) {
                                rate=ietmslist1[a].diffprice[i].rate
                                if(rate.length>0) {
                                    rateValue = ietmslist1[a].diffprice[i]
                                        .rate.toDouble()
                                }
                                var check_Value=false;
                                if ((rateValue >= price_min) && (rateValue <= price_max)) {
                                    check_Value=true;

                                }
                                if ((rateValue >= price_min) && (rateValue <= price_max)) {
                                    check_Value=true;

                                }
                                else
                                {
                                    /*if(!((ietmslist1[a].opM_DIVISION.equals("RETAIL DIVISION") && (ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")  || ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB"))))) {
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Please Enter the <b>" + ietmslist1[a].diffprice[i].producT_CATEGORY + "</b> Price in between the range1 <b>" + price_min + " to " + price_max + "</b>")
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
                                        flag = false;
                                        break@first
                                    }*/
                                }
                                if((ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))
                                {
                                    if ((rateValue >= wt_min) && (rateValue <= wt_max)) {
                                        check_Value=true;
                                    }
                                    else{
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Please Enter the <b>"+ietmslist1[a].diffprice[i].producT_CATEGORY+"</b> Price in between the range <b>"+wt_min+" to "+wt_max+"</b>")
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
                                        flag=false;
                                        break@first
                                    }
                                }
                                if (check_Value) {
                                    localrate=ietmslist1[a].diffprice[i].proV_PRICE
                                    if(localrate.length>0) {
                                        localrateValue = ietmslist1[a].diffprice[i]
                                            .proV_PRICE.toDouble()
                                    }
                                    if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                                            "2025",
                                            ignoreCase = true
                                        )
                                    ) {


                                    }
                                    if((localrateValue > 1) && (ietmslist1[a].diffprice[i].proV_STATUS.equals("N"))) /*|| (localrateValue<= 0.0)*/
                                    {
                                        flag = false
                                        AlertDialog.Builder(myContext)
                                            .setTitle("Warning")
                                            .setMessage(
                                                Html.fromHtml("Kindly declare either provisional price or final price for the branch <b>"+ietmslist1[a].brancH_NAME + "</b>"))
                                            .setPositiveButton(
                                                android.R.string.yes
                                            ) { dialog, which ->
                                                // continue with delete
                                            }
                                            .setIcon(
                                                android.R.drawable.ic_dialog_alert)
                                            .show()
                                        break@first
                                    }
                                    else if(validate_satellite_price(ietmslist1[a])  )
                                    {
                                        flag = false

                                        break@first
                                    }
                                    else {
                                        var rateValue_checkvalue=0.0f;

                                        var df = DecimalFormat("###")
                                        df.roundingMode = RoundingMode.FLOOR
                                        rateValue_checkvalue = rateValue.toFloat()-df.format(rateValue).toFloat()

                                        df = DecimalFormat("###.##")
                                        df.roundingMode = RoundingMode.HALF_UP
                                        rateValue_checkvalue=df.format(rateValue_checkvalue).toFloat()

                                        Log.e("rateValue_checkvalue", rateValue_checkvalue.toString())
                                        Log.e("predecimal_checkvalue", (rateValue_checkvalue.toDouble() % declare_value).toString())

                                        if(rateValue_checkvalue.toDouble() % declare_value != 0.0 && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))
                                        {
                                            flag = false
                                            AlertDialog.Builder(myContext)
                                                .setTitle("Warning")
                                                .setMessage(
                                                    Html.fromHtml( "Price declaration will be allowed multiplication of <b>"+declare_value+" paise."))
                                                .setPositiveButton(
                                                    android.R.string.yes
                                                ) { dialog, which ->
                                                    // continue with delete
                                                }
                                                .setIcon(
                                                    android.R.drawable.ic_dialog_alert)
                                                .show()
                                            break@first
                                        }

                                        flag = true
                                    }

                                } else {
                                    if(ietmslist1[a].diffprice[i].proV_STATUS.equals("N"))
                                    {
                                        localrateValue = ietmslist1[a].diffprice[i]
                                            .proV_PRICE.toDouble()
                                        if(ietmslist1[a].diffprice[i].entrY_PROV_STATUS.equals("0") && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))  {
                                            flag = false
                                            AlertDialog.Builder(myContext)
                                                .setTitle("Warning")
                                                .setMessage(
                                                    Html.fromHtml( "Provisional price list not available for the product <b>"+ietmslist1[a].diffprice[i].producT_CATEGORY+ "</b> in the branch <b>" +ietmslist1[a].brancH_NAME+"</b>"))
                                                .setPositiveButton(
                                                    android.R.string.yes
                                                ) { dialog, which ->
                                                    // continue with delete
                                                }
                                                .setIcon(
                                                    android.R.drawable.ic_dialog_alert)
                                                .show()
                                            break@first

                                        }
                                        else if(ietmslist1[a].diffprice[i].previouS_PRICE.equals("0") && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))  {
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
                                            break@first

                                        }
                                        else {
                                            var prevrateValue  = ietmslist1[a].diffprice[i]
                                                .previouS_PRICE.toDouble()
                                            var localratepermin = prevrateValue - (10 * prevrateValue) / 100.0
                                            var localratepermax = prevrateValue + (10 * prevrateValue) / 100.0
                                            Log.e("sss",det[i].producT_CATEGORY)
                                            Log.e("previouS_PRICE",ietmslist1[a].diffprice[i].previouS_PRICE)
                                            Log.e("localratepermin", localratepermin.toString())
                                            Log.e("localratepermax", localratepermax.toString())
                                            val df = DecimalFormat("###")
                                            df.roundingMode = RoundingMode.HALF_UP
                                             localratepermin = df.format(localratepermin).toDouble()
                                             localratepermax = df.format(localratepermax).toDouble()
                                            Log.e("localratepermin1", localratepermin.toString())
                                            Log.e("localratepermax1", localratepermax.toString())
                                            if(((localrateValue < localratepermin) || (localrateValue > localratepermax)) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt."))) /*|| (localrateValue<= 0.0)*/
                                            {
                                                flag = false
                                                AlertDialog.Builder(myContext)
                                                    .setTitle("Warning")
                                                    .setMessage(
                                                        Html.fromHtml("Provisional price should be with in the range of +/- 10% from the previous day branch <b>"+ietmslist1[a].brancH_NAME+"</b> declared rate or Reg. Avg. Selling price for the product <b>"+ietmslist1[a].diffprice[i].producT_CATEGORY+ "</b>"))
                                                    .setPositiveButton(
                                                        android.R.string.yes
                                                    ) { dialog, which ->
                                                        // continue with delete
                                                    }
                                                    .setIcon(
                                                        android.R.drawable.ic_dialog_alert)
                                                    .show()
                                                break@first
                                            }
                                            else if (((localrateValue < localratepermin) || (localrateValue > localratepermax)) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                            {
                                                flag = false
                                                AlertDialog.Builder(myContext)
                                                    .setTitle("Warning")
                                                    .setMessage(
                                                        Html.fromHtml("Provisional price should be with in the range of +/- 10% from the previous day branch <b>"+ietmslist1[a].brancH_NAME+"</b> declared rate or Reg. Avg. Selling price for the product <b>"+ietmslist1[a].diffprice[i].producT_CATEGORY+ "</b>"))
                                                    .setPositiveButton(
                                                        android.R.string.yes
                                                    ) { dialog, which ->
                                                        // continue with delete
                                                    }
                                                    .setIcon(
                                                        android.R.drawable.ic_dialog_alert)
                                                    .show()
                                                break@first
                                            }
                                            else if(((localrateValue < price_min) || (localrateValue > price_max)) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt."))) /*|| (localrateValue<= 0.0)*/
                                            {
                                                Log.e("ss1",ietmslist1[a].diffprice[i].producT_CATEGORY)
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
                                                break@first
                                            }
                                            else if (((localrateValue < price_min) || (localrateValue > price_max)) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")) && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))  //((cobbrateValue > 500.0)/* || (localrateValue > 500.0)*/)
                                            {
                                                Log.e("ss2",ietmslist1[a].diffprice[i].producT_CATEGORY)
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
                                                break@first
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

                                                if(localrateValue_checkvalue.toDouble() % declare_value != 0.0 && (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))
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
                                                    break@first
                                                }
                                                flag = true
                                            }
                                        }
                                    }
                                    else {
                                    this.flag = false
                                        if( (!ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")))
                                        {
                                            if(!(ietmslist1[a].opM_DIVISION.equals("RETAIL DIVISION") && (ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB Wt.")  || ietmslist1[a].diffprice[i].producT_CATEGORY.equals("COBB")))) {


                                                AlertDialog.Builder(myContext)
                                                    .setTitle("Warning")
                                                    .setMessage(
                                                        Html.fromHtml("Please Enter the <b>" + ietmslist1[a].diffprice[i].producT_CATEGORY + "</b> Price in between the range <b>" + price_min + " to " + price_max + "</b>")
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
                                                break@first
                                            }
                                        }
                                        else
                                        {
                                            if ((rateValue >= wt_min) && (rateValue <= wt_max)) {
                                                AlertDialog.Builder(myContext)
                                                    .setTitle("Warning")
                                                    .setMessage(
                                                        Html.fromHtml("Please Enter the <b>" + ietmslist1[a].diffprice[i].producT_CATEGORY + "</b> Weight in between the range <b>" + wt_min + " to " + wt_max + "</b>")
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
                                                break@first
                                            }
                                        }

                                }
                                }
                            }
                        }
                        if (flag) {

                            AlertDialog.Builder(myContext)
                                .setTitle("Price Declaration")

                                .setMessage(
                                    Html.fromHtml("Are you sure confirming the price. <br><br> (<b>This provisional price will be used to credit check customer orders</b>)"))
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
                                /*Toast.makeText(myContext, "Save.",
                                    Toast.LENGTH_LONG).show()*/
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
                        if(!flag_check){
                        Toast.makeText(myContext, "Please Enter All the Prices",
                            Toast.LENGTH_LONG).show()
                            flag_check=false
                        }
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
                        val det1 = det.diffprice
                        var dataRate="";
                        for (i in det1.indices) {
                            dataRate = det1[i].rate
                        }
                        if(dataRate == "" || dataRate.toDouble() <= 0)
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
                        val det1 = det.diffprice
                        var dataRate="";
                        for (i in det1.indices) {
                            val dataRate = det1[i].rate
                        }
                        if(dataRate == "" || dataRate.toDouble() <= 0)
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

                        val det1 = det.diffprice
                        val det2 = ietmslist1.diffprice
                        var dataRate="";
                        var provdataRate="";
                        for (i in det1.indices) {
                            dataRate = det1[i].rate
                            provdataRate=det2[i].proV_PRICE
                            if(provdataRate !="" && provdataRate.toDouble() > 0)
                            {
                                return false;
                            }
                        }
                        if((dataRate == "" || dataRate.toDouble() <= 0)  && provdataRate !="")
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
                        val det1 = det.diffprice
                        var dataRate="";
                        for (i in det1.indices) {
                            val dataRate = det1[i].rate
                        }
                        if(dataRate == "" || dataRate.toDouble() <= 0)
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
            mresponse = DataAccessManager.getlbdet_new(mlb_date, "BASE", myContext)
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null

            responsecode = if (mresponse != null) {
                mresponse!!.statusCode.toInt()
            } else {
                -1
            }
            if (success) {
                if (responsecode >= 200) {
                    ietmslist!!.clear()
                    ietmslist!!.addAll(mresponse!!.result)
                  //  mMenuAdapter!!.notifyDataSetChanged()
                    mMenuAdapter = PriceRecyclerAdapter_new(myContext, R.layout.lbprice_list_item,
                        ietmslist!!, mEditable!!, mdiffprice!!.result!!,this@DiffPriceAllocationActivity
                    )
                    mMenuRecycler!!.adapter = mMenuAdapter
                } else {
                }
                AppDialogs.hideProgressDialog()
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

    inner class DataSaveTask internal constructor(
        var mtrx_date: String, var mitemcode: String, var muom: String,
        var mitem: ArrayList<lbpricedet>
    ) : AsyncTask<Void?, Void?, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mresponse = DataAccessManager.savelbprice_new(mtrx_date, mitemcode,
                muom, mitem, mpricetype, "", myContext)  //to shankar
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
                    Toast.makeText(myContext, "Price Details has not updated",
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
            //AppDialogs.hideProgressDialog()
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
                       /* Toast.makeText(myContext, "Declare All Target Clusters", Toast.LENGTH_SHORT)
                            .show()
                        finish()*/
                        if (mEditable == true) {
                            mShowpopup=false
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

    inner class DataPricecount internal constructor() : AsyncTask<Void?, Void?, Boolean>() {
        protected override fun doInBackground(vararg params: Void?): Boolean {
            // mresponse= DataAccessManager.savelbprice(trx_date, itemcode, uom,
            // itemlist)(mUserName,mimei);
            mdiffprice = DataAccessManager.lbdeclare_prod_list("PRICE", myContext)
            responsecode = if (mdiffprice != null) {
                mdiffprice!!.statusCode.toInt()
            } else {
                -1
            }
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mGetItemTask = null
            //AppDialogs.hideProgressDialog()
            if (success) {
                if (responsecode >= 200) {

                    //list_custclub.setVisibility(View.GONE);
                    /* adapter = new ListViewAdapter_custclub(
                        CustomerClub.this, CustomerClub_POJO,
                        CustomerClub.this);
                list_custclub.setAdapter(adapter);*/

                   // var difflbpricepojo:difflbprice? = null
                  //  difflbpricepojo||.result[0].producT_CATEGORY="Cobb/Cobb Associate"
                    /*var pojo :difflblist ?=null
                    pojo?.producT_CATEGORY="Cobb/Cobb Asso."
                    pojo?.higH_WT="2.4"
                    pojo?.loW_WT="2"
                    pojo?.iteM_CODE="BRCULL"
                    pojo?.seQ_NO="7"
                    mdiffprice!!.result!!.add(pojo)*/
                    diffsize=mdiffprice!!.result!!.size

                    val recyclerView = findViewById<View>(R.id.recyclerview) as RecyclerView
                    val layoutManager = SpanningLinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )

                    recyclerView.layoutManager = layoutManager

                    val adapter =
                        RecyclerViewAdapter_Pricelistname(getApplicationContext(), mdiffprice!!.result!!)
                    //recyclerView.setHasFixedSize(true);
                    //  recyclerView.setLayoutManager(new LinearLayoutManager(CustomerClub.this));
                    //recyclerView.setHasFixedSize(true);
                    //  recyclerView.setLayoutManager(new LinearLayoutManager(CustomerClub.this));
                    recyclerView.adapter = adapter
                    if(diffsize == 0 || diffsize ==1)
                    {
                        if(mShowpopup==true) {
                            showDeclarePopup(myContext)
                        }
                      //  Toast.makeText(myContext,"Product category not yet declared, kindly declare than proceed for price declaration",Toast.LENGTH_LONG).show()
                      //  finish()
                    }
                } else {
                    diffsize=0
                    if(mShowpopup==true) {
                        showDeclarePopup(myContext)
                    }
                    //Toast.makeText(myContext,"Product category not yet declared, kindly declare than proceed for price declaration",Toast.LENGTH_LONG).show()
                    //finish()
                }
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            AppDialogs.hideProgressDialog()
        }
    }
    fun showDeclarePopup(myContext: BaseActivity) {
        val builder = AlertDialog.Builder(myContext)
        builder.setTitle("Declare Product category")
        builder.setMessage("Product category not yet declared, kindly declare than proceed for price declaration")
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
            return true
        }

        override fun onPostExecute(success: Boolean) {
            mGetpricemaxTask = null

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
        }

        override fun onCancelled() {
            mAuthTask = null

        }
    }

    override fun onClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(view: View?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun onSubmit(check:Boolean,branch_code:String) {
        AppDialogs.showProgressDialog(myContext)
        Log.e("sssd",branch_code)
        if(check)
        {
            for (j in ietmslist!!.indices) {
                if(ietmslist!![j].brancH_CODE.equals(branch_code))
                {
                val det = ietmslist!![j].diffprice


                for (i in det.indices) {

                    if(det[i].proV_STATUS=="Y" && det[i].status == "N")
                    {

                        val localRate = det[i].proV_PRICE
                        val localRate1=det[i].proV_PRICE.toDouble()
                        if (localRate.length > 0) {

                            if (localRate1 > 0 && det[i].proV_STATUS == "Y" && det[i].status == "N") {
                                det[i].rate=localRate
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
                        if (localRate1 > 0 && det[i].proV_STATUS == "Y" && det[i].status == "N") {

                            ietmslist!![j].diffprice.set(i,det[i])
                        }
                    }
                }
            }
            }
            mMenuAdapter!!.notifyDataSetChanged()
        }
        else
        {
            for (j in ietmslist!!.indices) {
                if(ietmslist!![j].brancH_CODE.equals(branch_code))
                {
                val det = ietmslist!![j].diffprice
                for (i in det.indices) {
                    if(det[i].proV_STATUS=="Y" && det[i].status == "N")
                    {

                        val localRate = det[i].proV_PRICE
                        val localRate1=det[i].proV_PRICE.toDouble()
                        if (localRate.length > 0) {

                            if (localRate1 > 0 && det[i].proV_STATUS == "Y" && det[i].status == "N") {
                                det[i].rate="0"
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
                        if (localRate1 > 0 && det[i].proV_STATUS == "Y" && det[i].status == "N") {
                            Log.e("sss",det[i].producT_CATEGORY)
                            Log.e("sss",det[i].proV_PRICE)
                            ietmslist!![j].diffprice.set(i,det[i])
                        }
                    }
                }
                }
            }
            mMenuAdapter!!.notifyDataSetChanged()
        }
        AppDialogs.hideProgressDialog()
    }
}




