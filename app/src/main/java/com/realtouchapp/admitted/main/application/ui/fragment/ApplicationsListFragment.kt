package com.realtouchapp.admitted.main.application.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.databinding.FragmentApplicationlistBinding
import com.realtouchapp.admitted.main.application.ui.adapter.ApplicationBirefAdapter
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.network.entity.ApplicationSchool
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.tools.ext.addSwipeLeftDelete
import com.realtouchapp.tools.ext.invisible
import com.realtouchapp.tools.ext.setViewSizeWidthByRatio
import com.realtouchapp.tools.ext.visible
import kotlinx.coroutines.launch

class ApplicationsListFragment : Fragment(), View.OnClickListener {
    companion object {
        private const val TAG = "ApplicationsListFragment"
    }

    private var _binding: FragmentApplicationlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    private var targetSchools = ArrayList<TargetSchool>()
    private var reachSchools = ArrayList<TargetSchool>()
    private var safetySchools = ArrayList<TargetSchool>()
    private var farReachSchools = ArrayList<TargetSchool>()
    private var isRefesh = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentApplicationlistBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        viewModel.cacheApplicationSchools()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden == false){
            reFreshreadliness()
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.cacheApplicationSchools()

    }

    fun subscribeUi() {
        viewModel.applicationSchools.observe(/* owner = */ viewLifecycleOwner) {
            targetSchools = ArrayList<TargetSchool>()
            reachSchools = ArrayList<TargetSchool>()
            safetySchools = ArrayList<TargetSchool>()
            farReachSchools = ArrayList<TargetSchool>()
            //分類
            it.forEach {
                // Far Reach 名單最高優先 (Harvard/Princeton/Yale/MIT/Caltech/Stanford)，
                // 無論機率或難易度為何，一律歸到 Far Reach（與 web 版/iOS 版相同）
                if (TargetSchool.isFarReachSchool(it)){
                    farReachSchools.add(it)
                }else if (it.difficulty.equals(TargetSchool.DIFFICULTY_TARGET)){
                    targetSchools.add(it)
                }else if (it.difficulty.equals(TargetSchool.DIFFICULTY_SAFETY)){
                    safetySchools.add(it)
                }else if (it.difficulty.equals(TargetSchool.DIFFICULTY_REACH)){
                    reachSchools.add(it)
                }else if (it.difficulty.equals(TargetSchool.DIFFICULTY_FAR)){
                    reachSchools.add(it)
                }
            }

            val userType = viewModel.userType.value

            (binding.rvApplicaitonlistTitleTargetschool.adapter as ApplicationBirefAdapter).dataList = targetSchools
            (binding.rvApplicaitonlistTitleTargetschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
            if (targetSchools.size>=5){
                binding.tvApplicaitonlistHintTargetschool.visibility = View.GONE
            }else{
                binding.tvApplicaitonlistHintTargetschool.visibility = View.VISIBLE
                binding.tvApplicaitonlistHintTargetschool.text = "Add ${5-targetSchools.size} more Target School${if (5-targetSchools.size>1){"s"}else {""}} to balance your list"
            }

            if (userType == UserType.STUDENT) {
                binding.rvApplicaitonlistTitleTargetschool.addSwipeLeftDelete(targetSchools) {
                    viewModel.deleteUserApplicationSchool(ApplicationSchool(targetSchools.get(it).schoolId!!,0))
                    targetSchools.removeAt(it)
                    (binding.rvApplicaitonlistTitleTargetschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
                    if (targetSchools.size>=5){
                        binding.tvApplicaitonlistHintTargetschool.visibility = View.GONE
                    }else{
                        binding.tvApplicaitonlistHintTargetschool.visibility = View.VISIBLE
                        binding.tvApplicaitonlistHintTargetschool.text = "Add ${5-targetSchools.size} more Target School${ if (5-targetSchools.size>1){"s"}else {""}} to balance your list"
                    }
                }
            }

            (binding.rvApplicaitonlistTitleReachschool.adapter as ApplicationBirefAdapter).dataList = reachSchools
            (binding.rvApplicaitonlistTitleReachschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
            if (reachSchools.size>=3){
                binding.tvApplicaitonlistHintReachschool.visibility = View.GONE
            }else{
                binding.tvApplicaitonlistHintReachschool.visibility = View.VISIBLE
                binding.tvApplicaitonlistHintReachschool.text = "Add ${3-reachSchools.size} more Reach School${ if (3-reachSchools.size>1){"s"}else {""} } to balance your list"
            }

            if (userType == UserType.STUDENT) {
                binding.rvApplicaitonlistTitleReachschool.addSwipeLeftDelete(reachSchools) {
                    viewModel.deleteUserApplicationSchool(ApplicationSchool(reachSchools.get(it).schoolId!!,0))
                    reachSchools.removeAt(it)
                    (binding.rvApplicaitonlistTitleReachschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
                    if (reachSchools.size>=3){
                        binding.tvApplicaitonlistHintReachschool.visibility = View.GONE
                    }else{
                        binding.tvApplicaitonlistHintReachschool.visibility = View.VISIBLE
                        binding.tvApplicaitonlistHintReachschool.text = "Add ${3-reachSchools.size} more Reach School${ if (3-reachSchools.size>1){"s"}else {""} } to balance your list"
                    }
                }
            }

            (binding.rvApplicaitonlistTitleSafetyschool.adapter as ApplicationBirefAdapter).dataList = safetySchools
            (binding.rvApplicaitonlistTitleSafetyschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
            if (safetySchools.size>=2){
                binding.tvApplicaitonlistHintSafetyschool.visibility = View.GONE
            }else{
                binding.tvApplicaitonlistHintSafetyschool.visibility = View.VISIBLE
                binding.tvApplicaitonlistHintSafetyschool.text = "Add ${2-safetySchools.size} more Safety School${ if (2-safetySchools.size>1){"s"}else {""} } to balance your list"

            }

            if (userType == UserType.STUDENT) {
                binding.rvApplicaitonlistTitleSafetyschool.addSwipeLeftDelete(safetySchools) {
                    viewModel.deleteUserApplicationSchool(ApplicationSchool(safetySchools.get(it).schoolId!!,0))
                    safetySchools.removeAt(it)
                    (binding.rvApplicaitonlistTitleSafetyschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
                    if (safetySchools.size>=2){
                        binding.tvApplicaitonlistHintSafetyschool.visibility = View.GONE
                    }else{
                        binding.tvApplicaitonlistHintSafetyschool.visibility = View.VISIBLE
                        binding.tvApplicaitonlistHintSafetyschool.text = "Add ${2-safetySchools.size} more Safety School${ if (2-safetySchools.size>1){"s"}else {""} } to balance your list"

                    }
                }
            }

            (binding.rvApplicaitonlistTitleFarreachschool.adapter as ApplicationBirefAdapter).dataList = farReachSchools
            (binding.rvApplicaitonlistTitleFarreachschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
            if (farReachSchools.size>=2){
                binding.tvApplicaitonlistHintFarreachschool.visibility = View.GONE
            }else{
                binding.tvApplicaitonlistHintFarreachschool.visibility = View.VISIBLE
                binding.tvApplicaitonlistHintFarreachschool.text = "Add ${2-farReachSchools.size} more Far Reach School${ if (2-farReachSchools.size>1){"s"}else {""} } to balance your list"
            }

            if (userType == UserType.STUDENT) {
                binding.rvApplicaitonlistTitleFarreachschool.addSwipeLeftDelete(farReachSchools) {
                    viewModel.deleteUserApplicationSchool(ApplicationSchool(farReachSchools.get(it).schoolId!!,0))
                    farReachSchools.removeAt(it)
                    (binding.rvApplicaitonlistTitleFarreachschool.adapter as ApplicationBirefAdapter).notifyDataSetChanged()
                    if (farReachSchools.size>=2){
                        binding.tvApplicaitonlistHintFarreachschool.visibility = View.GONE
                    }else{
                        binding.tvApplicaitonlistHintFarreachschool.visibility = View.VISIBLE
                        binding.tvApplicaitonlistHintFarreachschool.text = "Add ${2-farReachSchools.size} more Far Reach School${ if (2-farReachSchools.size>1){"s"}else {""} } to balance your list"
                    }
                }
            }

        }
    }

    fun initialViewSize() {
        binding.nslApplicaitonlist.setViewSizeWidthByRatio(.935f)

    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        (binding.rvApplicaitonlistTitleTargetschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(false)
                        (binding.rvApplicaitonlistTitleReachschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(false)
                        (binding.rvApplicaitonlistTitleSafetyschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(false)
                        (binding.rvApplicaitonlistTitleFarreachschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(false)
                    }
                    else -> {
                        // 學生
                        (binding.rvApplicaitonlistTitleTargetschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(true)
                        (binding.rvApplicaitonlistTitleReachschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(true)
                        (binding.rvApplicaitonlistTitleSafetyschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(true)
                        (binding.rvApplicaitonlistTitleFarreachschool.adapter as? ApplicationBirefAdapter)
                            ?.setStudent(true)
                    }
                }
            }
        }

        val userType = viewModel.userType.value
        val isStudent = userType == null || userType == UserType.STUDENT

        var adapter_targetschool = ApplicationBirefAdapter(requireContext(), isStudent)
        var linearLayoutManager_targetschool = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter_targetschool.dataList = targetSchools
        adapter_targetschool.onItemClick ={
            var bundle = Bundle()
            bundle.putSerializable(ApplicationsCollegeFragment.BUNDLE_TARGETSCHOOL,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment4)?.findNavController()!!.navigate(R.id.action_navigation_application_main_to_navigation_applicationcollegedetail, bundle)
        }
        adapter_targetschool.onItemEdit ={
            viewModel.postApplicationSchool(it)
        }

        binding.rvApplicaitonlistTitleTargetschool.layoutManager = linearLayoutManager_targetschool
        binding.rvApplicaitonlistTitleTargetschool.adapter = adapter_targetschool

        var adapter_reachschool = ApplicationBirefAdapter(requireContext(), isStudent)
        var linearLayoutManager_reachschool = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter_reachschool.dataList = reachSchools
        adapter_reachschool.onItemClick ={
            var bundle = Bundle()
            bundle.putSerializable(ApplicationsCollegeFragment.BUNDLE_TARGETSCHOOL,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment4)?.findNavController()!!.navigate(R.id.action_navigation_application_main_to_navigation_applicationcollegedetail, bundle)
        }
        adapter_reachschool.onItemEdit ={
            viewModel.postApplicationSchool(it)
        }

        binding.rvApplicaitonlistTitleReachschool.layoutManager = linearLayoutManager_reachschool
        binding.rvApplicaitonlistTitleReachschool.adapter = adapter_reachschool

        var adapter_safetyschool = ApplicationBirefAdapter(requireContext(), isStudent)
        var linearLayoutManager_safetyschool = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter_safetyschool.dataList = safetySchools
        adapter_safetyschool.onItemClick ={
            var bundle = Bundle()
            bundle.putSerializable(ApplicationsCollegeFragment.BUNDLE_TARGETSCHOOL,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment4)?.findNavController()!!.navigate(R.id.action_navigation_application_main_to_navigation_applicationcollegedetail, bundle)
        }
        adapter_safetyschool.onItemEdit ={
            viewModel.postApplicationSchool(it)
        }
        binding.rvApplicaitonlistTitleSafetyschool.layoutManager = linearLayoutManager_safetyschool
        binding.rvApplicaitonlistTitleSafetyschool.adapter = adapter_safetyschool

        var adapter_farreachschool = ApplicationBirefAdapter(requireContext(), isStudent)
        var linearLayoutManager_farreachschool = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter_farreachschool.dataList = farReachSchools
        adapter_farreachschool.onItemClick ={
            var bundle = Bundle()
            bundle.putSerializable(ApplicationsCollegeFragment.BUNDLE_TARGETSCHOOL,it)
            parentFragmentManager.findFragmentById(R.id.nav_host_fragment4)?.findNavController()!!.navigate(R.id.action_navigation_application_main_to_navigation_applicationcollegedetail, bundle)
        }
        adapter_farreachschool.onItemEdit ={
            viewModel.postApplicationSchool(it)
        }
        binding.rvApplicaitonlistTitleFarreachschool.layoutManager = linearLayoutManager_farreachschool
        binding.rvApplicaitonlistTitleFarreachschool.adapter = adapter_farreachschool

        binding.btnApplicaitonlistTitleReachschoolAdd.setOnClickListener(this)
        binding.btnApplicaitonlistTitleTargetschoolAdd.setOnClickListener(this)
        binding.btnApplicaitonlistTitleSafetyschoolAdd.setOnClickListener(this)
        binding.btnApplicaitonlistTitleFarreachschoolAdd.setOnClickListener(this)
        binding.llFreeVersion.setOnClickListener(this)
        binding.clApplicaitonlistFreeUpgrade.setOnClickListener(this)

        //checkFreeVersion
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.llFreeVersion.visibility = View.VISIBLE
                    binding.llPaidVersion.visibility = View.GONE
                    binding.clApplicaitonlistFreeUpgrade.visibility = View.VISIBLE

                }else{
                    binding.llFreeVersion.visibility = View.GONE
                    binding.llPaidVersion.visibility = View.VISIBLE
                    binding.clApplicaitonlistFreeUpgrade.visibility = View.GONE
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_applicaitonlist_title_targetschool_add -> {
                (requireActivity() as MainActivity).setSelecetTab(1)
            }
            R.id.btn_applicaitonlist_title_reachschool_add -> {
                (requireActivity() as MainActivity).setSelecetTab(1)
            }
            R.id.btn_applicaitonlist_title_safetyschool_add -> {
                (requireActivity() as MainActivity).setSelecetTab(1)
            }
            R.id.btn_applicaitonlist_title_farreachschool_add -> {
                (requireActivity() as MainActivity).setSelecetTab(1)
            }
            R.id.llFreeVersion -> {
                PremiumActivity.Intent(requireActivity() as MainActivity)
            }
            R.id.clApplicaitonlistFreeUpgrade -> {
                PremiumActivity.Intent(requireActivity() as MainActivity)
            }
        }
    }
    fun reFreshreadliness(){
        if(!isRefesh){
            isRefesh = true
            viewModel.cacheApplicationSchools()
        }
    }
}