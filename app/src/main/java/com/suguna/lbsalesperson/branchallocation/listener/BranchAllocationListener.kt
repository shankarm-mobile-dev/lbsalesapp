package com.suguna.lbsalesperson.branchallocation.listener

import android.view.View

interface BranchAllocationListener {

    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
    fun onSubmit()
}