package com.suguna.lbsalesperson.farmallocation

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.LocalStorageSP.getLoginUser
import com.suguna.lbsalesperson.Utils.Utility
import com.suguna.lbsalesperson.branchallocation.model.DeleteModel
import com.suguna.lbsalesperson.diffpriceallocation.DropDown_list
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.truelife.api.AppServices
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import org.json.JSONArray
import org.json.JSONObject

class FarmAllocationRecyclerSubAdapter(
    private val myContext: Context,
    private val mData: ArrayList<FarmAllocationModel.OrderDetails.OrderRows>,
    private val mBranchList: ArrayList<FarmAllocationModel.Branches>,
    private val mSupervosirList: ArrayList<FarmAllocationModel.Supervisor>,
    val isViewOrAllocate: String,
    val adapter: FarmAllocationRecyclerAdapter,
    val pos: Int,
    val mParentObj: FarmAllocationModel.OrderDetails,
    val VEHICLENUM: String,
    val CUS_SMS_STATUS: String,
    val PREFAVGWT: String,
    val status: String,
    val reqbty_status: String,
    private val mWeighmenttypeList: ArrayList<FarmAllocationModel.Weighmenttype>,
    val prod_cat: String,
    val fromwt: String,
    val towt: String,
    private val diffprice: ArrayList<FarmAllocationModel.Diffprice>
) : RecyclerView.Adapter<FarmAllocationRecyclerSubAdapter.ViewHolder>(), ResponseListener {

    var mSelectedBranch = arrayListOf<String>()
    val aBranchNameList = arrayListOf<String>()
    val aSupervisorNameList = arrayListOf<String>()
    val aWeigmentList = arrayListOf<String>()
    val aProductcatList = arrayListOf<String>()
    val aCancelresionList = arrayListOf<String>()
    var selectedPosition = -1
    private val reasonWsWronglyAllocated = "WS Wrongly Allocated"

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_farm_allocation_sub_item_adapter, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {


        aHolder.mBranchNameTv.text = ""
        aHolder.vehicle_name_tv.text =VEHICLENUM
        mBranchList.forEach {
            if ((it.FARM_CODE).equals(mData.get(position).branchCode)) {
                aHolder.mBranchNameTv.text = it.FARM_CODE+" - "+ it.FARM_DESC +" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE

                if (!mSelectedBranch.contains(it.FARM_CODE+" - "+it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE))
                    mSelectedBranch.add(it.FARM_CODE+" - "+it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE)
                return@forEach
            }
          //  mSelectedBranch.add(it.FARM_DESC)
        }
        if (aHolder.mBranchNameTv.text.isNullOrBlank()) {
            val extraFarm = adapter.findFarm(mData.get(position).branchCode)
            if (extraFarm != null) {
                aHolder.mBranchNameTv.text = farmDisplayName(extraFarm)
            }
        }
        Log.e("prod_cat",prod_cat);
        if(prod_cat!=("-") ) {
            aHolder.txt_prod_detls_1.visibility=View.VISIBLE
            aHolder.mPrefAvgQtyTv.visibility=View.VISIBLE
            if (mData.get(position).PROD_CATEGORY != null) {
                if (!mData.get(position).PROD_CATEGORY.equals("-")) {
                    Log.e("prod_cat1",prod_cat);
                    Log.e("prod_cat1",mData.get(position).PROD_CATEGORY);
                    aHolder.mPrefAvgQtyTv.setText(mData.get(position).PROD_CATEGORY!!+ " - " +mData.get(position).FROM_WEIGHT!!+ " to " +mData.get(position).TO_WEIGHT!!+" kgs")
                    //   aHolder.mSupervisorMobTv.setSelection(mData.get(position).mobileNo.length)
                    aHolder.mPrefAvgQtyTv.isFocusable = false
                    aHolder.mPrefAvgQtyTv.isClickable = false
                    aHolder.mPrefAvgQtyTv.isFocusable = false
                }
                else {
                    Log.e("prod_cat2",prod_cat);
                    aHolder.mPrefAvgQtyTv.setText(prod_cat + " - " + fromwt + " to " + towt+" kgs")
                    updateprodname(position, prod_cat, fromwt, towt)
                }
            }
            else {
                Log.e("prod_cat3",prod_cat);
                aHolder.mPrefAvgQtyTv.setText(prod_cat + " - " + fromwt + " to " + towt+" kgs")
                updateprodname(position, prod_cat, fromwt, towt)
            }
        }
        else
        {
            aHolder.txt_prod_detls_1.visibility=View.GONE
            aHolder.mPrefAvgQtyTv.visibility=View.GONE
        }

        if (!mData.get(position).qty.isEmpty()) {
            aHolder.mQtyTv.setText(mData.get(position).qty)
            aHolder.mQtyTv.setSelection(mData.get(position).qty.length)
        } else {
            aHolder.mQtyTv.setText("")
        }
        if (!mData.get(position).avgWgt.isEmpty()) {
            aHolder.mAvgQtyTv.setText(mData.get(position).avgWgt)
            aHolder.mAvgQtyTv.setSelection(mData.get(position).avgWgt.length)
        } else {
            aHolder.mAvgQtyTv.setText("")
        }
        if(reqbty_status.equals("1"))
        {
            aHolder.mAvgQtyTv.visibility=View.VISIBLE
            aHolder.txt_tv2.visibility=View.VISIBLE
        }
        else
        {
            aHolder.mAvgQtyTv.visibility=View.GONE
            aHolder.txt_tv2.visibility=View.GONE
        }
        if (mData.get(position).mobileNo != null) {
            if (!mData.get(position).mobileNo!!.isEmpty()) {
                aHolder.mSupervisorMobTv.setText(mData.get(position).mobileNo)
                //   aHolder.mSupervisorMobTv.setSelection(mData.get(position).mobileNo.length)
            } else {
                aHolder.mSupervisorMobTv.setText("")
            }
        }
        if (mWeighmenttypeList != null) {
            if (mWeighmenttypeList.size > 0) {
                aHolder.lyt_weighmenttype.visibility = View.VISIBLE
                if(mData.get(position).weighmentType!= null )
                {
                    if(mData.get(position).weighmentType!= "-" )
                    {
                        mWeighmenttypeList.forEach {
                            if ((it.LOOKUP_CODE).equals(mData.get(position).weighmentType, true)) {

                                aHolder.weighment_type_tv.text = it.MEANING
                                return@forEach
                            }
                        }
                    }
                }
            } else {
                aHolder.lyt_weighmenttype.visibility = View.GONE
            }
        } else {
            aHolder.lyt_weighmenttype.visibility = View.GONE
        }
        aHolder.mPrefAvgQtyTv.setOnClickListener {
            //birdsextype_drop_down(aHolder.mPrefAvgQtyTv)
            aProductcatList.clear()
            diffprice.forEach {
                aProductcatList.add(it.PROD_NAME+ " - " +it.LOW_WT+ " to " +it.HIGH_WT+" kgs");
            }



            AppDialogs.showSingleChoice(myContext, "Choose Product Category", aProductcatList, object : SingleChoiceAdapter.Callback {
                override fun info(pos: Int, text: String) {
                    AppDialogs.hideSingleChoice()
                    aHolder.mPrefAvgQtyTv.setText(text)
                    diffprice.forEach {
                       // aProductcatList.add(it.PRODUCT_CATEGORY+ " - " +it.LOW_WT+ " to " +it.HIGH_WT);
                        if(text.contains(it.PROD_NAME))
                        {
                            updateprodname(position,it.PROD_NAME,it.LOW_WT,it.HIGH_WT)
                        }
                    }

                }
            }, true, true)

        }
if(!PREFAVGWT.equals("0"))
{
   // aHolder.mPrefAvgQtyTv.setText(PREFAVGWT)
}
        else
{
   // aHolder.mPrefAvgQtyTv.setText("-")
}
        if (mData.get(position).supervisorCode != null) {
            if (!mData.get(position).supervisorCode!!.isEmpty()) {
                //if (!mData.get(position).supervisorName!!.isEmpty()) {
                    aHolder.mSupervisorNameTv.setText(mData.get(position).supervisorCode + " - " + mData.get(position).supervisorName)
               // }//   aHolder.mSupervisorMobTv.setSelection(mData.get(position).mobileNo.length)
            } else {
                aHolder.mSupervisorNameTv.setText("")
            }
        }



        aHolder.mQtyTv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                try {

                    updateQty(position, s.toString())
                } catch (e: Exception) {
                    updateQty(position, "0")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        aHolder.mAvgQtyTv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                try {
                    updateavgQty(position, s.toString())
                } catch (e: Exception) {
                    updateavgQty(position, "0")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })


        aHolder.mSupervisorMobTv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {

                try {
                   // updateavgQty(position, s.toString())
                    mData.get(position).mobileNo = s.toString().trim()
                } catch (e: Exception) {
                  //  updateavgQty(position, "0")
                    mData.get(position).mobileNo = "0"
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        if (position == mData.size - 1) {
            //   aHolder.mAddBranch.visibility = View.VISIBLE

            if (isViewOrAllocate.equals("1", true)) {
                if (mData.get(position).supervisorCode != null){
                    if (!mData.get(position).supervisorCode.isEmpty()) {

                        aHolder.mSubmitTV.text = myContext.getString(R.string.label_cancel_order)
                       /* if(!mParentObj.bookingType.equals("Normal")) {
                            //aHolder.mSubmitTV.visibility = View.VISIBLE
                           // aHolder.mSubmitlay.visibility = View.VISIBLE
                            aHolder.cancel_lay.visibility = View.VISIBLE
                        }
                        else
                        {
                            aHolder.mSubmitTV.visibility = View.GONE
                            aHolder.mSubmitlay.visibility = View.GONE
                            aHolder.cancel_lay.visibility = View.GONE
                        }*/
                        aHolder.cancel_lay.visibility= View.GONE
                        Log.e("c1","c1")
                    } else {
                        aHolder.mSubmitTV.text = myContext.getString(R.string.label_submit)
                        Log.e("c2","c2")
                        if(!mParentObj.bookingType.equals("Normal")) {
                            aHolder.cancel_lay.visibility = View.VISIBLE
                            Log.e("c3","c3")
                        }
                    }}else {
                    aHolder.mSubmitTV.text = myContext.getString(R.string.label_submit)
                    Log.e("c4","c4")
                    if(!mParentObj.bookingType.equals("Normal")) {
                        aHolder.cancel_lay.visibility = View.VISIBLE
                        Log.e("c5","c5")
                    }
                }
            } else {
                aHolder.mSubmitTV.text = myContext.getString(R.string.label_submit)
                aHolder.mSubmitlay.visibility = View.VISIBLE
                Log.e("c6","c6")
                if(!mParentObj.bookingType.equals("Normal")) {
                    aHolder.cancel_lay.visibility = View.VISIBLE
                    Log.e("c7","c7")
                }
            }
            if(status.equals("Y")) {
                aHolder.mSubmitlay.visibility = View.VISIBLE
                aHolder.cancel_lay.visibility = View.GONE
                Log.e("c10","c10")
                if(!mParentObj.bookingType.equals("Normal")) {
                    aHolder.cancel_lay.visibility = View.VISIBLE
                    Log.e("c11","c11")
                }
            }
            else
            {

                // aHolder.mSubmitlay.visibility = View.VISIBLE
                Log.e("c12","c12")
                aHolder.cancel_lay.visibility = View.GONE

            }
        } else {
            //    aHolder.mAddBranch.visibility = View.GONE
            aHolder.mSubmitlay.visibility = View.VISIBLE
            aHolder.cancel_lay.visibility = View.GONE
            Log.e("c8","c8")
            if(!mParentObj.bookingType.equals("Normal")) {
                aHolder.cancel_lay.visibility = View.VISIBLE
                Log.e("c9","c9")
            }
        }

        if(CUS_SMS_STATUS.equals("0"))
        {
            aHolder.txt_error1.visibility = View.GONE
            aHolder.txt_error2.visibility = View.GONE
            aHolder.resent_lay.visibility = View.GONE
        }
        else
        {
            if(mData.get(position).CUS_SMS_VALD_STATUS.equals("0")) {
                if (mData.get(position).supervisorCode != null) {
                    if (!mData.get(position).supervisorCode!!.isEmpty()) {
                        aHolder.txt_error1.visibility = View.VISIBLE
                        aHolder.txt_error2.visibility = View.GONE
                        aHolder.resent_lay.visibility = View.GONE
                    }
                }
                else
                {
                    aHolder.txt_error1.visibility = View.GONE
                    aHolder.txt_error2.visibility = View.GONE
                    aHolder.resent_lay.visibility = View.GONE
                }
            }

            else
            {
                aHolder.txt_error1.visibility = View.GONE
                aHolder.txt_error2.visibility = View.VISIBLE
                aHolder.resent_lay.visibility = View.VISIBLE
            }
        }

        aHolder.mAddBranch.setOnClickListener {
            addNewBranch()
        }

        aHolder.mSubmitlay.setOnClickListener {
            if (aHolder.mSubmitTV.text.equals(myContext.getString(R.string.label_submit))) {
                var totalCount = 0.0

                val items: ArrayList<FarmAllocationModel.OrderDetails.OrderRows> = getItems()

                items.forEach {

                    if (!it.qty.isEmpty()) {
                      //  Log.e("it.qty.toDouble()", it.qty.toDouble().toString());
                        totalCount = totalCount+it.qty.toDouble()
                    }
                }
                Log.e("totalCount",totalCount.toString());
                Log.e("Double",mParentObj.totalQty.toString());

                if (totalCount <= mParentObj.totalQty.toDouble()) {
                    if (Utility.isInternetAvailable(myContext)) {
                        try {


                        if(aHolder.mSupervisorMobTv.text.toString().trim().equals("0") || aHolder.mSupervisorMobTv.text.toString().trim().equals(""))
                        {
                            AppDialogs.customOkAction(myContext, "Alert", "Please enter supervisor mobile number", "Ok", object : AppDialogs.ConfirmListener {
                                override fun yes() {

                                }
                            }, false)
                        }
                        else if(aHolder.mSupervisorMobTv.text.toString().trim().length != 10 && getLoginUser(myContext).ledgerId.equals("2025"))
                        {
                            AppDialogs.customOkAction(myContext, "Alert", "Please enter valid supervisor mobile number", "Ok", object : AppDialogs.ConfirmListener {
                                override fun yes() {

                                }
                            }, false)
                        }
                        else if(!(aHolder.mSupervisorMobTv.text.toString().trim().length >= 10 && aHolder.mSupervisorMobTv.text.toString().trim().length <= 13) && getLoginUser(myContext).ledgerId.equals("2022"))
                        {
                            AppDialogs.customOkAction(myContext, "Alert", "Please enter valid supervisor mobile number-BD", "Ok", object : AppDialogs.ConfirmListener {
                                override fun yes() {

                                }
                            }, false)
                        }
                        else {
                           // mData.get(position).mobileNo = aHolder.mSupervisorMobTv?.text.toString().trim()
                            adapter.confirmSubmit(mData, pos)
                        }
                        }
                        catch (e: Exception)
                        {
                            Toast.makeText(myContext,e.message,Toast.LENGTH_LONG).show()
                        }
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

            } else if (aHolder.mSubmitTV.text.equals(myContext.getString(R.string.label_cancel_order))) {
                if (Utility.isInternetAvailable(myContext)) {
                    selectedPosition = position
                   cancelreasonemp(mParentObj.headerID, mData.get(position).detaillineId)
                  //  adapter.cancelOrder(mParentObj.headerID,mData.get(position).lineId,mData.get(position).detaillineId)
                } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                    override fun yes() {

                    }
                }, false)


            }
        }
        aHolder.cancel_lay.setOnClickListener {
            if (Utility.isInternetAvailable(myContext)) {
                selectedPosition = position
                cancelreasonemp(mParentObj.headerID,mData.get(position).detaillineId)
                // adapter.cancelOrder(mParentObj.headerID,mData.get(position).lineId,mData.get(position).detaillineId)
            } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
        }
        aHolder.mDeleteImg.setOnClickListener {

            selectedPosition = position
            AppDialogs.customDoubleAction(myContext, "Delete ?", "Are you sure want to delete this line ?", "Yes",
                    "cancel", object : AppDialogs.OptionListener {
                override fun no() {

                }

                override fun yes() {
                    if (mData.get(position).detaillineId.isNotEmpty() && !(mData.get(position).detaillineId.equals("0"))) {

                        if (Utility.isInternetAvailable(myContext)) {
                            deleteLine(mData.get(position).detaillineId)

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
        aHolder.resent_lay.setOnClickListener {

            selectedPosition = position
            AppDialogs.customDoubleAction(myContext, "Delete ?", "Are you sure want to resent OTP SMS ?", "Yes",
                    "cancel", object : AppDialogs.OptionListener {
                override fun no() {

                }

                override fun yes() {
                    if (!(CUS_SMS_STATUS.equals("0")) && !(mData.get(position).CUS_SMS_VALD_STATUS.equals("0"))) {

                        if (Utility.isInternetAvailable(myContext)) {
                            resentLine(mData.get(position).lineId,mData.get(position).detaillineId)

                        } else AppDialogs.customOkAction(myContext, "Alert", "Please check the internet", "Ok", object : AppDialogs.ConfirmListener {
                            override fun yes() {

                            }
                        }, false)

                    } else {
                        //addDeleteBranch(position)
                    }
                }


            }, true, false)

        }
        aHolder.mBranchNameTv.setOnClickListener {
            aBranchNameList.clear()
            mBranchList.forEach {
                //if (!mSelectedBranch.contains(it.FARM_CODE +" - " +it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT))
                    aBranchNameList.add(it.FARM_CODE +" - " +it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE)
            }
            if (mBranchList.size > 0) {
                AppDialogs.showSingleChoice(myContext, "Choose Farm", aBranchNameList, object : SingleChoiceAdapter.Callback {
                    override fun info(pos: Int, text: String) {
                        AppDialogs.hideSingleChoice()
                        aHolder.mBranchNameTv.text = text
                        mBranchList.forEach {
                            if ((it.FARM_CODE +" - " +it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE).equals(text, true)) {
                                //if (!mSelectedBranch.contains(it.FARM_CODE +" - " +it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT))
                                    mSelectedBranch.add(it.FARM_CODE +" - " +it.FARM_DESC+" - Bird Stock: "+ it.FARM_STOCK+" - Approx. wt: "+ it.BODY_WT+" - Age: "+ it.MEAN_AGE)
                                updateBranch(position, it.FARM_CODE)
                                return@forEach
                            }
                        }

                    }
                }, true, true)
            } else AppDialogs.customOkAction(myContext, "Alert", "Farm not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
        }

        aHolder.mSupervisorNameTv.setOnClickListener {
            aSupervisorNameList.clear()
            mSupervosirList.forEach {
                aSupervisorNameList.add(it.EMP_NO +" - " +it.EMP_NAME)
            }
            if (mSupervosirList.size > 0) {
                AppDialogs.showSingleChoice(myContext, "Choose Supervisor", aSupervisorNameList, object : SingleChoiceAdapter.Callback {
                    override fun info(pos: Int, text: String) {
                        AppDialogs.hideSingleChoice()
                        aHolder.mSupervisorNameTv.text = text
                        mSupervosirList.forEach {
                            if ((it.EMP_NO +" - " +it.EMP_NAME).equals(text, true)) {
//                                if (!mSelectedBranch.contains(it.FARM_CODE +" - " +it.FARM_DESC))
//                                    mSelectedBranch.add(it.FARM_CODE +" - " +it.FARM_DESC)
//                                updateBranch(position, it.FARM_CODE)
                                aHolder.mSupervisorMobTv.text = it.MOBILENO
                                updateSupervisor(position,it.EMP_NO, it.MOBILENO, it.EMP_NAME)
                                return@forEach
                            }
                        }

                    }
                }, true, true)
            } else AppDialogs.customOkAction(myContext, "Alert", "Supervisor not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
        }

        aHolder.weighment_type_tv.setOnClickListener {
            aWeigmentList.clear()
            mWeighmenttypeList.forEach {

                aWeigmentList.add(it.MEANING)
            }
            if (aWeigmentList.size > 0) {
                AppDialogs.showSingleChoice(myContext, "Choose Weighment Type", aWeigmentList, object : SingleChoiceAdapter.Callback {
                    override fun info(pos: Int, text: String) {
                        AppDialogs.hideSingleChoice()
                        aHolder.weighment_type_tv.text = text
                        mWeighmenttypeList.forEach {
                            if ((it.MEANING).equals(text, true)) {

                                aHolder.weighment_type_tv.text = it.MEANING
                                updateWeighmenttype(position,it.LOOKUP_CODE)
                                return@forEach
                            }
                        }

                    }
                }, true, true)
            } else AppDialogs.customOkAction(myContext, "Alert", "Weighment Type not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
        }

        if (isViewOrAllocate.equals("1") ) {

            aHolder.mDeleteImg.visibility = View.INVISIBLE
            aHolder.mBranchNameTv.isClickable = false
            aHolder.mQtyTv.isFocusable = false
            aHolder.mQtyTv.isClickable = false
            aHolder.mQtyTv.isFocusable = false

            aHolder.mPrefAvgQtyTv.isFocusable = false
            aHolder.mPrefAvgQtyTv.isClickable = false
            aHolder.mPrefAvgQtyTv.isFocusable = false
            if (mData.get(position).supervisorCode != null && (mData.get(position).STATUS.equals("Y"))) {
                if (!mData.get(position).supervisorCode.isEmpty() && (mData.get(position).STATUS.equals("Y"))) {
                    aHolder.mSupervisorMobTv.isFocusable = false
                    aHolder.mSupervisorMobTv.isClickable = false
                    aHolder.mSupervisorMobTv.isFocusable = false

                    aHolder.mSupervisorNameTv.isFocusable = false
                    aHolder.mSupervisorNameTv.isClickable = false
                    aHolder.mSupervisorNameTv.isFocusable = false

                    aHolder.mAvgQtyTv.isFocusable = false
                    aHolder.mAvgQtyTv.isClickable = false
                    aHolder.mAvgQtyTv.isFocusable = false

                    aHolder.weighment_type_tv.isFocusable = false
                    aHolder.weighment_type_tv.isClickable = false
                    aHolder.weighment_type_tv.isFocusable = false

                    aHolder.mPrefAvgQtyTv.isFocusable = false
                    aHolder.mPrefAvgQtyTv.isClickable = false
                    aHolder.mPrefAvgQtyTv.isFocusable = false
                }
        }
        } else {
            if (mData.get(position).supervisorCode != null && (mData.get(position).STATUS.equals("Y"))) {
                if (!mData.get(position).supervisorCode.isEmpty() && (mData.get(position).STATUS.equals("Y"))) {

                    aHolder.mDeleteImg.visibility = View.INVISIBLE
                    aHolder.mBranchNameTv.isClickable = false
                    aHolder.mQtyTv.isFocusable = false
                    aHolder.mQtyTv.isClickable = false
                    aHolder.mQtyTv.isFocusable = false

                    aHolder.mSupervisorMobTv.isFocusable = false
                    aHolder.mSupervisorMobTv.isClickable = false
                    aHolder.mSupervisorMobTv.isFocusable = false

                    aHolder.mSupervisorNameTv.isFocusable = false
                    aHolder.mSupervisorNameTv.isClickable = false
                    aHolder.mSupervisorNameTv.isFocusable = false

                    aHolder.mAvgQtyTv.isFocusable = false
                    aHolder.mAvgQtyTv.isClickable = false
                    aHolder.mAvgQtyTv.isFocusable = false

                    aHolder.weighment_type_tv.isFocusable = false
                    aHolder.weighment_type_tv.isClickable = false
                    aHolder.weighment_type_tv.isFocusable = false

                    aHolder.mPrefAvgQtyTv.isFocusable = false
                    aHolder.mPrefAvgQtyTv.isClickable = false
                    aHolder.mPrefAvgQtyTv.isFocusable = false

                        aHolder.mSubmitlay.visibility = View.GONE
                        aHolder.cancel_lay.visibility = View.GONE

                }
            }
            if(!mParentObj.bookingType.equals("Normal")) {
                aHolder.mDeleteImg.visibility = View.INVISIBLE
                aHolder.mBranchNameTv.isClickable = false
                aHolder.mQtyTv.isFocusable = false
                aHolder.mQtyTv.isClickable = false
                aHolder.mQtyTv.isFocusable = false

            }

            aHolder.mPrefAvgQtyTv.isFocusable = false
            aHolder.mPrefAvgQtyTv.isClickable = false
            aHolder.mPrefAvgQtyTv.isFocusable = false
        }

        if(mData.get(position).submittemp != 0){
            aHolder.txt_error1.visibility = View.GONE
            aHolder.txt_error2.visibility = View.GONE
            aHolder.resent_lay.visibility = View.GONE

            Log.e("c14","c14")
        }
        //aHolder.setIsRecyclable(false);
        if (mData.get(position).supervisorCode != null && (mData.get(position).STATUS.equals("Y"))){
            if (!mData.get(position).supervisorCode.isEmpty()) {
                aHolder.mSubmitTV.text = myContext.getString(R.string.label_cancel_order)

                    //aHolder.mSubmitTV.visibility = View.GONE
                    //aHolder.mSubmitlay.visibility = View.GONE

                Log.e("c15","c15")
            }
        }
        else
        {
            aHolder.mSubmitTV.text = myContext.getString(R.string.label_submit)
            Log.e("c16","c16")}

        if (isFarmPriceBooking()) {
            aHolder.mEditFarmImg.visibility = View.VISIBLE
            aHolder.mDeleteImg.visibility = View.GONE
        } else {
            aHolder.mEditFarmImg.visibility = View.GONE
        }
        aHolder.mEditFarmImg.setOnClickListener {
            selectedPosition = position
            promptChangeBookedFarm(position, aHolder)
        }

    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {
        val mDeleteImg: ImageView
        val mEditFarmImg: ImageView
        val mBranchNameTv: TextView
        val mSubmitTV: TextView
        val mQtyTv: EditText
        val mAddBranch: LinearLayout
        val mSubmitlay: LinearLayout
        val vehicle_name_tv: TextView
        val mSupervisorNameTv: TextView
        val mSupervisorMobTv: TextView
        val mAvgQtyTv: EditText
        val resent_lay: LinearLayout
        val txt_error1: TextView
        val txt_error2: TextView
        val mPrefAvgQtyTv: EditText
        val txt_tv2: TextView
        val lyt_weighmenttype:LinearLayout
        val weighment_type_tv: TextView
        val txt_prod_detls_1:TextView
        val cancel_lay:LinearLayout

        init {

            mQtyTv = aView.findViewById<View>(R.id.qty_tv) as EditText
            mAvgQtyTv = aView.findViewById<View>(R.id.qty_tv2) as EditText
            mPrefAvgQtyTv= aView.findViewById<View>(R.id.qty_tv3) as EditText
            mSubmitTV = aView.findViewById<View>(R.id.submit_tv) as TextView
            mBranchNameTv = aView.findViewById<View>(R.id.branch_name_tv) as TextView
            mDeleteImg = aView.findViewById<View>(R.id.delete_img) as ImageView
            mEditFarmImg = aView.findViewById<View>(R.id.edit_farm_img) as ImageView
            mAddBranch = aView.findViewById<View>(R.id.add_new_branch_lay) as LinearLayout
            mSubmitlay = aView.findViewById<View>(R.id.submit_lay) as LinearLayout
            vehicle_name_tv= aView.findViewById<View>(R.id.vehicle_name_tv) as TextView
            mSupervisorNameTv= aView.findViewById<View>(R.id.emp_name_tv) as TextView
            mSupervisorMobTv= aView.findViewById<View>(R.id.mob_tv) as TextView
            resent_lay = aView.findViewById<View>(R.id.resent_lay) as LinearLayout
            txt_error1= aView.findViewById<View>(R.id.txt_error1) as TextView
            txt_error2= aView.findViewById<View>(R.id.txt_error2) as TextView
            txt_tv2= aView.findViewById<View>(R.id.txt_tv2) as TextView
            lyt_weighmenttype=aView.findViewById<View>(R.id.lyt_weighmenttype) as LinearLayout
            weighment_type_tv= aView.findViewById<View>(R.id.weighment_type_tv) as TextView
            txt_prod_detls_1= aView.findViewById<View>(R.id.txt_prod_detls_1) as TextView
            cancel_lay=aView.findViewById<View>(R.id.cancel_lay) as LinearLayout

        }
    }

    fun addNewBranch() {
        val aNewBranch = FarmAllocationModel.OrderDetails().OrderRows()
        mData.add(aNewBranch)
        notifyDataSetChanged()
    }

    fun addDeleteBranch(position: Int) {
        mBranchList.forEach {
            if (it.FARM_CODE.equals(mData.get(position).branchCode)) {
                mSelectedBranch.remove(it.FARM_DESC)
                return@forEach
            }
        }
        mData.removeAt(position)
        notifyDataSetChanged()

    }


    fun getItems(): ArrayList<FarmAllocationModel.OrderDetails.OrderRows> {

        return mData
    }


    fun updateQty(position: Int, count: String) {

        mData.get(position).qty = count
        mData.get(position).submittemp = 1
    }
    fun updateavgQty(position: Int, count: String) {

        mData.get(position).avgWgt = count
        mData.get(position).submittemp = 1
    }
    fun updateBranch(position: Int, branchCode: String) {

        mData.get(position).branchCode = branchCode
        mData.get(position).submittemp = 1
    }

    fun updateSupervisor(position: Int, supercode: String, mobno: String, name: String) {

        mData.get(position).supervisorCode = supercode
        mData.get(position).mobileNo = mobno
        mData.get(position).supervisorName = name
        mData.get(position).submittemp = 1
    }

    private fun isFarmPriceBooking(): Boolean {
        return mParentObj.bookingType.contains("FarmPrice", ignoreCase = true)
    }

    private fun isWsAllocated(row: FarmAllocationModel.OrderDetails.OrderRows): Boolean {
        val code = row.supervisorCode?.trim().orEmpty()
        return code.isNotEmpty() && code != "-" && code != "0"
    }

    private fun farmDisplayName(farm: FarmAllocationModel.Branches): String {
        return farm.FARM_CODE + " - " + farm.FARM_DESC + " - Bird Stock: " + farm.FARM_STOCK + " - Approx. wt: " + farm.BODY_WT + " - Age: " + farm.MEAN_AGE
    }

    private fun promptChangeBookedFarm(position: Int, aHolder: ViewHolder) {
        AppDialogs.customDoubleAction(
            myContext,
            "",
            myContext.getString(R.string.msg_change_booked_farm),
            "Yes",
            "No",
            object : AppDialogs.OptionListener {
                override fun no() {
                }

                override fun yes() {
                    if (Utility.isInternetAvailable(myContext)) {
                        adapter.fetchBranchFarmPrice { farms ->
                            showFarmPriceChoice(position, aHolder, farms)
                        }
                    } else AppDialogs.customOkAction(
                        myContext,
                        "Alert",
                        "Please check the internet",
                        "Ok",
                        object : AppDialogs.ConfirmListener {
                            override fun yes() {
                            }
                        },
                        false
                    )
                }
            },
            true,
            false
        )
    }

    private fun showFarmPriceChoice(
        position: Int,
        aHolder: ViewHolder,
        farms: ArrayList<FarmAllocationModel.Branches>
    ) {
        val farmNames = arrayListOf<String>()
        farms.forEach {
            farmNames.add(farmDisplayName(it))
        }
        AppDialogs.showSingleChoice(myContext, "Choose Farm", farmNames, object : SingleChoiceAdapter.Callback {
            override fun info(pos: Int, text: String) {
                AppDialogs.hideSingleChoice()
                var selectedFarm: FarmAllocationModel.Branches? = null
                farms.forEach {
                    if (farmDisplayName(it).equals(text, true)) {
                        selectedFarm = it
                        return@forEach
                    }
                }
                if (selectedFarm == null) {
                    AppDialogs.showToastshort(myContext, "Please select farm")
                    return
                }
                aHolder.mBranchNameTv.text = text
                updateBranch(position, selectedFarm!!.FARM_CODE)
                val row = mData.get(position)
                if (isWsAllocated(row)) {
                    aHolder.mSupervisorNameTv.isClickable = false
                    aHolder.mSupervisorNameTv.isFocusable = false
                    aHolder.mSupervisorMobTv.isEnabled = false
                    aHolder.mSupervisorMobTv.isClickable = false
                    aHolder.mSupervisorMobTv.isFocusable = false
                    val items = adapter.buildSubmitItemsForRow(row, this@FarmAllocationRecyclerSubAdapter.pos)
                    adapter.cancelOrderreason(
                        mParentObj.headerID,
                        "Change Farm",
                        row.detaillineId,
                        items,
                        true
                    )
                } else {
                    promptWsThenInsertFarmPrice(position, aHolder)
                }
            }
        }, true, true)
    }

    private fun promptWsThenInsertFarmPrice(position: Int, aHolder: ViewHolder) {
        aSupervisorNameList.clear()
        mSupervosirList.forEach {
            aSupervisorNameList.add(it.EMP_NO + " - " + it.EMP_NAME)
        }
        if (mSupervosirList.size == 0) {
            AppDialogs.customOkAction(myContext, "Alert", "Supervisor not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {
                }
            }, false)
            return
        }
        AppDialogs.showSingleChoice(myContext, "Choose Supervisor", aSupervisorNameList, object : SingleChoiceAdapter.Callback {
            override fun info(pos: Int, text: String) {
                AppDialogs.hideSingleChoice()
                var selectedSup: FarmAllocationModel.Supervisor? = null
                mSupervosirList.forEach {
                    if ((it.EMP_NO + " - " + it.EMP_NAME).equals(text, true)) {
                        selectedSup = it
                        return@forEach
                    }
                }
                if (selectedSup == null) {
                    AppDialogs.showToastshort(myContext, "Please select WS name")
                    return
                }
                aHolder.mSupervisorNameTv.text = text
                aHolder.mSupervisorMobTv.setText(selectedSup!!.MOBILENO)
                updateSupervisor(position, selectedSup!!.EMP_NO, selectedSup!!.MOBILENO, selectedSup!!.EMP_NAME)
                val mobile = aHolder.mSupervisorMobTv.text.toString().trim()
                if (mobile.isEmpty() || mobile == "0") {
                    AppDialogs.customOkAction(myContext, "Alert", "Please enter supervisor mobile number", "Ok", object : AppDialogs.ConfirmListener {
                        override fun yes() {
                        }
                    }, false)
                    return
                }
                if (mobile.length != 10 && getLoginUser(myContext).ledgerId.equals("2025")) {
                    AppDialogs.customOkAction(myContext, "Alert", "Please enter valid supervisor mobile number", "Ok", object : AppDialogs.ConfirmListener {
                        override fun yes() {
                        }
                    }, false)
                    return
                }
                if (!(mobile.length >= 10 && mobile.length <= 13) && getLoginUser(myContext).ledgerId.equals("2022")) {
                    AppDialogs.customOkAction(myContext, "Alert", "Please enter valid supervisor mobile number-BD", "Ok", object : AppDialogs.ConfirmListener {
                        override fun yes() {
                        }
                    }, false)
                    return
                }
                val items = adapter.buildSubmitItemsForRow(mData.get(position), this@FarmAllocationRecyclerSubAdapter.pos)
                adapter.insertFarmPriceOrder(items)
            }
        }, true, true)
    }

    fun updateWeighmenttype(position: Int, type: String) {
        mData.get(position).weighmentType = type
        mData.get(position).submittemp = 1
    }
    fun updateprodname(position: Int, prod: String, lowwt: String, highwt: String) {

        mData.get(position).PROD_CATEGORY = prod
        mData.get(position).FROM_WEIGHT = lowwt
        mData.get(position).TO_WEIGHT = highwt
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
                AppServices.API.farm_allocation_line_delete,
                DeleteModel::class.java, jsonParam

        )
    }

    private fun resentLine(lineId: String,detaillineId: String) {

        try {
            AppDialogs.showProgressDialog(myContext)
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("LINEID", lineId)
        jsonParam.put("DETAILSLINEID", detaillineId)
        AppServices.execute(
                myContext, this,
                Request.Method.POST,
                AppServices.API.resent_sms,
            CanceldetailModel::class.java, jsonParam

        )
    }
    catch (e: Exception)
    {
        AppDialogs.hideProgressDialog()
    }
    }

    private fun confirmAndCancelAllocation(reason: String, resubmitItems: JSONArray? = null) {
        AppDialogs.customDoubleAction(myContext, "", "Are you sure want to Cancel this farm allocation ?", "Yes",
            "No", object : AppDialogs.OptionListener {
                override fun no() {

                }

                override fun yes() {
                    adapter.cancelOrderreason(
                        mParentObj.headerID,
                        reason,
                        mData.get(selectedPosition).detaillineId,
                        resubmitItems
                    )
                }
            }, true, false)
    }

    private fun promptNewWsNameThenCancel(reason: String) {
        if (selectedPosition < 0 || selectedPosition >= mData.size) {
            AppDialogs.showToastshort(myContext, "Unable to capture WS name")
            return
        }
        val currentWsCode = mData.get(selectedPosition).supervisorCode.trim()
        aSupervisorNameList.clear()
        mSupervosirList.forEach {
            aSupervisorNameList.add(it.EMP_NO + " - " + it.EMP_NAME)
        }
        if (mSupervosirList.size == 0) {
            AppDialogs.customOkAction(myContext, "Alert", "Supervisor not available.", "Ok", object : AppDialogs.ConfirmListener {
                override fun yes() {

                }
            }, false)
            return
        }
        AppDialogs.showSingleChoice(myContext, "Choose WS Name", aSupervisorNameList, object : SingleChoiceAdapter.Callback {
            override fun info(pos: Int, text: String) {
                AppDialogs.hideSingleChoice()
                var selectedSup: FarmAllocationModel.Supervisor? = null
                mSupervosirList.forEach {
                    if ((it.EMP_NO + " - " + it.EMP_NAME).equals(text, true)) {
                        selectedSup = it
                        return@forEach
                    }
                }
                if (selectedSup == null) {
                    AppDialogs.showToastshort(myContext, "Please select WS name")
                    return
                }
                if (selectedSup!!.EMP_NO.trim().equals(currentWsCode, true)) {
                    AppDialogs.showToastshort(myContext, "Please select a different WS name")
                    promptNewWsNameThenCancel(reason)
                    return
                }
                updateSupervisor(
                    selectedPosition,
                    selectedSup!!.EMP_NO,
                    selectedSup!!.MOBILENO,
                    selectedSup!!.EMP_NAME
                )
                val resubmitItems = adapter.buildSubmitItemsForRow(mData.get(selectedPosition), this@FarmAllocationRecyclerSubAdapter.pos)
                confirmAndCancelAllocation(reason, resubmitItems)
            }
        }, true, true)
    }

    private fun cancelreasonemp(lineId: String, detaillineId: String) {
        AppDialogs.showProgressDialog(myContext)

        try {
        val jsonParam = JSONObject()
        jsonParam.put("USERID", LocalStorageSP.getLoginUser(myContext).USERID)
        jsonParam.put("TKEY", LocalStorageSP.getLoginUser(myContext).TKEY)
        jsonParam.put("EMPNUM", LocalStorageSP.get(myContext, FeedBackActivity.Name, "")!!)
        jsonParam.put("HEADER_ID", lineId)
        jsonParam.put("DET_LINE_ID", detaillineId)

        AppServices.execute(
            myContext, this,
            Request.Method.POST,
            AppServices.API.get_cancelreasonemp,
            CancelreaionModel::class.java, jsonParam

        )
        }
        catch (e: Exception)
        {
            AppDialogs.hideProgressDialog()
        }
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()

        if (r != null) {

            if (r.requestType.hashCode() == AppServices.API.farm_allocation_line_delete.hashCode()) {
                val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    // notifyDataSetChanged()
                    AppDialogs.showToastshort(myContext, "Success")
                    addDeleteBranch(selectedPosition)
                    notifyDataSetChanged()
                } else {
                    AppDialogs.showToastshort(myContext, "Something went wrong!!!. Please try again")
                }
            }
            if (r.requestType.hashCode() == AppServices.API.resent_sms.hashCode()) {
               /* val res = (r as DeleteModel)
                if (res.data?.statusCode!!.equals("200")) {
                    // notifyDataSetChanged()
                    AppDialogs.showToastshort(myContext, "Success")
                    addDeleteBranch(selectedPosition)
                } else {
                    AppDialogs.showToastshort(myContext, "Something went wrong!!!. Please try again")
                }*/
                val res = (r as CanceldetailModel)
                if (res.data?.size!!>0) {
                    res.data?.forEach {
                        if(it.MESSAGE1!!.equals("200")) {
                            AppDialogs.showToastshort(myContext, it.MESSAGE1!!)
                        }
                        else
                        {
                            AppDialogs.showToastshort(myContext, it.MESSAGE1!!)
                        }
                    }

                } else {
                    AppDialogs.showToastshort(myContext, myContext.getString(R.string.msg_something_went_wrong))
                }
            }
            if (r.requestType.hashCode() == AppServices.API.get_cancelreasonemp.hashCode()) {
                val res = (r as CancelreaionModel)
                if (res.data?.size!! >0) {
                    // notifyDataSetChanged()

                    aCancelresionList.clear()
                    res.data?.forEach {
                        aCancelresionList.add(it.MEANING!!);
                    }
                    AppDialogs.showSingleChoice(myContext, "Choose Cancel Reason", aCancelresionList, object : SingleChoiceAdapter.Callback {
                        override fun info(pos: Int, text: String) {
                            AppDialogs.hideSingleChoice()
                            if (text.trim().equals(reasonWsWronglyAllocated, true)) {
                                promptNewWsNameThenCancel(text.trim())
                            } else {
                                confirmAndCancelAllocation(text)
                            }
                        }
                    }, true, true)
                } else {
                    if(res.STATUSCODE1.equals("113"))
                    {
                        res.MESSAGE1?.let {
                            AppDialogs.showToastshort(
                                myContext,
                                it.toString()
                            )
                        }
                    }
                    else {
                        AppDialogs.showToastshort(
                            myContext,
                            "Something went wrong!!!. Please try again"
                        )
                    }
                }
            }
        }
    }



    private class Dropdown_list_adapter(
        var context: Context,
        var arrayList: java.util.ArrayList<DropDown_list>,var popup_question_list:PopupWindow,var mPrefAvgQtyTv: EditText
    ) : BaseAdapter() {
        var inflater: LayoutInflater? = null
        override fun getCount(): Int {
            return arrayList.size
        }

        override fun getItem(position: Int): Any {
            return arrayList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val rowView = inflater!!.inflate(
                R.layout.body_condition_drop_down, null
            )
            /* val name_div = rowView
                .findViewById<View>(R.id.textView0) as TextView
            val div = arrayList[position]
            name_div.text = div.dropdown_name!!
            name_div.tag = div.dropdown_id!!
            rowView.setOnClickListener {
                mPrefAvgQtyTv.setText(name_div.text.toString())
                mPrefAvgQtyTv.setTag(name_div.tag.toString())
                popup_question_list.dismiss()
            } */
            return rowView
        }
    }
}