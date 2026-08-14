package com.suguna.lbsalesperson.fragmentmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.suguna.lbsalesperson.R



class LBFragmentManager
/**
 * Constructor to Initiate fragment manager
 *
 * @param aContext FragmentActivity
 */
    (private val myContext: FragmentActivity) {

    val backstackCount: Int
        get() {
            val aFragmentManager = myContext.supportFragmentManager
            return aFragmentManager.backStackEntryCount
        }

    /**
     * Update the Current Fragment by passing the below parameters
     *
     * @param aFragment Fragment
     * @param tag       String
     * @param aBundle   Bundle
     */
    fun addContent(aFragment: Fragment, tag: String, aBundle: Bundle?) {
        try {

            Log.e("TAG Screen name", tag)

            // Initialise Fragment Manager
            val aFragmentManager = myContext.supportFragmentManager

            // Initialise Fragment Transaction
            val aTransaction = aFragmentManager.beginTransaction()

            if (aBundle != null) {
                aFragment.arguments = aBundle
            }

            // Add the selected fragment
            aTransaction.add(R.id.frame_container, aFragment, tag)

            // add the tag to the backstack
            aTransaction.addToBackStack(tag)


            //Repalce New Fragment with old
            //            aTransaction.replace(R.id.frame_container,aFragment,tag);
            //            aTransaction.addToBackStack(null);


            // Commit the Fragment transaction
            aTransaction.commit()

            // aTransaction.commitAllowingStateLoss();

            myLastTag = tag

            Log.i("LastTag", myLastTag)

        } catch (e: StackOverflowError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun addContent(aFragment: Fragment, tag: String, start: Int, end: Int, aBundle: Bundle?) {
        try {

            Log.e("TAG Screen name", tag)

            // Initialise Fragment Manager
            val aFragmentManager = myContext.supportFragmentManager

            // Initialise Fragment Transaction
            val aTransaction = aFragmentManager.beginTransaction()
            aTransaction.setCustomAnimations(start, end)

            if (aBundle != null) {
                aFragment.arguments = aBundle
            }

            // Add the selected fragment
            aTransaction.add(R.id.frame_container, aFragment, tag)

            // add the tag to the backstack
            aTransaction.addToBackStack(tag)


            //Repalce New Fragment with old
            //            aTransaction.replace(R.id.frame_container,aFragment,tag);
            //            aTransaction.addToBackStack(null);


            // Commit the Fragment transaction
            aTransaction.commit()

            // aTransaction.commitAllowingStateLoss();

            myLastTag = tag

            Log.i("LastTag", myLastTag)

        } catch (e: StackOverflowError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun replaceContent(aFragment: Fragment, tag: String, aBundle: Bundle?) {
        try {

            Log.e("TAG Screen name", tag)


            // Initialise Fragment Manager
            val aFragmentManager = myContext.supportFragmentManager

            // Initialise Fragment Transaction
            val aTransaction = aFragmentManager.beginTransaction()


            if (aBundle != null) {
                aFragment.arguments = aBundle
            }

            //Repalce New Fragment with old
            aTransaction.replace(R.id.frame_container, aFragment, tag)
            aTransaction.addToBackStack(tag)


            // Commit the Fragment transaction
            aTransaction.commit()

            // aTransaction.commitAllowingStateLoss();

            myLastTag = tag

            Log.i("LastTag", myLastTag)

        } catch (e: StackOverflowError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @SuppressLint("CommitTransaction")
    fun refreshContent(aFragment: Fragment) {
        try {
            // Initialise Fragment Manager
            val aFragmentManager = myContext.supportFragmentManager

            // Initialise Fragment Transaction
            val aTransaction = aFragmentManager.beginTransaction()

            aTransaction.detach(aFragment).attach(aFragment)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Clear All Fragments
     */
    fun clearAllFragments() {
        try {
            val aFragmentManager = myContext.supportFragmentManager
            for (i in 0 until aFragmentManager.backStackEntryCount) {
                aFragmentManager.popBackStack()
            }
        } catch (e: StackOverflowError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun oneStepBack() {
        val fts = myContext.supportFragmentManager.beginTransaction()
        val fragmentManager = myContext.supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 2) {
            fragmentManager.popBackStackImmediate()
            fts.commit()
        }
    }


    fun removeFragment(aCount: Int) {
        val aFragmentManager = myContext.supportFragmentManager
        for (i in 0 until aCount) {
            aFragmentManager.popBackStack()
        }

    }

    fun onBackPress() {
        val count = myContext.supportFragmentManager.backStackEntryCount
        if (count == 1) {
            myContext.onBackPressed()
        } else {
            myContext.supportFragmentManager.popBackStack()
        }
    }

    companion object {

        /**
         * Last fragment tag
         */
        var myLastTag = ""
    }
}
