package com.realtouchapp.admitted.main.roadmmap.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.FragmentCimainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddCIActivityBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.CommunityImpactInfoAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.CommunityImpact
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch

class CommunityImpactMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentCimainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    lateinit var communityImpacts:ArrayList<CommunityImpact>
    var addCIActivityBSDailogFragment = AddCIActivityBSDailogFragment()
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCimainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()
        checkFreeVersion()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCommunityImpact()
        subscribeUi()
    }
    fun subscribeUi(){
        viewModel.responseCommunityImpact.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                        NetworkResult.Action.GET_COMMUNITYIMPACT,NetworkResult.Action.CACHE_COMMUNITYIMPACT->{

                            communityImpacts = ArrayList<CommunityImpact>()
                            result.data?.let { data ->
                                data.data?.let { it ->
                                    communityImpacts!!.addAll(it as List<CommunityImpact>)
                                }

                            }
                            viewModel.updateScoreAlgorithmCI(communityImpacts)
                            displayTite()
                            if (communityImpacts.size>0){
                                val userType = viewModel.userType.value

                                //顯示清單
                                (binding.rvCimainfragmentActivities.adapter as CommunityImpactInfoAdapter).setData(communityImpacts)
                                (binding.rvCimainfragmentActivities.adapter as CommunityImpactInfoAdapter).notifyDataSetChanged()

                                if (userType == UserType.STUDENT) {
                                    binding.rvCimainfragmentActivities.addSwipeLeftDelete(communityImpacts) {
                                        viewModel.deleteCommunityImpact(communityImpacts.get(it))
                                        communityImpacts.removeAt(it)
                                    }
                                }

                                binding.rvCimainfragmentActivities.visibility = View.VISIBLE
                                binding.ivCimainfragmentEmpty.visibility =View.GONE
                                binding.tvCimainfragmentEmpty.visibility =View.GONE
                            }else{
                                //顯示資料為空
                                binding.rvCimainfragmentActivities.visibility = View.GONE
                                binding.ivCimainfragmentEmpty.visibility =View.VISIBLE
                                binding.tvCimainfragmentEmpty.visibility =View.VISIBLE
                            }
                        }
                        NetworkResult.Action.DELETE_COMMUNITYIMPACT->{
                            viewModel.getCommunityImpactcache()
                        }
                        NetworkResult.Action.POST_COMMUNITYIMPACT,NetworkResult.Action.PUT_COMMUNITYIMPACT->{
                            viewModel.getCommunityImpactcache()
                            //新增成功顯示 成功對話框
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }

                                viewModel.scoreAlgorithm.value?.let {
                                    addSuccessDailogFragment.communityImpact?.let {communityImpact->
                                        it.communityImpacts.add(addSuccessDailogFragment.communityImpact!!)
                                    }
                                    val score = ((it.getCIScorePercent()*10f)/3f).toInt()
                                    addSuccessDailogFragment.score = score
                                    addSuccessDailogFragment.scoreMax = 100
                                }
                                addSuccessDailogFragment.show(it, "AddSuccessDailogFragment")
                            }

                        }
                        else->{

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
        viewModel.scoreAlgorithm.observe(viewLifecycleOwner){
            val score = ((it.getCIScorePercent()*10f)/3f).toInt()
            binding.pbCimainfragmentTest.progress = score
            binding.tvCimainfragmentTitlePb.text = score.toString() + "/100"
        }
    }
    fun initialViewSize() {
        //Back
        binding.ivCimainfragmentBack.setViewSizeOrignalWH(24,24)
        binding.tvCimainfragmentBack.setTextSize(13)

        //Card
        binding.ivCimainfragmentBack.setViewSizeOrignalWH(24,24)
        binding.rlCimainfragmentCard.setMarginTopOrignal(12)
        binding.rlCimainfragmentCard.setViewSizeOrignalHeight(120)
        binding.ivCimainfragmentIcon.setViewSizeOrignalWH(65,65)
        binding.rlCimainfragmentTitle.setMarginLeftOrignal(16)
        binding.tvCimainfragmentTitle.setTextSize(17)
        binding.tvCimainfragmentTitleDesp.setTextSize(13)
        binding.pbCimainfragmentTest.setViewSizeOrignalWH(155,8)
        binding.pbCimainfragmentTest.setMarginTopOrignal(16)
        binding.tvCimainfragmentTitlePb.setTextSize(13)
        binding.tvCimainfragmentTitlePb.setMarginLeftOrignal(8)
        binding.tvCimainfragmentTitlePb.setMarginTopOrignal(-10)

        //Activities
        binding.tvCimainfragmentActivities.setTextSize(17)
        binding.tvCimainfragmentActivities.setMarginTopOrignal(24)
        binding.ivCimainfragmentEmpty.setViewSizeOrignalWH(160,176)
        binding.tvCimainfragmentEmpty.setTextSize(13)
        binding.tvCimainfragmentEmpty.setMarginTopOrignal(24)
        binding.tvCimainfragmentAdd.setTextSize(13)
        binding.tvCimainfragmentAdd.setMarginTopOrignal(24)

        //Resource
        binding.tvCimainfragmentResource.setTextSize(17)
        binding.tvCimainfragmentResource.setMarginTopOrignal(24)
        binding.tvCimainfragmentResourceView.setTextSize(13)
        binding.tvCimainfragmentResourceView.setMarginTopOrignal(24)


    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlCimainfragmentAdd.gone()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlCimainfragmentAdd.visible()
                    }
                    else -> { }
                }
            }
        }

        //Back
        binding.llCimainfragmentBack.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvCimainfragmentActivities.layoutManager = linearLayoutManager
        val communityImpactInfoAdapter = CommunityImpactInfoAdapter(requireContext())
        communityImpactInfoAdapter.onItemClick = {position->
            val innerUserType = viewModel.userType.value
            if (innerUserType == UserType.STUDENT) {
                activity?.supportFragmentManager?.let {
                    if (!addCIActivityBSDailogFragment.isVisible){
                        addCIActivityBSDailogFragment.communityImpact = communityImpacts.get(position)
                        addCIActivityBSDailogFragment.show(it,"AddCIActivityBSDailogFragment")
                    }
                }
            }
        }
        communityImpacts = ArrayList()
        communityImpactInfoAdapter.setData(communityImpacts)
        binding.rvCimainfragmentActivities.adapter = communityImpactInfoAdapter


        binding.rlCimainfragmentAdd.setOnClickListener(this)
        binding.rlCimainfragmentResource.setOnClickListener(this)

        addCIActivityBSDailogFragment = AddCIActivityBSDailogFragment()
        addCIActivityBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_CI
            addSuccessDailogFragment.communityImpact = it
            viewModel.postCommunityImpact(it)
        }
        addCIActivityBSDailogFragment.editMeathod = {
            viewModel.putCommunityImpact(it)
        }
    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.pbCimainfragmentTest.visibility = View.GONE
                    binding.tvCimainfragmentTitlePb.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun displayTite(){
        if (communityImpacts.size>0){
            var size = communityImpacts.size.toString()
                var hours = 0
            for (item in communityImpacts){
                item.hoursPerWeek?.let { hour->
                    item.weekPerYear?.let {week->
                        if (hour!=0&&week!=0) {
                            hours = hours + (hour*week)
                        }
                    }
                }
            }
            binding.tvCimainfragmentTitleDesp.text = getString(R.string.tv_cimainfragment_title_desp,size,hours.toString())
        }else{
            binding.tvCimainfragmentTitleDesp.text=""
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_cimainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_cimainfragment_add -> {
                activity?.supportFragmentManager?.let {

                    if (!addCIActivityBSDailogFragment.isVisible){
                        addCIActivityBSDailogFragment.communityImpact = null

                        addCIActivityBSDailogFragment.show(it,"AddCIActivityBSDailogFragment")
                    }
                }
            }
            R.id.rl_cimainfragment_resource -> {
                (requireActivity()as MainActivity).setSelecetTab(1)
            }
        }
    }
}