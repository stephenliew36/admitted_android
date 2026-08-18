package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddscoreyearBinding
import com.realtouchapp.admitted.network.entity.AcademicYear
import com.realtouchapp.tools.ext.*

class AddScoreYearBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG = "AddScoreYearBSDailogFragment"
    }
    lateinit var doneMeathod: ((AcademicYear) -> Unit)
    lateinit var editMeathod: ((AcademicYear) -> Unit)
    var academicYear: AcademicYear? =null
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentAddscoreyearBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddscoreyearBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialViewSize()
        initialView()

    }

    override fun onResume() {
        super.onResume()
        setEditModeView()
    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfAddscoreyearDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddscoreyearDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddscoreyearTitle.setTextSize(17)
        binding.tvBsdfAddscoreyearTitle.setMarginTopOrignal(24)

        //GradeLevel
        binding.tvBsdfAddscoreyearGradeLevel.setTextSize(13)
        binding.tvBsdfAddscoreyearGradeLevel.setMarginTopOrignal(24)
        binding.llBsdfAddscoreyearGradeLevel.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddscoreyearGradeLevel.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddscoreyearGradeLevel.setMarginTopOrignal(8)
        binding.tvBsdfAddscoreyearGradeLevel9.setTextSize(13)
        binding.tvBsdfAddscoreyearGradeLevel10.setTextSize(13)
        binding.tvBsdfAddscoreyearGradeLevel11.setTextSize(13)
        binding.tvBsdfAddscoreyearGradeLevel12.setTextSize(13)
        binding.tvBsdfAddscoreyearGradeLevel9.tag=0
        binding.tvBsdfAddscoreyearGradeLevel10.tag=0
        binding.tvBsdfAddscoreyearGradeLevel11.tag=0
        binding.tvBsdfAddscoreyearGradeLevel12.tag=0

        //Grade
        binding.tvBsdfAddscoreyearGrade.setTextSize(13)
        binding.tvBsdfAddscoreyearGrade.setMarginTopOrignal(24)
        binding.rlBsdfAddscoreyearGrade.setViewSizeOrignalWH(343,48)
        binding.rlBsdfAddscoreyearGrade.setMarginTopOrignal(8)
        binding.spBsdfAddscoreyearGrade.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddscoreyearGrade.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddscoreyearGrade.setTextSize(13)
        binding.etBsdfAddscoreyearScoreMax.setTextSize(13)

        binding.btnBsdfAddscoreyearAdd.setMarginTopOrignal(24)
        binding.btnBsdfAddscoreyearAdd.setMarginBottomOrignal(36)
    }

    private fun initialView(){


        binding.tvBsdfAddscoreyearGradeLevel9.setOnClickListener (this)
        binding.tvBsdfAddscoreyearGradeLevel10.setOnClickListener (this)
        binding.tvBsdfAddscoreyearGradeLevel11.setOnClickListener (this)
        binding.tvBsdfAddscoreyearGradeLevel12.setOnClickListener (this)

        requireContext().setSpinner(binding.etBsdfAddscoreyearGrade,binding.spBsdfAddscoreyearGrade,R.array.gpaPoint)

        binding.etBsdfAddscoreyearGrade.addTextChangedListener {
            checkEnableDone()
        }
        binding.btnBsdfAddscoreyearAdd.setOnClickListener (this)
    }
    private fun selectGradeLevel(gradeLevel:Int){
        binding.tvBsdfAddscoreyearGradeLevel9.setBackgroundColor(0)
        binding.tvBsdfAddscoreyearGradeLevel10.setBackgroundColor(0)
        binding.tvBsdfAddscoreyearGradeLevel11.setBackgroundColor(0)
        binding.tvBsdfAddscoreyearGradeLevel12.setBackgroundColor(0)
        binding.tvBsdfAddscoreyearGradeLevel9.tag=0
        binding.tvBsdfAddscoreyearGradeLevel10.tag=0
        binding.tvBsdfAddscoreyearGradeLevel11.tag=0
        binding.tvBsdfAddscoreyearGradeLevel12.tag=0
        when(gradeLevel){
            9->{
                binding.tvBsdfAddscoreyearGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddscoreyearGradeLevel9.tag =1
            }
            10->{
                binding.tvBsdfAddscoreyearGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddscoreyearGradeLevel10.tag =1
            }
            11->{
                binding.tvBsdfAddscoreyearGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddscoreyearGradeLevel11.tag =1
            }
            12->{
                binding.tvBsdfAddscoreyearGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddscoreyearGradeLevel12.tag =1
            }

        }

    }
    private fun setEditModeView(){
        if (academicYear == null){
            isEditMode =false
            //代表是新增
            academicYear = AcademicYear("","")
        }else{
            isEditMode = true
        }

        if (!isEditMode){
            binding.tvBsdfAddscoreyearTitle.setText(R.string.tv_bsdf_addscoreyear_title)
            binding.tvBsdfAddscoreyearGradeLevel9.setBackgroundColor(0)
            binding.tvBsdfAddscoreyearGradeLevel9.tag=0
            binding.tvBsdfAddscoreyearGradeLevel10.setBackgroundColor(0)
            binding.tvBsdfAddscoreyearGradeLevel10.tag=0
            binding.tvBsdfAddscoreyearGradeLevel11.setBackgroundColor(0)
            binding.tvBsdfAddscoreyearGradeLevel11.tag=0
            binding.tvBsdfAddscoreyearGradeLevel12.setBackgroundColor(0)
            binding.tvBsdfAddscoreyearGradeLevel12.tag=0
            binding.etBsdfAddscoreyearGrade.setText("")
            binding.spBsdfAddscoreyearGrade.setSelection(0)
        }else{

            binding.tvBsdfAddscoreyearTitle.setText(R.string.tv_bsdf_addciactivity_title_edit)
            academicYear!!.academicYearId?.let {
                selectGradeLevel(it.toInt())
            }

            academicYear!!.grade?.let {
                binding.etBsdfAddscoreyearGrade.setText(it)
            }

        }

    }

    private fun  checkEnableDone(){
        if (binding.etBsdfAddscoreyearGrade.text.isNullOrEmpty()
            ||(binding.tvBsdfAddscoreyearGradeLevel9.getTag()==0 && binding.tvBsdfAddscoreyearGradeLevel10.getTag()==0 && binding.tvBsdfAddscoreyearGradeLevel11.getTag()==0 && binding.tvBsdfAddscoreyearGradeLevel12.getTag()==0)){
            enableDone(false)
        }else{
            enableDone(true)
        }
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddscoreyearAdd.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddscoreyearAdd.setOnClickListener (this)

        }else{
            binding.btnBsdfAddscoreyearAdd.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddscoreyearAdd.setOnClickListener(null)
        }
    }

    fun collectData() {
        academicYear!!.grade = binding.etBsdfAddscoreyearGrade.text.toString()
        var garde = ""
        if (binding.tvBsdfAddscoreyearGradeLevel9.getTag()==1){
            garde = "9"
        }
        if (binding.tvBsdfAddscoreyearGradeLevel10.getTag()==1){
            garde = "10"
        }
        if (binding.tvBsdfAddscoreyearGradeLevel11.getTag()==1){
            garde ="11"
        }
        if (binding.tvBsdfAddscoreyearGradeLevel12.getTag()==1){
            garde ="12"
        }
        academicYear!!.academicYearId = garde
    }
    override fun onClick(view: View) {
        when(view.id){

            R.id.tv_bsdf_addscoreyear_grade_level_9->{
                selectGradeLevel(9)
            }
            R.id.tv_bsdf_addscoreyear_grade_level_10->{
                selectGradeLevel(10)
            }
            R.id.tv_bsdf_addscoreyear_grade_level_11->{
                selectGradeLevel(11)
            }
            R.id.tv_bsdf_addscoreyear_grade_level_12->{
                selectGradeLevel(12)
            }
            R.id.btn_bsdf_addscoreyear_add->{
                collectData()
                if (isEditMode){
                    editMeathod(academicYear!!)
                }else{
                    doneMeathod(academicYear!!)

                }
                dismiss()
            }

        }
    }
}