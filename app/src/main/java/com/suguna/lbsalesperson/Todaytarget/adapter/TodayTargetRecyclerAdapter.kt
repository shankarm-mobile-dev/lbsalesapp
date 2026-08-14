package com.suguna.lbsalesperson.Todaytarget.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Todaytarget.Todaytarget
import com.suguna.lbsalesperson.Todaytarget.model.TodayTargetdeclareModel
import com.suguna.lbsalesperson.Utils.DecimalDigitsInputFilter
import com.suguna.lbsalesperson.Utils.ICaster
import com.suguna.lbsalesperson.diffpriceallocation.Adapter.DiffPriceRecyclerAdapter
import com.suguna.lbsalesperson.diffpriceallocation.DiffPriceallocation
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import java.text.DecimalFormat

class TodayTargetRecyclerAdapter (
    private val myContext: Context,
    private val mData: ArrayList<TodayTargetdeclareModel>,
    val adapter: Todaytarget
) : RecyclerView.Adapter<TodayTargetRecyclerAdapter.ViewHolder>() {

    var isViewOrAllocate = "0"
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.inflate_today_target_list, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        aHolder: ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {

        aHolder.txt_cluster_name.text = mData.get(position).CLUSTER_DESC
        aHolder.txt_target_rate.text = mData.get(position).DECLARE_TARGET
        aHolder.txt_avg_target.text = mData.get(position).AVG_TARGET
        if(mData.get(position).ISUPDATE.equals("N"))
        {
            aHolder.lyt_target_rate.visibility=View.VISIBLE
            aHolder.txt_target_rate.visibility=View.GONE
        }
        else
        {
            aHolder.lyt_target_rate.visibility=View.GONE
            aHolder.txt_target_rate.visibility=View.VISIBLE
        }
        //aHolder.edt_target_rate!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(0))

        /*aHolder.txt_fromdate.text = mData.get(position).FROM_DATE
        aHolder.txt_todate.text = mData.get(position).TO_DATE
        val threeZeroes = DecimalFormat("#0.000")
        val LOW_WT = threeZeroes.format(mData.get(position).LOW_WT!!.toFloat())
        val HIGH_WT = threeZeroes.format(mData.get(position).HIGH_WT!!.toFloat())
        aHolder.txt_fromwt.text =LOW_WT
        aHolder.txt_towt.text =HIGH_WT*/
        /*aHolder.lyt_prod_name.setOnClickListener(View.OnClickListener {
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
        })*/

        aHolder.edt_target_rate!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                aHolder.edt_target_rate!!.isFocusable = true
                aHolder.edt_target_rate!!.isFocusableInTouchMode = true
                aHolder.edt_target_rate!!.isClickable = true
                aHolder.edt_target_rate!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                if(arg0.toString().length>0)
                {
                  val value = arg0.toString().toInt()
                        if(value<=0)
                        {
                            aHolder.edt_target_rate!!.setText("")
                        }
                }
                mData.get(position).DECLARE_TARGET = arg0.toString()
            }
        })
        aHolder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return mData.size
    }




    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val txt_cluster_name: TextView
        val txt_target_rate: TextView
        val txt_avg_target: TextView
        val edt_target_rate:EditText
        val lyt_target_rate:LinearLayout
        init {

            txt_cluster_name = aView.findViewById<View>(R.id.txt_cluster_name) as TextView
            txt_target_rate = aView.findViewById<View>(R.id.txt_target_rate) as TextView
            txt_avg_target = aView.findViewById<View>(R.id.txt_avg_target) as TextView

            edt_target_rate= aView.findViewById<View>(R.id.edt_target_rate) as EditText
            lyt_target_rate= aView.findViewById<View>(R.id.lyt_target_rate) as LinearLayout

        }
    }









}