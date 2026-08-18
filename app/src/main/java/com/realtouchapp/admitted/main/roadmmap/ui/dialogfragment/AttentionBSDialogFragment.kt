package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentAttentionBinding
import com.realtouchapp.tools.ext.*

class AttentionBSDialogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((Int) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    private lateinit var binding:BsdialogfragmentAttentionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsdialogfragmentAttentionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()

    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfAttentionDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAttentionDivider.setMarginTopOrignal(16)

        binding.tvBsdfAttentionTitle.setTextSize(17)
        binding.tvBsdfAttentionTitle.setMarginTopOrignal(24)


        //Name
        binding.tvBsdfAttentionName.setTextSize(13)
        binding.tvBsdfAttentionName.setMarginTopOrignal(24)

        binding.btnBsdfAttentionCancel.setMarginTopOrignal(24)

        binding.btnBsdfAttentionDelete.setMarginTopOrignal(24)
        binding.btnBsdfAttentionDelete.setMarginBottomOrignal(36)

        binding.btnBsdfAttentionCancel.setTextSize(13)
        binding.btnBsdfAttentionDelete.setTextSize(13)
    }
    private fun initialView(){


        binding.btnBsdfAttentionCancel.setOnClickListener (this)
        binding.btnBsdfAttentionDelete.setOnClickListener (this)
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.btn_bsdf_attention_cancel->{
                dismiss()
            }
            R.id.btn_bsdf_attention_delete->{
            }

        }
    }
}