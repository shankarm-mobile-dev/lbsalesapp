package com.truelife.http


import com.google.gson.annotations.SerializedName

import java.io.Serializable



open class Response : Serializable {


    @SerializedName("response")
    var response: Response? = null


    @SerializedName("STATUSCODE")
    var responseCode: Int? = null

    @SerializedName("total_pages")
    var totalPages: Int = 0


    @SerializedName("MESSAGE")
    var responseMessage: String? = ""


    @SerializedName("is_user_active")
    var isUserActive: String? = null
    /**
     * unique integer number for the request
     */
    @SerializedName("a1")
    var requestType: Int? = null




    @SerializedName("a2")
    var extraOutput: String? = null

    /**
     * @return true if the response gets success id
     */
    val isSuccess: Boolean
        get() = responseCode == 1

}
