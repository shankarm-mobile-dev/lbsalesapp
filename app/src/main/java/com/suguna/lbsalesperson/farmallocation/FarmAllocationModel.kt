package com.suguna.lbsalesperson.farmallocation

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class FarmAllocationModel : Response(), Serializable {

    @SerializedName("RESULT1")
    val orders: ArrayList<OrderDetails>? = null

    @SerializedName("BRANCHES")
    val branches: ArrayList<Branches>? = null

    @SerializedName("SUPERVISOR")
    val supervisor: ArrayList<Supervisor>? = null

    @SerializedName("WEIGHMENTTYPE")
    val weighmenttype: ArrayList<Weighmenttype>? = null

    @SerializedName("DIFFPRICE")
    val diffprice: ArrayList<Diffprice>? = null

    class OrderDetails {

        @SerializedName("HEADER_ID")
        var headerID = ""

        @SerializedName("LINE_ID")
        var lineID = ""

        @SerializedName("REF_NO")
        var refNo = ""

        @SerializedName("REF_DATE")
        var refDate = ""

        @SerializedName("CUSTOMER_CODE")
        var customerCode = ""

        @SerializedName("CUSTOMER_NAME")
        var customerName = ""


        @SerializedName("QUANTITY")
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

        @SerializedName("CUS_SMS_STATUS")
        var CUS_SMS_STATUS = ""

        @SerializedName("STATUS")
        var STATUS = ""
        @SerializedName("LIFTTIMESLOT")
        var LIFTTIMESLOT = ""
        @SerializedName("CUS_REQBTY_STATUS")
        var CUS_REQBTY_STATUS = ""

        @SerializedName("PROD_CATEGORY")
        var PROD_CATEGORY = ""

        @SerializedName("FROM_WEIGHT")
        var FROM_WEIGHT = ""

        @SerializedName("TO_WEIGHT")
        var TO_WEIGHT = ""

        @SerializedName("ORDER_STATUS")
        var ORDER_STATUS = ""

        @SerializedName("ALLOCATIONDETAILS")
        var orderRows: ArrayList<OrderRows>? = arrayListOf()

        @SerializedName("DETAILS")
        var details: Details? = null

        inner class OrderRows {

            @SerializedName("LINE_ID")
            var lineId = "0"

            @SerializedName("BRANCH_CODE")
            var branchCode = ""

            @SerializedName("QUANTITY")
            var qty = ""

            @SerializedName("FARM_CODE")
            var farmCode = ""

            @SerializedName("SUPERVISOR_CODE")
            var supervisorCode = ""

            @SerializedName("MOBILE_NO")
            var mobileNo = ""

            @SerializedName("AVG_WGT")
            var avgWgt = ""

            @SerializedName("SUPERVISOR_NAME")
            var supervisorName = ""

            @SerializedName("CUS_SMS_VALD_STATUS")
            var CUS_SMS_VALD_STATUS = "0"

            @SerializedName("DETAIL_LINE_ID")
            var detaillineId = "0"

            @SerializedName("WEIGHMENTTYPE")
            var weighmentType = ""

            @SerializedName("PROD_CATEGORY")
            var PROD_CATEGORY = "-"

            @SerializedName("FROM_WEIGHT")
            var FROM_WEIGHT = "0"

            @SerializedName("TO_WEIGHT")
            var TO_WEIGHT = "0"

            @SerializedName("PREFERRED_FARM_CODE")
            var PREFERRED_FARM_CODE = "-"

            @SerializedName("PREFERRED_FARM_STATUS")
            var PREFERRED_FARM_STATUS = "N"

            @SerializedName("STATUS")
            var STATUS = "N"

            var headerId = headerID

            var submittemp = 0
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

        @SerializedName("FARM_CODE")
        var FARM_CODE = ""

        @SerializedName("FARM_DESC")
        var FARM_DESC = ""

        @SerializedName("FARM_STOCK")
        var FARM_STOCK = ""

        @SerializedName("BRANCH_ID")
        var branchId = ""

        @SerializedName("BODY_WT")
        var BODY_WT = ""

        @SerializedName("MEAN_AGE")
        var MEAN_AGE = ""

        @SerializedName("APPROX_AVG_WT")
        var APPROX_AVG_WT = ""
    }

    class Supervisor {

        @SerializedName("EMP_NAME")
        var EMP_NAME = ""

        @SerializedName("EMP_NO")
        var EMP_NO = ""

        @SerializedName("MOBILENO")
        var MOBILENO = ""

        @SerializedName("JOB")
        var JOB = ""

    }

    class Weighmenttype {

        @SerializedName("LOOKUP_CODE")
        var LOOKUP_CODE = ""

        @SerializedName("MEANING")
        var MEANING = ""

        @SerializedName("DESCRIPTION")
        var DESCRIPTION = ""

    }

    class Diffprice {

        @SerializedName("ITEM_CODE")
        var ITEM_CODE = ""

        @SerializedName("PRODUCT_CATEGORY")
        var PRODUCT_CATEGORY = ""

        @SerializedName("LOW_WT")
        var LOW_WT = ""

        @SerializedName("HIGH_WT")
        var HIGH_WT = ""

        @SerializedName("PROD_NAME")
        var PROD_NAME = ""
    }
}
