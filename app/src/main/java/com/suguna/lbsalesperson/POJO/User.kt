package com.suguna.lbsalesperson.POJO

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable


class User : Response(), Serializable {



    @SerializedName("USERNAME")
    var USERNAME: String? = null

    @SerializedName("USERDES")
    var USERDES: String? = null

    @SerializedName("USERID")
    var USERID: String? = null

    @SerializedName("USERTYPE")
    var USERTYPE: String? = null

    @SerializedName("PASSWORD")
    var PASSWORD: String? = null

    @SerializedName("TKEY")
    var TKEY: String? = null

    @SerializedName("LEDGER_ID")
    var ledgerId: String? = null

    @SerializedName("PROD_NAME")
    var PROD_NAME: String? = null

    @SerializedName("FARM_WISE_AREA")
    var FARM_WISE_AREA: String? = null


}
