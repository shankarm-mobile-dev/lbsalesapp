package com.suguna.lbsalesperson.targetActual.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class TargetVsActualModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<TargetVsActualModel>? = null

    @SerializedName("SALES_KGS")
    var salesKGS: Double? = null

    @SerializedName("ACH_PER")
    var achievedPercent: Double? = null

    @SerializedName("TARGET_QTY")
    var targetQty: Double? = null

    @SerializedName("DAY")
    var day: String? = null

    @SerializedName("WEEKDETAILS")
    var WeekDetails: WEEKDETAILS? = null

    class WEEKDETAILS {

        @SerializedName("WEEK_NO")
        var weekNo: String? = null
    }


}