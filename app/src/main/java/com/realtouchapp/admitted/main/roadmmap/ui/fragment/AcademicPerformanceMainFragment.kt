package com.realtouchapp.admitted.main.roadmmap.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.common.ui.fragment.ImageZoomFragment
import com.realtouchapp.admitted.common.parity.SelectionStateRules
import com.realtouchapp.admitted.databinding.FragmentApmainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.adapter.CoursePagerAdapter
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddCourseBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddScoreYearBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.CourseInfoAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.AcademicYear
import com.realtouchapp.admitted.network.entity.Course
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.stream.Collectors

class AcademicPerformanceMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentApmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    lateinit var couses: ArrayList<Course>
    var academicYears = ArrayList<AcademicYear>()
    lateinit var addScoreYearBSDailogFragment: AddScoreYearBSDailogFragment
    lateinit var addCourseBSDailogFragment: AddCourseBSDailogFragment
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    var addSuccessGpaDailogFragment = AddSuccessDailogFragment()
    private var selectedGradePosition = 0
    private var selectedInterestPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApmainBinding.inflate(inflater, container, false)
        selectedGradePosition = SelectionStateRules.restoredPosition(
            savedInstanceState?.getInt(STATE_GRADE_TAB) ?: 0, 4
        )
        selectedInterestPosition = savedInstanceState?.getInt(STATE_INTEREST_TAB) ?: 0
        val root: View = binding.root
        initialViewSize()
        initialView()
        checkFreeVersion()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAcademicYearCache()
        viewModel.getAcademicYear()
        viewModel.getCourse()
        subscribeUi()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAcademicYearCache()
//        viewModel.getCoursecache()
    }

    fun subscribeUi() {
        //
        viewModel.responseCourse.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_COURSE -> {
                            couses = ArrayList<Course>()
                            result.data?.let { it ->
                                couses!!.addAll(it.data as List<Course>)
                            }
                            if (couses.size > 0) {
                                refreshTabItem(binding.tlApmainfragment.selectedTabPosition)
                                //顯示清單
//                                binding.vpApmainfragment.visibility = View.VISIBLE
                                binding.llPageradapterCourse.visibility = View.VISIBLE
                                binding.ivApmainfragmentEmpty.visibility = View.GONE
                                binding.tvApmainfragmentEmpty.visibility = View.GONE
//                                binding.vpApmainfragment.adapter!!.notifyDataSetChanged()
                            } else {
                                //顯示資料為空
//                                binding.vpApmainfragment.visibility = View.GONE
                                binding.llPageradapterCourse.visibility = View.GONE
                                binding.ivApmainfragmentEmpty.visibility = View.VISIBLE
                                binding.tvApmainfragmentEmpty.visibility = View.VISIBLE

                            }
                        }
                        NetworkResult.Action.GET_COURSE, NetworkResult.Action.DELETE_COURSE -> {
                            viewModel.getCoursecache()
                        }
                        NetworkResult.Action.POST_COURSE,NetworkResult.Action.PUT_COURSE -> {
                            viewModel.getCoursecache()
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }
                                addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                            }

                        }
                        else -> {}

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
        })
        viewModel.academicYears.observe(viewLifecycleOwner, Observer { result ->

            academicYears = result
            testPrint("academicYears", Gson().toJson(result))
            viewModel.updateScoreAlgorithmGPA(academicYears)
            displayTite()
        })
        viewModel.responseAcademicYear.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_ACADEMICYEAR -> {
                            viewModel.getAcademicYearCache()

                        }
                        NetworkResult.Action.DELETE_ACADEMICYEAR -> {
                            viewModel.getAcademicYearCache()
                        }
                        NetworkResult.Action.PUT_ACADEMICYEAR -> {
                            viewModel.getAcademicYearCache()
                            activity?.supportFragmentManager?.let { fragmentManager ->
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessGpaDailogFragment.name = it
                                    }
                                }
                                viewModel.scoreAlgorithm.value?.let {
                                    it.gpas.forEach { gpa ->
                                        addSuccessGpaDailogFragment.academicYear?.let {
                                            if (gpa.academicYearId.equals(it.academicYearId)) {
                                                gpa.grade = it.grade
                                            }
                                        }
                                    }
                                    binding.pbApmainfragmentTest.progress = it.getGpaScorePercent()
                                    binding.tvApmainfragmentTitlePb.text =
                                        it.getGpaScorePercent().toString() + "/100"
                                    addSuccessGpaDailogFragment.score = it.getGpaScorePercent()
                                    addSuccessGpaDailogFragment.scoreMax = 100
                                    val myFragment =
                                        fragmentManager.findFragmentByTag("addSuccessGpaDailogFragment") as AddSuccessDailogFragment?
                                    if (myFragment != null && myFragment.isAdded) {
                                        fragmentManager.beginTransaction().show(myFragment).commit()
                                    } else {
                                        addSuccessGpaDailogFragment.show(
                                            fragmentManager,
                                            "addSuccessGpaDailogFragment"
                                        )
                                    }
                                }
                            }
                        }

                        else -> {

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
        })
        viewModel.scoreAlgorithm.observe(viewLifecycleOwner) {
            binding.pbApmainfragmentTest.progress = it.getGpaScorePercent()
            binding.tvApmainfragmentTitlePb.text = it.getGpaScorePercent().toString() + "/ 100"

        }
    }

    fun initialViewSize() {
        //Back
        _binding?.ivApmainfragmentBack?.setViewSizeOrignalWH(24, 24)
        _binding?.tvApmainfragmentBack?.setTextSize(13)

        //Card
        _binding?.ivApmainfragmentBack?.setViewSizeOrignalWH(24, 24)
        binding.rlApmainfragmentCard.setMarginTopOrignal(12)
        binding.rlApmainfragmentCard.setViewSizeOrignalHeight(120)
        binding.ivApmainfragmentIcon.setViewSizeOrignalWH(65, 65)
        binding.rlApmainfragmentTitle.setMarginLeftOrignal(16)
        binding.tvApmainfragmentTitle.setTextSize(17)
        binding.tvApmainfragmentTitleDesp.setTextSize(13)
        binding.pbApmainfragmentTest.setViewSizeOrignalWH(155, 8)
        binding.pbApmainfragmentTest.setMarginTopOrignal(16)
        binding.tvApmainfragmentTitlePb.setTextSize(13)
        binding.tvApmainfragmentTitlePb.setMarginLeftOrignal(8)
        binding.tvApmainfragmentTitlePb.setMarginTopOrignal(-10)

        //Courses
        binding.tvApmainfragmentCourses.setTextSize(17)
        binding.tvApmainfragmentCourses.setMarginTopOrignal(24)
        binding.ivApmainfragmentEmpty.setViewSizeOrignalWH(160, 176)
        binding.tvApmainfragmentEmpty.setTextSize(13)
        binding.tvApmainfragmentEmpty.setMarginTopOrignal(24)

        binding.llPageradapterCourse.setViewSizeWidthByRatio(.9f)
        binding.tvPageradapterCourseGpa.setTextSize(15)
        binding.tvPageradapterCoursePercent.setTextSize(10)
        binding.tvPageradapterCourseTitle.setTextSize(15)

        binding.tvApmainfragmentAdd.setTextSize(13)
        binding.tvApmainfragmentAdd.setMarginTopOrignal(24)

        binding.tvApmainfragmentYearAdd.setTextSize(13)
        binding.tvApmainfragmentYearAdd.setMarginTopOrignal(12)

        //Resommendation
        binding.tvApmainfragmentRecommendTitle.setTextSize(17)
        binding.tvApmainfragmentRecommendTitle.setMarginTopOrignal(24)
        binding.tvApmainfragmentRecommendContent.setTextSize(13)
        binding.tvApmainfragmentRecommendContent.setMarginTopOrignal(8)

    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlApmainfragmentAdd.gone()
                        binding.rlApmainfragmentYearAdd.gone()

                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlApmainfragmentAdd.visible()
                        binding.rlApmainfragmentYearAdd.visible()

                    }
                    else -> { }
                }
            }
        }

        //Back
        _binding?.llApmainfragmentBack?.setOnClickListener(this)

        _binding?.rlApmainfragmentAdd?.setOnClickListener(this)
        _binding?.rlApmainfragmentYearAdd?.setOnClickListener(this)
        couses = ArrayList<Course>()
        var pageGrade = resources.getStringArray(R.array.array_grade_title)
        binding.tlApmainfragment.removeAllTabs()
        pageGrade.forEach {
            var tab = binding.tlApmainfragment.newTab()
            tab.text = it
            binding.tlApmainfragment.addTab(tab)
        }

        binding.tlApmainfragment.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                selectedGradePosition = tab.position
                refreshTabItem(tab.position)
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabReselected(tab: Tab?) {

            }
        })


        addCourseBSDailogFragment = AddCourseBSDailogFragment()
        addCourseBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_ACADEMIC
            addSuccessDailogFragment.course = it
            viewModel.postCourse(it)
        }
        addCourseBSDailogFragment.editMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_ACADEMIC
            addSuccessDailogFragment.course = it
            viewModel.putCourse(it)
        }

        addScoreYearBSDailogFragment = AddScoreYearBSDailogFragment()
        addScoreYearBSDailogFragment.doneMeathod = {
            addSuccessGpaDailogFragment.type = AddSuccessDailogFragment.TYPE_ACADEMICYEAR
            addSuccessGpaDailogFragment.academicYear = it.copy()
            viewModel.putAcademicYear(it)
        }
        addScoreYearBSDailogFragment.editMeathod = {
            addSuccessGpaDailogFragment.type = AddSuccessDailogFragment.TYPE_ACADEMICYEAR
            addSuccessGpaDailogFragment.academicYear = it.copy()
            viewModel.putAcademicYear(it)
        }

        var coursePagerAdapter = CoursePagerAdapter(requireContext())
        coursePagerAdapter.onItemClick = { imageSourceID ->
            var bundle = Bundle()
            bundle.putSerializable(ImageZoomFragment.BUNDLE_IMAGESOURCEID, imageSourceID)
            findNavController().navigate(
                R.id.action_navigation_apmain_to_navigation_courseimage,
                bundle
            )
//            ImageZoomFragment.showCourseImage(requireParentFragment().childFragmentManager,imageSourceID)

        }

        var list = ArrayList<Int>()
        var pageTItle = resources.getStringArray(R.array.array_interest)
        selectedInterestPosition = SelectionStateRules.restoredPosition(selectedInterestPosition, pageTItle.size)
        pageTItle.forEach {
            list.add(0)
        }

        coursePagerAdapter.setData(list)
        binding.vpApmainfragmentTrack.adapter = coursePagerAdapter
        TabLayoutMediator(
            binding.tlApmainfragmentTrack,
            binding.vpApmainfragmentTrack
        ) { tab, position ->
            tab.text = pageTItle.get(position)
        }.attach()

        binding.tlApmainfragment.getTabAt(selectedGradePosition)?.select()
        binding.vpApmainfragmentTrack.setCurrentItem(selectedInterestPosition, false)
        binding.vpApmainfragmentTrack.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedInterestPosition = position
            }
        })
        val showGpaHelp = View.OnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.gpa_conversion_title)
                .setMessage(R.string.gpa_conversion_help)
                .setPositiveButton(android.R.string.ok, null)
                .show()
        }
        binding.tvPageradapterCoursePercent.setOnClickListener(showGpaHelp)
        binding.tvApmainfragmentGpaHelp.setOnClickListener(showGpaHelp)
        refreshTabItem(selectedGradePosition)

    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.pbApmainfragmentTest.visibility = View.GONE
                    binding.tvApmainfragmentTitlePb.visibility = View.GONE
                }
            }
        }
    }
    private fun refreshTabItem(position: Int) {
        val userType = viewModel.userType.value

        var gradeCourse = ArrayList<Course>()
        var academicYear = AcademicYear("", "0.0", "2")
        if (!academicYears.isNullOrEmpty()) {
            if (position > -1 && position < academicYears.size) {
                academicYear = academicYears.get(position)
                binding.rlPageradapterCourseTitle.setOnClickListener {
                    val innerUserType = viewModel.userType.value
                    if (innerUserType == UserType.STUDENT) {
                        activity?.supportFragmentManager?.let {
                            if (!addScoreYearBSDailogFragment.isVisible) {
                                addScoreYearBSDailogFragment.academicYear = academicYear.copy()
                                addScoreYearBSDailogFragment.show(it, "AddScoreYearBSDailogFragment")
                            }
                        }
                    }
                }
            }

        }


        gradeCourse = when (position) {
            0 -> couses.stream().filter { it.gradeLevel == 9 }
                .collect(Collectors.toList()) as ArrayList<Course>
            1 -> couses.stream().filter { it.gradeLevel == 10 }
                .collect(Collectors.toList()) as ArrayList<Course>
            2 -> couses.stream().filter { it.gradeLevel == 11 }
                .collect(Collectors.toList()) as ArrayList<Course>
            3 -> couses.stream().filter { it.gradeLevel == 12 }
                .collect(Collectors.toList()) as ArrayList<Course>
            else -> ArrayList<Course>()
        }
        if (gradeCourse.size > 0 || academicYear.grade != "0.0") {
            binding.tvPageradapterCourseGpa.text = String.format("%.1f", academicYear.grade.toFloat())
            var courseInfoAdapter = CourseInfoAdapter(requireContext())
            courseInfoAdapter.setData(gradeCourse)
            courseInfoAdapter.onItemClick = { course ->
                val innerUserType = viewModel.userType.value
                if (innerUserType == UserType.STUDENT) {
                    activity?.supportFragmentManager?.let {
                        if (!addCourseBSDailogFragment.isVisible) {
                            addCourseBSDailogFragment.course = course
                            addCourseBSDailogFragment.show(it, "AddCourseBSDailogFragment")
                        }
                    }
                }
            }
            var linearLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.rvPageradapterCourse.adapter = courseInfoAdapter
            binding.rvPageradapterCourse.layoutManager = linearLayoutManager

            if (userType == UserType.STUDENT) {
                // FIXME:
                binding.rvPageradapterCourse.addSwipeLeftDelete(gradeCourse) {

                    viewModel.deleteCourse(
                        (binding.rvPageradapterCourse.adapter as CourseInfoAdapter).dataList.get(
                            it
                        )
                    )
                }
            }

            binding.llPageradapterCourse.visibility = View.VISIBLE
            binding.ivApmainfragmentEmpty.visibility = View.GONE
            binding.tvApmainfragmentEmpty.visibility = View.GONE

//                                binding.vpApmainfragment.adapter!!.notifyDataSetChanged()
        } else {
            //顯示資料為空
//                                binding.vpApmainfragment.visibility = View.GONE
            binding.llPageradapterCourse.visibility = View.GONE
            binding.ivApmainfragmentEmpty.visibility = View.VISIBLE
            binding.tvApmainfragmentEmpty.visibility = View.VISIBLE

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayTite() {
        var text = ""

        if (couses.size > 0) {
            var size = couses.size.toString()
            if (size.equals("1")) {
                text = getString(R.string.tv_coursemainfragment_title_desp_singular, size)
            } else {
                text = getString(R.string.tv_coursemainfragment_title_desp_, size)
            }
        }
        var totalGrade = 0f
        academicYears.forEach {
            totalGrade = totalGrade + it.grade.toFloat()
        }
        if (text.isEmpty()) {
//            text = " " + (totalGrade / 4f).toString().substring(0, 2) + " GPA"
            text = " " + BigDecimal((totalGrade / 4f).toDouble()).setScale(
                1,
                RoundingMode.HALF_EVEN
            ) + " GPA"
        } else {
//            text = text + " ‧ " + (totalGrade / 4f).toString().substring(0, 2) + " GPA"
            text = text + " ‧ " + BigDecimal((totalGrade / 4f).toDouble()).setScale(
                1,
                RoundingMode.HALF_EVEN
            ) + " GPA"
        }
        binding.tvApmainfragmentTitleDesp.text = text

        if (academicYears.size > binding.tlApmainfragment.selectedTabPosition) {
            binding.tvPageradapterCourseGpa.text =
                academicYears.get(binding.tlApmainfragment.selectedTabPosition).grade
        }


//        binding.vpApmainfragment.adapter!!.notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_apmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_apmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addCourseBSDailogFragment.isVisible) {
                        addCourseBSDailogFragment.course = Course().apply {
                            gradeLevel = selectedGradePosition + 9
                        }
                        addCourseBSDailogFragment.show(it, "AddCourseBSDailogFragment")
                    }
                }
            }
            R.id.rl_apmainfragment_year_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addScoreYearBSDailogFragment.isVisible) {
                        addScoreYearBSDailogFragment.academicYear =
                            academicYears.getOrNull(selectedGradePosition)?.copy()
                                ?: AcademicYear((selectedGradePosition + 9).toString(), "0.0", "2")
                        addScoreYearBSDailogFragment.show(it, "AddScoreYearBSDailogFragment")
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_GRADE_TAB, selectedGradePosition)
        outState.putInt(STATE_INTEREST_TAB, selectedInterestPosition)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val STATE_GRADE_TAB = "academic_grade_tab"
        private const val STATE_INTEREST_TAB = "academic_interest_tab"
    }
}
