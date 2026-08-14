package com.suguna.lbsalesperson.branchallocation.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable


class BranchAllocationModel : Response(), Serializable {

    @SerializedName("RESULT1")
    val orders: ArrayList<OrderDetails>? = null

    @SerializedName("BRANCHES")
    val branches: ArrayList<Branches>? = null


    class OrderDetails {

        @SerializedName("HEADER_ID")
        var headerID = ""


        @SerializedName("REF_NO")
        var refNo = ""

        @SerializedName("REF_DATE")
        var refDate = ""

        @SerializedName("CUSTOMER_CODE")
        var customerCode = ""

        @SerializedName("CUSTOMER_NAME")
        var customerName = ""


        @SerializedName("TOTALQTY")
        var totalQty = ""

        @SerializedName("REGION_CODE")
        var regionCode = ""

        @SerializedName("PREFAVGWT")
        var prefAvgWgt = ""

        @SerializedName("BOOKING_TYPE")
        var bookingType = ""

        @SerializedName("PRIMARY_SALESREP_ID")
        var primarySalesRepId = ""

        @SerializedName("VEHICLENUM")
        var VEHICLENUM = ""

        @SerializedName("ALLOWSTATUS")
        var ALLOWSTATUS = ""
        @SerializedName("STATUS")
        var STATUS = ""
        @SerializedName("LIFTTIMESLOT")
        var LIFTTIMESLOT = ""

        @SerializedName("PROD_CATEGORY")
        var PROD_CATEGORY = ""

        @SerializedName("FROM_WEIGHT")
        var FROM_WEIGHT = ""

        @SerializedName("TO_WEIGHT")
        var TO_WEIGHT = ""

        @SerializedName("SALES_PERSON")
        var SALES_PERSON = ""

        @SerializedName("RETAIL_ORDER")
        var RETAIL_ORDER = ""

        @SerializedName("ALLOCATIONDETAILS")
        var orderRows: ArrayList<OrderRows>? = arrayListOf()

        @SerializedName("DETAILS")
        var details: Details? = null

     inner    class OrderRows {

            @SerializedName("LINE_ID")
            var lineId = "0"

            @SerializedName("BRANCH_CODE")
            var branchCode = ""

            @SerializedName("QUANTITY")
            var qty = ""

            var headerId = headerID
        }

        class Details {

            @SerializedName("CNT")
            var cnt = -1

            @SerializedName("SUMQTY")
            var sumQty = 0.0

            @SerializedName("QUANTITY")
            var qty = 0.0
        }



        override fun hashCode(): Int {
            return headerID.hashCode()
        }


    }


    class Branches {

        @SerializedName("ORGANIZATION_ID")
        var organizationId = ""

        @SerializedName("BRANCH_CODE")
        var branchCode = ""

        @SerializedName("BRANCH_NAME")
        var branchName = ""

        @SerializedName("LOCATION")
        var location = ""

        @SerializedName("BRANCH_ID")
        var branchId = ""

        @SerializedName("WF_STATUS")
        var wfStatus = ""

        @SerializedName("ORG_TYPE")
        var orgType = ""
    }
}
