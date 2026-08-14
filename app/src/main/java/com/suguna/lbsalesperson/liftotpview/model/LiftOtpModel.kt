package com.suguna.lbsalesperson.liftotpview.model

import com.google.gson.annotations.SerializedName
import com.suguna.lbsalesperson.branchallocation.model.RegionModel
import com.truelife.http.Response
import java.io.Serializable

class LiftOtpModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<LiftOtpModel>? = null

    @SerializedName("REGION_CODE")
    val regionCode: String? = null

    @SerializedName("BRANCH_CODE")
    val branchCode: String? = null

    @SerializedName("BRANCH_NAME")
    val branchName: String? = null

    @SerializedName("CUSTOMER_NUMBER")
    val customerNumber: String? = null

    @SerializedName("CUSTOMER_NAME")
    val customerName: String? = null

    @SerializedName("ORDER_DATE")
    val orderDate: String? = null

    @SerializedName("REF_DATE")
    val refDate: String? = null

    @SerializedName("REF_NO")
    val refNo: String? = null

    @SerializedName("TOTALQTY")
    val totalQty: String? = null

    @SerializedName("FARM_CODE")
    val farmCode: String? = null

    @SerializedName("SUPERVISOR_CODE")
    val supervisorCode: String? = null

    @SerializedName("CUSTOMER_OTP")
    val customerOtp: String? = null

    @SerializedName("FARMER_OTP")
    val farmerOtp: String? = null

    @SerializedName("VEHICLENUM")
    val vehicleNum: String? = null

    @SerializedName("BOOKING_TYPE")
    val bookingType: String? = null

    @SerializedName("SCHEDULE_NO")
    val scheduleNo: String? = null

            @SerializedName("FARM_NAME")
            val farmName: String? = null

    @SerializedName("SUPERVISOR_NAME")
    val supervisorName: String? = null

    @SerializedName("ALLOCATED_QTY")
    val allocatedQty: String? = null

}
