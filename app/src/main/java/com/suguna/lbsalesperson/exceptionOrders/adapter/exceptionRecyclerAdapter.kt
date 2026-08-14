package com.suguna.lbsalesperson.exceptionOrders.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.exceptionOrders.exceptionViewActivity
import com.suguna.lbsalesperson.exceptionOrders.model.ExceptionOtpModel
import com.truelife.http.Response
import com.truelife.http.ResponseListener

class exceptionRecyclerAdapter (
    private val myContext: Context,
    private val mData: ArrayList<ExceptionOtpModel>
) : RecyclerView.Adapter<exceptionRecyclerAdapter.ViewHolder>(), ResponseListener {

    var currentPosition: Int = -1

    lateinit var btnShowBottomSheet: Button
    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_exception_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {

        aHolder.branch_tv.text = mData.get(position).branchName
        aHolder.customername_tv.text = mData.get(position).customerName
        aHolder.vehicleno_tv.text=mData.get(position).totalQty
        aHolder.farmerotp_tv.text=mData.get(position).lAST_5WK_AVG
        aHolder.customerotp_tv.text=mData.get(position).lAST_5WK_AVG
        aHolder.status_tv.text=mData.get(position).sTATUS
        aHolder.itemView.setOnClickListener()
        {
            getbottomsheet(mData.get(position))
        }
        if(!mData.get(position).sTATUS.equals("Waiting for confirmation"))
        {
            aHolder.status_tv.setTextColor(ContextCompat.getColor(myContext, R.color.grey_active))
        }
        aHolder.setIsRecyclable(false);
    }

    private fun getbottomsheet(data: ExceptionOtpModel) {
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
            lyt_hide.visibility=View.GONE
            approved_tv.visibility=View.VISIBLE

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
                Toast.makeText(myContext,"Enter remark",Toast.LENGTH_SHORT).show()
            }
            else
            {

                (myContext as exceptionViewActivity).update_exceptionList(
                    data.refNo,
                    "0",
                    edit_reason.text.toString().trim(),
                    "R"
                )

                dialog.dismiss()
            }
        }
        BtnApprove.setOnClickListener {
            if(edit_quantity.text.toString().trim().equals(""))
            {
                Toast.makeText(myContext,"Enter Approve Quantity",Toast.LENGTH_SHORT).show()
            }
            else if(Integer.parseInt(edit_quantity.text.toString()) <= 0)
            {
                Toast.makeText(myContext,"Enter Approve Quantity",Toast.LENGTH_SHORT).show()
            }
            else if(Integer.parseInt(edit_quantity.text.toString()) > Integer.parseInt(data.totalQty))
            {
                Toast.makeText(myContext,"Enter Approve Quantity is greater than order quantity",Toast.LENGTH_SHORT).show()
            }
            else if(edit_reason.text.toString().trim().equals(""))
            {
                Toast.makeText(myContext,"Enter remark",Toast.LENGTH_SHORT).show()
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
        val status_tv: TextView

        init {

            branch_tv = aView.findViewById<View>(R.id.branch_tv) as TextView
            customername_tv = aView.findViewById<View>(R.id.customername_tv) as TextView
            vehicleno_tv = aView.findViewById<View>(R.id.vehicleno_tv) as TextView
            farmerotp_tv = aView.findViewById<View>(R.id.farmerotp_tv) as TextView
            customerotp_tv = aView.findViewById<View>(R.id.customerotp_tv) as TextView
            status_tv = aView.findViewById<View>(R.id.status_tv) as TextView
        }
    }

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()
        if (r != null) {
        }
    }
}