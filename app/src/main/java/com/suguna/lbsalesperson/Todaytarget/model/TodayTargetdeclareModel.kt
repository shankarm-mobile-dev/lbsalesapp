package com.suguna.lbsalesperson.Todaytarget.model

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffdeclareModel
import com.truelife.http.Response
import java.io.Serializable

class TodayTargetdeclareModel: Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<TodayTargetdeclareModel>? = null

    @SerializedName("CLUSTER_NAME")
    val CLUSTER_NAME: String? = null

    @SerializedName("CLUSTER_DESC")
    val CLUSTER_DESC: String? = null

    @SerializedName("CLUSTER_NAME_SEQ")
    val CLUSTER_NAME_SEQ: String? = null

    @SerializedName("SYSDATE")
    val SYSDATE: String? = null

    @SerializedName("DECLARE_TARGET")
    var DECLARE_TARGET: String? = null

    @SerializedName("AVG_TARGET")
    val AVG_TARGET: String? = null

    @SerializedName("ISUPDATE")
    val ISUPDATE: String? = null

    @SerializedName("REGION_CODE")
    val REGION_CODE: String? = null

    @SerializedName("DAY_TARGET")
    val DAY_TARGET: String? = null

    @SerializedName("ISCHECK")
    val ISCHECK: String? = null
}