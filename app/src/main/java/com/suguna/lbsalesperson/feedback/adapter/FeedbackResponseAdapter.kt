package com.suguna.lbsalesperson.feedback.adapter

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
import com.suguna.lbsalesperson.feedback.model.FeedbackResponseModel
import com.suguna.lbsalesperson.listener.ClickListener


class FeedbackResponseAdapter(
        private val myContext: Context,
        private val mData: ArrayList<FeedbackResponseModel>,
        private var mClickList: ClickListener
) : RecyclerView.Adapter<FeedbackResponseAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_feedback_response_adapter_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
            aHolder: ViewHolder,
            position: Int
    ) {
        val data = mData.get(position)
        aHolder.mName.text = data.name
        aHolder.mResponse.text = "${data.responseText}"
        aHolder.mRoleTxt.text = data.job

        if (position % 2 == 0) {
            aHolder.mRootLay.setBackgroundColor(Color.parseColor("#ededed"))
        }
        /* if (mData.size - 1 == position) {
             aHolder.mResponse.isFocusable = true
         }

         aHolder.mResponse.addTextChangedListener(object : TextWatcher {

             override fun afterTextChanged(s: Editable?) {

                 try {
                     updateResponseText(position, s.toString())
                 } catch (e: Exception) {
                     updateResponseText(position, "")
                 }
             }

             override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

             }

             override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

             }
         })*/
    }


    fun updateResponseText(pos: Int, text: String) {
        mData.get(pos).responseText = text
    }

    override fun getItemCount(): Int {
        return mData.size
    }


    inner class ViewHolder constructor(aView: View) :
            RecyclerView.ViewHolder(aView) {

        val mRoleTxt: TextView
        val mName: TextView
        val mResponse: TextView
        val mRootLay: LinearLayout


        init {

            mRoleTxt = aView.findViewById<View>(R.id.tv_role) as TextView
            mName = aView.findViewById<View>(R.id.txt_name) as TextView
            mResponse = aView.findViewById<View>(R.id.txt_response) as TextView
            mRootLay = aView.findViewById<View>(R.id.rootLay) as LinearLayout


        }
    }
}