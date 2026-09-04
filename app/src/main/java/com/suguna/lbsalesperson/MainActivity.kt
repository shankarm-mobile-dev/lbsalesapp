package com.suguna.lbsalesperson

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.suguna.lbsalesperson.Adapter.DashboardAdapter
import com.suguna.lbsalesperson.DAO.Sug_lblogin
import com.suguna.lbsalesperson.POJO.DasboardMenus
import com.suguna.lbsalesperson.R.string.label_feedback
import com.suguna.lbsalesperson.Todaytarget.Todaytarget
import com.suguna.lbsalesperson.Utils.LocalStorageSP.getLoginUser
import com.suguna.lbsalesperson.bookOrdersView.bookOrderViewActivity
import com.suguna.lbsalesperson.branchallocation.activity.BranchAllocationActivity
import com.suguna.lbsalesperson.customerclub.activity.CustomerClubActivity
import com.suguna.lbsalesperson.diffpriceallocation.DiffPriceallocation
import com.suguna.lbsalesperson.diffpriceallocation.Model.DiffnameModel
import com.suguna.lbsalesperson.exceptionOrders.exceptionViewActivity
import com.suguna.lbsalesperson.soOrders.soViewActivity
import com.suguna.lbsalesperson.farmallocation.FarmAllocationActivity
import com.suguna.lbsalesperson.feedback.activity.FeedBackActivity
import com.suguna.lbsalesperson.liftotpview.LiftingOtpViewActivity
import com.suguna.lbsalesperson.listener.DashBoardClickListener
import com.suguna.lbsalesperson.priceallocation.activity.PriceAllocationSelectionActivity
import com.suguna.lbsalesperson.targetActual.activity.TargetVsActualActivity


class MainActivity : AppCompatActivity(), DashBoardClickListener {
    var lyt_pricelist: LinearLayout? = null
    var lyt_ordersummary: LinearLayout? = null
    var lyt_outstanding: LinearLayout? = null
    var lyt_feedback: LinearLayout? = null
    var sharedpreferences: SharedPreferences? = null
    var lbloginDAO: Sug_lblogin? = null
    var mDashboardRV: RecyclerView? = null

    var mRootLayout: ConstraintLayout? = null

    var mAdapter: DashboardAdapter? = null
    lateinit var myContext: Context

    var mDiffnameList: ArrayList<DiffnameModel> = arrayListOf()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_new)

        myContext = this
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        sharedpreferences = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE)
        lbloginDAO = Sug_lblogin(this)


        /*  setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lb Sales Person");*/
        On_FieldDeclartion()
        setRecycleViewAdapter()
        On_FieldClick()

    }

    private fun On_FieldDeclartion() {
        lyt_pricelist = findViewById<View>(R.id.lyt_pricelist) as LinearLayout
        lyt_ordersummary = findViewById<View>(R.id.lyt_ordersummary) as LinearLayout
        lyt_outstanding = findViewById<View>(R.id.lyt_outstanding) as LinearLayout
        lyt_feedback = findViewById<View>(R.id.lyt_feedback) as LinearLayout
        mDashboardRV = findViewById<View>(R.id.dasboard_menu_RV) as RecyclerView
        mRootLayout = findViewById<View>(R.id.root_lay) as ConstraintLayout
        val user_name = findViewById<View>(R.id.user_name) as TextView
        var custnumber: String? = ""
        custnumber = sharedpreferences!!.getString(Name, "")
        var USERID: String? = ""
        val TKEY = ""
        USERID = lbloginDAO!!.getCUSTNAME(custnumber)
        user_name.text = USERID
    }

    fun setRecycleViewAdapter() {


        val myMenuTitleArray = myContext.resources.getStringArray(R.array.menu_title)
        val myMenuImageArray = myContext.resources.obtainTypedArray(R.array.menu_image)

        var mMenus: ArrayList<DasboardMenus> = arrayListOf()

        var k = 0
        for (i in myMenuTitleArray) {
           /* var custnumber: String? = ""
            custnumber = sharedpreferences!!.getString(Name, "")
            if(!(custnumber.equals("036112") || custnumber.equals("15355") || custnumber.equals("000935")))
            {*/
            if (getLoginUser(myContext).USERTYPE.equals("1")) {
                val vv: DasboardMenus? = DasboardMenus()
                vv?.icons = myMenuImageArray.getResourceId(k, 0)
                vv?.title = myMenuTitleArray.get(k)
               if (!((vv?.title.equals(getString(R.string.label_branch_allocation))) || (vv?.title.equals(getString(R.string.label_farm_allocation))) || ((vv?.title.equals(getString(R.string.label_order_status)))))) {
                    //mMenus.add(vv!!)
                   if (!((vv?.title.equals(getString(R.string.label_diff_price_declaration))) || (vv?.title.equals(getString(R.string.label_exceptipn_order))) || (vv?.title.equals(getString(R.string.label_so_order))) || (vv?.title.equals(getString(R.string.label_book_order_view))))) {
                       if(getLoginUser(myContext).ledgerId.equals("2022"))
                       {
                           if (!(vv?.title.equals(getString(R.string.label_liftotp_view))) || !(vv?.title.equals(getString(R.string.label_exceptipn_order))) || !(vv?.title.equals(getString(R.string.label_so_order))) || !(vv?.title.equals(getString(R.string.label_book_order_view)))) {
                               mMenus.add(vv!!)
                           }
                       }
                       else
                       {
                           mMenus.add(vv!!)
                       }
                   }
                }
                if(!getLoginUser(myContext).FARM_WISE_AREA.equals("0"))
                {
                    if ((vv?.title.equals(getString(R.string.label_exceptipn_order))) || (vv?.title.equals(getString(R.string.label_book_order_view)))) {
                        mMenus.add(vv!!)
                    }
                }

                if(!getLoginUser(myContext).PROD_NAME.equals("0"))
                {
                    if ((vv?.title.equals(getString(R.string.label_diff_price_declaration)))) {
                        mMenus.add(vv!!)
                    }
                }
//                //if (!((vv?.title.equals(getString(R.string.label_branch_allocation))) )) {
//                    mMenus.add(vv!!)
//                }//
               // mMenus.add(vv!!)
            } else if (getLoginUser(myContext).USERTYPE.equals("2")) {
                val vv: DasboardMenus? = DasboardMenus()
                vv?.icons = myMenuImageArray.getResourceId(k, 0)
                vv?.title = myMenuTitleArray.get(k)
                if ( (vv?.title.equals(getString(R.string.label_feedback))) || (vv?.title.equals(getString(R.string.label_order_status)))) {
                    mMenus.add(vv!!)
                }
            }

            else if (getLoginUser(myContext).USERTYPE.equals("3")) {
                val vv: DasboardMenus? = DasboardMenus()
                vv?.icons = myMenuImageArray.getResourceId(k, 0)
                vv?.title = myMenuTitleArray.get(k)
                if ( (vv?.title.equals(getString(R.string.label_order_status)))) {
                    mMenus.add(vv!!)
                }
            }
            else if (getLoginUser(myContext).USERTYPE.equals("0")) {
                val vv: DasboardMenus? = DasboardMenus()
                vv?.icons = myMenuImageArray.getResourceId(k, 0)
                vv?.title = myMenuTitleArray.get(k)
                if (!( (vv?.title.equals(getString(R.string.label_price_declaration))) || (vv?.title.equals(getString(R.string.label_diff_price_declaration))) || ((vv?.title.equals(getString(R.string.label_order_status)))) || ((vv?.title.equals(getString(R.string.label_liftotp_view)))) || ((vv?.title.equals(getString(R.string.label_today_target)))) || ((vv?.title.equals(getString(R.string.label_exceptipn_order)))) || ((vv?.title.equals(getString(R.string.label_so_order)))) || ((vv?.title.equals(getString(R.string.label_book_order_view)))))) {
                    mMenus.add(vv!!)
                }
            } /*}else {
                val vv: DasboardMenus? = DasboardMenus()
                vv?.icons = myMenuImageArray.getResourceId(k, 0)
                vv?.title = myMenuTitleArray.get(k)
                if ((vv?.title.equals(getString(R.string.label_order_status)))) {
                    mMenus.add(vv!!)

                }
            }*/
            k++
        }


        val layoutManager = GridLayoutManager(myContext, 2)
        mDashboardRV?.layoutManager = layoutManager
        mAdapter = DashboardAdapter(myContext as FragmentActivity, mMenus, this)
        mDashboardRV?.adapter = mAdapter
    }


    private fun On_FieldClick() {
        lyt_pricelist!!.setOnClickListener {
            val i = Intent(this@MainActivity, PriceListActivity::class.java)
            startActivity(i)
        }
        lyt_ordersummary!!.setOnClickListener {
            val i = Intent(this@MainActivity, OrderSummaryMenu::class.java)
            startActivity(i)
        }
        lyt_outstanding!!.setOnClickListener {
            val i = Intent(this@MainActivity, OutstandingMenu::class.java)
            startActivity(i)
        }
        lyt_feedback!!.setOnClickListener {
            val i = Intent(this@MainActivity, FeedBackActivity::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        popup_save()
        return
    }

    fun popup_save() {
        val dialogBuilder = AlertDialog.Builder(
                this@MainActivity)
        val inflater = this@MainActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.layout_popup, null)
        dialogBuilder.setView(dialogView)
        /*
         * final Dialog dialog = new Dialog(MainActivity.this); // Include
         * dialog.xml file dialog.setContentView(R.layout.layout_popup);
         */
        // Set dialog title
      /*  val customfont = Typeface.createFromAsset(assets,
                "fonts/MORE.TTF")
        val customfont1 = Typeface.createFromAsset(assets,
                "fonts/MORE.TTF")*/
        // set values for custom dialog components - text, image and
        // button
        val txtheading = dialogView.findViewById<View>(R.id.txttitle) as TextView
        txtheading.text = "Confirm"
        val txtsub = dialogView.findViewById<View>(R.id.txtsubject) as TextView
        txtsub.text = "Are you sure to Leave from Live Bird Sales Person Application?"
        // txtsub.setText(Html.fromHtml("Are you sure to save candling egg details and the candling date & time is : <span style=\"color:#228B22\">"+str_receipt_date+" "+str_receipt_time+"</span >"));
        val txtok = dialogView.findViewById<View>(R.id.txtok) as TextView
        val txtcancel = dialogView.findViewById<View>(R.id.txtcancel) as TextView
        txtcancel.text = "NO"
        txtok.text = "YES"
     /*   txtheading.typeface = customfont
        txtsub.typeface = customfont1
        txtok.typeface = customfont
        txtcancel.typeface = customfont*/
        val b = dialogBuilder.create()
        b.show()
        // dialog.show();
        // if decline button is clicked, close the custom dialog
        txtcancel.setOnClickListener { // Close dialog
            // dialog.dismiss();
            b.dismiss()
        }
        txtok.setOnClickListener {

            finish()
            b.dismiss()
        }
    }

    companion object {
        const val MyPREFERENCES = "MyPrefs"
        const val Name = "name"
        const val Password = "pass"
    }


    override fun click(value: String, aView : ViewGroup) {

        when (value) {
            getString(label_feedback) -> {
                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, FeedBackActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*val i = Intent(this@MainActivity, FeedBackActivity::class.java)
                startActivity(i)*/
            }
            getString(R.string.label_order_summary) -> {
                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, OrderSummaryMenu::class.java)
                inn.putExtra("userid", getLoginUser(myContext).USERTYPE)
                startActivity(inn, activityOptionsCompat.toBundle())

               /* val i = Intent(this@MainActivity, OrderSummaryMenu::class.java)
                startActivity(i)*/
            }
            getString(R.string.label_outStanding) -> {
                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, OutstandingMenu::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())
              /*
                val i = Intent(this@MainActivity, OutstandingMenu::class.java)
                startActivity(i)*/
            }
            getString(R.string.label_price_list) -> {
                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, PriceListActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

               /* val i = Intent(this@MainActivity, PriceListActivity::class.java)
                startActivity(i)*/
            }

            getString(R.string.label_branch_allocation) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, BranchAllocationActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

              /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                startActivity(i)*/
            }

            getString(R.string.label_farm_allocation) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, FarmAllocationActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                  startActivity(i)*/
            }

            getString(R.string.label_target_vs_achieved) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, TargetVsActualActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

              /*  val i = Intent(this@MainActivity, TargetVsActualActivity::class.java)
                startActivity(i)*/
            }
            getString(R.string.label_price_declaration) -> {


                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, PriceAllocationSelectionActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*val i = Intent(this@MainActivity, PriceAllocationSelectionActivity::class.java)
                startActivity(i)*/
            } getString(R.string.label_customer_club) -> {


            val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")


            val inn = Intent(this, CustomerClubActivity::class.java)

            startActivity(inn, activityOptionsCompat.toBundle())



              /*  val i = Intent(this@MainActivity, CustomerClubActivity::class.java)
                startActivity(i)*/
            }
            getString(R.string.label_diff_price_declaration) -> {


                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, DiffPriceallocation::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*val i = Intent(this@MainActivity, PriceAllocationSelectionActivity::class.java)
                startActivity(i)*/
            }
           getString(R.string.label_order_status) -> {
               if (getLoginUser(myContext).USERTYPE.equals("3")) {

                   val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                       this,
                       aView,
                       "toolbar_transition"
                   )
                   val inn = Intent(this, MPchart::class.java)

                   startActivity(inn, activityOptionsCompat.toBundle())
               }
               else
               {
                   val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                       this,
                       aView,
                       "toolbar_transition"
                   )
                   val inn = Intent(this, MPchart_manager::class.java)

                   startActivity(inn, activityOptionsCompat.toBundle())
               }


            }

            getString(R.string.label_liftotp_view) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, LiftingOtpViewActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                  startActivity(i)*/
            }
            getString(R.string.label_today_target) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, Todaytarget::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                  startActivity(i)*/
            }

            getString(R.string.label_exceptipn_order) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, exceptionViewActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                  startActivity(i)*/
            }
            getString(R.string.label_so_order) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, soViewActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())
            }
            getString(R.string.label_book_order_view) -> {

                val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, aView, "toolbar_transition")
                val inn = Intent(this, bookOrderViewActivity::class.java)
                startActivity(inn, activityOptionsCompat.toBundle())

                /*  val i = Intent(this@MainActivity, BranchAllocationActivity::class.java)
                  startActivity(i)*/
            }

        }
    }

}