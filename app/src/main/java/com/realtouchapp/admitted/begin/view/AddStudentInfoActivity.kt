package com.realtouchapp.admitted.begin.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.ext.configureEdgeToEdge
import com.realtouchapp.admitted.databinding.ActivityAddStudentInfoBinding
import com.realtouchapp.admitted.network.entity.Student
import dagger.hilt.android.AndroidEntryPoint

data class AddStudentInfo(
    val student: Student,
    val method: Int,
    val productId: String
): java.io.Serializable

@AndroidEntryPoint
class AddStudentInfoActivity: AppCompatActivity(), OnClickListener {
    companion object {
        private const val ADD_STUDENT_INFO_KEY = "AddStudentInfoKey"
        private const val IS_INTENT_FROM_MAIN_KEY = "isIntentFromMainKey"

        fun intent(
            activity: AppCompatActivity,
            student: Student,
            method: Int,
            productId: String,
            isIntentFromMain: Boolean = false
        ) {
            val intent = Intent()
                .setClass(activity, AddStudentInfoActivity::class.java)
                .apply {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        ADD_STUDENT_INFO_KEY,
                        AddStudentInfo(student, method, productId)
                    )
                    bundle.putBoolean(IS_INTENT_FROM_MAIN_KEY, isIntentFromMain)
                    putExtras(bundle)
                }
            activity.startActivity(intent)
            activity.finish()
        }
    }

    private lateinit var binding: ActivityAddStudentInfoBinding

    private var addStudentInfo: AddStudentInfo? = null
    private var isIntentFromMain: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStudentInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureEdgeToEdge()

        loadIntentExtras()
        setupUi()
        setClick()
        subscribeUi()
    }

    private fun loadIntentExtras() {
        intent.extras?.let { bundle ->
            addStudentInfo = bundle.getSerializable(ADD_STUDENT_INFO_KEY) as? AddStudentInfo
            isIntentFromMain = bundle.getBoolean(IS_INTENT_FROM_MAIN_KEY)
        }
    }

    private fun setupUi() {
        addStudentInfo?.student?.let { student ->
            val iconList = arrayOf(
                R.drawable.ic_student1,
                R.drawable.ic_student2,
                R.drawable.ic_student3,
                R.drawable.ic_student4
            )
            val randomIndex = iconList.indices.random()
            binding.ivAvatar.setImageResource(iconList[randomIndex])
            binding.tvStudentName.text = "${student.firstName} ${student.lastName}"
            binding.tvStudentInfo.text = student.school

            binding.clAddStudent.setBackgroundResource(R.drawable.selector_btn_default)
            binding.clAddStudent.setOnClickListener(this)

        } ?: run {
            binding.ivAvatar.setImageDrawable(null)
            binding.tvStudentName.text = ""
            binding.tvStudentInfo.text = ""

            binding.clAddStudent.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
            binding.clAddStudent.setOnClickListener(null)
        }
    }

    private fun subscribeUi() {

    }

    private fun setClick() {
        binding.ivBack.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                ScanStudentQrcodeActivity.intent(this, isIntentFromMain)
            }
            R.id.cl_add_student -> {
                addStudentInfo?.let {
                    SubscribeActivity.intentWithStudent(
                        this,
                        it.student,
                        it.method,
                        it.productId,
                        isIntentFromMain
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        ScanStudentQrcodeActivity.intent(this, isIntentFromMain)
    }
}