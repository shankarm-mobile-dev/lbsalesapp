package com.suguna.lbsalesperson.customerclub.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.customerclub.model.ClubModel
import com.suguna.lbsalesperson.listener.ClickListener


class CustomerClubAdapter(
        private val myContext: Context,
        private val mData: ArrayList<ClubModel>,
        private var mClickList: ClickListener
) : RecyclerView.Adapter<CustomerClubAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_customer_club_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {
        val data = mData.get(position)
        aHolder.day.text = data.club.toString()
        aHolder.mAchievedpercent.text = data.count
        if (position % 2 == 0) {
            aHolder.mRootLay.setBackgroundColor(Color.parseColor("#ededed"))
        }
        aHolder.mRootLay.setOnClickListener {
            mClickList.click(data.club!!)
        }
/*
        if(position == mData.size-1){
            aHolder.mTargetQty.setTypeface( null,Typeface.BOLD)
            aHolder.mAchievedQty.setTypeface( null,Typeface.BOLD)
            aHolder.mAchievedpercent.setTypeface( null,Typeface.BOLD)
            aHolder.day.setTypeface( null,Typeface.BOLD)
        }else{
            aHolder.mTargetQty.setTypeface( null,Typeface.NORMAL)
            aHolder.mAchievedQty.setTypeface( null,Typeface.NORMAL)
            aHolder.mAchievedpercent.setTypeface( null,Typeface.NORMAL)
            aHolder.day.setTypeface( null,Typeface.NORMAL)
        }*/
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
        val mRootLay: LinearLayout


        init {
            mRootLay = aView.findViewById<View>(R.id.root_lay) as LinearLayout
            day = aView.findViewById<View>(R.id.days_tv) as TextView
            mTargetQty = aView.findViewById<View>(R.id.target_qty_tv) as TextView
            mAchievedQty = aView.findViewById<View>(R.id.achieved_qty_tv) as TextView
            mAchievedpercent = aView.findViewById<View>(R.id.achieved_percent_tv) as TextView

        }
    }
}