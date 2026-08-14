package com.suguna.lbsalesperson.priceallocation.model

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import com.truelife.http.Response
import java.io.Serializable

class DiffpricedeclareModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<DiffpricedeclareModel>? = null

    @SerializedName("LEDGER_ID")
    val LEDGER_ID: String? = null

    @SerializedName("ORG_ID")
    val ORG_ID: String? = null

    @SerializedName("ITEM_CODE")
    val ITEM_CODE: String? = null

    @SerializedName("PRODUCT_CATEGORY")
    val PRODUCT_CATEGORY: String? = null

    @SerializedName("LOW_WT")
    val LOW_WT: String? = null

    @SerializedName("HIGH_WT")
    val HIGH_WT: String? = null

    @SerializedName("FROM_DATE")
    val FROM_DATE: String? = null

    @SerializedName("TO_DATE")
    val TO_DATE: String? = null

    @SerializedName("SEQ_NO")
    val SEQ_NO: String? = null


}