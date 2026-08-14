package com.suguna.lbsalesperson.targetActual.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.listener.ClickListener
import com.suguna.lbsalesperson.targetActual.model.TargetVsActualModel
import java.math.BigDecimal
import java.math.RoundingMode


class TargetVsActualAdapter(
        private val myContext: Context,
        private val mData: ArrayList<TargetVsActualModel>,
        private var mClickList: ClickListener
) : RecyclerView.Adapter<TargetVsActualAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_target_vs_actual_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {
        if (position % 2 == 0) {
            aHolder.mRootlay.setBackgroundColor(Color.parseColor("#ededed"))
        }
        if (position == mData.size - 1) {
            aHolder.mRootlay.setBackgroundColor(myContext.resources.getColor(R.color.colorAccent))
            aHolder.mTargetQty.setTextColor(myContext.resources.getColor(R.color.white))
            aHolder.mAchievedQty.setTextColor(myContext.resources.getColor(R.color.white))
            aHolder.mAchievedpercent.setTextColor(myContext.resources.getColor(R.color.white))
            aHolder.day.setTextColor(myContext.resources.getColor(R.color.white))
        }
        val data = mData.get(position)
        //aHolder.mTargetQty.text = data.targetQty.toString()
        aHolder.mTargetQty.text = BigDecimal(data.targetQty!!).setScale(2, RoundingMode.HALF_EVEN).toString()
        aHolder.mAchievedQty.text = BigDecimal(data.salesKGS!!).setScale(2, RoundingMode.HALF_EVEN).toString()
        aHolder.mAchievedpercent.text = BigDecimal(data.achievedPercent!!).setScale(2, RoundingMode.HALF_EVEN).toString()
        // aHolder.mAchievedpercent.text = data.achievedPercent.toString()
        aHolder.day.text = data.day

        if (position == mData.size - 1) {
            aHolder.mTargetQty.setTypeface(null, Typeface.BOLD)
            aHolder.mAchievedQty.setTypeface(null, Typeface.BOLD)
            aHolder.mAchievedpercent.setTypeface(null, Typeface.BOLD)
            aHolder.day.setTypeface(null, Typeface.BOLD)
        } else {
            aHolder.mTargetQty.setTypeface(null, Typeface.NORMAL)
            aHolder.mAchievedQty.setTypeface(null, Typeface.NORMAL)
            aHolder.mAchievedpercent.setTypeface(null, Typeface.NORMAL)
            aHolder.day.setTypeface(null, Typeface.NORMAL)
        }


    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {

        val mTargetQty: TextView
        val mAchievedQty: TextView
        val mAchievedpercent: TextView
        val day: TextView
        val mRootlay: LinearLayout


        init {
            day = aView.findViewById<View>(R.id.days_tv) as TextView
            mTargetQty = aView.findViewById<View>(R.id.target_qty_tv) as TextView
            mAchievedQty = aView.findViewById<View>(R.id.achieved_qty_tv) as TextView
            mAchievedpercent = aView.findViewById<View>(R.id.achieved_percent_tv) as TextView
            mRootlay = aView.findViewById<View>(R.id.root_lay) as LinearLayout

        }
    }
}