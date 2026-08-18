package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentEditpasswordBinding
import com.realtouchapp.tools.ext.*

class EditPasswordBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((Int) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentEditpasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentEditpasswordBinding.inflate(layoutInflater)
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

        binding.ivBsdfEditpasswordDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfEditpasswordDivider.setMarginTopOrignal(16)

        //Cuurent Password
        binding.etBsdfEditpasswordCurrent.setTextSize(13)
        binding.etBsdfEditpasswordCurrent.setViewSizeOrignalWH(343,48)
        binding.rlBsdfEditpasswordCurrent.setMarginTopOrignal(24)
        binding.rlBsdfEditpasswordCurrent.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditpasswordCurrentRemove.setViewSizeOrignalWH(20,20)
        //New Password
        binding.etBsdfEditpasswordNew.setTextSize(13)
        binding.etBsdfEditpasswordNew.setViewSizeOrignalWH(343,48)
        binding.rlBsdfEditpasswordNew.setMarginTopOrignal(24)
        binding.rlBsdfEditpasswordNew.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditpasswordNewRemove.setViewSizeOrignalWH(20,20)
        //Confirm Password
        binding.etBsdfEditpasswordConfirm.setTextSize(13)
        binding.etBsdfEditpasswordConfirm.setViewSizeOrignalWH(343,48)
        binding.rlBsdfEditpasswordConfirm.setMarginTopOrignal(24)
        binding.rlBsdfEditpasswordConfirm.setViewSizeOrignalWH(343,48)
        binding.ivBsdfEditpasswordConfirmRemove.setViewSizeOrignalWH(20,20)


        binding.btnBsdfEditpasswordSave.setMarginTopOrignal(24)
        binding.btnBsdfEditpasswordSave.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.ivBsdfEditpasswordCurrentRemove.setOnClickListener (this)
        binding.etBsdfEditpasswordCurrent.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditpasswordCurrentRemove.visibility = View.INVISIBLE
            }else{
                binding.ivBsdfEditpasswordCurrentRemove.visibility = View.VISIBLE
            }
        }


        binding.ivBsdfEditpasswordNewRemove.setOnClickListener (this)
        binding.etBsdfEditpasswordNew.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditpasswordNewRemove.visibility = View.INVISIBLE
            }else{
                binding.ivBsdfEditpasswordNewRemove.visibility = View.VISIBLE
            }
        }
        binding.ivBsdfEditpasswordConfirmRemove.setOnClickListener (this)
        binding.etBsdfEditpasswordConfirm.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfEditpasswordConfirmRemove.visibility = View.INVISIBLE
            }else{
                binding.ivBsdfEditpasswordConfirmRemove.visibility = View.VISIBLE
            }
        }



        binding.btnBsdfEditpasswordSave.setOnClickListener (this)
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.iv_bsdf_editpassword_current_remove->{
                binding.etBsdfEditpasswordCurrent.setText("")
            }
            R.id.iv_bsdf_editpassword_new_remove->{
                binding.etBsdfEditpasswordNew.setText("")
            }
            R.id.iv_bsdf_editpassword_confirm_remove->{
                binding.etBsdfEditpasswordConfirm.setText("")
            }
            R.id.btn_bsdf_editname_save->{
                dismiss()
            }

        }
    }
}