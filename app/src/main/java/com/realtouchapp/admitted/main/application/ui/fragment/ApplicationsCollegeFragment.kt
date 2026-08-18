package com.realtouchapp.admitted.main.application.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.common.parity.ApplicationTestAdviceRules
import com.realtouchapp.admitted.common.ui.fragment.WebViewFragment
import com.realtouchapp.admitted.databinding.FragmentApplicationcollegeBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationBirefAdapter
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationDateAdapter.DateItem
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationDateAdapter
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationEssayAdapter
import com.realtouchapp.admitted.main.application.viewmodel.ApplicationCollegeFragmentViewModel
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.setMarginTopBydp
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ext.visible
import com.realtouchapp.tools.utils.UserInterfaceTool
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
import androidx.core.net.toUri
import com.realtouchapp.admitted.main.explore.ui.fragment.CollegeDetailFragment

class ApplicationsCollegeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentApplicationcollegeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ApplicationCollegeFragmentViewModel by activityViewModels()
    lateinit var targetSchool: TargetSchool
    var userTargetSchools= ArrayList<UserTargetSchool>()
    lateinit var applicationSchool:ApplicationSchool
    var isFav = false
    var isAddedApplication = false
    private var county="USA"
    companion object {
        private const val TAG = "ApplicationsCollegeFragment"

        const val BUNDLE_TARGETSCHOOL = "targetschool"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplicationcollegeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        getData()
        initialViewSize()
        initialView()
        setCheckList()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        viewModel.loadUserType()
        viewModel.getApplicationSchool()
    }

    private fun getData(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            targetSchool= arguments?.getSerializable(BUNDLE_TARGETSCHOOL, TargetSchool::class.java)!!
        } else {
            arguments?.getSerializable(BUNDLE_TARGETSCHOOL)?.let {
                targetSchool =  it as TargetSchool
            }

        }
    }

    fun subscribeUi() {
        viewModel.currentApplicationSchool.observe(viewLifecycleOwner){it->
            applicationSchool = it
            configureApplyingStatus(it.applyingStatus)

            it.essay?.let {
                if (it.size>0){
                    //
                    binding.llApplciationcollegeEssayBody.visibility = View.VISIBLE
                    binding.rvApplciationcollegeEssayguides.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                    var adapter = ApplicationEssayAdapter(requireContext())
                    adapter.dataList = it as java.util.ArrayList<Essay>
                    adapter.onItemClick= {
                        applicationSchool.essay = it
                        viewModel.putApplicationSchool(applicationSchool)
                    }
                    binding.rvApplciationcollegeEssayguides.adapter = adapter
                }else{
                    binding.llApplciationcollegeEssayBody.visibility = View.GONE
                }
            }
            it.isVaccinationHealth?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegePrearrivalVaccination.isChecked = true
                }else{
                    binding.cbApplciationcollegePrearrivalVaccination.isChecked = false
                }

            }
            it.isNewStudentOrientation?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegePrearrivalNewstudent.isChecked = true
                }else{
                    binding.cbApplciationcollegePrearrivalNewstudent.isChecked = false
                }
            }
            it.isHousing?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegePrearrivalHousing.isChecked = true
                }else{
                    binding.cbApplciationcollegePrearrivalHousing.isChecked = false
                }
            }
            it.isCourseSelection?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegePrearrivalCourse.isChecked = true
                }else{
                    binding.cbApplciationcollegePrearrivalCourse.isChecked = false
                }
            }
            it.isStudentVisa?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalStudentvisa.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalStudentvisa.isChecked = false
                }
            }
            it.isEnteringTheUs?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalEntering.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalEntering.isChecked = false
                }
            }
            it.isTuitionFees?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalTuition.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalTuition.isChecked = false
                }
            }
            it.isBankAccount?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalBankacoount.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalBankacoount.isChecked = false
                }
            }
            it.isMobilePlan?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalMobile.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalMobile.isChecked = false
                }
            }
            it.isAcademicCalendar?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegeInternationalAcademicalendar.isChecked = true
                }else{
                    binding.cbApplciationcollegeInternationalAcademicalendar.isChecked = false
                }
            }
            it.isNewStudentOrientation?.let {
                if (it.equals("1")){
                    binding.cbApplciationcollegePrearrivalVaccination.isChecked = true
                }else{
                    binding.cbApplciationcollegePrearrivalVaccination.isChecked = false
                }
            }

        }
        viewModel.responseApplicationSchools.observe(viewLifecycleOwner){result->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_APPLICATIONSCHHOOL-> {
                            viewModel.cacheApplicationSchool(targetSchool!!.schoolId!!)
                        }
                        else ->{

                        }

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
    }

    fun initialViewSize() {
        binding.rlApplciationcollegeCommonApp.setViewSizeOrignalWH(343,48)
        binding.tvApplciationcollegeCommonApp.setTextSize(13)

        binding.rlApplciationcollegeUniversity.setViewSizeOrignalWH(343,48)
        binding.tvApplciationcollegeUniversity.setTextSize(13)
    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.tvApplciationcollegeApplystatus1.visible()
                        binding.rlApplciationcollegeApplystatus.gone()

                        binding.cbApplciationcollegePrearrivalVaccination.isEnabled = false
                        binding.cbApplciationcollegePrearrivalNewstudent.isEnabled = false
                        binding.cbApplciationcollegePrearrivalHousing.isEnabled = false
                        binding.cbApplciationcollegePrearrivalCourse.isEnabled = false
                        binding.cbApplciationcollegeInternationalStudentvisa.isEnabled = false
                        binding.cbApplciationcollegeInternationalEntering.isEnabled = false
                        binding.cbApplciationcollegeInternationalTuition.isEnabled = false
                        binding.cbApplciationcollegeInternationalBankacoount.isEnabled = false
                        binding.cbApplciationcollegeInternationalMobile.isEnabled = false
                        binding.cbApplciationcollegeInternationalAcademicalendar.isEnabled = false
                    }
                    else -> {
                        // 學生
                        binding.tvApplciationcollegeApplystatus1.gone()
                        binding.rlApplciationcollegeApplystatus.visible()

                        binding.cbApplciationcollegePrearrivalVaccination.isEnabled = true
                        binding.cbApplciationcollegePrearrivalNewstudent.isEnabled = true
                        binding.cbApplciationcollegePrearrivalHousing.isEnabled = true
                        binding.cbApplciationcollegePrearrivalCourse.isEnabled = true
                        binding.cbApplciationcollegeInternationalStudentvisa.isEnabled = true
                        binding.cbApplciationcollegeInternationalEntering.isEnabled = true
                        binding.cbApplciationcollegeInternationalTuition.isEnabled = true
                        binding.cbApplciationcollegeInternationalBankacoount.isEnabled = true
                        binding.cbApplciationcollegeInternationalMobile.isEnabled = true
                        binding.cbApplciationcollegeInternationalAcademicalendar.isEnabled = true
                    }
                }
            }
        }

        binding.llApplciationcollegeBack.setOnClickListener(this)
        setApplyStatus()
        targetSchool.schoolName?.let {
            binding.tvApplciationcollegeName.text = it
        }
        var city =""
        targetSchool.city?.let {
            city = it+", "
        }
        targetSchool.country?.let {

            when(it){
                CountryCode.USA.code-> {
                    //美國
                    city = city + "USA．"
                    county = "USA"
                }
                CountryCode.Canada.code-> {
                    //加拿大
                    city = city + "CA．"
                    county = "CAN"

                    binding.cbApplciationcollegePrearrivalVaccination.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalVaccinationContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesVaccination.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalNewstudentContent.text = getString(R.string.tv_applciationcollege_prearrival_newstudent_content_can)
                    binding.cbApplciationcollegePrearrivalCourse.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalCourseContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesCourse.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalHousingContent.text = getString(R.string.tv_applciationcollege_prearrival_housing_content_can)

                    binding.cbApplciationcollegePrearrivalCourse.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalCourseContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesCourse.visibility = View.GONE

                    binding.cbApplciationcollegePrearrivalTuitionfees.visibility = View.VISIBLE
                    binding.tvApplciationcollegePrearrivalTuitionfeesContent.visibility = View.VISIBLE

                    binding.rlApplciationcollegePrearrivalguidesTuitionfees.visibility = View.VISIBLE
                    binding.rlApplciationcollegePrearrivalguidesTuitionfees.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/prearrivalchecklist/$county/menu?index=tuitionfees")
                        findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
                    }
                    binding.tvApplciationcollegeInternationalStudentvisaContent.text = getString(R.string.tv_applciationcollege_international_studentvisa_content_can)
                    binding.cbApplciationcollegeInternationalEntering.visibility = View.GONE
                    binding.tvApplciationcollegeInternationalEnteringContent.visibility = View.GONE
                    binding.rlApplciationcollegeInternationalguidesEntering.visibility = View.GONE


                    binding.cbApplciationcollegeInternationalMedicalexamination.visibility = View.VISIBLE
                    binding.tvApplciationcollegeInternationalMedicalexaminationContent.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesMedicalexamination.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesMedicalexamination.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=medicalexamination")
                        findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
                    }

                    binding.cbApplciationcollegeInternationalBoardingcustomscheck.visibility = View.VISIBLE
                    binding.tvApplciationcollegeInternationalBoardingcustomscheckContent.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesBoardingcustomscheck.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesBoardingcustomscheck.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=boardingcustomscheck")
                        findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
                    }

                    binding.cbApplciationcollegeInternationalStudypermit.visibility = View.VISIBLE
                    binding.tvApplciationcollegeInternationalStudypermitContent.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesStudypermit.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesStudypermit.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=studypermit")
                        findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
                    }
                    binding.cbApplciationcollegeInternationalTuition.visibility = View.GONE
                    binding.tvApplciationcollegeInternationalTuitionContent.visibility = View.GONE
                    binding.rlApplciationcollegeInternationalguidesTuition.visibility = View.GONE

                    binding.tvApplciationcollegeInternationalBankacoountContent.text = getString(R.string.tv_applciationcollege_international_bankacoount_content_can)
                    binding.tvApplciationcollegeInternationalMobileContent.text = getString(R.string.tv_applciationcollege_international_mobile_content_can)
                    binding.tvApplciationcollegeInternationalAcademicalendarContent.text = getString(R.string.tv_applciationcollege_international_academicalendar_content_can)

                }
                CountryCode.UK.code-> {
                    //英國
                    city = city + "UK．"
                    county = "EN"
                    binding.cbApplciationcollegePrearrivalVaccination.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalVaccinationContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesVaccination.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalNewstudentContent.text = getString(R.string.tv_applciationcollege_prearrival_newstudent_content_uk)
                    binding.cbApplciationcollegePrearrivalCourse.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalCourseContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesCourse.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalHousingContent.text = getString(R.string.tv_applciationcollege_prearrival_housing_content_en)

                    binding.tvApplciationcollegeInternationalBankacoountContent.text = getString(R.string.tv_applciationcollege_international_bankacoount_content_uk)
                    binding.tvApplciationcollegeInternationalMobileContent.text = getString(R.string.tv_applciationcollege_international_mobile_content_uk)

                    binding.cbApplciationcollegePrearrivalVaccination.visibility = View.GONE
                    binding.tvApplciationcollegePrearrivalVaccinationContent.visibility = View.GONE
                    binding.rlApplciationcollegePrearrivalguidesVaccination.visibility = View.GONE


                    binding.tvApplciationcollegeInternationalStudentvisaContent.text = getString(R.string.tv_applciationcollege_international_studentvisa_content_uk)

                    binding.cbApplciationcollegeInternationalVaccinationstbtestandnhs.visibility = View.VISIBLE
                    binding.tvApplciationcollegeInternationalVaccinationstbtestandnhsContent.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesVaccinationstbtestandnhs.visibility = View.VISIBLE
                    binding.rlApplciationcollegeInternationalguidesVaccinationstbtestandnhs.setOnClickListener {
                        var bundle = Bundle()
                        bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=vaccinationstbtestandnhs")
                        findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
                    }

                    binding.cbApplciationcollegeInternationalEntering.visibility = View.GONE
                    binding.tvApplciationcollegeInternationalEnteringContent.visibility = View.GONE
                    binding.rlApplciationcollegeInternationalguidesEntering.visibility = View.GONE

                    binding.cbApplciationcollegeInternationalTuition.visibility = View.GONE
                    binding.tvApplciationcollegeInternationalTuitionContent.visibility = View.GONE
                    binding.rlApplciationcollegeInternationalguidesTuition.visibility = View.GONE

                    binding.tvApplciationcollegeInternationalBankacoountContent.text = getString(R.string.tv_applciationcollege_international_bankacoount_content_uk)
                    binding.tvApplciationcollegeInternationalMobileContent.text = getString(R.string.tv_applciationcollege_international_mobile_content_uk)

                    binding.cbApplciationcollegeInternationalAcademicalendar.visibility = View.GONE
                    binding.tvApplciationcollegeInternationalAcademicalendarContent.visibility = View.GONE
                    binding.rlApplciationcollegeInternationalguidesAcademicalendar.visibility = View.GONE


                }
            }
        }
        targetSchool.websiteSchool?.let {url->
            if (!url.equals("N/A")){
                binding.btnApplciationcollegeProfile.visibility= View.VISIBLE
                binding.btnApplciationcollegeProfile.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }

            }
        }
        //addDateCalander
        var dateItems = ArrayList<ApplicationDateAdapter.DateItem>()

        targetSchool.earlyActionApplicationDeadline.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Early  Action",this))
            }
        }
        targetSchool.restrictiveEarlyAction.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Restrictive Early Action",this))
            }
        }
        targetSchool.earlyDecisionI.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Early Decision I",this))
            }
        }
        targetSchool.earlyDecisionII.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Early Decision II",this))
            }
        }
        targetSchool.regularDecisionApplicationDeadline.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Regular Application",this))
            }
        }
        targetSchool.deadlineForAcceptingAdmissionOffers.apply {
            if (!(this.equals("")||this.equals("N/A"))){
                dateItems.add(DateItem("Deadline for accepting admission offers",this))
            }
        }
        if (dateItems.size>0){
            var adapter = ApplicationDateAdapter(requireContext())
            adapter.dataList = dateItems
            binding.rvApplciationcollegeDate.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            binding.rvApplciationcollegeDate.adapter = adapter
            adapter.onItemClick = {
                val intent = Intent(Intent.ACTION_INSERT).apply {
                    data = CalendarContract.Events.CONTENT_URI

                    if (viewModel.isValidDateFormat(it.date?:"")){
                        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
                        val date = dateFormat.parse(it.date)
                        val calendar = Calendar.getInstance()
                        calendar.time = date
                        calendar.timeZone = TimeZone.getDefault()
                        // 取得日期的毫秒表示形式
                        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis) // 設定日期（以毫秒為單位）
                        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis+360000) // 設定結束日期（通常和開始日期相同）
                    }
                    putExtra(CalendarContract.Events.TITLE, targetSchool.schoolName +"\n"+ it.title)
                }
                startActivity(intent)
            }
            binding.rvApplciationcollegeDate.visibility = View.VISIBLE
            binding.tvApplciationcollegeDates.visibility = View.VISIBLE
        }else{
            binding.rvApplciationcollegeDate.visibility = View.GONE
            binding.tvApplciationcollegeDates.visibility = View.GONE
        }

        binding.tvApplciationcollegeCity.text = city
        var content =""
        targetSchool.isPublic?.let {
            if (it == 0){
                //私立
                content = content+"Private．"
            }else{
                content = content+"Public．"
            }
        }
        targetSchool.tuition?.let {
            content = content+"$"+it
        }
        binding.tvApplciationcollegeInfo.text = content

        targetSchool.scoreAlgorithm?.let { binding.tvApplciationcollegeChancesValue.text = it +"%"}
        // Far Reach 名單學校固定顯示 FAR REACH（與 web 版/iOS 版相同）
        if (TargetSchool.isFarReachSchool(targetSchool)) {
            binding.tvApplciationcollegeDifficulty.text = "FAR REACH"
        } else {
            targetSchool.difficulty?.let { binding.tvApplciationcollegeDifficulty.text = it }
        }

        setGPA()
        setIC()
        setCI()
        setHonor()
        seteStandardized()

        binding.rlApplciationcollegeEssay.setOnClickListener {
            if (binding.llApplciationcollegeEssay.visibility == View.GONE){
                binding.llApplciationcollegeEssay.visibility = View.VISIBLE
                binding.ivApplciationcollegeEssay.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeEssay.visibility = View.GONE
                binding.ivApplciationcollegeEssay.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        binding.rlApplciationcollegeEssayguides.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/essayguides/menu")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeRecommendation.setOnClickListener {
            if (binding.llApplciationcollegeRecommendation.visibility == View.GONE){
                binding.llApplciationcollegeRecommendation.visibility = View.VISIBLE
                binding.ivApplciationcollegeRecommendation.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeRecommendation.visibility = View.GONE
                binding.ivApplciationcollegeRecommendation.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        binding.rlApplciationcollegeRecommendationguides.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/recommendationletters/menu?index=mentortutor")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeRecommendationguidesSubject.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/recommendationletters/menu?index=subjectteacher")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegePrearrival.setOnClickListener {
            if (binding.llApplciationcollegePrearrival.visibility == View.GONE){
                binding.llApplciationcollegePrearrival.visibility = View.VISIBLE
                binding.ivApplciationcollegePrearrival.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegePrearrival.visibility = View.GONE
                binding.ivApplciationcollegePrearrival.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }

        binding.rlApplciationcollegePrearrivalguidesVaccination.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/prearrivalchecklist/$county/menu?index=vaccinationhealthrecords")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegePrearrivalguidesNewstudent.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/prearrivalchecklist/$county/menu?index=newstudentorientation")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegePrearrivalguidesHousing.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/prearrivalchecklist/$county/menu?index=housing")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegePrearrivalguidesCourse.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/prearrivalchecklist/$county/menu?index=courseselection")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternational.setOnClickListener {
            if (binding.llApplciationcollegeInternational.visibility == View.GONE){
                binding.llApplciationcollegeInternational.visibility = View.VISIBLE
                binding.ivApplciationcollegeInternational.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeInternational.visibility = View.GONE
                binding.ivApplciationcollegeInternational.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        binding.rlApplciationcollegeInternationalguidesStudentvisa.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=studentvisa")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternationalguidesEntering.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=enteringtheusonastudentvisa")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternationalguidesTuition.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=tuitionfees")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternationalguidesBankacoount.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=bankaccount")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternationalguidesMobile.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=mobileplan")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        binding.rlApplciationcollegeInternationalguidesAcademicalendar.setOnClickListener {
            var bundle = Bundle()
            bundle.putSerializable(WebViewFragment.BUNDLE_WEBVIEWEURL,"${AdmittedApi.SERVER_ADDRESS}/internationalstudentchecklist/$county/menu?index=academiccalendar")
            findNavController().navigate(R.id.action_navigation_applicationcollegedetail_to_navigation_webviewfragment, bundle)
        }
        // 2025.05.20 修正1.1Application上榜機率右邊加一個「i」的圖示，點擊後顯示固定內容
        binding.ivApplciationcollegeExclamation.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Your Level of Readiness may vary by school, depending on each institution's specific application requirements. Colleges often prioritize different selection factors when making admission decisions, and Admitted's got them all.")
                .setNeutralButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .also { it.show() }
        }

        if (targetSchool.commonApplicationAccepted?.equals("yes", ignoreCase = true) == true) {
            binding.rlApplciationcollegeCommonApp.visibility = View.VISIBLE
            binding.rlApplciationcollegeCommonApp.setOnClickListener {
                val url = "https://www.commonapp.org/"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = url.toUri()
                startActivity(intent)
            }

        } else {
            binding.rlApplciationcollegeCommonApp.visibility = View.GONE
        }

        // 修正 1.3
        // B. Applications多一個「Go To University」，可以跳轉到該學校的頁面。
        // 然後該學校的頁面中，如果該學校有在Application清單中，多一個「Go To Application」的按鈕，可以跳轉到申請的學校內容中。
        binding.rlApplciationcollegeUniversity.setOnClickListener {
            if (!findNavController().popBackStack(R.id.navigation_collegedetail, false)) {
                val bundle = Bundle()
                bundle.putSerializable(
                    CollegeDetailFragment.BUNDLE_TARGETSCHOOL,
                    targetSchool
                )

                findNavController().navigate(
                    R.id.action_navigation_applicationcollegedetail_to_navigation_collegedetail,
                    bundle
                )
            }
        }
    }

    private fun setApplyStatus(){
        var tmppArrayList=ArrayList<String>()
        tmppArrayList.add("")
        tmppArrayList.addAll(requireContext().resources.getStringArray(R.array.array_applystatus))
        val spinnerArrayAdapter= SpinnerArrayAdapter(requireContext(),tmppArrayList.toList())
        binding.spApplciationcollegeApplystatus.adapter = spinnerArrayAdapter
        binding.spApplciationcollegeApplystatus.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                    if (!tmppArrayList[pos].equals("")){
                        configureApplyingStatus(pos-1)
                        applicationSchool.applyingStatus = pos-1
                        viewModel.putApplicationSchool(applicationSchool)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        binding.spApplciationcollegeApplystatus.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(binding.tvApplciationcollegeApplystatus.context,0.068f)
        //設定初始值不為0 才能觸發按相同位置
        binding.spApplciationcollegeApplystatus.setSelection(-1,false)
    }

    private fun configureApplyingStatus(position: Int) {
        val applyList = requireContext().resources.getStringArray(R.array.array_applystatus)
        binding.tvApplciationcollegeApplystatus.text = applyList[position]
        binding.tvApplciationcollegeApplystatus1.text = applyList[position]

        when (position) {
            2 -> {
                // Waitlisted
                binding.tvApplciationcollegeApplystatus.setTextColor(Color.parseColor("#4F5257"))
                binding.ivApplciationcollegeApplystatus.setBackgroundResource(R.drawable.bg_corner5_f3f0dd)

                binding.tvApplciationcollegeApplystatus1.setTextColor(Color.parseColor("#4F5257"))
                binding.tvApplciationcollegeApplystatus1.setBackgroundResource(R.drawable.bg_corner5_fdeab2)
            }
            3 -> {
                // Accepted
                binding.tvApplciationcollegeApplystatus.setTextColor(Color.parseColor("#4F5257"))
                binding.ivApplciationcollegeApplystatus.setBackgroundResource(R.drawable.bg_corner5_cde3cf)

                binding.tvApplciationcollegeApplystatus1.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvApplciationcollegeApplystatus1.setBackgroundResource(R.drawable.bg_corner5_679a6c)
            }
            4 -> {
                // Rejected
                binding.tvApplciationcollegeApplystatus.setTextColor(Color.parseColor("#4F5257"))
                binding.ivApplciationcollegeApplystatus.setBackgroundResource(R.drawable.bg_corner5_dddddd)

                binding.tvApplciationcollegeApplystatus1.setTextColor(Color.parseColor("#4F5257"))
                binding.tvApplciationcollegeApplystatus1.setBackgroundResource(R.drawable.bg_corner5_d6dbe3)
            }
            5 -> {
                // Enrolled
                binding.tvApplciationcollegeApplystatus.setTextColor(Color.parseColor("#4F5257"))
                binding.ivApplciationcollegeApplystatus.setBackgroundResource(R.drawable.bg_corner5_d2dced)

                binding.tvApplciationcollegeApplystatus1.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvApplciationcollegeApplystatus1.setBackgroundResource(R.drawable.bg_corner5_3f5d95)
            }
            else -> {
                // Applying, Applied
                binding.tvApplciationcollegeApplystatus.setTextColor(Color.parseColor("#3F5D95"))
                binding.ivApplciationcollegeApplystatus.setBackgroundResource(R.drawable.bg_corner5_f2f4f6)

                binding.tvApplciationcollegeApplystatus1.setTextColor(Color.parseColor("#3F5D95"))
                binding.tvApplciationcollegeApplystatus1.setBackgroundResource(R.drawable.bg_stroke_9eaec5)
            }
        }
    }

    // FIXME: 家長
    private fun setCheckList() {
        binding.cbApplciationcollegePrearrivalVaccination.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isVaccinationHealth = "1"
            }else{
                applicationSchool.isVaccinationHealth = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }

        binding.cbApplciationcollegePrearrivalNewstudent.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isNewStudentOrientation = "1"
            }else{
                applicationSchool.isNewStudentOrientation = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegePrearrivalHousing.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isHousing = "1"
            }else{
                applicationSchool.isHousing = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegePrearrivalCourse.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isCourseSelection = "1"
            }else{
                applicationSchool.isCourseSelection = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalStudentvisa.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isStudentVisa = "1"
            }else{
                applicationSchool.isStudentVisa = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalEntering.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isEnteringTheUs = "1"
            }else{
                applicationSchool.isEnteringTheUs = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalTuition.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isTuitionFees = "1"
            }else{
                applicationSchool.isTuitionFees = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalBankacoount.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isBankAccount = "1"
            }else{
                applicationSchool.isBankAccount = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalMobile.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isMobilePlan = "1"
            }else{
                applicationSchool.isMobilePlan = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }
        binding.cbApplciationcollegeInternationalAcademicalendar.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                applicationSchool.isAcademicCalendar = "1"
            }else{
                applicationSchool.isAcademicCalendar = "0"
            }
            viewModel.putApplicationSchool(applicationSchool)
        }


    }
    private fun setGPA(){
        binding.rlApplciationcollegeAcademic.setOnClickListener {
            if (binding.llApplciationcollegeAcademic.visibility == View.GONE){
                binding.llApplciationcollegeAcademic.visibility = View.VISIBLE
                binding.ivApplciationcollegeAcademic.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeAcademic.visibility = View.GONE
                binding.ivApplciationcollegeAcademic.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }

        var gpa =(requireActivity() as MainActivity).scoreAlgorithm.getGpaScoreTotalAverge()
        var index = 0
        when (gpa) {
            in 4.3..5.0 ->{
                index = 0
            }
            in 4.0..4.299 ->{
                index =1
            }
            in 3.5..3.999 ->{
                index = 2
            }
            in 3.0..3.499 ->{
                index = 3
            }
            in 0.0..2.999 ->{
                index = 4
            }
            else -> {
            }
        }

        var gpaTitles = requireContext().resources.getStringArray(R.array.array_application_gpa_title)
        var gpaContents = requireContext().resources.getStringArray(R.array.array_application_gpa_content)
        var gpaLevel = requireContext().resources.getStringArray(R.array.array_application_gpa_level)
        var gpaAdvice = requireContext().resources.getStringArray(   R.array.array_application_gpa_advice)

        binding.tvApplciationcollegeAcademicTitle.text = gpaTitles.get(index)
        binding.tvApplciationcollegeAcademicContent.text = gpaContents.get(index)
        binding.tvApplciationcollegeAcademicLevel.text =String.format("%.1f", gpa) + gpaLevel.get(index)
        binding.tvApplciationcollegeAcademicSubcontent.text = gpaAdvice.get(index)
    }

    private fun setIC(){
        binding.rlApplciationcollegeIntellectual.setOnClickListener {
            if (binding.llApplciationcollegeIntellectual.visibility == View.GONE){
                binding.llApplciationcollegeIntellectual.visibility = View.VISIBLE
                binding.ivApplciationcollegeIntellectual.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeIntellectual.visibility = View.GONE
                binding.ivApplciationcollegeIntellectual.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        binding.rlApplciationcollegeIntellectualExplore.setOnClickListener {
            (requireActivity() as MainActivity).setSelecetTab(1)
        }
        var honorPoint =(requireActivity() as MainActivity).scoreAlgorithm.getHonroScorePercent()
        var index = 0
        when (honorPoint) {
            in 60..75 ->{
                index = 0
            }
            in 45..59 ->{
                index =1
            }
            in 30..44 ->{
                index = 2
            }
            in 15..29 ->{
                index = 3
            }
            in 0..14 ->{
                index = 4
            }
            else -> {
            }
        }

        var icTitles = requireContext().resources.getStringArray(R.array.array_application_ic_title)
        var icContent = requireContext().resources.getStringArray(R.array.array_application_ic_content)
        var icAdvice = requireContext().resources.getStringArray(R.array.array_application_ic_advice)

        binding.tvApplciationcollegeIntellectualTitle.text = icTitles.get(index)
        binding.tvApplciationcollegeIntellectualContent.text = icContent.get(index)
        binding.tvApplciationcollegeIntellectualSubcontent.text = icAdvice.get(index)
    }
    private fun setCI(){
        binding.rlApplciationcollegeCommunity.setOnClickListener {
            if (binding.llApplciationcollegeCommunity.visibility == View.GONE){
                binding.llApplciationcollegeCommunity.visibility = View.VISIBLE
                binding.ivApplciationcollegeCommunity.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeCommunity.visibility = View.GONE
                binding.ivApplciationcollegeCommunity.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        binding.rlApplciationcollegeCommunityExplore.setOnClickListener {
            (requireActivity() as MainActivity).setSelecetTab(1)
        }
        var CIPoint =(requireActivity() as MainActivity).scoreAlgorithm.getCIScorePercent()
        var index = 0
        when (CIPoint) {
            in 24..30 ->{
                index = 0
            }
            in 18..23 ->{
                index =1
            }
            in 12..17 ->{
                index = 2
            }
            in 6..7 ->{
                index = 3
            }
            in 0..5 ->{
                index = 4
            }
            else -> {
            }
        }

        var ciTitles = requireContext().resources.getStringArray(R.array.array_application_ci_title)
        var ciContent = requireContext().resources.getStringArray(R.array.array_application_ci_content)
        var ciAdvice = requireContext().resources.getStringArray(R.array.array_application_ci_advice)

        binding.tvApplciationcollegeCommunityTitle.text = ciTitles.get(index)
        binding.tvApplciationcollegeCommunityContent.text = ciContent.get(index)
        binding.tvApplciationcollegeCommunitySubcontent.text = ciAdvice.get(index)
    }
    private fun setHonor(){
        binding.rlApplciationcollegeHonors.setOnClickListener {
            if (binding.llApplciationcollegeHonors.visibility == View.GONE){
                binding.llApplciationcollegeHonors.visibility = View.VISIBLE
                binding.ivApplciationcollegeHonors.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeHonors.visibility = View.GONE
                binding.ivApplciationcollegeHonors.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }

        var honorPoint =(requireActivity() as MainActivity).scoreAlgorithm.getHonroScorePercent()
        var index = 0
        when (honorPoint) {
            in 80..100 ->{
                index = 0
            }
            in 60..79 ->{
                index =1
            }
            in 40..59 ->{
                index = 2
            }
            in 20..39 ->{
                index = 3
            }
            in 0..19 ->{
                index = 4
            }
            else -> {
            }
        }

        var honorTitles = requireContext().resources.getStringArray(R.array.array_application_honor_title)
        var honorContent = requireContext().resources.getStringArray(R.array.array_application_honor_content)
        var honorAdvice = requireContext().resources.getStringArray(R.array.array_application_honor_advice)

        binding.tvApplciationcollegeHonorsTitle.text = honorTitles.get(index)
        binding.tvApplciationcollegeHonorsContent.text = honorContent.get(index)
        binding.tvApplciationcollegeHonorsSubcontent.text = honorAdvice.get(index)
    }

    private fun seteStandardized(){
        binding.rlApplciationcollegeStandardized.setOnClickListener {
            if (binding.llApplciationcollegeStandardized.visibility == View.GONE){
                binding.llApplciationcollegeStandardized.visibility = View.VISIBLE
                binding.ivApplciationcollegeStandardized.setImageResource(R.drawable.vd_ic_arrow_top)
            }else{
                binding.llApplciationcollegeStandardized.visibility = View.GONE
                binding.ivApplciationcollegeStandardized.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }
        }
        var testPoint =(requireActivity() as MainActivity).scoreAlgorithm.getTestsScore()
        var index = 4
        when (testPoint) {
            in 80..100 ->{
                index = 0
            }
            in 60..79 ->{
                index =1
            }
            in 40..59 ->{
                index = 2
            }
            in 20..39 ->{
                index = 3
            }
            in 0..19 ->{
                index = 4
            }
            else -> {
            }
        }

        var testTitles = requireContext().resources.getStringArray(R.array.array_application_test_title)
        var testContent = requireContext().resources.getStringArray(R.array.array_application_test_content)

        binding.tvApplciationcollegeStandardizedTitle.text =testTitles.get(index)
        binding.tvApplciationcollegeStandardizedContent.text =testContent.get(index)
        val tests = (requireActivity() as MainActivity).scoreAlgorithm.tests
        var satHighest: Test? = null
        var actHighest: Test? = null
        var toeflHighest: Test? = null
        var ieltsHighest: Test? = null
        var toeflAltHighest: Test? = null
        var duolingoHighest: Test? = null

        for (test in tests) {
            when (test.genre) {
                Test.GenreInfo.SAT.value() -> {
                    if (satHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (satHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) satHighest = test
                }
                Test.GenreInfo.ACT.value() -> {
                    if (actHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (actHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) actHighest = test
                }
                Test.GenreInfo.TOEFL.value() -> {
                    if (toeflHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (toeflHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) toeflHighest = test
                }
                Test.GenreInfo.IELTS.value() -> {
                    if (ieltsHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (ieltsHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) ieltsHighest = test
                }
                Test.GenreInfo.TOEFL_ALTERNATE.value() -> {
                    if (toeflAltHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (toeflAltHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) toeflAltHighest = test
                }
                Test.GenreInfo.DUOLINGO.value() -> {
                    if (duolingoHighest == null || (test.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY) >
                        (duolingoHighest.score?.toFloatOrNull() ?: Float.NEGATIVE_INFINITY)
                    ) duolingoHighest = test
                }
            }
        }

        satHighest?.let {
            val satContent = requireContext().resources.getStringArray(R.array.array_application_test_sat_content)
            binding.tvApplciationcollegeStandardizedSat.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedSatContent.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedSat.text = ApplicationTestAdviceRules.adviceTitle(it)
            binding.tvApplciationcollegeStandardizedSatContent.text =
                satContent.get(ApplicationTestAdviceRules.adviceIndex(it))
        }

        actHighest?.let {
            val actContent = requireContext().resources.getStringArray(R.array.array_application_test_act_content)
            binding.tvApplciationcollegeStandardizedAct.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedActContent.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedAct.text = ApplicationTestAdviceRules.adviceTitle(it)
            binding.tvApplciationcollegeStandardizedActContent.text =
                actContent.get(ApplicationTestAdviceRules.adviceIndex(it))
        }

        binding.llApplciationcollegeStandardizedAp.removeAllViews()
        val apSelected = Test.selectedApAttempts(tests)
        if (apSelected.isNotEmpty()) {
            val apContent = requireContext().resources.getStringArray(R.array.array_application_test_ap_content)
            for (test in apSelected) {
                addStandardizedAdviceRow(
                    binding.llApplciationcollegeStandardizedAp,
                    ApplicationTestAdviceRules.adviceTitle(test),
                    apContent.get(ApplicationTestAdviceRules.adviceIndex(test).coerceAtMost(apContent.lastIndex))
                )
            }
        }

        val ibSelected = Test.selectedIbAttempts(tests)
        if (ibSelected.isNotEmpty()) {
            val ibContent = requireContext().resources.getStringArray(R.array.array_application_test_ib_content)
            for (test in ibSelected) {
                addStandardizedAdviceRow(
                    binding.llApplciationcollegeStandardizedAp,
                    ApplicationTestAdviceRules.adviceTitle(test),
                    ibContent.get(ApplicationTestAdviceRules.adviceIndex(test).coerceAtMost(ibContent.lastIndex))
                )
            }
        }

        ieltsHighest?.let {
            val ieltsContent = requireContext().resources.getStringArray(R.array.array_application_test_ielts_content)
            binding.tvApplciationcollegeStandardizedIelts.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedIeltsContent.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedIelts.text = ApplicationTestAdviceRules.adviceTitle(it)
            binding.tvApplciationcollegeStandardizedIeltsContent.text =
                ieltsContent.get(ApplicationTestAdviceRules.adviceIndex(it))
        }

        toeflHighest?.let {
            val toeflContent = requireContext().resources.getStringArray(R.array.array_application_test_toefl_content)
            binding.tvApplciationcollegeStandardizedToefl.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedToeflContent.visibility = View.VISIBLE
            binding.tvApplciationcollegeStandardizedToefl.text = ApplicationTestAdviceRules.adviceTitle(it)
            binding.tvApplciationcollegeStandardizedToeflContent.text =
                toeflContent.get(ApplicationTestAdviceRules.adviceIndex(it))
        }

        toeflAltHighest?.let {
            val altContent = requireContext().resources.getStringArray(R.array.array_application_test_toefl_alternate_content)
            addStandardizedAdviceRow(
                binding.llApplciationcollegeStandardizedAp,
                ApplicationTestAdviceRules.adviceTitle(it),
                altContent.get(ApplicationTestAdviceRules.adviceIndex(it).coerceAtMost(altContent.lastIndex))
            )
        }

        duolingoHighest?.let {
            val duolingoContent = requireContext().resources.getStringArray(R.array.array_application_test_duolingo_content)
            addStandardizedAdviceRow(
                binding.llApplciationcollegeStandardizedAp,
                ApplicationTestAdviceRules.adviceTitle(it),
                duolingoContent.get(ApplicationTestAdviceRules.adviceIndex(it).coerceAtMost(duolingoContent.lastIndex))
            )
        }
    }

    private fun addStandardizedAdviceRow(
        container: androidx.appcompat.widget.LinearLayoutCompat,
        title: String,
        content: String
    ) {
        val tvTitle = AppCompatTextView(requireContext())
        val tvContent = AppCompatTextView(requireContext())
        tvTitle.text = title
        tvContent.text = content
        container.visibility = View.VISIBLE
        container.addView(tvTitle)
        container.addView(tvContent)
        tvTitle.setTextSize(13)
        tvTitle.setMarginTopBydp(15)
        tvTitle.setTextAppearance(R.style.textRalewaybold13spC4F5257)
        tvContent.setTextSize(15)
        tvContent.setMarginTopBydp(10)
        tvContent.setTextAppearance(R.style.textRaleway15spC4F5257)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_applciationcollege_back -> {
                findNavController().popBackStack()
            }
        }
    }
}