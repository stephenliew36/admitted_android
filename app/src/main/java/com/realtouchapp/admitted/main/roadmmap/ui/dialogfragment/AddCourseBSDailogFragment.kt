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
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddcourseBinding
import com.realtouchapp.admitted.network.entity.Course
import com.realtouchapp.tools.ext.*

class AddCourseBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG ="AddCourseBSDailogFragment"
    }
    lateinit var doneMeathod: ((Course) -> Unit)
    lateinit var editMeathod: ((Course) -> Unit)
    var course: Course? =null
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentAddcourseBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddcourseBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        setEditModeView()
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialViewSize()
        initialView()

    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfAddcourseDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddcourseDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddcourseTitle.setTextSize(17)
        binding.tvBsdfAddcourseTitle.setMarginTopOrignal(24)


        //Track
        binding.tvBsdfAddcourseTrack.setTextSize(13)
        binding.tvBsdfAddcourseTrack.setMarginTopOrignal(24)
        binding.rlBsdfAddcourseTrack.setMarginTopOrignal(8)
        binding.rlBsdfAddcourseTrack.setViewSizeOrignalWH(343,48)
        binding.spBsdfAddcourseTrack.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddcourseTrack.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddcourseTrack.setTextSize(13)
        binding.ivBsdfAddcourseTrack.setViewSizeOrignalWH(10,7)
        //Name
        binding.tvBsdfAddcourseName.setTextSize(13)
        binding.tvBsdfAddcourseName.setMarginTopOrignal(24)
        binding.etBsdfAddcourseName.setTextSize(13)
        binding.etBsdfAddcourseName.setMarginTopOrignal(8)
        binding.etBsdfAddcourseName.setViewSizeOrignalWH(343,45)
        //Level
        binding.tvBsdfAddcourseLevel.setTextSize(13)
        binding.tvBsdfAddcourseLevel.setMarginTopOrignal(24)
        binding.rlBsdfAddcourseLevel.setMarginTopOrignal(8)
        binding.rlBsdfAddcourseLevel.setViewSizeOrignalWH(343,48)
        binding.spBsdfAddcourseLevel.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddcourseLevel.setViewSizeOrignalWH(343,45)

        binding.etBsdfAddcourseLevel.setTextSize(13)
        binding.ivBsdfAddcourseLevel.setViewSizeOrignalWH(10,7)
        //Grade
        binding.tvBsdfAddcourseGrade.setTextSize(13)
        binding.tvBsdfAddcourseGrade.setMarginTopOrignal(24)
        binding.etBsdfAddcourseGrade.setTextSize(13)
        binding.etBsdfAddcourseGrade.setMarginTopOrignal(8)
        binding.etBsdfAddcourseGrade.setViewSizeOrignalWH(343,45)
        //GradeLevel
        binding.tvBsdfAddcourseGradeLevel.setTextSize(13)
        binding.tvBsdfAddcourseGradeLevel.setMarginTopOrignal(24)
        binding.llBsdfAddcourseGradeLevel.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddcourseGradeLevel.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddcourseGradeLevel.setMarginTopOrignal(8)

        binding.tvBsdfAddcourseGradeLevel9.setTextSize(10)
        binding.tvBsdfAddcourseGradeLevel9.tag=0
        binding.tvBsdfAddcourseGradeLevel10.setTextSize(10)
        binding.tvBsdfAddcourseGradeLevel10.tag=0
        binding.tvBsdfAddcourseGradeLevel11.setTextSize(10)
        binding.tvBsdfAddcourseGradeLevel11.tag=0
        binding.tvBsdfAddcourseGradeLevel12.setTextSize(10)
        binding.tvBsdfAddcourseGradeLevel12.tag=0

        binding.btnBsdfAddcourseAdd.setMarginTopOrignal(24)
        binding.btnBsdfAddcourseAdd.setMarginBottomOrignal(36)
    }

    private fun setEditModeView(){
        if (course == null){
            isEditMode =false
            //代表是新增
            course = Course(null,null,null,null,null,null,null)
        }else{
            isEditMode = true
        }

        if (!isEditMode){
            binding.tvBsdfAddcourseTitle.setText(R.string.tv_bsdf_addcourse_title)
            binding.etBsdfAddcourseTrack.setText("")
            binding.etBsdfAddcourseName.setText("")
            binding.etBsdfAddcourseLevel.setText("")
            binding.etBsdfAddcourseGrade.setText("")
            binding.tvBsdfAddcourseGradeLevel9.setBackgroundColor(0)
            binding.tvBsdfAddcourseGradeLevel9.tag=0
            binding.tvBsdfAddcourseGradeLevel10.setBackgroundColor(0)
            binding.tvBsdfAddcourseGradeLevel10.tag=0
            binding.tvBsdfAddcourseGradeLevel11.setBackgroundColor(0)
            binding.tvBsdfAddcourseGradeLevel11.tag=0
            binding.tvBsdfAddcourseGradeLevel12.setBackgroundColor(0)
            binding.tvBsdfAddcourseGradeLevel12.tag=0
            binding.btnBsdfAddcourseAdd.setText(R.string.btn_bsdf_addciactivity_add)
            binding.spBsdfAddcourseTrack.setSelection(0)
            binding.spBsdfAddcourseLevel.setSelection(0)
        }else{

            binding.tvBsdfAddcourseTitle.setText(R.string.tv_bsdf_addciactivity_title_edit)

            course!!.courseTrack?.let {
                binding.spBsdfAddcourseTrack.setSelection(it)
            }
            course!!.courseName?.let {
                binding.etBsdfAddcourseName.setText(it)
            }
            course!!.courseLevel?.let {
                binding.spBsdfAddcourseLevel.setSelection(it)
            }
            course!!.grade?.let {
                binding.etBsdfAddcourseGrade.setText(it)
            }
            course!!.gradeLevel?.let {
                selectGradeLevel(it)
            }
            binding.btnBsdfAddcourseAdd.setText(R.string.btn_bsdf_addciactivity_done)
        }

    }
    
    private fun selectGradeLevel(gradeLevel:Int){
        binding.tvBsdfAddcourseGradeLevel9.setBackgroundColor(0)
        binding.tvBsdfAddcourseGradeLevel10.setBackgroundColor(0)
        binding.tvBsdfAddcourseGradeLevel11.setBackgroundColor(0)
        binding.tvBsdfAddcourseGradeLevel12.setBackgroundColor(0)
        binding.tvBsdfAddcourseGradeLevel9.tag=0
        binding.tvBsdfAddcourseGradeLevel10.tag=0
        binding.tvBsdfAddcourseGradeLevel11.tag=0
        binding.tvBsdfAddcourseGradeLevel12.tag=0
        when(gradeLevel){
            9->{
                binding.tvBsdfAddcourseGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddcourseGradeLevel9.tag=1
            }
            10->{
                binding.tvBsdfAddcourseGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddcourseGradeLevel10.tag=1
            }
            11->{
                binding.tvBsdfAddcourseGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddcourseGradeLevel11.tag=1
            }
            12->{
                binding.tvBsdfAddcourseGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddcourseGradeLevel12.tag=1
            }

        }
        checkEnableDone()
    }
    private fun initialView(){

        requireContext().setSpinner(binding.etBsdfAddcourseTrack,binding.spBsdfAddcourseTrack,R.array.array_course_track)
        binding.etBsdfAddcourseTrack.addTextChangedListener {

            checkEnableDone()
        }
        binding.etBsdfAddcourseName.addTextChangedListener {
            checkEnableDone()
        }

        requireContext().setSpinner(binding.etBsdfAddcourseLevel,binding.spBsdfAddcourseLevel,R.array.array_course_level)
        binding.etBsdfAddcourseLevel.addTextChangedListener {
            checkEnableDone()
        }
        binding.etBsdfAddcourseGrade.addTextChangedListener {
            checkEnableDone()
        }

        binding.tvBsdfAddcourseGradeLevel9.setOnClickListener (this)
        binding.tvBsdfAddcourseGradeLevel10.setOnClickListener (this)
        binding.tvBsdfAddcourseGradeLevel11.setOnClickListener (this)
        binding.tvBsdfAddcourseGradeLevel12.setOnClickListener (this)

        binding.btnBsdfAddcourseAdd.setOnClickListener (this)
    }
    private fun  checkEnableDone(){
        if (binding.etBsdfAddcourseTrack.text.isNullOrEmpty()
            ||binding.etBsdfAddcourseName.text.isNullOrEmpty()
            ||binding.etBsdfAddcourseLevel.text.isNullOrEmpty()
            ||binding.etBsdfAddcourseGrade.text.isNullOrEmpty()
            ||(binding.tvBsdfAddcourseGradeLevel9.getTag()==0 && binding.tvBsdfAddcourseGradeLevel10.getTag()==0 && binding.tvBsdfAddcourseGradeLevel11.getTag()==0 && binding.tvBsdfAddcourseGradeLevel12.getTag()==0)){
            enableDone(false)
        }else{
            enableDone(true)
        }
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddcourseAdd.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddcourseAdd.setOnClickListener (this)

        }else{
            binding.btnBsdfAddcourseAdd.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddcourseAdd.setOnClickListener(null)
        }
    }
    fun collectData() {
        course!!.courseName = binding.etBsdfAddcourseName.text.toString()
        course!!.grade = binding.etBsdfAddcourseGrade.text.toString()

        var gradeLevel = 0
        if (binding.tvBsdfAddcourseGradeLevel9.getTag() == 1) {
            gradeLevel = 9
        }
        if (binding.tvBsdfAddcourseGradeLevel10.getTag() == 1) {
            gradeLevel = 10
        }
        if (binding.tvBsdfAddcourseGradeLevel11.getTag() == 1) {
            gradeLevel = 11
        }
        if (binding.tvBsdfAddcourseGradeLevel12.getTag() == 1) {
            gradeLevel = 12
        }
        course!!.gradeLevel = gradeLevel
        if (!binding.etBsdfAddcourseTrack.text.toString().equals("")) {
            resources.getStringArray(R.array.array_course_track)
                .indexOf(binding.etBsdfAddcourseTrack.text.toString())?.let {
                course!!.courseTrack = it + 1
            }
        }

        if (!binding.etBsdfAddcourseLevel.text.toString().equals("")) {
            resources.getStringArray(R.array.array_course_level).indexOf(binding.etBsdfAddcourseLevel.text.toString())?.let {
                course!!.courseLevel = it + 1
            }


        }
    }
    override fun onClick(view: View) {
        when(view.id){

            R.id.tv_bsdf_addcourse_grade_level_9->{
                selectGradeLevel(9)
            }
            R.id.tv_bsdf_addcourse_grade_level_10->{
                selectGradeLevel(10)
            }
            R.id.tv_bsdf_addcourse_grade_level_11->{
                selectGradeLevel(11)
            }
            R.id.tv_bsdf_addcourse_grade_level_12->{
                selectGradeLevel(12)
            }
            R.id.btn_bsdf_addcourse_add->{
                collectData()
                if (isEditMode){
                    editMeathod(course!!)
                }else{
                    doneMeathod(course!!)

                }
                dismiss()
            }

        }
    }

}