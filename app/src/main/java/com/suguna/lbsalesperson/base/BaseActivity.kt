package com.suguna.lbsalesperson.base

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.suguna.lbsalesperson.Utils.Utility




abstract class BaseActivity : AppCompatActivity() {



    lateinit var myContext: BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = this

    }



    abstract fun clickListener()


    abstract fun init()

    fun checkInternet(): Boolean {
        return Utility.isInternetAvailable(this)
    }

    fun getETValue(aEditText: EditText?): String {
        return aEditText?.text?.toString()?.trim { it <= ' ' } ?: ""
    }

    fun getTXTValue(aTextText: TextView?): String {
        return aTextText?.text?.toString()?.trim { it <= ' ' } ?: ""
    }

}
