package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentEditnameBinding
import com.realtouchapp.tools.ext.*

class EditNameBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((String,String) -> Unit)
    var firstName= ""
    var lastName =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentEditnameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentEditnameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialViewSize()
        initialView()
        binding.etBsdfEditnameFirst.setText(firstName)
        binding.etBsdfEditnameLast.setText(lastName)
    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfEditnameDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfEditnameDivider.setMarginTopOrignal(16)

        //First
        binding.etBsdfEditnameFirst.setTextSize(13)
        binding.rlBsdfEditnameFirst.setMarginTopOrignal(24)
        binding.rlBsdfEditnameFirst.setViewSizeOrignalWH(343,48)
        binding.etBsdfEditnameFirst.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditnameFirstRemove.setViewSizeOrignalWH(20,20)
        //Name
        binding.etBsdfEditnameLast.setTextSize(13)
        binding.etBsdfEditnameLast.setViewSizeOrignalWH(343,48)
        binding.rlBsdfEditnameLast.setMarginTopOrignal(24)
        binding.rlBsdfEditnameLast.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditnameLastRemove.setViewSizeOrignalWH(20,20)

        binding.btnBsdfEditnameSave.setMarginTopOrignal(24)
        binding.btnBsdfEditnameSave.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.etBsdfEditnameFirst.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditnameFirstRemove.visibility = View.INVISIBLE
                enableDone(false)
            }else{
                binding.ivBsdfEditnameFirstRemove.visibility = View.VISIBLE
                if (binding.ivBsdfEditnameLastRemove.visibility == View.VISIBLE){
                    enableDone(true)
                }
            }
        }
        binding.ivBsdfEditnameFirstRemove.setOnClickListener (this)

        binding.etBsdfEditnameLast.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditnameLastRemove.visibility = View.INVISIBLE
                enableDone(false)
            }else{
                binding.ivBsdfEditnameLastRemove.visibility = View.VISIBLE
                if (binding.ivBsdfEditnameFirstRemove.visibility == View.VISIBLE){
                    enableDone(true)
                }
            }
        }
        binding.ivBsdfEditnameLastRemove.setOnClickListener (this)

        binding.btnBsdfEditnameSave.setOnClickListener (this)
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfEditnameSave.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfEditnameSave.setOnClickListener (this)

        }else{
            binding.btnBsdfEditnameSave.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfEditnameSave.setOnClickListener(null)
        }
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.iv_bsdf_editname_first_remove->{
                binding.etBsdfEditnameFirst.setText("")
            }
            R.id.iv_bsdf_editname_last_remove->{
                binding.etBsdfEditnameLast.setText("")
            }
            R.id.btn_bsdf_editname_save->{
                doneMeathod(binding.etBsdfEditnameFirst.text.toString(),binding.etBsdfEditnameLast.text.toString())
                dismiss()
            }

        }
    }
}