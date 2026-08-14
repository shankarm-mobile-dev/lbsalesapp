package com.suguna.lbsalesperson.farmallocation

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel
import com.truelife.http.Response
import java.io.Serializable

class CancelreaionModel : Response(), Serializable {

    @SerializedName("STATUSCODE1")
    val STATUSCODE1: String? = null

    @SerializedName("MESSAGE1")
    val MESSAGE1: String? = null

    @SerializedName("STATUS1")
    val STATUS1: String? = null

    @SerializedName("RESULT")
    val data: ArrayList<CancelreaionModel>? = null

    @SerializedName("LOOKUP_CODE")
    val LOOKUP_CODE: String? = null

    @SerializedName("MEANING")
    val MEANING: String? = null

    @SerializedName("LOOKUP_TYPE")
    val LOOKUP_TYPE: String? = null


}