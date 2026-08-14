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
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.DecimalDigitsInputFilter
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.priceallocation.PriceDeclarationListener
import com.suguna.lbsalesperson.priceallocation.SpanningLinearLayoutManager
import com.suguna.lbsalesperson.priceallocation.activity.DiffPriceAllocationActivity
import com.suguna.lbsalesperson.priceallocation.model.difflblist
import com.suguna.lbsalesperson.priceallocation.model.lbpricedet
import java.text.NumberFormat
import java.util.*

class PriceRecyclerAdapter_new(
    private val context: Context,
    private val resource: Int,
    private val item: ArrayList<lbpricedet>,
    private val Editable: Boolean,
    private val result: MutableList<difflblist>,
    private val diffPriceAllocationActivity: PriceDeclarationListener
) : RecyclerView.Adapter<PriceRecyclerAdapter_new.ViewHolder>() {

    var myContext: Context = context
    var Itemlist: ArrayList<lbpricedet> = item
    var groupName: ArrayList<String> = ArrayList()
    var mEditable = Editable
    var mMenuAdapter: DiffPriceAllocationActivity? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
            .inflate(R.layout.lbprice_list_item_new, parent, false)
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
        holder.edtrate_cobb!!.setText(Itemlist[position].cobB_PRICE)
        holder.edtrate_ncobb!!.filters = arrayOf<InputFilter>(DecimalDigitsInputFilter(2))
        holder.edtrate_ncobb!!.setText(Itemlist[position].locaL_PRICE)
        holder.lydetail!!.visibility = View.VISIBLE
        val sdr = Itemlist[position].zonE_NAME
        val region = Itemlist[position].regioncode
        if(Itemlist[position].check)
        {
            holder.checkbox_fillfinalprice!!.isChecked=true
        }
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
            holder.tvrate!!.visibility = View.GONE
            holder.tvrate_copp!!.visibility = View.GONE
            holder.tvrate_ncopp!!.visibility = View.GONE
            holder.checkbox_fillfinalprice!!.visibility = View.GONE
        } else {
            if (Itemlist[position].status == "Y") {
                holder.edtrate!!.visibility = View.GONE
                holder.edtrate_cobb!!.visibility = View.GONE
                holder.edtrate_ncobb!!.visibility = View.GONE
                holder.tvrate!!.visibility = View.GONE
                holder.tvrate_copp!!.visibility = View.GONE
                holder.tvrate_ncopp!!.visibility = View.GONE
               // holder.checkbox_fillfinalprice!!.visibility = View.GONE
            } else {
                holder.edtrate!!.visibility = View.GONE
                holder.edtrate_cobb!!.visibility = View.GONE
                holder.edtrate_ncobb!!.visibility = View.GONE
                holder.tvrate!!.visibility = View.GONE
                holder.tvrate_copp!!.visibility = View.GONE
                holder.tvrate_ncopp!!.visibility = View.GONE
            }
        }
        if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals("2025", ignoreCase = true)) {
            //holder.edtrate_cobb!!.visibility = View.VISIBLE
            //holder.edtrate_ncobb!!.visibility = View.VISIBLE

        }
        else{
            holder.edtrate_cobb!!.visibility = View.GONE
            holder.edtrate_ncobb!!.visibility = View.GONE
            holder.tvrate_copp!!.visibility = View.GONE
            holder.tvrate_ncopp!!.visibility = View.GONE
        }
        val dataRate = Itemlist[position].rate

        if (dataRate.length > 0) {
            try {
                var rateValue1 = 0.0
                var rateValue2 = 0.0
                val rateValue = Itemlist[position].rate.toDouble()
                val dataRate1 = Itemlist[position].cobB_PRICE
                val dataRate2 = Itemlist[position].locaL_PRICE
                if (LocalStorageSP.getLoginUser(myContext).ledgerId.equals(
                        "2025",
                        ignoreCase = true
                    )
                ) {
                    if(dataRate1.length > 0){
                        rateValue1 = Itemlist[position].cobB_PRICE.toDouble()
                    }
                    if(dataRate2.length > 0){
                        rateValue2 = Itemlist[position].locaL_PRICE.toDouble()
                    }
                }
                else
                {
                    holder.tvrate_copp!!.text = ""
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
                            holder.tvrate_copp!!.text = "\u09F3 $rateValue"
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

                } else {
                    holder.edtrate!!.setText("")
                    holder.tvrate!!.text = ""
                    holder.tvrate_copp!!.text = ""
                    holder.tvrate_ncopp!!.text = ""
                    holder.edtrate_cobb!!.setText("")
                    holder.edtrate_ncobb!!.setText("")


                }
            }
            catch (e: Exception){
                Log.e("error",e.toString())
            }
        }

        else {
            holder.tvrate!!.text = ""
            holder.tvrate_copp!!.text = ""
            holder.tvrate_ncopp!!.text = ""
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
                Itemlist[holder.ref1].cobB_PRICE = arg0.toString()
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
                Itemlist[holder.ref2].locaL_PRICE = arg0.toString()
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
        holder.checkbox_fillfinalprice!!.setOnClickListener(View.OnClickListener {
            if(holder.checkbox_fillfinalprice!!.isChecked)
            {

               // (myContext).fill_finalprice(true,myContext)
                Itemlist[holder.ref2].check=true
                diffPriceAllocationActivity.onSubmit(true,Itemlist[position].brancH_CODE)
            }
            else
            {
               // (myContext).fill_finalprice(false,myContext)
                Itemlist[holder.ref2].check=false
                diffPriceAllocationActivity.onSubmit(false,Itemlist[position].brancH_CODE)

            }

        })

        val layoutManager = SpanningLinearLayoutManager(
            myContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        holder.recyclerview_textview?.layoutManager = layoutManager

        val adapter =
            DiffProvPriceRecyclerAdapter(myContext, Itemlist[position].diffprice,Editable)
        holder.recyclerview_textview?.adapter = adapter
        holder.setIsRecyclable(false);


        val layoutManager1 = SpanningLinearLayoutManager(
            myContext,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        holder.recyclerview_editext?.layoutManager = layoutManager1

        val adapter1 =
            DiffPriceRecyclerAdapter(myContext, Itemlist[position].diffprice,Editable,Itemlist[position].opM_DIVISION)
        holder.recyclerview_editext?.adapter = adapter1
        holder.setIsRecyclable(false);
        if(Itemlist[position].diffprice.get(0).status.equals("Y"))
        {
            holder.checkbox_fillfinalprice!!.visibility = View.GONE
        }
        if(Itemlist[position].opM_DIVISION.equals("SATELLITE") || Itemlist[position].opM_DIVISION.equals("RETAIL DIVISION"))
        {
            holder.recyclerview_textview?.visibility = View.GONE
            holder.lyt_provisional_price!!.visibility = View.GONE
            holder.checkbox_fillfinalprice!!.visibility = View.GONE
        }


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

        var recyclerview_textview: RecyclerView? = null
        var recyclerview_editext: RecyclerView? = null

        var checkbox_fillfinalprice:CheckBox? = null
        var lyt_provisional_price:LinearLayout? = null
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
            recyclerview_textview = aView.findViewById<View>(R.id.recyclerview_textview) as RecyclerView
            recyclerview_editext = aView.findViewById<View>(R.id.recyclerview_editext) as RecyclerView
            checkbox_fillfinalprice= aView.findViewById<View>(R.id.checkbox_fillfinalprice) as CheckBox
            lyt_provisional_price = aView.findViewById<View>(R.id.lyt_provisional_price) as LinearLayout

            mEditable = Editable
            myContext = context
            Itemlist = item
            groupName = ArrayList()
        }
    }



}
