package com.truelife.api

import android.content.Context
import android.util.Log
import com.suguna.lbsalesperson.Sendreceiver.WebserviceURLs
import com.suguna.lbsalesperson.Utils.LBConstant
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.truelife.api.AppServices.API.constructUrl
import com.truelife.http.JsonRestClient
import com.truelife.http.PlainResponseListener
import com.truelife.http.ResponseListener
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.apache.http.HttpHeaders
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.lang.reflect.Type


class AppServices {



    object API {

        var baseUrl = "118.185.181.62:8091/lborder.sugunafoods.co.in"
        // No Prefix

        fun constructUrl(case: String): String {
            if (LBConstant.URL_TYPE == WebserviceURLs.getDevelopment())
                baseUrl = WebserviceURLs.dev_ipandport
            else if (LBConstant.URL_TYPE == WebserviceURLs.getProduction())
                baseUrl = WebserviceURLs.prod_ipandport
            else if (LBConstant.URL_TYPE == WebserviceURLs.getTesting())
                baseUrl = WebserviceURLs.test_ipandport
            Log.e("baseUrl","$baseUrl/api/$case");
            return "$baseUrl/api/$case"
        }

        /*  fun constructUrl(urlKey: String): String {
              return String.format("%s%s", "BuildConfig.API_URL", urlKey)
          }
  */
        // With Prefix
        /*   fun constructSuffixUrl(c: Context, urlKey: String): String {
               val isProvider = LocalStorageSP[c, "is_provider", false]!!
               val SUFFIX = String.format("api/v1/%s/", if (isProvider) "providers" else "patients")
               return String.format("%s%s%s", BuildConfig.API_URL, SUFFIX, urlKey)
           }*/

        // WebSocket URL
        /*  fun constructSocketUrl(c: Context): String {
              return String.format(
                  "%s%s",
                  BuildConfig.SOCKET_URL,
                  String.format("cable?token=%s", LocalStorageSP[c, "auth_key", ""]!!)
              )
          }*/

        // API Case's


        val targetvsActual_executive = "targetachive/get_target_executive"
        val targetvsActual_head = "targetachive/get_target_head"
        val get_empbranch_allocation = "branchallocation/get_empbranch_allocation"
        val get_get_empbranch_orderdetails = "branchallocation/get_empbranch_orderdet"
        val branch_allocation_line_delete = "branchallocation/delete_orderlinedet"
        val cancel_order = "branchallocation/cancel_orderlinedet"
        val submit_order = "branchallocation/insert_orderlinedet"
        val get_customer_club_manager = "customer_club/get_manageremp_cusclub"
        val get_customer_club_executive = "customer_club/get_exeemp_cusclub"
        val get_customer_club_details = "customer_club/get_emp_cusclub_drill"
        val get_empexe_cusclub_drill = "customer_club/get_empexe_cusclub_drill"
        val get_empfarm_allocation = "farmallocation/get_empfarm_allocation"
        val get_get_empfarm_orderdetails = "farmallocation/get_empfarm_orderdet"
        val submit_farmorder = "farmallocation/insert_orderlinefarmdet"
        val cancel_farmorder = "farmallocation/cancel_orderlinfarmedet"
        val farm_allocation_line_delete = "farmallocation/delete_farmorderlinedet"
        val resent_sms = "farmallocation/resent_sms_farm"
        val get_diffprice_lookup = "pricedeclaration/get_diffprice_lookup"
        val get_declare_diffprice = "pricedeclaration/get_declare_diffprice"
        val get_cancelreasonemp = "farmallocation/get_cancelreasonemp"
        val OrderCancel = "farmallocation/OrderCancel"
        val get_liftsmsview_region = "liftingotp/get_liftsmsview_region"
        val get_liftsmsview_order = "liftingotp/get_liftsmsview_order"
        val get_today_target_cluster = "pricedeclaration/get_today_target_cluster"
        val save_today_target_cluster = "pricedeclaration/save_today_target_cluster"
        val get_exception_regions = "exceptionorder/get_exception_regions"
        val get_exception_order = "exceptionorder/get_exception_order"
        val update_exception_order = "exceptionorder/update_exception_order"
        val get_so_regions = "soorder/get_so_regions"
        val get_so_order = "soorder/get_so_order"
        val update_so_order = "soorder/update_so_order"
        val get_bookorder_regions = "bookorderview/get_bookorder_regions"
        val get_bookorder_order = "bookorderview/get_bookorder_order"
        val update_bookorder = "bookorderview/update_bookorder"
        val update_pricelistorder = "bookorderview/update_pricelistorder"
        val update_downtrentorder = "bookorderview/update_downtrentorder"
    }

    private interface ApiInterface {

        // ******* Update Family Member ******* //

        @Multipart
        @POST
        fun updateMember(
                @Url url: String,
                @HeaderMap mHeader: HashMap<String, String>,
                @Part file: MultipartBody.Part?,
                @QueryMap mParam: HashMap<String, String>
        ): Call<ResponseBody>

        @POST
        fun updateMember(
                @Url url: String,
                @HeaderMap mHeader: HashMap<String, String>,
                @QueryMap mParam: HashMap<String, String>
        ): Call<ResponseBody>

    }


    companion object {

        //  private var retrofit: Retrofit? = null

        /*   private fun fillCommons(c: Context, client: RestClient) {
            client.addHeader("Authorization", LocalStorageSP[c, "auth_key", ""]!!)
        }*/

        /*   private fun fillCommons(c: Context, client: JsonRestClient) {
            client.header("Authorization", LocalStorageSP[c, "auth_key", ""]!!)
            client.header("Content-Type", "application/json")
        }*/

        /*    private fun getClient(): Retrofit {

            val okHttpClient = OkHttpClient.Builder().build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit as Retrofit
        }*/


        fun execute(c: Context, data: String, method: Int, case: String, model: Type): Boolean {
            try {
                // Generating Req
                val obj = JSONObject()

                val client = JsonRestClient(c, method, data, case.hashCode())
                client.header("Content-Type", "application/json; charset=utf-8")

                client.execute(c as ResponseListener, obj, model)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun execute(
                c: Context,
                listener: ResponseListener,
                method: Int,
                case: String,
                model: Type, jsonParam: JSONObject
        ): Boolean {
            try {
                val client = JsonRestClient(c, method, constructUrl(case), case.hashCode())
                client.header("Content-Type", "application/json; charset=utf-8")
                client.header(HttpHeaders.AUTHORIZATION, "Bearer "+ LocalStorageSP.getLoginUser(c).TKEY)
                client.execute(listener, jsonParam, model)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun plainExecute(
                c: Context,
                listener: PlainResponseListener,
                method: Int,
                case: String,
                jsonParam: JSONObject
        ): Boolean {
            try {
                val client = JsonRestClient(c, method, constructUrl(case), case.hashCode())
                client.header("Content-Type", "application/json; charset=utf-8")
                client.header(HttpHeaders.AUTHORIZATION, "Bearer "+ LocalStorageSP.getLoginUser(c).TKEY)
                client.executePlain(listener, jsonParam)
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
            return true
        }

       

    }
}