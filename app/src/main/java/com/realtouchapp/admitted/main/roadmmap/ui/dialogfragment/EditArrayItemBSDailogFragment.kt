package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentEditarrayitemBinding
import com.realtouchapp.admitted.databinding.BsdialogfragmentEditnameBinding
import com.realtouchapp.tools.ext.*

class EditArrayItemBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((Int) -> Unit)
    var arrayid =0
    var selectIndex=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentEditarrayitemBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentEditarrayitemBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialViewSize()
        initialView()

    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfEditarrayitemDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfEditarrayitemDivider.setMarginTopOrignal(16)

        //Level
        binding.rlBsdfEditarrayitemItem.setMarginTopOrignal(8)
        binding.rlBsdfEditarrayitemItem.setViewSizeOrignalWH(343,48)
        binding.spBsdfEditarrayitemItem.setViewSizeOrignalWH(343,45)
        binding.etBsdfEditarrayitemItem.setViewSizeOrignalWH(343,45)

        binding.btnBsdfEditarrayitemSave.setMarginTopOrignal(24)
        binding.btnBsdfEditarrayitemSave.setMarginBottomOrignal(36)
        enableDone(false)
    }
    private fun initialView(){
        requireContext().setSpinner(binding.etBsdfEditarrayitemItem,binding.spBsdfEditarrayitemItem,arrayid,{
            selectIndex = it
            enableDone(true)
        })

        binding.btnBsdfEditarrayitemSave.setOnClickListener (this)
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.btn_bsdf_editarrayitem_save->{
                doneMeathod(selectIndex)
                dismiss()
            }

        }
    }

    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfEditarrayitemSave.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfEditarrayitemSave.setOnClickListener (this)

        }else{
            binding.btnBsdfEditarrayitemSave.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfEditarrayitemSave.setOnClickListener(null)
        }
    }
}