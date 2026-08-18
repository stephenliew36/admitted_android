package com.realtouchapp.admitted.main.roadmmap.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.member.view.adapter.CourseTrackAdapter
import com.realtouchapp.admitted.network.entity.AreaOfInterest
import com.realtouchapp.admitted.network.entity.Course
import com.realtouchapp.tools.ext.setViewSizeWidthByRatio
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class CoursePagerAdapter(context: Context): BaseRecyclerViewAdapter<Int, CoursePagerAdapter.ViewHolder>(context) {
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = View.inflate(parent.getContext(), R.layout.pageadapter_course,null)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        item.layoutParams = lp
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.courseTrackAdapter = CourseTrackAdapter(mContext)
        holder.courseTrackAdapter.setData(AreaOfInterest.getCourseTrack(mContext,mContext.resources.getStringArray(R.array.array_interest).get(position)))
        holder.courseTrackAdapter.onItemClick = onItemClick
        var linearLayoutManager = LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false)
        holder.rvPageradapterCourse.adapter = holder.courseTrackAdapter
        holder.rvPageradapterCourse.layoutManager = linearLayoutManager

    }
    inner class ViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
        val root:View = binding
        val ll_pageradapter_course: LinearLayoutCompat = binding.findViewById(R.id.ll_pageradapter_course)
        val rvPageradapterCourse: RecyclerView = binding.findViewById(R.id.rv_pageradapter_course)
        var courseTrackAdapter:CourseTrackAdapter = CourseTrackAdapter((binding.context))
        init {
            ll_pageradapter_course.setViewSizeWidthByRatio(.915f)
        }
    }


}


