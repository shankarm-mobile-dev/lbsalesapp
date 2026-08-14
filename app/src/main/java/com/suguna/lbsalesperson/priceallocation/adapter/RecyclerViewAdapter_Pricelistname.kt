package com.suguna.lbsalesperson.priceallocation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.diffpriceallocation.Adapter.DiffPriceRecyclerAdapter
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import com.suguna.lbsalesperson.priceallocation.model.difflblist
import com.suguna.lbsalesperson.priceallocation.model.difflbprice
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet

class RecyclerViewAdapter_Pricelistname(
    private val myContext: Context,
    private val mData: MutableList<difflblist>
) : RecyclerView.Adapter<RecyclerViewAdapter_Pricelistname.ViewHolder>() {

    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_diff_price_name_list, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        position: Int
    ) {

        aHolder.txt_prod_name.text = mData.get(position).producT_CATEGORY

        aHolder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return mData.size
    }




    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val txt_prod_name: TextView



        init {

            txt_prod_name = aView.findViewById<View>(R.id.txt_prod_name) as TextView




        }
    }









}