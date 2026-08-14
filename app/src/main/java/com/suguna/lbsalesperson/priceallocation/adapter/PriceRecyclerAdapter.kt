package com.suguna.lbsalesperson.priceallocation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.DecimalDigitsInputFilter
import com.suguna.lbsalesperson.Utils.LocalStorageSP

import com.suguna.lbsalesperson.branchallocation.adapter.BranchAllocationRecyclerSubAdapter
import com.suguna.lbsalesperson.branchallocation.listener.BranchAllocationListener
import com.suguna.lbsalesperson.branchallocation.model.BranchAllocationModel
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet
import java.text.NumberFormat
import java.util.*


class PriceRecyclerAdapter (
    private val context: Context, private val resource: Int, private val item: ArrayList<lbpricedet>, private val Editable: Boolean
) : RecyclerView.Adapter<PriceRecyclerAdapter.ViewHolder>() {

    var myContext: Context = context
    var Itemlist: ArrayList<lbpricedet> = item
    var groupName: ArrayList<String> = ArrayList()
    var mEditable = Editable
    var mMenuAdapter: DiffPriceRecyclerAdapter? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.lbprice_list_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        holder.broilerhdr!!.visibility = View.GONE
        holder.retailhdr!!.visibility = View.GONE
        holder.lydetail!!.visibility = View.GONE
        holder.ref = position
        holder.ref1 = position
        holder.ref2 = position
        holder.edtrate!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        holder.edtrate!!.setText(Itemlist[position].rate)
        holder.edtrate_cobb!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        holder.edtrate_cobb_wt!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
            holder.edtrate_cobb!!.setText(Itemlist[position].cobB_PRICE)
            holder.edtrate_cobb_wt!!.setText(Itemlist[position].cobB_WT)
        }
        else
        {
            holder.edtrate_cobb!!.setText(Itemlist[position].locaL_PRICE)
            Log.e("local",Itemlist[position].locaL_PRICE);
        }

        holder.edtrate_ncobb!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        holder.edtrate_ncobb!!.setText(Itemlist[position].proV_PRICE)
        holder.lydetail!!.visibility = View.VISIBLE
        val sdr = Itemlist[position].zonE_NAME
        val region = Itemlist[position].regioncode
        if (Itemlist[position].sortinG_ORDER != 0) {
            holder.hdrZone!!.visibility = View.VISIBLE
            holder.hdrZone!!.text = sdr
            holder.broilerhdr!!.visibility = View.GONE
            holder.retailhdr!!.visibility = View.GONE
            holder.lydetail!!.visibility = View.GONE
        } else {
            holder.hdrZone!!.visibility = View.GONE
        }
        holder.tvbranchname!!.text = Itemlist[position].brancH_NAME
        if (mEditable == false) {
            holder.edtrate!!.visibility = View.GONE
            holder.edtrate_cobb!!.visibility = View.GONE
            holder.edtrate_ncobb!!.visibility = View.GONE
            holder.edtrate_cobb_wt!!.visibility = View.GONE
            holder.tvrate!!.visibility = View.VISIBLE
            holder.tvrate_copp!!.visibility = View.VISIBLE
            holder.tvrate_ncopp!!.visibility = View.VISIBLE
            holder.tvrate_copp_wt!!.visibility = View.VISIBLE // to change
        } else {
            if (Itemlist[position].status == "Y") {
                holder.edtrate!!.visibility = View.GONE
                holder.edtrate_cobb!!.visibility = View.GONE
                holder.edtrate_ncobb!!.visibility = View.GONE
                holder.edtrate_cobb_wt!!.visibility = View.GONE
                holder.tvrate!!.visibility = View.VISIBLE
                holder.tvrate_copp!!.visibility = View.VISIBLE
                holder.tvrate_ncopp!!.visibility = View.VISIBLE
                holder.tvrate_copp_wt!!.visibility = View.VISIBLE // to change
            } else {
                holder.edtrate!!.visibility = View.VISIBLE
                holder.edtrate_cobb!!.visibility = View.VISIBLE
                holder.edtrate_ncobb!!.visibility = View.VISIBLE
                holder.edtrate_cobb_wt!!.visibility = View.VISIBLE // to change
                holder.tvrate!!.visibility = View.GONE
                holder.tvrate_copp!!.visibility = View.GONE
                holder.tvrate_ncopp!!.visibility = View.GONE
                holder.tvrate_copp_wt!!.visibility = View.GONE
            }
        }
        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
            //holder.edtrate_cobb!!.visibility = View.GONE
            //holder.edtrate_ncobb!!.visibility = View.GONE
           // holder.tvrate_copp!!.visibility = View.VISIBLE
           // holder.tvrate_ncopp!!.visibility = View.VISIBLE
        }
        else
        {
            //holder.edtrate_cobb!!.visibility = View.GONE
            holder.edtrate_ncobb!!.visibility = View.GONE
            holder.edtrate_cobb_wt!!.visibility = View.GONE
            //holder.tvrate_copp!!.visibility = View.GONE
            holder.tvrate_ncopp!!.visibility = View.GONE
            holder.tvrate_copp_wt!!.visibility = View.GONE

        }
        val dataRate = Itemlist[position].rate

        if (dataRate.length > 0) {
            try {
                var rateValue1 = 0.0
                var rateValue2 = 0.0
                var rateValue3 = 0.0
                val rateValue = Itemlist[position].rate.toDouble()
                var dataRate1 = ""
                val dataRate2 = Itemlist[position].proV_PRICE
                var dataRate3 = ""
                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                        "2025",
                        ignoreCase = true
                    )
                ) {
                    dataRate1 =Itemlist[position].cobB_PRICE
                    dataRate3 =Itemlist[position].cobB_WT
                    if(dataRate1.length > 0){
                        rateValue1 = Itemlist[position].cobB_PRICE.toDouble()
                    }
                    if(dataRate2.length > 0){
                        rateValue2 = Itemlist[position].proV_PRICE.toDouble()
                    }
                   if(dataRate3.length > 0){
                        rateValue3 = Itemlist[position].cobB_WT.toDouble()
                    }
                }
                else
                {
                    dataRate1 = Itemlist[position].locaL_PRICE
                    if(dataRate1.length > 0){
                        rateValue1 = Itemlist[position].locaL_PRICE.toDouble()
                    }
                    holder.tvrate_ncopp!!.text = ""
                }

                if (rateValue > 0.0) {

                    //Ledger ID need to change before release
                    // if (Userinfo.getLedgerid().equals("2025", ignoreCase = true)) {
                    if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                            "2025",
                            ignoreCase = true
                        )
                    ) {
                        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                        formatter.maximumFractionDigits = 4
                        val moneyString = formatter.format(rateValue)
                        holder.tvrate!!.text = moneyString
                    } else {
//					NumberFormat formatter2 = NumberFormat.getCurrencyInstance(new Locale("en", "BD"));
//					formatter2.setMaximumFractionDigits(4);
//					String moneyString2 = formatter2.format(rateValue);
                        holder.tvrate!!.text = "\u09F3 $rateValue"
                    }

                    if (rateValue1 > 0.0) {
                        //Ledger ID need to change before release
                        // if (Userinfo.getLedgerid().equals("2025", ignoreCase = true)) {
                        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                                "2025",
                                ignoreCase = true
                            )
                        ) {
                            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                            formatter.maximumFractionDigits = 4
                            val moneyString = formatter.format(rateValue1)
                            holder.tvrate_copp!!.text = moneyString
                        } else {
//					NumberFormat formatter2 = NumberFormat.getCurrencyInstance(new Locale("en", "BD"));
//					formatter2.setMaximumFractionDigits(4);
//					String moneyString2 = formatter2.format(rateValue);
                            holder.tvrate_copp!!.text = "\u09F3 $rateValue1"
                        }
                    }
                    else {

                        holder.tvrate_copp!!.text = ""

                        holder.edtrate_cobb!!.setText("")



                    }

                    if (rateValue2 > 0.0) {
                        //Ledger ID need to change before release
                        // if (Userinfo.getLedgerid().equals("2025", ignoreCase = true)) {
                        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                                "2025",
                                ignoreCase = true
                            )
                        ) {
                            val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                            formatter.maximumFractionDigits = 4
                            val moneyString = formatter.format(rateValue2)
                            holder.tvrate_ncopp!!.text = moneyString
                        } else {
//					NumberFormat formatter2 = NumberFormat.getCurrencyInstance(new Locale("en", "BD"));
//					formatter2.setMaximumFractionDigits(4);
//					String moneyString2 = formatter2.format(rateValue);
                            holder.tvrate_ncopp!!.text = "\u09F3 $rateValue"
                        }
                    }
                    else {

                        holder.tvrate_ncopp!!.text = ""
                        holder.edtrate_ncobb!!.setText("")


                    }
                   if (rateValue3 > 0.0) {
                        holder.tvrate_copp_wt!!.text = rateValue3.toString()
                        holder.edtrate_cobb_wt!!.setText(rateValue3.toString())
                    }else{
                        holder.tvrate_copp_wt!!.text = ""
                        holder.edtrate_cobb_wt!!.setText("")
                    }

                } else {
                    holder.edtrate!!.setText("")
                    holder.tvrate!!.text = ""
                    holder.tvrate_copp!!.text = ""
                    holder.tvrate_ncopp!!.text = ""
                    holder.tvrate_copp_wt!!.text = ""
                    holder.edtrate_cobb!!.setText("")
                    holder.edtrate_ncobb!!.setText("")
                    holder.edtrate_cobb_wt!!.setText("")


                }
            }
            catch (e: Exception){
                Log.e("error",e.toString())
            }

            if(Itemlist[position].proV_STATUS.equals("Y") && Itemlist[position].status.equals("N"))
            {
                var rateValue2 = 0.0
                val dataRate2 = Itemlist[position].proV_PRICE
                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                        "2025",
                        ignoreCase = true
                    )
                ) {
                    if(dataRate2.length > 0){
                        rateValue2 = Itemlist[position].proV_PRICE.toDouble()
                    }
                }
                if (rateValue2 > 0.0) {
                    //Ledger ID need to change before release
                    // if (Userinfo.getLedgerid().equals("2025", ignoreCase = true)) {
                    if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                            "2025",
                            ignoreCase = true
                        )
                    ) {
                        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
                        formatter.maximumFractionDigits = 4
                        val moneyString = formatter.format(rateValue2)
                        holder.tvrate_ncopp!!.text = moneyString
                       // holder.edtrate_ncobb!!.setText(dataRate2)
                        holder.edtrate_ncobb!!.visibility = View.GONE
                        holder.tvrate_ncopp!!.visibility = View.VISIBLE

                    } else {


                    }
                }
                else {

                    holder.tvrate_ncopp!!.text = ""
                    holder.edtrate_ncobb!!.setText("")


                }
            }
            if(Itemlist[position].opM_DIVISION.equals("SATELLITE") || Itemlist[position].opM_DIVISION.equals("RETAIL DIVISION"))
            {
                holder.edtrate_ncobb!!.visibility = View.GONE
                holder.tvrate_ncopp!!.visibility = View.INVISIBLE
            }
        }

        else {
            holder.tvrate!!.text = ""
            holder.tvrate_copp!!.text = ""
            holder.tvrate_ncopp!!.text = ""
            holder.tvrate_copp_wt!!.text = ""

        }

        holder.edtrate!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                  holder.edtrate!!.isFocusable = true
                  holder.edtrate!!.isFocusableInTouchMode = true
                  holder.edtrate!!.isClickable = true
                  holder.edtrate!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                Itemlist[holder.ref].rate = arg0.toString()
            }
        })
        /*  holder.edtrate!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
              // Toast.makeText(
              // myContext,
              // "Booting - "
              // + Itemlist.get(holder.ref).getBRANCH_NAME(),
              // 1950).show();
              val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
              imm.showSoftInput(holder.edtrate, InputMethodManager.SHOW_IMPLICIT)
              false
          }*/
        holder.edtrate_cobb!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                holder.edtrate_cobb!!.isFocusable = true
                holder.edtrate_cobb!!.isFocusableInTouchMode = true
                holder.edtrate_cobb!!.isClickable = true
                holder.edtrate_cobb!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                        "2025",
                        ignoreCase = true
                    )
                ) {
                    Itemlist[holder.ref1].cobB_PRICE = arg0.toString()
                }
                else
                {
                    Itemlist[holder.ref1].locaL_PRICE = arg0.toString()
                }
            }
        })
        /*holder.edtrate_cobb!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            // Toast.makeText(
            // myContext,
            // "Booting - "
            // + Itemlist.get(holder.ref).getBRANCH_NAME(),
            // 1950).show();
            val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(holder.edtrate_cobb, InputMethodManager.SHOW_IMPLICIT)
            false
        }*/
        holder.edtrate_ncobb!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                  holder.edtrate_ncobb!!.isFocusable = true
                  holder.edtrate_ncobb!!.isFocusableInTouchMode = true
                  holder.edtrate_ncobb!!.isClickable = true
                  holder.edtrate_ncobb!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                Itemlist[holder.ref2].proV_PRICE = arg0.toString()
            }
        })
        holder.edtrate_cobb_wt!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
                holder.edtrate_cobb_wt!!.isFocusable = true
                holder.edtrate_cobb_wt!!.isFocusableInTouchMode = true
                holder.edtrate_cobb_wt!!.isClickable = true
                holder.edtrate_cobb_wt!!.requestFocus()


            }

            override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(arg0: Editable) {
                // TODO Auto-generated method stub
                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                        "2025",
                        ignoreCase = true
                    )
                ) {
                    Itemlist[holder.ref1].cobB_WT = arg0.toString()
                }

            }
        })

        /*holder.edtrate_ncobb!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            // Toast.makeText(
            // myContext,
            // "Booting - "
            // + Itemlist.get(holder.ref).getBRANCH_NAME(),
            // 1950).show();
            val imm = myContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(holder.edtrate_ncobb, InputMethodManager.SHOW_IMPLICIT)
            false
        }*/

        //holder.edtrate_ncobb!!.visibility = View.GONE
        //holder.tvrate_ncopp!!.visibility = View.GONE
        holder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return item.size
    }




    inner class ViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        var tvrate: TextView? = null
        var tvbranchname: TextView? = null
        var hdrZone: TextView? = null
        var edtrate: EditText? = null
        var broilerhdr: TextView? = null
        var retailhdr: TextView? = null
        var ref = 0
        var ref1 = 0
        var ref2 = 0
        var lydetail: LinearLayout? = null
        var edtrate_cobb: EditText? = null
        var edtrate_ncobb: EditText? = null
        var tvrate_copp: TextView? = null
        var tvrate_ncopp: TextView? = null

        var tvrate_copp_wt: TextView? = null
        var edtrate_cobb_wt: EditText? = null
       // var recyclerview_textview:RecyclerView? = null
        //var recyclerview_editext:RecyclerView? = null
        init {

            tvbranchname = aView.findViewById<View>(R.id.tvbranchname) as TextView
            edtrate = aView.findViewById<View>(R.id.edtrate) as EditText

            tvrate = aView.findViewById<View>(R.id.tvrate) as TextView
            lydetail = aView.findViewById<View>(R.id.lydetail) as LinearLayout
            broilerhdr = aView.findViewById<View>(R.id.txtBroilerHdr) as TextView
            retailhdr = aView.findViewById<View>(R.id.txtRetailHdr) as TextView
            hdrZone = aView.findViewById<View>(R.id.txtZoneName) as TextView
            edtrate_cobb = aView.findViewById<View>(R.id.edtrate_cobb) as EditText
            edtrate_ncobb = aView.findViewById<View>(R.id.edtrate_ncobb) as EditText
            tvrate_copp = aView.findViewById<View>(R.id.tvrate_copp) as TextView
            tvrate_ncopp = aView.findViewById<View>(R.id.tvrate_ncopp) as TextView
           // recyclerview_textview = aView.findViewById<View>(R.id.recyclerview_textview) as RecyclerView
           // recyclerview_editext = aView.findViewById<View>(R.id.recyclerview_editext) as RecyclerView
           tvrate_copp_wt = aView.findViewById<View>(R.id.tvrate_copp_wt) as TextView
           edtrate_cobb_wt = aView.findViewById<View>(R.id.edtrate_cobb_wt) as EditText
            mEditable = Editable
            myContext = context
            Itemlist = item
            groupName = ArrayList()
        }
    }



}
