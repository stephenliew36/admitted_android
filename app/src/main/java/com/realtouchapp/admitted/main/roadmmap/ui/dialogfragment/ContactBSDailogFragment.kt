package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentAttentionBinding
import com.realtouchapp.admitted.databinding.BsdialogfragmentContactBinding
import com.realtouchapp.tools.ext.*

class ContactBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((Int) -> Unit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentContactBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()

    }

    private fun initialViewSize(){
//        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfContactDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfContactDivider.setMarginTopOrignal(16)

        binding.tvBsdfContactTitle.setTextSize(17)
        binding.tvBsdfContactTitle.setMarginTopOrignal(24)

        //Call
        binding.llBsdfContactCall.setViewSizeOrignalHeight(56)
        binding.llBsdfContactCall.setMarginTopOrignal(24)
        binding.ivBsdfContactCall.setViewSizeOrignalWH(24,24)
        binding.tvBsdfContactCall.setTextSize(13)
        binding.tvBsdfContactCall.setMarginLeftOrignal(16)

        //Map
        binding.llBsdfContactMap.setViewSizeOrignalHeight(56)
        binding.ivBsdfContactMap.setViewSizeOrignalWH(24,24)
        binding.tvBsdfContactMap.setTextSize(13)
        binding.tvBsdfContactMap.setMarginLeftOrignal(16)

        //Web
        binding.llBsdfContactWeb.setViewSizeOrignalHeight(56)
        binding.llBsdfContactWeb.setMarginBottomOrignal(32)
        binding.ivBsdfContactWeb.setViewSizeOrignalWH(24,24)
        binding.tvBsdfContactWeb.setTextSize(13)
        binding.tvBsdfContactWeb.setMarginLeftOrignal(16)

    }
    private fun initialView(){


        binding.llBsdfContactCall.setOnClickListener (this)
        binding.llBsdfContactMap.setOnClickListener (this)
        binding.llBsdfContactWeb.setOnClickListener (this)
    }

    override fun onClick(view: View) {
        when(view.id){

            R.id.ll_bsdf_contact_call->{
                dismiss()
            }
            R.id.ll_bsdf_contact_map->{
                dismiss()
            }
            R.id.ll_bsdf_contact_web->{
                dismiss()
            }

        }
    }
}