package com.suguna.lbsalesperson.branchallocation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.branchallocation.model.BranchAllocationModel
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.branchallocation.model.DeleteModel
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONObject


class BranchAllocationRecyclerSubAdapter(
        private val myContext: Context,
        private val mData: ArrayList<BranchAllocationModel.OrderDetails.OrderRows>,
        private val mBranchList: ArrayList<BranchAllocationModel.Branches>, val isViewOrAllocate: String,
        val adapter: BranchAllocationRecyclerAdapter, val pos: Int, val mParentObj: BranchAllocationModel.OrderDetails,val VEHICLENUM:String,val status:String
) : RecyclerView.Adapter<BranchAllocationRecyclerSubAdapter.ViewHolder>(), ResponseListener {

    var mSelectedBranch = arrayListOf<String>()
    val aBranchNameList = arrayListOf<String>()
    var selectedPosition = -1
    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_branch_allocation_sub_item_adapter, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {


        aHolder.mBranchNameTv.text = ""
        aHolder.vehicle_name_tv.text =VEHICLENUM
        mBranchList.forEach {
            if(mParentObj.RETAIL_ORDER.equals("N")) {
                if (it.branchCode.equals(mData.get(position).branchCode)) {
                    aHolder.mBranchNameTv.text = it.location

                    if (!mSelectedBranch.contains(it.location)){
                        mSelectedBranch.add(it.location)}
                    return@forEach
                }
            }
            else
            {
                if (it.branchCode.equals(mData.get(position).branchCode)) {
                    aHolder.mBranchNameTv.text = it.location

                    if (!mSelectedBranch.contains(it.location)){
                        if(it.orgType.equals("RETAIL DIVISION")){
                            mSelectedBranch.add(it.location)}}
                    return@forEach
                }
            }
        }
        if (!mData.get(position).qty.isEmpty()) {
            aHolder.mQtyTv.setText(mData.get(position).qty)
            aHolder.mQtyTv.setSelection(mData.get(position).qty.length)
        } else {
            aHolder.mQtyTv.setText("")
        }

        aHolder.mQtyTv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                try {
                    updateQty(position, s.toString())
                } catch (e: Exception) {
                     //updateQty(position, "0")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        if (position == mData.size - 1) {
            //   aHolder.mAddBranch.visibility = View.VISIBLE
            aHolder.mSubmitlay.visibility = View.VISIBLE
            if (isViewOrAllocate.equals("1", true)) {
                aHolder.mSubmitTV.text = myContext.getString(R.string.label_cancel)
                aHolder.mSubmitlay.visibility = View.GONE
            } else {
                aHolder.mSubmitTV.text = myContext.getString(R.string.label_submit)
            }

        } else {
            //    aHolder.mAddBranch.visibility = View.GONE
            aHolder.mSubmitlay.visibility = View.GONE
        }
        if(status.equals("Y")) {
            aHolder.mSubmitlay.visibility = View.GONE
        }
        aHolder.mAddBranch.setOnClickListener {

            addNewBranch()
        }

        aHolder.mSubmitlay.setOnClickListener {
            if (aHolder.mSubmitTV.text.equals(myContext.getString(R.string.label_submit))) {
                var totalCount = 0.0

                val items: ArrayList<BranchAllocationModel.OrderDetails.OrderRows> = getItems()

                items.forEach {

                    if (!it.qty.isEmpty())
                        totalCount = totalCount+it.qty.toDouble()
                }
                //Log.e("totalCount",totalCount.toString());
               // Log.e("Double",mParentObj.totalQty.toString());
                if (totalCount <= mParentObj.totalQty.toDouble()) {
                    if (Utility.isInternetAvailable(myContext)) {
                       adapter.confirmSubmit(mData, pos)
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
                    adapter.cancelOrder(mParentObj.headerID)
                } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                    override fun yes() {

                    }
                }, false)


            }
        }
        aHolder.mDeleteImg.setOnClickListener {

            selectedPosition = position
            AppDialogs.customDoubleAction(myContext, "Delete ?", "Are you sure want to delete this line ?", "Yes",
                    "cancel", object : AppDialogs.OptionListener {
                override fun no() {

                }

                override fun yes() {
                    if (mData.get(position).lineId.isNotEmpty() && !(mData.get(position).lineId.equals("0"))) {

                        if (Utility.isInternetAvailable(myContext)) {
                            deleteLine(mData.get(position).lineId)

                        } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                            override fun yes() {

                            }
                        }, false)

                    } else {
                        addDeleteBranch(position)
                    }
                }


            }, true, false)

        }

        aHolder.mBranchNameTv.setOnClickListener {
            aBranchNameList.clear()
            mBranchList.forEach {
                if(mParentObj.RETAIL_ORDER.equals("N")) {
                    if (!mSelectedBranch.contains(it.location)){
                        aBranchNameList.add(it.location)}
                }
                else
                {
                    if (!mSelectedBranch.contains(it.location)){
                        if(it.orgType.equals("RETAIL DIVISION")){
                            aBranchNameList.add(it.location)}}
                }
            }
            if (mBranchList.size > 0) {
                AppDialogs.showSingleChoice(myContext, "Choose Branch", aBranchNameList, object : SingleChoiceAdapter.Callback {
                    override fun info(pos: Int, text: String) {
                        AppDialogs.hideSingleChoice()
                        aHolder.mBranchNameTv.text = text
                        mBranchList.forEach {
                            if (it.location.equals(text, true)) {
                                if(it.wfStatus.equals("T")){
                                if (!mSelectedBranch.contains(it.location))
                                    mSelectedBranch.add(it.location)
                                updateBranch(position, it.branchCode)}
                                else{
                                    aHolder.mBranchNameTv.text =""
                                    AppDialogs.customOkAction(myContext, "Alert", it.wfStatus, "Ok", object : AppDialogs.ConfirmListener {
                                        override fun yes() {

                                        }
                                    }, false)
                                }
                                return@forEach
                            }
                        }

                    }
                }, true, true)
            } else AppDialogs.customOkAction(myContext, "Alert", "Branch not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
        }


        if (isViewOrAllocate.equals("1")) {
            aHolder.mDeleteImg.visibility = View.INVISIBLE
            aHolder.mBranchNameTv.isClickable = false
            aHolder.mQtyTv.isFocusable = false
            aHolder.mQtyTv.isClickable = false
            aHolder.mQtyTv.isFocusable = false
        } else {

        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {
        val mDeleteImg: ImageView
        val mBranchNameTv: TextView
        val mSubmitTV: TextView
        val mQtyTv: EditText
        val mAddBranch: LinearLayout
        val mSubmitlay: LinearLayout
        val vehicle_name_tv: TextView


        init {

            mQtyTv = aView.findViewById<View>(R.id.qty_tv) as EditText
            mSubmitTV = aView.findViewById<View>(R.id.submit_tv) as TextView
            mBranchNameTv = aView.findViewById<View>(R.id.branch_name_tv) as TextView
            mDeleteImg = aView.findViewById<View>(R.id.delete_img) as ImageView
            mAddBranch = aView.findViewById<View>(R.id.add_new_branch_lay) as LinearLayout
            mSubmitlay = aView.findViewById<View>(R.id.submit_lay) as LinearLayout
            vehicle_name_tv= aView.findViewById<View>(R.id.vehicle_name_tv) as TextView


        }
    }

    fun addNewBranch() {
        val aNewBranch = BranchAllocationModel.OrderDetails().OrderRows()
        mData.add(aNewBranch)
        notifyDataSetChanged()
    }

    fun addDeleteBranch(position: Int) {
        mBranchList.forEach {
            if (it.branchCode.equals(mData.get(position).branchCode)) {
                mSelectedBranch.remove(it.location)
                return@forEach
            }
        }
        mData.removeAt(position)
        notifyDataSetChanged()

    }


    fun getItems(): ArrayList<BranchAllocationModel.OrderDetails.OrderRows> {

        return mData
    }


    fun updateQty(position: Int, count: String) {
        mData.get(position).qty = count
    }

    fun updateBranch(position: Int, branchCode: String) {
        mData.get(position).branchCode = branchCode
    }

    private fun deleteLine(lineId: String) {
        AppDialogs.showProgressDialog(myContext)


        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("LINEID", lineId)
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.branch_allocation_line_delete,
                DeleteModel::class.java, jsonParam

        )
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()

        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.branch_allocation_line_delete.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    // notifyDataSetChanged()
                    AppDialogs.showToastshort(myContext, "Success")
                    addDeleteBranch(selectedPosition)
                } else {
                    AppDialogs.showToastshort(myContext, "Something went wrong!!!. Please try again")
                }
            }
        }
    }
}