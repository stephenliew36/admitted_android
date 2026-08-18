package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterCoursetrackBinding
import com.realtouchapp.admitted.network.entity.Course.CourseTrack
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.*
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import kotlin.collections.ArrayList

class CourseTrackAdapter(context: Context) : BaseRecyclerViewAdapter<CourseTrack, CourseTrackAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCoursetrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        var courseTrack = dataList[position]

        courseTrack?.let {
            holder.tvAdapterRecycleviewTitle.setText(it.title())
            holder.ivAdapterRecycleviewIcon.setImageResource(it.icon())
        }

        holder.tvAdapterRecycleviewCourses.setText(mContext.getString(R.string.course_track_courses,courseTrack.num()))

        val linearLayoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        linearLayoutManager.setInitialPrefetchItemCount(courseTrack.chartId().size);
        holder.rvAdapterRecycleview.layoutManager = linearLayoutManager
        val courseTrackInfoAdapter = CourseTrackInfoAdapter(mContext)
        courseTrackInfoAdapter.onItemClick = onItemClick
        courseTrackInfoAdapter.titlesId = courseTrack.chartId()
        courseTrackInfoAdapter.imagesId = courseTrack.imageId()
        val arrays = arrayListOf<Int>()
        arrays.addAll(courseTrack.chartId())
        courseTrackInfoAdapter.setData(arrays)
        holder.rvAdapterRecycleview.adapter = courseTrackInfoAdapter

        holder.itemView.setOnClickListener {
            if (holder.llAdapterRecycleviewIc.visibility == View.VISIBLE){
                holder.llAdapterRecycleviewIc.visibility = View.GONE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_down_black)
            }else{
                holder.llAdapterRecycleviewIc.visibility = View.VISIBLE
                holder.ivAdapterRecycleviewArrow.setImageResource(R.drawable.vd_ic_arrow_top)
            }
        }

    }
    inner class ViewHolder(binding: AdapterCoursetrackBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val rvAdapterRecycleview: RecyclerView = binding.rvAdapterRecycleview
        val tvAdapterRecycleviewCourses: AppCompatTextView = binding.tvAdapterRecycleviewCourses
        val ivAdapterRecycleviewIcon: AppCompatImageView = binding.ivAdapterRecycleviewIcon
        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow
        val llAdapterRecycleviewIc: LinearLayoutCompat = binding.llAdapterRecycleviewIc

        init {
            binding.root.setMarginTopOrignal(16)
            ivAdapterRecycleviewIcon.setViewSizeOrignalWH(24,24)
            tvAdapterRecycleviewTitle.setTextSize(13)
            tvAdapterRecycleviewCourses.setTextSize(10)
            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
        }

    }

}
