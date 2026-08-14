package com.suguna.lbsalesperson.priceallocation.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.*
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.priceallocation.DataAccessManager
import com.suguna.lbsalesperson.priceallocation.SpanningLinearLayoutManager
import com.suguna.lbsalesperson.priceallocation.model.*

class DiffProvPriceRecyclerAdapter (
    private val myContext: Context,
    private val mData: MutableList<difflblist>,
    private val Editable: Boolean
) : RecyclerView.Adapter<DiffProvPriceRecyclerAdapter.ViewHolder>() {

    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_diff_price_name_editext, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {



        aHolder.edtrate.setTag(mData.get(position).producT_CATEGORY)
        aHolder.edtrate!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        aHolder.setIsRecyclable(false);

        if(!Editable)
        {
            aHolder.edtrate!!.setKeyListener(null);
            aHolder.edtrate!!.visibility=View.VISIBLE
            aHolder.txtrate!!.visibility=View.GONE
            aHolder.edtrate!!.setKeyListener(null);
        }
        else
        {
            aHolder.edtrate!!.visibility=View.VISIBLE
            aHolder.txtrate!!.visibility=View.GONE
        }
        val dataRate = mData[position].proV_PRICE
        if (dataRate.length > 0) {
            try {
                val rateValue = mData[position].proV_PRICE

                if(!rateValue.equals("0")) {
                    if(mData[position].proV_STATUS.equals("Y") || mData[position].status.equals("Y") ) {

                        aHolder.edtrate!!.setText(rateValue)
                        // val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                        //formatter.maximumFractionDigits = 4
                        //val moneyString = formatter.format(rateValue)
                        aHolder.txtrate!!.text = rateValue
                        // aHolder.txtrate!!.setText(rateValue)
                        aHolder.edtrate!!.setKeyListener(null);
                    }

                    else {
                        aHolder.edtrate!!.setText(rateValue)
                        aHolder.txtrate!!.setText(rateValue)
                    }
                }
                else {
                    if(mData[position].proV_STATUS.equals("Y") || mData[position].status.equals("Y") ) {


                        aHolder.edtrate!!.setKeyListener(null);
                    }

                    aHolder.edtrate!!.setText("")

                    aHolder.txtrate!!.setText(rateValue)

                }
            }
            catch (e: Exception){
                Log.e("error",e.toString())
            }
        }else {
            aHolder.edtrate!!.setText("")
            aHolder.txtrate!!.setText("0")


        }

        if(mData.get(position).producT_CATEGORY.equals("COBB"))
        {
             aHolder.edtrate!!.visibility=View.INVISIBLE
        }
        if(mData.get(position).producT_CATEGORY.equals("COBB Wt."))
        {
            aHolder.edtrate!!.visibility=View.INVISIBLE
        }

        aHolder.edtrate!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                aHolder.edtrate!!.isFocusable = true
                aHolder.edtrate!!.isFocusableInTouchMode = true
                aHolder.edtrate!!.isClickable = true
                aHolder.edtrate!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                mData[position].proV_PRICE = arg0.toString()
            }
        })

        aHolder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return mData.size
    }




    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val edtrate: EditText

        val txtrate:TextView


        init {

            edtrate = aView.findViewById<View>(R.id.edit_price) as EditText

            txtrate =aView.findViewById<View>(R.id.tvrate) as TextView


        }
    }









}