package com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.DialogfragmentAddedAcdemicBinding
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.utils.UserInterfaceTool


class AddSuccessDailogFragment :DialogFragment(), View.OnClickListener {
    companion object{
        const val TAG = "AddSuccessDailogFragment"
        const val TYPE_ACADEMIC = 1
        const val TYPE_IC = 2
        const val TYPE_CI = 3
        const val TYPE_HONORS = 4
        const val TYPE_TEST = 5
        const val TYPE_ACADEMICYEAR = 6
    }
    lateinit var doneMeathod: ((Course) -> Unit)
    private lateinit var binding:DialogfragmentAddedAcdemicBinding
    var type = TYPE_ACADEMIC
    var course:Course? =null
    var intellectualCuriosity: IntellectualCuriosity? =null
    var communityImpact: CommunityImpact? =null
    var honors: Honors? =null
    var test: Test? =null
    var academicYear: AcademicYear? =null
    var name: String =""
    var scoreMax:Int =0
    var score:Int= 0
    var isEnableShow = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogfragmentAddedAcdemicBinding.inflate(layoutInflater)
        //背景設定透明
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialViewSize()
        initialView()

    }

    override fun onResume() {
        super.onResume()
        display()
    }

    fun display(){
        binding.tvDialogfragmentaddedTitle.setText(getString(R.string.tv_dialogfragmentadded_title,name))
        binding.pbDialogfragmentadded.setProgress(score)
        binding.pbDialogfragmentadded.max = scoreMax
        binding.tvDialogfragmentaddedScore.text = "$score / $scoreMax"
        binding.llDialogfragmentaddedAccess.removeAllViews()
        when(type){
            TYPE_ACADEMIC ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_academics)
                binding.pbDialogfragmentadded.visibility = View.GONE
                binding.tvDialogfragmentaddedScore.visibility = View.GONE
                course?.let{data->
                        data.courseName?.let {addText(it)}
                        data.courseLevel?.let {addText(resources.getStringArray(R.array.array_course_level).get(it-1))}
                        data.courseTrack?.let {addText(resources.getStringArray(R.array.array_course_track).get(it-1))}
                        data.grade?.let {addText(it)}
                        data.gradeLevel?.let {
                            addText("Grade $it")
                        }
                }
            }
            TYPE_IC ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_intellectual_interests)
                intellectualCuriosity?.let { data ->
                    data.title?.let {
                        addTextViewAccess(
                            binding.llDialogfragmentaddedAccess,
                            R.drawable.vd_ic_checkmark,
                            it
                        )
                    }
                    data.role?.let {
                        if (it.equals("1")) {
                            addTextViewAccess(
                                binding.llDialogfragmentaddedAccess,
                                R.drawable.vd_ic_checkmark,
                                getString(R.string.tv_bsdf_addciactivity_role_leader)
                            )
                        } else if (it.equals("2")) {
                            addTextViewAccess(
                                binding.llDialogfragmentaddedAccess,
                                R.drawable.vd_ic_checkmark,
                                getString(R.string.tv_bsdf_addciactivity_role_member)
                            )
                        }
                    }
                    data.type?.let {
                        var arr = resources.getStringArray(R.array.array_icactivity_type)
                        addTextViewAccess(
                            binding.llDialogfragmentaddedAccess,
                            R.drawable.vd_ic_checkmark,
                            arr.get(type.toInt()-1)
                        )
                    }
                    data.hoursPerWeek?.let { hours ->
                        data.weekPerYear?.let { weeks ->
                            if (hours ==null || weeks ==null) {

                            } else {


                                val perHour :StringBuilder = StringBuilder()
                                if (hours ==1){
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perhour))
                                }else{
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perhours,"$hours" ))
                                }
                                if (weeks == 1) {
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perweek))
                                }else{
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perweeks,"$weeks"))
                                }

                                addTextViewAccess(
                                    binding.llDialogfragmentaddedAccess,
                                    R.drawable.vd_ic_checkmark,
                                    perHour.toString()
                                )
                            }

                        }
                    }
                    data.grade?.let {
                        if (it.isNullOrEmpty()) {
                            return
                        }
                        var array = it.split("|")
                        var result = ""
                        var list = ArrayList<String>()
                        list.addAll(array)
                        list.removeAt(list.lastIndex)
                        list.removeAt(0)
                        for (item in list) {
                            if (list.size==1){
                                result = result + "Grade " + item
                            }else if (list.last() == item) {
                                result = result + "Grade " + item
                            } else {
                                result = result + "Grade " + item + ","
                            }
                        }
                        addTextViewAccess(
                            binding.llDialogfragmentaddedAccess,
                            R.drawable.vd_ic_checkmark,
                            result
                        )
                    }
                }
            }
            TYPE_CI ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_community_imapct)
                communityImpact?.let {data->
                    data.title?.let {
                        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,it)
                    }
                    data.role?.let {
                        if (it.equals("1")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.tv_bsdf_addciactivity_role_leader))
                        }else if (it.equals("2")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.tv_bsdf_addciactivity_role_member))
                        }
                    }
                    data.type?.let {
                        if (it.equals("1")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.tv_bsdf_addciactivity_type_international))
                        }else if (it.equals("2")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.tv_bsdf_addciactivity_type_notinternational))
                        }
                    }
                    data.hoursPerWeek?.let {hours->
                        data.weekPerYear?.let { weeks ->
                            if (hours==null||weeks == null){

                            }else{
                                val perHour :StringBuilder = StringBuilder()
                                if (hours ==1){
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perhour))
                                }else{
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perhours,"$hours" ))
                                }
                                if (weeks == 1) {
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perweek))
                                }else{
                                    perHour.append(getString(R.string.tv_bsdf_addciactivity_perweeks,"$weeks"))
                                }

                                addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,perHour.toString())
                            }

                        }
                    }
                    data.grade?.let {
                        if (it==null){
                            return
                        }
                        var array = it.split("|")
                        var result = ""
                        var list = ArrayList<String>()
                        list.addAll(array)
                        list.removeAt(list.lastIndex)
                        list.removeAt(0)
                        for (item in list) {
                            if (list.size==1){
                                result = result + "Grade" + item
                            }else if (list.last() == item) {
                                result = result + "Grade" + item
                            } else {
                                result = result + "Grade" + item + ","
                            }
                        }
                        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,result)
                    }
                }
            }
            TYPE_HONORS ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_honors)

                honors?.let { data->
                    data.title?.let {
                        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,it)
                    }
                    data.type?.let {
                        if (it.equals("1")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.et_bsdf_addhonors_type_competion))
                            data.rank?.let {
                                addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,resources.getStringArray(R.array.array_honors_rank)
                                    .get((it.toInt()) - 1))
                            }
                        }else if (it.equals("2")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.et_bsdf_addhonors_type_certificate))
                        }else if (it.equals("3")){
                            addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,getString(R.string.et_bsdf_addhonors_type_research))
                            data.rank?.let {
                                addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,resources.getStringArray(R.array.array_honors_research_type)
                                    .get((it.toInt()) - 1))
                            }
                        } else {
                        }
                    }
                    data.grade?.let {
                        if (it.isNullOrEmpty()){
                            return
                        }
                        var array = it.split("|")
                        var result = ""
                        var list = ArrayList<String>()
                        list.addAll(array)
                        list.removeAt(list.lastIndex)
                        list.removeAt(0)
                        for (item in list){
                            if (list.last() == item){
                                result = result +"Grade "+item
                            }else{
                                result = result +"Grade "+item+","
                            }
                        }
                        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,result)
                    }
                    data.level?.let {
                        if (it>3){
                            return
                        }
                        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,resources.getStringArray(R.array.array_honors_recognition)
                            .get((it.toInt()) - 1))
                    }
                }
            }
            TYPE_TEST ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_tests)
                test?.let { data->
                    addText(data.title())
                    addText(data.score+" / ${data.genreInfo().score_MAX().toInt()}")
                    addText("${data.month},${data.year}")

                }
            }
            TYPE_TEST ->{
                binding.ivDialogfragmentaddedAp.setImageResource(R.drawable.vd_ic_academics)
                academicYear?.let {
                    addText("grade ${it.academicYearId}")
                    addText("${it.grade}  /  5.0")
                }
            }
        }
    }
    private fun initialViewSize(){
//
        binding.ivDialogfragmentaddedAp.setViewSizeOrignalWH(100,100)
        binding.ivDialogfragmentaddedAp.setPadding(UserInterfaceTool().getPixelFromDpByDevice(requireContext(),8))
//
        binding.rlDialogfragmentaddedAp.setMarginTopOrignal(-50)
        binding.rlDialogfragmentaddedAp.setViewSizeOrignalWidth(343)
        binding.rlDialogfragmentaddedAp.setPadding(
            UserInterfaceTool().getPixelFromDpByDevice(requireContext(),15),
            UserInterfaceTool().getScreenHeightWithoutBarByRatio(requireContext(),50f/770f),
            UserInterfaceTool().getPixelFromDpByDevice(requireContext(),15),
            UserInterfaceTool().getPixelFromDpByDevice(requireContext(),32))


        binding.tvDialogfragmentaddedTitle.setTextSize(17)
        binding.tvDialogfragmentaddedTitle.setMarginTopOrignal(16)
        binding.tvDialogfragmentaddedContent.setTextSize(13)
        binding.tvDialogfragmentaddedContent.setMarginTopOrignal(8)
        binding.pbDialogfragmentadded.setViewSizeOrignalWH(201,8)
        binding.tvDialogfragmentaddedScore.setTextSize(13)
        binding.tvDialogfragmentaddedScore.setMarginLeftOrignal(10)

    }
    private fun initialView(){

//        binding.llDialogfragmentaddedAccess.removeAllViews()
//        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,"IBSL English Language & Literature 1")
//        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,"AP")
//        addTextViewAccess(binding.llDialogfragmentaddedAccess, R.drawable.vd_ic_checkmark,"Emglish Track")

        binding.btnDialogfragmentaddedOk.setOnClickListener (this)
    }
    private fun addText(text:String){
        addTextViewAccess(binding.llDialogfragmentaddedAccess,R.drawable.vd_ic_checkmark,text)
    }
    private fun addTextViewAccess(ll_access:LinearLayoutCompat,drawableID:Int,text:String){
        val appCompatTextView = AppCompatTextView(requireContext())
        appCompatTextView.setTextSize(13)
        appCompatTextView.setTextAppearance( R.style.textRaleway_regular13spC4F5257)
        appCompatTextView.text = text
        val img: Drawable? = requireActivity().getDrawable(drawableID)
        appCompatTextView.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
        appCompatTextView.compoundDrawablePadding = UserInterfaceTool().getPixelFromDpByDevice(requireContext(),15)
        ll_access.addView(appCompatTextView)
        appCompatTextView.setMarginTopOrignal(16)
        appCompatTextView.layoutParams.width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT
    }
    override fun onClick(view: View) {
        when(view.id){

            R.id.btn_dialogfragmentadded_ok->{
//                var course = Course(null,)
                dismiss()
            }
        }
    }
    fun show(fragmentManager: FragmentManager){
        if (isEnableShow == true){
            this.show(fragmentManager,TAG)
            isEnableShow = false
        }

    }
}