package com.realtouchapp.admitted.main.changestudent.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.begin.view.ParentMainActivity
import com.realtouchapp.admitted.begin.view.ScanStudentQrcodeActivity
import com.realtouchapp.admitted.begin.view.SplashActivity
import com.realtouchapp.admitted.databinding.FragmentParentChangeStudentBinding
import com.realtouchapp.admitted.ext.deCryption
import com.realtouchapp.admitted.ext.showSnackBar
import com.realtouchapp.admitted.main.changestudent.ui.adapter.ParentChangeStudentAdapter
import com.realtouchapp.admitted.main.changestudent.viewmodel.ParentChangeStudentViewModel
import com.realtouchapp.admitted.network.api.AdmittedApi
import com.realtouchapp.admitted.network.result.base.NetworkResult
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.visible

class ParentChangeStudentFragment: BottomSheetDialogFragment(), OnClickListener {
    companion object {
        const val TAG = "ParentChangeStudentFragment"
    }

    private val viewModel: ParentChangeStudentViewModel by activityViewModels()
    lateinit var binding: FragmentParentChangeStudentBinding

    private var doActionAfterPutStudent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentChangeStudentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialView()
        initialRecyclerView()
        subscribeUi()

        viewModel.loadStudents()

        doActionAfterPutStudent = false
        viewModel.loadCurrentStudent()
    }

    override fun onStart() {
        super.onStart()
//        //拿到系统的 bottom_sheet
//        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
//        //设置view高度
//        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        //获取behavior
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        //设置弹出高度
        behavior.peekHeight = 2000
        //设置展开状态
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })
    }

    private fun subscribeUi() {
        viewModel.students.observe(viewLifecycleOwner) { students  ->
            recyclerViewAdapter?.setData(students)

            if (students.size >= ParentChangeStudentAdapter.MAX_LIMIT) {
                binding.btnAddStudent.gone()
            } else {
                binding.btnAddStudent.visible()
            }
        }

        viewModel.currentStudent.observe(viewLifecycleOwner) { studentInfo ->
            if (doActionAfterPutStudent) {
                studentInfo?.let {
                    SplashActivity.Intent(requireActivity())
                } ?: run {
                    ParentMainActivity.intent(requireActivity())
                }
            } else {
                recyclerViewAdapter?.setCurrentStudentId(studentInfo?.id)
            }
        }

        viewModel.response.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                NetworkResult.Status.SUCCESS -> {
                    when (result.action) {
                        NetworkResult.Action.PUT_USER_STUDENT -> {
                            doActionAfterPutStudent = true

                            viewModel.loadCurrentStudent()
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

                }
            }
        }
    }


    private fun initialView() {
        binding.viewClose.setOnClickListener(this)

        binding.btnAddStudent.setOnClickListener(this)
        binding.btnBackToMain.setOnClickListener(this)
    }

    private val recyclerViewAdapter: ParentChangeStudentAdapter?
        get() = binding.rvStudents.adapter as? ParentChangeStudentAdapter

    private fun initialRecyclerView() {
        binding.rvStudents.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvStudents.adapter = ParentChangeStudentAdapter(
            requireContext(),
            onClickStudent = { student ->
                viewModel.selectStudent(student)
            }
        )
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_student -> {
                ScanStudentQrcodeActivity.intent(requireActivity(), true)
            }
            R.id.btn_back_to_main -> {
                viewModel.deselectStudent()
            }
            R.id.view_close -> {
                dismiss()
            }
        }
    }
}