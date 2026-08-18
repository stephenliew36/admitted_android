package com.realtouchapp.admitted.common.ui.dialogfragmnet

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.realtouchapp.admitted.R
import com.realtouchapp.tools.ext.setPressedBackground
import com.realtouchapp.tools.ext.setPressedTextColor
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeByRatioWH

/*
    作者: RobertChou
    時間: 2023/2/13 上午 12:33
    信箱: didi31139@gmail.com
    說明:
*/
class LoadingDialogFragment : DialogFragment() {
    lateinit var dialogView: View
    var sure: () -> Unit = {}
    var successMsg =""
    var message = "Loading"
    var onDismiss: () -> Unit = {}
    var isCanceledOnTouchOutside: Boolean = false

    companion object{
        const val TAG ="LoadingDialogFragment"

        fun newInstance(message: String = "Loading", onDismiss: (() -> Unit) = {}): LoadingDialogFragment {
            return LoadingDialogFragment()
                .apply {
                    this.arguments = Bundle()
//                        .apply {
//                            putString(BUNDLE_MESSAGE, message)
//                        }
                    this.message = message
                    this.onDismiss = onDismiss
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dialogView = View.inflate(context, R.layout.dialogfragment_loading, container)
        val rlDialogFragmentBody: View = dialogView.findViewById(R.id.rl_dialogfragment_body)
        rlDialogFragmentBody.setViewSizeByRatioWH(1f, 1f)
        rlDialogFragmentBody.setOnClickListener {
//            dialog!!.dismiss()
        }


        val tvExchangeSuccess: TextView = dialogView.findViewById(R.id.tv_exchangesuccess)
        tvExchangeSuccess.setTextSize(15)
        tvExchangeSuccess.text = message
//        tvExchangeSuccess.text =getString(R.string.rewardsuccessdialogfragment_success,successMsg,currencyName)
        val btnLogoutSure: Button = dialogView.findViewById(R.id.btn_logout_sure)
        btnLogoutSure.setTextSize(20)
        btnLogoutSure.setPressedBackground(R.color.primary_blue, android.R.color.black)
        btnLogoutSure.setPressedTextColor(android.R.color.white, android.R.color.darker_gray)
        btnLogoutSure.setOnClickListener {
            sure()
        }

        return dialogView
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onDismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = super.onCreateDialog(savedInstanceState)
        val dialogWindow = mDialog.window
        val dm = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(dm)
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow!!.setGravity(Gravity.CENTER)

        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside)
//        mDialog.setCancelable(true)
        return mDialog

    }


    fun showOnce(fragmanger: FragmentManager) {
        fragmanger.findFragmentByTag(TAG)?.let {
            fragmanger.beginTransaction().remove(this).commit()
        }
//        if (dialog != null) {
//            if (dialog!!.isShowing) {
//                return
//            }
//            fragmanger.beginTransaction().remove(this).commit()
//        }

        super.show(fragmanger, TAG)
    }

}
