package com.realtouchapp.admitted.main.explore.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.databinding.FragmentSchoolmainBinding
import com.realtouchapp.admitted.common.parity.SingleActionGate
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.explore.ui.adapter.TargetSchoolBirefAdapter
import com.realtouchapp.admitted.main.explore.ui.adapter.TargetSchoolCategoryAdapter
import com.realtouchapp.admitted.main.explore.ui.bsdialogfragment.CollegesFilterBSDailogFragment
import com.realtouchapp.admitted.main.explore.viewmodel.SchoolMainFragmentViewModel
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.TargetSchooFilter
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.entity.UserTargetSchool
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import kotlinx.coroutines.*
import java.util.stream.Collectors

class SchoolMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentSchoolmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel:SchoolMainFragmentViewModel by activityViewModels()
    private val mainViewModel:MainActivityViewModel by activityViewModels()
    private var targetSchooFilter = TargetSchooFilter()
    lateinit var targetSchoolBirefAdapter:TargetSchoolBirefAdapter
    lateinit var gridLayoutManager :GridLayoutManager
    lateinit var targetSchoolCategoryAdapter :TargetSchoolCategoryAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var isFavFilter = false
    var notifySchoolID = 0
    var userTargetSchools= ArrayList<UserTargetSchool>()
    var applicationSchools= ArrayList<ApplicationSchool>()
    private val navigationGate = SingleActionGate()
    private var filterDialog: CollegesFilterBSDailogFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSchoolmainBinding.inflate(inflater, container, false)
        isFavFilter = savedInstanceState?.getBoolean(STATE_FAVORITES) ?: false
        searchText = savedInstanceState?.getString(STATE_SEARCH).orEmpty()
        targetSchooFilter = restoreFilter(savedInstanceState)
        val root: View = binding.root
        initialViewSize()
        initialView()
        binding.etTargetschool.setText(searchText)
        checkFreeVersion()
        return root
    }

    override fun onResume() {
        super.onResume()
        navigationGate.reset()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()

        viewModel.cacheApplicationSchools()
        viewModel.cacheUserTargeSchools()
        mainViewModel.getCategoryTagetSchools("", TargetSchooFilter(),isFavFilter)

    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            mainViewModel.isFreeVersion.collect{
                if (it){
                    binding.clExploreFreeUpgrade.visibility = View.VISIBLE
                    binding.clExploreFreeUpgrade.setOnClickListener {
                        PremiumActivity.Intent(requireActivity() as AppCompatActivity)
                    }
                    binding.ivMainacivityFreeExampleExplore.visibility = View.VISIBLE
                }
            }
        }
    }

    fun subscribeUi() {
        mainViewModel.categoryTargetSchools.observe(/* owner = */ viewLifecycleOwner){
            if (targetSchooFilter.isFilter()){
                if (it.size>0){
                    targetSchoolBirefAdapter.dataList = it.get(0).getValue(it.get(0).keys.toTypedArray().get(0))
                    binding.rvFragmentTargetschool.layoutManager = gridLayoutManager
                    binding.rvFragmentTargetschool.adapter = targetSchoolBirefAdapter
                    targetSchoolBirefAdapter.notifyDataSetChanged()
                }else{
                    targetSchoolBirefAdapter.dataList = ArrayList<TargetSchool>()
                    binding.rvFragmentTargetschool.layoutManager = gridLayoutManager
                    binding.rvFragmentTargetschool.adapter = targetSchoolBirefAdapter
                    targetSchoolBirefAdapter.notifyDataSetChanged()
                }

            }else{
                targetSchoolCategoryAdapter.dataList = it
                binding.rvFragmentTargetschool.layoutManager = linearLayoutManager
                binding.rvFragmentTargetschool.adapter = targetSchoolCategoryAdapter
                targetSchoolCategoryAdapter.notifyDataSetChanged()
            }

        }
        mainViewModel.scoreAlgorithm.observe(viewLifecycleOwner){
            mainViewModel.getCategoryTagetSchools(binding.etTargetschool.text.toString(), TargetSchooFilter(),isFavFilter)
        }
        viewModel.responseUserTargetSchool.observe(viewLifecycleOwner){result->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_USERTARGETSCHHOOL, NetworkResult.Action.POST_USERTARGETSCHHOOL,NetworkResult.Action.DELETE_USERTARGETSCHHOOL -> {
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

            targetSchoolBirefAdapter.notifyItemChanged(targetSchoolBirefAdapter.dataList.indexOfFirst { it.schoolId==notifySchoolID})
//            targetSchoolBirefAdapter.notifyDataSetChanged()
            targetSchoolCategoryAdapter.notifyDataSetChanged()
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
            targetSchoolBirefAdapter.notifyItemChanged(targetSchoolBirefAdapter.dataList.indexOfFirst { it.schoolId==notifySchoolID})
//            targetSchoolBirefAdapter.notifyDataSetChanged()
            targetSchoolCategoryAdapter.notifyDataSetChanged()
        }

    }


    fun initialViewSize(){
        binding.etTargetschool.setTextSize(13)
        binding.etTargetschool.setViewSizeOrignalWH(261,40)
    }

    private var searchText = ""
    private var searchJob: Job? = null

    fun initialView(){

        binding.etTargetschool.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString().trim()
//                mainViewModel.getCategoryTagetSchools(s.toString(), targetSchooFilter)
            }

            override fun afterTextChanged(s: Editable?) {
                searchJob?.cancel()
                // 重置計時器
                searchJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(200) // 延遲500毫秒
                    if (searchText == binding.etTargetschool.text.toString()) {
                        // 如果輸入框內容沒有更改，則發送 API 請求
                        mainViewModel.getCategoryTagetSchools(searchText.trim(), targetSchooFilter,isFavFilter)
                    }
                }

            }
        })

        binding.ivTargetschoolFilter.setOnClickListener (this)

        binding.ivTargetschoolFav.setImageResource(
            if (isFavFilter) R.drawable.vd_ic_favorite_filled else R.drawable.vd_ic_favorite_empty
        )
        binding.ivTargetschoolFav.setOnClickListener (this)

        targetSchoolBirefAdapter = TargetSchoolBirefAdapter(requireContext(),mainViewModel.isFreeVersion.value?:false)
        gridLayoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL,false)
        targetSchoolBirefAdapter.onItemClick = { item ->
            navigateToSchool(item)
        }
        targetSchoolBirefAdapter.isInApplicationSchool = {id->
            (this.applicationSchools.stream().filter{it.schoolId ==id}.collect(Collectors.toList()).size)>0
        }
        targetSchoolBirefAdapter.postApplicationSchool={id->
            if (mainViewModel.isFreeVersion.value) {
                PremiumActivity.Intent(requireActivity() as AppCompatActivity)
            } else {
                notifySchoolID=id
                viewModel.postApplicationSchool(ApplicationSchool(id,0))
            }
        }
        targetSchoolBirefAdapter.isInUserTargetSchool = {id->
            (this.userTargetSchools.stream().filter{it.schoolId ==id}.collect(Collectors.toList()).size)>0
        }
        targetSchoolBirefAdapter.postUserTargetSchool={id->
            notifySchoolID=id
            viewModel.postUserTargetSchool(UserTargetSchool(id))
        }
        targetSchoolBirefAdapter.deleteUserTargetSchool={id->
            notifySchoolID=id
            viewModel.deleteUserTargetSchool(UserTargetSchool(id))
        }


        targetSchoolCategoryAdapter = TargetSchoolCategoryAdapter(requireContext(),mainViewModel.isFreeVersion.value?:false)
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        targetSchoolCategoryAdapter.onViewAllItemClick ={

        }
        targetSchoolCategoryAdapter.onItemClick = { item ->
            navigateToSchool(item)
        }

        targetSchoolCategoryAdapter.isInApplicationSchool = {id->
            (this.applicationSchools.stream().filter{it.schoolId ==id}.collect(Collectors.toList()).size)>0
        }
        targetSchoolCategoryAdapter.postApplicationSchool={id->
            if (mainViewModel.isFreeVersion.value) {
                PremiumActivity.Intent(requireActivity() as AppCompatActivity)
            } else {
                notifySchoolID=id
                viewModel.postApplicationSchool(ApplicationSchool(id,0))
            }
        }
        targetSchoolCategoryAdapter.isInUserTargetSchool = {id->
            (this.userTargetSchools.stream().filter{it.schoolId ==id}.collect(Collectors.toList()).size)>0
        }
        targetSchoolCategoryAdapter.postUserTargetSchool={id->
            notifySchoolID=id
            viewModel.postUserTargetSchool(UserTargetSchool(id))
        }
        targetSchoolCategoryAdapter.deleteUserTargetSchool={id->
            notifySchoolID=id
            viewModel.deleteUserTargetSchool(UserTargetSchool(id))
        }
    }

    private fun navigateToSchool(item: TargetSchool) {
        val controller = parentFragmentManager.findFragmentById(R.id.nav_host_fragment2)?.findNavController() ?: return
        if (!navigationGate.tryBegin()) return
        controller.navigate(
            R.id.action_navigation_explore_main_to_navigation_collegedetail,
            Bundle().apply { putSerializable(CollegeDetailFragment.BUNDLE_TARGETSCHOOL, item) }
        )
    }

    private fun reLoadAdapter(targetSchool: ArrayList<TargetSchool>?){
        var isUseFilter = false
        if (isUseFilter ){
            //有使用篩選條件
        }else{
            //沒有使用篩選條件


        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_honorsmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.iv_targetschool_filter -> {
                activity?.let {
                    if (filterDialog?.isAdded == true) return
                    val collegesFilterBSDailogFragment = CollegesFilterBSDailogFragment()
                    filterDialog = collegesFilterBSDailogFragment
                    collegesFilterBSDailogFragment.tagetSchooFilter = targetSchooFilter
                    collegesFilterBSDailogFragment.onDone={
                        targetSchooFilter = it.copy()
                        mainViewModel.getCategoryTagetSchools(binding.etTargetschool.text.toString(), targetSchooFilter,isFavFilter)
                        testPrint("onDone","collegesFilterBSDailogFragment")
                    }
                    collegesFilterBSDailogFragment.showOnce(it.supportFragmentManager)
                }
            }

            R.id.iv_targetschool_fav -> {
                if (isFavFilter){
                    isFavFilter = false
                    binding.ivTargetschoolFav.setImageResource(R.drawable.vd_ic_favorite_empty)
                    mainViewModel.getCategoryTagetSchools(searchText, targetSchooFilter,isFavFilter)
                }else{
                    isFavFilter = true
                    binding.ivTargetschoolFav.setImageResource(R.drawable.vd_ic_favorite_filled)
                    mainViewModel.getCategoryTagetSchools(searchText, targetSchooFilter,isFavFilter)
                }

            }

            R.id.rl_honorsmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                }

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_FAVORITES, isFavFilter)
        outState.putString(STATE_SEARCH, searchText)
        with(targetSchooFilter) {
            outState.putBooleanArray(STATE_FILTER, booleanArrayOf(
                isCountry_UnitedStates, isCountry_Canada, isCountry_UnitedKingdom,
                isDifficulty_FarReach, isDifficulty_Reach, isDifficulty_Target, isDifficulty_Safety,
                isPublic_Public, isPublic_Private, isOthers_LiberalArts, isOthers_Art
            ))
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreFilter(state: Bundle?): TargetSchooFilter {
        val values = state?.getBooleanArray(STATE_FILTER) ?: return TargetSchooFilter()
        if (values.size != 11) return TargetSchooFilter()
        return TargetSchooFilter(
            values[0],
            false, // Canada filter removed
            false, // UK filter removed
            values[3], values[4], values[5],
            values[6], values[7], values[8], values[9], values[10]
        )
    }

    companion object {
        private const val STATE_FAVORITES = "explore_favorites"
        private const val STATE_SEARCH = "explore_search"
        private const val STATE_FILTER = "explore_filter"
    }

}
