package com.suguna.lbsalesperson.branchallocation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.ICaster
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.BranchAllocationModel
import com.suguna.lbsalesperson.branchallocation.model.DeleteModel
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode


class BranchAllocationRecyclerAdapter(
        private val myContext: Context,
        private val mData: ArrayList<BranchAllocationModel.OrderDetails>,
        private val mBranchList: ArrayList<BranchAllocationModel.Branches>,
        private var mClickList: BranchAllocationListener
) : RecyclerView.Adapter<BranchAllocationRecyclerAdapter.ViewHolder>(), ResponseListener {
    var mMenuAdapter: BranchAllocationRecyclerSubAdapter? = null
    var currentPosition: Int = -1

    // view = 1
    // allocate = 2
    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_branch_allocation_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {

        aHolder.mNameTV.text = mData.get(position).refNo + " / " + mData.get(position).customerName
        aHolder.mlifttimeTV.text = " " + mData.get(position).LIFTTIMESLOT
        if (!mData.get(position).PROD_CATEGORY.equals("")){
            if (!mData.get(position).PROD_CATEGORY.equals("-")) {
                aHolder.lyt_prod_detls.visibility = View.VISIBLE
                aHolder.txt_prod_detls.text =
                    mData.get(position).PROD_CATEGORY + " - " + mData.get(position).FROM_WEIGHT + " to " + mData.get(
                        position
                    ).TO_WEIGHT + " kgs"
            }
    }
            if (mData.get(position).totalQty.contains(".")) {
            aHolder.mOrderQtyTV.text = BigDecimal(mData.get(position)!!.totalQty).setScale(2, RoundingMode.HALF_EVEN).toString()
        } else {
            aHolder.mOrderQtyTV.text = mData.get(position).totalQty
        }
        if (mData.get(position).prefAvgWgt.contains(".")) {
           // aHolder.mRqdQtyTV.text = BigDecimal(mData.get(position).prefAvgWgt).setScale(2, RoundingMode.HALF_EVEN).toString()
            aHolder.mRqdQtyTV.text = mData.get(position).prefAvgWgt
        } else {
            aHolder.mRqdQtyTV.text = mData.get(position).prefAvgWgt
        }
        aHolder.mSubRecycleLay.visibility = View.GONE
        if (currentPosition == position) {

            val animation = android.view.animation.AnimationUtils.loadAnimation(myContext, R.anim.slide_down)

            setRecycleViewAdapter(aHolder, position)
            aHolder.mSubRecycleLay.visibility = View.VISIBLE
            /* if (isViewOrAllocate.equals("1")) {
                 aHolder.mSubmitTV.visibility = View.GONE
             } else {
                 aHolder.mSubmitTV.visibility = View.VISIBLE
             }*/
            aHolder.mSubRecycleLay.animation = animation

        }


       if (mData.get(position).details!!.cnt == 0) {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.GONE
        } else if (mData.get(position).details!!.cnt > 0 && mData.get(position).details!!.sumQty < mData.get(position).totalQty.toDouble()) {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        } else if (mData.get(position).details!!.sumQty == mData.get(position).totalQty.toDouble()) {
            aHolder.mAllocateTV.text = myContext.getString(R.string.label_Allocated)
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        } else {
            aHolder.mAllocateTV.visibility = View.VISIBLE
            aHolder.mViewTV.visibility = View.VISIBLE
        }


        aHolder.mAllocateTV.setOnClickListener()
        {

            if (aHolder.mAllocateTV.text.equals(myContext.getString(R.string.allocate)) && mData.get(position).ALLOWSTATUS.equals("Y")) {
                if (aHolder.mAllocateTV.text.equals(myContext.getString(R.string.allocate)) && (!mData.get(position).SALES_PERSON.equals("1")))
                {
                    /*ICaster.Toast_msg_bottom(myContext,
                        mData.get(position).SALES_PERSON, Toast.LENGTH_SHORT,
                        ICaster.notify)*/
                    AppDialogs.customOkAction(myContext, "Alert", mData.get(position).SALES_PERSON, "Ok", object : AppDialogs.ConfirmListener {
                        override fun yes() {

                        }
                    }, false)

                }
                else
                {
                if (isViewOrAllocate.equals("2") && currentPosition == position) {
                    isViewOrAllocate = "0"
                    currentPosition = -1
                    notifyDataSetChanged()
                } else {
                    isViewOrAllocate = "2"
                    currentPosition = position
                    notifyDataSetChanged()
                }
                aHolder.mSubmitTV.visibility = View.VISIBLE }
            }
            else if (aHolder.mAllocateTV.text.equals(myContext.getString(R.string.allocate)) && mData.get(position).ALLOWSTATUS.equals("N"))
            {
               /* ICaster.Toast_msg_bottom(myContext,
                        "Still vehicle no. not yet allocated by the customer, advised to allocate the vehicle details in the LB  customer mobile app Order Status menu then proceed. ", Toast.LENGTH_SHORT,
                        ICaster.notify)*/
                AppDialogs.customOkAction(myContext, "Alert", "Still vehicle no. not yet allocated by the customer, advised to allocate the vehicle details in the LB  customer mobile app Order Status menu then proceed. ", "Ok", object : AppDialogs.ConfirmListener {
                    override fun yes() {

                    }
                }, false)

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
        mMenuAdapter = BranchAllocationRecyclerSubAdapter(myContext, mData.get(position).orderRows!!, mBranchList, isViewOrAllocate, this, position, mData.get(position),mData.get(position).VEHICLENUM,mData.get(position).STATUS )
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
        val lyt_prod_detls: LinearLayout
        val txt_prod_detls: TextView

        init {

            mRqdQtyTV = aView.findViewById<View>(R.id.required_qty_tv) as TextView
            mSubRecycleLay = aView.findViewById<View>(R.id.subrecycle_lay) as LinearLayout
            mNameTV = aView.findViewById<View>(R.id.customer_name_tv) as TextView
            mOrderQtyTV = aView.findViewById<View>(R.id.order_qty_tv) as TextView
            mAllocateTV = aView.findViewById<View>(R.id.allocate_tv) as TextView
            mViewTV = aView.findViewById<View>(R.id.view_tv) as TextView
            mSubmitTV = aView.findViewById<View>(R.id.submit_tv) as TextView
            mlifttimeTV = aView.findViewById<View>(R.id.lift_time_tv) as TextView
            mRecycleView = aView.findViewById<View>(R.id.sub_recyclerview) as RecyclerView
            lyt_prod_detls = aView.findViewById<View>(R.id.lyt_prod_detls) as LinearLayout
            txt_prod_detls = aView.findViewById<View>(R.id.txt_prod_detls) as TextView


        }
    }

    private fun submitOrder(items: JSONArray) {
        AppDialogs.showProgressDialog(myContext)


        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("LINES", items)

        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.submit_order,
                DeleteModel::class.java, jsonParam

        )
    }

    fun cancelOrder(headerID: String) {
        AppDialogs.showProgressDialog(myContext)

        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("HEADERID", headerID)

        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.cancel_order,
                DeleteModel::class.java, jsonParam

        )
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()

        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.cancel_order.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_success))
                    mClickList.onSubmit()
                } else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }


            } else if (r.requestType.hashCode() == AppServices.API.submit_order.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_success))
                    mClickList.onSubmit()
                } else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }
            }
        }
    }

    fun confirmSubmit(items: ArrayList<BranchAllocationModel.OrderDetails.OrderRows>, position: Int) {

        var isValid = true
        val jItemArray = JSONArray()
        for (i in items.indices) {
            val pnObj = JSONObject()
            pnObj.put("BRANCH_CODE", items.get(i).branchCode.trim())
            pnObj.put("QUANTITY", items.get(i).qty)
            pnObj.put("LINE_ID", items.get(i).lineId.trim())
            pnObj.put("HEADER_ID", mData.get(position).headerID.trim())
            if (items.get(i).qty.isEmpty() || items.get(i).branchCode.isEmpty() || items.get(i).branchCode.trim().equals("-")) {
                isValid = false
            } else
                jItemArray.put(pnObj)
        }
        if (isValid)
            submitOrder(jItemArray)
        else AppDialogs.customOkAction(myContext, "Alert", "Please enter the details", "Ok", object : AppDialogs.ConfirmListener {
            override fun yes() {

            }
        }, false)

    }

}