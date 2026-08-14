package com.suguna.lbsalesperson.branchallocation.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable


class DeleteModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ResultStatus? = null



    class ResultStatus {

        @SerializedName("STATUS")
        val status: String? = null

        @SerializedName("STATUSCODE")
        val statusCode: String? = null

        @SerializedName("MESSAGE")
        val message: String? = null

        @SerializedName("REF_ID")
        val refId: String? = null

        @SerializedName("ORDERREFERENCE")
        val orderReference: String? = null
    }


}
