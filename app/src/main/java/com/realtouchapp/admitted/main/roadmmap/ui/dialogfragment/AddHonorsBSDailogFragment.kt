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
import com.realtouchapp.admitted.databinding.BsdialogfragmentAddhonorsBinding
import com.realtouchapp.admitted.network.entity.Honors
import com.realtouchapp.tools.ext.*

class AddHonorsBSDailogFragment :BottomSheetDialogFragment(), View.OnClickListener {
    companion object{
        const val TAG="AddHonorsBSDailogFragment"
    }
    lateinit var doneMeathod: ((Honors) -> Unit)
    lateinit var editMeathod: ((Honors) -> Unit)
    var honors: Honors? =null
    private var isEditMode = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }
    private lateinit var binding:BsdialogfragmentAddhonorsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BsdialogfragmentAddhonorsBinding.inflate(layoutInflater)
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

        binding.ivBsdfAddhonorsDivider.setViewSizeOrignalWH(54,2)
        binding.ivBsdfAddhonorsDivider.setMarginTopOrignal(16)

        binding.tvBsdfAddhonorsTitle.setTextSize(17)
        binding.tvBsdfAddhonorsTitle.setMarginTopOrignal(24)


        //Name
        binding.tvBsdfAddhonorsName.setTextSize(13)
        binding.tvBsdfAddhonorsName.setMarginTopOrignal(24)
        binding.etBsdfAddhonorsName.setTextSize(13)
        binding.etBsdfAddhonorsName.setMarginTopOrignal(8)
        binding.etBsdfAddhonorsName.setViewSizeOrignalWH(343,45)
        //Award Type
        binding.tvBsdfAddhonorsType.setTextSize(13)
        binding.tvBsdfAddhonorsType.setMarginTopOrignal(24)
        binding.rlBsdfAddhonorsType.setMarginTopOrignal(8)
        binding.rlBsdfAddhonorsType.setViewSizeOrignalWH(343,48)
        binding.spBsdfAddhonorsType.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddhonorsType.setViewSizeOrignalWH(343,45)
        //Rank
        binding.tvBsdfAddhonorsRank.setTextSize(13)
        binding.tvBsdfAddhonorsRank.setMarginTopOrignal(24)
        binding.llBsdfAddhonorsRank.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddhonorsRank.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddhonorsRank.setMarginTopOrignal(8)
        binding.tvBsdfAddhonorsRankFirst.setTextSize(10)
        binding.tvBsdfAddhonorsRankSecond.setTextSize(10)
        binding.tvBsdfAddhonorsRankThird.setTextSize(10)
        binding.tvBsdfAddhonorsRankOther.setTextSize(10)
        //Research Type
        binding.tvBsdfAddhonorsResearchtype.setTextSize(13)
        binding.tvBsdfAddhonorsResearchtype.setMarginTopOrignal(24)
        binding.rlBsdfAddhonorsResearchtype.setMarginTopOrignal(8)
        binding.rlBsdfAddhonorsResearchtype.setViewSizeOrignalWH(343,48)
        binding.spBsdfAddhonorsResearchtype.setViewSizeOrignalWH(343,45)
        binding.etBsdfAddhonorsResearchtype.setViewSizeOrignalWH(343,45)
        //GradeLevel
        binding.tvBsdfAddhonorsGradeLevel.setTextSize(13)
        binding.tvBsdfAddhonorsGradeLevel.setMarginTopOrignal(24)
        binding.llBsdfAddhonorsGradeLevel.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddhonorsGradeLevel.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddhonorsGradeLevel.setMarginTopOrignal(8)
        binding.tvBsdfAddhonorsGradeLevel9.setTextSize(10)
        binding.tvBsdfAddhonorsGradeLevel10.setTextSize(10)
        binding.tvBsdfAddhonorsGradeLevel11.setTextSize(10)
        binding.tvBsdfAddhonorsGradeLevel12.setTextSize(10)
        //Level(s) of Recognition
        binding.tvBsdfAddhonorsRecognition.setTextSize(13)
        binding.tvBsdfAddhonorsRecognition.setMarginTopOrignal(24)
        binding.llBsdfAddhonorsRecognition.setViewSizeOrignalWH(343,48)
        binding.cvBsdfAddhonorsRecognition.setViewSizeOrignalWH(343,45)
        binding.cvBsdfAddhonorsRecognition.setMarginTopOrignal(8)
        binding.tvBsdfAddhonorsRecognitionSchool.setTextSize(10)
        binding.tvBsdfAddhonorsRecognitionState.setTextSize(10)
        binding.tvBsdfAddhonorsRecognitionNational.setTextSize(10)
        binding.tvBsdfAddhonorsRecognitionInternational.setTextSize(10)

        binding.btnBsdfAddhonorsAdd.setMarginTopOrignal(24)
        binding.btnBsdfAddhonorsAdd.setMarginBottomOrignal(36)
    }
    private fun initialView(){
        binding.etBsdfAddhonorsName.addTextChangedListener {
            checkEnableDone()
        }

        requireContext().setSpinner(binding.etBsdfAddhonorsType,binding.spBsdfAddhonorsType,R.array.array_honors_type)

        val arrayList = resources.getStringArray(R.array.array_honors_type).toList()
        binding.etBsdfAddhonorsType.addTextChangedListener {
            val result = it.toString()
            if (result.equals(arrayList.get(0))){
                //Competition
                binding.tvBsdfAddhonorsRank.visibility = View.VISIBLE
                binding.cvBsdfAddhonorsRank.visibility = View.VISIBLE

                binding.tvBsdfAddhonorsResearchtype.visibility = View.GONE
                binding.rlBsdfAddhonorsResearchtype.visibility = View.GONE
                binding.rlBsdfAddhonorsResearchtype.visibility = View.GONE
                honors!!.type =1
                honors!!.pointType =21

                //清除Research的資料
                binding.etBsdfAddhonorsResearchtype.setText("")
                honors!!.researchType=null
            }else if (result.equals(arrayList.get(1))){
                //Certificate
                binding.tvBsdfAddhonorsRank.visibility = View.GONE
                binding.cvBsdfAddhonorsRank.visibility = View.GONE

                binding.tvBsdfAddhonorsResearchtype.visibility = View.GONE
                binding.rlBsdfAddhonorsResearchtype.visibility = View.GONE
                honors!!.type =2
                honors!!.pointType =22

                //清除Competition的資料
                honors!!.rank=null
                binding.tvBsdfAddhonorsRankFirst.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankFirst.tag =0
                binding.tvBsdfAddhonorsRankSecond.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankSecond.tag =0
                binding.tvBsdfAddhonorsRankThird.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankThird.tag =0
                binding.tvBsdfAddhonorsRankOther.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankOther.tag =0
                //清除Research的資料

                binding.etBsdfAddhonorsResearchtype.setText("")
                honors!!.researchType=null

            }else if (result.equals(arrayList.get(2))){
                //Research
                binding.tvBsdfAddhonorsRank.visibility = View.GONE
                binding.cvBsdfAddhonorsRank.visibility = View.GONE
                honors!!.type =3
                honors!!.pointType =23

                binding.tvBsdfAddhonorsResearchtype.visibility = View.VISIBLE
                binding.rlBsdfAddhonorsResearchtype.visibility = View.VISIBLE

                //清除Competition的資料
                honors!!.rank=null
                binding.tvBsdfAddhonorsRankFirst.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankFirst.tag =0
                binding.tvBsdfAddhonorsRankSecond.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankSecond.tag =0
                binding.tvBsdfAddhonorsRankThird.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankThird.tag =0
                binding.tvBsdfAddhonorsRankOther.setBackgroundColor(0)
                binding.tvBsdfAddhonorsRankOther.tag =0
            }
        }
        binding.tvBsdfAddhonorsRankFirst.setOnClickListener (this)
        binding.tvBsdfAddhonorsRankSecond.setOnClickListener (this)
        binding.tvBsdfAddhonorsRankThird.setOnClickListener (this)
        binding.tvBsdfAddhonorsRankOther.setOnClickListener (this)

        requireContext().setSpinner(binding.etBsdfAddhonorsResearchtype,binding.spBsdfAddhonorsResearchtype,R.array.array_honors_research_type)
        binding.etBsdfAddhonorsResearchtype.addTextChangedListener {

            checkEnableDone()
        }
        binding.tvBsdfAddhonorsGradeLevel9.setOnClickListener (this)
        binding.tvBsdfAddhonorsGradeLevel10.setOnClickListener (this)
        binding.tvBsdfAddhonorsGradeLevel11.setOnClickListener (this)
        binding.tvBsdfAddhonorsGradeLevel12.setOnClickListener (this)

        binding.tvBsdfAddhonorsRecognitionSchool.setOnClickListener (this)
        binding.tvBsdfAddhonorsRecognitionState.setOnClickListener (this)
        binding.tvBsdfAddhonorsRecognitionNational.setOnClickListener (this)
        binding.tvBsdfAddhonorsRecognitionInternational.setOnClickListener (this)

        binding.btnBsdfAddhonorsAdd.setOnClickListener (this)
    }

    private fun setEditModeView(){
        if (honors == null){
            isEditMode =false
            //代表是新增
            honors = Honors(null,null,null,null,null,null,null,null,null)
        }else{
            isEditMode = true
        }

        if (!isEditMode){
            binding.tvBsdfAddhonorsTitle.setText(R.string.tv_bsdf_addhonors_title)

            binding.etBsdfAddhonorsName.setText("")
            binding.etBsdfAddhonorsType.setText("")
            binding.spBsdfAddhonorsType.setSelection(0)
            binding.tvBsdfAddhonorsRankFirst.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRankFirst.tag = 0
            binding.tvBsdfAddhonorsRankSecond.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRankSecond.tag = 0
            binding.tvBsdfAddhonorsRankThird.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRankThird.tag = 0
            binding.tvBsdfAddhonorsRankOther.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRankOther.tag = 0
            binding.etBsdfAddhonorsResearchtype.setText("")

            binding.tvBsdfAddhonorsGradeLevel9.setBackgroundColor(0)
            binding.tvBsdfAddhonorsGradeLevel9.tag=0
            binding.tvBsdfAddhonorsGradeLevel10.setBackgroundColor(0)
            binding.tvBsdfAddhonorsGradeLevel10.tag=0
            binding.tvBsdfAddhonorsGradeLevel11.setBackgroundColor(0)
            binding.tvBsdfAddhonorsGradeLevel11.tag=0
            binding.tvBsdfAddhonorsGradeLevel12.setBackgroundColor(0)
            binding.tvBsdfAddhonorsGradeLevel12.tag=0

            binding.tvBsdfAddhonorsRecognitionSchool.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRecognitionSchool.tag=0
            binding.tvBsdfAddhonorsRecognitionState.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRecognitionState.tag=0
            binding.tvBsdfAddhonorsRecognitionNational.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRecognitionNational.tag=0
            binding.tvBsdfAddhonorsRecognitionInternational.setBackgroundColor(0)
            binding.tvBsdfAddhonorsRecognitionInternational.tag=0
        }else{

            binding.tvBsdfAddhonorsTitle.setText(R.string.tv_bsdf_addciactivity_title_edit)

            honors!!.title?.let {
                binding.etBsdfAddhonorsName.setText(it)
            }
            honors!!.type?.let {
                binding.spBsdfAddhonorsType.setSelection(it.toInt())
            }
            honors!!.rank?.let {
                selectRank(it.toInt())
            }
            honors!!.researchType?.let {
                binding.spBsdfAddhonorsResearchtype.setSelection(it.toInt())
            }
            honors!!.grade?.let {
                if (it.contains("9")){
                        binding.tvBsdfAddhonorsGradeLevel9.tag =1
                    binding.tvBsdfAddhonorsGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("10")){
                    binding.tvBsdfAddhonorsGradeLevel10.tag =1
                    binding.tvBsdfAddhonorsGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("11")){
                    binding.tvBsdfAddhonorsGradeLevel11.tag =1
                    binding.tvBsdfAddhonorsGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
                if (it.contains("12")){
                    binding.tvBsdfAddhonorsGradeLevel12.tag =1
                    binding.tvBsdfAddhonorsGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                }
            }

            honors!!.level?.let {
                selectRecognition(it)
            }

            binding.btnBsdfAddhonorsAdd.setText(R.string.btn_bsdf_addciactivity_done)
        }

    }

    private fun selectRank(rank:Int){
        binding.tvBsdfAddhonorsRankFirst.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRankFirst.tag =0
        binding.tvBsdfAddhonorsRankSecond.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRankSecond.tag =0
        binding.tvBsdfAddhonorsRankThird.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRankThird.tag =0
        binding.tvBsdfAddhonorsRankOther.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRankOther.tag =0
        when(rank){

            1->{
                binding.tvBsdfAddhonorsRankFirst.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRankFirst.tag=1
            }
            2->{
                binding.tvBsdfAddhonorsRankSecond.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRankSecond.tag=1
            }
            3->{
                binding.tvBsdfAddhonorsRankThird.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRankThird.tag=1
            }
            4->{
                binding.tvBsdfAddhonorsRankOther.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRankOther.tag=1
            }
        }
        checkEnableDone()

    }
    private fun selectGradeLevel(gradeLevel:Int){
        binding.tvBsdfAddhonorsGradeLevel9.setBackgroundColor(0)
        binding.tvBsdfAddhonorsGradeLevel9.tag=0
        binding.tvBsdfAddhonorsGradeLevel10.setBackgroundColor(0)
        binding.tvBsdfAddhonorsGradeLevel10.tag=0
        binding.tvBsdfAddhonorsGradeLevel11.setBackgroundColor(0)
        binding.tvBsdfAddhonorsGradeLevel11.tag=0
        binding.tvBsdfAddhonorsGradeLevel12.setBackgroundColor(0)
        binding.tvBsdfAddhonorsGradeLevel12.tag=0
        when(gradeLevel){
            9->{
                binding.tvBsdfAddhonorsGradeLevel9.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsGradeLevel9.tag =1
            }
            10->{
                binding.tvBsdfAddhonorsGradeLevel10.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsGradeLevel10.tag =1
            }
            11->{
                binding.tvBsdfAddhonorsGradeLevel11.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsGradeLevel11.tag =1
            }
            12->{
                binding.tvBsdfAddhonorsGradeLevel12.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsGradeLevel12.tag =1
            }
        }
        checkEnableDone()

    }
    private fun selectRecognition(recognition:Int){
        binding.tvBsdfAddhonorsRecognitionSchool.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRecognitionSchool.tag =0
        binding.tvBsdfAddhonorsRecognitionState.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRecognitionState.tag =0
        binding.tvBsdfAddhonorsRecognitionNational.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRecognitionNational.tag =0
        binding.tvBsdfAddhonorsRecognitionInternational.setBackgroundColor(0)
        binding.tvBsdfAddhonorsRecognitionInternational.tag =0
        when(recognition){
            1->{
                binding.tvBsdfAddhonorsRecognitionSchool.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRecognitionSchool.tag =1
            }
            2->{
                binding.tvBsdfAddhonorsRecognitionState.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRecognitionState.tag =1
            }
            3->{
                binding.tvBsdfAddhonorsRecognitionNational.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRecognitionNational.tag =1
            }
            4->{
                binding.tvBsdfAddhonorsRecognitionInternational.setBackgroundColor(Color.parseColor("#F1E9E5"))
                binding.tvBsdfAddhonorsRecognitionInternational.tag =1
            }
        }
        checkEnableDone()
    }
    private fun  checkEnableDone(){
        if (binding.etBsdfAddhonorsName.text.isNullOrEmpty()
            ||binding.etBsdfAddhonorsType.text.isNullOrEmpty()
            ||(binding.tvBsdfAddhonorsGradeLevel9.getTag()==0 && binding.tvBsdfAddhonorsGradeLevel10.getTag()==0 && binding.tvBsdfAddhonorsGradeLevel11.getTag()==0 && binding.tvBsdfAddhonorsGradeLevel12.getTag()==0)
            ||(binding.tvBsdfAddhonorsRecognitionSchool.getTag()==0 && binding.tvBsdfAddhonorsRecognitionState.getTag()==0 && binding.tvBsdfAddhonorsRecognitionNational.getTag()==0 && binding.tvBsdfAddhonorsRecognitionInternational.getTag()==0)){
            enableDone(false)
        }else{
            if (honors!!.type == 1){
                //Competition 需要檢查有沒有沒被勾選
                if (binding.tvBsdfAddhonorsRankFirst.getTag()==0 && binding.tvBsdfAddhonorsRankSecond.getTag()==0 && binding.tvBsdfAddhonorsRankThird.getTag()==0 && binding.tvBsdfAddhonorsRankOther.getTag()==0){
                    //尚未勾選不能新增
                    enableDone(false)
                }else{
                    enableDone(true)
                }

            }else if (honors!!.type == 3){
                //Research 需要檢查有沒有沒被勾選
                if (binding.etBsdfAddhonorsResearchtype.text.isNullOrEmpty()){
                    enableDone(false)
                }else{
                    enableDone(true)
                }
            }else{
                enableDone(true)
            }

        }
    }
    private fun enableDone(isEnable :Boolean){
        if (isEnable) {
            binding.btnBsdfAddhonorsAdd.setBackgroundResource(R.drawable.selector_btn_default)
            binding.btnBsdfAddhonorsAdd.setOnClickListener (this)

        }else{
            binding.btnBsdfAddhonorsAdd.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.btnBsdfAddhonorsAdd.setOnClickListener(null)
        }
    }

    fun collectData(){
        honors!!.title = binding.etBsdfAddhonorsName.text.toString()


        if (honors!!.type ==1){

            if (binding.tvBsdfAddhonorsRankFirst.tag ==1){
                honors!!.rank =1
            }else if (binding.tvBsdfAddhonorsRankSecond.tag ==1){
                honors!!.rank =2
            }else if (binding.tvBsdfAddhonorsRankThird.tag ==1){
                honors!!.rank =3
            }else if (binding.tvBsdfAddhonorsRankOther.tag ==1){
                honors!!.rank =4
            }
        }


        var garde = "|"
        if (binding.tvBsdfAddhonorsGradeLevel9.getTag()==1){
            garde = garde+"9|"
        }
        if (binding.tvBsdfAddhonorsGradeLevel10.getTag()==1){
            garde = garde+"10|"
        }
        if (binding.tvBsdfAddhonorsGradeLevel11.getTag()==1){
            garde = garde+"11|"
        }
        if (binding.tvBsdfAddhonorsGradeLevel12.getTag()==1){
            garde = garde+"12|"
        }
        honors!!.grade = garde


        if (binding.tvBsdfAddhonorsRecognitionSchool.getTag()==1){
            honors!!.level = 1
        }
        if (binding.tvBsdfAddhonorsRecognitionState.getTag()==1){
            honors!!.level = 2
        }
        if (binding.tvBsdfAddhonorsRecognitionNational.getTag()==1){
            honors!!.level =3
        }
        if (binding.tvBsdfAddhonorsRecognitionInternational.getTag()==1){
            honors!!.level = 4
        }

        resources.getStringArray(R.array.array_honors_research_type).indexOf(binding.etBsdfAddhonorsResearchtype.text.toString())?.let {
            honors!!.researchType = (it+1)
        }

        //TODO
        honors!!.point = 0
    }
    override fun onClick(view: View) {
        when(view.id){

            R.id.tv_bsdf_addhonors_rank_first->{
                selectRank(1)
            }
            R.id.tv_bsdf_addhonors_rank_second->{
                selectRank(2)
            }
            R.id.tv_bsdf_addhonors_rank_third->{
                selectRank(3)
            }
            R.id.tv_bsdf_addhonors_rank_other->{
                selectRank(4)
            }
            R.id.tv_bsdf_addhonors_grade_level_9->{
                selectGradeLevel(9)
            }
            R.id.tv_bsdf_addhonors_grade_level_10->{
                selectGradeLevel(10)
            }
            R.id.tv_bsdf_addhonors_grade_level_11->{
                selectGradeLevel(11)
            }
            R.id.tv_bsdf_addhonors_grade_level_12->{
                selectGradeLevel(12)
            }
            R.id.tv_bsdf_addhonors_recognition_school->{
                selectRecognition(1)
            }
            R.id.tv_bsdf_addhonors_recognition_state->{
                selectRecognition(2)
                }
            R.id.tv_bsdf_addhonors_recognition_national->{
                selectRecognition(3)
            }
            R.id.tv_bsdf_addhonors_recognition_international->{
                selectRecognition(4)
            }
            R.id.btn_bsdf_addhonors_add->{
                collectData()
                if (isEditMode){
                    editMeathod(honors!!)
                }else{
                    doneMeathod(honors!!)

                }
                dismiss()
            }

        }
    }
}