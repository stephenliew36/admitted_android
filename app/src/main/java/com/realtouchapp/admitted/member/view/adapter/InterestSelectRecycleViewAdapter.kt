package com.realtouchapp.admitted.member.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.realtouchapp.admitted.databinding.AdapterRecycleviewInterestBinding
import com.realtouchapp.tools.ext.setViewSizeByRatioWH
import com.realtouchapp.tools.ui.adapter.BaseRecyclerViewAdapter
/*
    作者: RobertChou
    時間: 2023/1/3 下午 09:23
    信箱: didi31139@gmail.com
    說明:
*/
class InterestSelectRecycleViewAdapter(context: Context) : BaseRecyclerViewAdapter<String, InterestSelectRecycleViewAdapter.ViewHolder>(context) {
    lateinit var onItemDeleted: ((String) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterRecycleviewInterestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.tvAdapterRecycleviewTitle.text = dataList[position]
        holder.ivAdapterRecycleview.setOnClickListener {
            onItemDeleted?.let { onItemDeleted(dataList[position])}
            dataList.remove(dataList.get(position))
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    inner class ViewHolder(binding: AdapterRecycleviewInterestBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvAdapterRecycleviewTitle: AppCompatTextView = binding.tvAdapterRecycleviewTitle
        val ivAdapterRecycleview: AppCompatImageView = binding.ivAdapterRecycleview

        init {
            binding.root.setViewSizeByRatioWH(.89f,.075f)
            binding.tvAdapterRecycleviewTitle.setViewSizeByRatioWH(.89f,.062f)

        }
    }

}
