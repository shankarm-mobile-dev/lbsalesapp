package com.suguna.lbsalesperson.bookOrdersView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.AppDialogs
import com.suguna.lbsalesperson.bookOrdersView.bookOrderViewActivity
import com.suguna.lbsalesperson.bookOrdersView.model.BookOrderOtpModel
import com.truelife.http.Response
import com.truelife.http.ResponseListener
import java.math.BigDecimal
import java.math.RoundingMode

class bookorderRecyclerAdapter (
    private val myContext: Context,
    private val mData: ArrayList<BookOrderOtpModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ResponseListener {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
        private const val CLUSTER_OTHERS = "Others"
        private const val PAYLOAD_EXPAND = "payload_expand"
        private const val COLLAPSED_ARROW_ROTATION = -90f
        private const val EXPANDED_ARROW_ROTATION = 0f
    }

    private val clusterGroups: ArrayList<ClusterGroup> = arrayListOf()
    private val expandedClusters: HashSet<String> = hashSetOf()
    private val displayList: ArrayList<DisplayItem> = arrayListOf()

    var currentPosition: Int = -1

    lateinit var btnShowBottomSheet: Button
    var isViewOrAllocate = "0"

    init {
        rebuildGroups()
        rebuildDisplayList()
    }

    override fun getItemViewType(position: Int): Int {
        return displayList[position].type
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val v = LayoutInflater.from(myContext)
                    .inflate(R.layout.inflate_bookorderview_cluster_header, parent, false)
                HeaderViewHolder(v)
            }
            TYPE_FOOTER -> {
                val v = LayoutInflater.from(myContext)
                    .inflate(R.layout.inflate_bookorderview_cluster_footer, parent, false)
                FooterViewHolder(v)
            }
            else -> {
                val v = LayoutInflater.from(myContext)
                    .inflate(R.layout.inflate_bookorderview_adapter_item, parent, false)
                ItemViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains(PAYLOAD_EXPAND) && holder is HeaderViewHolder) {
            bindHeaderExpandState(holder, displayList[position])
            return
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = displayList[position]
        when (holder) {
            is HeaderViewHolder -> bindHeader(holder, item)
            is FooterViewHolder -> {
                holder.totalLabelTv.text = item.clusterName + " Total"
                holder.totDeclareTv.text = item.totDeclared.toString()
                holder.totBookedTv.text = item.totBooked.toString()
                holder.totPercentTv.text = item.totPercent
            }
            is ItemViewHolder -> bindItem(holder, item.order!!)
        }
    }

    private fun bindHeader(holder: HeaderViewHolder, item: DisplayItem) {
        holder.clusterNameTv.text = item.clusterName
        holder.totDeclareTv.text = item.totDeclared.toString()
        holder.totBookedTv.text = item.totBooked.toString()
        holder.totPercentTv.text = item.totPercent
        bindHeaderExpandState(holder, item)
        val clickListener = View.OnClickListener {
            val adapterPos = holder.adapterPosition
            if (adapterPos == RecyclerView.NO_POSITION) return@OnClickListener
            val clusterName = displayList[adapterPos].clusterName ?: return@OnClickListener
            toggleCluster(clusterName)
        }
        holder.headerRoot.setOnClickListener(clickListener)
        holder.expandIcon.setOnClickListener(clickListener)
        holder.itemView.setOnClickListener(clickListener)
    }

    private fun bindHeaderExpandState(holder: HeaderViewHolder, item: DisplayItem) {
        val expanded = expandedClusters.contains(item.clusterName)
        holder.expandIcon.animate().cancel()
        holder.expandIcon.rotation = if (expanded) EXPANDED_ARROW_ROTATION else COLLAPSED_ARROW_ROTATION
    }

    private fun toggleCluster(clusterName: String) {
        val headerPos = displayList.indexOfFirst {
            it.type == TYPE_HEADER && it.clusterName == clusterName
        }
        if (headerPos < 0) return
        val group = clusterGroups.firstOrNull { it.name == clusterName } ?: return
        val children = buildChildItems(group)

        if (expandedClusters.contains(clusterName)) {
            expandedClusters.remove(clusterName)
            repeat(children.size) {
                if (headerPos + 1 < displayList.size) {
                    displayList.removeAt(headerPos + 1)
                }
            }
            notifyItemChanged(headerPos, PAYLOAD_EXPAND)
            notifyItemRangeRemoved(headerPos + 1, children.size)
        } else {
            expandedClusters.add(clusterName)
            displayList.addAll(headerPos + 1, children)
            notifyItemChanged(headerPos, PAYLOAD_EXPAND)
            notifyItemRangeInserted(headerPos + 1, children.size)
        }
    }

    private fun bindItem(aHolder: ItemViewHolder, data: BookOrderOtpModel) {
        aHolder.branch_tv.text = data.locationName
        aHolder.customername_tv.text = data.availableKg
        aHolder.vehicleno_tv.text = data.bookedKg
        aHolder.farmerotp_tv.text = data.bookedKg
        aHolder.customerotp_tv.text = data.bookedKgPer
        aHolder.status_tv.text = data.closeOrder
        aHolder.itemView.setOnClickListener {
            //getbottomsheet(data)
        }

        aHolder.mySwitch.setOnCheckedChangeListener(null)
        aHolder.mySwitch_price.setOnCheckedChangeListener(null)
        aHolder.mySwitch_pricestop.setOnCheckedChangeListener(null)

        aHolder.mySwitch.isChecked = data.closeOrder.equals("1")
        aHolder.mySwitch_price.isChecked = data.priceStop.equals("1")
        aHolder.mySwitch_pricestop.isChecked = data.priceDowntrent.equals("1")

        aHolder.mySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                aHolder.mySwitch.isChecked = false
                (myContext as bookOrderViewActivity).update_bookorderList(
                    data.branchCode,
                    "Y",
                    data.locationName
                )
            } else {
                aHolder.mySwitch.isChecked = true
                (myContext as bookOrderViewActivity).update_bookorderList(
                    data.branchCode,
                    "N",
                    data.locationName
                )
            }
        }

        aHolder.mySwitch_price.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                aHolder.mySwitch_price.isChecked = false
                (myContext as bookOrderViewActivity).update_pricestopList(
                    data.branchCode,
                    "Y",
                    data.locationName
                )
            } else {
                aHolder.mySwitch_price.isChecked = true
                (myContext as bookOrderViewActivity).update_pricestopList(
                    data.branchCode,
                    "N",
                    data.locationName
                )
            }
        }
        aHolder.mySwitch_pricestop.setOnCheckedChangeListener { _, isChecked ->
            if (data.priceDowntrent.equals("0")) {
                if (data.priceDeclared.equals("0")) {
                    if (isChecked) {
                        aHolder.mySwitch_pricestop.isChecked = false
                        (myContext as bookOrderViewActivity).update_downtrendList(
                            data.branchCode,
                            "Y",
                            data.locationName
                        )
                    } else {
                        aHolder.mySwitch_pricestop.isChecked = true
                        (myContext as bookOrderViewActivity).update_downtrendList(
                            data.branchCode,
                            "N",
                            data.locationName
                        )
                    }
                } else {
                    aHolder.mySwitch_pricestop.isChecked = false
                    AppDialogs.showToastshort(myContext, "Price is already Declared to this branch.")
                }
            } else {
                aHolder.mySwitch_pricestop.isChecked = true
                AppDialogs.showToastshort(myContext, "Price Down trend is Enabled already.")
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList: ArrayList<BookOrderOtpModel>) {
        mData.clear()
        mData.addAll(newList)
        rebuildGroups()
        rebuildDisplayList()
        notifyDataSetChanged()
    }

    private fun rebuildGroups() {
        clusterGroups.clear()
        val grouped = LinkedHashMap<String, ArrayList<BookOrderOtpModel>>()
        mData.forEach { row ->
            val key = row.clusterName?.trim().orEmpty().ifEmpty { CLUSTER_OTHERS }
            grouped.getOrPut(key) { arrayListOf() }.add(row)
        }
        grouped.forEach { (cluster, rows) ->
            var totDeclared = 0
            var totBooked = 0
            rows.forEach { row ->
                totDeclared += parseQty(row.availableKg)
                totBooked += parseQty(row.bookedKg)
            }
            clusterGroups.add(
                ClusterGroup(
                    name = cluster,
                    orders = rows,
                    totDeclared = totDeclared,
                    totBooked = totBooked,
                    totPercent = bookedPercent(totDeclared, totBooked)
                )
            )
        }
        expandedClusters.retainAll(grouped.keys)
    }

    private fun rebuildDisplayList() {
        displayList.clear()
        clusterGroups.forEach { group ->
            displayList.add(
                DisplayItem(
                    TYPE_HEADER,
                    clusterName = group.name,
                    totDeclared = group.totDeclared,
                    totBooked = group.totBooked,
                    totPercent = group.totPercent
                )
            )
            if (expandedClusters.contains(group.name)) {
                displayList.addAll(buildChildItems(group))
            }
        }
    }

    private fun buildChildItems(group: ClusterGroup): ArrayList<DisplayItem> {
        val children = ArrayList<DisplayItem>(group.orders.size + 1)
        group.orders.forEach { row ->
            children.add(DisplayItem(TYPE_ITEM, order = row, clusterName = group.name))
        }
        children.add(
            DisplayItem(
                TYPE_FOOTER,
                clusterName = group.name,
                totDeclared = group.totDeclared,
                totBooked = group.totBooked,
                totPercent = group.totPercent
            )
        )
        return children
    }

    private fun parseQty(value: String?): Int {
        if (value.isNullOrBlank()) return 0
        return try {
            value.toDouble().toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun bookedPercent(declared: Int, booked: Int): String {
        if (declared <= 0) return "0.00"
        val totBookedPer = (booked.toFloat() / declared.toFloat()) * 100
        return BigDecimal(totBookedPer.toDouble())
            .setScale(2, RoundingMode.HALF_UP)
            .toString()
    }

    override fun getItemCount(): Int {
        return displayList.size
    }

    inner class HeaderViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {
        val headerRoot: View = aView.findViewById(R.id.cluster_header_root)
        val clusterNameTv: TextView = aView.findViewById(R.id.cluster_name_tv)
        val totDeclareTv: TextView = aView.findViewById(R.id.cluster_header_declare_tv)
        val totBookedTv: TextView = aView.findViewById(R.id.cluster_header_booked_tv)
        val totPercentTv: TextView = aView.findViewById(R.id.cluster_header_percent_tv)
        val expandIcon: ImageView = aView.findViewById(R.id.cluster_expand_iv)
    }

    inner class FooterViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {
        val totalLabelTv: TextView = aView.findViewById(R.id.cluster_total_label_tv)
        val totDeclareTv: TextView = aView.findViewById(R.id.cluster_tot_declare_qty)
        val totBookedTv: TextView = aView.findViewById(R.id.cluster_tot_booked_qty)
        val totPercentTv: TextView = aView.findViewById(R.id.cluster_tot_booked_per)
    }

    inner class ItemViewHolder constructor(aView: View) :
        RecyclerView.ViewHolder(aView) {

        val branch_tv: TextView
        val customername_tv: TextView
        val vehicleno_tv: TextView
        val farmerotp_tv: TextView
        val customerotp_tv: TextView
        val status_tv: TextView
        val mySwitch: Switch
        val mySwitch_price: Switch
        val mySwitch_pricestop: Switch

        init {
            branch_tv = aView.findViewById<View>(R.id.branch_tv) as TextView
            customername_tv = aView.findViewById<View>(R.id.customername_tv) as TextView
            vehicleno_tv = aView.findViewById<View>(R.id.vehicleno_tv) as TextView
            farmerotp_tv = aView.findViewById<View>(R.id.farmerotp_tv) as TextView
            customerotp_tv = aView.findViewById<View>(R.id.customerotp_tv) as TextView
            status_tv = aView.findViewById<View>(R.id.status_tv) as TextView
            mySwitch = aView.findViewById<View>(R.id.mySwitch) as Switch
            mySwitch_price = aView.findViewById<View>(R.id.mySwitch_price) as Switch
            mySwitch_pricestop = aView.findViewById<View>(R.id.mySwitch_pricesstop) as Switch
        }
    }

    private data class ClusterGroup(
        val name: String,
        val orders: ArrayList<BookOrderOtpModel>,
        val totDeclared: Int,
        val totBooked: Int,
        val totPercent: String
    )

    private data class DisplayItem(
        val type: Int,
        val order: BookOrderOtpModel? = null,
        val clusterName: String? = null,
        val totDeclared: Int = 0,
        val totBooked: Int = 0,
        val totPercent: String = ""
    )

    override fun onResponse(r: Response?) {
        AppDialogs.hideProgressDialog()
        if (r != null) {
        }
    }

}
