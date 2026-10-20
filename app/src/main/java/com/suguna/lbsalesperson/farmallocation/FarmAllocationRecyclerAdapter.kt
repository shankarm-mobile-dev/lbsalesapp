package com.suguna.lbsalesperson.farmallocation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.LoginActivity
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.DeleteModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode

class FarmAllocationRecyclerAdapter(
    private val myContext: Context,
    private val mData: ArrayList<FarmAllocationModel.OrderDetails>,
    private val mBranchList: ArrayList<FarmAllocationModel.Branches>,
    private val mSupervosirList: ArrayList<FarmAllocationModel.Supervisor>,
    private var mClickList: BranchAllocationListener,
    private var mRegionId: String,
    private val mWeighmenttypeList: ArrayList<FarmAllocationModel.Weighmenttype>,
    private val diffprice: ArrayList<FarmAllocationModel.Diffprice>,
    private var mOrderDate: String
) : RecyclerView.Adapter<FarmAllocationRecyclerAdapter.ViewHolder>(), ResponseListener {
    var mMenuAdapter: FarmAllocationRecyclerSubAdapter? = null
    var currentPosition: Int = -1

    // view = 1
    // allocate = 2
    var isViewOrAllocate = "0"
    private var pendingResubmitItems: JSONArray? = null
    private var refreshAfterWsResubmit = false
    private var insertFarmPriceAfterCancel = false
    private val extraFarmList: ArrayList<FarmAllocationModel.Branches> = arrayListOf()
    private var farmPriceListCallback: ((ArrayList<FarmAllocationModel.Branches>) -> Unit)? = null
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_farm_allocation_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {

        aHolder.mNameTV.text = mData.get(position).refNo
        aHolder.mlifttimeTV.text =" "+mData.get(position).LIFTTIMESLOT
        aHolder.mOrderQtyTV.text = mData.get(position).customerName
        aHolder.mlifttimeTV.text =" "+mData.get(position).LIFTTIMESLOT
        aHolder.vehicle_name_tv.text =mData.get(position).VEHICLENUM
        aHolder.ordertype_tv.text=mData.get(position).bookingType
        if(!mData.get(position).PROD_CATEGORY.equals("-") && !mData.get(position).PROD_CATEGORY.equals("Live Bird")) {
            aHolder.lyt_prod_detls.visibility=View.VISIBLE
            aHolder.txt_prod_detls.text =
                mData.get(position).PROD_CATEGORY + " - " + mData.get(position).FROM_WEIGHT + " to " + mData.get(
                    position
                ).TO_WEIGHT+" kgs"
        }
        else
        {
            aHolder.lyt_prod_detls.visibility=View.GONE
            aHolder.txt_prod_detls.visibility=View.INVISIBLE
        }

        if (mData.get(position).totalQty.contains(".")) {
            aHolder.mRqdQtyTV.text = BigDecimal(mData.get(position)!!.totalQty).setScale(2, RoundingMode.HALF_EVEN).toString()
        } else {
            aHolder.mRqdQtyTV.text = mData.get(position).totalQty
        }
//        if (mData.get(position).prefAvgWgt.contains(".")) {
//            aHolder.mRqdQtyTV.text = BigDecimal(mData.get(position).prefAvgWgt).setScale(2, RoundingMode.HALF_EVEN).toString()
//        } else {
//            aHolder.mRqdQtyTV.text = mData.get(position).prefAvgWgt
//        }
        aHolder.mSubRecycleLay.visibility = View.GONE
        if (currentPosition == position) {

            val animation = android.view.animation.AnimationUtils.loadAnimation(myContext, R.anim.slide_down)

            setRecycleViewAdapter(aHolder, position)
            aHolder.mSubRecycleLay.visibility = View.VISIBLE
            if (isViewOrAllocate.equals("1")) {
                 aHolder.mSubmitTV.visibility = View.GONE
             } else  if (isViewOrAllocate.equals("2")) {
                if( mData.get(position).bookingType.equals("Normal")) {
                    aHolder.mSubmitTV.visibility = View.VISIBLE
               }
             }
            aHolder.mSubRecycleLay.animation = animation
        }


        if (mData.get(position).details!!.cnt == 0) {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.GONE
        } else if (mData.get(position).details!!.cnt > 0 && mData.get(position).details!!.sumQty < mData.get(position).totalQty.toDouble()) {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        } else if (mData.get(position).details!!.sumQty == mData.get(position).totalQty.toDouble()) {
                    //if(!(mData.get(position).STATUS.equals("A") && mData.get(position).bookingType.equals("Concession"))) {

            if (mData.get(position).orderRows!!.get(0).supervisorCode != null) {
                if (!mData.get(position).orderRows!!.get(0).supervisorCode.isEmpty()) {
                    if (mData.get(position).orderRows!!.get(0).submittemp == 0) {
                        aHolder.mAllocateTV.text = myContext.getString(R.string.label_Allocated);
                   }
                }
            }
            else
            {

            }
            if( mData.get(position).bookingType.equals("NORMAL"))
            {
                if (mData.get(position).orderRows!!.get(0).submittemp == 0) {
                    aHolder.mAllocateTV.text = myContext.getString(R.string.label_Allocated);
                }
            }
                    //}
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        } else {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        }
        var ORDER_STATUS="";
        try{
        if(! mData!!.get(position).ORDER_STATUS!!.equals(null)){
            ORDER_STATUS= mData.get(position).ORDER_STATUS!!.trim()
        }}catch (ex:Exception){}
        if (ORDER_STATUS.trim().equals("Ready for farm allocation"))
        {
            aHolder.lyt_btn_detls.visibility = View.VISIBLE
            aHolder.lyt_error_detls.visibility = View.GONE
            aHolder.txt_error_detls.text="";
        }
        else{
            aHolder.lyt_btn_detls.visibility = View.GONE
            aHolder.lyt_error_detls.visibility = View.VISIBLE
            aHolder.txt_error_detls.text="Order Status : "+ORDER_STATUS
            if((!mData.get(position).PROD_CATEGORY.equals("-")) && (!mData.get(position).PROD_CATEGORY.equals("Live Bird"))) {
                aHolder.lyt_prod_detls.visibility=View.VISIBLE
                aHolder.lyt_btn_detls.visibility = View.VISIBLE
                aHolder.lyt_allocate.visibility = View.GONE
                aHolder.txt_prod_detls.text =
                    mData.get(position).PROD_CATEGORY + " - " + mData.get(position).FROM_WEIGHT + " to " + mData.get(
                        position
                    ).TO_WEIGHT+" kgs"
            }
        }

        aHolder.mAllocateTV.setOnClickListener()
        {

            if (aHolder.mAllocateTV.text.equals(myContext.getString(R.string.allocate)) ) {

                if (isViewOrAllocate.equals("2") && currentPosition == position) {
                    isViewOrAllocate = "0"
                    currentPosition = -1
                    notifyDataSetChanged()
                } else {
                    isViewOrAllocate = "2"
                    currentPosition = position
                    notifyDataSetChanged()
                }
                if( mData.get(position).bookingType.equals("Normal")) {
                    aHolder.mSubmitTV.visibility = View.VISIBLE
                }
                else
                {
                    aHolder.mSubmitTV.visibility = View.GONE
                }

            }


        }

        aHolder.mViewTV.setOnClickListener()
        {
            /* aHolder.mSubmitTV.visibility = View.VISIBLE
             aHolder.mSubmitTV.text = myContext.getString(R.string.label_cancel)*/

            if (isViewOrAllocate.equals("1") && currentPosition == position) {
                isViewOrAllocate = "0"
                currentPosition = -1
                notifyDataSetChanged()
            } else {
                isViewOrAllocate = "1"
                currentPosition = position
                notifyDataSetChanged()
            }
            aHolder.mSubmitTV.visibility = View.GONE


        }
        aHolder.mSubmitTV.setOnClickListener()
        {

            mMenuAdapter!!.addNewBranch()
            /*   if (aHolder.mSubmitTV.text.equals(myContext.getString(R.string.label_submit))) {

                   var totalCount = 0.0
                   val items: ArrayList<BranchAllocationModel.OrderDetails.OrderRows> = mMenuAdapter!!.getItems()

                   items.forEach {

                       totalCount = +it.qty.toDouble()
                   }

                   if (totalCount <= mData.get(position).totalQty.toDouble()) {
                       if (Utility.isInternetAvailable(myContext)) {
                           confirmSubmit(items, position)
                       } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                           override fun yes() {

                           }
                       }, false)

                   } else {
                       AppDialogs.customOkAction(myContext, "Alert", "Total quantity exists the required quantity", "Ok", object : AppDialogs.ConfirmListener {
                           override fun yes() {

                           }
                       }, false)
                       //   AppDialogs.showToastshort(myContext, "Total quantity exists the required quantity")
                   }
               } else if (aHolder.mSubmitTV.text.equals(myContext.getString(R.string.label_cancel))) {
                   if (Utility.isInternetAvailable(myContext)) {
                       cancelOrder(mData.get(position).headerID)
                   } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                       override fun yes() {

                       }
                   }, false)


               }*/
        }

        aHolder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    private fun setRecycleViewAdapter(aHolder: ViewHolder, position: Int) {

        aHolder.mRecycleView.setHasFixedSize(true)
        aHolder.mRecycleView.setLayoutManager(LinearLayoutManager(myContext))
        aHolder.mRecycleView.setNestedScrollingEnabled(true)
        /*var aData: ArrayList<BranchAllocationModel.OrderDetails> = arrayListOf()
        val aNewBranch = BranchAllocationModel.OrderDetails()
        val orderRows = BranchAllocationModel.OrderDetails.OrderRows()
        aNewBranch.orderRows!!.add(orderRows)
        aData.add(aNewBranch)*/

        /*   if (mData.get(position).orderRows!!.size == 0) {
               val orderRows = BranchAllocationModel.OrderDetails().OrderRows()
               mData.get(position).orderRows!!.add(orderRows)
           }*/
        mMenuAdapter = FarmAllocationRecyclerSubAdapter(myContext, mData.get(position).orderRows!!, mBranchList,mSupervosirList, isViewOrAllocate, this, position, mData.get(position),mData.get(position).VEHICLENUM,mData.get(position).CUS_SMS_STATUS,mData.get(position).prefAvgWgt,mData.get(position).STATUS,mData.get(position).CUS_REQBTY_STATUS.trim(),mWeighmenttypeList,mData.get(position).PROD_CATEGORY,mData.get(position).FROM_WEIGHT,mData.get(position).TO_WEIGHT,diffprice)
        aHolder.mRecycleView.adapter = mMenuAdapter


    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {

        val mAllocateTV: TextView
        val mViewTV: TextView
        val mNameTV: TextView
        val mOrderQtyTV: TextView
        val mRqdQtyTV: TextView
        val mSubmitTV: TextView
        val mRecycleView: RecyclerView
        val mSubRecycleLay: LinearLayout
        val mlifttimeTV: TextView
        val txt_prod_detls: TextView
        val lyt_prod_detls: LinearLayout
        val vehicle_name_tv: TextView
        val ordertype_tv: TextView
        val lyt_btn_detls:LinearLayout
        val lyt_error_detls:LinearLayout
        val txt_error_detls:TextView
        val lyt_allocate:LinearLayout
        init {

            mRqdQtyTV = aView.findViewById<View>(R.id.required_qty_tv) as TextView
            mSubRecycleLay = aView.findViewById<View>(R.id.subrecycle_lay) as LinearLayout
            mNameTV = aView.findViewById<View>(R.id.customer_name_tv) as TextView
            mOrderQtyTV = aView.findViewById<View>(R.id.order_qty_tv) as TextView
            mAllocateTV = aView.findViewById<View>(R.id.allocate_tv) as TextView
            mViewTV = aView.findViewById<View>(R.id.view_tv) as TextView
            mSubmitTV = aView.findViewById<View>(R.id.submit_tv) as TextView
            mRecycleView = aView.findViewById<View>(R.id.sub_recyclerview) as RecyclerView
            mlifttimeTV = aView.findViewById<View>(R.id.lift_time_tv) as TextView
            txt_prod_detls = aView.findViewById<View>(R.id.txt_prod_detls) as TextView
            lyt_prod_detls= aView.findViewById<View>(R.id.lyt_prod_detls) as LinearLayout
            vehicle_name_tv= aView.findViewById<View>(R.id.vehicle_name_tv) as TextView
            ordertype_tv            = aView.findViewById<View>(R.id.ordertype_tv) as TextView
            lyt_btn_detls= aView.findViewById<View>(R.id.lyt_btn_detls) as LinearLayout
            lyt_error_detls= aView.findViewById<View>(R.id.lyt_error_detls) as LinearLayout
            txt_error_detls= aView.findViewById<View>(R.id.txt_error_detls) as TextView
            lyt_allocate= aView.findViewById<View>(R.id.lyt_allocate) as LinearLayout
        }
    }

    private fun submitOrder(items: JSONArray) {
        AppDialogs.showProgressDialog(myContext)


        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, LoginActivity.Name, "")!!)
        jsonParam.put("BRANCH_CODE", mRegionId)
        jsonParam.put("LINES", items)

        Log.e("jsonParam",jsonParam.toString())
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.submit_farmorder,
                DeleteModel::class.java, jsonParam

        )
    }

    fun cancelOrder(headerID: String,lineID: String,detlineID: String) {
        AppDialogs.showProgressDialog(myContext)

        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("HEADERID", headerID)
        jsonParam.put("LINEID", lineID)
        jsonParam.put("DETLINEID", detlineID)
        Log.e("jsonParam",jsonParam.toString())
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.cancel_farmorder,
                DeleteModel::class.java, jsonParam

        )
    }

    fun cancelOrderreason(
        headerID: String,
        reason: String,
        detlineID: String,
        resubmitItems: JSONArray? = null,
        insertFarmPrice: Boolean = false
    ) {
        pendingResubmitItems = resubmitItems
        refreshAfterWsResubmit = false
        insertFarmPriceAfterCancel = insertFarmPrice
        try {
            AppDialogs.showProgressDialog(myContext)
            val jsonParam = JSONObject()
            jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
            jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
            jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
            jsonParam.put("REFERENCE_NO", headerID)
            jsonParam.put("DETAILSLINEID", detlineID)
            jsonParam.put("CANCEL_REASON", reason)
            jsonParam.put("ORDERTYPE", "Cancel")
            Log.e("jsonParam", jsonParam.toString())
            AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.OrderCancel,
                CanceldetailModel::class.java, jsonParam

            )
        }
        catch (e: Exception)
        {
            pendingResubmitItems = null
            insertFarmPriceAfterCancel = false
            AppDialogs.hideProgressDialog()
        }
    }

    fun buildSubmitItemsForRow(
        row: FarmAllocationModel.OrderDetails.OrderRows,
        orderPosition: Int
    ): JSONArray {
        val jItemArray = JSONArray()
        val pnObj = JSONObject()
        pnObj.put("BRANCH_CODE", row.branchCode.trim())
        pnObj.put("QUANTITY", row.qty)
        pnObj.put("LINE_ID", mData.get(orderPosition).lineID.trim())
        pnObj.put("HEADER_ID", mData.get(orderPosition).headerID.trim())
        pnObj.put("SUP_CODE", row.supervisorCode.trim())
        pnObj.put("SUP_MOB", row.mobileNo.trim())
        pnObj.put("REQ_WTD", row.avgWgt)
        pnObj.put("CUST_CODE", mData.get(orderPosition).customerCode.trim())
        pnObj.put("WEIGHMENT_TYPE", row.weighmentType.trim())
        pnObj.put("PRODUCT_NAME", row.PROD_CATEGORY.trim())
        pnObj.put("HIGH_WT", row.TO_WEIGHT.trim())
        pnObj.put("LOW_WT", row.FROM_WEIGHT.trim())
        pnObj.put("PREFERRED_FARM_CODE", row.PREFERRED_FARM_CODE?.trim())
        pnObj.put("PREFERRED_FARM_STATUS", row.PREFERRED_FARM_STATUS?.trim())
        extraFarmList.forEach {
            if ((it.FARM_CODE).equals(row.branchCode.trim())) {
                putFarmMetrics(pnObj, it)
                return@forEach
            }
        }
        if (!pnObj.has("FARM_STOCK")) {
            mBranchList.forEach {
                if ((it.FARM_CODE).equals(row.branchCode.trim())) {
                    putFarmMetrics(pnObj, it)
                    return@forEach
                }
            }
        }
        jItemArray.put(pnObj)
        return jItemArray
    }

    private fun putFarmMetrics(pnObj: JSONObject, farm: FarmAllocationModel.Branches) {
        pnObj.put("FARM_STOCK", toFloatSafe(farm.FARM_STOCK))
        pnObj.put("BODY_WT", toFloatSafe(farm.BODY_WT))
        pnObj.put("MEAN_AGE", toFloatSafe(farm.MEAN_AGE))
        pnObj.put("APPROX_AVG_WT", toFloatSafe(farm.APPROX_AVG_WT))
    }

    private fun toFloatSafe(value: String?): Float {
        return try {
            if (value.isNullOrBlank()) 0f else value.toFloat()
        } catch (e: Exception) {
            0f
        }
    }

    fun findFarm(farmCode: String?): FarmAllocationModel.Branches? {
        if (farmCode.isNullOrBlank()) return null
        extraFarmList.firstOrNull { it.FARM_CODE.equals(farmCode, true) }?.let { return it }
        return mBranchList.firstOrNull { it.FARM_CODE.equals(farmCode, true) }
    }

    fun addFarmPriceBranches(list: ArrayList<FarmAllocationModel.Branches>) {
        list.forEach { incoming ->
            if (extraFarmList.none { it.FARM_CODE.equals(incoming.FARM_CODE, true) }) {
                extraFarmList.add(incoming)
            }
        }
    }

    fun fetchBranchFarmPrice(onResult: (ArrayList<FarmAllocationModel.Branches>) -> Unit) {
        farmPriceListCallback = onResult
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, LoginActivity.Name, "")!!)
        jsonParam.put("REGIONCODE", mRegionId)
        jsonParam.put("ORDERDATE", mOrderDate)
        Log.e("jsonParam", jsonParam.toString())
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_branch_farmprice,
            FarmPriceFarmListModel::class.java, jsonParam
        )
    }

    fun insertFarmPriceOrder(items: JSONArray) {
        AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, LoginActivity.Name, "")!!)
        jsonParam.put("ORDERDATE", mOrderDate)
        jsonParam.put("BRANCH_CODE", mRegionId)
        jsonParam.put("LINES", items)
        Log.e("jsonParam", jsonParam.toString())
        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.insert_orderlinefarmpricedet,
            DeleteModel::class.java, jsonParam
        )
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()

        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.cancel_farmorder.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_success))
                    mClickList.onSubmit()
                }
                else if (res.data?.statusCode!!.equals("-113")) {
                    AppDialogs.showToastshort(myContext, res.data?.message.toString())
                    //mClickList.onSubmit()
                }
                else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }


            } else if (r.requestType.hashCode() == AppServices.API.submit_farmorder.hashCode()) {
                Log.e("error",r.toString())
                val res = (r as DeleteModel)
                val forceRefresh = refreshAfterWsResubmit
                refreshAfterWsResubmit = false
                if (res.data?.statusCode!!.equals("200")) {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_success))
                    mClickList.onSubmit()
                }
                else if (res.data?.statusCode!!.equals("-113")) {
                    AppDialogs.showToastshort(myContext, res.data?.message.toString())
                    if (forceRefresh) {
                        mClickList.onSubmit()
                    }
                }
                else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                    if (forceRefresh) {
                        mClickList.onSubmit()
                    }
                }
            }
            else if (r.requestType.hashCode() == AppServices.API.OrderCancel.hashCode()) {
                val res = (r as CanceldetailModel)
                if (res.data?.size!!>0) {
                    res.data?.forEach {
                        if(it.STATUSCODE1!!.equals("200")) {
                            AppDialogs.showToastshort(myContext, it.MESSAGE1!!)
                            val itemsToSubmit = pendingResubmitItems
                            pendingResubmitItems = null
                            if (itemsToSubmit != null && itemsToSubmit.length() > 0) {
                                if (insertFarmPriceAfterCancel) {
                                    insertFarmPriceAfterCancel = false
                                    insertFarmPriceOrder(itemsToSubmit)
                                } else {
                                    refreshAfterWsResubmit = true
                                    submitOrder(itemsToSubmit)
                                }
                            } else {
                                insertFarmPriceAfterCancel = false
                                mClickList.onSubmit()
                            }
                        }
                        else
                        {
                            pendingResubmitItems = null
                            insertFarmPriceAfterCancel = false
                            AppDialogs.showToastshort(myContext, it.MESSAGE1!!)
                        }
                    }

                } else {
                    pendingResubmitItems = null
                    insertFarmPriceAfterCancel = false
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }
            } else if (r.requestType.hashCode() == AppServices.API.get_branch_farmprice.hashCode()) {
                val callback = farmPriceListCallback
                farmPriceListCallback = null
                val data = (r as FarmPriceFarmListModel)
                val farms = data.farmList()
                if (farms.size > 0) {
                    addFarmPriceBranches(farms)
                    callback?.invoke(farms)
                } else {
                    AppDialogs.showToastshort(myContext, "Farm not available.")
                }
            } else if (r.requestType.hashCode() == AppServices.API.insert_orderlinefarmpricedet.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_success))
                    mClickList.onSubmit()
                } else if (res.data?.statusCode!!.equals("-113")) {
                    AppDialogs.showToastshort(myContext, res.data?.message.toString())
                    mClickList.onSubmit()
                } else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }
            }
        }
    }

    fun confirmSubmit(
        items: ArrayList<FarmAllocationModel.OrderDetails.OrderRows>,
        position: Int
    ) {
        try {
        var msg=""
        var isValid = true
        var isValid1 = true
            var isValid2 = false
        var farmstock=0f
        val jItemArray = JSONArray()
        var isWeigment = true
        var avgwt=0.0f
        var bdwt=0.0f
        var diff=0.0f
        for (i in items.indices) {
            if(items.get(i).STATUS.equals("N")) {
            farmstock=0f
            val pnObj = JSONObject()
            pnObj.put("BRANCH_CODE", items.get(i).branchCode.trim())
            pnObj.put("QUANTITY", items.get(i).qty)
            pnObj.put("LINE_ID", mData.get(position).lineID.trim())
            pnObj.put("HEADER_ID", mData.get(position).headerID.trim())
            pnObj.put("SUP_CODE", items.get(i).supervisorCode.trim())
            pnObj.put("SUP_MOB", items.get(i).mobileNo.trim())  //
            pnObj.put("REQ_WTD", items.get(i).avgWgt)
            pnObj.put("CUST_CODE", mData.get(position).customerCode.trim())
            pnObj.put("WEIGHMENT_TYPE", items.get(i).weighmentType.trim())

            pnObj.put("PRODUCT_NAME", items.get(i).PROD_CATEGORY.trim())
            pnObj.put("HIGH_WT", items.get(i).TO_WEIGHT.trim())
            pnObj.put("LOW_WT", items.get(i).FROM_WEIGHT.trim())

            pnObj.put("PREFERRED_FARM_CODE", items.get(i).PREFERRED_FARM_CODE?.trim())
            pnObj.put("PREFERRED_FARM_STATUS", items.get(i).PREFERRED_FARM_STATUS?.trim())

            mBranchList.forEach {
                if ((it.FARM_CODE).equals(items.get(i).branchCode.trim())) {

                    farmstock=it.FARM_STOCK.toFloat()
                    pnObj.put("FARM_STOCK", it.FARM_STOCK.toFloat())
                    pnObj.put("BODY_WT", it.BODY_WT.toFloat())
                    pnObj.put("MEAN_AGE", it.MEAN_AGE.toFloat())
                    pnObj.put("APPROX_AVG_WT", it.APPROX_AVG_WT.toFloat())
                    return@forEach
                }

                //  mSelectedBranch.add(it.FARM_DESC)
            }

            if (mWeighmenttypeList != null) {
                if (mWeighmenttypeList.size > 0) {
                    isWeigment=true
                } else {
                    isWeigment=false
                }
            } else {
                isWeigment=false
            }



            if (items.get(i).qty.isEmpty() || items.get(i).branchCode.isEmpty() || items.get(i).branchCode.trim().equals("-") || items.get(i).supervisorCode.isEmpty() || items.get(i).mobileNo.isEmpty() || items.get(i).mobileNo.trim().equals("0") /*||  items.get(i).mobileNo.trim().length <= 13*/ || (items.get(i).avgWgt.isEmpty()&& mData.get(position).CUS_REQBTY_STATUS.trim().equals("1")) || (isWeigment==true && items.get(i).weighmentType.trim().isEmpty())) {
                isValid = false
                msg="Please enter the all details "
            } else
                if(mData.get(position).CUS_REQBTY_STATUS.trim().equals("1"))
                {
                    avgwt=items.get(i).avgWgt.toFloat()*1000
                    mBranchList.forEach {
                        if ((it.FARM_CODE).equals(items.get(i).branchCode)) {
                            bdwt=it.BODY_WT.toFloat()*1000
                            return@forEach
                        }
                        //  mSelectedBranch.add(it.FARM_DESC)
                    }
                    diff=avgwt-bdwt;
                    if(items.get(i).avgWgt.toFloat() > 3 )
                    {
                        isValid = false
                        msg="Entered Average weight not greater then 3 kg"
                    }
                    else if(items.get(i).avgWgt.toFloat() <= 0 )
                    {
                        isValid = false
                        msg="Entered Average weight greater then 0 kg"
                    }
                    else if( items.get(i).qty.toFloat() <= 0 )
                    {
                        isValid = false
                        msg = "Entered order quantity greater then 0 kg"
                    }
                        else if((diff > 100 || diff < -100) && mData.get(position).bookingType.equals("Normal"))
                    {
                        isValid = false
                        isValid1 = false
                        msg="ETS Declared Body Weight and Allocated Body weight have difference more/less than 100 grams, do you want to proceed for Farm allocation?"
                    }
//            else if( items.get(i).qty.toFloat() > farmstock )
//                {
//                    isValid = false
//                    msg="Entered weight not greater then farmstock "+farmstock+"kg"
//                }
                    else
                    {
                        isValid2=true
                        jItemArray.put(pnObj)
                    }
                }
            else
                { if( items.get(i).qty.toFloat() <= 0 )
                    {
                        isValid = false
                        msg="Entered order quantity greater then 0 kg"
                    }
//            else if( items.get(i).qty.toFloat() > farmstock )
//                {
//                    isValid = false
//                    msg="Entered weight not greater then farmstock "+farmstock+"kg"
//                }
                    else
                    {
                        isValid2=true
                        jItemArray.put(pnObj)
                    }
                }
        }
        }
        Log.e("jItemArray",jItemArray.toString())
        if (isValid && isValid2)
        {
           submitOrder(jItemArray)
        }
        else if(!isValid2)
        {
                  AppDialogs.customOkAction(
                myContext,
                "Alert",
                "No Farm allocation details to upload.",
                "Ok",
                object : AppDialogs.ConfirmListener {
                    override fun yes() {

                    }
                },
                false
            )
        }
        else if(!isValid1)
        {
            AppDialogs.customDoubleAction(myContext, "Alert", msg, "Yes",
                "cancel", object : AppDialogs.OptionListener {
                    override fun no() {

                    }
                    override fun yes() {
                      // submitOrder(jItemArray)
                    }
                }, true, false)
        }
        else
        {
            AppDialogs.customOkAction(
                myContext,
                "Alert",
                msg,
                "Ok",
                object : AppDialogs.ConfirmListener {
                    override fun yes() {

                    }
                },
                false
            )
        }
        }
        catch (e: Exception)
        {
            Toast.makeText(myContext,e.message, Toast.LENGTH_LONG).show()
        }
    }

}