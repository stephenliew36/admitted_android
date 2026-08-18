package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.admitted.databinding.BsdialogfragmentSchoolselectBinding
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.InterestSelectBSDailogFragment.Companion.TAG
import com.realtouchapp.admitted.member.view.adapter.AddTargetSchoolRecyleViewAdapter
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.utils.UserInterfaceTool

class SchoolSelectBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    lateinit var doneMeathod: ((ArrayList<UserTargetSchool>) -> Unit)
    var selectetMap = HashMap<String,String>()

    companion object{
        var TAG = "SchoolSelectBSDailogFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentSchoolselectBinding
    lateinit var targetSchools:  ArrayList<TargetSchool>
    var unitedStatesOnly = false

    private fun availableSchools(): ArrayList<TargetSchool> =
        if (unitedStatesOnly) ArrayList(SchoolParityRules.roadMapSchools(targetSchools))
        else ArrayList(targetSchools)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentSchoolselectBinding.inflate(layoutInflater)
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

    private fun initialViewSize(){
//        binding.llBsdfInterset.setViewSizeByRatioWH(.915f,-2f)
        binding.llBsdfSchool.setViewSizeWidthByRatio(.915f)

        binding.ivBsdfSchoolDivider.setViewSizeByRatioWH(.145f,.003f)
        binding.ivBsdfSchoolDivider.setMarginTopRatioHWithoutBar(.021f)

        binding.rlBsdfSchool.setViewSizeByRatioWH(.915f,.043f)
        binding.rlBsdfSchool.setMarginTopRatioHWithoutBar(.031f)

        binding.etBsdfSchool.setViewSizeByRatioWH(.915f,.043f)
        val iv_size = UserInterfaceTool().getScreenHeightWithoutBarByRatio(requireContext(),.018f);
        binding.ivBsdfSchoolLeft.setViewSize(iv_size,iv_size)
        binding.ivBsdfSchoolRight.setViewSize(iv_size,iv_size)

        binding.etBsdfSchool.setTextSize(13)

//        binding.rvBsdfSchool.setMarginTopRatioHWithoutBar(.031f)

        binding.btnBsdfSchoolDone.setMarginTopRatioHWithoutBar(.031f)
        binding.btnBsdfSchoolDone.setMarginBottomRatioHWithoutBar(.031f)
    }

    private fun initialView(){
        binding.etBsdfSchool.addTextChangedListener {
            if (it.toString().equals("")){
                binding.ivBsdfSchoolRight.visibility = View.INVISIBLE
            }else{
                binding.ivBsdfSchoolRight.visibility = View.VISIBLE
            }
        }
        binding.ivBsdfSchoolRight.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvBsdfSchool.layoutManager = linearLayoutManager
        val addTargetSchoolRecyleViewAdapter= AddTargetSchoolRecyleViewAdapter(requireContext())

        val displayedSchools = availableSchools()
        if (displayedSchools.size>15){
            binding.rvBsdfSchool.setViewSizeByRatioWH(.915f,.043f*15)
        }
        binding.rvBsdfSchool.adapter = addTargetSchoolRecyleViewAdapter

        displayedSchools.forEach {
            it.isSelected = false
            if (selectetMap.keys.contains(it.schoolId.toString())){
                it.isSelected = true
            }
        }
        (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).selectetMap = selectetMap
        (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).setData(displayedSchools)


        binding.btnBsdfSchoolDone.setOnClickListener (this)

        binding.etBsdfSchool.addTextChangedListener {

            if (it.toString().equals("")){
                (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).setData(displayedSchools)
            }else{
                var subTargetschhols = displayedSchools.filter { it.schoolName.orEmpty().lowercase().contains(binding.etBsdfSchool.text.toString().lowercase())
                  }
                (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).setData(
                    subTargetschhols as java.util.ArrayList<TargetSchool>
                )
            }
        }
    }
    fun refreshData(){
            val displayedSchools = availableSchools()
            displayedSchools.forEach {
                if (selectetMap.keys.contains(it.schoolId.toString())){
                    it.isSelected = false
                }else{
                    it.isSelected = true
                }
            }
            (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).selectetMap = selectetMap
            (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).setData(displayedSchools)
            (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).notifyDataSetChanged()
    }
    override fun onClick(view: View) {
        when(view.id){
            R.id.iv_bsdf_school_right->{
                binding.etBsdfSchool.setText("")

            }
            R.id.btn_bsdf_school_done->{
                var userTargetSchools = ArrayList<UserTargetSchool>()
                selectetMap = (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).selectetMap
                for (item in (binding.rvBsdfSchool.adapter as AddTargetSchoolRecyleViewAdapter).selectetMap.keys){
                    userTargetSchools.add(UserTargetSchool(item.toInt()))
                }
                doneMeathod(userTargetSchools)

                dismiss()
            }

        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

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
