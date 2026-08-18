package com.realtouchapp.admitted.main.roadmmap.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.PremiumActivity
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.common.algorithm.ScoreAlgorithm
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityMainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.add.ui.fragment.QuickAddFragment
import com.realtouchapp.admitted.main.application.ui.fragment.ApplicationsListFragment
import com.realtouchapp.admitted.main.changestudent.ui.fragment.ParentChangeStudentFragment
import com.realtouchapp.admitted.main.roadmmap.ui.fragment.MainFragment
import com.realtouchapp.admitted.main.roadmmap.viewmodel.MainActivityViewModel
import com.realtouchapp.admitted.member.view.activity.NotificaitonActivity
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.entity.UserInfo
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.bottombar.AdmittedButtomBar
import com.realtouchapp.tools.ui.bottombar.BottomBarTab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity: AppCompatActivity(), OnClickListener {
    companion object {
        fun Intent(activity: AppCompatActivity) {
            val intent = android.content.Intent()
                .setClass(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()
    private lateinit var userInfo: UserInfo
    lateinit var scoreAlgorithm: ScoreAlgorithm

    var quickAddFragment: QuickAddFragment? = null
    var parentChangeStudentFragment: ParentChangeStudentFragment? = null
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    private val isParent: Boolean
        get() {
            val userType = viewModel.userType.value
            return userType == UserType.PARENT || userType == UserType.TEACHER
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        getData()
        initialViewsSize()
        initialView()
        subscribeUi()
        requestNotificationPermissionIfNeeded()

        viewModel.getUserInfoCache()
        viewModel.initialScoreAlgorithn()
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun getData() {

    }

    private fun initialViewsSize() {
        binding.tvActivityMainName.setTextSize(13)
        binding.tvActivityMainScore.setTextSize(10)
        binding.ivActivityMainNotification.setViewSizeOrignalWH(26, 26)
        binding.ivActivityMainProfile.setViewSizeOrignalWH(26, 26)
        binding.pbActivityMainName.setViewSizeByRatioHH(.035f, .035f)
    }

    private fun initialView() {
        lifecycleScope.launch {
            viewModel.isFreeVersion.collect {
                if (it) {
                    binding.tvActivityMainScore.visibility = View.GONE
                    binding.pbActivityMainName.visibility = View.GONE
                } else {
                    binding.tvActivityMainScore.visibility = View.VISIBLE
                    binding.pbActivityMainName.visibility = View.VISIBLE
                }
            }
        }
        lifecycleScope.launch {
            // 根據用戶類型(家長/學生)來設置UI
            viewModel.userType.collect {
                when (it) {
                    UserType.PARENT, UserType.TEACHER -> {
                        // 家長
                        // 根據用戶類型(家長/學生)來設置TopBar (可切換學生)
                        binding.clParentSelectStudent.visible()
                        binding.groupMainStudentIcons.invisible()

                        initialTab(UserType.PARENT)
                    }
                    UserType.STUDENT -> {
                        // 學生
                        binding.clParentSelectStudent.invisible()
                        binding.groupMainStudentIcons.visible()

                        initialTab(UserType.STUDENT)
                    }
                    else -> { }
                }
            }
        }
        lifecycleScope.launch {
            // 被選擇的學生名稱
            viewModel.currentStudentName.collect {
                binding.tvMainSelectedStudentName.text = it
            }
        }

        binding.ivActivityMainNotification.setOnClickListener(this)
        binding.ivActivityMainProfile.setOnClickListener(this)
        binding.clParentSelectStudent.setOnClickListener(this)
    }

    override fun onBackPressed() {
//        super.onBackPressed()

        val fragmentIdMap = tabFragmentIdMap
        val currentItemPosition = binding.bottomBar.currentItemPosition
        if (!fragmentIdMap.containsKey(currentItemPosition)) {
            super.onBackPressed()
            return
        }

        try {
            fragmentIdMap[currentItemPosition]?.let {
                if (!findNavController(it).navigateUp()) {
                    finish()
                }
            } ?: run {
                super.onBackPressed()
            }
        } catch (_: Exception) {
            super.onBackPressed()
        }
    }

    private fun subscribeUi() {
        viewModel.scoreAlgorithm.observe(this) {
            binding.pbActivityMainName.progress = it.getTotalPercent()
            binding.tvActivityMainScore.text = it.getTotalPercent().toString()
            scoreAlgorithm = it
        }

        viewModel.userInfo.observe(this) {
            this.userInfo = it
            binding.tvActivityMainName.text = it.firstName + "． Grade " + it.grade
        }

        viewModel.response.observe(this) { result ->
            when (result.status) {

                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.GET_USERBASIC -> {
                            result.data?.let {
                                viewModel.mergePendingGoogleName(it.data as UserInfo)
                            }
                        }
                        NetworkResult.Action.POST_INTELLECTUALCURIOSITY -> {
                        }
                        NetworkResult.Action.POST_COMMUNITYIMPACT -> {
                        }
                        NetworkResult.Action.POST_HONORS -> {
                        }
                        NetworkResult.Action.POST_TESTS -> {
                        }
                        NetworkResult.Action.PUT_USERBASIC -> {
                            result.data?.let {
                                userInfo = it.data as UserInfo
                                binding.tvActivityMainName.text = userInfo.firstName +"． Grade "+userInfo.grade
//                                runBlocking {
//                                    viewModel.saveUserBasic(userInfo)
//                                    viewModel.getUserInfoCache()
//                                }
                            }
                        }
                        else -> { }
                    }
                }
                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else if (result.statusCode == AdmittedApi.StatusCode.ERROR_SUBSCRIBE_FAILED.code) {
                        SplashActivity.Intent(this)
                    }else{
                        //失敗
                        result.error!!.message?.let { message ->
                            showSnackBar(binding.root, deCryption(message).msgDesc)
                        }
                    }
                }

                NetworkResult.Status.LOADING -> {
                    //載入中 可顯示載入動畫
                }
            }

        }
    }

    private val tabFragmentIdMap: Map<Int, Int>
        get() {
            return when (viewModel.userType.value) {
                UserType.PARENT, UserType.TEACHER -> {
                    mapOf(
                        0 to R.id.nav_host_fragment,
                        1 to R.id.nav_host_fragment4
                    )
                }
                UserType.STUDENT -> {
                    mapOf(
                        0 to R.id.nav_host_fragment,
                        1 to R.id.nav_host_fragment2,
                        3 to R.id.nav_host_fragment3,
                        4 to R.id.nav_host_fragment4
                    )
                }
                else -> emptyMap()
            }
        }

    private fun initialTab(userType: UserType) {
        if ((binding.bottomBar.getChildAt(0) as LinearLayout).childCount == 0) {
            binding.bottomBar.setViewSizeByRatioWH(.9f,.08f)
            binding.bottomBar.setViewSizeHeight(-2)

            binding.bottomBar.addItem(BottomBarTab(this, R.drawable.vd_tab_dashboard, getString(R.string.tab_1_)))
            if (userType == UserType.STUDENT) {
                // Note: 「家長」只有DashBoard和Application功能
                binding.bottomBar.addItem(BottomBarTab(this, R.drawable.vd_tab_explore, getString(R.string.tab_2_)))
                binding.bottomBar.addItem(BottomBarTab(this, R.drawable.vd_tab_add, getString(R.string.tab_3_),false))
                binding.bottomBar.addItem(BottomBarTab(this, R.drawable.vd_tab_concult, getString(R.string.tab_4_)))
            }
            binding.bottomBar.addItem(BottomBarTab(this, R.drawable.vd_tab_applications, getString(R.string.tab_5_)))

            binding.bottomBar.setOnTabSelectedListener(object : AdmittedButtomBar.OnTabSelectedListener {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onTabSelected(position: Int, prePosition: Int) {
                    val fragmentIdMap = tabFragmentIdMap
                    fragmentIdMap.values.forEach {
                        supportFragmentManager.findFragmentById(it)?.view?.visibility = View.GONE
                    }

                    if (fragmentIdMap.containsKey(position)) {
                        val fragmentId = fragmentIdMap[position] ?: return

                        val fragment = supportFragmentManager.findFragmentById(fragmentId)
                        if (fragmentId == R.id.nav_host_fragment) {
                            if ((fragment?.childFragmentManager?.fragments?.get(0))
                                        is MainFragment) {
                                // 因為第一次進入已經建好view沒辦法刷新分數
                                (fragment.childFragmentManager.fragments[0] as MainFragment)
                                    .reFreshSore()
                            }
                        } else if (fragmentId == R.id.nav_host_fragment4) {
                            if ((fragment?.childFragmentManager?.fragments?.get(0))
                                        is ApplicationsListFragment) {
                                // 因為第一次進入已經建好view沒辦法刷新分數
                                (fragment.childFragmentManager.fragments[0]
                                        as ApplicationsListFragment)
                                    .reFreshreadliness()
                            }
                        }

                        fragment?.view?.visibility = View.VISIBLE

                    } else {
                        // position = 2 when userType = STUDENT
                        showQuickAddFragment()
                        binding.bottomBar.setCurrentItem(prePosition)
                    }
                }

                override fun onTabUnselected(position: Int) {

                }

                override fun onTabReselected(position: Int) {
                    when (position) {
                        0 -> { }
                        1 -> { }
                        2 -> { }
                        3 -> { }
                        4 -> { }
                    }
                }
            })
        }
        runBlocking {
            if (viewModel.isFreeVersion.value) {
                binding.bottomBar.setCurrentItem(1)
            } else {
                binding.bottomBar.setCurrentItem(0)
            }
        }
    }

    fun setSelecetTab(indext: Int) {
        binding.bottomBar.setCurrentItem(indext)
    }

    private fun showQuickAddFragment() {
        if (quickAddFragment == null) {
            quickAddFragment = QuickAddFragment()
            quickAddFragment!!.show(supportFragmentManager,QuickAddFragment.TAG)
        } else {
            if (!quickAddFragment!!.isVisible) {
                supportFragmentManager.beginTransaction()
                    .remove(quickAddFragment!!)
                    .commit()
                quickAddFragment = null
                showQuickAddFragment()
            }
        }
    }

    private fun showParentChangeStudentFragment() {
        if (parentChangeStudentFragment == null) {
            parentChangeStudentFragment = ParentChangeStudentFragment()
            parentChangeStudentFragment!!.show(supportFragmentManager, ParentChangeStudentFragment.TAG)
        } else {
            if (!parentChangeStudentFragment!!.isVisible) {
                supportFragmentManager.beginTransaction()
                    .remove(parentChangeStudentFragment!!)
                    .commit()
                parentChangeStudentFragment = null
                showParentChangeStudentFragment()
            }
        }
    }

    fun initialScoreAlgorithn() {
        viewModel.initialScoreAlgorithn()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_activity_main_notification -> {
                if (viewModel.isFreeVersion.value) {
                    PremiumActivity.Intent(this@MainActivity)
                } else {
                    NotificaitonActivity.Intent(this@MainActivity)
                }
            }
            R.id.iv_activity_main_profile -> {
                ProfileActivity.Intent(this@MainActivity)
            }
            R.id.cl_parent_select_student -> {
                // 「家長」切換學生
                showParentChangeStudentFragment()
            }
        }
    }
}
