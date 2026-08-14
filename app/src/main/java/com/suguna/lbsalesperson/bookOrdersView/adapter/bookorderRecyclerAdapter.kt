package com.suguna.lbsalesperson.bookOrdersView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R

import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.bookOrdersView.bookOrderViewActivity
import com.suguna.lbsalesperson.bookOrdersView.model.BookOrderOtpModel
import com.truelife.http.Response
import com.truelife.http.ResponseListener

class bookorderRecyclerAdapter (
    private val myContext: Context,
    private val mData: ArrayList<BookOrderOtpModel>
) : RecyclerView.Adapter<bookorderRecyclerAdapter.ViewHolder>(), ResponseListener {

    var currentPosition: Int = -1

    lateinit var btnShowBottomSheet: Button
    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_bookorderview_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {

        aHolder.branch_tv.text = mData.get(position).locationName
        aHolder.customername_tv.text = mData.get(position).availableKg
        aHolder.vehicleno_tv.text=mData.get(position).bookedKg
        aHolder.farmerotp_tv.text=mData.get(position).bookedKg
        aHolder.customerotp_tv.text=mData.get(position).bookedKgPer
        aHolder.status_tv.text=mData.get(position).closeOrder
        aHolder.itemView.setOnClickListener() {
            //getbottomsheet(mData.get(position))
        }
        if(mData.get(position).closeOrder.equals("1"))
        {
            aHolder.mySwitch.isChecked = true

        }
        if(mData.get(position).priceStop.equals("1"))
        {
            aHolder.mySwitch_price.isChecked = true

        }
        if(mData.get(position).priceDowntrent.equals("1"))
        {
            aHolder.mySwitch_pricestop.isChecked = true
          //  aHolder.mySwitch_pricestop.setChecked(true);   // or false
        //    aHolder.mySwitch_pricestop.setEnabled(false);  // lock it

        }

        aHolder.mySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ON
                aHolder.mySwitch.isChecked = false
                (myContext as bookOrderViewActivity).update_bookorderList(
                    mData.get(position).branchCode,
                    "Y",
                    mData.get(position).locationName
                )
            } else {
                // OFF
                aHolder.mySwitch.isChecked = true
                (myContext as bookOrderViewActivity).update_bookorderList(
                    mData.get(position).branchCode,
                    "N",
                    mData.get(position).locationName
                )
            }
        }

        aHolder.mySwitch_price.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // ON
                aHolder.mySwitch_price.isChecked = false
                (myContext as bookOrderViewActivity).update_pricestopList(
                    mData.get(position).branchCode,
                    "Y",
                    mData.get(position).locationName
                )
            } else {
                // OFF
                aHolder.mySwitch_price.isChecked = true
                (myContext as bookOrderViewActivity).update_pricestopList(
                    mData.get(position).branchCode,
                    "N",
                    mData.get(position).locationName
                )
            }
        }
        aHolder.mySwitch_pricestop.setOnCheckedChangeListener { _, isChecked ->
            if(mData.get(position).priceDowntrent.equals("0")) {
                if(mData.get(position).priceDeclared.equals("0")) {
                    if (isChecked) {
                        // ON
                        aHolder.mySwitch_pricestop.isChecked = false
                        (myContext as bookOrderViewActivity).update_downtrendList(
                            mData.get(position).branchCode,
                            "Y",
                            mData.get(position).locationName
                        )
                    } else {
                        // OFF
                        aHolder.mySwitch_pricestop.isChecked = true
                        (myContext as bookOrderViewActivity).update_downtrendList(
                            mData.get(position).branchCode,
                            "N",
                            mData.get(position).locationName
                        )
                    }
                }
                else
                {
                    aHolder.mySwitch_pricestop.isChecked = true
                    AppDialogs.showToastshort(myContext, "Price is already Declared to this branch.")
                }
            }
            else
            {
                aHolder.mySwitch_pricestop.isChecked = true
                AppDialogs.showToastshort(myContext, "Price Down trend is Enabled already.")
            }
        }
        aHolder.setIsRecyclable(false);
    }
    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList: ArrayList<BookOrderOtpModel>) {
        mData.clear()
        mData.addAll(newList)
        notifyDataSetChanged()
    }

   /* private fun getbottomsheet(data: ExceptionOtpModel) {
        val dialog = BottomSheetDialog(myContext)
        val view = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_exceptionview_bottom_sheet, null)
        val BtnCancel = view.findViewById<Button>(R.id.idBtnCancel)
        val BtnApprove = view.findViewById<Button>(R.id.idBtnApprove)
        val orderno_tv = view.findViewById<TextView>(R.id.orderno_tv)
        val custname_tv = view.findViewById<TextView>(R.id.custname_tv)
        val farm_tv = view.findViewById<TextView>(R.id.farm_tv)
        val profile_tv = view.findViewById<TextView>(R.id.profile_tv)
        val custbalance_tv = view.findViewById<TextView>(R.id.custbalance_tv)
        val bodywt_tv = view.findViewById<TextView>(R.id.bodywt_tv)
        val distance_tv = view.findViewById<TextView>(R.id.distance_tv)
        val price_tv= view.findViewById<TextView>(R.id.price_tv)
        val orderqty_tv= view.findViewById<TextView>(R.id.orderqty_tv)
        val lastwk_tv= view.findViewById<TextView>(R.id.lastwk_tv)

        val exceedpct_tv= view.findViewById<TextView>(R.id.exceedpct_tv)
        val edit_quantity= view.findViewById<EditText>(R.id.edit_quantity)
        val edit_reason= view.findViewById<EditText>(R.id.edit_reason)
        val lyt_hide= view.findViewById<LinearLayout>(R.id.lyt_hide)
        val approved_tv= view.findViewById<TextView>(R.id.approved_tv)
        if(!data.sTATUS.equals("Waiting for confirmation"))
        {
            lyt_hide.visibility= View.GONE
            approved_tv.visibility= View.VISIBLE

        }
        approved_tv.text = data.aPPROVED_QTY
        orderno_tv.text = data.refNo
        custname_tv.text = data.customerNumber +" - "+ data.customerName
        farm_tv.text = data.farmCode +" - "+ data.farmName
        profile_tv.text = data.pROFILE_CLASS
        custbalance_tv.text = data.cUSTOMER_BALANCE
        bodywt_tv.text = data.aPPROX_BODY_WEIGHT
        distance_tv.text = data.dISTANCE
        price_tv.text = data.pRICE
        orderqty_tv.text = data.totalQty
        lastwk_tv.text = data.lAST_5WK_AVG
        exceedpct_tv.text=data.eXCEED_PCT
        BtnCancel.setOnClickListener {
            if(edit_reason.text.toString().trim().equals(""))
            {
                Toast.makeText(myContext,"Enter remark", Toast.LENGTH_SHORT).show()
            }
            else
            {

                (myContext as exceptionViewActivity).update_exceptionList(
                    data.refNo,
                    edit_quantity.text.toString().trim(),
                    edit_reason.text.toString().trim(),
                    "R"
                )

                dialog.dismiss()
            }
        }
        BtnApprove.setOnClickListener {
            if(edit_quantity.text.toString().trim().equals(""))
            {
                Toast.makeText(myContext,"Enter Approve Quantity", Toast.LENGTH_SHORT).show()
            }
            else if(Integer.parseInt(edit_quantity.text.toString()) <= 0)
            {
                Toast.makeText(myContext,"Enter Approve Quantity", Toast.LENGTH_SHORT).show()
            }
            else if(Integer.parseInt(edit_quantity.text.toString()) > Integer.parseInt(data.totalQty))
            {
                Toast.makeText(myContext,"Enter Approve Quantity is greater than order quantity",
                    Toast.LENGTH_SHORT).show()
            }
            else if(edit_reason.text.toString().trim().equals(""))
            {
                Toast.makeText(myContext,"Enter remark", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var status="P"
                if(edit_quantity.text.toString().trim().equals(data.totalQty))
                {
                    status="A"
                }
                (myContext as exceptionViewActivity).update_exceptionList(
                    data.refNo,
                    edit_quantity.text.toString().trim(),
                    edit_reason.text.toString().trim(),
                    status
                )
                dialog.dismiss()
            }
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()

    }*/

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val branch_tv: TextView
        val customername_tv: TextView
        val vehicleno_tv: TextView
        val farmerotp_tv: TextView
        val customerotp_tv: TextView
        val status_tv: TextView
        val mySwitch: Switch
        val mySwitch_price: Switch
        val mySwitch_pricestop: Switch

        init {

            branch_tv = aView.findViewById<View>(R.id.branch_tv) as TextView
            customername_tv = aView.findViewById<View>(R.id.customername_tv) as TextView
            vehicleno_tv = aView.findViewById<View>(R.id.vehicleno_tv) as TextView
            farmerotp_tv = aView.findViewById<View>(R.id.farmerotp_tv) as TextView
            customerotp_tv = aView.findViewById<View>(R.id.customerotp_tv) as TextView
            status_tv = aView.findViewById<View>(R.id.status_tv) as TextView
             mySwitch = aView.findViewById<View>(R.id.mySwitch) as Switch
            mySwitch_price= aView.findViewById<View>(R.id.mySwitch_price) as Switch
            mySwitch_pricestop= aView.findViewById<View>(R.id.mySwitch_pricesstop) as Switch

        }
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()
        if (r != null) {
        }
    }

}