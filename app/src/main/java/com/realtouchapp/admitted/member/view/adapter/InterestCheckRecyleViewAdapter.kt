package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterRecycleviewInterestCheckBinding
import com.realtouchapp.admitted.network.entity.AreaOfInterest
import com.realtouchapp.tools.ext.setMargin
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
import com.realtouchapp.tools.utils.UserInterfaceTool

/*
    作者: RobertChou
    時間: 2023/1/3 下午 09:23
    信箱: didi31139@gmail.com
    說明:
*/
class InterestCheckRecyleViewAdapter(context: Context) : BaseRecyclerViewAdapter<String, InterestCheckRecyleViewAdapter.ViewHolder>(context) {

    var areaOfInterest = AreaOfInterest()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewInterestCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = dataList[position]
        //顯示已點擊的部分
        holder.cbAdapterRecycleview.isChecked = areaOfInterest.isChecked(holder.root.context,dataList[position])

        holder.root.setOnClickListener {
            holder.cbAdapterRecycleview.isChecked = !holder.cbAdapterRecycleview.isChecked
            areaOfInterest.setChecked(holder.root.context,holder.cbAdapterRecycleview.isChecked,dataList[position])

        }

        holder.cbAdapterRecycleview.setOnClickListener {
            areaOfInterest.setChecked(holder.root.context,holder.cbAdapterRecycleview.isChecked,dataList[position])
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewInterestCheckBinding) : RecyclerView.ViewHolder(binding.root) {
        val root:View = binding.root
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val cbAdapterRecycleview: AppCompatCheckBox = binding.cbAdapterRecycleview

        init {
//            binding.root.setViewSizeByRatioWH(.89f,.052f)
            var margin = UserInterfaceTool().getScreenHeightByRatio(binding.root.context ,0.0155f)
            binding.tvAdapterRecycleviewTitle.setMargin(0,margin,0,margin)

        }
    }

}
