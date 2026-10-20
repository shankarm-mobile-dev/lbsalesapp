package com.suguna.lbsalesperson.bookOrdersView.model

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.exceptionOrders.model.ExceptionOtpModel
import com.truelife.http.Response
import java.io.Serializable

class BookOrderOtpModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<BookOrderOtpModel>? = null

    @SerializedName("AVAILABLEKG")
    val availableKg: String = ""

    @SerializedName("BRANCH_CODE")
    val branchCode: String? = null

    @SerializedName("LOCATION_NAME")
    val locationName: String? = null

    @SerializedName("BOOKED_KG")
    val bookedKg: String = ""

    @SerializedName("CLOSE_ORDER")
    val closeOrder: String? = null

    @SerializedName("BOOKED_KG_PER")
    val bookedKgPer: String = ""

    @SerializedName("PRICE_STOP")
    val priceStop: String? = null

    @SerializedName("PRICE_DOWNTRENT")
    val priceDowntrent: String? = null

    @SerializedName("PRICE_DECLARED")
    val priceDeclared: String? = null

    @SerializedName("CLUSTER_NAME")
    val clusterName: String? = null
}