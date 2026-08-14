package com.suguna.lbsalesperson.diffpriceallocation.Model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class DiffnameModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<DiffnameModel>? = null

    @SerializedName("LOOKUP_CODE")
    val LOOKUP_CODE: String? = null

    @SerializedName("MEANING")
    val MEANING: String? = null

    @SerializedName("DESCRIPTION")
    val DESCRIPTION: String? = null

    @SerializedName("TAG")
    val TAG: String? = null

    @SerializedName("REGION")
    val REGION: String? = null
}