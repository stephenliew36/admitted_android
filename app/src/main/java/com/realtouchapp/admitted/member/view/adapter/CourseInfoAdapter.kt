package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterCourseBinding
import com.realtouchapp.admitted.network.entity.Course
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import com.realtouchapp.tools.ext.setViewSizeOrignalHeight
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class CourseInfoAdapter(context: Context) : BaseRecyclerViewAdapter<Course, CourseInfoAdapter.ViewHolder>(context) {
    lateinit var onItemClick: ((Course) -> Unit)
    lateinit var tracks:Array<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        tracks = parent.context.resources.getStringArray(R.array.array_course_track)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.tvAdapterCourseTitle.text = dataList[position].courseName
        holder.tvAdapterCourseScore.text = dataList[position].grade
        if (dataList[position].courseTrack!!.toInt() in 0 .. tracks.size){

            holder.tvAdapterCourseTrack.text =mContext.resources.getStringArray(R.array.array_course_level).get(dataList[position].courseLevel!!-1)+"．"+tracks.get(dataList[position].courseTrack!!.toInt()-1)

        }


        holder.root.setOnClickListener {
            onItemClick(dataList[position])
        }
    }

    inner class ViewHolder(binding: AdapterCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val tvAdapterCourseTitle: AppCompatTextView = binding.tvAdapterCourseTitle
        val tvAdapterCourseScore: AppCompatTextView = binding.tvAdapterCourseScore
        val tvAdapterCourseTrack: AppCompatTextView = binding.tvAdapterCourseTrack

        init {

            binding.root.setViewSizeOrignalHeight(65)
            tvAdapterCourseTitle.setTextSize(15)
            tvAdapterCourseScore.setTextSize(15)
            tvAdapterCourseTrack.setTextSize(10)
        }
    }

}
