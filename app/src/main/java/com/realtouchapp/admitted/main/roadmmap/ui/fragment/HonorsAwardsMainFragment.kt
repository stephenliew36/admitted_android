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
import com.realtouchapp.admitted.databinding.FragmentHonorsmainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddHonorsBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.HonorsInfoAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.Honors
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch

class HonorsAwardsMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHonorsmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    lateinit var honors: ArrayList<Honors>
    var addHonorsBSDailogFragment = AddHonorsBSDailogFragment()
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHonorsmainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()
        checkFreeVersion()
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getHonors()
        subscribeUi()
    }

    fun subscribeUi() {
        viewModel.responsetestHonors.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.GET_HONORS, NetworkResult.Action.CACHE_HONORS -> {
                            honors = ArrayList<Honors>()
                            result.data?.let { it ->
                                honors!!.addAll(it.data as List<Honors>)
                            }
                            viewModel.updateScoreAlgorithmHonor(honors)
                            displayTite()
                            if (honors.size>0){
                                val userType = viewModel.userType.value

                                //顯示清單
                                (binding.rvHonorsmainfragmentHonors.adapter as HonorsInfoAdapter).setData(honors)
                                (binding.rvHonorsmainfragmentHonors.adapter as HonorsInfoAdapter).notifyDataSetChanged()

                                if (userType == UserType.STUDENT) {
                                    binding.rvHonorsmainfragmentHonors.addSwipeLeftDelete(honors) {
                                        viewModel.deleteHonors(honors.get(it))
                                        honors.removeAt(it)
                                    }
                                }

                                binding.rvHonorsmainfragmentHonors.visibility = View.VISIBLE
                                binding.ivHonorsmainfragmentEmpty.visibility =View.GONE
                                binding.tvHonorsmainfragmentEmpty.visibility =View.GONE
                            }else{
                                //顯示資料為空
                                binding.rvHonorsmainfragmentHonors.visibility = View.GONE
                                binding.ivHonorsmainfragmentEmpty.visibility =View.VISIBLE
                                binding.tvHonorsmainfragmentEmpty.visibility =View.VISIBLE
                            }
                        }
                        NetworkResult.Action.DELETE_HONORS->{
                            viewModel.getHonorscache()
                        }
                        NetworkResult.Action.POST_HONORS,NetworkResult.Action.PUT_HONORS->{
                            viewModel.getHonorscache()
                            //新增成功顯示 成功對話框
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }
                                viewModel.scoreAlgorithm.value?.let {
                                    addSuccessDailogFragment.honors?.let {honors->
                                        it.honors.add(addSuccessDailogFragment.honors!!)
                                    }
                                    addSuccessDailogFragment.score = it.getHonroScorePercent()
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
            binding.pbHonorsmainfragmentTest.progress = it.getHonroScorePercent()
            binding.tvHonorsmainfragmentTitlePb.text = it.getHonroScorePercent().toString() + "/100"
        }
    }

    fun initialViewSize() {
        //Back
        binding.ivHonorsmainfragmentBack.setViewSizeOrignalWH(24, 24)
        binding.tvHonorsmainfragmentBack.setTextSize(13)

        //Card
        binding.ivHonorsmainfragmentBack.setViewSizeOrignalWH(24, 24)
        binding.rlHonorsmainfragmentCard.setMarginTopOrignal(12)
        binding.rlHonorsmainfragmentCard.setViewSizeOrignalHeight(120)
        binding.ivHonorsmainfragmentIcon.setViewSizeOrignalWH(65, 65)
        binding.rlHonorsmainfragmentTitle.setMarginLeftOrignal(16)
        binding.tvHonorsmainfragmentTitle.setTextSize(17)
        binding.tvHonorsmainfragmentTitleDesp.setTextSize(13)
        binding.pbHonorsmainfragmentTest.setViewSizeOrignalWH(155, 8)
        binding.pbHonorsmainfragmentTest.setMarginTopOrignal(16)
        binding.tvHonorsmainfragmentTitlePb.setTextSize(13)
        binding.tvHonorsmainfragmentTitlePb.setMarginLeftOrignal(8)
        binding.tvHonorsmainfragmentTitlePb.setMarginTopOrignal(-10)

        //Tests Taken
        binding.tvHonorsmainfragmentHonors.setTextSize(17)
        binding.tvHonorsmainfragmentHonors.setMarginTopOrignal(24)
        binding.ivHonorsmainfragmentEmpty.setViewSizeOrignalWH(160, 176)
        binding.tvHonorsmainfragmentEmpty.setTextSize(13)
        binding.tvHonorsmainfragmentEmpty.setMarginTopOrignal(24)
        binding.tvHonorsmainfragmentAdd.setTextSize(13)
        binding.tvHonorsmainfragmentAdd.setMarginTopOrignal(24)


    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlHonorsmainfragmentAdd.gone()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlHonorsmainfragmentAdd.visible()
                    }
                    else -> { }
                }
            }
        }

        //Back
        binding.llHonorsmainfragmentBack?.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvHonorsmainfragmentHonors.layoutManager = linearLayoutManager
        val honorsInfoAdapter = HonorsInfoAdapter(requireContext())
        honorsInfoAdapter.onItemClick = {position->
            val innerUserType = viewModel.userType.value
            if (innerUserType == UserType.STUDENT) {
                activity?.supportFragmentManager?.let {
                    if (!addHonorsBSDailogFragment.isVisible){
                        addHonorsBSDailogFragment.honors = honors.get(position)
                        addHonorsBSDailogFragment.show(it,"AddHonorsBSDailogFragment")
                    }
                }
            }
        }

        honors = ArrayList()
        honorsInfoAdapter.setData(honors)
        binding.rvHonorsmainfragmentHonors.adapter = honorsInfoAdapter
        binding.rlHonorsmainfragmentAdd?.setOnClickListener(this)
        addHonorsBSDailogFragment = AddHonorsBSDailogFragment()
        addHonorsBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_HONORS
            addSuccessDailogFragment.honors = it
            viewModel.postHonors(it)
        }
        addHonorsBSDailogFragment.editMeathod = {
            viewModel.putHonors(it)
        }

    }
    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.pbHonorsmainfragmentTest.visibility = View.GONE
                    binding.tvHonorsmainfragmentTitlePb.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayTite() {
        if (honors.size > 0) {
            var size = honors.size.toString()
            if (size.equals("1")) {
                binding.tvHonorsmainfragmentTitleDesp.text =
                    getString(R.string.tv_honorsmainfragment_title_desp_singular, size)
            } else {
                binding.tvHonorsmainfragmentTitleDesp.text =
                    getString(R.string.tv_honorsmainfragment_title_desp, size)
            }
        } else {
            binding.tvHonorsmainfragmentTitleDesp.text = ""
            binding.pbHonorsmainfragmentTest.progress = 0
            binding.tvHonorsmainfragmentTitlePb.text = "0/100"
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_honorsmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_honorsmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addHonorsBSDailogFragment.isVisible) {
                        addHonorsBSDailogFragment.honors = null
                        addHonorsBSDailogFragment.show(it, "AddHonorsBSDailogFragment")
                    }
                }

            }
        }
    }


}