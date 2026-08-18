package com.realtouchapp.admitted.main.explore.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.databinding.FragmentCollegeDetailBinding
import com.realtouchapp.admitted.common.parity.SchoolParityRules
import com.realtouchapp.admitted.common.parity.BarLegendRules
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.application.ui.fragment.ApplicationsCollegeFragment
import com.realtouchapp.admitted.main.explore.ui.adapter.CollegePhotosAdapter
import com.realtouchapp.admitted.main.explore.viewmodel.CollegeDetailFragmentViewModel
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.CountryCode
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.invisible
import com.realtouchapp.tools.ext.setMargin
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.visible
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class CollegeDetailFragment : Fragment(), View.OnClickListener {
    companion object{
        const val BUNDLE_TARGETSCHOOL = "targetschool"
    }

    private var _binding: FragmentCollegeDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CollegeDetailFragmentViewModel by activityViewModels()
    lateinit var targetSchool: TargetSchool
    var userTargetSchools= ArrayList<UserTargetSchool>()
    var applicationSchools= ArrayList<ApplicationSchool>()
    var isFav = false
    var isAddedApplication = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCollegeDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    fun getData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            targetSchool= arguments?.getSerializable(BUNDLE_TARGETSCHOOL, TargetSchool::class.java)!!
        } else {
            targetSchool = arguments?.getSerializable(BUNDLE_TARGETSCHOOL) as TargetSchool
        }

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initialViewSize()
        initialView()
        subscribeUi()
        checkFreeVersion()
        viewModel.loadUserType()
        viewModel.cacheApplicationSchools()
        viewModel.cacheUserTargeSchools()
    }
    fun subscribeUi() {
        viewModel.responseUserTargetSchool.observe(viewLifecycleOwner){result->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_USERTARGETSCHHOOL, NetworkResult.Action.POST_USERTARGETSCHHOOL, NetworkResult.Action.DELETE_USERTARGETSCHHOOL -> {
                            viewModel.cacheUserTargeSchools()
                        }
                        else ->{}

                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }
                NetworkResult.Status.LOADING -> {

                }
            }
        }
        viewModel.userTargetSchools.observe(viewLifecycleOwner){
            userTargetSchools = it
            var list = userTargetSchools.stream().filter { it.schoolId == targetSchool.schoolId }.collect(
                Collectors.toList()) as ArrayList<TargetSchool>
            if (list.size>0){
                binding.ivCollegetdetailfragmentFav.setImageResource(R.drawable.vd_ic_favorite_filled)
                isFav = true
            }else{
                binding.ivCollegetdetailfragmentFav.setImageResource(R.drawable.vd_ic_favorite_empty)
                isFav = false
            }
        }

        viewModel.responseApplicationSchool.observe(viewLifecycleOwner){result->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_APPLICATIONSCHHOOL, NetworkResult.Action.POST_APPLICATIONSCHHOOL -> {
                            viewModel.cacheApplicationSchools()
                        }
                        else ->{}

                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }
                NetworkResult.Status.LOADING -> {
                }
            }

        }

        viewModel.applicationSchools.observe(viewLifecycleOwner){
            applicationSchools = it

            val userType = viewModel.userType.value

            var list = applicationSchools.stream().filter { it.schoolId == targetSchool.schoolId }.collect(
                Collectors.toList()) as ArrayList<TargetSchool>
            if (list.size>0){
                binding.rlCollegetdetailfragmentAddapplication.visibility = View.GONE
                binding.btnCollegetdetailfragmentChances.visibility = View.VISIBLE
                isAddedApplication = true
            }else{
                if (userType == UserType.STUDENT) {
                    binding.rlCollegetdetailfragmentAddapplication.visibility = View.VISIBLE
                } else {
                    binding.rlCollegetdetailfragmentAddapplication.visibility = View.GONE
                }
                binding.btnCollegetdetailfragmentChances.visibility = View.GONE
                isAddedApplication = false
            }
        }

    }
    fun initialViewSize(){
        binding.ivCollegetdetailfragmentBack.setViewSizeOrignalWH(24,24)
        binding.tvCollegetdetailfragmentBack.setTextSize(13)
        binding.tvCollegetdetailfragmentTitle.setTextSize(22)
        binding.tvCollegetdetailfragmentCity.setTextSize(13)
        binding.ivCollegetdetailfragmentFav.setViewSizeOrignalWH(32,32)
        binding.rlCollegetdetailfragmentAddapplication.setViewSizeOrignalWH(343,48)
        binding.vpCollegetdetailfragment.setViewSizeOrignalWH(343,186)
        binding.llCollegetdetailfragmentDot.setViewSizeOrignalWH(343,30)
        binding.llCollegetdetailfragmentDot.removeAllViews()
        SchoolParityRules.photoPages(targetSchool.photos).let {
            for (item in it) {
                var iv = ImageView(requireContext())
                iv.setImageResource(R.drawable.vd_dot_default)
                binding.llCollegetdetailfragmentDot.addView(iv)
                iv.setViewSizeOrignalWH(8,8)
                iv.setMargin(4,4,4,4)
            }
        }


        binding.tvCollegetdetailfragmentKey.setTextSize(17)

        binding.tvCollegetdetailfragmentNationalrankValue.setTextSize(22)
        binding.tvCollegetdetailfragmentNationalrank.setTextSize(10)
        binding.tvCollegetdetailfragmentTuitionValue.setTextSize(22)
        binding.tvCollegetdetailfragmentTuition.setTextSize(10)
        binding.tvCollegetdetailfragmentChancesValue.setTextSize(22)
        binding.tvCollegetdetailfragmentChances.setTextSize(10)
        binding.btnCollegetdetailfragmentChances.setViewSizeOrignalWH(343,48)

        binding.tvCollegetdetailfragmentApplicationTitle.setTextSize(17)
        binding.tvCollegetdetailfragmentAr.setTextSize(13)
        binding.tvCollegetdetailfragmentArValue.setTextSize(13)
        binding.tvCollegetdetailfragmentAcp.setTextSize(13)
        binding.tvCollegetdetailfragmentAcpValue.setTextSize(13)
        binding.tvCollegetdetailfragmentRl.setTextSize(13)
        binding.tvCollegetdetailfragmentRlValue.setTextSize(13)
        binding.tvCollegetdetailfragmentToefl.setTextSize(13)
        binding.tvCollegetdetailfragmentToeflValue.setTextSize(13)
        binding.tvCollegetdetailfragmentSat.setTextSize(13)
        binding.tvCollegetdetailfragmentSatValue.setTextSize(13)

        binding.tvCollegetdetailfragmentAdmissionstitle.setTextSize(13)
        binding.ivCollegetdetailfragmentAdmissionstitle.setViewSizeOrignalWH(24,24)
        binding.tvCollegetdetailfragmentCr.setTextSize(13)
        binding.tvCollegetdetailfragmentCrValue.setTextSize(13)
        binding.tvCollegetdetailfragmentCrValue.setTextSize(13)
        binding.tvCollegetdetailfragmentGpa.setTextSize(13)
        binding.tvCollegetdetailfragmentGpaValue.setTextSize(13)
        binding.tvCollegetdetailfragmentSts.setTextSize(13)
        binding.tvCollegetdetailfragmentStsValue.setTextSize(13)
        binding.tvCollegetdetailfragmentAe.setTextSize(13)
        binding.tvCollegetdetailfragmentAeValue.setTextSize(13)
        binding.tvCollegetdetailfragmentRecommendaton.setTextSize(13)
        binding.tvCollegetdetailfragmentRecommendatonValue.setTextSize(13)
        binding.tvCollegetdetailfragmentExtra.setTextSize(13)
        binding.tvCollegetdetailfragmentExtraValue.setTextSize(13)
        binding.tvCollegetdetailfragmentVw.setTextSize(13)
        binding.tvCollegetdetailfragmentVwValue.setTextSize(13)
        binding.tvCollegetdetailfragmentWe.setTextSize(13)
        binding.tvCollegetdetailfragmentWeValue.setTextSize(13)
        binding.tvCollegetdetailfragmentAil.setTextSize(13)
        binding.tvCollegetdetailfragmentAilValue.setTextSize(13)

        binding.tvCollegetdetailfragmentImportantdateTitle.setTextSize(13)
        binding.ivCollegetdetailfragmentImportantdateTitle.setViewSizeOrignalWH(24,24)
        binding.tvCollegetdetailfragmentEa.setTextSize(13)
        binding.tvCollegetdetailfragmentEaValue.setTextSize(13)
        binding.tvCollegetdetailfragmentRea.setTextSize(13)
        binding.tvCollegetdetailfragmentReaValue.setTextSize(13)
        binding.tvCollegetdetailfragmentEdi.setTextSize(13)
        binding.tvCollegetdetailfragmentEdiValue.setTextSize(13)
        binding.tvCollegetdetailfragmentDeii.setTextSize(13)
        binding.tvCollegetdetailfragmentDeiiValue.setTextSize(13)
        binding.tvCollegetdetailfragmentRa.setTextSize(13)
        binding.tvCollegetdetailfragmentRaValue.setTextSize(13)
        binding.tvCollegetdetailfragmentDeadline.setTextSize(13)
        binding.tvCollegetdetailfragmentDeadlineValue.setTextSize(13)

        binding.tvCollegetdetailfragmentStudentTitle.setTextSize(17)
        binding.tvCollegetdetailfragmentGender.setTextSize(13)
        binding.pbCollegetdetailfragmentGender.setViewSizeOrignalWH(310,20)
        binding.ivCollegetdetailfragmentMale.setViewSizeOrignalWH(16,16)
        binding.tvCollegetdetailfragmentMale.setTextSize(10)
        binding.tvCollegetdetailfragmentMaleValue.setTextSize(10)
        binding.ivCollegetdetailfragmentFemale.setViewSizeOrignalWH(16,16)
        binding.tvCollegetdetailfragmentFemale.setTextSize(10)
        binding.tvCollegetdetailfragmentFemaleValue.setTextSize(10)
        binding.tvCollegetdetailfragmentTe.setTextSize(13)
        binding.tvCollegetdetailfragmentTeValue.setTextSize(13)
        binding.tvCollegetdetailfragmentTue.setTextSize(13)
        binding.tvCollegetdetailfragmentTueValue.setTextSize(13)
        binding.tvCollegetdetailfragmentIs.setTextSize(13)
        binding.tvCollegetdetailfragmentIsValue.setTextSize(13)
        binding.tvCollegetdetailfragmentHousing.setTextSize(13)
        binding.tvCollegetdetailfragmentHousingValue.setTextSize(13)

        binding.tvCollegetdetailfragmentAcademicTitle.setTextSize(17)
        binding.tvCollegetdetailfragmentCs.setTextSize(13)
        binding.pbCollegetdetailfragment2050.setViewSizeOrignalWH(310,20)
        binding.pbCollegetdetailfragment20fewer.setViewSizeOrignalWH(310,20)
        binding.ivCollegetdetailfragmentFewer20.setViewSizeOrignalWH(16,16)
        binding.tvCollegetdetailfragmentFewer20.setTextSize(10)
        binding.tvCollegetdetailfragmentFewer20Value.setTextSize(10)
        binding.ivCollegetdetailfragment2049.setViewSizeOrignalWH(16,16)
        binding.tvCollegetdetailfragment2049.setTextSize(10)
        binding.tvCollegetdetailfragment2049Value.setTextSize(10)
        binding.ivCollegetdetailfragment50more.setViewSizeOrignalWH(16,16)
        binding.tvCollegetdetailfragment50more.setTextSize(10)
        binding.tvCollegetdetailfragment50moreValue.setTextSize(10)
        binding.tvCollegetdetailfragmentSfr.setTextSize(13)
        binding.tvCollegetdetailfragmentSfrValue.setTextSize(13)


        binding.tvCollegetdetailfragmentLinksTitle.setTextSize(17)

        binding.rlCollegetdetailfragmentSchool.setViewSizeOrignalWH(343,48)
        binding.tvCollegetdetailfragmentSchool.setTextSize(13)

        binding.rlCollegetdetailfragmentAdmissios.setViewSizeOrignalWH(343,48)
        binding.tvCollegetdetailfragmentAdmissios.setTextSize(13)
    }

    fun initialView(){
//        lifecycleScope.launch {
//            viewModel.userType.collect {
//                when (it) {
//                    UserType.PARENT, UserType.TEACHER -> {
//                        // 家長
//                    }
//                    UserType.STUDENT -> {
//                        // 學生
//                    }
//                    else -> { }
//                }
//            }
//        }

        binding.llCollegetdetailfragmentBack.setOnClickListener(this)
        targetSchool.schoolName?.let { binding.tvCollegetdetailfragmentTitle.text = it }
        targetSchool.city?.let {city->
            var content = city
            targetSchool.country?.let {
                when(it){
                    CountryCode.USA.code->content = content+",USA"
                    CountryCode.Canada.code->content = content+",CA"
                    CountryCode.UK.code->content = content+",UK"
                }
            }
            binding.tvCollegetdetailfragmentCity.text = content
        }
        SchoolParityRules.photoPages(targetSchool.photos).let { pages ->
            if (pages.isNotEmpty()) {
                var adapter = CollegePhotosAdapter(requireContext())
                adapter.dataList = ArrayList(pages)
                binding.vpCollegetdetailfragment.adapter = adapter
                binding.vpCollegetdetailfragment.registerOnPageChangeCallback(object :
                    ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    }

                    override fun onPageSelected(position: Int) {
                        for (index in 0 until binding.llCollegetdetailfragmentDot.childCount) {
                            (binding.llCollegetdetailfragmentDot.getChildAt(index) as ImageView)
                                .setImageResource(if (index == position) R.drawable.vd_dot_enable else R.drawable.vd_dot_default)
                        }


                        super.onPageSelected(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                    }
                })
            }
        }

        targetSchool.rank?.let { binding.tvCollegetdetailfragmentNationalrankValue.text = it.toString() }
        targetSchool.tuition?.let { binding.tvCollegetdetailfragmentTuitionValue.text = it.toString() }
        targetSchool.scoreAlgorithm?.let { binding.tvCollegetdetailfragmentChancesValue.text = it+"%"}

        targetSchool.acceptanceRate?.let { binding.tvCollegetdetailfragmentArValue.text = it.toInt().toString()+"%" }
        targetSchool.commonApplicationAccepted?.let { binding.tvCollegetdetailfragmentAcpValue.text = it }
        targetSchool.recommendationsLetter?.let { binding.tvCollegetdetailfragmentRlValue.text = it }
        targetSchool.toeflRequestInternet?.let { binding.tvCollegetdetailfragmentToeflValue.text = it }
        targetSchool.satRequest?.let { binding.tvCollegetdetailfragmentSatValue.text = it }
        targetSchool.classRank?.let { binding.tvCollegetdetailfragmentCrValue.text = it }
        targetSchool.gpaRequest?.let { binding.tvCollegetdetailfragmentGpaValue.text = it }
        targetSchool.standardizedTestScores?.let { binding.tvCollegetdetailfragmentStsValue.text = it }
        targetSchool.applicationEssay?.let { binding.tvCollegetdetailfragmentAeValue.text = it }
        targetSchool.recommendations?.let { binding.tvCollegetdetailfragmentRecommendatonValue.text = it }
        targetSchool.extracurricularActivities?.let { binding.tvCollegetdetailfragmentExtraValue.text = it }
        targetSchool.volunteerWork?.let { binding.tvCollegetdetailfragmentVwValue.text = it }
        targetSchool.workExperience?.let { binding.tvCollegetdetailfragmentWeValue.text = it }
        targetSchool.applicantsInterestLevel?.let { binding.tvCollegetdetailfragmentAilValue.text = it }
        targetSchool.earlyActionApplicationDeadline?.let { binding.tvCollegetdetailfragmentEaValue.text = it }
        targetSchool.restrictiveEarlyAction?.let { binding.tvCollegetdetailfragmentReaValue.text = it }
        targetSchool.earlyDecisionI?.let { binding.tvCollegetdetailfragmentEdiValue.text = it }
        targetSchool.earlyDecisionII?.let { binding.tvCollegetdetailfragmentDeiiValue.text = it }
        targetSchool.regularDecisionApplicationDeadline?.let { binding.tvCollegetdetailfragmentRaValue.text = it }
        targetSchool.deadlineForAcceptingAdmissionOffers?.let { binding.tvCollegetdetailfragmentDeadlineValue.text = it }

        targetSchool.male?.let {
            if(it == 0.0){
                binding.pbCollegetdetailfragmentGender.visibility = View.GONE
                binding.tvCollegetdetailfragmentMaleValue.text = "N/A"
            }else{
                binding.tvCollegetdetailfragmentMaleValue.text= it.toString() +"%"
                binding.pbCollegetdetailfragmentGender.progress = it.toInt()
            }
        }
        targetSchool.female?.let {
            if(it == 0.0){
                binding.pbCollegetdetailfragmentGender.visibility = View.GONE
                binding.tvCollegetdetailfragmentFemaleValue.text = "N/A"
            }else{
                binding.tvCollegetdetailfragmentFemaleValue.text= it.toString() +"%"
            }
        }

        targetSchool.totalEnrollment?.let { binding.tvCollegetdetailfragmentTeValue.text = it.toString() }
        targetSchool.undergraduateEnrollment?.let { binding.tvCollegetdetailfragmentTueValue.text = it.toString() }
        targetSchool.internationalStudents?.let { binding.tvCollegetdetailfragmentIsValue.text = it }
//        targetSchool.internationalStudents?.let { binding.tvCollegetdetailfragmentTeValue.text = it }

        targetSchool.classSizeFewerThan20?.let {
            if(it == 0.0){
                binding.pbCollegetdetailfragment2050.visibility = View.GONE
                binding.pbCollegetdetailfragment20fewer.visibility = View.GONE
                binding.tvCollegetdetailfragmentFewer20Value.text = "N/A"
            }else{
                binding.tvCollegetdetailfragmentFewer20Value.text= it.toString() +"%"
                binding.pbCollegetdetailfragment20fewer.progress = (it).toInt()
            }
        }
        targetSchool.classSizeBetween20And49?.let {
            if(it == 0.0){
                binding.pbCollegetdetailfragment2050.visibility = View.GONE
                binding.pbCollegetdetailfragment20fewer.visibility = View.GONE
                binding.tvCollegetdetailfragment2049Value.text = "N/A"
            }else{
                binding.tvCollegetdetailfragment2049Value.text= it.toString() +"%"
                var middle =  it
                targetSchool.classSizeFewerThan20?.let {
                    middle+= it
                }
                binding.pbCollegetdetailfragment2050.progress = (middle).toInt()
            }
        }
        targetSchool.classSizeMoreThan49?.let {
            if(it == 0.0){
                binding.pbCollegetdetailfragment2050.visibility = View.GONE
                binding.pbCollegetdetailfragment20fewer.visibility = View.GONE
                binding.tvCollegetdetailfragment50moreValue.text = "N/A"
            }else{
                binding.tvCollegetdetailfragment50moreValue.text= it.toString() +"%"
            }
        }
        alignSegmentLegends(targetSchool)
        targetSchool.studentFacultyRatio?.let { binding.tvCollegetdetailfragmentSfrValue.text = it }
        binding.rlCollegetdetailfragmentSchool.visibility = View.VISIBLE
        binding.rlCollegetdetailfragmentSchool.setOnClickListener {
            openExternalUrl(targetSchool.websiteSchool, R.string.school_website_unavailable)
        }
        binding.rlCollegetdetailfragmentAdmissios.visibility = View.VISIBLE
        binding.rlCollegetdetailfragmentAdmissios.setOnClickListener {
            openExternalUrl(targetSchool.websiteAdmission, R.string.school_admissions_unavailable)
        }


        binding.ivCollegetdetailfragmentFav.setOnClickListener(this)
        binding.rlCollegetdetailfragmentAddapplication.setOnClickListener(this)
        binding.btnCollegetdetailfragmentChances.setOnClickListener(this)
        binding.rlCollegetdetailfragmentAdmissionstitle.setOnClickListener(this)
        binding.rlCollegetdetailfragmentImportantdateTitle.setOnClickListener(this)
    }

    private fun alignSegmentLegends(school: TargetSchool) {
        binding.root.post {
            if (_binding == null) return@post

            val genderOrigins = BarLegendRules.origins(
                listOf(school.male ?: 0.0, school.female ?: 0.0)
            )
            alignLegendGroup(
                binding.pbCollegetdetailfragmentGender.width,
                genderOrigins.getOrElse(1) { 0.0 },
                binding.ivCollegetdetailfragmentFemale,
                binding.tvCollegetdetailfragmentFemale,
                binding.tvCollegetdetailfragmentFemaleValue
            )

            val classOrigins = BarLegendRules.origins(listOf(
                school.classSizeFewerThan20 ?: 0.0,
                school.classSizeBetween20And49 ?: 0.0,
                school.classSizeMoreThan49 ?: 0.0
            ))
            alignLegendGroup(
                binding.pbCollegetdetailfragment20fewer.width,
                classOrigins.getOrElse(1) { 0.0 },
                binding.ivCollegetdetailfragment2049,
                binding.llCollegetdetailfragment2049Labels
            )
            alignLegendGroup(
                binding.pbCollegetdetailfragment20fewer.width,
                classOrigins.getOrElse(2) { 0.0 },
                binding.ivCollegetdetailfragment50more,
                binding.llCollegetdetailfragment50moreLabels
            )
        }
    }

    private fun alignLegendGroup(progressWidth: Int, originPercent: Double, anchor: View, vararg labels: View) {
        if (progressWidth <= 0) return
        val delta = (progressWidth * (originPercent / 100.0)).toFloat() - anchor.left
        anchor.translationX = delta
        labels.forEach { it.translationX = delta }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_collegetdetailfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.iv_collegetdetailfragment_fav -> {
                val userType = viewModel.userType.value
                if (userType != UserType.STUDENT) return

                if (isFav){
                    viewModel.deleteUserTargetSchool(UserTargetSchool(targetSchool.schoolId))
                }else{
                    viewModel.postUserTargetSchool(UserTargetSchool(targetSchool.schoolId))
                }
            }

            R.id.rl_collegetdetailfragment_addapplication -> {
                if (mainActivityViewModel.isFreeVersion.value) {
                    PremiumActivity.Intent(requireActivity() as AppCompatActivity)
                } else {
                    viewModel.postApplicationSchool(ApplicationSchool(targetSchool.schoolId,0))
                }
            }
            R.id.btn_collegetdetailfragment_chances -> {
                if (mainActivityViewModel.isFreeVersion.value) {
                    PremiumActivity.Intent(requireActivity() as AppCompatActivity)
                } else {
                    val bundle = Bundle()
                    bundle.putSerializable(ApplicationsCollegeFragment.BUNDLE_TARGETSCHOOL,targetSchool)
                    when(findNavController().graph.id){
                        R.id.mobile_navigation -> {
                            //在Taba1
                            findNavController().navigate(R.id.action_navigation_collegedetail_to_navigation_applicationcollegedetail2, bundle)
                        }
                        R.id.navigation_explore,
                        R.id.navigation_application -> {
                            //在Tab2 / Tab4
                            if (!findNavController().popBackStack(R.id.navigation_applicationcollegedetail, false)) {
                                findNavController().navigate(
                                    R.id.action_navigation_collegedetail_to_navigation_applicationcollegedetail,
                                    bundle
                                )
                            }
                        }
                    }
                }
            }
            R.id.rl_collegetdetailfragment_admissionstitle -> {
                if (binding.llCollegetdetailfragmentAdmissionstitle.visibility ==View.VISIBLE){
                    binding.llCollegetdetailfragmentAdmissionstitle.visibility =View.GONE
                    binding.ivCollegetdetailfragmentAdmissionstitle.setImageResource(R.drawable.vd_ic_arrow_top)
                }else{
                    binding.llCollegetdetailfragmentAdmissionstitle.visibility =View.VISIBLE
                    binding.ivCollegetdetailfragmentAdmissionstitle.setImageResource(R.drawable.vd_ic_arrow_down_black)
                }
            }
            R.id.rl_collegetdetailfragment_importantdate_title -> {
                if (binding.llCollegetdetailfragmentImportantdateTitle.visibility ==View.VISIBLE){
                    binding.llCollegetdetailfragmentImportantdateTitle.visibility =View.GONE
                    binding.ivCollegetdetailfragmentImportantdateTitle.setImageResource(R.drawable.vd_ic_arrow_top)
                }else{
                    binding.llCollegetdetailfragmentImportantdateTitle.visibility =View.VISIBLE
                    binding.ivCollegetdetailfragmentImportantdateTitle.setImageResource(R.drawable.vd_ic_arrow_down_black)
                }
            }
        }
    }
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()

    private fun openExternalUrl(rawUrl: String?, unavailableMessage: Int) {
        val url = SchoolParityRules.normalizeExternalUrl(rawUrl)
        if (url == null) {
            showSnackBar(binding.root, getString(unavailableMessage))
            return
        }
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
            .onFailure { showSnackBar(binding.root, getString(unavailableMessage)) }
    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            mainActivityViewModel.isFreeVersion.collect{
                if (it){
                    binding.tvCollegetdetailfragmentChancesValue.visibility = View.GONE
                    binding.ivCollegetdetailfragmentChancesValue.visibility = View.VISIBLE
                }else{
                    binding.tvCollegetdetailfragmentChancesValue.visibility = View.VISIBLE
                    binding.ivCollegetdetailfragmentChancesValue.visibility = View.GONE
                }
            }
        }
    }
}
