package com.suguna.lbsalesperson.Utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.google.android.datatransport.runtime.backends.BackendResponse.ok

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.suguna.lbsalesperson.Adapter.SingleChoiceAdapter
import com.suguna.lbsalesperson.Adapter.TLBottomOptionAdapter
import com.suguna.lbsalesperson.R
import com.suguna.lbsalesperson.R.*
import com.suguna.lbsalesperson.fragmentmanager.LBFragmentManager

import com.truelife.http.Response
import com.truelife.http.ResponseListener

import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList




object AppDialogs {
    private var progressDialog: Dialog? = null
    private var dialog: Dialog? = null
    public var custom_dialog: Dialog? = null
    private var selection_dialog: Dialog? = null
    private var bottom_dialog: BottomSheetDialog? = null
    private var mProgressBar: ProgressBar? = null
    private var mCountTV: TextView? = null

    /**
     * Simple interface can be implemented for confirm an action via dialogs
     */
    interface ConfirmListener {
        fun yes()
    }

    interface OptionListener : ConfirmListener {
        fun no()
    }



    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     * @param l
     */
    @SuppressLint("InlinedApi")
    fun optionalAction(
        c: Context,
        text: String,
        l: OptionListener?,
        yes: String? = "YES",
        no: String? = "NO"
    ) {
        val builder = AlertDialog.Builder(c, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
        builder.setTitle(c.resources.getString(string.app_name))
        builder.setMessage(text)

        builder.setPositiveButton(yes) { dialog, _ ->
            l?.yes()
            dialog.dismiss()
        }

        builder.setNegativeButton(no) { dialog, _ ->
            l?.no()
            dialog.dismiss()
        }

        builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
        builder.create().show()
    }


    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     * @param l
     */
    @SuppressLint("InlinedApi")
    fun confirmAction(c: Context, title: String, text: String, l: ConfirmListener?) {
        try {
            val alertDialog: AlertDialog
            val builder =
                AlertDialog.Builder(c, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
            builder.setTitle(title)
            builder.setMessage(text)

            builder.setPositiveButton(c.resources.getString(android.R.string.yes)) { dialog, which ->
                l?.yes()
                dialog.dismiss()
            }
            builder.setNegativeButton(c.resources.getString(android.R.string.no)) { dialog, which -> dialog.dismiss() }
            builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
            alertDialog = builder.create()
            alertDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     */
    @SuppressLint("InlinedApi")
    fun okAction(c: Context, text: String) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(c, android.R.style.Theme_Material_Light_Dialog)
        builder.setTitle(c.resources.getString(string.app_name))
        builder.setMessage(text)

        builder.setPositiveButton(c.resources.getString(android.R.string.ok)) { dialog, which -> dialog.dismiss() }

        builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
        alertDialog = builder.create()
        alertDialog.show()

    }


    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun showProgressDialog(context: Context) {
        hideProgressDialog()
        progressDialog = Dialog(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(layout.dialog_progress_custom, null)

        val progressBar = view.findViewById<ProgressBar>(id.dialog_progress_bar)
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, color.colorPrimary), PorterDuff.Mode.SRC_IN
        )

        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(view)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgressDialog() {

        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
        }
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("InlinedApi")
    fun showcustomView(
        context: Context,
        layout: Int,
        isCancelable: Boolean,
        fullScreen: Boolean
    ): View? {
        try {
            hidecustomView()
            custom_dialog = if (fullScreen)
                Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            else
                Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)

            custom_dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            custom_dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(layout, null)
            custom_dialog!!.setContentView(view)
            custom_dialog!!.setCancelable(isCancelable)
            custom_dialog!!.show()
            return view
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Bottom Dialog
     */
    fun showBottomView(context: Context, layout: Int, isCancelable: Boolean): View? {
        try {
            hidecustomView()
            bottom_dialog = BottomSheetDialog(context, style.BottomSheetDialog)
            val view = LayoutInflater.from(context).inflate(layout, null)
            bottom_dialog!!.setContentView(view)
            bottom_dialog!!.setCancelable(isCancelable)
            bottom_dialog!!.show()
            return view
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Show Ok Action Dialog
     * @param context Context
     * @param title Title
     * @param msg Message
     * @param action Positive Button Text
     * @param l ConfirmListener
     * @param isCancelable Close button view or hide
     */

    fun customOkAction(
        context: Context,
        title: String?,
        msg: String,
        action: String?,
        l: ConfirmListener?,
        isCancelable: Boolean
    ) {

        hidecustomView()

        val builder = AlertDialog.Builder(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_ok_action, null)

        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogMessage = view.findViewById(id.dialog_message) as TextView
        val dialogAction = view.findViewById(id.dialog_action_button) as TextView
        val dialogClose = view.findViewById(id.dialog_close_button) as ImageView

        builder.setCancelable(false)

        if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title
        dialogMessage.text = msg

        dialogAction.text = if ((action == null)) context.getString(string.label_ok) else action
        dialogAction.setOnClickListener {
            l?.yes()
            custom_dialog!!.dismiss()
        }

        dialogClose.setOnClickListener {
            custom_dialog!!.dismiss()
        }

        builder.setView(view)
        custom_dialog = builder.create()
        custom_dialog!!.show()
    }

    fun customOkActionFullScreen(
        context: Context,
        title: String?,
        msg: String,
        action: String?,
        l: ConfirmListener?,
        isCancelable: Boolean
    ) {

        hidecustomView()

        val builder =
            AlertDialog.Builder(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
        val view = LayoutInflater.from(context).inflate(layout.dialog_ok_action_full, null)

        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogMessage = view.findViewById(id.dialog_message) as TextView
        val dialogAction = view.findViewById(id.dialog_action_button) as TextView
        val dialogClose = view.findViewById(id.dialog_close_button) as ImageView

        builder.setCancelable(false)

        if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title
        dialogMessage.text = msg

        dialogAction.text = if ((action == null)) context.getString(string.label_ok) else action
        dialogAction.setOnClickListener {
            l?.yes()
            custom_dialog!!.dismiss()
        }

        dialogClose.setOnClickListener {
            custom_dialog!!.dismiss()
        }

        builder.setView(view)
        custom_dialog = builder.create()
        custom_dialog!!.show()
    }

    /**
     * Show Double Action Dialog
     * @param context Context
     * @param title Title
     * @param msg Message
     * @param Positiveaction Positive Button Text
     * @param Negativeaction Negative Button Text
     * @param l OptionListener
     * @param isCancelable Close button view or hide
     * @param isOptionable Negative button background gray
     */

    fun customDoubleAction(
        context: Context,
        title: String?,
        msg: String,
        Positiveaction: String?,
        Negativeaction: String?,
        l: OptionListener?,
        isCancelable: Boolean,
        isOptionable: Boolean
    ) {

        try {
            hidecustomView()

            val builder = AlertDialog.Builder(context, style.BottomSheetDialog)
            val view = LayoutInflater.from(context).inflate(layout.dialog_double_action, null)

            val dialogTitle = view.findViewById(id.dialog_title) as TextView
            val dialogMessage = view.findViewById(id.dialog_message) as TextView
            val dialogActionPositive = view.findViewById(id.dialog_action_positive) as TextView
            val dialogActionNegative = view.findViewById(id.dialog_action_negative) as TextView
            val dialogClose = view.findViewById(id.dialog_close_button) as ImageView
            custom_dialog
            builder.setCancelable(isCancelable)

            dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title
            dialogMessage.text = msg

            dialogActionPositive.text =
                if ((Positiveaction == null)) context.getString(string.label_ok) else Positiveaction
            dialogActionPositive.setOnClickListener {
                l?.yes()
                custom_dialog!!.dismiss()
            }

            if (isOptionable) {
                dialogActionNegative.setBackgroundResource(drawable.bg_circle_blue)
            }

            dialogActionNegative.text =
                if ((Negativeaction == null)) context.getString(string.cancel) else Negativeaction
            dialogActionNegative.setOnClickListener {
                l?.no()
                custom_dialog!!.dismiss()
            }

            if (isCancelable)
                dialogClose.visibility = View.GONE
            else dialogClose.visibility = View.GONE

            dialogClose.setOnClickListener {
                custom_dialog!!.dismiss()
            }

            builder.setView(view)
            custom_dialog = builder.create()
            custom_dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hidecustomView() {
        if (custom_dialog != null && custom_dialog!!.isShowing) {
            custom_dialog!!.dismiss()
        }
    }

    /**
     * @param context
     */
    fun hidecustomProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    /**
     * Hides the soft keyboard
     *
     * @param activity Activity
     */
    fun hideSoftKeyboard(activity: Activity, view: View) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Shows the soft keyboard
     *
     * @param a    Activity
     * @param view current EditText view
     */
    fun showSoftKeyboard(a: Activity, view: View) {
        val inputMethodManager =
            a.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    fun showSnackbar(
        activity: Activity,
        view: View,
        msg: String,
        action: String? = activity.resources.getString(string.settings)
    ) {
        val snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG).setAction(action) {
            startActivityForResult(
                activity,
                Intent(android.provider.Settings.ACTION_SETTINGS),
                0,
                null
            )
        }
        snackbar.setActionTextColor(Utility.getColor(activity, color.colorPrimary))
        snackbar.show()
    }

    fun showSnackbar(view: View, msg: String) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * showCustomSnackbar
     */
    fun showCustomSnackbar(context: Context, view: View, msg: String) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
        val layout = snackbar.view as Snackbar.SnackbarLayout
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val snackView = inflater.inflate(R.layout.inflate_custom_snackbar, null)

        val mLayout = snackView.findViewById<LinearLayout>(id.inflate_snackbar_layout)
        val mImage = snackView.findViewById<ImageView>(id.inflate_snackbar_image)
        val mText = snackView.findViewById<TextView>(id.inflate_snackbar_text)

        mText.text = msg

        layout.setPadding(0, 0, 0, 0)
        layout.addView(snackView, 0)
        snackbar.show()
    }

    /**
     *Short Toast
     */
    fun showToastshort(context: Context, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     *Long Toast
     */
    fun showToastlong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    fun showFromDatedialogwithToday(
        context: Context,
        datepickListner: DatePickerDialog.OnDateSetListener
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(context, datepickListner, year, month, dayOfMonth)
        dialog.datePicker.minDate = System.currentTimeMillis() - 60 * 60 * 1000
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "SELECT", dialog)
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", dialog)
        dialog.setTitle("")
        dialog.show()
    }

    @SuppressLint("InlinedApi")
    fun showTimedialog(context: Context, timepickListner: TimePickerDialog.OnTimeSetListener) {

        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val dialog = TimePickerDialog(
            context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_Alert,
            timepickListner,
            hourOfDay,
            minute,
            false
        )
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "SELECT", dialog)
        dialog.setTitle("")
        dialog.show()
    }

    /**
     * Single Choice Selection
     */
    fun showSingleChoice(
            context: Context,
            title: String?,
            items: ArrayList<String>,
            callback: SingleChoiceAdapter.Callback,
            isCancelable: Boolean
    ) {

        val builder = AlertDialog.Builder(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_single_selection, null)

        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogRecyclerView = view.findViewById(id.dialog_recycler) as RecyclerView
        val dialogClose = view.findViewById(id.dialog_close_button) as ImageView

        dialogRecyclerView.layoutManager = LinearLayoutManager(context)
        dialogRecyclerView.adapter = SingleChoiceAdapter(context, items, callback)

        builder.setCancelable(false)

        if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title

        dialogClose.setOnClickListener {
            selection_dialog!!.dismiss()
        }

        builder.setView(view)
        selection_dialog = builder.create()
        selection_dialog!!.show()
    }

    /**
     * Single Choice Selection
     */
    @SuppressLint("WrongViewCast")
    fun showSingleChoice(
        context: Context,
        title: String?,
        items: ArrayList<String>,
        callback: SingleChoiceAdapter.Callback,
        isCancelable: Boolean, isSearch: Boolean
    ) {

        val builder = AlertDialog.Builder(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_single_selection, null)

        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogRecyclerView = view.findViewById(id.dialog_recycler) as RecyclerView
        val dialogClose = view.findViewById(id.dialog_close_button) as ImageView
        val dialogSearchLay = view.findViewById(id.search) as LinearLayout
        val dialogSearch = view.findViewById(id.fragment_search) as EditText
        val dialogSearchCancel = view.findViewById(id.fragment_cancel) as TextView

        dialogRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = SingleChoiceAdapter(context, items, callback)
        dialogRecyclerView.adapter = adapter

        builder.setCancelable(isCancelable)

        if (isSearch) {
            dialogSearchCancel.setOnClickListener {
                hideSoftKeyboard(context as Activity, dialogClose)
                dialogSearch.text = null
                dialogSearchCancel.visibility = View.GONE
                dialogSearch.clearFocus()
            }
            dialogSearchLay.visibility = View.VISIBLE
            dialogSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    try {
                        if (count > 0)
                            dialogSearchCancel.visibility = View.VISIBLE

                        adapter.filter.filter(s!!.toString()) {
                            showNoData(
                                view,
                                it == 0,
                                 String.format("We couldn't find anything for\n'%s'", s)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        } else dialogSearchLay.visibility = View.GONE

        /*if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else */dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title

        dialogClose.setOnClickListener {
            selection_dialog!!.dismiss()
        }

        builder.setView(view)
        selection_dialog = builder.create()
        selection_dialog!!.show()
    }

    fun hideSingleChoice() {
        if (selection_dialog != null && selection_dialog!!.isShowing)
            selection_dialog!!.dismiss()
    }

    fun hideBottomDialog() {
        if (bottom_dialog != null && bottom_dialog!!.isShowing)
            bottom_dialog!!.dismiss()
    }


    /**
     * Show Toast Message
     *
     * @param context Context
     * @param desc    String
     */
    fun showToastDialog(context: Context, desc: String) {
        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show()
    }

    fun swipeRefColor(context: Context, swipe: SwipeRefreshLayout) {
        swipe.setColorSchemeColors(
            Utility.getColor(context, color.colorPrimaryDark),
            Utility.getColor(context, color.app_blue_light),
            Utility.getColor(context, color.grey_active)
        )
    }

    /**
     * No Data Layout
     *
     * @param view View
     * @param show show/hide
     * @param icon Image
     * @param msg Message to show
     */

    fun showNoData(view: View, show: Boolean) {
        showNoData(view, show, null, null)
    }

    fun showNoData(view: View, show: Boolean, icon: Int?) {
        showNoData(view, show, icon, null)
    }

    fun showNoData(view: View, show: Boolean, msg: String?) {
        showNoData(view, show, null, msg)
    }

    fun showNoData(view: View, show: Boolean, icon: Int?, msg: String?) {
        try {
            val mNodataLayout = view.findViewById<LinearLayout>(id.layout_nodata_layout)
            val mNodataImage = view.findViewById<ImageView>(id.layout_nodata_image)
            val mNodataMessage = view.findViewById<TextView>(id.layout_nodata_message)

            if (show) {
                mNodataLayout.visibility = View.VISIBLE
                icon?.let { mNodataImage.setImageResource(it) }
                msg?.let { mNodataMessage.text = msg }
            } else mNodataLayout.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showBottomMenu(
            context: Context,
            array: Array<String>,
            callback: TLBottomOptionAdapter.Callback,
            text: String?, title: String?, info: String?
    ) {
        bottom_dialog = BottomSheetDialog(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_bottom_more_option, null)

        val mInfoView = view.findViewById<LinearLayout>(id.dialog_info)

        val titleView = view.findViewById<TextView>(id.dialog_title)
        titleView.text = if ((title == null)) context.getString(string.app_name) else title

        val infoView = view.findViewById<TextView>(id.dialog_message)
        if (info != null) {
            infoView.text = info
            infoView.visibility = View.VISIBLE
            mInfoView.visibility = View.VISIBLE
        } else {
            infoView.visibility = View.GONE
            mInfoView.visibility = View.GONE
        }

        val dialogRecyclerView = view.findViewById(id.dialog_bottom_recycler) as RecyclerView
        dialogRecyclerView.layoutManager = LinearLayoutManager(context)
        dialogRecyclerView.adapter = TLBottomOptionAdapter(context, array, callback)

        val dialogDismiss = view.findViewById(id.dialog_bottom_dismiss) as TextView
        if (text != null)
            dialogDismiss.text = text

        dialogDismiss.setOnClickListener {
            bottom_dialog!!.dismiss()
        }

        bottom_dialog!!.setContentView(view)
        bottom_dialog!!.show()
    }

    fun showBottomMenu(
        context: Context,
        array: Array<String>,
        callback: TLBottomOptionAdapter.Callback,
        text: String?
    ) {
        bottom_dialog = BottomSheetDialog(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_bottom_more_option, null)

        val dialogRecyclerView = view.findViewById(id.dialog_bottom_recycler) as RecyclerView

        dialogRecyclerView.layoutManager = LinearLayoutManager(context)
        dialogRecyclerView.adapter = TLBottomOptionAdapter(context, array, callback)

        val dialogDismiss = view.findViewById(id.dialog_bottom_dismiss) as TextView
        if (text != null)
            dialogDismiss.text = text

        dialogDismiss.setOnClickListener {
            bottom_dialog!!.dismiss()
        }

        bottom_dialog!!.setContentView(view)
        bottom_dialog!!.show()
    }

    fun alertDialogwebViw(myContext: Context, aURL: String) {
        try {
            hidecustomView()
            val builder = AlertDialog.Builder(myContext, style.BottomSheetDialog)
            val view =
                LayoutInflater.from(myContext).inflate(layout.terms_policy_webview, null)
            builder.setCancelable(false)
            val aWebView = view.findViewById<View>(
                id.webView
            ) as WebView
            val aCloseBTN = view.findViewById<View>(
                id.webview_close_button
            ) as AppCompatButton
            aWebView.loadUrl(aURL)
            aWebView.clearCache(true)
            aWebView.clearHistory()
            aWebView.settings.javaScriptEnabled = true
            aWebView.settings.javaScriptCanOpenWindowsAutomatically = true
            aWebView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    view.loadUrl(url)
                    return true
                }
            }
            aCloseBTN.setOnClickListener {
                custom_dialog!!.dismiss()
            }
            builder.setView(view)
            custom_dialog = builder.create()
            custom_dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }





    fun showProgressBarCounting(context: Context) {
        hideProgressBarCounting()
        progressDialog = Dialog(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(layout.dialog_progress_custom_counting, null)

        val progressBar = view.findViewById<ProgressBar>(id.circularProgressbar)
        val progressTV = view.findViewById<TextView>(id.tv)
        mProgressBar = progressBar
        mCountTV = progressTV
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(context, color.app_blue_light), PorterDuff.Mode.SRC_IN
        )
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.setContentView(view)
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

    fun hideProgressBarCounting() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun setProgressCount(count: Int) {
        mCountTV!!.text = count.toString() + "%"
        mProgressBar!!.progress = count
    }

    /**
     * Show Ok Action Dialog
     * @param context Context
     * @param title Title
     * @param msg Message
     * @param action Positive Button Text
     * @param l ConfirmListener
     * @param isCancelable Close button view or hide
     *//*

    fun customLogoutAction(
        context: Context,
        title: String?,
        msg: String,
        action: String?,
        l: ConfirmListener?,
        isCancelable: Boolean
    ) {
        bottom_dialog = BottomSheetDialog(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_logout_action, null)
        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogMessage = view.findViewById(id.dialog_message) as TextView
        val dialogAction = view.findViewById(id.dialog_ok) as TextView
        val dialogClose = view.findViewById(id.dialog_cancel) as TextView


        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title
        dialogMessage.text = msg

        dialogAction.setOnClickListener {
            l?.yes()
            bottom_dialog!!.dismiss()
        }

        dialogClose.setOnClickListener {
            bottom_dialog!!.dismiss()
        }

        bottom_dialog!!.setContentView(view)
        bottom_dialog!!.show()
    }*/

    fun customSuccessAction(
        context: Context,
        title: String?,
        msg: String,
        action: String?,
        l: ConfirmListener?,
        isCancelable: Boolean
    ) {

        hidecustomView()

        val builder = AlertDialog.Builder(context, style.BottomSheetDialog)
        val view = LayoutInflater.from(context).inflate(layout.dialog_success_action, null)

        val dialogTitle = view.findViewById(id.dialog_title) as TextView
        val dialogMessage = view.findViewById(id.dialog_message) as TextView
        val dialogAction = view.findViewById(id.dialog_action_button) as TextView
        val dialogClose = view.findViewById(id.dialog_close_button) as ImageView

        builder.setCancelable(false)

        if (isCancelable)
            dialogClose.visibility = View.VISIBLE
        else dialogClose.visibility = View.GONE

        dialogTitle.text = if ((title == null)) context.getString(string.app_name) else title
        dialogMessage.text = msg

        dialogAction.text = if ((action == null)) context.getString(string.label_ok) else action
        dialogAction.setOnClickListener {
            l?.yes()
            custom_dialog!!.dismiss()
        }

        dialogClose.setOnClickListener {
            custom_dialog!!.dismiss()
        }

        builder.setView(view)
        custom_dialog = builder.create()
        custom_dialog!!.show()
    }


/*    fun show_feed_menu(
        callback: FeedMenuClickListener,
        feedList: PublicFeedModel.FeedList,
        myContext: Context
    ) {
        try {
            bottom_dialog = BottomSheetDialog(myContext, style.BottomSheetDialog)
            val view = LayoutInflater.from(myContext).inflate(layout.bottom_feed_menu_dialog, null)
            bottom_dialog!!.setContentView(view)
            bottom_dialog!!.setCancelable(true)
            bottom_dialog!!.setCanceledOnTouchOutside(true)

            val mUser = LocalStorageSP.getLoginUser(myContext)


            when (feedList.isFollow) {
                "0" -> {
                    view.unfollow_user_TET.setText("Follow this user")
                    view.unfollow_user_IMG.setImageResource(drawable.feed_follow_icon)
                }
                "1" -> {
                    view.unfollow_user_TET.setText("UnFollow this user")
                    view.unfollow_user_IMG.setImageResource(drawable.close_circle_white)
                }
            }


            if (feedList.userId.equals(mUser.mUserId)) {
                view.report_post_LAY.visibility = View.GONE
                view.report_post_LAY_view.visibility = View.GONE
                view.block_user_LAY.visibility = View.GONE
                view.block_user_LAY_view.visibility = View.GONE
                view.unfollow_user_LAY.visibility = View.GONE
                view.delete_post_LAY.visibility = View.VISIBLE
                view.delete_post_LAY_view.visibility = View.GONE
            } else {
                view.edit_post_LAY.visibility = View.GONE
                view.edit_post_LAY_view.visibility = View.GONE
                view.delete_post_LAY.visibility = View.GONE
                view.delete_post_LAY_view.visibility = View.GONE
                view.report_post_LAY.visibility = View.VISIBLE
                view.report_post_LAY_view.visibility = View.VISIBLE
                view.block_user_LAY.visibility = View.VISIBLE
                view.block_user_LAY_view.visibility = View.VISIBLE
                view.unfollow_user_LAY.visibility = View.VISIBLE
            }


            view.edit_post_LAY.setOnClickListener {
                callback.EditThisPost(feedList)
                bottom_dialog!!.dismiss()
            }
            view.hide_post_LAY.setOnClickListener {
                callback.HideThisPost(feedList)
                bottom_dialog!!.dismiss()
            }
            view.delete_post_LAY.setOnClickListener {
                callback.DeleteThisPost(feedList)
                bottom_dialog!!.dismiss()
            }
            view.report_post_LAY.setOnClickListener {
                callback.ReportThisPost(feedList)
                bottom_dialog!!.dismiss()
            }
            view.block_user_LAY.setOnClickListener {
                callback.BlockThisPost(feedList)
                bottom_dialog!!.dismiss()
            }
            view.unfollow_user_LAY.setOnClickListener {
                callback.FollowThisPost(feedList)
                bottom_dialog!!.dismiss()
            }


            bottom_dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun show_user_more(
        callback: UserMoreClickListener,
        id: String,
        myContext: Context
    ) {
        try {
            bottom_dialog = BottomSheetDialog(myContext, style.BottomSheetDialog)
            val view = LayoutInflater.from(myContext).inflate(layout.bottom_user_more_dialog, null)
            bottom_dialog!!.setContentView(view)
            bottom_dialog!!.setCancelable(true)
            bottom_dialog!!.setCanceledOnTouchOutside(true)

            val mUser = LocalStorageSP.getLoginUser(myContext)

            view.block_user.setOnClickListener {
                callback.BlockThisUser(id)
                bottom_dialog!!.dismiss()
            }
            view.report_user.setOnClickListener {
                callback.ReportThisUser(id)
                bottom_dialog!!.dismiss()
            }


            bottom_dialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun ShowShareDialog(
        feedList: PublicFeedModel.FeedList,
        myContext: Context
    ) {
        try {
            bottom_dialog = BottomSheetDialog(myContext, style.BottomSheetDialog)
            val view = LayoutInflater.from(myContext).inflate(layout.bottom_share_layout, null)
            bottom_dialog!!.setContentView(view)
            bottom_dialog!!.setCancelable(false)
            bottom_dialog!!.setCanceledOnTouchOutside(true)


            val mFriendsPickerItems: ArrayList<String> = ArrayList()
            val mPublicPickerItems: ArrayList<String> = ArrayList()

            mPublicPickerItems.add("Area")
            mPublicPickerItems.add("City")
            mPublicPickerItems.add("State")
            mPublicPickerItems.add("Country")
            mPublicPickerItems.add("Internationally")

            var selectedValue = ""
            var clubsList: ClubListShareModel = ClubListShareModel()
            var adapter: ClubShareMultiSelectionAdapter = ClubShareMultiSelectionAdapter(clubsList)
            mFriendsPickerItems.add("Friends")
            mFriendsPickerItems.add("Friends of friends")

            val aWhereToPostFriends =
                view.findViewById<View>(id.numberFriendsPicker) as NumberPicker
            val aWhereToPostPublic =
                view.findViewById<View>(id.numberPublicPicker) as NumberPicker
            val header_title = view.findViewById<View>(id.header_title) as TextView
            val header_Ques = view.findViewById<View>(id.question) as TextView
            val header_image = view.findViewById<View>(id.header_image) as ImageView
            val header_share_back = view.findViewById<View>(id.back_share) as ImageView
            val public = view.findViewById<View>(id.share_public) as LinearLayout
            val friends = view.findViewById<View>(id.share_friends) as LinearLayout
            val club = view.findViewById<View>(id.share_club) as LinearLayout
            val firstPage = view.findViewById<View>(id.first_page) as LinearLayout
            val clubRV = view.findViewById<View>(id.clubs_recycler) as RecyclerView
            val progress = view.findViewById<View>(id.progress_bar) as RelativeLayout
            val shareBtn = view.findViewById<View>(id.share_now_btn) as AppCompatButton
            val friends_public_lay =
                view.findViewById<View>(id.friends_public_lay) as LinearLayout
            val back = view.findViewById<View>(id.back_arrow) as ImageView
            val strarray = mFriendsPickerItems.toTypedArray()
            val strPubarray = mPublicPickerItems.toTypedArray()
            var source = ""

            friends.setOnClickListener {
                source = "2"
                header_Ques.text = myContext.getString(string.label_share_feed_public_friends)
                header_title.text = myContext.getString(string.label_share_friends)
                header_image.setImageResource(drawable.clubs_main_icon)
                firstPage.visibility = View.GONE
                clubRV.visibility = View.GONE
                friends_public_lay.visibility = View.VISIBLE
                aWhereToPostPublic.visibility = View.GONE
                aWhereToPostFriends.visibility = View.VISIBLE
                selectedValue = strarray[0]
                aWhereToPostFriends.minValue = 0 //from array first value
                aWhereToPostFriends.isMeasureWithLargestChildEnabled = true
                aWhereToPostFriends.wrapSelectorWheel = false
                aWhereToPostFriends.isVerticalFadingEdgeEnabled = true
                aWhereToPostFriends.isHorizontalFadingEdgeEnabled = true
                aWhereToPostFriends.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                aWhereToPostFriends.displayedValues = strarray
                aWhereToPostFriends.maxValue = strarray.size - 1 //to array last value
                aWhereToPostFriends.setOnValueChangedListener { picker, oldVal, newVal ->
                    selectedValue = strarray[newVal]
                }
            }

            public.setOnClickListener {
                source = "1"
                header_Ques.text = myContext.getString(string.label_share_feed_public_friends)
                header_title.text = myContext.getString(string.label_share_public)
                header_image.setImageResource(drawable.ic_public_new)
                firstPage.visibility = View.GONE
                clubRV.visibility = View.GONE
                friends_public_lay.visibility = View.VISIBLE
                aWhereToPostPublic.visibility = View.VISIBLE
                aWhereToPostFriends.visibility = View.GONE
                selectedValue = strPubarray[0]
                aWhereToPostPublic.minValue = 0 //from array first value
                aWhereToPostPublic.isMeasureWithLargestChildEnabled = true
                aWhereToPostPublic.wrapSelectorWheel = false
                aWhereToPostPublic.isVerticalFadingEdgeEnabled = true
                aWhereToPostPublic.isHorizontalFadingEdgeEnabled = true
                aWhereToPostPublic.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
                aWhereToPostPublic.displayedValues = strPubarray
                aWhereToPostPublic.maxValue = strPubarray.size - 1 //to array last value
                aWhereToPostPublic.setOnValueChangedListener { picker, oldVal, newVal ->
                    selectedValue = strPubarray[newVal]
                }
            }


            club.setOnClickListener {
                source = "3"
                header_Ques.text = myContext.getString(string.label_share_feed_club)
                header_title.text = myContext.getString(string.label_share_club)
                header_image.setImageResource(drawable.clubs_friend_icon)
                firstPage.visibility = View.GONE
                aWhereToPostFriends.visibility = View.GONE
                aWhereToPostPublic.visibility = View.GONE
                clubRV.visibility = View.VISIBLE
                friends_public_lay.visibility = View.VISIBLE
                val mUser = LocalStorageSP.getLoginUser(myContext)

                val mCase = GetUserClubDetailsCase(
                    mUser.mUserId!!,
                    ""
                )
                val result =
                    Helper.GenerateEncrptedUrl(
                        BuildConfig.API_URL,
                        mCase!!
                    )
                Log.e("URL", result)
                //progress.visibility = View.VISIBLE
                AppServices.execute(
                    myContext, object : ResponseListener {
                        override fun onResponse(r: Response?) {
                            if (r != null) {
                                if (r.response!!.isSuccess) {
                                   // progress.visibility = View.GONE
                                    val gridLayoutManager = LinearLayoutManager(myContext)
                                    clubRV.layoutManager = gridLayoutManager
                                    clubRV.isNestedScrollingEnabled = false
                                    clubsList = r as ClubListShareModel
                                    adapter = ClubShareMultiSelectionAdapter(clubsList)
                                    clubRV.adapter = adapter

                                } else
                                    progress.visibility = View.GONE
                            } else
                                progress.visibility = View.GONE
                        }
                    },
                    result,
                    Request.Method.POST,
                    AppServices.API.ClubListShare,
                    ClubListShareModel::class.java
                )

                *//*GetUserClubDetailsCase*//*
            }

            back.setOnClickListener {
                firstPage.visibility = View.VISIBLE
                friends_public_lay.visibility = View.GONE
            }
            header_share_back.setOnClickListener {
                bottom_dialog!!.dismiss()
            }

            shareBtn.setOnClickListener {
                Log.e("clubs", adapter.getSelectedClubs().toString())
                val aList = adapter.getSelectedClubs()
                val club_ids: StringBuilder = StringBuilder()
                for (i in aList.indices)
                    club_ids.append(aList[i] + ",")


                val intent = Intent(myContext, FeedShareActivity::class.java)
                intent.putExtra("feed_id", feedList.id)
                intent.putExtra("level", selectedValue)
                intent.putExtra("clubid", club_ids.toString())
                intent.putExtra("source", source)
                intent.putExtra("feed", feedList)
                myContext.startActivity(intent)
                bottom_dialog?.dismiss()
            }




            bottom_dialog!!.show()
        } catch (e: Exception) {

            print(e.toString())
        }
    }*/


    fun GetUserClubDetailsCase(
        aLoginId: String,
        aLastUpdateTime: String
    ): String? {
        var aCaseStr: String? = " "
        try {
            val jsonParam1 = JSONObject()
            jsonParam1.put("login_user_id", aLoginId)
            jsonParam1.put("last_updated_time", aLastUpdateTime)
            val jsonParam = JSONObject()
            jsonParam.put("UserClubsList", jsonParam1)
            Log.e("UserClubsList", " $jsonParam")
            aCaseStr = Base64.encodeToString(jsonParam.toString().toByteArray(), 0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return aCaseStr
    }


}