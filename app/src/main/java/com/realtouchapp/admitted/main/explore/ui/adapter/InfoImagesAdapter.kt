package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterCollegePhotosBinding
import com.realtouchapp.admitted.network.entity.Course
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalHeight
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter

class InfoImagesAdapter(context: Context) : BaseRecyclerViewAdapter<Int, InfoImagesAdapter.ViewHolder>(context) {
    lateinit var onItemClick: ((Course) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterCollegePhotosBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.ivAdapterCollegePhotos.setImageResource(dataList[position])
    }

    inner class ViewHolder(binding: AdapterCollegePhotosBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val ivAdapterCollegePhotos: AppCompatImageView = binding.ivAdapterCollegePhotos
        init {

        }
    }

}
