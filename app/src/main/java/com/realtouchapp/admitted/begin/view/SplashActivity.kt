package com.realtouchapp.admitted.begin.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.realtouchapp.admitted.begin.viewmodel.SplashActivityViewModel
import com.realtouchapp.admitted.common.ui.dialogfragmnet.LoadingDialogFragment
import com.realtouchapp.admitted.databinding.ActivitySplashBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.setNavigationBarStyle
import com.realtouchapp.admitted.ext.setStatusBarStyle
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.ext.testPrint
import com.realtouchapp.admitted.main.roadmmap.ui.activity.MainActivity
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.admitted.network.result.base.NetworkResult.Action
import com.realtouchapp.admitted.storage.database.AdmittedDataBase
import com.realtouchapp.admitted.storage.datastore.UpdateTimeStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SplashActivity"

        fun Intent(activity: Activity) {
            val intent = Intent().setClass(activity, SplashActivity::class.java)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private lateinit var binding: ActivitySplashBinding
    private val viewModel by viewModels<SplashActivityViewModel>()
    private var isGetTargetSchool = false
    private var isIntentMain = false
    private var isRetryingStartupData = false
    private var startupLoadingDialog: LoadingDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //強制黑暗關閉模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setStatusBarStyle(false)
        setNavigationBarStyle(false)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            viewModel.saveDeviceToken(task.result)
            viewModel.postDeviceToken()
            testPrint("firebasetoken", task.result)
        })
//        BasicInfoActivity.Intent(this)
//        SubscribeActivity.Intent(this)
//        LoginActivity.Intent(this)
        subscribeUi()
        viewModel.getToken()
    }

    private fun subscribeUi() {
        viewModel.token.observe(this) { token ->
            if (token.isNullOrEmpty()) {
                Log.e("Intent", "BeginActivity.Intent(this)")
                BeginActivity.Intent(this)
            } else {
                if (!isGetTargetSchool) {
                    viewModel.getTargetSchools()
                    isGetTargetSchool = true
                }
            }
        }

        viewModel.response.observe(this) { result ->
            Log.d(TAG, "subscribeUi: action=${result.action} status=${result.status}")

            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        Action.GETTARGETSCHHOOL_DETAIL_LIST -> {
                            viewModel.getUserInfo()
                        }
                        Action.GET_USERBASIC -> {
//                            if ((result.data!!.data as UserInfo).nationality.isNullOrEmpty()) {
////                                國籍資料為空 尚未輸入完資料
//                                BasicInfoActivity.Intent(this)
//                            } else {
                            viewModel.getAllOrStudents()
//                            }
                        }
                        Action.GET_ALL, Action.GET_STUDENTS -> {
                            //讀取申請學校 用來判斷是否有訂閱
                            viewModel.getApplicationSchoolForCheckSubscribel()
                        }
                        Action.GET_APPLICATIONSCHHOOL -> {
                            enterNextPage(true)
                        }
                        else -> {
                        }
                    }
                }

                NetworkResult.Status.ERROR -> {
                    //失敗
                    if (result.action == Action.GETTARGETSCHHOOL_DETAIL_LIST) {
                        waitForStartupDataRetry()
                    } else if (result.statusCode == AdmittedApi.StatusCode.ERROR_OTTHER.code) {
                        //本地端失敗(網路未開啟等等)
                        result.message?.let {
                            showSnackBar(binding.root, it)
                        }
                    } else if (result.statusCode == AdmittedApi.StatusCode.ERROR_TOKEN_FAILED.code) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            val updateTimeStore = UpdateTimeStore(this@SplashActivity)
                            updateTimeStore.deleteAll()
//                            this@SplashActivity.deleteDatabase(AdmittedDataBase.DATABASE_NAME)
//                            this@SplashActivity.getDatabasePath(AdmittedDataBase.DATABASE_NAME).delete()
                            viewModel.dataBase.clearAllTables()
                        }
                    } else if (result.statusCode == AdmittedApi.StatusCode.ERROR_SUBSCRIBE_FAILED.code) {
                        enterNextPage(false)
                    } else {
                        //失敗
                        result.error!!.message?.let { message ->
                            runCatching { deCryption(message).msgDesc }
                                .getOrNull()
                                ?.takeIf { it.isNotBlank() }
                                ?.let { showSnackBar(binding.root, it) }
                        }
                    }
                }

                NetworkResult.Status.LOADING -> {
                    if (result.action == Action.GETTARGETSCHHOOL_DETAIL_LIST) {
                        showStartupLoading()
                    }
                    //載入中 可顯示載入動畫
                }
            }
        }
    }

    private fun waitForStartupDataRetry() {
        showStartupLoading()
        if (isRetryingStartupData) return

        isRetryingStartupData = true
        lifecycleScope.launch {
            delay(10_000)
            isRetryingStartupData = false
            viewModel.getTargetSchools()
        }
    }

    private fun showStartupLoading() {
        if (startupLoadingDialog?.isAdded == true) return

        startupLoadingDialog = LoadingDialogFragment.newInstance("Get User Data") {
            startupLoadingDialog = null
        }.apply {
            isCancelable = false
            isCanceledOnTouchOutside = false
        }
        startupLoadingDialog?.showOnce(supportFragmentManager)
    }

    private fun dismissStartupLoading() {
        startupLoadingDialog?.dismissAllowingStateLoss()
        startupLoadingDialog = null
    }

    /** 進入下一頁
     *
     * @param hasSubscribe 是否有訂閱
     */
    private fun enterNextPage(hasSubscribe: Boolean) {
        dismissStartupLoading()
        Log.d(TAG, "enterNextPage")
        viewModel.observableUserType.value?.let { userType ->
            // 已選擇角色 (學生/家長)
            when (userType) {
                UserType.STUDENT -> {
                    if (hasSubscribe) {
                        viewModel.updateFreeVersion(false)

                        //進入首頁
                        if (!isIntentMain) {
                            isIntentMain = true

                            MainActivity.Intent(this)
                        } else { }
                    } else {
                        viewModel.isFreeVersion.value?.let {
                            if (it) {
                                //免費版
                                MainActivity.Intent(this)
                            } else {
                                //尚未選擇是否付費
                                SelectFreeOrPremiumActivity.Intent(this)
                            }
                        }
                    }
                }
                UserType.TEACHER, UserType.PARENT -> {
                    viewModel.observableStudentToken.value?.let {
                        // 有選擇學生
                        MainActivity.Intent(this)
                    } ?: run {
                        // 未選擇學生
                        ParentMainActivity.intent(this)
                    }

                }
            }
        } ?: run {
            // 未選擇角色 (學生/家長)
            SelectParentOrStudentActivity.intent(this)
        }
    }
}
