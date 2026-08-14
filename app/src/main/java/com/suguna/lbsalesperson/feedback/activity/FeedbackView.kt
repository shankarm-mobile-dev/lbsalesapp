package com.suguna.lbsalesperson.feedback.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cloudist.acplibrary.ACProgressConstant
import cc.cloudist.acplibrary.ACProgressFlower
import com.suguna.lbsalesperson.Adapter.ListViewAdapter_feedback
import com.suguna.lbsalesperson.DAO.Sug_lblogin
import com.suguna.lbsalesperson.Database.DatabaseUpDown
import com.suguna.lbsalesperson.POJO.SugFeedBackDetail_POJO
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.ICaster
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.Utils.NetworkUtil
import com.suguna.lbsalesperson.branchallocation.adapter.BranchAllocationRecyclerAdapter
import com.suguna.lbsalesperson.branchallocation.model.BranchAllocationModel
import com.suguna.lbsalesperson.feedback.adapter.FeedbackResponseAdapter
import com.suguna.lbsalesperson.feedback.model.FeedbackResponseModel
import com.suguna.lbsalesperson.listener.ClickListener
import java.util.*
import kotlin.collections.ArrayList

class FeedbackView : AppCompatActivity(), ClickListener {
    private val progress: ProgressDialog? = null
    var databaseUpDown: DatabaseUpDown? = null
    var sharedpreferences: SharedPreferences? = null
    var lbloginDAO: Sug_lblogin? = null
    var Feedback_POJOlist: List<SugFeedBackDetail_POJO>? = null
    var txt_transdate: TextView? = null
    var lyt_add: LinearLayout? = null
    var alertDialog: AlertDialog? = null
    var list_ministatment: ListView? = null
    var adapter: ListViewAdapter_feedback? = null
    var emptyText: TextView? = null
    var netutil: NetworkUtil? = null
    var id = ""
    var txt_type: TextView? = null
    var txt_orderref: TextView? = null
    var txt_des: TextView? = null
    var txt_status: TextView? = null
    var txt_date: TextView? = null
    var txt_rating: TextView? = null
    var txt_finalcmd: TextView? = null
    var rating_img: ImageView? = null
    var edit_feedback: EditText? = null
    var btn_submit: Button? = null
    var selected_feed = ""
    var selected_orderref = ""
    var feedback = ""
    var mMenuAdapter: FeedbackResponseAdapter? = null
    var mMenuRecycler: RecyclerView? = null

    var customerName: TextView? = null
    var liftingDate: TextView? = null
    var liftingLocation: TextView? = null

    var responseLay: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedbackview)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "FeedBack"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        lbloginDAO = Sug_lblogin(this)
        databaseUpDown = DatabaseUpDown(this)
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE)
        netutil = NetworkUtil(this)
        On_Fielddeclartion()
        On_Fieldclick()
        if (netutil!!.isOnline) {
            getFeedbackdetails_asyn().execute()
        } else {
            ICaster.Toast_msg_internet(this@FeedbackView,
                    "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                    ICaster.notify)

            // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();
        }
    }

    private fun On_Fielddeclartion() {
        liftingDate = findViewById<View>(R.id.txt_lifting_branch) as TextView
        liftingLocation = findViewById<View>(R.id.txt_lifting_date) as TextView
        responseLay = findViewById<View>(R.id.responseLay) as LinearLayout
        customerName = findViewById<View>(R.id.txt_customer_name) as TextView

        txt_date = findViewById<View>(R.id.txt_date) as TextView
        edit_feedback = findViewById<View>(R.id.edit_feedback) as EditText
        btn_submit = findViewById<View>(R.id.btn_submit) as Button
        txt_type = findViewById<View>(R.id.txt_type) as TextView
        txt_orderref = findViewById<View>(R.id.txt_orderref) as TextView
        txt_des = findViewById<View>(R.id.txt_des) as TextView
        txt_status = findViewById<View>(R.id.txt_status) as TextView
        txt_rating = findViewById<View>(R.id.txt_rating) as TextView
        txt_finalcmd = findViewById<View>(R.id.txt_finalcmd) as TextView
        rating_img = findViewById<View>(R.id.rating_img) as ImageView
        mMenuRecycler = findViewById<View>(R.id.recycleview) as RecyclerView
        if (intent.extras != null) {
            id = intent.getStringExtra("id")!!
        }
    }

    private fun On_Fieldclick() {
        btn_submit!!.setOnClickListener {
            feedback = edit_feedback!!.text.toString().trim { it <= ' ' }
            if (feedback == "") {
                ICaster.Toast_msg_bottom(this@FeedbackView,
                        "Feedback is empty ", Toast.LENGTH_SHORT,
                        ICaster.notify)
                //Toast.makeText(getApplicationContext(),"Feedback is empty",Toast.LENGTH_SHORT).show();
            } else {
                if (netutil!!.isOnline) {
                    saveFeedbackdetails_asyn().execute()
                } else {
                    ICaster.Toast_msg_internet(this@FeedbackView,
                            "Internet Connection seems to be offline! ", Toast.LENGTH_SHORT,
                            ICaster.notify)
                    // Toast.makeText(getApplicationContext(),"Internet Connection seems to be offline! ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
        return
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class getFeedbackdetails_asyn : AsyncTask<String?, String?, Boolean>() {
        var dialog: ACProgressFlower? = null
        override fun onPreExecute() {
            super.onPreExecute()
            /* progress = new ProgressDialog(FeedBack.this);
            SpannableString spannableString = new SpannableString(
                    "Get Customer Feedback");
            Typeface typeFace = Typeface.createFromAsset(getAssets(),
                    "fonts/CALIBRIB.TTF");
            spannableString.setSpan(new RelativeSizeSpan(1.0f), 0,
                    spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new CustomTypefaceSpan("", typeFace), 0,
                    spannableString.length(), 0);
            progress.setMessage(spannableString);
            // progress.setMessage("Validate Login");
            progress.setCancelable(false);
            // Show progressdialog
            progress.setIndeterminate(true);
            progress.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.circular_progress_bar));
            progress.show();*/dialog = ACProgressFlower.Builder(this@FeedbackView)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build()
            dialog!!.setCancelable(false)
            dialog!!.show()
        }

        protected override fun doInBackground(vararg params: String?): Boolean {
            // TODO Auto-generated method stub
            val download = false
            var custnumber: String? = ""
            custnumber = sharedpreferences!!.getString(Name, "")
            var USERID: String? = ""
            var TKEY: String? = ""
            USERID = lbloginDAO!!.getUSERID(custnumber)
            TKEY = lbloginDAO!!.getTKEY(custnumber)
            Feedback_POJOlist = ArrayList()
            Feedback_POJOlist = databaseUpDown!!.get_Feedbackfull_details_server(USERID,
                    TKEY, custnumber, id)
            return true
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            dialog!!.dismiss()
            if (Feedback_POJOlist!!.size > 0) {
                var lsloginPOJO: SugFeedBackDetail_POJO
                for (i in Feedback_POJOlist!!.indices) {
                    lsloginPOJO = Feedback_POJOlist!![i]
                    txt_type!!.text = lsloginPOJO.feedbacK_TYPE
                    txt_orderref!!.text = lsloginPOJO.referencE_NO
                    txt_des!!.text = lsloginPOJO.feedbacK_DESC
                    txt_status!!.text = lsloginPOJO.referencE_STATUS
                    txt_date!!.text = lsloginPOJO.feedbacK_DATE
                    selected_orderref = lsloginPOJO.feedbacK_ID

                    liftingDate!!.text = lsloginPOJO.locatioN_NAME
                    liftingLocation!!.text = lsloginPOJO.liftinG_DATE
                    customerName!!.text = lsloginPOJO.customeR_NAME
                    if (lsloginPOJO.rating == "0") {
                        txt_rating!!.text = " No"
                    } else {
                        txt_rating!!.text = " " + lsloginPOJO.rating + " / 5"
                    }
                    rating_img!!.setImageResource(getimage(lsloginPOJO.rating))
                    if (lsloginPOJO.alloW_UPDATE.equals("N", ignoreCase = true)) {
                        btn_submit!!.visibility = View.GONE
                        txt_finalcmd!!.visibility = View.VISIBLE
                        txt_finalcmd!!.text = lsloginPOJO.finaL_COMMENTS
                        edit_feedback!!.visibility = View.GONE
                        responseLay!!.visibility = View.GONE
                        setRecycleViewAdapter(lsloginPOJO.getmResponseArray())
                    } else if (lsloginPOJO.alloW_UPDATE.equals("Y", ignoreCase = true)) {

                        for (i in lsloginPOJO.getmResponseArray().indices) {

                            if (lsloginPOJO.getmResponseArray().get(i).userId.equals(LocalStorageSP.getLoginUser(this@FeedbackView).USERNAME)) {
                                lsloginPOJO.getmResponseArray().removeAt(i)
                            }
                        }
                        setRecycleViewAdapter(lsloginPOJO.getmResponseArray())
                    }

                    // txt_response.setText(lsloginPOJO.getFINAL_COMMENTS());
                }
            } else {
            }
        }


    }

    private fun getimage(chicks: String): Int {
        var loader = R.drawable.smile6
        val rating = chicks.toFloat()
        if (rating == 0f) {
            loader = R.drawable.smile6
        } else if (rating > 0 && rating <= 1) {
            loader = R.drawable.smile1
        } else if (rating > 1 && rating <= 2) {
            loader = R.drawable.smile2
        } else if (rating > 2 && rating <= 3) {
            loader = R.drawable.smile3
        } else if (rating > 3 && rating <= 4) {
            loader = R.drawable.smile4
        } else if (rating > 4 && rating <= 5) {
            loader = R.drawable.smile5
        }
        return loader
    }

    inner class saveFeedbackdetails_asyn : AsyncTask<String?, String?, Boolean>() {
        var dialog: ACProgressFlower? = null
        override fun onPreExecute() {
            super.onPreExecute()
            /*  progress = new ProgressDialog(Addfeedback.this);
            SpannableString spannableString = new SpannableString(
                    "Save Feedback Details");
            Typeface typeFace = Typeface.createFromAsset(getAssets(),
                    "fonts/CALIBRIB.TTF");
            spannableString.setSpan(new RelativeSizeSpan(1.0f), 0,
                    spannableString.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new CustomTypefaceSpan("", typeFace), 0,
                    spannableString.length(), 0);
            progress.setMessage(spannableString);
            // progress.setMessage("Validate Login");
            progress.setCancelable(false);
            // Show progressdialog
            progress.setIndeterminate(true);
            progress.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.circular_progress_bar));
            progress.show();*/dialog = ACProgressFlower.Builder(this@FeedbackView)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .fadeColor(Color.DKGRAY).build()
            dialog!!.setCancelable(false)
            dialog!!.show()
        }

        protected override fun doInBackground(vararg params: String?): Boolean {
            // TODO Auto-generated method stub
            var download = false
            var custnumber: String? = ""
            custnumber = sharedpreferences!!.getString(Name, "")
            var USERID: String? = ""
            var TKEY: String? = ""
            USERID = lbloginDAO!!.getUSERID(custnumber)
            TKEY = lbloginDAO!!.getTKEY(custnumber)
            val str = selected_orderref
            val arrOfStr = str.split("-".toRegex()).toTypedArray()
            var result = ""
            result = databaseUpDown!!.save_feedback_details_server(USERID,
                    TKEY, custnumber, selected_orderref, feedback)
            if (result == "true") {
                download = true
            }
            return download
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            dialog!!.dismiss()
            if (result) {
                ICaster.Toast_msg_bottom(this@FeedbackView,
                        "Successfully Feedback Submited ", Toast.LENGTH_SHORT,
                        ICaster.notify)
                // Toast.makeText(getApplicationContext(),"Successfully Feedback Submited",Toast.LENGTH_SHORT).show();
                finish()
            } else {
                ICaster.Toast_msg_bottom(this@FeedbackView,
                        "Failed to submit Feedback Detail ", Toast.LENGTH_SHORT,
                        ICaster.notify)
                // Toast.makeText(getApplicationContext(),"Failled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    companion object {
        const val MyPREFERENCES = "MyPrefs"
        const val Name = "name"
        const val Password = "pass"
    }


    private fun setRecycleViewAdapter(data: ArrayList<FeedbackResponseModel>) {

        mMenuRecycler!!.setHasFixedSize(true)
        mMenuRecycler!!.setLayoutManager(LinearLayoutManager(this))
        mMenuRecycler!!.setNestedScrollingEnabled(true)

        mMenuAdapter = FeedbackResponseAdapter(this, data, this)
        mMenuRecycler!!.adapter = mMenuAdapter
    }

    override fun click(value: String) {

    }
}