package com.suguna.lbsalesperson.priceallocation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.Utils.LocalStorageSP
import com.suguna.lbsalesperson.base.BaseActivity
import com.suguna.lbsalesperson.priceallocation.PriceViewAllocationActivity_demo

class PriceAllocationSelectionActivity : BaseActivity() {

    var declarePriceLay: LinearLayout? = null
    var viewPriceLay: LinearLayout? = null
    var lyt_local_price: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_allocation_selection)
        setToolBar()
        init()
    }

    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this);
        super.onBackPressed()
    }

    fun setToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Price Declaration"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun clickListener() {

        declarePriceLay?.setOnClickListener {
            if(LocalStorageSP.getLoginUser(myContext).PROD_NAME.equals("0")) {
                val i = Intent(
                    myContext,
                    PriceAllocationActivity::class.java
                ).putExtra(getString(R.string.is_editable), true)
                startActivity(i)
            }
            else
            {
                val i = Intent(
                    myContext,
                    DiffPriceAllocationActivity::class.java
                ).putExtra(getString(R.string.is_editable), true)
                startActivity(i)
            }

        }

        viewPriceLay?.setOnClickListener {
            if(LocalStorageSP.getLoginUser(myContext).PROD_NAME.equals("0")) {
                val i = Intent(
                    myContext,
                    PriceAllocationActivity::class.java
                ).putExtra(getString(R.string.is_editable), false)
                startActivity(i)
            }
            else
            {
                val i = Intent(
                    myContext,
                    DiffPriceAllocationActivity::class.java
                ).putExtra(getString(R.string.is_editable), false)
                startActivity(i)
            }

        }
        lyt_local_price?.setOnClickListener {

            val i = Intent(myContext, ConcessionentryActivity::class.java).putExtra(getString(R.string.is_editable), true)
            startActivity(i)

        }
    }

    override fun init() {
        declarePriceLay = findViewById(R.id.lyt_declare_price)
        viewPriceLay = findViewById(R.id.lyt_view_price)
        lyt_local_price = findViewById(R.id.lyt_local_price)
        clickListener()
    }
}