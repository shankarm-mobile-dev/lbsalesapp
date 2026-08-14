package com.suguna.lbsalesperson.liftotpview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs

import com.suguna.lbsalesperson.liftotpview.model.LiftOtpModel
import com.truelife.http.Response
import com.truelife.http.ResponseListener


class liftOtpRecyclerAdapter(
    private val myContext: Context,
    private val mData: ArrayList<LiftOtpModel>
) : RecyclerView.Adapter<liftOtpRecyclerAdapter.ViewHolder>(), ResponseListener {

    var currentPosition: Int = -1

    lateinit var btnShowBottomSheet: Button
    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_liftotpview_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {

        aHolder.branch_tv.text = mData.get(position).branchName
        aHolder.customername_tv.text = mData.get(position).customerName
        aHolder.vehicleno_tv.text=mData.get(position).vehicleNum
        aHolder.farmerotp_tv.text=mData.get(position).farmerOtp
        aHolder.customerotp_tv.text=mData.get(position).customerOtp
        aHolder.itemView.setOnClickListener() {
         getbottomsheet(mData.get(position))
        }
        aHolder.setIsRecyclable(false);
    }

    private fun getbottomsheet(data: LiftOtpModel) {
            val dialog = BottomSheetDialog(myContext)
            val view = LayoutInflater.from(myContext)
                          .inflate(R.layout.inflate_liftotpview_bottom_sheet, null)
            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        val orderno_tv = view.findViewById<TextView>(R.id.orderno_tv)
        val scheduleno_tv = view.findViewById<TextView>(R.id.scheduleno_tv)
        val ordertype_tv = view.findViewById<TextView>(R.id.ordertype_tv)
        val custname_tv = view.findViewById<TextView>(R.id.custname_tv)
        val custotp_tv = view.findViewById<TextView>(R.id.custotp_tv)
        val farmername_tv = view.findViewById<TextView>(R.id.farmername_tv)
        val farmerotp_tv = view.findViewById<TextView>(R.id.farmerotp_tv)
        val supervisor_tv= view.findViewById<TextView>(R.id.supervisor_tv)
        val orderqty_tv= view.findViewById<TextView>(R.id.orderqty_tv)
        val allocatedqty_tv= view.findViewById<TextView>(R.id.allocatedqty_tv)
        orderno_tv.text = data.refNo
        scheduleno_tv.text = data.scheduleNo
        ordertype_tv.text = data.bookingType
        custname_tv.text =data.customerNumber +" - "+ data.customerName
        custotp_tv.text = data.customerOtp
        farmername_tv.text = data.farmCode +" - "+ data.farmName
        farmerotp_tv.text = data.farmerOtp
        supervisor_tv.text = data.supervisorCode +" - "+ data.supervisorName
        orderqty_tv.text = data.totalQty
        allocatedqty_tv.text = data.allocatedQty
            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()

    }

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

        init {

            branch_tv = aView.findViewById<View>(R.id.branch_tv) as TextView
            customername_tv = aView.findViewById<View>(R.id.customername_tv) as TextView
            vehicleno_tv = aView.findViewById<View>(R.id.vehicleno_tv) as TextView
            farmerotp_tv = aView.findViewById<View>(R.id.farmerotp_tv) as TextView
            customerotp_tv = aView.findViewById<View>(R.id.customerotp_tv) as TextView
        }
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()
        if (r != null) {
        }
    }
}