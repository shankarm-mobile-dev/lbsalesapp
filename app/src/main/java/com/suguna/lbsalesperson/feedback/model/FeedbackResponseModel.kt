package com.suguna.lbsalesperson.feedback.model

import com.google.gson.annotations.SerializedName
import com.truelife.http.Response
import java.io.Serializable

class FeedbackResponseModel : Response(), Serializable {

    @SerializedName("RESULT")
    val data: ArrayList<FeedbackResponseModel>? = null

    @SerializedName("NAME")
    var name: String? = ""

    @SerializedName("RESPONSE_TEXT")
    var responseText: String? = ""

    @SerializedName("TO_USER")
    var userId: String? = ""

    @SerializedName("JOB")
    var job: String? = ""

}