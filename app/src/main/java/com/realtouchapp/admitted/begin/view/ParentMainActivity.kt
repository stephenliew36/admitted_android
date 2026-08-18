package com.realtouchapp.admitted.begin.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.adapter.ParentMainRecyleViewAdapter
import com.realtouchapp.admitted.begin.viewmodel.ParentMainViewModel
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.common.ui.dialogfragmnet.LoadingDialogFragment
import com.realtouchapp.admitted.databinding.ActivityParentMainBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.roadmmap.ui.activity.ProfileActivity
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentMainActivity: AppCompatActivity(), OnClickListener {
    companion object {
        private const val TAG = "ParentMainActivity"

        fun intent(activity: Activity) {
            val intent = Intent()
                .setClass(activity, ParentMainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val viewModel by viewModels<ParentMainViewModel>()
    private lateinit var binding: ActivityParentMainBinding

    private var loadingDialogFragment: LoadingDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        setClick()
        setupRecyclerView()
        subscribeUi()

        viewModel.getUserInfoCache()
        viewModel.refreshStudentsFromRemote()
    }

    private fun subscribeUi() {
        viewModel.userInfo.observe(this) {
            binding.tvUserName.text = "${it.firstName} ${it.lastName}"
        }

        viewModel.students.observe(this) { students ->
            Log.d(TAG, "students: $students")
            recyclerViewAdapter?.setData(students)

            if (students.isNotEmpty()) {
                val studentNames = students
                    .map { "${it.firstName}" }
                    .toMutableList()

                val subtitle: String
                if (studentNames.size > 1) {
                    val lastStudentName = studentNames.removeAt(studentNames.lastIndex)
                    subtitle = "Parent of ${studentNames.joinToString(", ")} and $lastStudentName"
                } else {
                    subtitle = "Parent of ${studentNames.joinToString(", ")}"
                }
                binding.tvSubtitle.text = subtitle

            } else {
                binding.tvSubtitle.text = " "
            }
        }

        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    loadingDialogFragment?.dismiss()

                    //成功
                    when (result.action) {
                        NetworkResult.Action.GET_STUDENTS -> {

                        }
                        NetworkResult.Action.DELETE_STUDENT_FROM_PARENT -> {
                            viewModel.refreshStudentsFromRemote()
                        }
                        NetworkResult.Action.PUT_USER_STUDENT -> {
                            // FIXME: 進入學生主頁
                            SplashActivity.Intent(this)
                        }
                        else -> { }
                    }
                }
                NetworkResult.Status.ERROR -> {
                    loadingDialogFragment?.dismiss()

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
                    when (result.action) {
                        NetworkResult.Action.DELETE_STUDENT_FROM_PARENT -> {
                            loadingDialogFragment = LoadingDialogFragment.newInstance {
                                loadingDialogFragment = null
                            }
                            loadingDialogFragment?.showOnce(supportFragmentManager)
                        }
                        else -> { }
                    }
                }
            }
        }
    }

    private fun setClick() {
        binding.ivSetting.setOnClickListener(this)
    }

    private val recyclerViewAdapter: ParentMainRecyleViewAdapter?
        get() = binding.rvStudents.adapter as? ParentMainRecyleViewAdapter

    private fun setupRecyclerView() {
        binding.rvStudents.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvStudents.adapter = ParentMainRecyleViewAdapter(
            this,
            onClickStudent = { student ->
                viewModel.selectStudent(student)
            },
            onClickAddStudent = {
                ScanStudentQrcodeActivity.intent(this, true)
            },
            onClickManageStudent = {
                recyclerViewAdapter?.setEditing(true)
            },
            onClickDeleteStudents = { students ->
                val studentNames = students
                    .map { "${it.firstName ?: ""} ${it.lastName ?: ""}?" }
                    .toList()

                val title = "Are you sure to delete students: " +
                        "${studentNames.joinToString(", ")}?"
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteStudents(students)
                    }
                    .create()
                    .also { it.show() }
            }
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_setting -> {
                // 個人資料頁
                ProfileActivity.Intent(this)
            }
        }
    }
}