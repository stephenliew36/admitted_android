package com.realtouchapp.admitted.main.explore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.R
import com.realtouchapp.admitted.databinding.AdapterSchoolBriefBinding
import com.realtouchapp.tools.ext.setMarginTopOrignal
import com.realtouchapp.tools.ext.setTextSize
import com.realtouchapp.tools.ext.setViewSizeOrignalWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import java.util.ArrayList

/*
    作者: RobertChou
    時間: 2023/2/18 下午 07:42
    信箱: didi31139@gmail.com
    說明:
*/
class SchoolBirefAdapter(context: Context): BaseRecyclerViewAdapter<String, SchoolBirefAdapter.ViewHolder>(context){
    lateinit var onItemClick: ((Int) -> Unit)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterSchoolBriefBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)


    }

    inner class ViewHolder(binding: AdapterSchoolBriefBinding) : RecyclerView.ViewHolder(binding.root) {
//        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
//        val ivAdapterRecycleviewArrow: AppCompatImageView = binding.ivAdapterRecycleviewArrow
//        val llAdapterRecycleviewIc: LinearLayoutCompat = binding.llAdapterRecycleviewIc
//        val tvAdapterRecycleviewType: AppCompatTextView = binding.tvAdapterRecycleviewType
//        val tvAdapterRecycleviewRank: AppCompatTextView = binding.tvAdapterRecycleviewRank
//        val tvAdapterRecycleviewGrade: AppCompatTextView = binding.tvAdapterRecycleviewGrade
//        val tvAdapterRecycleviewRecognition: AppCompatTextView = binding.tvAdapterRecycleviewRecognition

        init {
//            binding.root.setMarginTopOrignal(16)
//            tvAdapterRecycleviewTitle.setTextSize(13)
//
//            ivAdapterRecycleviewArrow.setViewSizeOrignalWH(24,24)
//            tvAdapterRecycleviewType.setTextSize(13)
//            tvAdapterRecycleviewRank.setTextSize(13)
//            tvAdapterRecycleviewGrade.setTextSize(13)
//            tvAdapterRecycleviewRecognition.setTextSize(13)
        }

    }

}
