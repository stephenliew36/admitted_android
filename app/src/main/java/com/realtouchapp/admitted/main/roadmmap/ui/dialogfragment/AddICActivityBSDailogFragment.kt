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
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddicactivityBinding
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.tools.ext.*


class AddICActivityBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG="AddICActivityBSDailogFragment"
    }
    lateinit var doneMeathod: ((IntellectualCuriosity) -> Unit)
    lateinit var editMeathod: ((IntellectualCuriosity) -> Unit)
    var intellectualCuriosity:IntellectualCuriosity? =null
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentAddicactivityBinding
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddicactivityBinding.inflate(layoutInflater)
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

        binding.ivBsdfAddicactivityDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddicactivityDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddicactivityTitle.setTextSize(17)
        binding.tvBsdfAddicactivityTitle.setMarginTopOrignal(24)


        //Position
        binding.tvBsdfAddicactivityPosition.setTextSize(13)
        binding.tvBsdfAddicactivityPosition.setMarginTopOrignal(24)
        binding.etBsdfAddicactivityPosition.setTextSize(13)
        binding.etBsdfAddicactivityPosition.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityPosition.setViewSizeOrignalWH(343,45)
        //Role
        binding.tvBsdfAddicactivityRole.setTextSize(13)
        binding.tvBsdfAddicactivityRole.setMarginTopOrignal(24)
        binding.llBsdfAddicactivityRole.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddicactivityRole.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddicactivityRole.setMarginTopOrignal(8)
        binding.tvBsdfAddicactivityRoleLeader.setTextSize(10)
        binding.tvBsdfAddicactivityRoleMember.setTextSize(10)
        //Organization
        binding.tvBsdfAddicactivityName.setTextSize(13)
        binding.tvBsdfAddicactivityName.setMarginTopOrignal(24)
        binding.etBsdfAddicactivityName.setTextSize(13)
        binding.etBsdfAddicactivityName.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityName.setViewSizeOrignalWH(343,45)
        //Activity Type
        binding.tvBsdfAddicactivityType.setTextSize(13)
        binding.tvBsdfAddicactivityType.setMarginTopOrignal(24)
        binding.rlBsdfAddicactivityType.setMarginTopOrignal(8)
        binding.rlBsdfAddicactivityType.setViewSizeOrignalWH(343,48)
        binding.spBsdfAddicactivityType.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddicactivityType.setViewSizeOrignalWH(343,45)
        //GradeLevel
        binding.tvBsdfAddicactivityGradeLevel.setTextSize(13)
        binding.tvBsdfAddicactivityGradeLevel.setMarginTopOrignal(24)
        binding.llBsdfAddicactivityGradeLevel.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddicactivityGradeLevel.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddicactivityGradeLevel.setMarginTopOrignal(8)
        binding.tvBsdfAddicactivityGradeLevel9.setTextSize(10)
        binding.tvBsdfAddicactivityGradeLevel10.setTextSize(10)
        binding.tvBsdfAddicactivityGradeLevel11.setTextSize(10)
        binding.tvBsdfAddicactivityGradeLevel12.setTextSize(10)
        binding.tvBsdfAddicactivityGradeLevel9.tag =0
        binding.tvBsdfAddicactivityGradeLevel10.tag =0
        binding.tvBsdfAddicactivityGradeLevel11.tag =0
        binding.tvBsdfAddicactivityGradeLevel12.tag =0
        //Hour
        binding.tvBsdfAddicactivityHours.setTextSize(13)
        binding.tvBsdfAddicactivityHours.setMarginTopOrignal(24)
        binding.etBsdfAddicactivityHours.setTextSize(13)
        binding.etBsdfAddicactivityHours.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityHours.setViewSizeOrignalWH(170,45)
        //Week
        binding.tvBsdfAddicactivityWeeks.setTextSize(13)
        binding.tvBsdfAddicactivityWeeks.setMarginTopOrignal(24)
        binding.etBsdfAddicactivityWeeks.setTextSize(13)
        binding.etBsdfAddicactivityWeeks.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityWeeks.setViewSizeOrignalWH(170,45)
        //Description
        binding.tvBsdfAddicactivityDescription.setTextSize(13)
        binding.tvBsdfAddicactivityDescription.setMarginTopOrignal(24)
        binding.tvBsdfAddicactivityDescriptionContent.setTextSize(10)
        binding.tvBsdfAddicactivityDescriptionContent.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityDescription.setTextSize(13)
        binding.etBsdfAddicactivityDescription.setMarginTopOrignal(8)
        binding.etBsdfAddicactivityDescription.setViewSizeOrignalWH(343,45)

        binding.btnBsdfAddicactivityAdd.setMarginTopOrignal(24)
        binding.btnBsdfAddicactivityAdd.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.tvBsdfAddicactivityTitle.setText(R.string.tv_bsdf_addciactivity_title)

        binding.etBsdfAddicactivityPosition.addTextChangedListener {
            checkEnableDone()
        }
        binding.tvBsdfAddicactivityRoleLeader.setOnClickListener (this)
        binding.tvBsdfAddicactivityRoleMember.setOnClickListener (this)

        binding.etBsdfAddicactivityName.addTextChangedListener {
            checkEnableDone()
        }
        requireContext().setSpinner(binding.etBsdfAddicactivityType,binding.spBsdfAddicactivityType,R.array.array_icactivity_type)
        binding.etBsdfAddicactivityType.addTextChangedListener {
            checkEnableDone()
        }

        binding.tvBsdfAddicactivityGradeLevel9.setOnClickListener (this)
        binding.tvBsdfAddicactivityGradeLevel10.setOnClickListener (this)
        binding.tvBsdfAddicactivityGradeLevel11.setOnClickListener (this)
        binding.tvBsdfAddicactivityGradeLevel12.setOnClickListener (this)

        binding.etBsdfAddicactivityHours.addTextChangedListener {
            checkEnableDone()
        }

        binding.etBsdfAddicactivityWeeks.addTextChangedListener {
            checkEnableDone()
        }
    }
    private fun setEditModeView(){
        if (intellectualCuriosity == null){
            isEditMode =false
            //代表是新增
            intellectualCuriosity = IntellectualCuriosity(null,"","",0,"",0,0,1,"",0,0)
        }else{
            isEditMode = true
        }
        
        if (!isEditMode){
            binding.tvBsdfAddicactivityTitle.setText(R.string.tv_bsdf_addciactivity_title)
            binding.etBsdfAddicactivityPosition.setText("")
            binding.tvBsdfAddicactivityRoleLeader.setBackgroundColor(0)
            binding.tvBsdfAddicactivityRoleLeader.tag=0
            binding.tvBsdfAddicactivityRoleMember.setBackgroundColor(0)
            binding.tvBsdfAddicactivityRoleMember.tag=0
            binding.etBsdfAddicactivityName.setText("")
            binding.etBsdfAddicactivityType.text =""
            binding.tvBsdfAddicactivityGradeLevel9.setBackgroundColor(0)
            binding.tvBsdfAddicactivityGradeLevel9.tag=0
            binding.tvBsdfAddicactivityGradeLevel10.setBackgroundColor(0)
            binding.tvBsdfAddicactivityGradeLevel10.tag=0
            binding.tvBsdfAddicactivityGradeLevel11.setBackgroundColor(0)
            binding.tvBsdfAddicactivityGradeLevel11.tag=0
            binding.tvBsdfAddicactivityGradeLevel12.setBackgroundColor(0)
            binding.tvBsdfAddicactivityGradeLevel12.tag=0
            binding.etBsdfAddicactivityHours.setText("")
            binding.etBsdfAddicactivityWeeks.setText("")
            binding.etBsdfAddicactivityDescription.setText("")
            binding.btnBsdfAddicactivityAdd.setText(R.string.btn_bsdf_addciactivity_add)
        }else{

            binding.tvBsdfAddicactivityTitle.setText(R.string.tv_bsdf_addciactivity_title_edit)

            intellectualCuriosity!!.title?.let {
                binding.etBsdfAddicactivityPosition.setText(it)
            }
            intellectualCuriosity!!.role?.let {
                selectRole(it.toInt())
            }
            intellectualCuriosity!!.name?.let {
                binding.etBsdfAddicactivityName.setText(it)
            }
            intellectualCuriosity!!.type?.let {
                binding.spBsdfAddicactivityType.setSelection(it+1)
            }
            intellectualCuriosity!!.grade?.let {
                if (it.contains("9")){
                    binding.tvBsdfAddicactivityGradeLevel9.tag =1
                    binding.tvBsdfAddicactivityGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("10")){
                    binding.tvBsdfAddicactivityGradeLevel10.tag =1
                    binding.tvBsdfAddicactivityGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("11")){
                    binding.tvBsdfAddicactivityGradeLevel11.tag =1
                    binding.tvBsdfAddicactivityGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("12")){
                    binding.tvBsdfAddicactivityGradeLevel12.tag =1
                    binding.tvBsdfAddicactivityGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
            intellectualCuriosity!!.hoursPerWeek?.let {
                binding.etBsdfAddicactivityHours.setText(it.toString())
            }
            intellectualCuriosity!!.weekPerYear?.let {
                binding.etBsdfAddicactivityWeeks.setText(it.toString())
            }
            intellectualCuriosity!!.description?.let {
                binding.etBsdfAddicactivityDescription.setText(it)
            }
            binding.btnBsdfAddicactivityAdd.setText(R.string.btn_bsdf_addciactivity_done)
        }

    }

    private fun selectRole(role:Int){
        binding.tvBsdfAddicactivityRoleLeader.setBackgroundColor(0)
        binding.tvBsdfAddicactivityRoleLeader.tag =0
        binding.tvBsdfAddicactivityRoleMember.setBackgroundColor(0)
        binding.tvBsdfAddicactivityRoleMember.tag =0
        when(role){
            1->{
                binding.tvBsdfAddicactivityRoleLeader.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddicactivityRoleLeader.tag =1
            }
            2->{
                binding.tvBsdfAddicactivityRoleMember.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddicactivityRoleMember.tag =1
            }
        }
        intellectualCuriosity!!.role = role
        checkEnableDone()
    }
    private fun selectGradeLevel(gradeLevel:Int){
        when(gradeLevel){
            9->{
                if (binding.tvBsdfAddicactivityGradeLevel9.getTag() == 1){
                    binding.tvBsdfAddicactivityGradeLevel9.tag =0
                    binding.tvBsdfAddicactivityGradeLevel9.setBackgroundColor(0)
                }else{
                    binding.tvBsdfAddicactivityGradeLevel9.tag =1
                    binding.tvBsdfAddicactivityGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
            10->{
                if (binding.tvBsdfAddicactivityGradeLevel10.getTag() == 1){
                    binding.tvBsdfAddicactivityGradeLevel10.tag =0
                    binding.tvBsdfAddicactivityGradeLevel10.setBackgroundColor(0)
                }else{
                    binding.tvBsdfAddicactivityGradeLevel10.tag =1
                    binding.tvBsdfAddicactivityGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
            11->{
                if (binding.tvBsdfAddicactivityGradeLevel11.getTag() == 1){
                    binding.tvBsdfAddicactivityGradeLevel11.tag =0
                    binding.tvBsdfAddicactivityGradeLevel11.setBackgroundColor(0)
                }else{
                    binding.tvBsdfAddicactivityGradeLevel11.tag =1
                    binding.tvBsdfAddicactivityGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
            12->{
                if (binding.tvBsdfAddicactivityGradeLevel12.getTag() == 1){
                    binding.tvBsdfAddicactivityGradeLevel12.tag =0
                    binding.tvBsdfAddicactivityGradeLevel12.setBackgroundColor(0)
                }else{
                    binding.tvBsdfAddicactivityGradeLevel12.tag =1
                    binding.tvBsdfAddicactivityGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
        }
        checkEnableDone()
    }
    private fun  checkEnableDone(){
        if (binding.tvBsdfAddicactivityPosition.text.isNullOrEmpty()
            ||(binding.tvBsdfAddicactivityRoleLeader.getTag()==0 && binding.tvBsdfAddicactivityRoleMember.getTag()==0)
            ||binding.etBsdfAddicactivityName.text.isNullOrEmpty()
            ||binding.etBsdfAddicactivityType.text.isNullOrEmpty()
            ||(binding.tvBsdfAddicactivityGradeLevel9.getTag()==0 && binding.tvBsdfAddicactivityGradeLevel10.getTag()==0 && binding.tvBsdfAddicactivityGradeLevel11.getTag()==0 && binding.tvBsdfAddicactivityGradeLevel12.getTag()==0)
            ||binding.etBsdfAddicactivityHours.text.isNullOrEmpty()
            ||binding.etBsdfAddicactivityWeeks.text.isNullOrEmpty()){
            enableDone(false)
        }else{
            enableDone(true)
        }
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddicactivityAdd.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddicactivityAdd.setOnClickListener (this)

        }else{
            binding.btnBsdfAddicactivityAdd.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddicactivityAdd.setOnClickListener(null)
        }
    }

    fun collectData(){
        intellectualCuriosity!!.title = binding.etBsdfAddicactivityPosition.text.toString()
        if (binding.tvBsdfAddicactivityRoleLeader.tag ==1){
            intellectualCuriosity!!.role =1
        }else if (binding.tvBsdfAddicactivityRoleMember.tag ==1){
            intellectualCuriosity!!.role =2
        }
        intellectualCuriosity!!.name = binding.etBsdfAddicactivityName.text.toString()
        intellectualCuriosity!!.hoursPerWeek = binding.etBsdfAddicactivityHours.text.toString().toInt()
        intellectualCuriosity!!.weekPerYear = binding.etBsdfAddicactivityWeeks.text.toString().toInt()

        var garde = "|"
        if (binding.tvBsdfAddicactivityGradeLevel9.getTag()==1){
            garde = garde+"9|"
        }
        if (binding.tvBsdfAddicactivityGradeLevel10.getTag()==1){
            garde = garde+"10|"
        }
        if (binding.tvBsdfAddicactivityGradeLevel11.getTag()==1){
            garde = garde+"11|"
        }
        if (binding.tvBsdfAddicactivityGradeLevel12.getTag()==1){
            garde = garde+"12|"
        }
        intellectualCuriosity!!.grade = garde
        intellectualCuriosity!!.description = binding.etBsdfAddicactivityDescription.text.toString()

        when(intellectualCuriosity!!.type){
            5->{
                //Internship
                intellectualCuriosity!!.pointType = 11
            }
            0,1,2,3,4,6->{
                //School Club
                intellectualCuriosity!!.pointType = 12
            }
        }
        intellectualCuriosity!!.type = resources.getStringArray(R.array.array_icactivity_type).indexOf(binding.etBsdfAddicactivityType.text.toString())
    }
    override fun onClick(view: View) {
        when(view.id){

            R.id.tv_bsdf_addicactivity_role_leader->{
                selectRole(1)

            }
            R.id.tv_bsdf_addicactivity_role_member->{
                selectRole(2)
            }
            R.id.tv_bsdf_addicactivity_grade_level_9->{
                selectGradeLevel(9)
            }
            R.id.tv_bsdf_addicactivity_grade_level_10->{
                selectGradeLevel(10)
            }
            R.id.tv_bsdf_addicactivity_grade_level_11->{
                selectGradeLevel(11)
            }
            R.id.tv_bsdf_addicactivity_grade_level_12->{
                selectGradeLevel(12)
            }
            R.id.btn_bsdf_addicactivity_add->{
                collectData()
                if (isEditMode){
                    editMeathod(intellectualCuriosity!!)
                }else{
                    doneMeathod(intellectualCuriosity!!)

                }

                dismiss()
            }

        }
    }

}