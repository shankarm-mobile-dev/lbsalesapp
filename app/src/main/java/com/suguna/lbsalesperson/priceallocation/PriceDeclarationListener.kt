package com.suguna.lbsalesperson.priceallocation

import android.view.View

interface PriceDeclarationListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
    fun onSubmit(value:Boolean,branch_code:String)
}