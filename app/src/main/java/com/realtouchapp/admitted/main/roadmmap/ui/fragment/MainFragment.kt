package com.realtouchapp.admitted.main.roadmmap.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.databinding.BsdialogfragmentWhatBinding
import com.realtouchapp.admitted.databinding.FragmentMainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.explore.ui.fragment.CollegeDetailFragment
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.*
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.InterestContentRecyleViewAdapter
import com.realtouchapp.admitted.member.view.adapter.SchoolEditRecyleViewAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.stream.Collectors
import com.realtouchapp.admitted.common.parity.SingleActionGate


class MainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    var targetSchools = ArrayList<TargetSchool>()
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    var addCourseBSDailogFragment = AddCourseBSDailogFragment()
    var addICActivityBSDailogFragment = AddICActivityBSDailogFragment()
    var addCIActivityBSDailogFragment = AddCIActivityBSDailogFragment()
    var addHonorsBSDailogFragment = AddHonorsBSDailogFragment()
    var addTestFinalBSDailogFragment = AddTestFinalBSDailogFragment()
    var whatBSDialogFragment = WhatBSDialogFragment()
    private var readinessInfoOverlay: View? = null
    private val schoolNavigationGate = SingleActionGate()
    private lateinit var schoolSelectBSDailogFragment: SchoolSelectBSDailogFragment
    var selectSchools = HashMap<String, String>()

    private lateinit var userInfo: UserInfo
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewsSize()
        initalView()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        checkFreeVersion()
        viewModel.getUserInfoCache()
        viewModel.cacheTargeSchools()
    }


    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.rlMainacivityTotal.visibility = View.GONE
                    binding.clMainacivityFreeExampleRoadMap1.visibility =View.VISIBLE
                    binding.ivMainacivityFreeExampleWhat.setOnClickListener {
                        activity?.supportFragmentManager?.let {
                            whatBSDialogFragment.show(it,whatBSDialogFragment.tag)
                        }

                    }
                    binding.clRoadMapFreeUpgrade.visibility =View.VISIBLE
                    binding.clRoadMapFreeUpgrade.setOnClickListener {
                        PremiumActivity.Intent(requireActivity() as MainActivity)
                    }

                    binding.llMainacivitySchool.visibility = View.VISIBLE
                    binding.btnMainacivitySchoolAdd.visibility = View.VISIBLE
                    binding.pbMainacivityAp.visibility = View.INVISIBLE
                    binding.pbMainacivityCi.visibility = View.INVISIBLE
                    binding.pbMainacivityHonors.visibility = View.INVISIBLE
                    binding.pbMainacivityIc.visibility = View.INVISIBLE
                    binding.pbMainacivityTest.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        schoolNavigationGate.reset()
        viewModel.getUserBasic()
        viewModel.getUserTargetSchools()
        viewModel.initialScoreAlgorithn()
        viewModel.cacheTargeSchools()
    }

    private fun initialViewsSize() {
        binding.rlMainacivityTotal.setViewSizeOrignalWW(175, 175)
        binding.tvMainacivityTotal.setTextSize(34)
        binding.tvMainacivityTotalDesc.setTextSize(10)

        binding.rlMainacivityGpa.setViewSizeByRatioWH(.352f, .242f)
        binding.ivMainacivityGpa.setViewSizeByRatioHH(.073f, .073f)
        binding.llMainacivityGpa.setViewSizeByRatioWH(.352f, .205f)
        binding.tvMainacivityGpaTitle.setMarginBottomRatioH(.016f)
        binding.tvMainacivityGpaTitle.setTextSize(13)
        binding.tvMainacivityGpaAdd.setMarginBottomRatioH(.021f)
        binding.tvMainacivityGpaAdd.setTextSize(13)
        binding.ivMainacivityGpaMore.setMarginBottomRatioH(.021f)
        binding.tvMainacivityGpaMore.setTextSize(13)

        binding.rlMainacivityAp.setViewSizeByRatioWH(.352f, .242f)
        binding.ivMainacivityAp.setViewSizeByRatioHH(.073f, .073f)
//        binding.ivMainacivityAp.setViewSizeByRatioWH(.149f, .073f)
        binding.llMainacivityAp.setViewSizeByRatioWH(.352f, .205f)

        binding.tvMainacivityApTitle.setMarginBottomRatioH(.016f)
        binding.tvMainacivityApTitle.setTextSize(13)
        binding.pbMainacivityAp.setViewSizeByRatioWH(.235f, .01f)
        binding.tvMainacivityApAdd.setMarginBottomRatioH(.021f)
        binding.tvMainacivityApAdd.setTextSize(13)
        binding.ivMainacivityApMore.setMarginBottomRatioH(.021f)
        binding.tvMainacivityApMore.setTextSize(13)

        binding.rlMainacivityIc.setViewSizeByRatioWH(.352f, .242f)
        binding.ivMainacivityIc.setViewSizeByRatioHH(.073f, .073f)
        binding.llMainacivityIc.setViewSizeByRatioWH(.352f, .205f)
        binding.tvMainacivityIcTitle.setMarginBottomRatioH(.016f)
        binding.tvMainacivityIcTitle.setTextSize(13)
        binding.pbMainacivityIc.setViewSizeByRatioWH(.235f, .01f)
        binding.tvMainacivityIcAdd.setMarginBottomRatioH(.021f)
        binding.tvMainacivityIcAdd.setTextSize(13)
        binding.ivMainacivityIcMore.setMarginBottomRatioH(.021f)
        binding.tvMainacivityIcMore.setTextSize(13)

        binding.rlMainacivityCi.setViewSizeByRatioWH(.352f, .242f)
        binding.ivMainacivityCi.setViewSizeByRatioHH(.073f, .073f)
        binding.llMainacivityCi.setViewSizeByRatioWH(.352f, .205f)
        binding.tvMainacivityCiTitle.setMarginBottomRatioH(.016f)
        binding.tvMainacivityCiTitle.setTextSize(13)
        binding.pbMainacivityCi.setViewSizeByRatioWH(.235f, .01f)
        binding.tvMainacivityCiAdd.setMarginBottomRatioH(.021f)
        binding.tvMainacivityCiAdd.setTextSize(13)
        binding.ivMainacivityCiMore.setMarginBottomRatioH(.021f)
        binding.tvMainacivityCiMore.setTextSize(13)

        binding.rlMainacivityHonors.setViewSizeByRatioWH(.352f, .242f)
        binding.ivMainacivityHonors.setViewSizeByRatioHH(.073f, .073f)
        binding.llMainacivityHonors.setViewSizeByRatioWH(.352f, .205f)
        binding.tvMainacivityHonorsTitle.setMarginBottomRatioH(.016f)
        binding.tvMainacivityHonorsTitle.setTextSize(13)
        binding.pbMainacivityHonors.setViewSizeByRatioWH(.235f, .01f)
        binding.tvMainacivityHonorsAdd.setMarginBottomRatioH(.021f)
        binding.tvMainacivityHonorsAdd.setTextSize(13)
        binding.ivMainacivityHonorsMore.setMarginBottomRatioH(.021f)
        binding.tvMainacivityHonorsMore.setTextSize(13)

        binding.rlMainacivityTest.setMarginTopRatioHWithoutBar(.031f)
        binding.ivMainacivityTest.setViewSizeByRatioWH(0.16f, 0.078f)
        binding.ivMainacivityTest.setMarginLeftRatioW(.040f)
        binding.tvMainacivityTest.setMarginBottomRatioHWithoutBar(.008f)
        binding.tvMainacivityTest.setTextSize(17)
        binding.tvMainacivityTestDesp.setMarginBottomRatioHWithoutBar(.016f)
        binding.tvMainacivityTestDesp.setTextSize(13)
        binding.pbMainacivityTest.setViewSizeByRatioWH(0.413f, 0.01f)
        binding.ivMainacivityTestAdd.setViewSizeByRatioWH(0.085f, 0.042f)
//        binding.ivMainacivityTest.setViewSizeByRatioWH(0.16f, 0.078f)
        binding.ivMainacivityTest.setViewSizeByRatioHH(0.078f, 0.078f)
        binding.ivMainacivityTest.setMarginLeftRatioW(.040f)
        binding.tvMainacivityTest.setMarginBottomRatioHWithoutBar(.008f)
        binding.tvMainacivityTest.setTextSize(17)
        binding.tvMainacivityTestDesp.setMarginBottomRatioHWithoutBar(.016f)
        binding.tvMainacivityTestDesp.setTextSize(13)
        binding.pbMainacivityTest.setViewSizeByRatioWH(0.413f, 0.01f)
        binding.ivMainacivityTestAdd.setViewSizeByRatioWH(0.085f, 0.042f)

        binding.llMainacivityInterest.setMarginTopRatioHWithoutBar(.031f)
        binding.ivMainacivityInterest.setViewSizeByRatioHH(0.078f, 0.078f)
//        binding.ivMainacivityInterest.setViewSizeByRatioWH(0.16f, 0.078f)
        binding.ivMainacivityInterest.setMarginLeftRatioW(.040f)
        binding.tvMainacivityInterest.setMarginBottomRatioHWithoutBar(.008f)
        binding.tvMainacivityInterest.setTextSize(17)

        binding.tvMainacivityInterestDesp.setMarginBottomRatioHWithoutBar(.016f)
        binding.tvMainacivityInterestDesp.setTextSize(13)
        binding.ivMainacivityInterestExtend.setViewSizeByRatioWH(0.085f, 0.042f)

        binding.llMainacivitySchool.setMarginTopRatioHWithoutBar(.031f)
        binding.tvMainacivitySchool.setTextSize(17)

        binding.tvMainacivitySchool.setMarginTopRatioHWithoutBar(.032f)
        binding.tvMainacivitySchool.setMarginLeftRatioW(.043f)
        binding.ivMainacivitySchoolEdit.setViewSizeByRatioWH(.064f, .031f)
        binding.ivMainacivitySchoolEdit.setMarginTopRatioHWithoutBar(.032f)
        binding.ivMainacivitySchoolEdit.setMarginRightRatioW(.043f)
        binding.btnMainacivitySchoolCancel.setViewSizeByRatioWH(.181f, .044f)
        binding.btnMainacivitySchoolCancel.setMarginTopRatioHWithoutBar(.032f)
        binding.btnMainacivitySchoolCancel.setMarginRightRatioW(.043f)

        binding.btnMainacivitySchoolAdd.setMarginBottomRatioHWithoutBar(.226f)
    }

    private fun initalView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlMainacivityApAdd.gone()
                        binding.rlMainacivityCiAdd.gone()
                        binding.rlMainacivityIcAdd.gone()
                        binding.rlMainacivityHonorsAdd.gone()
                        binding.clMainacivityApMore.visible()
                        binding.clMainacivityCiMore.visible()
                        binding.clMainacivityIcMore.visible()
                        binding.clMainacivityHonorsMore.visible()

                        binding.ivMainacivityTestDivider.gone()
                        binding.ivMainacivityTestAdd.gone()
                        binding.ivMainacivityTestMore.visible()

                        binding.rlMainacivityInterestEdit.gone()
                        binding.ivMainacivitySchoolEdit.gone()
                        binding.btnMainacivitySchoolAdd.invisible()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlMainacivityApAdd.visible()
                        binding.rlMainacivityCiAdd.visible()
                        binding.rlMainacivityIcAdd.visible()
                        binding.rlMainacivityHonorsAdd.visible()
                        binding.clMainacivityApMore.gone()
                        binding.clMainacivityCiMore.gone()
                        binding.clMainacivityIcMore.gone()
                        binding.clMainacivityHonorsMore.gone()

                        binding.ivMainacivityTestDivider.visible()
                        binding.ivMainacivityTestAdd.visible()
                        binding.ivMainacivityTestMore.gone()

                        binding.rlMainacivityInterestEdit.visible()
                        binding.ivMainacivitySchoolEdit.visible()
                        binding.btnMainacivitySchoolAdd.visible()
                    }
                    else -> { }
                }
            }
        }

        binding.rlMainacivityAp.setOnClickListener(this)
        binding.rlMainacivityApAdd.setOnClickListener(this)
        addCourseBSDailogFragment = AddCourseBSDailogFragment()
        addCourseBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_ACADEMIC
            addSuccessDailogFragment.course = it.copy()
            addSuccessDailogFragment.isEnableShow = true
            viewModel.postCourse(it)
        }
        addCourseBSDailogFragment.editMeathod = {
        }

        binding.rlMainacivityIc.setOnClickListener(this)
        binding.rlMainacivityIcAdd.setOnClickListener(this)
        addICActivityBSDailogFragment = AddICActivityBSDailogFragment()
        addICActivityBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_IC
            addSuccessDailogFragment.intellectualCuriosity = it.copy()
            addSuccessDailogFragment.isEnableShow = true
            viewModel.postIntellectualCuriosity(it)
        }
        addICActivityBSDailogFragment.editMeathod = {
        }

        binding.rlMainacivityCi.setOnClickListener(this)
        binding.rlMainacivityCiAdd.setOnClickListener(this)
        addCIActivityBSDailogFragment = AddCIActivityBSDailogFragment()
        addCIActivityBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_CI
            addSuccessDailogFragment.communityImpact = it.copy()
            addSuccessDailogFragment.isEnableShow = true
            viewModel.postCommunityImpact(it)
        }
        addCIActivityBSDailogFragment.editMeathod = {
        }

        binding.rlMainacivityHonors.setOnClickListener(this)
        binding.rlMainacivityHonorsAdd.setOnClickListener(this)
        addHonorsBSDailogFragment = AddHonorsBSDailogFragment()
        addHonorsBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_HONORS
            addSuccessDailogFragment.honors = it.copy()
            addSuccessDailogFragment.isEnableShow = true
            viewModel.postHonors(it)
        }
        addHonorsBSDailogFragment.editMeathod = {
        }

        binding.rlMainacivityTest.setOnClickListener(this)
        binding.ivMainacivityTestAdd.setOnClickListener(this)
        binding.clvMainScore.isClickable = false
        binding.clvMainScore.isFocusable = false
        binding.tvMainacivityTotalWhat.bringToFront()
        binding.tvMainacivityTotalWhat.translationZ = 8f
        binding.tvMainacivityTotalWhat.isClickable = true
        binding.tvMainacivityTotalWhat.isFocusable = true
        Log.d(TAG, "tvMainacivityTotalWhat listener attached")
        binding.tvMainacivityTotalWhat.setOnClickListener {
            Log.d(TAG, "tvMainacivityTotalWhat clicked")
            showReadinessInfoDialog()
        }
        addTestFinalBSDailogFragment = AddTestFinalBSDailogFragment()
        addTestFinalBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_TEST
            addSuccessDailogFragment.test = it.copy()
            addSuccessDailogFragment.isEnableShow = true
            viewModel.postTest(it)
        }
        initialViewInteret()
        initialViewISchool()

        whatBSDialogFragment = WhatBSDialogFragment()


        schoolSelectBSDailogFragment = SchoolSelectBSDailogFragment()
        schoolSelectBSDailogFragment.unitedStatesOnly = true
        schoolSelectBSDailogFragment.doneMeathod = doneMethod@{
            if (viewModel.isFreeVersion.value && schoolSelectBSDailogFragment.selectetMap.size > 5) {
                //免費版選擇超過五個直接返回
                //彈出一個對話框 左邊按鈕是取消 右邊按鈕是升級
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Upgrade now to access a personalized list \n" +
                        "of college application opportunities!")
                    .setCancelable(false)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Upgrade Now") { dialog, _ ->
                        PremiumActivity.Intent(requireActivity() as MainActivity)
                        dialog.dismiss()
                    }

                val alert = builder.create()
                alert.show()

                return@doneMethod Unit
            }

            selectSchools = HashMap(schoolSelectBSDailogFragment.selectetMap)
            val list = ArrayList<String>()
            selectSchools.values.forEach {
                list.add(it)
            }
            val userTargetSchools = ArrayList<UserTargetSchool>()
            val selectTargetSchools = ArrayList<TargetSchool>()
            targetSchools.forEach {
                it.isSelected = false
                if (selectSchools.keys.contains(it.schoolId.toString())) {
                    it.isSelected = true
                    userTargetSchools.add(UserTargetSchool(it.schoolId))
                    selectTargetSchools.add(it)
                }
            }

            binding.rvMainacivitySchool.adapter?.let {
                (it as SchoolEditRecyleViewAdapter).setData(selectTargetSchools)
            }
            viewModel.putUserTargetSchool(userTargetSchools)
            runBlocking {

                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_TARGET_SCHOOL, selectSchools)
            }
        }

    }

    private fun showReadinessInfoDialog() {
        Log.d(TAG, "showReadinessInfoDialog called")
        dismissReadinessInfoDialog()

        val rootView = binding.root as ViewGroup
        val overlay = android.widget.FrameLayout(requireContext()).apply {
            setBackgroundColor(0x73000000)
            isClickable = true
            isFocusable = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setOnClickListener {
                Log.d(TAG, "readinessInfoOverlay outside clicked")
                dismissReadinessInfoDialog()
            }
        }

        val dialogBinding = BsdialogfragmentWhatBinding.inflate(layoutInflater, overlay, false)
        dialogBinding.root.setBackgroundResource(R.drawable.bg_bsdf_)
        dialogBinding.root.layoutParams = android.widget.FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = android.view.Gravity.BOTTOM
        }
        dialogBinding.llBsdf.setBackgroundResource(R.drawable.bg_bsdf_)
        dialogBinding.root.isClickable = true
        dialogBinding.root.setOnClickListener {
            // Keep taps inside the panel from closing the overlay.
        }
        dialogBinding.tvBsdfAttentionTitle.text = "Level of Readiness"
        dialogBinding.tvBsdfAttentionName.text =
            "We calculate your Level of Readiness based on your academic and extracurricular performances that you have recorded in the app. The score indicates the overall likelihood of admission to an undergraduate program at a university."
        dialogBinding.btnBsdfAttentionUpgrade.visibility = View.GONE

        dialogBinding.ivBsdfAttentionDivider.setViewSizeOrignalWH(54, 2)
        dialogBinding.ivBsdfAttentionDivider.setMarginTopOrignal(8)
        dialogBinding.tvBsdfAttentionTitle.setTextSize(17)
        dialogBinding.tvBsdfAttentionTitle.setMarginTopOrignal(14)
        dialogBinding.tvBsdfAttentionName.setTextSize(11)
        dialogBinding.tvBsdfAttentionName.setMarginTopOrignal(14)
        dialogBinding.llBsdf.setPadding(
            dialogBinding.llBsdf.paddingLeft,
            dialogBinding.llBsdf.paddingTop,
            dialogBinding.llBsdf.paddingRight,
            96
        )

        overlay.addView(dialogBinding.root)
        rootView.addView(overlay)
        readinessInfoOverlay = overlay
        Log.d(TAG, "readinessInfoOverlay added")
        dialogBinding.root.post {
            Log.d(
                TAG,
                "readinessInfoOverlay content size overlay=${overlay.width}x${overlay.height}, panel=${dialogBinding.root.width}x${dialogBinding.root.height}, ll=${dialogBinding.llBsdf.width}x${dialogBinding.llBsdf.height}"
            )
        }
    }


    private fun initialViewInteret() {
        binding.llMainacivityInterest.setOnClickListener(this)
        binding.rlMainacivityInterestEdit.setOnClickListener(this)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvMainacivityInterest.layoutManager = linearLayoutManager
        val interestContentRecyleViewAdapter = InterestContentRecyleViewAdapter(requireContext())
        interestContentRecyleViewAdapter.onItemClick = {
            val bundle = bundleOf(CourseTrackChartFragment.BUNLDE_TITLE to it)
            findNavController().navigate(
                R.id.action_navigation_main_to_navigation_coursetrackchart,
                bundle
            )

        }
//        var arrlist = ArrayList(resources.getStringArray(R.array.array_interest).toList())
//        interestContentRecyleViewAdapter.setData(arrlist)
        binding.rvMainacivityInterest.adapter = interestContentRecyleViewAdapter
//        binding.rvMainacivityInterest.setOnClickListener(this)
    }


    private fun initialViewISchool() {

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvMainacivitySchool.layoutManager = linearLayoutManager
        val schoolEditRecyleViewAdapter = SchoolEditRecyleViewAdapter(requireContext())
//        schoolEditRecyleViewAdapter.onItemClick = {
//
//            var bundle = Bundle()
//            bundle.putSerializable(
//                CollegeDetailFragment.BUNDLE_TARGETSCHOOL,
//                targetSchools.get((it))
//            )
//            findNavController().navigate(
//                R.id.action_navigation_main_to_navigation_collegedetail,
//                bundle
//            )
//
//        }

//        var arrlist = ArrayList(resources.getStringArray(R.array.array_interest).toList())
//        schoolEditRecyleViewAdapter.setData(arrlist)
//        itemTouchHelper = binding.rvMainacivitySchool.addSwipeLeftDelete(targetSchools) {}
        binding.rvMainacivitySchool.adapter = schoolEditRecyleViewAdapter
        binding.rvMainacivitySchool.setOnClickListener(this)

        binding.ivMainacivitySchoolEdit.setOnClickListener(this)
        binding.btnMainacivitySchoolCancel.setOnClickListener(this)
        binding.tvMainacivitySchoolRemove.setOnClickListener(this)
        binding.rlMainacivitySchoolAdd.setOnClickListener(this)
        binding.btnMainacivitySchoolAdd.setOnClickListener(this)


    }

    private fun subscribeUi() {

        viewModel.userInfo.observe(viewLifecycleOwner) {
            it?.let {
                userInfo = it.copy()
                userInfo.areaOfInterest?.let { areaOfInterest ->
                    var list = areaOfInterest.getCheckedList(requireContext())
                    var desp = ""
                    list.forEach {
                        if (list.last().equals(it)) {
                            desp = desp + it
                        } else {
                            desp = desp + it + ","
                        }
                    }
                    binding.tvMainacivityInterestDesp.text = desp
                }

            }
            it.areaOfInterest?.let {
                var arrlist = it.getCheckedList(requireContext())

                val interestContentRecyleViewAdapter =
                    InterestContentRecyleViewAdapter(requireContext())
                interestContentRecyleViewAdapter.onItemClick = {
                    val bundle = bundleOf(CourseTrackChartFragment.BUNLDE_TITLE to it)
                    findNavController().navigate(
                        R.id.action_navigation_main_to_navigation_coursetrackchart,
                        bundle
                    )
                }
                interestContentRecyleViewAdapter.setData(arrlist)
                binding.rvMainacivityInterest.adapter = interestContentRecyleViewAdapter

                if (arrlist.size > 0) {

                    binding.ivMainacivityInterestDividerTop.visibility = View.VISIBLE
                    binding.rvMainacivityInterest.visibility = View.VISIBLE
                    binding.ivMainacivityInterestDividerBottom.visibility = View.VISIBLE
                } else {
                    binding.ivMainacivityInterestDividerTop.visibility = View.GONE
                    binding.rvMainacivityInterest.visibility = View.GONE
                    binding.ivMainacivityInterestDividerBottom.visibility = View.GONE
                }
            }
        }

        viewModel.scoreAlgorithm.observe(viewLifecycleOwner) {
            binding.clvMainScore.progress = it.getTotalPercent()
            binding.tvMainacivityTotal.text = it.getTotalPercent().toString()

            binding.pbMainacivityAp.progress = it.getGpaScorePercent()
            binding.pbMainacivityIc.progress = it.getICScorePercent()
            binding.pbMainacivityCi.progress = it.getCIScorePercent()
            binding.pbMainacivityHonors.progress = it.getHonroScorePercent()
            binding.pbMainacivityTest.progress = it.getTestsScore()
            setTestDesp(it.tests as ArrayList<Test>)

        }

        viewModel.targetSchools.observe(viewLifecycleOwner) {
            targetSchools = it.clone() as ArrayList<TargetSchool>

            viewModel.cacheUserTargeSchools()
        }
        viewModel.userTargetSchools.observe(viewLifecycleOwner) {
            var schools = it.clone() as ArrayList<UserTargetSchool>
            var text = ","
            for (school in schools) {
                text = text + school.schoolId + ","
            }
            targetSchools.forEach {
                if (text.contains(",${it.schoolId},")) {
                    it.isSelected = true
                } else {
                    it.isSelected = false
                }
            }
            selectSchools =  HashMap<String, String>()
            val datas = targetSchools.stream().filter { data -> data.isSelected }.collect(Collectors.toList())
            datas.forEach {
                selectSchools[it.schoolId.toString()] = it.schoolName ?: ""
            }
            schoolSelectBSDailogFragment.targetSchools = targetSchools
            (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).setData(datas as ArrayList<TargetSchool>)
            (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).onItemClick = schoolClick@{
                if (!schoolNavigationGate.tryBegin()) return@schoolClick
                var bundle = Bundle()
                bundle.putSerializable(
                    CollegeDetailFragment.BUNDLE_TARGETSCHOOL,
                    datas.get((it))
                )
                findNavController().navigate(
                    R.id.action_navigation_main_to_navigation_collegedetail,
                    bundle
                )

            }
            if (datas.size == 0) {
                binding.llMainacivitySchool.visibility = View.GONE
            } else {
                if (!viewModel.isFreeVersion.value){
                    binding.llMainacivitySchool.visibility = View.VISIBLE
                }


            }

        }
        viewModel.responseUserTargetSchools.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_USERTARGETSCHHOOL, NetworkResult.Action.POST_USERTARGETSCHHOOL -> {
                            viewModel.cacheUserTargeSchools()
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
        viewModel.responseCourse.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_COURSE -> {

                        }
                        NetworkResult.Action.POST_COURSE -> {
                            viewModel.getCoursecache()
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }
                                addSuccessDailogFragment.show(it)
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
        viewModel.responseIntellectualCuriosity.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_INTELLECTUALCURIOSITY -> {
                            result.data?.let {
                                viewModel.updateScoreAlgorithmIC(it.data as ArrayList<IntellectualCuriosity>)
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    viewModel.scoreAlgorithm.value?.let {
                                        addSuccessDailogFragment.score = it.getICScorePercent()
                                        addSuccessDailogFragment.scoreMax = 70
                                    }

                                    addSuccessDailogFragment.show(it)
                                }
                            }

                        }
                        NetworkResult.Action.POST_INTELLECTUALCURIOSITY -> {
                            viewModel.getIntellectualCuriositycache()
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
        viewModel.responseCommunityImpact.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_COMMUNITYIMPACT -> {
                            result.data?.let {
                                viewModel.updateScoreAlgorithmCI(it.data as ArrayList<CommunityImpact>)
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    viewModel.scoreAlgorithm.value?.let {
                                        addSuccessDailogFragment.score = it.getCIScorePercent()
                                        addSuccessDailogFragment.scoreMax = 30
                                    }

                                    addSuccessDailogFragment.show(it)
                                }
                            }

                        }
                        NetworkResult.Action.POST_COMMUNITYIMPACT -> {
                            viewModel.getCommunityImpactcache()
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
        viewModel.responsetestHonors.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_HONORS -> {
                            result.data?.let {
                                viewModel.updateScoreAlgorithmHonor(it.data as ArrayList<Honors>)
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    viewModel.scoreAlgorithm.value?.let {
                                        addSuccessDailogFragment.score = it.getHonroScorePercent()
                                        addSuccessDailogFragment.scoreMax = 100
                                    }

                                    addSuccessDailogFragment.show(it)
                                }
                            }

                        }
                        NetworkResult.Action.POST_HONORS -> {
                            viewModel.getHonorscache()
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
        viewModel.responsetestTest.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.CACHE_TESTS -> {
                            result.data?.let {
                                viewModel.updateScoreAlgorithmTest(it.data as ArrayList<Test>)
                                setTestDesp(it.data as ArrayList<Test>)

                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    viewModel.scoreAlgorithm.value?.let {
                                        addSuccessDailogFragment.score = it.getTestsScore()
                                        addSuccessDailogFragment.scoreMax = 100
                                    }
                                    addSuccessDailogFragment.show(it)
                                }
                            }

                        }
                        NetworkResult.Action.POST_TESTS -> {
                            viewModel.getTestcache()
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
    }

    private fun setTestDesp(tests: ArrayList<Test>?) {
        if (tests == null || tests.size == 0) {
            binding.tvMainacivityTestDesp.text = ""
            return
        }

        val titles = Test.groupedAttempts(tests).mapNotNull { attempts ->
            attempts.firstOrNull()?.title()?.takeIf(String::isNotBlank)
        }
        binding.tvMainacivityTestDesp.text = titles.joinToString(", ")

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.rl_mainacivity_ap -> {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_apmain)
            }
            R.id.rl_mainacivity_ap_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addCourseBSDailogFragment.isVisible) {
                        addCourseBSDailogFragment.course = null

                        addCourseBSDailogFragment.show(it, "AddCourseBSDailogFragment")
                    }
                }
            }
            R.id.rl_mainacivity_ic -> {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_icmain)
            }
            R.id.rl_mainacivity_ic_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addICActivityBSDailogFragment.isVisible) {
                        addICActivityBSDailogFragment.intellectualCuriosity = null
                        addICActivityBSDailogFragment.show(it, "AddICActivityBSDailogFragment")
                    }
                }
            }
            R.id.rl_mainacivity_ci -> {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_cimain)
            }
            R.id.rl_mainacivity_ci_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addCIActivityBSDailogFragment.isVisible) {
                        addCIActivityBSDailogFragment.communityImpact = null
                        addCIActivityBSDailogFragment.show(it, "AddCIActivityBSDailogFragment")
                    }
                }
            }
            R.id.rl_mainacivity_honors -> {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_honorpmain)
            }
            R.id.rl_mainacivity_honors_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addHonorsBSDailogFragment.isVisible) {
                        addHonorsBSDailogFragment.honors = null
                        addHonorsBSDailogFragment.show(it, "AddHonorsBSDailogFragment")
                    }
                }
            }

            R.id.rl_mainacivity_test -> {
                findNavController().navigate(R.id.action_navigation_main_to_navigation_testmain)
            }
            R.id.iv_mainacivity_test_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addTestFinalBSDailogFragment.isVisible) {
                        addTestFinalBSDailogFragment.test = null
                        addTestFinalBSDailogFragment.show(it, "AddTestBSDailogFragment")
                    }
                }
            }
            R.id.ll_mainacivity_interest -> {
                //興趣展開
                if (binding.llMainacivityInterestCtl.visibility == View.VISIBLE) {
                    binding.llMainacivityInterestCtl.visibility = View.GONE
//                    binding.llMainacivityInterestCtl.animate()
//                        .translationY(0f)
//                        .alpha(0.0f)
//                        .setListener(object : AnimatorListenerAdapter() {
//                            override fun onAnimationEnd(animation: Animator) {
//                                super.onAnimationEnd(animation)
//                                binding.llMainacivityInterestCtl.visibility = View.GONE
//                            }
//                        })
                } else {
                    binding.llMainacivityInterestCtl.visibility = View.VISIBLE

//                    binding.llMainacivityInterestCtl.setAlpha(0.0f);
//                    // Start the animation
//                    binding.llMainacivityInterestCtl.animate()
//                        .translationY(binding.llMainacivityInterestCtl.height.toFloat())
//                        .alpha(1.0f)
//                        .setListener(null);
                }
            }

            R.id.rl_mainacivity_interest_edit -> {
                //編輯興趣
                activity?.let {
                    val interestSelectBSDailogFragment = InterestSelectBSDailogFragment()
                    userInfo?.let {
                        if (it.areaOfInterest == null) {
                            interestSelectBSDailogFragment.areaOfInterest = AreaOfInterest()
                        } else {
                            interestSelectBSDailogFragment.areaOfInterest = it.areaOfInterest!!
                        }
                    }
                    interestSelectBSDailogFragment.onDone = {
                        userInfo.areaOfInterest = it
                        //設定已選中的興趣 刷新
                        (binding.rvMainacivityInterest.adapter as InterestContentRecyleViewAdapter).setData(
                            it.getCheckedList(requireContext())
                        )
                        if ((binding.rvMainacivityInterest.adapter as InterestContentRecyleViewAdapter).dataList.size > 0) {

                            binding.ivMainacivityInterestDividerTop.visibility = View.VISIBLE
                            binding.rvMainacivityInterest.visibility = View.VISIBLE
                            binding.ivMainacivityInterestDividerBottom.visibility = View.VISIBLE
                        } else {
                            binding.ivMainacivityInterestDividerTop.visibility = View.GONE
                            binding.rvMainacivityInterest.visibility = View.GONE
                            binding.ivMainacivityInterestDividerBottom.visibility = View.GONE
                        }
                        //TODO PUT會導致重複發生BUG
                        viewModel.putUserBasic(userInfo)
                        testPrint("onDone", "interestSelectBSDailogFragment")
                    }

                    interestSelectBSDailogFragment.showOnce(it.supportFragmentManager)
                }
            }

            R.id.iv_mainacivity_school_edit -> {
                //編輯學校
                binding.ivMainacivitySchoolEdit.visibility = View.GONE
                binding.btnMainacivitySchoolCancel.visibility = View.VISIBLE
                binding.llMainacivitySchoolEdit.visibility = View.VISIBLE
                binding.ivMainacivitySchoolEditDivider.visibility = View.VISIBLE
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).isEditing = true
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).dataList.forEach {
                    it.isSelected = false
                }
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).notifyDataSetChanged()
//                itemTouchHelper.attachToRecyclerView(null)
            }
            R.id.btn_mainacivity_school_cancel -> {
                //取消編輯學校
                binding.ivMainacivitySchoolEdit.visibility = View.VISIBLE
                binding.btnMainacivitySchoolCancel.visibility = View.GONE
                binding.llMainacivitySchoolEdit.visibility = View.GONE
                binding.ivMainacivitySchoolEditDivider.visibility = View.GONE
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).isEditing =
                    false
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).notifyDataSetChanged()
//                itemTouchHelper.attachToRecyclerView(binding.rvMainacivitySchool)


            }
            R.id.tv_mainacivity_school_remove -> {
                //移除學校
                var userTargetSchools = ArrayList<UserTargetSchool>()
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).dataList.forEach {
                    if (it.isSelected){
                        userTargetSchools.add(UserTargetSchool(it.schoolId))
                    }
                }
                if (userTargetSchools.size>0){
                    for (item in userTargetSchools){
                        viewModel.deleteUserTargetSchool(item)
                    }
                }
                binding.btnMainacivitySchoolCancel.performClick()
            }
            R.id.rl_mainacivity_school_add -> {
                //加入申請學校
                var applications = ArrayList<ApplicationSchool>()
                (binding.rvMainacivitySchool.adapter as SchoolEditRecyleViewAdapter).dataList.forEach {
                    if (it.isSelected){
                        applications.add(ApplicationSchool(it.schoolId!!,0))
                    }
                }
                if (applications.size>0){
                    for (item in applications){
                        viewModel.postApplicationSchool(item)
                    }
                }
                binding.btnMainacivitySchoolCancel.performClick()
            }
            R.id.btn_mainacivity_school_add -> {
                //加入學校
                if (!schoolSelectBSDailogFragment.isVisible) {
                    schoolSelectBSDailogFragment.selectetMap = HashMap(selectSchools)
                    schoolSelectBSDailogFragment.showOnce(activity?.supportFragmentManager!!)
                }
            }

        }
    }
    var isRefesh =  false
    fun reFreshSore(){
        if(!isRefesh){
            isRefesh = true
            viewModel.getUserTargetSchools()
            viewModel.initialScoreAlgorithn()
            viewModel.cacheTargeSchools()
        }
    }

    override fun onStop() {
        dismissReadinessInfoDialog()
        super.onStop()
    }

    override fun onDestroyView() {
        dismissReadinessInfoDialog()
        super.onDestroyView()
        _binding = null
    }

    private fun dismissReadinessInfoDialog() {
        Log.d(TAG, "dismissReadinessInfoDialog")
        readinessInfoOverlay?.let { overlay ->
            (overlay.parent as? ViewGroup)?.removeView(overlay)
        }
        readinessInfoOverlay = null
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
