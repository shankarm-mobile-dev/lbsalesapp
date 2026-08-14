package com.suguna.lbsalesperson.bookOrdersView.model

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


}
