package com.suguna.lbsalesperson.Todaytarget.model

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel
import com.truelife.http.Response
import java.io.Serializable

class ResultPOJO : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<ResultPOJO>? = null

    @SerializedName("Response")
    val Response: String? = null

    @SerializedName("Message")
    val Message: String? = null

}