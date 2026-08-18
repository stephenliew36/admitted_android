package com.realtouchapp.admitted.member.view.activity

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.databinding.ActivityBasicinfoBinding
import com.realtouchapp.admitted.ext.*
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.AddTestBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.SchoolSelectBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.InterestSelectBSDailogFragment
import com.realtouchapp.admitted.main.roadmmap.ui.dialogfragment.LanguageSelectBSDailogFragment
import com.realtouchapp.admitted.member.view.activity.model.BasicInfoActivityViewModel
import com.realtouchapp.admitted.member.view.adapter.InterestSelectRecycleViewAdapter
import com.realtouchapp.admitted.member.view.adapter.SpinnerArrayAdapter
import com.realtouchapp.admitted.member.view.adapter.StandTestRecyleViewAdapter
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.*
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.storage.datastore.AdmittedDataStore
import com.realtouchapp.tools.ext.setMarginTopRatioHWithoutBar
import com.realtouchapp.tools.ext.setSpinner
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class BasicInfoActivity : AppCompatActivity(), OnClickListener {
    companion object {
        fun Intent(activity: AppCompatActivity) {
            val intent = android.content.Intent().setClass(activity, BasicInfoActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val viewModel by viewModels<BasicInfoActivityViewModel>()
    private lateinit var binding: ActivityBasicinfoBinding
    var userInfo = UserInfo()
    var academicYears = ArrayList<AcademicYear>()
    var areaOfInterest = AreaOfInterest()
    var targetSchools = ArrayList<TargetSchool>()
    var userTargetSchools = ArrayList<UserTargetSchool>()
    var selectSchools = HashMap<String, String>()
    var successTimes = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        initialView()
        subscribeUi()
        viewModel.cacheTargetSchoolDetail()
        testAction {
//            displayBarStauts(BasInfoStatus.PREFERENCE.ordinal)
        }
    }


    private fun initialView() {
        binding.ivBasicinfoBack.setOnClickListener(this)

//        initialLoadingDiloagFragment()
        initialViewBasicInfo()
        initialViewSchool()
        initialViewInterest()
        initialViewTest()
    }

    private fun subscribeUi() {
        viewModel.response.observe(this, Observer { result ->
            when (result.status) {

                NetworkResult.Status.SUCCESS -> {

                    //成功
                    when (result.action) {
                        NetworkResult.Action.CACHETARGETSCHHOOL_DETAIL_LIST -> {
                            result.data?.let { it ->
                                targetSchools = it.data as ArrayList<TargetSchool>
                                schoolSelectBSDailogFragment.targetSchools = it.data as ArrayList<TargetSchool>
                            }
                        }
                        NetworkResult.Action.PUT_USERTARGETSCHHOOL -> {
                            successTimes = successTimes + 1
                        }
                        NetworkResult.Action.POST_TESTS -> {
                            successTimes = successTimes + 1
                        }
                        NetworkResult.Action.PUT_USERBASIC -> {
                            successTimes = successTimes + 1
                        }
                        NetworkResult.Action.PUT_ACADEMICYEAR -> {
                            successTimes = successTimes + 1
                        }
                        else -> {
                        }
                    }
                    if (userTargetSchools.size > 0) {
                        if (successTimes == (1 + 1 + academicYears.size + tests.size)) {
                            //用來記錄 全部的POST 結束跳轉下一頁
//                        if (loadingDialogFragment.isVisible){
//                            loadingDialogFragment.dismiss()
//                        }

                            SplashActivity.Intent(this)
                        }
                    } else {
                        if (successTimes == (1 + academicYears.size + tests.size)) {
                            //用來記錄 全部的POST 結束跳轉下一頁
//                        if (loadingDialogFragment.isVisible){
//                            loadingDialogFragment.dismiss()
//                        }
                            SplashActivity.Intent(this)
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
//                    if (loadingDialogFragment.isVisible){
//                        loadingDialogFragment.dismiss()
//                    }
                }

                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
//                    if (!loadingDialogFragment.isVisible){
//                        loadingDialogFragment.showOnce(supportFragmentManager)
//                    }
                }
            }

        })
    }

    private lateinit var languageSelectBSDailogFragment: LanguageSelectBSDailogFragment

    private fun initialViewBasicInfo() {
        //國籍

        setSpinner(binding.vBasicinfo1.tvBasicinfo1National, binding.vBasicinfo1.spBasicinfo1National, R.array.nationalities_array) {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_NATIONALITY, binding.vBasicinfo1.tvBasicinfo1National.text.toString())
            }
        }

        binding.vBasicinfo1.tvBasicinfo1National.addTextChangedListener { checkEnableBasicInfoNext() }


        //語言
        binding.vBasicinfo1.tvBasicinfo1Language.setOnClickListener(this)
        binding.vBasicinfo1.tvBasicinfo1Language.addTextChangedListener { checkEnableBasicInfoNext() }
        languageSelectBSDailogFragment = LanguageSelectBSDailogFragment()
        languageSelectBSDailogFragment.onDone = {
            if (it) {
                binding.vBasicinfo1.tvBasicinfo1Language.text = "Yes"
                userInfo.isRequireLanguageExam = "1"
            } else {
                binding.vBasicinfo1.tvBasicinfo1Language.text = "No"
                userInfo.isRequireLanguageExam = "0"
            }
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_LANGUAGE, binding.vBasicinfo1.tvBasicinfo1Language.text.toString())
            }
        }
        if (binding.vBasicinfo1.tvBasicinfo1Language.text.isNullOrBlank()) {
            binding.vBasicinfo1.tvBasicinfo1Language.text = "No"
            userInfo.isRequireLanguageExam = "0"
        }


        //學校
        binding.vBasicinfo1.etBasicinfo1School.addTextChangedListener { checkEnableBasicInfoNext() }
        binding.vBasicinfo1.etBasicinfo1School.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 文本改变之前
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 文本改变时
            }

            override fun afterTextChanged(s: Editable?) {
                // 文本改变之后
                runBlocking {
                    //儲存選擇
                    viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_SCHOOL, s.toString())
                }
            }
        })


        //年級
        setGradSpinner(binding.vBasicinfo1.tvBasicinfo1Grade, binding.vBasicinfo1.spBasicinfo1Grade, R.array.grade_levels)
        binding.vBasicinfo1.tvBasicinfo1Grade.addTextChangedListener { checkEnableBasicInfoNext() }
        //9年級GPA
        setSpinner(binding.vBasicinfo1.tvBasicinfo1Gpa9, binding.vBasicinfo1.spBasicinfo1Gpa9, R.array.gpaPoint) {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_GRADE9, binding.vBasicinfo1.tvBasicinfo1Gpa9.text.toString())
            }
        }
        //10年級GPA
        setSpinner(binding.vBasicinfo1.tvBasicinfo1Gpa10, binding.vBasicinfo1.spBasicinfo1Gpa10, R.array.gpaPoint) {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_GRADE10, binding.vBasicinfo1.tvBasicinfo1Gpa10.text.toString())
            }
        }
        //11年級GPA
        setSpinner(binding.vBasicinfo1.tvBasicinfo1Gpa11, binding.vBasicinfo1.spBasicinfo1Gpa11, R.array.gpaPoint) {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_GRADE11, binding.vBasicinfo1.tvBasicinfo1Gpa11.text.toString())
            }
        }
        //12年級GPA
        setSpinner(binding.vBasicinfo1.tvBasicinfo1Gpa12, binding.vBasicinfo1.spBasicinfo1Gpa12, R.array.gpaPoint)
        runBlocking {
            //快取上次選擇
            //國籍
            binding.vBasicinfo1.tvBasicinfo1National.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_NATIONALITY).first()
            //語言
            binding.vBasicinfo1.tvBasicinfo1Language.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_LANGUAGE).first()
            if (binding.vBasicinfo1.tvBasicinfo1Language.text.equals("Yes")) {
                userInfo.isRequireLanguageExam = "1"
            } else {
                binding.vBasicinfo1.tvBasicinfo1Language.text = "No"
                userInfo.isRequireLanguageExam = "0"
            }
            //學校
            binding.vBasicinfo1.etBasicinfo1School.setText(viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_SCHOOL).first())
            //年級

            val grade = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_GRADE).first()
            if (grade.isNotEmpty()) {
                binding.vBasicinfo1.spBasicinfo1Grade.setSelection(grade.toInt())
            }

            //9年級GPA
            binding.vBasicinfo1.tvBasicinfo1Gpa9.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_GRADE9).first()
            //10年級GPA
            binding.vBasicinfo1.tvBasicinfo1Gpa10.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_GRADE10).first()
            //11年級GPA
            binding.vBasicinfo1.tvBasicinfo1Gpa11.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_GRADE11).first()

            //如果達成跳建自動跳轉往下
//            checkEnableBasicInfoNext()
            if (!(binding.vBasicinfo1.tvBasicinfo1National.text.isNullOrEmpty() || binding.vBasicinfo1.etBasicinfo1School.text.isNullOrEmpty())) {
                binding.vBasicinfo1.btnBasicinfo1Next.setOnClickListener(this@BasicInfoActivity)
                binding.vBasicinfo1.btnBasicinfo1Next.callOnClick()
            }


        }
    }

    private fun checkEnableBasicInfoNext() {
        if (binding.vBasicinfo1.tvBasicinfo1National.text.isNullOrEmpty() || binding.vBasicinfo1.etBasicinfo1School.text.isNullOrEmpty()) {
            enableBasicInfoNext(false)
        } else {
            enableBasicInfoNext(true)
        }
    }

    private fun enableBasicInfoNext(isEnable: Boolean) {
        if (isEnable) {
            binding.vBasicinfo1.btnBasicinfo1Next.setBackgroundResource(R.drawable.selector_btn_default)
            binding.vBasicinfo1.btnBasicinfo1Next.setOnClickListener(this)
        } else {
            binding.vBasicinfo1.btnBasicinfo1Next.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.vBasicinfo1.btnBasicinfo1Next.setOnClickListener(null)
        }
    }


    private fun initialViewSchool() {
        //School System
        setSpinner(binding.vBasicinfo2.tvBasicinfo2Schoolsystem, binding.vBasicinfo2.spBasicinfo2Schoolsystem, R.array.array_school_system, {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_SCHOOL_SYSTEM, binding.vBasicinfo2.tvBasicinfo2Schoolsystem.text.toString())
            }
        })
        binding.vBasicinfo2.tvBasicinfo2Schoolsystem.addTextChangedListener { checkEnableSchoolNext() }

        //GradingScale
        setSpinner(binding.vBasicinfo2.tvBasicinfo2Gradingscale, binding.vBasicinfo2.spBasicinfo2Gradingscale, R.array.array_gradingscale, {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_GRADING_SCALE, binding.vBasicinfo2.tvBasicinfo2Gradingscale.text.toString())
            }
        })
        binding.vBasicinfo2.tvBasicinfo2Gradingscale.addTextChangedListener { checkEnableSchoolNext() }
        //Schedule
        setSpinner(binding.vBasicinfo2.tvBasicinfo2Schedule, binding.vBasicinfo2.spBasicinfo2Schedule, R.array.array_schedule, {
            runBlocking {
                //儲存選擇
                viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_SCHEDULE, binding.vBasicinfo2.tvBasicinfo2Schedule.text.toString())
            }
        })
        binding.vBasicinfo2.tvBasicinfo2Schedule.addTextChangedListener { checkEnableSchoolNext() }

        runBlocking {
            //快取上次選擇
            //School System
            binding.vBasicinfo2.tvBasicinfo2Schoolsystem.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_SCHOOL_SYSTEM).first()
            //GradingScale
            binding.vBasicinfo2.tvBasicinfo2Gradingscale.text = viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_GRADING_SCALE).first()
            //Schedule
            binding.vBasicinfo2.tvBasicinfo2Schedule.text =
                viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_SCHEDULE).first()
                    .ifBlank { resources.getStringArray(R.array.array_schedule).first() }

            checkEnableSchoolNext()
            binding.vBasicinfo2.btnBasicinfo2Next.callOnClick()
        }
    }

    private fun checkEnableSchoolNext() {
        if (binding.vBasicinfo2.tvBasicinfo2Schoolsystem.text.isNullOrEmpty() || binding.vBasicinfo2.tvBasicinfo2Gradingscale.text.isNullOrEmpty()) {
            enableSchoolNext(false)
        } else {
            enableSchoolNext(true)
        }
    }

    private fun enableSchoolNext(isEnable: Boolean) {
        if (isEnable) {
            binding.vBasicinfo2.btnBasicinfo2Next.setBackgroundResource(R.drawable.selector_btn_default)
            binding.vBasicinfo2.btnBasicinfo2Next.setOnClickListener(this)
        } else {
            binding.vBasicinfo2.btnBasicinfo2Next.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.vBasicinfo2.btnBasicinfo2Next.setOnClickListener(null)
        }
    }

    private lateinit var interestSelectBSDailogFragment: InterestSelectBSDailogFragment
    private lateinit var schoolSelectBSDailogFragment: SchoolSelectBSDailogFragment
    private fun initialViewInterest() {
        binding.vBasicinfo3.btnBasicinfo3Addinterest.setMarginTopRatioHWithoutBar(.014f)
//        binding.vBasicinfo3.rvBasicinfo3Addinterest.setMarginTopRatioHWithoutBar(.014f)
        binding.vBasicinfo3.tvBasicinfo3Targetschool.setMarginTopRatioHWithoutBar(.031f)
        binding.vBasicinfo3.btnBasicinfo3Addtargetschool.setMarginTopRatioHWithoutBar(.014f)
        binding.vBasicinfo3.tvBasicinfo3Descrip2.setMarginTopRatioHWithoutBar(.031f)
        binding.vBasicinfo3.btnBasicinfo3Next.setMarginTopRatioHWithoutBar(.014f)
        binding.vBasicinfo3.btnBasicinfo3Skip.setMarginTopRatioHWithoutBar(.014f)

        binding.vBasicinfo3.btnBasicinfo3Addinterest.setOnClickListener(this)
        binding.vBasicinfo3.btnBasicinfo3Addtargetschool.setOnClickListener(this)
        binding.vBasicinfo3.btnBasicinfo3Next.setOnClickListener(this)
        binding.vBasicinfo3.btnBasicinfo3Skip.setOnClickListener(this)


        val linearLayoutManager = LinearLayoutManager(this)

        binding.vBasicinfo3.rvBasicinfo3Addinterest.layoutManager = linearLayoutManager
        val interestSelectRecycleViewAdapter = InterestSelectRecycleViewAdapter(this)
        interestSelectRecycleViewAdapter.onItemDeleted = {
            areaOfInterest.setChecked(this, false, it)
            runBlocking {
                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_INTEREST, areaOfInterest)
            }
            checkEnableInterstlNext()
        }
        interestSelectRecycleViewAdapter.setData(areaOfInterest.getCheckedList(this))
        binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter = interestSelectRecycleViewAdapter
        binding.vBasicinfo3.btnBasicinfo3Addinterest.setOnClickListener(this)

        (binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter as InterestSelectRecycleViewAdapter)
        interestSelectBSDailogFragment = InterestSelectBSDailogFragment()
        interestSelectBSDailogFragment.areaOfInterest = areaOfInterest

        interestSelectBSDailogFragment.onDone = {

            areaOfInterest = it
            //設定已選中的興趣 刷新
            (binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter as InterestSelectRecycleViewAdapter).setData(areaOfInterest.getCheckedList(this))
            runBlocking {
                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_INTEREST, areaOfInterest)
            }
            checkEnableInterstlNext()
        }

        schoolSelectBSDailogFragment = SchoolSelectBSDailogFragment()
        schoolSelectBSDailogFragment.unitedStatesOnly = true
        schoolSelectBSDailogFragment.doneMeathod = {
            selectSchools = HashMap(schoolSelectBSDailogFragment.selectetMap)
            val list = ArrayList<String>()
            selectSchools.values.forEach {
                list.add(it)
            }
            (binding.vBasicinfo3.rvBasicinfo3Targetschool.adapter as InterestSelectRecycleViewAdapter).setData(list)
            runBlocking {
                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_TARGET_SCHOOL, selectSchools)
            }
            checkEnableInterstlNext()
        }

        val linearLayoutManager2 = LinearLayoutManager(this)
        binding.vBasicinfo3.rvBasicinfo3Targetschool.layoutManager = linearLayoutManager2
        val adapter = InterestSelectRecycleViewAdapter(this)
        adapter.onItemDeleted = {
            for (key in selectSchools.keys) {
                if (selectSchools.get(key).equals(it)) {
                    selectSchools.remove(key)
                    break
                }
            }
            runBlocking {
                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_TARGET_SCHOOL, selectSchools)
            }
            checkEnableInterstlNext()
        }
        binding.vBasicinfo3.rvBasicinfo3Targetschool.adapter = adapter
        binding.vBasicinfo3.btnBasicinfo3Addtargetschool.setOnClickListener(this)
        checkEnableInterstlNext()

        runBlocking {
            //上一次輸入的
            viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_INTEREST).first()?.let {
                if (it.isNotEmpty()) {
                    areaOfInterest = Gson().fromJson(it, AreaOfInterest::class.java)
                    (binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter as InterestSelectRecycleViewAdapter).setData(areaOfInterest.getCheckedList(this@BasicInfoActivity))
                }

            }
            viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_TARGET_SCHOOL).first()?.let {
                if (it.isNotEmpty()) {
                    val typeToken = object : TypeToken<HashMap<String, String>>() {}.type
                    selectSchools = Gson().fromJson(it, typeToken)
                    val list = ArrayList<String>()
                    selectSchools.values.forEach {
                        list.add(it)
                    }
                    (binding.vBasicinfo3.rvBasicinfo3Targetschool.adapter as InterestSelectRecycleViewAdapter).setData(list)
                }

            }
            var adapterSize = (binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter as InterestSelectRecycleViewAdapter).dataList.size
            adapterSize = adapterSize + (binding.vBasicinfo3.rvBasicinfo3Targetschool.adapter as InterestSelectRecycleViewAdapter).dataList.size
            if (adapterSize >0) {
                enableInterestNext(true)
                binding.vBasicinfo3.btnBasicinfo3Next.callOnClick()
            }
        }
    }

    private fun checkEnableInterstlNext() {
        var adapterSize = (binding.vBasicinfo3.rvBasicinfo3Addinterest.adapter as InterestSelectRecycleViewAdapter).dataList.size
        adapterSize = adapterSize + (binding.vBasicinfo3.rvBasicinfo3Targetschool.adapter as InterestSelectRecycleViewAdapter).dataList.size
        if (adapterSize > 0) {
            enableInterestNext(true)
        } else {
            enableInterestNext(false)
        }
    }

    private fun enableInterestNext(isEnable: Boolean) {
        if (isEnable) {
            binding.vBasicinfo3.btnBasicinfo3Next.setBackgroundResource(R.drawable.selector_btn_default)
            binding.vBasicinfo3.btnBasicinfo3Next.setOnClickListener(this)
            binding.vBasicinfo3.btnBasicinfo3Skip.visibility = View.INVISIBLE
        } else {
            binding.vBasicinfo3.btnBasicinfo3Next.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.vBasicinfo3.btnBasicinfo3Next.setOnClickListener(null)
            binding.vBasicinfo3.btnBasicinfo3Skip.visibility = View.VISIBLE
        }
    }

    lateinit var addTestBSDailogFragment: AddTestBSDailogFragment
    lateinit var editTestBSDailogFragment: AddTestBSDailogFragment
    var tests = ArrayList<Test>()
    private fun initialViewTest() {
        binding.vBasicinfo4.btnBasicinfo4Addtest.setMarginTopRatioHWithoutBar(.014f)
        binding.vBasicinfo4.tvBasicinfo4Descrip2.setMarginTopRatioHWithoutBar(.031f)
        binding.vBasicinfo4.btnBasicinfo4Next.setMarginTopRatioHWithoutBar(.014f)
        binding.vBasicinfo4.btnBasicinfo4Skip.setMarginTopRatioHWithoutBar(.014f)

        binding.vBasicinfo4.btnBasicinfo4Addtest.setOnClickListener(this)
        binding.vBasicinfo4.btnBasicinfo4Next.setOnClickListener(this)
        binding.vBasicinfo4.btnBasicinfo4Skip.setOnClickListener(this)


        val linearLayoutManager = LinearLayoutManager(this)

        binding.vBasicinfo4.rvBasicinfo4Addinterest.layoutManager = linearLayoutManager
        val standTestRecyleViewAdapter = StandTestRecyleViewAdapter(this)
        standTestRecyleViewAdapter.onItenDeleted = {
            tests.removeAt(it)
            checkEnableTestNext()

        }
        standTestRecyleViewAdapter.setData(tests)
        binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter = standTestRecyleViewAdapter
        addTestBSDailogFragment = initialAddTestBSDailogFragment()
        editTestBSDailogFragment = initialAddTestBSDailogFragment()

        standTestRecyleViewAdapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<Test> {
            override fun onItemClick(item: Test, position: Int) {
                if (!editTestBSDailogFragment.isVisible) {
                    editTestBSDailogFragment = AddTestBSDailogFragment()
                    editTestBSDailogFragment.editTest = item
                    editTestBSDailogFragment.doneMeathod = {
                        tests.set(position, it)
                        binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter!!.notifyItemChanged(position)
                        runBlocking {
                            viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_TESTLIST, tests)
                        }
                    }
                    editTestBSDailogFragment.show(supportFragmentManager, "initialEditTestBSDailogFragment")
                }
            }
        })
        runBlocking {
            //上一次輸入的
            viewModel.getValue(AdmittedDataStore.KEY_BASIC_INFO_TESTLIST).first()?.let {
                if (it.isNotEmpty()) {
                    val typeToken = object : TypeToken<ArrayList<Test>>() {}.type
                    tests = Gson().fromJson(it, typeToken)
                    (binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter!! as StandTestRecyleViewAdapter).dataList = tests
                    (binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter!! as StandTestRecyleViewAdapter).notifyDataSetChanged()
                }
            }
        }
        checkEnableTestNext()
    }

    private fun initialAddTestBSDailogFragment(): AddTestBSDailogFragment {
        var addTestBSDailogFragment = AddTestBSDailogFragment()
        addTestBSDailogFragment.doneMeathod = {
            tests.add(it.copy())
            binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter!!.notifyItemChanged(tests.size)
            runBlocking {
                viewModel.saveDataClass(AdmittedDataStore.KEY_BASIC_INFO_TESTLIST, tests)
            }
            checkEnableTestNext()
        }
        return addTestBSDailogFragment
    }

    private fun checkEnableTestNext() {
        val adapterSize = (binding.vBasicinfo4.rvBasicinfo4Addinterest.adapter as StandTestRecyleViewAdapter).dataList.size
        if (adapterSize > 0) {
            enableTestNext(true)
        } else {
            enableTestNext(false)
        }
    }

    private fun enableTestNext(isEnable: Boolean) {
        if (isEnable) {
            binding.vBasicinfo4.btnBasicinfo4Next.setBackgroundResource(R.drawable.selector_btn_default)
            binding.vBasicinfo4.btnBasicinfo4Next.setOnClickListener(this)
            binding.vBasicinfo4.btnBasicinfo4Skip.visibility = View.INVISIBLE
        } else {
            binding.vBasicinfo4.btnBasicinfo4Next.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.vBasicinfo4.btnBasicinfo4Next.setOnClickListener(null)
            binding.vBasicinfo4.btnBasicinfo4Skip.visibility = View.VISIBLE
        }
    }

    private fun setGradSpinner(tv: AppCompatTextView, sp: AppCompatSpinner, arrayRes: Int) {

        var tmppArrayList = ArrayList<String>()
        tmppArrayList.add("")
        tmppArrayList.addAll(resources.getStringArray(arrayRes))
        val spinnerArrayAdapter = SpinnerArrayAdapter(this, tmppArrayList.toList())
        sp.adapter = spinnerArrayAdapter

        sp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long,
            ) {
                tv.text = tmppArrayList[pos]
                runBlocking {
                    viewModel.saveKey(AdmittedDataStore.KEY_BASIC_INFO_GRADE, pos.toString())
                }
                closeSoftIntPut(tv)
                when (pos) {
                    5 -> {

                        binding.vBasicinfo1.gpBasicinfo1Gpa9.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa10.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa11.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa12.visibility = View.VISIBLE
                    }
                    4 -> {
                        binding.vBasicinfo1.gpBasicinfo1Gpa9.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa10.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa11.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa12.visibility = View.GONE
                        //紀錄目前年級
                        userInfo.grade = 12
                    }
                    3 -> {
                        binding.vBasicinfo1.gpBasicinfo1Gpa9.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa10.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa11.visibility = View.GONE
                        binding.vBasicinfo1.gpBasicinfo1Gpa12.visibility = View.GONE
                        //紀錄目前年級
                        userInfo.grade = 11
                    }
                    2 -> {
                        binding.vBasicinfo1.gpBasicinfo1Gpa9.visibility = View.VISIBLE
                        binding.vBasicinfo1.gpBasicinfo1Gpa10.visibility = View.GONE
                        binding.vBasicinfo1.gpBasicinfo1Gpa11.visibility = View.GONE
                        binding.vBasicinfo1.gpBasicinfo1Gpa12.visibility = View.GONE
                        //紀錄目前年級
                        userInfo.grade = 10
                    }
                    1 -> {
                        //紀錄目前年級
                        userInfo.grade = 9
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                closeSoftIntPut(tv)
            }
        }
        sp.dropDownVerticalOffset = UserInterfaceTool().getScreenHeightWithoutBarByRatio(this, 0.066f)
        tv.text = ""
    }
    //    var loadingDialogFragment = LoadingDialogFragment()
//    private fun initialLoadingDiloagFragment(){
//        loadingDialogFragment = LoadingDialogFragment()
//        loadingDialogFragment.onDismiss={
//        }
//        loadingDialogFragment.sure={
//            loadingDialogFragment.dismiss()
//        }
//    }
    //顯示上方Bar的進度
    private fun displayBarStauts(stauts: Int) {
        currentPage = stauts
        when (stauts) {
            BasInfoStatus.BASICINFO.ordinal -> {
                binding.ivBasicinfoBack.visibility = View.INVISIBLE
                binding.ivBasicinfoDot1.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot2.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoDot3.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoDot4.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoLine1.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine2.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine3.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.vBasicinfo1.root.visibility = View.VISIBLE
                binding.vBasicinfo2.root.visibility = View.GONE
                binding.vBasicinfo3.root.visibility = View.GONE
                binding.vBasicinfo4.root.visibility = View.GONE
            }
            BasInfoStatus.SCHOOL.ordinal -> {
                binding.ivBasicinfoBack.visibility = View.VISIBLE
                binding.ivBasicinfoDot1.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot2.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot3.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoDot4.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoLine1.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine2.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine3.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.vBasicinfo1.root.visibility = View.GONE
                binding.vBasicinfo2.root.visibility = View.VISIBLE
                binding.vBasicinfo3.root.visibility = View.GONE
                binding.vBasicinfo4.root.visibility = View.GONE
            }
            BasInfoStatus.PREFERENCE.ordinal -> {
                binding.ivBasicinfoBack.visibility = View.VISIBLE
                binding.ivBasicinfoDot1.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot2.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot3.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot4.setImageResource(R.drawable.vd_dot_default)
                binding.ivBasicinfoLine1.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine2.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine3.setColorFilter(Color.parseColor("#D6DBE3"), PorterDuff.Mode.SRC_IN)
                binding.vBasicinfo1.root.visibility = View.GONE
                binding.vBasicinfo2.root.visibility = View.GONE
                binding.vBasicinfo3.root.visibility = View.VISIBLE
                binding.vBasicinfo4.root.visibility = View.GONE
            }
            BasInfoStatus.TEST.ordinal -> {
                binding.ivBasicinfoBack.visibility = View.VISIBLE
                binding.ivBasicinfoDot1.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot2.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot3.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoDot4.setImageResource(R.drawable.vd_dot_enable)
                binding.ivBasicinfoLine1.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine2.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.ivBasicinfoLine3.setColorFilter(Color.parseColor("#3F5D95"), PorterDuff.Mode.SRC_IN)
                binding.vBasicinfo1.root.visibility = View.GONE
                binding.vBasicinfo2.root.visibility = View.GONE
                binding.vBasicinfo3.root.visibility = View.GONE
                binding.vBasicinfo4.root.visibility = View.VISIBLE
            }

        }
    }

    enum class BasInfoStatus {
        BASICINFO,
        SCHOOL,
        PREFERENCE,
        TEST
    }

    var currentPage = 0;

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_basicinfo_back -> {
                when (currentPage) {
                    BasInfoStatus.SCHOOL.ordinal -> {
                        displayBarStauts(BasInfoStatus.BASICINFO.ordinal)
                    }
                    BasInfoStatus.PREFERENCE.ordinal -> {
                        displayBarStauts(BasInfoStatus.SCHOOL.ordinal)
                    }
                    BasInfoStatus.TEST.ordinal -> {
                        displayBarStauts(BasInfoStatus.PREFERENCE.ordinal)
                    }
                }
            }
            R.id.tv_basicinfo1_language -> {
                if (!languageSelectBSDailogFragment.isVisible) {
                    languageSelectBSDailogFragment.show(supportFragmentManager, LanguageSelectBSDailogFragment.TAG)
                }
            }
            R.id.btn_basicinfo1_next -> {
                //站存國籍 學校 學年資料 和每年GPA
                userInfo.nationality = binding.vBasicinfo1.tvBasicinfo1National.text.toString()
                userInfo.school = binding.vBasicinfo1.etBasicinfo1School.text.toString()

                academicYears = ArrayList<AcademicYear>()
                if (binding.vBasicinfo1.gpBasicinfo1Gpa9.visibility == View.VISIBLE) {
                    if (binding.vBasicinfo1.tvBasicinfo1Gpa9.text.isNotEmpty()) {
                        academicYears.add(AcademicYear("9", grade = binding.vBasicinfo1.tvBasicinfo1Gpa9.text.toString()))
                    }
                }

                if (binding.vBasicinfo1.gpBasicinfo1Gpa10.visibility == View.VISIBLE) {
                    if (binding.vBasicinfo1.tvBasicinfo1Gpa10.text.isNotEmpty()) {
                        academicYears.add(AcademicYear("10", grade = binding.vBasicinfo1.tvBasicinfo1Gpa10.text.toString()))
                    }
                }

                if (binding.vBasicinfo1.gpBasicinfo1Gpa11.visibility == View.VISIBLE) {
                    if (binding.vBasicinfo1.tvBasicinfo1Gpa11.text.isNotEmpty()) {
                        academicYears.add(AcademicYear("11", grade = binding.vBasicinfo1.tvBasicinfo1Gpa11.text.toString()))
                    }
                }

                if (binding.vBasicinfo1.gpBasicinfo1Gpa12.visibility == View.VISIBLE) {
                    if (binding.vBasicinfo1.tvBasicinfo1Gpa12.text.isNotEmpty()) {
                        academicYears.add(AcademicYear("12", grade = binding.vBasicinfo1.tvBasicinfo1Gpa12.text.toString()))
                    }
                }
                displayBarStauts(BasInfoStatus.SCHOOL.ordinal)
            }
            R.id.btn_basicinfo2_next -> {
                //暫存 學校系統 計分系統 跟 學期
                userInfo.schoolSystem = resources.getStringArray(R.array.array_school_system).indexOf(binding.vBasicinfo2.tvBasicinfo2Schoolsystem.text.toString())
                userInfo.gradingScale = resources.getStringArray(R.array.array_gradingscale).indexOf(binding.vBasicinfo2.tvBasicinfo2Gradingscale.text.toString())
                userInfo.schedule = resources.getStringArray(R.array.array_schedule).indexOf(binding.vBasicinfo2.tvBasicinfo2Schedule.text.toString())


                displayBarStauts(BasInfoStatus.PREFERENCE.ordinal)
            }

            R.id.btn_basicinfo3_addinterest -> {
                //加入興趣
                if (!interestSelectBSDailogFragment.isVisible) {
                    interestSelectBSDailogFragment.show(supportFragmentManager, "InterestSelectBSDailogFragment")
                }
            }
            R.id.btn_basicinfo3_addtargetschool -> {
                //加入 學校選單
                if (!schoolSelectBSDailogFragment.isVisible) {

                    schoolSelectBSDailogFragment.selectetMap = HashMap(selectSchools)
                    schoolSelectBSDailogFragment.showOnce(supportFragmentManager)
                }
            }
            R.id.btn_basicinfo3_next -> {
                //暫存興趣 和學校
                userInfo.areaOfInterest = areaOfInterest
                if (selectSchools.keys.size > 0) {
                    userTargetSchools = ArrayList<UserTargetSchool>()
                    selectSchools.keys.forEach {
                        userTargetSchools.add(UserTargetSchool(it.toInt()))
                    }
                } else {
                    userTargetSchools = ArrayList<UserTargetSchool>()
                }
                displayBarStauts(BasInfoStatus.TEST.ordinal)
            }
            R.id.btn_basicinfo3_skip -> {

                displayBarStauts(BasInfoStatus.TEST.ordinal)
            }

            R.id.btn_basicinfo4_addtest -> {
                if (!addTestBSDailogFragment.isVisible) {
                    //防呆防止點選過快重複開啟
                    addTestBSDailogFragment = initialAddTestBSDailogFragment()
                    addTestBSDailogFragment.show(supportFragmentManager, "AddTestBSDailogFragment")
                }
            }

            R.id.btn_basicinfo4_next -> {
                //上傳基本資訊
                viewModel.postData(userInfo, academicYears, userTargetSchools, tests)
                //包含考試資料
            }

            R.id.btn_basicinfo4_skip -> {
                //上傳基本資訊 包含考試資料
                viewModel.postData(userInfo, academicYears, userTargetSchools, ArrayList())
            }

        }
    }

    override fun onBackPressed() {

        when (currentPage) {
            BasInfoStatus.BASICINFO.ordinal -> {
            }
            BasInfoStatus.SCHOOL.ordinal -> {
                displayBarStauts(BasInfoStatus.BASICINFO.ordinal)
            }
            BasInfoStatus.PREFERENCE.ordinal -> {
                displayBarStauts(BasInfoStatus.SCHOOL.ordinal)
            }
            BasInfoStatus.TEST.ordinal -> {
                displayBarStauts(BasInfoStatus.PREFERENCE.ordinal)
            }
        }
//        super.onBackPressed()
    }
}
