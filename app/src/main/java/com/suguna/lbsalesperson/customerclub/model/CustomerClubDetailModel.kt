package com.suguna.lbsalesperson.customerclub.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class CustomerClubDetailModel : Response(), Serializable {


    @SerializedName("RESULT")
    var data: ArrayList<ClubDetails> = arrayListOf()

    @SerializedName("DATE")
    var date: String? = null

    class ClubDetails {
        @SerializedName("CUST_CODE")
        var customerCode: String? = null

        @SerializedName("DATE")
        var date: String? = null

        @SerializedName("CUSTOMER_ID")
        var customerId: String? = null

        @SerializedName("CUST_NAME")
        var customerName: String? = null

        @SerializedName("CLUB")
        var club: String? = null

        @SerializedName("YEAR")
        var year: String? = null

        @SerializedName("CURRENT_YEAR_CLUB")
        var currentYearClub: String? = null

    }


}