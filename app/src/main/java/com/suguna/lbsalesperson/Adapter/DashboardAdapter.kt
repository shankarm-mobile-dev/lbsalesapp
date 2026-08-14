package com.suguna.lbsalesperson.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.POJO.DasboardMenus
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.listener.ClickListener
import com.suguna.lbsalesperson.listener.DashBoardClickListener

class DashboardAdapter(
        private val myContext: FragmentActivity,
        var mMenus: List<DasboardMenus>, val listener: DashBoardClickListener
) : RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolder {
        val v: View = LayoutInflater.from(myContext)
                .inflate(R.layout.inflate_dasboard_item, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(aHolder: ViewHolder, position: Int) {
        var data = getNewsInfo(position)

        aHolder.mName.text = data.title
        aHolder.mCropImage.setImageResource(data.icons!!)

        aHolder.myClubLayout.setOnClickListener {

            listener.click(data.title!!,aHolder.myClubLayout )
        }

    }

    fun getNewsInfo(position: Int): DasboardMenus {
        return mMenus[position]
    }

    override fun getItemCount(): Int {
        return mMenus.size
    }


    inner class ViewHolder(aView: View) :
            RecyclerView.ViewHolder(aView) {
        // val mProfileimage: CircleImageView
        val mCropImage: ImageView
        val myClubLayout: LinearLayout
        val mName: TextView

        init {
            mCropImage = aView.findViewById(R.id.menu_icon) as ImageView

            myClubLayout = aView.findViewById(R.id.root_lay)
            mName = aView.findViewById(R.id.name_tv)
        }
    }

    fun updateAdapter(
            aUList: List<DasboardMenus>
    ) {
        mMenus = aUList
    }

}