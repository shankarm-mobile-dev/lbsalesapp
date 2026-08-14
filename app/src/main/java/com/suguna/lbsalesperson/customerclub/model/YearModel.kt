package com.suguna.lbsalesperson.customerclub.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class YearModel : Response(), Serializable {


    @SerializedName("periodYear")
    var year: String? = null

    @SerializedName("PERIOD_YEAR")
    var periodYear: String? = null



}