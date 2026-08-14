package com.suguna.lbsalesperson.farmallocation

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class CanceldetailModel : Response(), Serializable {

    @SerializedName("RESULT2")
    val data: ArrayList<CanceldetailModel>? = null

    @SerializedName("STATUSCODE1")
    val STATUSCODE1: String? = null

    @SerializedName("STATUS1")
    val STATUS1: String? = null

    @SerializedName("MESSAGE1")
    val MESSAGE1: String? = null


}