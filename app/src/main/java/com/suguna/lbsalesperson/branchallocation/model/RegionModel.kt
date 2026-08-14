package com.suguna.lbsalesperson.branchallocation.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable



class RegionModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<RegionModel>? = null

    @SerializedName("REGION_CODE")
    val regionCode: String? = null

    @SerializedName("REGION")
    val regionName: String? = null

    @SerializedName("ENTRYDAYS")
    val ENTRYDAYS: String? = null

    @SerializedName("ReasonList")
    val data_reason: ArrayList<ReasonModel>? = null

    class ReasonModel : Serializable
    {
        @SerializedName("isStopped")
        val isStopped: String? = null
        @SerializedName("Reason")
        val Reason: String? = null
    }
}
