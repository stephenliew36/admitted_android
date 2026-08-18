package com.realtouchapp.admitted.main.explore.ui.bsdialogfragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentExtrafilterBinding
import com.realtouchapp.admitted.network.entity.ExtraFilter
import com.realtouchapp.tools.ext.*

class ExtraFilterBSDailogFragment : BottomSheetDialogFragment(), OnClickListener {
    lateinit var onDone: ((ExtraFilter) -> Unit)
    var extraFilter = ExtraFilter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private lateinit var binding: BsdialogfragmentExtrafilterBinding
    companion object{
        fun newInstance(): ExtraFilterBSDailogFragment {
            val fragment = ExtraFilterBSDailogFragment()
            return fragment
        }
        const val TAG ="ExtraFilterBSDailogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentExtrafilterBinding.inflate(layoutInflater)
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
        binding.ivBsdfExtrafilterDivider.setViewSizeByRatioWH(.145f, .003f)
        binding.ivBsdfExtrafilterDivider.setMarginTopRatioHWithoutBar(.021f)
        binding.tvBsdfExtrafilterTitle.setTextSize(17)
        binding.tvBsdfExtrafilterInsitution.setTextSize(15)
        binding.cbBsdfExtrafilterSave.setTextSize(15)
        binding.cbBsdfExtrafilterHospitals.setTextSize(15)
        binding.cbBsdfExtrafilterOrphanages.setTextSize(15)
        binding.cbBsdfExtrafilterNursing.setTextSize(15)
        binding.cbBsdfExtrafilterLibraries.setTextSize(15)
        binding.cbBsdfExtrafilterMusuems.setTextSize(15)
        binding.cbBsdfExtrafilterAnimal.setTextSize(15)

        binding.btnBsdfExtrafilterDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfExtrafilterClear.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView() {
        binding.tvBsdfExtrafilterInsitution.setOnClickListener {
            if (binding.llBsdfExtrafilterInsitution.visibility == VISIBLE){
                binding.llBsdfExtrafilterInsitution.visibility = View.GONE
                binding.tvBsdfExtrafilterInsitution.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfExtrafilterInsitution.visibility = View.VISIBLE
                binding.tvBsdfExtrafilterInsitution.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }
        binding.llBsdfExtrafilterInsitution.visibility = View.VISIBLE
        binding.tvBsdfExtrafilterInsitution.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
        binding.cbBsdfExtrafilterSave.isChecked = extraFilter.isSaved
        binding.cbBsdfExtrafilterHospitals.isChecked = extraFilter.isHospitals
        binding.cbBsdfExtrafilterOrphanages.isChecked = extraFilter.isOrphanages
        binding.cbBsdfExtrafilterNursing.isChecked = extraFilter.isNursingHomes
        binding.cbBsdfExtrafilterLibraries.isChecked = extraFilter.isLibraries
        binding.cbBsdfExtrafilterMusuems.isChecked = extraFilter.isMusuemsGalleries
        binding.cbBsdfExtrafilterAnimal.isChecked = extraFilter.isAnimalShelters
        

        binding.tvBsdfExtrafilterInsitution.setOnClickListener {
            if (binding.llBsdfExtrafilterInsitution.visibility == VISIBLE){
                binding.llBsdfExtrafilterInsitution.visibility = View.GONE
                binding.tvBsdfExtrafilterInsitution.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfExtrafilterInsitution.visibility = View.VISIBLE
                binding.tvBsdfExtrafilterInsitution.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }

        binding.btnBsdfExtrafilterDone.setOnClickListener(this)
        binding.btnBsdfExtrafilterClear.setOnClickListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_bsdf_extrafilter_clear -> {
                onDone(ExtraFilter())
                dismiss()
            }
            R.id.btn_bsdf_extrafilter_done -> {
                extraFilter.isSaved = binding.cbBsdfExtrafilterSave.isChecked
                extraFilter.isHospitals = binding.cbBsdfExtrafilterHospitals.isChecked
                extraFilter.isOrphanages = binding.cbBsdfExtrafilterOrphanages.isChecked
                extraFilter.isNursingHomes = binding.cbBsdfExtrafilterNursing.isChecked
                extraFilter.isLibraries = binding.cbBsdfExtrafilterLibraries.isChecked
                extraFilter.isMusuemsGalleries = binding.cbBsdfExtrafilterMusuems.isChecked
                extraFilter.isAnimalShelters = binding.cbBsdfExtrafilterAnimal.isChecked

                onDone(extraFilter)
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