package com.realtouchapp.admitted.common.ui.dialogfragmnet

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.realtouchapp.tools.ext.dpToPx


/** 有文字輸入框/可增加自訂控制項的Dialog (限定Vertical LinearLayout顯示) */
class TextInputDialogFragment private constructor() : DialogFragment() {
    companion object {
        const val TAG = "TextInputDialogFragment"

        /**
         *
         * @param title title
         * @param message message
         * @param sureButtonText 確認按鈕文字
         * @param cancelButtonText 取消按鈕文字
         * @param onSure 確認按鈕執行動作，回傳是否可以關閉dialog
         * @param onCancel 取消按鈕執行動作
         */
        fun newInstance(
            title: String? = null,
            message: String? = null,
            sureButtonText: String? = null,
            cancelButtonText: String? = null,
            onSure: (TextInputDialogFragment) -> Boolean = { true },
            onCancel: (TextInputDialogFragment) -> Unit = { },
            onDismiss: (() -> Unit?) = { }
        ): TextInputDialogFragment {
            return TextInputDialogFragment()
                .apply {
                    this.title = title
                    this.message = message
                    this.sureButtonText = sureButtonText
                    this.cancelButtonText = cancelButtonText
                    this.onSurePressed = onSure
                    this.onCancelPressed = onCancel
                    this.onDismiss = onDismiss
                }
        }
    }

    private var title: String? = null
    private var message: String? = null
    private var sureButtonText: String? = null
    private var cancelButtonText: String? = null
    private var onSurePressed: (TextInputDialogFragment) -> Boolean = { true }
    private var onCancelPressed: (TextInputDialogFragment) -> Unit = { }

    var onDismiss: (() -> Unit?) = { }

    private lateinit var constraintLayoutDialogContent: ConstraintLayout
    private lateinit var linearLayoutForEditTexts: LinearLayout
    private val controlsList: ArrayList<View> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createEditTextContainer()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        addEditTextListToContainer()

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setView(constraintLayoutDialogContent)
            .setCancelable(false)
            .setNegativeButton(cancelButtonText ?: "Cancel") { dialog, _ ->
                onCancelPressed(this@TextInputDialogFragment)
                dialog.dismiss()
            }
//            .setPositiveButton(sureButtonText ?: getString(R.string.sure)) { dialog, _ ->
//                onSurePressed(this@TextInputDialogFragment)
//                dialog.dismiss()
//            }
            .setPositiveButton(sureButtonText ?: "OK", null)
            .create()

        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(true)

        alertDialog.setOnShowListener { dialog ->
            (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener {
                    if (onSurePressed(this@TextInputDialogFragment)) {
                        dialog.dismiss()
                    }
                }
        }

        return alertDialog
    }

    override fun onResume() {
        super.onResume()

        requestDefaultFocus()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        onDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()

        dialog?.dismiss()
    }

    private fun createEditTextContainer() {
        constraintLayoutDialogContent = ConstraintLayout(requireContext())

        linearLayoutForEditTexts = LinearLayout(requireContext())
            .apply {
                orientation = LinearLayout.VERTICAL
                isFocusable = true
                isFocusableInTouchMode = true
            }
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
            startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            marginStart = requireContext().dpToPx(10f)
            marginEnd = requireContext().dpToPx(10f)
            bottomMargin = requireContext().dpToPx(10f)
        }
        constraintLayoutDialogContent.addView(linearLayoutForEditTexts, layoutParams)
    }

    private fun addEditTextListToContainer() {
        for (i in 0 until controlsList.size) {
            val control = controlsList[i]
            addControlToContainer(control, i)
        }
    }

    private  fun addControlToContainer(control: View, index: Int) {
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = requireContext().dpToPx(10f)
            if (index == controlsList.lastIndex) {
                bottomMargin = requireContext().dpToPx(10f)
            }
        }
        linearLayoutForEditTexts.addView(control, layoutParams)
    }

    /** 增加文字輸入框 */
    fun addEditText(
        context: Context,
        configurator: AppCompatEditText.(TextInputDialogFragment) -> Unit = { }
    ): TextInputDialogFragment {
        val editText = AppCompatEditText(context)
        configurator.invoke(editText, this)

        return addControl { editText }
    }

    /** 增加自訂控制項 */
    fun addControl(controlBuilder: (TextInputDialogFragment) -> View? = { null }): TextInputDialogFragment {
        controlBuilder(this)?.let { control ->
            controlsList.add(control)

            this.context?.let {
                addControlToContainer(control, controlsList.size - 1)
            }
        }

        return this
    }

    /** 取得輸入的文字 */
    fun getInputText(index: Int): String? {
        return getEditText(index)?.text?.toString()
    }

    /** 取得文字輸入框 */
    fun getEditText(index: Int): EditText? {
        return getControl(index) as? EditText
    }

    fun getControl(index: Int): View? {
        return if (index in 0 until controlsList.size)
            controlsList[index]
        else
            null
    }

    /** 清除焦點 */
    fun clearFocus() {
        dialog?.currentFocus?.clearFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(linearLayoutForEditTexts.windowToken, 0)
    }

    /** 啟動初始化焦點 */
    fun requestDefaultFocus() {
        getEditText(0)?.let { editText ->
            editText.requestFocus()

            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(controlsList.firstOrNull(), 0)
        }
    }

    /** 設置確認按鈕事件 */
    fun setOnSureListener(onSure: (TextInputDialogFragment) -> Boolean = { true }): TextInputDialogFragment {
        onSurePressed = onSure
        return this
    }

    /** 設置取消按鈕事件 */
    fun setOnCancelListener(onCancel: (TextInputDialogFragment) -> Unit = { }): TextInputDialogFragment {
        onCancelPressed = onCancel
        return this
    }
}
