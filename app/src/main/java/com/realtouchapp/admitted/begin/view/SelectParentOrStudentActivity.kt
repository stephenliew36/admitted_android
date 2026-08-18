package com.realtouchapp.admitted.begin.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.viewmodel.SelectParentOrStudentViewModel
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivitySelectParentOrStudentBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.request.user.UserType
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

/** 選擇學生/家長身份 */
@AndroidEntryPoint
class SelectParentOrStudentActivity: AppCompatActivity(), OnClickListener {

    // FIXME: 是否該由後端取得的用戶類型來判斷是否該進來這頁
    // 2025.01.03: 暫時先加上了，see SplashActivity
    companion object {
        fun intent(
            activity: AppCompatActivity
        ) {
            val intent = Intent()
                .setClass(activity, SelectParentOrStudentActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    private val viewModel by viewModels<SelectParentOrStudentViewModel>()
    private lateinit var binding: ActivitySelectParentOrStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySelectParentOrStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        setClick()
        subscribeUi()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun subscribeUi() {
        viewModel.selectedUserType.observe(this) {
            when (it) {
                UserType.STUDENT -> {
                    binding.ivSelectStudent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9_border)
                    binding.ivSelectParent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9)

                    binding.btnSelectStudentOrParentNext.setBackgroundResource(R.drawable.selector_btn_default)
                    binding.btnSelectStudentOrParentNext.setOnClickListener(this)
                }
                else -> {
                    binding.ivSelectStudent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9)
                    binding.ivSelectParent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9_border)

                    binding.btnSelectStudentOrParentNext.setBackgroundResource(R.drawable.selector_btn_default)
                    binding.btnSelectStudentOrParentNext.setOnClickListener(this)
                }
            }
        }

        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.PUT_USER_TYPE -> {
                            // 修改用戶類型成功 -> 更新用戶基本資料
                            viewModel.getUserInfo()
                        }
                        NetworkResult.Action.GET_USERBASIC -> {
                            // 更新用戶基本資料成功

                            enterNextPage()
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
                    } else {
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

        viewModel.isLogout.observe(this) {
            if (it) {
                viewModelStore.clear()

                SplashActivity.Intent(this)
            }
        }
    }

    private fun setClick() {
        binding.clSelectStudent.setOnClickListener(this)
        binding.clSelectParent.setOnClickListener(this)

        binding.ivSelectStudent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9)
        binding.ivSelectParent.setBackgroundResource(R.drawable.bg_cornner_ecbfa9)

        binding.btnSelectStudentOrParentNext.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
        binding.btnSelectStudentOrParentNext.setOnClickListener(null)

        binding.ivSelectStudentOrParentBack.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_select_student_or_parent_back -> {
                viewModel.logout(this)
            }
            R.id.cl_select_student -> {
                viewModel.selectedUserType.postValue(UserType.STUDENT)
            }
            R.id.cl_select_parent -> {
                viewModel.selectedUserType.postValue(UserType.PARENT)
            }
            R.id.btn_select_student_or_parent_next -> {
                viewModel.putUserType()
//                enterNextPage()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        viewModel.logout(this)
    }

    private fun enterNextPage() {
        viewModel.selectedUserType.value?.let { userType ->
            when (userType) {
                UserType.STUDENT -> {
                    // 學生 -> 進到選擇訂閱畫面
                    SelectFreeOrPremiumActivity.Intent(this)
                }
                else -> {
                    // 家長 -> 進到掃描QRCode畫面
                    ScanStudentQrcodeActivity.intent(this)
                }
            }
        }
    }
}