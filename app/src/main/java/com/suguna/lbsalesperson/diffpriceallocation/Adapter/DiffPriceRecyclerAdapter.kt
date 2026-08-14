package com.suguna.lbsalesperson.diffpriceallocation.Adapter

import android.R.attr.x
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.ICaster
import com.suguna.lbsalesperson.diffpriceallocation.DiffPriceallocation
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import java.text.DecimalFormat


class DiffPriceRecyclerAdapter(
    private val myContext: Context,
    private val mData: ArrayList<DiffdeclareModel>,
    val adapter: DiffPriceallocation
) : RecyclerView.Adapter<DiffPriceRecyclerAdapter.ViewHolder>() {

    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_diff_price_declare_list, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {

        aHolder.txt_prod_name.text = mData.get(position).PROD_NAME
        aHolder.txt_fromwt.text = mData.get(position).LOW_WT
        aHolder.txt_towt.text = mData.get(position).HIGH_WT
        aHolder.txt_fromdate.text = mData.get(position).FROM_DATE
        aHolder.txt_todate.text = mData.get(position).TO_DATE
        val threeZeroes = DecimalFormat("#0.000")
        val LOW_WT = threeZeroes.format(mData.get(position).LOW_WT!!.toFloat())
        val HIGH_WT = threeZeroes.format(mData.get(position).HIGH_WT!!.toFloat())
        aHolder.txt_fromwt.text =LOW_WT
       aHolder.txt_towt.text =HIGH_WT
        aHolder.lyt_prod_name.setOnClickListener(View.OnClickListener {
            if(mData.get(position).EXPIRY.equals("1")) {
                adapter.call(position+1)
            }
            else
            {
                ICaster.Toast_msg_bottom(
                    myContext,
                    "Already category expired ", Toast.LENGTH_SHORT,
                    ICaster.notify
                )
            }
        })
        aHolder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return mData.size
    }




    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val txt_prod_name: TextView
        val txt_fromwt: TextView
        val txt_towt: TextView
        val txt_fromdate: TextView
        val txt_todate: TextView
        val lyt_prod_name: LinearLayout

        init {

            txt_prod_name = aView.findViewById<View>(R.id.txt_prod_name) as TextView
            txt_fromwt = aView.findViewById<View>(R.id.txt_fromwt) as TextView
            txt_towt = aView.findViewById<View>(R.id.txt_towt) as TextView
            txt_fromdate = aView.findViewById<View>(R.id.txt_fromdate) as TextView
            txt_todate = aView.findViewById<View>(R.id.txt_todate) as TextView
            lyt_prod_name = aView.findViewById<View>(R.id.lyt_prod_name) as LinearLayout



        }
    }









}