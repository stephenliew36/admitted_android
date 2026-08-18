package com.realtouchapp.admitted.begin.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterRecycleviewParentEditStudentBinding
import com.realtouchapp.admitted.databinding.AdapterRecycleviewTargetschoolAddkBinding
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.admitted.network.entity.TargetSchool
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.setMargin
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSize
import com.realtouchapp.tools.ext.visible
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool

class ParentEditStudentRecyleViewAdapter(
    context: Context,
    private val onClickDelete: ((Student) -> Unit)? = null,
    private val onClickAddStudent: (() -> Unit)? = null,
    private val onClickNext: (() -> Unit)? = null
): BaseRecyclerViewAdapter<Student, ParentEditStudentRecyleViewAdapter.ViewHolder>(context) {

    companion object {
        const val MAX_LIMIT = 5
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewParentEditStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        Log.e("ParentEditStudentRecyleViewAdapter", "onBindViewHolder: $position")
        if (position >= dataList.size) {
            // 最下方的按鈕+提示
            holder.clStudentInfo.gone()
            holder.clButtons.visible()

            if (dataList.size == MAX_LIMIT) {
                holder.clMaxLimitHint.visible()
                holder.btnNext.gone()

                holder.btnAddStudent.text = "Next"

            } else {
                holder.clMaxLimitHint.gone()
                holder.btnNext.visible()
                holder.btnNext.text = "Next"

                if (dataList.size == MAX_LIMIT - 1) {
                    holder.btnAddStudent.text = "Add 1 more students"
                } else {
                    holder.btnAddStudent.text = "Add more students"
                }
            }

            holder.btnAddStudent.setOnClickListener {
                if (dataList.size == MAX_LIMIT) {
                    onClickNext?.invoke()
                } else {
                    onClickAddStudent?.invoke()
                }
            }

            holder.btnNext.setOnClickListener {
                onClickNext?.invoke()
            }

        } else {
            holder.clStudentInfo.visible()
            holder.clButtons.gone()

            val student = dataList[position]
            holder.tvTitle.text = "${student.firstName ?: ""} ${student.lastName ?: ""}"
            holder.tvContent.text = student.school

            val index = (position % 4)
            holder.ivAvatar.setImageResource(
                when (index) {
                    0 -> R.drawable.ic_student1
                    1 -> R.drawable.ic_student2
                    2 -> R.drawable.ic_student3
                    3 -> R.drawable.ic_student4
                    else -> 0
                }
            )

            holder.ivDelete.setOnClickListener {
                onClickDelete?.invoke(student)
            }
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewParentEditStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val clAdapterRecycleview: ConstraintLayout = binding.clAdapterRecycleview
        val clStudentInfo: ConstraintLayout = binding.clStudentInfo
        val ivAvatar: AppCompatImageView = binding.ivAvatar
        val tvTitle: AppCompatTextView = binding.tvTitle
        val tvContent: AppCompatTextView = binding.tvContent
        val ivDelete: AppCompatImageView = binding.ivDelete

        val clButtons: ConstraintLayout = binding.clButtons
        val btnAddStudent: AppCompatButton = binding.btnAddStudent
        val btnNext: AppCompatButton = binding.btnNext
        val clMaxLimitHint: ConstraintLayout = binding.clMaxLimitHint
    }
}
