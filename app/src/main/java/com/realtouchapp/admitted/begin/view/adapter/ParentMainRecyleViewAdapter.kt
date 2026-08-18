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
import com.realtouchapp.admitted.databinding.AdapterRecycleviewParentMainBinding
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.visible
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class ParentMainRecyleViewAdapter(
    context: Context,
    private val onClickStudent: ((Student) -> Unit)? = null,
    private val onClickAddStudent: (() -> Unit)? = null,
    private val onClickManageStudent: (() -> Unit)? = null,
    private val onClickDeleteStudents: ((List<Student>) -> Unit)? = null
): BaseRecyclerViewAdapter<Student, ParentMainRecyleViewAdapter.ViewHolder>(context) {

    companion object {
        private const val TAG = "ParentMainRecyleViewAdapter"
        const val MAX_LIMIT = 5
    }

    private var _isEditing: Boolean = false
    val isEditing: Boolean
        get() = _isEditing

    private var selectedStudents = ArrayList<Student>()

    override fun setData(sources: ArrayList<Student>) {
        _isEditing = false
        selectedStudents.clear()

        super.setData(sources)
    }

    fun setEditing(isEditing: Boolean) {
        _isEditing = isEditing
        selectedStudents.clear()

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewParentMainBinding.inflate(
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

        if (isEditing) {
            holder.btnAddStudent.text = "Remove"
            holder.btnManageStudents.text = "Cancel"

            configureEditingMode(holder, position)
        } else {
            holder.btnAddStudent.text = "Add more students"
            holder.btnManageStudents.text = "Manage students"

            configureNormalMode(holder, position)
        }
    }

    private fun configureEditingMode(holder: ViewHolder, position: Int) {
        if (position >= dataList.size) {
            // 最下方的按鈕+提示
            holder.clStudentInfo.gone()
            holder.clEditStudentInfo.gone()
            holder.clButtons.visible()

            holder.btnAddStudent.visible()
            if (selectedStudents.isEmpty()) {
                holder.btnAddStudent.setBackgroundResource(R.drawable.vd_bg_btn_disabled)
                holder.btnAddStudent.setOnClickListener(null)
            } else {
                holder.btnAddStudent.setBackgroundResource(R.drawable.selector_btn_default)

                // Remove
                holder.btnAddStudent.setOnClickListener {
                    if (selectedStudents.isEmpty()) return@setOnClickListener

                    onClickDeleteStudents?.invoke(selectedStudents.toList())
                }
            }

            // Cancel
            holder.btnManageStudents.setOnClickListener {
                setEditing(false)
            }

        } else {
            holder.clStudentInfo.gone()
            holder.clEditStudentInfo.visible()
            holder.clButtons.gone()

            val student = dataList[position]
            holder.tvTitleEdit.text = "${student.firstName ?: ""} ${student.lastName ?: ""}"
            holder.tvContentEdit.text = student.school

            val index = (position % 4)
            holder.ivAvatarEdit.setImageResource(
                when (index) {
                    0 -> R.drawable.ic_student1
                    1 -> R.drawable.ic_student2
                    2 -> R.drawable.ic_student3
                    3 -> R.drawable.ic_student4
                    else -> 0
                }
            )

            if (selectedStudents.contains(student)) {
                holder.ivSelectEdit.setImageResource(R.drawable.checked_filled)
            } else {
                holder.ivSelectEdit.setImageResource(R.drawable.unchecked)
            }

            holder.clEditStudentInfo.setOnClickListener {
                if (selectedStudents.contains(student)) {
                    selectedStudents.remove(student)
                } else {
                    selectedStudents.add(student)
                }
                notifyItemChanged(position)
                notifyItemChanged(dataList.size)
            }
        }
    }

    private fun configureNormalMode(holder: ViewHolder, position: Int) {
        if (position >= dataList.size) {
            // 最下方的按鈕+提示
            holder.clStudentInfo.gone()
            holder.clEditStudentInfo.gone()
            holder.clButtons.visible()

            holder.btnAddStudent.setBackgroundResource(R.drawable.selector_btn_default)

            if (dataList.size == MAX_LIMIT) {
                holder.btnAddStudent.gone()
            } else {
                holder.btnAddStudent.visible()

                if (dataList.size == MAX_LIMIT - 1) {
                    holder.btnAddStudent.text = "Add 1 more students"
                } else {
                    holder.btnAddStudent.text = "Add more students"
                }
            }

            holder.btnAddStudent.setOnClickListener {
                onClickAddStudent?.invoke()
            }

            holder.btnManageStudents.setOnClickListener {
                onClickManageStudent?.invoke()
            }

        } else {
            holder.clStudentInfo.visible()
            holder.clEditStudentInfo.gone()
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

            holder.clStudentInfo.setOnClickListener {
                onClickStudent?.invoke(student)
            }
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewParentMainBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val clAdapterRecycleview: ConstraintLayout = binding.clAdapterRecycleview

        val clStudentInfo: ConstraintLayout = binding.clStudentInfo
        val ivAvatar: AppCompatImageView = binding.ivAvatar
        val tvTitle: AppCompatTextView = binding.tvTitle
        val tvContent: AppCompatTextView = binding.tvContent

        val clEditStudentInfo: ConstraintLayout = binding.clEditStudentInfo
        val ivAvatarEdit: AppCompatImageView = binding.ivAvatarEdit
        val tvTitleEdit: AppCompatTextView = binding.tvTitleEdit
        val tvContentEdit: AppCompatTextView = binding.tvContentEdit
        val ivSelectEdit: AppCompatImageView = binding.ivSelectEdit

        val clButtons: ConstraintLayout = binding.clButtons
        val btnAddStudent: AppCompatButton = binding.btnAddStudent
        val btnManageStudents: AppCompatButton = binding.btnManageStudents
    }
}
