package com.realtouchapp.admitted.main.changestudent.ui.adapter

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
import com.realtouchapp.admitted.databinding.AdapterParentChangeStudentBinding
import com.realtouchapp.admitted.databinding.AdapterRecycleviewParentMainBinding
import com.realtouchapp.admitted.network.entity.Student
import com.realtouchapp.tools.ext.gone
import com.realtouchapp.tools.ext.visible
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class ParentChangeStudentAdapter(
    context: Context,
    private val onClickStudent: ((Student) -> Unit)? = null
): BaseRecyclerViewAdapter<Student, ParentChangeStudentAdapter.ViewHolder>(context) {

    companion object {
        private const val TAG = "ParentChangeStudentAdapter"
        const val MAX_LIMIT = 5
    }

    private var currentStudentId: String? = null

    fun setCurrentStudentId(id: String?) {
        currentStudentId = id
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterParentChangeStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        Log.e(TAG, "onBindViewHolder: $position")

        val student = dataList[position]
        holder.tvTitle.text = "${student.firstName ?: ""} ${student.lastName ?: ""}"

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

        if (student.systemNumber == currentStudentId) {
            holder.tvViewing.visible()
        } else {
            holder.tvViewing.gone()
        }

        holder.clStudentInfo.setOnClickListener {
            if (student.systemNumber == currentStudentId) return@setOnClickListener

            onClickStudent?.invoke(student)
        }
    }

    inner class ViewHolder(binding: AdapterParentChangeStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val clAdapterRecycleview: ConstraintLayout = binding.clAdapterRecycleview

        val clStudentInfo: ConstraintLayout = binding.clStudentInfo
        val ivAvatar: AppCompatImageView = binding.ivAvatar
        val tvTitle: AppCompatTextView = binding.tvTitle
        val tvViewing: AppCompatTextView = binding.tvViewing
        val viewDivider: View = binding.viewDivider
    }
}
