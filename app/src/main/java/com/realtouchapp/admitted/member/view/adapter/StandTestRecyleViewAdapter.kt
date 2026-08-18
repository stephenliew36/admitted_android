package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterRecycleviewTestBinding
import com.realtouchapp.admitted.network.entity.Test
import com.realtouchapp.tools.ext.setMarginTopRatioH
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool

/*
    作者: RobertChou
    時間: 2023/1/3 下午 09:23
    信箱: didi31139@gmail.com
    說明:
*/
class StandTestRecyleViewAdapter(context: Context) : BaseRecyclerViewAdapter<Test, StandTestRecyleViewAdapter.ViewHolder>(context) {
    lateinit var onItenDeleted: ((Int) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = dataList[position].title()
        holder.tvAdapterTestScore.text = dataList[position].displayScore()

        holder.tvAdapterTestDate.text = "Taken in ${dataList[position].month} ,${ dataList[position].year}"

        holder.ivAdapterRecycleview.setOnClickListener {
            dataList.remove(dataList.get(position))
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dataList.size - position)
            onItenDeleted?.let { it ->position }
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewTestBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterTestTitle
        val tvAdapterTestDate: AppCompatTextView = binding.tvAdapterTestDate
        val tvAdapterTestScore: AppCompatTextView = binding.tvAdapterTestScore
        val ivAdapterRecycleview: AppCompatImageView = binding.ivAdapterRecycleview

        init {
            binding.root.setViewSizeByRatioWH(.89f,.143f)
            binding.clAdapterTest.setViewSizeByRatioWH(.89f,.128f)
            UserInterfaceTool().setViewSizeHeight(binding.tvAdapterTestTitle ,UserInterfaceTool().getScreenHeightWithoutBarByRatio(binding.root.context,.027f))

//            binding.tvAdapterTestTitle.setViewSizeByRatioWH(.632f,.027f)
            binding.tvAdapterTestTitle.setMarginTopRatioH(.017f)
            binding.tvAdapterTestScore.setViewSizeByRatioWH(.89f,.027f)
            binding.tvAdapterTestScore.setMarginTopRatioH(.012f)
            binding.tvAdapterTestDate.setViewSizeByRatioWH(.89f,.027f)
            binding.tvAdapterTestDate.setMarginTopRatioH(.0049f)

        }
    }

}
