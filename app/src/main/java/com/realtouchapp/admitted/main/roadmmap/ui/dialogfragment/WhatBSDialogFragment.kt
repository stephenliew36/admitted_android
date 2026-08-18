package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.databinding.BsdialogfragmentWhatBinding
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.tools.ext.setMarginTopOrignal
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.setViewSizeOrignalWidth

class WhatBSDialogFragment : BottomSheetDialogFragment(), View.OnClickListener {

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_MESSAGE = "arg_message"
        private const val ARG_SHOW_UPGRADE = "arg_show_upgrade"

        fun newInstance(
            title: String,
            message: String,
            showUpgradeButton: Boolean = true
        ): WhatBSDialogFragment {
            return WhatBSDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, message)
                    putBoolean(ARG_SHOW_UPGRADE, showUpgradeButton)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.WhatBottomSheetDialogTheme)
        isCancelable = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setCanceledOnTouchOutside(true)
            setOnShowListener {
                window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                window?.setDimAmount(0.45f)
            }
        }
    }

    private lateinit var binding: BsdialogfragmentWhatBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsdialogfragmentWhatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()

    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.let { bottomSheetDialog ->
            bottomSheetDialog.setCanceledOnTouchOutside(true)
            bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            bottomSheetDialog.window?.setDimAmount(0.45f)
        }
    }

    private fun initialViewSize() {
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfAttentionDivider.setViewSizeOrignalWH(54, 2)
        binding.ivBsdfAttentionDivider.setMarginTopOrignal(16)

        binding.tvBsdfAttentionTitle.setTextSize(17)
        binding.tvBsdfAttentionTitle.setMarginTopOrignal(24)


        //Name
        binding.tvBsdfAttentionName.setTextSize(13)
        binding.tvBsdfAttentionName.setMarginTopOrignal(24)

        binding.btnBsdfAttentionUpgrade.setMarginTopOrignal(24)

        binding.btnBsdfAttentionUpgrade.setTextSize(13)
    }

    private fun initialView() {
        val title = arguments?.getString(ARG_TITLE)
            ?: "Level of Readiness"
        val message = arguments?.getString(ARG_MESSAGE)
            ?: "We calculate your Level of Readiness based on your academic and extracurricular performances that you have recorded in the app. The score indicates the overall likelihood of admission to an undergraduate program at a university.\n\nFind out more about your Level of Readiness by upgrading to our Premium plan."
        val showUpgradeButton = arguments?.getBoolean(ARG_SHOW_UPGRADE, true) ?: true

        binding.tvBsdfAttentionTitle.text = title
        binding.tvBsdfAttentionName.text = message
        binding.btnBsdfAttentionUpgrade.visibility = if (showUpgradeButton) View.VISIBLE else View.GONE
        binding.btnBsdfAttentionUpgrade.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.btn_bsdf_attention_upgrade -> {
                PremiumActivity.Intent(requireActivity() as MainActivity)
                dismiss()
            }


        }
    }
}
