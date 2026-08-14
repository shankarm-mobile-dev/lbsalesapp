package com.suguna.lbsalesperson.customerclub.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class ClubModel : Response(), Serializable {


    @SerializedName("CLUB")
    var club: String? = null

    @SerializedName("SEQ_ID")
    var seqId: Int = 0

    @SerializedName("count")
    var count: String? = null


}