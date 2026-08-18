package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentInterestselectBinding
import com.realtouchapp.admitted.member.view.adapter.InterestCheckRecyleViewAdapter
import com.realtouchapp.admitted.network.entity.AreaOfInterest
import com.realtouchapp.tools.ext.setMarginBottomRatioHWithoutBar
import com.realtouchapp.tools.ext.setMarginTopRatioHWithoutBar
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import com.realtouchapp.tools.ext.setViewSizeWidthByRatio

class InterestSelectBSDailogFragment : BottomSheetDialogFragment(), OnClickListener {
    lateinit var onDone: ((AreaOfInterest) -> Unit)
    var areaOfInterest = AreaOfInterest()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private lateinit var binding: BsdialogfragmentInterestselectBinding
    lateinit var  interestCheckRecyleViewAdapter:InterestCheckRecyleViewAdapter
    companion object{
        fun newInstance(): InterestSelectBSDailogFragment {
            val fragment = InterestSelectBSDailogFragment()
            return fragment
        }
        const val TAG ="InterestSelectBSDailogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentInterestselectBinding.inflate(layoutInflater)
        interestCheckRecyleViewAdapter = InterestCheckRecyleViewAdapter(requireContext())
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
        binding.llBsdfInterset.setViewSizeWidthByRatio(.915f)
        binding.ivBsdfIntersetDivider.setViewSizeByRatioWH(.145f, .003f)

        binding.ivBsdfIntersetDivider.setMarginTopRatioHWithoutBar(.021f)
        binding.rvBsdfInterset.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfIntersetDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfIntersetDone.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView() {


        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvBsdfInterset.layoutManager = linearLayoutManager

        var arrlist = ArrayList(resources.getStringArray(R.array.array_interest).toList())
        interestCheckRecyleViewAdapter.setData(arrlist)
        interestCheckRecyleViewAdapter.areaOfInterest = areaOfInterest

        binding.rvBsdfInterset.adapter = interestCheckRecyleViewAdapter
        binding.btnBsdfIntersetDone.setOnClickListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_bsdf_interset_done -> {
                onDone(interestCheckRecyleViewAdapter.areaOfInterest)
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