package com.realtouchapp.admitted.begin.view

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
import com.realtouchapp.admitted.begin.view.adapter.ParentEditStudentRecyleViewAdapter
import com.realtouchapp.admitted.begin.view.adapter.ParentMainRecyleViewAdapter
import com.realtouchapp.admitted.begin.viewmodel.ParentEditStudentViewModel
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityParentEditStudentBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.result.base.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ParentEditStudentActivity: AppCompatActivity(), OnClickListener {
    companion object {
        private const val TAG = "ParentEditStudentActivity"

        fun intent(activity: AppCompatActivity) {
            val intent = Intent()
                .setClass(activity, ParentEditStudentActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private val viewModel by viewModels<ParentEditStudentViewModel>()
    private lateinit var binding: ActivityParentEditStudentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityParentEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        setClick()
        setupRecyclerView()
        subscribeUi()

        viewModel.refreshStudentsFromRemote()
    }

    private fun subscribeUi() {
        viewModel.students.observe(this) { students ->
            Log.d(TAG, "students: $students")
            recyclerViewAdapter?.setData(students)
        }

        viewModel.response.observe(this) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    //成功
                    when (result.action) {
                        NetworkResult.Action.GET_STUDENTS -> {

                        }
                        NetworkResult.Action.DELETE_STUDENT_FROM_PARENT -> {
                            viewModel.refreshStudentsFromRemote()
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
    }

    private fun setClick() {
        binding.ivBack.setOnClickListener(this)
    }

    private val recyclerViewAdapter: ParentMainRecyleViewAdapter?
        get() = binding.rvStudents.adapter as? ParentMainRecyleViewAdapter

    private fun setupRecyclerView() {
        binding.rvStudents.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvStudents.adapter = ParentEditStudentRecyleViewAdapter(
            this,
            onClickDelete = { student ->
                val title = "Are you sure to delete student: " +
                        "${student.firstName ?: ""} ${student.lastName ?: ""}?"
                AlertDialog.Builder(this)
                    .setTitle(title)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.deleteStudent(student)
                    }
                    .create()
                    .also { it.show() }
            },
            onClickAddStudent = {
                ScanStudentQrcodeActivity.intent(this)
            },
            onClickNext = {
                ParentMainActivity.intent(this)
            }
        )
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                ParentMainActivity.intent(this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        ParentMainActivity.intent(this)
    }
}