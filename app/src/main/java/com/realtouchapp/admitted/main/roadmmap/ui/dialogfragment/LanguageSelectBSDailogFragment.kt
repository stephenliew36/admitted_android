package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentLanguageselectBinding
import com.realtouchapp.tools.ext.*

class LanguageSelectBSDailogFragment : BottomSheetDialogFragment(), OnClickListener {
    lateinit var onDone: ((Boolean) -> Unit)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    var isYesSelect = true
    private lateinit var binding: BsdialogfragmentLanguageselectBinding
    companion object{
        fun newInstance(): LanguageSelectBSDailogFragment {
            val fragment = LanguageSelectBSDailogFragment()
            return fragment
        }
        const val TAG ="LanguageSelectBSDailogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentLanguageselectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    private fun initialViewSize() {
//        binding.llBsdfInterset.setViewSizeByRatioWH(.915f,-2f)
        binding.llBsdfLanguage.setViewSizeWidthByRatio(.915f)
        binding.ivBsdfLanguageDivider.setViewSizeByRatioWH(.145f, .003f)
        binding.ivBsdfLanguageDivider.setMarginTopRatioHWithoutBar(.021f)

        binding.tvBsdfLanguageTitle.setTextSize(17)
        binding.tvBsdfLanguageSelectYes.setTextSize(17)
        binding.tvBsdfLanguageSelectNo.setTextSize(17)

        binding.tvBsdfLanguageTitle.setMarginTopOrignal(24)
        binding.cvBsdfLanguage.setMarginTopOrignal(24)
        binding.cvBsdfLanguage.setMarginBottomOrignal(24)
        binding.tvBsdfLanguageContent.setTextSize(13)


        binding.btnBsdfLanguageDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfLanguageDone.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView() {
        if (isYesSelect){
            selectYes(true)
        }else{
            selectYes(false)
        }

        binding.tvBsdfLanguageSelectYes.setOnClickListener(this)
        binding.tvBsdfLanguageSelectNo.setOnClickListener(this)
        binding.btnBsdfLanguageDone.setOnClickListener(this)
    }


    private fun selectYes(isYesSelect:Boolean){
        this.isYesSelect = isYesSelect
        binding.tvBsdfLanguageSelectYes.setBackgroundColor(0)
        binding.tvBsdfLanguageSelectNo.setBackgroundColor(0)
        if (isYesSelect){
            binding.tvBsdfLanguageSelectYes.setBackgroundColor(Color.parseColor("#F1E9E5"))
            binding.tvBsdfLanguageSelectYes.tag=1
        }else{
            binding.tvBsdfLanguageSelectNo.setBackgroundColor(Color.parseColor("#F1E9E5"))
            binding.tvBsdfLanguageSelectNo.tag=1
        }
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_bsdf_language_select_yes -> {
                selectYes(true)
            }
            R.id.tv_bsdf_language_select_no -> {
                selectYes(false)
            }
            R.id.btn_bsdf_language_done -> {
                onDone(isYesSelect)
                dismiss()
            }

        }
    }

    fun showOnce(fragmanger: FragmentManager) {
        var fragment = fragmanger.findFragmentByTag(TAG)
        if (fragment!=null){
            fragmanger.beginTransaction().show(fragment)
        }else{
            super.show(fragmanger, TAG)
        }
    }
}