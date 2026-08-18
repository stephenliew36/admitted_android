package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddciactivityBinding
import com.realtouchapp.admitted.network.entity.CommunityImpact
import com.realtouchapp.tools.ext.*

class AddCIActivityBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG="AddCIActivityBSDailogFragment"
    }
    lateinit var doneMeathod: ((CommunityImpact) -> Unit)
    lateinit var editMeathod: ((CommunityImpact) -> Unit)
    var communityImpact: CommunityImpact? =null
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentAddciactivityBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddciactivityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialViewSize()
        initialView()
    }

    override fun onResume() {
        super.onResume()

        setEditModeView()
    }

    private fun initialViewSize(){
        binding.llBsdf.setViewSizeOrignalWidth(343)

        binding.ivBsdfAddciactivityDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddciactivityDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddciactivityTitle.setTextSize(17)
        binding.tvBsdfAddciactivityTitle.setMarginTopOrignal(24)

        binding.tvBsdfAddciactivityTitle.setText(R.string.tv_bsdf_addciactivity_title)
        //Position
        binding.tvBsdfAddciactivityPosition.setTextSize(13)
        binding.tvBsdfAddciactivityPosition.setMarginTopOrignal(24)
        binding.etBsdfAddciactivityPosition.setTextSize(13)
        binding.etBsdfAddciactivityPosition.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityPosition.setViewSizeOrignalWH(343,45)
        //Role
        binding.tvBsdfAddciactivityRole.setTextSize(13)
        binding.tvBsdfAddciactivityRole.setMarginTopOrignal(24)
        binding.llBsdfAddciactivityRole.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddciactivityRole.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddciactivityRole.setMarginTopOrignal(8)
        binding.tvBsdfAddciactivityRoleLeader.setTextSize(10)
        binding.tvBsdfAddciactivityRoleMember.setTextSize(10)
        //Organization Name
        binding.tvBsdfAddciactivityName.setTextSize(13)
        binding.tvBsdfAddciactivityName.setMarginTopOrignal(24)
        binding.etBsdfAddciactivityName.setTextSize(13)
        binding.etBsdfAddciactivityName.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityName.setViewSizeOrignalWH(343,45)
        //Organization Type
        binding.tvBsdfAddciactivityType.setTextSize(13)
        binding.tvBsdfAddciactivityType.setMarginTopOrignal(24)
        binding.llBsdfAddciactivityType.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddciactivityType.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddciactivityType.setMarginTopOrignal(8)
        binding.tvBsdfAddciactivityTypeInternational.setTextSize(10)
        binding.tvBsdfAddciactivityTypeNotinternational.setTextSize(10)
        //GradeLevel
        binding.tvBsdfAddciactivityGradeLevel.setTextSize(13)
        binding.tvBsdfAddciactivityGradeLevel.setMarginTopOrignal(24)
        binding.llBsdfAddciactivityGradeLevel.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddciactivityGradeLevel.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddciactivityGradeLevel.setMarginTopOrignal(8)
        binding.tvBsdfAddciactivityGradeLevel9.setTextSize(10)
        binding.tvBsdfAddciactivityGradeLevel10.setTextSize(10)
        binding.tvBsdfAddciactivityGradeLevel11.setTextSize(10)
        binding.tvBsdfAddciactivityGradeLevel12.setTextSize(10)
        binding.tvBsdfAddciactivityGradeLevel9.tag =0
        binding.tvBsdfAddciactivityGradeLevel10.tag =0
        binding.tvBsdfAddciactivityGradeLevel11.tag =0
        binding.tvBsdfAddciactivityGradeLevel12.tag =0
        //Hour
        binding.tvBsdfAddciactivityHours.setTextSize(13)
        binding.tvBsdfAddciactivityHours.setMarginTopOrignal(24)
        binding.etBsdfAddciactivityHours.setTextSize(13)
        binding.etBsdfAddciactivityHours.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityHours.setViewSizeOrignalWH(170,45)
        //Week
        binding.tvBsdfAddciactivityWeeks.setTextSize(13)
        binding.tvBsdfAddciactivityWeeks.setMarginTopOrignal(24)
        binding.etBsdfAddciactivityWeeks.setTextSize(13)
        binding.etBsdfAddciactivityWeeks.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityWeeks.setViewSizeOrignalWH(170,45)
        //Description
        binding.tvBsdfAddciactivityDescription.setTextSize(13)
        binding.tvBsdfAddciactivityDescription.setMarginTopOrignal(24)
        binding.tvBsdfAddciactivityDescriptionContent.setTextSize(10)
        binding.tvBsdfAddciactivityDescriptionContent.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityDescription.setTextSize(13)
        binding.etBsdfAddciactivityDescription.setMarginTopOrignal(8)
        binding.etBsdfAddciactivityDescription.setViewSizeOrignalWH(343,45)

        binding.btnBsdfAddciactivityAdd.setMarginTopOrignal(24)
        binding.btnBsdfAddciactivityAdd.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.etBsdfAddciactivityPosition.addTextChangedListener {
            checkEnableDone()
        }
        
        binding.tvBsdfAddciactivityRoleLeader.setOnClickListener (this)
        binding.tvBsdfAddciactivityRoleMember.setOnClickListener (this)
        
        binding.etBsdfAddciactivityName.addTextChangedListener {
            checkEnableDone()
        }
        
        binding.tvBsdfAddciactivityTypeInternational.setOnClickListener (this)
        binding.tvBsdfAddciactivityTypeNotinternational.setOnClickListener (this)


        binding.tvBsdfAddciactivityGradeLevel9.setOnClickListener (this)
        binding.tvBsdfAddciactivityGradeLevel10.setOnClickListener (this)
        binding.tvBsdfAddciactivityGradeLevel11.setOnClickListener (this)
        binding.tvBsdfAddciactivityGradeLevel12.setOnClickListener (this)
        binding.etBsdfAddciactivityHours.addTextChangedListener {
            checkEnableDone()
        }

        binding.etBsdfAddciactivityWeeks.addTextChangedListener {
            checkEnableDone()
        }
    }
    private fun setEditModeView(){
        if (communityImpact == null){
            isEditMode =false
            //代表是新增
            communityImpact = CommunityImpact(null,"","",0,"",0,0,0,"",0,0)
        }else{
            isEditMode = true
        }
        if (!isEditMode){
            binding.tvBsdfAddciactivityTitle.setText(R.string.tv_bsdf_addciactivity_title)
            binding.etBsdfAddciactivityPosition.setText("")
            binding.tvBsdfAddciactivityRoleLeader.setBackgroundColor(0)
            binding.tvBsdfAddciactivityRoleLeader.tag=0
            binding.tvBsdfAddciactivityRoleMember.setBackgroundColor(0)
            binding.tvBsdfAddciactivityRoleMember.tag=0
            binding.etBsdfAddciactivityName.setText("")
            binding.tvBsdfAddciactivityTypeInternational.setBackgroundColor(0)
            binding.tvBsdfAddciactivityTypeInternational.tag=0
            binding.tvBsdfAddciactivityTypeNotinternational.setBackgroundColor(0)
            binding.tvBsdfAddciactivityTypeNotinternational.tag=0
            binding.tvBsdfAddciactivityGradeLevel9.setBackgroundColor(0)
            binding.tvBsdfAddciactivityGradeLevel9.tag=0
            binding.tvBsdfAddciactivityGradeLevel10.setBackgroundColor(0)
            binding.tvBsdfAddciactivityGradeLevel10.tag=0
            binding.tvBsdfAddciactivityGradeLevel11.setBackgroundColor(0)
            binding.tvBsdfAddciactivityGradeLevel11.tag=0
            binding.tvBsdfAddciactivityGradeLevel12.setBackgroundColor(0)
            binding.tvBsdfAddciactivityGradeLevel12.tag=0
            binding.etBsdfAddciactivityHours.setText("")
            binding.etBsdfAddciactivityWeeks.setText("")
            binding.etBsdfAddciactivityDescription.setText("")
            binding.btnBsdfAddciactivityAdd.setText(R.string.btn_bsdf_addciactivity_add)

            return
        }else{
            binding.tvBsdfAddciactivityTitle.setText(R.string.tv_bsdf_addciactivity_title_edit)

            communityImpact!!.title?.let {
                binding.etBsdfAddciactivityPosition.setText(it)
            }
            communityImpact!!.role?.let {
                selectRole(it.toInt())
            }
            communityImpact!!.name?.let {
                binding.etBsdfAddciactivityName.setText(it)
            }
            communityImpact!!.type?.let {
                selectInternational(it.toInt())
            }
            communityImpact!!.grade?.let {
                if (it.contains("9")){
                    binding.tvBsdfAddciactivityGradeLevel9.tag =1
                    binding.tvBsdfAddciactivityGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("10")){
                    binding.tvBsdfAddciactivityGradeLevel10.tag =1
                    binding.tvBsdfAddciactivityGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("11")){
                    binding.tvBsdfAddciactivityGradeLevel11.tag =1
                    binding.tvBsdfAddciactivityGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("12")){
                    binding.tvBsdfAddciactivityGradeLevel12.tag =1
                    binding.tvBsdfAddciactivityGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }
            communityImpact!!.hoursPerWeek?.let {
                binding.etBsdfAddciactivityHours.setText(it.toString())
            }
            communityImpact!!.weekPerYear?.let {
                binding.etBsdfAddciactivityWeeks.setText(it.toString())
            }
            communityImpact!!.description?.let {
                binding.etBsdfAddciactivityDescription.setText(it)
            }
            binding.btnBsdfAddciactivityAdd.setText(R.string.btn_bsdf_addciactivity_done)
        }

    }
    private fun selectRole(role:Int){

        binding.tvBsdfAddciactivityRoleLeader.setBackgroundColor(0)
        binding.tvBsdfAddciactivityRoleLeader.tag=0
        binding.tvBsdfAddciactivityRoleMember.setBackgroundColor(0)
        binding.tvBsdfAddciactivityRoleMember.tag=0
        when(role){
            1->{
                binding.tvBsdfAddciactivityRoleLeader.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddciactivityRoleLeader.tag=1
            }
            2->{
                binding.tvBsdfAddciactivityRoleMember.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddciactivityRoleMember.tag=1
            }
        }
        checkEnableDone()
    }
    private fun selectInternational(isNonInternational:Int){

        binding.tvBsdfAddciactivityTypeInternational.setBackgroundColor(0)
        binding.tvBsdfAddciactivityTypeInternational.tag =0
        binding.tvBsdfAddciactivityTypeNotinternational.setBackgroundColor(0)
        binding.tvBsdfAddciactivityTypeNotinternational.tag =0
        when(isNonInternational){
            1->{
                binding.tvBsdfAddciactivityTypeInternational.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddciactivityTypeInternational.tag =1
            }
            2->{
                binding.tvBsdfAddciactivityTypeNotinternational.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddciactivityTypeNotinternational.tag =1
            }
        }
        checkEnableDone()
    }
    private fun selectGradeLevel(gradeLevel:Int){

        when(gradeLevel){
            9->{
                if (binding.tvBsdfAddciactivityGradeLevel9.tag ==1){
                    binding.tvBsdfAddciactivityGradeLevel9.setBackgroundColor(0)
                    binding.tvBsdfAddciactivityGradeLevel9.tag=0
                }else{
                    binding.tvBsdfAddciactivityGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                    binding.tvBsdfAddciactivityGradeLevel9.tag=1
                }
            }
            10->{
                if (binding.tvBsdfAddciactivityGradeLevel10.tag ==1){
                    binding.tvBsdfAddciactivityGradeLevel10.setBackgroundColor(0)
                    binding.tvBsdfAddciactivityGradeLevel10.tag=0
                }else{
                    binding.tvBsdfAddciactivityGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                    binding.tvBsdfAddciactivityGradeLevel10.tag=1
                }
            }
            11->{
                if (binding.tvBsdfAddciactivityGradeLevel11.tag ==1){
                    binding.tvBsdfAddciactivityGradeLevel11.setBackgroundColor(0)
                    binding.tvBsdfAddciactivityGradeLevel11.tag=0
                }else{
                    binding.tvBsdfAddciactivityGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                    binding.tvBsdfAddciactivityGradeLevel11.tag=1
                }
            }
            12->{
                if (binding.tvBsdfAddciactivityGradeLevel12.tag ==1){
                    binding.tvBsdfAddciactivityGradeLevel12.setBackgroundColor(0)
                    binding.tvBsdfAddciactivityGradeLevel12.tag=0
                }else{
                    binding.tvBsdfAddciactivityGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                    binding.tvBsdfAddciactivityGradeLevel12.tag=1
                }
            }
        }
        checkEnableDone()
    }

    private fun  checkEnableDone(){
        if (binding.tvBsdfAddciactivityPosition.text.isNullOrEmpty()
            ||(binding.tvBsdfAddciactivityRoleLeader.getTag()==0 && binding.tvBsdfAddciactivityRoleMember.getTag()==0)
            ||binding.etBsdfAddciactivityName.text.isNullOrEmpty()
            ||(binding.tvBsdfAddciactivityTypeInternational.getTag()==0 && binding.tvBsdfAddciactivityTypeNotinternational.getTag()==0)
            ||(binding.tvBsdfAddciactivityGradeLevel9.getTag()==0 && binding.tvBsdfAddciactivityGradeLevel10.getTag()==0 && binding.tvBsdfAddciactivityGradeLevel11.getTag()==0 && binding.tvBsdfAddciactivityGradeLevel12.getTag()==0)
            ||binding.etBsdfAddciactivityHours.text.isNullOrEmpty()
            ||binding.etBsdfAddciactivityWeeks.text.isNullOrEmpty()){
            enableDone(false)
        }else{
            enableDone(true)
        }
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddciactivityAdd.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddciactivityAdd.setOnClickListener (this)

        }else{
            binding.btnBsdfAddciactivityAdd.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddciactivityAdd.setOnClickListener(null)
        }
    }

    fun collectData(){
        communityImpact!!.title = binding.etBsdfAddciactivityPosition.text.toString()
        if (binding.tvBsdfAddciactivityRoleLeader.tag ==1){
            communityImpact!!.role =1
        }else if (binding.tvBsdfAddciactivityRoleMember.tag ==1){
            communityImpact!!.role =2
        }
        communityImpact!!.name = binding.etBsdfAddciactivityName.text.toString()
        if (binding.tvBsdfAddciactivityTypeInternational.tag ==1){
            communityImpact!!.type =1
        }else if (binding.tvBsdfAddciactivityTypeNotinternational.tag ==1){
            communityImpact!!.type =2
        }
        communityImpact!!.hoursPerWeek = binding.etBsdfAddciactivityHours.text.toString().toInt()
        communityImpact!!.weekPerYear = binding.etBsdfAddciactivityWeeks.text.toString().toInt()
        var garde = "|"
        if (binding.tvBsdfAddciactivityGradeLevel9.getTag()==1){
            garde = garde+"9|"
        }
        if (binding.tvBsdfAddciactivityGradeLevel10.getTag()==1){
            garde = garde+"10|"
        }
        if (binding.tvBsdfAddciactivityGradeLevel11.getTag()==1){
            garde = garde+"11|"
        }
        if (binding.tvBsdfAddciactivityGradeLevel12.getTag()==1){
            garde = garde+"12|"
        }
        communityImpact!!.grade = garde
        communityImpact!!.pointType = 1
        communityImpact!!.description = binding.etBsdfAddciactivityDescription.text.toString()



    }
    
    override fun onClick(view: View) {
        when(view.id){

            R.id.tv_bsdf_addciactivity_role_leader->{
                selectRole(1)

            }
            R.id.tv_bsdf_addciactivity_role_member->{
                selectRole(2)
            }
            R.id.tv_bsdf_addciactivity_type_international->{
                selectInternational(1)
            }
            R.id.tv_bsdf_addciactivity_type_notinternational->{
                selectInternational(2)
            }
            R.id.tv_bsdf_addciactivity_grade_level_9->{
                selectGradeLevel(9)
            }
            R.id.tv_bsdf_addciactivity_grade_level_10->{
                selectGradeLevel(10)
            }
            R.id.tv_bsdf_addciactivity_grade_level_11->{
                selectGradeLevel(11)
            }
            R.id.tv_bsdf_addciactivity_grade_level_12->{
                selectGradeLevel(12)
            }
            R.id.btn_bsdf_addciactivity_add->{
                collectData()
                if (isEditMode){
                    editMeathod(communityImpact!!)
                }else{
                    doneMeathod(communityImpact!!)
                }
                dismiss()
            }

        }
    }
}