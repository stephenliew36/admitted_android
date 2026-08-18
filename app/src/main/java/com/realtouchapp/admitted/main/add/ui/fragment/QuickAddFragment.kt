package com.realtouchapp.admitted.main.add.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.databinding.FragmentQuickaddBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.add.QuickAddFragmentViewModel
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddCIActivityBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddCourseBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddHonorsBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddICActivityBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddScoreYearBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddTestFinalBSDailogFragment
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.result.base.NetworkResult

/*
    作者: RobertChou
    時間: 2023/5/21 下午 10:59
    信箱: didi31139@gmail.com
    說明:
*/
class QuickAddFragment : BottomSheetDialogFragment() {
    private val viewModel: QuickAddFragmentViewModel by activityViewModels()
    companion object{
        const val TAG ="QcickAddFragment"
    }

    lateinit var binding:FragmentQuickaddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.qucikaddBottomSheetStyle);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuickaddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialView()
        subscribeUi()
    }
    override fun onStart() {
        super.onStart()
//        //拿到系统的 bottom_sheet
//        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
//        //设置view高度
//        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        //获取behavior
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        //设置弹出高度
        behavior.peekHeight = 2000
        //设置展开状态
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }
    private fun subscribeUi() {


        viewModel.responseCourse.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.POST_COURSE -> {
                            (requireActivity()as MainActivity).initialScoreAlgorithn()
                            addSuccessDailogFragment?.let {addSuccessDailogFragment->
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                                }
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
                        NetworkResult.Action.POST_INTELLECTUALCURIOSITY -> {
                            (requireActivity()as MainActivity).initialScoreAlgorithn()
                            addSuccessDailogFragment?.let {addSuccessDialogFragment->
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDialogFragment.name = it
                                        }
                                    }
                                    addSuccessDialogFragment.show(it, "AddSuccessDialogFragment")
                                }
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
        viewModel.responseCommunityImpact.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.POST_COMMUNITYIMPACT -> {
                            (requireActivity()as MainActivity).initialScoreAlgorithn()
                            addSuccessDailogFragment?.let {addSuccessDailogFragment->
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                                }
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
        viewModel.responsetestHonors.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.POST_HONORS -> {
                            (requireActivity()as MainActivity).initialScoreAlgorithn()
                            addSuccessDailogFragment?.let {addSuccessDailogFragment->
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                                }
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
        viewModel.responsetestTest.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.POST_TESTS -> {
                            (requireActivity()as MainActivity).initialScoreAlgorithn()
                            addSuccessDailogFragment?.let {addSuccessDailogFragment->
                                activity?.supportFragmentManager?.let {
                                    viewModel.userInfo.value?.let {
                                        it.firstName?.let {
                                            addSuccessDailogFragment.name = it
                                        }
                                    }
                                    addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                                }
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
    }

    var addScoreYearBSDailogFragment:AddScoreYearBSDailogFragment?=null
    fun showAddScoreYearBSDailogFragment(){
        if (addScoreYearBSDailogFragment==null){
            addScoreYearBSDailogFragment = AddScoreYearBSDailogFragment()

            addScoreYearBSDailogFragment!!.doneMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_ACADEMICYEAR
                addSuccessDailogFragment!!.academicYear = it.copy()
                viewModel.putAcademicYear(it)
            }
            addScoreYearBSDailogFragment!!.editMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_ACADEMICYEAR
                addSuccessDailogFragment!!.academicYear = it.copy()
                viewModel.putAcademicYear(it)
            }
            addScoreYearBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddScoreYearBSDailogFragment.TAG)
        }else{
            if (!addScoreYearBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addScoreYearBSDailogFragment!!).commit()
                addScoreYearBSDailogFragment=null
                showAddScoreYearBSDailogFragment()
            }
        }
    }

    var addCourseBSDailogFragment: AddCourseBSDailogFragment?=null
    fun showAddCourseBSDailogFragment(){
        if (addCourseBSDailogFragment==null){
            addCourseBSDailogFragment = AddCourseBSDailogFragment()
            addCourseBSDailogFragment!!.doneMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_ACADEMIC
                addSuccessDailogFragment!!.course = it.copy()
                viewModel.postCourse(it)
            }
            addCourseBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddCourseBSDailogFragment.TAG)
        }else{
            if (!addCourseBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addCourseBSDailogFragment!!).commit()
                addCourseBSDailogFragment=null
                showAddCourseBSDailogFragment()
            }
        }
    }

    var addICActivityBSDailogFragment: AddICActivityBSDailogFragment?=null
    fun showAddICActivityBSDailogFragment(){
        if (addICActivityBSDailogFragment==null){
            addICActivityBSDailogFragment = AddICActivityBSDailogFragment()
            addICActivityBSDailogFragment!!.doneMeathod={
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_IC
                addSuccessDailogFragment!!.intellectualCuriosity = it.copy()
                viewModel.postIntellectualCuriosity(it)
            }
            addICActivityBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddICActivityBSDailogFragment.TAG)
        }else{
            if (!addICActivityBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addICActivityBSDailogFragment!!).commit()
                addICActivityBSDailogFragment=null
                showAddICActivityBSDailogFragment()
            }
        }
    }

    var addCIActivityBSDailogFragment: AddCIActivityBSDailogFragment?=null
    fun showAddCIActivityBSDailogFragment(){
        if (addCIActivityBSDailogFragment==null){
            addCIActivityBSDailogFragment = AddCIActivityBSDailogFragment()
            addCIActivityBSDailogFragment!!.doneMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_CI
                addSuccessDailogFragment!!.communityImpact = it.copy()
                viewModel.postCommunityImpact(it)
            }
            addCIActivityBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddCIActivityBSDailogFragment.TAG)
        }else{
            if (!addCIActivityBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addCIActivityBSDailogFragment!!).commit()
                addCIActivityBSDailogFragment=null
                showAddCIActivityBSDailogFragment()
            }
        }
    }

    var addHonorsBSDailogFragment: AddHonorsBSDailogFragment?=null
    fun showAddHonorsBSDailogFragment(){
        if (addHonorsBSDailogFragment==null){
            addHonorsBSDailogFragment = AddHonorsBSDailogFragment()
            addHonorsBSDailogFragment!!.doneMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_HONORS
                addSuccessDailogFragment!!.honors = it.copy()
                viewModel.postHonors(it)
            }
            addHonorsBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddHonorsBSDailogFragment.TAG)
        }else{
            if (!addHonorsBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addHonorsBSDailogFragment!!).commit()
                addHonorsBSDailogFragment=null
                showAddHonorsBSDailogFragment()
            }
        }
    }

    var addTestFinalBSDailogFragment:AddTestFinalBSDailogFragment?=null
    fun showAddTestFinalBSDailogFragment(){
        if (addTestFinalBSDailogFragment==null){
            addTestFinalBSDailogFragment = AddTestFinalBSDailogFragment()
            addTestFinalBSDailogFragment!!.doneMeathod = {
                addSuccessDailogFragment = AddSuccessDailogFragment()
                addSuccessDailogFragment!!.type = AddSuccessDailogFragment.TYPE_TEST
                addSuccessDailogFragment!!.test = it.copy()
                viewModel.postTest(it)
            }
            addTestFinalBSDailogFragment!!.show(requireActivity().supportFragmentManager,AddTestFinalBSDailogFragment.TAG)
        }else{
            if (!addTestFinalBSDailogFragment!!.isVisible){
                requireActivity().supportFragmentManager.beginTransaction().remove(addTestFinalBSDailogFragment!!).commit()
                addTestFinalBSDailogFragment=null
                showAddTestFinalBSDailogFragment()
            }
        }
    }
    var addSuccessDailogFragment: AddSuccessDailogFragment?=null

    private fun initialView() {
        binding.llQucikaddGpa.setOnClickListener {showAddScoreYearBSDailogFragment() }
        binding.llQucikaddCourse.setOnClickListener { showAddCourseBSDailogFragment() }
        binding.llQucikaddIc.setOnClickListener { showAddICActivityBSDailogFragment() }
        binding.llQucikaddCi.setOnClickListener {  showAddCIActivityBSDailogFragment()}
        binding.llQucikaddHonors.setOnClickListener { showAddHonorsBSDailogFragment() }
        binding.llQucikaddTest.setOnClickListener {  showAddTestFinalBSDailogFragment()}
        binding.ivQucikaddClose.setOnClickListener {
            dismiss()
        }
    }

}