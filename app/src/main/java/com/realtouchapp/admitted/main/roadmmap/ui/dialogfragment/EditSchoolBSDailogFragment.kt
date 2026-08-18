package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentEditschoolBinding
import com.realtouchapp.tools.ext.*

class EditSchoolBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((String) -> Unit)
    var school= ""
    var lastName =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentEditschoolBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentEditschoolBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialViewSize()
        initialView()
        binding.etBsdfEditschoolFirst.setText(school)
    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfEditschoolDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfEditschoolDivider.setMarginTopOrignal(16)

        //First
        binding.etBsdfEditschoolFirst.setTextSize(13)
        binding.rlBsdfEditschoolFirst.setMarginTopOrignal(24)
        binding.rlBsdfEditschoolFirst.setViewSizeOrignalWH(343,48)
        binding.etBsdfEditschoolFirst.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditschoolFirstRemove.setViewSizeOrignalWH(20,20)

        binding.btnBsdfEditschoolSave.setMarginTopOrignal(24)
        binding.btnBsdfEditschoolSave.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.etBsdfEditschoolFirst.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditschoolFirstRemove.visibility = View.INVISIBLE
                enableDone(false)
            }else{
                binding.ivBsdfEditschoolFirstRemove.visibility = View.VISIBLE
                enableDone(true)
            }
        }
        binding.ivBsdfEditschoolFirstRemove.setOnClickListener (this)

        binding.btnBsdfEditschoolSave.setOnClickListener (this)
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfEditschoolSave.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfEditschoolSave.setOnClickListener (this)

        }else{
            binding.btnBsdfEditschoolSave.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfEditschoolSave.setOnClickListener(null)
        }
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.iv_bsdf_editschool_first_remove->{
                binding.etBsdfEditschoolFirst.setText("")
            }
            R.id.btn_bsdf_editschool_save->{
                doneMeathod(binding.etBsdfEditschoolFirst.text.toString())
                dismiss()
            }

        }
    }
}