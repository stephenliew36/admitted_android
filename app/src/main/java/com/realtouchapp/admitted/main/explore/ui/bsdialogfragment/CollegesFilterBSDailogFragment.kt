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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentCollegesfilterBinding
import com.realtouchapp.admitted.main.explore.viewmodel.CollegesFilterBSDailogFragmentViewModel
import com.realtouchapp.admitted.member.view.adapter.InterestCheckRecyleViewAdapter
import com.realtouchapp.admitted.network.entity.TargetSchooFilter
import com.realtouchapp.tools.ext.setMarginBottomRatioHWithoutBar
import com.realtouchapp.tools.ext.setMarginTopRatioHWithoutBar
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import kotlinx.coroutines.launch

class CollegesFilterBSDailogFragment : BottomSheetDialogFragment(), OnClickListener {
    lateinit var onDone: ((TargetSchooFilter) -> Unit)
    var tagetSchooFilter = TargetSchooFilter()
    private val viewModel: CollegesFilterBSDailogFragmentViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    private lateinit var binding: BsdialogfragmentCollegesfilterBinding
    lateinit var  interestCheckRecyleViewAdapter:InterestCheckRecyleViewAdapter
    companion object{
        fun newInstance(): CollegesFilterBSDailogFragment {
            val fragment = CollegesFilterBSDailogFragment()
            return fragment
        }
        const val TAG ="CollegesFilterBSDailogFragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BsdialogfragmentCollegesfilterBinding.inflate(layoutInflater)
        interestCheckRecyleViewAdapter = InterestCheckRecyleViewAdapter(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()
        checkIsFreeVersion()
    }
    private fun checkIsFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.tvBsdfCollegefilterDifficulty.visibility = View.GONE
                    binding.vBsdfCollegefilterDifficulty.visibility = View.GONE
                    binding.llBsdfCollegefilterDifficulty.visibility = View.GONE

                }else{
                    binding.tvBsdfCollegefilterDifficulty.visibility = View.VISIBLE
                    binding.vBsdfCollegefilterDifficulty.visibility = View.VISIBLE
                    binding.llBsdfCollegefilterDifficulty.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    private fun initialViewSize() {
//        binding.llBsdfInterset.setViewSizeByRatioWH(.915f,-2f)
        binding.ivBsdfCollegefilterDivider.setViewSizeByRatioWH(.145f, .003f)
        binding.ivBsdfCollegefilterDivider.setMarginTopRatioHWithoutBar(.021f)
        binding.tvBsdfCollegefilterTitle.setTextSize(17)
        binding.tvBsdfCollegefilterCountry.setTextSize(15)
        binding.cbBsdfCollegefilterUs.setTextSize(15)
        binding.tvBsdfCollegefilterDifficulty.setTextSize(15)
        binding.cbBsdfCollegefilterFarreach.setTextSize(15)
        binding.cbBsdfCollegefilterReach.setTextSize(15)
        binding.cbBsdfCollegefilterTarget.setTextSize(15)
        binding.cbBsdfCollegefilterSafety.setTextSize(15)
        binding.tvBsdfCollegefilterPublic.setTextSize(15)
        binding.cbBsdfCollegefilterPublic.setTextSize(15)
        binding.cbBsdfCollegefilterPrivate.setTextSize(15)
        binding.tvBsdfCollegefilterOthers.setTextSize(15)
        binding.cbBsdfCollegefilterLiberal.setTextSize(15)
        binding.cbBsdfCollegefilterArt.setTextSize(15)

        binding.btnBsdfCollegefilterDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfCollegefilterClear.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView() {
        binding.tvBsdfCollegefilterCountry.setOnClickListener {
            if (binding.llBsdfCollegefilterCountry.visibility == VISIBLE){
                binding.llBsdfCollegefilterCountry.visibility = View.GONE
                binding.tvBsdfCollegefilterCountry.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfCollegefilterCountry.visibility = View.VISIBLE
                binding.tvBsdfCollegefilterCountry.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }
        binding.llBsdfCollegefilterCountry.visibility = View.VISIBLE
        binding.tvBsdfCollegefilterCountry.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
        binding.cbBsdfCollegefilterUs.isChecked = tagetSchooFilter.isCountry_UnitedStates

        binding.tvBsdfCollegefilterDifficulty.setOnClickListener {
            if (binding.llBsdfCollegefilterDifficulty.visibility == VISIBLE){
                binding.llBsdfCollegefilterDifficulty.visibility = View.GONE
                binding.tvBsdfCollegefilterDifficulty.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfCollegefilterDifficulty.visibility = View.VISIBLE
                binding.tvBsdfCollegefilterDifficulty.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }
        binding.llBsdfCollegefilterDifficulty.visibility = View.VISIBLE
        binding.tvBsdfCollegefilterDifficulty.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
        binding.cbBsdfCollegefilterFarreach.isChecked = tagetSchooFilter.isDifficulty_FarReach
        binding.cbBsdfCollegefilterReach.isChecked = tagetSchooFilter.isDifficulty_Reach
        binding.cbBsdfCollegefilterTarget.isChecked = tagetSchooFilter.isDifficulty_Target
        binding.cbBsdfCollegefilterSafety.isChecked = tagetSchooFilter.isDifficulty_Safety

        binding.tvBsdfCollegefilterPublic.setOnClickListener {
            if (binding.llBsdfCollegefilterPublic.visibility == VISIBLE){
                binding.llBsdfCollegefilterPublic.visibility = View.GONE
                binding.tvBsdfCollegefilterPublic.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfCollegefilterPublic.visibility = View.VISIBLE
                binding.tvBsdfCollegefilterPublic.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }
        binding.llBsdfCollegefilterPublic.visibility = View.VISIBLE
        binding.tvBsdfCollegefilterPublic.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
        binding.cbBsdfCollegefilterPublic.isChecked = tagetSchooFilter.isPublic_Public
        binding.cbBsdfCollegefilterPrivate.isChecked = tagetSchooFilter.isPublic_Private

        binding.tvBsdfCollegefilterOthers.setOnClickListener {
            if (binding.llBsdfCollegefilterOthers.visibility == VISIBLE){
                binding.llBsdfCollegefilterOthers.visibility = View.GONE
                binding.tvBsdfCollegefilterOthers.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_down_black),null)
            }else{
                binding.llBsdfCollegefilterOthers.visibility = View.VISIBLE
                binding.tvBsdfCollegefilterOthers.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
            }
        }
        binding.llBsdfCollegefilterOthers.visibility = View.VISIBLE
        binding.tvBsdfCollegefilterOthers.setCompoundDrawablesWithIntrinsicBounds(null,null,requireActivity().getDrawable(R.drawable.vd_ic_arrow_top),null)
        binding.cbBsdfCollegefilterLiberal.isChecked = tagetSchooFilter.isOthers_LiberalArts
        binding.cbBsdfCollegefilterArt.isChecked = tagetSchooFilter.isOthers_Art
        
        binding.btnBsdfCollegefilterDone.setOnClickListener(this)
        binding.btnBsdfCollegefilterClear.setOnClickListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_bsdf_collegefilter_clear -> {
                onDone(TargetSchooFilter())
                dismiss()
            }
            R.id.btn_bsdf_collegefilter_done -> {
                tagetSchooFilter.isCountry_UnitedStates = binding.cbBsdfCollegefilterUs.isChecked
                // Canada/UK Explore filters removed; Explore remains US-only via SchoolParityRules.
                tagetSchooFilter.isCountry_Canada = false
                tagetSchooFilter.isCountry_UnitedKingdom = false

                tagetSchooFilter.isDifficulty_FarReach = binding.cbBsdfCollegefilterFarreach.isChecked
                tagetSchooFilter.isDifficulty_Reach = binding.cbBsdfCollegefilterReach.isChecked
                tagetSchooFilter.isDifficulty_Target = binding.cbBsdfCollegefilterTarget.isChecked
                tagetSchooFilter.isDifficulty_Safety = binding.cbBsdfCollegefilterSafety.isChecked

                tagetSchooFilter.isPublic_Public = binding.cbBsdfCollegefilterPublic.isChecked
                tagetSchooFilter.isPublic_Private= binding.cbBsdfCollegefilterPrivate.isChecked

                tagetSchooFilter.isOthers_LiberalArts = binding.cbBsdfCollegefilterLiberal.isChecked
                tagetSchooFilter.isOthers_Art = binding.cbBsdfCollegefilterArt.isChecked
                onDone(tagetSchooFilter)
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