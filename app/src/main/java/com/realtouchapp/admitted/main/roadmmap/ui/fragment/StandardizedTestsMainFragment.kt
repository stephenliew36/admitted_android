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
import com.realtouchapp.admitted.common.algorithm.ScoreAlgorithm
import com.realtouchapp.admitted.databinding.FragmentTestmainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddSuccessDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddTestFinalBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.TestInfoAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import kotlinx.coroutines.launch
import java.util.stream.Collectors

class StandardizedTestsMainFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTestmainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainActivityViewModel by activityViewModels()
    lateinit var tests:ArrayList<Test>
    lateinit var testsParent:ArrayList<ArrayList<Test>>
    var addTestFinalBSDailogFragment = AddTestFinalBSDailogFragment()
    var addSuccessDailogFragment = AddSuccessDailogFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTestmainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initialViewSize()
        initialView()
        checkFreeVersion()
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTest()
        subscribeUi()
    }
    fun subscribeUi(){
        viewModel.responsetestTest.observe(viewLifecycleOwner, Observer { result ->

            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    when(result.action){
                         NetworkResult.Action.CACHE_TESTS,NetworkResult.Action.GET_TESTS->{

                            tests = ArrayList<Test>()
                            result.data?.let { data ->
                                data.data?.let { it ->
                                    tests!!.addAll(it as List<Test>)
                                }
                            }
                             viewModel.updateScoreAlgorithmTest(tests)
                            createTestsParent()
                            displayTite()
                            if (testsParent.size>0){
                                val userType = viewModel.userType.value

                                //顯示清單
                                
                                (binding.rvTestmainfragmentTests.adapter as TestInfoAdapter).setData(testsParent)
                                (binding.rvTestmainfragmentTests.adapter as TestInfoAdapter).notifyDataSetChanged()

                                if (userType == UserType.STUDENT) {
                                    var itemTouchHelper =binding.rvTestmainfragmentTests.addSwipeLeftDelete(testsParent) {
                                        viewModel.deleteTests(testsParent.get(it))
                                        tests.removeAt(it)
                                    }
                                    itemTouchHelper.attachToRecyclerView(null)
//                                itemTouchHelper.attachToRecyclerView(binding.rvTestmainfragmentTests)
                                    (binding.rvTestmainfragmentTests.adapter as TestInfoAdapter).isOpenShiftRemove = {  isOpen->
                                        if (isOpen){
                                            itemTouchHelper.attachToRecyclerView(binding.rvTestmainfragmentTests)
                                        }else{

                                            itemTouchHelper.attachToRecyclerView(null)
                                        }
                                    }
//                                var ItemTouchHelper =binding.rvTestmainfragmentTests.addSwipeLeftDelete(testsParent) {
//                                    viewModel.deleteTests(testsParent.get(it))
//                                    tests.removeAt(it)
//                                }
                                } else {
                                    (binding.rvTestmainfragmentTests.adapter as TestInfoAdapter).isOpenShiftRemove = { isOpen ->

                                    }
                                }

                                binding.rvTestmainfragmentTests.visibility = View.VISIBLE
                                binding.ivTestmainfragmentEmpty.visibility =View.GONE
                                binding.tvTestmainfragmentEmpty.visibility =View.GONE
                            }else{
                                //顯示資料為空
                                binding.rvTestmainfragmentTests.visibility = View.GONE
                                binding.ivTestmainfragmentEmpty.visibility =View.VISIBLE
                                binding.tvTestmainfragmentEmpty.visibility =View.VISIBLE
                            }
                        }
                        NetworkResult.Action.DELETE_TESTS->{
                            viewModel.getTestcache()
                        }
                        NetworkResult.Action.POST_TESTS,NetworkResult.Action.PUT_TESTS->{
                            viewModel.getTestcache()
                            //新增成功顯示 成功對話框
                            activity?.supportFragmentManager?.let {
                                viewModel.userInfo.value?.let {
                                    it.firstName?.let {
                                        addSuccessDailogFragment.name = it
                                    }
                                }

                                viewModel.scoreAlgorithm.value?.let {
                                    addSuccessDailogFragment.test?.let {test->
                                        it.tests.add(addSuccessDailogFragment.test!!)
                                    }
                                    addSuccessDailogFragment.score = it.getTestsScore()
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
    }
    fun initialViewSize() {
        //Back
        binding.ivTestmainfragmentBack.setViewSizeOrignalWH(24,24)
        binding.tvTestmainfragmentBack.setTextSize(13)

        //Card
        binding.ivTestmainfragmentBack.setViewSizeOrignalWH(24,24)
        binding.rlTestmainfragmentCard.setMarginTopOrignal(12)
        binding.rlTestmainfragmentCard.setViewSizeOrignalHeight(120)
        binding.ivTestmainfragmentIcon.setViewSizeOrignalWH(65,65)
        binding.rlTestmainfragmentTitle.setMarginLeftOrignal(16)
        binding.tvTestmainfragmentTitle.setTextSize(17)
        binding.tvTestmainfragmentTitleDesp.setTextSize(13)
        binding.pbTestmainfragmentTest.setViewSizeOrignalWH(155,8)
        binding.pbTestmainfragmentTest.setMarginTopOrignal(16)
        binding.tvTestmainfragmentTitlePb.setTextSize(13)
        binding.tvTestmainfragmentTitlePb.setMarginLeftOrignal(8)
        binding.tvTestmainfragmentTitlePb.setMarginTopOrignal(-10)

        //Tests Taken
        binding.tvTestmainfragmentTests.setTextSize(17)
        binding.tvTestmainfragmentTests.setMarginTopOrignal(24)
        binding.ivTestmainfragmentEmpty.setViewSizeOrignalWH(160,176)
        binding.tvTestmainfragmentEmpty.setTextSize(13)
        binding.tvTestmainfragmentEmpty.setMarginTopOrignal(24)
        binding.tvTestmainfragmentAdd.setTextSize(13)
        binding.tvTestmainfragmentAdd.setMarginTopOrignal(24)



    }

    fun initialView() {
        lifecycleScope.launch {
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        binding.rlTestmainfragmentAdd.gone()
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.rlTestmainfragmentAdd.visible()
                    }
                    else -> { }
                }
            }
        }

        //Back
        binding.llTestmainfragmentBack.setOnClickListener(this)

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvTestmainfragmentTests.layoutManager = linearLayoutManager
        val testInfoAdapter = TestInfoAdapter(requireContext())

        testInfoAdapter.onItemDelete = { test ->
            viewModel.deleteTest(test)
        }
        testInfoAdapter.onItemClick = {test->
            val innerUserType = viewModel.userType.value
            if (innerUserType == UserType.STUDENT) {
                activity?.supportFragmentManager?.let {
                    if (!addTestFinalBSDailogFragment.isVisible){
                        addTestFinalBSDailogFragment.test = test
                        addTestFinalBSDailogFragment.show(it,"AddCIActivityBSDailogFragment")
                    }
                }
            }
        }
        testsParent = ArrayList<ArrayList<Test>>()
        testInfoAdapter.setData(testsParent)
        binding.rvTestmainfragmentTests.adapter = testInfoAdapter


        binding.rlTestmainfragmentAdd.setOnClickListener(this)

        addTestFinalBSDailogFragment = AddTestFinalBSDailogFragment()
        addTestFinalBSDailogFragment.doneMeathod = {
            addSuccessDailogFragment.type = AddSuccessDailogFragment.TYPE_TEST
            addSuccessDailogFragment.test = it
            viewModel.postTest(it)
        }
        addTestFinalBSDailogFragment.editMeathod = {
            viewModel.putTest(it)
        }


    }

    private fun checkFreeVersion() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect{
                if (it){
                    binding.pbTestmainfragmentTest.visibility = View.GONE
                    binding.tvTestmainfragmentTitlePb.visibility = View.GONE
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun displayTite(){
        if (testsParent.size>0){
            var title = ""
            var arrys = ArrayList<String>()
            for (item in testsParent){
                arrys.add(item.first().title())
            }
            for (string in arrys){
                if (arrys.last().equals(string)){
                    title = title + string
                }else{
                    title = title + string +","
                }
            }
            binding.tvTestmainfragmentTitleDesp.text = title

            (requireActivity() as MainActivity).scoreAlgorithm.tests = tests
            binding.pbTestmainfragmentTest.progress = (requireActivity() as MainActivity).scoreAlgorithm.getTestsScore()
            binding.tvTestmainfragmentTitlePb.text = (requireActivity() as MainActivity).scoreAlgorithm.getTestsScore().toString() + "/100"
        }else{
            binding.tvTestmainfragmentTitleDesp.text=""
            binding.pbTestmainfragmentTest.progress = 0
            binding.tvTestmainfragmentTitlePb.text = "0/100"
        }

    }

    fun createTestsParent() {
        testsParent = ArrayList(Test.groupedAttempts(tests).map { ArrayList(it) })
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.ll_testmainfragment_back -> {
                findNavController().popBackStack()
            }
            R.id.rl_testmainfragment_add -> {
                activity?.supportFragmentManager?.let {
                    if (!addTestFinalBSDailogFragment.isVisible){
                        addTestFinalBSDailogFragment.test = null
                        addTestFinalBSDailogFragment.show(it,"addTestFinalBSDailogFragment")
                    }
            }

        }
    }
}
}
