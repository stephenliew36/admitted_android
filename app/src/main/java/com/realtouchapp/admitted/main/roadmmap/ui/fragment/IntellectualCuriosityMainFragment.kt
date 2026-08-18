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
import com.realtouchapp.admitted.databinding.FragmentIcmainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddICActivityBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.IntellectualCuriosityInfoAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.IntellectualCuriosity
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch

class IntellectualCuriosityMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentIcmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    lateinit var intellectualCuriositys:ArrayList<IntellectualCuriosity>
    var addICActivityBSDailogFragment = AddICActivityBSDailogFragment()
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentIcmainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()
        checkFreeVersion()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getIntellectualCuriosity()
        subscribeUi()
    }
    fun subscribeUi(){
        viewModel.responseIntellectualCuriosity.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                        NetworkResult.Action.GET_INTELLECTUALCURIOSITY,NetworkResult.Action.CACHE_INTELLECTUALCURIOSITY->{

                            intellectualCuriositys = ArrayList<IntellectualCuriosity>()
                            result.data?.let { data ->
                                data.data?.let { it ->
                                    intellectualCuriositys!!.addAll(it as List<IntellectualCuriosity>)
                                }

                            }
                            viewModel.updateScoreAlgorithmIC(intellectualCuriositys)
                            displayTite()
                            if (intellectualCuriositys.size>0){
                                val userType = viewModel.userType.value

                                //顯示清單
                                (binding.rvIcmainfragmentActivities.adapter as IntellectualCuriosityInfoAdapter).setData(intellectualCuriositys)
                                (binding.rvIcmainfragmentActivities.adapter as IntellectualCuriosityInfoAdapter).notifyDataSetChanged()

                                if (userType == UserType.STUDENT) {
                                    binding.rvIcmainfragmentActivities.addSwipeLeftDelete(intellectualCuriositys) {
                                        viewModel.deleteIntellectualCuriosity(intellectualCuriositys.get(it))
                                        intellectualCuriositys.removeAt(it)
                                    }
                                }

                                binding.rvIcmainfragmentActivities.visibility = View.VISIBLE
                                binding.ivIcmainfragmentEmpty.visibility =View.GONE
                                binding.tvIcmainfragmentEmpty.visibility =View.GONE
                            }else{
                                //顯示資料為空
                                binding.rvIcmainfragmentActivities.visibility = View.GONE
                                binding.ivIcmainfragmentEmpty.visibility =View.VISIBLE
                                binding.tvIcmainfragmentEmpty.visibility =View.VISIBLE
                            }
                        }
                        NetworkResult.Action.DELETE_INTELLECTUALCURIOSITY,NetworkResult.Action.PUT_INTELLECTUALCURIOSITY-> {
                            viewModel.getIntellectualCuriositycache()
                        }
                        NetworkResult.Action.POST_INTELLECTUALCURIOSITY->{
                            viewModel.getIntellectualCuriositycache()
                            //新增成功顯示 成功對話框
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }

                                viewModel.scoreAlgorithm.value?.let {
                                    addSuccessDailogFragment.intellectualCuriosity?.let {intellectualCuriosity->
                                        it.intellectualCuriosities.add(addSuccessDailogFragment.intellectualCuriosity!!)
                                    }
                                    val score = ((it.getICScorePercent()*10f)/7f).toInt()
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
            val score = ((it.getICScorePercent()*10f)/7f).toInt()
            binding.pbIcmainfragmentTest.progress = score
            binding.tvIcmainfragmentTitlePb.text = score.toString() + "/100"
        }
    }
    fun initialViewSize() {
        //Back
        binding.ivIcmainfragmentBack?.setViewSizeOrignalWH(24,24)
        binding.tvIcmainfragmentBack?.setTextSize(13)

        //Card
        binding.ivIcmainfragmentBack?.setViewSizeOrignalWH(24,24)
        binding.rlIcmainfragmentCard.setMarginTopOrignal(12)
        binding.rlIcmainfragmentCard.setViewSizeOrignalHeight(120)
        binding.ivIcmainfragmentIcon.setViewSizeOrignalWH(65,65)
        binding.rlIcmainfragmentTitle.setMarginLeftOrignal(16)
        binding.tvIcmainfragmentTitle.setTextSize(17)
        binding.tvIcmainfragmentTitleDesp.setTextSize(13)
        binding.pbIcmainfragmentTest.setViewSizeOrignalWH(155,8)
        binding.pbIcmainfragmentTest.setMarginTopOrignal(16)
        binding.tvIcmainfragmentTitlePb.setTextSize(13)
        binding.tvIcmainfragmentTitlePb.setMarginLeftOrignal(8)
        binding.tvIcmainfragmentTitlePb.setMarginTopOrignal(-10)

        //Activities
        binding.tvIcmainfragmentActivities.setTextSize(17)
        binding.tvIcmainfragmentActivities.setMarginTopOrignal(24)
        binding.ivIcmainfragmentEmpty.setViewSizeOrignalWH(160,176)
        binding.tvIcmainfragmentEmpty.setTextSize(13)
        binding.tvIcmainfragmentEmpty.setMarginTopOrignal(24)
        binding.tvIcmainfragmentAdd.setTextSize(13)
        binding.tvIcmainfragmentAdd.setMarginTopOrignal(24)
        //Resource
        binding.tvIcmainfragmentResource.setTextSize(17)
        binding.tvIcmainfragmentResource.setMarginTopOrignal(24)

        binding.tvIcmainfragmentResourceView.setTextSize(13)
        binding.tvIcmainfragmentResourceView.setMarginTopOrignal(24)


    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlIcmainfragmentAdd.gone()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlIcmainfragmentAdd.visible()
                    }
                    else -> { }
                }
            }
        }

        //Back
        binding.llIcmainfragmentBack?.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvIcmainfragmentActivities.layoutManager = linearLayoutManager
        val intellectualCuriosityInfoAdapter = IntellectualCuriosityInfoAdapter(requireContext())
        intellectualCuriosityInfoAdapter.onItemClick = {position->
            val innerUserType = viewModel.userType.value
            if (innerUserType == UserType.STUDENT) {
                activity?.supportFragmentManager?.let {
                    if (!addICActivityBSDailogFragment.isVisible){
                        addICActivityBSDailogFragment.intellectualCuriosity = intellectualCuriositys.get(position)
                        addICActivityBSDailogFragment.show(it,"AddICActivityBSDailogFragment")
                    }
                }
            }
        }
        intellectualCuriositys = ArrayList()
        intellectualCuriosityInfoAdapter.setData(intellectualCuriositys)
        binding.rvIcmainfragmentActivities.adapter = intellectualCuriosityInfoAdapter
        binding.rlIcmainfragmentAdd?.setOnClickListener(this)
        binding.rlIcmainfragmentResource?.setOnClickListener(this)
        addICActivityBSDailogFragment = AddICActivityBSDailogFragment()
        addICActivityBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_IC
            addSuccessDailogFragment.intellectualCuriosity = it
            viewModel.postIntellectualCuriosity(it)
        }
        addICActivityBSDailogFragment.editMeathod = {
            viewModel.putIntellectualCuriosity(it)
        }
    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.pbIcmainfragmentTest.visibility = View.GONE
                    binding.tvIcmainfragmentTitlePb.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun displayTite(){
        if (intellectualCuriositys.size>0){
            var size = intellectualCuriositys.size.toString()
            var hours = 0
            for (item in intellectualCuriositys){
                item.hoursPerWeek?.let { hour->
                    item.weekPerYear?.let {week->
                        if (hour !=0 && week !=0) {
                            hours = hours + (hour*week)
                        }
                    }
                }
            }
            if (size.equals("1")){
                binding.tvIcmainfragmentTitleDesp.text = getString(R.string.tv_cimainfragment_title_desp_singular,size,hours.toString())
            }else{
                binding.tvIcmainfragmentTitleDesp.text = getString(R.string.tv_cimainfragment_title_desp,size,hours.toString())
            }

        }else{
            binding.tvIcmainfragmentTitleDesp.text=getString(R.string.tv_cimainfragment_title_desp,"0","0")
        }

    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_icmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_icmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addICActivityBSDailogFragment.isVisible) {
                        addICActivityBSDailogFragment.intellectualCuriosity = null
                        addICActivityBSDailogFragment.show(it, "AddICActivityBSDailogFragment")
                    }
                }
            }
            R.id.rl_icmainfragment_resource -> {
                (requireActivity()as MainActivity).setSelecetTab(1)
            }
        }
    }
}