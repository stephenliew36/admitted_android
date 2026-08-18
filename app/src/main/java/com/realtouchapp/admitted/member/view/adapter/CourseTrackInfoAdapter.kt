package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterCoursetrackInfoBinding
import com.realtouchapp.admitted.network.entity.Course.CourseTrack
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.setMarginTopOrignal
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class CourseTrackInfoAdapter(context: Context) : BaseRecyclerViewAdapter<Int, CourseTrackInfoAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    lateinit var titlesId :List<Int>
    lateinit var imagesId :List<Int>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCoursetrackInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
            holder.tvAdapterRecycleviewTitle.setText(titlesId.get(position))
            holder.ivAdapterRecycleview.setImageResource(imagesId.get(position))
        holder.ivAdapterRecycleview.setOnClickListener {
            onItemClick(imagesId.get(position))
        }
    }
    inner class ViewHolder(binding: AdapterCoursetrackInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val ivAdapterRecycleview: ImageView = binding.ivAdapterRecycleview

        init {
            tvAdapterRecycleviewTitle.setTextSize(15)
            ivAdapterRecycleview.setMarginTopOrignal(5)
        }

    }

}
