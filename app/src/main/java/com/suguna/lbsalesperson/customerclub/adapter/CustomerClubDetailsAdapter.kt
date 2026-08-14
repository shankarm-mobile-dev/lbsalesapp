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
import com.suguna.lbsalesperson.customerclub.model.CustomerClubDetailModel
import com.suguna.lbsalesperson.listener.ClickListener


class CustomerClubDetailsAdapter(
        private val myContext: Context,
        private val mData: ArrayList<CustomerClubDetailModel.ClubDetails>,
        private var mClickList: ClickListener
) : RecyclerView.Adapter<CustomerClubDetailsAdapter.ViewHolder>() {

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_customer_club_detail_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {
        val data = mData.get(position)
        aHolder.club.text = data.club
        aHolder.currentClub.text = data.currentYearClub
        aHolder.customerCode.text = data.customerCode
        aHolder.customerName.text = data.customerName

        if (position % 2 == 0) {
            aHolder.mRootlay.setBackgroundColor(Color.parseColor("#ededed"))
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {

        val club: TextView
        val customerCode: TextView
        val currentClub: TextView
        val customerName: TextView
        val mRootlay: LinearLayout


        init {
            mRootlay = aView.findViewById<View>(R.id.rootLay) as LinearLayout
            customerName = aView.findViewById<View>(R.id.customer_name_tv) as TextView
            club = aView.findViewById<View>(R.id.club_tv) as TextView
            customerCode = aView.findViewById<View>(R.id.customer_code_tv) as TextView
            currentClub = aView.findViewById<View>(R.id.current_club_tv) as TextView

        }
    }
}