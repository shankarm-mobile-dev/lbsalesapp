package com.suguna.lbsalesperson.customerclub.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class CustomerClubModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: CustomerClubModel? = null

    @SerializedName("SALES_KGS")
    var salesKGS: Double? = null

    @SerializedName("ACH_PER")
    var achievedPercent: Double? = null

    @SerializedName("TARGET_QTY")
    var targetQty: Double? = null

    @SerializedName("DAY")
    var day: String? = null

    @SerializedName("WEEKDETAILS")
    var WeekDetails: ArrayList<Year>? = null

    class Year {

        @SerializedName("periodYear")
        var year: String? = null

        @SerializedName("PERIOD_YEAR")
        var periodYear: String? = null
    }


}